package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import com.yunos.tvtaobao.tvsdk.utils.SystemProUtils;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.OnScrollListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;

public class FocusScrollerLinearLayout extends FocusLinearLayout {
    protected static final boolean DEBUG = false;
    public static final int HORIZONTAL_FULL = 2;
    public static final int HORIZONTAL_INVALID = -1;
    public static final int HORIZONTAL_OUTSIDE_FULL = 4;
    public static final int HORIZONTAL_OUTSIDE_SINGEL = 3;
    public static final int HORIZONTAL_SINGEL = 1;
    public static final int MAX_VALUE = Integer.MAX_VALUE;
    public static final int MIN_VALUE = Integer.MIN_VALUE;
    private static final int SCROLL_DURATION = 100;
    protected static final String TAG = "FocusScrollerLinearLayout";
    private int mCenterX = 960;
    private int mCenterY = 360;
    private int mDuration = 500;
    private int mHorizontalBaseLine = 0;
    private boolean mIsCheckBottom = true;
    private boolean mIsCheckTop = true;
    int mLastCode = 0;
    private int mLastHorizontalDirection = 0;
    private int mLastScrollState = 0;
    private int mLastVerticalDirection = 0;
    private int mLeftMoveOffset = 0;
    private int mManualPaddingBottom = 20;
    private int mManualPaddingRight = 20;
    private int mMaxBottom;
    private int mMaxLeft;
    View mMaxLeftView = null;
    private int mMaxRight;
    View mMaxRightView = null;
    private int mMaxScaledBottom;
    private int mMaxScaledLeft;
    private int mMaxScaledRight;
    private int mMaxScaledTop;
    private int mMaxTop;
    private int mMinBottom;
    private int mMinLeft;
    View mMinLeftView = null;
    private int mMinRight;
    View mMinRightView = null;
    private int mMinScaledBottom;
    private int mMinScaledLeft;
    private int mMinScaledRight;
    private int mMinScaledTop;
    private int mMinTop;
    View mMinTopView = null;
    private onNegativeScreenListener mNegativeScreenListener = null;
    private OutsideScrollListener mOutsideScrollListener = null;
    private int mScrollMode = 1;
    private int mScrollX = 0;
    private int mScrollY = 0;
    private Scroller mScroller;
    private OnScrollListener mScrollerListener = null;
    private int mTopMoveOffset = 0;
    private int mVerticalBaseLine = 0;
    View maxBottomView = null;
    View maxTopView = null;
    View minBottomView = null;

    public interface OutsideScrollListener {
        int getCurrX();

        int getCurrY();

        void smoothOutsideScrollBy(int i, int i2);
    }

    public interface onNegativeScreenListener {
        void onNegativeScreenStateChanged(boolean z);
    }

    public FocusScrollerLinearLayout(Context context) {
        super(context);
        this.mScroller = new Scroller(context);
    }

