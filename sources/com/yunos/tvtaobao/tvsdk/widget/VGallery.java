package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import com.yunos.tvtaobao.tvsdk.widget.AbsGallery;

public class VGallery extends AbsGallery {
    private static final String TAG = "VGallery";
    private static final boolean localLOGV = false;
    private int mBottomMost;
    private FlingRunnable mFlingRunnable = new FlingRunnable();
    /* access modifiers changed from: private */
    public boolean mIsRtl = true;
    private int mTopMost;

    public VGallery(Context context) {
        super(context);
        init(context);
    }

    public VGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mGravity = 1;
    }

    /* access modifiers changed from: protected */
    public int computeVerticalScrollExtent() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public int computeVerticalScrollOffset() {
        return this.mSelectedPosition;
    }

    /* access modifiers changed from: protected */
    public int computeVerticalScrollRange() {
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
    public int getChildWidth(View child) {
        return child.getMeasuredWidth();
    }

    /* access modifiers changed from: package-private */
    public void trackMotionScroll(int deltaY) {
        boolean toUp;
        if (getChildCount() != 0) {
            if (deltaY < 0) {
                toUp = true;
            } else {
                toUp = false;
            }
            int limitedDeltaY = getLimitedMotionScrollAmount(toUp, deltaY);
            if (limitedDeltaY != deltaY) {
                this.mFlingRunnable.endFling(false);
            }
            offsetChildrenTopAndBottom(limitedDeltaY);
            detachOffScreenChildren(toUp);
            if (toUp) {
                fillToGalleryDown();
            } else {
                fillToGalleryUp();
            }
            if (!this.mByPosition) {
                this.mRecycler.clear();
            }
            View selChild = this.mSelectedChild;
            if (selChild != null) {
                this.mSelectedCenterOffset = (selChild.getTop() + (selChild.getHeight() / 2)) - (getHeight() / 2);
            }
            onScrollChanged(0, 0, 0, 0);
            invalidate();
        }
    }

    /* access modifiers changed from: package-private */
    public int getLimitedMotionScrollAmount(boolean motionToUp, int deltaY) {
        int extremeItemPosition;
        if (motionToUp != this.mIsRtl) {
            extremeItemPosition = this.mItemCount - 1;
        } else {
            extremeItemPosition = 0;
        }
        View extremeChild = getChildAt(extremeItemPosition - this.mFirstPosition);
        if (extremeChild == null) {
            return deltaY;
        }
        int extremeChildCenter = getCenterOfView(extremeChild);
        int galleryCenter = getCenterOfGallery();
        if (motionToUp) {
            if (extremeChildCenter <= galleryCenter) {
                return 0;
            }
        } else if (extremeChildCenter >= galleryCenter) {
            return 0;
        }
        int centerDifference = galleryCenter - extremeChildCenter;
        if (motionToUp) {
            return Math.max(centerDifference, deltaY);
        }
        return Math.min(centerDifference, deltaY);
    }

    private int getCenterOfGallery() {
        return (((getHeight() - getPaddingTop()) - getPaddingBottom()) / 2) + getPaddingBottom();
    }

    private static int getCenterOfView(View view) {
        return view.getTop() + (view.getHeight() / 2);
    }

    private void detachOffScreenChildren(boolean toUp) {
        int n;
        int numChildren = getChildCount();
        int firstPosition = this.mFirstPosition;
        int start = 0;
        int count = 0;
        if (toUp) {
            int galleryTop = getPaddingTop();
            for (int i = 0; i < numChildren; i++) {
                if (this.mIsRtl) {
                    n = (numChildren - 1) - i;
                } else {
                    n = i;
                }
                View child = getChildAt(n);
                if (child.getBottom() >= galleryTop) {
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
            int galleryBottom = getHeight() - getPaddingBottom();
            for (int i2 = numChildren - 1; i2 >= 0; i2--) {
                int n2 = this.mIsRtl ? (numChildren - 1) - i2 : i2;
                View child2 = getChildAt(n2);
                if (child2.getTop() <= galleryBottom) {
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
        if (toUp != this.mIsRtl) {
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
            if (selView.getTop() > galleryCenter || selView.getBottom() < galleryCenter) {
                int closestEdgeDistance = Integer.MAX_VALUE;
                int newSelectedChildIndex = 0;
                int i = getChildCount() - 1;
                while (true) {
                    if (i < 0) {
                        break;
                    }
                    View child = getChildAt(i);
                    if (child.getTop() <= galleryCenter && child.getBottom() >= galleryCenter) {
                        newSelectedChildIndex = i;
                        break;
                    }
                    int childClosestEdgeDistance = Math.min(Math.abs(child.getTop() - galleryCenter), Math.abs(child.getBottom() - galleryCenter));
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
        int childrenTop = this.mSpinnerPadding.top;
        int childrenHeight = ((getBottom() - getTop()) - this.mSpinnerPadding.top) - this.mSpinnerPadding.bottom;
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
        this.mBottomMost = 0;
        this.mTopMost = 0;
        this.mFirstPosition = this.mSelectedPosition;
        View sel = makeAndAddView(this.mSelectedPosition, 0, 0, true);
        sel.offsetTopAndBottom(((childrenHeight / 2) + childrenTop) - (sel.getHeight() / 2));
        if (sel != null) {
            positionSelector(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        }
        fillToGalleryDown();
        fillToGalleryUp();
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

    private void fillToGalleryUp() {
        if (this.mIsRtl) {
            fillToGalleryUpRtl();
        } else {
            fillToGalleryUpLtr();
        }
    }

    private void fillToGalleryUpRtl() {
        int curPosition;
        int curBottomEdge;
        int itemSpacing = this.mSpacing;
        int galleryTop = getPaddingTop();
        int numChildren = getChildCount();
        int i = this.mItemCount;
        View prevIterationView = getChildAt(numChildren - 1);
        if (prevIterationView != null) {
            curPosition = this.mFirstPosition + numChildren;
            curBottomEdge = prevIterationView.getTop() - itemSpacing;
        } else {
            curPosition = this.mItemCount - 1;
            this.mFirstPosition = curPosition;
            curBottomEdge = (getBottom() - getTop()) - getPaddingBottom();
            this.mShouldStopFling = true;
        }
        while (curBottomEdge > galleryTop && curPosition < this.mItemCount) {
            curBottomEdge = makeAndAddView(curPosition, curPosition - this.mSelectedPosition, curBottomEdge, false).getTop() - itemSpacing;
            curPosition++;
        }
    }

    private void fillToGalleryUpLtr() {
        int curPosition;
        int curBottomEdge;
        int itemSpacing = this.mSpacing;
        int galleryTop = getPaddingTop();
        View prevIterationView = getChildAt(0);
        if (prevIterationView != null) {
            curPosition = this.mFirstPosition - 1;
            curBottomEdge = prevIterationView.getTop() - itemSpacing;
        } else {
            curPosition = 0;
            curBottomEdge = (getBottom() - getTop()) - getPaddingBottom();
            this.mShouldStopFling = true;
        }
        while (curBottomEdge > galleryTop && curPosition >= 0) {
            View prevIterationView2 = makeAndAddView(curPosition, curPosition - this.mSelectedPosition, curBottomEdge, false);
            this.mFirstPosition = curPosition;
            curBottomEdge = prevIterationView2.getTop() - itemSpacing;
            curPosition--;
        }
    }

    private void fillToGalleryDown() {
        if (this.mIsRtl) {
            fillToGalleryDownRtl();
        } else {
            fillToGalleryDownLtr();
        }
    }

    private void fillToGalleryDownRtl() {
        int curTopEdge;
        int curPosition;
        int itemSpacing = this.mSpacing;
        int galleryBottom = (getBottom() - getTop()) - getPaddingBottom();
        View prevIterationView = getChildAt(0);
        if (prevIterationView != null) {
            curPosition = this.mFirstPosition - 1;
            curTopEdge = prevIterationView.getBottom() + itemSpacing;
        } else {
            curPosition = 0;
            curTopEdge = getPaddingTop();
            this.mShouldStopFling = true;
        }
        while (curTopEdge < galleryBottom && curPosition >= 0) {
            View prevIterationView2 = makeAndAddView(curPosition, curPosition - this.mSelectedPosition, curTopEdge, true);
            this.mFirstPosition = curPosition;
            curTopEdge = prevIterationView2.getBottom() + itemSpacing;
            curPosition--;
        }
    }

    private void fillToGalleryDownLtr() {
        int curTopEdge;
        int curPosition;
        int itemSpacing = this.mSpacing;
        int galleryBottom = (getBottom() - getTop()) - getPaddingBottom();
        int numChildren = getChildCount();
        int numItems = this.mItemCount;
        View prevIterationView = getChildAt(numChildren - 1);
        if (prevIterationView != null) {
            curPosition = this.mFirstPosition + numChildren;
            curTopEdge = prevIterationView.getBottom() + itemSpacing;
        } else {
            curPosition = this.mItemCount - 1;
            this.mFirstPosition = curPosition;
            curTopEdge = getPaddingTop();
            this.mShouldStopFling = true;
        }
        while (curTopEdge < galleryBottom && curPosition < numItems) {
            curTopEdge = makeAndAddView(curPosition, curPosition - this.mSelectedPosition, curTopEdge, true).getBottom() + itemSpacing;
            curPosition++;
        }
    }

    private View makeAndAddView(int position, int offset, int y, boolean fromTop) {
        View child;
        View child2;
        if (this.mDataChanged || (child2 = this.mRecycler.get(position)) == null) {
            if (this.mByPosition) {
                child = this.mRecycler.get(position);
            } else {
                child = null;
            }
            View child3 = this.mAdapter.getView(position, child, this);
            setUpChild(child3, offset, y, fromTop);
            return child3;
        }
        int childTop = child2.getTop();
        this.mBottomMost = Math.max(this.mBottomMost, child2.getMeasuredHeight() + childTop);
        this.mTopMost = Math.min(this.mTopMost, childTop);
        if (this.mByPosition) {
            child2 = this.mAdapter.getView(position, child2, this);
        }
        setUpChild(child2, offset, y, fromTop);
        return child2;
    }

    private void setUpChild(View child, int offset, int y, boolean fromTop) {
        int childTop;
        int childBottom;
        AbsGallery.LayoutParams lp = (AbsGallery.LayoutParams) child.getLayoutParams();
        if (lp == null) {
            lp = (AbsGallery.LayoutParams) generateDefaultLayoutParams();
        }
        addViewInLayout(child, fromTop != this.mIsRtl ? -1 : 0, lp);
        child.setSelected(offset == 0);
        child.measure(getChildMeasureSpec(this.mWidthMeasureSpec, this.mSpinnerPadding.left + this.mSpinnerPadding.right, lp.width), getChildMeasureSpec(this.mHeightMeasureSpec, this.mSpinnerPadding.top + this.mSpinnerPadding.bottom, lp.height));
        int childLeft = calculateLeft(child, true);
        int childRight = childLeft + child.getMeasuredWidth();
        int height = child.getMeasuredHeight();
        if (fromTop) {
            childTop = y;
            childBottom = childTop + height;
        } else {
            childTop = y - height;
            childBottom = y;
        }
        child.layout(childLeft, childTop, childRight, childBottom);
    }

    private int calculateLeft(View child, boolean duringLayout) {
        int myWidth = duringLayout ? getMeasuredWidth() : getWidth();
        int childWidth = duringLayout ? child.getMeasuredWidth() : child.getWidth();
        switch (this.mGravity) {
            case 1:
                return this.mSpinnerPadding.left + ((((myWidth - this.mSpinnerPadding.right) - this.mSpinnerPadding.left) - childWidth) / 2);
            case 3:
                return this.mSpinnerPadding.left;
            case 5:
                return (myWidth - this.mSpinnerPadding.right) - childWidth;
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
        trackMotionScroll(((int) distanceY) * -1);
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
            case 19:
                if (movePrevious()) {
                    playSoundEffect(2);
                    return true;
                }
                break;
            case 20:
                if (moveNext()) {
                    playSoundEffect(4);
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
                            VGallery.this.dispatchUnpress();
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

    public void stopScroll(boolean scrollIntoSlots) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.stop(scrollIntoSlots);
        }
    }

    public void setFlingScrollFrameCount(int frameCount) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setFrameCount(frameCount);
        }
    }

    public void setFlingScrollPostDelay(int delay) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setPostDelay(delay);
        }
    }

    public void setFlingScrollMaxStep(float maxStep) {
        if (this.mFlingRunnable != null) {
            this.mFlingRunnable.setMaxStep(maxStep);
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

    protected class FlingRunnable implements Runnable {
        private float mDefatultScrollStep = 5.0f;
        private int mFrameCount;
        private int mLastFlingY;
        private ListLoopScroller mListLoopScroller;
        private int mPostDelay = 0;
        private Scroller mScroller;

        public boolean isFinished() {
            if (!this.mScroller.isFinished() || !this.mListLoopScroller.isFinished()) {
                return false;
            }
            return true;
        }

        public FlingRunnable() {
            this.mScroller = new Scroller(VGallery.this.getContext(), new DecelerateInterpolator());
            this.mListLoopScroller = new ListLoopScroller();
        }

        private void startCommon() {
            VGallery.this.removeCallbacks(this);
        }

        public void startUsingVelocity(int initialVelocity) {
            int initialY;
            if (initialVelocity != 0) {
                startCommon();
                if (initialVelocity < 0) {
                    initialY = Integer.MAX_VALUE;
                } else {
                    initialY = 0;
                }
                this.mLastFlingY = initialY;
                this.mScroller.fling(0, initialY, 0, initialVelocity, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
                VGallery.this.post(this);
            }
        }

        public void startUsingDistance(int distance) {
            int frameCount;
            if (distance != 0) {
                this.mLastFlingY = 0;
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
                if (this.mListLoopScroller.isFinished()) {
                    startCommon();
                    this.mListLoopScroller.startScroll(0, -distance, frameCount);
                    VGallery.this.post(this);
                } else {
                    this.mListLoopScroller.startScroll(0, -distance, frameCount);
                }
                VGallery.this.reportScrollStateChange(2);
            }
        }

        public void stop(boolean scrollIntoSlots) {
            VGallery.this.removeCallbacks(this);
            endFling(scrollIntoSlots);
        }

        public void setFrameCount(int frameCount) {
            this.mFrameCount = frameCount;
        }

        public void setMaxStep(float maxStep) {
            this.mListLoopScroller.setMaxStep(maxStep);
        }

        public void setPostDelay(int delay) {
            this.mPostDelay = delay;
        }

        /* access modifiers changed from: private */
        public void endFling(boolean scrollIntoSlots) {
            VGallery.this.reportScrollStateChange(0);
            this.mScroller.forceFinished(true);
            this.mListLoopScroller.finish();
            if (scrollIntoSlots) {
                VGallery.this.scrollIntoSlots();
            }
        }

        public void run() {
            int childCount;
            int delta;
            if (VGallery.this.mItemCount == 0) {
                endFling(true);
                return;
            }
            VGallery.this.mShouldStopFling = false;
            boolean more = this.mListLoopScroller.computeScrollOffset();
            int y = this.mListLoopScroller.getCurr();
            int delta2 = this.mLastFlingY - y;
            if (delta2 > 0) {
                VGallery.this.mDownTouchPosition = VGallery.this.mIsRtl ? (VGallery.this.mFirstPosition + VGallery.this.getChildCount()) - 1 : VGallery.this.mFirstPosition;
                delta = Math.min(((VGallery.this.getHeight() - VGallery.this.getPaddingTop()) - VGallery.this.getPaddingBottom()) - 1, delta2);
            } else {
                int childCount2 = VGallery.this.getChildCount() - 1;
                VGallery vGallery = VGallery.this;
                if (VGallery.this.mIsRtl) {
                    childCount = VGallery.this.mFirstPosition;
                } else {
                    childCount = (VGallery.this.mFirstPosition + VGallery.this.getChildCount()) - 1;
                }
                vGallery.mDownTouchPosition = childCount;
                delta = Math.max(-(((VGallery.this.getHeight() - VGallery.this.getPaddingTop()) - VGallery.this.getPaddingBottom()) - 1), delta2);
            }
            VGallery.this.trackMotionScroll(delta);
            if (!more || VGallery.this.mShouldStopFling) {
                endFling(true);
                return;
            }
            this.mLastFlingY = y;
            VGallery.this.postDelayed(this, (long) this.mPostDelay);
        }
    }

    public FlingRunnable getFlingRunnable() {
        return this.mFlingRunnable;
    }
}
