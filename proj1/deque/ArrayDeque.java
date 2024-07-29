package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] values;
    private int size;
    private int startIndex;

    private static final int GROWTH_FACTOR = 2;
    private static final int USAGE_FACTOR = 4;
    private static final int MIN_SHRINK_SIZE = 16;

    public ArrayDeque() {
        values = (T[]) new Object[8];
        size = 0;
    }

    private void resize(int newSize) {
        T[] newArray = (T[]) new Object[newSize];

        // Account for wrapping
        if (startIndex + size > values.length) {
            int offset = values.length - startIndex;
            System.arraycopy(values, startIndex, newArray, 0, offset);
            System.arraycopy(values, 0, newArray, offset, size - offset);
        } else {
            System.arraycopy(values, startIndex, newArray, 0, size);
        }
        values = newArray;
        startIndex = 0;
    }

    @Override
    public void addFirst(T item) {
        if (size == values.length) {
            resize(values.length * GROWTH_FACTOR);
        }

        int indexToInsert =
                startIndex - 1 < 0
                        ? values.length - 1
                        : startIndex - 1;
        values[indexToInsert] = item;
        startIndex =
                startIndex == 0
                    ? values.length - 1
                    : startIndex - 1;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == values.length) {
            resize(values.length * GROWTH_FACTOR);
        }

        int indexToInsert =
                startIndex + size >= values.length
                        ? startIndex + size - values.length
                        : startIndex + size;
        values[indexToInsert] = item;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i += 1) {
            System.out.print(get(i));
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else if (size > MIN_SHRINK_SIZE && size < values.length / USAGE_FACTOR) {
            resize(values.length / USAGE_FACTOR);
        }

        T value = values[startIndex];
        values[startIndex] = null;
        startIndex =
                startIndex + 1 >= values.length
                    ? startIndex - values.length + 1
                    : startIndex + 1;
        size -= 1;
        return value;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        } else if (size >= MIN_SHRINK_SIZE && size <= values.length / USAGE_FACTOR) {
            resize(values.length / USAGE_FACTOR);
        }

        int lastIndex =
                startIndex + size - 1 >= values.length
                    ? startIndex + size - 1 - values.length
                    : startIndex + size - 1;
        T value = values[lastIndex];
        values[lastIndex] = null;
        size -= 1;
        return value;
    }

    @Override
    public T get(int index) {
        int adjustedIndex =
                startIndex + index >= values.length
                        ? startIndex + index - values.length
                        : startIndex + index;
        return values[adjustedIndex];
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        } else if (obj instanceof Deque && size() == ((Deque<?>) obj).size()) {
            for (int i = 0; i < size(); i += 1) {
                if (get(i) != ((Deque<?>) obj).get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private class ArrayDequeIterator implements Iterator<T> {
        int index = 0;

        @Override
        public boolean hasNext() {
            return get(index) != null;
        }

        @Override
        public T next() {
            T item = get(index);
            index += 1;
            return item;
        }
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
}
