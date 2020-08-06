package com.alibaba.sdk.android.oss.callback;

public interface OSSProgressCallback<T> {
    void onProgress(T t, long j, long j2);
}
