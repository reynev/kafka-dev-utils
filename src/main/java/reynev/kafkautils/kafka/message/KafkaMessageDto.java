package reynev.kafkautils.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Date;

/**
 * @author Marcin Piłat.
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
