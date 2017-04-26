package reynev.kafkautils.kafka.message;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import reynev.kafkautils.kafka.common.TopicValidator;
import reynev.kafkautils.kafka.common.exception.TopicNotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Marcin Piłat
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KafkaConsumerControllerTest.TestContextConfiguration.class})
@WebMvcTest
public class KafkaConsumerControllerTest {
    
    private static final String TEST_TOPIC = "Test";
    private static final String TEST_MSG_ID = "Test Id";
    private static final String TEST_MSG_BODY = "Test body";

    @Autowired
    private MessageReader messageReader;

    @Autowired
    private TopicValidator topicValidator;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void beforeTest() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        reset(topicValidator);
        reset(messageReader);
    }

    @Test
    public void whenListTopMessagesInTopic_givenOneMessageIsInTopic_thenReturnOneMessage(){
        Iterable<ConsumerRecord<String, String>> messages = createKafkaMessages(TEST_TOPIC, 1);
        when(messageReader.readTopRecordsFromTopic(eq(TEST_TOPIC), anyInt())).thenReturn(messages);
        // @formatter:off
        given().
        when().
            get("/message/{topic}", TEST_TOPIC).
        then().
            statusCode(OK.value()).
            body(   "$", hasSize(1),
                    "[0].id", equalTo(TEST_MSG_ID),
                    "[0].body", equalTo(TEST_MSG_BODY));
        // @formatter:on
    }

    @Test
    public void whenListTopMessagesInTopic_givenTopicDoesNotExist_thenReturn404(){
        doThrow(TopicNotFoundException.class).when(topicValidator).validateTopic(eq(TEST_TOPIC));

        // @formatter:off
        given().
        when().
            get("/message/{topic}", TEST_TOPIC).
        then().
            statusCode(NOT_FOUND.value());
        // @formatter:on
    }

    @Test
    public void whenListTop5MessagesInTopic_givenOneMessageIsInTopic_thenReturnOneMessage(){
        int correctAmount = 5;
        Iterable<ConsumerRecord<String, String>> messages = createKafkaMessages(TEST_TOPIC, 1);
        when(messageReader.readTopRecordsFromTopic(eq(TEST_TOPIC), eq(correctAmount))).
                thenReturn(messages);
        // @formatter:off
        given().
        when().
            get("/message/{topic}/{amount}", TEST_TOPIC, correctAmount).
        then().
            statusCode(OK.value()).
            body(   "$", hasSize(1),
                    "[0].id", equalTo(TEST_MSG_ID),
                    "[0].body", equalTo(TEST_MSG_BODY));
        // @formatter:on
    }

    @Test
    public void whenListTop5MessagesInTopic_givenTopicDoesNotExist_thenReturn404(){
        int correctAmount = 5;
        doThrow(TopicNotFoundException.class).when(topicValidator).validateTopic(eq(TEST_TOPIC));

        // @formatter:off
        given().
        when().
            get("/message/{topic}/{amount}", TEST_TOPIC, correctAmount).
        then().
            statusCode(NOT_FOUND.value());
        // @formatter:on
    }

    private Iterable<ConsumerRecord<String, String>> createKafkaMessages(String topic, int amount) {
        Collection<ConsumerRecord<String, String>> messages = IntStream.range(0, amount)
                .mapToObj( i -> createConsumerRecord(topic, 0, i))
                .collect(Collectors.toList());
        return messages;
    }

    private ConsumerRecord<String, String> createConsumerRecord(String topic, int partition, int offset) {
        return new ConsumerRecord<>(topic,partition, offset, TEST_MSG_ID, TEST_MSG_BODY);
    }

    public static class TestContextConfiguration{
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

}