package com.ali.auth.third.offline.login.task;

import android.app.Activity;
import android.text.TextUtils;
import com.ali.auth.third.core.broadcast.LoginAction;
import com.ali.auth.third.core.callback.FailureCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.ali.auth.third.core.task.TaskWithDialog;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.callback.LogoutCallback;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.login.util.LoginStatus;

public class LogoutTask extends TaskWithDialog<Void, Void, Void> {
    /* access modifiers changed from: private */
    public LogoutCallback mLogoutCallback;

    public LogoutTask(Activity activity, LogoutCallback logoutCallback) {
        super(activity);
        this.mLogoutCallback = logoutCallback;
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(Void... params) {
        try {
            SDKLogger.e("logout task", "into logout " + CredentialManager.INSTANCE.getInternalSession().toString());
            if (!TextUtils.isEmpty(CredentialManager.INSTANCE.getInternalSession().user.userId)) {
                LoginComponent loginComponent = LoginComponent.INSTANCE;
                LoginComponent.logout();
            }
            LoginStatus.resetLoginFlag();
            ResultCode resultCode = LoginContext.credentialService.logout();
            if (ResultCode.SUCCESS.equals(resultCode)) {
                LoginContext.rpcService.logout();
                CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGOUT);
                KernelContext.executorService.postUITask(new Runnable() {
                    public void run() {
                        LogoutTask.this.mLogoutCallback.onSuccess();
                    }
                });
                return null;
            }
            CommonUtils.onFailure((FailureCallback) this.mLogoutCallback, resultCode);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            LoginStatus.resetLoginFlag();
            ResultCode resultCode2 = LoginContext.credentialService.logout();
            if (ResultCode.SUCCESS.equals(resultCode2)) {
                LoginContext.rpcService.logout();
                CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGOUT);
                KernelContext.executorService.postUITask(new Runnable() {
                    public void run() {
                        LogoutTask.this.mLogoutCallback.onSuccess();
                    }
                });
                return null;
            }
            CommonUtils.onFailure((FailureCallback) this.mLogoutCallback, resultCode2);
            return null;
        } catch (Throwable th) {
            LoginStatus.resetLoginFlag();
            ResultCode resultCode3 = LoginContext.credentialService.logout();
            if (ResultCode.SUCCESS.equals(resultCode3)) {
                LoginContext.rpcService.logout();
                CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGOUT);
                KernelContext.executorService.postUITask(new Runnable() {
                    public void run() {
                        LogoutTask.this.mLogoutCallback.onSuccess();
                    }
                });
            } else {
                CommonUtils.onFailure((FailureCallback) this.mLogoutCallback, resultCode3);
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        CommonUtils.onFailure((FailureCallback) this.mLogoutCallback, ResultCode.create(10010, t.getMessage()));
    }
}
