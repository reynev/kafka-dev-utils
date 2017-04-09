package reynev.kafkautils.kafka.message.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Marcin Piłat.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Amount of messages has to be greater than 1.")
public class IncorrectAmountException extends RuntimeException {
}
