package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.util.Pair;
import android.webkit.ValueCallback;
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
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Callable;

/* compiled from: ProGuard */
public final class z extends SetupTask {
    /* access modifiers changed from: private */
    public static Stack<UCSetupTask> a = new Stack<>();
    /* access modifiers changed from: private */
    public u b;
    /* access modifiers changed from: private */
    public Context c;
    /* access modifiers changed from: private */
    public UCElapseTime d;
    /* access modifiers changed from: private */
    public UCSetupException e;
    /* access modifiers changed from: private */
    public UCSetupTask f;
    private ValueCallback<u> g;
    private ValueCallback<u> h;
    /* access modifiers changed from: private */
    public List<ag> i;
    /* access modifiers changed from: private */
    public final ValueCallback<u> j = new aa(this);
    /* access modifiers changed from: private */
    public final ValueCallback<u> k = new ac(this);

    public final void a(String str, Callable<Boolean> callable) {
        this.b = (u) ((u) ((u) ((u) ((u) ((u) ((u) new bd().invoke(10001, UCSetupTask.getRoot())).setOptions(this.mOptions)).setup(UCCore.OPTION_UCM_ZIP_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_USE_SDK_SETUP, true)).setup("chkMultiCore", true)).onEvent("stat", this.g != null ? this.g : new UCSubSetupTask.a());
        if (callable != null) {
            this.b.setup(UCCore.OPTION_DOWNLOAD_CHECKER, callable);
        }
        if (!c.a(str)) {
            this.b.setup(UCCore.OPTION_UCM_UPD_URL, str);
        }
    }

    private u c() {
        u uVar;
        ValueCallback aVar;
        ad adVar = new ad(this);
        String str = (String) this.mOptions.get(UCCore.OPTION_DEX_FILE_PATH);
        if (!c.a(str)) {
            ao aoVar = new ao();
            ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
            ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar.setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).onEvent("start", aoVar.getSetupCrashImproverInst(str).d)).onEvent("die", aoVar.getSetupCrashImproverInst(str).e)).onEvent("crash_none", (ValueCallback) null)).onEvent("crash_seen", (ValueCallback) null)).onEvent("crash_repeat", adVar);
            uVar = (u) ((u) ((u) aoVar.setup(UCCore.OPTION_DEX_FILE_PATH, str)).setup(UCCore.OPTION_SO_FILE_PATH, getOption(UCCore.OPTION_SO_FILE_PATH))).setup(UCCore.OPTION_RES_FILE_PATH, getOption(UCCore.OPTION_RES_FILE_PATH));
        } else {
            String str2 = (String) this.mOptions.get(UCCore.OPTION_UCM_ZIP_FILE);
            if (!c.a(str2)) {
                l lVar = new l();
                ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) lVar.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
                ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) lVar.setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).onEvent("start", lVar.getSetupCrashImproverInst(str2).d)).onEvent("die", lVar.getSetupCrashImproverInst(str2).e)).onEvent("crash_none", (ValueCallback) null)).onEvent("crash_seen", (ValueCallback) null)).onEvent("crash_repeat", adVar);
                uVar = (u) lVar.setup(UCCore.OPTION_UCM_ZIP_FILE, str2);
            } else {
                String str3 = (String) this.mOptions.get(UCCore.OPTION_UCM_LIB_DIR);
                if (!c.a(str3)) {
                    ao aoVar2 = new ao();
                    ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar2.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
                    ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar2.setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).onEvent("start", aoVar2.getSetupCrashImproverInst(str3).d)).onEvent("die", aoVar2.getSetupCrashImproverInst(str3).e)).onEvent("crash_none", (ValueCallback) null)).onEvent("crash_seen", (ValueCallback) null)).onEvent("crash_repeat", adVar);
                    uVar = (u) aoVar2.setup(UCCore.OPTION_UCM_LIB_DIR, str3);
                } else {
                    String str4 = (String) this.mOptions.get(UCCore.OPTION_UCM_KRL_DIR);
                    if (!c.a(str4)) {
                        ao aoVar3 = new ao();
                        ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar3.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
                        ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar3.setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).onEvent("start", aoVar3.getSetupCrashImproverInst(str4).d)).onEvent("die", aoVar3.getSetupCrashImproverInst(str4).e)).onEvent("crash_none", (ValueCallback) null)).onEvent("crash_seen", (ValueCallback) null)).onEvent("crash_repeat", adVar);
                        uVar = (u) aoVar3.setup(UCCore.OPTION_UCM_KRL_DIR, str4);
                    } else {
                        String str5 = (String) this.mOptions.get(UCCore.OPTION_UCM_CFG_FILE);
                        if (!c.a(str5)) {
                            ao aoVar4 = new ao();
                            ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar4.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
                            ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar4.setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).onEvent("start", aoVar4.getSetupCrashImproverInst(str5).d)).onEvent("die", aoVar4.getSetupCrashImproverInst(str5).e)).onEvent("crash_none", (ValueCallback) null)).onEvent("crash_seen", (ValueCallback) null)).onEvent("crash_repeat", adVar);
                            uVar = (u) aoVar4.setup(UCCore.OPTION_UCM_CFG_FILE, str5);
                        } else {
                            uVar = null;
                        }
                    }
                }
            }
        }
        if (!c.a((String) getOption(UCCore.OPTION_UCM_UPD_URL))) {
            u uVar2 = (u) ((u) ((u) ((u) ((u) ((u) new bd().invoke(10001, UCSetupTask.getRoot())).setOptions(this.mOptions)).setup(UCCore.OPTION_UCM_ZIP_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_USE_SDK_SETUP, true)).setup("chkMultiCore", true);
            if (this.g != null) {
                aVar = this.g;
            } else {
                aVar = new UCSubSetupTask.a();
            }
            this.b = (u) uVar2.onEvent("stat", aVar);
            if (!c.a((String) null)) {
                this.b.setup(UCCore.OPTION_UCM_UPD_URL, (Object) null);
            }
            File file = (File) UCMPackageInfo.invoke(10002, this.c);
            if (file.list().length > 0) {
                if (uVar != null) {
                    a.push(uVar);
                }
                String absolutePath = file.getAbsolutePath();
                ao aoVar5 = new ao();
                ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar5.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
                ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) aoVar5.setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).onEvent("start", aoVar5.getSetupCrashImproverInst(absolutePath).d)).onEvent("die", aoVar5.getSetupCrashImproverInst(absolutePath).e)).onEvent("crash_none", (ValueCallback) null)).onEvent("crash_seen", (ValueCallback) null)).onEvent("crash_repeat", adVar);
                return (u) ((u) aoVar5.setup("chkDecFinish", true)).setup(UCCore.OPTION_UCM_KRL_DIR, absolutePath);
            }
        } else if (uVar == null) {
            throw new UCSetupException(3017, "At least 1 of OPTION_DEX_FILE_PATH|OPTION_UCM_LIB_DIR|OPTION_UCM_KRL_DIR|OPTION_UCM_CFG_FILE|OPTION_UCM_UPD_URL should be given.");
        }
        if (uVar == null) {
            uVar = new ao();
            ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) uVar.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
            ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) uVar.setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).onEvent("start", uVar.getSetupCrashImproverInst("").d)).onEvent("die", uVar.getSetupCrashImproverInst("").e)).onEvent("crash_none", (ValueCallback) null)).onEvent("crash_seen", (ValueCallback) null)).onEvent("crash_repeat", adVar);
        }
        return uVar;
    }

    public final void run() {
        this.d = new UCElapseTime();
        ((SetupTask) setup(UCCore.OPTION_UCM_ZIP_DIR, (Object) null)).setup(UCCore.OPTION_USE_SDK_SETUP, true);
        this.c = (Context) getOption("CONTEXT");
        setupGlobalOnce(this.c);
        onEvent("stat", new ae(this, (ValueCallback) invokeO(10007, "stat")));
        callbackStat(new Pair(IWaStat.SETUP_START, (Object) null));
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
        this.h = new UCAsyncTask.a();
        this.g = new UCSubSetupTask.a();
        ak akVar = new ak();
        ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) akVar.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
        if (!c.a(c.a((HashMap<String, Object>) this.mOptions, UCCore.OPTION_USE_SYSTEM_WEBVIEW))) {
            akVar.start();
            return;
        }
        a.push(akVar);
        if (((Boolean) UCMPackageInfo.invoke(10011, new Object[0])).booleanValue()) {
            am amVar = new am();
            ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) amVar.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
            amVar.start();
            d.a("Thick SDK");
            return;
        }
        String str = (String) this.mOptions.get(UCCore.OPTION_LOAD_POLICY);
        if (str == null) {
            str = UCCore.LOAD_POLICY_UCMOBILE_OR_SPECIFIED;
        }
        if (UCCore.LOAD_POLICY_UCMOBILE_ONLY.equals(str)) {
            ax axVar = new ax();
            ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) axVar.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
            axVar.start();
        } else if (UCCore.LOAD_POLICY_SPECIFIED_ONLY.equals(str)) {
            c().start();
        } else if (UCCore.LOAD_POLICY_SPECIFIED_OR_UCMOBILE.equals(str)) {
            Stack<UCSetupTask> stack = a;
            ax axVar2 = new ax();
            ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) axVar2.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
            stack.push(axVar2);
            c().start();
        } else {
            a.push(c());
            ax axVar3 = new ax();
            ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) axVar3.invoke(10001, this)).setOptions(this.mOptions)).onEvent("setup", this.h)).onEvent("load", this.h)).onEvent("init", this.h)).onEvent("switch", this.h)).onEvent("stat", this.g)).onEvent(BlitzServiceUtils.CSUCCESS, this.j)).onEvent("exception", this.k);
            axVar3.start();
        }
    }
}
