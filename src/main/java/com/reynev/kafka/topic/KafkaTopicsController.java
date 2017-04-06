package com.reynev.kafka.topic;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Marcin Piłat on 4/6/17.
 */
@RestController
@RequestScope
@RequestMapping("/topic")
class KafkaTopicsController {

    @Autowired
    private KafkaConsumer<String, String> kafkaConsumer;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    private Collection<KafkaTopicDto> listTopics(){
        return kafkaConsumer.listTopics().entrySet().stream().
                map(e -> new KafkaTopicDto(e.getKey())).collect(Collectors.toList());
    }
}
