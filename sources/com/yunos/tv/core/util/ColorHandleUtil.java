package com.yunos.tv.core.util;

import android.graphics.Color;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;

public class ColorHandleUtil {
    public static int ColorTransparency(int color, float transparency) {
        if (((double) transparency) >= ClientTraceData.b.f47a && ((double) transparency) <= 100.0d) {
            return Color.argb((int) (255.0d * ((double) transparency)), color, color, color);
        }
        throw new IllegalArgumentException("transparency >= 0.0  and  transparency <= 100.0");
    }

    public static int ColorTransparency(int color, int alpha) {
        return Color.argb(alpha, color, color, color);
    }
}
