package reynev.kafkautils.kafka.topic;

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

    private TopicLister topicLister;

    public KafkaTopicsController(@Autowired TopicLister topicLister) {
        this.topicLister = topicLister;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    private Collection<KafkaTopicDto> listTopics(){
        return topicLister.listTopics().entrySet().stream().
                map(e -> new KafkaTopicDto(e.getKey())).collect(Collectors.toList());
    }
}
