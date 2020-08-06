package com.yunos.tv.lib;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import com.bftv.fui.constantplugin.Constant;

public class DisplayUtil {
    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        return (int) ((spValue * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static String getViewStr(View v) {
        if (v == null) {
            return Constant.NULL;
        }
        Rect tmp = new Rect();
        if (v.getGlobalVisibleRect(tmp)) {
            return v.hashCode() + Constant.NLP_CACHE_TYPE + tmp.toString();
        }
        return "" + v.hashCode();
    }
}
