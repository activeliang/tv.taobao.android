package com.ali.user.open.oauth.alipay3;

import com.ali.user.open.core.callback.FailureCallback;

public interface GetSignCallback extends FailureCallback {
    void onGetSignSuccessed(String str);
}
