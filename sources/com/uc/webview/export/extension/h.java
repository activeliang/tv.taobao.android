package com.uc.webview.export.extension;

import android.content.Context;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.utility.download.UpdateTask;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.util.Map;

/* compiled from: ProGuard */
final class h implements ValueCallback<UpdateTask> {
    final /* synthetic */ Context a;
    final /* synthetic */ Map b;

    h(Context context, Map map) {
        this.a = context;
        this.b = map;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        ValueCallback valueCallback;
        IWaStat.WaStat.stat(IWaStat.VIDEO_DOWNLOAD_SUCCESS);
        d.a(10034, this.a);
        if (this.b != null && (valueCallback = (ValueCallback) this.b.get(BlitzServiceUtils.CSUCCESS)) != null) {
            try {
                valueCallback.onReceiveValue((Object) null);
            } catch (Throwable th) {
            }
        }
    }
}
