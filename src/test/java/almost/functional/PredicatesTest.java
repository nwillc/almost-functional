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

import org.junit.Test;

import static almost.functional.utils.ArrayIterable.newIterable;
import static org.fest.assertions.api.Assertions.assertThat;

public class PredicatesTest {
	@Test
	public void shouldBeEqual() throws Exception {
		Predicate<String> equals = Predicates.isEqual("some value");
		assertThat(equals.test("some value")).isTrue();
	}

	@Test
	public void shouldNotBeEqual() throws Exception {
		Predicate<Integer> equals = Predicates.isEqual(5);
		assertThat(equals.test(4)).isFalse();
	}

	@Test
	public void shouldContain() throws Exception {
		Iterable<String> strings = newIterable("one", "two", "three");
		Predicate<String> predicate = Predicates.contains(strings);
		assertThat(predicate.test("one")).isTrue();
	}

	@Test
	public void shouldNotContain() throws Exception {
		Iterable<String> strings = newIterable("one", "two", "three");
		Predicate<String> predicate = Predicates.contains(strings);
		assertThat(predicate.test("four")).isFalse();
	}
}
