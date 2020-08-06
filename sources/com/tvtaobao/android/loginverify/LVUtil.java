package com.tvtaobao.android.loginverify;

import android.content.res.Resources;

public class LVUtil {
    public static float dip2px(float dipValue) {
        return (dipValue * Resources.getSystem().getDisplayMetrics().density) + 0.5f;
    }
}
