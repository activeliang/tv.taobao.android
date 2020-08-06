package com.ali.auth.third.offline.login.task;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.task.TaskWithDialog;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.offline.LoginWebViewActivity;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.RequestCode;
import com.ali.auth.third.offline.login.context.LoginContext;

public abstract class AbsLoginByCodeTask extends TaskWithDialog<String, Void, Void> {
    private static final String TAG = "AbsLoginByCodeTask";

    /* access modifiers changed from: protected */
    public abstract void doWhenResultFail(int i, String str);

    /* access modifiers changed from: protected */
    public abstract void doWhenResultOk();

    /* access modifiers changed from: protected */
    public abstract RpcResponse<LoginReturnData> login(String[] strArr);

    public AbsLoginByCodeTask(Activity activity) {
        super(activity);
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        final RpcResponse<LoginReturnData> resultData = login(params);
        final int code = resultData.code;
        SDKLogger.d(TAG, "asyncExecute code = " + code);
        if (code == 3000) {
            try {
                if (resultData.returnValue != null) {
                    LoginContext.credentialService.refreshWhenLogin((LoginReturnData) resultData.returnValue);
                }
                KernelContext.executorService.postUITask(new Runnable() {
                    public void run() {
                        AbsLoginByCodeTask.this.doWhenResultOk();
                    }
                });
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (code == 13060) {
            String doubleCheckUrl = ((LoginReturnData) resultData.returnValue).h5Url;
            SDKLogger.d(TAG, "asyncExecute doubleCheckUrl = " + doubleCheckUrl);
            if (TextUtils.isEmpty(doubleCheckUrl)) {
                return null;
            }
            Activity startFrom = this.activity;
            if (startFrom instanceof LoginWebViewActivity) {
                LoginWebViewActivity loginWebViewActivity = (LoginWebViewActivity) startFrom;
                if (LoginWebViewActivity.originActivity != null) {
                    Activity startFrom2 = (LoginWebViewActivity) startFrom;
                    startFrom = LoginWebViewActivity.originActivity;
                }
            }
            CallbackContext.setActivity(startFrom);
            Intent intent = new Intent(startFrom, LoginWebViewActivity.class);
            intent.putExtra("url", doubleCheckUrl);
            intent.putExtra("token", ((LoginReturnData) resultData.returnValue).token);
            intent.putExtra("scene", ((LoginReturnData) resultData.returnValue).scene);
            LoginWebViewActivity.token = ((LoginReturnData) resultData.returnValue).token;
            LoginWebViewActivity.scene = ((LoginReturnData) resultData.returnValue).scene;
            this.activity.startActivityForResult(intent, RequestCode.OPEN_DOUBLE_CHECK);
            return null;
        } else {
            KernelContext.executorService.postUITask(new Runnable() {
                public void run() {
                    Message errorMessage = MessageUtils.createMessage(15, "login", "code " + code + " " + resultData.message);
                    SDKLogger.d(AbsLoginByCodeTask.TAG, errorMessage.toString());
                    AbsLoginByCodeTask.this.doWhenResultFail(errorMessage.code, errorMessage.message);
                }
            });
            return null;
        }
    }
}
