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

package almost.functional.utils;

/**
 * Utility operations designed to be used as precondition tests.
 */
public class Preconditions {
	private Preconditions() {}

	/**
	 * Check that a reference is not null, throwing a NullPointerException if it is.
	 * @param reference The reference to check
	 * @param errorMessage the message for the exception if the reference is null
	 * @param <T> the type of the reference
	 * @return the reference
	 * @throws  NullPointerException if the reference is null
	 */
	public static <T> T checkNotNull(T reference, String errorMessage) {
		if (reference == null) {
			throw new NullPointerException(errorMessage);
		}
		return reference;
	}
}
