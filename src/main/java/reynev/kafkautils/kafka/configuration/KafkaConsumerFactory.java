package reynev.kafkautils.kafka.configuration;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Properties;
import java.util.UUID;

/**
 * @author Marcin Piłat.
 */
@Configuration
class KafkaConsumerFactory {

    Logger logger = LoggerFactory.getLogger(KafkaConsumerFactory.class);

    /**
     * Creates one-time consumer with random group
     * @return
     */
    @Bean
    @Scope("prototype")
    KafkaConsumer<String, String> createConsumer(){
        String consumerGroup = generateGroupName();

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", consumerGroup);
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", StringDeserializer.class.getCanonicalName());
        props.put("value.deserializer", StringDeserializer.class.getCanonicalName());

        KafkaConsumer consumer = new KafkaConsumer<>(props);

        logger.info("Consumer created in group: {}", consumerGroup);

        return consumer;
    }

    private String generateGroupName() {
        return UUID.randomUUID().toString();
    }
}
