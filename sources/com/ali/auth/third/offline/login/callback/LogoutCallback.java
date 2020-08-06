package com.ali.auth.third.offline.login.callback;

import com.ali.auth.third.core.callback.FailureCallback;

public interface LogoutCallback extends FailureCallback {
    void onSuccess();
}
