package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> defaultComparator;

    public MaxArrayDeque(Comparator<T> c) {
        defaultComparator = c;
    }

    public T max() {
        return max(defaultComparator);
    }

    public T max(Comparator<T> customComparator) {
        T largest = null;

        for (int i = 0; i < size(); i += 1) {
            T item = get(i);
            if (largest == null || customComparator.compare(item, largest) > 0) {
                largest = item;
            }
        }
        return largest;
    }
}
