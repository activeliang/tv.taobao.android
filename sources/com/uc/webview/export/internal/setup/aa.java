package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;

/* compiled from: ProGuard */
final class aa implements ValueCallback<u> {
    final /* synthetic */ z a;

    aa(z zVar) {
        this.a = zVar;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final /* synthetic */ void onReceiveValue(java.lang.Object r14) {
        /*
            r13 = this;
            r12 = 2
            r2 = 1
            r0 = 0
            com.uc.webview.export.internal.setup.u r14 = (com.uc.webview.export.internal.setup.u) r14
            com.uc.webview.export.internal.setup.UCMRunningInfo r1 = r14.getLoadedUCM()
            if (r1 == 0) goto L_0x01ee
            com.uc.webview.export.internal.setup.UCMRunningInfo r8 = r14.getLoadedUCM()     // Catch:{ Throwable -> 0x01d8 }
            com.uc.webview.export.internal.setup.z r1 = r13.a     // Catch:{ Throwable -> 0x01d8 }
            r1.setLoadedUCM(r8)     // Catch:{ Throwable -> 0x01d8 }
            com.uc.webview.export.internal.setup.z r1 = r13.a     // Catch:{ Throwable -> 0x01d8 }
            r1.setTotalLoadedUCM(r8)     // Catch:{ Throwable -> 0x01d8 }
            com.uc.webview.export.internal.setup.z r1 = r13.a     // Catch:{ Throwable -> 0x0229 }
            android.content.Context r1 = r1.c     // Catch:{ Throwable -> 0x0229 }
            r3 = 1
            r4 = 0
            com.uc.webview.export.internal.setup.UCSetupTask.deleteSetupFiles(r1, r3, r4)     // Catch:{ Throwable -> 0x0229 }
        L_0x0024:
            com.uc.webview.export.internal.setup.z r1 = r13.a     // Catch:{ Throwable -> 0x0226 }
            com.uc.webview.export.internal.setup.UCMRunningInfo r3 = com.uc.webview.export.internal.setup.UCSetupTask.getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0226 }
            int r3 = r3.coreType     // Catch:{ Throwable -> 0x0226 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Throwable -> 0x0226 }
            r1.callbackFinishStat(r3)     // Catch:{ Throwable -> 0x0226 }
        L_0x0033:
            java.lang.String r3 = ""
            java.lang.String r4 = ""
            java.lang.String r5 = ""
            java.lang.String r6 = ""
            java.lang.String r7 = ""
            com.uc.webview.export.internal.setup.UCMRunningInfo r1 = com.uc.webview.export.internal.setup.UCSetupTask.getTotalLoadedUCM()     // Catch:{ Throwable -> 0x021d }
            int r1 = r1.coreType     // Catch:{ Throwable -> 0x021d }
            if (r1 != r12) goto L_0x01cd
            com.uc.webview.export.internal.setup.z r1 = r13.a     // Catch:{ Throwable -> 0x021d }
            com.uc.webview.export.internal.setup.UCSetupException r1 = r1.e     // Catch:{ Throwable -> 0x021d }
            if (r1 == 0) goto L_0x01cd
        L_0x0052:
            if (r2 == 0) goto L_0x009a
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x021d }
            com.uc.webview.export.internal.setup.UCSetupException r0 = r0.e     // Catch:{ Throwable -> 0x021d }
            int r0 = r0.errCode()     // Catch:{ Throwable -> 0x021d }
            java.lang.String r3 = java.lang.String.valueOf(r0)     // Catch:{ Throwable -> 0x021d }
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x0223 }
            com.uc.webview.export.internal.setup.UCSetupException r0 = r0.e     // Catch:{ Throwable -> 0x0223 }
            java.lang.Throwable r0 = r0.getRootCause()     // Catch:{ Throwable -> 0x0223 }
            java.lang.Class r0 = r0.getClass()     // Catch:{ Throwable -> 0x0223 }
            java.lang.String r4 = r0.getSimpleName()     // Catch:{ Throwable -> 0x0223 }
        L_0x0074:
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x0220 }
            com.uc.webview.export.internal.setup.UCSetupException r0 = r0.e     // Catch:{ Throwable -> 0x0220 }
            java.lang.Throwable r0 = r0.getRootCause()     // Catch:{ Throwable -> 0x0220 }
            java.lang.String r5 = r0.getMessage()     // Catch:{ Throwable -> 0x0220 }
        L_0x0082:
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x021d }
            com.uc.webview.export.internal.setup.UCSetupTask r0 = r0.f     // Catch:{ Throwable -> 0x021d }
            java.lang.String r6 = r0.getCrashCode()     // Catch:{ Throwable -> 0x021d }
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x021d }
            com.uc.webview.export.internal.setup.UCSetupTask r0 = r0.f     // Catch:{ Throwable -> 0x021d }
            java.lang.Class r0 = r0.getClass()     // Catch:{ Throwable -> 0x021d }
            java.lang.String r7 = r0.getSimpleName()     // Catch:{ Throwable -> 0x021d }
        L_0x009a:
            com.uc.webview.export.internal.setup.z r9 = r13.a     // Catch:{ Throwable -> 0x021d }
            android.util.Pair r10 = new android.util.Pair     // Catch:{ Throwable -> 0x021d }
            java.lang.String r11 = "sdk_stp_suc"
            com.uc.webview.export.internal.setup.ab r0 = new com.uc.webview.export.internal.setup.ab     // Catch:{ Throwable -> 0x021d }
            r1 = r13
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x021d }
            r10.<init>(r11, r0)     // Catch:{ Throwable -> 0x021d }
            r9.callbackStat(r10)     // Catch:{ Throwable -> 0x021d }
        L_0x00ad:
            com.uc.webview.export.internal.setup.UCMRunningInfo r0 = com.uc.webview.export.internal.setup.UCSetupTask.getTotalLoadedUCM()     // Catch:{ Throwable -> 0x021a }
            int r0 = r0.coreType     // Catch:{ Throwable -> 0x021a }
            if (r0 == r12) goto L_0x00c2
            r0 = 10041(0x2739, float:1.407E-41)
            r1 = 1
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x021a }
            r2 = 0
            java.lang.ClassLoader r3 = r8.shellClassLoader     // Catch:{ Throwable -> 0x021a }
            r1[r2] = r3     // Catch:{ Throwable -> 0x021a }
            com.uc.webview.export.internal.setup.UCMPackageInfo.invoke(r0, r1)     // Catch:{ Throwable -> 0x021a }
        L_0x00c2:
            com.uc.webview.export.internal.setup.UCMPackageInfo r1 = r8.ucmPackageInfo     // Catch:{ Throwable -> 0x0217 }
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x0217 }
            java.lang.String r2 = "loadPolicy"
            java.lang.Object r0 = r0.getOption(r2)     // Catch:{ Throwable -> 0x0217 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x0217 }
            java.lang.String r0 = com.uc.webview.export.internal.d.a((com.uc.webview.export.internal.setup.UCMPackageInfo) r1, (java.lang.String) r0)     // Catch:{ Throwable -> 0x0217 }
            com.uc.webview.export.internal.d.a(r0)     // Catch:{ Throwable -> 0x0217 }
        L_0x00d6:
            com.uc.webview.export.internal.setup.UCMRunningInfo r0 = com.uc.webview.export.internal.setup.UCSetupTask.getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0214 }
            int r0 = r0.coreType     // Catch:{ Throwable -> 0x0214 }
            if (r0 == r12) goto L_0x0115
            com.uc.webview.export.internal.setup.af r0 = new com.uc.webview.export.internal.setup.af     // Catch:{ Throwable -> 0x0214 }
            r0.<init>()     // Catch:{ Throwable -> 0x0214 }
            r1 = 10001(0x2711, float:1.4014E-41)
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0214 }
            r3 = 0
            com.uc.webview.export.internal.setup.UCAsyncTask r4 = com.uc.webview.export.utility.SetupTask.getRoot()     // Catch:{ Throwable -> 0x0214 }
            r2[r3] = r4     // Catch:{ Throwable -> 0x0214 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.invoke(r1, r2)     // Catch:{ Throwable -> 0x0214 }
            com.uc.webview.export.internal.setup.af r0 = (com.uc.webview.export.internal.setup.af) r0     // Catch:{ Throwable -> 0x0214 }
            com.uc.webview.export.internal.setup.z r1 = r13.a     // Catch:{ Throwable -> 0x0214 }
            java.util.HashMap r1 = r1.mOptions     // Catch:{ Throwable -> 0x0214 }
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setOptions(r1)     // Catch:{ Throwable -> 0x0214 }
            com.uc.webview.export.internal.setup.af r0 = (com.uc.webview.export.internal.setup.af) r0     // Catch:{ Throwable -> 0x0214 }
            java.lang.String r1 = "stat"
            com.uc.webview.export.internal.setup.UCSubSetupTask$a r2 = new com.uc.webview.export.internal.setup.UCSubSetupTask$a     // Catch:{ Throwable -> 0x0214 }
            com.uc.webview.export.internal.setup.z r3 = r13.a     // Catch:{ Throwable -> 0x0214 }
            r3.getClass()     // Catch:{ Throwable -> 0x0214 }
            r2.<init>()     // Catch:{ Throwable -> 0x0214 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.onEvent(r1, r2)     // Catch:{ Throwable -> 0x0214 }
            com.uc.webview.export.internal.setup.af r0 = (com.uc.webview.export.internal.setup.af) r0     // Catch:{ Throwable -> 0x0214 }
            r0.start()     // Catch:{ Throwable -> 0x0214 }
        L_0x0115:
            com.uc.webview.export.internal.setup.a r0 = new com.uc.webview.export.internal.setup.a     // Catch:{ Throwable -> 0x0211 }
            r0.<init>()     // Catch:{ Throwable -> 0x0211 }
            r1 = 10001(0x2711, float:1.4014E-41)
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0211 }
            r3 = 0
            com.uc.webview.export.internal.setup.UCAsyncTask r4 = com.uc.webview.export.utility.SetupTask.getRoot()     // Catch:{ Throwable -> 0x0211 }
            r2[r3] = r4     // Catch:{ Throwable -> 0x0211 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.invoke(r1, r2)     // Catch:{ Throwable -> 0x0211 }
            com.uc.webview.export.internal.setup.a r0 = (com.uc.webview.export.internal.setup.a) r0     // Catch:{ Throwable -> 0x0211 }
            com.uc.webview.export.internal.setup.z r1 = r13.a     // Catch:{ Throwable -> 0x0211 }
            java.util.HashMap r1 = r1.mOptions     // Catch:{ Throwable -> 0x0211 }
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setOptions(r1)     // Catch:{ Throwable -> 0x0211 }
            com.uc.webview.export.internal.setup.a r0 = (com.uc.webview.export.internal.setup.a) r0     // Catch:{ Throwable -> 0x0211 }
            java.lang.String r1 = "stat"
            com.uc.webview.export.internal.setup.UCSubSetupTask$a r2 = new com.uc.webview.export.internal.setup.UCSubSetupTask$a     // Catch:{ Throwable -> 0x0211 }
            com.uc.webview.export.internal.setup.z r3 = r13.a     // Catch:{ Throwable -> 0x0211 }
            r3.getClass()     // Catch:{ Throwable -> 0x0211 }
            r2.<init>()     // Catch:{ Throwable -> 0x0211 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.onEvent(r1, r2)     // Catch:{ Throwable -> 0x0211 }
            com.uc.webview.export.internal.setup.a r0 = (com.uc.webview.export.internal.setup.a) r0     // Catch:{ Throwable -> 0x0211 }
            r0.start()     // Catch:{ Throwable -> 0x0211 }
        L_0x014c:
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x020e }
            com.uc.webview.export.internal.setup.u r0 = r0.b     // Catch:{ Throwable -> 0x020e }
            if (r0 == 0) goto L_0x0181
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x020e }
            com.uc.webview.export.internal.setup.u r0 = r0.b     // Catch:{ Throwable -> 0x020e }
            r2 = 2000(0x7d0, double:9.88E-321)
            r0.start(r2)     // Catch:{ Throwable -> 0x020e }
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x020e }
            com.uc.webview.export.internal.setup.u unused = r0.b = null     // Catch:{ Throwable -> 0x020e }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = new com.uc.webview.export.internal.setup.UCAsyncTask     // Catch:{ Throwable -> 0x020e }
            com.uc.webview.export.cyclone.UCDex r1 = new com.uc.webview.export.cyclone.UCDex     // Catch:{ Throwable -> 0x020e }
            r1.<init>()     // Catch:{ Throwable -> 0x020e }
            r0.<init>((java.lang.Runnable) r1)     // Catch:{ Throwable -> 0x020e }
            r1 = 10001(0x2711, float:1.4014E-41)
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x020e }
            r3 = 0
            com.uc.webview.export.internal.setup.UCAsyncTask r4 = com.uc.webview.export.utility.SetupTask.getRoot()     // Catch:{ Throwable -> 0x020e }
            r2[r3] = r4     // Catch:{ Throwable -> 0x020e }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.invoke(r1, r2)     // Catch:{ Throwable -> 0x020e }
            r0.start()     // Catch:{ Throwable -> 0x020e }
        L_0x0181:
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            java.util.List r0 = r0.i     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            if (r0 == 0) goto L_0x01c6
            com.uc.webview.export.internal.setup.n r0 = new com.uc.webview.export.internal.setup.n     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            com.uc.webview.export.internal.setup.z r1 = r13.a     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            java.util.List r1 = r1.i     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            r0.<init>(r1)     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            r1 = 10001(0x2711, float:1.4014E-41)
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            r3 = 0
            com.uc.webview.export.internal.setup.UCAsyncTask r4 = com.uc.webview.export.utility.SetupTask.getRoot()     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            r2[r3] = r4     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.invoke(r1, r2)     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            com.uc.webview.export.internal.setup.n r0 = (com.uc.webview.export.internal.setup.n) r0     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            com.uc.webview.export.internal.setup.z r1 = r13.a     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            java.util.HashMap r1 = r1.mOptions     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setOptions(r1)     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            com.uc.webview.export.internal.setup.n r0 = (com.uc.webview.export.internal.setup.n) r0     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            java.lang.String r1 = "stat"
            com.uc.webview.export.internal.setup.UCSubSetupTask$a r2 = new com.uc.webview.export.internal.setup.UCSubSetupTask$a     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            com.uc.webview.export.internal.setup.z r3 = r13.a     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            r3.getClass()     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            r2.<init>()     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.onEvent(r1, r2)     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            com.uc.webview.export.internal.setup.n r0 = (com.uc.webview.export.internal.setup.n) r0     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
            r0.start()     // Catch:{ Throwable -> 0x01d0, all -> 0x01e6 }
        L_0x01c6:
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x01d8 }
            r1 = 0
            java.util.List unused = r0.i = r1     // Catch:{ Throwable -> 0x01d8 }
        L_0x01cc:
            return
        L_0x01cd:
            r2 = r0
            goto L_0x0052
        L_0x01d0:
            r0 = move-exception
            com.uc.webview.export.internal.setup.z r0 = r13.a     // Catch:{ Throwable -> 0x01d8 }
            r1 = 0
            java.util.List unused = r0.i = r1     // Catch:{ Throwable -> 0x01d8 }
            goto L_0x01cc
        L_0x01d8:
            r0 = move-exception
            com.uc.webview.export.internal.setup.z r1 = r13.a
            com.uc.webview.export.internal.setup.UCSetupException r2 = new com.uc.webview.export.internal.setup.UCSetupException
            r3 = 4004(0xfa4, float:5.611E-42)
            r2.<init>((int) r3, (java.lang.Throwable) r0)
            r1.setException(r2)
            goto L_0x01cc
        L_0x01e6:
            r0 = move-exception
            com.uc.webview.export.internal.setup.z r1 = r13.a     // Catch:{ Throwable -> 0x01d8 }
            r2 = 0
            java.util.List unused = r1.i = r2     // Catch:{ Throwable -> 0x01d8 }
            throw r0     // Catch:{ Throwable -> 0x01d8 }
        L_0x01ee:
            com.uc.webview.export.internal.setup.z r1 = r13.a
            com.uc.webview.export.internal.setup.UCSetupException r3 = new com.uc.webview.export.internal.setup.UCSetupException
            r4 = 4001(0xfa1, float:5.607E-42)
            java.lang.String r5 = "Task [%s] report success but no loaded UCM."
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.Class r6 = r14.getClass()
            java.lang.String r6 = r6.getName()
            r2[r0] = r6
            java.lang.String r0 = java.lang.String.format(r5, r2)
            r3.<init>((int) r4, (java.lang.String) r0)
            r1.setException(r3)
            goto L_0x01cc
        L_0x020e:
            r0 = move-exception
            goto L_0x0181
        L_0x0211:
            r0 = move-exception
            goto L_0x014c
        L_0x0214:
            r0 = move-exception
            goto L_0x0115
        L_0x0217:
            r0 = move-exception
            goto L_0x00d6
        L_0x021a:
            r0 = move-exception
            goto L_0x00c2
        L_0x021d:
            r0 = move-exception
            goto L_0x00ad
        L_0x0220:
            r0 = move-exception
            goto L_0x0082
        L_0x0223:
            r0 = move-exception
            goto L_0x0074
        L_0x0226:
            r1 = move-exception
            goto L_0x0033
        L_0x0229:
            r1 = move-exception
            goto L_0x0024
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.aa.onReceiveValue(java.lang.Object):void");
    }
}
