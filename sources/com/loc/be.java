package com.loc;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import java.lang.ref.WeakReference;

/* compiled from: OfflineLocManager */
public class be {
    static int a = 1000;
    static boolean b = false;
    static int c = 20;
    /* access modifiers changed from: private */
    public static WeakReference<bb> d;
    /* access modifiers changed from: private */
    public static int e = 10;

    public static synchronized void a(int i, boolean z, int i2) {
        synchronized (be.class) {
            a = i;
            b = z;
            if (i2 < 10 || i2 > 100) {
                i2 = 20;
            }
            c = i2;
            if (i2 / 5 > e) {
                e = c / 5;
            }
        }
    }

    public static void a(final Context context) {
        ab.d().submit(new Runnable() {
            public final void run() {
                try {
                    bb a2 = bh.a(be.d);
                    bh.a(context, a2, z.i, be.a, 2097152, Constants.LogTransferLevel.L6);
                    a2.h = 14400000;
                    if (a2.g == null) {
                        an anVar = new an(new ap(new ar()));
                        a2.g = new bl(new bk(context, new bp(), anVar, new String(w.a(10)), k.f(context), n.x(context), n.m(context), n.h(context), n.a(), Build.MANUFACTURER, Build.DEVICE, n.A(context), k.c(context), Build.MODEL, k.d(context), k.b(context)));
                    }
                    if (TextUtils.isEmpty(a2.i)) {
                        a2.i = "fKey";
                    }
                    a2.f = new bt(context, a2.h, a2.i, new br(context, be.b, be.e * 1024, be.c * 1024, "offLocKey"));
                    bc.a(a2);
                } catch (Throwable th) {
                    ab.b(th, "ofm", "uold");
                }
            }
        });
    }

    public static synchronized void a(final bd bdVar, final Context context) {
        synchronized (be.class) {
            ab.d().submit(new Runnable() {
                public final void run() {
                    try {
                        synchronized (be.class) {
                            String l = Long.toString(System.currentTimeMillis());
                            bb a2 = bh.a(be.d);
                            bh.a(context, a2, z.i, be.a, 2097152, Constants.LogTransferLevel.L6);
                            if (a2.e == null) {
                                a2.e = new an(new ap(new ar(new ap())));
                            }
                            bc.a(l, bdVar.a(), a2);
                        }
                    } catch (Throwable th) {
                        ab.b(th, "ofm", "aple");
                    }
                }
            });
        }
    }
}
