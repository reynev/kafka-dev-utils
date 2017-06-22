package reynev.kafkautils.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Date;

/**
 * @author Marcin Piłat.
 */
@AllArgsConstructor
class KafkaMessageDto{

    @Getter
    private String id;

    @Getter
    private String body;

    @Getter
    private Long offset;

    @Getter
    private Date date;

    KafkaMessageDto(ConsumerRecord<String, String> record) {
        this(record.key(), record.value(), record.offset(), new Date(record.timestamp()));
    }
}
