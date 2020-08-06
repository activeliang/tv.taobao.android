package com.yunos.tvtaobao.tvsdk.utils.pool;

import com.yunos.tvtaobao.tvsdk.utils.pool.Poolable;

public interface Pool<T extends Poolable<T>> {
    T acquire();

    void release(T t);
}
