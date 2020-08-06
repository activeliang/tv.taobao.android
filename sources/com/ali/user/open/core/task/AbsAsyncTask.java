package com.ali.user.open.core.task;

import android.os.AsyncTask;

public abstract class AbsAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private static final String TAG = "kernel";

    /* access modifiers changed from: protected */
    public abstract Result asyncExecute(Params... paramsArr);

    /* access modifiers changed from: protected */
    public abstract void doFinally();

    /* access modifiers changed from: protected */
    public abstract void doWhenException(Throwable th);

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public Result doInBackground(Params... params) {
        try {
            Result asyncExecute = asyncExecute(params);
            doFinally();
            return asyncExecute;
        } catch (Throwable th) {
            doFinally();
            throw th;
        }
    }
}
