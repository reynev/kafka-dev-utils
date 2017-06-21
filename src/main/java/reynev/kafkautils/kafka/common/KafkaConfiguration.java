package reynev.kafkautils.kafka.common;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Marcin Piłat
 */
@Data
@Component
@ConfigurationProperties("kafka")
public class KafkaConfiguration {

    @NotEmpty
    private String bootstrapServers;

}
