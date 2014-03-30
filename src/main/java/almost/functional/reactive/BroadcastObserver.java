/*
 * Copyright (c) 2014, nwillc@gmail.com
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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import static almost.functional.utils.ArrayIterable.newIterable;
import static almost.functional.utils.Iterables.forEach;
import static almost.functional.utils.LogFactory.getLogger;

public class BroadcastObserver<T> implements Observer<T> {
    private static final Logger LOGGER = getLogger();
    private final List<Consumer<T>> nextConsumers = new CopyOnWriteArrayList<Consumer<T>>();
    private final List<Consumer<Throwable>> errorConsumers = new CopyOnWriteArrayList<Consumer<Throwable>>();
    private final List<Consumer<Boolean>> completedConsumers = new CopyOnWriteArrayList<Consumer<Boolean>>();

    public BroadcastObserver() {
        this(Optional.<Consumer<T>>empty(), Optional.<Consumer<Throwable>>empty(), Optional.<Consumer<Boolean>>empty());
    }

    public BroadcastObserver(Optional<Consumer<T>> nextConsumer, Optional<Consumer<Throwable>> errorConsumer, Optional<Consumer<Boolean>> completedConsumer) {
        if (nextConsumer.isPresent()) {
            nextConsumers.add(nextConsumer.get());
        }
        if (errorConsumer.isPresent()) {
            errorConsumers.add(errorConsumer.get());
        }
        if (completedConsumer.isPresent()) {
            completedConsumers.add(completedConsumer.get());
        }
    }

    @Override
    public void completed(Boolean withoutError) {
        inform(withoutError, completedConsumers);
    }

    @Override
    public void next(T t) {
        inform(t, nextConsumers);
    }

    @Override
    public void error(Throwable e) {
        inform(e, errorConsumers);
    }

    public Observer<T> addNextConsumer(Consumer<T> ... consumers) {
        addAll(nextConsumers, consumers);
        return this;
    }

    public Observer<T> addErrorConsumer(Consumer<Throwable> ... consumers) {
        addAll(errorConsumers, consumers);
        return this;
    }

    public Observer<T> addCompletedConsumer(Consumer<Boolean> ... consumers) {
        addAll(completedConsumers, consumers);
        return this;
    }

    private static <V> void addAll(final List<Consumer<V>> consumerList, Consumer<V> ... additions) {
        forEach(newIterable(additions), new Consumer<Consumer<V>>() {
            @Override
            public void accept(Consumer<V> vConsumer) {
                consumerList.add(vConsumer);
            }
        });
    }

    private static <V> void inform(V value, List<Consumer<V>> consumers) {
        for (Consumer<V> consumer : consumers) {
            try {
                consumer.accept(value);
            } catch (Exception e) {
                LOGGER.warning("Failed informing of " + value);
            }
        }
    }
}
