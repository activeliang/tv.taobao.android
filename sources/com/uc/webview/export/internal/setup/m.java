package com.uc.webview.export.internal.setup;

import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.c;
import java.util.HashMap;

/* compiled from: ProGuard */
final class m extends HashMap<String, String> {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ l e;

    m(l lVar, String str, String str2, String str3, String str4) {
        this.e = lVar;
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
        put(IWaStat.KEY_CNT, "1");
        put("code", this.a);
        put(IWaStat.KEY_COST, this.b);
        put(IWaStat.KEY_COST_CPU, this.c);
        put("data", this.d);
        put(IWaStat.KEY_CPU_CNT, c.a());
        put(IWaStat.KEY_CPU_FREQ, c.b());
    }
}
