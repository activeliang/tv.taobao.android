package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Interpolator;
import com.yunos.tvtaobao.tvsdk.widget.VGallery;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DeepListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemSelectedListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;

public class FocusVGallery extends VGallery implements DeepListener, ItemListener {
    protected static boolean DEBUG = false;
    protected static String TAG = "FocusVGallery";
    boolean mAimateWhenGainFocusFromDown = true;
    boolean mAimateWhenGainFocusFromLeft = true;
    boolean mAimateWhenGainFocusFromRight = true;
    boolean mAimateWhenGainFocusFromUp = true;
    boolean mCanDraw = true;
    protected Rect mClipFocusRect = new Rect();
    boolean mDeepFocus = false;
    boolean mFocusBackground = false;
    protected FocusRectParams mFocusRectparams = new FocusRectParams();
    GalleyPreKeyListener mGalleryPreKeyListener;
    boolean mIsAnimate = true;
    ItemSelectedListener mItemSelectedListener;
    public boolean mLayouted = false;
    protected Params mParams = new Params(1.1f, 1.1f, 10, (Interpolator) null, true, 20, new AccelerateDecelerateFrameInterpolator());
    boolean mReset = false;

    public interface GalleyPreKeyListener {
        void preKeyDownListener(View view, int i, KeyEvent keyEvent);
    }

    public FocusVGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FocusVGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusVGallery(Context context) {
        super(context);
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

    public void setPreKeyListener(GalleyPreKeyListener l) {
        this.mGalleryPreKeyListener = l;
    }

    public void setOnItemSelectedListener(ItemSelectedListener listener) {
        this.mItemSelectedListener = listener;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        ZpLogger.d(TAG, "onFocusChanged");
        if (gainFocus && gainFocus && getChildCount() > 0 && this.mLayouted) {
            reset();
        }
        this.mIsAnimate = checkAnimate(direction);
        performSelect(gainFocus);
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

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mLayouted = true;
        if (isLayoutRequested()) {
            if ((hasFocus() || hasDeepFocus()) && getChildCount() > 0 && this.mLayouted) {
                reset();
            }
            this.mClipFocusRect.set(0, 0, getWidth(), getHeight());
        }
    }

    public void requestLayout() {
        super.requestLayout();
        this.mLayouted = false;
    }

    private void reset() {
        ItemListener item = (ItemListener) getSelectedView();
        if (item != null) {
            this.mFocusRectparams.set(item.getFocusParams());
            offsetDescendantRectToMyCoords(getSelectedView(), this.mFocusRectparams.focusRect());
        }
    }

    public Params getParams() {
        if (this.mParams != null) {
            return this.mParams;
        }
        throw new IllegalArgumentException("The params is null, you must call setScaleParams before it's running");
    }

    public boolean canDraw() {
        boolean z = true;
        if (this.mItemCount <= 0) {
            return false;
        }
        if (getSelectedView() != null && this.mReset) {
            performSelect(true);
            this.mReset = false;
        }
        if (getSelectedView() == null || !this.mLayouted) {
            z = false;
        }
        return z;
    }

    public boolean isAnimate() {
        return this.mIsAnimate;
    }

    public ItemListener getItem() {
        return (ItemListener) getSelectedView();
    }

    public boolean isScrolling() {
        return isFling();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ZpLogger.d(TAG, "onKeyDown keyCode = " + keyCode);
        if (checkState(keyCode)) {
            return true;
        }
        switch (keyCode) {
            case 19:
                if (getSelectedItemPosition() <= 0) {
                    return true;
                }
                performSelect(false);
                this.mIsAnimate = true;
                setSelectedPositionInt(getSelectedItemPosition() - 1);
                setNextSelectedPositionInt(getSelectedItemPosition() - 1);
                smoothScrollBy(getChildAt(0).getHeight() + this.mSpacing);
                if (canDraw()) {
                    this.mReset = false;
                    performSelect(true);
                    return true;
                }
                this.mReset = true;
                return true;
            case 20:
                if (getSelectedItemPosition() >= this.mItemCount - 1 || getSelectedItemPosition() == this.mItemCount - 1) {
                    return true;
                }
                performSelect(false);
                this.mIsAnimate = true;
                setSelectedPositionInt(getSelectedItemPosition() + 1);
                setNextSelectedPositionInt(getSelectedItemPosition() + 1);
                smoothScrollBy(-(getChildAt(0).getHeight() + this.mSpacing));
                if (canDraw()) {
                    this.mReset = false;
                    performSelect(true);
                    return true;
                }
                this.mReset = true;
                return true;
            default:
                this.mIsAnimate = false;
                return super.onKeyDown(keyCode, event);
        }
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        ZpLogger.d(TAG, "preOnKeyDown keyCode = " + keyCode);
        if (checkState(keyCode)) {
            return true;
        }
        if (getChildCount() <= 0) {
            return false;
        }
        switch (keyCode) {
            case 19:
                if (getSelectedItemPosition() <= 0) {
                    return false;
                }
                return true;
            case 20:
                if (getSelectedItemPosition() >= this.mItemCount - 1) {
                    return false;
                }
                return true;
            case 21:
            case 22:
                if (this.mGalleryPreKeyListener != null) {
                    this.mGalleryPreKeyListener.preKeyDownListener(this, keyCode, event);
                    break;
                }
                break;
            case 23:
            case 66:
                return true;
        }
        return false;
    }

    public boolean checkState(int keyCode) {
        if (this.mLastScrollState == 2 && (keyCode == 21 || keyCode == 22)) {
            return true;
        }
        return false;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (views == null || !isFocusable()) {
            return;
        }
        if ((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) {
            views.add(this);
        }
    }

    private void performSelect(boolean select) {
        if (this.mItemSelectedListener != null) {
            this.mItemSelectedListener.onItemSelected(getSelectedView(), getSelectedItemPosition(), select, this);
        }
    }

    public FocusRectParams getFocusParams() {
        if (getSelectedView() != null) {
            if (this.mFocusRectparams == null || isScrolling()) {
                reset();
            }
            return this.mFocusRectparams;
        }
        Rect r = new Rect();
        getFocusedRect(r);
        this.mFocusRectparams.set(r, 0.5f, 0.5f);
        return this.mFocusRectparams;
    }

    public boolean canDeep() {
        return true;
    }

    public boolean hasDeepFocus() {
        return this.mDeepFocus;
    }

    public void onFocusDeeped(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        this.mDeepFocus = gainFocus;
        onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void onItemSelected(boolean selected) {
        performSelect(selected);
    }

    public void onItemClick() {
        if (getSelectedView() != null) {
            performItemClick(getSelectedView(), getSelectedItemPosition(), 0);
        }
    }

    public boolean isScale() {
        return true;
    }

    public int getItemWidth() {
        return getWidth();
    }

    public int getItemHeight() {
        return getHeight();
    }

    public Rect getManualPadding() {
        return null;
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

    public boolean isFlingRunnableFinish() {
        if (getFlingRunnable() == null) {
            return true;
        }
        return getFlingRunnable().isFinished();
    }

    public Rect getClipFocusRect() {
        return this.mClipFocusRect;
    }
}
