package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

public class TvTaoBaoTextView extends TextView {
    public TvTaoBaoTextView(Context context) {
        super(context);
    }

    public TvTaoBaoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TvTaoBaoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        CharSequence text = getText();
        setText((CharSequence) null);
        super.onDraw(canvas);
        TextPaint paint = getPaint();
        Paint.FontMetrics ft = paint.getFontMetrics();
        int height = getHeight();
        float f = ft.descent;
        canvas.drawText("2345", 0.0f, (float) getHeight(), paint);
    }

    public void setDrawText(String text) {
    }
}
