package money.open.cards.visabase.service.impl;

import lombok.extern.slf4j.Slf4j;
import money.open.cards.visabase.Utility.Convertor;
import money.open.cards.visabase.dto.TransactionRequestDto;
import money.open.cards.visabase.kafka.KafkaConstants;
import money.open.cards.visabase.service.LoadTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Hashtable;
import java.util.UUID;

@Slf4j
@Component
public class LoadTransactionServiceImpl implements LoadTransactionService {

    @Autowired
    TransactionRequestDto transactionRequestDto;

    @Override
    public String getDeliveryChannel(String tpCode,String mcc) {
        String tranCode = "";
        log.info("Finding Delivery channel is started ");
        if (tpCode!=null) {
            tranCode = tpCode.substring(0, 2);
        }

        if ((tranCode.equals("01") || tranCode.equals("02") || tranCode.equals("22") || tranCode.equals("70")
                || tranCode.equals("72")) && mcc.equals("6011")) {
            return "ATM";
        }
        return "POS";
    }


    @Override
    public void loadTransactionDetails(Hashtable<String, String> isoBuffer) {


        transactionRequestDto.setReserverFld1(isoBuffer.get("MSG-HDR"));
        transactionRequestDto.setRequestId(UUID.randomUUID().toString());
        transactionRequestDto.setMti(isoBuffer.get("MTI"));
        transactionRequestDto.setProxyCardNumber(isoBuffer.get("DE002"));
        transactionRequestDto.setTpCode(isoBuffer.get("DE003"));
        //Transaction Amount
        if(isoBuffer.get("DE004")==null) {
            transactionRequestDto.setTransactionAmount("0");
        }else {
            transactionRequestDto.setTransactionAmount(isoBuffer.get("DE004"));
        }
        //Settlement Amount
        if(isoBuffer.get("DE005")==null) {
            transactionRequestDto.setSettlementAmount("0");
        }else {
            transactionRequestDto.setSettlementAmount(isoBuffer.get("DE005"));
        }
        //Billing Amount
        if(isoBuffer.get("DE006")==null) {
            transactionRequestDto.setBillingAmount("0");
        }else {
            transactionRequestDto.setBillingAmount(isoBuffer.get("DE006"));
        }
        //Billing Fee Amount
        if(isoBuffer.get("DE008")==null) {
            transactionRequestDto.setBillingFeeAmount("0");
        }else {
            transactionRequestDto.setBillingFeeAmount(isoBuffer.get("DE008"));
        }
        // Settlement Conversion Rate
        if(isoBuffer.get("DE009")==null) {
            transactionRequestDto.setSettlementConversionRate("0");
        }else {
            transactionRequestDto.setSettlementConversionRate(isoBuffer.get("DE009"));
        }
        //Billing Conversion Rate
        if(isoBuffer.get("DE010")==null) {
            transactionRequestDto.setBillingConversionRate("0");
        }else {
            transactionRequestDto.setBillingConversionRate(isoBuffer.get("DE010"));
        }


        transactionRequestDto.setStan(isoBuffer.get("DE011"));// stan
        transactionRequestDto.setLocalTime(Convertor.getLocalTimeForString(isoBuffer.get("DE012")).toString());
        transactionRequestDto.setTransactionDateTime(LocalDateTime.now());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(isoBuffer.get("DE013")!=null) {
            DateTimeFormatter MMdd = new DateTimeFormatterBuilder()
                    .appendPattern("MMdd")
                    .parseDefaulting(ChronoField.YEAR, 2000)
                    .toFormatter();
            LocalDate localDate = LocalDate.parse(isoBuffer.get("DE013"), MMdd);
            transactionRequestDto.setLocalDate(localDate);
        }
        if(isoBuffer.get("DE014")!=null) {
            DateTimeFormatter yyMM = new DateTimeFormatterBuilder()
                    .appendPattern("yyMM")
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter();

            LocalDate expiryDate = LocalDate.parse(isoBuffer.get("DE014"), yyMM);
            transactionRequestDto.setExpiryDate(expiryDate);
        }
        if(isoBuffer.get("DE015")!=null) {
            DateTimeFormatter yyMM = new DateTimeFormatterBuilder()
                    .appendPattern("yyMM")
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter();
            LocalDate settlementDate = LocalDate.parse(isoBuffer.get("DE015"), yyMM);
            transactionRequestDto.setSettlementDate(settlementDate);
        }else {
            transactionRequestDto.setSettlementDate(null);
        }
        if(isoBuffer.get("DE016")!=null) {
            if(!isoBuffer.get("DE016").equalsIgnoreCase("0000")) {
                DateTimeFormatter yyMM = new DateTimeFormatterBuilder()
                        .appendPattern("yyMM")
                        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                        .toFormatter();
                LocalDate convertionDate = LocalDate.parse(isoBuffer.get("DE016"), yyMM);
                transactionRequestDto.setConversionDate(convertionDate);
            }else {
                transactionRequestDto.setConversionDate(LocalDate.now());
            }
        }else
        {
            transactionRequestDto.setConversionDate(LocalDate.now());
        }
        if(isoBuffer.get("DE017")!=null) {
            LocalDate captureDate = LocalDate.parse(isoBuffer.get("DE017"), formatter);
            transactionRequestDto.setCaptureDate(captureDate);
        }else {
            transactionRequestDto.setCaptureDate(null);
        }


        transactionRequestDto.setMcc(isoBuffer.get("DE018"));// mcc
        transactionRequestDto.setAcqInstCountryCode(isoBuffer.get("DE019"));
        if(isoBuffer.get("DE021")==null){
            transactionRequestDto.setFwdInstCountryCode(null);
        }else
        {
            transactionRequestDto.setFwdInstCountryCode(isoBuffer.get("DE021"));
        }


        transactionRequestDto.setPosEntryMode(isoBuffer.get("DE022"));// pos entry mode
        transactionRequestDto.setCardSeqNum(isoBuffer.get("DE023"));
        transactionRequestDto.setPosConditionCode(isoBuffer.get("DE025"));// pos condition code
        if(isoBuffer.get("DE026")!=null){
            transactionRequestDto.setPosPinCaptureCode(isoBuffer.get("DE026"));
        }else {
            transactionRequestDto.setPosPinCaptureCode("*");
        }

        if(isoBuffer.get("DE027")!=null){
            transactionRequestDto.setAuthIdResp(isoBuffer.get("DE027"));
        }else {
            transactionRequestDto.setAuthIdResp("*");
        }



        //Transaction Fee Amount
        if(isoBuffer.get("DE028")!=null)
            transactionRequestDto.setTransactionFeeAmount(Convertor.convertEBCDICHexValue(isoBuffer.get("DE028")).replace("D",""));// transaction fee
        else
            transactionRequestDto.setTransactionFeeAmount(Convertor.convertEBCDICHexValue("00000000"));// transaction fee

        // Settlement Fee Amount
        if(isoBuffer.get("DE029")==null) {
            transactionRequestDto.setSettlementFeeAmount(null);
        }else {
            transactionRequestDto.setSettlementFeeAmount(isoBuffer.get("DE029"));
        }
        // Transaction Processing Fee Amount
        if(isoBuffer.get("DE030")==null) {
            transactionRequestDto.setTransactionProcessingFeeAmount(null);
        }else {
            transactionRequestDto.setTransactionProcessingFeeAmount(isoBuffer.get("DE030"));
        }
        // Settlement Processing Fee amount
        if(isoBuffer.get("DE031")==null) {
            transactionRequestDto.setSettlementProcessingFeeAmount(null);
        }else {
            transactionRequestDto.setSettlementProcessingFeeAmount(isoBuffer.get("DE031"));
        }

        //@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
        //LocalDateTime transactionDateTime;// transaction datetime
        if(isoBuffer.get("DE032")!=null) {
            transactionRequestDto.setAcquirerBin(isoBuffer.get("DE032"));// acquirerBin
        }else {
            transactionRequestDto.setAcquirerBin("*");// acquirerBin
        }
        if(isoBuffer.get("DE033")!=null){
            transactionRequestDto.setForwardingInstIdCode(isoBuffer.get("DE033"));
        }else{
            transactionRequestDto.setForwardingInstIdCode("*");
        }



        transactionRequestDto.setTrack2Data(isoBuffer.get("DE035"));
        if(isoBuffer.get("DE037")!=null)
        transactionRequestDto.setRrn(Convertor.convertEBCDICHexValue(isoBuffer.get("DE037")));// rrn
        if(isoBuffer.get("DE041")!=null)
        transactionRequestDto.setCardAcceptorTerminalid(Convertor.convertEBCDICHexValue(isoBuffer.get("DE041")));// terminalId
        if(isoBuffer.get("DE042")!=null)
        transactionRequestDto.setCardAcceptorId(Convertor.convertEBCDICHexValue(isoBuffer.get("DE042")));// merchantId
        if(isoBuffer.get("DE043")!=null)
        transactionRequestDto.setCardAcceptorLocation(Convertor.convertEBCDICHexValue(isoBuffer.get("DE043")));// terminalLocation
        transactionRequestDto.setTransactionCurrencyCode(isoBuffer.get("DE049"));
        transactionRequestDto.setSettlementCurrencyCode(isoBuffer.get("DE050"));
        transactionRequestDto.setBillingCurrencyCode(isoBuffer.get("DE051"));
        if(isoBuffer.get("DE052")!=null)
        transactionRequestDto.setPinData(isoBuffer.get("DE052"));
        transactionRequestDto.setSecurityRelatedControlInformation(isoBuffer.get("DE053"));
        transactionRequestDto.setIccRelatedData(isoBuffer.get("DE055"));

        transactionRequestDto.setVisaposInformation(isoBuffer.get("DE060"));
        transactionRequestDto.setInfData(isoBuffer.get("DE062"));
        transactionRequestDto.setFinancialNetworkCode(isoBuffer.get("DE063"));

        if(isoBuffer.get("DE066")!=null){
            transactionRequestDto.setSettlementInstIdCode(isoBuffer.get("DE066"));
        }else {
            transactionRequestDto.setSettlementInstIdCode("*");
        }

        if(isoBuffer.get("DE068")!=null){
            transactionRequestDto.setReceivingInstIdCode(isoBuffer.get("DE068"));
        }else {
            transactionRequestDto.setReceivingInstIdCode("*");
        }



        if(isoBuffer.get("DE066")!=null){
            transactionRequestDto.setSettlementCode(isoBuffer.get("DE066"));
        }else{
            transactionRequestDto.setSettlementCode("*");
        }

        if(isoBuffer.get("DE068")!=null){
            transactionRequestDto.setReceivingInstCountryCode(isoBuffer.get("DE068"));
        }else{
            transactionRequestDto.setReceivingInstCountryCode("*");
        }




        if(isoBuffer.get("DE069")!=null){
            transactionRequestDto.setSettlementInstCountryCode(isoBuffer.get("DE069"));
        }else {
            transactionRequestDto.setSettlementInstCountryCode("*");
        }
        if(isoBuffer.get("DE090")!=null){
            transactionRequestDto.setOriginalDataElements(isoBuffer.get("DE090"));
        }else {
            transactionRequestDto.setOriginalDataElements("*");
        }
        if(isoBuffer.get("DE102")!=null){
            transactionRequestDto.setAccountIdentification1(isoBuffer.get("DE102"));
        }else {
            transactionRequestDto.setAccountIdentification1("*");
        }
        if(isoBuffer.get("DE103")!=null){
            transactionRequestDto.setAccountIdentification2(isoBuffer.get("DE103"));
        }else {
            transactionRequestDto.setAccountIdentification2("*");
        }
        transactionRequestDto.setAcqNetworkId("VISA"); // Network Type
        transactionRequestDto.setAcqInstitutionId(transactionRequestDto.getAcquirerBin());// Acquirer Institution Id
        transactionRequestDto.setChannelType(getDeliveryChannel(transactionRequestDto.getTpCode(),transactionRequestDto.getMcc()));// Channel Type // acquirerChannel --- delivery channel
        transactionRequestDto.setTransactionType("01");// Transaction Type  - mti based todo:
        transactionRequestDto.setIssuerBin(transactionRequestDto.getProxyCardNumber().substring(0,6));// IssuerId/IssuerBin  - 12 digit of p-2 -search logic todo:
        transactionRequestDto.setIssuerInstId(null);// Issuer Institution Id null
        transactionRequestDto.setTransactionKey(null);   //null
        transactionRequestDto.setSecurityInfo("YYYYYY");//<PIN/CVV1/CCV2/EMV/iCVV/CAVV>  -- logic todo:
        transactionRequestDto.setSecurityResult(null);//<S/F>   - -- null
        transactionRequestDto.setAccountNumber("*");   /// null
        transactionRequestDto.setAccumFlag("*");  ///// null
        transactionRequestDto.setAcquirerId("VISA");/// null
        transactionRequestDto.setIssuerId("*");/// null
        //transactionRequestDto.setNetworkId(null);/// null
        //String requestId = transactionRequestDto.getRrn() + transactionRequestDto.getCardAcceptorTerminalid()
        //        + transactionRequestDto.getStan() + transactionRequestDto.getTransactionDateTime();
        transactionRequestDto.setApiRefNo(UUID.randomUUID().toString());  // uuid
        transactionRequestDto.setAuthNumber("*");  // null
        transactionRequestDto.setAvailableBalance("0");   // null
        transactionRequestDto.setBusinessDate(null);  /// null
        transactionRequestDto.setChargeAccumFlag("*");/// null
        transactionRequestDto.setCin("*");/// null
        transactionRequestDto.setDrCrAmount("0");/// null
        transactionRequestDto.setDrCrFlag("*");/// null
        transactionRequestDto.setIssConvRate(null);  /// null
        transactionRequestDto.setIssCurrencyCode(null);/// null
        transactionRequestDto.setKycFlag(null);/// null
        transactionRequestDto.setProductId(null);/// null
        transactionRequestDto.setReasonCode(null);/// null
        transactionRequestDto.setResponseCode(null);/// null
        transactionRequestDto.setRevAmount(null);/// null  based on 90 and 95 fi=eld
        transactionRequestDto.setRevFlag(null);/// null  R
        transactionRequestDto.setTransactionStatus(null); /// null
        transactionRequestDto.setIncTxnCount(null); /// null
        transactionRequestDto.setReserverFld2(null); /// null
        transactionRequestDto.setReserverFld3(null); /// null
        transactionRequestDto.setReserverFld4(null); /// null
        transactionRequestDto.setReserverFld5(null); /// null
        transactionRequestDto.setReserverFld6(null); /// null
        transactionRequestDto.setReserverFld7(null); /// null
        transactionRequestDto.setReserverFld8(null); /// null
        transactionRequestDto.setReserverFld9(null); /// null
        transactionRequestDto.setReserverFld10(null); /// null
        transactionRequestDto.setCardProduct(null); /// null
        transactionRequestDto.setAcquirerInstitutionId("*"); /// null
        transactionRequestDto.setNetworkType("VISA"); /// null
        transactionRequestDto.setReplyTopic(KafkaConstants.TOPIC_NAME_RECIEVE);  // reply topic
        transactionRequestDto.setIssuerInstitutionId("*"); /// null
        transactionRequestDto.setMaskedPan(Convertor.maskCardNumber(transactionRequestDto.getProxyCardNumber()));
        transactionRequestDto.setHashedPan(Convertor.convertToEBSDICHEXValue(transactionRequestDto.getProxyCardNumber())); //todo:
        transactionRequestDto.setIncrementalTransactionCount(0); /// null
        transactionRequestDto.setContactlessTransaction(0); // 0 -- false , 1-- true  /// null logic todo:


    }
}
