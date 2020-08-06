package com.uc.webview.export.internal.a;

import android.webkit.DownloadListener;

/* compiled from: ProGuard */
public final class b implements DownloadListener {
    private com.uc.webview.export.DownloadListener a;

    public b(com.uc.webview.export.DownloadListener downloadListener) {
        this.a = downloadListener;
    }

    public final void onDownloadStart(String str, String str2, String str3, String str4, long j) {
        if (this.a != null) {
            this.a.onDownloadStart(str, str2, str3, str4, j);
        }
    }
}
