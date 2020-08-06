package com.ali.user.open.core.callback;

public interface MemberCallback<RESULT> extends FailureCallback {
    void onSuccess(RESULT result);
}
