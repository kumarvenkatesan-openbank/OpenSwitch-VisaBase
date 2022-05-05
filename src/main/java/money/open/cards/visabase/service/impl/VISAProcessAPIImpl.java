package money.open.cards.visabase.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import money.open.cards.visabase.Utility.Convertor;
import money.open.cards.visabase.Utility.ISOUtil;
import money.open.cards.visabase.Utility.LogUtils;
import money.open.cards.visabase.Utility.TPEGeneralUtil;
import money.open.cards.visabase.config.VisaBaseConfig;
import money.open.cards.visabase.constants.TPEConstants;
import money.open.cards.visabase.constants.TPEResponseCode;
import money.open.cards.visabase.dto.Message;
import money.open.cards.visabase.dto.TransactionRequestDto;
import money.open.cards.visabase.exception.ReqInteruptedException;
import money.open.cards.visabase.exception.RspInteruptedException;
import money.open.cards.visabase.exception.TransactionException;
import money.open.cards.visabase.listener.SocketMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Hashtable;

@Slf4j
@Component
public class VISAProcessAPIImpl implements TransactionProcessAPIImpl {

	@Autowired
	Hashtable<String, String> isoBuffer;
	@Autowired
	VisaFormatterImpl visaFormatter;
	@Autowired
	LoadTransactionServiceImpl loadTransactionService;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	TransactionRequestDto transactionRequestDto;

	DataOutputStream outputStream;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	VisaBaseConfig visaBaseConfig;

	@Override
	public Message authorize(Message msgBuffer) {
		log.info("authorize method called....."+Convertor.alpha2Hex(msgBuffer.getMessage()));
		LogUtils.writeHexDump(msgBuffer.getMessageStatus(),msgBuffer.getMessage().getBytes());
		final String messageStatus = msgBuffer.getMessageStatus();
		try {
			msgBuffer.setMessage(Convertor.alpha2Hex(msgBuffer.getMessage()));
			isoBuffer = visaFormatter.formatMessage(msgBuffer, isoBuffer);
			LogUtils.writeRequestLog(messageStatus,isoBuffer);
			if ("REQ".equalsIgnoreCase(messageStatus)) {
				switch(isoBuffer.get("MTI")){
					case "0800":
						handleNonFinancialRequest(msgBuffer);
						break;
					case "0200":
					case "0100":

						for (int i = 0; i < 128; i++) {
							if(isoBuffer.get("DE" + String.format("%03d", (i + 1))).equalsIgnoreCase("*"))
							{
								isoBuffer.remove("DE" + String.format("%03d", (i + 1)));
							}
						}

						loadTransactionService.loadTransactionDetails(isoBuffer);
						System.out.println("Check"+objectMapper.findAndRegisterModules().writeValueAsString(transactionRequestDto));
						try {
							kafkaTemplate.send("atm","1234",objectMapper.writeValueAsString(transactionRequestDto));
						}catch(Exception e){
							log.error("Exception posting data to queue",e);
						}
						//handleFinancialRequest(msgBuffer);
						break;
					case "0810":
						System.out.println("0810-->"+msgBuffer.getMessageStatus()+msgBuffer);
						break;

					default:
				//		throw new Exception("MTI not Identified......");
				}

			}
			else
			{
				System.out.println("CHECK-->"+msgBuffer.getMessageStatus()+msgBuffer);
			}

		} catch (ReqInteruptedException e) {
			e.printStackTrace();
			// Build Bad response to Vocalink
			log.info("System Unable to process, Transaction is declied and message is logged");
			log.info("Check : " + e.getMessage());
			isoBuffer.put("P-39", Convertor.convertToEBSDICHEXValue(e.getMessage()));
			msgBuffer.setMessage(visaFormatter.buildISOErrorMessage(isoBuffer));
			msgBuffer.setMessageStatus("RSP");
		} catch (RspInteruptedException e) {
			log.info(" Transaction Interrupted during the response processing");
			e.printStackTrace();
			msgBuffer.setMessageStatus("RSP_LOG");
		} catch (TransactionException e) {
			log.error("Unknown Exception Occured: Exception is " + e);
			e.printStackTrace();
			isoBuffer.put("P-39", Convertor.convertToEBSDICHEXValue(e.getMessage()));
			msgBuffer.setMessage(visaFormatter.buildISOErrorMessage(isoBuffer));
			msgBuffer.setMessageStatus("RSP");
		} catch (Exception e) {
			log.error("Unknown Exception Occured: Exception is " + e);
			e.printStackTrace();
			isoBuffer.put("P-39", Convertor.convertToEBSDICHEXValue(TPEResponseCode.SYSTEM_ERROR.getResponseCode()));
			msgBuffer.setMessage(visaFormatter.buildISOErrorMessage(isoBuffer));
			msgBuffer.setMessageStatus("RSP");
		}
		msgBuffer.setResponseRequired(0);
		return msgBuffer;
	}

