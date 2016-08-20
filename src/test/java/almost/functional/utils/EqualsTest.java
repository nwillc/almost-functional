/*
 * Copyright (c) 2016, nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */

package almost.functional.utils;

import almost.functional.Function;
import com.github.nwillc.contracts.PrivateConstructorContract;
import com.github.nwillc.contracts.UtilityClassContract;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class EqualsTest extends UtilityClassContract {
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