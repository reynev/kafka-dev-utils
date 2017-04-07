package reynev.kafkautils.collections;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Marcin Piłat on 3/23/17.
 *
 * Simple collection class that should have similar functionality to {@link com.google.common.collect.MinMaxPriorityQueue}.
 *
 *
 */
public class LimitedSortedSet<T> implements Collection<T> {

    private final int maxSize;

    private final TreeSet<T> treeSet;

    public LimitedSortedSet(Comparator<T> comparator, int maxSize) {
        checkArgument(maxSize > 0);
        checkNotNull(comparator);
        this.maxSize = maxSize;
        treeSet = new TreeSet<>(comparator);
    }

    public LimitedSortedSet(int maxSize) {
        checkArgument(maxSize > 0);
        this.maxSize = maxSize;
        treeSet = new TreeSet<>();
    }

    @Override
    public boolean add(T obj) {
        treeSet.add(obj);
        if(treeSet.size() > maxSize){
            T removed = treeSet.pollLast();
            return obj != removed;
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return treeSet.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return treeSet.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        checkNotNull(collection);
        boolean changed = false;
        for(T obj : collection){
            changed |= add(obj);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return treeSet.removeAll(collection);
    }

    @Override
    public boolean removeIf(Predicate<? super T> predicate) {
        return treeSet.removeIf(predicate);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return treeSet.retainAll(collection);
    }

    @Override
    public void clear() {
        treeSet.clear();
    }

    @Override
    public int size() {
        return treeSet.size();
    }

    @Override
    public boolean isEmpty() {
        return treeSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return treeSet.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return treeSet.iterator();
    }

    @Override
    public Object[] toArray() {
        return treeSet.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return treeSet.toArray(t1s);
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        treeSet.forEach(consumer);
    }

    @Override
    public Spliterator<T> spliterator() {
        return treeSet.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return treeSet.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return treeSet.parallelStream();
    }

    @Override
    public String toString() {
        return treeSet.toString();
    }
}
