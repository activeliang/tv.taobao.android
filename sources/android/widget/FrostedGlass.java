package android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.WindowManager;

public class FrostedGlass {
    static final boolean DEBUG = true;

    public native void nativeBoxBlur(Bitmap bitmap, int i);

    public native void nativeColorWaterPaint(Bitmap bitmap, int i);

    public native void nativeOilPaint(Bitmap bitmap, int i);

    public native void nativeScreenShot(Bitmap bitmap);

    public native void nativeStackBlur(Bitmap bitmap, int i);

    static {
        String str;
        String str2;
        try {
            System.loadLibrary("jni_yunos_filtershow_filters");
        } catch (Exception e) {
        } finally {
            str = "GlassScreen";
            str2 = "---------loadLibray jni_yunos_filtershow_filters Fail----------";
            Log.d(str, str2);
        }
    }

    public void boxBlur(Bitmap srcBitmap, int radius) {
        nativeBoxBlur(srcBitmap, radius);
    }

    public void stackBlur(Bitmap srcBitmap, int radius) {
        nativeStackBlur(srcBitmap, radius);
    }

    public void oilPaint(Bitmap srcBitmap, int radius) {
        nativeOilPaint(srcBitmap, radius);
    }

    public void colorWaterPaint(Bitmap srcBitmap, int radius) {
        nativeColorWaterPaint(srcBitmap, radius);
    }

    public Bitmap getFrostedGlassBitmap(Context context) {
        return getFrostedGlassBitmap(context, 5, 320, 180);
    }

    public Bitmap getFrostedGlassBitmap(Context context, int radius, int dstWidth, int dstHeight) {
        Bitmap screenbitmap = getScreenShot2(context);
        if (screenbitmap == null || screenbitmap.isRecycled()) {
            return null;
        }
        Bitmap glassBitmap = Bitmap.createScaledBitmap(screenbitmap, 320, 180, true);
        screenbitmap.recycle();
        stackBlur(glassBitmap, radius);
        return glassBitmap;
    }

    private Bitmap getScreenShot2(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(dm);
        Bitmap bp = null;
        int sdkVersion = Build.VERSION.SDK_INT;
        Log.d("GlassScreen", "dm - w:" + dm.widthPixels + " , h:" + dm.heightPixels + " , density:" + dm.density + " , scaledDensity:" + dm.scaledDensity + " , sdkVersion = " + sdkVersion);
        if (sdkVersion > 17) {
            Log.d("GlassScreen", "doGetScreenshot android.view.SurfaceControl");
            bp = SurfaceControl.screenshot(dm.widthPixels, dm.heightPixels);
        } else if (sdkVersion >= 14) {
            Log.d("GlassScreen", "doGetScreenshot android.view.Surface");
            bp = doGetScreenshot("android.view.Surface", dm.widthPixels, dm.heightPixels);
        }
        if (bp == null || bp.getByteCount() == 0) {
            Log.e("GlassScreen", "Surface.screenshot return null bitmap");
            return null;
        }
        Bitmap ifClipedBitmap = getIfClipedBitmap(bp);
        Log.d("GlassScreen", "Surface.screenshot bitmap  w:" + bp.getWidth() + " , h:" + bp.getHeight() + " , density: " + bp.getDensity() + " , " + bp.getByteCount());
        return ifClipedBitmap;
    }

    private Bitmap doGetScreenshot(String className, int width, int height) {
        try {
            Class<?> cls = Class.forName(className);
            return (Bitmap) cls.getMethod("screenshot", new Class[]{Integer.TYPE, Integer.TYPE}).invoke(cls, new Object[]{Integer.valueOf(width), Integer.valueOf(height)});
        } catch (Exception exception) {
            Log.e("GlassScreen", " get screenshot error ", exception);
            return null;
        }
    }

    private Bitmap getIfClipedBitmap(Bitmap src) {
        Bitmap dest = src;
        if (!isNeedClip(src)) {
            return dest;
        }
        Log.i("GlassScreen", "getIfClipedBitmap isNeedClip");
        int destWidth = src.getWidth() - getClipWidth(src);
        int destHeight = src.getHeight() - getClipHeight(src);
        if (destWidth <= 0) {
            destWidth = src.getWidth();
        }
        if (destHeight <= 0) {
            destHeight = src.getHeight();
        }
        return Bitmap.createBitmap(src, 0, 0, destWidth, destHeight);
    }

    private int getClipWidth(Bitmap bitmap) {
        int w = bitmap.getWidth();
        for (int i = 0; i < w; i++) {
            if (bitmap.getPixel((w - 1) - i, 0) != -16777216) {
                return i + 1;
            }
        }
        return 0;
    }

    private int getClipHeight(Bitmap bitmap) {
        int h = bitmap.getHeight();
        for (int j = 0; j < h; j++) {
            if (bitmap.getPixel(0, (h - 1) - j) != -16777216) {
                return j + 1;
            }
        }
        return 0;
    }

    private boolean isNeedClip(Bitmap bitmap) {
        boolean isNotBlackColor = false;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int j = 0;
        while (true) {
            if (j >= h) {
                break;
            } else if (bitmap.getPixel(w - 1, (h - 1) - j) != -16777216) {
                isNotBlackColor = true;
                break;
            } else {
                j++;
            }
        }
        Log.v("GlassScreen", "sbbb");
        if (isNotBlackColor) {
            return false;
        }
        int i = 0;
        while (true) {
            if (i >= w) {
                break;
            } else if (bitmap.getPixel((w - 1) - i, h - 1) != -16777216) {
                isNotBlackColor = true;
                break;
            } else {
                i++;
            }
        }
        if (!isNotBlackColor) {
            return true;
        }
        return false;
    }
}
