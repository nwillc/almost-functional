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

import almost.functional.Consumer;
import almost.functional.Function;
import org.junit.Test;

import static almost.functional.utils.ArrayIterable.newIterable;
import static almost.functional.utils.Iterables.forEach;
import static org.fest.assertions.api.Assertions.assertThat;


public class IterablesTest {

	private String[] strings = {"a", "b", "c"};

	@Test
	public void shouldForEachConsumer() throws Exception {
		final StringBuffer stringBuffer = new StringBuffer();
		forEach(newIterable(strings), new Consumer<String>() {
			@Override
			public void accept(String s) {
				stringBuffer.append(s);
			}
		});
		assertThat(stringBuffer.toString()).isEqualTo("abc");
	}

	@Test
	public void shouldForEachFunction() throws Exception {
		Function<String,String> function = new Function<String, String>() {
			private StringBuffer stringBuffer = new StringBuffer();
			@Override
			public String apply(String s) {
				stringBuffer.append(s);
				return stringBuffer.toString();
			}
		};
		assertThat(forEach(newIterable(strings),function)).isEqualTo("abc");
	}
}
