package reynev.kafkautils.test.integration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import info.batey.kafka.unit.KafkaUnit;
import info.batey.kafka.unit.KafkaUnitRule;
import kafka.producer.KeyedMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reynev.kafkautils.WebApplication;
import reynev.kafkautils.test.configuration.category.IntegrationTest;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Tests that are checking happy path of application functionality.
 *
 * FIt uses kafka-unit to run Zookeeper and Kafka servers in memory.
 *
 * @author Marcin Piłat.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebApplication.class, webEnvironment = RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicIntegrationTest {

    private String randomTestTopic;
    private static final String TEST_MSG_ID = "ID";
    private static final String TEST_MSG_BODY = "Test Body";

    @Value("${local.server.port}")
    private int port;

    @Value("${kafka.bootstrap-servers}")
    private String kafkaServerConnection;

    private KafkaUnit kafkaUnit;

    @Rule
    public KafkaUnitRule kafkaUnitRule = new KafkaUnitRule("localhost:9091", "localhost:9092");

    @Before
    public void setUp(){
        randomTestTopic = UUID.randomUUID().toString();
        kafkaUnit = kafkaUnitRule.getKafkaUnit();
        RestAssured.port = port;
    }

    @Test
    public void whenAAListTopics_givenNoTopicsWasCreated_thenReturn200AndEmptyList() {
        // @formatter:off
        given().
        when().
            get("/topic").
        then().
            statusCode(HttpStatus.OK.value()).
            body("$", hasSize(0));
        // @formatter:on
    }

    @Test
    public void whenListTopics_givenTopicWasCreated_thenReturn200AndNameOfTopic() {
        //given
        kafkaUnit.createTopic(randomTestTopic);
        // @formatter:off
        given().
        when().
            get("/topic").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", hasItem(randomTestTopic));
        // @formatter:on
    }

    @Test
    public void whenCreateMessageInTopic_givenTopicWasntCreated_thenReturn200AndTopicISCreated() throws JSONException, TimeoutException, InterruptedException {
        JSONObject message = new JSONObject();
        message.put("id", TEST_MSG_ID);
        message.put("body", TEST_MSG_BODY);
        // @formatter:off
        given().
            contentType(ContentType.JSON).
            body(message.toString()).
            pathParam("topic", randomTestTopic).
        when().
            post("/message/{topic}").
        then().
            statusCode(HttpStatus.OK.value());

        given().
        when().
            get("/topic").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", hasItem(randomTestTopic));
        // @formatter:on
    }

    @Test
    public void whenCreateMessageInTopic_givenTopicWasCreated_thenReturn200AndMessageIsInKafka() throws JSONException, TimeoutException, InterruptedException {
        //given
        kafkaUnit.createTopic(randomTestTopic);
        kafkaUnit.readKeyedMessages(randomTestTopic,0);

        JSONObject message = new JSONObject();
        message.put("id", TEST_MSG_ID);
        message.put("body", TEST_MSG_BODY);
        // @formatter:off
        given().
            contentType(ContentType.JSON).
            body(message.toString()).
            pathParam("topic", randomTestTopic).
        when().
            post("/message/{topic}").
        then().
            statusCode(HttpStatus.OK.value());
        // @formatter:on

        kafkaUnit.readKeyedMessages(randomTestTopic,1);
    }

    @Test
    public void whenReadMessageFromTopic_givenOneMessageIsInTopic_thenReturn200AndMessage() throws JSONException, TimeoutException, InterruptedException {
        //given
        kafkaUnit.createTopic(randomTestTopic);
        KeyedMessage<String, String> message = new KeyedMessage<String, String>(randomTestTopic, TEST_MSG_ID, TEST_MSG_BODY);
        kafkaUnit.sendMessages(message);

        // @formatter:off
        given().
            pathParam("topic", randomTestTopic).
        when().
            get("/message/{topic}").
        then().
            statusCode(HttpStatus.OK.value()).
            body(   "$", hasSize(1),
                    "[0].id", equalTo(TEST_MSG_ID),
                    "[0].body", equalTo(TEST_MSG_BODY));
        // @formatter:on

    }
}
