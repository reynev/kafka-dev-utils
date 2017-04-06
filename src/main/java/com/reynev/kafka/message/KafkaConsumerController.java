package com.reynev.kafka.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Marcin Piłat on 3/21/17.
 */
@RestController
@RequestMapping("/message")
class KafkaConsumerController {

    @Autowired
    private MessageReader messageReader;

    @RequestMapping(value = "/{topic}/{amount}", method = RequestMethod.GET)
    private List<KafkaMessageDto> getTopMessages(@PathVariable String topic, @PathVariable Integer amount){
        Iterable< ConsumerRecord<String, String> > latestRecords =
                messageReader.readTopRecordsFromTopic(topic, amount);
        List<KafkaMessageDto> latestMessages = new ArrayList<>(amount);
        latestRecords.forEach( record -> latestMessages.add( new KafkaMessageDto(record) ));

        return latestMessages;
    }
}
