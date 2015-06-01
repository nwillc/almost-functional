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
 */

package almost.functional.utils;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterable over an array.
 * @param <T> the type of the array elements.
 */
public class ArrayIterable<T> implements Iterable<T> {
	private final T[] data;

	/**
	 * Basic constructor.
	 * @param data array of type T
	 */
	public ArrayIterable(final T ... data) {
		this.data = data;
	}

	/**
	 * Factory function to create an iterable over an array of T.
	 * @param data the array of type T
	 * @param <T>  the type of the elements in the array
	 * @return and ArrayIterable of type T
	 */
    public static <T> ArrayIterable<T> newIterable(final T ... data) {
        return new ArrayIterable<T>(data);
    }

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator<T>(data);    //NOPMD
	}

	static public class ArrayIterator<D> extends ImmutableIterator<D> {
		private final D[] data;
		private int index;

		public ArrayIterator(final D ... data) {   //NOPMD
			super();
			this.data = data;
			index = 0;
		}

		@Override
		public boolean hasNext() {
			return data != null && index < data.length;
		}

		@Override
		public D next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			final D next = data[index];
			index++;
			return next;
		}
	}


}
