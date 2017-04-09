package reynev.kafkautils.test.integration;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reynev.kafkautils.WebApplication;
import reynev.kafkautils.kafka.message.CreateMessageDto;
import reynev.kafkautils.test.configuration.IntegrationTest;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Tests that are checking happy path of application functionality.
 *
 * For now it requires Kafka on localhost:9092 but hopefully it will change.
 *
 * @author Marcin Piłat.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebApplication.class, webEnvironment = RANDOM_PORT)
public class BasicIntegrationTest {

    private String randomTestTopic;
    private static final String TEST_MSG_ID = "ID";
    private static final String TEST_MSG_BODY = "Test Body";

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testCreatingMessagesAndListingTopicsAndListingLastMessage(){
        randomTestTopic = UUID.randomUUID().toString();
        // @formatter:off
        CreateMessageDto message = new CreateMessageDto(TEST_MSG_ID, TEST_MSG_BODY);
        given().
            contentType(ContentType.JSON).
            port(port).
            body(message).
            pathParam("topic", randomTestTopic).
        when().
            post("/message/{topic}").
        then().
            statusCode(HttpStatus.OK.value());

        given().
            port(port).
        when().
            get("/topic").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", hasItem(randomTestTopic));

        given().
            port(port).
            pathParam("topic", randomTestTopic).
        when().
            get("/message/{topic}/1").
        then().
            statusCode(HttpStatus.OK.value()).
            body("id", hasItem(TEST_MSG_ID)).
            body("body", hasItem(TEST_MSG_BODY));
        // @formatter:on
    }
}
