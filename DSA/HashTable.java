package DSA;

import java.util.LinkedList;

public class HashTable<K, V> {
    private static final int SIZE = 10;
    private LinkedList<Node<K, V>>[] array = new LinkedList[SIZE];

    // Node class for storing key-value pairs
    static class Node<K, V> {
        K key;
        V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Hash function to calculate the index
    private int getHashIndex(K key) {
        int hashCode = key.hashCode();
        return Math.abs(hashCode % SIZE);
    }

    // Insert key-value pair into the hash table
    public void put(K key, V value) {
        int index = getHashIndex(key);
        if (array[index] == null) {
            array[index] = new LinkedList<>();
        }

        // Check if key already exists
        for (Node<K, V> node : array[index]) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        array[index].add(new Node<>(key, value));
    }

    // Retrieve the value for a given key
    public V get(K key) {
        int index = getHashIndex(key);
        LinkedList<Node<K, V>> nodes = array[index];
        if (nodes == null) {
            return null;
        }

        for (Node<K, V> node : nodes) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }

        return null;
    }

    // Display the hash table contents
    public void display() {
        for (int i = 0; i < SIZE; i++) {
            System.out.print("Bucket " + i + ": ");
            LinkedList<Node<K, V>> nodes = array[i];
            if (nodes != null) {
                for (Node<K, V> node : nodes) {
                    System.out.print("[Key: " + node.key + ", Value: " + node.value + "] ");
                }
            }
            System.out.println();
        }
    }

    // Example usage
    public static void main(String[] args) {
        HashTable<String, String> hashTable = new HashTable<>();
        hashTable.put("key1", "value1");
        hashTable.put("key2", "value2");
        hashTable.put("key3", "value3");

        System.out.println("Value for key1: " + hashTable.get("key1"));
        hashTable.display();
    }
}
