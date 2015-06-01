package almost.functional.utils;

import almost.functional.BiFunction;
import almost.functional.Consumer;
import almost.functional.Function;
import almost.functional.Predicate;
import org.junit.Test;

import static almost.functional.utils.IterablesTest.Accumulator;
import static org.assertj.core.api.Assertions.assertThat;

public class StreamTest {

    @Test
    public void testOfIterable() throws Exception {

    }

    @Test
    public void testOfVarargs() throws Exception {

    }

    @Test
    public void testForEach() throws Exception {
        Stream<String> strings = Stream.of("a", "b", "c");
        final StringBuffer stringBuffer = new StringBuffer();
        strings.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                stringBuffer.append(s);
            }
        });
        assertThat(stringBuffer.toString()).isEqualTo("abc");
    }

    @Test
    public void testReduce() throws Exception {
        Stream<String> numbers = Stream.of("1", "2", "3", "4", "5");
        Accumulator accumulator = new Accumulator();
        Integer sum = numbers.reduce(1, accumulator);
        assertThat(sum).isEqualTo(16);
    }

    @Test
    public void testFilter() throws Exception {
        Stream<Integer> numbers = Stream.of(1, 2, 3, 4, 5);
        Integer sum = numbers.filter(new Predicate<Integer>() {
                                         @Override
                                         public boolean test(Integer integer) {
                                             return integer % 2 != 0;
                                         }
                                     })
                                .reduce(0, new BiFunction<Integer, Integer, Integer>() {
                                    @Override
                                    public Integer apply(Integer first, Integer second) {
                                        return first + second;
                                    }
                                });
        assertThat(sum).isEqualTo(9);
    }

    @Test
    public void testMap() throws Exception {
        Stream<Integer> twos = Stream.of(2, 4, 6, 8);
        Stream<Integer> squares = twos.map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer argument) {
                return argument * argument;
            }
        });

        assertThat(squares.reduce(0, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer first, Integer second) {
                return first + second;
            }
        })).isEqualTo(4 + 16 + 36 + 64);
    }

    @Test
    public void testAnyMatchSucceed() throws Exception {
        Stream<String> strings = Stream.of("a", "b", "c");
        assertThat(strings.anyMatch(new Predicate<String>() {
            @Override
            public boolean test(String testValue) {
                return testValue.equals("b");
            }
        })).isTrue();
    }

    @Test
    public void testAnyMatchFails() throws Exception {
        Stream<String> strings = Stream.of("a", "b", "c");
        assertThat(strings.anyMatch(new Predicate<String>() {
            @Override
            public boolean test(String testValue) {
                return testValue.equals("d");
            }
        })).isFalse();
    }
}