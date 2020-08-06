package com.yunos.tvtaobao.tvsdk.widget.focus.params;

import android.view.animation.Interpolator;

public class AlphaParams {
    private int mAlphaFrameRate = 5;
    private Interpolator mAlphaInterpolator = null;
    float mFromAlpha = 0.0f;
    float mToAlpha = 1.0f;

    public AlphaParams(int alphaFrameRate, float fromAlpha, float toAlpha, Interpolator interpolator) {
        this.mAlphaFrameRate = alphaFrameRate;
        this.mFromAlpha = fromAlpha;
        this.mToAlpha = toAlpha;
        this.mAlphaInterpolator = interpolator;
    }

    public float getFromAlpha() {
        return this.mFromAlpha;
    }

    public float getToAlpha() {
        return this.mToAlpha;
    }

    public Interpolator getAlphaInteroplator() {
        return this.mAlphaInterpolator;
    }

    public int getAlphaFrameRate() {
        return this.mAlphaFrameRate;
    }
}
