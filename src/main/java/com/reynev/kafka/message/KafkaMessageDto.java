package com.reynev.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Marcin Piłat on 3/22/17.
 */
@Value
@AllArgsConstructor
class KafkaMessageDto{

    private String id;

    private String body;

    private Long offset;

    private Date date;

    KafkaMessageDto(ConsumerRecord<String, String> record) {
        this(record.key(), record.value(), record.offset(), new Date(record.timestamp()));
    }
}
