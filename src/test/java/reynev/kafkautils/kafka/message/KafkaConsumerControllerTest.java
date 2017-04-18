package reynev.kafkautils.kafka.message;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;

/**
 * @author Marcin Piłat
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestControllersConfiguration.class})
@WebMvcTest
public class KafkaConsumerControllerTest {
    
    private static final String TEST_TOPIC = "Test";
    private static final String TEST_MSG_ID = "Test Id";
    private static final String TEST_MSG_BODY = "Test body";

    private int offset;

    @Autowired
    private MessageReader messageReader;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void beforeTest() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void testCreatingMessagesAndListingTopicsAndListingLastMessage(){
        int correctAmount = 5;
        Iterable<ConsumerRecord<String, String>> messages = createKafkaMessages(TEST_TOPIC, 1);
        Mockito.when(messageReader.readTopRecordsFromTopic(TEST_TOPIC, correctAmount)).
                thenReturn(messages);
        // @formatter:off
        given().
        when().
            get("/message/{topic}/{amount}", TEST_TOPIC, correctAmount).
        then().
            statusCode(HttpStatus.OK.value());
        // @formatter:on
    }

    private Iterable<ConsumerRecord<String, String>> createKafkaMessages(String topic, int amount) {
        Collection<ConsumerRecord<String, String>> messages = IntStream.rangeClosed(0, amount)
                .mapToObj( i -> createConsumerRecord(topic, 0, i))
                .collect(Collectors.toList());
        return messages;
    }

    private ConsumerRecord<String, String> createConsumerRecord(String topic, int partition, int i) {
        return new ConsumerRecord<>(topic,partition, offset++, TEST_MSG_ID, TEST_MSG_BODY);
    }

}