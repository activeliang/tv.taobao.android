package com.ali.user.open.ucc;

import com.ali.user.open.core.callback.MemberCallback;

public interface UccDataProvider {
    void getUserToken(String str, MemberCallback<String> memberCallback);
}
