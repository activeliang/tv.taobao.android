package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EdgeEffect;
import com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView;

public abstract class AbsListView extends AbsBaseListView {
    private static final boolean DEBUG = false;
    private static final boolean PROFILE_SCROLLING = false;
    private static final String TAG = "AbsListView";
    static final Interpolator sLinearInterpolator = new LinearInterpolator();
    /* access modifiers changed from: private */
    public int mActivePointerId = -1;
    private int mDirection = 0;
    private Runnable mDisableSuppressSelectionChangedRunnable = new Runnable() {
        public void run() {
            boolean unused = AbsListView.this.mSuppressSelectionChanged = false;
            AbsListView.this.selectionChanged();
        }
    };
    protected int mDownPreLoadedCount = 0;
    /* access modifiers changed from: private */
    public EdgeEffect mEdgeGlowBottom;
    /* access modifiers changed from: private */
    public EdgeEffect mEdgeGlowTop;
    private boolean mFlingProfilingStarted = false;
    FlingRunnable mFlingRunnable = new FlingRunnable();
    Interpolator mInterpolator = null;
    protected boolean mItemsCanFocus = false;
    int mLastY;
    /* access modifiers changed from: private */
    public int mMaximumVelocity;
    /* access modifiers changed from: private */
    public int mMinimumVelocity;
    int mMotionCorrection;
    int mMotionViewOriginalTop;
    int mMotionX;
    int mMotionY;
    int mOverflingDistance;
    int mOverscrollDistance;
    private Runnable mPendingCheckForTap;
    /* access modifiers changed from: private */
    public PositionScroller mPositionScroller;
    protected boolean mReceivedInvokeKeyDown;
    private boolean mScrollProfilingStarted = false;
    int mSelectedLeft;
    protected boolean mShouldStopFling;
    protected boolean mStackFromBottom = false;
    /* access modifiers changed from: private */
    public boolean mSuppressSelectionChanged;
    private Runnable mTouchModeReset;
    private int mTouchSlop;
    protected int mUpPreLoadedCount = 0;
    private float mVelocityScale = 1.0f;
    boolean unhandleFullVisible;

    /* access modifiers changed from: package-private */
    public abstract void fillGap(boolean z);

    /* access modifiers changed from: package-private */
    public abstract int findMotionRow(int i);

    public AbsListView(Context context) {
        super(context);
        this.needMeasureSelectedView = false;
    }

