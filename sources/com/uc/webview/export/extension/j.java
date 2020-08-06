package com.uc.webview.export.extension;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: ProGuard */
final class j implements ValueCallback<UpdateTask> {
    j() {
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        IWaStat.WaStat.stat(IWaStat.VIDEO_UNZIP_SUCCESS);
    }
}
