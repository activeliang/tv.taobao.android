package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.graphics.Rect;
import android.graphics.RectF;

public class ScalePositionManager {
    private static ScalePositionManager manager = null;

    public static ScalePositionManager instance() {
        if (manager == null) {
            manager = new ScalePositionManager();
        }
        return manager;
    }

    public Rect getScaledRect(Rect r, float scaleX, float scaleY) {
        Rect rScaled = new Rect();
        int imgW = r.width();
        int imgH = r.height();
        rScaled.left = (int) (((float) r.left) - (((scaleX - 1.0f) * ((float) imgW)) / 2.0f));
        rScaled.right = (int) (((float) r.left) + (((float) imgW) * scaleX));
        rScaled.top = (int) (((float) r.top) - (((scaleY - 1.0f) * ((float) imgH)) / 2.0f));
        rScaled.bottom = (int) (((float) r.top) + (((float) imgH) * scaleY));
        return rScaled;
    }

    public Rect getScaledRect(Rect r, float scaleX, float scaleY, float coefX, float coefY) {
        int width = r.width();
        int height = r.height();
        float diffScaleX = scaleX - 1.0f;
        float diffScaleY = scaleY - 1.0f;
        r.left = (int) (((float) r.left) - ((((float) width) * coefX) * diffScaleX));
        r.right = (int) (((float) r.right) + (((float) width) * (1.0f - coefX) * diffScaleX));
        r.top = (int) (((float) r.top) - ((((float) height) * coefY) * diffScaleY));
        r.bottom = (int) (((float) r.bottom) + (((float) height) * (1.0f - coefY) * diffScaleY));
        return r;
    }

    public void getScaledRect(Rect src, Rect dst, float scaleX, float scaleY, float coefX, float coefY) {
        int width = src.width();
        int height = src.height();
        float diffScaleX = scaleX - 1.0f;
        float diffScaleY = scaleY - 1.0f;
        dst.left = (int) (((float) src.left) - ((((float) width) * coefX) * diffScaleX));
        dst.right = (int) (((float) src.right) + (((float) width) * (1.0f - coefX) * diffScaleX));
        dst.top = (int) (((float) src.left) - ((((float) height) * coefY) * diffScaleY));
        dst.bottom = (int) (((float) src.bottom) + (((float) height) * (1.0f - coefY) * diffScaleY));
    }

    public void getScaledRect(RectF src, RectF dst, float scaleX, float scaleY, float coefX, float coefY) {
        float width = src.right - src.left;
        float height = src.bottom - src.top;
        float diffScaleX = scaleX - 1.0f;
        float diffScaleY = scaleY - 1.0f;
        dst.left = src.left - ((width * coefX) * diffScaleX);
        dst.right = src.right + ((1.0f - coefX) * width * diffScaleX);
        dst.top = src.left - ((height * coefY) * diffScaleY);
        dst.bottom = src.bottom + ((1.0f - coefY) * height * diffScaleY);
    }

    public void getScaledRectNoReturn(Rect r, float scaleX, float scaleY) {
        int imgW = r.width();
        int imgH = r.height();
        r.left = (int) (((float) r.left) - (((scaleX - 1.0f) * ((float) imgW)) / 2.0f));
        r.right = (int) (((float) r.left) + (((float) imgW) * scaleX));
        r.top = (int) (((float) r.top) - (((scaleY - 1.0f) * ((float) imgH)) / 2.0f));
        r.bottom = (int) (((float) r.top) + (((float) imgH) * scaleY));
    }
}
