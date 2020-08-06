package anetwork.channel.entity;

import android.os.RemoteException;
import anet.channel.bytes.ByteArray;
import anet.channel.util.ALog;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.aidl.DefaultProgressEvent;
import anetwork.channel.aidl.ParcelableHeader;
import anetwork.channel.aidl.ParcelableNetworkListener;
import anetwork.channel.aidl.adapter.ParcelableInputStreamImpl;
import anetwork.channel.interceptor.Callback;
import java.util.List;
import java.util.Map;

public class Repeater implements Callback {
    private static final String TAG = "anet.Repeater";
    /* access modifiers changed from: private */
    public boolean bInputStreamListener = false;
    /* access modifiers changed from: private */
    public RequestConfig config = null;
    /* access modifiers changed from: private */
    public ParcelableInputStreamImpl inputStream = null;
    private ParcelableNetworkListener mListenerWrapper;
    /* access modifiers changed from: private */
    public String seqNo;
    /* access modifiers changed from: private */
    public long startTime;

    public Repeater(ParcelableNetworkListener listener, RequestConfig config2) {
        this.mListenerWrapper = listener;
        this.config = config2;
        if (listener != null) {
            try {
                if ((listener.getListenerState() & 8) != 0) {
                    this.bInputStreamListener = true;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void onResponseCode(final int responseCode, final Map<String, List<String>> headers) {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "[onResponseCode]", this.seqNo, new Object[0]);
        }
        if (this.mListenerWrapper != null) {
            final ParcelableNetworkListener l = this.mListenerWrapper;
            dispatchCallBack(new Runnable() {
                public void run() {
                    try {
                        l.onResponseCode(responseCode, new ParcelableHeader(responseCode, headers));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void onDataReceiveSize(int index, int total, ByteArray byteArray) {
        if (this.mListenerWrapper != null) {
            final ParcelableNetworkListener l = this.mListenerWrapper;
            final ByteArray byteArray2 = byteArray;
            final int i = total;
            final int i2 = index;
            dispatchCallBack(new Runnable() {
                public void run() {
                    if (!Repeater.this.bInputStreamListener) {
                        DefaultProgressEvent progress = new DefaultProgressEvent();
                        progress.setSize(byteArray2.getDataLength());
                        progress.setTotal(i);
                        progress.setDesc("");
                        progress.setIndex(i2);
                        progress.setBytedata(byteArray2.getBuffer());
                        try {
                            l.onDataReceived(progress);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            if (Repeater.this.inputStream == null) {
                                ParcelableInputStreamImpl unused = Repeater.this.inputStream = new ParcelableInputStreamImpl();
                                Repeater.this.inputStream.init(Repeater.this.config, i);
                                Repeater.this.inputStream.write(byteArray2);
                                l.onInputStreamGet(Repeater.this.inputStream);
                                return;
                            }
                            Repeater.this.inputStream.write(byteArray2);
                        } catch (Exception e2) {
                            if (Repeater.this.inputStream != null) {
                                try {
                                    Repeater.this.inputStream.close();
                                } catch (RemoteException e3) {
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public void onFinish(final DefaultFinishEvent event) {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "[onFinish] ", this.seqNo, new Object[0]);
        }
        if (this.mListenerWrapper != null) {
            final ParcelableNetworkListener l = this.mListenerWrapper;
            Runnable callback = new Runnable() {
                public void run() {
                    if (ALog.isPrintLog(1)) {
                        ALog.d(Repeater.TAG, "[onFinish]on Finish waitTime:" + (System.currentTimeMillis() - Repeater.this.startTime), Repeater.this.seqNo, new Object[0]);
                    }
                    long unused = Repeater.this.startTime = System.currentTimeMillis();
                    if (event != null) {
                        event.setContext((Object) null);
                    }
                    try {
                        l.onFinished(event);
                        if (Repeater.this.inputStream != null) {
                            Repeater.this.inputStream.writeEnd();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    if (ALog.isPrintLog(1)) {
                        ALog.d(Repeater.TAG, "[onFinish]on Finish process time:" + (System.currentTimeMillis() - Repeater.this.startTime), Repeater.this.seqNo, new Object[0]);
                    }
                }
            };
            this.startTime = System.currentTimeMillis();
            dispatchCallBack(callback);
        }
        this.mListenerWrapper = null;
    }

    private void dispatchCallBack(Runnable callback) {
        RepeatProcessor.submitTask(this.seqNo != null ? this.seqNo.hashCode() : hashCode(), callback);
    }

    public void setSeqNo(String seqNo2) {
        this.seqNo = seqNo2;
    }
}
