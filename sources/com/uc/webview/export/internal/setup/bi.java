package com.uc.webview.export.internal.setup;

import android.os.Handler;
import android.os.Looper;
import android.webkit.ValueCallback;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: ProGuard */
final class bi implements ValueCallback<UpdateTask> {
    final /* synthetic */ bd a;
    private int b = 3;

    bi(bd bdVar) {
        this.a = bdVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        UpdateTask updateTask = (UpdateTask) obj;
        int i = this.b;
        this.b = i - 1;
        if (i > 0) {
            new Handler(Looper.getMainLooper()).postDelayed(new bj(this, updateTask), 60000);
        }
    }
}
