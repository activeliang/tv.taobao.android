package com.uc.webview.export.internal.c;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PowerManager;
import com.uc.webview.export.internal.c;
import com.uc.webview.export.internal.c.a.b;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.a;
import com.uc.webview.export.utility.Utils;

/* compiled from: ProGuard */
public final class d extends c {
    static Runnable f = new e();
    /* access modifiers changed from: private */
    public static a g;

    public d(Context context) {
        if (g == null) {
            a aVar = new a(context);
            g = aVar;
            aVar.b = new f(this, context);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            aVar.a.registerReceiver(aVar, intentFilter);
            if (a.a((PowerManager) aVar.a.getSystemService("power"))) {
                if (aVar.b != null) {
                    aVar.b.a();
                }
            } else if (aVar.b != null) {
                aVar.b.b();
            }
        }
    }

    public final void a(int i, int i2) {
        if (b != i || c != i2) {
            com.uc.webview.export.internal.d.d.onWindowSizeChanged();
            b = i;
            c = i2;
        }
    }

    public final void a(IWebView iWebView, int i) {
        Log.d("WebViewDetector", "onWindowVisibilityChanged: " + i);
        iWebView.notifyForegroundChanged(i == 0);
        if (i == 0) {
            if (d != 1) {
                com.uc.webview.export.internal.d.d.onResume();
                Log.d("WebViewDetector", "WebViewDetector:onResume");
                d = 1;
            }
        } else if (d == 1) {
            e.removeCallbacks(f);
            e.post(f);
        }
    }

    public final void b(IWebView iWebView) {
        a.remove(iWebView);
        if (a.isEmpty()) {
            if (Utils.sWAPrintLog) {
                Log.d("SDKWaStat", "WebViewDetector:destroy");
            }
            com.uc.webview.export.internal.d.d.onDestroy();
            if (com.uc.webview.export.internal.c.a.a.a == null && com.uc.webview.export.internal.d.e != null) {
                com.uc.webview.export.internal.c.a.a.a(com.uc.webview.export.internal.d.e);
            }
            com.uc.webview.export.internal.c.a.a aVar = com.uc.webview.export.internal.c.a.a.a;
            if (!com.uc.webview.export.internal.d.f) {
                if (((Boolean) com.uc.webview.export.internal.d.a(10006, "stat", true)).booleanValue()) {
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
