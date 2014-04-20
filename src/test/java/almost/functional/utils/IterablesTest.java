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

package almost.functional.utils;

import almost.functional.*;
import org.junit.Test;

import java.util.*;

import static almost.functional.Predicates.isEqual;
import static almost.functional.utils.ArrayIterable.newIterable;
import static almost.functional.utils.Iterables.*;
import static org.fest.assertions.api.Assertions.assertThat;


public class IterablesTest {

	@Test
	public void shouldForEachConsumer() throws Exception {
        String[] strings = {"a", "b", "c"};
		final StringBuffer stringBuffer = new StringBuffer();
		forEach(newIterable(strings), new Consumer<String>() {
			@Override
			public void accept(String s) {
				stringBuffer.append(s);
			}
		});
		assertThat(stringBuffer.toString()).isEqualTo("abc");
	}

	@Test
    @SuppressWarnings("deprecation")
	public void shouldForEachFunction() throws Exception {
        String[] strings = {"a", "b", "c"};
		Function<String,String> function = new Function<String, String>() {
			private StringBuffer stringBuffer = new StringBuffer();
			@Override
			public String apply(String s) {
				stringBuffer.append(s);
				return stringBuffer.toString();
			}
		};
		assertThat(forEach(newIterable(strings), function)).isEqualTo("abc");
	}

	@Test
	public void shouldFind() throws Exception {
		Iterable<Integer> twos = newIterable(2, 4, 6, 8);

		Optional<Integer> six = find(twos, isEqual(6));
		assertThat(six.isPresent()).isTrue();
		assertThat(six.get()).isEqualTo(6);
	}

	@Test
	public void shouldNotFind() throws Exception {
		Iterable<Integer> twos = newIterable(2, 4, 6, 8);

		Optional<Integer> seven = find(twos, isEqual(7));
		assertThat(seven.isPresent()).isFalse();
	}

	@Test
	public void shouldNotFindUseDefault() throws Exception {
		Iterable<Integer> twos = newIterable(2, 4, 6, 8);
		assertThat(find(twos, isEqual(7), 9)).isEqualTo(9);
	}

	@Test
	public void shouldAny() throws Exception {
		Iterable<Integer> twos = newIterable(2, 4, 6, 8);

		assertThat(any(twos, isEqual(4))).isTrue();
	}

	@Test
	public void shouldNotAny() throws Exception {
		Iterable<Integer> twos = newIterable(2, 4, 6, 8);

		assertThat(any(twos, isEqual(5))).isFalse();
	}

	@Test
	public void shouldReduce() throws Exception {
		Iterable<String> numbers = newIterable("1", "2", "3", "4", "5");
        Accumulator accumulator = new Accumulator();
		Integer sum = reduce(numbers, 1, accumulator);
		assertThat(sum).isEqualTo(16);
	}

	@Test
    public void testMap() throws Exception {
        Iterable<Integer> twos = newIterable(2, 4, 6, 8);

        Iterable<Integer> squares = map(twos, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer * integer;
            }
        });

        Iterator<Integer> squareValues = squares.iterator();
        for (Integer value : twos) {
            assertThat(squareValues.hasNext()).isTrue();
            assertThat(squareValues.next()).isEqualTo(value * value);
        }
    }

	@Test
	public void shouldContain() throws Exception {
		Iterable<Integer> numbers = newIterable(1,2,3,4,5);

		assertThat(contains(numbers, 4)).isTrue();
	}

	@Test
	public void shouldNotContain() throws Exception {
		Iterable<Integer> numbers = newIterable(1,2,3,4,5);

		assertThat(contains(numbers, 6)).isFalse();
	}

	@Test
    public void testFilter() throws Exception {
        Iterable<Integer> numbers = newIterable(1, 2, 3, 4, 5, 6);
        Iterable<Integer> odds = newIterable(1, 3, 5);

        Iterable<Integer> filtered = filter(numbers, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 1;
            }
        });

        Iterator<Integer> filteredValues = filtered.iterator();
        for (Integer odd : odds) {
            assertThat(filteredValues.hasNext()).isTrue();
            assertThat(filteredValues.next()).isEqualTo(odd);
        }

        assertThat(filteredValues.hasNext()).isFalse();
    }

    @Test
    public void testShouldCreateIterableFromEnumeration() throws Exception {

        Collection<String> numbers = new ArrayList<String>();
        numbers.add("1");
        numbers.add("2");
        numbers.add("3");

        int sum = reduce(numbers, 0, new Accumulator());

        Enumeration<String> enumeration = Collections.enumeration(numbers);
        Iterable<String> iterable = Iterables.iterable(enumeration);
        assertThat(reduce(iterable, 0, new Accumulator())).isEqualTo(sum);
    }

    private class Accumulator implements BiFunction<Integer, String, Integer> {
        @Override
        public Integer apply(Integer first, String second) {
            return first + Integer.valueOf(second);
        }
    };
}
