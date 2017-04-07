package reynev.kafkautils.kafka.message;

import lombok.Value;

/**
 * Created by Marcin Piłat on 3/21/17.
 */
@Value
public class MessageDto {

    private String id;

    private String body;
}
