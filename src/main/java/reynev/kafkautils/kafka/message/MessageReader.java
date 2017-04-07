package reynev.kafkautils.kafka.message;

import reynev.kafkautils.collections.LimitedSortedSet;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Marcin Piłat on 4/5/17.
 */
@RequestScope
@Component
class MessageReader {

    private Consumer<String, String> kafkaConsumer;

    public MessageReader(@Autowired Consumer<String, String> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    /**
     * Reads last messages from topic.
     *
     * It reads from each partition and chooses last messages.
     *
     * @param topic - name of topic
     * @param amount - number of messages to read
     * @return Last messages
     */
    Iterable<ConsumerRecord<String, String>> readTopRecordsFromTopic(String topic, int amount){
        moveConsumerOffsetOnPartitions(topic, amount);
        ConsumerRecords<String, String> records = readMessages();
        return getLatestMessages(amount, records);
    }

    private LimitedSortedSet<ConsumerRecord<String, String>> getLatestMessages(int amount, ConsumerRecords<String, String> records) {
        LimitedSortedSet<ConsumerRecord<String, String>> topRecords =
                new LimitedSortedSet(new ConsumerRecordTimeComparator(), amount);
        records.forEach(topRecords::add);
        return topRecords;
    }

    private ConsumerRecords<String, String> readMessages() {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(1000);
        kafkaConsumer.close();
        return records;
    }

    private void moveConsumerOffsetOnPartitions(String topic, int amount) {
        kafkaConsumer.subscribe(Arrays.asList(topic));
        Set<TopicPartition> partitions = waitForAssignment();
        Map<TopicPartition, Long> partitionsOffset = kafkaConsumer.endOffsets(partitions);

        partitionsOffset.forEach( (partition, offset) -> kafkaConsumer.seek(partition, offset - amount));
    }

    private Set<TopicPartition> waitForAssignment() {
        kafkaConsumer.poll( 1000);
        Set<TopicPartition> assignment;
        do {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new InternalError(e);
            }
            assignment = kafkaConsumer.assignment();
        }while (assignment.isEmpty());
        return assignment;
    }

}