	private void handleNonFinancialRequest(Message msgBuffer) {

		for (int i = 0; i < 128; i++) {
			isoBuffer.putIfAbsent("DE" + String.format("%03d", (i + 1)), "*");
		}
		isoBuffer.put("DE039", Convertor.convertToEBSDICHEXValue("00"));
		msgBuffer.setMessage(visaFormatter.buildSignOnMessage(isoBuffer));
		msgBuffer.setMessageStatus("RSP");
	}

	private void handleFinancialRequest(Message msgBuffer) {

		for (int i = 0; i < 128; i++) {
			isoBuffer.putIfAbsent("DE" + String.format("%03d", (i + 1)), "*");
		}
		isoBuffer.put("DE039", Convertor.convertToEBSDICHEXValue("00"));
		msgBuffer.setMessage(visaFormatter.buildTransactionMessage(isoBuffer));
		msgBuffer.setMessageStatus("RSP");
	}


	public String convertEBCDICtoString(String valEBCDICHEX) {
		if (!valEBCDICHEX.equals("*")) {
			valEBCDICHEX = Convertor.convertEBCDICHexValue(valEBCDICHEX);
		}

		return valEBCDICHEX;
	}




	@Override
	public Message signOn(Message msgBuffer) {
		log.info("signOn Started");
		try {
			setVisaHeader();
			isoBuffer.put("MTI", "0800");
			isoBuffer.put("DE007", TPEGeneralUtil.getDE007());
			isoBuffer.put("DE011", TPEGeneralUtil.getDE011());
			isoBuffer.put("DE070", TPEConstants.VISA_SIGNON_ADVICE);
			isoBuffer.put("DE063", "8000000002");
			isoBuffer.put("DE037", Convertor.convertToEBSDICHEXValue(TPEGeneralUtil.getDE037(isoBuffer.get("DE007"))));

			for (int i = 0; i < 128; i++) {
				isoBuffer.putIfAbsent("DE" + String.format("%03d", (i + 1)), "*");
			}

			msgBuffer.setMessage(visaFormatter.buildSignOnMessage(isoBuffer));

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("signOn Message generated");
		return msgBuffer;
	}

	@Override
	public Message keyEchange(Message msgBuffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message signOff(Message msgBuffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message echo(Message msgBuffer) {

		return msgBuffer;
	}

	public void setVisaHeader() {
		isoBuffer.put("H01", "16"); // Field-1
		isoBuffer.put("H02", "01"); // Field-2
		isoBuffer.put("H03", "02"); // Field-3
		isoBuffer.put("H04", "0000"); // Field-4
		isoBuffer.put("H05", "100443"); // Field-5 - Source Id
		isoBuffer.put("H06", "000000"); // Field-6// Destination Id - Will be swapped while iso foramtion
		isoBuffer.put("H07", "00"); // Field-7
		isoBuffer.put("H08", "0000"); // Field-8
		isoBuffer.put("H09", "000000"); // Field-9
		isoBuffer.put("H10", "00"); // Field-10
		isoBuffer.put("H11", "000000"); // Field-11
		isoBuffer.put("H12", "00"); // Field-12
	}

	public static void main(String[] args) {
		try {
			VisaFormatterImpl messageHandler = new VisaFormatterImpl();
			Hashtable<String, String> isoBuffer = new Hashtable<String, String>();




			isoBuffer.put("MSG-HDR-LEN", "16"); // Field-1
			isoBuffer.put("MSG-HDR-FMT", "01"); // Field-2
			isoBuffer.put("MSG-TXT-FMT", "02"); // Field-3
			isoBuffer.put("MSG-TOT-MSG-LEN", "0000"); // Field-4
			isoBuffer.put("MSG-DEST-ID", "953625"); // Field-5
			isoBuffer.put("MSG-SRC-ID", "953626"); // Field-6
			isoBuffer.put("MSG-RND-TRIP", "00"); // Field-7
			isoBuffer.put("MSG-BAS1-FLG", "0000"); // Field-8
			isoBuffer.put("MSG-MSG-STS-FLG", "000000"); // Field-9
			isoBuffer.put("MSG-BATCH-NO", "00"); // Field-10
			isoBuffer.put("MSG-RSRVD", "000000"); // Field-11
			isoBuffer.put("MSG-USR-INFO", "00"); // Field-12

			isoBuffer.put("MSG-TYP", "0800");
			isoBuffer.put("P-7", new SimpleDateFormat("MMddhhmmss").format(new Date()));
			isoBuffer.put("P-11", new SimpleDateFormat("hhmmss").format(new Date()));
			isoBuffer.put("S-70", TPEConstants.VISA_SIGNON_ADVICE);
			isoBuffer.put("P-63", "8000000000");

			for (int i = 0; i < 64; i++) {
				if (isoBuffer.get("P-" + (i + 1)) == null) {
					isoBuffer.put("P-" + (i + 1), "*");
				}
			}
			for (int i = 64; i < 128; i++) {
				if (isoBuffer.get("S-" + (i + 1)) == null) {
					isoBuffer.put("S-" + (i + 1), "*");
				}
			}
			log.info(messageHandler.buildSignOnMessage(isoBuffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Message sessionActivation(Message msgBuffer) {
		log.info("signOn Started");
		try {
			setVisaHeader();
			isoBuffer.put("MSG-TYP", "0800");
			isoBuffer.put("DE007", TPEGeneralUtil.getDE007());
			isoBuffer.put("DE011", TPEGeneralUtil.getDE011());
			isoBuffer.put("DE070", TPEConstants.VISA_SIGNON_ADVICE);
			isoBuffer.put("DE063", "8000000002");
			isoBuffer.put("DE037", Convertor.convertToEBSDICHEXValue(TPEGeneralUtil.getDE037(isoBuffer.get("DE007"))));

			for (int i = 0; i < 128; i++) {
				isoBuffer.putIfAbsent("DE" + String.format("%03d",(i + 1)), "*");
			}
			msgBuffer.setMessage(visaFormatter.buildSignOnMessage(isoBuffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("signOn Message generated");
		return msgBuffer;
	}

	@Override
	public Message respond(Message msgBuffer) throws Exception {

		System.out.println("------->"+msgBuffer.getMessage());

		try {
			 transactionRequestDto = objectMapper.findAndRegisterModules().readValue(msgBuffer.getMessage(),TransactionRequestDto.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 128; i++) {
			isoBuffer.put("DE" + String.format("%03d", (i + 1)), "*");
		}
		//setVisaHeader();
		isoBuffer.put("MSG-HDR",transactionRequestDto.getReserverFld1());
		//transactionRequestDto.setRequestId(UUID.randomUUID().toString());

		String mti = transactionRequestDto.getMti();
		//if(mti.equals("0200")){mti="0210";}
		//if(mti.equals("0100")){mti="0110";}
		isoBuffer.put("MTI","0210");
		isoBuffer.put("DE002",transactionRequestDto.getProxyCardNumber());
		isoBuffer.put("DE003",transactionRequestDto.getTpCode());

		isoBuffer.put("DE004",StringUtils.leftPad(transactionRequestDto.getTransactionAmount(),12,"0"));
		isoBuffer.put("DE005",StringUtils.leftPad(transactionRequestDto.getSettlementAmount(),12,"0"));
		isoBuffer.put("DE006",StringUtils.leftPad(transactionRequestDto.getBillingAmount(),12,"0"));

		isoBuffer.put("DE007", TPEGeneralUtil.getDE007());

		if(transactionRequestDto.getBillingFeeAmount().equalsIgnoreCase("0")){
			isoBuffer.put("DE008","*");
		}else{
			isoBuffer.put("DE008",transactionRequestDto.getBillingFeeAmount());
		}


		isoBuffer.put("DE009",transactionRequestDto.getSettlementConversionRate());
		isoBuffer.put("DE010",transactionRequestDto.getBillingConversionRate());
		isoBuffer.put("DE011",transactionRequestDto.getStan());
		if(transactionRequestDto.getLocalTime().contains(":")){
			isoBuffer.put("DE012",transactionRequestDto.getLocalTime().replaceAll(":",""));
		}else{
			isoBuffer.put("DE012",transactionRequestDto.getLocalTime());
		}


		if(isoBuffer.get("DE013")!=null) {
			isoBuffer.put("DE013", transactionRequestDto.getLocalDate().
					format(DateTimeFormatter.ofPattern("MMdd")));
		}else{
			isoBuffer.put("DE013", "*");
		}

		if(isoBuffer.get("DE014")!=null) {
			isoBuffer.put("DE014", transactionRequestDto.getExpiryDate().
					format(DateTimeFormatter.ofPattern("yyMM")));
		}else{
			isoBuffer.put("DE014", "*");
		}
		/*if(isoBuffer.get("DE015")!=null) {
			isoBuffer.put("DE015", "0000");
			//isoBuffer.put("DE015",transactionRequestDto.getSettlementDate().toString());
		}else{
			isoBuffer.put("DE015", "*");
		}*/

		if(isoBuffer.get("DE016")!=null) {
			isoBuffer.put("DE016", "0000");
			//isoBuffer.put("DE016",transactionRequestDto.getConversionDate().toString());
		}else{
			isoBuffer.put("DE016", "*");
		}

		/*if(isoBuffer.get("DE017")!=null) {
			isoBuffer.put("DE017", "0000");
			//isoBuffer.put("DE017",transactionRequestDto.getCaptureDate().toString());
		}else{
			isoBuffer.put("DE017", "*");
		}*/


		isoBuffer.put("DE018",transactionRequestDto.getMcc());
		isoBuffer.put("DE019",transactionRequestDto.getAcqInstCountryCode());
		if(transactionRequestDto.getFwdInstCountryCode()==null) {
			isoBuffer.put("DE021", "*");
		}else{
			isoBuffer.put("DE021", transactionRequestDto.getFwdInstCountryCode());
		}
		isoBuffer.put("DE022",transactionRequestDto.getPosEntryMode());
		isoBuffer.put("DE023",transactionRequestDto.getCardSeqNum());
		isoBuffer.put("DE025",transactionRequestDto.getPosConditionCode());
		isoBuffer.put("DE026",transactionRequestDto.getPosPinCaptureCode());
		isoBuffer.put("DE027",transactionRequestDto.getAuthIdResp());

		if(isoBuffer.get("DE028")!=null) {
			String de28 = "D"+StringUtils.leftPad(transactionRequestDto.getTransactionFeeAmount(),8,"0");
			isoBuffer.put("DE028", Convertor.convertToEBSDICHEXValue(de28));
		}else{
			isoBuffer.put("DE028", "*");
		}

		if(transactionRequestDto.getSettlementFeeAmount()!=null){
			isoBuffer.put("DE029",transactionRequestDto.getSettlementFeeAmount());
		}else {
			isoBuffer.put("DE029","*");
		}

		if(transactionRequestDto.getTransactionProcessingFeeAmount()!=null){
			isoBuffer.put("DE030",transactionRequestDto.getTransactionProcessingFeeAmount());
		}else{
			isoBuffer.put("DE030","*");
		}
		if(transactionRequestDto.getSettlementProcessingFeeAmount()!=null){
			isoBuffer.put("DE031",transactionRequestDto.getSettlementProcessingFeeAmount());
		}else{
			isoBuffer.put("DE031","*");
		}


		if(isoBuffer.get("DE032")!=null) {
			isoBuffer.put("DE032", transactionRequestDto.getAcquirerBin());
		}else{
			isoBuffer.put("DE032", "*");
		}
		if(transactionRequestDto.getForwardingInstIdCode()!=null) {
			isoBuffer.put("DE033", transactionRequestDto.getForwardingInstIdCode());
		}else{
			isoBuffer.put("DE033", "*");
		}
		if(transactionRequestDto.getTrack2Data()!=null){
			isoBuffer.put("DE035",transactionRequestDto.getTrack2Data());
		}else{
			isoBuffer.put("DE035","*");
		}

		isoBuffer.put("DE037",Convertor.convertToEBSDICHEXValue(transactionRequestDto.getRrn()));
        if(transactionRequestDto.getCardAcceptorTerminalid()!=null){
			isoBuffer.put("DE041",Convertor.convertToEBSDICHEXValue(transactionRequestDto.getCardAcceptorTerminalid()));
		}else {
			isoBuffer.put("DE041","*");
		}

		if(transactionRequestDto.getCardAcceptorId()!=null){
			isoBuffer.put("DE042",Convertor.convertToEBSDICHEXValue(transactionRequestDto.getCardAcceptorId()));
		}else {
			isoBuffer.put("DE042","*");
		}
		if(transactionRequestDto.getCardAcceptorLocation()!=null){
			isoBuffer.put("DE043",Convertor.convertToEBSDICHEXValue(transactionRequestDto.getCardAcceptorLocation()));
		}else {
			isoBuffer.put("DE043","*");
		}

		isoBuffer.put("DE049",transactionRequestDto.getTransactionCurrencyCode());
		isoBuffer.put("DE050",transactionRequestDto.getSettlementCurrencyCode());
		isoBuffer.put("DE051",transactionRequestDto.getBillingCurrencyCode());

		if(transactionRequestDto.getPinData()!=null){
			isoBuffer.put("DE052",transactionRequestDto.getPinData());
		}else {
			isoBuffer.put("DE052","*");
		}
		if(transactionRequestDto.getSecurityRelatedControlInformation()!=null){
			isoBuffer.put("DE053",transactionRequestDto.getSecurityRelatedControlInformation());
		}else{
			isoBuffer.put("DE053","*");
		}

		if(transactionRequestDto.getIccRelatedData()!=null){
			isoBuffer.put("DE055",transactionRequestDto.getIccRelatedData());
		}else{
			isoBuffer.put("DE055","*");
		}
		isoBuffer.put("DE060",transactionRequestDto.getVisaposInformation());
		isoBuffer.put("DE062",transactionRequestDto.getInfData());
		isoBuffer.put("DE063",transactionRequestDto.getFinancialNetworkCode());


		isoBuffer.put("DE066",transactionRequestDto.getSettlementCode());
		isoBuffer.put("DE068",transactionRequestDto.getReceivingInstCountryCode());
		isoBuffer.put("DE069",transactionRequestDto.getSettlementInstCountryCode());

		isoBuffer.put("DE090",transactionRequestDto.getOriginalDataElements());
		isoBuffer.put("DE0102",transactionRequestDto.getAccountIdentification1());
		isoBuffer.put("DE0103",transactionRequestDto.getAccountIdentification2());

		for (int i = 0; i < 128; i++) {
			isoBuffer.putIfAbsent("DE" + String.format("%03d", (i + 1)), "*");
		}

		msgBuffer.setMessage(null);
		msgBuffer.setMessage(visaFormatter.buildSignOnMessage(isoBuffer));

		System.out.println(ISOUtil.byte2hex(msgBuffer.getMessage().getBytes(StandardCharsets.ISO_8859_1)));


		if(msgBuffer.getMessage()!=null) {

			LogUtils.writeHexDump(msgBuffer.getMessageStatus(), msgBuffer.getMessage().getBytes(StandardCharsets.ISO_8859_1));

			/*for (int i = 0; i < 128; i++) {
				if (!isoBuffer.get("DE" + String.format("%03d", (i + 1))).equalsIgnoreCase("*"))
					System.out.println("isoBuffer.put(\"DE" + String.format("%03d", (i + 1)) + "\" ," + "" + "" + isoBuffer.get("DE" + String.format("%03d", (i + 1))) + "" + ");");
			}*/

			LogUtils.writeRequestLog(msgBuffer.getMessageStatus(), isoBuffer);

			String stationName = visaBaseConfig.getHostIp() + "_" + visaBaseConfig.getHostPort() + "_" + 0;


			//System.out.println(SocketMap.sckMap);

			Socket socket = SocketMap.sckMap.get(stationName);


			socket.getOutputStream().write(msgBuffer.getMessage().getBytes(StandardCharsets.ISO_8859_1));
			socket.getOutputStream().flush();

		}


		return msgBuffer;
	}

	public LocalDate convertToLocalDate(LocalDate dateFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(dateFormat.toString(), formatter);
	}
}
