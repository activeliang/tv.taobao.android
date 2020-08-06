package com.yunos.tvtaobao.tvsdk.utils.pool;

public interface Poolable<T> {
    T getNextPoolable();

    boolean isPooled();

    void setNextPoolable(T t);

    void setPooled(boolean z);
}
