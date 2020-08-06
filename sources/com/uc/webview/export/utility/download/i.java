package com.uc.webview.export.utility.download;

/* compiled from: ProGuard */
final class i implements Runnable {
    final /* synthetic */ DownloadTask a;
    final /* synthetic */ h b;

    i(h hVar, DownloadTask downloadTask) {
        this.b = hVar;
        this.a = downloadTask;
    }

    public final void run() {
        this.a.start();
    }
}
