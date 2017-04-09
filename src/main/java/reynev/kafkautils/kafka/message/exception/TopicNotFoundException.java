package reynev.kafkautils.kafka.message.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Marcin Piłat.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="There is no such topic.")
public class TopicNotFoundException extends RuntimeException {
}
