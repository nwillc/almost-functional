package almost.functional.utils;

import com.github.nwillc.contracts.ImmutableIteratorContract;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static almost.functional.utils.Iterators.concat;
import static almost.functional.utils.Iterators.next;
import static org.assertj.core.api.Assertions.assertThat;

public class IteratorsTest extends ImmutableIteratorContract {

	@SuppressWarnings("unchecked")
	@Override
	protected Iterator getNonEmptyIterator() {
		List<Integer> first = Arrays.asList(1, 2, 3);
		List<Integer> second = Arrays.asList(4, 5, 6);

		return concat(first.iterator(), second.iterator());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testBasic() throws Exception {
		Iterator<Integer> combined = getNonEmptyIterator();

		assertThat(combined).containsExactly(1, 2, 3, 4, 5, 6);
	}

	@Test
	public void shouldNext() throws Exception {
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
		Iterator<Integer> numbersIterator = numbers.iterator();
		Iterator<Integer> next4 = next(numbersIterator, 4);
		int count = 0;
		while (next4.hasNext()) {
			next4.next();
			count++;
		}
		assertThat(count).isEqualTo(4);
		next4 = next(numbersIterator,4);
		count = 0;
		while (next4.hasNext()) {
			next4.next();
			count++;
		}
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void shouldConcat() throws Exception {

	}
}