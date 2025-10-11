package deque;

public class ArrayDeque<Item> {
    Item[] items;
    int size;

    public ArrayDeque() {
        items = (Item[]) new Object[100];
        size = 0;
    }

    public void resize(int newSize) {
        Item[] a =  (Item[]) new Object[newSize];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }
    public void addFirst(Item item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[size] = item;
        size += 1;
    }

    public void addLast(Item item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[size] = item;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public Item removeFirst() {
        Item[] a =  (Item[]) new Object[size - 1];
        System.arraycopy(items, 1, a, 0, size - 1);
        Item first = items[0];
        items = a;
        size -= 1;
        return first;
    }

    public Item removeLast() {
        Item last = items[size - 1];
        size -= 1;
        return last;
    }

    public Item get(int index) {
        return items[index];
    }


}
