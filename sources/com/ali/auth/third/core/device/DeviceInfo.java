package com.ali.auth.third.core.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.ReflectionUtils;

public class DeviceInfo {
    private static final String TAG = "DeviceInfo";
    public static String deviceId;

    public static void init(Context context) {
        if (TextUtils.isEmpty(deviceId)) {
            _initDeviceId(context);
        }
    }

    private static void _initDeviceId(final Context context) {
        String utdid = null;
        Class<?> deviceClazz = ReflectionUtils.loadClassQuietly("com.ta.utdid2.device.UTDevice");
        if (deviceClazz != null) {
            try {
                Object ret = deviceClazz.getMethod("getUtdid", new Class[]{Context.class}).invoke((Object) null, new Object[]{context});
                if (ret == null) {
                    SDKLogger.e(TAG, "get utdid null");
                } else {
                    utdid = (String) ret;
                }
            } catch (Exception e) {
                SDKLogger.e(TAG, "get utdid error");
            }
        }
        if (utdid == null) {
            KernelContext.executorService.postTask(new Runnable() {
                public void run() {
                    try {
                        DeviceInfo.deviceId = context.getSharedPreferences("auth_sdk_device", 0).getString("deviceId", "");
                        SDKLogger.e(DeviceInfo.TAG, "DeviceInfo.deviceId = " + DeviceInfo.deviceId);
                    } catch (Throwable th) {
                    }
                }
            });
            return;
        }
        deviceId = utdid;
        SDKLogger.e(TAG, "utdid = " + utdid);
        KernelContext.executorService.postTask(new Runnable() {
            public void run() {
                try {
                    SharedPreferences.Editor editor = context.getSharedPreferences("auth_sdk_device", 0).edit();
                    editor.putString("deviceId", DeviceInfo.deviceId);
                    editor.apply();
                } catch (Throwable th) {
                }
            }
        });
    }
}
