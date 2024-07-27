package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> defaultComparator;
    private T defaultLargest = null;

    public MaxArrayDeque(Comparator<T> c) {
        defaultComparator = c;
    }

    @Override
    public void addFirst(T item) {
        if (defaultLargest == null || defaultComparator.compare(item, defaultLargest) > 0) {
            defaultLargest = item;
        }

        super.addFirst(item);
    }

    @Override
    public void addLast(T item) {
        if (defaultLargest == null || defaultComparator.compare(item, defaultLargest) > 0) {
            defaultLargest = item;
        }

        super.addLast(item);
    }

    @Override
    public T removeFirst() {
        T item = super.removeFirst();

        if (defaultComparator.compare(item, defaultLargest) == 0) {
            defaultLargest = max(defaultComparator);
        }
        return item;
    }

    @Override
    public T removeLast() {
        T item = super.removeLast();

        if (defaultComparator.compare(item, defaultLargest) == 0) {
            defaultLargest = max(defaultComparator);
        }
        return item;
    }

    public T max() {
        return defaultLargest;
    }

    public T max(Comparator<T> customComparator) {
        T customLargest = null;

        for (int i = 0; i < size(); i += 1) {
            T item = get(i);
            if (customLargest == null || customComparator.compare(item, customLargest) > 0) {
                customLargest = item;
            }
        }
        return customLargest;
    }
}
