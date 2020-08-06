package com.loc;

import android.content.Context;
import android.os.Looper;
import java.lang.Thread;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: SDKLogHandler */
public final class ab extends y implements Thread.UncaughtExceptionHandler {
    private static ExecutorService e;
    private static Set<Integer> f = Collections.synchronizedSet(new HashSet());
    private static WeakReference<Context> g;
    private static final ThreadFactory h = new ThreadFactory() {
        private final AtomicInteger a = new AtomicInteger(1);

        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, "pama#" + this.a.getAndIncrement());
        }
    };
    /* access modifiers changed from: private */
    public Context d;
    private List<Object> i;

    private ab(Context context) {
        this.d = context;
        try {
            this.b = Thread.getDefaultUncaughtExceptionHandler();
            if (this.b == null) {
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.c = true;
                return;
            }
            String obj = this.b.toString();
            if (obj.startsWith("com.amap.apis.utils.core.dynamiccore") || (obj.indexOf("com.amap.api") == -1 && obj.indexOf("com.loc") == -1)) {
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.c = true;
                return;
            }
            this.c = false;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static synchronized ab a(Context context, t tVar) throws j {
        ab abVar;
        synchronized (ab.class) {
            if (tVar == null) {
                throw new j("sdk info is null");
            } else if (tVar.a() == null || "".equals(tVar.a())) {
                throw new j("sdk name is invalid");
            } else {
                try {
                    if (!f.add(Integer.valueOf(tVar.hashCode()))) {
                        abVar = (ab) y.a;
                    } else {
                        if (y.a == null) {
                            y.a = new ab(context);
                        } else {
                            y.a.c = false;
                        }
                        y.a.a(context, tVar, y.a.c);
                        abVar = (ab) y.a;
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
        return abVar;
    }

    public static void a(t tVar, String str, j jVar) {
        if (jVar != null) {
            a(tVar, str, jVar.c(), jVar.d(), jVar.e(), jVar.b());
        }
    }

    public static void a(t tVar, String str, String str2, String str3, String str4) {
        a(tVar, str, str2, str3, "", str4);
    }

    public static void a(t tVar, String str, String str2, String str3, String str4, String str5) {
        try {
            if (y.a != null) {
                StringBuilder sb = new StringBuilder("path:");
                sb.append(str).append(",type:").append(str2).append(",gsid:").append(str3).append(",csid:").append(str4).append(",code:").append(str5);
                y.a.a(tVar, sb.toString(), "networkError");
            }
        } catch (Throwable th) {
        }
    }

    public static synchronized void b() {
        synchronized (ab.class) {
            try {
                if (e != null) {
                    e.shutdown();
                }
                as.a();
            } catch (Throwable th) {
                th.printStackTrace();
            }
            try {
                if (!(y.a == null || Thread.getDefaultUncaughtExceptionHandler() != y.a || y.a.b == null)) {
                    Thread.setDefaultUncaughtExceptionHandler(y.a.b);
                }
                y.a = null;
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
        }
        return;
    }

    public static void b(t tVar, String str, String str2) {
        try {
            if (y.a != null) {
                y.a.a(tVar, str, str2);
            }
        } catch (Throwable th) {
        }
    }

    public static void b(Throwable th, String str, String str2) {
        try {
            if (y.a != null) {
                y.a.a(th, 1, str, str2);
            }
        } catch (Throwable th2) {
        }
    }

    public static void c() {
        if (g != null && g.get() != null) {
            z.a((Context) g.get());
        } else if (y.a != null) {
            y.a.a();
        }
    }

    public static synchronized ExecutorService d() {
        ExecutorService executorService;
        synchronized (ab.class) {
            try {
                if (e == null || e.isShutdown()) {
                    e = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(256), h);
                }
            } catch (Throwable th) {
            }
            executorService = e;
        }
        return executorService;
    }

    /* access modifiers changed from: protected */
    public final void a() {
        z.a(this.d);
    }

    /* access modifiers changed from: protected */
    public final void a(final Context context, final t tVar, final boolean z) {
        try {
            ExecutorService d2 = d();
            if (d2 != null && !d2.isShutdown()) {
                d2.submit(new Runnable() {
                    public final void run() {
                        try {
                            synchronized (Looper.getMainLooper()) {
                                new ak(context, true).a(tVar);
                            }
                            if (z) {
                                ac.a(ab.this.d);
                            }
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                });
            }
        } catch (RejectedExecutionException e2) {
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public final void a(t tVar, String str, String str2) {
        ac.a(tVar, this.d, str2, str);
    }

    /* access modifiers changed from: protected */
    public final void a(Throwable th, int i2, String str, String str2) {
        ac.a(this.d, th, i2, str, str2);
    }

    public final void uncaughtException(Thread thread, Throwable th) {
        int i2 = 0;
        while (i2 < this.i.size() && i2 < 10) {
            try {
                this.i.get(i2);
                i2++;
            } catch (Throwable th2) {
            }
        }
        if (th != null) {
            a(th, 0, (String) null, (String) null);
            if (this.b != null) {
                try {
                    Thread.setDefaultUncaughtExceptionHandler(this.b);
                } catch (Throwable th3) {
                }
                this.b.uncaughtException(thread, th);
            }
        }
    }
}
