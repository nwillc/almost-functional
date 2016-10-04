package almost.functional.utils;

import almost.functional.BiFunction;
import almost.functional.Consumer;
import almost.functional.Function;
import com.github.nwillc.contracts.UtilityClassContract;
import org.junit.Test;

import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

import static almost.functional.utils.Compose.andThen;
import static almost.functional.utils.Compose.compose;
import static org.assertj.core.api.Assertions.assertThat;


public class ComposeTest extends UtilityClassContract {

    @Override
    public Class<?> getClassToTest() {
        return Compose.class;
    }

    @Test
    public void testCompose() throws Exception {
        Function<String,Integer> first = new Function<String, Integer>() {
            @Override
            public Integer apply(String argument) {
                return Integer.parseInt(argument);
            }
        };
        Function<Integer,Double> second = new Function<Integer, Double>() {
            @Override
            public Double apply(Integer argument) {
                return argument.floatValue()/2.0;
            }
        };

        Function<String,Double> composed = compose(first,second);
        assertThat(composed.apply("3")).isEqualTo(1.5);
    }

    @Test
    public void testAndThenConsumer() throws Exception {
        final AtomicInteger firstFlag = new AtomicInteger(0);
        final AtomicInteger secondFlag = new AtomicInteger(0);
        Consumer<Integer> first = new Consumer<Integer>() {
            @Override
            public void accept(Integer consumable) {
                firstFlag.set(consumable);
            }
        };
        Consumer<Integer> second = new Consumer<Integer>() {
            @Override
            public void accept(Integer consumable) {
                secondFlag.set(consumable);
            }
        };

        final Consumer<Integer> composed = andThen(first, second);
        composed.accept(3);
        assertThat(firstFlag.get()).isEqualTo(3);
        assertThat(secondFlag.get()).isEqualTo(3);
    }

    @Test
    public void testAndTheBiFunction() throws Exception {
        BiFunction<Integer, Integer, String> first = new BiFunction<Integer, Integer, String>() {
            @Override
            public String apply(Integer first, Integer second) {
                return String.valueOf(first + second);
            }
        };
        Function<String,Double> second = new Function<String, Double>() {
            @Override
            public Double apply(String argument) {
                return Double.valueOf(argument) / 2.0;
            }
        };

        final BiFunction<Integer, Integer, Double> composed = andThen(first, second);
        assertThat(composed.apply(10,4)).isEqualTo(7.0);
    }
}