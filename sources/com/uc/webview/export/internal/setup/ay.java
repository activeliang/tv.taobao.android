package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.d;

/* compiled from: ProGuard */
final class ay implements ValueCallback {
    ay() {
    }

    public final void onReceiveValue(Object obj) {
        d.n = false;
        d.a((int) UCMPackageInfo.sortByLastModified, new Object[0]);
    }
}
