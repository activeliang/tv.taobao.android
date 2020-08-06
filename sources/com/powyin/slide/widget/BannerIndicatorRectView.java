package com.powyin.slide.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BannerIndicatorRectView extends View {
    private int mBot;
    private float mDiver;
    private float mDiverContent;
    private float mDiverSpace;
    private int mIndex;
    private Paint mRectSelect;
    private Paint mRectUnSelect;
    private int mTop;
    private int mViewCount;
    private float offset;
    private int[] split;

    public BannerIndicatorRectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerIndicatorRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.split = new int[1000];
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
            this.mDiver = 0.07f * ((float) w);
        }
        this.mDiverSpace = this.mDiver * 0.1f;
        this.mDiverContent = this.mDiver - (this.mDiverSpace * 2.0f);
        float left = (((float) w) - (this.mDiver * ((float) this.mViewCount))) / 2.0f;
        for (int i = 0; i < this.mViewCount; i++) {
            this.split[i * 4] = (int) ((this.mDiver * ((float) i)) + left + this.mDiverSpace);
            this.split[(i * 4) + 3] = (int) (((this.mDiver * ((float) (i + 1))) + left) - this.mDiverSpace);
        }
        this.mTop = (int) (0.898f * ((float) h));
        this.mBot = (int) (0.91f * ((float) h));
    }

    private void ensureProgerss() {
        for (int i = 0; i < this.mViewCount; i++) {
            this.split[(i * 4) + 1] = this.split[i * 4];
            this.split[(i * 4) + 2] = this.split[(i * 4) + 3];
        }
        float endPoint = ((float) this.mIndex) + this.offset;
        if (endPoint < 0.0f) {
            this.split[1] = ((int) ((1.0f + endPoint) * this.mDiverContent)) + this.split[0];
            this.split[((this.mViewCount * 4) - 4) + 2] = (int) (((float) this.split[((this.mViewCount * 4) - 4) + 3]) + (this.mDiverContent * endPoint));
        } else if (endPoint > ((float) (this.mViewCount - 1)) + 0.5f) {
            this.split[1] = ((int) (this.offset * this.mDiverContent)) + this.split[0];
            this.split[((this.mViewCount * 4) - 4) + 2] = (int) (((float) this.split[((this.mViewCount * 4) - 4) + 3]) - (this.offset * this.mDiverContent));
        } else {
            int left = this.offset < 0.0f ? this.mIndex - 1 : this.mIndex;
            int right = this.offset < 0.0f ? this.mIndex : this.mIndex + 1;
            this.split[(left * 4) + 2] = (int) (((float) this.split[left * 4]) + ((endPoint - ((float) left)) * this.mDiverContent));
            this.split[(right * 4) + 1] = (int) (((float) this.split[(right * 4) + 3]) - ((((float) right) - endPoint) * this.mDiverContent));
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < this.mViewCount; i++) {
            int index0 = this.split[i * 4];
            int index1 = this.split[(i * 4) + 1];
            int index2 = this.split[(i * 4) + 2];
            int index3 = this.split[(i * 4) + 3];
            if (index0 != index1) {
                canvas.drawRect((float) index0, (float) this.mTop, (float) index1, (float) this.mBot, this.mRectSelect);
            }
            if (index1 != index2) {
                canvas.drawRect((float) index1, (float) this.mTop, (float) index2, (float) this.mBot, this.mRectUnSelect);
            }
            if (index2 != index3) {
                canvas.drawRect((float) index2, (float) this.mTop, (float) index3, (float) this.mBot, this.mRectSelect);
            }
        }
    }

    public void onButtonLineScroll(int viewCount, int centerIndex, float off) {
        if (this.mViewCount != viewCount) {
            this.mViewCount = viewCount;
            this.mViewCount = viewCount;
            ensureConfig();
        }
        this.mIndex = centerIndex;
        this.offset = off;
        ensureProgerss();
        invalidate();
    }
}
