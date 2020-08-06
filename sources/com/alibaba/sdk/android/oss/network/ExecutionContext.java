package com.alibaba.sdk.android.oss.network;

import android.content.Context;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.callback.OSSRetryCallback;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;
import okhttp3.OkHttpClient;

public class ExecutionContext<Request extends OSSRequest, Result extends OSSResult> {
    private Context applicationContext;
    private CancellationHandler cancellationHandler;
    private OkHttpClient client;
    private OSSCompletedCallback completedCallback;
    private OSSProgressCallback progressCallback;
    private Request request;
    private OSSRetryCallback retryCallback;

    public ExecutionContext(OkHttpClient client2, Request request2) {
        this(client2, request2, (Context) null);
    }

    public ExecutionContext(OkHttpClient client2, Request request2, Context applicationContext2) {
        this.cancellationHandler = new CancellationHandler();
        setClient(client2);
        setRequest(request2);
        this.applicationContext = applicationContext2;
    }

    public Context getApplicationContext() {
        return this.applicationContext;
    }

    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request request2) {
        this.request = request2;
    }

    public OkHttpClient getClient() {
        return this.client;
    }

    public void setClient(OkHttpClient client2) {
        this.client = client2;
    }

    public CancellationHandler getCancellationHandler() {
        return this.cancellationHandler;
    }

    public OSSCompletedCallback<Request, Result> getCompletedCallback() {
        return this.completedCallback;
    }

    public void setCompletedCallback(OSSCompletedCallback<Request, Result> completedCallback2) {
        this.completedCallback = completedCallback2;
    }

    public OSSProgressCallback getProgressCallback() {
        return this.progressCallback;
    }

    public void setProgressCallback(OSSProgressCallback progressCallback2) {
        this.progressCallback = progressCallback2;
    }

    public OSSRetryCallback getRetryCallback() {
        return this.retryCallback;
    }

    public void setRetryCallback(OSSRetryCallback retryCallback2) {
        this.retryCallback = retryCallback2;
    }
}
