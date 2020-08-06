package com.nostra13.universalimageloader.cache.memory;

import com.nostra13.universalimageloader.utils.L;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LimitedMemoryCache<K, V> extends BaseMemoryCache<K, V> {
    private static final int MAX_NORMAL_CACHE_SIZE = 16777216;
    private static final int MAX_NORMAL_CACHE_SIZE_IN_MB = 16;
    private final AtomicInteger cacheSize;
    private final List<V> hardCache = Collections.synchronizedList(new LinkedList());
    private final int sizeLimit;

    /* access modifiers changed from: protected */
    public abstract int getSize(V v);

    /* access modifiers changed from: protected */
    public abstract V removeNext();

    public LimitedMemoryCache(int sizeLimit2) {
        this.sizeLimit = sizeLimit2;
        this.cacheSize = new AtomicInteger();
        if (sizeLimit2 > 16777216) {
            L.w("You set too large memory cache size (more than %1$d Mb)", 16);
        }
    }

    public boolean put(K key, V value) {
        boolean putSuccessfully = false;
        int valueSize = getSize(value);
        int sizeLimit2 = getSizeLimit();
        int curCacheSize = this.cacheSize.get();
        if (valueSize < sizeLimit2) {
            while (curCacheSize + valueSize > sizeLimit2) {
                V removedValue = removeNext();
                if (this.hardCache.remove(removedValue)) {
                    curCacheSize = this.cacheSize.addAndGet(-getSize(removedValue));
                }
            }
            this.hardCache.add(value);
            this.cacheSize.addAndGet(valueSize);
            putSuccessfully = true;
        }
        super.put(key, value);
        return putSuccessfully;
    }

    public void remove(K key) {
        V value = super.get(key);
        if (value != null && this.hardCache.remove(value)) {
            this.cacheSize.addAndGet(-getSize(value));
        }
        super.remove(key);
    }

    public void clear() {
        this.hardCache.clear();
        this.cacheSize.set(0);
        super.clear();
    }

    /* access modifiers changed from: protected */
    public int getSizeLimit() {
        return this.sizeLimit;
    }
}