    public AbsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.needMeasureSelectedView = false;
    }

    public AbsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.needMeasureSelectedView = false;
    }

    public int getUpPreLoadedCount() {
        return this.mUpPreLoadedCount;
    }

    public int getDownPreLoadedCount() {
        return this.mDownPreLoadedCount;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isLayoutRequested()) {
            this.mInLayout = true;
            layoutChildren();
            this.mInLayout = false;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        if (!this.mIsAttached) {
            return false;
        }
        switch (action & 255) {
            case 0:
                int touchMode = this.mTouchMode;
                if (touchMode == 6 || touchMode == 5) {
                    this.mMotionCorrection = 0;
                    return true;
                }
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                this.mActivePointerId = ev.getPointerId(0);
                int motionPosition = findMotionRow(y);
                if (touchMode != 4 && motionPosition >= 0) {
                    this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
                    this.mMotionX = x;
                    this.mMotionY = y;
                    this.mMotionPosition = motionPosition;
                    this.mTouchMode = 0;
                    clearScrollingCache();
                }
                this.mLastY = Integer.MIN_VALUE;
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(ev);
                if (touchMode == 4) {
                    return true;
                }
                return false;
            case 1:
            case 3:
                this.mTouchMode = -1;
                this.mActivePointerId = -1;
                recycleVelocityTracker();
                reportScrollStateChange(0);
                return false;
            case 2:
                switch (this.mTouchMode) {
                    case 0:
                        int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                        if (pointerIndex == -1) {
                            pointerIndex = 0;
                            this.mActivePointerId = ev.getPointerId(0);
                        }
                        int y2 = (int) ev.getY(pointerIndex);
                        initVelocityTrackerIfNotExists();
                        this.mVelocityTracker.addMovement(ev);
                        if (startScrollIfNeeded(y2)) {
                            return true;
                        }
                        return false;
                    default:
                        return false;
                }
            case 6:
                onSecondaryPointerUp(ev);
                return false;
            default:
                return false;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        Drawable d;
        Runnable runnable;
        if (isEnabled()) {
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
            }
            if (!this.mIsAttached) {
                return false;
            }
            int action = ev.getAction();
            initVelocityTrackerIfNotExists();
            this.mVelocityTracker.addMovement(ev);
            switch (action & 255) {
                case 0:
                    switch (this.mTouchMode) {
                        case 6:
                            this.mFlingRunnable.endFling();
                            if (this.mPositionScroller != null) {
                                this.mPositionScroller.stop();
                            }
                            this.mTouchMode = 5;
                            this.mMotionX = (int) ev.getX();
                            int y = (int) ev.getY();
                            this.mLastY = y;
                            this.mMotionY = y;
                            this.mMotionCorrection = 0;
                            this.mActivePointerId = ev.getPointerId(0);
                            this.mDirection = 0;
                            break;
                        default:
                            this.mActivePointerId = ev.getPointerId(0);
                            int x = (int) ev.getX();
                            int y2 = (int) ev.getY();
                            int motionPosition = pointToPosition(x, y2);
                            if (!this.mDataChanged) {
                                if (this.mTouchMode != 4 && motionPosition >= 0 && getAdapter().isEnabled(motionPosition)) {
                                    this.mTouchMode = 0;
                                    if (this.mPendingCheckForTap == null) {
                                        this.mPendingCheckForTap = new CheckForTap();
                                    }
                                    postDelayed(this.mPendingCheckForTap, (long) ViewConfiguration.getTapTimeout());
                                } else if (this.mTouchMode == 4) {
                                    createScrollingCache();
                                    this.mTouchMode = 3;
                                    this.mMotionCorrection = 0;
                                    motionPosition = findMotionRow(y2);
                                    this.mFlingRunnable.flywheelTouch();
                                }
                            }
                            if (motionPosition >= 0) {
                                this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
                            }
                            this.mMotionX = x;
                            this.mMotionY = y2;
                            this.mMotionPosition = motionPosition;
                            this.mLastY = Integer.MIN_VALUE;
                            break;
                    }
                    if (performButtonActionOnTouchDown(ev) && this.mTouchMode == 0) {
                        removeCallbacks(this.mPendingCheckForTap);
                        break;
                    }
                case 1:
                    switch (this.mTouchMode) {
                        case 0:
                        case 1:
                        case 2:
                            int motionPosition2 = this.mMotionPosition;
                            final View child = getChildAt(motionPosition2 - this.mFirstPosition);
                            float x2 = ev.getX();
                            boolean inList = x2 > ((float) this.mListPadding.left) && x2 < ((float) (getWidth() - this.mListPadding.right));
                            if (child != null && !child.hasFocusable() && inList) {
                                if (this.mTouchMode != 0) {
                                    child.setPressed(false);
                                }
                                if (this.mPerformClick == null) {
                                    this.mPerformClick = new AbsBaseListView.PerformClick();
                                }
                                AbsBaseListView.PerformClick performClick = this.mPerformClick;
                                performClick.mClickMotionPosition = motionPosition2;
                                performClick.rememberWindowAttachCount();
                                this.mResurrectToPosition = motionPosition2;
                                if (this.mTouchMode == 0 || this.mTouchMode == 1) {
                                    Handler handler = getHandler();
                                    if (handler != null) {
                                        if (this.mTouchMode == 0) {
                                            runnable = this.mPendingCheckForTap;
                                        } else {
                                            runnable = this.mPendingCheckForLongPress;
                                        }
                                        handler.removeCallbacks(runnable);
                                    }
                                    this.mLayoutMode = 0;
                                    if (this.mDataChanged || !this.mAdapter.isEnabled(motionPosition2)) {
                                        this.mTouchMode = -1;
                                        updateSelectorState();
                                    } else {
                                        this.mTouchMode = 1;
                                        setSelectedPositionInt(this.mMotionPosition);
                                        layoutChildren();
                                        child.setPressed(true);
                                        positionSelector(this.mMotionPosition, child);
                                        setPressed(true);
                                        if (!(this.mSelector == null || (d = this.mSelector.getCurrent()) == null || !(d instanceof TransitionDrawable))) {
                                            ((TransitionDrawable) d).resetTransition();
                                        }
                                        if (this.mTouchModeReset != null) {
                                            removeCallbacks(this.mTouchModeReset);
                                        }
                                        final AbsBaseListView.PerformClick performClick2 = performClick;
                                        this.mTouchModeReset = new Runnable() {
                                            public void run() {
                                                AbsListView.this.mTouchMode = -1;
                                                child.setPressed(false);
                                                AbsListView.this.setPressed(false);
                                                if (!AbsListView.this.mDataChanged) {
                                                    performClick2.run();
                                                }
                                            }
                                        };
                                        postDelayed(this.mTouchModeReset, (long) ViewConfiguration.getPressedStateDuration());
                                    }
                                    return true;
                                } else if (!this.mDataChanged && this.mAdapter.isEnabled(motionPosition2)) {
                                    performClick.run();
                                }
                            }
                            this.mTouchMode = -1;
                            updateSelectorState();
                            break;
                        case 3:
                            int childCount = getChildCount();
                            if (childCount <= 0) {
                                this.mTouchMode = -1;
                                reportScrollStateChange(0);
                                break;
                            } else {
                                int firstChildTop = getChildAt(0).getTop();
                                int lastChildBottom = getChildAt(childCount - 1).getBottom();
                                int contentTop = this.mListPadding.top;
                                int contentBottom = getHeight() - this.mListPadding.bottom;
                                if (this.mFirstPosition == 0 && firstChildTop >= contentTop && this.mFirstPosition + childCount < this.mItemCount && lastChildBottom <= getHeight() - contentBottom) {
                                    this.mTouchMode = -1;
                                    reportScrollStateChange(0);
                                    break;
                                } else {
                                    VelocityTracker velocityTracker = this.mVelocityTracker;
                                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                                    int initialVelocity = (int) (velocityTracker.getYVelocity(this.mActivePointerId) * this.mVelocityScale);
                                    if (Math.abs(initialVelocity) > this.mMinimumVelocity && ((this.mFirstPosition != 0 || firstChildTop != contentTop - this.mOverscrollDistance) && (this.mFirstPosition + childCount != this.mItemCount || lastChildBottom != this.mOverscrollDistance + contentBottom))) {
                                        if (this.mFlingRunnable == null) {
                                            this.mFlingRunnable = new FlingRunnable();
                                        }
                                        reportScrollStateChange(2);
                                        this.mFlingRunnable.start(-initialVelocity);
                                        break;
                                    } else {
                                        this.mTouchMode = -1;
                                        reportScrollStateChange(0);
                                        if (this.mFlingRunnable != null) {
                                            this.mFlingRunnable.endFling();
                                        }
                                        if (this.mPositionScroller != null) {
                                            this.mPositionScroller.stop();
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                        case 5:
                            if (this.mFlingRunnable == null) {
                                this.mFlingRunnable = new FlingRunnable();
                            }
                            VelocityTracker velocityTracker2 = this.mVelocityTracker;
                            velocityTracker2.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                            int initialVelocity2 = (int) velocityTracker2.getYVelocity(this.mActivePointerId);
                            reportScrollStateChange(2);
                            if (Math.abs(initialVelocity2) <= this.mMinimumVelocity) {
                                this.mFlingRunnable.startSpringback();
                                break;
                            } else {
                                this.mFlingRunnable.startOverfling(-initialVelocity2);
                                break;
                            }
                    }
                    setPressed(false);
                    if (this.mEdgeGlowTop != null) {
                        this.mEdgeGlowTop.onRelease();
                        this.mEdgeGlowBottom.onRelease();
                    }
                    invalidate();
                    Handler handler2 = getHandler();
                    if (handler2 != null) {
                        handler2.removeCallbacks(this.mPendingCheckForLongPress);
                    }
                    recycleVelocityTracker();
                    this.mActivePointerId = -1;
                    break;
                case 2:
                    int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex == -1) {
                        pointerIndex = 0;
                        this.mActivePointerId = ev.getPointerId(0);
                    }
                    int y3 = (int) ev.getY(pointerIndex);
                    if (this.mDataChanged) {
                        layoutChildren();
                    }
                    switch (this.mTouchMode) {
                        case 0:
                        case 1:
                        case 2:
                            startScrollIfNeeded(y3);
                            break;
                        case 3:
                        case 5:
                            scrollIfNeeded(y3);
                            break;
                    }
                case 3:
                    switch (this.mTouchMode) {
                        case 5:
                            if (this.mFlingRunnable == null) {
                                this.mFlingRunnable = new FlingRunnable();
                            }
                            this.mFlingRunnable.startSpringback();
                            break;
                        case 6:
                            break;
                        default:
                            this.mTouchMode = -1;
                            setPressed(false);
                            View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
                            if (motionView != null) {
                                motionView.setPressed(false);
                            }
                            clearScrollingCache();
                            Handler handler3 = getHandler();
                            if (handler3 != null) {
                                handler3.removeCallbacks(this.mPendingCheckForLongPress);
                            }
                            recycleVelocityTracker();
                            break;
                    }
                    if (this.mEdgeGlowTop != null) {
                        this.mEdgeGlowTop.onRelease();
                        this.mEdgeGlowBottom.onRelease();
                    }
                    this.mActivePointerId = -1;
                    break;
                case 5:
                    int index = ev.getActionIndex();
                    int id = ev.getPointerId(index);
                    int x3 = (int) ev.getX(index);
                    int y4 = (int) ev.getY(index);
                    this.mMotionCorrection = 0;
                    this.mActivePointerId = id;
                    this.mMotionX = x3;
                    this.mMotionY = y4;
                    int motionPosition3 = pointToPosition(x3, y4);
                    if (motionPosition3 >= 0) {
                        this.mMotionViewOriginalTop = getChildAt(motionPosition3 - this.mFirstPosition).getTop();
                        this.mMotionPosition = motionPosition3;
                    }
                    this.mLastY = y4;
                    break;
                case 6:
                    onSecondaryPointerUp(ev);
                    int x4 = this.mMotionX;
                    int y5 = this.mMotionY;
                    int motionPosition4 = pointToPosition(x4, y5);
                    if (motionPosition4 >= 0) {
                        this.mMotionViewOriginalTop = getChildAt(motionPosition4 - this.mFirstPosition).getTop();
                        this.mMotionPosition = motionPosition4;
                    }
                    this.mLastY = y5;
                    break;
            }
            return true;
        } else if (isClickable() || isLongClickable()) {
            return true;
        } else {
            return false;
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mMotionX = (int) ev.getX(newPointerIndex);
            this.mMotionY = (int) ev.getY(newPointerIndex);
            this.mMotionCorrection = 0;
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private void scrollIfNeeded(int y) {
        int incrementalDeltaY;
        int incrementalDeltaY2;
        int motionIndex;
        ViewParent parent;
        int rawDeltaY = y - this.mMotionY;
        int deltaY = rawDeltaY - this.mMotionCorrection;
        if (this.mLastY != Integer.MIN_VALUE) {
            incrementalDeltaY = y - this.mLastY;
        } else {
            incrementalDeltaY = deltaY;
        }
        if (this.mTouchMode == 3) {
            if (y != this.mLastY) {
                if ((getGroupFlags() & 524288) == 0 && Math.abs(rawDeltaY) > this.mTouchSlop && (parent = getParent()) != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                if (this.mMotionPosition >= 0) {
                    motionIndex = this.mMotionPosition - this.mFirstPosition;
                } else {
                    motionIndex = getChildCount() / 2;
                }
                int motionViewPrevTop = 0;
                View motionView = getChildAt(motionIndex);
                if (motionView != null) {
                    motionViewPrevTop = motionView.getTop();
                }
                boolean atEdge = false;
                if (incrementalDeltaY != 0) {
                    atEdge = trackMotionScroll(deltaY, incrementalDeltaY);
                }
                View motionView2 = getChildAt(motionIndex);
                if (motionView2 != null) {
                    int motionViewRealTop = motionView2.getTop();
                    if (atEdge) {
                        int overscroll = (-incrementalDeltaY) - (motionViewRealTop - motionViewPrevTop);
                        overScrollBy(0, overscroll, 0, getScrollY(), 0, 0, 0, this.mOverscrollDistance, true);
                        if (Math.abs(this.mOverscrollDistance) == Math.abs(getScrollY()) && this.mVelocityTracker != null) {
                            this.mVelocityTracker.clear();
                        }
                        int overscrollMode = getOverScrollMode();
                        if (overscrollMode == 0 || (overscrollMode == 1 && !contentFits())) {
                            this.mDirection = 0;
                            this.mTouchMode = 5;
                            if (rawDeltaY > 0) {
                                this.mEdgeGlowTop.onPull(((float) overscroll) / ((float) getHeight()));
                                if (!this.mEdgeGlowBottom.isFinished()) {
                                    this.mEdgeGlowBottom.onRelease();
                                }
                            } else if (rawDeltaY < 0) {
                                this.mEdgeGlowBottom.onPull(((float) overscroll) / ((float) getHeight()));
                                if (!this.mEdgeGlowTop.isFinished()) {
                                    this.mEdgeGlowTop.onRelease();
                                }
                            }
                        }
                    }
                    this.mMotionY = y;
                }
                this.mLastY = y;
            }
        } else if (this.mTouchMode == 5 && y != this.mLastY) {
            int oldScroll = getScrollY();
            int newScroll = oldScroll - incrementalDeltaY;
            int newDirection = y > this.mLastY ? 1 : -1;
            if (this.mDirection == 0) {
                this.mDirection = newDirection;
            }
            int overScrollDistance = -incrementalDeltaY;
            if ((newScroll >= 0 || oldScroll < 0) && (newScroll <= 0 || oldScroll > 0)) {
                incrementalDeltaY2 = 0;
            } else {
                overScrollDistance = -oldScroll;
                incrementalDeltaY2 = incrementalDeltaY + overScrollDistance;
            }
            if (overScrollDistance != 0) {
                overScrollBy(0, overScrollDistance, 0, getScrollY(), 0, 0, 0, this.mOverscrollDistance, true);
                int overscrollMode2 = getOverScrollMode();
                if (overscrollMode2 == 0 || (overscrollMode2 == 1 && !contentFits())) {
                    if (rawDeltaY > 0) {
                        this.mEdgeGlowTop.onPull(((float) overScrollDistance) / ((float) getHeight()));
                        if (!this.mEdgeGlowBottom.isFinished()) {
                            this.mEdgeGlowBottom.onRelease();
                        }
                    } else if (rawDeltaY < 0) {
                        this.mEdgeGlowBottom.onPull(((float) overScrollDistance) / ((float) getHeight()));
                        if (!this.mEdgeGlowTop.isFinished()) {
                            this.mEdgeGlowTop.onRelease();
                        }
                    }
                }
            }
            if (incrementalDeltaY2 != 0) {
                if (getScrollY() != 0) {
                    scrollTo(getScrollX(), 0);
                    invalidateParentIfNeeded();
                }
                trackMotionScroll(incrementalDeltaY2, incrementalDeltaY2);
                this.mTouchMode = 3;
                int motionPosition = findClosestMotionRow(y);
                this.mMotionCorrection = 0;
                View motionView3 = getChildAt(motionPosition - this.mFirstPosition);
                this.mMotionViewOriginalTop = motionView3 != null ? motionView3.getTop() : 0;
                this.mMotionY = y;
                this.mMotionPosition = motionPosition;
            }
            this.mLastY = y;
            this.mDirection = newDirection;
        }
    }

    /* access modifiers changed from: package-private */
    public int findClosestMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return -1;
        }
        int motionRow = findMotionRow(y);
        return motionRow == -1 ? (this.mFirstPosition + childCount) - 1 : motionRow;
    }

    /* access modifiers changed from: private */
    public boolean contentFits() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }
        if (childCount != this.mItemCount) {
            return false;
        }
        if (getChildAt(0).getTop() < this.mListPadding.top || getChildAt(childCount - 1).getBottom() > getHeight() - this.mListPadding.bottom) {
            return false;
        }
        return true;
    }

    private boolean startScrollIfNeeded(int y) {
        boolean overscroll;
        int deltaY = y - this.mMotionY;
        int distance = Math.abs(deltaY);
        if (getScrollY() != 0) {
            overscroll = true;
        } else {
            overscroll = false;
        }
        if (!overscroll && distance <= this.mTouchSlop) {
            return false;
        }
        createScrollingCache();
        if (overscroll) {
            this.mTouchMode = 5;
            this.mMotionCorrection = 0;
        } else {
            this.mTouchMode = 3;
            this.mMotionCorrection = deltaY > 0 ? this.mTouchSlop : -this.mTouchSlop;
        }
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.mPendingCheckForLongPress);
        }
        setPressed(false);
        View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
        if (motionView != null) {
            motionView.setPressed(false);
        }
        reportScrollStateChange(1);
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        scrollIfNeeded(y);
        return true;
    }

    final class CheckForTap implements Runnable {
        CheckForTap() {
        }

        public void run() {
            Drawable d;
            if (AbsListView.this.mTouchMode == 0) {
                AbsListView.this.mTouchMode = 1;
                View child = AbsListView.this.getChildAt(AbsListView.this.mMotionPosition - AbsListView.this.mFirstPosition);
                if (child != null && !child.hasFocusable()) {
                    AbsListView.this.mLayoutMode = 0;
                    if (!AbsListView.this.mDataChanged) {
                        child.setPressed(true);
                        AbsListView.this.setPressed(true);
                        AbsListView.this.layoutChildren();
                        AbsListView.this.positionSelector(AbsListView.this.mMotionPosition, child);
                        AbsListView.this.refreshDrawableState();
                        int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                        boolean longClickable = AbsListView.this.isLongClickable();
                        if (!(AbsListView.this.mSelector == null || (d = AbsListView.this.mSelector.getCurrent()) == null || !(d instanceof TransitionDrawable))) {
                            if (longClickable) {
                                ((TransitionDrawable) d).startTransition(longPressTimeout);
                            } else {
                                ((TransitionDrawable) d).resetTransition();
                            }
                        }
                        if (longClickable) {
                            if (AbsListView.this.mPendingCheckForLongPress == null) {
                                AbsListView.this.mPendingCheckForLongPress = new AbsBaseListView.CheckForLongPress();
                            }
                            AbsListView.this.mPendingCheckForLongPress.rememberWindowAttachCount();
                            AbsListView.this.postDelayed(AbsListView.this.mPendingCheckForLongPress, (long) longPressTimeout);
                            return;
                        }
                        AbsListView.this.mTouchMode = 2;
                        return;
                    }
                    AbsListView.this.mTouchMode = 2;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean trackMotionScroll(int deltaX, int incrementalDeltaX) {
        boolean isDown;
        if (getChildCount() == 0) {
            return true;
        }
        if (deltaX < 0) {
            isDown = true;
        } else {
            isDown = false;
        }
        int limitedDeltaX = deltaX;
        if (limitedDeltaX != deltaX) {
            this.mFlingRunnable.endFling();
        }
        offsetChildrenTopAndBottom(limitedDeltaX);
        detachOffScreenChildren(isDown);
        fillGap(isDown);
        onScrollChanged(0, 0, 0, 0);
        invalidate();
        return false;
    }

    /* access modifiers changed from: protected */
    public void detachOffScreenChildren(boolean isDown) {
        int numChildren = getChildCount();
        int firstPosition = this.mFirstPosition;
        int start = 0;
        int count = 0;
        if (!isDown) {
            int bottom = getHeight() - getPaddingBottom();
            for (int i = numChildren - 1; i >= 0; i--) {
                int n = i;
                View child = getChildAt(n);
                if (child.getTop() <= bottom) {
                    break;
                }
                start = n;
                count++;
                this.mRecycler.addScrapView(child, firstPosition + n);
            }
        } else {
            int top = getPaddingTop();
            for (int i2 = 0; i2 < numChildren; i2++) {
                int n2 = i2;
                View child2 = getChildAt(n2);
                if (child2.getBottom() >= top) {
                    break;
                }
                int start2 = n2;
                count++;
                this.mRecycler.addScrapView(child2, firstPosition + n2);
            }
            start = 0;
        }
        detachViewsFromParent(start, count);
        if (isDown) {
            this.mFirstPosition += count;
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        int saveCount = 0;
        boolean clipToPadding = (getGroupFlags() & 34) == 34;
        if (clipToPadding) {
            saveCount = canvas.save();
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            canvas.clipRect(getPaddingLeft() + scrollX, getPaddingTop() + scrollY, ((getRight() + scrollX) - getLeft()) - getPaddingRight(), ((getBottom() + scrollY) - getTop()) - getPaddingBottom());
            setGroupFlags(getGroupFlags() & -35);
        }
        boolean drawSelectorOnTop = drawSclectorOnTop();
        if (!drawSelectorOnTop) {
            drawSelector(canvas);
        }
        super.dispatchDraw(canvas);
        if (drawSelectorOnTop) {
            drawSelector(canvas);
        }
        if (clipToPadding) {
            canvas.restoreToCount(saveCount);
            setGroupFlags(getGroupFlags() & -35);
        }
    }

    public void setSelectionFromTop(int position, int x) {
        if (this.mAdapter != null) {
            if (!isInTouchMode()) {
                position = lookForSelectablePosition(position, true);
                if (position >= 0) {
                    setNextSelectedPositionInt(position);
                }
            } else {
                this.mResurrectToPosition = position;
            }
            if (position >= 0) {
                this.mLayoutMode = 4;
                this.mSpecificTop = this.mListPadding.left + x;
                if (this.mNeedSync) {
                    this.mSyncPosition = position;
                    this.mSyncRowId = this.mAdapter.getItemId(position);
                }
                requestLayout();
            }
        }
    }

    public void setStackFromRight(boolean stackFromRight) {
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mAdapter == null || !this.mIsAttached) {
            return false;
        }
        switch (keyCode) {
            case 23:
            case 66:
                if (!isEnabled()) {
                    return true;
                }
                if (this.mSelectedPosition >= 0 && this.mAdapter != null && this.mSelectedPosition < this.mAdapter.getCount()) {
                    View view = getChildAt(this.mSelectedPosition - this.mFirstPosition);
                    if (view != null) {
                        performItemClick(view, this.mSelectedPosition, this.mSelectedRowId);
                        view.setPressed(false);
                    }
                    setPressed(false);
                    return true;
                }
        }
        return super.onKeyUp(keyCode, event);
    }

    /* access modifiers changed from: protected */
    public void setSelectedPositionInt(int position) {
        super.setSelectedPositionInt(position);
        setNextSelectedPositionInt(position);
    }

    static int getDistance(Rect source, Rect dest, int direction) {
        int sX;
        int sY;
        int dX;
        int dY;
        switch (direction) {
            case 1:
            case 2:
                sX = source.right + (source.width() / 2);
                sY = source.top + (source.height() / 2);
                dX = dest.left + (dest.width() / 2);
                dY = dest.top + (dest.height() / 2);
                break;
            case 17:
                sX = source.left;
                sY = source.top + (source.height() / 2);
                dX = dest.right;
                dY = dest.top + (dest.height() / 2);
                break;
            case 33:
                sX = source.left + (source.width() / 2);
                sY = source.top;
                dX = dest.left + (dest.width() / 2);
                dY = dest.bottom;
                break;
            case 66:
                sX = source.right;
                sY = source.top + (source.height() / 2);
                dX = dest.left;
                dY = dest.top + (dest.height() / 2);
                break;
            case 130:
                sX = source.left + (source.width() / 2);
                sY = source.bottom;
                dX = dest.left + (dest.width() / 2);
                dY = dest.top;
                break;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
        }
        int deltaX = dX - sX;
        int deltaY = dY - sY;
        return (deltaY * deltaY) + (deltaX * deltaX);
    }

    /* access modifiers changed from: package-private */
    public boolean resurrectSelection() {
        int selectedPos;
        int childCount = getChildCount();
        if (childCount <= 0) {
            return false;
        }
        int selectedTop = 0;
        int childrenTop = this.mListPadding.top;
        int childrenBottom = (getBottom() - getTop()) - this.mListPadding.bottom;
        int firstPosition = this.mFirstPosition;
        int toPosition = this.mResurrectToPosition;
        boolean down = true;
        if (toPosition < firstPosition || toPosition >= firstPosition + childCount) {
            if (toPosition >= firstPosition) {
                int itemCount = this.mItemCount;
                down = false;
                selectedPos = (firstPosition + childCount) - 1;
                int i = childCount - 1;
                while (true) {
                    if (i < 0) {
                        break;
                    }
                    View v = getChildAt(i);
                    int top = v.getTop();
                    int bottom = v.getBottom();
                    if (i == childCount - 1) {
                        selectedTop = top;
                        if (firstPosition + childCount < itemCount || bottom > childrenBottom) {
                            childrenBottom -= getVerticalFadingEdgeLength();
                        }
                    }
                    if (bottom <= childrenBottom) {
                        selectedPos = firstPosition + i;
                        selectedTop = top;
                        break;
                    }
                    i--;
                }
            } else {
                selectedPos = firstPosition;
                int i2 = 0;
                while (true) {
                    if (i2 >= childCount) {
                        break;
                    }
                    int top2 = getChildAt(i2).getTop();
                    if (i2 == 0) {
                        selectedTop = top2;
                        if (firstPosition > 0 || top2 < childrenTop) {
                            childrenTop += getVerticalFadingEdgeLength();
                        }
                    }
                    if (top2 >= childrenTop) {
                        selectedPos = firstPosition + i2;
                        selectedTop = top2;
                        break;
                    }
                    i2++;
                }
            }
        } else {
            selectedPos = toPosition;
            View selected = getChildAt(selectedPos - this.mFirstPosition);
            selectedTop = selected.getTop();
            int selectedBottom = selected.getBottom();
            if (selectedTop < childrenTop) {
                selectedTop = childrenTop + getVerticalFadingEdgeLength();
            } else if (selectedBottom > childrenBottom) {
                selectedTop = (childrenBottom - selected.getMeasuredHeight()) - getVerticalFadingEdgeLength();
            }
        }
        this.mResurrectToPosition = -1;
        removeCallbacks(this.mFlingRunnable);
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        this.mTouchMode = -1;
        clearScrollingCache();
        this.mSpecificTop = selectedTop;
        int selectedPos2 = lookForSelectablePosition(selectedPos, down);
        if (selectedPos2 < firstPosition || selectedPos2 > getLastVisiblePosition()) {
            selectedPos2 = -1;
        } else {
            this.mLayoutMode = 4;
            updateSelectorState();
            setSelectionInt(selectedPos2);
        }
        reportScrollStateChange(0);
        if (selectedPos2 >= 0) {
            return true;
        }
        return false;
    }

    public void setDefatultScrollStep(float step) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        this.mFlingRunnable.setDefatultScrollStep(step);
    }

    public void smoothScrollBy(int distance) {
        smoothScrollBy(distance, false);
    }

    /* access modifiers changed from: package-private */
    public void smoothScrollBy(int distance, boolean linear) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        int firstPos = this.mFirstPosition;
        int childCount = getChildCount();
        int lastPos = firstPos + childCount;
        int topLimit = getPaddingTop();
        int bottomLimit = getHeight() - getPaddingBottom();
        if (distance == 0 || this.mItemCount == 0 || childCount == 0 || ((firstPos == 0 && getChildAt(0).getTop() == topLimit && distance < 0) || (lastPos == this.mItemCount && getChildAt(childCount - 1).getBottom() == bottomLimit && distance > 0))) {
            this.mFlingRunnable.endFling();
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
                return;
            }
            return;
        }
        reportScrollStateChange(2);
        this.mFlingRunnable.startScroll(distance, linear);
    }

    public void setFlipScrollFrameCount(int frameCount) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setFrameCount(frameCount);
        }
    }

    public void setFlipScrollMaxStep(float maxStep) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setMaxStep(maxStep);
        }
    }

    public boolean isListLoopScrolling() {
        if (this.mFlingRunnable != null) {
            return this.mFlingRunnable.isListLoopScrolling();
        }
        return false;
    }

    public int getLeftScrollDistance() {
        if (this.mFlingRunnable != null) {
            return this.mFlingRunnable.getLeftScrollDistance();
        }
        return 0;
    }

    public void postOnAnimation(Runnable action) {
        post(action);
    }

    /* access modifiers changed from: protected */
    public OverScroller getOverScrollerFromFlingRunnable() {
        if (this.mFlingRunnable != null) {
            return this.mFlingRunnable.mScroller;
        }
        return null;
    }

    public void setFlingInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    private class FlingRunnable implements Runnable {
        private static final int FLYWHEEL_TIMEOUT = 40;
        private final Runnable mCheckFlywheel = new Runnable() {
            public void run() {
                int activeId = AbsListView.this.mActivePointerId;
                VelocityTracker vt = AbsListView.this.mVelocityTracker;
                OverScroller scroller = FlingRunnable.this.mScroller;
                if (vt != null && activeId != -1) {
                    vt.computeCurrentVelocity(1000, (float) AbsListView.this.mMaximumVelocity);
                    float yvel = -vt.getYVelocity(activeId);
                    if (Math.abs(yvel) < ((float) AbsListView.this.mMinimumVelocity) || !scroller.isScrollingInDirection(0.0f, yvel)) {
                        FlingRunnable.this.endFling();
                        AbsListView.this.mTouchMode = 3;
                        AbsListView.this.reportScrollStateChange(1);
                        return;
                    }
                    AbsListView.this.postDelayed(this, 40);
                }
            }
        };
        private float mDefatultScrollStep = 5.0f;
        private int mFrameCount;
        private int mLastFlingY;
        private ListLoopScroller mListLoopScroller;
        /* access modifiers changed from: private */
        public final OverScroller mScroller;

        public void setDefatultScrollStep(float step) {
            this.mDefatultScrollStep = step;
        }

        FlingRunnable() {
            this.mScroller = new OverScroller(AbsListView.this.getContext());
            this.mListLoopScroller = new ListLoopScroller();
        }

        /* access modifiers changed from: package-private */
        public void start(int initialVelocity) {
            int initialY;
            if (initialVelocity < 0) {
                initialY = Integer.MAX_VALUE;
            } else {
                initialY = 0;
            }
            this.mLastFlingY = initialY;
            this.mScroller.setInterpolator((Interpolator) null);
            this.mScroller.fling(0, initialY, 0, initialVelocity, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            AbsListView.this.mTouchMode = 4;
            AbsListView.this.postOnAnimation(this);
        }

        /* access modifiers changed from: package-private */
        public void startSpringback() {
            if (this.mScroller.springBack(0, AbsListView.this.getScrollY(), 0, 0, 0, 0)) {
                AbsListView.this.mTouchMode = 6;
                AbsListView.this.invalidate();
                AbsListView.this.postOnAnimation(this);
                return;
            }
            AbsListView.this.mTouchMode = -1;
            AbsListView.this.reportScrollStateChange(0);
        }

        /* access modifiers changed from: package-private */
        public void startOverfling(int initialVelocity) {
            this.mScroller.setInterpolator((Interpolator) null);
            this.mScroller.fling(0, AbsListView.this.getScrollY(), 0, initialVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, AbsListView.this.getHeight());
            AbsListView.this.mTouchMode = 6;
            AbsListView.this.invalidate();
            AbsListView.this.postOnAnimation(this);
        }

        /* access modifiers changed from: package-private */
        public void edgeReached(int delta) {
            this.mScroller.notifyVerticalEdgeReached(AbsListView.this.getScrollY(), 0, AbsListView.this.mOverflingDistance);
            int overscrollMode = AbsListView.this.getOverScrollMode();
            if (overscrollMode == 0 || (overscrollMode == 1 && !AbsListView.this.contentFits())) {
                AbsListView.this.mTouchMode = 6;
                int vel = (int) this.mScroller.getCurrVelocity();
                if (delta > 0) {
                    AbsListView.this.mEdgeGlowTop.onAbsorb(vel);
                } else {
                    AbsListView.this.mEdgeGlowBottom.onAbsorb(vel);
                }
            } else {
                AbsListView.this.mTouchMode = -1;
                if (AbsListView.this.mPositionScroller != null) {
                    AbsListView.this.mPositionScroller.stop();
                }
            }
            AbsListView.this.invalidate();
            AbsListView.this.postOnAnimation(this);
        }

        /* access modifiers changed from: package-private */
        public void startScroll(int distance, boolean linear) {
            int frameCount;
            if (this.mFrameCount <= 0) {
                frameCount = (int) (((float) distance) / this.mDefatultScrollStep);
                if (frameCount < 0) {
                    frameCount = -frameCount;
                } else if (frameCount == 0) {
                    frameCount = 1;
                }
            } else {
                frameCount = this.mFrameCount;
            }
            this.mLastFlingY = 0;
            if (this.mListLoopScroller.isFinished()) {
                this.mListLoopScroller.startScroll(0, distance, frameCount);
                AbsListView.this.mTouchMode = 4;
                AbsListView.this.postOnAnimation(this);
                return;
            }
            this.mListLoopScroller.startScroll(0, distance, frameCount);
        }

        /* access modifiers changed from: package-private */
        public void endFling() {
            AbsListView.this.mTouchMode = -1;
            AbsListView.this.removeCallbacks(this);
            AbsListView.this.removeCallbacks(this.mCheckFlywheel);
            AbsListView.this.reportScrollStateChange(0);
            AbsListView.this.clearScrollingCache();
            this.mScroller.abortAnimation();
            this.mListLoopScroller.finish();
        }

        /* access modifiers changed from: package-private */
        public void setFrameCount(int frameCount) {
            this.mFrameCount = frameCount;
        }

        /* access modifiers changed from: package-private */
        public void setMaxStep(float maxStep) {
            this.mListLoopScroller.setMaxStep(maxStep);
        }

        /* access modifiers changed from: package-private */
        public int getLeftScrollDistance() {
            return this.mListLoopScroller.getFinal() - this.mListLoopScroller.getCurr();
        }

        /* access modifiers changed from: package-private */
        public boolean isListLoopScrolling() {
            if (AbsListView.this.mTouchMode != 4 || this.mListLoopScroller.isFinished()) {
                return false;
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public void flywheelTouch() {
            AbsListView.this.postDelayed(this.mCheckFlywheel, 40);
        }

        public void run() {
            int delta;
            switch (AbsListView.this.mTouchMode) {
                case 3:
                    if (this.mScroller.isFinished()) {
                        return;
                    }
                    break;
                case 4:
                    break;
                case 6:
                    OverScroller scroller = this.mScroller;
                    if (scroller.computeScrollOffset()) {
                        int scrollY = AbsListView.this.getScrollY();
                        int currY = scroller.getCurrY();
                        if (AbsListView.this.overScrollBy(0, currY - scrollY, 0, scrollY, 0, 0, 0, AbsListView.this.mOverflingDistance, false)) {
                            boolean crossDown = scrollY <= 0 && currY > 0;
                            boolean crossUp = scrollY >= 0 && currY < 0;
                            if (crossDown || crossUp) {
                                int velocity = (int) scroller.getCurrVelocity();
                                if (crossUp) {
                                    velocity = -velocity;
                                }
                                scroller.abortAnimation();
                                start(velocity);
                                return;
                            }
                            startSpringback();
                            return;
                        }
                        AbsListView.this.invalidate();
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    endFling();
                    return;
                default:
                    endFling();
                    return;
            }
            if (AbsListView.this.mDataChanged) {
                AbsListView.this.layoutChildren();
            }
            if (AbsListView.this.mItemCount == 0 || AbsListView.this.getChildCount() == 0) {
                endFling();
                return;
            }
            boolean more = this.mListLoopScroller.computeScrollOffset();
            int y = this.mListLoopScroller.getCurr();
            int delta2 = this.mLastFlingY - y;
            if (delta2 > 0) {
                AbsListView.this.mMotionPosition = AbsListView.this.mFirstPosition;
                AbsListView.this.mMotionViewOriginalTop = AbsListView.this.getChildAt(0).getTop();
                delta = Math.min(((AbsListView.this.getHeight() - AbsListView.this.getPaddingBottom()) - AbsListView.this.getTop()) - 1, delta2);
            } else {
                int offsetToLast = AbsListView.this.getChildCount() - 1;
                AbsListView.this.mMotionPosition = AbsListView.this.mFirstPosition + offsetToLast;
                AbsListView.this.mMotionViewOriginalTop = AbsListView.this.getChildAt(offsetToLast).getTop();
                delta = Math.max(-(((AbsListView.this.getHeight() - AbsListView.this.getPaddingBottom()) - AbsListView.this.getPaddingTop()) - 1), delta2);
            }
            View motionView = AbsListView.this.getChildAt(AbsListView.this.mMotionPosition - AbsListView.this.mFirstPosition);
            int oldTop = 0;
            if (motionView != null) {
                oldTop = motionView.getTop();
            }
            boolean atEdge = AbsListView.this.trackMotionScroll(delta, delta);
            boolean atEnd = atEdge && delta != 0;
            if (atEnd) {
                if (motionView != null) {
                    boolean unused = AbsListView.this.overScrollBy(0, -(delta - (motionView.getTop() - oldTop)), 0, AbsListView.this.getScrollY(), 0, 0, 0, AbsListView.this.mOverflingDistance, false);
                }
                if (more) {
                    edgeReached(delta);
                }
            } else if (!more || atEnd) {
                endFling();
            } else {
                if (atEdge) {
                    AbsListView.this.invalidate();
                }
                this.mLastFlingY = y;
                AbsListView.this.postOnAnimation(this);
            }
        }
    }

    class PositionScroller implements Runnable {
        private static final int MOVE_DOWN_BOUND = 3;
        private static final int MOVE_DOWN_POS = 1;
        private static final int MOVE_OFFSET = 5;
        private static final int MOVE_UP_BOUND = 4;
        private static final int MOVE_UP_POS = 2;
        private static final int SCROLL_DURATION = 400;
        private int mBoundPos;
        private final int mExtraScroll;
        private int mLastSeenPos;
        private int mMode;
        private int mOffsetFromTop;
        private int mScrollDuration;
        private int mTargetPos;

        PositionScroller() {
            this.mExtraScroll = ViewConfiguration.get(AbsListView.this.getContext()).getScaledFadingEdgeLength();
        }

        /* access modifiers changed from: package-private */
        public void start(int position) {
            int viewTravelCount;
            stop();
            int firstPos = AbsListView.this.mFirstPosition;
            int lastPos = (AbsListView.this.getChildCount() + firstPos) - 1;
            if (position <= firstPos) {
                viewTravelCount = (firstPos - position) + 1;
                this.mMode = 2;
            } else if (position >= lastPos) {
                viewTravelCount = (position - lastPos) + 1;
                this.mMode = 1;
            } else {
                return;
            }
            if (viewTravelCount > 0) {
                this.mScrollDuration = 400 / viewTravelCount;
            } else {
                this.mScrollDuration = 400;
            }
            this.mTargetPos = position;
            this.mBoundPos = -1;
            this.mLastSeenPos = -1;
            AbsListView.this.post(this);
        }

        /* access modifiers changed from: package-private */
        public void start(int position, int boundPosition) {
            int boundPosFromFirst;
            int viewTravelCount;
            stop();
            if (boundPosition == -1) {
                start(position);
                return;
            }
            int firstPos = AbsListView.this.mFirstPosition;
            int lastPos = (AbsListView.this.getChildCount() + firstPos) - 1;
            if (position <= firstPos) {
                int boundPosFromLast = lastPos - boundPosition;
                if (boundPosFromLast >= 1) {
                    int posTravel = (firstPos - position) + 1;
                    int boundTravel = boundPosFromLast - 1;
                    if (boundTravel < posTravel) {
                        viewTravelCount = boundTravel;
                        this.mMode = 4;
                    } else {
                        viewTravelCount = posTravel;
                        this.mMode = 2;
                    }
                } else {
                    return;
                }
            } else if (position >= lastPos && (boundPosFromFirst = boundPosition - firstPos) >= 1) {
                int posTravel2 = (position - lastPos) + 1;
                int boundTravel2 = boundPosFromFirst - 1;
                if (boundTravel2 < posTravel2) {
                    viewTravelCount = boundTravel2;
                    this.mMode = 3;
                } else {
                    viewTravelCount = posTravel2;
                    this.mMode = 1;
                }
            } else {
                return;
            }
            if (viewTravelCount > 0) {
                this.mScrollDuration = 400 / viewTravelCount;
            } else {
                this.mScrollDuration = 400;
            }
            this.mTargetPos = position;
            this.mBoundPos = boundPosition;
            this.mLastSeenPos = -1;
            AbsListView.this.post(this);
        }

        /* access modifiers changed from: package-private */
        public void startWithOffset(int position, int offset) {
            startWithOffset(position, offset, 400);
        }

        /* access modifiers changed from: package-private */
        public void startWithOffset(int position, int offset, int duration) {
            int viewTravelCount;
            stop();
            this.mTargetPos = position;
            this.mOffsetFromTop = offset;
            this.mBoundPos = -1;
            this.mLastSeenPos = -1;
            this.mMode = 5;
            int firstPos = AbsListView.this.mFirstPosition;
            int childCount = AbsListView.this.getChildCount();
            int lastPos = (firstPos + childCount) - 1;
            if (position < firstPos) {
                viewTravelCount = firstPos - position;
            } else if (position > lastPos) {
                viewTravelCount = position - lastPos;
            } else {
                AbsListView.this.smoothScrollBy(AbsListView.this.getChildAt(position - firstPos).getTop() - offset);
                return;
            }
            float screenTravelCount = ((float) viewTravelCount) / ((float) childCount);
            this.mScrollDuration = screenTravelCount < 1.0f ? (int) (((float) duration) * screenTravelCount) : (int) (((float) duration) / screenTravelCount);
            this.mLastSeenPos = -1;
            AbsListView.this.post(this);
        }

        /* access modifiers changed from: package-private */
        public void stop() {
            AbsListView.this.removeCallbacks(this);
        }

        public void run() {
            int extraScroll;
            int extraScroll2;
            if (AbsListView.this.mTouchMode == 4 || this.mLastSeenPos == -1) {
                int listHeight = AbsListView.this.getHeight();
                int firstPos = AbsListView.this.mFirstPosition;
                switch (this.mMode) {
                    case 1:
                        int lastViewIndex = AbsListView.this.getChildCount() - 1;
                        int lastPos = firstPos + lastViewIndex;
                        if (lastViewIndex < 0) {
                            return;
                        }
                        if (lastPos == this.mLastSeenPos) {
                            AbsListView.this.post(this);
                            return;
                        }
                        View lastView = AbsListView.this.getChildAt(lastViewIndex);
                        int lastViewHeight = lastView.getHeight();
                        int lastViewPixelsShowing = listHeight - lastView.getTop();
                        if (lastPos < AbsListView.this.mItemCount - 1) {
                            extraScroll2 = this.mExtraScroll;
                        } else {
                            extraScroll2 = AbsListView.this.mListPadding.bottom;
                        }
                        AbsListView.this.smoothScrollBy((lastViewHeight - lastViewPixelsShowing) + extraScroll2);
                        this.mLastSeenPos = lastPos;
                        if (lastPos < this.mTargetPos) {
                            AbsListView.this.post(this);
                            return;
                        }
                        return;
                    case 2:
                        if (firstPos == this.mLastSeenPos) {
                            AbsListView.this.post(this);
                            return;
                        }
                        View firstView = AbsListView.this.getChildAt(0);
                        if (firstView != null) {
                            int firstViewTop = firstView.getTop();
                            if (firstPos > 0) {
                                extraScroll = this.mExtraScroll;
                            } else {
                                extraScroll = AbsListView.this.mListPadding.top;
                            }
                            AbsListView.this.smoothScrollBy(firstViewTop - extraScroll);
                            this.mLastSeenPos = firstPos;
                            if (firstPos > this.mTargetPos) {
                                AbsListView.this.post(this);
                                return;
                            }
                            return;
                        }
                        return;
                    case 3:
                        int childCount = AbsListView.this.getChildCount();
                        if (firstPos != this.mBoundPos && childCount > 1 && firstPos + childCount < AbsListView.this.mItemCount) {
                            int nextPos = firstPos + 1;
                            if (nextPos == this.mLastSeenPos) {
                                AbsListView.this.post(this);
                                return;
                            }
                            View nextView = AbsListView.this.getChildAt(1);
                            int nextViewHeight = nextView.getHeight();
                            int nextViewTop = nextView.getTop();
                            int extraScroll3 = this.mExtraScroll;
                            if (nextPos < this.mBoundPos) {
                                AbsListView.this.smoothScrollBy(Math.max(0, (nextViewHeight + nextViewTop) - extraScroll3));
                                this.mLastSeenPos = nextPos;
                                AbsListView.this.post(this);
                                return;
                            } else if (nextViewTop > extraScroll3) {
                                AbsListView.this.smoothScrollBy(nextViewTop - extraScroll3);
                                return;
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    case 4:
                        int lastViewIndex2 = AbsListView.this.getChildCount() - 2;
                        if (lastViewIndex2 >= 0) {
                            int lastPos2 = firstPos + lastViewIndex2;
                            if (lastPos2 == this.mLastSeenPos) {
                                AbsListView.this.post(this);
                                return;
                            }
                            View lastView2 = AbsListView.this.getChildAt(lastViewIndex2);
                            int lastViewHeight2 = lastView2.getHeight();
                            int lastViewTop = lastView2.getTop();
                            int lastViewPixelsShowing2 = listHeight - lastViewTop;
                            this.mLastSeenPos = lastPos2;
                            if (lastPos2 > this.mBoundPos) {
                                AbsListView.this.smoothScrollBy(-(lastViewPixelsShowing2 - this.mExtraScroll));
                                AbsListView.this.post(this);
                                return;
                            }
                            int bottom = listHeight - this.mExtraScroll;
                            int lastViewBottom = lastViewTop + lastViewHeight2;
                            if (bottom > lastViewBottom) {
                                AbsListView.this.smoothScrollBy(-(bottom - lastViewBottom));
                                return;
                            }
                            return;
                        }
                        return;
                    case 5:
                        if (this.mLastSeenPos == firstPos) {
                            AbsListView.this.post(this);
                            return;
                        }
                        this.mLastSeenPos = firstPos;
                        int childCount2 = AbsListView.this.getChildCount();
                        int position = this.mTargetPos;
                        int lastPos3 = (firstPos + childCount2) - 1;
                        int viewTravelCount = 0;
                        if (position < firstPos) {
                            viewTravelCount = (firstPos - position) + 1;
                        } else if (position > lastPos3) {
                            viewTravelCount = position - lastPos3;
                        }
                        float modifier = Math.min(Math.abs(((float) viewTravelCount) / ((float) childCount2)), 1.0f);
                        if (position < firstPos) {
                            AbsListView.this.smoothScrollBy((int) (((float) (-AbsListView.this.getHeight())) * modifier));
                            AbsListView.this.post(this);
                            return;
                        } else if (position > lastPos3) {
                            AbsListView.this.smoothScrollBy((int) (((float) AbsListView.this.getHeight()) * modifier));
                            AbsListView.this.post(this);
                            return;
                        } else {
                            AbsListView.this.smoothScrollBy(AbsListView.this.getChildAt(position - firstPos).getTop() - this.mOffsetFromTop);
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    }
}
