/*
 * Copyright (c) 2016, nwillc@gmail.com
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

import java.util.Enumeration;
import java.util.Iterator;

import static almost.functional.Optional.of;
import static almost.functional.utils.Preconditions.checkNotNull;

/**
 * Utility operations on an iterable.
 */
public final class Iterables {

    private Iterables() {
    }

    /**
     * Accept a consumer for each element of an iterable.
     *
     * @param consumer the consumer to accept for each element
     * @param <T>      the type of the iterable and consumer
     * @param iterable the iterable
     */
    public static <T> void forEach(final Iterable<T> iterable, final Consumer<? super T> consumer) {
        checkNotNull(iterable, "forEach must have a valid iterable");
        checkNotNull(consumer, "forEach must have a valid consumer");

        for (T t : iterable) {
            consumer.accept(t);
        }
    }

    /**
     * Perform a reduction of an iterable, using an initial value, and a two argument function. The function
     * is applied to each element using the last result as the first argument and the element as the second.
     *
     * @param <T>         type of the iterable elements and accumulator's second argument
     * @param <R>         type returned by reduce, the accumulator and it's first argument
     * @param iterable    the iterable.
     * @param initial     the initial value of the first argument.
     * @param accumulator the two argument function.
     * @return the final result of the function.
     * @since 1.5
     */
    public static <T, R> R reduce(final Iterable<T> iterable, final R initial, final BiFunction<R, ? super T, R> accumulator) {
        checkNotNull(iterable, "reduce must have a non null iterable");
        checkNotNull(accumulator, "reduce must have a non null function");

        R returnValue = initial;
        for (T r : iterable) {
            returnValue = accumulator.apply(returnValue, r);
        }
        return returnValue;
    }

    /**
     * Apply a predicate to an iterable, returning an optional of the first element where the predicate is true,
     * or empty if no true is found.
     *
     * @param iterable  the iterable to traverse
     * @param predicate the predicate to test
     * @param <T>       they type of the iterable and predicate
     * @return an optional of the first element where the predicate is true, or empty if no true is found.
     */
    public static <T> Optional<T> find(final Iterable<? extends T> iterable, final Predicate<? super T> predicate) {
        checkNotNull(iterable, "iterable may not be null");
        checkNotNull(predicate, "the predicate may not be null");
        for (T t : iterable) {
            if (predicate.test(t)) {
                return of(t);
            }
        }
        return Optional.empty();
    }

    /**
     * Determine if any element of an iterable matches a given predicate.
     *
     * @param iterable  the iterable to traverse
     * @param predicate the predicate to test
     * @param <T>       the type of the iterable and predicate
     * @return true if any element matches the predicate, otherwise false.
     */
    public static <T> boolean any(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return find(iterable, predicate).isPresent();
    }

    /**
     * Does an iterable contain a value as determined by Object.isEqual(Object, Object).
     *
     * @param iterable the iterable
     * @param value    the value
     * @param <T>      the type of the iterable and object
     * @return true if iterable contain a value as determined by Object.isEqual(Object, Object).
     */
    public static <T> boolean contains(final Iterable<T> iterable, final T value) {
        return any(iterable, Predicates.isEqual(value));
    }

    /**
     * Create an iterable that maps an existing iterable to a new one by applying a function to each of it's elements.
     *
     * @param fromIterable the iterable to be transformed
     * @param function     the function to apply to the elements
     * @param <F>          the type of the original elements, and the argument to the function
     * @param <T>          the type of the resulting iterable, and the result of the function
     * @return an iterable that transforms an existing iterable by applying a function to each or it's elements.
     */
    public static <F, T> Iterable<T> map(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
        checkNotNull(fromIterable, "iterable must be non null");
        checkNotNull(function, "map function must be non null");
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                final Iterator<F> fromIterator = fromIterable.iterator();
                return new ImmutableIterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return fromIterator.hasNext();
                    }

                    @Override
                    public T next() {
                        return function.apply(fromIterator.next());
                    }
                };
            }
        };
    }

    /**
     * Create an iterable that filters an existing iterable based on a predicate.
     *
     * @param fromIterable Iterable being filtered
     * @param predicate    the predicate to base inclusion upon, true cases are included, false excluded
     * @param <T>          the type of the elements
     * @return iterable of filtered elements
     */
    public static <T> Iterable<T> filter(final Iterable<T> fromIterable, final Predicate<? super T> predicate) {
        checkNotNull(fromIterable, "iterable must be non null");
        checkNotNull(predicate, "predicate must be non null");
        return new SupplierIterable<T>(new Supplier<Optional<T>>() {
            private final Iterator<T> fromIterator = fromIterable.iterator();

            public Optional<T> get() {
                while (fromIterator.hasNext()) {
                    final T value = fromIterator.next();
                    if (predicate.test(value)) {
                        return of(value);
                    }
                }
                return Optional.empty();
            }
        });
    }

    /**
     * Convert an enumeration to an iterator.
     *
     * @param enumeration the enumeration to make into an Iterator
     * @param <E>         type of the enumeration
     * @return An Iterable
     * @throws java.lang.NullPointerException if enumeration is null
     */
    public static <E> Iterable<E> iterable(final Enumeration<E> enumeration) {
        checkNotNull(enumeration, "Can not create an Iterable from a null enumeration");
        return new Iterable<E>() {
            public Iterator<E> iterator() {
                return new ImmutableIterator<E>() {
                    public boolean hasNext() {
                        return enumeration.hasMoreElements();
                    }

                    public E next() {
                        return enumeration.nextElement();
                    }
                };
            }
        };
    }

    /**
     * Return an optional of an element from a specified position in an iterable. If the position is out of bounds
     * an empty optional is returned. Positions start at 0.
     *
     * @param iterable the iterable
     * @param position the position
     * @param <E>      the type of the elements
     * @return an optional from the given position, or empty if out of bounds
     * @since 1.7
     */
    public static <E> Optional<E> get(final Iterable<E> iterable, final int position) {
        checkNotNull(iterable, "Get requires an iterable");
        if (position < 0) {
            return Optional.empty();
        }

        int iterablePosition = 0;
        for (E anIterable : iterable) {
            if (iterablePosition == position) {
                return of(anIterable);
            }
            iterablePosition++;
        }

        return Optional.empty();
    }

    /**
     * Return the last element of an iterable, or empty if the iterable is empty.
     *
     * @param iterable the iterable
     * @param <E>      type of the iterable elements
     * @return Optional of the last element, or empty if nothing in the iterable
     */
    public static <E> Optional<E> last(final Iterable<E> iterable) {
        checkNotNull(iterable, "last requires a non null iterable");
        final Iterator<E> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }

        E lastElement;
        do {
            lastElement = iterator.next();
        } while (iterator.hasNext());

        return Optional.of(lastElement);
    }
}
