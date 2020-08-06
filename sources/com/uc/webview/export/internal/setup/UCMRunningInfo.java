package com.uc.webview.export.internal.setup;

import android.content.Context;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;

@Api
/* compiled from: ProGuard */
public class UCMRunningInfo {
    public final Context appCtx;
    public final ClassLoader classLoader;
    public final int coreType;
    public final boolean isFirstTimeOdex;
    public final boolean isOldExtraKernel;
    public final ClassLoader shellClassLoader;
    public final UCMobileWebKit ucMobileWebKit;
    public final UCMPackageInfo ucmPackageInfo;

    public UCMRunningInfo(Context context, UCMPackageInfo uCMPackageInfo, ClassLoader classLoader2, ClassLoader classLoader3, boolean z, boolean z2, UCMobileWebKit uCMobileWebKit, int i) {
        this.appCtx = context.getApplicationContext();
        this.ucmPackageInfo = uCMPackageInfo;
        this.classLoader = classLoader2;
        this.ucMobileWebKit = uCMobileWebKit;
        this.coreType = i;
        this.isOldExtraKernel = z;
        this.shellClassLoader = classLoader3;
        this.isFirstTimeOdex = z2;
    }
}
