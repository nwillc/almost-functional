package almost.functional.utils;

import almost.functional.BiFunction;
import almost.functional.Consumer;
import almost.functional.Function;
import almost.functional.Predicate;

/**
 * @since 1.8
 * @param <T>
 */
public class Stream<T> {
    private final Iterable<T> iterable;

    public static <R> Stream<R> of(Iterable<R> iterable) {
        return  new Stream<R>(iterable);
    }

    public static <R> Stream<R> of(R ... elements) {
        return new Stream<R>(new ArrayIterable<R>(elements));
    }

    private Stream(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    public void forEach(Consumer<? super T> action) {
        Iterables.forEach(iterable, action);
    }

    public <R> R reduce(final R initial, final BiFunction<R, ? super T, R> accumulator) {
        return Iterables.reduce(iterable, initial, accumulator);
    }

    public Stream<T> filter(Predicate<? super T> predicate) {
        return new Stream<T>(Iterables.filter(iterable,predicate));
    }

    public <R> Stream<R> map(Function<? super T,? extends R> mapper) {
        return new Stream<R>(Iterables.map(iterable, mapper));
    }

    public boolean anyMatch(Predicate<? super T> predicate) {
        return Iterables.any(iterable, predicate);
    }

    public boolean noneMatch(Predicate<? super T> predicate) {
        return !anyMatch(predicate);
    }
}
