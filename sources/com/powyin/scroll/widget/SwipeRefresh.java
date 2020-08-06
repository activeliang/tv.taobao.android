package com.powyin.scroll.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
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

public class SwipeRefresh extends ViewGroup implements NestedScrollingParent, ISwipe {
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
    private View mEmptyView;
    /* access modifiers changed from: private */
    public ISwipe.FreshStatus mFreshStatus;
    private boolean mIsTouchEventMode;
    private boolean mLoadMoreOverScroll;
    /* access modifiers changed from: private */
    public ISwipe.LoadedStatus mLoadedStatus;
    /* access modifiers changed from: private */
    public boolean mLoadedStatusContinueRunning;
    private int mMaxFlingDirection;
    private SwipeController.SwipeModel mModel;
    private boolean mNestedScrollInProgress;
    /* access modifiers changed from: private */
    public ISwipe.OnRefreshListener mOnRefreshListener;
    private int mOverScrollBottom;
    private int mOverScrollBottomMiddle;
    private int mOverScrollTop;
    private int mOverScrollTopMiddle;
    private final NestedScrollingParentHelper mParentHelper;
    private boolean mPreScroll;
    /* access modifiers changed from: private */
    public boolean mRefreshStatusContinueRunning;
    private ScrollerCompat mScroller;
    private boolean mShowEmptyView;
    /* access modifiers changed from: private */
    public SwipeController mSwipeController;
    private View mTargetView;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private View mViewBottom;
    /* access modifiers changed from: private */
    public View mViewTop;
    private int preScroll;

    public SwipeRefresh(Context context) {
        this(context, (AttributeSet) null);
    }

