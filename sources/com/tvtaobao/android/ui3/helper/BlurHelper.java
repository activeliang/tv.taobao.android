package com.tvtaobao.android.ui3.helper;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import com.tvtaobao.android.ui3.UI3Logger;
import java.lang.reflect.Array;

public class BlurHelper {
    private static final String TAG = BlurHelper.class.getSimpleName();

    public static Bitmap clipBmp(Bitmap bitmap, Rect blurRect) {
        return Bitmap.createBitmap(bitmap, blurRect.left, blurRect.top, blurRect.width(), blurRect.height());
    }

    public static Bitmap blurPoor(Bitmap bitmap) {
        int newAlpha;
        int newRed;
        int newGreen;
        int newBlue;
        int i;
        long bgn = System.currentTimeMillis();
        int bmpW = bitmap.getWidth();
        int bmpH = bitmap.getHeight();
        int[] src = new int[(bmpW * bmpH)];
        bitmap.getPixels(src, 0, bmpW, 0, 0, bmpW, bmpH);
        int[] out = new int[(bmpW * bmpH)];
        int[][] kernel = (int[][]) Array.newInstance(Integer.TYPE, new int[]{3, 3});
        int w = bmpW - 1;
        int h = bmpH - 1;
        int i2 = 0;
        while (i2 <= h) {
            int j = 0;
            while (j <= w) {
                kernel[0][0] = src[Math.max(0, j - 1) + (Math.max(0, i2 - 1) * bmpW)];
                kernel[0][1] = src[(Math.max(0, i2 - 1) * bmpW) + j];
                kernel[0][2] = src[Math.min(w, j + 1) + (Math.max(0, i2 - 1) * bmpW)];
                kernel[1][0] = src[Math.max(0, j - 1) + (i2 * bmpW)];
                kernel[1][1] = src[(i2 * bmpW) + j];
                kernel[1][2] = src[Math.min(w, j + 1) + (i2 * bmpW)];
                kernel[2][0] = src[Math.max(0, j - 1) + (Math.min(h, i2 + 1) * bmpW)];
                kernel[2][1] = src[(Math.min(h, i2 + 1) * bmpW) + j];
                kernel[2][2] = src[Math.min(w, j + 1) + (Math.min(h, i2 + 1) * bmpW)];
                int sumBlue = 0;
                int sumGreen = 0;
                int sumRed = 0;
                int sumAlpha = 0;
                if ((i2 <= 5 || j <= 5 || i2 >= h - 5 || j >= w - 5) && false) {
                    int sideDistance = Math.min(Math.min(Math.abs(i2 - 5), Math.abs(i2 - (h - 5))), Math.min(Math.abs(j - 5), Math.abs(j - (w - 5))));
                    int power = ((5 - sideDistance) * (5 - sideDistance) * (5 - sideDistance)) + 1;
                    for (int kh = 0; kh < 3; kh++) {
                        for (int kv = 0; kv < 3; kv++) {
                            int srcClr = kernel[kh][kv];
                            if (kh == 1 && kv == 1) {
                                sumAlpha += ((-16777216 & srcClr) >>> 24) * power;
                                sumRed += ((16711680 & srcClr) >>> 16) * power;
                                sumGreen += ((65280 & srcClr) >>> 8) * power;
                                i = (srcClr & 255) * power;
                            } else {
                                sumAlpha += (-16777216 & srcClr) >>> 24;
                                sumRed += (16711680 & srcClr) >>> 16;
                                sumGreen += (65280 & srcClr) >>> 8;
                                i = srcClr & 255;
                            }
                            sumBlue += i;
                        }
                    }
                    newAlpha = sumAlpha / (power + 9);
                    newRed = sumRed / (power + 9);
                    newGreen = sumGreen / (power + 9);
                    newBlue = sumBlue / (power + 9);
                } else {
                    for (int kh2 = 0; kh2 < 3; kh2++) {
                        for (int kv2 = 0; kv2 < 3; kv2++) {
                            int srcClr2 = kernel[kh2][kv2];
                            sumAlpha += (-16777216 & srcClr2) >>> 24;
                            sumRed += (16711680 & srcClr2) >>> 16;
                            sumGreen += (65280 & srcClr2) >>> 8;
                            sumBlue += srcClr2 & 255;
                        }
                    }
                    newAlpha = sumAlpha / 9;
                    newRed = sumRed / 9;
                    newGreen = sumGreen / 9;
                    newBlue = sumBlue / 9;
                }
                out[(i2 * bmpW) + j] = (newAlpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
                j++;
            }
            i2++;
        }
        Bitmap outBmp = Bitmap.createBitmap(out, bmpW, bmpH, Bitmap.Config.ARGB_8888);
        UI3Logger.i(TAG, "blurPoor cost " + (System.currentTimeMillis() - bgn));
        return outBmp;
    }

    public static Bitmap blurByGauss(Bitmap srcBitmap, int radius) {
        long bgn = System.currentTimeMillis();
        Bitmap bitmap = srcBitmap.copy(srcBitmap.getConfig(), true);
        if (radius < 1) {
            return null;
        }
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
        int temp = divsum2 * 256;
        int[] dv = new int[temp];
        for (int i = 0; i < temp; i++) {
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
                int[] sir = stack[i2 + radius];
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
                    vmin[x] = Math.min(x + radius + 1, wm);
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
                int[] sir4 = stack[i3 + radius];
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
        UI3Logger.i(TAG, "blurByGauss cost " + (System.currentTimeMillis() - bgn));
        return bitmap;
    }

    public static Bitmap bmpScale(Bitmap originBmp, float wScaleRadio, float hScaleRadio) {
        int bitmap_w = originBmp.getWidth();
        int bitmap_h = originBmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postScale(wScaleRadio, hScaleRadio);
        return Bitmap.createBitmap(originBmp, 0, 0, bitmap_w, bitmap_h, matrix, true);
    }
}
