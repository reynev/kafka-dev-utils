package reynev.kafkautils.collections;

import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.TestStringCollectionGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

import java.util.Collection;

/**
 * @author Marcin Piłat.
 */
@RunWith(AllTests.class)
public class LimitedSortedSetSuiteTest {

    public static Test suite() {
        TestSuite suite =
                new TestSuite("LimitedSortedSetFunctionalTest");
        suite.addTest(testsForLimitedSortedSet());
        return suite;
    }

    public static TestSuite testsForLimitedSortedSet() {
        return CollectionTestSuiteBuilder
                .using(new TestStringCollectionGenerator(){

                    @Override
                    protected Collection<String> create(String[] strings) {
                        LimitedSortedSet collection = new LimitedSortedSet(5);
                        for(String s : strings){
                            collection.add(s);
                        }
                        return collection;
                    }
                })
                .named("test LimitedSortedSet")
                .withFeatures(
                        CollectionSize.ANY,
                        CollectionFeature.GENERAL_PURPOSE,
                        CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                        CollectionFeature.REJECTS_DUPLICATES_AT_CREATION,
                        CollectionFeature.RESTRICTS_ELEMENTS
                        )
                .createTestSuite();
    }

}