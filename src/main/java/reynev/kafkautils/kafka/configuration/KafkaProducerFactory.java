package reynev.kafkautils.kafka.configuration;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reynev.kafkautils.kafka.common.KafkaConfiguration;

import java.util.Properties;

/**
 * Created by reynev on 3/21/17.
 */
@Configuration
class KafkaProducerFactory {

    @Autowired
    private KafkaConfiguration kafkaConfiguration;

    @Bean
    KafkaProducer<String, String> createProducer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaConfiguration.getBootstrapServers());
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", StringSerializer.class.getCanonicalName());
        props.put("value.serializer", StringSerializer.class.getCanonicalName());

        return new KafkaProducer<>(props);
    }
}
