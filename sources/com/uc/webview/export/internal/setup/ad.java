package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;

/* compiled from: ProGuard */
final class ad implements ValueCallback<u> {
    final /* synthetic */ z a;

    ad(z zVar) {
        this.a = zVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        u uVar = (u) obj;
        uVar.setException(new UCSetupException(4005, String.format("Multi crash detected in [%s|%s|%s|%s].", new Object[]{uVar.getOption(UCCore.OPTION_DEX_FILE_PATH), uVar.getOption(UCCore.OPTION_UCM_LIB_DIR), uVar.getOption(UCCore.OPTION_UCM_KRL_DIR), uVar.getOption(UCCore.OPTION_UCM_CFG_FILE)})));
        uVar.onEvent("die", (ValueCallback) null);
    }
}
