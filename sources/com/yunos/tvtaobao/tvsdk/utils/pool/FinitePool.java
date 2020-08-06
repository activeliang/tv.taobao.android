package com.yunos.tvtaobao.tvsdk.utils.pool;

import com.yunos.tvtaobao.tvsdk.utils.pool.Poolable;
import com.zhiping.dev.android.logger.ZpLogger;

class FinitePool<T extends Poolable<T>> implements Pool<T> {
    private static final String LOG_TAG = "FinitePool";
    private final boolean mInfinite;
    private final int mLimit;
    private final PoolableManager<T> mManager;
    private int mPoolCount;
    private T mRoot;

    FinitePool(PoolableManager<T> manager) {
        this.mManager = manager;
        this.mLimit = 0;
        this.mInfinite = true;
    }

    FinitePool(PoolableManager<T> manager, int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("The pool limit must be > 0");
        }
        this.mManager = manager;
        this.mLimit = limit;
        this.mInfinite = false;
    }

    public T acquire() {
        T element;
        if (this.mRoot != null) {
            element = this.mRoot;
            this.mRoot = (Poolable) element.getNextPoolable();
            this.mPoolCount--;
        } else {
            element = this.mManager.newInstance();
        }
        if (element != null) {
            element.setNextPoolable(null);
            element.setPooled(false);
            this.mManager.onAcquired(element);
        }
        return element;
    }

    public void release(T element) {
        if (!element.isPooled()) {
            if (this.mInfinite || this.mPoolCount < this.mLimit) {
                this.mPoolCount++;
                element.setNextPoolable(this.mRoot);
                element.setPooled(true);
                this.mRoot = element;
            }
            this.mManager.onReleased(element);
            return;
        }
        ZpLogger.w(LOG_TAG, "Element is already in pool: " + element);
    }
}
