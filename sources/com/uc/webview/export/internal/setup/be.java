package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.d;

/* compiled from: ProGuard */
final class be implements ValueCallback<u> {
    final /* synthetic */ bd a;

    be(bd bdVar) {
        this.a = bdVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        u uVar = (u) obj;
        UCMRunningInfo totalLoadedUCM = UCSetupTask.getTotalLoadedUCM();
        if (totalLoadedUCM == null) {
            return;
        }
        if (totalLoadedUCM.coreType != 2 || !d.j) {
            uVar.stop();
        }
    }
}
