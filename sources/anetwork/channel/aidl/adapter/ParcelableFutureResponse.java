package anetwork.channel.aidl.adapter;

import android.os.RemoteException;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anetwork.channel.Response;
import anetwork.channel.aidl.NetworkResponse;
import anetwork.channel.aidl.ParcelableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ParcelableFutureResponse extends ParcelableFuture.Stub {
    private static final String TAG = "anet.ParcelableFutureResponse";
    Future<Response> future;
    NetworkResponse response;

    public ParcelableFutureResponse(Future<Response> future2) {
        this.future = future2;
    }

    public ParcelableFutureResponse(NetworkResponse response2) {
        this.response = response2;
    }

    public boolean cancel(boolean mayInterruptIfRunning) throws RemoteException {
        if (this.future == null) {
            return true;
        }
        return this.future.cancel(mayInterruptIfRunning);
    }

    public boolean isCancelled() throws RemoteException {
        if (this.future == null) {
            return true;
        }
        return this.future.isCancelled();
    }

    public boolean isDone() throws RemoteException {
        if (this.future == null) {
            return true;
        }
        return this.future.isDone();
    }

    public NetworkResponse get(long timeout) throws RemoteException {
        if (this.future != null) {
            try {
                return (NetworkResponse) this.future.get(timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                if ("NO SUPPORT".equalsIgnoreCase(e.getMessage())) {
                    ALog.e(TAG, "[get]有listener将不支持future.get()方法，如有需要请listener传入null", (String) null, e, new Object[0]);
                }
                return new NetworkResponse(ErrorConstant.ERROR_REQUEST_FAIL);
            }
        } else if (this.response != null) {
            return this.response;
        } else {
            return new NetworkResponse(ErrorConstant.ERROR_REQUEST_FAIL);
        }
    }
}
