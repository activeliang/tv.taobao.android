package com.loc;

import android.content.Context;
import android.text.TextUtils;

/* compiled from: StatisticsEntity */
public final class bf {
    private Context a;
    private String b;
    private String c;
    private String d;
    private String e;

    public bf(Context context, String str, String str2, String str3) throws j {
        if (TextUtils.isEmpty(str3) || str3.length() > 256) {
            throw new j("无效的参数 - IllegalArgumentException");
        }
        this.a = context.getApplicationContext();
        this.c = str;
        this.d = str2;
        this.b = str3;
    }

    public final void a(String str) throws j {
        if (TextUtils.isEmpty(str) || str.length() > 65536) {
            throw new j("无效的参数 - IllegalArgumentException");
        }
        this.e = str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0097 A[SYNTHETIC, Splitter:B:29:0x0097] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00a4 A[SYNTHETIC, Splitter:B:36:0x00a4] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final byte[] a() {
        /*
            r8 = this;
            r1 = 0
            r3 = 0
            byte[] r0 = new byte[r1]
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x008a, all -> 0x00a0 }
            r2.<init>()     // Catch:{ Throwable -> 0x008a, all -> 0x00a0 }
            java.lang.String r3 = r8.c     // Catch:{ Throwable -> 0x00b1 }
            com.loc.u.a((java.io.ByteArrayOutputStream) r2, (java.lang.String) r3)     // Catch:{ Throwable -> 0x00b1 }
            java.lang.String r3 = r8.d     // Catch:{ Throwable -> 0x00b1 }
            com.loc.u.a((java.io.ByteArrayOutputStream) r2, (java.lang.String) r3)     // Catch:{ Throwable -> 0x00b1 }
            java.lang.String r3 = r8.b     // Catch:{ Throwable -> 0x00b1 }
            com.loc.u.a((java.io.ByteArrayOutputStream) r2, (java.lang.String) r3)     // Catch:{ Throwable -> 0x00b1 }
            android.content.Context r3 = r8.a     // Catch:{ Throwable -> 0x00b1 }
            int r3 = com.loc.n.r(r3)     // Catch:{ Throwable -> 0x00b1 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Throwable -> 0x00b1 }
            com.loc.u.a((java.io.ByteArrayOutputStream) r2, (java.lang.String) r3)     // Catch:{ Throwable -> 0x00b1 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00b3 }
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            int r1 = (int) r4
        L_0x002d:
            r3 = 4
            byte[] r3 = new byte[r3]     // Catch:{ Throwable -> 0x00b1 }
            r4 = 0
            int r5 = r1 >> 24
            r5 = r5 & 255(0xff, float:3.57E-43)
            byte r5 = (byte) r5     // Catch:{ Throwable -> 0x00b1 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x00b1 }
            r4 = 1
            int r5 = r1 >> 16
            r5 = r5 & 255(0xff, float:3.57E-43)
            byte r5 = (byte) r5     // Catch:{ Throwable -> 0x00b1 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x00b1 }
            r4 = 2
            int r5 = r1 >> 8
            r5 = r5 & 255(0xff, float:3.57E-43)
            byte r5 = (byte) r5     // Catch:{ Throwable -> 0x00b1 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x00b1 }
            r4 = 3
            r1 = r1 & 255(0xff, float:3.57E-43)
            byte r1 = (byte) r1     // Catch:{ Throwable -> 0x00b1 }
            r3[r4] = r1     // Catch:{ Throwable -> 0x00b1 }
            r2.write(r3)     // Catch:{ Throwable -> 0x00b1 }
            java.lang.String r1 = r8.e     // Catch:{ Throwable -> 0x00b1 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x00b1 }
            if (r1 == 0) goto L_0x0075
            r1 = 2
            byte[] r1 = new byte[r1]     // Catch:{ Throwable -> 0x00b1 }
            r1 = {0, 0} // fill-array     // Catch:{ Throwable -> 0x00b1 }
        L_0x005f:
            r2.write(r1)     // Catch:{ Throwable -> 0x00b1 }
            java.lang.String r1 = r8.e     // Catch:{ Throwable -> 0x00b1 }
            byte[] r1 = com.loc.u.a((java.lang.String) r1)     // Catch:{ Throwable -> 0x00b1 }
            r2.write(r1)     // Catch:{ Throwable -> 0x00b1 }
            byte[] r0 = r2.toByteArray()     // Catch:{ Throwable -> 0x00b1 }
            if (r2 == 0) goto L_0x0074
            r2.close()     // Catch:{ Throwable -> 0x00ad }
        L_0x0074:
            return r0
        L_0x0075:
            java.lang.String r1 = r8.e     // Catch:{ Throwable -> 0x00b1 }
            byte[] r1 = com.loc.u.a((java.lang.String) r1)     // Catch:{ Throwable -> 0x00b1 }
            if (r1 != 0) goto L_0x0084
            r1 = 2
            byte[] r1 = new byte[r1]     // Catch:{ Throwable -> 0x00b1 }
            r1 = {0, 0} // fill-array     // Catch:{ Throwable -> 0x00b1 }
            goto L_0x005f
        L_0x0084:
            int r1 = r1.length     // Catch:{ Throwable -> 0x00b1 }
            byte[] r1 = com.loc.u.a((int) r1)     // Catch:{ Throwable -> 0x00b1 }
            goto L_0x005f
        L_0x008a:
            r1 = move-exception
            r2 = r3
        L_0x008c:
            java.lang.String r3 = "se"
            java.lang.String r4 = "tds"
            com.loc.ab.b((java.lang.Throwable) r1, (java.lang.String) r3, (java.lang.String) r4)     // Catch:{ all -> 0x00af }
            if (r2 == 0) goto L_0x0074
            r2.close()     // Catch:{ Throwable -> 0x009b }
            goto L_0x0074
        L_0x009b:
            r1 = move-exception
        L_0x009c:
            r1.printStackTrace()
            goto L_0x0074
        L_0x00a0:
            r0 = move-exception
            r2 = r3
        L_0x00a2:
            if (r2 == 0) goto L_0x00a7
            r2.close()     // Catch:{ Throwable -> 0x00a8 }
        L_0x00a7:
            throw r0
        L_0x00a8:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00a7
        L_0x00ad:
            r1 = move-exception
            goto L_0x009c
        L_0x00af:
            r0 = move-exception
            goto L_0x00a2
        L_0x00b1:
            r1 = move-exception
            goto L_0x008c
        L_0x00b3:
            r3 = move-exception
            goto L_0x002d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bf.a():byte[]");
    }
}
