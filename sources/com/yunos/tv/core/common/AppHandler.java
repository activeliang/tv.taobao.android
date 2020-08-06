package com.yunos.tv.core.common;

import android.os.Handler;
import java.lang.ref.WeakReference;

public class AppHandler<T> extends Handler {
    private final WeakReference<T> mT;

    public AppHandler(T t) {
        this.mT = new WeakReference<>(t);
    }

    public T getT() {
        return this.mT.get();
    }
}
