package com.taobao.alimama.net.core.task;

import android.os.Handler;
import android.os.Looper;
import com.taobao.alimama.net.NetRequestCallback;
import com.taobao.alimama.net.core.state.NetRequestRetryPolicy;
import com.taobao.alimama.net.core.state.a;
import java.util.UUID;

public abstract class AbsNetRequestTask {
    private NetRequestCallback callback;
    private Looper callbackLooper;
    private final String requestId;
    private final NetRequestRetryPolicy retryPolicy;
    private final a state;
    private final String uri;

    AbsNetRequestTask(String str, NetRequestRetryPolicy netRequestRetryPolicy) {
        this.uri = str;
        this.retryPolicy = netRequestRetryPolicy == null ? NetRequestRetryPolicy.DEFAULT_NO_RETRY : netRequestRetryPolicy;
        this.state = new a();
        this.requestId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public NetRequestCallback getCallback() {
        return this.callback;
    }

    public Handler getCallbackHandler() {
        if (this.callbackLooper == null || !this.callbackLooper.getThread().isAlive()) {
            return null;
        }
        return new Handler(this.callbackLooper);
    }

    public String getRequestId() {
        return this.requestId;
    }

    public NetRequestRetryPolicy getRetryPolicy() {
        return this.retryPolicy;
    }

    public a getState() {
        return this.state;
    }

    public String getUri() {
        return this.uri;
    }

    public boolean hasCallbackLooper() {
        return this.callbackLooper != null;
    }

    public abstract boolean isRequestSuccess(String str);

    public abstract boolean isRequestSystemError(String str);

    public void setCallback(NetRequestCallback netRequestCallback) {
        this.callback = netRequestCallback;
    }

    public void setCallbackLooper(Looper looper) {
        this.callbackLooper = looper;
    }
}
