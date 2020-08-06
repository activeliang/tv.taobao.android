package anetwork.channel.aidl.adapter;

import android.os.RemoteException;
import anetwork.channel.aidl.NetworkResponse;
import anetwork.channel.aidl.ParcelableFuture;

public class ErrorParcelableFuture extends ParcelableFuture.Stub {
    int error;

    public ErrorParcelableFuture(int error2) {
        this.error = error2;
    }

    public boolean cancel(boolean mayInterruptIfRunning) throws RemoteException {
        return false;
    }

    public boolean isCancelled() throws RemoteException {
        return false;
    }

    public boolean isDone() throws RemoteException {
        return true;
    }

    public NetworkResponse get(long timeout) throws RemoteException {
        return new NetworkResponse(this.error);
    }
}
