package com.yunos.tvtaobao.tvsdk.widget.interpolator;

import android.view.animation.Interpolator;

public abstract class TweenInterpolator implements Interpolator {
    private static final String TAG = "TweenInterpolator";
    private float mChange;
    private int mDuration;
    private float mStart;
    private float mTarget;

    public abstract float interpolation(float f, float f2, float f3, float f4);

    public TweenInterpolator() {
    }

    public TweenInterpolator(float start, float target, int duration) {
        this.mStart = start;
        this.mTarget = target;
        this.mDuration = duration;
        this.mChange = this.mTarget - this.mStart;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public float getStart() {
        return this.mStart;
    }

    public float getTarget() {
        return this.mTarget;
    }

    public void setStartAndTarget(float start, float target) {
        this.mStart = start;
        this.mTarget = target;
        this.mChange = this.mTarget - this.mStart;
    }

    public float getInterpolation(float input) {
        return interpolation(input, 0.0f, 1.0f, 1.0f);
    }

    public float getValue(int current) {
        return interpolation((float) current, this.mStart, this.mChange, (float) this.mDuration);
    }

    public float getReverseValue(int current) {
        return interpolation((float) (this.mDuration - current), this.mStart, this.mChange, (float) this.mDuration);
    }
}
