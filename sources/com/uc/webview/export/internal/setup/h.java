package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCSubSetupTask;
import com.yunos.tv.blitz.service.BlitzServiceUtils;

/* compiled from: ProGuard */
final class h implements ValueCallback<u> {
    final /* synthetic */ b a;

    h(b bVar) {
        this.a = bVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        this.a.callStatException(IWaStat.SETUP_DEFAULT_EXCEPTION, ((u) obj).getException());
        y yVar = new y();
        Object[] objArr = {this.a};
        b bVar = this.a;
        bVar.getClass();
        ((u) ((u) ((u) ((u) ((u) yVar.invoke(10001, objArr)).setOptions(this.a.b.mOptions)).onEvent("stat", new UCSubSetupTask.a())).onEvent(BlitzServiceUtils.CSUCCESS, this.a.i)).onEvent("exception", this.a.j)).start();
    }
}
