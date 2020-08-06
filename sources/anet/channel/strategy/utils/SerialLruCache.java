package anet.channel.strategy.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class SerialLruCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = -4331642331292721006L;
    private int cacheSize;

    public SerialLruCache(int cacheSize2) {
        super(cacheSize2 + 1, 1.0f, true);
        this.cacheSize = cacheSize2;
    }

    /* access modifiers changed from: protected */
    public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if (size() > this.cacheSize) {
            return entryRemoved(eldest);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean entryRemoved(Map.Entry<K, V> entry) {
        return true;
    }
}
