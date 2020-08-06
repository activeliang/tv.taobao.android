package com.uc.webview.export.internal.setup;

import com.bftv.fui.constantplugin.Constant;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.c;
import java.util.HashMap;
import java.util.concurrent.Callable;

/* compiled from: ProGuard */
final class ab extends HashMap<String, String> {
    final /* synthetic */ boolean a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ String e;
    final /* synthetic */ String f;
    final /* synthetic */ aa g;

    ab(aa aaVar, boolean z, String str, String str2, String str3, String str4, String str5) {
        String str6;
        this.g = aaVar;
        this.a = z;
        this.b = str;
        this.c = str2;
        this.d = str3;
        this.e = str4;
        this.f = str5;
        put(IWaStat.KEY_CNT, "1");
        put("code", String.valueOf(UCSetupTask.getTotalLoadedUCM().coreType));
        put(IWaStat.KEY_DIR, UCSetupTask.getTotalLoadedUCM().ucmPackageInfo == null ? Constant.NULL : UCSetupTask.getTotalLoadedUCM().ucmPackageInfo.getDirAlias(this.g.a.c));
        put(IWaStat.KEY_OLD, UCSetupTask.getTotalLoadedUCM().isOldExtraKernel ? "T" : "F");
        put(IWaStat.KEY_FIRST_RUN, UCSetupTask.getTotalLoadedUCM().isFirstTimeOdex ? "T" : "F");
        put(IWaStat.KEY_CPU_CNT, c.a());
        put(IWaStat.KEY_CPU_FREQ, c.b());
        put(IWaStat.KEY_COST_CPU, String.valueOf(this.g.a.d.getMilisCpu()));
        put(IWaStat.KEY_COST, String.valueOf(this.g.a.d.getMilis()));
        Integer num = (Integer) this.g.a.getOption(UCCore.OPTION_SETUP_THREAD_PRIORITY);
        put(IWaStat.KEY_PRIORITY, num == null ? "n" : String.valueOf(num));
        try {
            Callable callable = (Callable) this.g.a.getOption(UCCore.OPTION_DOWNLOAD_CHECKER);
            str6 = callable == null ? "N" : ((Boolean) callable.call()).booleanValue() ? "T" : "F";
        } catch (Throwable th) {
            str6 = "E";
        }
        put("wifi", str6);
        if (this.a) {
            put(IWaStat.KEY_MULTI_CORE, d.j ? "1" : "0");
            put("err", this.b);
            put(IWaStat.KEY_CLASS, this.c);
            put("msg", this.d);
            put(IWaStat.KEY_CRASH, this.e);
            put(IWaStat.KEY_TASK, this.f);
        }
    }
}
