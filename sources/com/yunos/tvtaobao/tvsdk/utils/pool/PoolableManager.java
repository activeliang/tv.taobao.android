package com.yunos.tvtaobao.tvsdk.utils.pool;

import com.yunos.tvtaobao.tvsdk.utils.pool.Poolable;

public interface PoolableManager<T extends Poolable<T>> {
    T newInstance();

    void onAcquired(T t);

    void onReleased(T t);
}
