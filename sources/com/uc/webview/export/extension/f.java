package com.uc.webview.export.extension;

import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: ProGuard */
final class f implements Runnable {
    final /* synthetic */ UpdateTask a;
    final /* synthetic */ e b;

    f(e eVar, UpdateTask updateTask) {
        this.b = eVar;
        this.a = updateTask;
    }

    public final void run() {
        this.a.start();
    }
}
