package com.taobao.alimama.net.core.future;

import com.taobao.alimama.net.NetRequestCallback;

public interface NetFuture {
    void cancel();

    void retryNow();

    void setCallback(NetRequestCallback netRequestCallback);
}
