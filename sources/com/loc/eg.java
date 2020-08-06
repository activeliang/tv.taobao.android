package com.loc;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

/* compiled from: AMapDnsManager */
public final class eg {
    public static int a = 1;
    public static int b = 2;
    private static eg e;
    private long c = 0;
    private boolean d = false;
    private ArrayList<String> f = new ArrayList<>();
    private du g = new du();
    private du h = new du();
    /* access modifiers changed from: private */
    public long i = 120000;
    /* access modifiers changed from: private */
    public Context j;
    private String k;
    private boolean l = false;

    private eg(Context context) {
        this.j = context;
    }

    public static synchronized eg a(Context context) {
        eg egVar;
        synchronized (eg.class) {
            if (e == null) {
                e = new eg(context);
            }
            egVar = e;
        }
        return egVar;
    }

    static /* synthetic */ boolean a(String[] strArr, String[] strArr2) {
        if (strArr == null || strArr.length == 0 || strArr2 == null || strArr2.length == 0 || strArr.length != strArr2.length) {
            return false;
        }
        int length = strArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (!strArr[i2].equals(strArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    static /* synthetic */ String[] a(JSONArray jSONArray, int i2) throws JSONException {
        if (jSONArray == null || jSONArray.length() == 0) {
            return new String[0];
        }
        int length = jSONArray.length();
        String[] strArr = new String[length];
        for (int i3 = 0; i3 < length; i3++) {
            String string = jSONArray.getString(i3);
            if (!TextUtils.isEmpty(string)) {
                if (i2 == b) {
                    string = "[" + string + "]";
                }
                strArr[i3] = string;
            }
        }
        return strArr;
    }

    /* access modifiers changed from: private */
    public du b(int i2) {
        return i2 == b ? this.h : this.g;
    }

    static /* synthetic */ void b(eg egVar, int i2) {
        if (egVar.b(i2).a() != null && egVar.b(i2).a().length > 0) {
            String str = egVar.b(i2).a()[0];
            if (!str.equals(egVar.k) && !egVar.f.contains(str)) {
                egVar.k = str;
                SharedPreferences.Editor a2 = es.a(egVar.j, "cbG9jaXA");
                es.a(a2, "last_ip", str);
                es.a(a2);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002d, code lost:
        if ((r0 - r7.c) >= 60000) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000b, code lost:
        if (r7.l != false) goto L_0x000d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void b(boolean r8, final int r9) {
        /*
            r7 = this;
            monitor-enter(r7)
            if (r8 != 0) goto L_0x000f
            boolean r0 = com.loc.em.v()     // Catch:{ all -> 0x0095 }
            if (r0 != 0) goto L_0x000f
            boolean r0 = r7.l     // Catch:{ all -> 0x0095 }
            if (r0 == 0) goto L_0x000f
        L_0x000d:
            monitor-exit(r7)
            return
        L_0x000f:
            long r0 = r7.c     // Catch:{ all -> 0x0095 }
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 == 0) goto L_0x002f
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0095 }
            long r2 = r7.c     // Catch:{ all -> 0x0095 }
            long r2 = r0 - r2
            long r4 = r7.i     // Catch:{ all -> 0x0095 }
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 < 0) goto L_0x000d
            long r2 = r7.c     // Catch:{ all -> 0x0095 }
            long r0 = r0 - r2
            r2 = 60000(0xea60, double:2.9644E-319)
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 < 0) goto L_0x000d
        L_0x002f:
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0095 }
            r7.c = r0     // Catch:{ all -> 0x0095 }
            r0 = 1
            r7.l = r0     // Catch:{ all -> 0x0095 }
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0095 }
            java.lang.StackTraceElement[] r1 = r0.getStackTrace()     // Catch:{ all -> 0x0095 }
            java.lang.StringBuffer r2 = new java.lang.StringBuffer     // Catch:{ all -> 0x0095 }
            r2.<init>()     // Catch:{ all -> 0x0095 }
            int r3 = r1.length     // Catch:{ all -> 0x0095 }
            r0 = 0
        L_0x0047:
            if (r0 >= r3) goto L_0x0087
            r4 = r1[r0]     // Catch:{ all -> 0x0095 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0095 }
            r5.<init>()     // Catch:{ all -> 0x0095 }
            java.lang.String r6 = r4.getClassName()     // Catch:{ all -> 0x0095 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x0095 }
            java.lang.String r6 = "("
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x0095 }
            java.lang.String r6 = r4.getMethodName()     // Catch:{ all -> 0x0095 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x0095 }
            java.lang.String r6 = ":"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x0095 }
            int r4 = r4.getLineNumber()     // Catch:{ all -> 0x0095 }
            java.lang.StringBuilder r4 = r5.append(r4)     // Catch:{ all -> 0x0095 }
            java.lang.String r5 = "),"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0095 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0095 }
            r2.append(r4)     // Catch:{ all -> 0x0095 }
            int r0 = r0 + 1
            goto L_0x0047
        L_0x0087:
            java.util.concurrent.ExecutorService r0 = com.loc.ab.d()     // Catch:{ all -> 0x0095 }
            com.loc.eg$1 r1 = new com.loc.eg$1     // Catch:{ all -> 0x0095 }
            r1.<init>(r9)     // Catch:{ all -> 0x0095 }
            r0.submit(r1)     // Catch:{ all -> 0x0095 }
            goto L_0x000d
        L_0x0095:
            r0 = move-exception
            monitor-exit(r7)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eg.b(boolean, int):void");
    }

    private static String c(int i2) {
        return i2 == b ? "last_ip_6" : "last_ip_4";
    }

    private void d(int i2) {
        if (b(i2).d()) {
            SharedPreferences.Editor a2 = es.a(this.j, "cbG9jaXA");
            try {
                a2.remove(c(i2));
            } catch (Throwable th) {
                en.a(th, "SpUtil", "setPrefsLong");
            }
            es.a(a2);
            b(i2).a(false);
        }
    }

    private String e(int i2) {
        String str;
        int i3 = 0;
        b(false, i2);
        String[] a2 = b(i2).a();
        if (a2 == null || a2.length <= 0) {
            String a3 = es.a(this.j, "cbG9jaXA", c(i2), (String) null);
            if (!TextUtils.isEmpty(a3) && !this.f.contains(a3)) {
                b(i2).a(a3);
                b(i2).b(a3);
                b(i2).a(true);
            }
            return "";
        }
        int length = a2.length;
        while (true) {
            if (i3 >= length) {
                str = null;
                break;
            }
            str = a2[i3];
            if (!this.f.contains(str)) {
                break;
            }
            i3++;
        }
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        b(i2).a(str);
        return str;
    }

    public final String a(ej ejVar, int i2) {
        try {
            if (!em.w() || ejVar == null) {
                return null;
            }
            String c2 = ejVar.c();
            String host = new URL(c2).getHost();
            if ("http://abroad.apilocate.amap.com/mobile/binary".equals(c2) || "abroad.apilocate.amap.com".equals(host)) {
                return null;
            }
            String str = "apilocate.amap.com".equalsIgnoreCase(host) ? "httpdns.apilocate.amap.com" : host;
            String e2 = e(i2);
            if (TextUtils.isEmpty(e2)) {
                return null;
            }
            if (i2 == b) {
                ejVar.h = c2.replace(host, e2);
            } else {
                ejVar.g = c2.replace(host, e2);
            }
            ejVar.b().put("host", str);
            ejVar.c(str);
            return e2;
        } catch (Throwable th) {
            return null;
        }
    }

    public final void a(int i2) {
        if (!b(i2).e()) {
            this.f.add(b(i2).b());
            d(i2);
            b(true, i2);
            return;
        }
        d(i2);
    }

    public final void a(boolean z, int i2) {
        b(i2).b(z);
        if (z) {
            String c2 = b(i2).c();
            String b2 = b(i2).b();
            if (!TextUtils.isEmpty(b2) && !b2.equals(c2)) {
                SharedPreferences.Editor a2 = es.a(this.j, "cbG9jaXA");
                es.a(a2, c(i2), b2);
                es.a(a2);
            }
        }
    }
}
