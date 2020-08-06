package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import com.yunos.tvtaobao.biz.listener.OnInterceptListener;
import com.zhiping.dev.android.logger.ZpLogger;

public class TvRecyclerView extends LastVisiblePositionRecyclerView {
    private static final String TAG = "TvRecyclerView";
    private int lastExposePosition;
    private OnInterceptListener mInterceptLister;
    private View mItemUpFocusView;
    private int mLastx;
    private int mLoadMoreBeforehandCount;
    private OnKeyListener mOnKeyListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private Scroller mScroller;
    private boolean mSelectedItemCentered;
    private int mSelectedItemOffsetEnd;
    private int mSelectedItemOffsetStart;
    private int mTargetPos;

    public interface OnKeyListener {
        void onKeyEvent(String str, boolean z);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public TvRecyclerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TvRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TvRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLoadMoreBeforehandCount = 0;
        this.lastExposePosition = -1;
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        initView();
        this.mScroller = new Scroller(context);
    }

    private void initView() {
        setDescendantFocusability(262144);
        setHasFixedSize(true);
        setWillNotDraw(true);
        setOverScrollMode(2);
        setChildrenDrawingOrderEnabled(true);
        setClipChildren(false);
        setClipToPadding(false);
        setClickable(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setItemAnimator((RecyclerView.ItemAnimator) null);
    }

    private int getFreeWidth() {
        return (getWidth() - getPaddingLeft()) - getPaddingRight();
    }

    private int getFreeHeight() {
        return (getHeight() - getPaddingTop()) - getPaddingBottom();
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        ZpLogger.i(TAG, ".onFocusChanged");
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public boolean hasFocus() {
        return super.hasFocus();
    }

    public boolean isInTouchMode() {
        if (Build.VERSION.SDK_INT == 19) {
            return !hasFocus() || super.isInTouchMode();
        }
        return super.isInTouchMode();
    }

    public void requestChildFocus(View child, View focused) {
        ZpLogger.i(TAG, ".requestChildFocus");
        if (child != null && this.mSelectedItemCentered) {
            this.mSelectedItemOffsetStart = !isVertical() ? getFreeWidth() - child.getWidth() : getFreeHeight() - child.getHeight();
            this.mSelectedItemOffsetStart /= 2;
            this.mSelectedItemOffsetEnd = this.mSelectedItemOffsetStart;
        }
        super.requestChildFocus(child, focused);
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        int dx;
        int dy;
        ZpLogger.i(TAG, ".requestChildRectangleOnScreen");
        int parentLeft = getPaddingLeft();
        int parentRight = getWidth() - getPaddingRight();
        int parentTop = getPaddingTop();
        int parentBottom = getHeight() - getPaddingBottom();
        int childLeft = child.getLeft() + rect.left;
        int childTop = child.getTop() + rect.top;
        int childRight = childLeft + rect.width();
        int offScreenLeft = Math.min(0, (childLeft - parentLeft) - this.mSelectedItemOffsetStart);
        int offScreenRight = Math.max(0, (childRight - parentRight) + this.mSelectedItemOffsetEnd);
        int offScreenTop = Math.min(0, (childTop - parentTop) - this.mSelectedItemOffsetStart);
        int offScreenBottom = Math.max(0, ((childTop + rect.height()) - parentBottom) + this.mSelectedItemOffsetEnd);
        boolean canScrollHorizontal = getLayoutManager().canScrollHorizontally();
        boolean canScrollVertical = getLayoutManager().canScrollVertically();
        if (!canScrollHorizontal) {
            dx = 0;
        } else if (ViewCompat.getLayoutDirection(this) == 1) {
            if (offScreenRight != 0) {
                dx = offScreenRight;
            } else {
                dx = Math.max(offScreenLeft, childRight - parentRight);
            }
        } else if (offScreenLeft != 0) {
            dx = offScreenLeft;
        } else {
            dx = Math.min(childLeft - parentLeft, offScreenRight);
        }
        if (canScrollVertical) {
            dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
        } else {
            dy = 0;
        }
        if (dx == 0 && dy == 0) {
            return false;
        }
        if (immediate) {
            scrollBy(dx, dy);
        } else {
            smoothScrollBy(dx, dy);
        }
        postInvalidate();
        return true;
    }

    public int getBaseline() {
        return -1;
    }

    public int getSelectedItemOffsetStart() {
        return this.mSelectedItemOffsetStart;
    }

    public int getSelectedItemOffsetEnd() {
        return this.mSelectedItemOffsetEnd;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    private boolean isVertical() {
        if (getLayoutManager() == null) {
            return false;
        }
        if (((LinearLayoutManager) getLayoutManager()).getOrientation() == 1) {
            return true;
        }
        return false;
    }

    public void setSelectedItemOffset(int offsetStart, int offsetEnd) {
        setSelectedItemAtCentered(false);
        this.mSelectedItemOffsetStart = offsetStart;
        this.mSelectedItemOffsetEnd = offsetEnd;
    }

    public void setSelectedItemAtCentered(boolean isCentered) {
        this.mSelectedItemCentered = isCentered;
    }

    public int getFirstVisiblePosition() {
        ZpLogger.i(TAG, ".getFirstVisiblePosition");
        if (getChildCount() == 0) {
            return 0;
        }
        return getChildAdapterPosition(getChildAt(0));
    }

    public int getLastVisiblePosition() {
        ZpLogger.i(TAG, ".getLastVisiblePosition");
        int childCount = getChildCount();
        if (childCount == 0) {
            return 0;
        }
        int lastVisiblePosition = getChildAdapterPosition(getChildAt(childCount - 1));
        if (lastVisiblePosition <= this.lastExposePosition) {
            return lastVisiblePosition;
        }
        setLastExposePosition(lastVisiblePosition);
        return lastVisiblePosition;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setOnInterceptListener(OnInterceptListener listener) {
        this.mInterceptLister = listener;
    }

    /* access modifiers changed from: protected */
    public int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    public void onScrollStateChanged(int state) {
        if (state == 0) {
            getLastVisiblePosition();
            if (this.mOnLoadMoreListener == null) {
                getLastVisiblePosition();
            } else if (getLastVisiblePosition() >= getAdapter().getItemCount() - (this.mLoadMoreBeforehandCount + 1)) {
                this.mOnLoadMoreListener.onLoadMore();
            }
        }
        super.onScrollStateChanged(state);
    }

    public void setOnItemListener(OnKeyListener onItemListener) {
        this.mOnKeyListener = onItemListener;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        ZpLogger.i(TAG, ".dispatchKeyEvent");
        if (this.mInterceptLister != null && this.mInterceptLister.onIntercept(event)) {
            return true;
        }
        boolean result = super.dispatchKeyEvent(event);
        ZpLogger.i(TAG, "dispatchKeyEvent result=" + result);
        View focusView = getFocusedChild();
        if (focusView == null) {
            ZpLogger.i(TAG, "dispatchKeyEvent rtn=" + result);
            return result;
        }
        int dy = 0;
        int dx = 0;
        if (getChildCount() > 0) {
            View firstView = getChildAt(0);
            dy = firstView.getHeight();
            dx = firstView.getWidth();
        }
        if (event.getAction() == 1) {
            ZpLogger.i(TAG, "dispatchKeyEvent rtn=true");
            return true;
        }
        switch (event.getKeyCode()) {
            case 19:
                View upView = FocusFinder.getInstance().findNextFocus(this, focusView, 33);
                ZpLogger.i(TAG, "upView is null:" + (upView == null));
                if (this.mOnKeyListener != null) {
                    OnKeyListener onKeyListener = this.mOnKeyListener;
                    if (upView == null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    onKeyListener.onKeyEvent("up", z);
                }
                if (event.getAction() == 1) {
                    ZpLogger.i(TAG, "dispatchKeyEvent rtn=true");
                    return true;
                } else if (upView != null) {
                    upView.requestFocus();
                    ZpLogger.i(TAG, "dispatchKeyEvent rtn=true");
                    return true;
                } else {
                    if (this.mItemUpFocusView != null) {
                        this.mItemUpFocusView.requestFocus();
                    } else {
                        smoothScrollBy(0, -dy);
                    }
                    ZpLogger.i(TAG, "dispatchKeyEvent rtn=true");
                    return true;
                }
            case 20:
                View downView = FocusFinder.getInstance().findNextFocus(this, focusView, 130);
                StringBuilder append = new StringBuilder().append(" downView is null:");
                if (downView == null) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                ZpLogger.i(TAG, append.append(z2).toString());
                if (downView != null) {
                    downView.requestFocus();
                    ZpLogger.i(TAG, "dispatchKeyEvent rtn=true");
                    return true;
                }
                smoothScrollBy(0, dy);
                ZpLogger.i(TAG, "dispatchKeyEvent rtn=true");
                return true;
            case 21:
                View leftView = FocusFinder.getInstance().findNextFocus(this, focusView, 17);
                StringBuilder append2 = new StringBuilder().append("leftView is null:");
                if (leftView == null) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                ZpLogger.i(TAG, append2.append(z3).toString());
                if (leftView != null) {
                    leftView.requestFocus();
                    ZpLogger.i(TAG, "dispatchKeyEvent rtn=true");
                    return true;
                }
                smoothScrollBy(-dx, 0);
                ZpLogger.i(TAG, "dispatchKeyEvent rtn=true");
                return true;
            case 22:
                View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, 66);
                StringBuilder append3 = new StringBuilder().append("rightView is null:");
                if (rightView == null) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                ZpLogger.i(TAG, append3.append(z4).toString());
                if (rightView != null) {
                    rightView.requestFocus();
                    ZpLogger.i(TAG, "dispatchKeyEvent rtn=true");
                    return true;
                }
                smoothScrollBy(dx, 0);
                ZpLogger.i(TAG, "dispatchKeyEvent rtn=true");
                return true;
            default:
                ZpLogger.i(TAG, "dispatchKeyEvent rtn=" + result);
                return result;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    public void setSelectedPosition(int pos) {
        smoothScrollToPosition(pos);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        if (getLayoutManager() != null) {
            super.onDetachedFromWindow();
        }
    }

    public void setUpFoucsView(View view) {
        this.mItemUpFocusView = view;
    }

    public boolean isRightEdge(int childPosition) {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookUp = gridLayoutManager.getSpanSizeLookup();
            int totalSpanCount = gridLayoutManager.getSpanCount();
            int totalItemCount = gridLayoutManager.getItemCount();
            int childSpanCount = 0;
            for (int i = 0; i <= childPosition; i++) {
                childSpanCount += spanSizeLookUp.getSpanSize(i);
            }
            if (!isVertical()) {
                int lastColumnSize = totalItemCount % totalSpanCount;
                if (lastColumnSize == 0) {
                    lastColumnSize = totalSpanCount;
                }
                if (childSpanCount > totalItemCount - lastColumnSize) {
                    return true;
                }
            } else if (childSpanCount % gridLayoutManager.getSpanCount() == 0) {
                return true;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (isVertical() || childPosition == getLayoutManager().getItemCount() - 1) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isLeftEdge(int childPosition) {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookUp = gridLayoutManager.getSpanSizeLookup();
            int totalSpanCount = gridLayoutManager.getSpanCount();
            int childSpanCount = 0;
            for (int i = 0; i <= childPosition; i++) {
                childSpanCount += spanSizeLookUp.getSpanSize(i);
            }
            if (isVertical()) {
                if (childSpanCount % gridLayoutManager.getSpanCount() == 1) {
                    return true;
                }
            } else if (childSpanCount <= totalSpanCount) {
                return true;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (isVertical() || childPosition == 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isTopEdge(int childPosition) {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookUp = gridLayoutManager.getSpanSizeLookup();
            int totalSpanCount = gridLayoutManager.getSpanCount();
            int childSpanCount = 0;
            for (int i = 0; i <= childPosition; i++) {
                childSpanCount += spanSizeLookUp.getSpanSize(i);
            }
            if (isVertical()) {
                if (childSpanCount <= totalSpanCount) {
                    return true;
                }
            } else if (childSpanCount % totalSpanCount == 1) {
                return true;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (!isVertical() || childPosition == 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isBottomEdge(int childPosition) {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookUp = gridLayoutManager.getSpanSizeLookup();
            int itemCount = gridLayoutManager.getItemCount();
            int childSpanCount = 0;
            int totalSpanCount = gridLayoutManager.getSpanCount();
            for (int i = 0; i <= childPosition; i++) {
                childSpanCount += spanSizeLookUp.getSpanSize(i);
            }
            if (isVertical()) {
                int lastRowCount = itemCount % totalSpanCount;
                if (lastRowCount == 0) {
                    lastRowCount = gridLayoutManager.getSpanCount();
                }
                if (childSpanCount > itemCount - lastRowCount) {
                    return true;
                }
            } else if (childSpanCount % totalSpanCount == 0) {
                return true;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (!isVertical() || childPosition == getLayoutManager().getItemCount() - 1) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mScroller != null && this.mScroller.computeScrollOffset()) {
            ZpLogger.d(TAG, "getCurrX = " + this.mScroller.getCurrX());
            scrollBy(this.mLastx - this.mScroller.getCurrX(), 0);
            this.mLastx = this.mScroller.getCurrX();
            postInvalidate();
        }
    }

    public int getLastExposePosition() {
        return this.lastExposePosition;
    }

    public void setLastExposePosition(int lastExposePosition2) {
        this.lastExposePosition = lastExposePosition2;
    }
}
