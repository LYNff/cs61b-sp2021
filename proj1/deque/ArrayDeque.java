package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;

    private static final int MIN_RESING = 16;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 2;
        nextLast = 3;
        size = 0;
    }

    private void resize(int newSize) {
        T[] a =  (T[]) new Object[newSize];
        int first = nextIndex(nextFirst);
        for (int j = 0; j < size; j++) {
            a[j] = items[(first + j) % items.length];
        }
        nextFirst = newSize - 1;
        nextLast = size;
        items = a;
    }

    // Helper method to get the next index in range items.length.
    private int nextIndex(int index) {
        return (index + 1) % items.length;
    }
    // Helper method to get the front index in range items.length.
    private int frontIndex(int index) {
        return (index - 1 + items.length) % items.length;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = frontIndex(nextFirst);
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = nextIndex(nextLast);
        size += 1;
    }

    /**
     * public boolean isEmpty() {
        return size == 0;
     }
     */

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int first = nextIndex(nextFirst);
        for (int i = 0; i < size; i++) {
            System.out.print(items[(first + i) % items.length] + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = nextIndex(nextFirst);
        T first = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        if (items.length >= MIN_RESING && size * 4 < items.length) {
            resize(items.length / 2);
        }
        return first;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast = frontIndex(nextLast);
        T last = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        if (items.length >= MIN_RESING && size < items.length / 4) {
            resize(items.length / 2);
        }
        return last;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int first = nextIndex(nextFirst);
        int finalIndex = (first + index) % items.length;
        return items[finalIndex];
    }

    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {
        private int pos;

        ArrayIterator() {
            pos = 0;
        }

        public boolean hasNext() {
            return pos < size;
        }

        public T next() {
            T returnT = get(pos);
            pos += 1;
            return returnT;
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
