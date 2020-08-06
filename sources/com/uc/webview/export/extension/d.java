package com.uc.webview.export.extension;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.webkit.ValueCallback;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.utility.download.UpdateTask;
import java.io.File;
import java.util.Map;

/* compiled from: ProGuard */
final class d implements ValueCallback<UpdateTask> {
    final /* synthetic */ Map a;
    final /* synthetic */ File b;

    d(Map map, File file) {
        this.a = map;
        this.b = file;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        ValueCallback valueCallback;
        UpdateTask updateTask = (UpdateTask) obj;
        if (!(this.a == null || (valueCallback = (ValueCallback) this.a.get("exists")) == null)) {
            try {
                valueCallback.onReceiveValue((Object) null);
            } catch (Throwable th) {
            }
        }
        try {
            Thread.sleep(10000);
            if ((updateTask.getUpdateDir().getAbsolutePath() + WVNativeCallbackUtil.SEPERATER).equals(com.uc.webview.export.internal.d.s)) {
                UCCyclone.recursiveDelete(this.b, true, updateTask.getUpdateDir());
            }
        } catch (Exception e) {
        }
    }
}
