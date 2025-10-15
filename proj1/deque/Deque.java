package deque;

import java.util.Iterator;

public interface Deque<Item> {
    void addFirst(Item item);
    void addLast(Item item);

    default boolean isEmpty() {
        return size() == 0;
    }

    int size();
    void printDeque();
    Item removeFirst();
    Item removeLast();
    Item get(int index);
    boolean equals(Object o);
    Iterator<Item> iterator();
}
