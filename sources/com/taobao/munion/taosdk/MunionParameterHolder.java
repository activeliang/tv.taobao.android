package com.taobao.munion.taosdk;

import android.support.annotation.Keep;

@Keep
public interface MunionParameterHolder {
    String getParameter(String str);

    void holdParameter(String str);
}
