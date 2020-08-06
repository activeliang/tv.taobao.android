package com.uc.webview.export.cyclone;

import android.os.AsyncTask;
import android.os.Process;
import android.webkit.ValueCallback;
import java.util.concurrent.ConcurrentLinkedQueue;

/* compiled from: ProGuard */
public class UCLogger {
    private static boolean a = false;
    private static Class<?> b = null;
    /* access modifiers changed from: private */
    public static ValueCallback<Object[]> c = null;
    private static String d = "[all]";
    private static String e = "[all]";
    private static boolean f = false;
    private static boolean g = false;
    /* access modifiers changed from: private */
    public static final ConcurrentLinkedQueue<Object[]> h = new ConcurrentLinkedQueue<>();
    /* access modifiers changed from: private */
    public static AsyncTask<Object, Object, Object> i;
    private static final Class[] j = {String.class, String.class, Throwable.class};
    private static final Class[] k = {String.class, String.class};
    private String l;
    private String m;

    private UCLogger(String str, String str2) {
        this.l = str;
        this.m = str2;
    }

    public static UCLogger create(String str, String str2) {
        if (a && !(b == null && c == null) && ((f || d.contains(str)) && (g || e.contains(new StringBuilder("[").append(str2).append("]").toString())))) {
            return new UCLogger(str, str2);
        }
        return null;
    }

    public void print(String str, Throwable... thArr) {
        Throwable th = (thArr == null || thArr.length <= 0 || thArr[0] == null) ? null : thArr[0];
        try {
            if (b != null) {
                UCCyclone.a(b, this.l, th != null ? j : k, th != null ? new Object[]{this.m, str, th} : new Object[]{this.m, str});
            }
        } catch (Throwable th2) {
        }
        try {
            if (c != null) {
                h.add(new Object[]{Long.valueOf(System.currentTimeMillis()), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()), this.l, this.m, str, th});
                if (i == null) {
                    i = new AsyncTask<Object, Object, Object>() {
                        /* access modifiers changed from: protected */
                        public final Object doInBackground(Object... objArr) {
                            boolean z;
                            ValueCallback a2 = UCLogger.c;
                            if (a2 == null) {
                                try {
                                    UCLogger.h.clear();
                                } catch (Throwable th) {
                                    AsyncTask unused = UCLogger.i = null;
                                    throw th;
                                }
                            } else {
                                boolean z2 = true;
                                while (z2) {
                                    for (Object[] objArr2 = (Object[]) UCLogger.h.poll(); objArr2 != null; objArr2 = (Object[]) UCLogger.h.poll()) {
                                        a2.onReceiveValue(objArr2);
                                    }
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                    }
                                    if (((Object[]) UCLogger.h.peek()) == null) {
                                        AsyncTask unused2 = UCLogger.i = null;
                                        z = false;
                                    } else {
                                        z = z2;
                                    }
                                    z2 = z;
                                }
                            }
                            AsyncTask unused3 = UCLogger.i = null;
                            return null;
                        }
                    }.execute(new Object[0]);
                }
            }
        } catch (Throwable th3) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x006d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void setup(java.lang.Object[] r8) {
        /*
            r5 = 0
            r7 = 1
            r6 = 0
            r0 = r8[r6]
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            r1 = r8[r7]
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            r2 = 2
            r2 = r8[r2]
            android.webkit.ValueCallback r2 = (android.webkit.ValueCallback) r2
            r3 = 3
            r3 = r8[r3]
            java.lang.String r3 = (java.lang.String) r3
            r4 = 4
            r4 = r8[r4]
            java.lang.String r4 = (java.lang.String) r4
            if (r0 == 0) goto L_0x005c
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x005c
            a = r7
        L_0x0024:
            if (r1 == 0) goto L_0x0061
            boolean r0 = r1.booleanValue()     // Catch:{ Throwable -> 0x0065 }
            if (r0 == 0) goto L_0x0061
            boolean r0 = r1.booleanValue()     // Catch:{ Throwable -> 0x0065 }
            if (r0 == 0) goto L_0x005f
            java.lang.String r0 = "android.util.Log"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Throwable -> 0x0065 }
        L_0x0039:
            b = r0     // Catch:{ Throwable -> 0x0065 }
        L_0x003b:
            c = r2
            if (r3 == 0) goto L_0x0067
            d = r3
        L_0x0041:
            if (r4 == 0) goto L_0x006d
            e = r4
        L_0x0045:
            java.lang.String r0 = d
            java.lang.String r1 = "[all]"
            boolean r0 = r0.contains(r1)
            f = r0
            java.lang.String r0 = e
            java.lang.String r1 = "[all]"
            boolean r0 = r0.contains(r1)
            g = r0
            return
        L_0x005c:
            a = r6
            goto L_0x0024
        L_0x005f:
            r0 = r5
            goto L_0x0039
        L_0x0061:
            r0 = 0
            b = r0     // Catch:{ Throwable -> 0x0065 }
            goto L_0x003b
        L_0x0065:
            r0 = move-exception
            goto L_0x003b
        L_0x0067:
            java.lang.String r0 = ""
            d = r0
            goto L_0x0041
        L_0x006d:
            java.lang.String r0 = ""
            e = r0
            goto L_0x0045
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCLogger.setup(java.lang.Object[]):void");
    }
}
