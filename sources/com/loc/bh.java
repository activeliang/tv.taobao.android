package com.loc;

import android.content.Context;
import java.lang.ref.WeakReference;

/* compiled from: Utils */
public final class bh {
    public static bb a(WeakReference<bb> weakReference) {
        if (weakReference == null || weakReference.get() == null) {
            weakReference = new WeakReference<>(new bb());
        }
        return (bb) weakReference.get();
    }

    public static String a(Context context, t tVar) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("\"sim\":\"").append(n.f(context)).append("\",\"sdkversion\":\"").append(tVar.c()).append("\",\"product\":\"").append(tVar.a()).append("\",\"ed\":\"").append(tVar.d()).append("\",\"nt\":\"").append(n.d(context)).append("\",\"np\":\"").append(n.b(context)).append("\",\"mnc\":\"").append(n.c(context)).append("\",\"ant\":\"").append(n.e(context)).append("\"");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }

    public static void a(Context context, bb bbVar, String str, int i, int i2, String str2) {
        bbVar.a = z.c(context, str);
        bbVar.d = i;
        bbVar.b = (long) i2;
        bbVar.c = str2;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: com.loc.as$b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: com.loc.as$b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: com.loc.as$b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: com.loc.as$b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: com.loc.as$b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: com.loc.as$b} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0054 A[SYNTHETIC, Splitter:B:40:0x0054] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x005e A[SYNTHETIC, Splitter:B:46:0x005e] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0063 A[SYNTHETIC, Splitter:B:49:0x0063] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static byte[] a(com.loc.as r6, java.lang.String r7) {
        /*
            r2 = 0
            r1 = 0
            r0 = 0
            byte[] r0 = new byte[r0]
            com.loc.as$b r3 = r6.a((java.lang.String) r7)     // Catch:{ Throwable -> 0x0042, all -> 0x005a }
            if (r3 != 0) goto L_0x0016
            if (r2 == 0) goto L_0x0010
            r1.close()     // Catch:{ Throwable -> 0x0076 }
        L_0x0010:
            if (r3 == 0) goto L_0x0015
            r3.close()     // Catch:{ Throwable -> 0x007b }
        L_0x0015:
            return r0
        L_0x0016:
            java.io.InputStream r2 = r3.a()     // Catch:{ Throwable -> 0x0089 }
            if (r2 != 0) goto L_0x002c
            if (r2 == 0) goto L_0x0021
            r2.close()     // Catch:{ Throwable -> 0x007d }
        L_0x0021:
            if (r3 == 0) goto L_0x0015
            r3.close()     // Catch:{ Throwable -> 0x0027 }
            goto L_0x0015
        L_0x0027:
            r1 = move-exception
        L_0x0028:
            r1.printStackTrace()
            goto L_0x0015
        L_0x002c:
            int r1 = r2.available()     // Catch:{ Throwable -> 0x0089 }
            byte[] r0 = new byte[r1]     // Catch:{ Throwable -> 0x0089 }
            r2.read(r0)     // Catch:{ Throwable -> 0x0089 }
            if (r2 == 0) goto L_0x003a
            r2.close()     // Catch:{ Throwable -> 0x0082 }
        L_0x003a:
            if (r3 == 0) goto L_0x0015
            r3.close()     // Catch:{ Throwable -> 0x0040 }
            goto L_0x0015
        L_0x0040:
            r1 = move-exception
            goto L_0x0028
        L_0x0042:
            r1 = move-exception
            r3 = r2
        L_0x0044:
            java.lang.String r4 = "sui"
            java.lang.String r5 = "rdS"
            com.loc.ab.b((java.lang.Throwable) r1, (java.lang.String) r4, (java.lang.String) r5)     // Catch:{ all -> 0x0087 }
            if (r2 == 0) goto L_0x0052
            r2.close()     // Catch:{ Throwable -> 0x0071 }
        L_0x0052:
            if (r3 == 0) goto L_0x0015
            r3.close()     // Catch:{ Throwable -> 0x0058 }
            goto L_0x0015
        L_0x0058:
            r1 = move-exception
            goto L_0x0028
        L_0x005a:
            r0 = move-exception
            r3 = r2
        L_0x005c:
            if (r2 == 0) goto L_0x0061
            r2.close()     // Catch:{ Throwable -> 0x0067 }
        L_0x0061:
            if (r3 == 0) goto L_0x0066
            r3.close()     // Catch:{ Throwable -> 0x006c }
        L_0x0066:
            throw r0
        L_0x0067:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0061
        L_0x006c:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0066
        L_0x0071:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0052
        L_0x0076:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0010
        L_0x007b:
            r1 = move-exception
            goto L_0x0028
        L_0x007d:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0021
        L_0x0082:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x003a
        L_0x0087:
            r0 = move-exception
            goto L_0x005c
        L_0x0089:
            r1 = move-exception
            goto L_0x0044
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bh.a(com.loc.as, java.lang.String):byte[]");
    }
}
