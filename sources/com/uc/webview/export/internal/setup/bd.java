package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.taobao.windvane.service.WVEventId;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import com.uc.webview.export.internal.utility.c;
import com.uc.webview.export.utility.SetupTask;
import com.uc.webview.export.utility.download.UpdateTask;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.io.File;
import java.util.concurrent.Callable;

/* compiled from: ProGuard */
public final class bd extends u {
    /* access modifiers changed from: private */
    public UpdateTask a = null;

    public final void run() {
        Long valueOf;
        Long valueOf2;
        Pair<Integer, Object> a2;
        be beVar;
        Context context = (Context) getOption("CONTEXT");
        String str = (String) getOption(UCCore.OPTION_UCM_UPD_URL);
        Boolean bool = (Boolean) getOption("chkMultiCore");
        Callable callable = (Callable) getOption(UCCore.OPTION_DOWNLOAD_CHECKER);
        if (c.a(str)) {
            throw new UCSetupException((int) WVEventId.WV_CONTACT_AUTH_STATUS_EVENT, String.format("Option [%s] expected.", new Object[]{UCCore.OPTION_UCM_UPD_URL}));
        }
        ap apVar = new ap();
        Object option = getOption(UCCore.OPTION_UPD_SETUP_TASK_WAIT_MILIS);
        Long valueOf3 = Long.valueOf(option == null ? 7200000 : option instanceof Long ? ((Long) option).longValue() : option instanceof Integer ? ((Integer) option).longValue() : Long.parseLong(String.valueOf(option)));
        synchronized (apVar) {
            File file = (File) UCMPackageInfo.invoke(10002, context);
            Object option2 = getOption(UCCore.OPTION_DWN_RETRY_WAIT_MILIS);
            if (option2 == null) {
                valueOf = null;
            } else {
                valueOf = Long.valueOf(option2 instanceof Long ? ((Long) option2).longValue() : option2 instanceof Integer ? ((Integer) option2).longValue() : Long.parseLong(String.valueOf(option2)));
            }
            Object option3 = getOption(UCCore.OPTION_DWN_RETRY_MAX_WAIT_MILIS);
            if (option3 == null) {
                valueOf2 = null;
            } else {
                valueOf2 = Long.valueOf(option3 instanceof Long ? ((Long) option3).longValue() : option3 instanceof Integer ? ((Integer) option3).longValue() : Long.parseLong(String.valueOf(option3)));
            }
            this.a = new UpdateTask(context, str, file.getAbsolutePath(), "core.jar", (ValueCallback<Object[]>) null, valueOf, valueOf2);
            this.a.onEvent("check", new bk(this, context, callable)).onEvent("exception", new bi(this)).onEvent(BlitzServiceUtils.CSUCCESS, new bh(this, apVar)).onEvent("failed", new bg(this, apVar)).onEvent("exists", new bf(this, apVar)).start();
            a2 = apVar.a(valueOf3.longValue());
        }
        if (((Integer) a2.first).intValue() == 1) {
            throw new UCSetupException(4010, String.format("Thread [%s] waitting for update is up to [%s] milis.", new Object[]{Thread.currentThread().getName(), String.valueOf(valueOf3)}));
        } else if (((Integer) a2.first).intValue() == 3) {
            throw new UCSetupException(4019, (Throwable) (Exception) a2.second);
        } else {
            boolean z = ((Integer) a2.first).intValue() == 0;
            boolean z2 = z || ((Integer) a2.first).intValue() == 4;
            File updateDir = this.a.getUpdateDir();
            if (!z2) {
                return;
            }
            if (z || SetupTask.getTotalLoadedUCM() == null) {
                u uVar = (u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) new ao().invoke(10001, this)).setOptions(this.mOptions)).invoke(10002, this.mCallbacks)).setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, updateDir.getAbsolutePath())).onEvent("stop", new UCAsyncTask.c());
                if (c.a(bool)) {
                    beVar = null;
                } else {
                    beVar = new be(this);
                }
                ((u) uVar.onEvent("setup", beVar)).start();
            }
        }
    }
}
