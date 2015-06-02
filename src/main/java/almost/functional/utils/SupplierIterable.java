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

import almost.functional.Optional;
import almost.functional.Supplier;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static almost.functional.utils.Preconditions.checkNotNull;

/**
 * Creates an immutable Iterable from any Supplier of Optionals. The resulting Iterator hasNext until the
 * Supplier returns an empty Optional.
 * @param <T> type of the Iterable and the Suppliers Optional
 */
public class SupplierIterable<T> implements Iterable<T> {
	private final Supplier<Optional<T>> supplier;

	/**
	 * Constructor accepting the Supplier. Supplier must return non null Optionals.
	 * @param supplier a Supplier of Optionals of type T
	 */
	public SupplierIterable(final Supplier<Optional<T>> supplier) {
		checkNotNull(supplier, "Supplier must not be null");
		this.supplier = supplier;
	}

	@Override
	public Iterator<T> iterator() {
		return new ImmutableIterator<T>() {
			private Optional<T> nextValue = null;

			@Override
			public boolean hasNext() {
                if (nextValue == null) {
                    nextValue = supplier.get();
                    checkNotNull(nextValue, "supplier returned null");
                }
                return nextValue.isPresent();
			}

			@Override
			public T next() {
				if (!hasNext()) {
					throw new NoSuchElementException("next invoked on iterator where hasNext is false.");
				}
				final T value = nextValue.get();
				nextValue = null; //NOPMD
				return value;
			}
		};
	}
}
