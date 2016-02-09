/*
 * Copyright (c) 2015, nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */

package almost.functional.utils;

import almost.functional.*;
import almost.functional.Optional;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A sequence of elements supporting sequential aggregate operations.
 * @since 1.8
 * @param <T>
 */
public class Stream<T> implements Closeable {
    private static final Logger LOGGER = LogFactory.getLogger();
    private final Iterator<T> iterator;
    private final Set<Runnable> closeHandlers = new HashSet<Runnable>();

    /**
     * Create a new stream of the elements that the iterable.iterator() will yield
     * @param iterable the source of the iterator
     * @param <R> the type of the resultant stream's elements
     * @return a new stream
     */
    public static <R> Stream<R> of(Iterable<R> iterable) {
        return new Stream<R>(iterable.iterator());
    }

    /**
     * Create a stream of the elements provided.
     * @param elements elements of the new stream
     * @param <R> type of elements
     * @return a new stream
     */
    public static <R> Stream<R> of(R ... elements) {
        return new Stream<R>(new ArrayIterable.ArrayIterator<R>(elements));
    }

    /**
     * Create a stream based on an iterator.
     * @param iterator the iterator
     * @param <R> type of elements
     * @return a new stream
     * @since 1.9.1
     */
    public static <R> Stream<R> of(Iterator<R> iterator) {
        return new Stream<R>(iterator);
    }

    private Stream(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    /**
     * Performs an action for each element of this stream.
     * @param action an action to perform on the elements
     */
    public void forEach(Consumer<? super T> action) {
        while (iterator.hasNext()) {
            action.accept(iterator.next());
        }
    }

    /**
     * Performs a reduction on the elements of the stream, using an accumulation function, and returns an Optional describing the reduced value, if any.
     * @param accumulator an associative function for combining two values
     * @return value of the reduction if any
     */
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

    /**
     * Performs a reduction on the elements of this stream, using the provided initial value and accumulation functions.
     * @param initial the initial value
     * @param accumulator the acummulation function
     * @param <R> return type
     * @return the result of the reduction
     */
    public <R> R reduce(final R initial, final BiFunction<R, ? super T, R> accumulator) {
        R returnValue = initial;
        while (iterator.hasNext()) {
            returnValue = accumulator.apply(returnValue, iterator.next());
        }
        return returnValue;
    }

    /**
     * Returns a stream consisting of the elements of this stream that match the given predicate.
     * @param predicate to apply to each element to determine if it should be included
     * @return the filtered stream
     */
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

    /**
     * Returns a stream consisting of the results of applying the given function to the elements of this stream.
     * @param mapper function to apply to each element
     * @param <R> The element type of the new stream
     * @return the new stream
     */
    public <R> Stream<R> map(Function<? super T,? extends R> mapper) {
        List<R> list = new ArrayList<R>();
        while (iterator.hasNext()) {
            list.add(mapper.apply(iterator.next()));
        }
        return Stream.of(list);
    }

    /**
     * Returns whether any elements of this stream match the provided predicate. If the stream is empty then false is
     * returned and the predicate is not evaluated.
     * @param predicate to apply to elements of this stream
     * @return true if any elements of the stream match the provided predicate
     */
    public boolean anyMatch(Predicate<? super T> predicate) {
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether no elements of this stream match the provided predicate.  If the stream is empty then true is
     * returned and the predicate is not evaluated.
     * @param predicate to apply to elements of this stream
     * @return true if no elements of the stream match the provided predicate
     */
    public boolean noneMatch(Predicate<? super T> predicate) {
        return !anyMatch(predicate);
    }

    /**
     *Returns whether all elements of this stream match the provided predicate.
     * If the stream is empty then true is returned and the predicate is not evaluated.
     * @param predicate to apply to elements of this stream
     * @return true if all match the provided predicate
     */
    public boolean allMatch(Predicate<? super T> predicate) {
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * The element count in the stream.
     * @return the count
     */
    public long count() {
        long count = 0L;

        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        return count;
    }

    /**
     * Creates a concatenated stream whose elements are all the elements of the first stream followed by all the elements of the second stream.
     * @param a first stream
     * @param b second stream
     * @param <T> type of elements
     * @return a stream concatenating first and second
     */
    @SuppressWarnings("unchecked")
    public static <T> Stream<T> concat(Stream<? extends T> a,
                                Stream<? extends T> b) {
        return new Stream(Iterators.concat(a.iterator, b.iterator));
    }

    public Stream<T> onClose(Runnable closeHandler) {
        closeHandlers.add(closeHandler);
        return this;
    }

    @Override
    public void close() throws IOException {
        for (Runnable runnable : closeHandlers) {
            try {
                runnable.run();
            } catch (Exception e) {
              LOGGER.log(Level.WARNING, "Exception on close", e);
            }
        }
    }
}
