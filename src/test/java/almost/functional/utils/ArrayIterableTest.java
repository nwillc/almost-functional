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
 */

package almost.functional.utils;

import com.github.nwillc.contracts.ImmutableIteratorContract;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static almost.functional.utils.ArrayIterable.newIterable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

public class ArrayIterableTest extends ImmutableIteratorContract {

    @Override
    protected Iterator getNonEmptyIterator() {
        return newIterable("a").iterator();
    }

    @Test
	public void shouldHandleNullArray() throws Exception {
		Iterable<String> strings = new ArrayIterable<String>();
     	assertNotNull(strings);
		Iterator<String> iter = strings.iterator();
		assertNotNull(iter);
		assertThat(iter.hasNext()).isFalse();
	}

	@Test
	public void shouldIterateAll() throws Exception {
		String[] stringArray = new String[]{"a", "b", "c"};

		int length = 0;
		for (String s : newIterable(stringArray)) {
			assertThat(Arrays.binarySearch(stringArray, s)).isGreaterThan(-1);
			length++;
		}
		assertThat(length).isEqualTo(stringArray.length);
	}
}
