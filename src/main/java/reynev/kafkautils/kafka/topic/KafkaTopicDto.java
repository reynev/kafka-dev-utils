package reynev.kafkautils.kafka.topic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Marcin Piłat.
 */
@AllArgsConstructor
class KafkaTopicDto {

    @Getter
    @Setter
    private String name;
}
