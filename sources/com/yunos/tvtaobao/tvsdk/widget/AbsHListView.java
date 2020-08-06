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

public abstract class AbsHListView extends AbsBaseListView {
    private static final boolean DEBUG = false;
    private static final boolean PROFILE_SCROLLING = false;
    private static final String TAG = "AbsHListView";
    static final Interpolator sLinearInterpolator = new LinearInterpolator();
    /* access modifiers changed from: private */
    public int mActivePointerId = -1;
    private int mDirection = 0;
    private Runnable mDisableSuppressSelectionChangedRunnable = new Runnable() {
        public void run() {
            boolean unused = AbsHListView.this.mSuppressSelectionChanged = false;
            AbsHListView.this.selectionChanged();
        }
    };
    /* access modifiers changed from: private */
    public EdgeEffect mEdgeGlowLeft;
    /* access modifiers changed from: private */
    public EdgeEffect mEdgeGlowRight;
    private boolean mFlingProfilingStarted = false;
    FlingRunnable mFlingRunnable = new FlingRunnable();
    Interpolator mInterpolator = null;
    protected boolean mItemsCanFocus = false;
    int mLastX;
    /* access modifiers changed from: private */
    public int mMaximumVelocity;
    /* access modifiers changed from: private */
    public int mMinimumVelocity;
    int mMotionCorrection;
    int mMotionViewOriginalLeft;
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
    int mSpecificLeft;
    protected boolean mStackFromBottom = false;
    /* access modifiers changed from: private */
    public boolean mSuppressSelectionChanged;
    private Runnable mTouchModeReset;
    private int mTouchSlop;
    private float mVelocityScale = 1.0f;
    boolean unhandleFullVisible;

    /* access modifiers changed from: package-private */
    public abstract void fillGap(boolean z);

    /* access modifiers changed from: package-private */
    public abstract int findMotionRow(int i);

    public AbsHListView(Context context) {
        super(context);
        this.needMeasureSelectedView = false;
    }

