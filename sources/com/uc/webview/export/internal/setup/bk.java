package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.utility.download.UpdateTask;
import java.io.File;
import java.util.concurrent.Callable;

/* compiled from: ProGuard */
final class bk implements ValueCallback<UpdateTask> {
    final /* synthetic */ Context a;
    final /* synthetic */ Callable b;
    final /* synthetic */ bd c;

    bk(bd bdVar, Context context, Callable callable) {
        this.c = bdVar;
        this.a = context;
        this.b = callable;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        String str;
        File file = null;
        try {
            File file2 = (File) UCMPackageInfo.invoke(10002, this.a);
            UCMRunningInfo totalLoadedUCM = UCSetupTask.getTotalLoadedUCM();
            if (!(totalLoadedUCM.ucmPackageInfo == null || (str = totalLoadedUCM.ucmPackageInfo.dataDir) == null)) {
                File file3 = new File(str);
                if (file3.getAbsolutePath().startsWith(file2.getAbsolutePath())) {
                    file = file3;
                }
            }
            if (file == null) {
                file = this.c.a.getUpdateDir();
            }
            UCCyclone.recursiveDelete(file2, true, file);
        } catch (Throwable th) {
        }
        try {
            if (this.b == null || ((Boolean) this.b.call()).booleanValue()) {
                this.c.callbackStat(new Pair(IWaStat.UCM_WIFI, (Object) null));
                return;
            }
            throw new RuntimeException("Update should be in wifi network.");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
