package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.taobao.windvane.service.WVEventId;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.utility.c;
import java.io.File;
import java.io.FilenameFilter;

/* compiled from: ProGuard */
public final class y extends u {
    public final void run() {
        if (c.a((String) getOption(UCCore.OPTION_UCM_LIB_DIR))) {
            throw new UCSetupException((int) WVEventId.APP_ONCREATE, String.format("Option [%s] expected.", new Object[]{UCCore.OPTION_UCM_LIB_DIR}));
        }
        Context context = (Context) getOption("CONTEXT");
        File expectFile = UCCyclone.expectFile(context.getPackageResourcePath());
        File file = (File) UCMPackageInfo.invoke(UCMPackageInfo.expectCreateDirFile2P, (File) UCMPackageInfo.invoke(UCMPackageInfo.expectCreateDirFile2P, (File) UCMPackageInfo.invoke(10006, context), UCCyclone.getSourceHash(expectFile.getAbsolutePath())), UCCyclone.getSourceHash(expectFile.length(), expectFile.lastModified()));
        UCCyclone.decompressIfNeeded(context, false, expectFile, file, (FilenameFilter) UCMPackageInfo.invoke(UCMPackageInfo.getLibFilter, new Object[0]), false);
        Object[] objArr = {this.mCallbacks};
        ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) ((u) new ao().invoke(10001, this)).setOptions(this.mOptions)).invoke(10002, objArr)).setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, file.getAbsolutePath() + "/lib")).start();
        this.mCallbacks = null;
    }
}
