package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import com.zhiping.dev.android.logger.ZpLogger;

public class CenterRecyclerView extends RecyclerView {
    private static final String TAG = "CenterRecyclerView";
    private View curFocusChildView;
    private int lastExposePosition = -1;
    private int mLastFocusPosition;
    private int mLoadMoreBeforehandCount = 4;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnDispatchKeyEventFocusViewIntercept onDispatchKeyEventFocusViewIntercept;

    public interface OnDispatchKeyEventFocusViewIntercept {
        View onInterceptDispatchKeyEventFocusView(KeyEvent keyEvent);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public CenterRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public CenterRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CenterRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        this.curFocusChildView = null;
    }

    public void setOnDispatchKeyEventFocusViewIntercept(OnDispatchKeyEventFocusViewIntercept onDispatchKeyEventIntercept) {
        this.onDispatchKeyEventFocusViewIntercept = onDispatchKeyEventIntercept;
    }

    public int getLastFocusPosition() {
        return this.mLastFocusPosition;
    }

    public void computeScroll() {
        super.computeScroll();
    }

    private boolean isCenter(View view) {
        ZpLogger.i(TAG, "CenterRecyclerView.isCenter Height : " + getHeight());
        ZpLogger.i(TAG, "CenterRecyclerView.isCenter view.Height : " + view.getHeight() + " ,view.Y : " + view.getY());
        return ((float) (getHeight() / 2)) == view.getY() + ((float) (view.getHeight() / 2));
    }

    public void refreshFocusItemToCenter(View child) {
        int dx;
        ZpLogger.i(TAG, "CenterRecyclerView.refreshFocusItemToCenter view : " + child);
        if (child != null) {
            Rect mTempRect = new Rect();
            mTempRect.set(0, 0, child.getWidth(), child.getHeight());
            ZpLogger.i(TAG, "fixed position: " + mTempRect);
            int childWidth = child.getWidth();
            int childHeight = child.getHeight();
            int parentLeft = (((getWidth() - getPaddingLeft()) - getPaddingRight()) / 2) - (childWidth / 2);
            int parentTop = (((getHeight() - getPaddingTop()) - getPaddingBottom()) / 2) - (childHeight / 2);
            ZpLogger.i(TAG, "onRequestChildFocus child : " + child);
            int childLeft = child.getLeft() + mTempRect.left;
            int childTop = child.getTop() + mTempRect.top;
            int childRight = childLeft + mTempRect.width();
            int childBottom = childTop + mTempRect.height();
            int offScreenLeft = Math.min(0, childLeft - parentLeft);
            int offScreenTop = Math.min(0, childTop - parentTop);
            int offScreenRight = Math.max(0, childRight - (parentLeft + childWidth));
            int offScreenBottom = Math.max(0, childBottom - (parentTop + childHeight));
            if (offScreenLeft != 0) {
                dx = offScreenLeft;
            } else {
                dx = Math.min(childLeft - parentLeft, offScreenRight);
            }
            int dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
            if (dx != 0 || dy != 0) {
                smoothScrollBy(dx, dy);
            }
        }
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        this.mLastFocusPosition = getChildViewHolder(child).getAdapterPosition();
        ZpLogger.i(TAG, "CenterRecyclerView.requestChildFocus child : " + child + " " + focused);
        if (focused != this.curFocusChildView) {
            this.curFocusChildView = focused;
            refreshFocusItemToCenter(focused);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public void onScrollStateChanged(int state) {
        if (state == 0 && this.mOnLoadMoreListener != null) {
            if (getLastVisiblePosition() >= getAdapter().getItemCount() - (this.mLoadMoreBeforehandCount + 1)) {
                this.mOnLoadMoreListener.onLoadMore();
                ZpLogger.i(TAG, "lastVisiblePosition = " + getLastVisiblePosition() + " , itemCount = " + (getAdapter().getItemCount() - (this.mLoadMoreBeforehandCount + 1)));
            }
            ZpLogger.i(TAG, "LastVisiblePosition = " + getLastVisiblePosition() + " , itemCount = " + (getAdapter().getItemCount() - (this.mLoadMoreBeforehandCount + 1)));
        }
        super.onScrollStateChanged(state);
    }

    public int getLastVisiblePosition() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return 0;
        }
        int position = getChildAdapterPosition(getChildAt(childCount - 1));
        if (position <= this.lastExposePosition) {
            return position;
        }
        setLastExposePosition(position);
        return position;
    }

