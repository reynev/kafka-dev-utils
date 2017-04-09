package reynev.kafkautils.kafka.message;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for writing messages to given topic.
 *
 * @author Marcin Piłat
 */
@Service
class MessageWriter {

    private Logger logger = LoggerFactory.getLogger(MessageWriter.class);

    private KafkaProducer<String, String> kafkaProducer;

    MessageWriter(@Autowired KafkaProducer<String, String> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    void writeMessage(String topic, CreateMessageDto createMessageDto){
        ProducerRecord record = new ProducerRecord(topic, createMessageDto.getId(), createMessageDto.getBody());
        kafkaProducer.send(record);
        kafkaProducer.flush();
        logger.info("Message '{}' sent to topic [{}]", createMessageDto.getBody(), topic);
    }

}
