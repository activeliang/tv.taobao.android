package com.powyin.scroll.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;

public class LoadProgressBar extends View {
    /* access modifiers changed from: private */
    public ValueAnimator animator;
    private final int ballCount;
    private int canvasHei;
    private int canvasWei;
    private Paint circlePaint;
    /* access modifiers changed from: private */
    public float divide;
    /* access modifiers changed from: private */
    public boolean mAttach;
    /* access modifiers changed from: private */
    public int mVisibility;

    public LoadProgressBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public LoadProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mAttach = false;
        this.ballCount = 12;
        this.mVisibility = 0;
        this.circlePaint = new Paint();
        this.circlePaint.setColor(-2046820353);
        this.circlePaint.setStrokeWidth(4.0f);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) ((40.0f * getContext().getResources().getDisplayMetrics().density) + 0.5f));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 12; i++) {
            canvas.drawCircle(((float) (this.canvasWei / 2)) + (getSplit((4.0f * (((1.0f * ((float) i)) / 12.0f) - 0.5f)) + this.divide) * ((float) this.canvasWei) * 0.08f), (float) (this.canvasHei / 2), 8.0f, this.circlePaint);
        }
    }

    public void ensureAnimation(boolean forceReStart) {
        if (forceReStart) {
            if (this.animator != null) {
                this.animator.cancel();
                this.animator = null;
            }
        } else if (this.animator != null && this.animator.isStarted()) {
            return;
        }
        this.animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.animator.setDuration(AbstractClientManager.BIND_SERVICE_TIMEOUT);
        this.animator.setRepeatCount(1);
        this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float unused = LoadProgressBar.this.divide = ((float) (8 * ((System.currentTimeMillis() % 3000) - 1500))) / 3000.0f;
                LoadProgressBar.this.invalidate();
            }
        });
        this.animator.addListener(new Animator.AnimatorListener() {
            boolean isDeprecated = false;

            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                if (!this.isDeprecated && LoadProgressBar.this.mAttach && LoadProgressBar.this.animator == animation && LoadProgressBar.this.mVisibility != 4 && LoadProgressBar.this.mVisibility != 8) {
                    this.isDeprecated = true;
                    ViewParent parent = LoadProgressBar.this.getParent();
                    while (parent != null && !(parent instanceof ISwipe)) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof SwipeNest) {
                        SwipeNest swipeNest = (SwipeNest) parent;
                        if (swipeNest.getScrollY() > swipeNest.computeVerticalScrollRange() - swipeNest.computeVerticalScrollExtent()) {
                            LoadProgressBar.this.ensureAnimation(true);
                        }
                    } else if ((parent instanceof SwipeRefresh) && ((SwipeRefresh) parent).getScrollY() > 0) {
                        LoadProgressBar.this.ensureAnimation(true);
                    }
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.animator.start();
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        this.mVisibility = visibility;
        if ((visibility == 8 || visibility == 4) && this.animator != null) {
            this.animator.cancel();
            this.animator = null;
        }
        if (visibility == 0) {
            ensureAnimation(false);
        }
    }

    private float getSplit(float value) {
        int positive = value >= 0.0f ? 1 : -1;
        float value2 = Math.abs(value);
        if (value2 <= 1.0f) {
            return ((float) positive) * value2;
        }
        return ((float) Math.pow((double) value2, 2.0d)) * ((float) positive);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttach = true;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAttach = false;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.canvasWei = right - left;
        this.canvasHei = bottom - top;
        ensureAnimation(false);
    }
}
