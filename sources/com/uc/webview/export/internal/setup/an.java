package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.util.Pair;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.utility.c;
import java.io.File;
import java.net.URL;
import java.util.concurrent.Callable;

/* compiled from: ProGuard */
public class an extends o {
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00b2, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00b3, code lost:
        r24 = r3;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00b2 A[ExcHandler: UCSetupException (r3v83 'e' com.uc.webview.export.internal.setup.UCSetupException A[CUSTOM_DECLARE]), Splitter:B:97:0x02c5] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r30 = this;
            r0 = r30
            java.util.HashMap r2 = r0.mOptions
            java.lang.String r3 = "CONTEXT"
            java.lang.Object r2 = r2.get(r3)
            android.content.Context r2 = (android.content.Context) r2
            boolean r25 = com.uc.webview.export.internal.d.p
            r5 = 0
            r0 = r30
            java.util.HashMap r3 = r0.mOptions
            java.lang.String r4 = "scanUcmPrefix"
            java.lang.Object r3 = r3.get(r4)
            java.lang.String r3 = (java.lang.String) r3
            if (r3 == 0) goto L_0x00b6
            r0 = r25
            java.util.List r3 = com.uc.webview.export.internal.setup.UCMPackageInfo.a((android.content.Context) r2, (java.lang.String) r3, (boolean) r0)
            r4 = r3
        L_0x0026:
            int r3 = r4.size()
            if (r3 <= 0) goto L_0x033a
            r0 = r30
            java.util.HashMap r3 = r0.mOptions
            java.lang.String r6 = "VERIFY_POLICY"
            java.lang.Object r11 = r3.get(r6)
            java.lang.Integer r11 = (java.lang.Integer) r11
            r0 = r30
            java.util.HashMap r3 = r0.mOptions
            java.lang.String r6 = "chkDecFinish"
            java.lang.Object r3 = r3.get(r6)
            java.lang.Boolean r3 = (java.lang.Boolean) r3
            boolean r3 = com.uc.webview.export.internal.utility.c.a((java.lang.Boolean) r3)
            if (r3 != 0) goto L_0x00c1
            r3 = 1
            r22 = r3
        L_0x004f:
            r0 = r30
            java.util.HashMap r3 = r0.mOptions
            java.lang.String r6 = "sdk_setup"
            java.lang.Object r3 = r3.get(r6)
            java.lang.Boolean r3 = (java.lang.Boolean) r3
            boolean r3 = com.uc.webview.export.internal.utility.c.a((java.lang.Boolean) r3)
            if (r3 != 0) goto L_0x00c5
            r3 = 1
            r23 = r3
        L_0x0065:
            r0 = r30
            java.util.HashMap r3 = r0.mOptions
            java.lang.String r6 = "core_ver_excludes"
            java.lang.Object r3 = r3.get(r6)
            r8 = r3
            java.lang.String r8 = (java.lang.String) r8
            java.lang.Class<com.uc.webview.export.internal.setup.an> r3 = com.uc.webview.export.internal.setup.an.class
            java.lang.ClassLoader r7 = r3.getClassLoader()
            java.util.Iterator r26 = r4.iterator()
            r24 = r5
        L_0x007f:
            boolean r3 = r26.hasNext()
            if (r3 == 0) goto L_0x0308
            java.lang.Object r3 = r26.next()
            r9 = r3
            com.uc.webview.export.internal.setup.UCMPackageInfo r9 = (com.uc.webview.export.internal.setup.UCMPackageInfo) r9
            if (r22 == 0) goto L_0x00c9
            java.io.File r3 = new java.io.File     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r4 = r9.dataDir     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r3.<init>(r4)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            boolean r3 = com.uc.webview.export.cyclone.UCCyclone.isDecompressFinished(r3)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r3 != 0) goto L_0x00c9
            com.uc.webview.export.internal.setup.UCSetupException r3 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r4 = 2007(0x7d7, float:2.812E-42)
            java.lang.String r5 = "Package [%s] decompress not finished."
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r10 = 0
            java.lang.String r9 = r9.dataDir     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r6[r10] = r9     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r5 = java.lang.String.format(r5, r6)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r3.<init>((int) r4, (java.lang.String) r5)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            throw r3     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
        L_0x00b2:
            r3 = move-exception
            r24 = r3
            goto L_0x007f
        L_0x00b6:
            r0 = r30
            java.util.HashMap r3 = r0.mOptions
            java.util.List r3 = com.uc.webview.export.internal.setup.UCMPackageInfo.a((android.content.Context) r2, (java.util.HashMap<java.lang.String, java.lang.Object>) r3)
            r4 = r3
            goto L_0x0026
        L_0x00c1:
            r3 = 0
            r22 = r3
            goto L_0x004f
        L_0x00c5:
            r3 = 0
            r23 = r3
            goto L_0x0065
        L_0x00c9:
            r0 = r30
            r0.a(r9)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r0 = r30
            boolean r12 = r0.a(r2, r9)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            android.util.Pair<java.lang.String, java.lang.String> r3 = r9.coreImplModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r3 = r3.first     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            android.util.Pair<java.lang.String, java.lang.String> r4 = r9.coreImplModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r4 = r4.second     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.io.File r3 = com.uc.webview.export.cyclone.UCCyclone.optimizedFileFor(r3, r4)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            boolean r3 = r3.exists()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r3 != 0) goto L_0x012a
            r3 = 1
            r10 = r3
        L_0x00ec:
            if (r12 == 0) goto L_0x013c
            if (r23 != 0) goto L_0x012d
            r4 = 1
        L_0x00f1:
            java.util.Map<java.lang.String, java.lang.String> r3 = com.uc.webview.export.internal.d.w     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r3 != 0) goto L_0x012f
            r3 = 0
            r5 = r3
        L_0x00f7:
            boolean r3 = com.uc.webview.export.internal.utility.c.a((java.lang.String) r5)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r3 != 0) goto L_0x0337
            boolean r3 = java.lang.Boolean.parseBoolean(r5)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
        L_0x0101:
            if (r3 == 0) goto L_0x013c
            com.uc.webview.export.internal.setup.UCSetupException r3 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r4 = 4006(0xfa6, float:5.614E-42)
            java.lang.String r6 = "UCM [%s] is excluded by param skip_old_extra_kernel value [%s]."
            r10 = 2
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r12 = 0
            java.lang.String r9 = r9.dataDir     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r10[r12] = r9     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r9 = 1
            r10[r9] = r5     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r5 = java.lang.String.format(r6, r10)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r3.<init>((int) r4, (java.lang.String) r5)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            throw r3     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
        L_0x011d:
            r3 = move-exception
            r4 = r3
            com.uc.webview.export.internal.setup.UCSetupException r3 = new com.uc.webview.export.internal.setup.UCSetupException
            r5 = 3003(0xbbb, float:4.208E-42)
            r3.<init>((int) r5, (java.lang.Throwable) r4)
            r24 = r3
            goto L_0x007f
        L_0x012a:
            r3 = 0
            r10 = r3
            goto L_0x00ec
        L_0x012d:
            r4 = 0
            goto L_0x00f1
        L_0x012f:
            java.util.Map<java.lang.String, java.lang.String> r3 = com.uc.webview.export.internal.d.w     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r5 = "skip_old_extra_kernel"
            java.lang.Object r3 = r3.get(r5)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r5 = r3
            goto L_0x00f7
        L_0x013c:
            r14 = 0
            r16 = 0
            if (r11 == 0) goto L_0x0163
            int r3 = r11.intValue()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r3 = r3 & 1
            if (r3 == 0) goto L_0x0163
            android.util.Pair<java.lang.String, java.lang.String> r3 = r9.sdkShellModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r3 = r3.first     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            com.uc.webview.export.cyclone.UCElapseTime r3 = a(r2, r11, r3)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r4 = 0
            long r14 = r3.getMilisCpu()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r16 = r4 + r14
            r4 = 0
            long r14 = r3.getMilis()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r14 = r14 + r4
        L_0x0163:
            com.uc.webview.export.cyclone.UCElapseTime r13 = new com.uc.webview.export.cyclone.UCElapseTime     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r13.<init>()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r3 = 0
            android.util.Pair<java.lang.String, java.lang.String> r4 = r9.sdkShellModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r4 = r4.first     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            android.util.Pair<java.lang.String, java.lang.String> r5 = r9.sdkShellModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r5 = r5.second     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r6 = r9.soDirPath     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            dalvik.system.DexClassLoader r4 = com.uc.webview.export.cyclone.UCDex.createDexClassLoader(r2, r3, r4, r5, r6, r7)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r18 = 0
            long r20 = r13.getMilisCpu()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r18 = r18 + r20
            r20 = 0
            long r28 = r13.getMilis()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r28 = r28 + r20
            r0 = r30
            r0.mShellCL = r4     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r0 = r30
            r0.mUCM = r9     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r23 == 0) goto L_0x0211
            if (r25 != 0) goto L_0x01e9
            java.lang.String r3 = "com.uc.webview.browser.shell.Build"
            r5 = 0
            java.lang.Class r3 = java.lang.Class.forName(r3, r5, r4)     // Catch:{ Exception -> 0x01e0 }
            java.lang.String r5 = "TYPE"
            java.lang.reflect.Field r3 = r3.getField(r5)     // Catch:{ Exception -> 0x01e0 }
            r5 = 0
            java.lang.Object r3 = r3.get(r5)     // Catch:{ Exception -> 0x01e0 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x01e0 }
            boolean r5 = r9.isSpecified     // Catch:{ Exception -> 0x01e0 }
            if (r5 != 0) goto L_0x01e9
            java.lang.String r5 = r3.toLowerCase()     // Catch:{ Exception -> 0x01e0 }
            java.lang.String r6 = "ucrelease"
            boolean r5 = r5.startsWith(r6)     // Catch:{ Exception -> 0x01e0 }
            if (r5 != 0) goto L_0x01e9
            java.lang.String r5 = r3.toLowerCase()     // Catch:{ Exception -> 0x01e0 }
            java.lang.String r6 = "ucpatch"
            boolean r5 = r5.startsWith(r6)     // Catch:{ Exception -> 0x01e0 }
            if (r5 != 0) goto L_0x01e9
            com.uc.webview.export.internal.setup.UCSetupException r4 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ Exception -> 0x01e0 }
            r5 = 4011(0xfab, float:5.62E-42)
            java.lang.String r6 = "ucrelease or ucpatch is expected but get [%s] to shared UCM."
            r9 = 1
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x01e0 }
            r10 = 0
            r9[r10] = r3     // Catch:{ Exception -> 0x01e0 }
            java.lang.String r3 = java.lang.String.format(r6, r9)     // Catch:{ Exception -> 0x01e0 }
            r4.<init>((int) r5, (java.lang.String) r3)     // Catch:{ Exception -> 0x01e0 }
            throw r4     // Catch:{ Exception -> 0x01e0 }
        L_0x01e0:
            r3 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r4 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r5 = 4012(0xfac, float:5.622E-42)
            r4.<init>((int) r5, (java.lang.Throwable) r3)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            throw r4     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
        L_0x01e9:
            a(r8, r4)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r0 = r30
            r0.a(r9, r2, r4)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r11 == 0) goto L_0x0211
            int r3 = r11.intValue()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r3 = r3 & 2
            if (r3 == 0) goto L_0x0211
            android.util.Pair<java.lang.String, java.lang.String> r3 = r9.browserIFModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r3 = r3.first     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            com.uc.webview.export.cyclone.UCElapseTime r3 = a(r2, r11, r3)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r20 = r3.getMilisCpu()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r16 = r16 + r20
            long r20 = r3.getMilis()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r14 = r14 + r20
        L_0x0211:
            if (r11 == 0) goto L_0x022e
            int r3 = r11.intValue()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r3 = r3 & 8
            if (r3 == 0) goto L_0x022e
            com.uc.webview.export.cyclone.UCElapseTime r3 = new com.uc.webview.export.cyclone.UCElapseTime     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r3.<init>()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            b(r9, r2, r4)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r4 = r3.getMilisCpu()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r16 = r16 + r4
            long r4 = r3.getMilis()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r14 = r14 + r4
        L_0x022e:
            if (r11 == 0) goto L_0x024d
            int r3 = r11.intValue()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r3 = r3 & 4
            if (r3 == 0) goto L_0x024d
            android.util.Pair<java.lang.String, java.lang.String> r3 = r9.coreImplModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r3 = r3.first     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            com.uc.webview.export.cyclone.UCElapseTime r3 = a(r2, r11, r3)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r4 = r3.getMilisCpu()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r16 = r16 + r4
            long r4 = r3.getMilis()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r14 = r14 + r4
        L_0x024d:
            com.uc.webview.export.cyclone.UCElapseTime r13 = new com.uc.webview.export.cyclone.UCElapseTime     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r13.<init>()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r3 = 0
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r5.<init>()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r23 == 0) goto L_0x0323
            android.util.Pair<java.lang.String, java.lang.String> r4 = r9.browserIFModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r4 = r4.first     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r4 == 0) goto L_0x0323
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r6.<init>()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            android.util.Pair<java.lang.String, java.lang.String> r4 = r9.browserIFModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r4 = r4.first     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.StringBuilder r4 = r6.append(r4)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r6 = ":"
            java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r4 = r4.toString()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
        L_0x027a:
            java.lang.StringBuilder r5 = r5.append(r4)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            android.util.Pair<java.lang.String, java.lang.String> r4 = r9.coreImplModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r4 = r4.first     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.StringBuilder r4 = r5.append(r4)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r4 = r4.toString()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            android.util.Pair<java.lang.String, java.lang.String> r5 = r9.coreImplModule     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.Object r5 = r5.second     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r6 = r9.soDirPath     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            dalvik.system.DexClassLoader r3 = com.uc.webview.export.cyclone.UCDex.createDexClassLoader(r2, r3, r4, r5, r6, r7)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r4 = r13.getMilisCpu()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r20 = r18 + r4
            long r4 = r13.getMilis()     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            long r18 = r28 + r4
            r0 = r30
            r0.mCL = r3     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r0 = r30
            r0.a = r12     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r10 != 0) goto L_0x02b4
            r4 = 1000(0x3e8, double:4.94E-321)
            int r3 = (r18 > r4 ? 1 : (r18 == r4 ? 0 : -1))
            if (r3 < 0) goto L_0x0328
        L_0x02b4:
            r3 = 1
        L_0x02b5:
            r0 = r30
            r0.b = r3     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r3 = "sdk_vrf"
            r0 = r30
            boolean r12 = r0.b     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r23 == 0) goto L_0x032a
            java.lang.String r13 = "sdk"
        L_0x02c5:
            android.util.Pair r4 = new android.util.Pair     // Catch:{ Throwable -> 0x0335, UCSetupException -> 0x00b2 }
            com.uc.webview.export.internal.setup.p r9 = new com.uc.webview.export.internal.setup.p     // Catch:{ Throwable -> 0x0335, UCSetupException -> 0x00b2 }
            r10 = r30
            r9.<init>(r10, r11, r12, r13, r14, r16)     // Catch:{ Throwable -> 0x0335, UCSetupException -> 0x00b2 }
            r4.<init>(r3, r9)     // Catch:{ Throwable -> 0x0335, UCSetupException -> 0x00b2 }
            r0 = r30
            r0.callbackStat(r4)     // Catch:{ Throwable -> 0x0335, UCSetupException -> 0x00b2 }
        L_0x02d6:
            java.lang.String r3 = "sdk_opt"
            r0 = r30
            boolean r0 = r0.b     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r16 = r0
            r4 = 0
            java.lang.Integer r15 = java.lang.Integer.valueOf(r4)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            if (r23 == 0) goto L_0x032e
            java.lang.String r17 = "sdk"
        L_0x02e9:
            android.util.Pair r4 = new android.util.Pair     // Catch:{ Throwable -> 0x0333, UCSetupException -> 0x00b2 }
            com.uc.webview.export.internal.setup.p r13 = new com.uc.webview.export.internal.setup.p     // Catch:{ Throwable -> 0x0333, UCSetupException -> 0x00b2 }
            r14 = r30
            r13.<init>(r14, r15, r16, r17, r18, r20)     // Catch:{ Throwable -> 0x0333, UCSetupException -> 0x00b2 }
            r4.<init>(r3, r13)     // Catch:{ Throwable -> 0x0333, UCSetupException -> 0x00b2 }
            r0 = r30
            r0.callbackStat(r4)     // Catch:{ Throwable -> 0x0333, UCSetupException -> 0x00b2 }
        L_0x02fa:
            android.util.Pair r3 = new android.util.Pair     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            java.lang.String r4 = "sdk_stp_s"
            r5 = 0
            r3.<init>(r4, r5)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
            r0 = r30
            r0.callbackStat(r3)     // Catch:{ UCSetupException -> 0x00b2, Throwable -> 0x011d }
        L_0x0308:
            r0 = r30
            java.lang.ClassLoader r2 = r0.mCL
            if (r2 == 0) goto L_0x0314
            r0 = r30
            com.uc.webview.export.internal.setup.UCMPackageInfo r2 = r0.mUCM
            if (r2 != 0) goto L_0x0332
        L_0x0314:
            if (r24 != 0) goto L_0x0322
            com.uc.webview.export.internal.setup.UCSetupException r24 = new com.uc.webview.export.internal.setup.UCSetupException
            r2 = 3004(0xbbc, float:4.21E-42)
            java.lang.String r3 = "UCM packages not found."
            r0 = r24
            r0.<init>((int) r2, (java.lang.String) r3)
        L_0x0322:
            throw r24
        L_0x0323:
            java.lang.String r4 = ""
            goto L_0x027a
        L_0x0328:
            r3 = 0
            goto L_0x02b5
        L_0x032a:
            java.lang.String r13 = "ucm"
            goto L_0x02c5
        L_0x032e:
            java.lang.String r17 = "ucm"
            goto L_0x02e9
        L_0x0332:
            return
        L_0x0333:
            r3 = move-exception
            goto L_0x02fa
        L_0x0335:
            r3 = move-exception
            goto L_0x02d6
        L_0x0337:
            r3 = r4
            goto L_0x0101
        L_0x033a:
            r24 = r5
            goto L_0x0308
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.an.run():void");
    }

    private boolean a(Context context, UCMPackageInfo uCMPackageInfo) {
        String str = (String) getOption(UCCore.OPTION_UCM_ZIP_FILE);
        if (!c.a(uCMPackageInfo.dataDir)) {
            if (!c.a(str)) {
                File file = (File) UCMPackageInfo.invoke(10003, context);
                if (uCMPackageInfo.dataDir.startsWith(file.getAbsolutePath())) {
                    File file2 = new File(str);
                    if (!uCMPackageInfo.dataDir.startsWith(new File(new File(file, UCCyclone.getSourceHash(file2.getAbsolutePath())), UCCyclone.getSourceHash(file2.length(), file2.lastModified())).getAbsolutePath())) {
                        return true;
                    }
                }
            } else {
                String str2 = (String) getOption(UCCore.OPTION_UCM_UPD_URL);
                if (!c.a(str2)) {
                    File file3 = (File) UCMPackageInfo.invoke(10002, context);
                    if (uCMPackageInfo.dataDir.startsWith(file3.getAbsolutePath())) {
                        File file4 = new File(file3, UCCyclone.getSourceHash(str2));
                        if (!uCMPackageInfo.dataDir.startsWith(file4.getAbsolutePath())) {
                            return true;
                        }
                        try {
                            if (!((Boolean) ((Callable) getOption(UCCore.OPTION_DOWNLOAD_CHECKER)).call()).booleanValue()) {
                                return false;
                            }
                            Pair<Long, Long> a = c.a(str2, (URL) null);
                            if (!uCMPackageInfo.dataDir.startsWith(new File(file4, UCCyclone.getSourceHash(((Long) a.first).longValue(), ((Long) a.second).longValue())).getAbsolutePath())) {
                                return true;
                            }
                        } catch (Throwable th) {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0054, code lost:
        if (r0 == false) goto L_0x0056;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.uc.webview.export.cyclone.UCElapseTime a(android.content.Context r11, java.lang.Integer r12, java.lang.String r13) {
        /*
            r10 = 3005(0xbbd, float:4.211E-42)
            r9 = 10005(0x2715, float:1.402E-41)
            r7 = 1
            r6 = 0
            com.uc.webview.export.cyclone.UCElapseTime r8 = new com.uc.webview.export.cyclone.UCElapseTime
            r8.<init>()
            int r0 = r12.intValue()
            r1 = 1073741824(0x40000000, float:2.0)
            r0 = r0 & r1
            if (r0 == 0) goto L_0x0056
            java.lang.Object[] r0 = new java.lang.Object[r7]
            r0[r6] = r11
            java.lang.Object r0 = com.uc.webview.export.internal.setup.UCMPackageInfo.invoke(r9, r0)
            java.io.File r0 = (java.io.File) r0
            java.io.File r1 = new java.io.File
            r1.<init>(r13)
            long r2 = r1.length()
            long r4 = r1.lastModified()
            r1 = r13
            java.lang.String r1 = com.uc.webview.export.cyclone.UCCyclone.getDecompressSourceHash(r1, r2, r4, r6)
            java.lang.String r1 = com.uc.webview.export.cyclone.UCCyclone.getSourceHash(r1)
            java.io.File r2 = new java.io.File
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.StringBuilder r3 = r3.append(r1)
            java.lang.String r4 = "_y"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r0, r3)
            boolean r2 = r2.exists()
            if (r2 == 0) goto L_0x00c9
            r0 = r7
        L_0x0054:
            if (r0 != 0) goto L_0x0154
        L_0x0056:
            java.lang.String r0 = "com.UCMobile"
            r1 = 0
            boolean r0 = com.uc.webview.export.internal.utility.b.a(r13, r11, r11, r0, r1)
            if (r0 != 0) goto L_0x00fc
            java.lang.Object[] r0 = new java.lang.Object[r7]
            r0[r6] = r11
            java.lang.Object r0 = com.uc.webview.export.internal.setup.UCMPackageInfo.invoke(r9, r0)
            java.io.File r0 = (java.io.File) r0
            java.io.File r1 = new java.io.File
            r1.<init>(r13)
            long r2 = r1.length()
            long r4 = r1.lastModified()
            r1 = r13
            java.lang.String r1 = com.uc.webview.export.cyclone.UCCyclone.getDecompressSourceHash(r1, r2, r4, r6)
            java.lang.String r1 = com.uc.webview.export.cyclone.UCCyclone.getSourceHash(r1)
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x0157 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0157 }
            r3.<init>()     // Catch:{ Exception -> 0x0157 }
            java.lang.StringBuilder r3 = r3.append(r1)     // Catch:{ Exception -> 0x0157 }
            java.lang.String r4 = "_y"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0157 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0157 }
            r2.<init>(r0, r3)     // Catch:{ Exception -> 0x0157 }
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x0157 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0157 }
            r4.<init>()     // Catch:{ Exception -> 0x0157 }
            java.lang.StringBuilder r1 = r4.append(r1)     // Catch:{ Exception -> 0x0157 }
            java.lang.String r4 = "_n"
            java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Exception -> 0x0157 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0157 }
            r3.<init>(r0, r1)     // Catch:{ Exception -> 0x0157 }
            r2.delete()     // Catch:{ Exception -> 0x0157 }
            r3.createNewFile()     // Catch:{ Exception -> 0x0157 }
        L_0x00b8:
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            java.lang.String r1 = "[%s] verify failed"
            java.lang.Object[] r2 = new java.lang.Object[r7]
            r2[r6] = r13
            java.lang.String r1 = java.lang.String.format(r1, r2)
            r0.<init>((int) r10, (java.lang.String) r1)
            throw r0
        L_0x00c9:
            java.io.File r2 = new java.io.File
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r3 = "_n"
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r1 = r1.toString()
            r2.<init>(r0, r1)
            boolean r0 = r2.exists()
            if (r0 == 0) goto L_0x00f9
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            java.lang.String r1 = "[%s] verifyQuick failed"
            java.lang.Object[] r2 = new java.lang.Object[r7]
            r2[r6] = r13
            java.lang.String r1 = java.lang.String.format(r1, r2)
            r0.<init>((int) r10, (java.lang.String) r1)
            throw r0
        L_0x00f9:
            r0 = r6
            goto L_0x0054
        L_0x00fc:
            java.lang.Object[] r0 = new java.lang.Object[r7]
            r0[r6] = r11
            java.lang.Object r0 = com.uc.webview.export.internal.setup.UCMPackageInfo.invoke(r9, r0)
            java.io.File r0 = (java.io.File) r0
            java.io.File r1 = new java.io.File
            r1.<init>(r13)
            long r2 = r1.length()
            long r4 = r1.lastModified()
            r1 = r13
            java.lang.String r1 = com.uc.webview.export.cyclone.UCCyclone.getDecompressSourceHash(r1, r2, r4, r6)
            java.lang.String r1 = com.uc.webview.export.cyclone.UCCyclone.getSourceHash(r1)
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x0155 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0155 }
            r3.<init>()     // Catch:{ Exception -> 0x0155 }
            java.lang.StringBuilder r3 = r3.append(r1)     // Catch:{ Exception -> 0x0155 }
            java.lang.String r4 = "_y"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0155 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0155 }
            r2.<init>(r0, r3)     // Catch:{ Exception -> 0x0155 }
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x0155 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0155 }
            r4.<init>()     // Catch:{ Exception -> 0x0155 }
            java.lang.StringBuilder r1 = r4.append(r1)     // Catch:{ Exception -> 0x0155 }
            java.lang.String r4 = "_n"
            java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Exception -> 0x0155 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0155 }
            r3.<init>(r0, r1)     // Catch:{ Exception -> 0x0155 }
            r2.createNewFile()     // Catch:{ Exception -> 0x0155 }
            r3.delete()     // Catch:{ Exception -> 0x0155 }
        L_0x0154:
            return r8
        L_0x0155:
            r0 = move-exception
            goto L_0x0154
        L_0x0157:
            r0 = move-exception
            goto L_0x00b8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.an.a(android.content.Context, java.lang.Integer, java.lang.String):com.uc.webview.export.cyclone.UCElapseTime");
    }
}
