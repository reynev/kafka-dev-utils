package reynev.kafkautils.kafka.topic;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Marcin Piłat.
 */
@RestController
@RequestMapping("/topic")
class KafkaTopicsController {

    @Autowired
    private KafkaConsumer<String, String> kafkaConsumer;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private Collection<KafkaTopicDto> listTopics(){
        return kafkaConsumer.listTopics().entrySet().stream().
                map(e -> new KafkaTopicDto(e.getKey())).collect(Collectors.toList());
    }
}
