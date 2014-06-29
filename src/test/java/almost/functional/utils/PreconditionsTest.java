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

package almost.functional.utils;

import com.github.nwillc.contracts.PrivateConstructorContract;
import org.junit.Test;

import static almost.functional.utils.Preconditions.*;
import static org.fest.assertions.Assertions.assertThat;

public class PreconditionsTest extends PrivateConstructorContract {

    @Override
    protected Class<?> getUtilityClass() {
        return Preconditions.class;
    }

    @Test
    public void shouldPassNotNull() throws Exception {
        checkNotNull(Boolean.TRUE, "Testing preconditions");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailNotNull() throws Exception {
        checkNotNull(null, "Testing preconditions");
    }

    @Test
    public void shouldPassIsAssignableTo() throws Exception {
        isAssignableTo(NullPointerException.class, Exception.class, "Should be assignable");
    }

    @Test(expected = ClassCastException.class)
    public void shouldFailIsAssignableTo() throws Exception {
        isAssignableTo(NullPointerException.class, Integer.class, "Should not be assignable");
    }

    @Test
    public void shouldPassNonEmptyString() throws Exception {
        checkNonEmptyString(" hello world", "Shouldn't fail");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testName() throws Exception {
        checkNonEmptyString(" ", "Should fail");
    }

	@Test
	public void shouldReturnValue() throws Exception {
		final String str = "hello";
		assertThat(checkNonEmptyString(checkNotNull(str, "null"), "empty")).isEqualTo(str);
	}
}
