package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private class BSTNode {
        K key;
        V value;
        BSTNode left, right;
        int size;

        private BSTNode(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }
    private BSTNode root;

    public BSTMap() {
        root = null;
    }
    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        BSTNode node = getNode(root, key);
        return node != null;
    }

    @Override
    public V get(K key) {
        BSTNode node = getNode(root, key);
        if (node == null) {
            return null;
        }
        return node.value;
    }
    private BSTNode getNode(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return getNode(node.left, key);
        }
        else if (cmp > 0) {
            return getNode(node.right, key);
        }
        else {
            return node;
        }
    }

    @Override
    public int size() {
        return size(root);
    }
    private int size(BSTNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }
    private BSTNode put(BSTNode node, K key, V value) {
        if (node == null) {
            node = new BSTNode(key, value, 1);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        }
        else if (cmp > 0) {
            node.right = put(node.right, key, value);
        }
        else {
            node.value = value;
        }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    public void printInOrder() {
            }

    @Override
    public Set<K> keySet() {
        Set<K> set = new TreeSet<>();
        for (K key : this) {
            set.add(key);
        }
        return set;
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        V value = get(key);
        root = afterRemove(root, key);
        return value;
    }
    private BSTNode afterRemove(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = afterRemove(node.left, key);
        }
        else if (cmp > 0) {
            node.right = afterRemove(node.right, key);
        }
        // Delete root node.
        else {
            if (node.right == null) {
                return node.left;
            }
            if (node.left == null) {
                return node.right;
            }
            BSTNode t = node;
            // Find the smallest node in the right branch.
            // Then swep the key and the value of the smallest node with root.
            node = min(t.right);
            // Delete the node value and key.
            node.right = removeMin(t.right);
            node.left = t.left;
        }
        return node;
    }
    private BSTNode min(BSTNode x) {
        if (x.left == null) {
            return x;
        }
        return min(x.left);
    }
    private BSTNode removeMin(BSTNode x) {
        if (x.left == null) {
            return x.right;
        }
        x.left = removeMin(x.left);
        x.size = size(x.left) + size(x.right) - 1;
        return x;
    }

    @Override
    public V remove(K key, V value) {
        BSTNode node = getNode(root, key);
        if (node == null || !node.value.equals(value)) {
            return null;
        }
        root = afterRemove(root, key);
        return value;
    }

    @Override
    public Iterator<K> iterator() {
        return new launchIterator();
    }
    private class launchIterator implements Iterator<K> {
        Stack<BSTNode> stack = new Stack<>();

        // Turn left from root until meet null.
        private void pushLeftNode(BSTNode root) {
            BSTNode node = root;
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }
        public launchIterator() {
            pushLeftNode(root);
        }
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        @Override
        public K next() {
            BSTNode node = stack.pop();

            if (node.right != null) {
                pushLeftNode(node.right);
            }
            return node.key;
        }
    }
}
