package com.yunos.tv.core.common;

public interface RequestListener<T> {

    public interface RequestListenerWithLogin<T> extends RequestListener<T> {
        void onStartLogin();
    }

    void onRequestDone(T t, int i, String str);
}
