package com.powyin.scroll.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.ListView;
import android.widget.ScrollView;
import com.powyin.scroll.R;
import com.powyin.scroll.widget.ISwipe;
import com.powyin.scroll.widget.SwipeController;
import com.uc.webview.export.extension.UCCore;

public class SwipeNest extends ViewGroup implements NestedScrollingParent, ISwipe, ScrollingView {
    private ValueAnimator animationScrollY;
    private int mActivePointerId;
    private int mContentScroll;
    private float mDragBeginDirect;
    private float mDragBeginY;
    private float mDragLastY;
    private boolean mDraggedDispatch;
    private boolean mDraggedIntercept;
    /* access modifiers changed from: private */
    public EmptyController mEmptyController;
    private View mEmptyReplaceView;
    private View mEmptyView;
    private int mEmptyViewIndex;
    /* access modifiers changed from: private */
    public ISwipe.FreshStatus mFreshStatus;
    private boolean mIsTouchEventMode;
    private boolean mLoadMoreOverScroll;
    /* access modifiers changed from: private */
    public ISwipe.LoadedStatus mLoadedStatus;
    /* access modifiers changed from: private */
    public boolean mLoadedStatusContinueRunning;
    private int mMaxFlingDirection;
    private int mMaxFlingScrollButton;
    private View mMaxFlingScrollDesView;
    private int mMaxFlingScrollUp;
    private SwipeController.SwipeModel mModel;
    private boolean mNestedScrollInProgress;
    /* access modifiers changed from: private */
    public ISwipe.OnRefreshListener mOnRefreshListener;
    private OnScrollListener mOnScrollListener;
    private int mOverScrollBottom;
    private int mOverScrollBottomMiddle;
    /* access modifiers changed from: private */
    public int mOverScrollTop;
    private int mOverScrollTopMiddle;
    private NestedScrollingParentHelper mParentHelper;
    private boolean mPreScroll;
    private int mPreScrollY;
    /* access modifiers changed from: private */
    public boolean mRefreshStatusContinueRunning;
    private ScrollerCompat mScroller;
    private boolean mShouldCancelMotionEvent;
    private boolean mShowEmptyView;
    /* access modifiers changed from: private */
    public SwipeController mSwipeController;
    private View mTargetView;
    private View mTargetViewContain;
    private int mTargetViewContainIndex;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private View mViewBottom;
    private View mViewTop;

    public interface OnScrollListener {
        void onScroll(int i, int i2);
    }

    public SwipeNest(Context context) {
        this(context, (AttributeSet) null);
    }

