package com.ali.user.open.core.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.ReflectionUtils;
import java.util.Locale;

public class DeviceInfo {
    private static DeviceInfo INSTANCE = null;
    private static final String TAG = "DeviceInfo";
    public static String deviceId;

    public static void init(Context context) {
        if (TextUtils.isEmpty(deviceId)) {
            initDeviceId(context);
        }
    }

    public static synchronized DeviceInfo getInstance() {
        DeviceInfo deviceInfo;
        synchronized (DeviceInfo.class) {
            if (INSTANCE == null) {
                INSTANCE = new DeviceInfo();
            }
            deviceInfo = INSTANCE;
        }
        return deviceInfo;
    }

    private static void initDeviceId(final Context context) {
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

    public static String getLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= 24) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale.getLanguage() + "_" + locale.getCountry();
    }
}
