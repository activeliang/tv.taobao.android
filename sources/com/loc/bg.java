package com.loc;

import android.content.Context;
import android.text.TextUtils;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;

/* compiled from: StatisticsManager */
public class bg {
    static boolean a = false;
    static int b = 20;
    /* access modifiers changed from: private */
    public static int c = 20;
    /* access modifiers changed from: private */
    public static WeakReference<bb> d;

    public static void a(final Context context) {
        ab.d().submit(new Runnable() {
            public final void run() {
                try {
                    bb a2 = bh.a(bg.d);
                    bh.a(context, a2, z.h, 1000, 307200, "2");
                    if (a2.g == null) {
                        a2.g = new bi(new bm(context, new bj(new bn(new bp()))));
                    }
                    a2.h = 3600000;
                    if (TextUtils.isEmpty(a2.i)) {
                        a2.i = "cKey";
                    }
                    if (a2.f == null) {
                        a2.f = new bt(context, a2.h, a2.i, new bq(a2.a, new br(context, bg.a, bg.c * 1024, bg.b * 1024, "staticUpdate")));
                    }
                    bc.a(a2);
                } catch (Throwable th) {
                    ab.b(th, "stm", "usd");
                }
            }
        });
    }

    static /* synthetic */ void a(Context context, byte[] bArr) throws IOException {
        bb a2 = bh.a(d);
        bh.a(context, a2, z.h, 1000, 307200, "2");
        if (a2.e == null) {
            a2.e = new aq();
        }
        try {
            bc.a(Integer.toString(new Random().nextInt(100)) + Long.toString(System.nanoTime()), bArr, a2);
        } catch (Throwable th) {
            ab.b(th, "stm", "wts");
        }
    }

    public static synchronized void a(final bf bfVar, final Context context) {
        synchronized (bg.class) {
            ab.d().submit(new Runnable() {
                public final void run() {
                    try {
                        synchronized (bg.class) {
                            bg.a(context, bfVar.a());
                        }
                    } catch (Throwable th) {
                        ab.b(th, "stm", "as");
                    }
                }
            });
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0007, code lost:
        if (r3.size() == 0) goto L_0x0009;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void a(final java.util.List<com.loc.bf> r3, final android.content.Context r4) {
        /*
            java.lang.Class<com.loc.bg> r1 = com.loc.bg.class
            monitor-enter(r1)
            int r0 = r3.size()     // Catch:{ Throwable -> 0x000b }
            if (r0 != 0) goto L_0x000c
        L_0x0009:
            monitor-exit(r1)
            return
        L_0x000b:
            r0 = move-exception
        L_0x000c:
            java.util.concurrent.ExecutorService r0 = com.loc.ab.d()     // Catch:{ all -> 0x0019 }
            com.loc.bg$2 r2 = new com.loc.bg$2     // Catch:{ all -> 0x0019 }
            r2.<init>(r3, r4)     // Catch:{ all -> 0x0019 }
            r0.submit(r2)     // Catch:{ all -> 0x0019 }
            goto L_0x0009
        L_0x0019:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bg.a(java.util.List, android.content.Context):void");
    }

    public static synchronized void a(boolean z) {
        synchronized (bg.class) {
            a = z;
        }
    }
}
