package com.taobao.wireless.trade.mcart.sdk.utils;

import android.app.Activity;
import android.content.Context;

public class ContextUtils {
    public static boolean isActivityFinishing(Context context) {
        if (context == null || !(context instanceof Activity) || !((Activity) context).isFinishing()) {
            return false;
        }
        return true;
    }
}
