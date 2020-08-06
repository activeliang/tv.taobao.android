package com.tvtaobao.android.ui3.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class OffScreenRenderHelper {
    Bitmap offscreenBmp;
    Canvas offscreenCanvas;
    View view;

    public interface OffScreenDrawTask {
        void draw(Canvas canvas);
    }

    public OffScreenRenderHelper(View view2) {
        this.view = view2;
    }

    private void preDraw() {
        if (!(this.offscreenBmp == null || (this.offscreenBmp.getWidth() == this.view.getWidth() && this.offscreenBmp.getHeight() == this.view.getHeight()))) {
            if (!this.offscreenBmp.isRecycled()) {
                this.offscreenBmp.recycle();
            }
            this.offscreenBmp = null;
        }
        if (this.offscreenBmp == null) {
            try {
                this.offscreenBmp = Bitmap.createBitmap(this.view.getWidth(), this.view.getHeight(), Bitmap.Config.ARGB_8888);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (this.offscreenCanvas == null) {
            this.offscreenCanvas = new Canvas();
        }
        if (this.offscreenBmp != null) {
            this.offscreenCanvas.setBitmap(this.offscreenBmp);
        }
    }

    public void doOffScreenDraw(OffScreenDrawTask task) {
        preDraw();
        if (this.offscreenCanvas != null && this.offscreenBmp != null) {
            task.draw(this.offscreenCanvas);
        }
    }

    public Bitmap getOffscreenBmp() {
        return this.offscreenBmp;
    }

    public Canvas getOffscreenCanvas() {
        return this.offscreenCanvas;
    }

    public View getView() {
        return this.view;
    }
}
