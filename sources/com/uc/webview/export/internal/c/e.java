package com.uc.webview.export.internal.c;

import com.uc.webview.export.internal.c.a.a;
import com.uc.webview.export.internal.c.a.b;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.Utils;
import java.util.Iterator;

/* compiled from: ProGuard */
final class e implements Runnable {
    e() {
    }

    public final void run() {
        boolean z;
        try {
            Iterator it = d.a.iterator();
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
            if (!z && d.d == 1) {
                if (Utils.sWAPrintLog) {
                    Log.d("WebViewDetector", "WebViewDetector:onPause");
                }
                if (a.a == null && d.e != null) {
                    a.a(d.e);
                }
                a aVar = a.a;
                if (!d.f) {
                    if (((Boolean) d.a(10006, "stat", true)).booleanValue()) {
                        new b(aVar).start();
                    }
                }
                d.d.onPause();
                int unused = d.d = 0;
            }
        } catch (Exception e) {
            Log.e("SDKWaStat", "saveData", e);
        } catch (Throwable th) {
        }
    }
}
