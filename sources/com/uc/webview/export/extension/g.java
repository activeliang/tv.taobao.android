package com.uc.webview.export.extension;

import android.webkit.ValueCallback;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: ProGuard */
final class g implements ValueCallback<UpdateTask> {
    g() {
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        ((UpdateTask) obj).delete();
    }
}
