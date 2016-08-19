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

/**
 *  Utilities for accessing Runtime info.
 *  @since 1.9.6
 */
public final class RuntimeUtils {

    public static final int PARENT_OF_CALLER = 4;

    private RuntimeUtils() {}

    /**
     * Get the class name of a caller in the current stack.
     * @param depth in the call stack
     * @return class name
     */
    public static String getCallerClassName(int depth) {
        return Thread.currentThread().getStackTrace()[depth].getClassName();
    }

    /**
     * Get the name of the class calling the method.
     * @return class name
     */
    public static String getCallerClassName() {
        return getCallerClassName(PARENT_OF_CALLER);
    }

    /**
     * Get the method name of callers in current stack.
     * @param depth  in the call stack
     * @return  method name
     */
    public static String getCallerMethodName(int depth) {
        return Thread.currentThread().getStackTrace()[depth].getMethodName();
    }

    /**
     * Get the name of the current method.
     * @return  method Name
     */
    public static String getCurrentMethodName() {
        return getCallerMethodName(PARENT_OF_CALLER - 1);
    }
}
