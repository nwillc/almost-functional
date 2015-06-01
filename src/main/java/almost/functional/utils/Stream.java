package almost.functional.utils;

import almost.functional.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @since 1.8
 * @param <T>
 */
public class Stream<T> {
    private final Iterator<T> iterator;

    public static <R> Stream<R> of(Iterable<R> iterable) {
        return new Stream<R>(iterable.iterator());
    }

    public static <R> Stream<R> of(R ... elements) {
        return new Stream<R>(new ArrayIterable.ArrayIterator<R>(elements));
    }

    private Stream(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public void forEach(Consumer<? super T> action) {
        while (iterator.hasNext()) {
            action.accept(iterator.next());
        }
    }

    public Optional<T> reduce(final BiFunction<T, ? super T, T> accumulator) {
        boolean found = false;
        T result = null;
        while (iterator.hasNext()) {
            if (!found) {
                result = iterator.next();
                found = true;
            } else {
                result = accumulator.apply(result, iterator.next());
            }
        }
        return found ? Optional.of(result) : Optional.<T>empty();
    }

    public <R> R reduce(final R initial, final BiFunction<R, ? super T, R> accumulator) {
        R returnValue = initial;
        while (iterator.hasNext()) {
            returnValue = accumulator.apply(returnValue, iterator.next());
        }
        return returnValue;
    }

    public Stream<T> filter(Predicate<? super T> predicate) {
        List<T> reduction = new ArrayList<T>();
        while (iterator.hasNext()) {
            final T element = iterator.next();
            if (predicate.test(element)) {
                reduction.add(element);
            }
        }
        return Stream.of(reduction);
    }

    public <R> Stream<R> map(Function<? super T,? extends R> mapper) {
        List<R> list = new ArrayList<R>();
        while (iterator.hasNext()) {
            list.add(mapper.apply(iterator.next()));
        }
        return Stream.of(list);
    }

    public boolean anyMatch(Predicate<? super T> predicate) {
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                return true;
            }
        }
        return false;
    }

    public boolean noneMatch(Predicate<? super T> predicate) {
        return !anyMatch(predicate);
    }
    
}
