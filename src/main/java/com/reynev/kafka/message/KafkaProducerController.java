package com.reynev.kafka.message;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Marcin Piłat on 3/21/17.
 */
@RestController
@RequestMapping("/message")
class KafkaProducerController {

    Logger logger = LoggerFactory.getLogger(KafkaProducerController.class);

    @Autowired
    private KafkaProducer<String, String> kafkaProducer;

    @RequestMapping(value = "/{topic}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    String produceMessage(@PathVariable String topic, @RequestBody MessageDto messageDto) {
        ProducerRecord record = new ProducerRecord(topic, messageDto.getId(), messageDto.getBody());
        kafkaProducer.send(record);
        kafkaProducer.flush();
        logger.info("Message sent: " + messageDto.getBody());
        return String.format("MessageDto %s sent to topic %s.", messageDto, topic);
    }
}
