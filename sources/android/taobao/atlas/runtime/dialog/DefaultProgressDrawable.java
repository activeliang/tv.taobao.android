package android.taobao.atlas.runtime.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;

public class DefaultProgressDrawable extends Drawable implements Animatable {
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    private final RectF fBounds = new RectF();
    private Property<DefaultProgressDrawable, Float> mAngleProperty = new Property<DefaultProgressDrawable, Float>(Float.class, "angle") {
        public Float get(DefaultProgressDrawable object) {
            return Float.valueOf(object.getCurrentGlobalAngle());
        }

        public void set(DefaultProgressDrawable object, Float value) {
            object.setCurrentGlobalAngle(value.floatValue());
        }
    };
    private float mBorderWidth;
    private float mCurrentGlobalAngle;
    private float mCurrentGlobalAngleOffset;
    private float mCurrentSweepAngle;
    private boolean mModeAppearing;
    private ObjectAnimator mObjectAnimatorAngle;
    private ObjectAnimator mObjectAnimatorSweep;
    private Paint mPaint;
    private boolean mRunning;
    private Property<DefaultProgressDrawable, Float> mSweepProperty = new Property<DefaultProgressDrawable, Float>(Float.class, "arc") {
        public Float get(DefaultProgressDrawable object) {
            return Float.valueOf(object.getCurrentSweepAngle());
        }

        public void set(DefaultProgressDrawable object, Float value) {
            object.setCurrentSweepAngle(value.floatValue());
        }
    };

    public DefaultProgressDrawable(int color, float borderWidth) {
        this.mBorderWidth = borderWidth;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(borderWidth);
        this.mPaint.setColor(color);
        setupAnimations();
    }

    public void draw(Canvas canvas) {
        float sweepAngle;
        float startAngle = this.mCurrentGlobalAngle - this.mCurrentGlobalAngleOffset;
        float sweepAngle2 = this.mCurrentSweepAngle;
        if (!this.mModeAppearing) {
            startAngle += sweepAngle2;
            sweepAngle = (360.0f - sweepAngle2) - 30.0f;
        } else {
            sweepAngle = sweepAngle2 + 30.0f;
        }
        canvas.drawArc(this.fBounds, startAngle, sweepAngle, false, this.mPaint);
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        return -2;
    }

    /* access modifiers changed from: private */
    public void toggleAppearingMode() {
        this.mModeAppearing = !this.mModeAppearing;
        if (this.mModeAppearing) {
            this.mCurrentGlobalAngleOffset = (this.mCurrentGlobalAngleOffset + 60.0f) % 360.0f;
        }
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.fBounds.left = ((float) bounds.left) + (this.mBorderWidth / 2.0f) + 0.5f;
        this.fBounds.right = (((float) bounds.right) - (this.mBorderWidth / 2.0f)) - 0.5f;
        this.fBounds.top = ((float) bounds.top) + (this.mBorderWidth / 2.0f) + 0.5f;
        this.fBounds.bottom = (((float) bounds.bottom) - (this.mBorderWidth / 2.0f)) - 0.5f;
    }

    private void setupAnimations() {
        this.mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, this.mAngleProperty, new float[]{360.0f});
        this.mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        this.mObjectAnimatorAngle.setDuration(AbstractClientManager.BIND_SERVICE_TIMEOUT);
        this.mObjectAnimatorAngle.setRepeatMode(1);
        this.mObjectAnimatorAngle.setRepeatCount(-1);
        this.mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, this.mSweepProperty, new float[]{300.0f});
        this.mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        this.mObjectAnimatorSweep.setDuration(600);
        this.mObjectAnimatorSweep.setRepeatMode(1);
        this.mObjectAnimatorSweep.setRepeatCount(-1);
        this.mObjectAnimatorSweep.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
                DefaultProgressDrawable.this.toggleAppearingMode();
            }
        });
    }

    public void start() {
        if (!isRunning()) {
            this.mRunning = true;
            this.mObjectAnimatorAngle.start();
            this.mObjectAnimatorSweep.start();
            invalidateSelf();
        }
    }

    public void stop() {
        if (isRunning()) {
            this.mRunning = false;
            this.mObjectAnimatorAngle.cancel();
            this.mObjectAnimatorSweep.cancel();
            invalidateSelf();
        }
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        this.mCurrentGlobalAngle = currentGlobalAngle;
        invalidateSelf();
    }

    public float getCurrentGlobalAngle() {
        return this.mCurrentGlobalAngle;
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        this.mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }

    public float getCurrentSweepAngle() {
        return this.mCurrentSweepAngle;
    }

    public void setRingColor(int color) {
        this.mPaint.setColor(color);
        invalidateSelf();
    }

    public void setRingWidth(float width) {
        this.mPaint.setStrokeWidth(width);
        invalidateSelf();
    }
}
