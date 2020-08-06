package com.loc;

import android.content.Context;

/* compiled from: LocNetManager */
public final class ei {
    private static ei b = null;
    aw a = null;
    private Context c = null;
    private int d = en.g;
    private boolean e = false;
    private int f = 0;

    private ei(Context context) {
        try {
            p.a().a(context);
        } catch (Throwable th) {
        }
        this.c = context;
        this.a = aw.a();
    }

    public static ei a(Context context) {
        if (b == null) {
            b = new ei(context);
        }
        return b;
    }

    public final int a() {
        return this.d;
    }

    public final ba a(ej ejVar) throws Throwable {
        return aw.a(ejVar, this.e || et.k(this.c));
    }

    public final ba a(ej ejVar, int i) throws Throwable {
        return aw.a(ejVar, this.e || et.k(this.c), i);
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.loc.ej a(android.content.Context r8, byte[] r9, java.lang.String r10, java.lang.String r11, boolean r12) {
        /*
            r7 = this;
            r1 = 0
            java.util.HashMap r2 = new java.util.HashMap     // Catch:{ Throwable -> 0x0132 }
            r0 = 16
            r2.<init>(r0)     // Catch:{ Throwable -> 0x0132 }
            com.loc.ej r0 = new com.loc.ej     // Catch:{ Throwable -> 0x0132 }
            com.loc.t r3 = com.loc.en.c()     // Catch:{ Throwable -> 0x0132 }
            r0.<init>(r8, r3)     // Catch:{ Throwable -> 0x0132 }
            java.lang.String r1 = "Content-Type"
            java.lang.String r3 = "application/octet-stream"
            r2.put(r1, r3)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = "Accept-Encoding"
            java.lang.String r3 = "gzip"
            r2.put(r1, r3)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = "gzipped"
            java.lang.String r3 = "1"
            r2.put(r1, r3)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = "Connection"
            java.lang.String r3 = "Keep-Alive"
            r2.put(r1, r3)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = "User-Agent"
            java.lang.String r3 = "AMAP_Location_SDK_Android 4.9.0"
            r2.put(r1, r3)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = "KEY"
            java.lang.String r3 = com.loc.k.f(r8)     // Catch:{ Throwable -> 0x011c }
            r2.put(r1, r3)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = "enginever"
            java.lang.String r3 = "5.1"
            r2.put(r1, r3)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = com.loc.m.a()     // Catch:{ Throwable -> 0x011c }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x011c }
            java.lang.String r4 = "key="
            r3.<init>(r4)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r4 = com.loc.k.f(r8)     // Catch:{ Throwable -> 0x011c }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x011c }
            java.lang.String r3 = com.loc.m.a((android.content.Context) r8, (java.lang.String) r1, (java.lang.String) r3)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r4 = "ts"
            r2.put(r4, r1)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = "scode"
            r2.put(r1, r3)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = "encr"
            java.lang.String r3 = "1"
            r2.put(r1, r3)     // Catch:{ Throwable -> 0x011c }
            r0.f = r2     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = "loc"
            if (r12 != 0) goto L_0x008c
            java.lang.String r1 = "locf"
        L_0x008c:
            r2 = 1
            r0.n = r2     // Catch:{ Throwable -> 0x011c }
            java.util.Locale r2 = java.util.Locale.US     // Catch:{ Throwable -> 0x011c }
            java.lang.String r3 = "platform=Android&sdkversion=%s&product=%s&loc_channel=%s"
            r4 = 3
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x011c }
            r5 = 0
            java.lang.String r6 = "4.9.0"
            r4[r5] = r6     // Catch:{ Throwable -> 0x011c }
            r5 = 1
            r4[r5] = r1     // Catch:{ Throwable -> 0x011c }
            r1 = 2
            r5 = 3
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x011c }
            r4[r1] = r5     // Catch:{ Throwable -> 0x011c }
            java.lang.String r1 = java.lang.String.format(r2, r3, r4)     // Catch:{ Throwable -> 0x011c }
            r0.l = r1     // Catch:{ Throwable -> 0x011c }
            r0.k = r12     // Catch:{ Throwable -> 0x011c }
            r0.g = r10     // Catch:{ Throwable -> 0x011c }
            r0.h = r11     // Catch:{ Throwable -> 0x011c }
            byte[] r1 = com.loc.et.a((byte[]) r9)     // Catch:{ Throwable -> 0x011c }
            r0.i = r1     // Catch:{ Throwable -> 0x011c }
            java.net.Proxy r1 = com.loc.s.a(r8)     // Catch:{ Throwable -> 0x011c }
            r0.a((java.net.Proxy) r1)     // Catch:{ Throwable -> 0x011c }
            java.util.HashMap r1 = new java.util.HashMap     // Catch:{ Throwable -> 0x011c }
            r2 = 16
            r1.<init>(r2)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r2 = "output"
            java.lang.String r3 = "bin"
            r1.put(r2, r3)     // Catch:{ Throwable -> 0x011c }
            java.lang.String r2 = "policy"
            java.lang.String r3 = "3103"
            r1.put(r2, r3)     // Catch:{ Throwable -> 0x011c }
            int r2 = r7.f     // Catch:{ Throwable -> 0x011c }
            switch(r2) {
                case 0: goto L_0x0115;
                case 1: goto L_0x011e;
                case 2: goto L_0x0128;
                default: goto L_0x00df;
            }     // Catch:{ Throwable -> 0x011c }
        L_0x00df:
            java.lang.String r2 = "custom"
            r1.remove(r2)     // Catch:{ Throwable -> 0x011c }
        L_0x00e5:
            r0.m = r1     // Catch:{ Throwable -> 0x011c }
            int r1 = r7.d     // Catch:{ Throwable -> 0x011c }
            r0.a((int) r1)     // Catch:{ Throwable -> 0x011c }
            int r1 = r7.d     // Catch:{ Throwable -> 0x011c }
            r0.b(r1)     // Catch:{ Throwable -> 0x011c }
            boolean r1 = r7.e     // Catch:{ Throwable -> 0x011c }
            if (r1 != 0) goto L_0x00fb
            boolean r1 = com.loc.et.k(r8)     // Catch:{ Throwable -> 0x011c }
            if (r1 == 0) goto L_0x0114
        L_0x00fb:
            java.lang.String r1 = "http:"
            boolean r1 = r10.startsWith(r1)     // Catch:{ Throwable -> 0x011c }
            if (r1 == 0) goto L_0x0114
            java.lang.String r1 = r0.c()     // Catch:{ Throwable -> 0x011c }
            java.lang.String r2 = "https:"
            java.lang.String r3 = "https:"
            java.lang.String r1 = r1.replace(r2, r3)     // Catch:{ Throwable -> 0x011c }
            r0.g = r1     // Catch:{ Throwable -> 0x011c }
        L_0x0114:
            return r0
        L_0x0115:
            java.lang.String r2 = "custom"
            r1.remove(r2)     // Catch:{ Throwable -> 0x011c }
            goto L_0x00e5
        L_0x011c:
            r1 = move-exception
            goto L_0x0114
        L_0x011e:
            java.lang.String r2 = "custom"
            java.lang.String r3 = "language:cn"
            r1.put(r2, r3)     // Catch:{ Throwable -> 0x011c }
            goto L_0x00e5
        L_0x0128:
            java.lang.String r2 = "custom"
            java.lang.String r3 = "language:en"
            r1.put(r2, r3)     // Catch:{ Throwable -> 0x011c }
            goto L_0x00e5
        L_0x0132:
            r0 = move-exception
            r0 = r1
            goto L_0x0114
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.a(android.content.Context, byte[], java.lang.String, java.lang.String, boolean):com.loc.ej");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00f4 A[Catch:{ Throwable -> 0x015c }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x013c A[SYNTHETIC, Splitter:B:37:0x013c] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x014b A[SYNTHETIC, Splitter:B:45:0x014b] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0154 A[SYNTHETIC, Splitter:B:51:0x0154] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0095 A[SYNTHETIC, Splitter:B:9:0x0095] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.String a(android.content.Context r11, double r12, double r14) {
        /*
            r10 = this;
            r1 = 0
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ Throwable -> 0x0116 }
            r0 = 16
            r3.<init>(r0)     // Catch:{ Throwable -> 0x0116 }
            com.loc.ej r4 = new com.loc.ej     // Catch:{ Throwable -> 0x0116 }
            com.loc.t r0 = com.loc.en.c()     // Catch:{ Throwable -> 0x0116 }
            r4.<init>(r11, r0)     // Catch:{ Throwable -> 0x0116 }
            r3.clear()     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r0 = "Content-Type"
            java.lang.String r2 = "application/x-www-form-urlencoded"
            r3.put(r0, r2)     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r0 = "Connection"
            java.lang.String r2 = "Keep-Alive"
            r3.put(r0, r2)     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r0 = "User-Agent"
            java.lang.String r2 = "AMAP_Location_SDK_Android 4.9.0"
            r3.put(r0, r2)     // Catch:{ Throwable -> 0x0116 }
            java.util.HashMap r5 = new java.util.HashMap     // Catch:{ Throwable -> 0x0116 }
            r0 = 16
            r5.<init>(r0)     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r0 = "custom"
            java.lang.String r2 = "26260A1F00020002"
            r5.put(r0, r2)     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r0 = "key"
            java.lang.String r2 = com.loc.k.f(r11)     // Catch:{ Throwable -> 0x0116 }
            r5.put(r0, r2)     // Catch:{ Throwable -> 0x0116 }
            int r0 = r10.f     // Catch:{ Throwable -> 0x0116 }
            switch(r0) {
                case 0: goto L_0x010e;
                case 1: goto L_0x0119;
                case 2: goto L_0x0124;
                default: goto L_0x004e;
            }     // Catch:{ Throwable -> 0x0116 }
        L_0x004e:
            java.lang.String r0 = "language"
            r5.remove(r0)     // Catch:{ Throwable -> 0x0116 }
        L_0x0054:
            java.lang.String r0 = com.loc.m.a()     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r2 = com.loc.u.b((java.util.Map<java.lang.String, java.lang.String>) r5)     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r2 = com.loc.m.a((android.content.Context) r11, (java.lang.String) r0, (java.lang.String) r2)     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r6 = "ts"
            r5.put(r6, r0)     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r0 = "scode"
            r5.put(r0, r2)     // Catch:{ Throwable -> 0x0116 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r2 = "output=json&radius=1000&extensions=all&location="
            r0.<init>(r2)     // Catch:{ Throwable -> 0x0116 }
            java.lang.StringBuilder r0 = r0.append(r14)     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r2 = ","
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Throwable -> 0x0116 }
            java.lang.StringBuilder r0 = r0.append(r12)     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r2 = "UTF-8"
            byte[] r0 = r0.getBytes(r2)     // Catch:{ Throwable -> 0x0116 }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x0135, all -> 0x0147 }
            r2.<init>()     // Catch:{ Throwable -> 0x0135, all -> 0x0147 }
            if (r0 == 0) goto L_0x00a0
            int r6 = r0.length     // Catch:{ Throwable -> 0x016a }
            byte[] r6 = com.loc.u.a((int) r6)     // Catch:{ Throwable -> 0x016a }
            r2.write(r6)     // Catch:{ Throwable -> 0x016a }
            r2.write(r0)     // Catch:{ Throwable -> 0x016a }
        L_0x00a0:
            byte[] r0 = r2.toByteArray()     // Catch:{ Throwable -> 0x016a }
            r4.j = r0     // Catch:{ Throwable -> 0x016a }
            r2.close()     // Catch:{ IOException -> 0x012f }
        L_0x00a9:
            r0 = 0
            r4.n = r0     // Catch:{ Throwable -> 0x0116 }
            r0 = 1
            r4.k = r0     // Catch:{ Throwable -> 0x0116 }
            java.util.Locale r0 = java.util.Locale.US     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r2 = "platform=Android&sdkversion=%s&product=%s&loc_channel=%s"
            r6 = 3
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x0116 }
            r7 = 0
            java.lang.String r8 = "4.9.0"
            r6[r7] = r8     // Catch:{ Throwable -> 0x0116 }
            r7 = 1
            java.lang.String r8 = "loc"
            r6[r7] = r8     // Catch:{ Throwable -> 0x0116 }
            r7 = 2
            r8 = 3
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Throwable -> 0x0116 }
            r6[r7] = r8     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r0 = java.lang.String.format(r0, r2, r6)     // Catch:{ Throwable -> 0x0116 }
            r4.l = r0     // Catch:{ Throwable -> 0x0116 }
            r4.m = r5     // Catch:{ Throwable -> 0x0116 }
            r4.f = r3     // Catch:{ Throwable -> 0x0116 }
            java.net.Proxy r0 = com.loc.s.a(r11)     // Catch:{ Throwable -> 0x0116 }
            r4.a((java.net.Proxy) r0)     // Catch:{ Throwable -> 0x0116 }
            int r0 = com.loc.en.g     // Catch:{ Throwable -> 0x0116 }
            r4.a((int) r0)     // Catch:{ Throwable -> 0x0116 }
            int r0 = com.loc.en.g     // Catch:{ Throwable -> 0x0116 }
            r4.b(r0)     // Catch:{ Throwable -> 0x0116 }
            java.lang.String r0 = "http://restapi.amap.com/v3/geocode/regeo"
            java.lang.String r2 = "http://dualstack-restapi.amap.com/v3/geocode/regeo"
            r4.h = r2     // Catch:{ Throwable -> 0x015c }
            boolean r2 = com.loc.et.k(r11)     // Catch:{ Throwable -> 0x015c }
            if (r2 == 0) goto L_0x0154
            java.lang.String r2 = "http:"
            java.lang.String r3 = "https:"
            java.lang.String r0 = r0.replace(r2, r3)     // Catch:{ Throwable -> 0x015c }
            r4.g = r0     // Catch:{ Throwable -> 0x015c }
            byte[] r0 = com.loc.aw.a(r4)     // Catch:{ Throwable -> 0x015c }
            r2 = r0
        L_0x0105:
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x015c }
            java.lang.String r3 = "utf-8"
            r0.<init>(r2, r3)     // Catch:{ Throwable -> 0x015c }
        L_0x010d:
            return r0
        L_0x010e:
            java.lang.String r0 = "language"
            r5.remove(r0)     // Catch:{ Throwable -> 0x0116 }
            goto L_0x0054
        L_0x0116:
            r0 = move-exception
            r0 = r1
            goto L_0x010d
        L_0x0119:
            java.lang.String r0 = "language"
            java.lang.String r2 = "zh-CN"
            r5.put(r0, r2)     // Catch:{ Throwable -> 0x0116 }
            goto L_0x0054
        L_0x0124:
            java.lang.String r0 = "language"
            java.lang.String r2 = "en"
            r5.put(r0, r2)     // Catch:{ Throwable -> 0x0116 }
            goto L_0x0054
        L_0x012f:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0116 }
            goto L_0x00a9
        L_0x0135:
            r0 = move-exception
            r2 = r1
        L_0x0137:
            r0.printStackTrace()     // Catch:{ all -> 0x0168 }
            if (r2 == 0) goto L_0x00a9
            r2.close()     // Catch:{ IOException -> 0x0141 }
            goto L_0x00a9
        L_0x0141:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0116 }
            goto L_0x00a9
        L_0x0147:
            r0 = move-exception
            r2 = r1
        L_0x0149:
            if (r2 == 0) goto L_0x014e
            r2.close()     // Catch:{ IOException -> 0x014f }
        L_0x014e:
            throw r0     // Catch:{ Throwable -> 0x0116 }
        L_0x014f:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ Throwable -> 0x0116 }
            goto L_0x014e
        L_0x0154:
            r4.g = r0     // Catch:{ Throwable -> 0x015c }
            byte[] r0 = com.loc.aw.b(r4)     // Catch:{ Throwable -> 0x015c }
            r2 = r0
            goto L_0x0105
        L_0x015c:
            r0 = move-exception
            java.lang.String r2 = "LocNetManager"
            java.lang.String r3 = "post"
            com.loc.en.a(r0, r2, r3)     // Catch:{ Throwable -> 0x0116 }
            r0 = r1
            goto L_0x010d
        L_0x0168:
            r0 = move-exception
            goto L_0x0149
        L_0x016a:
            r0 = move-exception
            goto L_0x0137
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.a(android.content.Context, double, double):java.lang.String");
    }

    public final void a(long j, boolean z, int i) {
        try {
            this.e = z;
            try {
                p.a().a(z);
            } catch (Throwable th) {
            }
            this.d = Long.valueOf(j).intValue();
            this.f = i;
        } catch (Throwable th2) {
            en.a(th2, "LocNetManager", "setOption");
        }
    }
}
