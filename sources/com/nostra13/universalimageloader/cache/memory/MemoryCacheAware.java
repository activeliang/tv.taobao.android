package com.nostra13.universalimageloader.cache.memory;

import java.util.Collection;

public interface MemoryCacheAware<K, V> {
    void clear();

    V get(K k);

    Collection<K> keys();

    boolean put(K k, V v);

    void remove(K k);
}
