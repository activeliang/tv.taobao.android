package anetwork.channel.cache;

import anet.channel.util.ALog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheManager {
    private static List<CacheItem> cacheList = new ArrayList();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    private static class CacheItem implements Comparable<CacheItem> {
        final Cache cache;
        final CachePrediction prediction;
        final int priority;

        CacheItem(Cache cache2, CachePrediction prediction2, int priority2) {
            this.cache = cache2;
            this.prediction = prediction2;
            this.priority = priority2;
        }

        public int compareTo(CacheItem o) {
            return this.priority - o.priority;
        }
    }

    public static void addCache(Cache cache, CachePrediction prediction, int priority) {
        if (cache == null) {
            try {
                throw new IllegalArgumentException("cache is null");
            } catch (Throwable th) {
                writeLock.unlock();
                throw th;
            }
        } else if (prediction == null) {
            throw new IllegalArgumentException("prediction is null");
        } else {
            writeLock.lock();
            cacheList.add(new CacheItem(cache, prediction, priority));
            Collections.sort(cacheList);
            writeLock.unlock();
        }
    }

    public static void removeCache(Cache cache) {
        try {
            writeLock.lock();
            ListIterator<CacheItem> itor = cacheList.listIterator();
            while (true) {
                if (itor.hasNext()) {
                    if (itor.next().cache == cache) {
                        itor.remove();
                        break;
                    }
                } else {
                    break;
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    public static Cache getCache(String url, Map<String, String> requestHeaders) {
        try {
            readLock.lock();
            for (CacheItem cacheItem : cacheList) {
                if (cacheItem.prediction.handleCache(url, requestHeaders)) {
                    return cacheItem.cache;
                }
            }
            readLock.unlock();
            return null;
        } finally {
            readLock.unlock();
        }
    }

    public static void clearAllCache() {
        ALog.w("anet.CacheManager", "clearAllCache", (String) null, new Object[0]);
        for (CacheItem cacheItem : cacheList) {
            try {
                cacheItem.cache.clear();
            } catch (Exception e) {
            }
        }
    }
}
