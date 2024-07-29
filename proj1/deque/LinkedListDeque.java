package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private static class Node<T> {
        T value;
        Node<T> prev;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }

        Node(T value, Node<T> prev, Node<T> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private final Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node<>(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        Node<T> newFirst = new Node<>(item, sentinel, sentinel.next);
        sentinel.next.prev = newFirst;
        sentinel.next = newFirst;
        if (size == 0) {
            sentinel.prev = newFirst;
        }
        size += 1;
    }

    @Override
    public void addLast(T item) {
        Node<T> newLast = new Node<>(item, sentinel.prev, sentinel);
        sentinel.prev.next = newLast;
        sentinel.prev = newLast;
        if (size == 0) {
            sentinel.next = newLast;
        }
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (Node<T> head = sentinel.next; head != sentinel; head = head.next) {
            System.out.print(head.value);
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }

        Node<T> first = sentinel.next;
        first.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;

        if (size == 0) {
            sentinel.prev = sentinel;
        }
        return first.value;
    }

    @Override
    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }

        Node<T> last = sentinel.prev;
        last.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;

        if (size == 0) {
            sentinel.next = sentinel;
        }
        return last.value;
    }

    @Override
    public T get(int index) {
        if (index >= size || index <= -size) {
            return null;
        }

        Node<T> head = index >= 0 ? sentinel.next : sentinel.prev;
        if (index >= 0) {
            while (index > 0) {
                head = head.next;
                index -= 1;
            }
        } else {
            while (index < 0) {
                head = head.prev;
                index += 1;
            }
        }
        return head.value;
    }

    public T getRecursive(int index) {
        if (index >= size || index <= -size) {
            return null;
        }

        return getRecursive(index, sentinel.next);
    }

    private T getRecursive(int index, Node<T> node) {
        if (node == null) {
            return null;
        } else if (index == 0) {
            return node.value;
        } else if (index > 0) {
            return getRecursive(index - 1, node.next);
        } else {
            return getRecursive(index + 1, node.prev);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        } else if (obj instanceof Deque && size() == ((Deque<?>) obj).size()) {
            for (int i = 0; i < size(); i += 1) {
                if (!get(i).equals(((Deque<?>) obj).get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node<T> head = sentinel;

        @Override
        public boolean hasNext() {
            return head.next != sentinel;
        }

        @Override
        public T next() {
            head = head.next;
            return head.value;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }
}
