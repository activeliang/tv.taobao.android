package com.uc.webview.export.internal.setup;

import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.c;
import java.util.HashMap;

/* compiled from: ProGuard */
final class g extends HashMap<String, String> {
    final /* synthetic */ String a;
    final /* synthetic */ b b;

    g(b bVar, String str) {
        this.b = bVar;
        this.a = str;
        put(IWaStat.KEY_CNT, "1");
        put("data", this.a);
        put(IWaStat.KEY_CPU_CNT, c.a());
        put(IWaStat.KEY_CPU_FREQ, c.b());
    }
}
