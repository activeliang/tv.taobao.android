package com.powyin.scroll.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.View;
import com.powyin.scroll.R;

class CircleViewBac extends View {
    Paint arcPaint;
    RectF arcRectF;
    BitmapDrawable bitmapDrawablePre;
    int hei;
    float progress;
    int wei;

    public CircleViewBac(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleViewBac(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.bitmapDrawablePre = (BitmapDrawable) context.getResources().getDrawable(R.drawable.powyin_scroll_progress_pre);
    }

    public void setProgress(float progress2) {
        if (this.progress != progress2) {
            this.progress = progress2;
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.wei = w;
        this.hei = h;
        this.bitmapDrawablePre.setBounds(0, 0, this.wei, this.hei);
        this.arcRectF = new RectF(0.0f, 0.0f, (float) w, (float) h);
        this.arcPaint = new Paint();
        this.arcPaint.setStrokeWidth(2.0f);
        this.arcPaint.setStyle(Paint.Style.STROKE);
        this.arcPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.arcPaint.setAntiAlias(true);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawImage(canvas);
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawArc(this.arcRectF, 90.0f, (float) ((this.progress < 0.0f || this.progress > 1.0f) ? 360 : (int) (360.0f * this.progress)), false, this.arcPaint);
    }

    private void drawImage(Canvas canvas) {
        int sweepAngle = 180;
        if (this.progress <= 0.0f) {
            sweepAngle = 0;
        } else if (this.progress < 1.0f) {
            sweepAngle = Math.min(180, (int) ((180.0f * this.progress) - 0.61f));
        }
        this.bitmapDrawablePre.setBounds(0, 0, this.wei, this.hei);
        canvas.save();
        canvas.rotate((float) sweepAngle, (float) (this.wei / 2), (float) (this.hei / 2));
        this.bitmapDrawablePre.draw(canvas);
        canvas.restore();
    }
}
