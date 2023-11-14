package DSA;

import java.util.LinkedList;

class HashTable {
    private static final int SIZE = 10;
    private LinkedList<Node>[] array = new LinkedList[SIZE];

    // Node class to store key-value pairs
    static class Node {
        String key;
        String value;

        Node(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // Hash function to calculate index
    private int getHashIndex(String key) {
        int hashCode = key.hashCode();
        int index = hashCode % SIZE;
        return index;
    }

    // Insert key-value pair into the hash table
    public void put(String key, String value) {
        int index = getHashIndex(key);
        if (array[index] == null) {
            array[index] = new LinkedList<>();
        }
        for (Node node : array[index]) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        array[index].add(new Node(key, value));
    }

    // Retrieve value for a given key
    public String get(String key) {
        int index = getHashIndex(key);
        LinkedList<Node> nodes = array[index];
        if (nodes == null) return null;
        for (Node node : nodes) {
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
            LinkedList<Node> nodes = array[i];
            if (nodes != null) {
                for (Node node : nodes) {
                    System.out.print("[Key: " + node.key + ", Value: " + node.value + "] ");
                }
            }
            System.out.println();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        HashTable hashTable = new HashTable();
        hashTable.put("key1", "value1");
        hashTable.put("key2", "value2");
        hashTable.put("key3", "value3");
        hashTable.put("key4", "value3");
        hashTable.put("key5", "value3");
        hashTable.put("key6", "value3");
        hashTable.put("key7", "value3");
        hashTable.put("key8", "value3");
        hashTable.put("key9", "value3");
        hashTable.put("key10", "value3");
        hashTable.put("key11", "value3");

        System.out.println("Value for key1: " + hashTable.get("key1"));
        hashTable.display();
    }
}
