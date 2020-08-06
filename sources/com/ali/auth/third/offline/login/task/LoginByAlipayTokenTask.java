package com.ali.auth.third.offline.login.task;

import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.rpc.protocol.RpcException;
import com.ali.auth.third.core.task.AbsAsyncTask;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.core.util.ResourceUtils;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.login.util.LoginStatus;
import com.ali.auth.third.offline.model.ResultActionType;

public class LoginByAlipayTokenTask extends AbsAsyncTask<String, Void, Void> {
    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        String token = params[0];
        if (!CommonUtils.isNetworkAvailable()) {
            RpcResponse<String> result = new RpcResponse<>();
            result.code = -1;
            result.message = ResourceUtils.getString("com_taobao_tae_sdk_network_not_available_message");
            onFailure(result.code, result.message);
        } else {
            RpcResponse<LoginReturnData> resultData = null;
            try {
                LoginComponent loginComponent = LoginComponent.INSTANCE;
                resultData = LoginComponent.loginByAlipayLoginToken(token);
            } catch (RpcException e) {
            }
            if (resultData == null) {
                onFailure(-1, ResourceUtils.getString("aliusersdk_network_error"));
            } else {
                try {
                    if (resultData.code == 3000) {
                        LoginContext.credentialService.refreshWhenLogin((LoginReturnData) resultData.returnValue);
                        onSuccess();
                    } else if (ResultActionType.ALERT.equals(resultData.actionType)) {
                        onFailure(resultData.code, resultData.message);
                    } else if (ResultActionType.TOAST.equals(resultData.actionType)) {
                        onFailure(resultData.code, resultData.message);
                    } else {
                        onFailure(resultData.code, resultData.message);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
    }

    private void onSuccess() {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (CallbackContext.loginCallback != null) {
                    ((LoginCallback) CallbackContext.loginCallback).onSuccess(LoginContext.credentialService.getSession());
                }
                LoginStatus.resetLoginFlag();
            }
        });
    }

    private void onFailure(final int code, final String message) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (CallbackContext.loginCallback != null) {
                    ((LoginCallback) CallbackContext.loginCallback).onFailure(code, message);
                }
            }
        });
    }
}
