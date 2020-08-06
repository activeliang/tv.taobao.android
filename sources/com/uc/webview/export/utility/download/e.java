package com.uc.webview.export.utility.download;

import com.uc.webview.export.cyclone.UCCyclone;

/* compiled from: ProGuard */
final class e implements Runnable {
    final /* synthetic */ UpdateTask a;

    e(UpdateTask updateTask) {
        this.a = updateTask;
    }

    public final void run() {
        try {
            synchronized (this.a) {
                UCCyclone.recursiveDelete(this.a.getUpdateDir(), false, (Object) null);
            }
        } catch (Throwable th) {
        }
    }
}
