package com.ali.auth.third.offline.login.task;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ali.auth.third.core.task.AbsAsyncTask;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.core.util.ResourceUtils;
import com.ali.auth.third.offline.LoginWebViewActivity;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.RequestCode;
import com.ali.auth.third.offline.login.UTConstants;
import com.ali.auth.third.offline.login.util.ActivityUIHelper;
import com.ali.auth.third.offline.login.util.LoginStatus;
import com.ali.auth.third.offline.login.util.SqliteHelper;
import com.ali.auth.third.offline.model.ResultActionType;
import java.util.HashMap;
import java.util.Map;

public class LoginByIVTokenTask extends AbsAsyncTask<String, Void, Void> {
    private static final String TAG = "login";
    /* access modifiers changed from: private */
    public Activity mActivity;
    private ActivityUIHelper mActivityHelper;
    /* access modifiers changed from: private */
    public LoginCallback mLoginCallback;

    public LoginByIVTokenTask(Activity activity, LoginCallback loginCallback) {
        this.mActivity = activity;
        this.mActivityHelper = new ActivityUIHelper(activity);
        this.mLoginCallback = loginCallback;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        try {
            this.mActivityHelper.showProgress("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        if (!CommonUtils.isNetworkAvailable()) {
            RpcResponse<String> result = new RpcResponse<>();
            result.code = -1;
            result.message = ResourceUtils.getString("com_taobao_tae_sdk_network_not_available_message");
            doWhenResultFail(result.code, result.message);
        } else {
            RpcResponse<LoginReturnData> loginReturnData = LoginComponent.INSTANCE.loginByIVToken(params[0], params[1], params[2]);
            if (loginReturnData == null || loginReturnData.returnValue == null) {
                doWhenResultFail(ResultCode.SYSTEM_EXCEPTION.code, ResultCode.SYSTEM_EXCEPTION.message);
            } else if (loginReturnData.code == 3000) {
                KernelContext.credentialService.refreshWhenLogin((LoginReturnData) loginReturnData.returnValue);
                if (params.length >= 5 && !TextUtils.isEmpty(params[3]) && !TextUtils.isEmpty(params[4])) {
                    SqliteHelper.getInstance().saveToSqlite(params[3], params[4]);
                }
                doWhenResultOk();
            } else if (ResultActionType.H5.equals(loginReturnData.actionType)) {
                String url = ((LoginReturnData) loginReturnData.returnValue).h5Url;
                if (!TextUtils.isEmpty(url)) {
                    Intent intent = new Intent(this.mActivity, LoginWebViewActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("token", ((LoginReturnData) loginReturnData.returnValue).token);
                    intent.putExtra("scene", ((LoginReturnData) loginReturnData.returnValue).scene);
                    this.mActivity.startActivityForResult(intent, RequestCode.OPEN_DOUBLE_CHECK);
                } else {
                    doWhenResultFail(loginReturnData.code, loginReturnData.message);
                }
            } else {
                doWhenResultFail(loginReturnData.code, loginReturnData.message);
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        Message errorMessage = MessageUtils.createMessage(10010, t.getMessage());
        SDKLogger.log("login", errorMessage, t);
        Map<String, String> json = new HashMap<>();
        json.put("code", "10010");
        json.put("message", "exception");
        ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_H5_LOGIN_FAILURE, json);
        doWhenResultFail(errorMessage.code, errorMessage.message);
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
        try {
            this.mActivityHelper.dismissProgressDialog();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void doWhenResultFail(final int code, final String message) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                Map<String, String> json = new HashMap<>();
                json.put("code", code + "");
                if (!TextUtils.isEmpty(message)) {
                    json.put("message", message);
                }
                ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_H5_LOGIN_FAILURE, json);
                if (LoginByIVTokenTask.this.mLoginCallback != null) {
                    LoginByIVTokenTask.this.mLoginCallback.onFailure(code, message);
                }
            }
        });
    }

    private void doWhenResultOk() {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (LoginByIVTokenTask.this.mLoginCallback != null) {
                    LoginByIVTokenTask.this.mLoginCallback.onSuccess(KernelContext.credentialService.getSession());
                }
                if (LoginByIVTokenTask.this.mActivity != null) {
                    LoginByIVTokenTask.this.mActivity.finish();
                }
                LoginStatus.resetLoginFlag();
            }
        });
    }
}
