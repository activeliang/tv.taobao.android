package com.uc.webview.export.internal.a;

import com.uc.webview.export.internal.c;
import com.uc.webview.export.internal.c.a.a;
import com.uc.webview.export.internal.c.a.b;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.Utils;

/* compiled from: ProGuard */
public final class s extends c {
    Runnable f = new t(this);

    public final void a(IWebView iWebView, int i) {
        if (i == 0) {
            if (d != 1) {
                d = 1;
            }
        } else if (d == 1) {
            e.removeCallbacks(this.f);
            e.post(this.f);
        }
    }

    public final void b(IWebView iWebView) {
        a.remove(iWebView);
        if (a.isEmpty()) {
            boolean z = Utils.sWAPrintLog;
            if (a.a == null && d.e != null) {
                a.a(d.e);
            }
            a aVar = a.a;
            if (!d.f) {
                if (((Boolean) d.a(10006, "stat", true)).booleanValue()) {
                    try {
                        new b(aVar).start();
                        Thread.sleep(20);
                    } catch (Exception e) {
                        Log.e("SDKWaStat", "saveData", e);
                    }
                }
            }
        }
    }
}
