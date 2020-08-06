package com.uc.webview.export.utility.download;

import android.webkit.ValueCallback;

/* compiled from: ProGuard */
final class m implements ValueCallback<DownloadTask> {
    final /* synthetic */ Runnable a;
    final /* synthetic */ UpdateTask b;

    m(UpdateTask updateTask, Runnable runnable) {
        this.b = updateTask;
        this.a = runnable;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        this.a.run();
    }
}
