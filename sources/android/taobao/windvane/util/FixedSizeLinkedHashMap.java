package android.taobao.windvane.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class FixedSizeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 2230704826523879449L;
    private Object lock = new Object();
    private long maxSize = 10;

    public FixedSizeLinkedHashMap() {
    }

    public FixedSizeLinkedHashMap(long size) {
        this.maxSize = size;
    }

    public void setMaxSize(long maxSize2) {
        this.maxSize = maxSize2;
    }

    public long getMaxSize() {
        return this.maxSize;
    }

    /* access modifiers changed from: protected */
    public boolean removeEldestEntry(Map.Entry<K, V> entry) {
        return ((long) size()) > this.maxSize;
    }

    public V get(Object key) {
        V v;
        synchronized (this.lock) {
            v = super.get(key);
        }
        return v;
    }

    public V put(K key, V value) {
        V put;
        synchronized (this.lock) {
            put = super.put(key, value);
        }
        return put;
    }
}
