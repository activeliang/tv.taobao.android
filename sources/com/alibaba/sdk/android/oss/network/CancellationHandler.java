package com.alibaba.sdk.android.oss.network;

import okhttp3.Call;

public class CancellationHandler {
    private volatile Call call;
    private volatile boolean isCancelled;

    public void cancel() {
        if (this.call != null) {
            this.call.cancel();
        }
        this.isCancelled = true;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCall(Call call2) {
        this.call = call2;
    }
}
