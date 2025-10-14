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
        Item maxVal = get(0);
        for (int i = 1; i < size(); i += 1) {
            if (cmp.compare(maxVal, get(i)) < 0) {
                maxVal = get(i);
            }
        }
        return maxVal;
    }

    public Item max(Comparator<Item> c) {
        if (isEmpty()) {
            return null;
        }
        Item maxVal = get(0);
        for (int i = 1; i < size(); i += 1) {
            if (c.compare(maxVal, get(i)) < 0) {
                maxVal = get(i);
            }
        }
        return maxVal;

    }
}
