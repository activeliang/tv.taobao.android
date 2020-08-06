package com.yunos.tvtaobao.biz.request.bo.tvdetail.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ImageSpan;

public class ImageSpanCentre extends ImageSpan {
    public static final int CENTRE = 2;

    public ImageSpanCentre(Drawable drawable, int verticalAlignment) {
        super(drawable, verticalAlignment);
    }

    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        if (this.mVerticalAlignment == 2) {
            Drawable b = getDrawable();
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            canvas.save();
            canvas.translate(x, (float) (((((fm.descent + y) + y) + fm.ascent) / 2) - (b.getBounds().bottom / 2)));
            b.draw(canvas);
            canvas.restore();
            return;
        }
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
    }

    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        if (getVerticalAlignment() != 2) {
            return super.getSize(paint, text, start, end, fm);
        }
        Rect rect = getDrawable().getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;
            int top = (drHeight / 2) - (fontHeight / 4);
            int bottom = (drHeight / 2) + (fontHeight / 4);
            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }
}
