package com.uc.webview.export.utility.download;

import com.uc.webview.export.cyclone.UCCyclone;
import java.io.File;

/* compiled from: ProGuard */
final class c implements Runnable {
    final /* synthetic */ DownloadTask a;

    c(DownloadTask downloadTask) {
        this.a = downloadTask;
    }

    public final void run() {
        try {
            File file = new File(this.a.d[1]);
            synchronized (this.a) {
                UCCyclone.recursiveDelete(file, false, (Object) null);
            }
        } catch (Throwable th) {
        }
    }
}
