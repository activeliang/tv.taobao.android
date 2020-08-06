package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Transformation;
import com.yunos.tvtaobao.tvsdk.utils.SystemProUtils;
import com.yunos.tvtaobao.tvsdk.widget.AdapterView;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.OnScrollListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;

public abstract class AbsGallery extends AbsSpinner implements GestureDetector.OnGestureListener {
    public static final int ACTION_SCROLL_BACKWARD = 8192;
    public static final int ACTION_SCROLL_FORWARD = 4096;
    protected static final int FLAG_SUPPORT_STATIC_TRANSFORMATIONS = 2048;
    protected static final int FLAG_USE_CHILD_DRAWING_ORDER = 1024;
    static final int SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT = 250;
    int mAnimationDuration = 400;
    private AdapterView.AdapterContextMenuInfo mContextMenuInfo;
    Runnable mDisableSuppressSelectionChangedRunnable = new Runnable() {
        public void run() {
            AbsGallery.this.mSuppressSelectionChanged = false;
            AbsGallery.this.selectionChanged();
        }
    };
    int mDownTouchPosition;
    View mDownTouchView;
    GestureDetector mGestureDetector;
    int mGravity;
    boolean mIsFirstScroll;
    protected int mLastScrollState = 0;
    private OnScrollListener mOnScrollListener;
    boolean mReceivedInvokeKeyDown;
    int mSelectedCenterOffset;
    View mSelectedChild;
    boolean mShouldCallbackDuringFling = true;
    boolean mShouldCallbackOnUnselectedItemClick = true;
    boolean mShouldStopFling;
    protected int mSpacing = 0;
    boolean mSuppressSelectionChanged;
    private float mUnselectedAlpha;

    /* access modifiers changed from: protected */
    public abstract boolean scrollToChild(int i);

    public AbsGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public AbsGallery(Context context) {
        super(context);
        init(context);
    }