    public SwipeRefresh(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mNestedScrollInProgress = false;
        this.mActivePointerId = -1;
        this.mIsTouchEventMode = false;
        this.mRefreshStatusContinueRunning = false;
        this.mFreshStatus = null;
        this.mLoadedStatusContinueRunning = false;
        this.mLoadedStatus = null;
        this.mModel = SwipeController.SwipeModel.SWIPE_BOTH;
        this.mLoadMoreOverScroll = true;
        this.mMaxFlingDirection = 0;
        this.preScroll = Integer.MIN_VALUE;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeRefresh);
            int modelIndex = a.getInt(R.styleable.SwipeRefresh_fresh_model, -1);
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
            if ((scrollingView.computeVerticalScrollRange() - scrollingView.computeVerticalScrollOffset()) - scrollingView.computeVerticalScrollExtent() < getHeight() * 6) {
                this.mLoadedStatusContinueRunning = true;
                if (this.mOnRefreshListener != null) {
                    this.mOnRefreshListener.onLoading();
                }
            }
        }
        this.mTargetView = target;
        this.mNestedScrollInProgress = true;
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
        if (tryBackToRefreshing() || tryBackToFreshFinish() || tryBackToLoading()) {
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
        int scrollY = getScrollY();
        if (scrollY > 0 && scrollY < this.mOverScrollBottomMiddle) {
            fling((int) velocityY);
        }
        return scrollY != 0;
    }

    private void fling(int velocityY) {
        if ((this.animationScrollY == null || !this.animationScrollY.isStarted()) && velocityY != 0) {
            int currentScrollY = getScrollY();
            if (this.mContentScroll != 0 || currentScrollY != 0 || velocityY <= 0 || canChildScrollDown()) {
                this.mMaxFlingDirection = velocityY > 0 ? 1 : -1;
                this.mScroller.fling(0, currentScrollY, 0, velocityY, 0, 0, -1000000, 1000000, 0, 0);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    private int offSetScroll(int deltaOriginY, boolean pre) {
        int deltaY = deltaOriginY;
        if (deltaOriginY == 0) {
            return 0;
        }
        int currentScrollY = getScrollY();
        if (deltaOriginY < 0 && currentScrollY < (-this.mOverScrollTopMiddle) && this.mOverScrollTop != this.mOverScrollTopMiddle && (deltaY = (int) (((double) deltaY) * Math.pow((double) ((((float) ((-this.mOverScrollTop) - currentScrollY)) * 1.0f) / ((float) (this.mOverScrollTop - this.mOverScrollTopMiddle))), 3.0d))) >= 0) {
            deltaY = -deltaY;
        }
        if (deltaOriginY > 0 && currentScrollY > this.mOverScrollBottomMiddle + this.mContentScroll && this.mOverScrollBottom != this.mOverScrollBottomMiddle && (deltaY = (int) (((double) deltaY) * Math.pow((double) ((((float) ((this.mOverScrollBottom + this.mContentScroll) - currentScrollY)) * 1.0f) / ((float) (this.mOverScrollBottom - this.mOverScrollBottomMiddle))), 3.0d))) <= 0) {
            deltaY = -deltaY;
        }
        if (currentScrollY == this.mContentScroll && pre) {
            return 0;
        }
        int willTo = Math.max(Math.min(currentScrollY + deltaY, this.mOverScrollBottom), -this.mOverScrollTop);
        if ((currentScrollY > 0 && willTo < 0) || (currentScrollY < 0 && willTo > 0)) {
            willTo = 0;
        }
        if ((currentScrollY > this.mContentScroll && willTo < this.mContentScroll) || (currentScrollY < this.mContentScroll && willTo > this.mContentScroll)) {
            willTo = this.mContentScroll;
        }
        if (this.mDragBeginDirect > 0.0f && willTo > this.mContentScroll) {
            willTo = this.mContentScroll;
        }
        if (this.mDragBeginDirect < 0.0f && willTo < 0) {
            willTo = 0;
        }
        if (willTo > 0 && !canChildScrollDown()) {
            willTo = 0;
        }
        if (willTo == currentScrollY) {
            return deltaOriginY;
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
                int value = ((Integer) animation.getAnimatedValue()).intValue();
                if (value > SwipeRefresh.this.getScrollY()) {
                    SwipeRefresh.this.scrollTo(0, value);
                }
            }
        });
        this.animationScrollY.addListener(new Animator.AnimatorListener() {
            boolean isCancel = false;

            public void onAnimationEnd(Animator animation) {
                if (!this.isCancel && !SwipeRefresh.this.mRefreshStatusContinueRunning) {
                    boolean unused = SwipeRefresh.this.mRefreshStatusContinueRunning = true;
                    SwipeRefresh.this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_LOADING, SwipeRefresh.this.mViewTop.getHeight(), SwipeRefresh.this.mViewTop.getHeight());
                    if (SwipeRefresh.this.mEmptyController != null) {
                        SwipeRefresh.this.mEmptyController.onSwipeStatue(SwipeRefresh.this.mFreshStatus);
                    }
                    if (SwipeRefresh.this.mOnRefreshListener != null) {
                        SwipeRefresh.this.mOnRefreshListener.onRefresh();
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
        this.animationScrollY.setDuration((long) (Math.abs((((-this.mOverScrollTopMiddle) - scrollY) * 500) / this.mViewTop.getHeight()) + 50));
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
                SwipeRefresh.this.scrollTo(0, ((Integer) animation.getAnimatedValue()).intValue());
            }
        });
        if (scrollY == (-this.mOverScrollTopMiddle)) {
            this.animationScrollY.setDuration((long) Math.abs(((0 - scrollY) * 550) / this.mViewTop.getHeight()));
            this.animationScrollY.setStartDelay(650);
        } else {
            this.animationScrollY.setDuration(320);
        }
        this.animationScrollY.start();
        return true;
    }

    private boolean tryBackToLoading() {
        int scrollY = getScrollY();
        if (this.mIsTouchEventMode || this.mOverScrollBottomMiddle == 0 || scrollY < this.mContentScroll + this.mOverScrollBottomMiddle || this.mOverScrollBottomMiddle == this.mOverScrollBottom) {
            return false;
        }
        stopAllScroll();
        this.animationScrollY = ValueAnimator.ofInt(new int[]{scrollY, this.mContentScroll + this.mOverScrollBottomMiddle});
        this.animationScrollY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SwipeRefresh.this.scrollTo(0, ((Integer) animation.getAnimatedValue()).intValue());
            }
        });
        this.animationScrollY.setDuration((long) (((int) ((((float) ((scrollY - this.mContentScroll) - this.mOverScrollBottomMiddle)) * 250.0f) / ((float) (this.mOverScrollBottom - this.mOverScrollBottomMiddle)))) + 50));
        this.animationScrollY.start();
        return true;
    }

    private void stopAllScroll() {
        if (this.animationScrollY != null) {
            this.animationScrollY.cancel();
        }
        this.mScroller.abortAnimation();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthTarget = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightTarget = View.MeasureSpec.getSize(heightMeasureSpec);
        int childWidMeasure = View.MeasureSpec.makeMeasureSpec(widthTarget, UCCore.VERIFY_POLICY_QUICK);
        int spHei = View.MeasureSpec.makeMeasureSpec(heightTarget, Integer.MIN_VALUE);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (!(child.getVisibility() == 8 || this.mViewTop == child || this.mViewBottom == child)) {
                child.measure(childWidMeasure, View.MeasureSpec.makeMeasureSpec(heightTarget, UCCore.VERIFY_POLICY_QUICK));
            }
        }
        if (this.mViewTop != null) {
            this.mViewTop.measure(childWidMeasure, spHei);
        }
        if (this.mViewBottom != null) {
            this.mViewBottom.measure(childWidMeasure, spHei);
        }
        setMeasuredDimension(widthTarget, heightTarget);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int i;
        int i2;
        this.mContentScroll = 0;
        this.mOverScrollTop = this.mViewTop.getMeasuredHeight();
        if (this.mOverScrollTop != 0) {
            i = this.mOverScrollTop - this.mSwipeController.getOverScrollHei();
        } else {
            i = 0;
        }
        this.mOverScrollTopMiddle = i;
        if (this.mOverScrollTopMiddle > 0) {
            i2 = this.mOverScrollTopMiddle;
        } else {
            i2 = 0;
        }
        this.mOverScrollTopMiddle = i2;
        this.mOverScrollBottomMiddle = this.mViewBottom.getMeasuredHeight();
        this.mOverScrollBottom = this.mLoadMoreOverScroll ? this.mOverScrollBottomMiddle + ((int) (((float) (bottom - top)) * 0.2f)) : this.mOverScrollBottomMiddle;
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View child = getChildAt(i3);
            if (!(child == this.mViewTop || child == this.mViewBottom || child == this.mEmptyView)) {
                if (this.mShowEmptyView) {
                    child.layout(right - left, 0, (right - left) * 2, bottom - top);
                } else {
                    child.layout(0, 0, right - left, bottom - top);
                }
            }
        }
        this.mViewTop.layout(left, -this.mViewTop.getMeasuredHeight(), right, 0);
        this.mViewBottom.layout(0, bottom - top, right - left, (bottom - top) + this.mOverScrollBottomMiddle);
        if (this.mEmptyView != null) {
            if (this.mShowEmptyView) {
                this.mEmptyView.layout(0, 0, right - left, bottom - top);
            } else {
                this.mEmptyView.layout(right - left, 0, (right - left) * 2, bottom - top);
            }
        }
        if (this.mModel == SwipeController.SwipeModel.SWIPE_NONE || this.mModel == SwipeController.SwipeModel.SWIPE_ONLY_LOADINN) {
            this.mOverScrollTop = 0;
            this.mOverScrollTopMiddle = 0;
        }
        if (this.mModel == SwipeController.SwipeModel.SWIPE_NONE || this.mModel == SwipeController.SwipeModel.SWIPE_ONLY_REFRESH) {
            this.mOverScrollBottomMiddle = 0;
            this.mOverScrollBottom = 0;
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

    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (ev.getAction() == 0) {
            this.mDraggedIntercept = false;
            this.mPreScroll = false;
            this.mDraggedDispatch = false;
            this.mActivePointerId = ev.getPointerId(0);
            this.mDragBeginY = (float) ((int) ev.getY());
            this.mDragBeginDirect = 0.0f;
            this.mIsTouchEventMode = true;
            this.mDragLastY = this.mDragBeginY;
            this.mTargetView = findScrollView(this, (int) ev.getX(), (int) ev.getY(), 0);
            stopAllScroll();
        }
        if (ev.getAction() == 1 || ev.getAction() == 3) {
            this.mIsTouchEventMode = false;
        }
        if (this.mNestedScrollInProgress) {
            return super.dispatchTouchEvent(ev);
        }
        if (action == 0 && getScrollY() != 0) {
            this.mPreScroll = true;
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            } else {
                this.mVelocityTracker.clear();
            }
            this.mVelocityTracker.addMovement(ev);
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
                    float y = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                    float yDiff = this.mDragBeginY - y;
                    if (!this.mDraggedDispatch && Math.abs(yDiff) > ((float) (this.mTouchSlop / 2))) {
                        this.mDraggedDispatch = true;
                        this.mDragLastY = y;
                    }
                    if (this.mDraggedDispatch) {
                        offSetScroll((int) (this.mDragLastY - y), true);
                        if (getScrollY() == 0) {
                            this.mPreScroll = false;
                        }
                        this.mDragLastY = y;
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
        if ((ev.getAction() == 1 || ev.getAction() == 3) && (tryBackToRefreshing() || tryBackToFreshFinish() || tryBackToLoading() || this.mDraggedDispatch || this.mDraggedIntercept)) {
            ev.setAction(3);
        }
        return super.dispatchTouchEvent(ev);
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
                break;
            case 2:
                float y = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                float yDiff = this.mDragBeginY - y;
                if (Math.abs(yDiff) > ((float) this.mTouchSlop) && !this.mDraggedIntercept && ((yDiff > 0.0f && !canChildScrollUp()) || (yDiff < 0.0f && !canChildScrollDown()))) {
                    this.mDraggedIntercept = true;
                    this.mDragLastY = y;
                    this.mDragBeginDirect = -yDiff;
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
                this.mDragLastY = (float) ((int) ev.getY());
                break;
            case 2:
                float y = ev.getY(ev.findPointerIndex(this.mActivePointerId));
                int deltaY = ((int) this.mDragBeginY) - ((int) y);
                if (!this.mDraggedIntercept && Math.abs(deltaY) > this.mTouchSlop) {
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    this.mDraggedIntercept = true;
                    this.mDragBeginDirect = (float) (-deltaY);
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
        int pointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
        if (pointerIndex >= 0) {
            this.mDragLastY = MotionEventCompat.getY(ev, pointerIndex);
        }
        return true;
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if ((this.mEmptyView != null ? -1 : 0) + getChildCount() > 3) {
            throw new RuntimeException("only one View is support");
        }
        super.addView(child, index, params);
    }

    public void computeScroll() {
        int scrollY = getScrollY();
        if (this.mScroller.computeScrollOffset() && !this.mScroller.isFinished()) {
            int y = this.mScroller.getCurrY();
            if (y < 0 || y > this.mContentScroll + this.mOverScrollBottomMiddle) {
                if (y < 0) {
                    y = 0;
                }
                if (y > this.mContentScroll + this.mOverScrollBottomMiddle) {
                    y = this.mContentScroll + this.mOverScrollBottomMiddle;
                }
                if (scrollY != y) {
                    scrollTo(0, y);
                }
                int remainVelocity = (int) (((float) this.mMaxFlingDirection) * this.mScroller.getCurrVelocity());
                if (this.mTargetView != null) {
                    if (this.mTargetView instanceof RecyclerView) {
                        ((RecyclerView) this.mTargetView).fling(0, remainVelocity);
                    } else if (this.mTargetView instanceof ListView) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            ((ListView) this.mTargetView).fling(remainVelocity);
                        }
                    } else if (this.mTargetView instanceof ScrollView) {
                        ((ScrollView) this.mTargetView).fling(remainVelocity);
                    } else if (this.mTargetView instanceof NestedScrollView) {
                        ((NestedScrollView) this.mTargetView).fling(remainVelocity);
                    }
                }
                this.mScroller.abortAnimation();
            } else if (y == scrollY) {
                ViewCompat.postInvalidateOnAnimation(this);
            } else {
                scrollTo(0, y);
            }
        }
        if (scrollY != this.preScroll) {
            this.preScroll = scrollY;
            if (scrollY == 0 && ((this.mFreshStatus == ISwipe.FreshStatus.ERROR || this.mFreshStatus == ISwipe.FreshStatus.ERROR_NET || this.mFreshStatus == ISwipe.FreshStatus.SUCCESS) && !this.mRefreshStatusContinueRunning)) {
                this.mFreshStatus = null;
            }
            if (scrollY < 0) {
                int swipeViewVisibilityHei = 0 - scrollY;
                if (this.mFreshStatus == null && !this.mRefreshStatusContinueRunning) {
                    if (scrollY < (-this.mOverScrollTopMiddle)) {
                        this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_OVER, swipeViewVisibilityHei, this.mOverScrollTop);
                    } else {
                        this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_TOAST, swipeViewVisibilityHei, this.mOverScrollTop);
                    }
                }
            }
            if (scrollY > this.mContentScroll && !this.mLoadedStatusContinueRunning) {
                this.mLoadedStatusContinueRunning = true;
                if (this.mOnRefreshListener != null) {
                    this.mOnRefreshListener.onLoading();
                }
            }
            if (scrollY <= this.mContentScroll) {
                return;
            }
            if (this.mLoadedStatus == ISwipe.LoadedStatus.NO_MORE) {
                this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_NO_MORE, scrollY - this.mContentScroll, this.mOverScrollBottomMiddle);
            } else if (this.mLoadedStatus == ISwipe.LoadedStatus.ERROR) {
                this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_ERROR, scrollY - this.mContentScroll, this.mOverScrollBottomMiddle);
            } else if (this.mLoadedStatus == null) {
                this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_LOADING, scrollY - this.mContentScroll, this.mOverScrollBottomMiddle);
            }
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
        if (MotionEventCompat.getPointerId(ev, pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mDragLastY = (float) ((int) ev.getY(newPointerIndex));
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private boolean canChildScrollDown() {
        return !this.mShowEmptyView && this.mTargetView != null && ViewCompat.canScrollVertically(this.mTargetView, -1);
    }

    private boolean canChildScrollUp() {
        return !this.mShowEmptyView && this.mTargetView != null && ViewCompat.canScrollVertically(this.mTargetView, 1);
    }

    public void requestDisallowInterceptTouchEvent(boolean b) {
        if (this.mShowEmptyView || this.mTargetView == null || (this.mTargetView instanceof NestedScrollingChild)) {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    public void setOnRefreshListener(ISwipe.OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }

    public void refresh() {
        this.mFreshStatus = null;
        if (this.mEmptyController != null) {
            this.mEmptyController.onSwipeStatue((ISwipe.FreshStatus) null);
        }
        this.mRefreshStatusContinueRunning = true;
        this.mLoadedStatus = null;
        this.mLoadedStatusContinueRunning = false;
        if (this.mOnRefreshListener != null) {
            this.mOnRefreshListener.onRefresh();
        }
        this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_LOADING, -getScrollY(), this.mViewTop.getHeight());
        tryBackToRefreshing();
    }

    public void setFreshResult(ISwipe.FreshStatus statue) {
        switch (statue) {
            case SUCCESS:
                postDelayed(new Runnable() {
                    public void run() {
                        ISwipe.FreshStatus unused = SwipeRefresh.this.mFreshStatus = ISwipe.FreshStatus.SUCCESS;
                        if (SwipeRefresh.this.mEmptyController != null) {
                            SwipeRefresh.this.mEmptyController.onSwipeStatue(SwipeRefresh.this.mFreshStatus);
                        }
                        boolean unused2 = SwipeRefresh.this.mRefreshStatusContinueRunning = false;
                        ISwipe.LoadedStatus unused3 = SwipeRefresh.this.mLoadedStatus = null;
                        boolean unused4 = SwipeRefresh.this.mLoadedStatusContinueRunning = false;
                        SwipeRefresh.this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_COMPLETE_OK, -SwipeRefresh.this.getScrollY(), SwipeRefresh.this.mViewTop.getHeight());
                        if (SwipeRefresh.this.getScrollY() == 0) {
                            ISwipe.FreshStatus unused5 = SwipeRefresh.this.mFreshStatus = null;
                        } else {
                            boolean unused6 = SwipeRefresh.this.tryBackToFreshFinish();
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
                        ISwipe.FreshStatus unused = SwipeRefresh.this.mFreshStatus = ISwipe.FreshStatus.ERROR_NET;
                        if (SwipeRefresh.this.mEmptyController != null) {
                            SwipeRefresh.this.mEmptyController.onSwipeStatue(SwipeRefresh.this.mFreshStatus);
                        }
                        boolean unused2 = SwipeRefresh.this.mRefreshStatusContinueRunning = false;
                        ISwipe.LoadedStatus unused3 = SwipeRefresh.this.mLoadedStatus = null;
                        boolean unused4 = SwipeRefresh.this.mLoadedStatusContinueRunning = false;
                        SwipeRefresh.this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_COMPLETE_ERROR_NET, -SwipeRefresh.this.getScrollY(), SwipeRefresh.this.mViewTop.getHeight());
                        if (SwipeRefresh.this.getScrollY() == 0) {
                            ISwipe.FreshStatus unused5 = SwipeRefresh.this.mFreshStatus = null;
                        } else {
                            boolean unused6 = SwipeRefresh.this.tryBackToFreshFinish();
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
                        ISwipe.FreshStatus unused = SwipeRefresh.this.mFreshStatus = ISwipe.FreshStatus.ERROR;
                        if (SwipeRefresh.this.mEmptyController != null) {
                            SwipeRefresh.this.mEmptyController.onSwipeStatue(SwipeRefresh.this.mFreshStatus);
                        }
                        boolean unused2 = SwipeRefresh.this.mRefreshStatusContinueRunning = false;
                        ISwipe.LoadedStatus unused3 = SwipeRefresh.this.mLoadedStatus = null;
                        boolean unused4 = SwipeRefresh.this.mLoadedStatusContinueRunning = false;
                        SwipeRefresh.this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_HEAD_COMPLETE_ERROR, -SwipeRefresh.this.getScrollY(), SwipeRefresh.this.mViewTop.getHeight());
                        if (SwipeRefresh.this.getScrollY() == 0) {
                            ISwipe.FreshStatus unused5 = SwipeRefresh.this.mFreshStatus = null;
                        } else {
                            boolean unused6 = SwipeRefresh.this.tryBackToFreshFinish();
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
            if (((this.mTargetView instanceof RecyclerView) || (this.mTargetView instanceof ListView) || (this.mTargetView instanceof ScrollView) || (this.mTargetView instanceof NestedScrollView)) && canChildScrollUp()) {
                this.mTargetView.scrollBy(0, currentScrollY);
            }
            stopAllScroll();
            scrollTo(0, this.mContentScroll);
        }
        this.mLoadedStatusContinueRunning = false;
        this.mLoadedStatus = null;
        this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_LOADING, getHeight() - this.mViewBottom.getTop(), this.mViewBottom.getHeight());
    }

    public void setLoadMoreResult(ISwipe.LoadedStatus status) {
        switch (status) {
            case ERROR:
                this.mLoadedStatusContinueRunning = true;
                this.mLoadedStatus = ISwipe.LoadedStatus.ERROR;
                this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_ERROR, getHeight() - this.mViewBottom.getTop(), this.mViewBottom.getHeight());
                return;
            case NO_MORE:
                this.mLoadedStatusContinueRunning = true;
                this.mLoadedStatus = ISwipe.LoadedStatus.NO_MORE;
                this.mSwipeController.onSwipeStatue(SwipeController.SwipeStatus.SWIPE_LOAD_NO_MORE, getHeight() - this.mViewBottom.getTop(), this.mViewBottom.getHeight());
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

    public void setSwipeController(SwipeController controller) {
        if (controller != null && this.mSwipeController != controller) {
            removeView(this.mViewTop);
            removeView(this.mViewBottom);
            this.mSwipeController = controller;
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
            addView(this.mEmptyView);
            requestLayout();
        }
    }

    public void enableEmptyView(boolean show) {
        if (this.mShowEmptyView != show) {
            this.mShowEmptyView = show;
            requestLayout();
        }
    }

    public void enableLoadMoreOverScroll(boolean enable) {
        if (this.mLoadMoreOverScroll != enable) {
            this.mLoadMoreOverScroll = enable;
            requestLayout();
        }
    }
}
