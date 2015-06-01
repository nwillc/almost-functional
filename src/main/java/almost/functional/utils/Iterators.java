package almost.functional.utils;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Utility class for Iterators.
 */
public final class Iterators {
    private Iterators() {
    }

    /**
     * Create an iterator which sequentially iterates over a collection of iterators.
     * @param iterators the iterators to iterate
     * @param <T> the element type
     * @return the new iterator
     */
    public static  <T> Iterator<T> concat(final Iterator<? extends T> ... iterators) {
        return new ImmutableIterator<T>() {
            int current = 0;

            @Override
            public boolean hasNext() {
                advance();
                return current < iterators.length;
            }

            @Override
            public T next() {
                advance();
                try {
                    return iterators[current].next();
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            private void advance() {
                while (current < iterators.length && !iterators[current].hasNext()) {
                    current++;
                }
            }
        };
    }


}
