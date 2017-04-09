package reynev.kafkautils.kafka.common;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reynev.kafkautils.kafka.common.exception.TopicNotFoundException;

/**
 * Component that checks if topic exists.
 *
 * @author Marcin Piłat
 */
@Component
public class TopicValidator {

    private Consumer<String, String> kafkaConsumer;

    private TopicValidator(@Autowired Consumer<String, String> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    public void validateTopic(String topic) {
        boolean topicExists = kafkaConsumer.listTopics().containsKey(topic);
        if(!topicExists){
            throw new TopicNotFoundException();
        }
    }
}
