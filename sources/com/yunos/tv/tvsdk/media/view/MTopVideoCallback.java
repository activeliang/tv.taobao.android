package com.yunos.tv.tvsdk.media.view;

public interface MTopVideoCallback<T> {
    void beforeDoProgress();

    T doProgress(T t) throws Exception;

    void onCancel(boolean z);

    void onError(Exception exc);

    void onPost(boolean z, T t);

    void onPre() throws Exception;

    void onUpdate(Object... objArr) throws Exception;
}
