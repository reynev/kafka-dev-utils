package reynev.kafkautils.kafka.message;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import reynev.kafkautils.kafka.message.exception.IncorrectAmountException;
import reynev.kafkautils.kafka.message.exception.TopicNotFoundException;
import reynev.kafkautils.test.collections.LimitedSortedSet;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;

/**
 * @author Marcin Piłat.
 */
@RequestScope
@Service
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
        verifyTopic(topic);
        validateAmount(amount);

        ConsumerRecords<String, String> records = readMessages(topic, amount);
        return getLatestMessages(amount, records);
    }

    private ConsumerRecords<String, String> readMessages(String topic, int amount) {
        moveConsumerOffsetOnPartitions(topic, amount);
        ConsumerRecords<String, String> records = kafkaConsumer.poll(5000);
        kafkaConsumer.close();
        return records;
    }

    private LimitedSortedSet<ConsumerRecord<String, String>> getLatestMessages(int amount, ConsumerRecords<String, String> records) {
        LimitedSortedSet<ConsumerRecord<String, String>> topRecords =
                new LimitedSortedSet<>(Comparator.comparingLong(ConsumerRecord::timestamp), amount);
        records.forEach(topRecords::add);
        return topRecords;
    }

    private void moveConsumerOffsetOnPartitions(String topic, int amount) {
        kafkaConsumer.subscribe(Arrays.asList(topic));
        Set<TopicPartition> partitions = waitForAssignment();
        Map<TopicPartition, Long> partitionsOffset = kafkaConsumer.endOffsets(partitions);

        partitionsOffset.forEach(
                (partition, offset) -> kafkaConsumer.seek(partition, max(offset - amount,0)));
    }

    private Set<TopicPartition> waitForAssignment() {
        kafkaConsumer.poll( 1000);
        Set<TopicPartition> assignment;
        do {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new InternalError(e);
            }
            assignment = kafkaConsumer.assignment();
        }while (assignment.isEmpty());
        return assignment;
    }

    @Override
    public String toString() {
        return "MessageReader";
    }

    private void validateAmount(int amount) {
        if(amount < 1){
            throw new IncorrectAmountException();
        }
    }

    private void verifyTopic(String topic) {
        boolean topicExists = kafkaConsumer.listTopics().containsKey(topic);
        if(!topicExists){
            throw new TopicNotFoundException();
        }
    }
}
