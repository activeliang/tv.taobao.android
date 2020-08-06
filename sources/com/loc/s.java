package com.loc;

import android.content.Context;
import android.net.Proxy;
import android.os.Build;
import com.bftv.fui.constantplugin.Constant;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

/* compiled from: ProxyUtil */
public final class s {
    private static String a() {
        String str;
        try {
            str = Proxy.getDefaultHost();
        } catch (Throwable th) {
            ab.b(th, "pu", "gdh");
            str = null;
        }
        return str == null ? Constant.NULL : str;
    }

    public static java.net.Proxy a(Context context) {
        try {
            return Build.VERSION.SDK_INT >= 11 ? a(context, new URI("http://restapi.amap.com")) : b(context);
        } catch (Throwable th) {
            ab.b(th, "pu", "gp");
            return null;
        }
    }

    private static java.net.Proxy a(Context context, URI uri) {
        if (c(context)) {
            try {
                List<java.net.Proxy> select = ProxySelector.getDefault().select(uri);
                if (select == null || select.isEmpty()) {
                    return null;
                }
                java.net.Proxy proxy = select.get(0);
                if (proxy == null || proxy.type() == Proxy.Type.DIRECT) {
                    return null;
                }
                return proxy;
            } catch (Throwable th) {
                ab.b(th, "pu", "gpsc");
            }
        }
        return null;
    }