    public AbsHListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.needMeasureSelectedView = false;
    }

    public AbsHListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.needMeasureSelectedView = false;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInLayout = true;
        layoutChildren();
        this.mInLayout = false;
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
                int motionPosition = findMotionRow(x);
                if (touchMode != 4 && motionPosition >= 0) {
                    this.mMotionViewOriginalLeft = getChildAt(motionPosition - this.mFirstPosition).getLeft();
                    this.mMotionX = x;
                    this.mMotionY = y;
                    this.mMotionPosition = motionPosition;
                    this.mTouchMode = 0;
                    clearScrollingCache();
                }
                this.mLastX = Integer.MIN_VALUE;
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
                        int x2 = (int) ev.getX(pointerIndex);
                        initVelocityTrackerIfNotExists();
                        this.mVelocityTracker.addMovement(ev);
                        if (startScrollIfNeeded(x2)) {
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
                            int x = (int) ev.getX();
                            this.mLastX = x;
                            this.mMotionX = x;
                            this.mMotionY = (int) ev.getY();
                            this.mMotionCorrection = 0;
                            this.mActivePointerId = ev.getPointerId(0);
                            this.mDirection = 0;
                            break;
                        default:
                            this.mActivePointerId = ev.getPointerId(0);
                            int x2 = (int) ev.getX();
                            int y = (int) ev.getY();
                            int motionPosition = pointToPosition(x2, y);
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
                                    motionPosition = findMotionRow(x2);
                                    this.mFlingRunnable.flywheelTouch();
                                }
                            }
                            if (motionPosition >= 0) {
                                this.mMotionViewOriginalLeft = getChildAt(motionPosition - this.mFirstPosition).getLeft();
                            }
                            this.mMotionX = x2;
                            this.mMotionY = y;
                            this.mMotionPosition = motionPosition;
                            this.mLastX = Integer.MIN_VALUE;
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
                            float x3 = ev.getX();
                            boolean inList = x3 > ((float) this.mListPadding.left) && x3 < ((float) (getWidth() - this.mListPadding.right));
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
                                                AbsHListView.this.mTouchMode = -1;
                                                child.setPressed(false);
                                                AbsHListView.this.setPressed(false);
                                                if (!AbsHListView.this.mDataChanged) {
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
                                int firstChildLeft = getChildAt(0).getLeft();
                                int lastChildRight = getChildAt(childCount - 1).getRight();
                                int contentLeft = this.mListPadding.left;
                                int contentRight = getWidth() - this.mListPadding.right;
                                if (this.mFirstPosition == 0 && firstChildLeft >= contentLeft && this.mFirstPosition + childCount < this.mItemCount && lastChildRight <= getWidth() - contentRight) {
                                    this.mTouchMode = -1;
                                    reportScrollStateChange(0);
                                    break;
                                } else {
                                    VelocityTracker velocityTracker = this.mVelocityTracker;
                                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                                    int initialVelocity = (int) (velocityTracker.getYVelocity(this.mActivePointerId) * this.mVelocityScale);
                                    if (Math.abs(initialVelocity) > this.mMinimumVelocity && ((this.mFirstPosition != 0 || firstChildLeft != contentLeft - this.mOverscrollDistance) && (this.mFirstPosition + childCount != this.mItemCount || lastChildRight != this.mOverscrollDistance + contentRight))) {
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
                    if (this.mEdgeGlowLeft != null) {
                        this.mEdgeGlowLeft.onRelease();
                        this.mEdgeGlowRight.onRelease();
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
                    int x4 = (int) ev.getX(pointerIndex);
                    if (this.mDataChanged) {
                        layoutChildren();
                    }
                    switch (this.mTouchMode) {
                        case 0:
                        case 1:
                        case 2:
                            startScrollIfNeeded(x4);
                            break;
                        case 3:
                        case 5:
                            scrollIfNeeded(x4);
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
                    if (this.mEdgeGlowLeft != null) {
                        this.mEdgeGlowLeft.onRelease();
                        this.mEdgeGlowRight.onRelease();
                    }
                    this.mActivePointerId = -1;
                    break;
                case 5:
                    int index = ev.getActionIndex();
                    int id = ev.getPointerId(index);
                    int x5 = (int) ev.getX(index);
                    int y2 = (int) ev.getY(index);
                    this.mMotionCorrection = 0;
                    this.mActivePointerId = id;
                    this.mMotionX = x5;
                    this.mMotionY = y2;
                    int motionPosition3 = pointToPosition(x5, y2);
                    if (motionPosition3 >= 0) {
                        this.mMotionViewOriginalLeft = getChildAt(motionPosition3 - this.mFirstPosition).getLeft();
                        this.mMotionPosition = motionPosition3;
                    }
                    this.mLastX = x5;
                    break;
                case 6:
                    onSecondaryPointerUp(ev);
                    int x6 = this.mMotionX;
                    int motionPosition4 = pointToPosition(x6, this.mMotionY);
                    if (motionPosition4 >= 0) {
                        this.mMotionViewOriginalLeft = getChildAt(motionPosition4 - this.mFirstPosition).getLeft();
                        this.mMotionPosition = motionPosition4;
                    }
                    this.mLastX = x6;
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

    private void scrollIfNeeded(int x) {
        int incrementalDeltaX;
        int incrementalDeltaX2;
        int motionIndex;
        ViewParent parent;
        int rawDeltaX = x - this.mMotionX;
        int deltaX = rawDeltaX - this.mMotionCorrection;
        if (this.mLastX != Integer.MIN_VALUE) {
            incrementalDeltaX = x - this.mLastX;
        } else {
            incrementalDeltaX = deltaX;
        }
        if (this.mTouchMode == 3) {
            if (x != this.mLastX) {
                if ((getGroupFlags() & 524288) == 0 && Math.abs(rawDeltaX) > this.mTouchSlop && (parent = getParent()) != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                if (this.mMotionPosition >= 0) {
                    motionIndex = this.mMotionPosition - this.mFirstPosition;
                } else {
                    motionIndex = getChildCount() / 2;
                }
                int motionViewPrevLeft = 0;
                View motionView = getChildAt(motionIndex);
                if (motionView != null) {
                    motionViewPrevLeft = motionView.getLeft();
                }
                boolean atEdge = false;
                if (incrementalDeltaX != 0) {
                    atEdge = trackMotionScroll(deltaX, incrementalDeltaX);
                }
                View motionView2 = getChildAt(motionIndex);
                if (motionView2 != null) {
                    int motionViewRealLeft = motionView2.getLeft();
                    if (atEdge) {
                        int overscroll = (-incrementalDeltaX) - (motionViewRealLeft - motionViewPrevLeft);
                        overScrollBy(overscroll, 0, getScrollX(), 0, 0, 0, this.mOverscrollDistance, 0, true);
                        if (Math.abs(this.mOverscrollDistance) == Math.abs(getScrollX()) && this.mVelocityTracker != null) {
                            this.mVelocityTracker.clear();
                        }
                        int overscrollMode = getOverScrollMode();
                        if (overscrollMode == 0 || (overscrollMode == 1 && !contentFits())) {
                            this.mDirection = 0;
                            this.mTouchMode = 5;
                            if (rawDeltaX > 0) {
                                this.mEdgeGlowLeft.onPull(((float) overscroll) / ((float) getWidth()));
                                if (!this.mEdgeGlowRight.isFinished()) {
                                    this.mEdgeGlowRight.onRelease();
                                }
                            } else if (rawDeltaX < 0) {
                                this.mEdgeGlowRight.onPull(((float) overscroll) / ((float) getWidth()));
                                if (!this.mEdgeGlowLeft.isFinished()) {
                                    this.mEdgeGlowLeft.onRelease();
                                }
                            }
                        }
                    }
                    this.mMotionX = x;
                }
                this.mLastX = x;
            }
        } else if (this.mTouchMode == 5 && x != this.mLastX) {
            int oldScroll = getScrollX();
            int newScroll = oldScroll - incrementalDeltaX;
            int newDirection = x > this.mLastX ? 1 : -1;
            if (this.mDirection == 0) {
                this.mDirection = newDirection;
            }
            int overScrollDistance = -incrementalDeltaX;
            if ((newScroll >= 0 || oldScroll < 0) && (newScroll <= 0 || oldScroll > 0)) {
                incrementalDeltaX2 = 0;
            } else {
                overScrollDistance = -oldScroll;
                incrementalDeltaX2 = incrementalDeltaX + overScrollDistance;
            }
            if (overScrollDistance != 0) {
                overScrollBy(overScrollDistance, 0, getScrollX(), 0, 0, 0, this.mOverscrollDistance, 0, true);
                int overscrollMode2 = getOverScrollMode();
                if (overscrollMode2 == 0 || (overscrollMode2 == 1 && !contentFits())) {
                    if (rawDeltaX > 0) {
                        this.mEdgeGlowLeft.onPull(((float) overScrollDistance) / ((float) getWidth()));
                        if (!this.mEdgeGlowRight.isFinished()) {
                            this.mEdgeGlowRight.onRelease();
                        }
                    } else if (rawDeltaX < 0) {
                        this.mEdgeGlowRight.onPull(((float) overScrollDistance) / ((float) getWidth()));
                        if (!this.mEdgeGlowLeft.isFinished()) {
                            this.mEdgeGlowLeft.onRelease();
                        }
                    }
                }
            }
            if (incrementalDeltaX2 != 0) {
                if (getScrollX() != 0) {
                    scrollTo(0, getScrollY());
                    invalidateParentIfNeeded();
                }
                trackMotionScroll(incrementalDeltaX2, incrementalDeltaX2);
                this.mTouchMode = 3;
                int motionPosition = findClosestMotionRow(x);
                this.mMotionCorrection = 0;
                View motionView3 = getChildAt(motionPosition - this.mFirstPosition);
                this.mMotionViewOriginalLeft = motionView3 != null ? motionView3.getLeft() : 0;
                this.mMotionX = x;
                this.mMotionPosition = motionPosition;
            }
            this.mLastX = x;
            this.mDirection = newDirection;
        }
    }

    /* access modifiers changed from: package-private */
    public int findClosestMotionRow(int x) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return -1;
        }
        int motionRow = findMotionRow(x);
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
        if (getChildAt(0).getLeft() < this.mListPadding.left || getChildAt(childCount - 1).getRight() > getWidth() - this.mListPadding.right) {
            return false;
        }
        return true;
    }

    private boolean startScrollIfNeeded(int x) {
        boolean overscroll;
        int deltaX = x - this.mMotionX;
        int distance = Math.abs(deltaX);
        if (getScrollX() != 0) {
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
            this.mMotionCorrection = deltaX > 0 ? this.mTouchSlop : -this.mTouchSlop;
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
        scrollIfNeeded(x);
        return true;
    }

    final class CheckForTap implements Runnable {
        CheckForTap() {
        }

        public void run() {
            Drawable d;
            if (AbsHListView.this.mTouchMode == 0) {
                AbsHListView.this.mTouchMode = 1;
                View child = AbsHListView.this.getChildAt(AbsHListView.this.mMotionPosition - AbsHListView.this.mFirstPosition);
                if (child != null && !child.hasFocusable()) {
                    AbsHListView.this.mLayoutMode = 0;
                    if (!AbsHListView.this.mDataChanged) {
                        child.setPressed(true);
                        AbsHListView.this.setPressed(true);
                        AbsHListView.this.layoutChildren();
                        AbsHListView.this.positionSelector(AbsHListView.this.mMotionPosition, child);
                        AbsHListView.this.refreshDrawableState();
                        int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                        boolean longClickable = AbsHListView.this.isLongClickable();
                        if (!(AbsHListView.this.mSelector == null || (d = AbsHListView.this.mSelector.getCurrent()) == null || !(d instanceof TransitionDrawable))) {
                            if (longClickable) {
                                ((TransitionDrawable) d).startTransition(longPressTimeout);
                            } else {
                                ((TransitionDrawable) d).resetTransition();
                            }
                        }
                        if (longClickable) {
                            if (AbsHListView.this.mPendingCheckForLongPress == null) {
                                AbsHListView.this.mPendingCheckForLongPress = new AbsBaseListView.CheckForLongPress();
                            }
                            AbsHListView.this.mPendingCheckForLongPress.rememberWindowAttachCount();
                            AbsHListView.this.postDelayed(AbsHListView.this.mPendingCheckForLongPress, (long) longPressTimeout);
                            return;
                        }
                        AbsHListView.this.mTouchMode = 2;
                        return;
                    }
                    AbsHListView.this.mTouchMode = 2;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean trackMotionScroll(int deltaX, int incrementalDeltaX) {
        boolean isRight;
        if (getChildCount() == 0) {
            return true;
        }
        if (deltaX < 0) {
            isRight = true;
        } else {
            isRight = false;
        }
        int limitedDeltaX = deltaX;
        if (limitedDeltaX != deltaX) {
            this.mFlingRunnable.endFling();
        }
        offsetChildrenLeftAndRight(limitedDeltaX);
        detachOffScreenChildren(isRight);
        fillGap(isRight);
        onScrollChanged(0, 0, 0, 0);
        invalidate();
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean detachOffScreenChildren(boolean isRight) {
        int left;
        int numChildren = getChildCount();
        int firstPosition = this.mFirstPosition;
        int start = 0;
        int count = 0;
        if (!isRight) {
            int right = (getGroupFlags() & 34) == 34 ? getWidth() - getPaddingRight() : getWidth();
            for (int i = numChildren - 1; i >= 0; i--) {
                int n = i;
                View child = getChildAt(n);
                if (child.getLeft() <= right) {
                    break;
                }
                start = n;
                count++;
                this.mRecycler.addScrapView(child, firstPosition + n);
            }
        } else {
            if ((getGroupFlags() & 34) == 34) {
                left = getPaddingLeft();
            } else {
                left = 0;
            }
            for (int i2 = 0; i2 < numChildren; i2++) {
                int n2 = i2;
                View child2 = getChildAt(n2);
                if (child2.getRight() >= left) {
                    break;
                }
                int start2 = n2;
                count++;
                this.mRecycler.addScrapView(child2, firstPosition + n2);
            }
            start = 0;
        }
        detachViewsFromParent(start, count);
        if (isRight) {
            this.mFirstPosition += count;
        }
        if (count > 0) {
            return true;
        }
        return false;
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

    public void setSelectionFromLeft(int position, int y) {
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
                this.mSpecificLeft = this.mListPadding.top + y;
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
                    if (view != null && view.isPressed()) {
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
        int selectedLeft = 0;
        int childrenLeft = this.mListPadding.top;
        int childrenRight = (getRight() - getLeft()) - this.mListPadding.right;
        int firstPosition = this.mFirstPosition;
        int toPosition = this.mResurrectToPosition;
        boolean isRight = true;
        if (toPosition < firstPosition || toPosition >= firstPosition + childCount) {
            if (toPosition >= firstPosition) {
                int itemCount = this.mItemCount;
                isRight = false;
                selectedPos = (firstPosition + childCount) - 1;
                int i = childCount - 1;
                while (true) {
                    if (i < 0) {
                        break;
                    }
                    View v = getChildAt(i);
                    int left = v.getLeft();
                    int right = v.getRight();
                    if (i == childCount - 1) {
                        selectedLeft = left;
                        if (firstPosition + childCount < itemCount || right > childrenRight) {
                            childrenRight -= getHorizontalFadingEdgeLength();
                        }
                    }
                    if (right <= childrenRight) {
                        selectedPos = firstPosition + i;
                        selectedLeft = left;
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
                    int left2 = getChildAt(i2).getLeft();
                    if (i2 == 0) {
                        selectedLeft = left2;
                        if (firstPosition > 0 || left2 < childrenLeft) {
                            childrenLeft += getHorizontalFadingEdgeLength();
                        }
                    }
                    if (left2 >= childrenLeft) {
                        selectedPos = firstPosition + i2;
                        selectedLeft = left2;
                        break;
                    }
                    i2++;
                }
            }
        } else {
            selectedPos = toPosition;
            View selected = getChildAt(selectedPos - this.mFirstPosition);
            selectedLeft = selected.getLeft();
            int selectedRight = selected.getRight();
            if (selectedLeft < childrenLeft) {
                selectedLeft = childrenLeft + getHorizontalFadingEdgeLength();
            } else if (selectedRight > childrenRight) {
                selectedLeft = (childrenRight - selected.getMeasuredWidth()) - getHorizontalFadingEdgeLength();
            }
        }
        this.mResurrectToPosition = -1;
        removeCallbacks(this.mFlingRunnable);
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        this.mTouchMode = -1;
        clearScrollingCache();
        this.mSpecificLeft = selectedLeft;
        int selectedPos2 = lookForSelectablePosition(selectedPos, isRight);
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
        int leftLimit = getPaddingLeft();
        int rightLimit = getWidth() - getPaddingRight();
        if (distance == 0 || this.mItemCount == 0 || childCount == 0 || ((firstPos == 0 && getChildAt(0).getLeft() == leftLimit && distance < 0) || (lastPos == this.mItemCount && getChildAt(childCount - 1).getRight() == rightLimit && distance > 0))) {
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

    public void postOnAnimation(Runnable action) {
        post(action);
    }

    public void setFlipScrollFrameCount(int frameCount) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setFrameCount(frameCount);
        }
    }

    public void setFlingScrollMaxStep(float maxStep) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setMaxStep(maxStep);
        }
    }

    public void setFlingSlowDownRatio(float ratio) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setSlowDownRatio(ratio);
        }
    }

    public int getLeftScrollDistance() {
        if (this.mFlingRunnable != null) {
            return this.mFlingRunnable.getLeftScrollDistance();
        }
        return 0;
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
                int activeId = AbsHListView.this.mActivePointerId;
                VelocityTracker vt = AbsHListView.this.mVelocityTracker;
                OverScroller scroller = FlingRunnable.this.mScroller;
                if (vt != null && activeId != -1) {
                    vt.computeCurrentVelocity(1000, (float) AbsHListView.this.mMaximumVelocity);
                    float yvel = -vt.getYVelocity(activeId);
                    if (Math.abs(yvel) < ((float) AbsHListView.this.mMinimumVelocity) || !scroller.isScrollingInDirection(0.0f, yvel)) {
                        FlingRunnable.this.endFling();
                        AbsHListView.this.mTouchMode = 3;
                        AbsHListView.this.reportScrollStateChange(1);
                        return;
                    }
                    AbsHListView.this.postDelayed(this, 40);
                }
            }
        };
        private float mDefatultScrollStep = 5.0f;
        private int mFrameCount;
        private int mLastFlingX;
        private ListLoopScroller mListLoopScroller;
        /* access modifiers changed from: private */
        public final OverScroller mScroller;

        FlingRunnable() {
            this.mScroller = new OverScroller(AbsHListView.this.getContext());
            this.mListLoopScroller = new ListLoopScroller();
        }

        /* access modifiers changed from: package-private */
        public void start(int initialVelocitx) {
            int initialX;
            if (initialVelocitx < 0) {
                initialX = Integer.MAX_VALUE;
            } else {
                initialX = 0;
            }
            this.mLastFlingX = initialX;
            this.mScroller.setInterpolator((Interpolator) null);
            this.mScroller.fling(initialX, 0, initialVelocitx, 0, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            AbsHListView.this.mTouchMode = 4;
            AbsHListView.this.postOnAnimation(this);
        }

        /* access modifiers changed from: package-private */
        public void startSpringback() {
            if (this.mScroller.springBack(AbsHListView.this.getScrollX(), 0, 0, 0, 0, 0)) {
                AbsHListView.this.mTouchMode = 6;
                AbsHListView.this.invalidate();
                AbsHListView.this.postOnAnimation(this);
                return;
            }
            AbsHListView.this.mTouchMode = -1;
            AbsHListView.this.reportScrollStateChange(0);
        }

        /* access modifiers changed from: package-private */
        public void startOverfling(int initialVelocitx) {
            this.mScroller.setInterpolator((Interpolator) null);
            this.mScroller.fling(AbsHListView.this.getScrollX(), 0, initialVelocitx, 0, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, AbsHListView.this.getWidth(), 0);
            AbsHListView.this.mTouchMode = 6;
            AbsHListView.this.invalidate();
            AbsHListView.this.postOnAnimation(this);
        }

        /* access modifiers changed from: package-private */
        public void edgeReached(int delta) {
            this.mScroller.notifyHorizontalEdgeReached(AbsHListView.this.getScrollX(), 0, AbsHListView.this.mOverflingDistance);
            int overscrollMode = AbsHListView.this.getOverScrollMode();
            if (overscrollMode == 0 || (overscrollMode == 1 && !AbsHListView.this.contentFits())) {
                AbsHListView.this.mTouchMode = 6;
                int vel = (int) this.mScroller.getCurrVelocity();
                if (delta > 0) {
                    AbsHListView.this.mEdgeGlowLeft.onAbsorb(vel);
                } else {
                    AbsHListView.this.mEdgeGlowRight.onAbsorb(vel);
                }
            } else {
                AbsHListView.this.mTouchMode = -1;
                if (AbsHListView.this.mPositionScroller != null) {
                    AbsHListView.this.mPositionScroller.stop();
                }
            }
            AbsHListView.this.invalidate();
            AbsHListView.this.postOnAnimation(this);
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
            this.mLastFlingX = 0;
            if (this.mListLoopScroller.isFinished()) {
                this.mListLoopScroller.startScroll(0, distance, frameCount);
                AbsHListView.this.mTouchMode = 4;
                AbsHListView.this.postOnAnimation(this);
                return;
            }
            this.mListLoopScroller.startScroll(0, distance, frameCount);
        }

        /* access modifiers changed from: package-private */
        public void endFling() {
            AbsHListView.this.mTouchMode = -1;
            AbsHListView.this.removeCallbacks(this);
            AbsHListView.this.removeCallbacks(this.mCheckFlywheel);
            AbsHListView.this.reportScrollStateChange(0);
            AbsHListView.this.clearScrollingCache();
            this.mScroller.abortAnimation();
            this.mListLoopScroller.finish();
        }

        /* access modifiers changed from: package-private */
        public int getLeftScrollDistance() {
            return this.mListLoopScroller.getFinal() - this.mListLoopScroller.getCurr();
        }

        public void setFrameCount(int frameCount) {
            this.mFrameCount = frameCount;
        }

        public void setMaxStep(float maxStep) {
            this.mListLoopScroller.setMaxStep(maxStep);
        }

        public void setSlowDownRatio(float ratio) {
            this.mListLoopScroller.setSlowDownRatio(ratio);
        }

        /* access modifiers changed from: package-private */
        public void flywheelTouch() {
            AbsHListView.this.postDelayed(this.mCheckFlywheel, 40);
        }

        public void run() {
            int delta;
            switch (AbsHListView.this.mTouchMode) {
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
                        int scrollX = AbsHListView.this.getScrollX();
                        int currX = scroller.getCurrX();
                        if (AbsHListView.this.overScrollBy(currX - scrollX, 0, scrollX, 0, 0, 0, AbsHListView.this.mOverflingDistance, 0, false)) {
                            boolean crossDown = scrollX <= 0 && currX > 0;
                            boolean crossUp = scrollX >= 0 && currX < 0;
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
                        AbsHListView.this.invalidate();
                        AbsHListView.this.postOnAnimation(this);
                        return;
                    }
                    endFling();
                    return;
                default:
                    endFling();
                    return;
            }
            if (AbsHListView.this.mDataChanged) {
                AbsHListView.this.layoutChildren();
            }
            if (AbsHListView.this.mItemCount == 0 || AbsHListView.this.getChildCount() == 0) {
                endFling();
                return;
            }
            boolean more = this.mListLoopScroller.computeScrollOffset();
            int x = this.mListLoopScroller.getCurr();
            int delta2 = this.mLastFlingX - x;
            if (delta2 > 0) {
                AbsHListView.this.mMotionPosition = AbsHListView.this.mFirstPosition;
                AbsHListView.this.mMotionViewOriginalLeft = AbsHListView.this.getChildAt(0).getLeft();
                delta = Math.min(((AbsHListView.this.getWidth() - AbsHListView.this.getPaddingRight()) - AbsHListView.this.getLeft()) - 1, delta2);
            } else {
                int offsetToLast = AbsHListView.this.getChildCount() - 1;
                AbsHListView.this.mMotionPosition = AbsHListView.this.mFirstPosition + offsetToLast;
                AbsHListView.this.mMotionViewOriginalLeft = AbsHListView.this.getChildAt(offsetToLast).getLeft();
                delta = Math.max(-(((AbsHListView.this.getWidth() - AbsHListView.this.getPaddingLeft()) - AbsHListView.this.getPaddingRight()) - 1), delta2);
            }
            View motionView = AbsHListView.this.getChildAt(AbsHListView.this.mMotionPosition - AbsHListView.this.mFirstPosition);
            int oldLeft = 0;
            if (motionView != null) {
                oldLeft = motionView.getLeft();
            }
            boolean atEdge = AbsHListView.this.trackMotionScroll(delta, delta);
            boolean atEnd = atEdge && delta != 0;
            if (atEnd) {
                if (motionView != null) {
                    boolean unused = AbsHListView.this.overScrollBy(-(delta - (motionView.getLeft() - oldLeft)), 0, AbsHListView.this.getScrollX(), 0, 0, 0, AbsHListView.this.mOverflingDistance, 0, false);
                }
                if (more) {
                    edgeReached(delta);
                }
            } else if (!more || atEnd) {
                endFling();
            } else {
                if (atEdge) {
                    AbsHListView.this.invalidate();
                }
                this.mLastFlingX = x;
                AbsHListView.this.postOnAnimation(this);
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
        private int mOffsetFromLeft;
        private int mScrollDuration;
        private int mTargetPos;

        PositionScroller() {
            this.mExtraScroll = ViewConfiguration.get(AbsHListView.this.getContext()).getScaledFadingEdgeLength();
        }

        /* access modifiers changed from: package-private */
        public void start(int position) {
            int viewTravelCount;
            stop();
            int firstPos = AbsHListView.this.mFirstPosition;
            int lastPos = (AbsHListView.this.getChildCount() + firstPos) - 1;
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
            AbsHListView.this.post(this);
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
            int firstPos = AbsHListView.this.mFirstPosition;
            int lastPos = (AbsHListView.this.getChildCount() + firstPos) - 1;
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
            AbsHListView.this.post(this);
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
            this.mOffsetFromLeft = offset;
            this.mBoundPos = -1;
            this.mLastSeenPos = -1;
            this.mMode = 5;
            int firstPos = AbsHListView.this.mFirstPosition;
            int childCount = AbsHListView.this.getChildCount();
            int lastPos = (firstPos + childCount) - 1;
            if (position < firstPos) {
                viewTravelCount = firstPos - position;
            } else if (position > lastPos) {
                viewTravelCount = position - lastPos;
            } else {
                AbsHListView.this.smoothScrollBy(AbsHListView.this.getChildAt(position - firstPos).getLeft() - offset);
                return;
            }
            float screenTravelCount = ((float) viewTravelCount) / ((float) childCount);
            this.mScrollDuration = screenTravelCount < 1.0f ? (int) (((float) duration) * screenTravelCount) : (int) (((float) duration) / screenTravelCount);
            this.mLastSeenPos = -1;
            AbsHListView.this.post(this);
        }

        /* access modifiers changed from: package-private */
        public void stop() {
            AbsHListView.this.removeCallbacks(this);
        }

        public void run() {
            int extraScroll;
            int extraScroll2;
            if (AbsHListView.this.mTouchMode == 4 || this.mLastSeenPos == -1) {
                int listWidth = AbsHListView.this.getWidth();
                int firstPos = AbsHListView.this.mFirstPosition;
                switch (this.mMode) {
                    case 1:
                        int lastViewIndex = AbsHListView.this.getChildCount() - 1;
                        int lastPos = firstPos + lastViewIndex;
                        if (lastViewIndex < 0) {
                            return;
                        }
                        if (lastPos == this.mLastSeenPos) {
                            AbsHListView.this.post(this);
                            return;
                        }
                        View lastView = AbsHListView.this.getChildAt(lastViewIndex);
                        int lastViewWidth = lastView.getWidth();
                        int lastViewPixelsShowing = listWidth - lastView.getLeft();
                        if (lastPos < AbsHListView.this.mItemCount - 1) {
                            extraScroll2 = this.mExtraScroll;
                        } else {
                            extraScroll2 = AbsHListView.this.mListPadding.bottom;
                        }
                        AbsHListView.this.smoothScrollBy((lastViewWidth - lastViewPixelsShowing) + extraScroll2);
                        this.mLastSeenPos = lastPos;
                        if (lastPos < this.mTargetPos) {
                            AbsHListView.this.post(this);
                            return;
                        }
                        return;
                    case 2:
                        if (firstPos == this.mLastSeenPos) {
                            AbsHListView.this.post(this);
                            return;
                        }
                        View firstView = AbsHListView.this.getChildAt(0);
                        if (firstView != null) {
                            int firstViewLeft = firstView.getLeft();
                            if (firstPos > 0) {
                                extraScroll = this.mExtraScroll;
                            } else {
                                extraScroll = AbsHListView.this.mListPadding.top;
                            }
                            AbsHListView.this.smoothScrollBy(firstViewLeft - extraScroll);
                            this.mLastSeenPos = firstPos;
                            if (firstPos > this.mTargetPos) {
                                AbsHListView.this.post(this);
                                return;
                            }
                            return;
                        }
                        return;
                    case 3:
                        int childCount = AbsHListView.this.getChildCount();
                        if (firstPos != this.mBoundPos && childCount > 1 && firstPos + childCount < AbsHListView.this.mItemCount) {
                            int nextPos = firstPos + 1;
                            if (nextPos == this.mLastSeenPos) {
                                AbsHListView.this.post(this);
                                return;
                            }
                            View nextView = AbsHListView.this.getChildAt(1);
                            int nextViewWidth = nextView.getWidth();
                            int nextViewLeft = nextView.getLeft();
                            int extraScroll3 = this.mExtraScroll;
                            if (nextPos < this.mBoundPos) {
                                AbsHListView.this.smoothScrollBy(Math.max(0, (nextViewWidth + nextViewLeft) - extraScroll3));
                                this.mLastSeenPos = nextPos;
                                AbsHListView.this.post(this);
                                return;
                            } else if (nextViewLeft > extraScroll3) {
                                AbsHListView.this.smoothScrollBy(nextViewLeft - extraScroll3);
                                return;
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    case 4:
                        int lastViewIndex2 = AbsHListView.this.getChildCount() - 2;
                        if (lastViewIndex2 >= 0) {
                            int lastPos2 = firstPos + lastViewIndex2;
                            if (lastPos2 == this.mLastSeenPos) {
                                AbsHListView.this.post(this);
                                return;
                            }
                            View lastView2 = AbsHListView.this.getChildAt(lastViewIndex2);
                            int lastViewWidth2 = lastView2.getWidth();
                            int lastViewLeft = lastView2.getLeft();
                            int lastViewPixelsShowing2 = lastViewWidth2 - lastViewLeft;
                            this.mLastSeenPos = lastPos2;
                            if (lastPos2 > this.mBoundPos) {
                                AbsHListView.this.smoothScrollBy(-(lastViewPixelsShowing2 - this.mExtraScroll));
                                AbsHListView.this.post(this);
                                return;
                            }
                            int right = listWidth - this.mExtraScroll;
                            int lastViewRight = lastViewLeft + lastViewWidth2;
                            if (right > lastViewRight) {
                                AbsHListView.this.smoothScrollBy(-(right - lastViewRight));
                                return;
                            }
                            return;
                        }
                        return;
                    case 5:
                        if (this.mLastSeenPos == firstPos) {
                            AbsHListView.this.post(this);
                            return;
                        }
                        this.mLastSeenPos = firstPos;
                        int childCount2 = AbsHListView.this.getChildCount();
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
                            AbsHListView.this.smoothScrollBy((int) (((float) (-AbsHListView.this.getWidth())) * modifier));
                            AbsHListView.this.post(this);
                            return;
                        } else if (position > lastPos3) {
                            AbsHListView.this.smoothScrollBy((int) (((float) AbsHListView.this.getWidth()) * modifier));
                            AbsHListView.this.post(this);
                            return;
                        } else {
                            AbsHListView.this.smoothScrollBy(AbsHListView.this.getChildAt(position - firstPos).getLeft() - this.mOffsetFromLeft);
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    }
}
