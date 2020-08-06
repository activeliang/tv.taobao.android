package com.uc.webview.export.internal.setup;

import com.uc.webview.export.internal.setup.UCAsyncTask;
import com.uc.webview.export.internal.setup.UCSubSetupTask;
import com.yunos.tv.blitz.service.BlitzServiceUtils;

/* compiled from: ProGuard */
public abstract class ag extends u {
    protected o a;

    /* access modifiers changed from: protected */
    public abstract o a();

    public void run() {
        this.a = (o) ((o) ((o) ((o) ((o) ((o) ((o) a().invoke(10001, this)).setOptions(this.mOptions)).onEvent("stat", new UCSubSetupTask.a())).onEvent("exception", new UCAsyncTask.b())).onEvent("stop", new UCAsyncTask.c())).onEvent(BlitzServiceUtils.CSUCCESS, new ah(this))).start();
    }
}
