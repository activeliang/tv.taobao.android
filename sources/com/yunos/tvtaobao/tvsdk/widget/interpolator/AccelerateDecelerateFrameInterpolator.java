package com.yunos.tvtaobao.tvsdk.widget.interpolator;

public class AccelerateDecelerateFrameInterpolator extends TweenInterpolator {
    private double mCoef;
    private float mExponent = 2.0f;
    private float mScale = 10.0f;

    public AccelerateDecelerateFrameInterpolator() {
        init();
    }

    public AccelerateDecelerateFrameInterpolator(float scale, float exponent) {
        this.mScale = scale;
        this.mExponent = exponent;
        init();
    }

    public float getInterpolation(float input) {
        return (float) (Math.atan(Math.pow((double) input, (double) this.mExponent) * ((double) this.mScale)) * this.mCoef);
    }

    private void init() {
        this.mCoef = 1.0d / Math.atan((double) this.mScale);
    }

    public float interpolation(float t, float b, float c, float d) {
        return (getInterpolation(1.0f - ((d - t) / d)) * c) + b;
    }
}
