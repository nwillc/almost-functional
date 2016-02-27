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


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Utility class for Iterators.
 */
public final class Iterators {
    private Iterators() {}

	/**
	 * This returns an Iterator limited to no more then the next count elements. The original
	 * iterator <b>is</b> advanced by the calls to next on the returned iterator.
	 * @param iterator the iterator to draw elements from.
	 * @param count the maximum number of elements to draw.
	 * @param <T>  the element type.
	 * @return An iterator.
	 * @since 1.9.3
	 */
	public static <T> Iterator<T> next(final Iterator<? extends T> iterator, final int count) {
        return new ImmutableIterator<T>() {
			private int position = 0;

			@Override
			public boolean hasNext() {
				return iterator.hasNext() && position < count;
			}

			@Override
			public T next() {
				T next = iterator.next();
				position++;
				return next;
			}
		};
    }

    /**
     * Create an iterator which sequentially iterates over a collection of iterators.
     * @param iterators the iterators to iterate
     * @param <T> the element type
     * @return the new iterator
     */
    public static  <T> Iterator<T> concat(final Iterator<? extends T> ... iterators) {
        return new ImmutableIterator<T>() {
            int current = 0;

            @Override
            public boolean hasNext() {
                advance();
                return current < iterators.length;
            }

            @Override
            public T next() {
                advance();
                try {
                    return iterators[current].next();
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            private void advance() {
                while (current < iterators.length && !iterators[current].hasNext()) {
                    current++;
                }
            }
        };
    }


}
