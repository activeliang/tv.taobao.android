package com.uc.webview.export.internal.setup;

import android.os.HandlerThread;

/* compiled from: ProGuard */
final class aq extends HandlerThread {
    final /* synthetic */ UCAsyncTask a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    aq(UCAsyncTask uCAsyncTask, String str, int i) {
        super(str, i);
        this.a = uCAsyncTask;
    }

    /* access modifiers changed from: protected */
    public final void onLooperPrepared() {
        this.a.i = new ar(this, getLooper()).post(this.a);
    }
}
