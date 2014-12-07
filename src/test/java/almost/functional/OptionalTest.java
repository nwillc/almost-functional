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

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static almost.functional.Predicates.isEqual;
import static org.assertj.core.api.Assertions.assertThat;


public class OptionalTest {

	@Test(expected = NoSuchElementException.class)
	public void shouldBeEmplty() throws Exception {
		Optional<String> optional = Optional.empty();

		assertThat(optional.isPresent()).isFalse();
		optional.get();
	}

	@Test
	public void shouldOf() throws Exception {
		Optional<Integer> optional = Optional.of(5);

		assertThat(optional.isPresent()).isTrue();
		assertThat(optional.get()).isEqualTo(5);
	}

	@Test
	public void shouldIfPresent() throws Exception {
		Optional<Integer> optional = Optional.of(5);
		Variable<Integer> variable = new Variable<Integer>(10);

		assertThat(variable.get()).isEqualTo(10);
		optional.ifPresent(variable);
		assertThat(optional.get()).isEqualTo(5);
		assertThat(variable.get()).isEqualTo(optional.get());
	}

    @Test
    public void shouldIfPresentEmpty() throws Exception {
        Optional<Integer> optional = Optional.empty();
        Variable<Integer> variable = new Variable<Integer>(10);

        assertThat(variable.get()).isEqualTo(10);
        optional.ifPresent(variable);
        assertThat(variable.get()).isEqualTo(10);
    }

    @Test
	public void shouldOrElseEmpty() throws Exception {
		Optional<Integer> optional = Optional.empty();

		assertThat(optional.orElse(10)).isEqualTo(10);
		assertThat(optional.orElse(null)).isNull();
	}

	@Test
	public void shouldOrElseOf() throws Exception {
		Optional<Integer> optional = Optional.of(5);

		assertThat(optional.orElse(10)).isEqualTo(optional.get());
	}

    @Test
    public void shouldFilterTrue() throws Exception {
        Optional<Boolean> booleanOptional = Optional.of(true);

        Optional<Boolean> result = booleanOptional.filter(isEqual(true));

        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isTrue();
    }

    @Test
    public void shouldFilterFalse() throws Exception {
        Optional<Boolean> booleanOptional = Optional.of(true);

        Optional<Boolean> result = booleanOptional.filter(isEqual(false));

        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldFilterEmpty() throws Exception {
        Optional<Boolean> booleanOptional = Optional.empty();

        Optional<Boolean> result = booleanOptional.filter(isEqual(false));

        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isFalse();
    }

    @Test
	public void shouldTransformEmpty() throws Exception {
		Optional<Integer> optional = Optional.empty();

		assertThat(optional.map(new Increment()).isPresent()).isFalse();
	}

	@Test
	public void shouldMapOf() throws Exception {
		Optional<Integer> optional = Optional.of(0);

		Optional<Integer> returned = optional.map(new Increment());
		assertThat(returned.isPresent()).isTrue();
		assertThat(returned.get()).isEqualTo(1);
	}

    @Test
    public void shouldMapOfNullReturn() throws Exception {
        Optional<Integer> optional = Optional.of(0);
        Optional<Integer> returned = optional.map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer argument) {
                return null;
            }
        });

        assertThat(returned).isNotNull();
        assertThat(returned.isPresent()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
	public void shouldMapNullFunction() throws Exception {
		Optional<Integer> optional = Optional.of(0);
		optional.map(null);
	}

    @Test
    public void shouldEmptyOnOfNullableNull() throws Exception {
        Optional<String> stringOptional = Optional.ofNullable(null);
        assertThat(stringOptional).isNotNull();
        assertThat(stringOptional.isPresent()).isFalse();
    }

    @Test
    public void shouldReturnValueOfOrElseNotSupplier() throws Exception {
        Optional<Boolean> booleanOptional = Optional.of(Boolean.TRUE);

        Boolean value = booleanOptional.orElseSupplier(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return false;
            }
        });

        assertThat(value).isNotNull();
        assertThat(value).isTrue();
    }

    @Test
    public void shouldReturnValueOfOrElseSupplier() throws Exception {
        Optional<Boolean> booleanOptional = Optional.empty();

        Boolean value = booleanOptional.orElseSupplier(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return false;
            }
        });

        assertThat(value).isNotNull();
        assertThat(value).isFalse();
    }

    @Test
    public void shouldPresentOfNullable() throws Exception {
        Optional<String> stringOptional = Optional.ofNullable("");
        assertThat(stringOptional).isNotNull();
        assertThat(stringOptional.isPresent()).isTrue();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionOnEmpty() throws Exception {
        final String msg = "Hello";

        try {
            Optional.empty().orElseThrow(msg);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(msg);
            throw e;
        }
    }

    @Test
    public void testIfPresentConsumer() throws Exception {
        final Integer increment = 5;
        final AtomicInteger value = new AtomicInteger(increment);
        Optional<AtomicInteger> integerOptional = Optional.of(value);

        assertThat(value.get()).isEqualTo(increment);
        assertThat(integerOptional.isPresent()).isTrue();
        Optional<AtomicInteger> ret = integerOptional.ifPresent(new Consumer<AtomicInteger>() {
            @Override
            public void accept(AtomicInteger consumable) {
                value.getAndAdd(increment);
            }
        });
        assertThat(ret).isEqualTo(integerOptional);
        assertThat(value.get()).isEqualTo(2 * increment);
    }

    @Test
    public void testOrElseRun() throws Exception {
        final Integer increment = 5;
        final AtomicInteger value = new AtomicInteger(increment);
        Optional<AtomicInteger> integerOptional = Optional.empty();

        assertThat(value.get()).isEqualTo(increment);
        assertThat(integerOptional.isPresent()).isFalse();
        Optional<AtomicInteger> ret = integerOptional.orElseRun(new Runnable() {
            @Override
            public void run() {
                value.getAndAdd(increment);
            }
        });
        assertThat(ret).isEqualTo(integerOptional);
        assertThat(value.get()).isEqualTo(2 * increment);
    }

    @Test
    public void shouldReturnValueNotThrow() throws Exception {
        final String msg = "Hey";

        Optional<String> optional = Optional.of(msg);
        assertThat(optional.orElseThrow("Bail")).isEqualTo(msg);
    }

    private static class Increment implements Function<Integer,Integer> {
		@Override
		public Integer apply(Integer integer) {
			return integer + 1;
		}
	}
}
