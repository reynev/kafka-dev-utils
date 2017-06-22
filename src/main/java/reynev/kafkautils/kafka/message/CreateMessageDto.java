package reynev.kafkautils.kafka.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Marcin Piłat.
 */
class CreateMessageDto {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String body;
}
