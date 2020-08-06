package com.yunos.tvtaobao.payment.utils;

import android.content.Context;

public class UtilsDistance {
    public static int dp2px(Context context, int dpVal) {
        return (int) (((float) dpVal) * (((float) context.getResources().getDisplayMetrics().heightPixels) / 720.0f));
    }
}