    public FocusScrollerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mScroller = new Scroller(context);
    }

    public FocusScrollerLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mScroller = new Scroller(context);
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mScrollerListener = l;
    }

    public void setHorizontalMode(int mode) {
        this.mScrollMode = mode;
    }

    public void setOutsideScrollListener(OutsideScrollListener l) {
        this.mOutsideScrollListener = l;
    }

    public void setManualPaddingRight(int padding) {
        this.mManualPaddingRight = padding;
    }

    public void setManualPaddingBottom(int padding) {
        this.mManualPaddingBottom = padding;
    }

    public void setCenter(int centerX, int centerY) {
        this.mCenterX = centerX;
        this.mCenterY = centerY;
    }

    public void setScrollDuration(int duration) {
        this.mDuration = duration;
    }

    public int getLeftMoveHideOffset() {
        return this.mLeftMoveOffset;
    }

    public int getVerticalBaseLine() {
        return this.mVerticalBaseLine;
    }

    public void setVerticalBaseLine(int verticalBaseLine) {
        this.mVerticalBaseLine = verticalBaseLine;
    }

    public int getHorizontalBaseLine() {
        return this.mHorizontalBaseLine;
    }

    public void setHorizontalBaseLine(int horizontalBaseLine) {
        this.mHorizontalBaseLine = horizontalBaseLine;
    }

    public int getTopMoveHideOffset() {
        return this.mTopMoveOffset;
    }

    public void setSelectedView(View v, int direction, boolean isSync) {
        setSelectedView(v, direction);
        int code = getKeyCode(direction);
        this.mLastCode = -1;
        if (isSync) {
            scrollSingel(code, 0);
            return;
        }
        requestLayout();
        this.mLastCode = code;
    }

    /* access modifiers changed from: package-private */
    public int getKeyCode(int direction) {
        switch (direction) {
            case 33:
                return 19;
            case 66:
                return 22;
            case 130:
                return 20;
            default:
                return 21;
        }
    }

    /* access modifiers changed from: protected */
    public void initNode() {
        beforeInitNode();
        if (this.mNeedInitNode) {
            if (getChildCount() > 0) {
                this.mMinLeft = Integer.MAX_VALUE;
                this.mMaxLeft = Integer.MIN_VALUE;
                this.mMinRight = Integer.MAX_VALUE;
                this.mMaxRight = Integer.MIN_VALUE;
                this.mMinTop = Integer.MAX_VALUE;
                this.mMaxTop = Integer.MIN_VALUE;
                this.mMinBottom = Integer.MAX_VALUE;
                this.mMaxBottom = Integer.MIN_VALUE;
                for (int index = 0; index < getChildCount(); index++) {
                    View child = getChildAt(index);
                    if (!(child == null || child.getVisibility() == 8)) {
                        int leftOffset = 0;
                        int rightOffset = 0;
                        int topOffset = 0;
                        int bottomOffset = 0;
                        ViewGroup.LayoutParams obj = child.getLayoutParams();
                        if (obj instanceof ViewGroup.MarginLayoutParams) {
                            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) obj;
                            leftOffset = lp.leftMargin;
                            rightOffset = lp.rightMargin;
                            topOffset = lp.topMargin;
                            bottomOffset = lp.bottomMargin;
                        }
                        if (child.getLeft() - leftOffset < this.mMinLeft) {
                            this.mMinLeftView = child;
                            this.mMinLeft = child.getLeft() - leftOffset;
                        }
                        if (child.getLeft() - leftOffset > this.mMaxLeft) {
                            this.mMaxLeftView = child;
                            this.mMaxLeft = child.getLeft() - leftOffset;
                        }
                        if (child.getRight() + rightOffset < this.mMinRight) {
                            this.mMinRightView = child;
                            this.mMinRight = child.getRight() + rightOffset;
                        }
                        if (child.getRight() + rightOffset > this.mMaxRight) {
                            this.mMaxRightView = child;
                            this.mMaxRight = child.getRight() + rightOffset;
                        }
                        if (child.getTop() - topOffset < this.mMinTop) {
                            this.mMinTopView = child;
                            this.mMinTop = child.getTop() - topOffset;
                        }
                        if (child.getTop() - topOffset > this.mMaxTop) {
                            this.maxTopView = child;
                            this.mMaxTop = child.getTop() - topOffset;
                        }
                        if (child.getBottom() + bottomOffset < this.mMinBottom) {
                            this.minBottomView = child;
                            this.mMinBottom = child.getBottom() + bottomOffset;
                        }
                        if (child.getBottom() + bottomOffset > this.mMaxBottom) {
                            this.maxBottomView = child;
                            this.mMaxBottom = child.getBottom() + bottomOffset;
                        }
                    }
                }
                ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) getLayoutParams();
                if (lp2 != null) {
                    int leftMargin = lp2.leftMargin;
                    int topMargin = lp2.topMargin;
                    int rightMargin = lp2.rightMargin;
                    int bottomMargin = lp2.bottomMargin;
                    this.mMinLeft += leftMargin - rightMargin;
                    this.mMaxLeft += leftMargin - rightMargin;
                    this.mMinRight += leftMargin - rightMargin;
                    this.mMaxRight += leftMargin - rightMargin;
                    this.mMinTop += topMargin - bottomMargin;
                    this.mMaxTop += topMargin - bottomMargin;
                    this.mMinBottom += topMargin - bottomMargin;
                    this.mMaxBottom += topMargin - bottomMargin;
                }
                if (getChildCount() > 0) {
                    Rect r = new Rect();
                    FocusRectParams focusParams = null;
                    if (this.mMinLeftView != null && (this.mMinLeftView instanceof ItemListener)) {
                        focusParams = getScaleRectParams((ItemListener) this.mMinLeftView, r);
                    }
                    if (focusParams == null) {
                        focusParams = getFocusParams();
                        getFocusedRect(r);
                    }
                    this.mMinScaledLeft = ScalePositionManager.instance().getScaledRect(r, this.mParams.getScaleParams().getScaleX(), this.mParams.getScaleParams().getScaleY(), focusParams.coefX(), focusParams.coefY()).left;
                    if (this.mMaxLeftView != null && (this.mMaxLeftView instanceof ItemListener)) {
                        focusParams = getScaleRectParams((ItemListener) this.mMaxLeftView, r);
                    }
                    if (focusParams == null) {
                        focusParams = getFocusParams();
                        getFocusedRect(r);
                    }
                    this.mMaxScaledLeft = ScalePositionManager.instance().getScaledRect(r, this.mParams.getScaleParams().getScaleX(), this.mParams.getScaleParams().getScaleY(), focusParams.coefX(), focusParams.coefY()).left;
                    if (this.mMinRightView != null && (this.mMinRightView instanceof ItemListener)) {
                        focusParams = getScaleRectParams((ItemListener) this.mMinRightView, r);
                    }
                    if (focusParams == null) {
                        focusParams = getFocusParams();
                        getFocusedRect(r);
                    }
                    this.mMinScaledRight = ScalePositionManager.instance().getScaledRect(r, this.mParams.getScaleParams().getScaleX(), this.mParams.getScaleParams().getScaleY(), focusParams.coefX(), focusParams.coefY()).right;
                    if (this.mMaxRightView != null && (this.mMaxRightView instanceof ItemListener)) {
                        focusParams = getScaleRectParams((ItemListener) this.mMaxRightView, r);
                    }
                    if (focusParams == null) {
                        focusParams = getFocusParams();
                        getFocusedRect(r);
                    }
                    this.mMaxScaledRight = ScalePositionManager.instance().getScaledRect(r, this.mParams.getScaleParams().getScaleX(), this.mParams.getScaleParams().getScaleY(), focusParams.coefX(), focusParams.coefY()).right;
                    if (this.mMinTopView != null && (this.mMinTopView instanceof ItemListener)) {
                        focusParams = getScaleRectParams((ItemListener) this.mMinTopView, r);
                    }
                    if (focusParams == null) {
                        focusParams = getFocusParams();
                        getFocusedRect(r);
                    }
                    this.mMinScaledTop = ScalePositionManager.instance().getScaledRect(r, this.mParams.getScaleParams().getScaleX(), this.mParams.getScaleParams().getScaleY(), focusParams.coefX(), focusParams.coefY()).top;
                    if (this.maxTopView != null && (this.maxTopView instanceof ItemListener)) {
                        focusParams = getScaleRectParams((ItemListener) this.maxTopView, r);
                    }
                    if (focusParams == null) {
                        focusParams = getFocusParams();
                        getFocusedRect(r);
                    }
                    this.mMaxScaledTop = ScalePositionManager.instance().getScaledRect(r, this.mParams.getScaleParams().getScaleX(), this.mParams.getScaleParams().getScaleY(), focusParams.coefX(), focusParams.coefY()).top;
                    if (this.minBottomView != null && (this.minBottomView instanceof ItemListener)) {
                        focusParams = getScaleRectParams((ItemListener) this.minBottomView, r);
                    }
                    if (focusParams == null) {
                        focusParams = getFocusParams();
                        getFocusedRect(r);
                    }
                    this.mMinScaledBottom = ScalePositionManager.instance().getScaledRect(r, this.mParams.getScaleParams().getScaleX(), this.mParams.getScaleParams().getScaleY(), focusParams.coefX(), focusParams.coefY()).bottom;
                    if (this.maxBottomView != null && (this.maxBottomView instanceof ItemListener)) {
                        focusParams = getScaleRectParams((ItemListener) this.maxBottomView, r);
                    }
                    if (focusParams == null) {
                        focusParams = getFocusParams();
                        getFocusedRect(r);
                    }
                    this.mMaxScaledBottom = ScalePositionManager.instance().getScaledRect(r, this.mParams.getScaleParams().getScaleX(), this.mParams.getScaleParams().getScaleY(), focusParams.coefX(), focusParams.coefY()).bottom;
                }
            } else {
                return;
            }
        }
        super.initNode();
    }

    private void beforeInitNode() {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
        if (lp != null) {
            if (this.mLeftMoveOffset != lp.leftMargin) {
                this.mLeftMoveOffset = lp.leftMargin;
            }
            if (this.mTopMoveOffset != lp.topMargin) {
                this.mTopMoveOffset = lp.topMargin;
            }
        }
    }

    private FocusRectParams getScaleRectParams(ItemListener listener, Rect rect) {
        if (listener == null) {
            return null;
        }
        FocusRectParams focusParams = listener.getFocusParams();
        rect.set(focusParams.focusRect());
        offsetDescendantRectToMyCoords((View) listener, rect);
        return focusParams;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean hr = super.onKeyDown(keyCode, event);
        if (hr) {
            scrollSingel(keyCode, this.mDuration);
        }
        return hr;
    }

    public void computeScroll() {
        if (this.mScrollMode == 2 || this.mScrollMode == 1) {
            if (this.mScroller.computeScrollOffset()) {
                int offsetX = this.mScroller.getCurrX() - this.mScrollX;
                int offsetY = this.mScroller.getCurrY() - this.mScrollY;
                this.mScrollX = this.mScroller.getCurrX();
                this.mScrollY = this.mScroller.getCurrY();
                offsetFocusRect(offsetX, offsetY);
                scrollTo(this.mScrollX, this.mScrollY);
                invalidate();
            }
            if (this.mScroller.isFinished()) {
                reportScrollStateChange(0);
            }
        }
        super.computeScroll();
    }

    public void offsetFocusRect(int offsetX, int offsetY) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            this.mFocusRectparams.focusRect().offset(offsetX, offsetY);
        }
    }

    public boolean smoothScrollBy(int dx, int dy, int duration) {
        if (dx == 0 && dy == 0) {
            if (!this.mScroller.isFinished()) {
                offsetFocusRect(-(this.mScroller.getFinalX() - this.mScroller.getCurrX()), -(this.mScroller.getFinalY() - this.mScroller.getCurrY()));
            }
            return false;
        }
        this.mScrollX = this.mScroller.getCurrX();
        this.mScrollY = this.mScroller.getCurrY();
        if (!this.mScroller.isFinished()) {
            this.mScroller.forceFinished(true);
        }
        this.mScroller.startScroll(this.mScrollX, this.mScrollY, dx, dy, duration);
        reportScrollStateChange(2);
        invalidate();
        return true;
    }

    /* access modifiers changed from: protected */
    public int getLeftX() {
        return this.mScroller.getFinalX() - this.mScroller.getCurrX();
    }

    /* access modifiers changed from: protected */
    public int getLeftY() {
        return this.mScroller.getFinalY() - this.mScroller.getCurrY();
    }

    /* access modifiers changed from: package-private */
    public void reportScrollStateChange(int newState) {
        if (newState != this.mLastScrollState && this.mScrollerListener != null) {
            this.mLastScrollState = newState;
            this.mScrollerListener.onScrollStateChanged(this, newState);
        }
    }

    /* access modifiers changed from: package-private */
    public void scrollSingel(int keyCode, int duration) {
        int[] diff = getScrollByDiff(keyCode);
        if (smoothScrollBy(diff[0], diff[1], duration)) {
            offsetFocusRect(-diff[0], -diff[1]);
        }
        if (keyCode == 22 || keyCode == 21) {
            this.mLastHorizontalDirection = keyCode;
        } else if (keyCode == 20 || keyCode == 19) {
            this.mLastVerticalDirection = keyCode;
        }
    }

    /* access modifiers changed from: protected */
    public int getOffset(int keyCode) {
        View selectedView = getSelectedView();
        Rect visibleRect = new Rect();
        getGlobalVisibleRect(visibleRect);
        int remainScrollX = 0;
        int remainScrollY = 0;
        if (this.mScroller != null && !this.mScroller.isFinished()) {
            remainScrollX = this.mScroller.getFinalX() - this.mScroller.getCurrX();
            remainScrollY = this.mScroller.getFinalY() - this.mScroller.getCurrY();
        }
        switch (keyCode) {
            case 19:
                int selectCenterY = getCenterY(selectedView);
                int diff = (this.mCenterY + getScrollY()) - selectCenterY;
                int visibleTopBind = visibleRect.top + getScrollY();
                int i = visibleRect.bottom + this.mManualPaddingBottom;
                if (diff > 0) {
                    boolean isShowNegative = true;
                    if ((visibleTopBind + remainScrollY) - diff < (this.mMinTop - this.mTopMoveOffset) + this.mHorizontalBaseLine) {
                        if (selectCenterY < (this.mMinTop - this.mTopMoveOffset) + this.mHorizontalBaseLine) {
                            if (this.mNegativeScreenListener != null) {
                                this.mNegativeScreenListener.onNegativeScreenStateChanged(true);
                            }
                            return this.mTopMoveOffset - getScrollY();
                        }
                        diff = Math.min(visibleTopBind, diff);
                        if (visibleTopBind - diff < this.mMinTop - this.mTopMoveOffset) {
                            isShowNegative = false;
                        }
                    }
                    if (diff > 0) {
                        if (this.mNegativeScreenListener != null) {
                            this.mNegativeScreenListener.onNegativeScreenStateChanged(isShowNegative);
                        }
                        int diff2 = -diff;
                        if (visibleTopBind + diff2 > this.mMinTop || !this.mIsCheckTop) {
                            return diff2;
                        }
                        return this.mMinTop - visibleTopBind;
                    }
                }
                return 0;
            case 20:
                int selectCenterY2 = getCenterY(selectedView);
                int diff3 = selectCenterY2 - (this.mCenterY + getScrollY());
                int visibleBottomBind = visibleRect.bottom + getScrollY();
                int visibleTopBind2 = visibleRect.top + getScrollY();
                if (visibleTopBind2 + remainScrollY >= this.mMinTop - this.mTopMoveOffset || selectCenterY2 <= (this.mMinTop - this.mTopMoveOffset) + this.mHorizontalBaseLine) {
                    if (diff3 > 0) {
                        if (visibleBottomBind + diff3 > this.mMaxBottom + this.mManualPaddingBottom && this.mIsCheckBottom) {
                            diff3 = (this.mMaxBottom + this.mManualPaddingBottom) - visibleBottomBind;
                        }
                        if (diff3 > 0) {
                            if (this.mNegativeScreenListener == null) {
                                return diff3;
                            }
                            this.mNegativeScreenListener.onNegativeScreenStateChanged(true);
                            return diff3;
                        }
                    }
                    return 0;
                }
                if (this.mNegativeScreenListener != null) {
                    this.mNegativeScreenListener.onNegativeScreenStateChanged(false);
                }
                return -visibleTopBind2;
            case 21:
                int selectCenterX = getCenterX(selectedView);
                int diff4 = (this.mCenterX + getScrollX()) - selectCenterX;
                int visibleLeftBind = visibleRect.left + getScrollX();
                int i2 = visibleRect.right + this.mManualPaddingRight;
                if (diff4 > 0) {
                    boolean isShowNegative2 = true;
                    if ((visibleLeftBind + remainScrollX) - diff4 < (this.mMinLeft - this.mLeftMoveOffset) + this.mVerticalBaseLine) {
                        if (selectCenterX < (this.mMinLeft - this.mLeftMoveOffset) + this.mVerticalBaseLine) {
                            if (this.mNegativeScreenListener != null) {
                                this.mNegativeScreenListener.onNegativeScreenStateChanged(true);
                            }
                            return this.mLeftMoveOffset - getScrollX();
                        }
                        diff4 = Math.min(visibleLeftBind, diff4);
                        if (visibleLeftBind - diff4 <= this.mMinLeft - this.mLeftMoveOffset) {
                            isShowNegative2 = false;
                        }
                    }
                    if (diff4 > 0) {
                        if (this.mNegativeScreenListener != null) {
                            this.mNegativeScreenListener.onNegativeScreenStateChanged(isShowNegative2);
                        }
                        int diff5 = -diff4;
                        if (visibleLeftBind + diff5 <= this.mMinLeft) {
                            return this.mMinLeft - visibleLeftBind;
                        }
                        return diff5;
                    }
                }
                return 0;
            case 22:
                int selectCenterX2 = getCenterX(selectedView);
                int diff6 = selectCenterX2 - (this.mCenterX + getScrollX());
                int visibleRightBind = visibleRect.right + getScrollX() + this.mManualPaddingRight;
                int visibleLeftBind2 = visibleRect.left + getScrollX();
                if (visibleLeftBind2 + remainScrollX < this.mMinLeft - this.mLeftMoveOffset) {
                    if (selectCenterX2 > (this.mMinLeft - this.mLeftMoveOffset) + this.mVerticalBaseLine) {
                        if (this.mNegativeScreenListener != null) {
                            this.mNegativeScreenListener.onNegativeScreenStateChanged(false);
                        }
                        return -visibleLeftBind2;
                    }
                } else if (diff6 > 0) {
                    if (visibleRightBind + diff6 > this.mMaxRight + this.mManualPaddingRight) {
                        diff6 = (this.mMaxRight + this.mManualPaddingRight) - visibleRightBind;
                    } else if (selectCenterX2 < this.mVerticalBaseLine) {
                        diff6 = 0;
                    }
                    if (diff6 > 0) {
                        if (this.mNegativeScreenListener == null) {
                            return diff6;
                        }
                        this.mNegativeScreenListener.onNegativeScreenStateChanged(true);
                        return diff6;
                    }
                }
                return 0;
            default:
                return 0;
        }
    }

    /* access modifiers changed from: protected */
    public int getCenterX(View v) {
        int selectCenterX;
        if ((this.mFocusRectparams.centerMode() & 4) == 4) {
            selectCenterX = (this.mFocusRectparams.focusRect().left + this.mFocusRectparams.focusRect().right) / 2;
        } else if ((this.mFocusRectparams.centerMode() & 1) == 1) {
            selectCenterX = (v.getLeft() + v.getRight()) / 2;
        } else {
            throw new IllegalArgumentException("FocusScrollerRelativeLayout: getCenterX: mFocusRectparams.centerMode() = " + this.mFocusRectparams.centerMode());
        }
        return this.mLeftMoveOffset + selectCenterX;
    }

    /* access modifiers changed from: protected */
    public int getCenterY(View v) {
        int selectCenterY;
        if ((this.mFocusRectparams.centerMode() & 64) == 64) {
            selectCenterY = (this.mFocusRectparams.focusRect().top + this.mFocusRectparams.focusRect().bottom) / 2;
        } else if ((this.mFocusRectparams.centerMode() & 16) == 16) {
            selectCenterY = (v.getTop() + v.getBottom()) / 2;
        } else {
            throw new IllegalArgumentException("FocusScrollerRelativeLayout: getCenterY: mFocusRectparams.centerMode() = " + this.mFocusRectparams.centerMode());
        }
        return this.mTopMoveOffset + selectCenterY;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        offsetFocusRect(-getLeftX(), -getLeftY());
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus && previouslyFocusedRect != null) {
            previouslyFocusedRect.offset(getScrollX(), getScrollY());
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public int[] getScrollByDiff(int keyCode) {
        int[] diff = new int[2];
        if (keyCode == 22) {
            diff[0] = getOffset(keyCode);
            diff[1] = 0;
            if (this.mLastVerticalDirection == 20 || this.mLastVerticalDirection == 19) {
                diff[1] = getOffset(this.mLastVerticalDirection);
            }
        } else if (keyCode == 21) {
            diff[0] = getOffset(keyCode);
            diff[1] = 0;
            if (this.mLastVerticalDirection == 20 || this.mLastVerticalDirection == 19) {
                diff[1] = getOffset(this.mLastVerticalDirection);
            }
        } else if (keyCode == 20) {
            diff[1] = getOffset(keyCode);
            diff[0] = 0;
            if (this.mLastHorizontalDirection == 21 || this.mLastHorizontalDirection == 22) {
                diff[0] = getOffset(this.mLastHorizontalDirection);
            }
        } else if (keyCode == 19) {
            diff[1] = getOffset(keyCode);
            diff[0] = 0;
            if (this.mLastHorizontalDirection == 21 || this.mLastHorizontalDirection == 22) {
                diff[0] = getOffset(this.mLastHorizontalDirection);
            }
        }
        return diff;
    }

    public void isCheckBottom(boolean isCheck) {
        this.mIsCheckBottom = isCheck;
    }

    public void isCheckTop(boolean isCheck) {
        this.mIsCheckTop = isCheck;
    }

    public onNegativeScreenListener getNegativeScreenListener() {
        return this.mNegativeScreenListener;
    }

    public void setNegativeScreenListener(onNegativeScreenListener negativeScreenListener) {
        this.mNegativeScreenListener = negativeScreenListener;
    }

    public boolean isScrolling() {
        boolean z = true;
        if (this.mDeep == null) {
            if (this.mScroller.isFinished()) {
                z = false;
            }
            return z;
        } else if (this.mDeep.isScrolling() || !this.mScroller.isFinished()) {
            return true;
        } else {
            return false;
        }
    }
}
