package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.cyclone.UCDex;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCSubSetupTask;
import com.uc.webview.export.utility.SetupTask;

/* compiled from: ProGuard */
final class c implements ValueCallback<u> {
    final /* synthetic */ b a;

    c(b bVar) {
        this.a = bVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        boolean z = true;
        u uVar = (u) obj;
        if (uVar.getLoadedUCM() != null) {
            try {
                UCMRunningInfo loadedUCM = uVar.getLoadedUCM();
                this.a.setLoadedUCM(loadedUCM);
                this.a.setTotalLoadedUCM(loadedUCM);
                try {
                    Context a2 = this.a.e;
                    if (this.a.f != null) {
                        z = false;
                    }
                    UCSetupTask.deleteSetupFiles(a2, z, true);
                } catch (Throwable th) {
                }
                try {
                    this.a.callbackFinishStat(String.valueOf(UCSetupTask.getTotalLoadedUCM().coreType));
                } catch (Throwable th2) {
                }
                try {
                    this.a.callbackStat(new Pair(IWaStat.SETUP_SUCCESS, new d(this)));
                } catch (Throwable th3) {
                }
                try {
                    UCMPackageInfo.invoke(UCMPackageInfo.initUCMBuildInfo, loadedUCM.shellClassLoader);
                } catch (Throwable th4) {
                }
                try {
                    af afVar = new af();
                    Object[] objArr = {SetupTask.getRoot()};
                    b bVar = this.a;
                    bVar.getClass();
                    ((af) ((af) ((af) afVar.invoke(10001, objArr)).setOptions(this.a.mOptions)).onEvent("stat", new UCSubSetupTask.a())).start();
                } catch (Throwable th5) {
                }
                try {
                    if (this.a.c != null) {
                        ((u) this.a.c.invoke(10001, UCSetupTask.getRoot())).start(5000);
                        u unused = this.a.c = null;
                        new UCAsyncTask((Runnable) new UCDex()).invoke(10001, SetupTask.getRoot()).start();
                    }
                } catch (Throwable th6) {
                }
            } catch (Throwable th7) {
                this.a.setException(new UCSetupException(4004, th7));
            }
        } else {
            this.a.setException(new UCSetupException(4001, String.format("Task [%s] report success but no loaded UCM.", new Object[]{uVar.getClass().getName()})));
        }
    }
}
