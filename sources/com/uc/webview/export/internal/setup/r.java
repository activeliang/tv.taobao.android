package com.uc.webview.export.internal.setup;

import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.internal.c.b;

/* compiled from: ProGuard */
final class r implements Runnable {
    final /* synthetic */ q a;

    r(q qVar) {
        this.a = qVar;
    }

    public final void run() {
        b.k().setStringValue("PaksResourcePath", this.a.mUCM.resDirPath);
        UCLogger create = UCLogger.create("d", "InitTask");
        if (create != null) {
            create.print(String.format("setLoadedUCM: u4: SettingKeys.PaksResourcePath=[%s]", new Object[]{this.a.mUCM.resDirPath}), new Throwable[0]);
        }
    }
}
