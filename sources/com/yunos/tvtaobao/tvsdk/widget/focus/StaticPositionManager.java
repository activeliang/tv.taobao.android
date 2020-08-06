package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.graphics.Canvas;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DrawListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.PositionListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.AlphaParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.ScaleParams;
import com.zhiping.dev.android.logger.ZpLogger;

public class StaticPositionManager extends PositionManager {
    static String TAG = "StaticPositionManager";
    private boolean mReset = false;

    public StaticPositionManager(int focusMode, PositionListener l) {
        super(focusMode, l);
    }

    public void draw(Canvas canvas) {
        if (this.mFocus != null) {
            preDrawOut(canvas);
            super.draw(canvas);
            if (!this.mStart) {
                return;
            }
            if (!this.mFocus.canDraw()) {
                postDrawOut(canvas);
                this.mListener.postInvalidateDelayed(30);
                return;
            }
            ItemListener item = this.mFocus.getItem();
            if (item != null) {
                processReset();
                drawBeforeFocus(item, canvas);
                boolean isInvalidate = false;
                if (!isFinished()) {
                    if (this.mFrame == 1) {
                        this.mFocus.onFocusStart();
                    }
                    if (!isAlphaFinished()) {
                        drawAlpha(item);
                        this.mAlphaFrame++;
                    }
                    if (!isScaleFinished()) {
                        if (item.isScale()) {
                            drawScale(canvas, item);
                            drawStaticFocus(canvas, item, item.getScaleX(), item.getScaleY());
                        } else {
                            drawStaticFocus(canvas, item);
                        }
                        this.mScaleFrame++;
                    } else if (this.mFocus.isScrolling()) {
                        drawStaticFocus(canvas, item);
                    } else if (this.mForceDrawFocus) {
                        drawStaticFocus(canvas, item);
                        this.mForceDrawFocus = false;
                    } else {
                        drawFocus(canvas);
                    }
                    this.mFrame++;
                    isInvalidate = true;
                    if (this.mFrame == getTotalFrame()) {
                        this.mFocus.onFocusFinished();
                    }
                } else if (this.mFocus.isScrolling()) {
                    drawStaticFocus(canvas, item);
                    isInvalidate = true;
                } else if (this.mForceDrawFocus) {
                    drawStaticFocus(canvas, item);
                    isInvalidate = true;
                    this.mForceDrawFocus = false;
                } else {
                    drawFocus(canvas);
                }
                if (this.mSelector != null && !this.mPause && (isInvalidate || this.mSelector.isDynamicFocus())) {
                    this.mListener.invalidate();
                }
                if (isFinished()) {
                    addCurNode(item);
                    resetSelector();
                } else {
                    this.mCurNodeAdded = false;
                }
                drawAfterFocus(item, canvas);
                postDrawOut(canvas);
                this.mLastItem = item;
            }
        }
    }

    public void resetSelector() {
        if (this.mConvertSelector != null) {
            this.mSelector = this.mConvertSelector;
            setConvertSelector((DrawListener) null);
        }
    }

    private void processReset() {
        if (this.mFocus != null && this.mReset) {
            ItemListener item = this.mFocus.getItem();
            if (item == null) {
                ZpLogger.e(TAG, "processReset: item is null! mFocus:" + this.mFocus);
                return;
            }
            removeNode(item);
            this.mReset = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void drawAlpha(ItemListener item) {
        if (this.mFocus != null) {
            AlphaParams alphaParams = this.mFocus.getParams().getAlphaParams();
            float dstAlpha = alphaParams.getFromAlpha();
            float diffAlpha = alphaParams.getToAlpha() - alphaParams.getFromAlpha();
            float coef = ((float) this.mFrame) / ((float) alphaParams.getAlphaFrameRate());
            Interpolator alphaInterpolator = this.mFocus.getParams().getAlphaParams().getAlphaInteroplator();
            if (alphaInterpolator == null) {
                alphaInterpolator = new LinearInterpolator();
            }
            float dstAlpha2 = dstAlpha + (diffAlpha * alphaInterpolator.getInterpolation(coef));
            if (this.mLastItem == item) {
                dstAlpha2 = alphaParams.getToAlpha();
            }
            if (this.mConvertSelector != null) {
                this.mConvertSelector.setAlpha(dstAlpha2);
                if (this.mSelector != null) {
                    this.mSelector.setAlpha(0.0f);
                }
            } else if (this.mSelector != null) {
                this.mSelector.setAlpha(dstAlpha2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void drawScale(Canvas canvas, ItemListener item) {
        if (this.mFocus != null) {
            ScaleParams scaleParams = this.mFocus.getParams().getScaleParams();
            float itemDiffScaleXValue = scaleParams.getScaleX() - 1.0f;
            float itemDiffScaleYValue = scaleParams.getScaleY() - 1.0f;
            float coef = ((float) this.mFrame) / ((float) scaleParams.getScaleFrameRate());
            Interpolator scaleInterpolator = this.mFocus.getParams().getScaleParams().getScaleInterpolator();
            if (scaleInterpolator == null) {
                scaleInterpolator = new LinearInterpolator();
            }
            float coef2 = scaleInterpolator.getInterpolation(coef);
            float dstScaleXValue = 1.0f + (itemDiffScaleXValue * coef2);
            float dstScaleYValue = 1.0f + (itemDiffScaleYValue * coef2);
            if (this.mLastItem == item) {
                dstScaleXValue = scaleParams.getScaleX();
                dstScaleYValue = scaleParams.getScaleY();
            }
            item.setScaleX(dstScaleXValue);
            item.setScaleY(dstScaleYValue);
        }
    }

    public int getTotalFrame() {
        return Math.max(this.mScaleFrameRate, this.mAlphaFrameRate);
    }

    public boolean isFinished() {
        return isScaleFinished() && isAlphaFinished();
    }

    public boolean isScaleFinished() {
        return this.mFrame > this.mScaleFrameRate;
    }

    public boolean isAlphaFinished() {
        return this.mFrame > this.mAlphaFrameRate;
    }

    public void reset() {
        super.reset();
        this.mReset = true;
    }
}
