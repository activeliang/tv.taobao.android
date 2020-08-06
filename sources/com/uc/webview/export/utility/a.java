package com.uc.webview.export.utility;

import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCSetupTask;
import java.util.HashMap;

/* compiled from: ProGuard */
final class a extends HashMap<String, String> {
    final /* synthetic */ String a;
    final /* synthetic */ SetupTask b;

    a(SetupTask setupTask, String str) {
        this.b = setupTask;
        this.a = str;
        put(IWaStat.KEY_CNT, "1");
        put("code", this.a);
        put(IWaStat.KEY_FIRST_RUN, UCSetupTask.getTotalLoadedUCM().isFirstTimeOdex ? "T" : "F");
    }
}
