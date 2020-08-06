package com.ali.auth.third.offline.task;

import android.app.Activity;
import android.text.TextUtils;
import com.ali.auth.third.core.broadcast.LoginAction;
import com.ali.auth.third.core.callback.FailureCallback;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.offline.LoginActivity;
import com.ali.auth.third.offline.context.BridgeCallbackContext;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.UTConstants;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.login.task.AbsLoginByCodeTask;
import java.util.HashMap;
import java.util.Map;

public class LoginBySsoTokenTask extends AbsLoginByCodeTask {
    private static final String TAG = "login";
    private BridgeCallbackContext bridgeCallbackContext;
    private LoginCallback loginCallback;

    public LoginBySsoTokenTask(Activity activity, LoginCallback loginCallback2) {
        super(activity);
        this.loginCallback = loginCallback2;
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        final RpcResponse<LoginReturnData> resultData = login(params);
        if (resultData != null) {
            final int code = resultData.code;
            SDKLogger.d("login", "asyncExecute code = " + code);
            if (code == 3000) {
                try {
                    if (resultData.returnValue != null) {
                        LoginContext.credentialService.refreshWhenLogin((LoginReturnData) resultData.returnValue);
                    }
                    KernelContext.executorService.postUITask(new Runnable() {
                        public void run() {
                            LoginBySsoTokenTask.this.doWhenResultOk();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                KernelContext.executorService.postUITask(new Runnable() {
                    public void run() {
                        Message errorMessage = MessageUtils.createMessage(15, "login", "code " + code + " " + resultData.message);
                        SDKLogger.d("login", errorMessage.toString());
                        LoginBySsoTokenTask.this.doWhenResultFail(errorMessage.code, errorMessage.message);
                    }
                });
            }
        }
        return null;
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
        return LoginComponent.INSTANCE.loginBySsoToken(params[0]);
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
