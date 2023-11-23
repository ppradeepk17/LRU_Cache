package test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class LFUCache<K, V> {
    private final int capacity;
    private final Map<K, CacheNode<K, V>> cache;
    private final PriorityQueue<CacheNode<K, V>> minHeap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.minHeap = new PriorityQueue<>((node1, node2) -> Integer.compare(node1.frequency, node2.frequency));
    }

    public V get(K key) {
        if (cache.containsKey(key)) {
            CacheNode<K, V> node = cache.get(key);
            node.frequency++;
            minHeap.remove(node);
            minHeap.offer(node);
            return node.value;
        } else {
            return null; // Key not found
        }
    }

    public void put(K key, V value) {
        if (capacity > 0) {
            if (cache.containsKey(key)) {
                CacheNode<K, V> node = cache.get(key);
                node.value = value;
                node.frequency++;
                minHeap.remove(node);
                minHeap.offer(node);
            } else {
                if (cache.size() == capacity) {
                    evictLFU();
                }
                CacheNode<K, V> newNode = new CacheNode<>(key, value);
                cache.put(key, newNode);
                minHeap.offer(newNode);
            }
        }
    }

    private void evictLFU() {
        if (!minHeap.isEmpty()) {
            CacheNode<K, V> evictedNode = minHeap.poll();
            cache.remove(evictedNode.key);
        }
    }

    static class CacheNode<K, V> {
        private final K key;
        private V value;
        private int frequency;

        public CacheNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.frequency = 1;
        }
    }
}
