package com.uc.webview.export.internal.setup;

import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: ProGuard */
final class bj implements Runnable {
    final /* synthetic */ UpdateTask a;
    final /* synthetic */ bi b;

    bj(bi biVar, UpdateTask updateTask) {
        this.b = biVar;
        this.a = updateTask;
    }

    public final void run() {
        this.a.start();
    }
}
