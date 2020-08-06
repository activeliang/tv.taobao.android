package com.alibaba.analytics.core.logbuilder;

import android.content.Context;
import com.alibaba.analytics.utils.ReflectUtils;

public class LogAssembleHelper {
    private static String mUMID = null;
    private static boolean mUMIDGetSwitch = true;

    public static String getSecurityToken(Context aContext) {
        if (aContext != null && mUMIDGetSwitch) {
            try {
                Class lClass = Class.forName("com.taobao.dp.DeviceSecuritySDK");
                if (lClass != null) {
                    Object lObj = ReflectUtils.invokeStaticMethod(lClass, "getInstance", new Object[]{aContext}, Context.class);
                    if (lObj != null) {
                        Object lSToken = ReflectUtils.invokeMethod(lObj, "getSecurityToken");
                        if (lSToken != null) {
                            mUMID = (String) lSToken;
                        }
                        return (String) lSToken;
                    }
                } else {
                    mUMIDGetSwitch = false;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }
}
