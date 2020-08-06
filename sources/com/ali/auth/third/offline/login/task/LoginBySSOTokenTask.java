package com.ali.auth.third.offline.login.task;

import android.app.Activity;
import android.text.TextUtils;
import com.ali.auth.third.core.callback.FailureCallback;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.login.util.LoginStatus;
import java.util.HashMap;
import java.util.Map;

public class LoginBySSOTokenTask extends AbsLoginByCodeTask {
    private LoginCallback mLoginCallback;

    public LoginBySSOTokenTask(Activity activity, LoginCallback loginCallback) {
        super(activity);
        this.mLoginCallback = loginCallback;
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        LoginStatus.resetLoginFlag();
        Map<String, String> json = new HashMap<>();
        json.put("code", "10010");
        json.put("message", "exception");
        CommonUtils.onFailure((FailureCallback) this.mLoginCallback, ResultCode.create(10010, t.getMessage()));
    }

    /* access modifiers changed from: protected */
    public void doWhenResultFail(int code, String message) {
        if (this.mLoginCallback != null) {
            Map<String, String> json = new HashMap<>();
            json.put("code", code + "");
            if (!TextUtils.isEmpty(message)) {
                json.put("message", message);
            }
            ResultCode logout = LoginContext.credentialService.logout();
            if (this.mLoginCallback != null) {
                this.mLoginCallback.onFailure(code, message);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doWhenResultOk() {
        LoginStatus.resetLoginFlag();
        if (this.mLoginCallback != null) {
            this.mLoginCallback.onSuccess(LoginContext.credentialService.getSession());
        }
    }

    /* access modifiers changed from: protected */
    public RpcResponse<LoginReturnData> login(String[] params) {
        LoginComponent loginComponent = LoginComponent.INSTANCE;
        return LoginComponent.loginBySSOToken();
    }
}
