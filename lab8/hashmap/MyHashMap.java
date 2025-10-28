package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private HashSet<K> hashkeySet = new HashSet<>();
    private int initialSize = 16;
    private double loadFactor = 0.75;
    /** Constructors */
    public MyHashMap() {
        buckets = createTable(initialSize);
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        buckets = createTable(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.loadFactor = maxLoad;
        buckets = createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        Arrays.fill(buckets, null);
        hashkeySet.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return hashkeySet.contains(key);
    }

    @Override
    public V get(K key) {
        Node node = getNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }
    private Node getNode(K key) {
        if (!containsKey(key))
            return null;
        Collection<Node> bucket = buckets[Math.floorMod(key.hashCode(), initialSize)];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return hashkeySet.size();
    }

    @Override
    public void put(K key, V value) {
        // If key exists than change the value.
        if (containsKey(key)) {
            Node node = getNode(key);
            node.value = value;
            return;
        }
        double factor = 1.0 * hashkeySet.size() / initialSize;
        if (factor > loadFactor) {
            resizing(initialSize * 2);
            initialSize *= 2;
        }
        int hashcode = Math.floorMod(key.hashCode(), initialSize);
        if (buckets[hashcode] == null) {
            buckets[hashcode] = createBucket();
        }
        hashkeySet.add(key);
        buckets[hashcode].add(createNode(key, value));
    }
    private void resizing(int newSize) {
        Collection<Node>[] newBuckets = createTable(newSize);
        for (K key : hashkeySet) {
            Node node = getNode(key);
            int hashcode = Math.floorMod(key.hashCode(), newSize);
            if (newBuckets[hashcode] == null) {
                newBuckets[hashcode] = createBucket();
            }
            newBuckets[hashcode].add(createNode(key, node.value));
        }
        buckets = newBuckets;
    }

    @Override public Set<K> keySet() {
        return hashkeySet;
    }
    @Override public Iterator<K> iterator() {
        return hashkeySet.iterator();
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        Collection<Node> bucket = buckets[Math.floorMod(key.hashCode(), initialSize)];
        Node node = getNode(key);
        bucket.remove(node);
        hashkeySet.remove(key);
        return node.value;
    }

    @Override
    public V remove(K key, V value) {
        if (!containsKey(key)) {
            return null;
        }
        Node node = getNode(key);
        if (node.value.equals(value)) {
            return remove(key);
        }
        return null;
    }
}
