package deque;

public class ArrayDeque<Item> implements Deque<Item> {
    Item[] items;
    int nextFirst;
    int nextLast;
    int size;

    public ArrayDeque() {
        items = (Item[]) new Object[8];
        nextFirst = 2;
        nextLast = 3;
        size = 0;
    }

    public ArrayDeque(int capacity) {
        items = (Item[]) new Object[capacity];
        nextFirst = 2;
        nextLast = 3;
        size = 0;
    }

    public void resize(int newSize) {
        Item[] a =  (Item[]) new Object[newSize];
        int first = NextIndex(nextFirst);
        for (int j = 0; j < size; j++) {
            a[j] = items[(first + j) % items.length];
        }
        nextFirst = newSize - 1;
        nextLast = size;
        items = a;
    }

    // Helper method to get the next index in range items.length.
    public int NextIndex(int index) {
        return (index + 1) % items.length;
    }
    // Helper method to get the front index in range items.length.
    public int FrontIndex(int index) {
        return (index - 1 + items.length) % items.length;
    }

    @Override
    public void addFirst(Item item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = FrontIndex(nextFirst);
        size += 1;
    }

    @Override
    public void addLast(Item item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = NextIndex(nextLast);
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
        int first = NextIndex(nextFirst);
        for (int i = 0; i < size; i++) {
            System.out.print(items[(first + i) % items.length] + " ");
        }
        System.out.println();
    }

    @Override
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = NextIndex(nextFirst);
        Item first = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        if (items.length >= 16 && size * 4 < items.length) {
            resize(items.length / 2);
        }
        return first;
    }

    @Override
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast = FrontIndex(nextLast);
        Item last = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        if (items.length >= 16 && size < items.length / 4) {
            resize(items.length / 2);
        }
        return last;
    }

    @Override
    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int first = NextIndex(nextFirst);
        int finalIndex = (first + index) % items.length;
        return items[finalIndex];
    }

}
