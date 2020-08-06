package com.uc.webview.export.internal.a;

import com.uc.webview.export.internal.c.a.a;
import com.uc.webview.export.internal.c.a.b;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.Utils;
import java.util.Iterator;

/* compiled from: ProGuard */
final class t implements Runnable {
    final /* synthetic */ s a;

    t(s sVar) {
        this.a = sVar;
    }

    public final void run() {
        boolean z;
        Iterator it = s.a.iterator();
        while (true) {
            if (it.hasNext()) {
                if (((IWebView) it.next()).getView().getWindowVisibility() == 0) {
                    z = true;
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        if (!z && s.d == 1) {
            boolean z2 = Utils.sWAPrintLog;
            if (a.a == null && d.e != null) {
                a.a(d.e);
            }
            a aVar = a.a;
            if (!d.f) {
                if (((Boolean) d.a(10006, "stat", true)).booleanValue()) {
                    try {
                        new b(aVar).start();
                    } catch (Exception e) {
                        Log.e("SDKWaStat", "saveData", e);
                    }
                }
            }
            int unused = s.d = 0;
        }
    }
}
