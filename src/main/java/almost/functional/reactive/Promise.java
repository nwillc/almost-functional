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

import java.util.concurrent.atomic.AtomicReference;

/**
 * A Promise is a composite of a Supplier and an Observer allowing for observation or the supplier.
 *
 * This implements Runnable so that it can be tucked into a Future etc.  Once the Observer is completed
 * it is released.
 *
 * @see almost.functional.reactive.Observer
 * @see almost.functional.Consumer
 * @see almost.functional.Supplier
 * @param <T> the type the supplier is committed to provide.
 */
public class Promise<T> implements Runnable {
	private final Supplier<T> supplier;
	private final AtomicReference<State> state = new AtomicReference<State>(State.CREATED);
	private Observer<T> observer;

	/**
	 * The current state of the promise.
	 */
	public enum State {
        /** The promise has been created. */
        CREATED,
        /** The promise has been run but not completed. */
        PENDING,
        /** The promise is completed successfully. */
        COMPLETED,
        /** The promise had error before successful completion. */
        ERROR
    }

	/**
	 * Create a Promise based on a given supplier and with an Observer.
	 * @param supplier the supplier
	 */
	public Promise(Supplier<T> supplier, Observer<T> observer) {
		this.supplier = supplier;
        this.observer = observer;
	}

	/**
	 * This method calls the supplier and on successful completion informs the fulfilled consumers of the
	 * return value. If the supplier throws an exception the rejected consumers are informed. In either case
	 * when the call to the supplier completes the settled consumers are informed with an Optional containing the
	 * supplied value on success.
	 */
	@Override
	public void run() {
		if (!state.compareAndSet(State.CREATED, State.PENDING)) {
			throw new IllegalStateException("Can only run a promise in the CREATED state");
		}
		T result;
		try {
			result = supplier.get();
			state.set(State.COMPLETED);
            observer.next(result);
			observer.completed(true);
		} catch (Exception e) {
			state.set(State.ERROR);
			observer.error(e);
            observer.completed(false);
		}
		observer = null;
	}

	/**
	 * Get the current state of this promise.
	 * @return the state
	 */
	public State getState() {
		return state.get();
	}

}
