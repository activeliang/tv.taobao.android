package com.ut.mini.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import java.util.Map;

public class UTSendLogDelegate {
    private static final int SEND_LOG_HANDLER_MSG_ID = 1;
    private Handler mHandler = null;
    private HandlerThread mHandlerThread = null;
    /* access modifiers changed from: private */
    public ISendLogListener mListener = null;

    public interface ISendLogListener {
        void onLogArrived(Map<String, String> map);
    }

    public void start() {
        this.mHandlerThread = new HandlerThread("UT-INVOKE-ASYNC");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                if (msg.what != 1) {
                    return;
                }
                if (msg.obj != null) {
                    Map<String, String> lLogMap = (Map) msg.obj;
                    if (UTSendLogDelegate.this.mListener != null) {
                        UTSendLogDelegate.this.mListener.onLogArrived(lLogMap);
                        return;
                    }
                    return;
                }
                UTSendLogDelegate.this.mListener.onLogArrived((Map<String, String>) null);
            }
        };
    }

    public void setSendLogListener(ISendLogListener aListener) {
        this.mListener = aListener;
    }

    public void send(Map<String, String> aLogMap) {
        if (this.mHandler != null) {
            if (aLogMap != null && aLogMap.containsKey("_sls")) {
                aLogMap.remove("_sls");
                if (this.mListener != null) {
                    this.mListener.onLogArrived(aLogMap);
                    return;
                }
            }
            Message lMessage = Message.obtain();
            lMessage.what = 1;
            lMessage.obj = aLogMap;
            this.mHandler.sendMessage(lMessage);
        }
    }
}
