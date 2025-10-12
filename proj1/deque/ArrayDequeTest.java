package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.ArrayDeque;
import java.util.Optional;

public class ArrayDequeTest {
    @Test
    public void testRandom() {
        deque.ArrayDeque<Integer> student = new deque.ArrayDeque<>();
        ArrayDeque<Integer> solution = new ArrayDeque<>();

        int n = 5000;
        for (int i = 0; i < n; i += 1) {
            double choice = StdRandom.uniform(0, 3);
            Integer randVal = StdRandom.uniform(0, 100);

            if (choice == 0) {
                student.addLast(randVal);
                solution.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            }
            else if (choice == 1) {
                student.addFirst(randVal);
                solution.addFirst(randVal);
                System.out.println("addFirst(" + randVal + ")");
            } else {
                int size = student.size();
                System.out.println("size()");
                if (size > 0) {
                    if (randVal < 50) {
                        System.out.println("removeFirst()");
                        assertEquals(solution.removeFirst(), student.removeFirst());
                    } else {
                        System.out.println("removeLast()");
                        assertEquals(solution.removeLast(), student.removeLast());
                    }
                }
            }
        }
    }

    @Test
    public void testget() {
        deque.ArrayDeque<Integer> student = new deque.ArrayDeque<>();
        for (int i = 0; i < 10; i += 1) {
            student.addLast(i);
        }
        for (int i = 0; i < 10; i += 1) {
            int trueNum = student.get(i);
            assertEquals(i, trueNum);
        }
    }

    @Test
    public void testReSize() {
        deque.ArrayDeque<Integer> student = new deque.ArrayDeque<>();
        ArrayDeque<Integer> solution = new ArrayDeque<>();
        student.addFirst(1);
        solution.addFirst(1);
        student.addFirst(2);
        solution.addFirst(2);
        assertEquals(solution.removeFirst(), student.removeFirst());
        assertEquals(solution.removeFirst(), student.removeFirst());
    }

    @Test
    public void testReSize2() {
        deque.ArrayDeque<Integer> student = new deque.ArrayDeque<>();
        ArrayDeque<Integer> solution = new ArrayDeque<>();
        student.addLast(0);
        student.addLast(1);
        solution.addLast(0);
        solution.addLast(1);
        assertEquals(solution.removeFirst(), student.removeFirst());
        solution.addLast(3);
        solution.addLast(4);
        solution.addLast(5);
        solution.addLast(6);
        solution.addLast(7);
        solution.addLast(8);
        student.addLast(3);
        student.addLast(4);
        student.addLast(5);
        student.addLast(6);
        student.addLast(7);
        student.addLast(8);
        assertEquals(solution.removeFirst(), student.removeFirst());
    }
}
