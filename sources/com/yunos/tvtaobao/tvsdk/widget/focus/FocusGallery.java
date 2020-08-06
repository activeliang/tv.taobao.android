package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Interpolator;
import com.yunos.tvtaobao.tvsdk.widget.Gallery;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DeepListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemSelectedListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;

public class FocusGallery extends Gallery implements DeepListener, ItemListener {
    protected static final boolean DEBUG = false;
    protected static final String TAG = "FocusGallery";
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
    boolean mLayouted = false;
    protected Params mParams = new Params(1.1f, 1.1f, 10, (Interpolator) null, true, 20, new AccelerateDecelerateFrameInterpolator());
    boolean mReset = false;

    public interface GalleyPreKeyListener {
        void preKeyDownListener(View view, int i, KeyEvent keyEvent);
    }

    public FocusGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FocusGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusGallery(Context context) {
        super(context);
    }

    public void setCanDraw(boolean canDraw) {
        this.mCanDraw = canDraw;
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
        if (gainFocus && gainFocus && getChildCount() > 0 && this.mLayouted && getLeftScrollDistance() == 0) {
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
            if ((hasFocus() || hasDeepFocus()) && getChildCount() > 0 && this.mLayouted && getLeftScrollDistance() == 0) {
                reset();
            }
            this.mClipFocusRect.set(0, 0, getWidth(), getHeight());
        }
    }

    public void requestLayout() {
        super.requestLayout();
        this.mLayouted = false;
    }

    /* access modifiers changed from: protected */
    public void layout(int delta, boolean animate) {
        this.mIsRtl = false;
        int childrenLeft = this.mSpinnerPadding.left;
        int childrenWidth = ((getRight() - getLeft()) - this.mSpinnerPadding.left) - this.mSpinnerPadding.right;
        if (this.mDataChanged) {
            handleDataChanged();
        }
        if (this.mItemCount == 0) {
            resetList();
            return;
        }
        int lastPosition = getLastVisiblePosition();
        int selectPosition = this.mSelectedPosition;
        boolean isOutSideVisibleRegion = false;
        int preOffsetX = 0;
        int deltaPosition = 0;
        if (this.mNextSelectedPosition >= 0) {
            deltaPosition = this.mNextSelectedPosition - this.mSelectedPosition;
        }
        int childrenNum = getChildCount();
        if (this.mFirstPosition <= lastPosition) {
            if (this.mSelectedPosition < this.mFirstPosition) {
                isOutSideVisibleRegion = true;
                selectPosition = this.mFirstPosition;
                if (getChildAt(0) != null) {
                    preOffsetX = getChildAt(0).getLeft();
                }
            } else if (this.mSelectedPosition > lastPosition) {
                isOutSideVisibleRegion = true;
                selectPosition = (this.mFirstPosition + childrenNum) - 1;
                if (getChildAt(childrenNum - 1) != null) {
                    preOffsetX = getChildAt(childrenNum - 1).getLeft();
                }
            }
        }
        if (this.mNextSelectedPosition >= 0) {
            setSelectedPositionInt(this.mNextSelectedPosition);
        }
        recycleAllViews();
        detachAllViewsFromParent();
        this.mRightMost = 0;
        this.mLeftMost = 0;
        if (deltaPosition != 0) {
            selectPosition = this.mSelectedPosition;
        }
        this.mFirstPosition = selectPosition;
        View sel = makeAndAddView(selectPosition, 0, preOffsetX, true);
        if (!isOutSideVisibleRegion) {
            sel.offsetLeftAndRight((((childrenWidth / 2) + childrenLeft) - (sel.getWidth() / 2)) + getLeftScrollDistance());
        }
        if (sel != null) {
            positionSelector(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        }
        fillToGalleryRight();
        fillToGalleryLeft();
        this.mRecycler.clear();
        invalidate();
        checkSelectionChanged();
        this.mDataChanged = false;
        this.mNeedSync = false;
        setNextSelectedPositionInt(this.mSelectedPosition);
        updateSelectedItemMetadata();
    }

    /* access modifiers changed from: protected */
    public void reset() {
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
        if (!this.mCanDraw) {
            return this.mCanDraw;
        }
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
            case 21:
                if (getSelectedItemPosition() <= 0) {
                    return true;
                }
                performSelect(false);
                this.mIsAnimate = true;
                setSelectedPositionInt(getSelectedItemPosition() - 1);
                setNextSelectedPositionInt(getSelectedItemPosition() - 1);
                smoothScrollBy(getChildItemWidth() + this.mSpacing);
                if (canDraw()) {
                    this.mReset = false;
                    performSelect(true);
                    return true;
                }
                this.mReset = true;
                return true;
            case 22:
                if (getSelectedItemPosition() >= this.mItemCount - 1 || getSelectedItemPosition() == this.mItemCount - 1) {
                    return true;
                }
                performSelect(false);
                this.mIsAnimate = true;
                setSelectedPositionInt(getSelectedItemPosition() + 1);
                setNextSelectedPositionInt(getSelectedItemPosition() + 1);
                smoothScrollBy(-(getChildItemWidth() + this.mSpacing));
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

    /* access modifiers changed from: protected */
    public int getChildItemWidth() {
        View v = getChildAt(0);
        if (v == null) {
            return 0;
        }
        return v.getWidth();
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        if (checkState(keyCode)) {
            return true;
        }
        switch (keyCode) {
            case 19:
            case 20:
                if (this.mGalleryPreKeyListener != null) {
                    this.mGalleryPreKeyListener.preKeyDownListener(this, keyCode, event);
                    break;
                }
                break;
            case 21:
                if (getSelectedItemPosition() <= 0) {
                    return false;
                }
                return true;
            case 22:
                if (getSelectedItemPosition() >= this.mItemCount - 1) {
                    return false;
                }
                return true;
            case 23:
            case 66:
                return true;
        }
        return false;
    }

    public boolean checkState(int keyCode) {
        if (this.mLastScrollState == 2 && (keyCode == 19 || keyCode == 20)) {
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

    public Rect getClipFocusRect() {
        return this.mClipFocusRect;
    }
}
