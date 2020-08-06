package com.yunos.tvtaobao.biz.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.zhiping.dev.android.logger.ZpLogger;

public class TvCanvas extends Canvas {
    public TvCanvas() {
    }

    public TvCanvas(Bitmap output) {
        super(output);
    }

    public void drawCornerRect(RectF rectf, float radius, Paint paint, Boolean... corners) {
        ZpLogger.i("TvCanvas", "TvCanvas.drawCornerRect rectf=" + rectf + ", radius=" + radius + ", corners=" + corners);
        if (corners == null || corners.length <= 0) {
            drawRect(rectf, paint);
            return;
        }
        boolean alltrue = true;
        boolean allfalse = true;
        float[] radii = new float[8];
        int size = Math.min(corners.length, 4);
        if (size < 4) {
            alltrue = false;
            allfalse = false;
        }
        for (int i = 0; i < size; i++) {
            if (corners[i].booleanValue()) {
                allfalse = false;
                radii[i * 2] = radius;
                radii[(i * 2) + 1] = radius;
            } else {
                alltrue = false;
            }
            ZpLogger.i("TvCanvas", "TvCanvas.drawCornerRect corners[" + i + "]=" + corners[i]);
        }
        if (!allfalse && !alltrue) {
            Path path = new Path();
            path.addRoundRect(rectf, radii, Path.Direction.CW);
            drawPath(path, paint);
        } else if (allfalse) {
            drawRect(rectf, paint);
        } else {
            drawRoundRect(rectf, radius, radius, paint);
        }
    }
}
