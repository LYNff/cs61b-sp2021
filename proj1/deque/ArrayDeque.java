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

    public void resize(int newSize) {
        Item[] a =  (Item[]) new Object[newSize];
        int first = NextIndex(nextFirst);
        int last = FrontIndex(nextLast);
        for (int i = first, j = 0; i != last; i = NextIndex(i), j += 1) {
            a[j] = items[i];
        }
        items = a;
    }

    // Helper method to get the next index in range items.length.
    public int NextIndex(int index) {
        return (index + 1) % items.length;
    }
    // Helper method to get the front index in range items.length.
    public int FrontIndex(int index) {
        return (index - 1) % items.length;
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
        for (int i = 0; i < size; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    @Override
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Item First = items[NextIndex(nextFirst)];
        items[NextIndex(nextFirst)] = null;
        nextFirst = NextIndex(nextFirst);
        size -= 1;
        if (items.length >= 16 && size * 4 < items.length) {
            resize(items.length / 4);
        }
        return First;
    }

    @Override
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        Item last = items[FrontIndex(nextLast)];
        items[NextIndex(nextLast)] = null;
        nextLast = NextIndex(nextLast);
        size -= 1;
        if (items.length >= 16 && size * 4 < items.length) {
            resize(items.length / 4);
        }
        return last;
    }


    @Override
    public Item get(int index) {
        return items[index];
    }


}
