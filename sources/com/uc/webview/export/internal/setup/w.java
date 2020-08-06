package com.uc.webview.export.internal.setup;

import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.internal.interfaces.IWaStat;
import java.util.HashMap;

/* compiled from: ProGuard */
final class w extends HashMap<String, String> {
    final /* synthetic */ String a;
    final /* synthetic */ UCElapseTime b;
    final /* synthetic */ UCSetupException c;
    final /* synthetic */ v d;

    w(v vVar, String str, UCElapseTime uCElapseTime, UCSetupException uCSetupException) {
        this.d = vVar;
        this.a = str;
        this.b = uCElapseTime;
        this.c = uCSetupException;
        put(IWaStat.KEY_CNT, "1");
        put("code", this.a);
        put(IWaStat.KEY_COST_CPU, String.valueOf(this.b.getMilisCpu()));
        put(IWaStat.KEY_COST, String.valueOf(this.b.getMilis()));
        put("data", this.c != null ? String.valueOf(this.c.errCode()) : "");
    }
}
