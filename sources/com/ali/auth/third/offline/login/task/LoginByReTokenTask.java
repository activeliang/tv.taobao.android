package com.ali.auth.third.offline.login.task;

import android.app.Activity;
import android.text.TextUtils;
import com.ali.auth.third.core.callback.FailureCallback;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.UTConstants;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.login.util.LoginStatus;
import java.util.HashMap;
import java.util.Map;

public class LoginByReTokenTask extends AbsLoginByCodeTask {
    private LoginCallback loginCallback;

    public LoginByReTokenTask(Activity activity, LoginCallback loginCallback2) {
        super(activity);
        this.loginCallback = loginCallback2;
    }

    /* access modifiers changed from: protected */
    public void doWhenResultFail(int code, String message) {
        if (this.loginCallback != null) {
            Map<String, String> json = new HashMap<>();
            json.put("code", code + "");
            if (!TextUtils.isEmpty(message)) {
                json.put("message", message);
            }
            ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_AUTO_LOGIN_FAILURE, json);
            ResultCode logout = LoginContext.credentialService.logout();
            if (this.loginCallback != null) {
                this.loginCallback.onFailure(code, message);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doWhenResultOk() {
        LoginStatus.resetLoginFlag();
        if (this.loginCallback != null) {
            ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_AUTO_LOGIN_SUCCESS, (Map<String, String>) null);
            this.loginCallback.onSuccess(LoginContext.credentialService.getSession());
        }
    }

    /* access modifiers changed from: protected */
    public RpcResponse<LoginReturnData> login(String[] params) {
        LoginComponent loginComponent = LoginComponent.INSTANCE;
        return LoginComponent.loginByRefreshToken();
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        LoginStatus.resetLoginFlag();
        Map<String, String> json = new HashMap<>();
        json.put("code", "10010");
        json.put("message", "exception");
        ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_AUTO_LOGIN_FAILURE, json);
        CommonUtils.onFailure((FailureCallback) this.loginCallback, ResultCode.create(10010, t.getMessage()));
    }
}
