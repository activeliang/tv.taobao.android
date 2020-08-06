package com.yunos.tvtaobao.tvsdk.widget.focus.params;

import android.view.animation.Interpolator;

public class FocusParams {
    private int mFocusFrameRate = 5;
    private Interpolator mFocusInterpolator = null;
    private boolean mFocusVisible = true;

    public FocusParams(boolean focusVisible, int focusFrameRate, Interpolator interpolator) {
        this.mFocusVisible = focusVisible;
        this.mFocusFrameRate = focusFrameRate;
        this.mFocusInterpolator = interpolator;
    }

    public void setFocusVisible(boolean visible) {
        this.mFocusVisible = visible;
    }

    public boolean isFocusVisible() {
        return this.mFocusVisible;
    }

    public void setFocusFrameRate(int rate) {
        this.mFocusFrameRate = rate;
    }

    public int getFocusFrameRate() {
        return this.mFocusFrameRate;
    }

    public Interpolator getFocusInterpolator() {
        return this.mFocusInterpolator;
    }
}
