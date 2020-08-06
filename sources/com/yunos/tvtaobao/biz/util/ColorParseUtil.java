package com.yunos.tvtaobao.biz.util;

import android.graphics.Color;

public class ColorParseUtil {
    public static int parseColor(String color, int defaultColor) {
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            return defaultColor;
        }
    }
}
