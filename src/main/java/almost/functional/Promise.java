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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class Promise<T> implements Runnable {
	private static final Logger LOG = Logger.getLogger(Promise.class.getName());
	private final Supplier<T> supplier;
	private final List<Consumer<T>> fullfilledConsumers = new CopyOnWriteArrayList<Consumer<T>>();
	private final List<Consumer<Exception>> rejectedConsumers = new CopyOnWriteArrayList<Consumer<Exception>>();
	private final List<Consumer<Optional<T>>> settledConsumers = new CopyOnWriteArrayList<Consumer<Optional<T>>>();
	private final AtomicReference<State> state = new AtomicReference<State>(State.PENDING);

	public enum State {PENDING, FULFILLED, REJECTED}

	public Promise(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		T result;
		try {
			result = supplier.get();
			state.set(State.FULFILLED);
			inform(result, fullfilledConsumers);
			inform(Optional.of(result), settledConsumers);
		} catch (Exception e) {
			state.set(State.REJECTED);
			inform(e, rejectedConsumers);
			inform((Optional<T>) Optional.empty(), settledConsumers);
		}
	}

	public void fullfilled(Consumer<T> consumer) {
		fullfilledConsumers.add(consumer);
	}

	public void rejected(Consumer<Exception> consumer) {
		rejectedConsumers.add(consumer);
	}

	public void settled(Consumer<Optional<T>> consumer) {
		settledConsumers.add(consumer);
	}

	public State getState() {
		return state.get();
	}

	private static <V> void inform(V value, List<Consumer<V>> consumers) {
		for (Consumer<V> comsumer : consumers) {
			try {
				comsumer.accept(value);
			} catch (Exception e) {
				LOG.warning("Failed informing of " + value);
			}
		}
	}
}
