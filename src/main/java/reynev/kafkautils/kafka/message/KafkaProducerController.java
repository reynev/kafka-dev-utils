package reynev.kafkautils.kafka.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcin Piłat.
 */
@RestController
@RequestMapping("/message")
class KafkaProducerController {

    private MessageWriter messageWriter;

    KafkaProducerController(@Autowired MessageWriter messageWriter) {
        this.messageWriter = messageWriter;
    }

    @RequestMapping(value = "/{topic}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void produceMessage(@PathVariable String topic, @RequestBody(required = false) CreateMessageDto createMessageDto) {
        messageWriter.writeMessage(topic, createMessageDto);
    }
}