    public void setCurrentItemToCenter(int position) {
        if (getLayoutManager().getChildCount() > 0) {
            int itemHeight = getLayoutManager().getChildAt(0).getHeight();
            scrollBy(0, ((itemHeight * position) - (getHeight() / 2)) + (itemHeight / 2));
        }
    }

    public View findCurFocused() {
        ZpLogger.e(TAG, "CenterRecyclerView.findCurFocused curFocusChildView : " + this.curFocusChildView);
        if (this.curFocusChildView != null) {
            return this.curFocusChildView;
        }
        for (int i = 0; i < getChildCount(); i++) {
            View firstView = getChildAt(i);
            if (firstView != null && firstView.hasFocusable()) {
                return firstView;
            }
        }
        return null;
    }

    public View findNextFocused(View focused, int direction) {
        if (direction == 66) {
            return findCurFocused();
        }
        return null;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        View view;
        if (event.getKeyCode() == 4 || event.getKeyCode() == 111) {
            return super.dispatchKeyEvent(event);
        }
        boolean dispatchKeyEvent = super.dispatchKeyEvent(event);
        if (this.onDispatchKeyEventFocusViewIntercept == null || (view = this.onDispatchKeyEventFocusViewIntercept.onInterceptDispatchKeyEventFocusView(event)) == null) {
            View focusView = getFocusedChild();
            if (focusView == null) {
                return dispatchKeyEvent;
            }
            int dy = 0;
            if (getChildCount() > 0) {
                View firstView = getChildAt(0);
                dy = firstView.getHeight();
                int dx = firstView.getWidth();
            }
            if (event.getAction() == 1) {
                return true;
            }
            switch (event.getKeyCode()) {
                case 19:
                    View upView = FocusFinder.getInstance().findNextFocus(this, focusView, 33);
                    ZpLogger.i(TAG, "upView is null:" + (upView == null));
                    if (event.getAction() == 1) {
                        return true;
                    }
                    if (upView != null) {
                        upView.requestFocus();
                        return true;
                    }
                    smoothScrollBy(0, -dy);
                    return true;
                case 20:
                    View downView = FocusFinder.getInstance().findNextFocus(this, focusView, 130);
                    ZpLogger.i(TAG, " downView is null:" + (downView == null));
                    if (downView != null) {
                        downView.requestFocus();
                        return true;
                    }
                    smoothScrollBy(0, dy);
                    return true;
                case 21:
                    View leftView = FocusFinder.getInstance().findNextFocus(this, focusView, 17);
                    ZpLogger.i(TAG, "leftView is null:" + (leftView == null));
                    if (leftView == null) {
                        return super.dispatchKeyEvent(event);
                    }
                    leftView.requestFocus();
                    return true;
                case 22:
                    View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, 66);
                    ZpLogger.i(TAG, "rightView is null:" + (rightView == null));
                    if (rightView == null) {
                        return super.dispatchKeyEvent(event);
                    }
                    rightView.requestFocus();
                    return true;
                default:
                    return dispatchKeyEvent;
            }
        } else {
            view.requestFocus();
            return true;
        }
    }

    public int getLastExposePosition() {
        return this.lastExposePosition;
    }

    public void setLastExposePosition(int lastExposePosition2) {
        this.lastExposePosition = lastExposePosition2;
    }
}
