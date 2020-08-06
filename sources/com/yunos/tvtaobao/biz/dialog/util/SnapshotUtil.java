package com.yunos.tvtaobao.biz.dialog.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.widget.FrostedGlass;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;

public final class SnapshotUtil {
    /* access modifiers changed from: private */
    public static boolean Shotting = false;
    /* access modifiers changed from: private */
    public static String TAG = "SnapshotUtil";

    public interface OnFronstedGlassSreenDoneListener {
        void onFronstedGlassSreenDone(Bitmap bitmap);
    }

    public static synchronized void getFronstedSreenShot(WeakReference<Activity> mBaseActivityRef, int radius, float fronstedScale, OnFronstedGlassSreenDoneListener listener) {
        synchronized (SnapshotUtil.class) {
            ZpLogger.i(TAG, "getFronstedSreenShot -->  radius = " + radius + "; fronstedScale = " + fronstedScale + "; Shotting = " + Shotting);
            Handler mHandle = new Handler();
            if (Shotting) {
                mHandle.post(new MainHandleRunnable(mBaseActivityRef, listener, (Bitmap) null));
            } else {
                Shotting = true;
                ZpLogger.v(TAG, TAG + ".getFronstedSreenShot.1 ");
                new Thread(new SnapshotRunnable(mHandle, mBaseActivityRef, listener)).start();
            }
        }
    }

    private static class SnapshotRunnable implements Runnable {
        private final OnFronstedGlassSreenDoneListener listener;
        private final Handler mHandle;
        private final WeakReference<Activity> mReference;

        public SnapshotRunnable(Handler mHandle2, WeakReference<Activity> mBaseActivityRef, OnFronstedGlassSreenDoneListener listener2) {
            this.mReference = mBaseActivityRef;
            this.mHandle = mHandle2;
            this.listener = listener2;
        }

