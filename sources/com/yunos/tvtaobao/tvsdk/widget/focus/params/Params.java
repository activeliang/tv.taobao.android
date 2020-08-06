package com.yunos.tvtaobao.tvsdk.widget.focus.params;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public class Params {
    AlphaParams mAlphaParams = new AlphaParams(20, 0.0f, 1.0f, new AccelerateDecelerateInterpolator());
    FocusParams mFocusParams = null;
    ScaleParams mScaleParams = null;

    public Params(float scaleX, float scaleY, int scaleFrameRate, Interpolator scaleInterpolator, boolean focusVisible, int focusFrameRate, Interpolator focusInterpolator) {
        this.mScaleParams = new ScaleParams(scaleX, scaleY, scaleFrameRate, scaleInterpolator);
        this.mFocusParams = new FocusParams(focusVisible, focusFrameRate, focusInterpolator);
    }

    public Params(float scaleX, float scaleY, int scaleFrameRate, Interpolator scaleInterpolator, boolean focusVisible, int focusFrameRate, Interpolator focusInterpolator, int alphaFrameRate, float fromAlpha, float toAlpha, Interpolator alphaInterpolator) {
        this.mScaleParams = new ScaleParams(scaleX, scaleY, scaleFrameRate, scaleInterpolator);
        this.mFocusParams = new FocusParams(focusVisible, focusFrameRate, focusInterpolator);
        this.mAlphaParams = new AlphaParams(alphaFrameRate, fromAlpha, toAlpha, alphaInterpolator);
    }

    public Params(ScaleParams scaleParams, FocusParams focusParams, AlphaParams alphaParams) {
        this.mScaleParams = scaleParams;
        this.mFocusParams = focusParams;
        this.mAlphaParams = alphaParams;
    }

    public ScaleParams getScaleParams() {
        return this.mScaleParams;
    }

    public FocusParams getFocusParams() {
        return this.mFocusParams;
    }

    public AlphaParams getAlphaParams() {
        return this.mAlphaParams;
    }
}
