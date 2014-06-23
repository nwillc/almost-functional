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

import almost.functional.Consumer;
import almost.functional.Optional;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.fest.assertions.api.Assertions.assertThat;

public class BroadcastObserverTest {

	@Test
	public void shouldAllowSimplifiedConstructor() throws Exception {
		BooleanConsumer booleanConsumer = new BooleanConsumer();

		BroadcastObserver<Boolean> broadcastObserver = new BroadcastObserver<Boolean>(
				Optional.<Consumer<Boolean>>of(booleanConsumer),
				Optional.<Consumer<Throwable>>empty(),
				Optional.<Consumer<Boolean>>empty()
		);

		assertThat(booleanConsumer.flag.get()).isFalse();
		broadcastObserver.next(true);
		assertThat(booleanConsumer.flag.get()).isTrue();
	}

	@Test
	public void shouldBeCompleted() throws Exception {
		BooleanConsumer consumer = new BooleanConsumer();
		BroadcastObserver<Boolean> broadcastObserver = new BroadcastObserver<Boolean>();
		broadcastObserver.addCompletedConsumer(consumer);

		assertThat(consumer.flag.get()).isFalse();
		broadcastObserver.completed(true);
		assertThat(consumer.flag.get()).isTrue();
	}

	@Test
	public void shouldNext() throws Exception {
		BooleanConsumer consumer = new BooleanConsumer();
		BroadcastObserver<Boolean> broadcastObserver = new BroadcastObserver<Boolean>();
		broadcastObserver.addNextConsumer(consumer);

		assertThat(consumer.flag.get()).isFalse();
		broadcastObserver.next(true);
		assertThat(consumer.flag.get()).isTrue();
	}

	@Test
	public void shouldError() throws Exception {
		ErrorConsumer errorConsumer = new ErrorConsumer();
		BroadcastObserver<Boolean> broadcastObserver = new BroadcastObserver<Boolean>();
		broadcastObserver.addErrorConsumer(errorConsumer);

		assertThat(errorConsumer.error.get()).isNull();
		broadcastObserver.error(new NullPointerException());
		assertThat(errorConsumer.error.get()).isInstanceOf(NullPointerException.class);
	}

	@Test
	public void shouldRespectCompleted() throws Exception {
		BooleanConsumer next = new BooleanConsumer();
		BooleanConsumer completed = new BooleanConsumer();
		ErrorConsumer error = new ErrorConsumer();
		BroadcastObserver<Boolean> broadcastObserver = new BroadcastObserver<Boolean>(Optional.of(next),
                                                                                Optional.of(error),
                                                                                Optional.of(completed));

		assertThat(next.flag.get()).isFalse();
		assertThat(completed.flag.get()).isFalse();
		assertThat(error.error.get()).isNull();

		broadcastObserver.next(true);
		assertThat(next.flag.get()).isTrue();
		broadcastObserver.next(false);
		assertThat(next.flag.get()).isFalse();

		broadcastObserver.error(new NullPointerException());
		assertThat(error.error.get()).isInstanceOf(NullPointerException.class);
		broadcastObserver.error(null);
		assertThat(error.error.get()).isNull();

		completed.flag.set(true);
		broadcastObserver.completed(false);
		assertThat(completed.flag.get()).isFalse();

		// Should ignore further messages since completed
		broadcastObserver.completed(true);
		assertThat(completed.flag.get()).isFalse();
		broadcastObserver.next(true);
		assertThat(next.flag.get()).isFalse();
		broadcastObserver.error(new NullPointerException());
		assertThat(error.error.get()).isNull();
	}

	@Test
	public void shouldDoMultiples() throws Exception {
		BooleanConsumer one = new BooleanConsumer();
		BooleanConsumer two = new BooleanConsumer();
		BooleanConsumer three = new BooleanConsumer();
		BooleanConsumer four = new BooleanConsumer();

		BroadcastObserver<Boolean> broadcastObserver = new BroadcastObserver<Boolean>();
		broadcastObserver.addNextConsumer(one, two);
		broadcastObserver.addCompletedConsumer(three, four);

		assertThat(one.flag.get()).isFalse();
		assertThat(two.flag.get()).isFalse();
		assertThat(three.flag.get()).isFalse();
		assertThat(four.flag.get()).isFalse();

		broadcastObserver.next(true);

		assertThat(one.flag.get()).isTrue();
		assertThat(two.flag.get()).isTrue();
		assertThat(three.flag.get()).isFalse();
		assertThat(four.flag.get()).isFalse();

		broadcastObserver.next(false);

		assertThat(one.flag.get()).isFalse();
		assertThat(two.flag.get()).isFalse();
		assertThat(three.flag.get()).isFalse();
		assertThat(four.flag.get()).isFalse();

		broadcastObserver.completed(true);
		assertThat(one.flag.get()).isFalse();
		assertThat(two.flag.get()).isFalse();
		assertThat(three.flag.get()).isTrue();
		assertThat(four.flag.get()).isTrue();
	}

    @Test
    public void shouldIgnoreExceptions() throws Exception {
        Consumer<Boolean> exceptionThrower = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                throw new IllegalArgumentException();
            }
        };
        BroadcastObserver<Boolean> broadcastObserver = new BroadcastObserver<Boolean>(Optional.of(exceptionThrower),
                                                                    Optional.<Consumer<Throwable>>empty(),
                                                                    Optional.<Consumer<Boolean>>empty());

        broadcastObserver.next(true);
    }

    private static class BooleanConsumer implements Consumer<Boolean> {
		final AtomicBoolean flag = new AtomicBoolean(false);

		@Override
		public void accept(Boolean aBoolean) {
			flag.set(aBoolean);
		}
	}

	private static class ErrorConsumer implements Consumer<Throwable> {
		final AtomicReference<Throwable> error = new AtomicReference<Throwable>(null);

		@Override
		public void accept(Throwable throwable) {
			error.set(throwable);
		}
	}
}
