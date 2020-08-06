package com.ali.user.open.service;

import com.ali.user.open.core.model.LoginReturnData;
import com.ali.user.open.core.model.ResultCode;
import com.ali.user.open.session.Session;

public interface SessionService {
    Session getSession();

    Session getSession(String str);

    boolean isSessionValid();

    ResultCode logout(String str);

    void refreshCookie(String str, LoginReturnData loginReturnData);

    void refreshWhenLogin(String str, LoginReturnData loginReturnData);
}
