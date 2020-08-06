package com.ali.user.open.ucc.biz;

import java.util.HashMap;
import java.util.Map;

public class UccBizContants {
    public static Map<String, Long> mBusyControlMap = new HashMap();
    public static final long mBusyControlThreshold = 1000;
    public static Map<String, Integer> mTrustLoginErrorTime = new HashMap();
}
