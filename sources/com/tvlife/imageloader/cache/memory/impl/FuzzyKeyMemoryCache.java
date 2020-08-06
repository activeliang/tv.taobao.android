package com.tvlife.imageloader.cache.memory.impl;

import com.tvlife.imageloader.cache.memory.MemoryCacheAware;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class FuzzyKeyMemoryCache<K, V> implements MemoryCacheAware<K, V> {
    private final MemoryCacheAware<K, V> cache;
    private final Comparator<K> keyComparator;

    public FuzzyKeyMemoryCache(MemoryCacheAware<K, V> cache2, Comparator<K> keyComparator2) {
        this.cache = cache2;
        this.keyComparator = keyComparator2;
    }

    public boolean put(K key, V value) {
        synchronized (this.cache) {
            K keyToRemove = null;
            Iterator<K> it = this.cache.keys().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                K cacheKey = it.next();
                if (this.keyComparator.compare(key, cacheKey) == 0) {
                    keyToRemove = cacheKey;
                    break;
                }
            }
            if (keyToRemove != null) {
                this.cache.remove(keyToRemove);
            }
        }
        return this.cache.put(key, value);
    }

    public V get(K key) {
        return this.cache.get(key);
    }

    public void remove(K key) {
        this.cache.remove(key);
    }

    public void clear() {
        this.cache.clear();
    }

    public Collection<K> keys() {
        return this.cache.keys();
    }
}
