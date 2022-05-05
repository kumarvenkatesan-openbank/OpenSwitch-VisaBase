package money.open.cards.visabase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import money.open.cards.visabase.Utility.Convertor;
import money.open.cards.visabase.Utility.LogUtils;
import money.open.cards.visabase.config.VisaBaseConfig;
import money.open.cards.visabase.constants.Constants;
import money.open.cards.visabase.dto.Message;
import money.open.cards.visabase.listener.StartupListenerTransactionInterchange;
import money.open.cards.visabase.service.impl.TransactionProcessAPIImpl;
import money.open.cards.visabase.service.impl.VisaFormatterImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.DataOutputStream;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;
@Slf4j
@RequiredArgsConstructor
public class TransactionProcess implements Runnable {

	public TransactionProcess(String data, DataOutputStream outputStream, String requestType, String stationCode) {
		super();
		this.data = data;
		this.outputStream = outputStream;
		this.requestType = requestType;
		this.stationCode = stationCode;
	}


	private String data;
	private DataOutputStream outputStream;
	private String requestType;
	private String stationCode;


	TransactionProcessAPIImpl transactionProcessAPIImpl;

	@Override
	public void run() {
		Message msgBuffer = null;
		try {
			msgBuffer = new Message();
			msgBuffer.setMessage(data);
			msgBuffer.setMessageStatus("REQ");
			msgBuffer.setStationName("HOST");
			msgBuffer.setStationCode(stationCode);

				ApplicationContext context = new AnnotationConfigApplicationContext(VisaBaseConfig.class);
				transactionProcessAPIImpl = context.getBean(TransactionProcessAPIImpl.class);



			if (requestType.equals(Constants.ECHO)) {
				while (StartupListenerTransactionInterchange.masterLoopFlag) {
					TimeUnit.SECONDS.sleep(90);
					msgBuffer = transactionProcessAPIImpl.echo(msgBuffer);
					if (!(msgBuffer == null || msgBuffer.getMessage() == null)) {
						log.info("Echo Response Message : " + msgBuffer.getMessage());
						outputStream.writeBytes(msgBuffer.getMessage());
						outputStream.flush();
					}

				}

			} else if (requestType.equals(Constants.KEY_EXCHANGE)) {

				while (StartupListenerTransactionInterchange.masterLoopFlag) {
					msgBuffer = transactionProcessAPIImpl.keyEchange(msgBuffer);
					if (!(msgBuffer == null || msgBuffer.getMessage() == null)) {
						log.info("Key Exchange Response Message : " + msgBuffer.getMessage());
						outputStream.writeBytes(msgBuffer.getMessage());
						outputStream.flush();
						TimeUnit.HOURS.sleep(1);
					} else {
						TimeUnit.MINUTES.sleep(5);
					}
				}

			} else if (data != null && !data.trim().isEmpty()) {

				switch (requestType) {
				case Constants.TRANSACTION:
					assert transactionProcessAPIImpl != null;
					msgBuffer = transactionProcessAPIImpl.authorize(msgBuffer);
					break;
				case Constants.SESSIONACTIVATION:
					msgBuffer = transactionProcessAPIImpl.sessionActivation(msgBuffer);
					break;
				case Constants.SIGNON:
					assert transactionProcessAPIImpl != null;
					msgBuffer = transactionProcessAPIImpl.signOn(msgBuffer);
					break;
				case Constants.SIGNOFF:
					msgBuffer = transactionProcessAPIImpl.signOff(msgBuffer);
					break;
				case Constants.KEY_EXCHANGE:
					msgBuffer = transactionProcessAPIImpl.keyEchange(msgBuffer);
					break;
				case Constants.ECHO:
					msgBuffer = transactionProcessAPIImpl.echo(msgBuffer);
					break;
				default:
					log.info("requestType Not Able to identify....");
				}
				if (msgBuffer == null || msgBuffer.getMessage() == null)
					return;
				if (msgBuffer.getResponseRequired() == 1) {
					
					  outputStream.writeBytes(msgBuffer.getMessage()); 
					  outputStream.flush();


					msgBuffer.setMessage(Convertor.alpha2Hex(msgBuffer.getMessage()));

					LogUtils.writeHexDump(msgBuffer.getMessageStatus(),msgBuffer.getMessage().getBytes());

					VisaFormatterImpl visaisoFormatter = new VisaFormatterImpl();
					Hashtable<String, String> isoBuffer = new Hashtable<String, String>();
					Message m = new Message();
					m.setMessage(msgBuffer.getMessage().substring(4));
					isoBuffer = visaisoFormatter.formatMessage(m, isoBuffer);

					LogUtils.writeRequestLog(msgBuffer.getMessageStatus(),isoBuffer);

					/*
					 * outputStream.writeUTF(msgBuffer.getMessage());
					 * log.info("Response Successfully Sent....");
					 * System.out.println("Response Successfully Sent...."+msgBuffer);
					 */
		   			//outputStream.flush();
				}

			}

		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			msgBuffer = null;
			log.info("End of TransactionProcess:" + requestType);
		}
	}

}
