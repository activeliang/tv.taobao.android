package com.ali.auth.third.offline.login.task;

import android.app.Activity;
import android.webkit.WebView;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.support.WebViewActivitySupport;

public class RefreshPageAfterOpenTb extends AbsLoginByCodeTask {
    private WebView view;

    public RefreshPageAfterOpenTb(Activity activity, WebView view2) {
        super(activity);
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void doWhenResultFail(int code, String message) {
        CommonUtils.toastSystemException();
        if (CallbackContext.loginCallback != null) {
            ((LoginCallback) CallbackContext.loginCallback).onFailure(code, message);
        }
        if (CallbackContext.mGlobalLoginCallback != null) {
            CallbackContext.mGlobalLoginCallback.onFailure(code, message);
        }
    }

    /* access modifiers changed from: protected */
    public void doWhenResultOk() {
        if (this.view != null) {
            WebViewActivitySupport.getInstance().safeReload(this.view);
        }
        if (CallbackContext.loginCallback != null) {
            ((LoginCallback) CallbackContext.loginCallback).onSuccess(LoginContext.credentialService.getSession());
        }
        if (CallbackContext.mGlobalLoginCallback != null) {
            CallbackContext.mGlobalLoginCallback.onSuccess(LoginContext.credentialService.getSession());
        }
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        CommonUtils.toastSystemException();
    }

    /* access modifiers changed from: protected */
    public RpcResponse<LoginReturnData> login(String[] params) {
        return LoginComponent.INSTANCE.loginByCode(params[0]);
    }
}
