package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.taobao.windvane.service.WVEventId;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.Build;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCDex;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import com.uc.webview.export.internal.setup.UCSubSetupTask;
import com.uc.webview.export.internal.utility.c;
import com.uc.webview.export.utility.SetupTask;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.io.File;

/* compiled from: ProGuard */
public final class b extends SetupTask {
    private static b a;
    /* access modifiers changed from: private */
    public u b;
    /* access modifiers changed from: private */
    public u c;
    private u d;
    /* access modifiers changed from: private */
    public Context e;
    /* access modifiers changed from: private */
    public File f = null;
    /* access modifiers changed from: private */
    public UCElapseTime g;
    /* access modifiers changed from: private */
    public long h;
    /* access modifiers changed from: private */
    public final ValueCallback<u> i = new c(this);
    /* access modifiers changed from: private */
    public final ValueCallback<u> j = new e(this);

    public static synchronized b a() {
        b bVar;
        synchronized (b.class) {
            if (a == null) {
                a = new b();
            }
            bVar = a;
        }
        return bVar;
    }

    public final void run() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        this.g = new UCElapseTime();
        this.h = SystemClock.currentThreadTimeMillis();
        this.e = (Context) getOption("CONTEXT");
        setupGlobalOnce(this.e);
        onEvent("stat", new f(this, (ValueCallback) invokeO(10007, "stat")));
        try {
            x xVar = new x();
            UCCyclone.statCallback = xVar;
            ((x) ((x) xVar.invoke(10001, SetupTask.getRoot())).onEvent("stat", new UCSubSetupTask.a())).start();
        } catch (Throwable th) {
        }
        try {
            new UCAsyncTask((Runnable) new UCDex()).invoke(10001, SetupTask.getRoot()).start(5000);
        } catch (Throwable th2) {
        }
        ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_UPD_URL, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null);
        if (getOption(UCCore.OPTION_SHARE_CORE) == null) {
            setup(UCCore.OPTION_SHARE_CORE, true);
        }
        String str6 = (String) getOption(UCCore.OPTION_UCM_ZIP_DIR);
        if (!c.a(str6) && (Build.PACK_TYPE == 43 || (Build.PACK_TYPE == 34 && d.h == 1 && Build.VERSION.SDK_INT >= 14))) {
            try {
                this.f = (File) UCMPackageInfo.invoke(UCMPackageInfo.getKernelFileIfMultiCoreFromDir, str6, this.e);
            } catch (Throwable th3) {
            }
        }
        StringBuilder sb = new StringBuilder();
        if (c.a((String) getOption(UCCore.OPTION_UCM_LIB_DIR))) {
            str = "LIB:N";
        } else {
            str = "LIB:Y";
        }
        StringBuilder append = sb.append(str).append(",");
        if (c.a((String) getOption(UCCore.OPTION_UCM_ZIP_DIR))) {
            str2 = "ZIP:N";
        } else {
            str2 = "ZIP:Y";
        }
        StringBuilder append2 = append.append(str2).append(",");
        if (c.a((Boolean) getOption(UCCore.OPTION_INIT_IN_SETUP_THREAD))) {
            str3 = "IIST:F";
        } else {
            str3 = "IIST:T";
        }
        StringBuilder append3 = append2.append(str3).append(",");
        if (c.a((Boolean) getOption(UCCore.OPTION_HARDWARE_ACCELERATED))) {
            str4 = "HA:F";
        } else {
            str4 = "HA:T";
        }
        StringBuilder append4 = append3.append(str4).append(",VP:").append(String.valueOf((Integer) getOption(UCCore.OPTION_VERIFY_POLICY))).append(",WP:").append(String.valueOf((Integer) getOption(UCCore.OPTION_WEBVIEW_POLICY))).append(",CD_LD:").append(String.valueOf(d.a(10005, "load"))).append(",CD_SOEK:").append(String.valueOf(d.a(10005, "skip_old_extra_kernel"))).append(",PT:").append(String.valueOf(com.uc.webview.export.Build.PACK_TYPE)).append(",KF:");
        if (this.f == null) {
            str5 = "N";
        } else {
            str5 = "Y";
        }
        callbackStat(new Pair(IWaStat.SETUP_START, new g(this, append4.append(str5).toString())));
        if (c.a((String) getOption(UCCore.OPTION_UCM_LIB_DIR))) {
            throw new UCSetupException((int) WVEventId.APP_ONCREATE, String.format("Option [%s] expected.", new Object[]{UCCore.OPTION_UCM_LIB_DIR}));
        }
        UCAsyncTask.a aVar = new UCAsyncTask.a();
        this.b = (u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) new ao().invoke(10001, this)).setOptions(this.mOptions)).setup(UCCore.OPTION_UCM_ZIP_DIR, (Object) null)).onEvent("stat", new UCSubSetupTask.a())).onEvent(BlitzServiceUtils.CSUCCESS, this.i)).onEvent("setup", aVar)).onEvent("load", aVar)).onEvent("init", aVar)).onEvent("switch", aVar)).onEvent("exception", new h(this));
        if (this.f != null) {
            this.c = (u) ((u) ((u) ((u) ((u) ((u) new l().setOptions(this.mOptions)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, this.f.getAbsolutePath())).onEvent("stat", new UCSubSetupTask.a())).onEvent("setup", new i(this));
            File file = (File) UCMPackageInfo.invoke(10003, this.e);
            if (file.list().length > 0) {
                ao aoVar = new ao();
                this.d = aoVar;
                ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar.invoke(10001, this)).setOptions(this.mOptions)).setup(UCCore.OPTION_UCM_ZIP_FILE, this.f.getAbsolutePath())).setup(UCCore.OPTION_UCM_ZIP_DIR, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, file.getAbsolutePath())).setup("chkDecFinish", true)).onEvent("stat", new UCSubSetupTask.a())).onEvent(BlitzServiceUtils.CSUCCESS, this.i)).onEvent("start", this.d.getSetupCrashImproverInst(file.getAbsolutePath()).d)).onEvent("die", this.d.getSetupCrashImproverInst(file.getAbsolutePath()).e)).onEvent("crash_none", (ValueCallback) null)).onEvent("crash_seen", (ValueCallback) null)).onEvent("crash_repeat", new k(this, file))).onEvent("exception", new j(this))).start();
                return;
            }
        }
        this.b.start();
    }
}
