package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.yunos.tv.blitz.service.BlitzServiceUtils;

/* compiled from: ProGuard */
final class e implements ValueCallback<u> {
    final /* synthetic */ b a;

    e(b bVar) {
        this.a = bVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        u uVar = (u) obj;
        if (this.a.c != null) {
            this.a.callStatException(IWaStat.SETUP_REPAIR_EXCEPTION, uVar.getException());
            ((u) ((u) ((u) this.a.c.invoke(10001, this.a)).onEvent(BlitzServiceUtils.CSUCCESS, this.a.i)).onEvent("exception", this.a.j)).start();
            u unused = this.a.c = null;
            return;
        }
        this.a.setException(uVar.getException());
    }
}
