package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class IntNode {
        T item;
        IntNode prev;
        IntNode next;

        public IntNode(T i, IntNode p, IntNode n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private IntNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new IntNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T x) {
        IntNode firstNode = new IntNode(x, sentinel, sentinel.next);
        sentinel.next.prev = firstNode;
        sentinel.next = firstNode;
        size += 1;
    }

    public void addLast(T x) {
        IntNode lastNode = new IntNode(x, sentinel.prev, sentinel);
        sentinel.prev.next = lastNode;
        sentinel.prev = lastNode;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        IntNode pointer = sentinel;
        while (!isEmpty()) {
            System.out.print(pointer.next.item + " ");
            pointer = pointer.next;
            size -= 1;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (!isEmpty()) {
            IntNode p = sentinel.next;
            sentinel.next = p.next;
            p.next.prev = sentinel;
            size -= 1;
            return p.item;
        }
        return null;
    }

    public T removeLast() {
        if (!isEmpty()) {
            IntNode p = sentinel.prev;
            sentinel.prev = p.prev;
            p.prev.next = sentinel;
            size -= 1;
            return p.item;
        }
        return null;
    }

    public T get(int index) {
        IntNode p = sentinel;
        for (int i = 0; i < size; i++) {
            if (i == index) {
                return p.next.item;
            }
            p = p.next;
        }
        return null;
    }

    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }
    // Helper method to implement Recursive method.
    public T getRecursiveHelper(IntNode p, int index) {
        if (p == sentinel) {
            return null;
        }
        if (index == 0) {
            return p.item;
        }
        return getRecursiveHelper(p.next, index - 1);
    }

    public Iterator<T> iterator() {
        return new LLDIterator();
    }
    private class LLDIterator implements Iterator<T> {
        int pos;
        public LLDIterator() {
            pos = 0;
        }

        public boolean hasNext() {
            return pos < size;
        }

        public T next() {
            T returnitem = get(pos);
            pos += 1;
            return returnitem;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }

        Deque<T> other = (Deque<T>) o;
        if (other.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < other.size(); i += 1) {
            if (!other.get(i).equals(this.get(i))) {
                return false;
            }
        }
        return true;
    }

}
