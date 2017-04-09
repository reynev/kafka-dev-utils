package reynev.kafkautils.kafka.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcin Piłat.
 */
@RestController
@RequestMapping("/message")
class KafkaConsumerController {

    public static final int DEFAULT_MESSAGES_AMOUNT = 10;

    @Autowired
    private MessageReader messageReader;

    @RequestMapping(value = "/{topic}", method = RequestMethod.GET)
    private List<KafkaMessageDto> getTopMessagesWithDefaultAmount(@PathVariable String topic){
        return getTopMessages(topic, DEFAULT_MESSAGES_AMOUNT);
    }

    @RequestMapping(value = "/{topic}/{amount}", method = RequestMethod.GET)
    private List<KafkaMessageDto> getTopMessages(@PathVariable String topic,
                                                 @PathVariable Integer amount){
        Iterable< ConsumerRecord<String, String> > latestRecords =
                messageReader.readTopRecordsFromTopic(topic, amount);
        List<KafkaMessageDto> latestMessages = new ArrayList<>(amount);
        latestRecords.forEach( record -> latestMessages.add( new KafkaMessageDto(record) ));

        return latestMessages;
    }
}
