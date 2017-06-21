package reynev.kafkautils.kafka.topic;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Lists all topics from kafka.
 *
 * @author Marcin Piłat
 */
@Component
class TopicLister {

    private Consumer<String, String> kafkaConsumer;

    TopicLister(@Autowired Consumer<String, String> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    Map<String, List<PartitionInfo>> listTopics(){
        return kafkaConsumer.listTopics();
    }
}
