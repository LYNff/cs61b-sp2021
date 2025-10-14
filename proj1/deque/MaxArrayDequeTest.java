package deque;

import org.junit.Test;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {
    private static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer a, Integer b) {
            return a.compareTo(b);
        }
    }
    public static Comparator<Integer> getIntegerComparator() {
        return new IntegerComparator();
    }

    private static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    }
    public static Comparator<String> getStringComparator() {
        return new StringComparator();
    }
    @Test
    public void testMaxArrayDequeInteger() {
        Comparator<Integer> IntComparator = getIntegerComparator();
        MaxArrayDeque<Integer> IntArray = new MaxArrayDeque(IntComparator);
        IntArray.addFirst(1);
        IntArray.addFirst(2);
        IntArray.addFirst(3);
        IntArray.addFirst(4);
        IntArray.addFirst(5);

        // Compare
        int maxInt = IntArray.max();
        assertEquals(5, maxInt);
    }
    @Test
    public void testMaxArrayDequeString() {
        Comparator<String> StringComparator = getStringComparator();
        MaxArrayDeque<String> StringArray = new MaxArrayDeque<>(StringComparator);
        StringArray.addFirst("students");
        StringArray.addFirst("teacher");
        StringArray.addFirst("parent");
        StringArray.addFirst("human");
        StringArray.addFirst("animal");

        // Compare
        String maxString = StringArray.max();
        assertEquals("teacher", maxString);
    }
}
