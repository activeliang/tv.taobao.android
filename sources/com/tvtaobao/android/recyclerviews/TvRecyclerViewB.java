package com.tvtaobao.android.recyclerviews;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import java.util.ArrayList;

public class TvRecyclerViewB extends RecyclerView {
    private static final String TAG = TvRecyclerViewB.class.getSimpleName();
    private boolean isFirstChildFocus = true;
    private int lastExposePosition = -1;
    private boolean mCanFocusOutHorizontal = true;
    private boolean mCanFocusOutVertical = false;
    private boolean mEnableLoadMore = false;
    private FocusGainListener mFocusGainListener;
    private FocusLostListener mFocusLostListener;
    private int mLastFocusPosition = 0;
    private View mLastFocusView = null;
    private int mLoadMoreBeforehandCount = 0;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnFocusSearchIntercept onFocusSearchIntercept;

    public interface FocusGainListener {
        void onFocusGain(View view, View view2);
    }

    public interface FocusLostListener {
        boolean onFocusLost(View view, int i);
    }

    public interface OnFocusSearchIntercept {
        View onInterceptFocusSearch(View view, int i);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public TvRecyclerViewB(Context context) {
        super(context);
        init();
    }

    public TvRecyclerViewB(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TvRecyclerViewB(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        setItemAnimator((RecyclerView.ItemAnimator) null);
        setDescendantFocusability(262144);
        setChildrenDrawingOrderEnabled(true);
        setFocusable(true);
    }

    public void setEnableLoadMore(boolean enableLoadMore) {
        this.mEnableLoadMore = enableLoadMore;
    }

    public void setLoadMoreBeforehandCount(int loadMoreBeforehandCount) {
        this.mLoadMoreBeforehandCount = loadMoreBeforehandCount;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setIsFirstChildFocus(boolean isFocus) {
        this.isFirstChildFocus = isFocus;
    }

    public void setOnFocusSearchIntercept(OnFocusSearchIntercept onFocusSearchIntercept2) {
        this.onFocusSearchIntercept = onFocusSearchIntercept2;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (hasFocus() || this.mLastFocusView == null) {
            super.addFocusables(views, direction, focusableMode);
            return;
        }
        View tmp = getLayoutManager().findViewByPosition(this.mLastFocusPosition);
        if (tmp != null) {
            views.add(tmp);
        } else {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    public View focusSearch(View focused, int direction) {
        View interceptView;
        try {
            View realNextFocus = super.focusSearch(focused, direction);
            if (this.onFocusSearchIntercept != null && (interceptView = this.onFocusSearchIntercept.onInterceptFocusSearch(focused, direction)) != null) {
                return interceptView;
            }
            View nextFocus = FocusFinder.getInstance().findNextFocus(this, focused, direction);
            switch (direction) {
                case 17:
                    if (nextFocus != null) {
                        return realNextFocus;
                    }
                    if (!this.mCanFocusOutHorizontal) {
                        return null;
                    }
                    if (this.mFocusLostListener == null || !this.mFocusLostListener.onFocusLost(focused, direction)) {
                        return realNextFocus;
                    }
                    return null;
                case 33:
                    if (nextFocus != null || canScrollVertically(-1)) {
                        return realNextFocus;
                    }
                    if (!this.mCanFocusOutVertical) {
                        return null;
                    }
                    if (this.mFocusLostListener == null || !this.mFocusLostListener.onFocusLost(focused, direction)) {
                        return realNextFocus;
                    }
                    return null;
                case 66:
                    if (nextFocus != null) {
                        return realNextFocus;
                    }
                    if (!this.mCanFocusOutHorizontal) {
                        return null;
                    }
                    if (this.mFocusLostListener == null || !this.mFocusLostListener.onFocusLost(focused, direction)) {
                        return realNextFocus;
                    }
                    return null;
                case 130:
                    if (nextFocus != null || canScrollVertically(1)) {
                        return realNextFocus;
                    }
                    if (!this.mCanFocusOutVertical) {
                        return null;
                    }
                    if (this.mFocusLostListener == null || !this.mFocusLostListener.onFocusLost(focused, direction)) {
                        return realNextFocus;
                    }
                    return null;
                default:
                    return realNextFocus;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public void requestChildFocus(View child, View focused) {
        if (child != null) {
            if (!hasFocus() && this.mFocusGainListener != null) {
                this.mFocusGainListener.onFocusGain(child, focused);
            }
            this.mLastFocusView = focused;
            this.mLastFocusPosition = getChildViewHolder(child).getAdapterPosition();
        }
        super.requestChildFocus(child, focused);
        try {
            Rect rect1 = new Rect();
            getDrawingRect(rect1);
            Rect rect2 = new Rect();
            View leafView = findFocus();
            leafView.getDrawingRect(rect2);
            offsetDescendantRectToMyCoords(leafView, rect2);
            int dy = rect2.centerY() - rect1.centerY();
            int firstLinePosition = 0;
            if (getLayoutManager() instanceof GridLayoutManager) {
                firstLinePosition = ((GridLayoutManager) getLayoutManager()).getSpanCount() - 1;
            }
            if (getChildAdapterPosition(child) <= firstLinePosition) {
                smoothScrollToPosition(0);
            } else if (0 != 0 || dy != 0) {
                smoothScrollBy(0, dy);
                postInvalidate();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void onScrollStateChanged(int state) {
        if (this.mEnableLoadMore && state == 0) {
            int lastVisiblePosition = getLastVisiblePosition();
            if (this.mOnLoadMoreListener != null && lastVisiblePosition >= getAdapter().getItemCount() - (this.mLoadMoreBeforehandCount + 1)) {
                this.mOnLoadMoreListener.onLoadMore();
            }
        }
        super.onScrollStateChanged(state);
    }

    public void onChildAttachedToWindow(View child) {
        if (this.isFirstChildFocus && child.isFocusable()) {
            child.requestFocus();
            this.isFirstChildFocus = false;
        }
        super.onChildAttachedToWindow(child);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        View nextFocusView;
        View nextFocusView2;
        try {
            if (event.getAction() == 0) {
                int keyCode = event.getKeyCode();
                View focusedView = getFocusedChild();
                if (keyCode == 20) {
                    try {
                        nextFocusView2 = FocusFinder.getInstance().findNextFocus(this, focusedView, 130);
                    } catch (Exception e) {
                        nextFocusView2 = null;
                    }
                    if (nextFocusView2 == null) {
                        focusedView.requestFocus();
                        return true;
                    }
                } else if (keyCode == 19) {
                    try {
                        nextFocusView = FocusFinder.getInstance().findNextFocus(this, focusedView, 33);
                    } catch (Exception e2) {
                        nextFocusView = null;
                    }
                    if (nextFocusView == null) {
                        if ((getLayoutManager() instanceof GridLayoutManager) && getChildAdapterPosition(focusedView) > ((GridLayoutManager) getLayoutManager()).getSpanCount()) {
                            focusedView.requestFocus();
                            return true;
                        } else if (!this.mCanFocusOutVertical) {
                            focusedView.requestFocus();
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e3) {
        }
        return super.dispatchKeyEvent(event);
    }

    public int getLastVisiblePosition() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return 0;
        }
        int lastVisiblePosition = getChildAdapterPosition(getChildAt(childCount - 1));
        if (lastVisiblePosition <= this.lastExposePosition) {
            return lastVisiblePosition;
        }
        this.lastExposePosition = lastVisiblePosition;
        return lastVisiblePosition;
    }

    public int getLastFocusPosition() {
        return this.mLastFocusPosition;
    }

    public View getLastFocusView() {
        return this.mLastFocusView;
    }

    public void setLastFocusView(View lastFocusView) {
        this.mLastFocusView = lastFocusView;
    }

    public int getLastExposePosition() {
        return this.lastExposePosition;
    }

    public boolean isCanFocusOutVertical() {
        return this.mCanFocusOutVertical;
    }

    public void setCanFocusOutVertical(boolean canFocusOutVertical) {
        this.mCanFocusOutVertical = canFocusOutVertical;
    }

    public boolean isCanFocusOutHorizontal() {
        return this.mCanFocusOutHorizontal;
    }

    public void setCanFocusOutHorizontal(boolean canFocusOutHorizontal) {
        this.mCanFocusOutHorizontal = canFocusOutHorizontal;
    }

    public void setFocusLostListener(FocusLostListener focusLostListener) {
        this.mFocusLostListener = focusLostListener;
    }

    public void setGainFocusListener(FocusGainListener focusListener) {
        this.mFocusGainListener = focusListener;
    }
}
