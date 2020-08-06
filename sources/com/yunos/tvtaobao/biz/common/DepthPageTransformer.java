package com.yunos.tvtaobao.biz.common;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.view.View;

public class DepthPageTransformer implements ViewPager.PageTransformer {
    @SuppressLint({"NewApi"})
    public void transformPage(View view, float position) {
        float alpha;
        if (position < -1.0f) {
            alpha = 0.0f;
        } else if (position <= 0.0f) {
            alpha = position + 1.0f;
        } else if (position <= 1.0f) {
            alpha = 1.0f - position;
        } else {
            alpha = 0.0f;
        }
        view.setAlpha(alpha * alpha);
    }
}
