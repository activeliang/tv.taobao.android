package android.taobao.windvane.extra.uc;

import android.os.Handler;
import android.os.HandlerThread;

public class WVThread extends HandlerThread {
    private Handler mHandler;

    public WVThread(String name) {
        super(name);
        start();
        this.mHandler = new Handler(getLooper());
    }

    public WVThread(String name, int priority) {
        super(name, priority);
        start();
        this.mHandler = new Handler(getLooper());
    }

    public WVThread(String name, Handler.Callback callback) {
        super(name);
        start();
        this.mHandler = new Handler(getLooper(), callback);
    }

    public WVThread(String name, int priority, Handler.Callback callback) {
        super(name, priority);
        start();
        this.mHandler = new Handler(getLooper(), callback);
    }

    public boolean quit() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        return super.quit();
    }

    public Handler getHandler() {
        return this.mHandler;
    }
}
