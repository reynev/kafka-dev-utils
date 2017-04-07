package reynev.kafkautils.rest;

import com.jayway.restassured.http.ContentType;
import reynev.kafkautils.WebApplication;
import reynev.kafkautils.kafka.message.MessageDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by Marcin Piłat on 4/3/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebApplication.class, webEnvironment = RANDOM_PORT)
public class BasicIntegrationTest {

    public static final String TEST_TOPIC = "test";
    public static final String TEST_MSG_ID = "ID";
    public static final String TEST_MSG_BODY = "Test Body";

    @Value("${local.server.port}")
    int port;

    @Test
    public void testProducer(){
        // @formatter:off
        MessageDto message = new MessageDto(TEST_MSG_ID, TEST_MSG_BODY);
        given().
            contentType(ContentType.JSON).
            port(port).
            body(message).
            pathParam("topic", TEST_TOPIC).
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
            body("name", hasItem(TEST_TOPIC));

        given().
            port(port).
            pathParam("topic", TEST_TOPIC).
        when().
            get("/message/{topic}/1").
        then().
            statusCode(HttpStatus.OK.value()).
            body("id", hasItem(TEST_MSG_ID)).
            body("body", hasItem(TEST_MSG_BODY));
        // @formatter:on

    }

}
