package almost.functional.utils;

import almost.functional.Function;
import com.github.nwillc.contracts.PrivateConstructorContract;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class EqualsTest extends PrivateConstructorContract {
    private Bean one, two;

    @Before
    public void setUp() throws Exception {
        one = new Bean();
        two = new Bean();

        one.a = two.a = 4;
        one.b = two.b = 2;
        one.c = "one";
        two.c = "two";

    }

    @Test
    public void testNullArgs() throws Exception {
        assertThat(Equals.equals(null,null)).isTrue();
        assertThat(Equals.equals(null,two)).isFalse();
        assertThat(Equals.equals(one,null)).isFalse();
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(Equals.equals(one,one)).isTrue();
        assertThat(Equals.equals(one, two, new Function<Bean, Integer>() {
            @Override
            public Integer apply(Bean argument) {
                return argument.a;
            }
        })).isTrue();

        assertThat(Equals.equals(one, two,
                new Function<Bean, Integer>() {
                    @Override
                    public Integer apply(Bean argument) {
                        return argument.a;
                    }
                },
                new Function<Bean, Integer>() {
                    @Override
                    public Integer apply(Bean argument) {
                        return argument.b;
                    }
                }
        )).isTrue();
    }

    @Test
    public void testNotEquals() throws Exception {
        assertThat(Equals.equals(one, two,
                new Function<Bean, Integer>() {
                    @Override
                    public Integer apply(Bean argument) {
                        return argument.a;
                    }
                },
                new Function<Bean, Integer>() {
                    @Override
                    public Integer apply(Bean argument) {
                        return argument.b;
                    }
                },
                new Function<Bean, String>() {
                    @Override
                    public String apply(Bean argument) {
                        return argument.c;
                    }
                }
        )).isFalse();
    }


    @Override
    public Class<?> getClassToTest() {
        return Equals.class;
    }

    private static class Bean {
        Integer a;
        Integer b;
        String c;
    }
}