    private static int b() {
        try {
            return android.net.Proxy.getDefaultPort();
        } catch (Throwable th) {
            ab.b(th, "pu", "gdp");
            return -1;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:101:0x0146 A[SYNTHETIC, Splitter:B:101:0x0146] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x006b A[SYNTHETIC, Splitter:B:29:0x006b] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0071 A[SYNTHETIC, Splitter:B:33:0x0071] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x007c A[Catch:{ Throwable -> 0x014d }] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00cc A[Catch:{ all -> 0x018c }] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0100 A[SYNTHETIC, Splitter:B:75:0x0100] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x013b A[SYNTHETIC, Splitter:B:95:0x013b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.net.Proxy b(android.content.Context r12) {
        /*
            r6 = 80
            r10 = 0
            r9 = 1
            r7 = 0
            r8 = -1
            boolean r0 = c(r12)
            if (r0 == 0) goto L_0x015a
            java.lang.String r0 = "content://telephony/carriers/preferapn"
            android.net.Uri r1 = android.net.Uri.parse(r0)
            android.content.ContentResolver r0 = r12.getContentResolver()
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r2 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ SecurityException -> 0x00b9, Throwable -> 0x0129, all -> 0x0142 }
            if (r2 == 0) goto L_0x01e0
            boolean r0 = r2.moveToFirst()     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
            if (r0 == 0) goto L_0x01e0
            java.lang.String r0 = "apn"
            int r0 = r2.getColumnIndex(r0)     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
            java.lang.String r0 = r2.getString(r0)     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
            if (r0 == 0) goto L_0x003a
            java.util.Locale r1 = java.util.Locale.US     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
            java.lang.String r0 = r0.toLowerCase(r1)     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
        L_0x003a:
            if (r0 == 0) goto L_0x0088
            java.lang.String r1 = "ctwap"
            boolean r1 = r0.contains(r1)     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
            if (r1 == 0) goto L_0x0088
            java.lang.String r3 = a()     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
            int r0 = b()     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
            boolean r1 = android.text.TextUtils.isEmpty(r3)     // Catch:{ SecurityException -> 0x01a9, Throwable -> 0x0193 }
            if (r1 != 0) goto L_0x01e7
            java.lang.String r1 = "null"
            boolean r1 = r3.equals(r1)     // Catch:{ SecurityException -> 0x01a9, Throwable -> 0x0193 }
            if (r1 != 0) goto L_0x01e7
            r1 = r9
        L_0x005d:
            if (r1 != 0) goto L_0x01e4
            java.lang.String r1 = "QMTAuMC4wLjIwMA=="
            java.lang.String r1 = com.loc.u.c((java.lang.String) r1)     // Catch:{ SecurityException -> 0x01b1, Throwable -> 0x0199 }
        L_0x0066:
            if (r0 != r8) goto L_0x0069
            r0 = r6
        L_0x0069:
            if (r2 == 0) goto L_0x006e
            r2.close()     // Catch:{ Throwable -> 0x017e }
        L_0x006e:
            r6 = r0
        L_0x006f:
            if (r1 == 0) goto L_0x014a
            int r0 = r1.length()     // Catch:{ Throwable -> 0x014d }
            if (r0 <= 0) goto L_0x014a
            if (r6 == r8) goto L_0x014a
            r0 = r9
        L_0x007a:
            if (r0 == 0) goto L_0x015a
            java.net.Proxy r0 = new java.net.Proxy     // Catch:{ Throwable -> 0x014d }
            java.net.Proxy$Type r2 = java.net.Proxy.Type.HTTP     // Catch:{ Throwable -> 0x014d }
            java.net.InetSocketAddress r1 = java.net.InetSocketAddress.createUnresolved(r1, r6)     // Catch:{ Throwable -> 0x014d }
            r0.<init>(r2, r1)     // Catch:{ Throwable -> 0x014d }
        L_0x0087:
            return r0
        L_0x0088:
            if (r0 == 0) goto L_0x01e0
            java.lang.String r1 = "wap"
            boolean r0 = r0.contains(r1)     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
            if (r0 == 0) goto L_0x01e0
            java.lang.String r3 = a()     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
            int r1 = b()     // Catch:{ SecurityException -> 0x01a3, Throwable -> 0x018f }
            boolean r0 = android.text.TextUtils.isEmpty(r3)     // Catch:{ SecurityException -> 0x01b8, Throwable -> 0x019e }
            if (r0 != 0) goto L_0x01dc
            java.lang.String r0 = "null"
            boolean r0 = r3.equals(r0)     // Catch:{ SecurityException -> 0x01b8, Throwable -> 0x019e }
            if (r0 != 0) goto L_0x01dc
            r0 = r9
        L_0x00ab:
            if (r0 != 0) goto L_0x01d9
            java.lang.String r0 = "QMTAuMC4wLjE3Mg=="
            java.lang.String r0 = com.loc.u.c((java.lang.String) r0)     // Catch:{ SecurityException -> 0x01bf, Throwable -> 0x01a1 }
        L_0x00b4:
            if (r1 != r8) goto L_0x01d4
            r1 = r0
            r0 = r6
            goto L_0x0069
        L_0x00b9:
            r0 = move-exception
            r1 = r7
            r2 = r8
            r3 = r7
        L_0x00bd:
            java.lang.String r4 = "pu"
            java.lang.String r5 = "ghp"
            com.loc.ab.b((java.lang.Throwable) r0, (java.lang.String) r4, (java.lang.String) r5)     // Catch:{ all -> 0x018c }
            java.lang.String r0 = com.loc.n.t(r12)     // Catch:{ all -> 0x018c }
            if (r0 == 0) goto L_0x01c9
            java.util.Locale r2 = java.util.Locale.US     // Catch:{ all -> 0x018c }
            java.lang.String r4 = r0.toLowerCase(r2)     // Catch:{ all -> 0x018c }
            java.lang.String r0 = a()     // Catch:{ all -> 0x018c }
            int r2 = b()     // Catch:{ all -> 0x018c }
            java.lang.String r5 = "ctwap"
            int r5 = r4.indexOf(r5)     // Catch:{ all -> 0x018c }
            if (r5 == r8) goto L_0x0106
            boolean r4 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x018c }
            if (r4 != 0) goto L_0x01d0
            java.lang.String r4 = "null"
            boolean r4 = r0.equals(r4)     // Catch:{ all -> 0x018c }
            if (r4 != 0) goto L_0x01d0
            r3 = r9
        L_0x00f3:
            if (r3 != 0) goto L_0x00fc
            java.lang.String r0 = "QMTAuMC4wLjIwMA=="
            java.lang.String r0 = com.loc.u.c((java.lang.String) r0)     // Catch:{ all -> 0x018c }
        L_0x00fc:
            if (r2 != r8) goto L_0x01cd
        L_0x00fe:
            if (r1 == 0) goto L_0x0103
            r1.close()     // Catch:{ Throwable -> 0x0168 }
        L_0x0103:
            r1 = r0
            goto L_0x006f
        L_0x0106:
            java.lang.String r5 = "wap"
            int r4 = r4.indexOf(r5)     // Catch:{ all -> 0x018c }
            if (r4 == r8) goto L_0x01c9
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x018c }
            if (r2 != 0) goto L_0x01c5
            java.lang.String r2 = "null"
            boolean r2 = r0.equals(r2)     // Catch:{ all -> 0x018c }
            if (r2 != 0) goto L_0x01c5
            r2 = r9
        L_0x011f:
            if (r2 != 0) goto L_0x00fe
            java.lang.String r0 = "QMTAuMC4wLjE3Mg=="
            java.lang.String r0 = com.loc.u.c((java.lang.String) r0)     // Catch:{ all -> 0x018c }
            goto L_0x00fe
        L_0x0129:
            r0 = move-exception
            r2 = r7
            r1 = r8
            r3 = r7
        L_0x012d:
            java.lang.String r4 = "pu"
            java.lang.String r5 = "gPx1"
            com.loc.ab.b((java.lang.Throwable) r0, (java.lang.String) r4, (java.lang.String) r5)     // Catch:{ all -> 0x018a }
            r0.printStackTrace()     // Catch:{ all -> 0x018a }
            if (r2 == 0) goto L_0x013e
            r2.close()     // Catch:{ Throwable -> 0x0173 }
        L_0x013e:
            r6 = r1
            r1 = r3
            goto L_0x006f
        L_0x0142:
            r0 = move-exception
            r2 = r7
        L_0x0144:
            if (r2 == 0) goto L_0x0149
            r2.close()     // Catch:{ Throwable -> 0x015d }
        L_0x0149:
            throw r0
        L_0x014a:
            r0 = r10
            goto L_0x007a
        L_0x014d:
            r0 = move-exception
            java.lang.String r1 = "pu"
            java.lang.String r2 = "gp2"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r1, (java.lang.String) r2)
            r0.printStackTrace()
        L_0x015a:
            r0 = r7
            goto L_0x0087
        L_0x015d:
            r1 = move-exception
            java.lang.String r2 = "pu"
            java.lang.String r3 = "gPx2"
            com.loc.ab.b((java.lang.Throwable) r1, (java.lang.String) r2, (java.lang.String) r3)
            goto L_0x0149
        L_0x0168:
            r1 = move-exception
            java.lang.String r2 = "pu"
            java.lang.String r3 = "gPx2"
            com.loc.ab.b((java.lang.Throwable) r1, (java.lang.String) r2, (java.lang.String) r3)
            goto L_0x0103
        L_0x0173:
            r0 = move-exception
            java.lang.String r2 = "pu"
            java.lang.String r4 = "gPx2"
            com.loc.ab.b((java.lang.Throwable) r0, (java.lang.String) r2, (java.lang.String) r4)
            goto L_0x013e
        L_0x017e:
            r2 = move-exception
            java.lang.String r3 = "pu"
            java.lang.String r4 = "gPx2"
            com.loc.ab.b((java.lang.Throwable) r2, (java.lang.String) r3, (java.lang.String) r4)
            goto L_0x006e
        L_0x018a:
            r0 = move-exception
            goto L_0x0144
        L_0x018c:
            r0 = move-exception
            r2 = r1
            goto L_0x0144
        L_0x018f:
            r0 = move-exception
            r1 = r8
            r3 = r7
            goto L_0x012d
        L_0x0193:
            r1 = move-exception
            r3 = r7
            r11 = r0
            r0 = r1
            r1 = r11
            goto L_0x012d
        L_0x0199:
            r1 = move-exception
            r11 = r1
            r1 = r0
            r0 = r11
            goto L_0x012d
        L_0x019e:
            r0 = move-exception
            r3 = r7
            goto L_0x012d
        L_0x01a1:
            r0 = move-exception
            goto L_0x012d
        L_0x01a3:
            r0 = move-exception
            r1 = r2
            r3 = r7
            r2 = r8
            goto L_0x00bd
        L_0x01a9:
            r1 = move-exception
            r3 = r7
            r11 = r2
            r2 = r0
            r0 = r1
            r1 = r11
            goto L_0x00bd
        L_0x01b1:
            r1 = move-exception
            r11 = r1
            r1 = r2
            r2 = r0
            r0 = r11
            goto L_0x00bd
        L_0x01b8:
            r0 = move-exception
            r3 = r7
            r11 = r1
            r1 = r2
            r2 = r11
            goto L_0x00bd
        L_0x01bf:
            r0 = move-exception
            r11 = r2
            r2 = r1
            r1 = r11
            goto L_0x00bd
        L_0x01c5:
            r2 = r10
            r0 = r3
            goto L_0x011f
        L_0x01c9:
            r6 = r2
            r0 = r3
            goto L_0x00fe
        L_0x01cd:
            r6 = r2
            goto L_0x00fe
        L_0x01d0:
            r0 = r3
            r3 = r10
            goto L_0x00f3
        L_0x01d4:
            r11 = r1
            r1 = r0
            r0 = r11
            goto L_0x0069
        L_0x01d9:
            r0 = r3
            goto L_0x00b4
        L_0x01dc:
            r0 = r10
            r3 = r7
            goto L_0x00ab
        L_0x01e0:
            r0 = r8
            r1 = r7
            goto L_0x0069
        L_0x01e4:
            r1 = r3
            goto L_0x0066
        L_0x01e7:
            r1 = r10
            r3 = r7
            goto L_0x005d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.s.b(android.content.Context):java.net.Proxy");
    }

    private static boolean c(Context context) {
        return n.r(context) == 0;
    }
}
