package com.yunos.tvtaobao.tvsdk.widget.focus.params;

import android.view.animation.Interpolator;

public class ScaleParams {
    public static final int SCALED_FIXED_COEF = 1;
    public static final int SCALED_FIXED_X = 2;
    public static final int SCALED_FIXED_Y = 3;
    private int mFixedScaleX;
    private int mFixedScaleY;
    private int mScaleFrameRate;
    private Interpolator mScaleInterpolator;
    private int mScaleMode;
    private float mScaleX;
    private float mScaleY;

    public ScaleParams(float scaleX, float scaleY, int scaleFrameRate, Interpolator interpolator) {
        this.mScaleMode = 1;
        this.mScaleX = 1.1f;
        this.mScaleY = 1.1f;
        this.mScaleFrameRate = 5;
        this.mScaleInterpolator = null;
        this.mScaleMode = 1;
        this.mScaleX = scaleX;
        this.mScaleY = scaleY;
        this.mScaleFrameRate = scaleFrameRate;
        this.mScaleInterpolator = interpolator;
    }

    public ScaleParams(int scaleMode, int fixedScale, int scaleFrameRate, Interpolator interpolator) {
        this.mScaleMode = 1;
        this.mScaleX = 1.1f;
        this.mScaleY = 1.1f;
        this.mScaleFrameRate = 5;
        this.mScaleInterpolator = null;
        this.mScaleFrameRate = scaleFrameRate;
        this.mScaleInterpolator = interpolator;
        setScale(scaleMode, fixedScale);
    }

    public void setScaleFrameRate(int rate) {
        this.mScaleFrameRate = rate;
    }

    public void setScale(int scaleMode, float scaleX, float scaleY) {
        if (scaleMode == 1) {
            this.mScaleMode = 1;
            this.mScaleX = scaleX;
            this.mScaleY = scaleY;
            return;
        }
        throw new IllegalArgumentException("setScale:scaleMode must be SCALED_FIXED_COEF(1), but it is " + scaleMode);
    }

    public void setScale(int scaleMode, int fixedScale) {
        if (scaleMode == 2) {
            this.mFixedScaleX = fixedScale;
        } else if (scaleMode == 3) {
            this.mFixedScaleY = fixedScale;
        } else {
            throw new IllegalArgumentException("scaleMode must be SCALED_FIXED_X(2) or SCALED_FIXED_Y(3), but it is " + scaleMode);
        }
    }

    public Interpolator getScaleInterpolator() {
        return this.mScaleInterpolator;
    }

    public int getScaleFrameRate() {
        return this.mScaleFrameRate;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public float getScaleY() {
        return this.mScaleY;
    }

    public int getFixedScaleX() {
        return this.mFixedScaleX;
    }

    public int getFixedScaleY() {
        return this.mFixedScaleY;
    }

    public void computeScaleXY(int width, int height) {
        if (this.mScaleMode == 2) {
            this.mScaleX = (((float) this.mFixedScaleX) / ((float) width)) + 1.0f;
            this.mScaleY = this.mScaleX;
        } else if (this.mScaleMode == 3) {
            this.mScaleY = (((float) this.mFixedScaleY) / ((float) height)) + 1.0f;
            this.mScaleX = this.mScaleY;
        } else if (this.mScaleMode != 1) {
            throw new IllegalArgumentException("scaleMode must be SCALED_FIXED_COEF(1), SCALED_FIXED_X(2) or SCALED_FIXED_Y(3), but it is " + this.mScaleMode);
        }
    }
}
