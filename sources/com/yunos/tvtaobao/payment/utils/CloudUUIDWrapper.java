package com.yunos.tvtaobao.payment.utils;

import android.text.TextUtils;
import java.lang.reflect.InvocationTargetException;

public class CloudUUIDWrapper {
    private static String uuid;

    public static String getCloudUUID() {
        if (TextUtils.isEmpty(uuid)) {
            try {
                Class clz = Class.forName("com.yunos.CloudUUIDWrapper");
                uuid = (String) clz.getDeclaredMethod("getCloudUUID", new Class[0]).invoke(clz, new Object[0]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            }
        }
        return uuid;
    }
}
