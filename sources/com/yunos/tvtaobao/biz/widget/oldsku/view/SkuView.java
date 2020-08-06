package com.yunos.tvtaobao.biz.widget.oldsku.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import com.zhiping.dev.android.logger.ZpLogger;

public class SkuView extends ViewGroup {
    private final String TAG = "SkuView";
    private Drawable mFocusDrawable;
    private boolean mHorizontal;
    private boolean mIsLayoutDone;
    private int mItemSpace;
    private View.OnFocusChangeListener mOnFocusChangeListener;
    private OnScrollStateChangedListener mOnScrollStateChangedListener;
    private OnSelectedItemListener mOnSelectedItemListener;
    private int mReferenceDistance;
    private int mScrollDuration;
    private ScrollState mScrollState;
    private Scroller mScroller;
    private int mSelectedIndex;
    private View mSelectedView;

    protected enum ItemActionDirection {
        CURRENT,
        BEFORE,
        AFTER
    }

    public interface OnScrollStateChangedListener {
        void onScrollStateChanged(ScrollState scrollState);
    }

    public interface OnSelectedItemListener {
        void onSelectedItemListener(int i, View view, boolean z);
    }

    public enum ScrollState {
        IDLE,
        SCROLLING
    }

    public SkuView(Context context) {
        super(context);
        init();
    }

    public SkuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SkuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setItemSpace(int space) {
        this.mItemSpace = space;
    }

    public void setFocusDrawable(Drawable focusDrawable) {
        this.mFocusDrawable = focusDrawable;
    }

    public void setReferenceDistance(int distance) {
        this.mReferenceDistance = distance;
    }

    public void setOnSelectedItemListener(OnSelectedItemListener l) {
        this.mOnSelectedItemListener = l;
    }

    public void setOnScrollStateChangedListener(OnScrollStateChangedListener l) {
        this.mOnScrollStateChangedListener = l;
    }

    public void setDefaultSelectedItem(int index) {
        this.mSelectedIndex = index;
    }

    public void setScrollDuration(int time) {
        this.mScrollDuration = time;
    }

