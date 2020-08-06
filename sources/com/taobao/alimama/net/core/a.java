package com.taobao.alimama.net.core;

import android.os.Handler;
import android.os.Looper;
import com.taobao.alimama.net.NetRequestManager;
import com.taobao.alimama.net.core.a.d;
import com.taobao.alimama.net.core.b.b;
import com.taobao.alimama.net.core.b.c;
import com.taobao.alimama.net.core.future.NetFuture;
import com.taobao.alimama.net.core.task.AbsNetRequestTask;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class a extends NetRequestManager {
    private NetRequestManager.a a;
    private ConcurrentLinkedQueue<AbsNetRequestTask> b;
    /* access modifiers changed from: private */
    public ConcurrentHashMap<AbsNetRequestTask, com.taobao.alimama.net.core.a.a> c;
    private AtomicInteger d;

    /* renamed from: com.taobao.alimama.net.core.a$a  reason: collision with other inner class name */
    private static class C0006a {
        /* access modifiers changed from: private */
        public static final a a = new a();

        private C0006a() {
        }
    }

    private a() {
        this.a = new NetRequestManager.a();
        this.b = new ConcurrentLinkedQueue<>();
        this.c = new ConcurrentHashMap<>();
        this.d = new AtomicInteger(0);
    }

    public static a a() {
        return C0006a.a;
    }

    private NetFuture a(final AbsNetRequestTask absNetRequestTask, final boolean z) {
        com.taobao.alimama.net.core.a.a a2 = d.a(absNetRequestTask);
        if (a2 == null || absNetRequestTask == null || absNetRequestTask.getState() == null) {
            return null;
        }
        if (z) {
            absNetRequestTask.getState().a.incrementAndGet();
            this.d.incrementAndGet();
        }
        this.c.put(absNetRequestTask, a2);
        a2.a(absNetRequestTask, new b() {
            public void a(com.taobao.alimama.net.core.b.a aVar) {
                a.this.c.remove(absNetRequestTask);
                if (aVar.a()) {
                    if (z) {
                        a.this.d(absNetRequestTask, (String) null, (String) null);
                    } else {
                        a.this.b(absNetRequestTask, (String) null, (String) null);
                    }
                } else if (absNetRequestTask.isRequestSuccess(aVar.a)) {
                    if (z) {
                        a.this.b(absNetRequestTask, aVar.a, aVar.c);
                    } else {
                        a.this.a(absNetRequestTask, aVar.a, aVar.c);
                    }
                } else if (absNetRequestTask.isRequestSystemError(aVar.a)) {
                    if (z) {
                        a.this.d(absNetRequestTask, aVar.a, aVar.b);
                    } else {
                        a.this.b(absNetRequestTask, aVar.a, aVar.b);
                    }
                } else if (z) {
                    a.this.c(absNetRequestTask, aVar.a, aVar.b);
                } else {
                    a.this.a(absNetRequestTask, aVar.a, aVar.b);
                }
            }
        });
        return new com.taobao.alimama.net.core.future.a(absNetRequestTask);
    }

    private void a(AbsNetRequestTask absNetRequestTask, Runnable runnable) {
        Handler callbackHandler = absNetRequestTask.getCallbackHandler();
        if (callbackHandler != null) {
            callbackHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    /* access modifiers changed from: private */
    public void a(AbsNetRequestTask absNetRequestTask, String str, Object obj) {
        c(absNetRequestTask, str, obj);
        b();
    }

    /* access modifiers changed from: private */
    public void a(AbsNetRequestTask absNetRequestTask, String str, String str2) {
        f(absNetRequestTask, str, str2);
    }

    private void b() {
        int i = this.a.b - this.d.get();
        if (i > 0) {
            ArrayList<AbsNetRequestTask> arrayList = new ArrayList<>();
            while (true) {
                int i2 = i - 1;
                if (i <= 0) {
                    break;
                }
                AbsNetRequestTask poll = this.b.poll();
                if (poll != null) {
                    arrayList.add(poll);
                    i = i2;
                } else {
                    i = i2;
                }
            }
            for (AbsNetRequestTask a2 : arrayList) {
                a(a2, true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void b(AbsNetRequestTask absNetRequestTask, String str, Object obj) {
        this.d.decrementAndGet();
        c(absNetRequestTask, str, obj);
    }

    /* access modifiers changed from: private */
    public void b(AbsNetRequestTask absNetRequestTask, String str, String str2) {
        if (c(absNetRequestTask)) {
            if (!this.b.contains(absNetRequestTask)) {
                c();
                this.b.add(absNetRequestTask);
            }
            e(absNetRequestTask, str, str2);
            return;
        }
        f(absNetRequestTask, str, str2);
    }

    private void c() {
        ArrayList<AbsNetRequestTask> arrayList = new ArrayList<>();
        while (this.b.size() >= this.a.a) {
            AbsNetRequestTask poll = this.b.poll();
            if (poll != null) {
                arrayList.add(poll);
            }
        }
        for (final AbsNetRequestTask absNetRequestTask : arrayList) {
            a(absNetRequestTask, (Runnable) new Runnable() {
                public void run() {
                    absNetRequestTask.getCallback().onFinalFailed(c.a, (String) null);
                }
            });
        }
    }

    private void c(final AbsNetRequestTask absNetRequestTask, final String str, final Object obj) {
        if (absNetRequestTask.getCallback() != null) {
            a(absNetRequestTask, (Runnable) new Runnable() {
                public void run() {
                    absNetRequestTask.getCallback().onSuccess(str, obj);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void c(AbsNetRequestTask absNetRequestTask, String str, String str2) {
        this.d.decrementAndGet();
        f(absNetRequestTask, str, str2);
    }

    private boolean c(AbsNetRequestTask absNetRequestTask) {
        return absNetRequestTask.getRetryPolicy().maxRetryCount > 0 && absNetRequestTask.getState().a.get() < absNetRequestTask.getRetryPolicy().maxRetryCount;
    }

    /* access modifiers changed from: private */
    public void d(AbsNetRequestTask absNetRequestTask, String str, String str2) {
        this.d.decrementAndGet();
        if (c(absNetRequestTask)) {
            if (!this.b.contains(absNetRequestTask)) {
                c();
                this.b.add(absNetRequestTask);
            }
            e(absNetRequestTask, str, str2);
            return;
        }
        f(absNetRequestTask, str, str2);
    }

    private void e(final AbsNetRequestTask absNetRequestTask, final String str, final String str2) {
        if (absNetRequestTask.getCallback() != null) {
            a(absNetRequestTask, (Runnable) new Runnable() {
                public void run() {
                    absNetRequestTask.getCallback().onTempFailed(str, str2);
                }
            });
        }
    }

    private void f(final AbsNetRequestTask absNetRequestTask, final String str, final String str2) {
        if (absNetRequestTask.getCallback() != null) {
            a(absNetRequestTask, (Runnable) new Runnable() {
                public void run() {
                    absNetRequestTask.getCallback().onFinalFailed(str, str2);
                }
            });
        }
    }

    public void a(AbsNetRequestTask absNetRequestTask) {
        if (absNetRequestTask != null) {
            if (this.b.contains(absNetRequestTask) && this.b.remove(absNetRequestTask)) {
                absNetRequestTask.getCallback().onFinalFailed(c.b, (String) null);
            }
            com.taobao.alimama.net.core.a.a aVar = this.c.get(absNetRequestTask);
            if (aVar != null) {
                aVar.a();
            }
        }
    }

    public void b(AbsNetRequestTask absNetRequestTask) {
        if (absNetRequestTask != null && this.b.contains(absNetRequestTask) && this.b.remove(absNetRequestTask)) {
            a(absNetRequestTask, false);
        }
    }

    public void setGlobalConfig(NetRequestManager.a aVar) {
        if (aVar != null && aVar.a > 0 && aVar.b > 0) {
            this.a = aVar;
        }
    }

    public NetFuture startRequest(AbsNetRequestTask absNetRequestTask) {
        if (absNetRequestTask == null) {
            return null;
        }
        if (!absNetRequestTask.hasCallbackLooper()) {
            absNetRequestTask.setCallbackLooper(Looper.myLooper());
        }
        return a(absNetRequestTask, false);
    }
}
