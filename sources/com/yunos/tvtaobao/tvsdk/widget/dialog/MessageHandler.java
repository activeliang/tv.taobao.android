package com.yunos.tvtaobao.tvsdk.widget.dialog;

import android.os.Handler;
import android.os.Looper;
import com.zhiping.dev.android.logger.ZpLogger;

public class MessageHandler {
    private static final String TAG = "MessageHandler";
    private static MessageHandler mMessageHandler;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public Thread mThread = new Thread(new Runnable() {
        public void run() {
            Looper.prepare();
            synchronized (MessageHandler.this.mThread) {
                Handler unused = MessageHandler.this.mHandler = new Handler();
                ZpLogger.i(MessageHandler.TAG, "mThread is started");
                MessageHandler.this.mThread.notifyAll();
            }
            Looper unused2 = MessageHandler.this.mThreadLooper = Looper.myLooper();
            Looper.loop();
            ZpLogger.i(MessageHandler.TAG, "mThread is stopped");
        }
    });
    /* access modifiers changed from: private */
    public Looper mThreadLooper;

    public static MessageHandler getInstance() {
        if (mMessageHandler == null) {
            mMessageHandler = new MessageHandler();
        }
        return mMessageHandler;
    }

    public void destroy() {
        ZpLogger.i(TAG, "destroy");
        if (this.mThreadLooper != null) {
            this.mThreadLooper.quit();
        }
        mMessageHandler = null;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void post(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public void postDelay(Runnable runnable, long delayMillis) {
        this.mHandler.postDelayed(runnable, delayMillis);
    }

    public void remove(Runnable runnable) {
        this.mHandler.removeCallbacks(runnable);
    }

    public void removeAll() {
        this.mHandler.removeMessages(0);
    }

    private MessageHandler() {
        this.mThread.start();
        ZpLogger.i(TAG, "mThread is start");
        synchronized (this.mThread) {
            while (this.mHandler == null) {
                try {
                    ZpLogger.i(TAG, "mThread is waitting start");
                    this.mThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ZpLogger.i(TAG, "mThread is continue");
    }
}
