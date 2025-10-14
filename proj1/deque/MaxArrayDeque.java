package deque;

import java.util.Comparator;

public class MaxArrayDeque<Item> extends ArrayDeque<Item>{
    private Comparator<Item> cmp;

    public MaxArrayDeque(Comparator<Item> c) {
        super();
        cmp = c;
   }

    public Item max() {
        if (isEmpty()) {
            return null;
        }
        int index = NextIndex(nextFirst);
        Item maxItem = items[index];
        for (int i = 0; i < size; i += 1) {
            if (cmp.compare(items[index], maxItem) > 0) {
                maxItem = items[index];
            }
            index = NextIndex(index);
        }
        return maxItem;
    }

    public Item max(Comparator<Item> c) {
        if (isEmpty()) {
            return null;
        }
        int index = NextIndex(nextFirst);
        Item maxItem = items[index];
        for (int i = 0; i < size; i += 1) {
            if (c.compare(items[index], maxItem) > 0) {
                maxItem = items[index];
            }
            index = NextIndex(index);
        }
        return maxItem;
    }
}
