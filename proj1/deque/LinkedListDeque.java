package deque;

public class LinkedListDeque<T> {
    private class Node<T> {
        T value;
        Node<T> prev;
        Node<T> next;

        public Node(T value) {
            this.value = value;
        }

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        public Node(T value, Node<T> prev, Node<T> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node<>(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node<T> newFirst = new Node<>(item, sentinel, sentinel.next);
        sentinel.next.prev = newFirst;
        sentinel.next = newFirst;
        if (size == 0) {
            sentinel.prev = newFirst;
        }
        size += 1;
    }

    public void addLast(T item) {
        Node<T> newLast = new Node<>(item, sentinel.prev, sentinel);
        sentinel.prev.next = newLast;
        sentinel.prev = newLast;
        if (size == 0) {
            sentinel.next = newLast;
        }
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (Node<T> head = sentinel.next; head != sentinel; head = head.next) {
            System.out.print(head.value);
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }

        Node<T> first = sentinel.next;
        sentinel.next = sentinel.next.next;
        size -= 1;

        if (size == 0) {
            sentinel.prev = sentinel;
        }
        return first.value;
    }

    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }

        Node<T> last = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;

        if (size == 0) {
            sentinel.next = sentinel;
        }
        return last.value;
    }

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
}
