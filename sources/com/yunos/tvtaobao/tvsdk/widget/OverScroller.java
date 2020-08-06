package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.zhiping.dev.android.logger.ZpLogger;

public class OverScroller {
    private static final int DEFAULT_DURATION = 250;
    private static final int FLING_MODE = 1;
    private static final int SCROLL_MODE = 0;
    private static float sViscousFluidNormalize;
    private static float sViscousFluidScale = 8.0f;
    private final boolean mFlywheel;
    private Interpolator mInterpolator;
    private int mMode;
    private final SplineOverScroller mScrollerX;
    private final SplineOverScroller mScrollerY;

    public OverScroller(Context context) {
        this(context, (Interpolator) null);
    }

    public OverScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    public OverScroller(Context context, Interpolator interpolator, boolean flywheel) {
        this.mInterpolator = interpolator;
        this.mFlywheel = flywheel;
        this.mScrollerX = new SplineOverScroller(context);
        this.mScrollerY = new SplineOverScroller(context);
    }

    public OverScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY) {
        this(context, interpolator, true);
    }

    public OverScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY, boolean flywheel) {
        this(context, interpolator, flywheel);
    }

    /* access modifiers changed from: package-private */
    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public final void setFriction(float friction) {
        this.mScrollerX.setFriction(friction);
        this.mScrollerY.setFriction(friction);
    }

    public final boolean isFinished() {
        return this.mScrollerX.mFinished && this.mScrollerY.mFinished;
    }

    public final void forceFinished(boolean finished) {
        boolean unused = this.mScrollerX.mFinished = this.mScrollerY.mFinished = finished;
    }

    public final int getCurrX() {
        return this.mScrollerX.mCurrentPosition;
    }

    public final int getCurrY() {
        return this.mScrollerY.mCurrentPosition;
    }

    public float getCurrVelocity() {
        return (float) Math.sqrt((double) ((this.mScrollerX.mCurrVelocity * this.mScrollerX.mCurrVelocity) + (this.mScrollerY.mCurrVelocity * this.mScrollerY.mCurrVelocity)));
    }

    public final int getStartX() {
        return this.mScrollerX.mStart;
    }

    public final int getStartY() {
        return this.mScrollerY.mStart;
    }

    public final int getFinalX() {
        return this.mScrollerX.mFinal;
    }

    public final int getFinalY() {
        return this.mScrollerY.mFinal;
    }

    @Deprecated
    public final int getDuration() {
        return Math.max(this.mScrollerX.mDuration, this.mScrollerY.mDuration);
    }

    @Deprecated
    public void extendDuration(int extend) {
        this.mScrollerX.extendDuration(extend);
        this.mScrollerY.extendDuration(extend);
    }

    @Deprecated
    public void setFinalX(int newX) {
        this.mScrollerX.setFinalPosition(newX);
    }

    @Deprecated
    public void setFinalY(int newY) {
        this.mScrollerY.setFinalPosition(newY);
    }

    public boolean computeScrollOffset() {
        float q;
        if (isFinished()) {
            return false;
        }
        switch (this.mMode) {
            case 0:
                long elapsedTime = AnimationUtils.currentAnimationTimeMillis() - this.mScrollerX.mStartTime;
                int duration = this.mScrollerX.mDuration;
                if (elapsedTime >= ((long) duration)) {
                    abortAnimation();
                    break;
                } else {
                    float q2 = ((float) elapsedTime) / ((float) duration);
                    if (this.mInterpolator == null) {
                        q = viscousFluid(q2);
                    } else {
                        q = this.mInterpolator.getInterpolation(q2);
                    }
                    this.mScrollerX.updateScroll(q);
                    this.mScrollerY.updateScroll(q);
                    break;
                }
            case 1:
                if (!this.mScrollerX.mFinished && !this.mScrollerX.update() && !this.mScrollerX.continueWhenFinished()) {
                    this.mScrollerX.finish();
                }
                if (!this.mScrollerY.mFinished && !this.mScrollerY.update() && !this.mScrollerY.continueWhenFinished()) {
                    this.mScrollerY.finish();
                    break;
                }
        }
        return true;
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, 250);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mMode = 0;
        this.mScrollerX.startScroll(startX, dx, duration);
        this.mScrollerY.startScroll(startY, dy, duration);
    }

    public boolean springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        this.mMode = 1;
        boolean spingbackX = this.mScrollerX.springback(startX, minX, maxX);
        boolean spingbackY = this.mScrollerY.springback(startY, minY, maxY);
        if (spingbackX || spingbackY) {
            return true;
        }
        return false;
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        if (this.mFlywheel && !isFinished()) {
            float oldVelocityX = this.mScrollerX.mCurrVelocity;
            float oldVelocityY = this.mScrollerY.mCurrVelocity;
            if (Math.signum((float) velocityX) == Math.signum(oldVelocityX) && Math.signum((float) velocityY) == Math.signum(oldVelocityY)) {
                velocityX = (int) (((float) velocityX) + oldVelocityX);
                velocityY = (int) (((float) velocityY) + oldVelocityY);
            }
        }
        this.mMode = 1;
        this.mScrollerX.fling(startX, velocityX, minX, maxX, overX);
        this.mScrollerY.fling(startY, velocityY, minY, maxY, overY);
    }

    public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
        this.mScrollerX.notifyEdgeReached(startX, finalX, overX);
    }

    public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
        this.mScrollerY.notifyEdgeReached(startY, finalY, overY);
    }

    public boolean isOverScrolled() {
        return (!this.mScrollerX.mFinished && this.mScrollerX.mState != 0) || (!this.mScrollerY.mFinished && this.mScrollerY.mState != 0);
    }

    public void abortAnimation() {
        this.mScrollerX.finish();
        this.mScrollerY.finish();
    }

    public int timePassed() {
        return (int) (AnimationUtils.currentAnimationTimeMillis() - Math.min(this.mScrollerX.mStartTime, this.mScrollerY.mStartTime));
    }

    public boolean isScrollingInDirection(float xvel, float yvel) {
        return !isFinished() && Math.signum(xvel) == Math.signum((float) (this.mScrollerX.mFinal - this.mScrollerX.mStart)) && Math.signum(yvel) == Math.signum((float) (this.mScrollerY.mFinal - this.mScrollerY.mStart));
    }

    static class SplineOverScroller {
        private static final int BALLISTIC = 2;
        private static final int CUBIC = 1;
        private static float DECELERATION_RATE = ((float) (Math.log(0.78d) / Math.log(0.9d)));
        private static final float END_TENSION = 1.0f;
        private static final float GRAVITY = 2000.0f;
        private static final float INFLEXION = 0.35f;
        private static final int NB_SAMPLES = 100;
        private static final float P1 = 0.175f;
        private static final float P2 = 0.35000002f;
        private static final int SPLINE = 0;
        private static final float[] SPLINE_POSITION = new float[101];
        private static final float[] SPLINE_TIME = new float[101];
        private static final float START_TENSION = 0.5f;
        /* access modifiers changed from: private */
        public float mCurrVelocity;
        /* access modifiers changed from: private */
        public int mCurrentPosition;
        private float mDeceleration;
        /* access modifiers changed from: private */
        public int mDuration;
        /* access modifiers changed from: private */
        public int mFinal;
        /* access modifiers changed from: private */
        public boolean mFinished = true;
        private float mFlingFriction = ViewConfiguration.getScrollFriction();
        private int mOver;
        private float mPhysicalCoeff;
        private int mSplineDistance;
        private int mSplineDuration;
        /* access modifiers changed from: private */
        public int mStart;
        /* access modifiers changed from: private */
        public long mStartTime;
        /* access modifiers changed from: private */
        public int mState = 0;
        private int mVelocity;

        static {
            float x;
            float coef;
            float y;
            float coef2;
            float x_min = 0.0f;
            float y_min = 0.0f;
            for (int i = 0; i < 100; i++) {
                float alpha = ((float) i) / 100.0f;
                float x_max = END_TENSION;
                while (true) {
                    x = x_min + ((x_max - x_min) / 2.0f);
                    coef = 3.0f * x * (END_TENSION - x);
                    float tx = ((((END_TENSION - x) * P1) + (P2 * x)) * coef) + (x * x * x);
                    if (((double) Math.abs(tx - alpha)) < 1.0E-5d) {
                        break;
                    } else if (tx > alpha) {
                        x_max = x;
                    } else {
                        x_min = x;
                    }
                }
                SPLINE_POSITION[i] = ((((END_TENSION - x) * START_TENSION) + x) * coef) + (x * x * x);
                float y_max = END_TENSION;
                while (true) {
                    y = y_min + ((y_max - y_min) / 2.0f);
                    coef2 = 3.0f * y * (END_TENSION - y);
                    float dy = ((((END_TENSION - y) * START_TENSION) + y) * coef2) + (y * y * y);
                    if (((double) Math.abs(dy - alpha)) < 1.0E-5d) {
                        break;
                    } else if (dy > alpha) {
                        y_max = y;
                    } else {
                        y_min = y;
                    }
                }
                SPLINE_TIME[i] = ((((END_TENSION - y) * P1) + (P2 * y)) * coef2) + (y * y * y);
            }
            float[] fArr = SPLINE_POSITION;
            SPLINE_TIME[100] = 1.0f;
            fArr[100] = 1.0f;
        }

        /* access modifiers changed from: package-private */
        public void setFriction(float friction) {
            this.mFlingFriction = friction;
        }

        SplineOverScroller(Context context) {
            this.mPhysicalCoeff = 386.0878f * context.getResources().getDisplayMetrics().density * 160.0f * 0.84f;
        }

        /* access modifiers changed from: package-private */
        public void updateScroll(float q) {
            this.mCurrentPosition = this.mStart + Math.round(((float) (this.mFinal - this.mStart)) * q);
        }

        private static float getDeceleration(int velocity) {
            if (velocity > 0) {
                return -2000.0f;
            }
            return GRAVITY;
        }

        private void adjustDuration(int start, int oldFinal, int newFinal) {
            float x = Math.abs(((float) (newFinal - start)) / ((float) (oldFinal - start)));
            int index = (int) (100.0f * x);
            if (index < 100) {
                float x_inf = ((float) index) / 100.0f;
                float t_inf = SPLINE_TIME[index];
                this.mDuration = (int) (((float) this.mDuration) * (t_inf + (((x - x_inf) / ((((float) (index + 1)) / 100.0f) - x_inf)) * (SPLINE_TIME[index + 1] - t_inf))));
            }
        }

        /* access modifiers changed from: package-private */
        public void startScroll(int start, int distance, int duration) {
            this.mFinished = false;
            this.mStart = start;
            this.mFinal = start + distance;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = duration;
            this.mDeceleration = 0.0f;
            this.mVelocity = 0;
        }

        /* access modifiers changed from: package-private */
        public void finish() {
            this.mCurrentPosition = this.mFinal;
            this.mFinished = true;
        }

        /* access modifiers changed from: package-private */
        public void setFinalPosition(int position) {
            this.mFinal = position;
            this.mFinished = false;
        }

        /* access modifiers changed from: package-private */
        public void extendDuration(int extend) {
            this.mDuration = ((int) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime)) + extend;
            this.mFinished = false;
        }

        /* access modifiers changed from: package-private */
        public boolean springback(int start, int min, int max) {
            this.mFinished = true;
            this.mFinal = start;
            this.mStart = start;
            this.mVelocity = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 0;
            if (start < min) {
                startSpringback(start, min, 0);
            } else if (start > max) {
                startSpringback(start, max, 0);
            }
            if (!this.mFinished) {
                return true;
            }
            return false;
        }

        private void startSpringback(int start, int end, int velocity) {
            this.mFinished = false;
            this.mState = 1;
            this.mStart = start;
            this.mFinal = end;
            int delta = start - end;
            this.mDeceleration = getDeceleration(delta);
            this.mVelocity = -delta;
            this.mOver = Math.abs(delta);
            this.mDuration = (int) (1000.0d * Math.sqrt((-2.0d * ((double) delta)) / ((double) this.mDeceleration)));
        }

        /* access modifiers changed from: package-private */
        public void fling(int start, int velocity, int min, int max, int over) {
            this.mOver = over;
            this.mFinished = false;
            this.mVelocity = velocity;
            this.mCurrVelocity = (float) velocity;
            this.mSplineDuration = 0;
            this.mDuration = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStart = start;
            this.mCurrentPosition = start;
            if (start > max || start < min) {
                startAfterEdge(start, min, max, velocity);
                return;
            }
            this.mState = 0;
            double totalDistance = ClientTraceData.b.f47a;
            if (velocity != 0) {
                int splineFlingDuration = getSplineFlingDuration(velocity);
                this.mSplineDuration = splineFlingDuration;
                this.mDuration = splineFlingDuration;
                totalDistance = getSplineFlingDistance(velocity);
            }
            this.mSplineDistance = (int) (((double) Math.signum((float) velocity)) * totalDistance);
            this.mFinal = this.mSplineDistance + start;
            if (this.mFinal < min) {
                adjustDuration(this.mStart, this.mFinal, min);
                this.mFinal = min;
            }
            if (this.mFinal > max) {
                adjustDuration(this.mStart, this.mFinal, max);
                this.mFinal = max;
            }
        }

        private double getSplineDeceleration(int velocity) {
            return Math.log((double) ((INFLEXION * ((float) Math.abs(velocity))) / (this.mFlingFriction * this.mPhysicalCoeff)));
        }

        private double getSplineFlingDistance(int velocity) {
            return ((double) (this.mFlingFriction * this.mPhysicalCoeff)) * Math.exp((((double) DECELERATION_RATE) / (((double) DECELERATION_RATE) - 1.0d)) * getSplineDeceleration(velocity));
        }

        private int getSplineFlingDuration(int velocity) {
            return (int) (1000.0d * Math.exp(getSplineDeceleration(velocity) / (((double) DECELERATION_RATE) - 1.0d)));
        }

        private void fitOnBounceCurve(int start, int end, int velocity) {
            float durationToApex = ((float) (-velocity)) / this.mDeceleration;
            float totalDuration = (float) Math.sqrt((2.0d * ((double) (((((float) (velocity * velocity)) / 2.0f) / Math.abs(this.mDeceleration)) + ((float) Math.abs(end - start))))) / ((double) Math.abs(this.mDeceleration)));
            this.mStartTime -= (long) ((int) (1000.0f * (totalDuration - durationToApex)));
            this.mStart = end;
            this.mVelocity = (int) ((-this.mDeceleration) * totalDuration);
        }

        private void startBounceAfterEdge(int start, int end, int velocity) {
            int i;
            if (velocity == 0) {
                i = start - end;
            } else {
                i = velocity;
            }
            this.mDeceleration = getDeceleration(i);
            fitOnBounceCurve(start, end, velocity);
            onEdgeReached();
        }

        private void startAfterEdge(int start, int min, int max, int velocity) {
            int edge;
            if (start <= min || start >= max) {
                boolean positive = start > max;
                if (positive) {
                    edge = max;
                } else {
                    edge = min;
                }
                int overDistance = start - edge;
                if (overDistance * velocity >= 0) {
                    startBounceAfterEdge(start, edge, velocity);
                } else if (getSplineFlingDistance(velocity) > ((double) Math.abs(overDistance))) {
                    fling(start, velocity, positive ? min : start, positive ? start : max, this.mOver);
                } else {
                    startSpringback(start, edge, velocity);
                }
            } else {
                ZpLogger.e("OverScroller", "startAfterEdge called from a valid position");
                this.mFinished = true;
            }
        }

        /* access modifiers changed from: package-private */
        public void notifyEdgeReached(int start, int end, int over) {
            if (this.mState == 0) {
                this.mOver = over;
                this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                startAfterEdge(start, end, end, (int) this.mCurrVelocity);
            }
        }

        private void onEdgeReached() {
            float distance = ((float) (this.mVelocity * this.mVelocity)) / (Math.abs(this.mDeceleration) * 2.0f);
            float sign = Math.signum((float) this.mVelocity);
            if (distance > ((float) this.mOver)) {
                this.mDeceleration = (((-sign) * ((float) this.mVelocity)) * ((float) this.mVelocity)) / (((float) this.mOver) * 2.0f);
                distance = (float) this.mOver;
            }
            this.mOver = (int) distance;
            this.mState = 2;
            int i = this.mStart;
            if (this.mVelocity <= 0) {
                distance = -distance;
            }
            this.mFinal = i + ((int) distance);
            this.mDuration = -((int) ((1000.0f * ((float) this.mVelocity)) / this.mDeceleration));
        }

        /* access modifiers changed from: package-private */
        public boolean continueWhenFinished() {
            switch (this.mState) {
                case 0:
                    if (this.mDuration < this.mSplineDuration) {
                        this.mStart = this.mFinal;
                        this.mVelocity = (int) this.mCurrVelocity;
                        this.mDeceleration = getDeceleration(this.mVelocity);
                        this.mStartTime += (long) this.mDuration;
                        onEdgeReached();
                        break;
                    } else {
                        return false;
                    }
                case 1:
                    return false;
                case 2:
                    this.mStartTime += (long) this.mDuration;
                    startSpringback(this.mFinal, this.mStart, 0);
                    break;
            }
            update();
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean update() {
            long currentTime = AnimationUtils.currentAnimationTimeMillis() - this.mStartTime;
            if (currentTime > ((long) this.mDuration)) {
                return false;
            }
            double distance = ClientTraceData.b.f47a;
            switch (this.mState) {
                case 0:
                    float t = ((float) currentTime) / ((float) this.mSplineDuration);
                    int index = (int) (100.0f * t);
                    float distanceCoef = END_TENSION;
                    float velocityCoef = 0.0f;
                    if (index < 100) {
                        float t_inf = ((float) index) / 100.0f;
                        float d_inf = SPLINE_POSITION[index];
                        velocityCoef = (SPLINE_POSITION[index + 1] - d_inf) / ((((float) (index + 1)) / 100.0f) - t_inf);
                        distanceCoef = d_inf + ((t - t_inf) * velocityCoef);
                    }
                    distance = (double) (((float) this.mSplineDistance) * distanceCoef);
                    this.mCurrVelocity = ((((float) this.mSplineDistance) * velocityCoef) / ((float) this.mSplineDuration)) * 1000.0f;
                    break;
                case 1:
                    float t2 = ((float) currentTime) / ((float) this.mDuration);
                    float t22 = t2 * t2;
                    float sign = Math.signum((float) this.mVelocity);
                    distance = (double) (((float) this.mOver) * sign * ((3.0f * t22) - ((2.0f * t2) * t22)));
                    this.mCurrVelocity = ((float) this.mOver) * sign * 6.0f * ((-t2) + t22);
                    break;
                case 2:
                    float t3 = ((float) currentTime) / 1000.0f;
                    this.mCurrVelocity = ((float) this.mVelocity) + (this.mDeceleration * t3);
                    distance = (double) ((((float) this.mVelocity) * t3) + (((this.mDeceleration * t3) * t3) / 2.0f));
                    break;
            }
            this.mCurrentPosition = this.mStart + ((int) Math.round(distance));
            return true;
        }
    }

    static {
        sViscousFluidNormalize = 1.0f;
        sViscousFluidNormalize = 1.0f / viscousFluid(1.0f);
    }

    static float viscousFluid(float x) {
        float x2;
        float x3 = x * sViscousFluidScale;
        if (x3 < 1.0f) {
            x2 = x3 - (1.0f - ((float) Math.exp((double) (-x3))));
        } else {
            x2 = 0.36787945f + ((1.0f - 0.36787945f) * (1.0f - ((float) Math.exp((double) (1.0f - x3)))));
        }
        return x2 * sViscousFluidNormalize;
    }
}
