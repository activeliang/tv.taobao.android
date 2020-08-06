package android.taobao.windvane.service;

import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WVEventService {
    private static volatile WVEventService EventManager;
    public static int WV_BACKWARD_EVENT = -1;
    public static int WV_EVENT = 0;
    public static int WV_FORWARD_EVENT = 1;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private List<WVEventListener> mBackwardList = new ArrayList();
    private List<WVEventListener> mForwardList = new ArrayList();
    private List<WVEventListener> mList = new ArrayList();

    public static WVEventService getInstance() {
        if (EventManager == null) {
            synchronized (WVEventService.class) {
                if (EventManager == null) {
                    EventManager = new WVEventService();
                }
            }
        }
        return EventManager;
    }

    public void addEventListener(WVEventListener listener, int priority) {
        this.lock.writeLock().lock();
        if (listener != null) {
            try {
                if (priority == WV_FORWARD_EVENT) {
                    this.mForwardList.add(listener);
                } else if (priority == WV_EVENT) {
                    this.mList.add(listener);
                } else if (priority == WV_BACKWARD_EVENT) {
                    this.mBackwardList.add(listener);
                }
            } catch (Throwable th) {
                this.lock.writeLock().unlock();
                throw th;
            }
        }
        this.lock.writeLock().unlock();
    }

    public void addEventListener(WVEventListener listener) {
        addEventListener(listener, WV_EVENT);
    }

    public void removeEventListener(WVEventListener listener) {
        TaoLog.d("removeEventListener", "writeLock lock:" + Thread.currentThread().getId());
        this.lock.writeLock().lock();
        if (listener != null) {
            try {
                if (this.mList.contains(listener)) {
                    this.mList.remove(listener);
                }
                if (this.mForwardList.contains(listener)) {
                    this.mForwardList.remove(listener);
                }
                if (this.mBackwardList.contains(listener)) {
                    this.mBackwardList.remove(listener);
                }
            } catch (Throwable th) {
                this.lock.writeLock().unlock();
                TaoLog.d("removeEventListener", "writeLock unlock:" + Thread.currentThread().getId());
                throw th;
            }
        }
        this.lock.writeLock().unlock();
        TaoLog.d("removeEventListener", "writeLock unlock:" + Thread.currentThread().getId());
    }

    public WVEventResult onEvent(int Id, IWVWebView view, String url, Object... obj) {
        WVEventResult result;
        WVEventResult result2;
        WVEventResult result3;
        this.lock.readLock().lock();
        WVEventContext ctx = new WVEventContext(view, url);
        int i = 0;
        while (this.mForwardList != null && i < this.mForwardList.size()) {
            if (this.mForwardList.get(i) == null || (result3 = this.mForwardList.get(i).onEvent(Id, ctx, obj)) == null || !result3.isSuccess) {
                i++;
            } else {
                this.lock.readLock().unlock();
                return result3;
            }
        }
        int i2 = 0;
        while (this.mList != null && i2 < this.mList.size()) {
            try {
                if (this.mList.get(i2) != null && (result2 = this.mList.get(i2).onEvent(Id, ctx, obj)) != null && result2.isSuccess) {
                    return result2;
                }
                i2++;
            } finally {
                this.lock.readLock().unlock();
            }
        }
        int i3 = 0;
        while (this.mBackwardList != null && i3 < this.mBackwardList.size()) {
            if (this.mBackwardList.get(i3) == null || (result = this.mBackwardList.get(i3).onEvent(Id, ctx, obj)) == null || !result.isSuccess) {
                i3++;
            } else {
                this.lock.readLock().unlock();
                return result;
            }
        }
        this.lock.readLock().unlock();
        return new WVEventResult(false);
    }

    public WVEventResult onEvent(int id) {
        return onEvent(id, (IWVWebView) null, (String) null, new Object[0]);
    }

    public WVEventResult onEvent(int id, Object... obj) {
        return onEvent(id, (IWVWebView) null, (String) null, obj);
    }
}
