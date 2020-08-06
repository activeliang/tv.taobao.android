package com.ali.auth.third.offline.login.task;

import android.app.Activity;
import android.text.TextUtils;
import com.ali.auth.third.core.broadcast.LoginAction;
import com.ali.auth.third.core.callback.FailureCallback;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.offline.LoginActivity;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.UTConstants;
import com.ali.auth.third.offline.login.context.LoginContext;
import java.util.HashMap;
import java.util.Map;

public class LoginAfterOpenTb extends AbsLoginByCodeTask {
    private LoginCallback loginCallback;

    public LoginAfterOpenTb(Activity activity, LoginCallback loginCallback2) {
        super(activity);
        this.loginCallback = loginCallback2;
    }

    /* access modifiers changed from: protected */
    public void doWhenResultFail(int code, String message) {
        if (this.loginCallback != null) {
            this.loginCallback.onFailure(code, message);
            Map<String, String> json = new HashMap<>();
            json.put("code", code + "");
            if (!TextUtils.isEmpty(message)) {
                json.put("message", message);
            }
            ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_TB_LOGIN_FAILURE, json);
        }
        if (CallbackContext.mGlobalLoginCallback != null) {
            CallbackContext.mGlobalLoginCallback.onFailure(code, message);
        }
        CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED);
        if (this.activity != null && (this.activity instanceof LoginActivity)) {
            CallbackContext.activity = null;
            this.activity.finish();
        }
    }

    /* access modifiers changed from: protected */
    public void doWhenResultOk() {
        if (this.loginCallback != null) {
            this.loginCallback.onSuccess(LoginContext.credentialService.getSession());
            ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_TB_LOGIN_SUCCESS, (Map<String, String>) null);
        }
        if (CallbackContext.mGlobalLoginCallback != null) {
            CallbackContext.mGlobalLoginCallback.onSuccess(LoginContext.credentialService.getSession());
        }
        CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGIN_SUCCESS);
        if (this.activity != null && (this.activity instanceof LoginActivity)) {
            CallbackContext.activity = null;
            this.activity.finish();
        }
    }

    /* access modifiers changed from: protected */
    public RpcResponse<LoginReturnData> login(String[] params) {
        return LoginComponent.INSTANCE.loginByCode(params[0]);
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED);
        Map<String, String> json = new HashMap<>();
        json.put("code", "10010");
        json.put("message", "exception");
        ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_TB_LOGIN_FAILURE, json);
        CommonUtils.onFailure((FailureCallback) this.loginCallback, ResultCode.create(10010, t.getMessage()));
    }
}
