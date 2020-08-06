package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.webkit.CookieSyncManager;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;

/* compiled from: ProGuard */
public final class ak extends u {
    public final void run() {
        CookieSyncManager.createInstance((Context) getOption("CONTEXT"));
        callback("setup");
        callback("load");
        d.a(10021, 2);
        setLoadedUCM(new UCMRunningInfo(getContext(), (UCMPackageInfo) null, (ClassLoader) null, (ClassLoader) null, false, false, (UCMobileWebKit) null, 2));
        callback("init");
        callback("switch");
    }
}
