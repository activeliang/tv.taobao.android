package com.yunos.tvtaobao.biz.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

public class DrawRect {
    private Rect mDrawRect = new Rect();
    private Paint mPaint = new Paint();
    private boolean mShowMark = true;
    private ViewGroup mView;

    public DrawRect(ViewGroup view) {
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setAlpha(51);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mView = view;
    }

    public void showMark() {
        this.mShowMark = true;
        this.mView.invalidate();
    }

    public void hideMark() {
        this.mShowMark = false;
        this.mView.invalidate();
    }

    public void drawRect(Canvas canvas, View specialView) {
        if (this.mShowMark) {
            if (specialView == null || specialView.getVisibility() != 0) {
                this.mDrawRect.set(0, 0, this.mView.getWidth(), this.mView.getHeight());
            } else {
                specialView.getFocusedRect(this.mDrawRect);
                this.mDrawRect.left += specialView.getPaddingLeft();
                this.mDrawRect.top += specialView.getPaddingTop();
                this.mDrawRect.right -= specialView.getPaddingRight();
                this.mDrawRect.bottom -= specialView.getPaddingBottom();
                this.mView.offsetDescendantRectToMyCoords(specialView, this.mDrawRect);
            }
            canvas.drawRect(this.mDrawRect, this.mPaint);
        }
    }
}
