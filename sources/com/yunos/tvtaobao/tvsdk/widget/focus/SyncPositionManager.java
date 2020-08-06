package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DrawListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.PositionListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.ScaleParams;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;
import com.zhiping.dev.android.logger.ZpLogger;

public class SyncPositionManager extends PositionManager {
    protected static final boolean DEBUG = false;
    protected static final String TAG = "SyncPositionManager";
    private Rect mCurrFocusRect = new Rect();
    private boolean mIsAnimate = false;
    private Rect mLastFocusRect = new Rect();
    private boolean mReset = false;

    public SyncPositionManager(int focusMode, PositionListener l) {
        super(focusMode, l);
    }

    public void draw(Canvas canvas) {
        boolean z = true;
        preDrawUnscale(canvas);
        super.draw(canvas);
        if (this.mStart) {
            if (!this.mFocus.canDraw()) {
                drawFocus(canvas);
                postDrawUnscale(canvas);
                this.mListener.postInvalidateDelayed(30);
                return;
            }
            ItemListener item = this.mFocus.getItem();
            if (item != null) {
                processReset();
                drawBeforeFocus(item, canvas);
                boolean isInvalidate = false;
                if (!isFinished()) {
                    boolean isScrolling = this.mFocus.isScrolling();
                    if (this.mFrame == 1) {
                        this.mFocus.onFocusStart();
                        onFocusStart();
                    }
                    onFocusProcess();
                    boolean drawFocus = true;
                    if (this.mLastFocusRect.isEmpty()) {
                        drawFocus = false;
                    }
                    if (isScrolling && !isFocusFinished()) {
                        updateDstRect(item);
                    }
                    if (this.mIsAnimate) {
                        if (!isFocusFinished() && drawFocus) {
                            drawFocus(canvas, item);
                            this.mFocusFrame++;
                        }
                    } else if (isScaleFinished()) {
                        if (isScrolling) {
                            updateDstRect(item);
                            this.mFocusRect.set(this.mCurrFocusRect);
                        }
                        drawFocus(canvas);
                    } else {
                        drawFocus = false;
                    }
                    if (!isScaleFinished()) {
                        if (item.isScale()) {
                            if (isScrolling) {
                                updateDstRect(item);
                                this.mFocusRect.set(this.mCurrFocusRect);
                            }
                            if (drawFocus) {
                                z = false;
                            }
                            drawScale(canvas, item, z);
                        } else if (!drawFocus) {
                            drawStaticFocus(canvas, item, this.mFocus.getParams().getScaleParams().getScaleX(), this.mFocus.getParams().getScaleParams().getScaleY());
                        }
                        this.mScaleFrame++;
                    } else if (!drawFocus) {
                        if (isScrolling) {
                            updateDstRect(item);
                            this.mFocusRect.set(this.mCurrFocusRect);
                        }
                        drawFocus(canvas);
                    }
                    this.mFrame++;
                    isInvalidate = true;
                    if (this.mFrame == getTotalFrame()) {
                        this.mFocus.onFocusFinished();
                        onFocusFinished();
                    }
                } else if (this.mFocus.isScrolling()) {
                    drawStaticFocus(canvas, item);
                    this.mLastFocusRect.set(this.mFocusRect);
                    this.mCurrFocusRect.set(this.mFocusRect);
                    isInvalidate = true;
                } else if (this.mForceDrawFocus) {
                    drawStaticFocus(canvas, item);
                    isInvalidate = true;
                    this.mForceDrawFocus = false;
                } else {
                    drawFocus(canvas);
                }
                if (!item.isFinished()) {
                    isInvalidate = true;
                }
                if (!this.mPause && (isInvalidate || this.mSelector.isDynamicFocus())) {
                    this.mListener.invalidate();
                }
                drawAfterFocus(item, canvas);
                postDrawUnscale(canvas);
            }
        }
    }

    private void onFocusStart() {
        if (this.mSelectorPolator == null) {
            this.mSelectorPolator = new AccelerateDecelerateFrameInterpolator();
        }
    }

