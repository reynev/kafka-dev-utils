package reynev.kafkautils.kafka.common;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Marcin Piłat
 */
@Component
@ConfigurationProperties("kafka")
public class KafkaConfiguration {

    @Getter
    @Setter
    @NotEmpty
    private String bootstrapServers;

}
