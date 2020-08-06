package com.taobao.utils;

import java.io.Serializable;

public interface ILoginInfoGetter extends Serializable {
    LoginInfo getLastLoginUserInfo();

    LoginInfo getLoginUserInfo();
}
