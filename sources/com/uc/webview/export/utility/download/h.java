package com.uc.webview.export.utility.download;

import android.os.Handler;
import android.os.Looper;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;

/* compiled from: ProGuard */
final class h implements ValueCallback<DownloadTask> {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ UpdateTask b;

    h(UpdateTask updateTask, ValueCallback valueCallback) {
        this.b = updateTask;
        this.a = valueCallback;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        DownloadTask downloadTask = (DownloadTask) obj;
        Throwable exception = downloadTask.getException();
        if (exception != null) {
            int hashCode = exception.getClass().getName().hashCode();
            IWaStat.WaStat.stat(IWaStat.UCM_LAST_EXCEPTION, String.valueOf(hashCode));
            try {
                if (this.b.h != null) {
                    this.b.h.onReceiveValue(new Object[]{7, Integer.valueOf(hashCode)});
                }
            } catch (Throwable th) {
            }
        }
        long[] c = this.b.c;
        c[3] = c[3] + this.b.c[4];
        if (this.b.c[3] < this.b.c[5]) {
            new Handler(Looper.getMainLooper()).postDelayed(new i(this, downloadTask), this.b.c[4]);
            return;
        }
        IWaStat.WaStat.stat(IWaStat.UCM_EXCEPTION_DOWNLOAD);
        this.b.e[1] = new RuntimeException("Download aborted because of up to 10080 retries. Last exception is: " + (downloadTask.getException() != null ? downloadTask.getException().getMessage() : ""));
        try {
            if (this.a != null) {
                this.a.onReceiveValue(this.b);
            }
        } catch (Throwable th2) {
        }
        try {
            if (this.b.h != null) {
                this.b.h.onReceiveValue(new Object[]{4});
            }
        } catch (Throwable th3) {
        }
    }
}