    private void onFocusFinished() {
        resetSelector();
    }

    public void resetSelector() {
        if (this.mConvertSelector != null) {
            this.mSelector = this.mConvertSelector;
            setConvertSelector((DrawListener) null);
        }
    }

    private void onFocusProcess() {
        int totalFrame = getTotalFrame();
        float alpha1 = this.mSelectorPolator.getInterpolation((((float) this.mFrame) * 1.0f) / ((float) totalFrame));
        float alpha2 = 1.0f - this.mSelectorPolator.getInterpolation((((float) this.mFrame) * 1.0f) / ((float) totalFrame));
        if (this.mFrame >= totalFrame || !this.mIsAnimate) {
            resetSelector();
        }
        if (this.mConvertSelector != null && this.mFrame < totalFrame && this.mIsAnimate) {
            this.mConvertSelector.setAlpha(alpha1);
            if (this.mSelector != null) {
                this.mSelector.setAlpha(alpha2);
            }
        } else if (this.mSelector != null) {
            this.mSelector.setAlpha(1.0f);
        }
    }

    private void processReset() {
        if (this.mReset) {
            ScaleParams scaleParams = this.mFocus.getParams().getScaleParams();
            ItemListener item = this.mFocus.getItem();
            if (item == null) {
                ZpLogger.e(TAG, "processReset: item is null! mFocus:" + this.mFocus);
                return;
            }
            removeScaleNode(item);
            scaleParams.computeScaleXY(item.getItemWidth(), item.getItemHeight());
            this.mLastFocusRect.set(this.mFocusRect);
            updateDstRect(item);
            this.mIsAnimate = this.mFocus.isAnimate();
            this.mReset = false;
        }
    }

    private void updateDstRect(ItemListener item) {
        this.mCurrFocusRect.set(getFinalRect(item));
    }

    private void drawFocus(Canvas canvas, ItemListener item) {
        float coef = ((float) this.mFrame) / ((float) this.mFocus.getParams().getFocusParams().getFocusFrameRate());
        Interpolator focusInterpolator = this.mFocus.getParams().getFocusParams().getFocusInterpolator();
        if (focusInterpolator == null) {
            focusInterpolator = new LinearInterpolator();
        }
        float coef2 = focusInterpolator.getInterpolation(coef);
        this.mFocusRect.left = (int) (((float) this.mLastFocusRect.left) + (((float) (this.mCurrFocusRect.left - this.mLastFocusRect.left)) * coef2));
        this.mFocusRect.right = (int) (((float) this.mLastFocusRect.right) + (((float) (this.mCurrFocusRect.right - this.mLastFocusRect.right)) * coef2));
        this.mFocusRect.top = (int) (((float) this.mLastFocusRect.top) + (((float) (this.mCurrFocusRect.top - this.mLastFocusRect.top)) * coef2));
        this.mFocusRect.bottom = (int) (((float) this.mLastFocusRect.bottom) + (((float) (this.mCurrFocusRect.bottom - this.mLastFocusRect.bottom)) * coef2));
        drawFocus(canvas);
    }

    private void drawScale(Canvas canvas, ItemListener item, boolean drawFocus) {
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
        item.setScaleX(dstScaleXValue);
        item.setScaleY(dstScaleYValue);
        if (drawFocus) {
            drawStaticFocus(canvas, item, dstScaleXValue, dstScaleYValue);
        }
    }

    public boolean isFinished() {
        return this.mFrame > getTotalFrame();
    }

    public int getTotalFrame() {
        return Math.max(this.mFocusFrameRate, this.mScaleFrameRate);
    }

    private boolean isFocusFinished() {
        return this.mFrame > this.mFocusFrameRate;
    }

    private boolean isScaleFinished() {
        return this.mFrame > this.mScaleFrameRate;
    }

    public void reset() {
        super.reset();
        this.mReset = true;
    }
}
