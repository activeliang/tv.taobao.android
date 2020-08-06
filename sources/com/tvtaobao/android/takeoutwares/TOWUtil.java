package com.tvtaobao.android.takeoutwares;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.bftv.fui.constantplugin.Constant;

public class TOWUtil {
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

    public static boolean isViewHasFocus(View v) {
        if (v == null || !v.hasFocus()) {
            return false;
        }
        if (v instanceof ViewGroup) {
            return isViewHasFocus(((ViewGroup) v).getFocusedChild());
        }
        return true;
    }

    public static void invalidateToRoot(View from) {
        invalidateToRoot(from, 30);
    }

    public static void invalidateToRoot(View from, int loopCount) {
        if (from != null) {
            from.postInvalidate();
            ViewParent vp = from.getParent();
            while (vp != null) {
                if (vp instanceof View) {
                    ((View) vp).postInvalidate();
                }
                vp = vp.getParent();
                loopCount--;
                if (loopCount < 0) {
                    return;
                }
            }
        }
    }
}
