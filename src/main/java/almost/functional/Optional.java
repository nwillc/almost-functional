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

package almost.functional;

import java.util.NoSuchElementException;

import static almost.functional.utils.Preconditions.checkNotNull;

/**
 * An Optional implementation. Will be deprecated by 1.8
 */
public final class Optional<T> {
    private static final Optional EMPTY = new Optional();
    private final T optional;

    /**
     * Instantiate an Optional of a given non-null value.
     *
     * @param optional the value.
     */
    private Optional(T optional) {
		checkNotNull(optional, "Optionals may not contain null value");
        this.optional = optional;
    }

    /**
     * Instantiate an empty Optional.
     */
    private Optional() {
        optional = null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> empty() {
        return EMPTY;
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    public T get() throws NoSuchElementException {
        if (!isPresent()) {
            throw new NoSuchElementException("Attempting to get an empty Optional");
        }
        return optional;
    }

    public boolean isPresent() {
        return optional != null;
    }

	public void ifPresent(Consumer<? super T> consumer){
		if (isPresent()) {
			consumer.accept(get());
		}
	}

    public T orElse(T other) {
        if (isPresent()) {
            return get();
        }

        return other;
    }

	public <V> Optional<V> transform(Function<T,V> function){
		if (isPresent()) {
			return Optional.of(function.apply(get()));
		} else {
			return Optional.empty();
		}
	}
}
