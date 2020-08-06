package com.uc.webview.export.internal.setup;

import android.os.SystemClock;
import com.bftv.fui.constantplugin.Constant;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.c;
import java.util.HashMap;
import java.util.concurrent.Callable;

/* compiled from: ProGuard */
final class d extends HashMap<String, String> {
    final /* synthetic */ c a;

    d(c cVar) {
        String str;
        this.a = cVar;
        put(IWaStat.KEY_CNT, "1");
        put("code", String.valueOf(UCSetupTask.getTotalLoadedUCM().coreType));
        put(IWaStat.KEY_DIR, UCSetupTask.getTotalLoadedUCM().ucmPackageInfo == null ? Constant.NULL : UCSetupTask.getTotalLoadedUCM().ucmPackageInfo.getDirAlias(this.a.a.e));
        put(IWaStat.KEY_OLD, UCSetupTask.getTotalLoadedUCM().isOldExtraKernel ? "T" : "F");
        put(IWaStat.KEY_FIRST_RUN, UCSetupTask.getTotalLoadedUCM().isFirstTimeOdex ? "T" : "F");
        put(IWaStat.KEY_CPU_CNT, c.a());
        put(IWaStat.KEY_CPU_FREQ, c.b());
        put(IWaStat.KEY_COST_CPU, String.valueOf(SystemClock.currentThreadTimeMillis() - this.a.a.h));
        put(IWaStat.KEY_COST, String.valueOf(this.a.a.g.getMilis()));
        Integer num = (Integer) this.a.a.getOption(UCCore.OPTION_SETUP_THREAD_PRIORITY);
        put(IWaStat.KEY_PRIORITY, num == null ? "n" : String.valueOf(num));
        try {
            Callable callable = (Callable) this.a.a.getOption(UCCore.OPTION_DOWNLOAD_CHECKER);
            str = callable == null ? "N" : ((Boolean) callable.call()).booleanValue() ? "T" : "F";
        } catch (Throwable th) {
            str = "E";
        }
        put("wifi", str);
    }
}
