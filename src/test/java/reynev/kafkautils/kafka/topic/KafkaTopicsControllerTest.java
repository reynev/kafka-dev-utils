package reynev.kafkautils.kafka.topic;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.kafka.common.PartitionInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.lang.String.valueOf;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Marcin Piłat
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KafkaTopicsControllerTest.TestContextConfiguration.class})
@WebMvcTest
public class KafkaTopicsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TopicLister topicLister;

    @Before
    public void beforeTest() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        reset(topicLister);
    }

    @Test
    public void whenListTopics_givenNoTopicExists_thenReturnEmptyList(){
        when(topicLister.listTopics()).thenReturn(emptyMap());
        // @formatter:off
        given().
        when().
            get("/topic").
        then().
            statusCode(OK.value()).
            body("$", hasSize(0));
        // @formatter:on
    }

    @Test
    public void whenListTopics_givenTopicsExist_thenReturnListWithTopics(){
        Map<String, List<PartitionInfo>> mockedTopics = createMockedTopics(2);
        when(topicLister.listTopics()).thenReturn(mockedTopics);
        // @formatter:off
        given().
        when().
            get("/topic").
        then().
            statusCode(OK.value()).
            body(   "$", hasSize(2),
                    "[0].name", equalTo("test-topic-0"),
                    "[1].name", equalTo("test-topic-1"));
        // @formatter:on
    }

    private Map<String, List<PartitionInfo>> createMockedTopics(int noOfTopics) {
        Map<String, List<PartitionInfo>> topics = IntStream.range(0, noOfTopics)
                .mapToObj( i -> valueOf("test-topic-" + i))
                .collect(toMap(identity(), (t) -> emptyList() ));
        return topics;
    }

    public static class TestContextConfiguration{

        @Bean
        TopicLister createTopicLister() {
            return mock(TopicLister.class);
        }

        @Bean
        KafkaTopicsController createKafkaConsumerController(@Autowired TopicLister topicLister) {
            return new KafkaTopicsController(topicLister);
        }
    }
}