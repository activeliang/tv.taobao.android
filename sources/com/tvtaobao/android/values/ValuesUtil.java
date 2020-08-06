package com.tvtaobao.android.values;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import java.util.concurrent.atomic.AtomicInteger;

public class ValuesUtil {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(int pxValue) {
        return (int) ((((float) pxValue) / Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }

    public static float dip2px(float dipValue) {
        return (dipValue * Resources.getSystem().getDisplayMetrics().density) + 0.5f;
    }

    public static Rect getScreenRect() {
        new DisplayMetrics();
        DisplayMetrics displayMetric = Resources.getSystem().getDisplayMetrics();
        return new Rect(0, 0, displayMetric.widthPixels, displayMetric.heightPixels);
    }

    public static int getScreenWidth() {
        return getScreenRect().width();
    }

    public static int getScreenHeight() {
        return getScreenRect().height();
    }

    public static int generateViewId() {
        int result;
        int newValue;
        do {
            result = sNextGeneratedId.get();
            newValue = result + 1;
            if (newValue > 16777215) {
                newValue = 1;
            }
        } while (!sNextGeneratedId.compareAndSet(result, newValue));
        return result;
    }
}
