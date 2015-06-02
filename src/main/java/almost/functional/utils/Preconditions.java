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

/**
 * Utility operations designed to be used as precondition tests.
 */
public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Check that a reference is not null, throwing a IllegalArgumentException if it is.
     *
     * @param reference    The reference to check
     * @param errorMessage the message for the exception if the reference is null
     * @param <T>          the type of the reference
     * @throws java.lang.IllegalArgumentException if the reference is null
     */
    public static <T> T checkNotNull(final T reference, final String errorMessage) {
        if (reference == null) {
            throw new IllegalArgumentException(errorMessage);
        }
		return reference;
    }

	/**
	 * Check that a String is not empty after removing whitespace.
	 * @param reference the string
	 * @param errorMessage the message for the exception
	 * @throws java.lang.NullPointerException if the String is null
	 * @throws java.lang.IllegalArgumentException if the string is non-null but zero length
	 */
	public static String checkNonEmptyString(final String reference, final String errorMessage) {
        for (Character c : checkNotNull(reference, errorMessage).toCharArray()) {
            if (!Character.isWhitespace(c)) {
                return reference;
            }
        }
        throw new IllegalArgumentException(errorMessage);
	}

    /**
     * Check that one class is assignable to another.
     * @param fromValue the class assigning from
     * @param toValue the class assigning to
     * @param message the message used in the exception if thrown
     * @throws ClassCastException if the assignment can not be made
     */
    public static Class isAssignableTo(final Class<?> fromValue, final Class<?> toValue, final String message) {
        if (!toValue.isAssignableFrom(fromValue)) {
            throw new ClassCastException(message);
        }
		return fromValue;
    }
}
