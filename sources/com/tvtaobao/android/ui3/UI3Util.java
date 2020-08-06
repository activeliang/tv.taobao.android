package com.tvtaobao.android.ui3;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.bftv.fui.constantplugin.Constant;

public class UI3Util {
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

    public static boolean callInvalidateFromChildToRoot(View root, View child) {
        if (child == null) {
            return false;
        }
        if (child == root) {
            child.postInvalidate();
            return true;
        }
        if (child != null) {
            child.postInvalidate();
        }
        ViewParent vp = child.getParent();
        if (vp != null && (vp instanceof View)) {
            ((View) vp).postInvalidate();
        }
        int loopCount = 50;
        while (true) {
            loopCount--;
            if (loopCount <= 0) {
                return false;
            }
            if (vp == root) {
                if (vp instanceof View) {
                    ((View) vp).postInvalidate();
                }
                return true;
            } else if (vp == null) {
                return false;
            } else {
                vp = vp.getParent();
                if (vp != null && (vp instanceof View)) {
                    ((View) vp).postInvalidate();
                }
            }
        }
    }

    public static final boolean isConfirmKey(int keyCode) {
        switch (keyCode) {
            case 23:
            case 62:
            case 66:
            case 160:
                return true;
            default:
                return false;
        }
    }

    public static String getString(View rect) {
        if (rect != null) {
            return rect.getClass().getSimpleName() + Constant.NLP_CACHE_TYPE + rect.hashCode();
        }
        return null;
    }

    public static String getString(Object obj) {
        if (obj != null) {
            return obj.getClass().getSimpleName() + Constant.NLP_CACHE_TYPE + obj.hashCode();
        }
        return null;
    }

    public static String getString(Rect view) {
        if (view != null) {
            return view.getClass().getSimpleName() + Constant.NLP_CACHE_TYPE + view.toShortString();
        }
        return null;
    }

    public static boolean isContraryDirection(int direction1, int direction2) {
        if (direction1 == 33 && direction2 == 130) {
            return true;
        }
        if (direction1 == 17 && direction2 == 66) {
            return true;
        }
        if (direction2 == 33 && direction1 == 130) {
            return true;
        }
        if (direction2 == 17 && direction1 == 66) {
            return true;
        }
        return false;
    }

    public static boolean isUDContrary(int direction1, int direction2) {
        if (direction1 == 33 && direction2 == 130) {
            return true;
        }
        if (direction2 == 33 && direction1 == 130) {
            return true;
        }
        return false;
    }

    public static boolean isLRContrary(int direction1, int direction2) {
        if (direction1 == 17 && direction2 == 66) {
            return true;
        }
        if (direction2 == 17 && direction1 == 66) {
            return true;
        }
        return false;
    }

    public static FrameLayout findDecorView(View from) {
        FrameLayout rtn = null;
        if (from != null) {
            if (from instanceof FrameLayout) {
                rtn = (FrameLayout) from;
            }
            for (ViewParent vp = from.getParent(); vp != null; vp = vp.getParent()) {
                if (vp instanceof FrameLayout) {
                    rtn = (FrameLayout) vp;
                }
            }
        }
        return rtn;
    }
}
