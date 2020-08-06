package com.uc.webview.export.internal.setup;

/* compiled from: ProGuard */
public final class v extends UCSubSetupTask<v, v> {
    private String a = null;

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0053 A[Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0076 A[SYNTHETIC, Splitter:B:20:0x0076] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00b4 A[SYNTHETIC, Splitter:B:26:0x00b4] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0106  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r15 = this;
            r5 = 0
            r14 = 3007(0xbbf, float:4.214E-42)
            r6 = 1
            r3 = 0
            java.lang.ClassLoader r0 = r15.mCL
            com.uc.webview.export.internal.d.c = r0
            com.uc.webview.export.internal.c.b.j()
            java.util.HashMap r0 = r15.mOptions
            java.lang.String r1 = "CONTEXT"
            java.lang.Object r0 = r0.get(r1)
            android.content.Context r0 = (android.content.Context) r0
            com.uc.webview.export.internal.setup.UCMPackageInfo r1 = r15.mUCM
            java.lang.String r7 = r1.soDirPath
            java.util.HashMap r1 = r15.mOptions
            java.lang.String r2 = "BREAKPAD_CONFIG"
            java.lang.Object r1 = r1.get(r2)
            com.uc.webview.export.extension.BreakpadConfig r1 = (com.uc.webview.export.extension.BreakpadConfig) r1
            if (r1 == 0) goto L_0x002b
            com.uc.webview.export.internal.b.a(r0, r7, r1)
        L_0x002b:
            java.lang.String r1 = r15.a
            if (r1 != 0) goto L_0x0035
            com.uc.webview.export.internal.setup.UCMPackageInfo r1 = r15.mUCM
            java.lang.String r1 = r1.mainLibrary
            r15.a = r1
        L_0x0035:
            com.uc.webview.export.internal.setup.UCMPackageInfo r1 = r15.mUCM     // Catch:{ Throwable -> 0x00d0 }
            android.util.Pair<java.lang.String, java.lang.String> r1 = r1.coreImplModule     // Catch:{ Throwable -> 0x00d0 }
            java.lang.Object r1 = r1.first     // Catch:{ Throwable -> 0x00d0 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x00d0 }
            com.uc.webview.export.internal.setup.UCMPackageInfo r2 = r15.mUCM     // Catch:{ Throwable -> 0x0114 }
            android.util.Pair<java.lang.String, java.lang.String> r2 = r2.coreImplModule     // Catch:{ Throwable -> 0x0114 }
            java.lang.Object r2 = r2.second     // Catch:{ Throwable -> 0x0114 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ Throwable -> 0x0114 }
        L_0x0045:
            java.lang.String r4 = "4"
            com.uc.webview.export.cyclone.UCElapseTime r9 = new com.uc.webview.export.cyclone.UCElapseTime
            r9.<init>()
            boolean r8 = com.uc.webview.export.internal.utility.c.a((java.lang.String) r7)     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            if (r8 != 0) goto L_0x00e0
            java.io.File r8 = new java.io.File     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            r8.<init>(r7)     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            boolean r10 = r8.isDirectory()     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            if (r10 != 0) goto L_0x00d5
            com.uc.webview.export.internal.setup.UCSetupException r8 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            r10 = 3006(0xbbe, float:4.212E-42)
            java.lang.String r11 = "Directory expected for LibraryTask but [%s] given."
            r12 = 1
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            r13 = 0
            r12[r13] = r7     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            java.lang.String r7 = java.lang.String.format(r11, r12)     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            r8.<init>((int) r10, (java.lang.String) r7)     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            throw r8     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
        L_0x0073:
            r7 = move-exception
        L_0x0074:
            if (r5 != 0) goto L_0x0119
            java.io.File r8 = new java.io.File     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            android.content.pm.ApplicationInfo r10 = r0.getApplicationInfo()     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            java.lang.String r10 = r10.nativeLibraryDir     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            r8.<init>(r10)     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            java.io.File r10 = new java.io.File     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            java.lang.String r12 = "lib"
            r11.<init>(r12)     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            java.lang.String r12 = r15.a     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            java.lang.String r12 = ".so"
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            java.lang.String r11 = r11.toString()     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            r10.<init>(r8, r11)     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            boolean r10 = r10.exists()     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            if (r10 == 0) goto L_0x011e
            java.lang.String r8 = r8.getAbsolutePath()     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            com.uc.webview.export.internal.c.b.a(r0, r1, r2, r8)     // Catch:{ UCSetupException -> 0x00ea, Throwable -> 0x00f0 }
            java.lang.String r0 = "2"
        L_0x00af:
            r1 = r0
            r2 = r7
            r5 = r6
        L_0x00b2:
            if (r5 != 0) goto L_0x0116
            java.lang.String r0 = r15.a     // Catch:{ Throwable -> 0x00fb }
            java.lang.System.loadLibrary(r0)     // Catch:{ Throwable -> 0x00fb }
            java.lang.String r0 = "3"
            r1 = r2
        L_0x00bd:
            android.util.Pair r2 = new android.util.Pair     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r4 = "sdk_lib"
            com.uc.webview.export.internal.setup.w r5 = new com.uc.webview.export.internal.setup.w     // Catch:{ Throwable -> 0x0112 }
            r5.<init>(r15, r0, r9, r1)     // Catch:{ Throwable -> 0x0112 }
            r2.<init>(r4, r5)     // Catch:{ Throwable -> 0x0112 }
            r15.callbackStat(r2)     // Catch:{ Throwable -> 0x0112 }
        L_0x00cd:
            if (r1 == 0) goto L_0x0106
            throw r1
        L_0x00d0:
            r1 = move-exception
            r1 = r3
        L_0x00d2:
            r2 = r3
            goto L_0x0045
        L_0x00d5:
            java.lang.String r7 = r8.getAbsolutePath()     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            com.uc.webview.export.internal.c.b.a(r0, r1, r2, r7)     // Catch:{ UCSetupException -> 0x0073, Throwable -> 0x00e2 }
            java.lang.String r4 = "1"
            r5 = r6
        L_0x00e0:
            r7 = r3
            goto L_0x0074
        L_0x00e2:
            r7 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r8 = new com.uc.webview.export.internal.setup.UCSetupException
            r8.<init>((int) r14, (java.lang.Throwable) r7)
            r7 = r8
            goto L_0x0074
        L_0x00ea:
            r0 = move-exception
            if (r7 != 0) goto L_0x011c
        L_0x00ed:
            r1 = r4
            r2 = r0
            goto L_0x00b2
        L_0x00f0:
            r0 = move-exception
            if (r7 != 0) goto L_0x0119
            com.uc.webview.export.internal.setup.UCSetupException r7 = new com.uc.webview.export.internal.setup.UCSetupException
            r7.<init>((int) r14, (java.lang.Throwable) r0)
            r1 = r4
            r2 = r7
            goto L_0x00b2
        L_0x00fb:
            r0 = move-exception
            if (r2 != 0) goto L_0x0116
            com.uc.webview.export.internal.setup.UCSetupException r2 = new com.uc.webview.export.internal.setup.UCSetupException
            r2.<init>((int) r14, (java.lang.Throwable) r0)
            r0 = r1
            r1 = r2
            goto L_0x00bd
        L_0x0106:
            android.util.Pair r0 = new android.util.Pair
            java.lang.String r1 = "sdk_stp_l"
            r0.<init>(r1, r3)
            r15.callbackStat(r0)
            return
        L_0x0112:
            r0 = move-exception
            goto L_0x00cd
        L_0x0114:
            r2 = move-exception
            goto L_0x00d2
        L_0x0116:
            r0 = r1
            r1 = r2
            goto L_0x00bd
        L_0x0119:
            r1 = r4
            r2 = r7
            goto L_0x00b2
        L_0x011c:
            r0 = r7
            goto L_0x00ed
        L_0x011e:
            r0 = r4
            r6 = r5
            goto L_0x00af
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.v.run():void");
    }
}
