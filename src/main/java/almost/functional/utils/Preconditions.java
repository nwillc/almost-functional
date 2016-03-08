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

import almost.functional.Predicate;
import almost.functional.Predicates;

import java.lang.reflect.Constructor;

/**
 * Utility operations designed to be used as precondition tests.
 */
public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Test a Predicate against a reference value, and if it fails throw a runtime exception with a message.
     *
     * @param reference the value to test
     * @param predicate the predicate test to invoke
     * @param ex        The RuntimeException to use
     * @param msg       The message to add to the exception
     * @param <T>       The type of the reference value
     * @return The reference value
     * @throws RuntimeException An instance of ex if the predicate fails
     */
    public static <T> T precondition(T reference, Predicate<T> predicate,
                                     Class<? extends RuntimeException> ex, String msg)
            throws RuntimeException {
        if (predicate.test(reference)) {
            return reference;
        }

        final Constructor<? extends RuntimeException> constructor;
        try {
            constructor = ex.getConstructor(String.class);
            throw constructor.newInstance(msg);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(msg);
        }
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
        return precondition(reference, Predicates.<T>notNull(),
                IllegalArgumentException.class, errorMessage);
    }

    /**
     * Check that a String is not empty after removing whitespace.
     *
     * @param reference    the string
     * @param errorMessage the message for the exception
     * @throws java.lang.NullPointerException     if the String is null
     * @throws java.lang.IllegalArgumentException if the string is non-null but zero length
     */
    public static String checkNonEmptyString(final String reference, final String errorMessage) {
        return precondition(reference, Predicates.notEmptyString(),
                IllegalArgumentException.class, errorMessage);
    }

    /**
     * Check that one class is assignable to another.
     *
     * @param reference the class to test
     * @param toValue   the class assigning to
     * @param message   the message used in the exception if thrown
     * @throws ClassCastException if the assignment can not be made
     */
    public static Class isAssignableTo(final Class<?> reference, final Class<?> toValue, final String message) {
        return precondition(reference, new Predicate<Class>() {
            @Override
            public boolean test(Class testValue) {
                return toValue.isAssignableFrom(testValue);
            }
        }, ClassCastException.class, message);
    }
}
