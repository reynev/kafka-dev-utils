package reynev.kafkautils.test.collections;

import com.google.common.collect.MinMaxPriorityQueue;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Marcin Piłat.
 */
@RunWith(JUnitParamsRunner.class)
public class MinMaxPriorityQueueTest {

    public Object[] parametersForTestQueue() {
       return new Object[]{
                new Object[]{3, Arrays.asList(1,2,3), Arrays.asList(1,2,3)},
                new Object[]{3, Arrays.asList(1,2,3,4), Arrays.asList(2,3,4)},
                new Object[]{3, Arrays.asList(1,2), Arrays.asList(1,2)},
                new Object[]{3, Arrays.asList(5,1,3,2,4), Arrays.asList(3,4,5)}
            };
    }

    @Test
    @Parameters
    public void testQueue(int size, List<Integer> elements, List<Integer> expectedElements){

        Comparator<Integer> comparator = (m1, m2) -> -Integer.compare(m1, m2);

        MinMaxPriorityQueue<Integer> queue = MinMaxPriorityQueue
                .orderedBy( comparator )
                .maximumSize(size)
                .create();

        elements.forEach(e -> queue.add(e));

        assertThat(queue).hasSameElementsAs(expectedElements);

    }


}
