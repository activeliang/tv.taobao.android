package com.yunos.tv.core.util;

import android.os.Environment;

public class SDCardUtil {
    public static boolean isSdCardWritable() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    public static boolean isSdCardReadable() {
        String state = Environment.getExternalStorageState();
        if ("mounted".equals(state) || "mounted_ro".equals(state)) {
            return true;
        }
        return false;
    }

    public static String getSdCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
