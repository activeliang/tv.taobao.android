package com.ali.user.open.ucc.context;

import com.ali.user.open.ucc.BindComponentProxy;
import java.util.HashMap;
import java.util.Map;

public class UccContext {
    private static Map<String, String> bizParams = new HashMap();
    private static BindComponentProxy mBindComponentProxy;

    public static Map<String, String> getBizParams() {
        return bizParams;
    }

    public static void setBizParams(Map<String, String> bizParams2) {
        bizParams = bizParams2;
    }

    public static void setBindComponentProxy(BindComponentProxy bindComponentProxy) {
        mBindComponentProxy = bindComponentProxy;
    }

    public static BindComponentProxy getBindComponentProxy() {
        return mBindComponentProxy;
    }
}
