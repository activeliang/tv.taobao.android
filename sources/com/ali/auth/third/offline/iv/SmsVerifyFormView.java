package com.ali.auth.third.offline.iv;

import com.ali.auth.third.core.model.RpcResponse;

public interface SmsVerifyFormView {
    String getPageName();

    void onSMSSendFail(RpcResponse rpcResponse);

    void onSendSMSSuccess(long j);

    void onVerifyFail(int i, String str);

    void onVerifySuccess(String str);
}
