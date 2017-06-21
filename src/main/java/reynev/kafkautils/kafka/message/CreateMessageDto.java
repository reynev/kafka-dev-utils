package reynev.kafkautils.kafka.message;

import lombok.Value;

/**
 * @author Marcin Piłat.
 */
@Value
class CreateMessageDto {

    private String id;

    private String body;
}
