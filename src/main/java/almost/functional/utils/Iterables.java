/*
 * Copyright (c) 2014, nwillc@gmail.com
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

import static almost.functional.utils.Preconditions.checkNotNull;

public class Iterables {
	private Iterables() {}

	public static <T> void forEach(Iterable<T> i, final Consumer<T> c) {
		checkNotNull(i, "forEach must have a valid iterable");
		checkNotNull(c, "forEach must have a valid consumer");

		for (T t : i) {
			c.accept(t);
		}
	}

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

	public static <T> Optional<T> find(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
		for (T t : iterable) {
			if (predicate.test(t)) {
				return Optional.of(t);
			}
		}
		return Optional.empty();
	}

	public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, T defaultValue) {
		return find(iterable, predicate).orElse(defaultValue);
	}

	public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
		return find(iterable, predicate).isPresent();
	}

	public static <T> boolean contains(Iterable<T> iterable, T value) {
		return any(iterable, Predicates.isEqual(value));
	}

	public static <F,T> Iterable<T>	transform(final Iterable<F> fromIterable, final Function<? super F,? extends T> function) {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				final Iterator<F> fromIterator = fromIterable.iterator();
				return new Iterator<T>() {
					@Override
					public boolean hasNext() {
						return fromIterator.hasNext();
					}

					@Override
					public T next() {
						return function.apply(fromIterator.next());
					}

					@Override
					public void remove() {
					  throw new NoSuchMethodError("transform does not implement remove");
					}
				};
			}
		};
	}
}
