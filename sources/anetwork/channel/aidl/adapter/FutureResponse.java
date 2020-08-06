package anetwork.channel.aidl.adapter;

import android.os.RemoteException;
import anet.channel.util.ALog;
import anetwork.channel.Response;
import anetwork.channel.aidl.ParcelableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureResponse implements Future<Response> {
    private static final String TAG = "anet.FutureResponse";
    private static final int TIMEOUT = 20000;
    private ParcelableFuture delegate;

    public FutureResponse(ParcelableFuture future) {
        this.delegate = future;
    }

    public FutureResponse() {
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        if (this.delegate == null) {
            return false;
        }
        try {
            return this.delegate.cancel(mayInterruptIfRunning);
        } catch (RemoteException e) {
            ALog.w(TAG, "[cancel]", (String) null, e, new Object[0]);
            return false;
        }
    }

    public boolean isCancelled() {
        try {
            return this.delegate.isCancelled();
        } catch (RemoteException e) {
            ALog.w(TAG, "[isCancelled]", (String) null, e, new Object[0]);
            return false;
        }
    }

    public boolean isDone() {
        try {
            return this.delegate.isDone();
        } catch (RemoteException e) {
            ALog.w(TAG, "[isDone]", (String) null, e, new Object[0]);
            return true;
        }
    }

    public Response get() throws InterruptedException, ExecutionException {
        if (this.delegate == null) {
            return null;
        }
        try {
            return this.delegate.get(20000);
        } catch (RemoteException e) {
            ALog.w(TAG, "[get]", (String) null, e, new Object[0]);
            return null;
        }
    }

    public Response get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (this.delegate == null) {
            return null;
        }
        try {
            return this.delegate.get(timeout);
        } catch (RemoteException e) {
            ALog.w(TAG, "[get(long timeout, TimeUnit unit)]", (String) null, e, new Object[0]);
            return null;
        }
    }

    public void setFuture(ParcelableFuture future) {
        this.delegate = future;
    }
}
