package reynev.kafkautils.kafka.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reynev.kafkautils.kafka.common.TopicValidator;

import static org.mockito.Mockito.mock;

/**
 * @author Marcin Piłat
 */
@Configuration
public class RestControllersConfiguration {

    @Bean
    MessageReader createMessageReader() {
        return mock(MessageReader.class);
    }

    @Bean
    TopicValidator createTopicValidator() {
        return mock(TopicValidator.class);
    }

    @Bean
    KafkaConsumerController createKafkaConsumerController(@Autowired MessageReader messageReader,
                                                          @Autowired TopicValidator topicValidator) {
        return new KafkaConsumerController(messageReader, topicValidator);
    }
}
