package com.taobao.wireless.trade.mcart.sdk.protocol;

import com.taobao.tao.purchase.inject.Definition;

public interface LogProtocol extends Definition {
    void d(String str, String str2);

    void d(String str, String... strArr);

    void e(String str, String str2);

    void e(String str, String... strArr);

    void i(String str, String str2);

    void i(String str, String... strArr);

    void v(String str, String str2);

    void v(String str, String... strArr);

    void w(String str, String str2);

    void w(String str, String... strArr);
}
