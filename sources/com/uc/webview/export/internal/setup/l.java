package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.taobao.windvane.service.WVEventId;
import android.util.Pair;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import com.uc.webview.export.internal.utility.c;
import com.uc.webview.export.utility.SetupTask;
import java.io.File;
import java.io.FilenameFilter;

/* compiled from: ProGuard */
public final class l extends u {
    public final void run() {
        File expectFile;
        String str;
        String str2;
        String str3;
        Throwable th;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8 = (String) getOption(UCCore.OPTION_UCM_ZIP_DIR);
        String str9 = (String) getOption(UCCore.OPTION_UCM_ZIP_FILE);
        boolean a = c.a(str8);
        boolean a2 = c.a(str9);
        if ((!a || !a2) && (a || a2)) {
            Context context = (Context) getOption("CONTEXT");
            if (a2) {
                expectFile = (File) UCMPackageInfo.invoke(UCMPackageInfo.getKernelFileIfMultiCoreFromDir, str8, context);
            } else {
                expectFile = UCCyclone.expectFile(str9);
            }
            if (expectFile == null) {
                throw new UCSetupException((int) WVEventId.WV_JSCALLBAK_SUCCESS, String.format("No kernel file found in dir [%s].", new Object[]{str8}));
            }
            File file = (File) UCMPackageInfo.invoke(UCMPackageInfo.expectCreateDirFile2P, (File) UCMPackageInfo.invoke(UCMPackageInfo.expectCreateDirFile2P, (File) UCMPackageInfo.invoke(10003, context), UCCyclone.getSourceHash(expectFile.getAbsolutePath())), UCCyclone.getSourceHash(expectFile.length(), expectFile.lastModified()));
            try {
                UCElapseTime uCElapseTime = new UCElapseTime();
                boolean decompressIfNeeded = UCCyclone.decompressIfNeeded(context, false, expectFile, file, (FilenameFilter) null, false);
                if (decompressIfNeeded) {
                    str3 = "0";
                    try {
                        str4 = String.valueOf(uCElapseTime.getMilisCpu());
                    } catch (UCSetupException e) {
                        e = e;
                        str4 = "0";
                    } catch (Throwable th2) {
                        str = "";
                        str2 = "0";
                        th = th2;
                        try {
                            callbackStat(new Pair(IWaStat.SEVENZIP, new m(this, str3, "0", str2, str)));
                        } catch (Throwable th3) {
                        }
                        throw th;
                    }
                    try {
                        str5 = String.valueOf(uCElapseTime.getMilis());
                        str6 = str4;
                        str7 = str3;
                    } catch (UCSetupException e2) {
                        e = e2;
                        str3 = "2";
                        try {
                            String valueOf = String.valueOf(e.errCode());
                            throw e;
                        } catch (Throwable th4) {
                            th = th4;
                            str = "";
                            str2 = str4;
                            callbackStat(new Pair(IWaStat.SEVENZIP, new m(this, str3, "0", str2, str)));
                            throw th;
                        }
                    }
                } else {
                    str5 = "0";
                    str6 = "0";
                    str7 = "1";
                }
                try {
                    callbackStat(new Pair(IWaStat.SEVENZIP, new m(this, str7, str5, str6, "")));
                } catch (Throwable th5) {
                }
                if (decompressIfNeeded || SetupTask.getTotalLoadedUCM() == null) {
                    Object[] objArr = {this.mCallbacks};
                    ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) new ao().invoke(10001, this)).setOptions(this.mOptions)).invoke(10002, objArr)).setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, file.getAbsolutePath())).onEvent("stop", new UCAsyncTask.c())).start();
                }
                this.mCallbacks = null;
            } catch (UCSetupException e3) {
                e = e3;
                str4 = "0";
                Object obj = "";
                str3 = "2";
                String valueOf2 = String.valueOf(e.errCode());
                throw e;
            } catch (Throwable th6) {
                str = "";
                str2 = "0";
                str3 = "";
                th = th6;
                callbackStat(new Pair(IWaStat.SEVENZIP, new m(this, str3, "0", str2, str)));
                throw th;
            }
        } else {
            throw new UCSetupException((int) WVEventId.WEBVIEW_LOADURL, String.format("Option [%s] or  [%s] expected.", new Object[]{UCCore.OPTION_UCM_ZIP_DIR, UCCore.OPTION_UCM_ZIP_FILE}));
        }
    }
}
