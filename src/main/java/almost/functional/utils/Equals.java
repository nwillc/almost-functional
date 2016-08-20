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

import almost.functional.Function;
import almost.functional.Predicate;

import java.util.Objects;

/**
 * @since  1.9.6
 */
public final class Equals {
    private Equals() {}

	/**
     * Compare equality of two objects using a series of accessors. The accessors are used to access
     * values from the objects, values which are then compared for equality.
     * @param one first object to compare
     * @param two second object to compare
     * @param accessors functions used to access values from the objects
     * @param <T>  type to compare as
     * @return  true if both are null or all accessors values are equal
     */
    public static <T> boolean equals(final T one, final T two, Function<? super T, ?>... accessors) {
        return one == two ||
                !(one == null || two == null) &&
                        Stream.of(accessors).allMatch(new Predicate<Function<? super T, ?>>() {
                            @Override
                            public boolean test(Function<? super T, ?> accessor) {
                                return Objects.equals(accessor.apply(one), accessor.apply(two));
                            }
                        });
    }
}
