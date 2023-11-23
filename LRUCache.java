package test;

import java.util.HashMap;
import java.util.Map;

class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> cache;
    private final DoublyLinkedList<K, V> dll;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.dll = new DoublyLinkedList<>();
    }

    public V get(K key) {
        if (cache.containsKey(key)) {
            Node<K, V> node = cache.get(key);
            dll.moveToFront(node);
            return node.value;
        } else {
            return null; // Key not found
        }
    }

    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            Node<K, V> node = cache.get(key);
            node.value = value;
            dll.moveToFront(node);
        } else {
            if (cache.size() == capacity) {
                Node<K, V> removedNode = dll.removeFromEnd();
                cache.remove(removedNode.key);
            }

            Node<K, V> newNode = new Node<>(key, value);
            dll.addToFront(newNode);
            cache.put(key, newNode);
        }
    }
}

class DoublyLinkedList<K, V> {
    private Node<K, V> head;
    private Node<K, V> tail;

    public void addToFront(Node<K, V> node) {
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
    }

    public void moveToFront(Node<K, V> node) {
        if (node == head) {
            // Already at the front
            return;
        }

        if (node == tail) {
            tail = node.prev;
            tail.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        node.prev = null;
        node.next = head;
        head.prev = node;
        head = node;
    }

    public Node<K, V> removeFromEnd() {
        if (tail == null) {
            return null;
        }

        Node<K, V> removedNode = tail;

        if (tail == head) {
            tail = head = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }

        return removedNode;
    }
}

class Node<K, V> {
    public K key;
    public V value;
    public Node<K, V> prev;
    public Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
