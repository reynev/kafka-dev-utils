package reynev.kafkautils.collections;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Marcin Piłat on 3/23/17.
 */
@RunWith(JUnitParamsRunner.class)
public class LimitedSortedSetFunctionalTest {

    public Object[] parametersForTestQueue() {
        return new Object[]{
                new Object[]{5, Arrays.asList(), Arrays.asList()},
                new Object[]{3, Arrays.asList(1,2,3), Arrays.asList(1,2,3)},
                new Object[]{3, Arrays.asList(1,2,3,4), Arrays.asList(2,3,4)},
                new Object[]{3, Arrays.asList(1,2), Arrays.asList(1,2)},
                new Object[]{3, Arrays.asList(5,1,3,2,4), Arrays.asList(3,4,5)},
                new Object[]{5, Arrays.asList(Integer.MAX_VALUE, Integer.MIN_VALUE, 0, -1, 1, 100, -100),
                        Arrays.asList(Integer.MAX_VALUE, 0, -1, 1, 100)}

        };
    }

    @org.junit.Test
    @Parameters
    public void testQueue(int size, List<Integer> elements, List<Integer> expectedElements){

        Comparator<Integer> comparator = (m1, m2) -> -Integer.compare(m1, m2);

        LimitedSortedSet<Integer> queue = new LimitedSortedSet(comparator, size);

        queue.addAll(elements);

        assertThat(queue).hasSameElementsAs(expectedElements);
    }
}