package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import com.uc.webview.export.internal.setup.UCSubSetupTask;
import com.yunos.tv.blitz.service.BlitzServiceUtils;

/* compiled from: ProGuard */
final class ah implements ValueCallback<o> {
    final /* synthetic */ ag a;

    ah(ag agVar) {
        this.a = agVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        o oVar = (o) obj;
        this.a.callback("setup");
        v vVar = new v();
        Object[] objArr = {this.a};
        ag agVar = this.a;
        agVar.getClass();
        ag agVar2 = this.a;
        agVar2.getClass();
        ag agVar3 = this.a;
        agVar3.getClass();
        ((v) ((v) ((v) ((v) ((v) ((v) ((v) ((v) vVar.invoke(10001, objArr)).setOptions(this.a.mOptions)).setUCM(oVar.mUCM)).setClassLoader(oVar.mCL)).onEvent("stat", new UCSubSetupTask.a())).onEvent("exception", new UCAsyncTask.b())).onEvent("stop", new UCAsyncTask.c())).onEvent(BlitzServiceUtils.CSUCCESS, new ai(this, oVar))).start();
    }
}
