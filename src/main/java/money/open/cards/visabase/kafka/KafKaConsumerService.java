package money.open.cards.visabase.kafka;

import lombok.extern.slf4j.Slf4j;
import money.open.cards.visabase.dto.Message;
import money.open.cards.visabase.service.impl.TransactionProcessAPIImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafKaConsumerService
{

    @Autowired
    TransactionProcessAPIImpl  transactionProcessAPIImpl;

    @KafkaListener(topics = KafkaConstants.TOPIC_NAME_RECIEVE,
            groupId = KafkaConstants.GROUP_ID)
    public void consume(String message) throws Exception {
        log.info(String.format("Message received -> %s", message));

        Message msgBuffer = null;
            msgBuffer = new Message();
            msgBuffer.setMessage(message);
            msgBuffer.setMessageStatus("RSP");
            msgBuffer.setStationName("HOST");


            msgBuffer = transactionProcessAPIImpl.respond(msgBuffer);

    }
}