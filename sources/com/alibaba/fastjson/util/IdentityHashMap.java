package com.alibaba.fastjson.util;

public class IdentityHashMap<K, V> {
    private final Entry<K, V>[] buckets;
    private final int indexMask;

    public IdentityHashMap() {
        this(1024);
    }

    public IdentityHashMap(int tableSize) {
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
    }

    public final V get(K key) {
        for (Entry<K, V> entry = this.buckets[System.identityHashCode(key) & this.indexMask]; entry != null; entry = entry.next) {
            if (key == entry.key) {
                return entry.value;
            }
        }
        return null;
    }

    public Class findClass(String keyString) {
        for (Entry<K, V> bucket : this.buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry = bucket; entry != null; entry = entry.next) {
                    K k = bucket.key;
                    if (k instanceof Class) {
                        Class clazz = (Class) k;
                        if (clazz.getName().equals(keyString)) {
                            return clazz;
                        }
                    }
                }
                continue;
            }
        }
        return null;
    }

    public boolean put(K key, V value) {
        int hash = System.identityHashCode(key);
        int bucket = hash & this.indexMask;
        for (Entry<K, V> entry = this.buckets[bucket]; entry != null; entry = entry.next) {
            if (key == entry.key) {
                entry.value = value;
                return true;
            }
        }
        this.buckets[bucket] = new Entry<>(key, value, hash, this.buckets[bucket]);
        return false;
    }

    protected static final class Entry<K, V> {
        public final int hashCode;
        public final K key;
        public final Entry<K, V> next;
        public V value;

        public Entry(K key2, V value2, int hash, Entry<K, V> next2) {
            this.key = key2;
            this.value = value2;
            this.next = next2;
            this.hashCode = hash;
        }
    }
}
