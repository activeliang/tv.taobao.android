package anetwork.channel.aidl.adapter;

import android.os.RemoteException;
import anetwork.channel.IBodyHandler;
import anetwork.channel.aidl.ParcelableBodyHandler;

public class ParcelableBodyHandlerWrapper extends ParcelableBodyHandler.Stub {
    private static final String TAG = "anet.ParcelableBodyHandlerWrapper";
    private IBodyHandler handler;

    public ParcelableBodyHandlerWrapper(IBodyHandler handler2) {
        this.handler = handler2;
    }

    public int read(byte[] buffer) throws RemoteException {
        if (this.handler != null) {
            return this.handler.read(buffer);
        }
        return 0;
    }

    public boolean isCompleted() throws RemoteException {
        if (this.handler != null) {
            return this.handler.isCompleted();
        }
        return true;
    }

    public String toString() {
        return super.toString() + " handle:" + this.handler;
    }
}
