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

import java.util.NoSuchElementException;

import static org.fest.assertions.api.Assertions.assertThat;

public class OptionalTest {

	@Test(expected = NoSuchElementException.class)
	public void shouldBeEmplty() throws Exception {
		Optional<String> optional = Optional.empty();

		assertThat(optional.isPresent()).isFalse();
		optional.get();
	}

	@Test
	public void shouldOf() throws Exception {
		Optional<Integer> optional = Optional.of(5);

		assertThat(optional.isPresent()).isTrue();
		assertThat(optional.get()).isEqualTo(5);
	}

	@Test
	public void shouldIfPresent() throws Exception {
		Optional<Integer> optional = Optional.of(5);
		Variable<Integer> variable = new Variable<Integer>();

		variable.accept(10);
		assertThat(variable.get()).isEqualTo(10);
		optional.ifPresent(variable);
		assertThat(optional.get()).isEqualTo(5);
		assertThat(variable.get()).isEqualTo(optional.get());
	}

	@Test
	public void shouldOrElseEmpty() throws Exception {
		Optional<Integer> optional = Optional.empty();

		assertThat(optional.orElse(10)).isEqualTo(10);
		assertThat(optional.orElse(null)).isNull();
	}

	@Test
	public void shouldOrElseOf() throws Exception {
		Optional<Integer> optional = Optional.of(5);

		assertThat(optional.orElse(10)).isEqualTo(optional.get());
	}

	@Test
	public void shouldTransformEmpty() throws Exception {
		Optional<Integer> optional = Optional.empty();

		assertThat(optional.transform(new Increment()).isPresent()).isFalse();
	}

	@Test
	public void shouldTransformOf() throws Exception {
		Optional<Integer> optional = Optional.of(0);

		Optional<Integer> returned = optional.transform(new Increment());
		assertThat(returned.isPresent()).isTrue();
		assertThat(returned.get()).isEqualTo(1);
	}

	@Test(expected = NullPointerException.class)
	public void shouldTransformNullFunction() throws Exception {
		Optional<Integer> optional = Optional.of(0);
		optional.transform(null);
	}

	private class Variable<T> implements Consumer<T>, Supplier<T> {
		private T t;

		@Override
		public void accept(T t) {
			this.t = t;
		}

		@Override
		public T get() {
			return t;
		}
	}

	private class Increment implements Function<Integer,Integer> {
		@Override
		public Integer apply(Integer integer) {
			return integer + 1;
		}
	}
}