    public AbsGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        int flags = getGroupFlags() | 1024 | 2048;
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
    }

    /* access modifiers changed from: protected */
    public void reportScrollStateChange(int newState) {
        if (newState != this.mLastScrollState && this.mOnScrollListener != null) {
            this.mLastScrollState = newState;
            this.mOnScrollListener.onScrollStateChanged(this, newState);
        }
    }

    private void init(Context context) {
        this.mGestureDetector = new GestureDetector(context, this);
        this.mGestureDetector.setIsLongpressEnabled(true);
    }

    public void setCallbackDuringFling(boolean shouldCallback) {
        this.mShouldCallbackDuringFling = shouldCallback;
    }

    public void setCallbackOnUnselectedItemClick(boolean shouldCallback) {
        this.mShouldCallbackOnUnselectedItemClick = shouldCallback;
    }

    public void setAnimationDuration(int animationDurationMillis) {
        this.mAnimationDuration = animationDurationMillis;
    }

    public void setSpacing(int spacing) {
        this.mSpacing = spacing;
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            this.mGravity = gravity;
            requestLayout();
        }
    }

    public void setUnselectedAlpha(float unselectedAlpha) {
        this.mUnselectedAlpha = unselectedAlpha;
    }

    /* access modifiers changed from: protected */
    public boolean getChildStaticTransformation(View child, Transformation t) {
        t.clear();
        t.setAlpha(child == this.mSelectedChild ? 1.0f : this.mUnselectedAlpha);
        return true;
    }

    public void offsetChildrenTopAndBottom(int offset) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).offsetTopAndBottom(offset);
        }
    }

    /* access modifiers changed from: package-private */
    public void offsetChildrenLeftAndRight(int offset) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).offsetLeftAndRight(offset);
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchPress(View child) {
        if (child != null) {
            child.setPressed(true);
        }
        setPressed(true);
    }

    /* access modifiers changed from: package-private */
    public void dispatchUnpress() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).setPressed(false);
        }
        setPressed(false);
    }

    /* access modifiers changed from: package-private */
    public void selectionChanged() {
        if (!this.mSuppressSelectionChanged) {
            super.selectionChanged();
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchSetPressed(boolean pressed) {
        if (this.mSelectedChild != null) {
            this.mSelectedChild.setPressed(pressed);
        }
    }

    /* access modifiers changed from: package-private */
    public void onFinishedMovement() {
        if (this.mSuppressSelectionChanged) {
            this.mSuppressSelectionChanged = false;
            super.selectionChanged();
        }
        this.mSelectedCenterOffset = 0;
        invalidate();
    }

    /* access modifiers changed from: protected */
    public ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return this.mContextMenuInfo;
    }

    public boolean showContextMenuForChild(View originalView) {
        int longPressPosition = getPositionForView(originalView);
        if (longPressPosition < 0) {
            return false;
        }
        return dispatchLongPress(originalView, longPressPosition, this.mAdapter.getItemId(longPressPosition));
    }

    public boolean showContextMenu() {
        if (!isPressed() || this.mSelectedPosition < 0) {
            return false;
        }
        return dispatchLongPress(getChildAt(this.mSelectedPosition - this.mFirstPosition), this.mSelectedPosition, this.mSelectedRowId);
    }

    private boolean dispatchLongPress(View view, int position, long id) {
        boolean handled = false;
        if (this.mOnItemLongClickListener != null) {
            handled = this.mOnItemLongClickListener.onItemLongClick(this, this.mDownTouchView, this.mDownTouchPosition, id);
        }
        if (!handled) {
            this.mContextMenuInfo = new AdapterView.AdapterContextMenuInfo(view, position, id);
            handled = super.showContextMenuForChild(this);
        }
        if (handled) {
            performHapticFeedback(0);
        }
        return handled;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return event.dispatch(this, (KeyEvent.DispatcherState) null, (Object) null);
    }

    public void dispatchSetSelected(boolean selected) {
    }

    public void onLongPress(MotionEvent e) {
        if (this.mDownTouchPosition >= 0) {
            performHapticFeedback(0);
            dispatchLongPress(this.mDownTouchView, this.mDownTouchPosition, getItemIdAtPosition(this.mDownTouchPosition));
        }
    }

    public void onShowPress(MotionEvent e) {
    }

    /* access modifiers changed from: protected */
    public void setSelectedPositionInt(int position) {
        super.setSelectedPositionInt(position);
        updateSelectedItemMetadata();
    }

    /* access modifiers changed from: protected */
    public void updateSelectedItemMetadata() {
        View oldSelectedChild = this.mSelectedChild;
        View child = getChildAt(this.mSelectedPosition - this.mFirstPosition);
        this.mSelectedChild = child;
        if (child != null) {
            child.setSelected(true);
            if (hasFocus()) {
            }
            if (oldSelectedChild != null && oldSelectedChild != child) {
                oldSelectedChild.setSelected(false);
                oldSelectedChild.setFocusable(false);
            }
        }
    }

    public boolean onSingleTapUp(MotionEvent e) {
        if (this.mDownTouchPosition < 0) {
            return false;
        }
        scrollToChild(this.mDownTouchPosition - this.mFirstPosition);
        if (this.mShouldCallbackOnUnselectedItemClick || this.mDownTouchPosition == this.mSelectedPosition) {
            performItemClick(this.mDownTouchView, this.mDownTouchPosition, this.mAdapter.getItemId(this.mDownTouchPosition));
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean movePrevious() {
        if (this.mItemCount <= 0 || this.mSelectedPosition <= 0) {
            return false;
        }
        scrollToChild((this.mSelectedPosition - this.mFirstPosition) - 1);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean moveNext() {
        if (this.mItemCount <= 0 || this.mSelectedPosition >= this.mItemCount - 1) {
            return false;
        }
        scrollToChild((this.mSelectedPosition - this.mFirstPosition) + 1);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onUp() {
        dispatchUnpress();
    }

    /* access modifiers changed from: protected */
    public void onCancel() {
        onUp();
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean retValue = this.mGestureDetector.onTouchEvent(event);
        int action = event.getAction();
        if (action == 1) {
            onUp();
        } else if (action == 3) {
            onCancel();
        }
        return retValue;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!this.mShouldCallbackDuringFling) {
            removeCallbacks(this.mDisableSuppressSelectionChangedRunnable);
            if (!this.mSuppressSelectionChanged) {
                this.mSuppressSelectionChanged = true;
            }
        }
        return true;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if (!this.mShouldCallbackDuringFling) {
            if (this.mIsFirstScroll) {
                if (!this.mSuppressSelectionChanged) {
                    this.mSuppressSelectionChanged = true;
                }
                postDelayed(this.mDisableSuppressSelectionChangedRunnable, 250);
            }
        } else if (this.mSuppressSelectionChanged) {
            this.mSuppressSelectionChanged = false;
        }
        this.mIsFirstScroll = false;
        return true;
    }

    public boolean onDown(MotionEvent e) {
        this.mDownTouchPosition = pointToPosition((int) e.getX(), (int) e.getY());
        if (this.mDownTouchPosition >= 0) {
            this.mDownTouchView = getChildAt(this.mDownTouchPosition - this.mFirstPosition);
            this.mDownTouchView.setPressed(true);
        }
        this.mIsFirstScroll = true;
        return true;
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        int selectedIndex = this.mSelectedPosition - this.mFirstPosition;
        if (selectedIndex < 0) {
            return i;
        }
        if (i == childCount - 1) {
            return selectedIndex;
        }
        if (i >= selectedIndex) {
            return i + 1;
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus && this.mSelectedChild != null) {
            this.mSelectedChild.setSelected(true);
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(VGallery.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        boolean z = true;
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(VGallery.class.getName());
        if (this.mItemCount <= 1) {
            z = false;
        }
        info.setScrollable(z);
        if (isEnabled()) {
            if (this.mItemCount > 0 && this.mSelectedPosition < this.mItemCount - 1) {
                info.addAction(4096);
            }
            if (isEnabled() && this.mItemCount > 0 && this.mSelectedPosition > 0) {
                info.addAction(8192);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    public void offsetFocusRect(FocusRectParams rectParam, int offsetX, int offsetY) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            rectParam.focusRect().offset(offsetX, offsetY);
        }
    }

    public void offsetFocusRect(FocusRectParams rectParam, int left, int right, int top, int bottom) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            rectParam.focusRect().left += left;
            rectParam.focusRect().right += right;
            rectParam.focusRect().top += top;
            rectParam.focusRect().bottom += bottom;
        }
    }

    public void offsetFocusRectLeftAndRight(FocusRectParams rectParam, int left, int right) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            rectParam.focusRect().left += left;
            rectParam.focusRect().right += right;
        }
    }

    public void offsetFocusRectTopAndBottom(FocusRectParams rectParam, int top, int bottom) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            rectParam.focusRect().top += top;
            rectParam.focusRect().bottom += bottom;
        }
    }
}
