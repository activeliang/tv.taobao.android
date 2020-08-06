package com.uc.webview.export.internal.setup;

import com.uc.webview.export.internal.setup.UCMPackageInfo;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.c;
import com.uc.webview.export.utility.SetupTask;
import java.io.File;
import java.util.ArrayList;

/* compiled from: ProGuard */
public final class af extends UCSubSetupTask<af, af> {
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: com.uc.webview.export.internal.setup.UCMPackageInfo$a} */
    /* JADX WARNING: type inference failed for: r3v0 */
    /* JADX WARNING: type inference failed for: r3v1, types: [java.io.File] */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r9 = this;
            r1 = 0
            r3 = 0
            java.lang.String r0 = "share_core"
            java.lang.Object r0 = r9.getOption(r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            if (r0 != 0) goto L_0x0028
            r2 = r1
        L_0x000e:
            java.lang.String r0 = "CONTEXT"
            java.lang.Object r0 = r9.getOption(r0)
            android.content.Context r0 = (android.content.Context) r0
            r4 = 10011(0x271b, float:1.4028E-41)
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.Object r1 = com.uc.webview.export.internal.setup.UCMPackageInfo.invoke(r4, r1)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x002e
        L_0x0027:
            return
        L_0x0028:
            boolean r0 = r0.booleanValue()
            r2 = r0
            goto L_0x000e
        L_0x002e:
            com.uc.webview.export.internal.setup.UCMRunningInfo r1 = com.uc.webview.export.utility.SetupTask.getTotalLoadedUCM()
            if (r1 == 0) goto L_0x0027
            int r4 = r1.coreType
            r5 = 2
            if (r4 == r5) goto L_0x0027
            int r4 = r1.coreType
            r5 = 1
            if (r4 != r5) goto L_0x0085
            com.uc.webview.export.internal.setup.UCMPackageInfo r1 = r1.ucmPackageInfo
            r4 = r1
            r1 = r3
        L_0x0042:
            java.io.File r6 = com.uc.webview.export.utility.download.UpdateTask.getUpdateRoot(r0)     // Catch:{ Exception -> 0x012d }
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x012d }
            java.lang.String r0 = r6.getParent()     // Catch:{ Exception -> 0x012d }
            java.lang.String r7 = "config.json"
            r5.<init>(r0, r7)     // Catch:{ Exception -> 0x012d }
            java.lang.String r0 = "ShareCoreTask"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0074 }
            java.lang.String r8 = "处理共享内核:"
            r7.<init>(r8)     // Catch:{ Exception -> 0x0074 }
            java.lang.StringBuilder r7 = r7.append(r2)     // Catch:{ Exception -> 0x0074 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x0074 }
            com.uc.webview.export.internal.utility.Log.d(r0, r7)     // Catch:{ Exception -> 0x0074 }
            if (r2 != 0) goto L_0x008e
            boolean r0 = r5.exists()     // Catch:{ Exception -> 0x0074 }
            if (r0 == 0) goto L_0x0027
            r5.delete()     // Catch:{ Exception -> 0x0074 }
            goto L_0x0027
        L_0x0074:
            r0 = move-exception
            r3 = r5
        L_0x0076:
            java.lang.String r1 = "ShareCoreTask"
            java.lang.String r2 = "FilePermissions.run"
            com.uc.webview.export.internal.utility.Log.e(r1, r2, r0)
            if (r3 == 0) goto L_0x0027
            r3.delete()
            goto L_0x0027
        L_0x0085:
            int r4 = r1.coreType
            r5 = 3
            if (r4 != r5) goto L_0x0130
            com.uc.webview.export.internal.setup.UCMPackageInfo r1 = r1.ucmPackageInfo
            r4 = r3
            goto L_0x0042
        L_0x008e:
            java.util.ArrayList r7 = new java.util.ArrayList     // Catch:{ Exception -> 0x0074 }
            r7.<init>()     // Catch:{ Exception -> 0x0074 }
            if (r4 != 0) goto L_0x00b3
            r0 = r3
        L_0x0096:
            if (r0 == 0) goto L_0x009b
            r7.add(r0)     // Catch:{ Exception -> 0x0074 }
        L_0x009b:
            if (r1 != 0) goto L_0x00d7
        L_0x009d:
            if (r3 == 0) goto L_0x00a2
            r7.add(r3)     // Catch:{ Exception -> 0x0074 }
        L_0x00a2:
            boolean r1 = a((java.util.List<com.uc.webview.export.internal.setup.UCMPackageInfo.a>) r7, (java.io.File) r5)     // Catch:{ Exception -> 0x0074 }
            if (r1 != 0) goto L_0x00dd
            java.lang.String r0 = "ShareCoreTask"
            java.lang.String r1 = "处理共享内核:内核信息无变化"
            com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Exception -> 0x0074 }
            goto L_0x0027
        L_0x00b3:
            com.uc.webview.export.internal.setup.UCMPackageInfo$a r2 = new com.uc.webview.export.internal.setup.UCMPackageInfo$a     // Catch:{ Exception -> 0x0074 }
            r2.<init>()     // Catch:{ Exception -> 0x0074 }
            java.lang.String r0 = com.uc.webview.export.Build.UCM_VERSION     // Catch:{ Exception -> 0x0074 }
            r2.a = r0     // Catch:{ Exception -> 0x0074 }
            java.lang.String r0 = com.uc.webview.export.Build.UCM_SUPPORT_SDK_MIN     // Catch:{ Exception -> 0x0074 }
            r2.b = r0     // Catch:{ Exception -> 0x0074 }
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x0074 }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r4.sdkShellModule     // Catch:{ Exception -> 0x0074 }
            java.lang.Object r0 = r0.first     // Catch:{ Exception -> 0x0074 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0074 }
            r8.<init>(r0)     // Catch:{ Exception -> 0x0074 }
            java.lang.String r0 = r8.getParent()     // Catch:{ Exception -> 0x0074 }
            r2.c = r0     // Catch:{ Exception -> 0x0074 }
            java.lang.String r0 = r4.soDirPath     // Catch:{ Exception -> 0x0074 }
            r2.d = r0     // Catch:{ Exception -> 0x0074 }
            r0 = r2
            goto L_0x0096
        L_0x00d7:
            com.uc.webview.export.internal.setup.UCMPackageInfo$a r3 = new com.uc.webview.export.internal.setup.UCMPackageInfo$a     // Catch:{ Exception -> 0x0074 }
            r3.<init>()     // Catch:{ Exception -> 0x0074 }
            goto L_0x009d
        L_0x00dd:
            boolean r1 = com.uc.webview.export.internal.utility.c.c((java.io.File) r6)     // Catch:{ Exception -> 0x0074 }
            if (r1 != 0) goto L_0x00ee
            java.lang.String r0 = "ShareCoreTask"
            java.lang.String r1 = "setExecutable(ucmRoot) fail."
            com.uc.webview.export.internal.utility.Log.e(r0, r1)     // Catch:{ Exception -> 0x0074 }
            goto L_0x0027
        L_0x00ee:
            java.lang.String[] r1 = a()     // Catch:{ Exception -> 0x0074 }
            if (r0 == 0) goto L_0x00fd
            boolean r1 = a((com.uc.webview.export.internal.setup.UCMPackageInfo.a) r0, (java.lang.String[]) r1)     // Catch:{ Exception -> 0x0074 }
            if (r1 != 0) goto L_0x00fd
            r7.remove(r0)     // Catch:{ Exception -> 0x0074 }
        L_0x00fd:
            r0 = 0
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0074 }
            if (r3 == 0) goto L_0x010b
            boolean r0 = a((com.uc.webview.export.internal.setup.UCMPackageInfo.a) r3, (java.lang.String[]) r0)     // Catch:{ Exception -> 0x0074 }
            if (r0 != 0) goto L_0x010b
            r7.remove(r3)     // Catch:{ Exception -> 0x0074 }
        L_0x010b:
            boolean r0 = com.uc.webview.export.internal.setup.UCMPackageInfo.a.a(r7, r5)     // Catch:{ Exception -> 0x0074 }
            if (r0 == 0) goto L_0x0122
            boolean r0 = com.uc.webview.export.internal.utility.c.a((java.io.File) r5)     // Catch:{ Exception -> 0x0074 }
            if (r0 == 0) goto L_0x0122
            java.lang.String r0 = "ShareCoreTask"
            java.lang.String r1 = "成功修改共享内核配置文件"
            com.uc.webview.export.internal.utility.Log.i(r0, r1)     // Catch:{ Exception -> 0x0074 }
            goto L_0x0027
        L_0x0122:
            java.lang.String r0 = "ShareCoreTask"
            java.lang.String r1 = "修改共享内核配置文件失败"
            com.uc.webview.export.internal.utility.Log.i(r0, r1)     // Catch:{ Exception -> 0x0074 }
            goto L_0x0027
        L_0x012d:
            r0 = move-exception
            goto L_0x0076
        L_0x0130:
            r1 = r3
            r4 = r3
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.af.run():void");
    }

    private static String[] a() {
        ArrayList arrayList = new ArrayList();
        try {
            if (((Boolean) SetupTask.classForName("com.uc.webview.utils.CoreFeatureConfig").getDeclaredField("ENABLE_V8_LIB_SHARED").get((Object) null)).booleanValue()) {
                arrayList.add("libV8_UC.so");
            }
        } catch (Throwable th) {
        }
        arrayList.add("libWebCore_UC.so");
        arrayList.add("libandroid_uc_40.so");
        arrayList.add("libandroid_uc_41.so");
        arrayList.add("libandroid_uc_42.so");
        arrayList.add("libandroid_uc_43.so");
        arrayList.add("libandroid_uc_44.so");
        arrayList.add("libandroid_uc_50.so");
        arrayList.add("libskia_neon_uc.so");
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private static boolean a(UCMPackageInfo.a aVar, String[] strArr) {
        File file = new File(aVar.c);
        if (!c.c(file.getParentFile())) {
            Log.e("ShareCoreTask", "setExecutable(dexDir.getParentFile()) fail." + file.getParentFile());
            return false;
        } else if (!c.c(file)) {
            Log.e("ShareCoreTask", "setExecutable(dexDir) fail." + file);
            return false;
        } else {
            File file2 = new File(aVar.d);
            if (c.d(file2) || c.c(file2)) {
                int length = strArr.length;
                int i = 0;
                while (i < length) {
                    File file3 = new File(file2, strArr[i]);
                    if ((c.d(file3) || c.c(file3)) && (c.b(file3) || c.a(file3))) {
                        i++;
                    } else {
                        Log.e("ShareCoreTask", "setExecutable(soFile) or setReadable(soFile) fail." + file3);
                        return false;
                    }
                }
                String[] strArr2 = {"browser_if.jar", "core.jar", "sdk_shell.jar"};
                int i2 = 0;
                while (i2 < 3) {
                    File file4 = new File(file, strArr2[i2]);
                    if (c.b(file4) || c.a(file4)) {
                        i2++;
                    } else {
                        Log.e("ShareCoreTask", "setReadable(new File(dexDir, dex)) fail." + file4);
                        return false;
                    }
                }
                return true;
            }
            Log.e("ShareCoreTask", "setExecutable(soDir) fail." + file2);
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:66:0x00b1 A[LOOP:0: B:6:0x0019->B:66:0x00b1, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x00a0 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean a(java.util.List<com.uc.webview.export.internal.setup.UCMPackageInfo.a> r8, java.io.File r9) {
        /*
            r4 = 1
            r3 = 0
            boolean r0 = r9.exists()
            if (r0 != 0) goto L_0x000a
            r3 = r4
        L_0x0009:
            return r3
        L_0x000a:
            java.util.List r6 = com.uc.webview.export.internal.setup.UCMPackageInfo.a.a(r9)
            int r0 = r6.size()
            int r1 = r8.size()
            if (r0 != r1) goto L_0x0009
            r2 = r3
        L_0x0019:
            int r0 = r6.size()
            if (r2 >= r0) goto L_0x0009
            java.lang.Object r0 = r6.get(r2)
            com.uc.webview.export.internal.setup.UCMPackageInfo$a r0 = (com.uc.webview.export.internal.setup.UCMPackageInfo.a) r0
            java.lang.Object r1 = r8.get(r2)
            com.uc.webview.export.internal.setup.UCMPackageInfo$a r1 = (com.uc.webview.export.internal.setup.UCMPackageInfo.a) r1
            java.lang.String r5 = r0.a
            java.lang.String r7 = r1.a
            if (r5 != 0) goto L_0x0033
            if (r7 == 0) goto L_0x003b
        L_0x0033:
            if (r5 == 0) goto L_0x00a3
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00a3
        L_0x003b:
            r5 = r4
        L_0x003c:
            if (r5 == 0) goto L_0x00af
            java.lang.String r5 = r0.b
            java.lang.String r7 = r1.b
            if (r5 != 0) goto L_0x0046
            if (r7 == 0) goto L_0x004e
        L_0x0046:
            if (r5 == 0) goto L_0x00a5
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00a5
        L_0x004e:
            r5 = r4
        L_0x004f:
            if (r5 == 0) goto L_0x00af
            java.lang.String r5 = r0.c
            java.lang.String r7 = r1.c
            if (r5 != 0) goto L_0x0059
            if (r7 == 0) goto L_0x0061
        L_0x0059:
            if (r5 == 0) goto L_0x00a7
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00a7
        L_0x0061:
            r5 = r4
        L_0x0062:
            if (r5 == 0) goto L_0x00af
            java.lang.String r5 = r0.d
            java.lang.String r7 = r1.d
            if (r5 != 0) goto L_0x006c
            if (r7 == 0) goto L_0x0074
        L_0x006c:
            if (r5 == 0) goto L_0x00a9
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00a9
        L_0x0074:
            r5 = r4
        L_0x0075:
            if (r5 == 0) goto L_0x00af
            java.lang.String r5 = r0.e
            java.lang.String r7 = r1.e
            if (r5 != 0) goto L_0x007f
            if (r7 == 0) goto L_0x0087
        L_0x007f:
            if (r5 == 0) goto L_0x00ab
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00ab
        L_0x0087:
            r5 = r4
        L_0x0088:
            if (r5 == 0) goto L_0x00af
            java.lang.String r0 = r0.f
            java.lang.String r1 = r1.f
            if (r0 != 0) goto L_0x0092
            if (r1 == 0) goto L_0x009a
        L_0x0092:
            if (r0 == 0) goto L_0x00ad
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00ad
        L_0x009a:
            r0 = r4
        L_0x009b:
            if (r0 == 0) goto L_0x00af
            r0 = r4
        L_0x009e:
            if (r0 != 0) goto L_0x00b1
            r3 = r4
            goto L_0x0009
        L_0x00a3:
            r5 = r3
            goto L_0x003c
        L_0x00a5:
            r5 = r3
            goto L_0x004f
        L_0x00a7:
            r5 = r3
            goto L_0x0062
        L_0x00a9:
            r5 = r3
            goto L_0x0075
        L_0x00ab:
            r5 = r3
            goto L_0x0088
        L_0x00ad:
            r0 = r3
            goto L_0x009b
        L_0x00af:
            r0 = r3
            goto L_0x009e
        L_0x00b1:
            int r0 = r2 + 1
            r2 = r0
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.af.a(java.util.List, java.io.File):boolean");
    }
}
