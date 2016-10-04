package almost.functional.utils;

import almost.functional.Function;
import almost.functional.Predicate;
import almost.functional.Stream;

import java.util.Objects;


/**
 * @since 1.6+
 */
public final class Equals {
    private Equals() {
    }

    public static <T> boolean equals(final T one, final T two, Function<? super T, ?>... accessors) {
        return one == two ||
                !(one == null || two == null) &&
                        Stream.of(accessors).allMatch(new Predicate<Function<? super T, ?>>() {
                            @Override
                            public boolean test(Function<? super T, ?> accessor) {
                                return Objects.equals(accessor.apply(one), accessor.apply(two));
                            }
                        });
    }
}
