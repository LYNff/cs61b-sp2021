package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> list1 = new AListNoResizing<>();
        BuggyAList<Integer> list2 = new BuggyAList<>();

        list1.addLast(4);
        list1.addLast(5);
        list1.addLast(6);

        list2.addLast(4);
        list2.addLast(5);
        list2.addLast(6);

        assertEquals(list1.size(), list2.size());

        assertEquals(list1.removeLast(), list2.removeLast());
        assertEquals(list1.removeLast(), list2.removeLast());
        assertEquals(list1.removeLast(), list2.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L= new AListNoResizing<>();
        BuggyAList<Integer> list = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i++) {
            int operationNmber = StdRandom.uniform(0, 4);
            if (operationNmber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                list.addLast(randVal);
                // System.out.println("addLast(" + randVal + ")");
            }
            else if (operationNmber == 1) {
                // size
                assertEquals(L.size(), list.size());
            }
            else if (operationNmber == 2) {
                if (L.size() > 0 && list.size() > 0) {
                    assertEquals(L.removeLast(), list.removeLast());
                }
            }
            else if (operationNmber == 3) {
                if (L.size() > 0 && list.size() > 0) {
                    assertEquals(L.getLast(), list.getLast());
                }
            }
        }
    }
}
