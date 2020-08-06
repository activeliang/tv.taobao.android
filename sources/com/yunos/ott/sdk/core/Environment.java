package com.yunos.ott.sdk.core;

import android.util.Log;
import java.io.File;
import java.lang.reflect.Method;

public class Environment {
    public static String TAG = "SDK.CORE";
    private static Environment instance = null;
    private boolean yunos;

    public static Environment getInstance() {
        if (instance == null) {
            synchronized (Environment.class) {
                if (instance == null) {
                    instance = new Environment();
                }
            }
        }
        return instance;
    }

    private Environment() {
        this.yunos = false;
        this.yunos = setupYunos();
        log("Platfrom=YUNOS is", Boolean.toString(this.yunos));
        System.gc();
    }

    public boolean isYunos() {
        return this.yunos;
    }

    private boolean setupYunos() {
        String[] YUNOS_SYSTEM_PROPERTY_NAMES = {"ro.yunos.product.board", "ro.yunos.product.chip", "ro.yunos.product.vendor"};
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
            int length = YUNOS_SYSTEM_PROPERTY_NAMES.length;
            int i = 0;
            while (i < length) {
                String key = YUNOS_SYSTEM_PROPERTY_NAMES[i];
                Object o = method.invoke((Object) null, new Object[]{key});
                if (o == null || "".equalsIgnoreCase(o.toString())) {
                    i++;
                } else {
                    log("SystemProperties", key, o);
                    return true;
                }
            }
        } catch (Exception e) {
        }
        String[] YUNOS_SYSTEM_SPECIAL_FILES = {"/system/lib/libaoc.so", "/system/lib/libvmkid_lemur.so", "/system/etc/yunos_sensor_init.sh"};
        int length2 = YUNOS_SYSTEM_SPECIAL_FILES.length;
        int i2 = 0;
        while (i2 < length2) {
            String f = YUNOS_SYSTEM_SPECIAL_FILES[i2];
            File file = new File(f);
            if (!file.exists() || !file.isFile()) {
                i2++;
            } else {
                log("Special File exit", f);
                return true;
            }
        }
        return false;
    }

    private void log(String reason, Object... data) {
        StringBuffer sb = new StringBuffer(reason);
        for (Object s : data) {
            sb.append(" ");
            sb.append(s);
        }
        Log.d(TAG, sb.toString());
    }
}
