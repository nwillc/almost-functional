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

import static almost.functional.utils.LogFactory.getLogger;

/**
 * A runnable supplier promise. The supplier will be called on run() and if is succeeds or fails
 * associated consumers will be informed.
 *
 * This implements Runnable so that it can be tucked into a Future etc.
 *
 * @param <T> the type the supplier is committed to provide.
 */
public class Promise<T> implements Runnable {
	private static final Logger LOG = getLogger();
	private final Supplier<T> supplier;
	private final List<Consumer<T>> fulfilledConsumers = new CopyOnWriteArrayList<Consumer<T>>();
	private final List<Consumer<Exception>> rejectedConsumers = new CopyOnWriteArrayList<Consumer<Exception>>();
	private final List<Consumer<Optional<T>>> settledConsumers = new CopyOnWriteArrayList<Consumer<Optional<T>>>();
	private final AtomicReference<State> state = new AtomicReference<State>(State.CREATED);

	/**
	 * The current state of the promise.
	 */
	public enum State {CREATED, PENDING, FULFILLED, REJECTED}

	/**
	 * Create a Promise based on a given supplier.
	 * @param supplier the supplier
	 */
	public Promise(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	/**
	 * This method calls the supplier and on successful completion informs the fulfilled consumers of the
	 * return value. If the supplier throws an exception the rejected consumers are informed. In either case
	 * when the call to the supplier completes the settled consumers are informed with an Optional containing the
	 * supplied value on success.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		if (!state.compareAndSet(State.CREATED, State.PENDING)) {
			return;
		}
		T result;
		try {
			result = supplier.get();
			state.set(State.FULFILLED);
			inform(result, fulfilledConsumers);
			inform(Optional.of(result), settledConsumers);
		} catch (Exception e) {
			state.set(State.REJECTED);
			inform(e, rejectedConsumers);
			inform((Optional<T>) Optional.empty(), settledConsumers);
		}
	}

	/**
	 * Add a Consumer to be informed upon successful completion of the invocation of the supplier.
	 * @param consumer the consumer
	 * @return this promise
	 */
	public Promise<T> fulfilled(Consumer<T> consumer) {
		fulfilledConsumers.add(consumer);
		return this;
	}

	/**
	 * Add a Consumer to be informed if the supplier throws an exception and is rejected.
	 * @param consumer the consumer to inform
	 * @return this promise
	 */
	public Promise<T> rejected(Consumer<Exception> consumer) {
		rejectedConsumers.add(consumer);
		return this;
	}

	/**
	 * Add a consumer to be informed when the promise is settled either by fulfillment or rejection. The
	 * consumer will be passed an Optional that will contain the value supplied if the promise was fulfilled.
	 * @param consumer the consumer
	 * @return this promise
	 */
	public Promise<T> settled(Consumer<Optional<T>> consumer) {
		settledConsumers.add(consumer);
		return this;
	}

	/**
	 * Get the current state of this promise.
	 * @return the state
	 */
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
