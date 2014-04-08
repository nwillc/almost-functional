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

import java.util.NoSuchElementException;

import static almost.functional.utils.Preconditions.checkNotNull;

/**
 * A container object which may or may not contain a non-null value. If a value is present,
 * isPresent() will return true and get() will return the value.
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

	/**
	 * Returns an empty Optional instance. No value is present for this Optional.
	 * @param <T> Type of the non-existent value
	 * @return an empty Optional
	 */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> empty() {
        return  (Optional<T>) EMPTY;
    }

	/**
	 * Returns an Optional with the specified present non-null value.
	 * @param value the value to be present, which must be non-null
	 * @param <T> the class of the value
	 * @return an Optional with the value present
	 */
    public static <T> Optional<T> of(T value) {
        checkNotNull(value, "Optional value may not be null in method of");
        return new Optional<T>(value);
    }

    /**
     * Returns an Optional of a value which might be null.
     * @param value the value
     * @param <T> the type of the optional
     * @return an Optional of the value, or empty if value was null
     */
    public static <T> Optional<T> ofNullable(T value) {
        if (value == null) {
            return Optional.empty();
        }

        return of(value);
    }
	/**
	 * If a value is present in this Optional, returns the value, otherwise throws NoSuchElementException.
	 * @return the non-null value held by this Optional
	 * @throws NoSuchElementException
	 */
    public T get() throws NoSuchElementException {
        if (!isPresent()) {
            throw new NoSuchElementException("Attempting to get an empty Optional");
        }
        return optional;
    }

	/**
	 * Return true if there is a value present, otherwise false.
	 * @return true if there is a value present, otherwise false.
	 */
    public boolean isPresent() {
        return optional != null;
    }

	/**
	 * If a value is present, invoke the specified consumer with the value, otherwise do nothing.
	 * @param consumer consumer to be invoked if present.
	 */
	public void ifPresent(Consumer<? super T> consumer){
		if (isPresent()) {
			consumer.accept(get());
		}
	}

	/**
	 * Return the value if present, otherwise return other.
	 * @param other the value to be returned if there is no value present, may be null.
	 * @return the value, if present, otherwise other
	 */
    public T orElse(T other) {
        if (isPresent()) {
            return get();
        }

        return other;
    }

	/**
	 * If a value is present, map is with function, and if the result is non-null,
	 * return an Optional describing the result. Otherwise return an empty Optional.
	 * @param function  a map function to apply to the value, if present
	 * @param <V>  The type of the result of the mapping function
	 * @return an Optional describing the result of map, if a value is present, otherwise an empty Optional
	 */
	public <V> Optional<V> map(Function<T, V> function){
		checkNotNull(function, "Must provide non null function to map");
		if (isPresent()) {
			V v = function.apply(get());
			return v == null ? Optional.<V>empty() : Optional.of(function.apply(get()));
		} else {
			return Optional.empty();
		}
	}
}