    public View getSelectedView() {
        return this.mSelectedView;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int selectedIndex = this.mSelectedIndex;
        if (keyCode == 21) {
            if (selectedIndex > 0) {
                int index = checkEnableItemIndex(selectedIndex, ItemActionDirection.BEFORE);
                if (index == selectedIndex) {
                    return true;
                }
                startScrollTarget(index);
                return true;
            }
        } else if (keyCode == 22 && selectedIndex < getChildCount() - 1) {
            int index2 = checkEnableItemIndex(selectedIndex, ItemActionDirection.AFTER);
            if (index2 == selectedIndex) {
                return true;
            }
            startScrollTarget(index2);
            return true;
        }
        if ((23 != keyCode && 66 != keyCode) || this.mSelectedView == null) {
            return super.onKeyUp(keyCode, event);
        }
        this.mSelectedView.performClick();
        return true;
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mScroller.computeScrollOffset()) {
            scrollBy(this.mScroller.getCurrX() - getScrollX(), this.mScroller.getCurrY() - getScrollY());
            invalidate();
            return;
        }
        stopScroll();
    }

    public void setOnFocusChangeListener(View.OnFocusChangeListener l) {
        this.mOnFocusChangeListener = l;
        super.setOnFocusChangeListener(l);
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        ZpLogger.i("SkuView", "onFocusChanged gainFocus=" + gainFocus);
        if (this.mOnFocusChangeListener != null) {
            this.mOnFocusChangeListener.onFocusChange(this, gainFocus);
        }
        if (gainFocus) {
            selectedDefaultPosition(this.mSelectedIndex);
            return;
        }
        performSelected(-1);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (!(this.mFocusDrawable == null || this.mSelectedView == null)) {
            int left = getScrollX();
            int top = getScrollY();
            if (this.mHorizontal) {
                left += this.mReferenceDistance;
            } else {
                top += this.mReferenceDistance;
            }
            this.mFocusDrawable.setBounds(left, top, left + this.mSelectedView.getWidth(), top + this.mSelectedView.getHeight());
            this.mFocusDrawable.draw(canvas);
        }
        super.dispatchDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (!(child == null || child.getVisibility() == 8)) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                child.layout(left, top, left + childWidth, top + childHeight);
                if (this.mHorizontal) {
                    left += this.mItemSpace + childWidth;
                } else {
                    top += this.mItemSpace + childHeight;
                }
            }
        }
        this.mIsLayoutDone = true;
        ZpLogger.i("SkuView", "onLayout focused=" + isFocused() + " mSelectedView=" + this.mSelectedView);
        if (this.mSelectedView == null) {
            selectedDefaultPosition(this.mSelectedIndex);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child != null) {
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                if (this.mHorizontal) {
                    if (lp == null || lp.height != -2) {
                        child.measure(0, heightMeasureSpec);
                    } else {
                        child.measure(0, 0);
                    }
                } else if (lp == null || lp.width != -2) {
                    child.measure(widthMeasureSpec, 0);
                } else {
                    child.measure(0, 0);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public int checkEnableItemIndex(int position, ItemActionDirection direction) {
        switch (direction) {
            case CURRENT:
                View childView = getChildAt(position);
                if (childView == null) {
                    return -1;
                }
                if (childView.isEnabled()) {
                    return position;
                }
                int count = getChildCount();
                for (int i = position + 1; i < count; i++) {
                    View view = getChildAt(i);
                    if (view != null && view.isEnabled()) {
                        return i;
                    }
                }
                for (int i2 = position - 1; i2 >= 0; i2--) {
                    View view2 = getChildAt(i2);
                    if (view2 != null && view2.isEnabled()) {
                        return i2;
                    }
                }
                return -1;
            case AFTER:
                int nextPosition = position + 1;
                View childView2 = getChildAt(nextPosition);
                if (childView2 == null) {
                    return position;
                }
                if (childView2.isEnabled()) {
                    return nextPosition;
                }
                int count2 = getChildCount();
                for (int i3 = nextPosition + 1; i3 < count2; i3++) {
                    View view3 = getChildAt(i3);
                    if (view3 != null && view3.isEnabled()) {
                        return i3;
                    }
                }
                return position;
            case BEFORE:
                int prePosition = position - 1;
                View childView3 = getChildAt(prePosition);
                if (childView3 == null) {
                    return position;
                }
                if (childView3.isEnabled()) {
                    return prePosition;
                }
                for (int i4 = prePosition - 1; i4 >= 0; i4--) {
                    View view4 = getChildAt(i4);
                    if (view4 != null && view4.isEnabled()) {
                        return i4;
                    }
                }
                return position;
            default:
                return -1;
        }
    }

    /* access modifiers changed from: protected */
    public void moveItemToReferenceDistance() {
        if (this.mHorizontal) {
            scrollTo(-this.mReferenceDistance, 0);
        } else {
            scrollTo(0, -this.mReferenceDistance);
        }
        invalidate();
    }

    private void startScrollTarget(int index) {
        performSelected(index);
        View selectedView = getChildAt(index);
        if (selectedView != null) {
            int target = this.mReferenceDistance;
            int moveX = 0;
            int moveY = 0;
            if (this.mHorizontal) {
                moveX = (selectedView.getLeft() - target) - getScrollX();
            } else {
                moveY = (selectedView.getTop() - target) - getScrollY();
            }
            ZpLogger.i("SkuView", "startScrollTarget index=" + index + " target=" + target + " moveX=" + moveX + " moveY=" + moveY + " scroll=" + getScrollX());
            if (!(moveX == 0 && moveY == 0)) {
                scrollStateChanged(ScrollState.SCROLLING);
                this.mScroller.abortAnimation();
                this.mScroller.startScroll(getScrollX(), getScrollY(), moveX, moveY, this.mScrollDuration);
            }
            invalidate();
        }
    }

    private void performSelected(int index) {
        if (this.mSelectedView != null) {
            this.mSelectedView.setSelected(false);
            if (this.mOnSelectedItemListener != null) {
                this.mOnSelectedItemListener.onSelectedItemListener(this.mSelectedIndex, this.mSelectedView, false);
            }
            this.mSelectedView = null;
        }
        if (isFocused() && index >= 0) {
            View selectedView = getChildAt(index);
            if (selectedView != null) {
                selectedView.setSelected(true);
            }
            if (this.mOnSelectedItemListener != null) {
                this.mOnSelectedItemListener.onSelectedItemListener(index, selectedView, true);
            }
            this.mSelectedView = selectedView;
            this.mSelectedIndex = index;
        }
    }

    private void selectedDefaultPosition(int defalutIndex) {
        int count;
        int index;
        ZpLogger.i("SkuView", "selectedDefaultPosition mIsLayoutDone=" + this.mIsLayoutDone + " count=" + getChildCount() + " defalutIndex=" + defalutIndex);
        if (this.mIsLayoutDone && (count = getChildCount()) > 0 && defalutIndex >= 0 && defalutIndex < count && (index = checkEnableItemIndex(defalutIndex, ItemActionDirection.CURRENT)) >= 0) {
            startScrollTarget(index);
        }
    }

    private void stopScroll() {
        if (this.mScrollState == ScrollState.SCROLLING) {
            scrollStateChanged(ScrollState.IDLE);
        }
    }

    private void scrollStateChanged(ScrollState newState) {
        ZpLogger.i("SkuView", "scrollStateChanged mScrollState=" + this.mScrollState + " newState=" + newState);
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mOnScrollStateChangedListener != null) {
                this.mOnScrollStateChangedListener.onScrollStateChanged(newState);
            }
        }
    }

    private void init() {
        this.mScrollState = ScrollState.IDLE;
        this.mHorizontal = true;
        this.mScroller = new Scroller(getContext());
        this.mScrollDuration = 1000;
    }
}
