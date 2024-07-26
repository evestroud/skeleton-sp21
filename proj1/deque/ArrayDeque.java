package deque;

public class ArrayDeque<T> {
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

    private void resize(int new_size) {
        T[] new_array = (T[]) new Object[new_size];

        // Account for wrapping
        if (startIndex + size > values.length) {
            int offset = values.length - startIndex;
            System.arraycopy(values, startIndex, new_array, 0, offset);
            System.arraycopy(values, 0, new_array, offset, size - offset);
        } else {
            System.arraycopy(values, startIndex, new_array, 0, size);
        }
        values = new_array;
        startIndex = 0;
    }

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

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = startIndex; i != startIndex + size; i += 1) {
            if (i == values.length) {
                i = 0;
            }
            System.out.print(values[i]);
            System.out.print(" ");
        }
        System.out.print("\n");
    }

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

    public T removeLast() {
        if (size == 0) {
            return null;
        } else if (size > MIN_SHRINK_SIZE && size < values.length / USAGE_FACTOR) {
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

    public T get(int index) {
        int adjustedIndex =
                startIndex + index > values.length
                        ? startIndex + index - values.length
                        : startIndex + index;
        return values[adjustedIndex];
    }
}
