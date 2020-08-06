package com.uc.webview.export.utility;

import android.util.Pair;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCMPackageInfo;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.setup.UCSetupTask;

@Api
/* compiled from: ProGuard */
public abstract class SetupTask extends UCSetupTask<SetupTask, SetupTask> {
    public void startSync() {
        start();
        try {
            Thread.sleep(200);
        } catch (Throwable th) {
        }
        d.a(10029, new Object[0]);
    }

    public void callbackFinishStat(String str) {
        callbackStat(new Pair(IWaStat.SETUP_START_FINISH, new a(this, str)));
        callbackStat(new Pair(IWaStat.SETUP_START_FINISH, (Object) null));
    }

    public void setException(UCSetupException uCSetupException) {
        super.setException(uCSetupException);
        callStatException(IWaStat.SETUP_TOTAL_EXCEPTION, uCSetupException);
        callbackFinishStat("0");
        d.a((int) UCMPackageInfo.compareVersion, uCSetupException.toRunnable());
    }

    public void callStatException(String str, UCSetupException uCSetupException) {
        try {
            callbackStat(new Pair(str, new b(this, uCSetupException)));
        } catch (Throwable th) {
        }
    }

    public SetupTask setAsDefault() {
        d.y = this;
        return this;
    }
}
