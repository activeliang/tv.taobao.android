package anetwork.channel.aidl.adapter;

import android.os.Handler;
import android.os.RemoteException;
import anet.channel.util.ALog;
import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkListener;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.aidl.DefaultProgressEvent;
import anetwork.channel.aidl.ParcelableHeader;
import anetwork.channel.aidl.ParcelableInputStream;
import anetwork.channel.aidl.ParcelableNetworkListener;

public class ParcelableNetworkListenerWrapper extends ParcelableNetworkListener.Stub {
    private static final String TAG = "anet.ParcelableNetworkListenerWrapper";
    private Handler handler;
    private NetworkListener listener;
    private Object mContext;
    private byte state = 0;

    public NetworkListener getListener() {
        return this.listener;
    }

    public ParcelableNetworkListenerWrapper(NetworkListener listener2, Handler handler2, Object context) {
        this.listener = listener2;
        if (listener2 != null) {
            if (NetworkCallBack.FinishListener.class.isAssignableFrom(listener2.getClass())) {
                this.state = (byte) (this.state | 1);
            }
            if (NetworkCallBack.ProgressListener.class.isAssignableFrom(listener2.getClass())) {
                this.state = (byte) (this.state | 2);
            }
            if (NetworkCallBack.ResponseCodeListener.class.isAssignableFrom(listener2.getClass())) {
                this.state = (byte) (this.state | 4);
            }
            if (NetworkCallBack.InputStreamListener.class.isAssignableFrom(listener2.getClass())) {
                this.state = (byte) (this.state | 8);
            }
        }
        this.handler = handler2;
        this.mContext = context;
    }

    private void dispatch(final byte tag, final Object arg) {
        if (this.handler == null) {
            dispatchCallback(tag, arg);
        } else {
            this.handler.post(new Runnable() {
                public void run() {
                    ParcelableNetworkListenerWrapper.this.dispatchCallback(tag, arg);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void dispatchCallback(byte tag, Object arg) {
        if (tag == 4) {
            try {
                ParcelableHeader header = (ParcelableHeader) arg;
                ((NetworkCallBack.ResponseCodeListener) this.listener).onResponseCode(header.getResponseCode(), header.getHeader(), this.mContext);
                if (ALog.isPrintLog(1)) {
                    ALog.d(TAG, "[onResponseCode]" + header, (String) null, new Object[0]);
                }
            } catch (Exception e) {
                ALog.e(TAG, "dispatchCallback error", (String) null, new Object[0]);
            }
        } else if (tag == 2) {
            DefaultProgressEvent event = (DefaultProgressEvent) arg;
            if (event != null) {
                event.setContext(this.mContext);
            }
            ((NetworkCallBack.ProgressListener) this.listener).onDataReceived(event, this.mContext);
            if (ALog.isPrintLog(1)) {
                ALog.d(TAG, "[onDataReceived]" + event, (String) null, new Object[0]);
            }
        } else if (tag == 1) {
            DefaultFinishEvent event2 = (DefaultFinishEvent) arg;
            if (event2 != null) {
                event2.setContext(this.mContext);
            }
            ((NetworkCallBack.FinishListener) this.listener).onFinished(event2, this.mContext);
            if (ALog.isPrintLog(1)) {
                ALog.d(TAG, "[onFinished]" + event2, (String) null, new Object[0]);
            }
        } else if (tag == 8) {
            ((NetworkCallBack.InputStreamListener) this.listener).onInputStreamGet((ParcelableInputStream) arg, this.mContext);
            if (ALog.isPrintLog(1)) {
                ALog.d(TAG, "[onInputStreamReceived]", (String) null, new Object[0]);
            }
        }
    }

    public void onDataReceived(DefaultProgressEvent event) throws RemoteException {
        if ((this.state & 2) != 0) {
            dispatch((byte) 2, event);
        }
    }

    public void onFinished(DefaultFinishEvent event) throws RemoteException {
        if ((this.state & 1) != 0) {
            dispatch((byte) 1, event);
        }
        this.listener = null;
        this.mContext = null;
        this.handler = null;
    }

    public boolean onResponseCode(int code, ParcelableHeader header) throws RemoteException {
        if ((this.state & 4) == 0) {
            return false;
        }
        dispatch((byte) 4, header);
        return false;
    }

    public void onInputStreamGet(ParcelableInputStream inputStream) throws RemoteException {
        if ((this.state & 8) != 0) {
            dispatch((byte) 8, inputStream);
        }
    }

    public byte getListenerState() throws RemoteException {
        return this.state;
    }
}
