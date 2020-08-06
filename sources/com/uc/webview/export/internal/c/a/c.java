package com.uc.webview.export.internal.c.a;

import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.utility.Log;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;

/* compiled from: ProGuard */
public final class c implements Runnable {
    final /* synthetic */ a a;

    public c(a aVar) {
        this.a = aVar;
    }

    public final void run() {
        try {
            a aVar = this.a;
            if (!d.f) {
                if (((Boolean) d.a(10006, "stat", true)).booleanValue()) {
                    new d(aVar).start();
                }
            }
        } catch (Throwable th) {
            Log.i("SDKWaStat", UpdatePreference.UT_CLICK_UPDATE, th);
        }
    }
}
