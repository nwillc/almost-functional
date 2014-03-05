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

package almost.functional;


import almost.functional.utils.Iterables;

/**
 * Utility operations on predicates.
 */
public class Predicates {
	private Predicates(){}

	/**
	 * Returns a predicate that tests if two arguments are equal according to Objects.equals(Object, Object).
	 * @param targetRef the object reference with which to compare for equality, which may be null
	 * @param <T>  the type of arguments to the predicate
	 * @return a predicate that tests if two arguments are equal according to Objects.equals(Object, Object)
	 */
	public static <T> Predicate<T> isEqual(final T targetRef) {
		return new Predicate<T>() {
			@Override
			public boolean test(T t) {
				return t.equals(targetRef);
			}
		};
	}

	/**
	 * Returns a predicate that that tests if an iterable contains an argument.
	 * @param iterable the iterable, may not be null
	 * @param <T> the type of the argument to the predicate
	 * @return a predicate that that tests if an iterable contains an argument
	 */
	public static <T> Predicate<T> contains(final Iterable<T> iterable) {
		return new Predicate<T>() {
			@Override
			public boolean test(T t) {
				return Iterables.contains(iterable, t);
			}
		};
	}
}