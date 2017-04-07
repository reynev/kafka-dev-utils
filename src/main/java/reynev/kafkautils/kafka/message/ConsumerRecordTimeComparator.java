package reynev.kafkautils.kafka.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Comparator;

/**
 * Created by Marcin Piłat on 3/27/17.
 */
public class ConsumerRecordTimeComparator implements Comparator<ConsumerRecord> {

    @Override
    public int compare(ConsumerRecord record1, ConsumerRecord record2) {
        return Long.compare(record1.timestamp(),record2.timestamp());
    }
}
