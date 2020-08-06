package com.uc.webview.export.internal.c;

import android.content.Context;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.a;

/* compiled from: ProGuard */
final class f implements a.C0010a {
    final /* synthetic */ Context a;
    final /* synthetic */ d b;

    f(d dVar, Context context) {
        this.b = dVar;
        this.a = context;
    }

    public final void a() {
        Log.d("WebViewDetector", "onScreenOn: onScreenOn");
        a unused = d.g;
        if (!a.a(this.a) && d.d != null) {
            d.d.onScreenUnLock();
            d.d.onResume();
            Log.d("WebViewDetector", "onScreenOn: onScreenUnLock");
        }
    }

    public final void b() {
        Log.d("WebViewDetector", "onScreenOff: onScreenOff");
        if (d.d != null) {
            d.d.onScreenLock();
            d.d.onPause();
            Log.d("WebViewDetector", "onScreenOff: onScreenLock");
        }
    }

    public final void c() {
        Log.d("WebViewDetector", "onUserPresent: onUserPresent");
        if (d.d != null) {
            d.d.onScreenUnLock();
            d.d.onResume();
            Log.d("WebViewDetector", "onUserPresent: onScreenUnLock");
        }
    }
}
