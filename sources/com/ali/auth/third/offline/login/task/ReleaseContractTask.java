package com.ali.auth.third.offline.login.task;

import com.ali.auth.third.core.callback.CommonCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.task.AbsAsyncTask;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.core.util.ResourceUtils;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.util.CallbackHelper;
import com.ali.auth.third.offline.model.ContractModel;

public class ReleaseContractTask extends AbsAsyncTask<ContractModel, Void, Void> {
    public static final String TAG = "login.ReleaseContractTask";
    public CommonCallback mCommonCallback;

    public ReleaseContractTask(CommonCallback callback) {
        this.mCommonCallback = callback;
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(ContractModel... params) {
        if (!CommonUtils.isNetworkAvailable()) {
            RpcResponse<String> result = new RpcResponse<>();
            result.code = -1;
            result.message = ResourceUtils.getString("com_taobao_tae_sdk_network_not_available_message");
            onFailure(result.code, result.message);
        } else {
            ContractModel model = params[0];
            LoginComponent loginComponent = LoginComponent.INSTANCE;
            RpcResponse<String> response = LoginComponent.releaseContract(model);
            if (response != null && response.success) {
                onSuccess();
            } else if (response != null) {
                onFailure(response.code, response.message);
            } else {
                Message errorMessage = MessageUtils.createMessage(10010, new Object[0]);
                SDKLogger.d(TAG, errorMessage.toString());
                onFailure(errorMessage.code, errorMessage.message);
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        Message errorMessage = MessageUtils.createMessage(10010, t.getMessage());
        SDKLogger.d(TAG, errorMessage.toString());
        onFailure(errorMessage.code, errorMessage.message);
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
    }

    private void onFailure(int code, String message) {
        CallbackHelper.onFailure(code, message, this.mCommonCallback);
    }

    private void onSuccess() {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (ReleaseContractTask.this.mCommonCallback != null) {
                    ReleaseContractTask.this.mCommonCallback.onSuccess();
                }
            }
        });
    }
}
