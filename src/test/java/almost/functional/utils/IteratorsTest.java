package almost.functional.utils;

import com.github.nwillc.contracts.ImmutableIteratorContract;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static almost.functional.utils.Iterators.concat;
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
}