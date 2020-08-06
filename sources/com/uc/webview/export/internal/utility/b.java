package com.uc.webview.export.internal.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.util.DisplayMetrics;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/* compiled from: ProGuard */
public final class b {
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x03ec, code lost:
        com.uc.webview.export.internal.utility.Log.d("SignatureVerifier", "Verify: total costs:" + (java.lang.System.currentTimeMillis() - r6) + "ms");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x040f, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x03a1, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x03a2, code lost:
        if (r20 != null) goto L_0x03a4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:?, code lost:
        r20.onReceiveValue(new java.lang.Object[]{8, java.lang.Integer.valueOf(r2.getClass().getName().hashCode())});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x03c8, code lost:
        com.uc.webview.export.internal.utility.Log.d("SignatureVerifier", "Verify: total costs:" + (java.lang.System.currentTimeMillis() - r6) + "ms");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x03eb, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x03eb A[ExcHandler: all (r2v3 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:4:0x002b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(java.lang.String r16, android.content.Context r17, android.content.Context r18, java.lang.String r19, android.webkit.ValueCallback<java.lang.Object[]> r20) {
        /*
            java.io.File r2 = new java.io.File
            r0 = r16
            r2.<init>(r0)
            boolean r2 = r2.exists()
            if (r2 != 0) goto L_0x000f
            r2 = 0
        L_0x000e:
            return r2
        L_0x000f:
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "verify: file = "
            r3.<init>(r4)
            r0 = r16
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
            long r6 = java.lang.System.currentTimeMillis()
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            r0 = r17
            r1 = r16
            android.content.pm.Signature[] r3 = com.uc.webview.export.internal.utility.b.a.b(r0, r1)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            if (r3 == 0) goto L_0x003c
            int r2 = r3.length     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            if (r2 > 0) goto L_0x00a2
        L_0x003c:
            r2 = 1
        L_0x003d:
            if (r2 == 0) goto L_0x00a4
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r8 = "verify: failed: Signatures of archive is empty. Costs "
            r3.<init>(r8)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r4 = r8 - r4
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r4 = "ms."
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            com.uc.webview.export.internal.utility.Log.d(r2, r3)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            if (r20 == 0) goto L_0x007d
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x041b, all -> 0x03eb }
            r3 = 0
            r4 = 8
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x041b, all -> 0x03eb }
            r2[r3] = r4     // Catch:{ Throwable -> 0x041b, all -> 0x03eb }
            r3 = 1
            r4 = 1
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x041b, all -> 0x03eb }
            r2[r3] = r4     // Catch:{ Throwable -> 0x041b, all -> 0x03eb }
            r0 = r20
            r0.onReceiveValue(r2)     // Catch:{ Throwable -> 0x041b, all -> 0x03eb }
        L_0x007d:
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Verify: total costs:"
            r3.<init>(r4)
            long r4 = java.lang.System.currentTimeMillis()
            long r4 = r4 - r6
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "ms"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
            r2 = 0
            goto L_0x000e
        L_0x00a2:
            r2 = 0
            goto L_0x003d
        L_0x00a4:
            java.security.PublicKey[] r8 = com.uc.webview.export.internal.utility.b.a.a(r3)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            if (r8 == 0) goto L_0x00ad
            int r2 = r8.length     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            if (r2 > 0) goto L_0x0113
        L_0x00ad:
            r2 = 1
        L_0x00ae:
            if (r2 == 0) goto L_0x0115
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r8 = "verify: failed: PublicKeys of archive is empty. Costs "
            r3.<init>(r8)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r4 = r8 - r4
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r4 = "ms."
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            com.uc.webview.export.internal.utility.Log.d(r2, r3)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            if (r20 == 0) goto L_0x00ee
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0418, all -> 0x03eb }
            r3 = 0
            r4 = 8
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x0418, all -> 0x03eb }
            r2[r3] = r4     // Catch:{ Throwable -> 0x0418, all -> 0x03eb }
            r3 = 1
            r4 = 2
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x0418, all -> 0x03eb }
            r2[r3] = r4     // Catch:{ Throwable -> 0x0418, all -> 0x03eb }
            r0 = r20
            r0.onReceiveValue(r2)     // Catch:{ Throwable -> 0x0418, all -> 0x03eb }
        L_0x00ee:
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Verify: total costs:"
            r3.<init>(r4)
            long r4 = java.lang.System.currentTimeMillis()
            long r4 = r4 - r6
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "ms"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
            r2 = 0
            goto L_0x000e
        L_0x0113:
            r2 = 0
            goto L_0x00ae
        L_0x0115:
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r9 = "verify: step 0: get PublicKeys of archive ok. Costs "
            r3.<init>(r9)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r4 = r10 - r4
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r4 = "ms."
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            com.uc.webview.export.internal.utility.Log.d(r2, r3)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            if (r18 == 0) goto L_0x01b9
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r4 = r18.getPackageName()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            r0 = r17
            android.content.pm.Signature[] r4 = com.uc.webview.export.internal.utility.b.a.a(r0, r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            boolean r4 = a(r8, r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            if (r4 == 0) goto L_0x0196
            java.lang.String r4 = "SignatureVerifier"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r8 = "verify: step 1: get Signatures of app from current context and verify ok. Costs "
            r5.<init>(r8)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r2 = r8 - r2
            java.lang.StringBuilder r2 = r5.append(r2)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r3 = "ms."
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            com.uc.webview.export.internal.utility.Log.d(r4, r2)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Verify: total costs:"
            r3.<init>(r4)
            long r4 = java.lang.System.currentTimeMillis()
            long r4 = r4 - r6
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "ms"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
            r2 = 1
            goto L_0x000e
        L_0x0196:
            java.lang.String r4 = "SignatureVerifier"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r9 = "verify: step 1: get Signatures of app from current context and verify failed. Costs "
            r5.<init>(r9)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r2 = r10 - r2
            java.lang.StringBuilder r2 = r5.append(r2)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r3 = "ms."
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            com.uc.webview.export.internal.utility.Log.d(r4, r2)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
        L_0x01b9:
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r2 = "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"
            r3 = 2
            byte[] r2 = android.util.Base64.decode(r2, r3)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.io.File r5 = new java.io.File     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.io.File r3 = r17.getCacheDir()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            r4.<init>()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.String r9 = "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"
            java.lang.String r9 = com.uc.webview.export.cyclone.UCCyclone.getSourceHash(r9)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.String r9 = "_2336_"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            int r9 = r2.length     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.String r9 = java.lang.String.valueOf(r9)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            r5.<init>(r3, r4)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            boolean r3 = r5.exists()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            if (r3 == 0) goto L_0x0208
            boolean r3 = r5.isFile()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            if (r3 == 0) goto L_0x0208
            long r12 = r5.length()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            int r3 = r2.length     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            long r14 = (long) r3     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            int r3 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r3 == 0) goto L_0x0244
        L_0x0208:
            java.io.File r9 = new java.io.File     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.io.File r3 = r17.getCacheDir()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.String r4 = java.lang.String.valueOf(r12)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            r9.<init>(r3, r4)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            boolean r3 = r9.exists()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            if (r3 == 0) goto L_0x0222
            r9.delete()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
        L_0x0222:
            r4 = 0
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ all -> 0x029b }
            r3.<init>(r9)     // Catch:{ all -> 0x029b }
            r3.write(r2)     // Catch:{ all -> 0x0415 }
            com.uc.webview.export.cyclone.UCCyclone.close(r3)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            boolean r3 = r5.exists()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            if (r3 == 0) goto L_0x0237
            r5.delete()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
        L_0x0237:
            long r12 = r9.length()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            int r2 = r2.length     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            long r2 = (long) r2     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            int r2 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r2 != 0) goto L_0x0244
            r9.renameTo(r5)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
        L_0x0244:
            java.lang.String r2 = r5.getAbsolutePath()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            r0 = r17
            android.content.pm.Signature[] r2 = com.uc.webview.export.internal.utility.b.a.b(r0, r2)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            boolean r2 = a(r8, r2)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            if (r2 == 0) goto L_0x02a2
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.String r4 = "verify: step 2: get Signatures of app from hardcode app and verify ok. Costs "
            r3.<init>(r4)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            long r4 = r4 - r10
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.String r4 = "ms."
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            com.uc.webview.export.internal.utility.Log.d(r2, r3)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Verify: total costs:"
            r3.<init>(r4)
            long r4 = java.lang.System.currentTimeMillis()
            long r4 = r4 - r6
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "ms"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
            r2 = 1
            goto L_0x000e
        L_0x029b:
            r2 = move-exception
            r3 = r4
        L_0x029d:
            com.uc.webview.export.cyclone.UCCyclone.close(r3)     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
            throw r2     // Catch:{ Throwable -> 0x02a1, all -> 0x03eb }
        L_0x02a1:
            r2 = move-exception
        L_0x02a2:
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r4 = "verify: step 2: get Signatures of app from hardcode app and verify failed. Costs "
            r3.<init>(r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r4 = r4 - r10
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r4 = "ms."
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            com.uc.webview.export.internal.utility.Log.d(r2, r3)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            int r2 = r19.length()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            if (r2 <= 0) goto L_0x0361
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            r0 = r17
            r1 = r19
            android.content.pm.Signature[] r4 = com.uc.webview.export.internal.utility.b.a.a(r0, r1)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            boolean r4 = a(r8, r4)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            if (r4 == 0) goto L_0x0331
            java.lang.String r4 = "SignatureVerifier"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r8 = "verify: step 3: get Signatures of app from "
            r5.<init>(r8)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            r0 = r19
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r8 = " and verify ok. Costs "
            java.lang.StringBuilder r5 = r5.append(r8)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r2 = r8 - r2
            java.lang.StringBuilder r2 = r5.append(r2)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r3 = "ms."
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            com.uc.webview.export.internal.utility.Log.d(r4, r2)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Verify: total costs:"
            r3.<init>(r4)
            long r4 = java.lang.System.currentTimeMillis()
            long r4 = r4 - r6
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "ms"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
            r2 = 1
            goto L_0x000e
        L_0x0331:
            java.lang.String r4 = "SignatureVerifier"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r8 = "verify: step 3: get Signatures of app from "
            r5.<init>(r8)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            r0 = r19
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r8 = " and verify failed. Costs "
            java.lang.StringBuilder r5 = r5.append(r8)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            long r2 = r8 - r2
            java.lang.StringBuilder r2 = r5.append(r2)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r3 = "ms."
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
            com.uc.webview.export.internal.utility.Log.d(r4, r2)     // Catch:{ Throwable -> 0x03a1, all -> 0x03eb }
        L_0x0361:
            if (r20 == 0) goto L_0x037c
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0412, all -> 0x03eb }
            r3 = 0
            r4 = 8
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x0412, all -> 0x03eb }
            r2[r3] = r4     // Catch:{ Throwable -> 0x0412, all -> 0x03eb }
            r3 = 1
            r4 = 3
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x0412, all -> 0x03eb }
            r2[r3] = r4     // Catch:{ Throwable -> 0x0412, all -> 0x03eb }
            r0 = r20
            r0.onReceiveValue(r2)     // Catch:{ Throwable -> 0x0412, all -> 0x03eb }
        L_0x037c:
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Verify: total costs:"
            r3.<init>(r4)
            long r4 = java.lang.System.currentTimeMillis()
            long r4 = r4 - r6
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "ms"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
        L_0x039e:
            r2 = 0
            goto L_0x000e
        L_0x03a1:
            r2 = move-exception
            if (r20 == 0) goto L_0x03c8
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0410, all -> 0x03eb }
            r4 = 0
            r5 = 8
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x0410, all -> 0x03eb }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0410, all -> 0x03eb }
            r4 = 1
            java.lang.Class r2 = r2.getClass()     // Catch:{ Throwable -> 0x0410, all -> 0x03eb }
            java.lang.String r2 = r2.getName()     // Catch:{ Throwable -> 0x0410, all -> 0x03eb }
            int r2 = r2.hashCode()     // Catch:{ Throwable -> 0x0410, all -> 0x03eb }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x0410, all -> 0x03eb }
            r3[r4] = r2     // Catch:{ Throwable -> 0x0410, all -> 0x03eb }
            r0 = r20
            r0.onReceiveValue(r3)     // Catch:{ Throwable -> 0x0410, all -> 0x03eb }
        L_0x03c8:
            java.lang.String r2 = "SignatureVerifier"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Verify: total costs:"
            r3.<init>(r4)
            long r4 = java.lang.System.currentTimeMillis()
            long r4 = r4 - r6
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "ms"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
            goto L_0x039e
        L_0x03eb:
            r2 = move-exception
            java.lang.String r3 = "SignatureVerifier"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "Verify: total costs:"
            r4.<init>(r5)
            long r8 = java.lang.System.currentTimeMillis()
            long r6 = r8 - r6
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r5 = "ms"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.uc.webview.export.internal.utility.Log.d(r3, r4)
            throw r2
        L_0x0410:
            r2 = move-exception
            goto L_0x03c8
        L_0x0412:
            r2 = move-exception
            goto L_0x037c
        L_0x0415:
            r2 = move-exception
            goto L_0x029d
        L_0x0418:
            r2 = move-exception
            goto L_0x00ee
        L_0x041b:
            r2 = move-exception
            goto L_0x007d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.b.a(java.lang.String, android.content.Context, android.content.Context, java.lang.String, android.webkit.ValueCallback):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x002a A[Catch:{ Throwable -> 0x0034 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0069  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final boolean a(java.security.PublicKey[] r7, android.content.pm.Signature[] r8) {
        /*
            r1 = 1
            r0 = 0
            java.security.PublicKey[] r3 = com.uc.webview.export.internal.utility.b.a.a(r8)     // Catch:{ Throwable -> 0x0034 }
            if (r3 == 0) goto L_0x000b
            int r2 = r3.length     // Catch:{ Throwable -> 0x0034 }
            if (r2 > 0) goto L_0x0018
        L_0x000b:
            r2 = r1
        L_0x000c:
            if (r2 == 0) goto L_0x001a
            java.lang.String r1 = "SignatureVerifier"
            java.lang.String r2 = "公钥校验错误：Implement.isEmpty(appPublicKeys) == true"
            com.uc.webview.export.internal.utility.Log.d(r1, r2)     // Catch:{ Throwable -> 0x0034 }
        L_0x0017:
            return r0
        L_0x0018:
            r2 = r0
            goto L_0x000c
        L_0x001a:
            if (r3 == 0) goto L_0x001e
            if (r7 != 0) goto L_0x003f
        L_0x001e:
            java.lang.String r2 = "SignatureVerifier"
            java.lang.String r3 = "Sign.equals: s1 == null || s2 == null"
            com.uc.webview.export.internal.utility.Log.e(r2, r3)     // Catch:{ Throwable -> 0x0034 }
        L_0x0027:
            r2 = r0
        L_0x0028:
            if (r2 != 0) goto L_0x0069
            java.lang.String r1 = "SignatureVerifier"
            java.lang.String r2 = "公钥校验错误：Implement.equals(appPublicKeys, archiveKeys) == false"
            com.uc.webview.export.internal.utility.Log.d(r1, r2)     // Catch:{ Throwable -> 0x0034 }
            goto L_0x0017
        L_0x0034:
            r1 = move-exception
            java.lang.String r1 = "SignatureVerifier"
            java.lang.String r2 = "公钥校验错误：Implement.isEmpty(appPublicKeys) == true"
            com.uc.webview.export.internal.utility.Log.d(r1, r2)
            goto L_0x0017
        L_0x003f:
            java.util.HashSet r4 = new java.util.HashSet     // Catch:{ Throwable -> 0x0034 }
            r4.<init>()     // Catch:{ Throwable -> 0x0034 }
            int r5 = r3.length     // Catch:{ Throwable -> 0x0034 }
            r2 = r0
        L_0x0046:
            if (r2 >= r5) goto L_0x0050
            r6 = r3[r2]     // Catch:{ Throwable -> 0x0034 }
            r4.add(r6)     // Catch:{ Throwable -> 0x0034 }
            int r2 = r2 + 1
            goto L_0x0046
        L_0x0050:
            java.util.HashSet r3 = new java.util.HashSet     // Catch:{ Throwable -> 0x0034 }
            r3.<init>()     // Catch:{ Throwable -> 0x0034 }
            int r5 = r7.length     // Catch:{ Throwable -> 0x0034 }
            r2 = r0
        L_0x0057:
            if (r2 >= r5) goto L_0x0061
            r6 = r7[r2]     // Catch:{ Throwable -> 0x0034 }
            r3.add(r6)     // Catch:{ Throwable -> 0x0034 }
            int r2 = r2 + 1
            goto L_0x0057
        L_0x0061:
            boolean r2 = r4.equals(r3)     // Catch:{ Throwable -> 0x0034 }
            if (r2 == 0) goto L_0x0027
            r2 = r1
            goto L_0x0028
        L_0x0069:
            r0 = r1
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.b.a(java.security.PublicKey[], android.content.pm.Signature[]):boolean");
    }

    /* compiled from: ProGuard */
    private static class a {
        public static Signature[] a(Context context, String str) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 64);
                if (packageInfo == null) {
                    return null;
                }
                return packageInfo.signatures;
            } catch (Exception e) {
                Log.e("SignatureVerifier", e.getMessage());
                return null;
            }
        }

        public static Signature[] b(Context context, String str) {
            Throwable th;
            Signature[] signatureArr;
            Object newInstance;
            Object invoke;
            Log.d("SignatureVerifier", String.format("getUninstalledAPKSignature(): archiveApkFilePath = %1s", new Object[]{str}));
            try {
                long currentTimeMillis = System.currentTimeMillis();
                Class<?> cls = Class.forName("android.content.pm.PackageParser");
                if (Build.VERSION.SDK_INT >= 21) {
                    newInstance = cls.getConstructor(new Class[0]).newInstance(new Object[0]);
                } else {
                    newInstance = cls.getConstructor(new Class[]{String.class}).newInstance(new Object[]{""});
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    invoke = cls.getMethod("parsePackage", new Class[]{File.class, Integer.TYPE}).invoke(newInstance, new Object[]{new File(str), 0});
                } else {
                    invoke = cls.getMethod("parsePackage", new Class[]{File.class, String.class, DisplayMetrics.class, Integer.TYPE}).invoke(newInstance, new Object[]{new File(str), null, context.getResources().getDisplayMetrics(), 0});
                }
                cls.getMethod("collectCertificates", new Class[]{Class.forName("android.content.pm.PackageParser$Package"), Integer.TYPE}).invoke(newInstance, new Object[]{invoke, 64});
                signatureArr = (Signature[]) invoke.getClass().getField("mSignatures").get(invoke);
                if (signatureArr == null || signatureArr.length <= 0) {
                    Log.e("SignatureVerifier", "摘要校验失败");
                    signatureArr = null;
                } else {
                    try {
                        Log.d("SignatureVerifier", "摘要校验成功!");
                    } catch (Throwable th2) {
                        th = th2;
                        Log.e("SignatureVerifier", th.getMessage());
                        return signatureArr;
                    }
                }
                Log.i("SignatureVerifier", "耗时：" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
            } catch (Throwable th3) {
                Throwable th4 = th3;
                signatureArr = null;
                th = th4;
                Log.e("SignatureVerifier", th.getMessage());
                return signatureArr;
            }
            return signatureArr;
        }

        public static PublicKey[] a(Signature[] signatureArr) {
            if (signatureArr != null) {
                try {
                    if (signatureArr.length != 0) {
                        int length = signatureArr.length;
                        PublicKey[] publicKeyArr = new PublicKey[length];
                        for (int i = 0; i < length; i++) {
                            publicKeyArr[i] = ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(signatureArr[i].toByteArray()))).getPublicKey();
                        }
                        return publicKeyArr;
                    }
                } catch (Exception e) {
                    Log.e("SignatureVerifier", "获取公钥异常：\n" + e.getMessage());
                    return null;
                }
            }
            return null;
        }
    }
}
