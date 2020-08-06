package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusDrawStateListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusStateListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;

public class FocusNoDeepFrameLayout extends FrameLayout implements FocusListener, ItemListener {
    boolean mAimateWhenGainFocusFromDown = true;
    boolean mAimateWhenGainFocusFromLeft = true;
    boolean mAimateWhenGainFocusFromRight = true;
    boolean mAimateWhenGainFocusFromUp = true;
    private boolean mCanDraw = true;
    private Rect mClipFocusRect = new Rect();
    private Rect mCustomerPaddingRect;
    boolean mFocusBackground = false;
    protected FocusDrawStateListener mFocusDrawStateListener = null;
    protected FocusRectParams mFocusRectparams = new FocusRectParams();
    protected FocusStateListener mFocusStateListener = null;
    boolean mIsAnimate = true;
    protected Params mParams = new Params(1.05f, 1.05f, 10, (Interpolator) null, true, 20, new AccelerateDecelerateFrameInterpolator());

    public FocusNoDeepFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FocusNoDeepFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusNoDeepFrameLayout(Context context) {
        super(context);
    }

    public void setOnFocusStateListener(FocusStateListener l) {
        this.mFocusStateListener = l;
    }

    public void setOnFocusDrawStateListener(FocusDrawStateListener l) {
        this.mFocusDrawStateListener = l;
    }

    public void setAnimateWhenGainFocus(boolean fromleft, boolean fromUp, boolean fromRight, boolean fromDown) {
        this.mAimateWhenGainFocusFromLeft = fromleft;
        this.mAimateWhenGainFocusFromUp = fromUp;
        this.mAimateWhenGainFocusFromRight = fromRight;
        this.mAimateWhenGainFocusFromDown = fromDown;
    }

    public void setFocusBackground(boolean back) {
        this.mFocusBackground = back;
    }

    public void setFocusCanDraw(boolean can) {
        this.mCanDraw = can;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.mIsAnimate = checkAnimate(direction);
    }

    private boolean checkAnimate(int direction) {
        switch (direction) {
            case 17:
                if (!this.mAimateWhenGainFocusFromRight) {
                    return false;
                }
                return true;
            case 33:
                if (!this.mAimateWhenGainFocusFromDown) {
                    return false;
                }
                return true;
            case 66:
                if (!this.mAimateWhenGainFocusFromLeft) {
                    return false;
                }
                return true;
            case 130:
                return this.mAimateWhenGainFocusFromUp;
            default:
                return true;
        }
    }

    public void setCustomerFocusPaddingRect(Rect rect) {
        this.mCustomerPaddingRect = rect;
    }

    public FocusRectParams getFocusParams() {
        Rect r = new Rect();
        getFocusedRect(r);
        if (this.mCustomerPaddingRect != null) {
            r.left += this.mCustomerPaddingRect.left;
            r.top += this.mCustomerPaddingRect.top;
            r.right -= this.mCustomerPaddingRect.right;
            r.bottom -= this.mCustomerPaddingRect.bottom;
        }
        this.mFocusRectparams.set(r, 0.5f, 0.5f);
        return this.mFocusRectparams;
    }

    public boolean canDraw() {
        return this.mCanDraw;
    }

    public boolean isAnimate() {
        return this.mIsAnimate;
    }

    public boolean isScale() {
        return true;
    }

    public Params getParams() {
        if (this.mParams != null) {
            return this.mParams;
        }
        throw new IllegalArgumentException("The params is null, you must call setScaleParams before it's running");
    }

    public ItemListener getItem() {
        return this;
    }

    public boolean isScrolling() {
        return false;
    }

    public int getItemWidth() {
        return getWidth();
    }

    public int getItemHeight() {
        return getHeight();
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 23:
            case 66:
                return true;
            default:
                return false;
        }
    }

    public Rect getManualPadding() {
        return null;
    }

    public void onFocusStart() {
        if (this.mFocusStateListener != null) {
            this.mFocusStateListener.onFocusStart(this, (View) getParent());
        }
    }

    public void onFocusFinished() {
        if (this.mFocusStateListener != null) {
            this.mFocusStateListener.onFocusFinished(this, (View) getParent());
        }
    }

    public boolean isFocusBackground() {
        return this.mFocusBackground;
    }

    public void drawBeforeFocus(Canvas canvas) {
    }

    public void drawAfterFocus(Canvas canvas) {
    }

    public boolean isFinished() {
        return true;
    }

    public Rect getClipFocusRect() {
        if (this.mClipFocusRect != null) {
            return this.mClipFocusRect;
        }
        return new Rect();
    }
}
