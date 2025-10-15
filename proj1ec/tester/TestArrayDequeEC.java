package tester;

import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    private static final int RANDOM_VAL = 100;
    private static final int TEST_TIMES =  5000;
    @Test
    public void testRandom() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<Integer>();

        for (int i = 0; i < TEST_TIMES; i += 1) {
            int choice = StdRandom.uniform(0, 6);
            int randVal = StdRandom.uniform(0, RANDOM_VAL);

            if (choice == 0) {
                student.addFirst(randVal);
                solution.addFirst(randVal);
                System.out.println("addFirst(" + randVal + ")");
            } else if (choice == 1) {
                student.addLast(randVal);
                solution.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (choice == 2) {
                assertEquals(solution.size(), student.size());
            } else if (choice == 3) {
                if (student.isEmpty()) {
                    continue;
                }
                System.out.println("removeFirst()");
                assertEquals(solution.removeFirst(), student.removeFirst());
            } else if (choice == 4) {
                if (student.isEmpty()) {
                    continue;
                }
                System.out.println("removeLast()");
                assertEquals(solution.removeLast(), student.removeLast());
            } else if (choice == 5) {
                for (int j = 0; j < student.size(); j += 1) {
                    assertEquals(solution.get(j), student.get(j));
                }
            }
        }
    }
}
