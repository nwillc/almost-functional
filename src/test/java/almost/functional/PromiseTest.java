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

import static org.fest.assertions.api.Assertions.assertThat;

public class PromiseTest {
	@Test
	public void shouldInformFullfilled() throws Exception {
		Promise<Boolean> promise = new Promise<Boolean>(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				return true;
			}
		});

		Variable<Boolean> value = new Variable<Boolean>(false);
		promise.fulfilled(value);
		assertThat(value.get()).isFalse();
		promise.run();
		assertThat(value.get()).isNotNull();
		assertThat(value.get()).isTrue();
	}

	@Test
	public void shouldInformRejected() throws Exception {
		Promise<Boolean> promise = new Promise<Boolean>(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				throw new RuntimeException();
			}
		});

		Variable<Exception> value = new Variable<Exception>();
		promise.rejected(value);
		assertThat(value.get()).isNull();
		promise.run();
		assertThat(value.get()).isNotNull();
		assertThat(value.get()).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void shouldInformSettledWhenFullfiled() throws Exception {
		Promise<Boolean> promise = new Promise<Boolean>(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				return true;
			}
		});

		Variable<Optional<Boolean>> value = new Variable<Optional<Boolean>>();
		promise.settled(value);
		assertThat(value.get()).isNull();
		promise.run();
		assertThat(value.get()).isNotNull();
		assertThat(value.get().isPresent()).isTrue();
		assertThat(value.get().get()).isTrue();
	}

	@Test
	public void shouldInformSettledWhenRejected() throws Exception {
		Promise<Boolean> promise = new Promise<Boolean>(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				throw new RuntimeException();
			}
		});

		Variable<Optional<Boolean>> value = new Variable<Optional<Boolean>>();
		promise.settled(value);
		assertThat(value.get()).isNull();
		promise.run();
		assertThat(value.get()).isNotNull();
		assertThat(value.get().isPresent()).isFalse();
	}

	@Test
	public void shouldSetStateFullfilled() throws Exception {
		Promise<Boolean> promise = new Promise<Boolean>(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				return true;
			}
		});

		assertThat(promise.getState()).isEqualTo(Promise.State.CREATED);
		promise.run();
		assertThat(promise.getState()).isEqualTo(Promise.State.FULFILLED);
	}

	@Test
	public void shouldSetStateRejected() throws Exception {
		Promise<Boolean> promise = new Promise<Boolean>(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				throw new RuntimeException();
			}
		});

		assertThat(promise.getState()).isEqualTo(Promise.State.CREATED);
		promise.run();
		assertThat(promise.getState()).isEqualTo(Promise.State.REJECTED);
	}
}
