package com.powyin.slide.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BannerIndicatorCircleView extends View {
    private float mCircle;
    private float mDiver;
    private int mIndex;
    private float mLeft;
    private Paint mRectSelect;
    private Paint mRectUnSelect;
    private int mViewCount;
    private int mY;

    public BannerIndicatorCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerIndicatorCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mViewCount = 0;
        this.mRectUnSelect = new Paint();
        this.mRectUnSelect.setStyle(Paint.Style.FILL);
        this.mRectUnSelect.setColor(1711276032);
        this.mRectSelect = new Paint();
        this.mRectSelect.setStyle(Paint.Style.FILL);
        this.mRectSelect.setColor(-1);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(heightMeasureSpec));
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ensureConfig();
    }

    private void ensureConfig() {
        int w = getWidth();
        int h = getHeight();
        if (this.mViewCount >= 7) {
            this.mDiver = (((float) w) * 0.7f) / ((float) this.mViewCount);
        } else {
            this.mDiver = 0.05f * ((float) w);
        }
        this.mLeft = (((float) w) - (this.mDiver * ((float) this.mViewCount))) / 2.0f;
        this.mCircle = 0.015f * ((float) h);
        this.mY = (int) (0.93d * ((double) h));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < this.mViewCount; i++) {
            if (i == this.mIndex) {
                canvas.drawCircle(this.mLeft + (((float) i) * this.mDiver) + (this.mDiver / 2.0f), (float) this.mY, this.mCircle, this.mRectSelect);
            } else {
                canvas.drawCircle(this.mLeft + (((float) i) * this.mDiver) + (this.mDiver / 2.0f), (float) this.mY, this.mCircle, this.mRectUnSelect);
            }
        }
    }

    public void onButtonLineScroll(int viewCount, int centerIndex, float off) {
        if (this.mViewCount != viewCount) {
            this.mViewCount = viewCount;
            ensureConfig();
        }
        if (this.mIndex != centerIndex) {
            this.mIndex = centerIndex;
            invalidate();
        }
    }
}