        public void run() {
            Bitmap postBmp = null;
            try {
                FrostedGlass fg = new FrostedGlass();
                if (!(this.mReference == null || this.mReference.get() == null)) {
                    postBmp = fg.getFrostedGlassBitmap((Context) this.mReference.get());
                    ZpLogger.i(SnapshotUtil.TAG, "SnapshotRunnable -->  mReference = " + this.mReference + "; postBmp = " + postBmp);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            boolean unused = SnapshotUtil.Shotting = false;
            ZpLogger.i(SnapshotUtil.TAG, "SnapshotRunnable -->  listener = " + this.listener + "; postBmp = " + postBmp + "; mHandle = " + this.mHandle);
            if (this.mHandle != null) {
                this.mHandle.post(new MainHandleRunnable(this.mReference, this.listener, postBmp));
            }
        }
    }

    private static class MainHandleRunnable implements Runnable {
        private final Bitmap bm;
        private final OnFronstedGlassSreenDoneListener listener;
        private final WeakReference<Activity> mReference;

        public MainHandleRunnable(WeakReference<Activity> mBaseActivityRef, OnFronstedGlassSreenDoneListener listener2, Bitmap bm2) {
            this.listener = listener2;
            this.bm = bm2;
            this.mReference = mBaseActivityRef;
        }

        public void run() {
            ZpLogger.i(SnapshotUtil.TAG, "MainHandleRunnable -->  listener = " + this.listener + "; bm = " + this.bm);
            if (this.listener != null) {
                this.listener.onFronstedGlassSreenDone(this.bm);
            }
        }
    }

    public static synchronized void getFronstedBitmap(final Bitmap bitmap, final int radius, final OnFronstedGlassSreenDoneListener listener) {
        synchronized (SnapshotUtil.class) {
            final Handler mHandle = new Handler();
            if (Shotting) {
                mHandle.post(new Runnable() {
                    public void run() {
                        if (listener != null) {
                            listener.onFronstedGlassSreenDone((Bitmap) null);
                        }
                    }
                });
            } else {
                Shotting = true;
                new Thread(new Runnable() {
                    public void run() {
                        Bitmap bmp = bitmap;
                        if (bmp == null || bmp.isRecycled()) {
                            mHandle.post(new Runnable() {
                                public void run() {
                                    if (listener != null) {
                                        listener.onFronstedGlassSreenDone((Bitmap) null);
                                    }
                                }
                            });
                        } else {
                            try {
                                int bitmap_w = bmp.getWidth();
                                int bitmap_h = bmp.getHeight();
                                Matrix matrix = new Matrix();
                                matrix.reset();
                                matrix.postScale(0.25f, 0.25f);
                                Bitmap bmpStack = Bitmap.createBitmap(bmp, 0, 0, bitmap_w, bitmap_h, matrix, true);
                                new FrostedGlass().stackBlur(bmpStack, radius);
                                final Bitmap postBmp = bmpStack;
                                mHandle.post(new Runnable() {
                                    public void run() {
                                        if (listener != null) {
                                            listener.onFronstedGlassSreenDone(postBmp);
                                        }
                                    }
                                });
                            } catch (Throwable e) {
                                if (listener != null) {
                                    listener.onFronstedGlassSreenDone((Bitmap) null);
                                }
                                ZpLogger.e(SnapshotUtil.TAG, "FrostedGlass error:" + e);
                            }
                        }
                        boolean unused = SnapshotUtil.Shotting = false;
                    }
                }).start();
            }
        }
    }

    public static void fastBlur(final Bitmap sentBitmap, final int radius, final OnFronstedGlassSreenDoneListener listener) {
        if (!Shotting) {
            Shotting = true;
            new Thread(new Runnable() {
                public void run() {
                    Bitmap bmp = sentBitmap;
                    if (bmp != null && !bmp.isRecycled()) {
                        try {
                            int bitmap_w = bmp.getWidth();
                            int bitmap_h = bmp.getHeight();
                            Matrix matrix = new Matrix();
                            matrix.reset();
                            matrix.postScale(0.25f, 0.25f);
                            Bitmap bmp2 = Bitmap.createBitmap(bmp, 0, 0, bitmap_w, bitmap_h, matrix, true);
                            Bitmap bitmap = bmp2.copy(bmp2.getConfig(), true);
                            if (radius >= 1) {
                                int w = bitmap.getWidth();
                                int h = bitmap.getHeight();
                                int[] pix = new int[(w * h)];
                                bitmap.getPixels(pix, 0, w, 0, 0, w, h);
                                int wm = w - 1;
                                int hm = h - 1;
                                int wh = w * h;
                                int div = radius + radius + 1;
                                int[] r = new int[wh];
                                int[] g = new int[wh];
                                int[] b = new int[wh];
                                int[] vmin = new int[Math.max(w, h)];
                                int divsum = (div + 1) >> 1;
                                int divsum2 = divsum * divsum;
                                int[] dv = new int[(divsum2 * 256)];
                                for (int i = 0; i < divsum2 * 256; i++) {
                                    dv[i] = i / divsum2;
                                }
                                int yi = 0;
                                int yw = 0;
                                int[][] stack = (int[][]) Array.newInstance(Integer.TYPE, new int[]{div, 3});
                                int r1 = radius + 1;
                                for (int y = 0; y < h; y++) {
                                    int bsum = 0;
                                    int gsum = 0;
                                    int rsum = 0;
                                    int boutsum = 0;
                                    int goutsum = 0;
                                    int routsum = 0;
                                    int binsum = 0;
                                    int ginsum = 0;
                                    int rinsum = 0;
                                    for (int i2 = -radius; i2 <= radius; i2++) {
                                        int p = pix[Math.min(wm, Math.max(i2, 0)) + yi];
                                        int[] sir = stack[radius + i2];
                                        sir[0] = (16711680 & p) >> 16;
                                        sir[1] = (65280 & p) >> 8;
                                        sir[2] = p & 255;
                                        int rbs = r1 - Math.abs(i2);
                                        rsum += sir[0] * rbs;
                                        gsum += sir[1] * rbs;
                                        bsum += sir[2] * rbs;
                                        if (i2 > 0) {
                                            rinsum += sir[0];
                                            ginsum += sir[1];
                                            binsum += sir[2];
                                        } else {
                                            routsum += sir[0];
                                            goutsum += sir[1];
                                            boutsum += sir[2];
                                        }
                                    }
                                    int stackpointer = radius;
                                    for (int x = 0; x < w; x++) {
                                        r[yi] = dv[rsum];
                                        g[yi] = dv[gsum];
                                        b[yi] = dv[bsum];
                                        int rsum2 = rsum - routsum;
                                        int gsum2 = gsum - goutsum;
                                        int bsum2 = bsum - boutsum;
                                        int[] sir2 = stack[((stackpointer - radius) + div) % div];
                                        int routsum2 = routsum - sir2[0];
                                        int goutsum2 = goutsum - sir2[1];
                                        int boutsum2 = boutsum - sir2[2];
                                        if (y == 0) {
                                            vmin[x] = Math.min(radius + x + 1, wm);
                                        }
                                        int p2 = pix[vmin[x] + yw];
                                        sir2[0] = (16711680 & p2) >> 16;
                                        sir2[1] = (65280 & p2) >> 8;
                                        sir2[2] = p2 & 255;
                                        int rinsum2 = rinsum + sir2[0];
                                        int ginsum2 = ginsum + sir2[1];
                                        int binsum2 = binsum + sir2[2];
                                        rsum = rsum2 + rinsum2;
                                        gsum = gsum2 + ginsum2;
                                        bsum = bsum2 + binsum2;
                                        stackpointer = (stackpointer + 1) % div;
                                        int[] sir3 = stack[stackpointer % div];
                                        routsum = routsum2 + sir3[0];
                                        goutsum = goutsum2 + sir3[1];
                                        boutsum = boutsum2 + sir3[2];
                                        rinsum = rinsum2 - sir3[0];
                                        ginsum = ginsum2 - sir3[1];
                                        binsum = binsum2 - sir3[2];
                                        yi++;
                                    }
                                    yw += w;
                                }
                                for (int x2 = 0; x2 < w; x2++) {
                                    int bsum3 = 0;
                                    int gsum3 = 0;
                                    int rsum3 = 0;
                                    int boutsum3 = 0;
                                    int goutsum3 = 0;
                                    int routsum3 = 0;
                                    int binsum3 = 0;
                                    int ginsum3 = 0;
                                    int rinsum3 = 0;
                                    int yp = (-radius) * w;
                                    for (int i3 = -radius; i3 <= radius; i3++) {
                                        int yi2 = Math.max(0, yp) + x2;
                                        int[] sir4 = stack[radius + i3];
                                        sir4[0] = r[yi2];
                                        sir4[1] = g[yi2];
                                        sir4[2] = b[yi2];
                                        int rbs2 = r1 - Math.abs(i3);
                                        rsum3 += r[yi2] * rbs2;
                                        gsum3 += g[yi2] * rbs2;
                                        bsum3 += b[yi2] * rbs2;
                                        if (i3 > 0) {
                                            rinsum3 += sir4[0];
                                            ginsum3 += sir4[1];
                                            binsum3 += sir4[2];
                                        } else {
                                            routsum3 += sir4[0];
                                            goutsum3 += sir4[1];
                                            boutsum3 += sir4[2];
                                        }
                                        if (i3 < hm) {
                                            yp += w;
                                        }
                                    }
                                    int yi3 = x2;
                                    int stackpointer2 = radius;
                                    for (int y2 = 0; y2 < h; y2++) {
                                        pix[yi3] = (-16777216 & pix[yi3]) | (dv[rsum3] << 16) | (dv[gsum3] << 8) | dv[bsum3];
                                        int rsum4 = rsum3 - routsum3;
                                        int gsum4 = gsum3 - goutsum3;
                                        int bsum4 = bsum3 - boutsum3;
                                        int[] sir5 = stack[((stackpointer2 - radius) + div) % div];
                                        int routsum4 = routsum3 - sir5[0];
                                        int goutsum4 = goutsum3 - sir5[1];
                                        int boutsum4 = boutsum3 - sir5[2];
                                        if (x2 == 0) {
                                            vmin[y2] = Math.min(y2 + r1, hm) * w;
                                        }
                                        int p3 = x2 + vmin[y2];
                                        sir5[0] = r[p3];
                                        sir5[1] = g[p3];
                                        sir5[2] = b[p3];
                                        int rinsum4 = rinsum3 + sir5[0];
                                        int ginsum4 = ginsum3 + sir5[1];
                                        int binsum4 = binsum3 + sir5[2];
                                        rsum3 = rsum4 + rinsum4;
                                        gsum3 = gsum4 + ginsum4;
                                        bsum3 = bsum4 + binsum4;
                                        stackpointer2 = (stackpointer2 + 1) % div;
                                        int[] sir6 = stack[stackpointer2];
                                        routsum3 = routsum4 + sir6[0];
                                        goutsum3 = goutsum4 + sir6[1];
                                        boutsum3 = boutsum4 + sir6[2];
                                        rinsum3 = rinsum4 - sir6[0];
                                        ginsum3 = ginsum4 - sir6[1];
                                        binsum3 = binsum4 - sir6[2];
                                        yi3 += w;
                                    }
                                }
                                bitmap.setPixels(pix, 0, w, 0, 0, w, h);
                                Bitmap postBmp = bitmap;
                                if (listener != null) {
                                    listener.onFronstedGlassSreenDone(postBmp);
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable e) {
                            if (listener != null) {
                                listener.onFronstedGlassSreenDone((Bitmap) null);
                            }
                            ZpLogger.e(SnapshotUtil.TAG, "FrostedGlass error:" + e);
                        }
                    } else if (listener != null) {
                        listener.onFronstedGlassSreenDone((Bitmap) null);
                    }
                    boolean unused = SnapshotUtil.Shotting = false;
                }
            }).start();
        } else if (listener != null) {
            listener.onFronstedGlassSreenDone((Bitmap) null);
        }
    }
}
