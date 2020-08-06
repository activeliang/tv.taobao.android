package android.taobao.atlas.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BundleLock {
    static Map<String, ReentrantReadWriteLock> bundleIdentifierMap = new HashMap();

    public static void WriteLock(String bundle) {
        synchronized (bundleIdentifierMap) {
            ReentrantReadWriteLock lock = bundleIdentifierMap.get(bundle);
            if (lock == null) {
                ReentrantReadWriteLock lock2 = new ReentrantReadWriteLock();
                try {
                    bundleIdentifierMap.put(bundle, lock2);
                    lock = lock2;
                } catch (Throwable th) {
                    th = th;
                    ReentrantReadWriteLock reentrantReadWriteLock = lock2;
                    throw th;
                }
            }
            try {
                lock.writeLock().lock();
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public static void WriteUnLock(String bundle) {
        synchronized (bundleIdentifierMap) {
            ReentrantReadWriteLock lock = bundleIdentifierMap.get(bundle);
            if (lock != null) {
                lock.writeLock().unlock();
            }
        }
    }

    public static boolean ReadLock(String bundle) {
        synchronized (bundleIdentifierMap) {
            ReentrantReadWriteLock lock = bundleIdentifierMap.get(bundle);
            if (lock == null) {
                ReentrantReadWriteLock lock2 = new ReentrantReadWriteLock();
                try {
                    bundleIdentifierMap.put(bundle, lock2);
                    lock = lock2;
                } catch (Throwable th) {
                    th = th;
                    ReentrantReadWriteLock reentrantReadWriteLock = lock2;
                    throw th;
                }
            }
            try {
                try {
                    return lock.readLock().tryLock(3, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    return false;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public static void ReadUnLock(String bundle) {
        synchronized (bundleIdentifierMap) {
            ReentrantReadWriteLock lock = bundleIdentifierMap.get(bundle);
            if (lock != null) {
                lock.readLock().unlock();
            }
        }
    }
}
