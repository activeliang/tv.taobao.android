package com.uc.webview.export.internal.setup;

import java.util.List;

/* compiled from: ProGuard */
public final class n extends UCSubSetupTask<n, n> {
    private List<ag> a;

    public n(List<ag> list) {
        this.a = list;
    }

    /* JADX WARNING: Removed duplicated region for block: B:57:0x0175  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x017a A[Catch:{ Exception -> 0x01a5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01c1 A[SYNTHETIC, Splitter:B:71:0x01c1] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x028c A[Catch:{ Exception -> 0x02dd }] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x02a5 A[SYNTHETIC, Splitter:B:82:0x02a5] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x02ee  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x02f1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r12 = this;
            java.lang.String r0 = "DeleteCoreTask"
            java.lang.String r1 = "======deleteSo====="
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            r0 = 12
            java.lang.String[] r3 = new java.lang.String[r0]
            r0 = 0
            java.lang.String r1 = "libV8_UC.so"
            r3[r0] = r1
            r0 = 1
            java.lang.String r1 = "libWebCore_UC.so"
            r3[r0] = r1
            r0 = 2
            java.lang.String r1 = "libandroid_uc_40.so"
            r3[r0] = r1
            r0 = 3
            java.lang.String r1 = "libandroid_uc_41.so"
            r3[r0] = r1
            r0 = 4
            java.lang.String r1 = "libandroid_uc_42.so"
            r3[r0] = r1
            r0 = 5
            java.lang.String r1 = "libandroid_uc_43.so"
            r3[r0] = r1
            r0 = 6
            java.lang.String r1 = "libandroid_uc_44.so"
            r3[r0] = r1
            r0 = 7
            java.lang.String r1 = "libandroid_uc_50.so"
            r3[r0] = r1
            r0 = 8
            java.lang.String r1 = "libskia_neon_uc.so"
            r3[r0] = r1
            r0 = 9
            java.lang.String r1 = "libwebviewuc.so"
            r3[r0] = r1
            r0 = 10
            java.lang.String r1 = "libimagehelper.so"
            r3[r0] = r1
            r0 = 11
            java.lang.String r1 = "libvinit.so"
            r3[r0] = r1
            r0 = 0
            java.util.List<com.uc.webview.export.internal.setup.ag> r1 = r12.a
            java.util.Iterator r5 = r1.iterator()
            r2 = r0
        L_0x0061:
            boolean r0 = r5.hasNext()
            if (r0 == 0) goto L_0x00aa
            java.lang.Object r0 = r5.next()
            com.uc.webview.export.internal.setup.ag r0 = (com.uc.webview.export.internal.setup.ag) r0
            com.uc.webview.export.internal.setup.o r1 = r0.a
            if (r1 == 0) goto L_0x0061
            com.uc.webview.export.internal.setup.o r1 = r0.a
            com.uc.webview.export.internal.setup.UCMPackageInfo r1 = r1.mUCM
            if (r1 == 0) goto L_0x0061
            com.uc.webview.export.internal.setup.o r1 = r0.a
            com.uc.webview.export.internal.setup.UCMPackageInfo r6 = r1.mUCM
            r1 = 10011(0x271b, float:1.4028E-41)
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]
            java.lang.Object r1 = com.uc.webview.export.internal.setup.UCMPackageInfo.invoke(r1, r4)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x00b8
            java.io.File r1 = new java.io.File
            java.lang.String r4 = r6.soDirPath
            r1.<init>(r4)
        L_0x0093:
            java.io.File r4 = new java.io.File
            java.lang.String r7 = "03d5ffead8e5b0b60ef97c56ac6123be"
            r4.<init>(r1, r7)
            boolean r4 = r4.exists()
            if (r4 == 0) goto L_0x00c8
            java.lang.String r0 = "DeleteCoreTask"
            java.lang.String r1 = "超过3次,不再删除"
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
        L_0x00aa:
            java.util.List<com.uc.webview.export.internal.setup.ag> r0 = r12.a
            r0.clear()
            if (r2 == 0) goto L_0x00b7
            java.lang.String r0 = "sdk_stp_dcc"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
        L_0x00b7:
            return
        L_0x00b8:
            java.io.File r4 = new java.io.File
            android.util.Pair<java.lang.String, java.lang.String> r1 = r6.coreImplModule
            java.lang.Object r1 = r1.first
            java.lang.String r1 = (java.lang.String) r1
            r4.<init>(r1)
            java.io.File r1 = r4.getParentFile()
            goto L_0x0093
        L_0x00c8:
            java.lang.String r4 = r6.soDirPath
            boolean r4 = com.uc.webview.export.internal.utility.c.a((java.lang.String) r4)
            if (r4 != 0) goto L_0x01b0
            java.io.File r2 = new java.io.File
            java.lang.String r4 = r6.soDirPath
            r2.<init>(r4)
            java.io.File r4 = r2.getParentFile()
            java.io.File[] r7 = r4.listFiles()     // Catch:{ Exception -> 0x0166 }
            int r8 = r7.length     // Catch:{ Exception -> 0x0166 }
            r2 = 0
        L_0x00e1:
            if (r2 >= r8) goto L_0x0105
            r9 = r7[r2]     // Catch:{ Exception -> 0x0166 }
            java.lang.String r10 = r9.getName()     // Catch:{ Exception -> 0x0166 }
            java.lang.String r11 = "_1119001019_"
            boolean r10 = r10.endsWith(r11)     // Catch:{ Exception -> 0x0166 }
            if (r10 != 0) goto L_0x00ff
            java.lang.String r10 = r9.getName()     // Catch:{ Exception -> 0x0166 }
            java.lang.String r11 = "_1199233459_"
            boolean r10 = r10.endsWith(r11)     // Catch:{ Exception -> 0x0166 }
            if (r10 == 0) goto L_0x0102
        L_0x00ff:
            r9.delete()     // Catch:{ Exception -> 0x0166 }
        L_0x0102:
            int r2 = r2 + 1
            goto L_0x00e1
        L_0x0105:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x0166 }
            java.lang.String r7 = "2e67cdbeb4ec133dcc8204d930aa7145"
            r2.<init>(r4, r7)     // Catch:{ Exception -> 0x0166 }
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x0166 }
            java.lang.String r8 = "299772b0fd1634653ae3c31f366de3f8"
            r7.<init>(r4, r8)     // Catch:{ Exception -> 0x0166 }
            boolean r4 = r2.exists()     // Catch:{ Exception -> 0x0166 }
            if (r4 == 0) goto L_0x011e
            r2.delete()     // Catch:{ Exception -> 0x0166 }
        L_0x011e:
            boolean r2 = r7.exists()     // Catch:{ Exception -> 0x0166 }
            if (r2 == 0) goto L_0x0127
            r7.delete()     // Catch:{ Exception -> 0x0166 }
        L_0x0127:
            r2 = 0
            com.uc.webview.export.internal.setup.o r4 = r0.a     // Catch:{ Throwable -> 0x0171 }
            java.lang.ClassLoader r4 = r4.mShellCL     // Catch:{ Throwable -> 0x0171 }
            if (r4 == 0) goto L_0x02f4
            r4 = 0
            java.lang.String r7 = "com.uc.webview.browser.shell.NativeLibraries"
            r8 = 1
            com.uc.webview.export.internal.setup.o r0 = r0.a     // Catch:{ Throwable -> 0x0171 }
            java.lang.ClassLoader r0 = r0.mShellCL     // Catch:{ Throwable -> 0x0171 }
            java.lang.Class r0 = java.lang.Class.forName(r7, r8, r0)     // Catch:{ Throwable -> 0x0171 }
            if (r0 == 0) goto L_0x0150
            java.lang.String r4 = "LIBRARIES"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r4)     // Catch:{ Throwable -> 0x0171 }
            r4 = 1
            r0.setAccessible(r4)     // Catch:{ Throwable -> 0x0171 }
            r4 = 0
            java.lang.Object r0 = r0.get(r4)     // Catch:{ Throwable -> 0x0171 }
            java.lang.String[][] r0 = (java.lang.String[][]) r0     // Catch:{ Throwable -> 0x0171 }
            r4 = r0
        L_0x0150:
            if (r4 == 0) goto L_0x02f4
            int r0 = r4.length     // Catch:{ Throwable -> 0x0171 }
            if (r0 <= 0) goto L_0x02f4
            int r0 = r4.length     // Catch:{ Throwable -> 0x0171 }
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch:{ Throwable -> 0x0171 }
            r2 = 0
        L_0x0159:
            int r7 = r0.length     // Catch:{ Throwable -> 0x02ea }
            if (r2 >= r7) goto L_0x0173
            r7 = r4[r2]     // Catch:{ Throwable -> 0x02ea }
            r8 = 0
            r7 = r7[r8]     // Catch:{ Throwable -> 0x02ea }
            r0[r2] = r7     // Catch:{ Throwable -> 0x02ea }
            int r2 = r2 + 1
            goto L_0x0159
        L_0x0166:
            r2 = move-exception
            java.lang.String r4 = "DeleteCoreTask"
            java.lang.String r7 = "delete flag:"
            com.uc.webview.export.internal.utility.Log.e(r4, r7, r2)
            goto L_0x0127
        L_0x0171:
            r0 = move-exception
        L_0x0172:
            r0 = r2
        L_0x0173:
            if (r0 != 0) goto L_0x02f1
            r2 = r3
        L_0x0176:
            int r4 = r2.length     // Catch:{ Exception -> 0x01a5 }
            r0 = 0
        L_0x0178:
            if (r0 >= r4) goto L_0x01af
            r7 = r2[r0]     // Catch:{ Exception -> 0x01a5 }
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x01a5 }
            java.lang.String r9 = r6.soDirPath     // Catch:{ Exception -> 0x01a5 }
            r8.<init>(r9, r7)     // Catch:{ Exception -> 0x01a5 }
            boolean r7 = r8.exists()     // Catch:{ Exception -> 0x01a5 }
            if (r7 == 0) goto L_0x018c
            r8.delete()     // Catch:{ Exception -> 0x01a5 }
        L_0x018c:
            java.lang.String r7 = "DeleteCoreTask"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01a5 }
            java.lang.String r10 = "deleteSo:"
            r9.<init>(r10)     // Catch:{ Exception -> 0x01a5 }
            java.lang.StringBuilder r8 = r9.append(r8)     // Catch:{ Exception -> 0x01a5 }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x01a5 }
            com.uc.webview.export.internal.utility.Log.d(r7, r8)     // Catch:{ Exception -> 0x01a5 }
            int r0 = r0 + 1
            goto L_0x0178
        L_0x01a5:
            r0 = move-exception
            java.lang.String r2 = "DeleteCoreTask"
            java.lang.String r4 = "deleteSo:"
            com.uc.webview.export.internal.utility.Log.e(r2, r4, r0)
        L_0x01af:
            r2 = 1
        L_0x01b0:
            r0 = 10011(0x271b, float:1.4028E-41)
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]
            java.lang.Object r0 = com.uc.webview.export.internal.setup.UCMPackageInfo.invoke(r0, r4)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x02ee
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x029a }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r6.coreImplModule     // Catch:{ Exception -> 0x029a }
            java.lang.Object r0 = r0.first     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x029a }
            r2.<init>(r0)     // Catch:{ Exception -> 0x029a }
            r2.delete()     // Catch:{ Exception -> 0x029a }
            java.lang.String r2 = "DeleteCoreTask"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = "delete dex:"
            r4.<init>(r0)     // Catch:{ Exception -> 0x029a }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r6.coreImplModule     // Catch:{ Exception -> 0x029a }
            java.lang.Object r0 = r0.first     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x029a }
            java.lang.StringBuilder r0 = r4.append(r0)     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x029a }
            com.uc.webview.export.internal.utility.Log.d(r2, r0)     // Catch:{ Exception -> 0x029a }
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x029a }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r6.sdkShellModule     // Catch:{ Exception -> 0x029a }
            java.lang.Object r0 = r0.first     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x029a }
            r2.<init>(r0)     // Catch:{ Exception -> 0x029a }
            r2.delete()     // Catch:{ Exception -> 0x029a }
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x029a }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r6.browserIFModule     // Catch:{ Exception -> 0x029a }
            java.lang.Object r0 = r0.first     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x029a }
            r2.<init>(r0)     // Catch:{ Exception -> 0x029a }
            r2.delete()     // Catch:{ Exception -> 0x029a }
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x029a }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r6.coreImplModule     // Catch:{ Exception -> 0x029a }
            java.lang.Object r0 = r0.second     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x029a }
            java.lang.String r4 = "sdk_shell.dex"
            r2.<init>(r0, r4)     // Catch:{ Exception -> 0x029a }
            r2.delete()     // Catch:{ Exception -> 0x029a }
            java.lang.String r2 = "DeleteCoreTask"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = "delete odex:"
            r4.<init>(r0)     // Catch:{ Exception -> 0x029a }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r6.coreImplModule     // Catch:{ Exception -> 0x029a }
            java.lang.Object r0 = r0.second     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x029a }
            java.lang.StringBuilder r0 = r4.append(r0)     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x029a }
            com.uc.webview.export.internal.utility.Log.d(r2, r0)     // Catch:{ Exception -> 0x029a }
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x029a }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r6.coreImplModule     // Catch:{ Exception -> 0x029a }
            java.lang.Object r0 = r0.second     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x029a }
            java.lang.String r4 = "sdk_shell_patch.dex"
            r2.<init>(r0, r4)     // Catch:{ Exception -> 0x029a }
            r2.delete()     // Catch:{ Exception -> 0x029a }
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x029a }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r6.coreImplModule     // Catch:{ Exception -> 0x029a }
            java.lang.Object r0 = r0.second     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x029a }
            java.lang.String r4 = "browser_if.dex"
            r2.<init>(r0, r4)     // Catch:{ Exception -> 0x029a }
            r2.delete()     // Catch:{ Exception -> 0x029a }
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x029a }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r6.coreImplModule     // Catch:{ Exception -> 0x029a }
            java.lang.Object r0 = r0.second     // Catch:{ Exception -> 0x029a }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x029a }
            java.lang.String r4 = "core.dex"
            r2.<init>(r0, r4)     // Catch:{ Exception -> 0x029a }
            r2.delete()     // Catch:{ Exception -> 0x029a }
        L_0x0267:
            r0 = 1
        L_0x0268:
            java.lang.String r2 = "DeleteCoreTask"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02dd }
            java.lang.String r6 = "deleteCoreFlagDir:"
            r4.<init>(r6)     // Catch:{ Exception -> 0x02dd }
            java.lang.StringBuilder r4 = r4.append(r1)     // Catch:{ Exception -> 0x02dd }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x02dd }
            com.uc.webview.export.internal.utility.Log.d(r2, r4)     // Catch:{ Exception -> 0x02dd }
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x02dd }
            java.lang.String r4 = "357bec925f6b3819f19dacb4ded4250d"
            r2.<init>(r1, r4)     // Catch:{ Exception -> 0x02dd }
            boolean r2 = r2.exists()     // Catch:{ Exception -> 0x02dd }
            if (r2 != 0) goto L_0x02a5
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x02dd }
            java.lang.String r4 = "357bec925f6b3819f19dacb4ded4250d"
            r2.<init>(r1, r4)     // Catch:{ Exception -> 0x02dd }
            r2.createNewFile()     // Catch:{ Exception -> 0x02dd }
            r2 = r0
            goto L_0x0061
        L_0x029a:
            r0 = move-exception
            java.lang.String r2 = "DeleteCoreTask"
            java.lang.String r4 = "deleteSo:"
            com.uc.webview.export.internal.utility.Log.e(r2, r4, r0)
            goto L_0x0267
        L_0x02a5:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x02dd }
            java.lang.String r4 = "1bdae3b62f36b3464c560bffb1572d91"
            r2.<init>(r1, r4)     // Catch:{ Exception -> 0x02dd }
            boolean r2 = r2.exists()     // Catch:{ Exception -> 0x02dd }
            if (r2 != 0) goto L_0x02c1
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x02dd }
            java.lang.String r4 = "1bdae3b62f36b3464c560bffb1572d91"
            r2.<init>(r1, r4)     // Catch:{ Exception -> 0x02dd }
            r2.createNewFile()     // Catch:{ Exception -> 0x02dd }
            r2 = r0
            goto L_0x0061
        L_0x02c1:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x02dd }
            java.lang.String r4 = "03d5ffead8e5b0b60ef97c56ac6123be"
            r2.<init>(r1, r4)     // Catch:{ Exception -> 0x02dd }
            boolean r2 = r2.exists()     // Catch:{ Exception -> 0x02dd }
            if (r2 != 0) goto L_0x02da
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x02dd }
            java.lang.String r4 = "03d5ffead8e5b0b60ef97c56ac6123be"
            r2.<init>(r1, r4)     // Catch:{ Exception -> 0x02dd }
            r2.createNewFile()     // Catch:{ Exception -> 0x02dd }
        L_0x02da:
            r2 = r0
            goto L_0x0061
        L_0x02dd:
            r1 = move-exception
            java.lang.String r2 = "DeleteCoreTask"
            java.lang.String r4 = "deleteCoreFlag:"
            com.uc.webview.export.internal.utility.Log.e(r2, r4, r1)
            r2 = r0
            goto L_0x0061
        L_0x02ea:
            r2 = move-exception
            r2 = r0
            goto L_0x0172
        L_0x02ee:
            r0 = r2
            goto L_0x0268
        L_0x02f1:
            r2 = r0
            goto L_0x0176
        L_0x02f4:
            r0 = r2
            goto L_0x0173
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.n.run():void");
    }
}
