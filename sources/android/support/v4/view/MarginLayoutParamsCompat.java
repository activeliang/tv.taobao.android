package android.support.v4.view;

import android.os.Build;
import android.view.ViewGroup;

public final class MarginLayoutParamsCompat {
    static final MarginLayoutParamsCompatImpl IMPL;

    interface MarginLayoutParamsCompatImpl {
        int getLayoutDirection(ViewGroup.MarginLayoutParams marginLayoutParams);

        int getMarginEnd(ViewGroup.MarginLayoutParams marginLayoutParams);

        int getMarginStart(ViewGroup.MarginLayoutParams marginLayoutParams);

        boolean isMarginRelative(ViewGroup.MarginLayoutParams marginLayoutParams);

        void resolveLayoutDirection(ViewGroup.MarginLayoutParams marginLayoutParams, int i);

        void setLayoutDirection(ViewGroup.MarginLayoutParams marginLayoutParams, int i);

        void setMarginEnd(ViewGroup.MarginLayoutParams marginLayoutParams, int i);

        void setMarginStart(ViewGroup.MarginLayoutParams marginLayoutParams, int i);
    }

    static class MarginLayoutParamsCompatImplBase implements MarginLayoutParamsCompatImpl {
        MarginLayoutParamsCompatImplBase() {
        }

        public int getMarginStart(ViewGroup.MarginLayoutParams lp) {
            return lp.leftMargin;
        }

        public int getMarginEnd(ViewGroup.MarginLayoutParams lp) {
            return lp.rightMargin;
        }

        public void setMarginStart(ViewGroup.MarginLayoutParams lp, int marginStart) {
            lp.leftMargin = marginStart;
        }

        public void setMarginEnd(ViewGroup.MarginLayoutParams lp, int marginEnd) {
            lp.rightMargin = marginEnd;
        }

        public boolean isMarginRelative(ViewGroup.MarginLayoutParams lp) {
            return false;
        }

        public int getLayoutDirection(ViewGroup.MarginLayoutParams lp) {
            return 0;
        }

        public void setLayoutDirection(ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        }

        public void resolveLayoutDirection(ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        }
    }

    static class MarginLayoutParamsCompatImplJbMr1 implements MarginLayoutParamsCompatImpl {
        MarginLayoutParamsCompatImplJbMr1() {
        }

        public int getMarginStart(ViewGroup.MarginLayoutParams lp) {
            return MarginLayoutParamsCompatJellybeanMr1.getMarginStart(lp);
        }

        public int getMarginEnd(ViewGroup.MarginLayoutParams lp) {
            return MarginLayoutParamsCompatJellybeanMr1.getMarginEnd(lp);
        }

        public void setMarginStart(ViewGroup.MarginLayoutParams lp, int marginStart) {
            MarginLayoutParamsCompatJellybeanMr1.setMarginStart(lp, marginStart);
        }

        public void setMarginEnd(ViewGroup.MarginLayoutParams lp, int marginEnd) {
            MarginLayoutParamsCompatJellybeanMr1.setMarginEnd(lp, marginEnd);
        }

        public boolean isMarginRelative(ViewGroup.MarginLayoutParams lp) {
            return MarginLayoutParamsCompatJellybeanMr1.isMarginRelative(lp);
        }

        public int getLayoutDirection(ViewGroup.MarginLayoutParams lp) {
            return MarginLayoutParamsCompatJellybeanMr1.getLayoutDirection(lp);
        }

        public void setLayoutDirection(ViewGroup.MarginLayoutParams lp, int layoutDirection) {
            MarginLayoutParamsCompatJellybeanMr1.setLayoutDirection(lp, layoutDirection);
        }

        public void resolveLayoutDirection(ViewGroup.MarginLayoutParams lp, int layoutDirection) {
            MarginLayoutParamsCompatJellybeanMr1.resolveLayoutDirection(lp, layoutDirection);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 17) {
            IMPL = new MarginLayoutParamsCompatImplJbMr1();
        } else {
            IMPL = new MarginLayoutParamsCompatImplBase();
        }
    }

    public static int getMarginStart(ViewGroup.MarginLayoutParams lp) {
        return IMPL.getMarginStart(lp);
    }

    public static int getMarginEnd(ViewGroup.MarginLayoutParams lp) {
        return IMPL.getMarginEnd(lp);
    }

    public static void setMarginStart(ViewGroup.MarginLayoutParams lp, int marginStart) {
        IMPL.setMarginStart(lp, marginStart);
    }

    public static void setMarginEnd(ViewGroup.MarginLayoutParams lp, int marginEnd) {
        IMPL.setMarginEnd(lp, marginEnd);
    }

    public static boolean isMarginRelative(ViewGroup.MarginLayoutParams lp) {
        return IMPL.isMarginRelative(lp);
    }

    public static int getLayoutDirection(ViewGroup.MarginLayoutParams lp) {
        int result = IMPL.getLayoutDirection(lp);
        if (result == 0 || result == 1) {
            return result;
        }
        return 0;
    }

    public static void setLayoutDirection(ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        IMPL.setLayoutDirection(lp, layoutDirection);
    }

    public static void resolveLayoutDirection(ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        IMPL.resolveLayoutDirection(lp, layoutDirection);
    }

    private MarginLayoutParamsCompat() {
    }
}
