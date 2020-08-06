package com.yunos.tv.tvsdk.base;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import com.yunos.tv.blitz.utils.NetworkUtil;
import com.yunos.tv.tvsdk.exception.ExceptionManager;
import com.yunos.tv.tvsdk.exception.NoNetworkException;

public abstract class WorkAsyncTask<Result> extends AsyncTask<Object, Object, Result> {
    private static final String TAG = "WorkAsyncTask";
    private Context mContext;
    private Exception mException;
    private boolean mIsHttpTask;
    protected View[] mViews;

    public abstract Result doProgress() throws Exception;

    public Exception getmException() {
        return this.mException;
    }

    public WorkAsyncTask(Context context) {
        this(context, true, (View[]) null);
    }

    public WorkAsyncTask(Context context, View... views) {
        this(context, true, views);
    }

    public WorkAsyncTask(Context context, boolean isHttpTask) {
        this(context, isHttpTask, (View[]) null);
    }

    public WorkAsyncTask(Context context, boolean isHttpTask, View... views) {
        this.mIsHttpTask = true;
        this.mException = null;
        this.mIsHttpTask = isHttpTask;
        this.mContext = context;
        this.mViews = views;
    }

    /* access modifiers changed from: protected */
    public final void onPreExecute() {
        try {
            if (this.mViews != null) {
                for (View view : this.mViews) {
                    if (view != null) {
                        view.setEnabled(false);
                    }
                }
            }
            onPre();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public final Result doInBackground(Object... params) {
        try {
            if (isCancelled()) {
                return null;
            }
            if (!this.mIsHttpTask || NetworkUtil.isNetworkAvailable()) {
                return doProgress();
            }
            throw new NoNetworkException();
        } catch (Exception e) {
            this.mException = e;
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public final void onProgressUpdate(Object... values) {
        try {
            onUpdate(values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public final void onPostExecute(Result resultObject) {
        if (this.mViews != null) {
            for (View view : this.mViews) {
                if (view != null) {
                    view.setEnabled(true);
                }
            }
        }
        try {
            if (this.mException != null) {
                boolean isHandled = ExceptionManager.handleException(this.mContext, this.mException);
                Log.w(TAG, "onPostExecute -- mException -- isHandled:" + isHandled);
                if (!isHandled) {
                    onError(this.mException);
                }
                onPost(false, resultObject);
                return;
            }
            onPost(true, resultObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public final void onCancelled() {
        super.onCancelled();
        if (this.mViews != null) {
            for (View view : this.mViews) {
                view.setEnabled(true);
            }
        }
        try {
            if (this.mException != null) {
                if (!ExceptionManager.handleException(this.mContext, this.mException)) {
                    onError(this.mException);
                }
                onCancel(false);
                return;
            }
            onCancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPre() throws Exception {
    }

    public void onUpdate(Object... values) throws Exception {
    }

    public void onPost(boolean isSuccess, Result result) throws Exception {
    }

    public void onCancel(boolean isSuccess) {
    }

    public void onError(Exception e) {
    }

    /* access modifiers changed from: protected */
    public Context getContext() {
        return this.mContext;
    }
}
