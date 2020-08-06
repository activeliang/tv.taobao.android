package com.uc.webview.export.internal.setup;

import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.internal.interfaces.IWaStat;
import java.util.HashMap;

/* compiled from: ProGuard */
final class s extends HashMap<String, String> {
    final /* synthetic */ boolean a;
    final /* synthetic */ UCElapseTime b;
    final /* synthetic */ q c;

    s(q qVar, boolean z, UCElapseTime uCElapseTime) {
        this.c = qVar;
        this.a = z;
        this.b = uCElapseTime;
        put(IWaStat.KEY_CNT, "1");
        put("data", this.a ? "ucm" : "sdk");
        put(IWaStat.KEY_COST_CPU, String.valueOf(this.b.getMilisCpu()));
        put(IWaStat.KEY_COST, String.valueOf(this.b.getMilis()));
    }
}