    public SwipeNest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeNest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mActivePointerId = -1;
        this.mNestedScrollInProgress = false;
        this.mIsTouchEventMode = false;
        this.mShouldCancelMotionEvent = false;
        this.mLoadMoreOverScroll = false;
        this.mRefreshStatusContinueRunning = false;
        this.mFreshStatus = null;
        this.mLoadedStatusContinueRunning = false;
        this.mLoadedStatus = null;
        this.mModel = SwipeController.SwipeModel.SWIPE_BOTH;
        this.mEmptyViewIndex = -1;
        this.mMaxFlingScrollUp = 0;
        this.mMaxFlingScrollButton = 0;
        this.mPreScrollY = 0;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeNest);
            int modelIndex = a.getInt(R.styleable.SwipeNest_fresh, -1);
            if (modelIndex >= 0 && modelIndex < SwipeController.SwipeModel.values().length) {
                this.mModel = SwipeController.SwipeModel.values()[modelIndex];
            }
            a.recycle();
        }
        this.mScroller = ScrollerCompat.create(getContext(), (Interpolator) null);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mParentHelper = new NestedScrollingParentHelper(this);
        initSwipeControl();
    }

    private void initSwipeControl() {
        this.mSwipeController = new SwipeControllerStyleNormal(getContext());
        this.mViewTop = this.mSwipeController.getSwipeHead();
        this.mViewBottom = this.mSwipeController.getSwipeFoot();
        addView(this.mViewTop, 0);
        addView(this.mViewBottom, getChildCount());
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (!this.mLoadedStatusContinueRunning && this.mOnRefreshListener != null && (target instanceof ScrollingView)) {
            ScrollingView scrollingView = (ScrollingView) target;
            if ((scrollingView.computeVerticalScrollRange() - scrollingView.computeVerticalScrollOffset()) - scrollingView.computeVerticalScrollExtent() < getHeight() * 5) {
                this.mLoadedStatusContinueRunning = true;
                if (this.mOnRefreshListener != null) {
                    this.mOnRefreshListener.onLoading();
                }
            }
        }
        this.mNestedScrollInProgress = true;
        if (target != this.mEmptyView && (target != this.mEmptyReplaceView || !this.mShowEmptyView)) {
            this.mTargetView = target;
            this.mTargetViewContain = child;
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) == this.mTargetViewContain) {
                    this.mTargetViewContainIndex = i;
                }
            }
        }
        stopAllScroll();
        if ((nestedScrollAxes & 2) != 0) {
            return true;
        }
        return false;
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        this.mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        consumed[1] = offSetScroll(dy, true);
    }

    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

    public void onStopNestedScroll(View target) {
        this.mParentHelper.onStopNestedScroll(target);
        this.mNestedScrollInProgress = false;
        if (!tryBackToRefreshing() && !tryBackToFreshFinish()) {
            tryBackToLoading();
        }
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        offSetScroll(dyUnconsumed, false);
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (velocityY >= 0.0f && !this.mLoadedStatusContinueRunning && this.mOnRefreshListener != null && (target instanceof ScrollingView)) {
            float radio = velocityY / ((float) getHeight());
            if (radio <= 1.0f) {
                radio = 1.0f;
            }
            if (radio >= 10.0f) {
                radio = 10.0f;
            }
            ScrollingView scrollingView = (ScrollingView) target;
            if (((float) ((scrollingView.computeVerticalScrollRange() - scrollingView.computeVerticalScrollOffset()) - scrollingView.computeVerticalScrollExtent())) < ((float) getHeight()) * radio) {
                this.mLoadedStatusContinueRunning = true;
                if (this.mOnRefreshListener != null) {
                    this.mOnRefreshListener.onLoading();
                }
            }
        }
        return false;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        boolean consume = true;
        Integer but = this.mTargetViewContain != null ? (Integer) this.mTargetViewContain.getTag(UCCore.VERIFY_POLICY_QUICK) : null;
        Integer but2 = Integer.valueOf(but == null ? Integer.MAX_VALUE : but.intValue());
        int currentScrollY = getScrollY();
        if (currentScrollY >= 0 && currentScrollY <= this.mContentScroll + this.mOverScrollBottomMiddle) {
            if (but2.intValue() != currentScrollY) {
                fling((int) velocityY);
            } else {
                if ((velocityY > 0.0f && canChildScrollUp()) || (velocityY < 0.0f && canChildScrollDown())) {
                    consume = false;
                }
                if (consume) {
                    fling((int) velocityY);
                }
            }
        }
        return consume;
    }

    private void fling(int velocityY) {
        if (this.animationScrollY == null || !this.animationScrollY.isStarted()) {
            int currentScrollY = getScrollY();
            if (this.mContentScroll != 0 || currentScrollY != 0 || velocityY <= 0 || canChildScrollDown()) {
                this.mMaxFlingScrollUp = 0;
                this.mMaxFlingScrollButton = this.mContentScroll + this.mOverScrollBottomMiddle;
                this.mMaxFlingDirection = 0;
                if (velocityY > 0) {
                    int i = this.mTargetViewContainIndex > 0 ? this.mTargetViewContainIndex : 0;
                    while (true) {
                        if (i >= getChildCount()) {
                            break;
                        }
                        Integer integer = (Integer) getChildAt(i).getTag(UCCore.VERIFY_POLICY_QUICK);
                        if (integer != null && integer.intValue() > currentScrollY) {
                            View targetView = getChildAt(i);
                            int[] tem = new int[2];
                            targetView.getLocationInWindow(tem);
                            this.mMaxFlingScrollDesView = findScrollView(targetView, tem[0] + (targetView.getWidth() / 2), tem[1] + (targetView.getHeight() / 2), 1);
                            if (this.mMaxFlingScrollDesView != null) {
                                this.mMaxFlingScrollButton = integer.intValue();
                                break;
                            }
                        }
                        i++;
                    }
                    this.mMaxFlingDirection = 1;
                    this.mScroller.fling(0, currentScrollY, 0, velocityY, 0, 0, -1000000, 1000000, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                if (velocityY < 0) {
                    int i2 = this.mTargetViewContainIndex > 0 ? this.mTargetViewContainIndex : getChildCount() - 1;
                    while (true) {
                        if (i2 < 0) {
                            break;
                        }
                        Integer integer2 = (Integer) getChildAt(i2).getTag(UCCore.VERIFY_POLICY_QUICK);
                        if (integer2 != null && integer2.intValue() < currentScrollY) {
                            View targetView2 = getChildAt(i2);
                            int[] tem2 = new int[2];
                            targetView2.getLocationInWindow(tem2);
                            this.mMaxFlingScrollDesView = findScrollView(targetView2, tem2[0] + (targetView2.getWidth() / 2), tem2[1] + (targetView2.getHeight() / 2), -1);
                            if (this.mMaxFlingScrollDesView != null) {
                                this.mMaxFlingScrollUp = integer2.intValue();
                                break;
                            }
                        }
                        i2--;
                    }
                    this.mMaxFlingScrollUp = this.mMaxFlingScrollUp > 0 ? this.mMaxFlingScrollUp : 0;
                    this.mMaxFlingDirection = -1;
                    this.mScroller.fling(0, currentScrollY, 0, velocityY, 0, 0, -1000000, 1000000, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(this);
                }
            }
        }
    }

    private View findScrollView(View view, int centerX, int centerY, int direction) {
        if (view.getVisibility() == 4 || view.getVisibility() == 8) {
            return null;
        }
        if (view == this.mViewTop || view == this.mViewBottom || view == this.mEmptyView) {
            return null;
        }
        int[] tem = new int[2];
        view.getLocationInWindow(tem);
        if (tem[0] >= centerX || centerX >= tem[0] + view.getWidth() || tem[1] >= centerY || centerY >= tem[1] + view.getHeight()) {
            return null;
        }
        if (direction == 0) {
            if (view.canScrollVertically(-1) || view.canScrollVertically(1)) {
                return view;
            }
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = findScrollView(group.getChildAt(i), centerX, centerY, direction);
                    if (child != null) {
                        return child;
                    }
                }
            }
            return null;
        } else if (view.canScrollVertically(direction)) {
            return view;
        } else {
            if (view instanceof ViewGroup) {
                ViewGroup group2 = (ViewGroup) view;
                for (int i2 = 0; i2 < group2.getChildCount(); i2++) {
                    View child2 = findScrollView(group2.getChildAt(i2), centerX, centerY, direction);
                    if (child2 != null) {
                        return child2;
                    }
                }
            }
            return null;
        }
    }

    public int computeHorizontalScrollRange() {
        return getWidth();
    }

    public int computeHorizontalScrollOffset() {
        return 0;
    }

    public int computeHorizontalScrollExtent() {
        return getWidth();
    }

    public int computeVerticalScrollRange() {
        return this.mContentScroll + getHeight();
    }

    public int computeVerticalScrollOffset() {
        return getScrollY();
    }

    public int computeVerticalScrollExtent() {
        return getHeight();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthTarget = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightTarget = View.MeasureSpec.getSize(heightMeasureSpec);
        int childWidMeasure = View.MeasureSpec.makeMeasureSpec(widthTarget, UCCore.VERIFY_POLICY_QUICK);
        int spHei = View.MeasureSpec.makeMeasureSpec(heightTarget, Integer.MIN_VALUE);
        int mTotalLength = 0;
        int normalIndex = 0;
        int childCount = getChildCount();
        boolean hasMeasureEmptyView = false;
        this.mEmptyReplaceView = null;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (!(child == this.mViewTop || child == this.mViewBottom || child == this.mEmptyView)) {
                if (child.getVisibility() != 8) {
                    ViewGroup.LayoutParams lp = child.getLayoutParams();
                    if (!(lp instanceof LayoutParams) || !((LayoutParams) lp).expend) {
                        switch (lp.height) {
                            case -2:
                                child.measure(childWidMeasure, View.MeasureSpec.makeMeasureSpec(heightTarget, Integer.MIN_VALUE));
                                break;
                            case -1:
                                child.measure(childWidMeasure, View.MeasureSpec.makeMeasureSpec(heightTarget, UCCore.VERIFY_POLICY_QUICK));
                                break;
                            default:
                                child.measure(childWidMeasure, View.MeasureSpec.makeMeasureSpec(lp.height, UCCore.VERIFY_POLICY_QUICK));
                                break;
                        }
                    } else {
                        child.measure(childWidMeasure, View.MeasureSpec.makeMeasureSpec(15360, 0));
                    }
                    int childMeasureHei = child.getMeasuredHeight();
                    if (childMeasureHei <= 0) {
                        childMeasureHei = 0;
                    }
                    mTotalLength += childMeasureHei;
                    if (this.mEmptyViewIndex == normalIndex) {
                        this.mEmptyReplaceView = child;
                        if (this.mEmptyView != null) {
                            hasMeasureEmptyView = true;
                            if (normalIndex == childCount - 4) {
                                int preLen = mTotalLength - childMeasureHei;
                                if (heightTarget - preLen > 0) {
                                    childMeasureHei = heightTarget - preLen;
                                }
                            }
                            this.mEmptyView.measure(View.MeasureSpec.makeMeasureSpec(widthTarget, UCCore.VERIFY_POLICY_QUICK), View.MeasureSpec.makeMeasureSpec(childMeasureHei, UCCore.VERIFY_POLICY_QUICK));
                        }
                    }
                }
                normalIndex++;
            }
        }
        if (this.mViewTop != null) {
            this.mViewTop.measure(childWidMeasure, spHei);
        }
        if (this.mViewBottom != null) {
            this.mViewBottom.measure(childWidMeasure, spHei);
        }
        if (!hasMeasureEmptyView && this.mEmptyView != null) {
            this.mEmptyView.measure(childWidMeasure, spHei);
        }
        int endHei = Math.max(mTotalLength, getSuggestedMinimumHeight());
        setMeasuredDimension(resolveSizeAndState(Math.max(widthTarget, getSuggestedMinimumWidth()), widthMeasureSpec, 0), resolveSizeAndState(endHei, heightMeasureSpec, 0));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int i;
        int oldContentScroll = this.mContentScroll;
        int currentHei = 0;
        boolean hasEmptyViewLayout = false;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View child = getChildAt(i2);
            if (!(child == this.mViewTop || child == this.mViewBottom || child == this.mEmptyView || child.getVisibility() == 8)) {
                if (child != this.mEmptyReplaceView) {
                    int itemHei = child.getMeasuredHeight();
                    LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                    if (itemHei > b - t && (layoutParams == null || !layoutParams.expend)) {
                        itemHei = b - t;
                    }
                    child.layout(0, currentHei, r - l, currentHei + itemHei);
                    currentHei += itemHei;
                    child.setTag(UCCore.VERIFY_POLICY_QUICK, Integer.valueOf(currentHei - (b - t)));
                } else if (!this.mShowEmptyView) {
                    if (this.mEmptyView != null) {
                        hasEmptyViewLayout = true;
                        this.mEmptyView.layout(r - l, currentHei, (r - l) * 2, this.mEmptyView.getMeasuredHeight() + currentHei);
                    }
                    int itemHei2 = child.getMeasuredHeight();
                    LayoutParams layoutParams2 = (LayoutParams) child.getLayoutParams();
                    if (itemHei2 > b - t && (layoutParams2 == null || !layoutParams2.expend)) {
                        itemHei2 = b - t;
                    }
                    child.layout(0, currentHei, r - l, currentHei + itemHei2);
                    currentHei += itemHei2;
                    child.setTag(UCCore.VERIFY_POLICY_QUICK, Integer.valueOf(currentHei - (b - t)));
                } else if (this.mEmptyView != null) {
                    hasEmptyViewLayout = true;
                    int itemEmptyHei = this.mEmptyView.getMeasuredHeight();
                    this.mEmptyView.layout(0, currentHei, r - l, currentHei + itemEmptyHei);
                    currentHei += itemEmptyHei;
                    child.layout(r - l, currentHei, (r - l) * 2, currentHei + child.getMeasuredHeight());
                    child.setTag(UCCore.VERIFY_POLICY_QUICK, (Object) null);
                } else {
                    int itemHei3 = child.getMeasuredHeight();
                    currentHei += itemHei3;
                    child.layout(r - l, currentHei, (r - l) * 2, currentHei + itemHei3);
                    child.setTag(UCCore.VERIFY_POLICY_QUICK, (Object) null);
                }
            }
        }
        boolean canOverScrollBottom = currentHei >= b - t;
        this.mContentScroll = canOverScrollBottom ? currentHei - (b - t) : 0;
        this.mOverScrollTop = this.mViewTop.getMeasuredHeight();
        this.mOverScrollTopMiddle = this.mOverScrollTop != 0 ? this.mOverScrollTop - this.mSwipeController.getOverScrollHei() : 0;
        this.mOverScrollTopMiddle = this.mOverScrollTopMiddle > 0 ? this.mOverScrollTopMiddle : 0;
        this.mOverScrollBottomMiddle = this.mViewBottom.getMeasuredHeight();
        if (this.mLoadMoreOverScroll) {
            i = this.mOverScrollBottomMiddle + ((int) (((float) (b - t)) * 0.2f));
        } else {
            i = this.mOverScrollBottomMiddle;
        }
        this.mOverScrollBottom = i;
        this.mViewTop.layout(0, -this.mOverScrollTop, r - l, 0);
        this.mViewTop.setTag(UCCore.VERIFY_POLICY_QUICK, 0);
        this.mViewBottom.layout(0, (b - t) + this.mContentScroll, r - l, (b - t) + this.mContentScroll + this.mOverScrollBottomMiddle);
        this.mViewTop.setTag(UCCore.VERIFY_POLICY_QUICK, Integer.valueOf(this.mContentScroll + this.mOverScrollBottomMiddle));
        if (!hasEmptyViewLayout && this.mEmptyView != null) {
            this.mEmptyView.layout(r - l, 0, (r - l) * 2, this.mEmptyView.getMeasuredHeight());
        }
        if (this.mModel == SwipeController.SwipeModel.SWIPE_NONE || this.mModel == SwipeController.SwipeModel.SWIPE_ONLY_LOADINN) {
            this.mOverScrollTop = 0;
            this.mOverScrollTopMiddle = 0;
        }
        if (!canOverScrollBottom || this.mModel == SwipeController.SwipeModel.SWIPE_NONE || this.mModel == SwipeController.SwipeModel.SWIPE_ONLY_REFRESH) {
            this.mOverScrollBottomMiddle = 0;
            this.mOverScrollBottom = 0;
        }
        if (oldContentScroll > this.mContentScroll && getScrollY() > this.mContentScroll) {
            int currentScrollY = getScrollY() - (oldContentScroll - this.mContentScroll);
            if (currentScrollY <= 0) {
                currentScrollY = 0;
            }
            scrollTo(0, currentScrollY);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == 0) {
            this.mDraggedIntercept = false;
            this.mPreScroll = false;
            this.mDraggedDispatch = false;
            this.mActivePointerId = ev.getPointerId(0);
            this.mDragBeginY = (float) ((int) ev.getY());
            this.mDragBeginDirect = 0.0f;
            this.mIsTouchEventMode = true;
            this.mDragLastY = this.mDragBeginY;
            this.mShouldCancelMotionEvent = false;
            this.mTargetView = null;
            this.mTargetViewContain = null;
            this.mTargetViewContainIndex = -1;
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            int i = 0;
            while (true) {
                if (i >= getChildCount()) {
                    break;
                }
                int scrollY = getScrollY();
                View child = getChildAt(i);
                if (!(child == this.mViewTop || child == this.mViewBottom || child == this.mEmptyView)) {
                    if (y >= child.getTop() - scrollY && y < child.getBottom() - scrollY && x >= child.getLeft() && x < child.getRight()) {
                        if (child != this.mEmptyReplaceView) {
                            this.mTargetView = findScrollView(child, x, y, 0);
                            this.mTargetViewContain = child;
                            this.mTargetViewContainIndex = i;
                        }
                    }
                }
                i++;
            }
            stopAllScroll();
        }
        if (action == 1 || action == 3) {
            this.mIsTouchEventMode = false;
        }
        if (this.mNestedScrollInProgress) {
            return super.dispatchTouchEvent(ev);
        }
        if (action == 0 && (getScrollY() < 0 || getScrollY() > this.mContentScroll)) {
            this.mPreScroll = true;
        }
        if (this.mPreScroll) {
            switch (action) {
                case 0:
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    this.mDragBeginY = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                    this.mDragLastY = this.mDragBeginY;
                    break;
                case 1:
                case 3:
                    ev.setAction(3);
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.addMovement(ev);
                        this.mVelocityTracker.computeCurrentVelocity(1000, 20000.0f);
                        this.mVelocityTracker.getYVelocity();
                        fling(-((int) this.mVelocityTracker.getYVelocity()));
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                        break;
                    }
                    break;
                case 2:
                    float y2 = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                    float yDiff = this.mDragBeginY - y2;
                    if (!this.mDraggedDispatch && Math.abs(yDiff) > ((float) (this.mTouchSlop / 2))) {
                        this.mDraggedDispatch = true;
                        this.mDragLastY = y2;
                        if (this.mVelocityTracker == null) {
                            this.mVelocityTracker = VelocityTracker.obtain();
                        } else {
                            this.mVelocityTracker.clear();
                        }
                    }
                    if (this.mDraggedDispatch) {
                        offSetScroll((int) (this.mDragLastY - y2), true);
                        if (getScrollY() == 0) {
                            this.mPreScroll = false;
                        }
                        this.mDragLastY = y2;
                        this.mVelocityTracker.addMovement(ev);
                        return true;
                    }
                    break;
                case 5:
                    this.mActivePointerId = ev.getPointerId(MotionEventCompat.getActionIndex(ev));
                    this.mDragBeginY = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                    this.mDragLastY = this.mDragBeginY;
                    break;
                case 6:
                    onSecondaryPointerUp(ev);
                    break;
            }
        }
        if ((action == 1 || action == 3) && (tryBackToRefreshing() || tryBackToFreshFinish() || tryBackToLoading() || this.mDraggedDispatch || this.mDraggedIntercept || this.mShouldCancelMotionEvent)) {
            ev.setAction(3);
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(ev);
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(ev);
        if ((action != 1 && action != 3) || this.mVelocityTracker == null) {
            return dispatchTouchEvent;
        }
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        return dispatchTouchEvent;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.mNestedScrollInProgress || !isEnabled()) {
            return false;
        }
        if (canChildScrollDown() && canChildScrollUp()) {
            return false;
        }
        switch (MotionEventCompat.getActionMasked(ev)) {
            case 0:
                this.mActivePointerId = ev.getPointerId(0);
                this.mDragBeginY = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                this.mDragLastY = this.mDragBeginY;
                if (this.mScroller.computeScrollOffset() && !this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                    this.mShouldCancelMotionEvent = true;
                    break;
                }
            case 2:
                float y = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                float yDiff = this.mDragBeginY - y;
                if (Math.abs(yDiff) > ((float) this.mTouchSlop) && !this.mDraggedIntercept && ((yDiff > 0.0f && !canChildScrollUp()) || (yDiff < 0.0f && !canChildScrollDown()))) {
                    this.mDraggedIntercept = true;
                    this.mDragLastY = y;
                    this.mDragBeginDirect = -yDiff;
                    if (this.mVelocityTracker == null) {
                        this.mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        this.mVelocityTracker.clear();
                    }
                    this.mVelocityTracker.addMovement(ev);
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                }
                break;
            case 5:
                this.mActivePointerId = ev.getPointerId(MotionEventCompat.getActionIndex(ev));
                this.mDragBeginY = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                this.mDragLastY = this.mDragBeginY;
                break;
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        return this.mDraggedIntercept;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case 0:
                this.mActivePointerId = ev.getPointerId(0);
                this.mDragBeginY = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                if (this.mScroller.computeScrollOffset() && !this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                    this.mShouldCancelMotionEvent = true;
                }
                this.mDragLastY = (float) ((int) ev.getY());
                break;
            case 1:
            case 3:
                if (this.mDraggedIntercept && this.mVelocityTracker != null) {
                    this.mVelocityTracker.computeCurrentVelocity(1000, 20000.0f);
                    this.mVelocityTracker.getYVelocity();
                    fling(-((int) this.mVelocityTracker.getYVelocity()));
                    break;
                }
            case 2:
                float y = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                int deltaY = ((int) this.mDragBeginY) - ((int) y);
                if (!this.mDraggedIntercept && Math.abs(deltaY) > this.mTouchSlop) {
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    this.mDraggedIntercept = true;
                    this.mDragLastY = y;
                    this.mDragBeginDirect = (float) (-deltaY);
                    if (this.mVelocityTracker == null) {
                        this.mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        this.mVelocityTracker.clear();
                    }
                    this.mVelocityTracker.addMovement(ev);
                }
                if (this.mDraggedIntercept) {
                    offSetScroll((int) (this.mDragLastY - y), false);
                    break;
                }
                break;
            case 5:
                this.mActivePointerId = ev.getPointerId(MotionEventCompat.getActionIndex(ev));
                this.mDragBeginY = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                this.mDragLastY = this.mDragBeginY;
                break;
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
        if (pointerIndex >= 0) {
            this.mDragLastY = ev.getY(pointerIndex);
        }
        return true;
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept && this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public void computeScroll() {
        int scrollY = getScrollY();
        if (this.mScroller.computeScrollOffset() && !this.mScroller.isFinished()) {
            int y = this.mScroller.getCurrY();
            if (y > this.mMaxFlingScrollButton || y < this.mMaxFlingScrollUp) {
                if (y > this.mMaxFlingScrollButton) {
                    y = this.mMaxFlingScrollButton;
                }
                if (y < this.mMaxFlingScrollUp) {
                    y = this.mMaxFlingScrollUp;
                }
                int remainVelocity = (int) (((float) this.mMaxFlingDirection) * this.mScroller.getCurrVelocity());
                if (this.mMaxFlingScrollDesView != null) {
                    if (this.mMaxFlingScrollDesView instanceof RecyclerView) {
                        ((RecyclerView) this.mMaxFlingScrollDesView).fling(0, remainVelocity);
                    } else if (this.mMaxFlingScrollDesView instanceof ListView) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            ((ListView) this.mMaxFlingScrollDesView).fling(remainVelocity);
                        }
                    } else if (this.mMaxFlingScrollDesView instanceof ScrollView) {
                        ((ScrollView) this.mMaxFlingScrollDesView).fling(remainVelocity);
                    } else if (this.mMaxFlingScrollDesView instanceof NestedScrollView) {
                        ((NestedScrollView) this.mMaxFlingScrollDesView).fling(remainVelocity);
                    }
                }
                this.mScroller.abortAnimation();
            }
            if (scrollY != y) {
                scrollTo(0, y);
                scrollY = y;
            }
        }
        if (this.mPreScrollY != scrollY) {
            this.mPreScrollY = scrollY;
            if (this.mPreScrollY == 0 && ((this.mFreshStatus == ISwipe.FreshStatus.ERROR || this.mFreshStatus == ISwipe.FreshStatus.ERROR_NET || this.mFreshStatus == ISwipe.FreshStatus.SUCCESS) && !this.mRefreshStatusContinueRunning)) {
                this.mFreshStatus = null;
            }
            if (this.mPreScrollY < 0) {
                int swipeViewVisibilityHei = 0 - this.mPreScrollY;
                if (this.mFreshStatus == null && !this.mRefreshStatusContinueRunning) {
                    if (this.mPreScrollY < (-this.mOverScrollTopMiddle)) {
                        this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_OVER, swipeViewVisibilityHei, this.mOverScrollTop);
                    } else {
                        this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_TOAST, swipeViewVisibilityHei, this.mOverScrollTop);
                    }
                }
            }
            if (this.mPreScrollY > this.mContentScroll && !this.mLoadedStatusContinueRunning && this.mOnRefreshListener != null) {
                this.mLoadedStatusContinueRunning = true;
                this.mOnRefreshListener.onLoading();
            }
            if (this.mPreScrollY > this.mContentScroll) {
                if (this.mLoadedStatus == ISwipe.LoadedStatus.NO_MORE) {
                    this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_NO_MORE, this.mPreScrollY - this.mContentScroll, this.mOverScrollBottomMiddle);
                } else if (this.mLoadedStatus == ISwipe.LoadedStatus.ERROR) {
                    this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_ERROR, this.mPreScrollY - this.mContentScroll, this.mOverScrollBottomMiddle);
                } else if (this.mLoadedStatus == null) {
                    this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_LOADING, this.mPreScrollY - this.mContentScroll, this.mOverScrollBottomMiddle);
                }
            }
            if (this.mOnScrollListener != null) {
                this.mOnScrollListener.onScroll(this.mPreScrollY, this.mContentScroll);
            }
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
        if (MotionEventCompat.getPointerId(ev, pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mDragLastY = (float) ((int) ev.getY(newPointerIndex));
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private boolean canChildScrollDown() {
        return this.mTargetView != null && ViewCompat.canScrollVertically(this.mTargetView, -1);
    }

    private boolean canChildScrollUp() {
        return this.mTargetView != null && ViewCompat.canScrollVertically(this.mTargetView, 1);
    }

    private int offSetScroll(int deltaOriginY, boolean pre) {
        if (deltaOriginY == 0) {
            return 0;
        }
        int currentScrollY = getScrollY();
        boolean progress = !pre;
        if (pre) {
            if (currentScrollY < 0 || currentScrollY > this.mContentScroll) {
                progress = true;
            } else {
                Integer scroll = this.mTargetViewContain != null ? (Integer) this.mTargetViewContain.getTag(UCCore.VERIFY_POLICY_QUICK) : null;
                Integer scroll2 = Integer.valueOf(scroll == null ? Integer.MAX_VALUE : scroll.intValue());
                if (currentScrollY != scroll2.intValue()) {
                    progress = true;
                }
                if (currentScrollY == scroll2.intValue()) {
                    progress = false;
                }
            }
        }
        if (!progress) {
            return 0;
        }
        int deltaY = deltaOriginY;
        if (deltaOriginY < 0 && currentScrollY < (-this.mOverScrollTopMiddle) && this.mOverScrollTop != this.mOverScrollTopMiddle && (deltaY = (int) (((double) deltaY) * Math.pow((double) ((((float) ((-this.mOverScrollTop) - currentScrollY)) * 1.0f) / ((float) (this.mOverScrollTop - this.mOverScrollTopMiddle))), 3.0d))) >= 0) {
            deltaY = -deltaY;
        }
        if (deltaOriginY > 0 && currentScrollY > this.mOverScrollBottomMiddle + this.mContentScroll && this.mOverScrollBottom != this.mOverScrollBottomMiddle && (deltaY = (int) (((double) deltaY) * Math.pow((double) ((((float) ((this.mOverScrollBottom + this.mContentScroll) - currentScrollY)) * 1.0f) / ((float) (this.mOverScrollBottom - this.mOverScrollBottomMiddle))), 3.0d))) <= 0) {
            deltaY = -deltaY;
        }
        int willTo = currentScrollY + deltaY;
        if (willTo >= this.mOverScrollBottom + this.mContentScroll) {
            willTo = this.mOverScrollBottom + this.mContentScroll;
        }
        if (willTo <= (-this.mOverScrollTop)) {
            willTo = -this.mOverScrollTop;
        }
        Integer maxScroll = this.mTargetViewContain != null ? (Integer) this.mTargetViewContain.getTag(UCCore.VERIFY_POLICY_QUICK) : null;
        Integer maxScroll2 = Integer.valueOf(maxScroll == null ? Integer.MAX_VALUE : maxScroll.intValue());
        if ((currentScrollY > maxScroll2.intValue() && willTo < maxScroll2.intValue()) || (currentScrollY < maxScroll2.intValue() && willTo > maxScroll2.intValue())) {
            willTo = maxScroll2.intValue();
        }
        if (this.mDragBeginDirect > 0.0f && willTo > this.mContentScroll) {
            willTo = this.mContentScroll;
        }
        if (this.mDragBeginDirect < 0.0f && willTo < 0) {
            willTo = 0;
        }
        if (this.mContentScroll == 0 && currentScrollY == 0 && deltaOriginY > 0 && !canChildScrollDown()) {
            willTo = 0;
        }
        if (willTo == currentScrollY) {
            return deltaOriginY;
        }
        if (willTo > this.mContentScroll && deltaOriginY > 0) {
            for (int i = this.mTargetViewContainIndex > 0 ? this.mTargetViewContainIndex : 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != this.mViewTop && child != this.mViewBottom && child.getVisibility() != 8 && child.getVisibility() != 4 && child.canScrollVertically(1)) {
                    return deltaOriginY;
                }
            }
        }
        scrollTo(0, willTo);
        return deltaOriginY;
    }

    private boolean tryBackToRefreshing() {
        if (this.mIsTouchEventMode || this.mOverScrollTopMiddle == 0 || this.mFreshStatus == ISwipe.FreshStatus.SUCCESS || this.mFreshStatus == ISwipe.FreshStatus.ERROR || this.mFreshStatus == ISwipe.FreshStatus.ERROR_NET) {
            return false;
        }
        int scrollY = getScrollY();
        if (scrollY > (-this.mOverScrollTopMiddle)) {
            return false;
        }
        stopAllScroll();
        this.animationScrollY = ValueAnimator.ofInt(new int[]{scrollY, -this.mOverScrollTopMiddle});
        this.animationScrollY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SwipeNest.this.scrollTo(0, ((Integer) animation.getAnimatedValue()).intValue());
            }
        });
        this.animationScrollY.addListener(new Animator.AnimatorListener() {
            boolean isCancel = false;

            public void onAnimationEnd(Animator animation) {
                if (!this.isCancel && !SwipeNest.this.mRefreshStatusContinueRunning) {
                    boolean unused = SwipeNest.this.mRefreshStatusContinueRunning = true;
                    SwipeNest.this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_LOADING, SwipeNest.this.mOverScrollTop, SwipeNest.this.mOverScrollTop);
                    if (SwipeNest.this.mEmptyController != null) {
                        SwipeNest.this.mEmptyController.onSwipeStatue((ISwipe.FreshStatus) null);
                    }
                    if (SwipeNest.this.mOnRefreshListener != null) {
                        SwipeNest.this.mOnRefreshListener.onRefresh();
                    }
                }
                this.isCancel = true;
            }

            public void onAnimationCancel(Animator animation) {
                this.isCancel = true;
            }

            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.animationScrollY.setDuration((long) (Math.abs((((-this.mOverScrollTopMiddle) - scrollY) * 500) / this.mOverScrollTop) + 50));
        this.animationScrollY.start();
        return true;
    }

    /* access modifiers changed from: private */
    public boolean tryBackToFreshFinish() {
        int scrollY = getScrollY();
        if (this.mIsTouchEventMode || (this.mFreshStatus == null && this.mRefreshStatusContinueRunning && scrollY == (-this.mOverScrollTopMiddle))) {
            return false;
        }
        if (scrollY >= 0) {
            return false;
        }
        stopAllScroll();
        this.animationScrollY = ValueAnimator.ofInt(new int[]{scrollY, 0});
        this.animationScrollY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SwipeNest.this.scrollTo(0, ((Integer) animation.getAnimatedValue()).intValue());
            }
        });
        if (scrollY == (-this.mOverScrollTopMiddle)) {
            this.animationScrollY.setDuration((long) Math.abs(((0 - scrollY) * 550) / this.mOverScrollTop));
            this.animationScrollY.setStartDelay(650);
        } else {
            this.animationScrollY.setDuration(320);
        }
        this.animationScrollY.start();
        return true;
    }

    private boolean tryBackToLoading() {
        int scrollY = getScrollY();
        if (this.mIsTouchEventMode || scrollY <= this.mContentScroll + this.mOverScrollBottomMiddle || this.mOverScrollBottomMiddle == this.mOverScrollBottom) {
            return false;
        }
        stopAllScroll();
        this.animationScrollY = ValueAnimator.ofInt(new int[]{scrollY, this.mContentScroll + this.mOverScrollBottomMiddle});
        this.animationScrollY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SwipeNest.this.scrollTo(0, ((Integer) animation.getAnimatedValue()).intValue());
            }
        });
        this.animationScrollY.setDuration((long) (((int) ((((float) ((scrollY - this.mContentScroll) - this.mOverScrollBottomMiddle)) * 250.0f) / ((float) (this.mOverScrollBottom - this.mOverScrollBottomMiddle)))) + 50));
        this.animationScrollY.start();
        return true;
    }

    private void stopAllScroll() {
        if (this.animationScrollY != null) {
            this.animationScrollY.cancel();
            this.animationScrollY = null;
        }
        this.mScroller.abortAnimation();
    }

    public void setOnRefreshListener(ISwipe.OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }

    public void refresh() {
        this.mFreshStatus = null;
        if (this.mEmptyController != null) {
            this.mEmptyController.onSwipeStatue(this.mFreshStatus);
        }
        this.mRefreshStatusContinueRunning = true;
        this.mLoadedStatus = null;
        this.mLoadedStatusContinueRunning = false;
        if (this.mOnRefreshListener != null) {
            this.mOnRefreshListener.onRefresh();
        }
        this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_LOADING, -getScrollY(), this.mOverScrollTop);
        tryBackToRefreshing();
    }

    public void setFreshResult(ISwipe.FreshStatus statue) {
        switch (statue) {
            case SUCCESS:
                postDelayed(new Runnable() {
                    public void run() {
                        ISwipe.FreshStatus unused = SwipeNest.this.mFreshStatus = ISwipe.FreshStatus.SUCCESS;
                        if (SwipeNest.this.mEmptyController != null) {
                            SwipeNest.this.mEmptyController.onSwipeStatue(SwipeNest.this.mFreshStatus);
                        }
                        boolean unused2 = SwipeNest.this.mRefreshStatusContinueRunning = false;
                        ISwipe.LoadedStatus unused3 = SwipeNest.this.mLoadedStatus = null;
                        boolean unused4 = SwipeNest.this.mLoadedStatusContinueRunning = false;
                        SwipeNest.this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_COMPLETE_OK, -SwipeNest.this.getScrollY(), SwipeNest.this.mOverScrollTop);
                        if (SwipeNest.this.getScrollY() == 0) {
                            ISwipe.FreshStatus unused5 = SwipeNest.this.mFreshStatus = null;
                        } else {
                            boolean unused6 = SwipeNest.this.tryBackToFreshFinish();
                        }
                    }
                }, 1000);
                return;
            case ERROR_NET:
                if (this.mEmptyController != null) {
                    this.mEmptyController.onSwipeStatue(this.mFreshStatus);
                }
                postDelayed(new Runnable() {
                    public void run() {
                        ISwipe.FreshStatus unused = SwipeNest.this.mFreshStatus = ISwipe.FreshStatus.ERROR_NET;
                        if (SwipeNest.this.mEmptyController != null) {
                            SwipeNest.this.mEmptyController.onSwipeStatue(SwipeNest.this.mFreshStatus);
                        }
                        boolean unused2 = SwipeNest.this.mRefreshStatusContinueRunning = false;
                        ISwipe.LoadedStatus unused3 = SwipeNest.this.mLoadedStatus = null;
                        boolean unused4 = SwipeNest.this.mLoadedStatusContinueRunning = false;
                        SwipeNest.this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_COMPLETE_ERROR_NET, -SwipeNest.this.getScrollY(), SwipeNest.this.mOverScrollTop);
                        if (SwipeNest.this.getScrollY() == 0) {
                            ISwipe.FreshStatus unused5 = SwipeNest.this.mFreshStatus = null;
                        } else {
                            boolean unused6 = SwipeNest.this.tryBackToFreshFinish();
                        }
                    }
                }, 1000);
                return;
            case ERROR:
                if (this.mEmptyController != null) {
                    this.mEmptyController.onSwipeStatue(this.mFreshStatus);
                }
                postDelayed(new Runnable() {
                    public void run() {
                        ISwipe.FreshStatus unused = SwipeNest.this.mFreshStatus = ISwipe.FreshStatus.ERROR;
                        if (SwipeNest.this.mEmptyController != null) {
                            SwipeNest.this.mEmptyController.onSwipeStatue(SwipeNest.this.mFreshStatus);
                        }
                        boolean unused2 = SwipeNest.this.mRefreshStatusContinueRunning = false;
                        ISwipe.LoadedStatus unused3 = SwipeNest.this.mLoadedStatus = null;
                        boolean unused4 = SwipeNest.this.mLoadedStatusContinueRunning = false;
                        SwipeNest.this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_COMPLETE_ERROR, -SwipeNest.this.getScrollY(), SwipeNest.this.mOverScrollTop);
                        if (SwipeNest.this.getScrollY() == 0) {
                            ISwipe.FreshStatus unused5 = SwipeNest.this.mFreshStatus = null;
                        } else {
                            boolean unused6 = SwipeNest.this.tryBackToFreshFinish();
                        }
                    }
                }, 1000);
                return;
            default:
                return;
        }
    }

    public void completeLoadMore() {
        int currentScrollY = getScrollY() - this.mContentScroll;
        if (currentScrollY > 0) {
            if ((this.mTargetView instanceof RecyclerView) || (this.mTargetView instanceof ListView) || (this.mTargetView instanceof ScrollView) || (this.mTargetView instanceof NestedScrollView)) {
                this.mTargetView.scrollBy(0, currentScrollY);
            }
            stopAllScroll();
            scrollTo(0, this.mContentScroll);
        }
        this.mLoadedStatusContinueRunning = false;
        this.mLoadedStatus = null;
        this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_LOADING, getHeight() - this.mViewBottom.getTop(), this.mOverScrollBottomMiddle);
    }

    public void setLoadMoreResult(ISwipe.LoadedStatus status) {
        switch (status) {
            case ERROR:
                this.mLoadedStatusContinueRunning = true;
                this.mLoadedStatus = ISwipe.LoadedStatus.ERROR;
                this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_ERROR, getHeight() - this.mViewBottom.getTop(), this.mOverScrollBottomMiddle);
                return;
            case NO_MORE:
                this.mLoadedStatusContinueRunning = true;
                this.mLoadedStatus = ISwipe.LoadedStatus.NO_MORE;
                this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_NO_MORE, getHeight() - this.mViewBottom.getTop(), this.mOverScrollBottomMiddle);
                return;
            default:
                return;
        }
    }

    public void setSwipeModel(SwipeController.SwipeModel model) {
        if (this.mModel != model && model != null) {
            this.mModel = model;
            requestLayout();
        }
    }

    public void setSwipeController(SwipeController control) {
        if (control != null && this.mSwipeController != control) {
            removeView(this.mViewTop);
            removeView(this.mViewBottom);
            this.mSwipeController = control;
            this.mViewTop = this.mSwipeController.getSwipeHead();
            this.mViewBottom = this.mSwipeController.getSwipeFoot();
            addView(this.mViewTop);
            addView(this.mViewBottom);
            requestLayout();
        }
    }

    public void setEmptyController(EmptyController controller) {
        if (controller != null && this.mEmptyController != controller) {
            this.mEmptyController = controller;
            if (this.mEmptyView != null) {
                removeView(this.mEmptyView);
            }
            this.mEmptyView = controller.getView();
            this.mEmptyViewIndex = controller.attachToViewIndex();
            addView(this.mEmptyView);
            requestLayout();
        }
    }

    public void enableEmptyView(boolean show) {
        if (this.mShowEmptyView != show) {
            this.mShowEmptyView = show;
            requestLayout();
            if (this.mEmptyReplaceView == this.mTargetViewContain && this.mShowEmptyView) {
                this.mTargetView = null;
                this.mTargetViewContain = null;
                this.mTargetViewContainIndex = -1;
            }
        }
    }

    public void enableLoadMoreOverScroll(boolean enable) {
        if (this.mLoadMoreOverScroll != enable) {
            this.mLoadMoreOverScroll = enable;
            requestLayout();
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) lp);
        }
        return new LayoutParams(lp);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public boolean expend;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.SwipeNest_Layout);
            this.expend = a.getBoolean(R.styleable.SwipeNest_Layout_layout_expend, false);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.expend = source.expend;
        }
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }
}
