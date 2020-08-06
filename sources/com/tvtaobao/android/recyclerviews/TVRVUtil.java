package com.tvtaobao.android.recyclerviews;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;
import com.bftv.fui.constantplugin.Constant;

public class TVRVUtil {
    public static boolean isBinA(View A, View B) {
        if (A == null || B == null) {
            return false;
        }
        if (B == A) {
            return true;
        }
        ViewParent vp = B.getParent();
        int loopCount = 50;
        while (true) {
            loopCount--;
            if (loopCount <= 0) {
                return false;
            }
            if (vp == A) {
                return true;
            }
            if (vp == null) {
                return false;
            }
            vp = vp.getParent();
        }
    }

    public static String getString(View v) {
        if (v == null) {
            return null;
        }
        Rect r = new Rect();
        try {
            v.getGlobalVisibleRect(r);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return v.getClass().getSimpleName() + Constant.NLP_CACHE_TYPE + v.hashCode() + "&" + r.toString();
    }
}
