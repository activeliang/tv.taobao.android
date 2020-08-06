package com.uc.webview.export.utility.download;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import java.io.File;

/* compiled from: ProGuard */
final class j implements ValueCallback<DownloadTask> {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ UpdateTask b;

    j(UpdateTask updateTask, ValueCallback valueCallback) {
        this.b = updateTask;
        this.a = valueCallback;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        int i = -1;
        DownloadTask downloadTask = (DownloadTask) obj;
        try {
            if (this.a != null) {
                this.a.onReceiveValue(this.b);
            }
        } catch (Throwable th) {
        }
        try {
            long totalSize = downloadTask.getTotalSize();
            long currentSize = totalSize == 0 ? 0 : ((downloadTask.getCurrentSize() * 10) / totalSize) * 10;
            int i2 = (currentSize > 100 || currentSize < 0) ? -1 : (int) currentSize;
            if (i2 > this.b.f || i2 == 100) {
                UpdateTask.f(this.b);
                IWaStat.WaStat.stat(IWaStat.UCM_PERCENT, String.valueOf(i2));
                File file = new File(downloadTask.getFilePath());
                long totalSpace = file.getTotalSpace();
                long freeSpace = file.getFreeSpace();
                IWaStat.WaStat.stat(IWaStat.UCM_DISK_MB, String.valueOf((int) (freeSpace / 1048576)));
                long j = totalSpace == 0 ? 0 : ((freeSpace * 10) / totalSpace) * 10;
                if (j <= 100 && j >= 0) {
                    i = (int) j;
                }
                IWaStat.WaStat.stat(IWaStat.UCM_DISK_PERCENT, String.valueOf(i));
            }
        } catch (Throwable th2) {
        }
    }
}
