package com.taobao.media;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.taobao.windvane.jsbridge.api.BlowSensor;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import anet.channel.strategy.dispatch.DispatchConstants;

public class DWViewUtil {
    private static int mCutoutHeight = -1;

    public static int getScreenWidth() {
        if (MediaSystemUtils.sApplication == null) {
            return 600;
        }
        return ((WindowManager) MediaSystemUtils.sApplication.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight() {
        if (MediaSystemUtils.sApplication == null) {
            return 600;
        }
        return ((WindowManager) MediaSystemUtils.sApplication.getSystemService("window")).getDefaultDisplay().getHeight();
    }

    public static int getPortraitScreenWidth() {
        int screenWidth = getScreenWidth();
        int screenHeight = getScreenHeight();
        return screenWidth < screenHeight ? screenWidth : screenHeight;
    }

    public static int getPortraitScreenHeight() {
        int screenWidth = getScreenWidth();
        int screenHeight = getScreenHeight();
        return screenWidth > screenHeight ? screenWidth : screenHeight;
    }

    public static int getContentAreaHeight(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect.height();
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", DispatchConstants.ANDROID);
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getVideoWidthInLandscape(Activity activity) {
        if (Build.VERSION.SDK_INT == 18) {
            return getScreenHeight();
        }
        if (Build.VERSION.SDK_INT < 18) {
            return activity.getRequestedOrientation() == 1 ? getScreenHeight() - getStatusBarHeight(activity) : getScreenHeight();
        }
        int width = getRealHeightInPx(activity);
        if (Build.VERSION.SDK_INT < 26 || Build.VERSION.SDK_INT >= 28) {
            return width;
        }
        return width - getDisplayCutoutHeight(activity);
    }

    public static int getVideoWidthInActivityLandscape(Activity activity) {
        if (Build.VERSION.SDK_INT < 18) {
            return getRealHeightInPx(activity) - getStatusBarHeight(activity);
        }
        return getRealHeightInPx(activity);
    }

    public static int getVideoHeightInActivityLandscape(Activity activity) {
        if (Build.VERSION.SDK_INT < 18) {
            return getScreenWidth() - getStatusBarHeight(activity);
        }
        return getScreenWidth();
    }

    public static int getRealPxByWidth(float pxValue) {
        if (Float.isNaN(pxValue)) {
            return 0;
        }
        float realPx = (((float) getScreenWidth()) * pxValue) / 750.0f;
        if (((double) realPx) <= 0.005d || realPx >= 1.0f) {
            return (int) Math.rint((double) realPx);
        }
        return 1;
    }

    public static int getRealWithInPx(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class.forName("android.view.Display").getMethod("getRealMetrics", new Class[]{DisplayMetrics.class}).invoke(display, new Object[]{dm});
            return dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getRealHeightInPx(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class.forName("android.view.Display").getMethod("getRealMetrics", new Class[]{DisplayMetrics.class}).invoke(display, new Object[]{dm});
            return dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getRealPxByHeight(float pxValue) {
        if (Float.isNaN(pxValue)) {
            return 0;
        }
        float realPx = (((float) getScreenHeight()) * pxValue) / 750.0f;
        if (((double) realPx) <= 0.005d || realPx >= 1.0f) {
            return (int) Math.rint((double) realPx);
        }
        return 1;
    }

    public static boolean isVerticalScreen(Context context) {
        if (!(context instanceof Activity) || ((Activity) context).getResources().getConfiguration().orientation == 1) {
            return true;
        }
        return false;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static void setNavigationBar(Window window, int uiOptions) {
        if (Build.VERSION.SDK_INT > 18) {
            ((ViewGroup) window.getDecorView()).setSystemUiVisibility(uiOptions);
        }
    }

    public static int hideNavigationBar(Window window) {
        ViewGroup overlayLayout = (ViewGroup) window.getDecorView();
        int lastUiOptions = overlayLayout.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT > 18 && lastUiOptions != 4102) {
            overlayLayout.setSystemUiVisibility(BlowSensor.BLOW_HANDLER_FAIL);
        }
        return lastUiOptions;
    }

    public static int getDisplayCutoutHeight(Activity activity) {
        View decorView;
        WindowInsets windowInsets;
        DisplayCutout displayCutout;
        if (mCutoutHeight != -1) {
            return mCutoutHeight;
        }
        if (activity == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT < 28 || (decorView = activity.getWindow().getDecorView()) == null || (windowInsets = decorView.getRootWindowInsets()) == null || (displayCutout = windowInsets.getDisplayCutout()) == null) {
            if (Build.VERSION.SDK_INT >= 26) {
                if (activity.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism")) {
                    mCutoutHeight = 80;
                    return mCutoutHeight;
                } else if (hasDisplayCutoutVivo(activity)) {
                    mCutoutHeight = dip2px(activity, 27.0f);
                    return mCutoutHeight;
                } else if (hasDisplayCutoutHuawei(activity)) {
                    mCutoutHeight = getNotchSize(activity)[1];
                    return mCutoutHeight;
                } else if (hasDisplayCutoutXiaomi(activity)) {
                    int resourceId = activity.getResources().getIdentifier("notch_height", "dimen", DispatchConstants.ANDROID);
                    if (resourceId > 0) {
                        mCutoutHeight = activity.getResources().getDimensionPixelSize(resourceId);
                    }
                    return mCutoutHeight;
                }
            }
            mCutoutHeight = 0;
            return mCutoutHeight;
        }
        mCutoutHeight = displayCutout.getSafeInsetTop();
        return mCutoutHeight;
    }

    private static boolean hasDisplayCutoutVivo(Context context) {
        try {
            Class FtFeature = context.getClassLoader().loadClass("android.util.FtFeature");
            return ((Boolean) FtFeature.getMethod("isFeatureSupport", new Class[]{Integer.TYPE}).invoke(FtFeature, new Object[]{32})).booleanValue();
        } catch (Exception e) {
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    private static boolean hasDisplayCutoutHuawei(Context context) {
        try {
            Class HwNotchSizeUtil = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            return ((Boolean) HwNotchSizeUtil.getMethod("hasNotchInScreen", new Class[0]).invoke(HwNotchSizeUtil, new Object[0])).booleanValue();
        } catch (Exception e) {
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    private static boolean hasDisplayCutoutXiaomi(Context context) {
        boolean ret;
        try {
            Class SysPro = context.getClassLoader().loadClass("android.os.SystemProperties");
            if (((Integer) SysPro.getMethod("getInt", new Class[]{String.class, Integer.TYPE}).invoke(SysPro, new Object[]{"ro.miui.notch", 0})).intValue() == 1) {
                ret = true;
            } else {
                ret = false;
            }
            return ret;
        } catch (Exception e) {
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    private static int[] getNotchSize(Context context) {
        int[] ret = {0, 0};
        try {
            Class HwNotchSizeUtil = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            return (int[]) HwNotchSizeUtil.getMethod("getNotchSize", new Class[0]).invoke(HwNotchSizeUtil, new Object[0]);
        } catch (Exception e) {
            return ret;
        } catch (Throwable th) {
            return ret;
        }
    }
}
