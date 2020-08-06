package com.taobao.ju.track.util;

import android.content.Intent;

public class IntentUtil {
    public static String getComponentSimpleClassName(Intent intent) {
        if (intent == null) {
            return null;
        }
        String clazzName = intent.getComponent().getClassName();
        return clazzName.substring(Math.max(0, clazzName.lastIndexOf(".") + 1));
    }
}
