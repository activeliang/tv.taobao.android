package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import com.yunos.tvtaobao.tvsdk.widget.AbsGallery;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;

public class Gallery extends AbsGallery {
    private static final String TAG = "Gallery";
    private static final boolean localLOGV = false;
    private FlingRunnable mFlingRunnable = new FlingRunnable();
    protected boolean mIsRtl = true;
    protected int mLeftMost;
    protected int mRightMost;

    public Gallery(Context context) {
        super(context);
        init(context);
    }

    public Gallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Gallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mGravity = 16;
    }

    /* access modifiers changed from: protected */
    public int computeHorizontalScrollExtent() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public int computeHorizontalScrollOffset() {
        return this.mSelectedPosition;
    }

    /* access modifiers changed from: protected */
    public int computeHorizontalScrollRange() {
        return this.mItemCount;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInLayout = true;
        layout(0, false);
        this.mInLayout = false;
    }

    /* access modifiers changed from: package-private */
    public int getChildHeight(View child) {
        return child.getMeasuredHeight();
    }

    /* access modifiers changed from: package-private */
    public void trackMotionScroll(int deltaX) {
        boolean toLeft;
        if (getChildCount() != 0) {
            if (deltaX < 0) {
                toLeft = true;
            } else {
                toLeft = false;
            }
            int limitedDeltaX = getLimitedMotionScrollAmount(toLeft, deltaX);
            if (limitedDeltaX != deltaX) {
                this.mFlingRunnable.endFling(false);
                onFinishedMovement();
            }
            offsetChildrenLeftAndRight(limitedDeltaX);
            detachOffScreenChildren(toLeft);
            if (toLeft) {
                fillToGalleryRight();
            } else {
                fillToGalleryLeft();
            }
            if (!this.mByPosition) {
                this.mRecycler.clear();
            }
            View selChild = this.mSelectedChild;
            if (selChild != null) {
                this.mSelectedCenterOffset = (selChild.getLeft() + (selChild.getWidth() / 2)) - (getWidth() / 2);
            }
            onScrollChanged(0, 0, 0, 0);
            invalidate();
        }
    }

    /* access modifiers changed from: package-private */
    public int getLimitedMotionScrollAmount(boolean motionToLeft, int deltaX) {
        int extremeItemPosition;
        if (motionToLeft != this.mIsRtl) {
            extremeItemPosition = this.mItemCount - 1;
        } else {
            extremeItemPosition = 0;
        }
        View extremeChild = getChildAt(extremeItemPosition - this.mFirstPosition);
        if (extremeChild == null) {
            return deltaX;
        }
        int extremeChildCenter = getCenterOfView(extremeChild);
        int galleryCenter = getCenterOfGallery();
        if (motionToLeft) {
            if (extremeChildCenter <= galleryCenter) {
                return 0;
            }
        } else if (extremeChildCenter >= galleryCenter) {
            return 0;
        }
        int centerDifference = galleryCenter - extremeChildCenter;
        if (motionToLeft) {
            return Math.max(centerDifference, deltaX);
        }
        return Math.min(centerDifference, deltaX);
    }

    private int getCenterOfGallery() {
        return (((getWidth() - getPaddingLeft()) - getPaddingRight()) / 2) + getPaddingRight();
    }

    private static int getCenterOfView(View view) {
        return view.getLeft() + (view.getWidth() / 2);
    }

    private void detachOffScreenChildren(boolean toLeft) {
        int n;
        int numChildren = getChildCount();
        int firstPosition = this.mFirstPosition;
        int start = 0;
        int count = 0;
        if (toLeft) {
            int galleryLeft = getPaddingLeft();
            for (int i = 0; i < numChildren; i++) {
                if (this.mIsRtl) {
                    n = (numChildren - 1) - i;
                } else {
                    n = i;
                }
                View child = getChildAt(n);
                if (child.getRight() >= galleryLeft) {
                    break;
                }
                start = n;
                count++;
                this.mRecycler.put(firstPosition + n, child);
            }
            if (!this.mIsRtl) {
                start = 0;
            }
        } else {
            int galleryRight = getWidth() - getPaddingRight();
            for (int i2 = numChildren - 1; i2 >= 0; i2--) {
                int n2 = this.mIsRtl ? (numChildren - 1) - i2 : i2;
                View child2 = getChildAt(n2);
                if (child2.getLeft() <= galleryRight) {
                    break;
                }
                start = n2;
                count++;
                this.mRecycler.put(firstPosition + n2, child2);
            }
            if (this.mIsRtl) {
                start = 0;
            }
        }
        detachViewsFromParent(start, count);
        if (toLeft != this.mIsRtl) {
            this.mFirstPosition += count;
        }
    }

    /* access modifiers changed from: private */
    public void scrollIntoSlots() {
        if (getChildCount() != 0 && this.mSelectedChild != null) {
            int scrollAmount = getCenterOfGallery() - getCenterOfView(this.mSelectedChild);
            if (scrollAmount != 0) {
                this.mFlingRunnable.startUsingDistance(scrollAmount);
            } else {
                onFinishedMovement();
            }
        }
    }

    private void setSelectionToCenterChild() {
        View selView = this.mSelectedChild;
        if (this.mSelectedChild != null) {
            int galleryCenter = getCenterOfGallery();
            if (selView.getLeft() > galleryCenter || selView.getRight() < galleryCenter) {
                int closestEdgeDistance = Integer.MAX_VALUE;
                int newSelectedChildIndex = 0;
                int i = getChildCount() - 1;
                while (true) {
                    if (i < 0) {
                        break;
                    }
                    View child = getChildAt(i);
                    if (child.getLeft() <= galleryCenter && child.getRight() >= galleryCenter) {
                        newSelectedChildIndex = i;
                        break;
                    }
                    int childClosestEdgeDistance = Math.min(Math.abs(child.getLeft() - galleryCenter), Math.abs(child.getRight() - galleryCenter));
                    if (childClosestEdgeDistance < closestEdgeDistance) {
                        closestEdgeDistance = childClosestEdgeDistance;
                        newSelectedChildIndex = i;
                    }
                    i--;
                }
                int newPos = this.mFirstPosition + newSelectedChildIndex;
                if (newPos != this.mSelectedPosition) {
                    setSelectedPositionInt(newPos);
                    setNextSelectedPositionInt(newPos);
                    checkSelectionChanged();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void layout(int delta, boolean animate) {
        this.mIsRtl = false;
        int childrenLeft = this.mSpinnerPadding.left;
        int childrenWidth = ((getRight() - getLeft()) - this.mSpinnerPadding.left) - this.mSpinnerPadding.right;
        if (this.mDataChanged) {
            handleDataChanged();
        }
        if (this.mItemCount == 0) {
            resetList();
            return;
        }
        if (this.mNextSelectedPosition >= 0) {
            setSelectedPositionInt(this.mNextSelectedPosition);
        }
        recycleAllViews();
        detachAllViewsFromParent();
        this.mRightMost = 0;
        this.mLeftMost = 0;
        this.mFirstPosition = this.mSelectedPosition;
        View sel = makeAndAddView(this.mSelectedPosition, 0, 0, true);
        sel.offsetLeftAndRight((((childrenWidth / 2) + childrenLeft) - (sel.getWidth() / 2)) + this.mSelectedCenterOffset);
        if (sel != null) {
            positionSelector(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        }
        fillToGalleryRight();
        fillToGalleryLeft();
        if (!this.mByPosition) {
            this.mRecycler.clear();
        }
        invalidate();
        checkSelectionChanged();
        this.mDataChanged = false;
        this.mNeedSync = false;
        setNextSelectedPositionInt(this.mSelectedPosition);
        updateSelectedItemMetadata();
    }

    /* access modifiers changed from: protected */
    public void fillToGalleryLeft() {
        if (this.mIsRtl) {
            fillToGalleryLeftRtl();
        } else {
            fillToGalleryLeftLtr();
        }
    }

    private void fillToGalleryLeftRtl() {
        int curRightEdge;
        int curPosition;
        int itemSpacing = this.mSpacing;
        int galleryLeft = getPaddingLeft();
        int numChildren = getChildCount();
        int i = this.mItemCount;
        View prevIterationView = getChildAt(numChildren - 1);
        if (prevIterationView != null) {
            curPosition = this.mFirstPosition + numChildren;
            curRightEdge = prevIterationView.getLeft() - itemSpacing;
        } else {
            curPosition = this.mItemCount - 1;
            this.mFirstPosition = curPosition;
            curRightEdge = (getRight() - getLeft()) - getPaddingRight();
            this.mShouldStopFling = true;
        }
        while (curRightEdge > galleryLeft && curPosition < this.mItemCount) {
            curRightEdge = makeAndAddView(curPosition, curPosition - this.mSelectedPosition, curRightEdge, false).getLeft() - itemSpacing;
            curPosition++;
        }
    }

    private void fillToGalleryLeftLtr() {
        int curRightEdge;
        int curPosition;
        int itemSpacing = this.mSpacing;
        int galleryLeft = getPaddingLeft();
        View prevIterationView = getChildAt(0);
        if (prevIterationView != null) {
            curPosition = this.mFirstPosition - 1;
            curRightEdge = prevIterationView.getLeft() - itemSpacing;
        } else {
            curPosition = 0;
            curRightEdge = (getRight() - getLeft()) - getPaddingRight();
            this.mShouldStopFling = true;
        }
        while (curRightEdge > galleryLeft && curPosition >= 0) {
            View prevIterationView2 = makeAndAddView(curPosition, curPosition - this.mSelectedPosition, curRightEdge, false);
            this.mFirstPosition = curPosition;
            curRightEdge = prevIterationView2.getLeft() - itemSpacing;
            curPosition--;
        }
    }

    /* access modifiers changed from: protected */
    public void fillToGalleryRight() {
        if (this.mIsRtl) {
            fillToGalleryRightRtl();
        } else {
            fillToGalleryRightLtr();
        }
    }

    private void fillToGalleryRightRtl() {
        int curPosition;
        int curLeftEdge;
        int itemSpacing = this.mSpacing;
        int galleryRight = (getRight() - getLeft()) - getPaddingRight();
        View prevIterationView = getChildAt(0);
        if (prevIterationView != null) {
            curPosition = this.mFirstPosition - 1;
            curLeftEdge = prevIterationView.getRight() + itemSpacing;
        } else {
            curPosition = 0;
            curLeftEdge = getPaddingLeft();
            this.mShouldStopFling = true;
        }
        while (curLeftEdge < galleryRight && curPosition >= 0) {
            View prevIterationView2 = makeAndAddView(curPosition, curPosition - this.mSelectedPosition, curLeftEdge, true);
            this.mFirstPosition = curPosition;
            curLeftEdge = prevIterationView2.getRight() + itemSpacing;
            curPosition--;
        }
    }

    private void fillToGalleryRightLtr() {
        int curPosition;
        int curLeftEdge;
        int itemSpacing = this.mSpacing;
        int galleryRight = (getRight() - getLeft()) - getPaddingRight();
        int numChildren = getChildCount();
        int numItems = this.mItemCount;
        View prevIterationView = getChildAt(numChildren - 1);
        if (prevIterationView != null) {
            curPosition = this.mFirstPosition + numChildren;
            curLeftEdge = prevIterationView.getRight() + itemSpacing;
        } else {
            curPosition = this.mItemCount - 1;
            this.mFirstPosition = curPosition;
            curLeftEdge = getPaddingLeft();
            this.mShouldStopFling = true;
        }
        while (curLeftEdge < galleryRight && curPosition < numItems) {
            curLeftEdge = makeAndAddView(curPosition, curPosition - this.mSelectedPosition, curLeftEdge, true).getRight() + itemSpacing;
            curPosition++;
        }
    }

    /* access modifiers changed from: protected */
    public View makeAndAddView(int position, int offset, int x, boolean fromLeft) {
        View child;
        View child2;
        if (this.mDataChanged || (child2 = this.mRecycler.get(position)) == null) {
            if (this.mByPosition) {
                child = this.mRecycler.get(position);
            } else {
                child = null;
            }
            View child3 = this.mAdapter.getView(position, child, this);
            setUpChild(child3, offset, x, fromLeft);
            return child3;
        }
        int childLeft = child2.getLeft();
        this.mRightMost = Math.max(this.mRightMost, child2.getMeasuredWidth() + childLeft);
        this.mLeftMost = Math.min(this.mLeftMost, childLeft);
        if (this.mByPosition) {
            child2 = this.mAdapter.getView(position, child2, this);
        }
        setUpChild(child2, offset, x, fromLeft);
        return child2;
    }

    private void setUpChild(View child, int offset, int x, boolean fromLeft) {
        int childLeft;
        int childRight;
        AbsGallery.LayoutParams lp = (AbsGallery.LayoutParams) child.getLayoutParams();
        if (lp == null) {
            lp = (AbsGallery.LayoutParams) generateDefaultLayoutParams();
        }
        addViewInLayout(child, fromLeft != this.mIsRtl ? -1 : 0, lp);
        child.setSelected(offset == 0);
        child.measure(getChildMeasureSpec(this.mWidthMeasureSpec, this.mSpinnerPadding.left + this.mSpinnerPadding.right, lp.width), getChildMeasureSpec(this.mHeightMeasureSpec, this.mSpinnerPadding.top + this.mSpinnerPadding.bottom, lp.height));
        int childTop = calculateTop(child, true);
        int childBottom = childTop + child.getMeasuredHeight();
        int width = child.getMeasuredWidth();
        if (fromLeft) {
            childLeft = x;
            childRight = childLeft + width;
        } else {
            childLeft = x - width;
            childRight = x;
        }
        child.layout(childLeft, childTop, childRight, childBottom);
    }

    private int calculateTop(View child, boolean duringLayout) {
        int myHeight = duringLayout ? getMeasuredHeight() : getHeight();
        int childHeight = duringLayout ? child.getMeasuredHeight() : child.getHeight();
        switch (this.mGravity) {
            case 16:
                return this.mSpinnerPadding.top + ((((myHeight - this.mSpinnerPadding.bottom) - this.mSpinnerPadding.top) - childHeight) / 2);
            case 48:
                return this.mSpinnerPadding.top;
            case 80:
                return (myHeight - this.mSpinnerPadding.bottom) - childHeight;
            default:
                return 0;
        }
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        super.onFling(e1, e2, velocityX, velocityY);
        this.mFlingRunnable.startUsingVelocity((int) (-velocityX));
        return true;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        super.onScroll(e1, e2, distanceX, distanceY);
        trackMotionScroll(((int) distanceX) * -1);
        return true;
    }

    public boolean onDown(MotionEvent e) {
        super.onDown(e);
        this.mFlingRunnable.stop(false);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onUp() {
        super.onUp();
        if (this.mFlingRunnable.isFinished()) {
            scrollIntoSlots();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 21:
                if (movePrevious()) {
                    playSoundEffect(1);
                    return true;
                }
                break;
            case 22:
                if (moveNext()) {
                    playSoundEffect(3);
                    return true;
                }
                break;
            case 23:
            case 66:
                this.mReceivedInvokeKeyDown = true;
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 23:
            case 66:
                if (this.mReceivedInvokeKeyDown && this.mItemCount > 0) {
                    dispatchPress(this.mSelectedChild);
                    postDelayed(new Runnable() {
                        public void run() {
                            Gallery.this.dispatchUnpress();
                        }
                    }, (long) ViewConfiguration.getPressedStateDuration());
                    performItemClick(getChildAt(this.mSelectedPosition - this.mFirstPosition), this.mSelectedPosition, this.mAdapter.getItemId(this.mSelectedPosition));
                }
                this.mReceivedInvokeKeyDown = false;
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public void smoothScrollBy(int distance) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.startUsingDistance(distance);
        }
    }

    public void setFlingScrollFrameCount(int frameCount) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setFrameCount(frameCount);
        }
    }

    public void setFlingScrollMaxStep(float maxStep) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setMaxStep(maxStep);
        }
    }

    public int getLeftScrollDistance() {
        if (this.mFlingRunnable != null) {
            return this.mFlingRunnable.getLeftScrollDistance();
        }
        return 0;
    }

    public void setFlingScrollSlowDownRatio(float ratio) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setSlowDownRatio(ratio);
        }
    }

    /* access modifiers changed from: protected */
    public boolean scrollToChild(int childPosition) {
        View child = getChildAt(childPosition);
        if (child == null) {
            return false;
        }
        this.mFlingRunnable.startUsingDistance(getCenterOfGallery() - getCenterOfView(child));
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isFling() {
        return !this.mFlingRunnable.isFinished();
    }

    private class FlingRunnable implements Runnable {
        private float mDefatultScrollStep = 5.0f;
        private int mFrameCount;
        private int mLastFlingX;
        private ListLoopScroller mListLoopScroller;
        private Scroller mScroller;

        public FlingRunnable() {
            this.mScroller = new Scroller(Gallery.this.getContext(), new AccelerateDecelerateFrameInterpolator());
            this.mListLoopScroller = new ListLoopScroller();
        }

        private void startCommon() {
            Gallery.this.removeCallbacks(this);
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
        public void setSlowDownRatio(float ratio) {
            this.mListLoopScroller.setSlowDownRatio(ratio);
        }

        public boolean isFinished() {
            if (!this.mScroller.isFinished() || !this.mListLoopScroller.isFinished()) {
                return false;
            }
            return true;
        }

        public void startUsingVelocity(int initialVelocity) {
            int initialX;
            if (initialVelocity != 0) {
                startCommon();
                if (initialVelocity < 0) {
                    initialX = Integer.MAX_VALUE;
                } else {
                    initialX = 0;
                }
                this.mLastFlingX = initialX;
                this.mScroller.fling(initialX, 0, initialVelocity, 0, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
                Gallery.this.post(this);
            }
        }

        public void startUsingDistance(int distance) {
            int frameCount;
            if (distance != 0) {
                this.mLastFlingX = 0;
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
                    startCommon();
                    this.mListLoopScroller.startScroll(0, -distance, frameCount);
                    Gallery.this.post(this);
                } else {
                    this.mListLoopScroller.startScroll(0, -distance, frameCount);
                }
                Gallery.this.reportScrollStateChange(2);
            }
        }

        public void stop(boolean scrollIntoSlots) {
            Gallery.this.removeCallbacks(this);
            endFling(scrollIntoSlots);
        }

        /* access modifiers changed from: private */
        public void endFling(boolean scrollIntoSlots) {
            Gallery.this.reportScrollStateChange(0);
            this.mScroller.forceFinished(true);
            this.mListLoopScroller.finish();
            if (scrollIntoSlots) {
                Gallery.this.scrollIntoSlots();
            }
        }

        public void run() {
            int childCount;
            int delta;
            if (Gallery.this.mItemCount == 0) {
                endFling(true);
                return;
            }
            Gallery.this.mShouldStopFling = false;
            boolean more = this.mListLoopScroller.computeScrollOffset();
            int x = this.mListLoopScroller.getCurr();
            int delta2 = this.mLastFlingX - x;
            if (delta2 > 0) {
                Gallery.this.mDownTouchPosition = Gallery.this.mIsRtl ? (Gallery.this.mFirstPosition + Gallery.this.getChildCount()) - 1 : Gallery.this.mFirstPosition;
                delta = Math.min(((Gallery.this.getWidth() - Gallery.this.getPaddingLeft()) - Gallery.this.getPaddingRight()) - 1, delta2);
            } else {
                int childCount2 = Gallery.this.getChildCount() - 1;
                Gallery gallery = Gallery.this;
                if (Gallery.this.mIsRtl) {
                    childCount = Gallery.this.mFirstPosition;
                } else {
                    childCount = (Gallery.this.mFirstPosition + Gallery.this.getChildCount()) - 1;
                }
                gallery.mDownTouchPosition = childCount;
                delta = Math.max(-(((Gallery.this.getWidth() - Gallery.this.getPaddingRight()) - Gallery.this.getPaddingLeft()) - 1), delta2);
            }
            Gallery.this.trackMotionScroll(delta);
            if (!more || Gallery.this.mShouldStopFling) {
                endFling(true);
                return;
            }
            this.mLastFlingX = x;
            Gallery.this.post(this);
        }
    }
}
