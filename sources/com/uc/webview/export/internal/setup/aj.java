package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.ali.auth.third.core.model.KernelMessageConstants;
import com.uc.webview.export.internal.d;

/* compiled from: ProGuard */
final class aj implements ValueCallback<q> {
    final /* synthetic */ ai a;

    aj(ai aiVar) {
        this.a = aiVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        try {
            this.a.b.a.setLoadedUCM(new UCMRunningInfo(this.a.b.a.getContext(), this.a.a.mUCM, this.a.a.mCL, this.a.a.mShellCL, this.a.a.a, this.a.a.b, d.d, ((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue()));
            this.a.b.a.callback("init");
            this.a.b.a.callback("switch");
        } catch (UCSetupException e) {
            this.a.b.a.setException(e);
        } catch (Throwable th) {
            this.a.b.a.setException(new UCSetupException(4018, th));
        }
    }
}
