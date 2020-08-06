package com.uc.webview.export.internal.setup;

import com.uc.webview.export.internal.interfaces.IWaStat;
import java.util.HashMap;

/* compiled from: ProGuard */
final class p extends HashMap<String, String> {
    final /* synthetic */ Integer a;
    final /* synthetic */ boolean b;
    final /* synthetic */ String c;
    final /* synthetic */ long d;
    final /* synthetic */ long e;
    final /* synthetic */ o f;

    p(o oVar, Integer num, boolean z, String str, long j, long j2) {
        this.f = oVar;
        this.a = num;
        this.b = z;
        this.c = str;
        this.d = j;
        this.e = j2;
        put(IWaStat.KEY_CNT, "1");
        put("code", String.valueOf(this.a));
        put(IWaStat.KEY_FIRST_RUN, this.b ? "T" : "F");
        put("data", this.c);
        put(IWaStat.KEY_COST, String.valueOf(this.d));
        put(IWaStat.KEY_COST_CPU, String.valueOf(this.e));
    }
}
