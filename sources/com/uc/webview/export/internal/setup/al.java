package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.taobao.windvane.service.WVEventId;
import android.util.Pair;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import java.util.HashMap;
import java.util.List;

/* compiled from: ProGuard */
public final class al extends o {
    public final void run() {
        long j;
        long j2;
        setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null);
        Context context = (Context) this.mOptions.get("CONTEXT");
        List<UCMPackageInfo> a = UCMPackageInfo.a(context, (HashMap<String, Object>) this.mOptions);
        if (a.size() != 1) {
            throw new UCSetupException((int) WVEventId.WV_COMMON_CONFIG_UPDATE_DONE, "1 UCMPackage expected for thick mode.");
        }
        Integer num = (Integer) this.mOptions.get(UCCore.OPTION_VERIFY_POLICY);
        UCMPackageInfo uCMPackageInfo = a.get(0);
        if (num == null || (num.intValue() & 8) == 0) {
            j = 0;
            j2 = 0;
        } else {
            UCElapseTime uCElapseTime = new UCElapseTime();
            b(uCMPackageInfo, context, UCMPackageInfo.class.getClassLoader());
            j = 0 + uCElapseTime.getMilisCpu();
            j2 = 0 + uCElapseTime.getMilis();
        }
        try {
            callbackStat(new Pair(IWaStat.SETUP_TASK_VERIFY, new p(this, num, false, "thick", j2, j)));
        } catch (Throwable th) {
        }
        this.mUCM = uCMPackageInfo;
        this.mCL = UCMPackageInfo.class.getClassLoader();
        this.a = false;
        this.b = false;
        callbackStat(new Pair(IWaStat.SETUP_SUCCESS_SETUPED, (Object) null));
    }
}
