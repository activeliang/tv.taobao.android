package com.ali.user.open.tbauth.callback;

import com.ali.user.open.core.callback.FailureCallback;

public interface LogoutCallback extends FailureCallback {
    void onSuccess();
}
