package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> cmp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        cmp = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }
        T maxVal = get(0);
        for (int i = 1; i < size(); i += 1) {
            if (cmp.compare(maxVal, get(i)) < 0) {
                maxVal = get(i);
            }
        }
        return maxVal;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T maxVal = get(0);
        for (int i = 1; i < size(); i += 1) {
            if (c.compare(maxVal, get(i)) < 0) {
                maxVal = get(i);
            }
        }
        return maxVal;

    }
}
