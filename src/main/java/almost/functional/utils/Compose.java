package almost.functional.utils;

import almost.functional.BiFunction;
import almost.functional.Consumer;
import almost.functional.Function;

/**
 * Utility class to compose functions from pairs of existing ones.
 *
 * @since 1.9.8
 */
public final class Compose {
    private Compose() {
    }

    /**
     * Create a function that uses the result of the first function as the input to the second.
     *
     * @param first  a function
     * @param second a function
     * @param <T>    input to the first function
     * @param <R>    result of the first function, input to second
     * @param <V>    result of the second function
     * @return a function composed o first and second
     */
    public static <T, R, V> Function<T, V> compose(final Function<T, R> first, final Function<? super R, ? extends V> second) {
        return new Function<T, V>() {
            @Override
            public V apply(T argument) {
                return second.apply(first.apply(argument));
            }
        };
    }

    /**
     * Compose a BiFunction which applies a Function to the result of another BiFunction.
     *
     * @param first  a Bifunction
     * @param second a Function
     * @param <F>    first argument of BiFunction
     * @param <S>    second argument to Bifunction
     * @param <R>    result type of first BiFunction
     * @param <V>    result type of resultant BiFunction
     * @return A new BiFunction
     */
    public static <F, S, R, V> BiFunction<F, S, V> andThen(final BiFunction<F, S, R> first, final Function<? super R, ? extends V> second) {
        return new BiFunction<F, S, V>() {
            @Override
            public V apply(F f, S s) {
                return second.apply(first.apply(f, s));
            }
        };
    }

    /**
     * Compose a new consumer from two existing ones. The consumable will be passed to the first then the second.
     *
     * @param first  a consumer
     * @param second a consumer
     * @param <T>    the type the consumers consume
     * @return a new consumer
     */
    public static <T> Consumer<T> andThen(final Consumer<T> first, final Consumer<T> second) {
        return new Consumer<T>() {
            @Override
            public void accept(T consumable) {
                first.accept(consumable);
                second.accept(consumable);
            }
        };
    }
}
