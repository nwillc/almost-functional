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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RuntimeUtilsTest {

    @Test
    public void testGetCallerClassName() throws Exception {
        assertThat(RuntimeUtils.getCallerClassName()).isNotNull();
    }

    @Test
    public void getCallerClassName() throws Exception {
        assertThat(RuntimeUtils.getCallerClassName(2)).isEqualTo(getClass().getName());
    }

    @Test
    public void testGetMethodName() throws Exception {
        assertThat(RuntimeUtils.getCurrentMethodName()).isEqualTo("testGetMethodName");

    }
}