package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;
import ua.edu.ucu.iterators.FilterIterator;
import ua.edu.ucu.iterators.FlatMapIterator;
import ua.edu.ucu.iterators.MapIterator;

import java.util.*;

public class AsIntStream implements IntStream {

    private final Iterator<Integer> iterInt;

    private AsIntStream(Iterator<Integer> iterInt) {
        this.iterInt = iterInt;
    }

    public void isEmpty() {
        if (!this.iterInt.hasNext()) {
            throw new IllegalArgumentException("Stream is empty!");
        }
    }

    public static IntStream of(int... values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Stream is empty!");
        }
        int[] arrays = Arrays.copyOf(values, values.length);
        return new AsIntStream(new Iterator<Integer>() {
            private int counter = 0;

            @Override
            public boolean hasNext() {
                return counter < arrays.length;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return arrays[counter++];
            }
        });
    }

    @Override
    public Double average() {
        isEmpty();
        Iterable<Integer> iterable = () -> this.iterInt;
        int sum = 0;
        int counter = 0;
        for (int elem : iterable) {
            sum += elem;
            counter++;
        }
        return (double) sum / counter;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer max() {
        return reduce((int) Double.NEGATIVE_INFINITY, Math::max);
    }

    @Override
    public Integer min() {
        return reduce((int) Double.POSITIVE_INFINITY, Math::min);
    }

    @Override
    public long count() {
        return (long) toArray().length;
    }

    @Override
    public Integer sum() {
        return reduce(0, Integer::sum);
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        return new AsIntStream(new FilterIterator(iterInt, predicate));
    }

    @Override
    public void forEach(IntConsumer action) {
        while (this.iterInt.hasNext()) {
            int element = this.iterInt.next();
            action.accept(element);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        return new AsIntStream(new MapIterator(this.iterInt, mapper));
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        return new AsIntStream(new FlatMapIterator(this.iterInt, func));
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        isEmpty();
        int result = identity;
        while (iterInt.hasNext()) {
            result = op.apply(result, iterInt.next());
        }
        return result;
    }

    @Override
    public int[] toArray() {
        List<Integer> array = new ArrayList<>();
        while (iterInt.hasNext()) {
            array.add(iterInt.next());
        }
        int[] array2 = new int[array.size()];
        for (int i = 0; i < array.size(); i++) {
            array2[i] = array.get(i);
        }
        return array2;
    }

}
