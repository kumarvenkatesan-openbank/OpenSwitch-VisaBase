package money.open.cards.visabase.listener;

import lombok.extern.slf4j.Slf4j;
import money.open.cards.visabase.config.VisaBaseConfig;
import money.open.cards.visabase.constants.Constants;
import money.open.cards.visabase.constants.TPEConstants;
import money.open.cards.visabase.service.TransactionProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.*;
@Slf4j
@Configuration
public class TransactionInterchangeListener extends Thread {
	@Autowired
	VisaBaseConfig visaBaseConfig;

	BlockingQueue<Runnable> threadPool = new LinkedBlockingQueue<Runnable>();
	ExecutorService tpExecutor = Executors.newFixedThreadPool(100);
	private boolean keyxch = false;
	private static int BUFFER_SIZE = 1200;
	private int socketIndex;

	public int getSocketIndex() {
		return socketIndex;
	}

	public void setSocketIndex(int socketIndex) {
		this.socketIndex = socketIndex;
	}

	public boolean isKeyxch() {
		return keyxch;
	}

	public void setKeyxch(boolean keyxch) {
		this.keyxch = keyxch;
	}

	public void run() {
		log.info("TransactionInterchangeListener Starts");
		char[] data = null;
		BufferedReader bufferedReader = null;
		DataOutputStream outputStream = null;
		String socketType = null;
		String socketIp=null;
		int socketPort;
		ServerSocket serverSocket = null;
		Socket socket = null;
		int signOnRequired = 0, keyExchangeRequired = 0, echoRequired = 0, reConnectionRequired = 0,
				reConnectIntervalTime = 0,sessionActivationRequired=0;
		String stationName = null;
		String stationCode = null;
		long totallength = 0;
		int tcpReaderDelay = 0;

		try {
			socketType = visaBaseConfig.getHostType();
			socketIp = visaBaseConfig.getHostIp();
			socketPort = visaBaseConfig.getHostPort();
			System.out.println("Socket IP and Port ["+socketIp+"]-["+socketPort+"]");
			sessionActivationRequired = visaBaseConfig.getSessionActivationRequired();
			signOnRequired = visaBaseConfig.getSignRequest();
			keyExchangeRequired = visaBaseConfig.getKeyExchangeRequired();
			echoRequired = visaBaseConfig.getEchoRequired();
			stationName = socketIp + "_" + socketPort + "_" + socketIndex;
			stationCode = visaBaseConfig.getStationCode();
			reConnectionRequired = visaBaseConfig.getReConnectionRequired();
			reConnectIntervalTime = visaBaseConfig.getReConnectIntervalTime();
			tcpReaderDelay = visaBaseConfig.getTcpReaderDelay();
			if (socketType.equals("S")) {
				log.info("TransactionInterchangeListener Socket Connection Type :: Server");

				// Creating Server Socket
				log.info("Creating Server Socket");
				serverSocket = new ServerSocket(socketPort);
				SocketMap.serverScoketMap.put(stationName, serverSocket);
				// accepting new Connection
				log.info("Wating for new Connection->" + stationName);

				socket = serverSocket.accept();

				log.info("New Connection Detected->" + stationName);
				
				System.out.println("Connection established...."+socket.getLocalPort());

			} else {
				log.info("TransactionInterchangeListener Socket Connection Type :: Client");

				log.info("Wating for Server Connection->" + stationName);

				socket = new Socket(socketIp, socketPort);
				System.out.println("connection  estabished "+stationName+"---"+socket.getLocalPort());
				log.info("New Connection Established With Server->" + stationName);
			}

			socket.setKeepAlive(true);
			socket.setReceiveBufferSize(visaBaseConfig.getBufferSize());
			// socket.setSoLinger(true, 0);

			SocketMap.sckMap.put(stationName, socket);

			log.info("Station Code->" + stationCode);

			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ISO_8859_1"));
			outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			log.info("TransactionInterchangeListener Connected");
			System.out.println("signOnRequired "+signOnRequired);
			// Send SignOn Request
			System.out.println("sessionActivationRequired "+sessionActivationRequired);
			if (sessionActivationRequired == 1) {
				TransactionProcess sessionProcess = new TransactionProcess(Constants.SESSIONACTIVATION, outputStream,
						Constants.SESSIONACTIVATION, stationCode);
				tpExecutor.execute(sessionProcess);
			}
			
			
			if (signOnRequired == 1) {
				TransactionProcess signOnProcess = new TransactionProcess(Constants.SIGNON, outputStream,
						Constants.SIGNON, stationCode);
				tpExecutor.execute(signOnProcess);
			}

			// send Key Exchange Request
			if (keyxch) {
				// To Start the dynamic key exchange
				if (keyExchangeRequired == 1) {
					TransactionProcess keyExchangeProcess = new TransactionProcess(Constants.KEY_EXCHANGE, outputStream,
							Constants.KEY_EXCHANGE, stationCode);
					tpExecutor.execute(keyExchangeProcess);
				}

			}

			// To Start the keep alive exchange
			if (echoRequired == 1) {
				TransactionProcess keepAliveProcess = new TransactionProcess(Constants.ECHO, outputStream,
						Constants.ECHO, stationCode);
				tpExecutor.execute(keepAliveProcess);
			}

			while (StartupListenerTransactionInterchange.masterLoopFlag) {
				data = new char[2];

				bufferedReader.read(data, 0, 2);

				totallength = toDecimal(data);

				if (totallength == 0) {

					if (stationCode.equals(TPEConstants.VISA_INTERCHANGE)) {
						//continue;
					}
					log.info("Socket Is Closed:" + stationName);
					break;
				}
				if (stationCode.equals(TPEConstants.VISA_INTERCHANGE)) {
					totallength += 2;
				}
				data = new char[(int) totallength];

				if (totallength > BUFFER_SIZE) {
					if (tcpReaderDelay > 0) {
						TimeUnit.MILLISECONDS.sleep(tcpReaderDelay);
					}
				}

				bufferedReader.read(data, 0, (int) totallength);

				tpExecutor.execute(
						new TransactionProcess(new String(data), outputStream, Constants.TRANSACTION, stationCode));
				data = null;

			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {

			try {
				if (socket != null) {
					socket.close();
				}
				if (serverSocket != null) {
					serverSocket.close();
					SocketMap.serverScoketMap.remove(stationName);
				}
				SocketMap.sckMap.remove(stationName);
				socket = null;
				serverSocket = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
			log.info("New Connection Closed->" + stationName);
			// have to reconnect
			if (tpExecutor != null) {
				tpExecutor.shutdown();
			}
			log.info("tpExecutor.shutdown()->" + stationName);
			if (reConnectionRequired == 1 && StartupListenerTransactionInterchange.masterLoopFlag) {
				if (!socketType.equals("S")) {
					try {
						TimeUnit.SECONDS.sleep(reConnectIntervalTime);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error(e.getMessage());
					}
				}
				log.info("Connection retrying.........." + stationName);
				TransactionInterchangeListener TransactionInterchangeListener = new TransactionInterchangeListener();
				TransactionInterchangeListener.setSocketIndex(socketIndex);
				if (socketIndex == 0) {
					TransactionInterchangeListener.setKeyxch(true);
				}
				TransactionInterchangeListener.start();
			}

		}

	}

	long toDecimal(char[] graphical) {
		int length = graphical.length;
		long decimal = 0;
		for (int i = 0; i < graphical.length; i++) {
			length--;
			decimal += graphical[i] * (long) Math.pow(16, 2 * length);
		}
		return decimal;
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket=null;
		socket = new Socket("192.168.2.71", 4444);
		System.out.println("connection  estabished ---"+socket.getLocalPort());
		ExecutorService tpExecutor = Executors.newFixedThreadPool(5);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ISO_8859_1"));
		DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	log.info("TransactionInterchangeListener Connected");
	// Send SignOn Request
		TransactionProcess sessionProcess = new TransactionProcess(Constants.SESSIONACTIVATION, outputStream,
				Constants.SESSIONACTIVATION, "VI");
		tpExecutor.execute(sessionProcess);
	
		socket.close();
		
	}

}
