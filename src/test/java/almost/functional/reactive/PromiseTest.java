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

package almost.functional.reactive;

import almost.functional.Supplier;
import almost.functional.reactive.Observer;
import almost.functional.reactive.Promise;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class PromiseTest {
	@Test
	public void shouldObserveNext() throws Exception {
	    Supplier<Boolean> supplier = new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return true;
            }
        };
        BooleanObserver observer = new BooleanObserver();
        Promise<Boolean> promise = new Promise<Boolean>(supplier, observer);

        assertThat(observer.value).isNull();
        promise.run();
        assertThat(observer.value).isTrue();
        assertThat(observer.throwable).isNull();
        assertThat(observer.withoutError).isTrue();
	}

	@Test
	public void shouldObserveError() throws Exception {
        Supplier<Boolean> supplier = new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                throw new RuntimeException();
            }
        };
        BooleanObserver observer = new BooleanObserver();
        Promise<Boolean> promise = new Promise<Boolean>(supplier, observer);

        assertThat(observer.throwable).isNull();
        promise.run();
        assertThat(observer.value).isNull();
        assertThat(observer.throwable).isNotNull();
        assertThat(observer.withoutError).isFalse();
	}

	@Test
	public void shouldSetStateCompleted() throws Exception {
        Supplier<Boolean> supplier = new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return true;
            }
        };
        BooleanObserver observer = new BooleanObserver();
        Promise<Boolean> promise = new Promise<Boolean>(supplier, observer);

        assertThat(promise.getState()).isEqualTo(Promise.State.CREATED);
        promise.run();
        assertThat(promise.getState()).isEqualTo(Promise.State.COMPLETED);
	}

	@Test
	public void shouldSetStateError() throws Exception {
        Supplier<Boolean> supplier = new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                throw new RuntimeException();
            }
        };
        BooleanObserver observer = new BooleanObserver();
        Promise<Boolean> promise = new Promise<Boolean>(supplier, observer);

        assertThat(promise.getState()).isEqualTo(Promise.State.CREATED);
        promise.run();
        assertThat(promise.getState()).isEqualTo(Promise.State.ERROR);
	}

    private class BooleanObserver implements Observer<Boolean> {
        Boolean value;
        Boolean withoutError;
        Throwable throwable;

        @Override
        public void completed(Boolean withoutError) {
          this.withoutError = withoutError;
        }

        @Override
        public void next(Boolean aBoolean) {
            value = aBoolean;
        }

        @Override
        public void error(Throwable e) {
            throwable = e;
        }
    }
}
