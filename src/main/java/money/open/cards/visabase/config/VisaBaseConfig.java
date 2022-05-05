package money.open.cards.visabase.config;

import lombok.Getter;
import lombok.Setter;
import money.open.cards.visabase.service.impl.LoadTransactionServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@Setter
@ComponentScan
@PropertySource(value={"application.properties"})
public class VisaBaseConfig {

    @Value("${socket.count}")
    String socketCount;

    @Value("${host.threads.count.max}")
    int threadCount;

    @Value("${host.connection.type}")
    String hostType;
    @Value("${host.connection.ip}")
    String hostIp;
    @Value("${host.connection.port}")
    int hostPort;

    @Value("${host.sessionactivation.request}")
    int sessionActivationRequired;
    @Value("${host.sign.request}")
    int signRequest;
    @Value("${host.dynamic.key.exchange}")
    int keyExchangeRequired;
    @Value("${host.echo.enable}")
    int echoRequired;
    @Value("${host.reconnection.flag}")
    int reConnectionRequired;
    @Value("${host.reconnect.interval}")
    int reConnectIntervalTime;
    @Value("${interchange.station.code}")
    String stationCode;

    @Value("${host.tcp.reader.delay}")
    int tcpReaderDelay;

    @Value("${host.socket.read.buffer.size}")
    int bufferSize;

    @Bean
    public LoadTransactionServiceImpl loadTransactionService() {
        return new LoadTransactionServiceImpl();
    }

//    @Bean
//    public TransactionInterchangeListener transactionInterchangeListener() {
//        return new TransactionInterchangeListener();
//    }






}
