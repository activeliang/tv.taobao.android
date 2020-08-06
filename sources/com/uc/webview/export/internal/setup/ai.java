package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import com.uc.webview.export.internal.setup.UCSubSetupTask;
import com.yunos.tv.blitz.service.BlitzServiceUtils;

/* compiled from: ProGuard */
final class ai implements ValueCallback<v> {
    final /* synthetic */ o a;
    final /* synthetic */ ah b;

    ai(ah ahVar, o oVar) {
        this.b = ahVar;
        this.a = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        this.b.a.callback("load");
        q qVar = new q();
        Object[] objArr = {this.b.a};
        ag agVar = this.b.a;
        agVar.getClass();
        ag agVar2 = this.b.a;
        agVar2.getClass();
        ag agVar3 = this.b.a;
        agVar3.getClass();
        ((q) ((q) ((q) ((q) ((q) ((q) ((q) ((q) qVar.invoke(10001, objArr)).setOptions(this.b.a.mOptions)).setUCM(this.a.mUCM)).setClassLoader(this.a.mCL)).onEvent("stat", new UCSubSetupTask.a())).onEvent("exception", new UCAsyncTask.b())).onEvent("stop", new UCAsyncTask.c())).onEvent(BlitzServiceUtils.CSUCCESS, new aj(this))).start();
    }
}
