package deque;

public class MaxArrayDeque<Item> {
    ArrayDeque<Item> maxAD;

    public MaxArrayDeque() {
        maxAD = new ArrayDeque<> ();
    }

    public void addFirst(Item item) {
        maxAD.addFirst(item);
    }

    public void addLast(Item item) {
        maxAD.addLast(item);
    }

    public boolean isEmpty() {
        return maxAD.isEmpty();
    }

    public int size() {
        return maxAD.size();
    }

    public void printDeque() {
        maxAD.printDeque();
    }

    public Item removeFirst() {
        return maxAD.removeFirst();
    }

    public Item removeLast() {
        return maxAD.removeLast();
    }

    public Item get(int index) {
        return maxAD.get(index);
    }


}
