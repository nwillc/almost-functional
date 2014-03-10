/*
 * Copyright (c) 2013-2014, nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or
 * without fee is hereby granted, provided that the above copyright notice and this permission
 * notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO
 * THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT
 * SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR
 * ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF
 * CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
 * OR PERFORMANCE OF THIS SOFTWARE.
 */

package almost.functional.utils;

import almost.functional.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static almost.functional.utils.Preconditions.checkNotNull;

/**
 * Utility operations on an iterable.
 */
public class Iterables {
	private Iterables() {}

	/**
	 * Accept a consumer for each element of an iterable.
	 * @param i the iterable
	 * @param c the consumer to accept for each element
	 * @param <T> the type of the iterable and consumer
	 */
	public static <T> void forEach(Iterable<T> i, final Consumer<T> c) {
		checkNotNull(i, "forEach must have a valid iterable");
		checkNotNull(c, "forEach must have a valid consumer");

		for (T t : i) {
			c.accept(t);
		}
	}

	/**
	 * Apply a function to an iterable, returning the final result.
	 * @param i the iterable
	 * @param f the function to apply
	 * @param <T> the type of the iterable and input to the function
	 * @param <R> the type of the function result
	 * @return the final result of the function after being applied to the elements
	 */
	public static <T,R> R forEach(Iterable<T> i, final Function<T,R> f) {
		checkNotNull(i, "forEach must have a valid iterable");
		checkNotNull(f, "forEach must have a valid function");
		R ret = null;

		for (T t : i) {
			ret = f.apply(t);
		}
		checkNotNull(ret, "Function can not return null");
		return ret;
	}

	/**
	 * Apply a predicate to an iterable, returning an optional of the first element where the predicate is true,
	 * or empty if no true is found.
	 * @param iterable the iterable to traverse
	 * @param predicate the predicate to test
	 * @param <T> they type of the iterable and predicate
	 * @return an optional of the first element where the predicate is true, or empty if no true is found.
	 */
	public static <T> Optional<T> find(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
		checkNotNull(iterable, "iterable may not be null");
		checkNotNull(predicate, "the predicate may not be null");
		for (T t : iterable) {
			if (predicate.test(t)) {
				return Optional.of(t);
			}
		}
		return Optional.empty();
	}

	/**
	 * Apply a predicate to an iterable, returning the first element where the predicate is true,
	 * or the default value if no true is found.
	 * @param iterable the iterable to traverse
	 * @param predicate the predicate to test
	 * @param defaultValue the default value should a value not be found
	 * @param <T> they type of the iterable, predicate and deault value
	 * @return the first element where the predicate is true, or the default value if no true is found.
	 */
	public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, T defaultValue) {
		return find(iterable, predicate).orElse(defaultValue);
	}

	/**
	 * Determine if any element of an iterable matches a given predicate.
	 * @param iterable the iterable to traverse
	 * @param predicate the predicate to test
	 * @param <T> the type of the iterable and predicate
	 * @return true if any element matches the predicate, otherwise false.
	 */
	public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
		return find(iterable, predicate).isPresent();
	}

	/**
	 * Does an iterable contain a value as determined by Object.isEqual(Object, Object).
	 * @param iterable the iterable
	 * @param value the value
	 * @param <T> the type of the iterable and object
	 * @return true if iterable contain a value as determined by Object.isEqual(Object, Object).
	 */
	public static <T> boolean contains(Iterable<T> iterable, T value) {
		return any(iterable, Predicates.isEqual(value));
	}

	/**
	 * Create an iterable that maps an existing iterable to a new one by applying a function to each of it's elements.
	 * @param fromIterable the iterable to be transformed
	 * @param function the function to apply to the elements
	 * @param <F> the type of the original elements, and the argument to the function
	 * @param <T> the type of the resulting iterable, and the result of the function
	 * @return an iterable that transforms an existing iterable by applying a function to each or it's elements.
	 */
	public static <F,T> Iterable<T> map(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
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

    public static <T> Iterable<T> filter(final Iterable<T> fromIterable, final Predicate<? super T> predicate) {
        checkNotNull(fromIterable, "iterable must be non null");
        checkNotNull(predicate, "predicate must be non null");
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                final Iterator<T> fromIterator = fromIterable.iterator();
                return new ImmutableIterator<T>() {
                    Optional<T> next = advance();

                    @Override
                    public boolean hasNext() {
                        return next.isPresent();
                    }

                    @Override
                    public T next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        T value = next.get();
                        next = advance();
                        return value;
                    }

                    private Optional<T> advance(){
                        while (fromIterator.hasNext()) {
                            T value = fromIterator.next();
                            if (predicate.test(value)) {
                                return Optional.of(value);
                            }
                        }
                     return Optional.empty();
                    }

                };
            }
        };
    }
}
