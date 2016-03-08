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

import java.util.Iterator;

/**
 * Iterator that does not support remove, making it immutable.
 *
 * @param <E> the type of elements returned by this iterator
 * @since 1.2
 */
public abstract class ImmutableIterator<E> implements Iterator<E> {

    /**
     * Make an iterator immutable.
     *
     * @param iterator the iterator
     * @param <E>      the type the iterator returns
     * @return an immutable iterator
     */
    public static <E> ImmutableIterator<E> makeImmutable(final Iterator<E> iterator) {
        return new ImmutableIterator<E>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }
        };
    }

    /**
     * @throws java.lang.UnsupportedOperationException
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Iterator is immutable");
    }

}
