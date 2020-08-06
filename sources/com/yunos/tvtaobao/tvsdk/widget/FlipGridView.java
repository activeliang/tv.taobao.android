package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView;
import com.yunos.tvtaobao.tvsdk.widget.ItemFlipScroller;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.List;

public class FlipGridView extends GridView {
    /* access modifiers changed from: private */
    public boolean DEBUG = false;
    private final String TAG = "FlipGridView";
    private OnFlipRunnableListener mFlipGridViewListener;
    private FlipRunnable mFlipRunnable;
    private List<Integer> mFooterViewList;
    private List<Integer> mHeaderViewList;
    private int mLayoutPreFirstPos;
    private int mLayoutPreLastPos;
    private boolean mLockFlipAnim;
    private boolean mLockOffset;
    protected int mScrollOffset;
    private int mTestSpeedFrame;
    private long mTestSpeedStartTime;

    public interface FlipGridViewHeaderOrFooterInterface {
        int getHorCount();

        int getVerticalCount();

        View getView(int i);

        int getViewIndex(View view);
    }

    public interface OnFlipRunnableListener {
        void onFinished();

        void onFlipItemRunnable(float f, View view, int i);

        void onStart();
    }

    public FlipGridView(Context context) {
        super(context);
        init();
    }

    public FlipGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FlipGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void lockFlipAnimOnceLayout() {
        this.mLockFlipAnim = true;
    }

    public boolean lockKeyEvent(int keyCode) {
        return this.mFlipRunnable.lockKeyEvent(keyCode);
    }

    public boolean isFlipDown() {
        if (!this.mFlipRunnable.isFinished()) {
            return this.mFlipRunnable.isDown();
        }
        return false;
    }

    public boolean isFlipFinished() {
        return this.mFlipRunnable.isFinished();
    }

    public void setDelayAnim(boolean delay) {
        this.mFlipRunnable.setDelayAnim(delay);
    }

    public void setFlipSelectedPosition(int pos) {
        this.mFlipRunnable.setSelectedPosition(pos);
    }

    public int getFastFlipStep() {
        return this.mFlipRunnable.getFastFlipStep();
    }

    public void stopFlip() {
        this.mFlipRunnable.stop();
    }

    public void startRealScroll(int delta) {
        this.mFlipRunnable.setSelectedPosition(this.mSelectedPosition);
        this.mFlipRunnable.startRealScroll(delta);
    }

    public int getFlipColumnFirstItemLeftMoveDistance(int index) {
        return this.mFlipRunnable.getFlipColumnFirstItemLeftMoveDistance(index);
    }

    /* access modifiers changed from: protected */
    public void correctTooHigh(int numColumns, int verticalSpacing, int childCount) {
        this.mLockFlipAnim = true;
        super.correctTooHigh(numColumns, verticalSpacing, childCount);
        this.mLockFlipAnim = false;
    }

    /* access modifiers changed from: protected */
    public void correctTooLow(int numColumns, int verticalSpacing, int childCount) {
        this.mLockFlipAnim = true;
        super.correctTooLow(numColumns, verticalSpacing, childCount);
        this.mLockFlipAnim = false;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.DEBUG) {
            if (System.currentTimeMillis() - this.mTestSpeedStartTime >= 1000) {
                ZpLogger.d("FlipGridView", "dispatchDraw frame = " + this.mTestSpeedFrame);
                this.mTestSpeedStartTime = System.currentTimeMillis();
                this.mTestSpeedFrame = 0;
            }
            this.mTestSpeedFrame++;
        }
    }

    /* access modifiers changed from: protected */
    public void startFlip(int offset) {
        this.mLockOffset = true;
        flipScrollBy(offset);
    }

    public void offsetChildrenTopAndBottom(int offset) {
        if (this.mTouchMode >= 0 || this.mLockFlipAnim) {
            this.mScrollOffset = 0;
            super.offsetChildrenTopAndBottom(offset);
        } else if (!this.mLockOffset) {
            this.mScrollOffset = offset;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mTouchMode >= 0 || !this.mFlipRunnable.lockKeyEvent(keyCode)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!this.mFlipRunnable.isFinished() && (keyCode == 20 || keyCode == 19)) {
            this.mFlipRunnable.startComeDown();
        }
        return super.onKeyUp(keyCode, event);
    }

    /* access modifiers changed from: protected */
    public void layoutChildren() {
        if (!this.mFlipRunnable.isFinished()) {
            this.mLayoutPreFirstPos = getFirstVisiblePosition();
            this.mLayoutPreLastPos = getLastVisiblePosition();
            if (this.mFlipRunnable.isDown()) {
                setMinLastPos(this.mLayoutPreLastPos);
            } else {
                setMinFirstPos(this.mLayoutPreFirstPos);
            }
        }
        gridViewLayoutChildren();
        setMinLastPos(-1);
        setMinFirstPos(Integer.MAX_VALUE);
        if (this.mTouchMode < 0) {
            onLayoutChildrenDone();
            if (this.mScrollOffset != 0) {
                this.mFlipRunnable.setStartingFlipScroll();
                this.mFlipRunnable.checkFlipFastScroll();
                this.mFlipRunnable.addView(getFirstVisiblePosition(), getLastVisiblePosition());
                this.mFlipRunnable.setSelectedPosition(this.mSelectedPosition);
                flipScrollBy(this.mScrollOffset);
                this.mScrollOffset = 0;
            } else {
                this.mFlipRunnable.checkFlipFastScroll();
            }
        }
        this.mLockFlipAnim = false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x008f A[Catch:{ all -> 0x01ff }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x009a A[Catch:{ all -> 0x01ff }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00d9 A[Catch:{ all -> 0x01ff }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void gridViewLayoutChildren() {
        /*
            r21 = this;
            r0 = r21
            boolean r0 = r0.mNeedLayout
            r19 = r0
            if (r19 != 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            r0 = r21
            boolean r3 = r0.mBlockLayoutRequests
            if (r3 != 0) goto L_0x0017
            r19 = 1
            r0 = r19
            r1 = r21
            r1.mBlockLayoutRequests = r0
        L_0x0017:
            r21.invalidate()     // Catch:{ all -> 0x01ff }
            r0 = r21
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r19 != 0) goto L_0x0030
            r21.resetList()     // Catch:{ all -> 0x01ff }
            if (r3 != 0) goto L_0x0008
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mBlockLayoutRequests = r0
            goto L_0x0008
        L_0x0030:
            r0 = r21
            android.graphics.Rect r0 = r0.mListPadding     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r19
            int r7 = r0.top     // Catch:{ all -> 0x01ff }
            int r19 = r21.getBottom()     // Catch:{ all -> 0x01ff }
            int r20 = r21.getTop()     // Catch:{ all -> 0x01ff }
            int r19 = r19 - r20
            r0 = r21
            android.graphics.Rect r0 = r0.mListPadding     // Catch:{ all -> 0x01ff }
            r20 = r0
            r0 = r20
            int r0 = r0.bottom     // Catch:{ all -> 0x01ff }
            r20 = r0
            int r6 = r19 - r20
            int r5 = r21.getChildCount()     // Catch:{ all -> 0x01ff }
            r12 = 0
            r9 = 0
            r16 = 0
            r15 = 0
            r14 = 0
            r0 = r21
            int r0 = r0.mLayoutMode     // Catch:{ all -> 0x01ff }
            r19 = r0
            switch(r19) {
                case 1: goto L_0x0089;
                case 2: goto L_0x00a9;
                case 3: goto L_0x0089;
                case 4: goto L_0x0089;
                case 5: goto L_0x0089;
                case 6: goto L_0x00c2;
                case 7: goto L_0x0065;
                case 8: goto L_0x0065;
                case 9: goto L_0x00a9;
                default: goto L_0x0065;
            }     // Catch:{ all -> 0x01ff }
        L_0x0065:
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01ff }
            r20 = r0
            int r12 = r19 - r20
            if (r12 < 0) goto L_0x007d
            if (r12 >= r5) goto L_0x007d
            r0 = r21
            android.view.View r16 = r0.getChildAt(r12)     // Catch:{ all -> 0x01ff }
        L_0x007d:
            int r19 = r21.getHeaderViewsCount()     // Catch:{ all -> 0x01ff }
            r0 = r21
            r1 = r19
            android.view.View r15 = r0.getChildAt(r1)     // Catch:{ all -> 0x01ff }
        L_0x0089:
            r0 = r21
            boolean r8 = r0.mDataChanged     // Catch:{ all -> 0x01ff }
            if (r8 == 0) goto L_0x0092
            r21.handleDataChanged()     // Catch:{ all -> 0x01ff }
        L_0x0092:
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r19 != 0) goto L_0x00d9
            r21.resetList()     // Catch:{ all -> 0x01ff }
            if (r3 != 0) goto L_0x0008
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mBlockLayoutRequests = r0
            goto L_0x0008
        L_0x00a9:
            r0 = r21
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01ff }
            r20 = r0
            int r12 = r19 - r20
            if (r12 < 0) goto L_0x0089
            if (r12 >= r5) goto L_0x0089
            r0 = r21
            android.view.View r14 = r0.getChildAt(r12)     // Catch:{ all -> 0x01ff }
            goto L_0x0089
        L_0x00c2:
            r0 = r21
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r19 < 0) goto L_0x0089
            r0 = r21
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01ff }
            r20 = r0
            int r9 = r19 - r20
            goto L_0x0089
        L_0x00d9:
            r0 = r21
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            r1 = r19
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x01ff }
            if (r16 == 0) goto L_0x010e
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01ff }
            r20 = r0
            int r19 = r19 - r20
            r0 = r19
            if (r0 == r12) goto L_0x010e
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01ff }
            r20 = r0
            int r12 = r19 - r20
            r0 = r21
            android.view.View r16 = r0.getChildAt(r12)     // Catch:{ all -> 0x01ff }
        L_0x010e:
            r0 = r21
            int r10 = r0.mFirstPosition     // Catch:{ all -> 0x01ff }
            r0 = r21
            com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView$RecycleBin r0 = r0.mRecycler     // Catch:{ all -> 0x01ff }
            r17 = r0
            if (r8 == 0) goto L_0x0131
            r11 = 0
        L_0x011b:
            if (r11 >= r5) goto L_0x0136
            r0 = r21
            android.view.View r19 = r0.getChildAt(r11)     // Catch:{ all -> 0x01ff }
            int r20 = r10 + r11
            r0 = r17
            r1 = r19
            r2 = r20
            r0.addScrapView(r1, r2)     // Catch:{ all -> 0x01ff }
            int r11 = r11 + 1
            goto L_0x011b
        L_0x0131:
            r0 = r17
            r0.fillActiveViews(r5, r10)     // Catch:{ all -> 0x01ff }
        L_0x0136:
            r21.detachAllViewsFromParent()     // Catch:{ all -> 0x01ff }
            r17.removeSkippedScrap()     // Catch:{ all -> 0x01ff }
            r0 = r21
            int r0 = r0.mLayoutMode     // Catch:{ all -> 0x01ff }
            r19 = r0
            switch(r19) {
                case 1: goto L_0x01ec;
                case 2: goto L_0x01d6;
                case 3: goto L_0x020b;
                case 4: goto L_0x0220;
                case 5: goto L_0x0238;
                case 6: goto L_0x0250;
                case 7: goto L_0x0145;
                case 8: goto L_0x0145;
                case 9: goto L_0x01c0;
                default: goto L_0x0145;
            }     // Catch:{ all -> 0x01ff }
        L_0x0145:
            if (r5 != 0) goto L_0x0286
            r0 = r21
            boolean r0 = r0.mStackFromBottom     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r19 != 0) goto L_0x025c
            r0 = r21
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r19 == 0) goto L_0x015d
            boolean r19 = r21.isInTouchMode()     // Catch:{ all -> 0x01ff }
            if (r19 == 0) goto L_0x0258
        L_0x015d:
            r19 = -1
        L_0x015f:
            r0 = r21
            r1 = r19
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x01ff }
            r0 = r21
            android.view.View r18 = r0.fillFromTop(r7)     // Catch:{ all -> 0x01ff }
        L_0x016c:
            r17.scrapActiveViews()     // Catch:{ all -> 0x01ff }
            if (r18 == 0) goto L_0x02ec
            r19 = -1
            r0 = r21
            r1 = r19
            r2 = r18
            r0.positionSelector(r1, r2)     // Catch:{ all -> 0x01ff }
        L_0x017c:
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mLayoutMode = r0     // Catch:{ all -> 0x01ff }
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mDataChanged = r0     // Catch:{ all -> 0x01ff }
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mNeedSync = r0     // Catch:{ all -> 0x01ff }
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            r1 = r19
            r0.setNextSelectedPositionInt(r1)     // Catch:{ all -> 0x01ff }
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r19 <= 0) goto L_0x01ac
            r21.checkSelectionChanged()     // Catch:{ all -> 0x01ff }
        L_0x01ac:
            if (r3 != 0) goto L_0x01b6
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mBlockLayoutRequests = r0
        L_0x01b6:
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mNeedLayout = r0
            goto L_0x0008
        L_0x01c0:
            if (r14 == 0) goto L_0x01cf
            int r19 = r14.getTop()     // Catch:{ all -> 0x01ff }
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillFromSelection(r1, r7, r6)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x01cf:
            r0 = r21
            android.view.View r18 = r0.fillSelectionMiddle(r7, r6)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x01d6:
            if (r14 == 0) goto L_0x01e5
            int r19 = r14.getTop()     // Catch:{ all -> 0x01ff }
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillFromSelection(r1, r7, r6)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x01e5:
            r0 = r21
            android.view.View r18 = r0.fillSelection(r7, r6)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x01ec:
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mFirstPosition = r0     // Catch:{ all -> 0x01ff }
            r0 = r21
            android.view.View r18 = r0.fillFromTop(r7)     // Catch:{ all -> 0x01ff }
            r21.adjustViewsUpOrDown()     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x01ff:
            r19 = move-exception
            if (r3 != 0) goto L_0x020a
            r20 = 0
            r0 = r20
            r1 = r21
            r1.mBlockLayoutRequests = r0
        L_0x020a:
            throw r19
        L_0x020b:
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01ff }
            r19 = r0
            int r19 = r19 + -1
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillUp(r1, r6)     // Catch:{ all -> 0x01ff }
            r21.adjustViewsUpOrDown()     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x0220:
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            int r0 = r0.mSpecificTop     // Catch:{ all -> 0x01ff }
            r20 = r0
            r0 = r21
            r1 = r19
            r2 = r20
            android.view.View r18 = r0.fillSpecific(r1, r2)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x0238:
            r0 = r21
            int r0 = r0.mSyncPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            int r0 = r0.mSpecificTop     // Catch:{ all -> 0x01ff }
            r20 = r0
            r0 = r21
            r1 = r19
            r2 = r20
            android.view.View r18 = r0.fillSpecific(r1, r2)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x0250:
            r0 = r21
            android.view.View r18 = r0.moveSelection(r9, r7, r6)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x0258:
            r19 = 0
            goto L_0x015f
        L_0x025c:
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01ff }
            r19 = r0
            int r13 = r19 + -1
            r0 = r21
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r19 == 0) goto L_0x0272
            boolean r19 = r21.isInTouchMode()     // Catch:{ all -> 0x01ff }
            if (r19 == 0) goto L_0x0283
        L_0x0272:
            r19 = -1
        L_0x0274:
            r0 = r21
            r1 = r19
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x01ff }
            r0 = r21
            android.view.View r18 = r0.fillFromBottom(r13, r6)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x0283:
            r19 = r13
            goto L_0x0274
        L_0x0286:
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r19 < 0) goto L_0x02b7
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01ff }
            r20 = r0
            r0 = r19
            r1 = r20
            if (r0 >= r1) goto L_0x02b7
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r16 != 0) goto L_0x02b2
        L_0x02a8:
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillSpecific(r1, r7)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x02b2:
            int r7 = r16.getTop()     // Catch:{ all -> 0x01ff }
            goto L_0x02a8
        L_0x02b7:
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01ff }
            r20 = r0
            r0 = r19
            r1 = r20
            if (r0 >= r1) goto L_0x02e0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r15 != 0) goto L_0x02db
        L_0x02d1:
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillSpecific(r1, r7)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x02db:
            int r7 = r15.getTop()     // Catch:{ all -> 0x01ff }
            goto L_0x02d1
        L_0x02e0:
            r19 = 0
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillSpecific(r1, r7)     // Catch:{ all -> 0x01ff }
            goto L_0x016c
        L_0x02ec:
            r0 = r21
            int r0 = r0.mTouchMode     // Catch:{ all -> 0x01ff }
            r19 = r0
            if (r19 <= 0) goto L_0x0329
            r0 = r21
            int r0 = r0.mTouchMode     // Catch:{ all -> 0x01ff }
            r19 = r0
            r20 = 3
            r0 = r19
            r1 = r20
            if (r0 >= r1) goto L_0x0329
            r0 = r21
            int r0 = r0.mMotionPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01ff }
            r20 = r0
            int r19 = r19 - r20
            r0 = r21
            r1 = r19
            android.view.View r4 = r0.getChildAt(r1)     // Catch:{ all -> 0x01ff }
            if (r4 == 0) goto L_0x017c
            r0 = r21
            int r0 = r0.mMotionPosition     // Catch:{ all -> 0x01ff }
            r19 = r0
            r0 = r21
            r1 = r19
            r0.positionSelector(r1, r4)     // Catch:{ all -> 0x01ff }
            goto L_0x017c
        L_0x0329:
            r0 = r21
            android.graphics.Rect r0 = r0.mSelectorRect     // Catch:{ all -> 0x01ff }
            r19 = r0
            r19.setEmpty()     // Catch:{ all -> 0x01ff }
            goto L_0x017c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.tvsdk.widget.FlipGridView.gridViewLayoutChildren():void");
    }

    /* access modifiers changed from: protected */
    public void onLayoutChildrenDone() {
    }

    /* access modifiers changed from: protected */
    public int getFillGapNextChildIndex(boolean isDown) {
        if (this.mTouchMode >= 0) {
            return super.getFillGapNextChildIndex(isDown);
        }
        if (isDown) {
            int first = getFirstVisiblePosition();
            int last = getLastVisiblePosition();
            if (first >= last) {
                return 0;
            }
            int left = ((last - this.mHeaderViewList.size()) + 1) % getColumnNum();
            if (left <= 0) {
                left = getColumnNum();
            }
            int childIndex = (last - (left - 1)) - first;
            if (childIndex >= getChildCount()) {
                return getChildCount() - 1;
            }
            return childIndex;
        } else if (getFirstVisiblePosition() < this.mHeaderViewList.size()) {
            return 0;
        } else {
            int numColumn = getColumnNum();
            int i = numColumn - 1;
            int count = getChildCount();
            if (count < numColumn) {
                return count - 1;
            }
            return i;
        }
    }

    /* access modifiers changed from: protected */
    public void onFlipItemRunnableStart() {
        if (this.mFlipGridViewListener != null) {
            this.mFlipGridViewListener.onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onFlipItemRunnableRunning(float moveRatio, View itemView, int index) {
        if (this.mFlipGridViewListener != null) {
            this.mFlipGridViewListener.onFlipItemRunnable(moveRatio, itemView, index);
        }
    }

    /* access modifiers changed from: protected */
    public void onFlipItemRunnableFinished() {
        this.mLockOffset = false;
        if (this.mFlipGridViewListener != null) {
            this.mFlipGridViewListener.onFinished();
        }
    }

    public void setOnFlipGridViewRunnableListener(OnFlipRunnableListener l) {
        this.mFlipGridViewListener = l;
    }

    public void addHeaderView(View v) {
        if (v == null || !(v instanceof FlipGridViewHeaderOrFooterInterface)) {
            throw new IllegalStateException("Cannot add FlipGridView header view to list header view maybe not FlipGridViewHeaderOrFooterInterface");
        }
        FlipGridViewHeaderOrFooterInterface headView = (FlipGridViewHeaderOrFooterInterface) v;
        this.mHeaderViewList.add(Integer.valueOf(this.mFlipRunnable.makeHeaderAndFooterViewInfo(headView.getHorCount(), headView.getVerticalCount())));
        super.addHeaderView(v);
    }

    public void addHeaderViewInfo(View v) {
        if (v == null || !(v instanceof FlipGridViewHeaderOrFooterInterface)) {
            throw new IllegalStateException("Cannot add FlipGridView header view to list header view maybe not FlipGridViewHeaderOrFooterInterface");
        }
        FlipGridViewHeaderOrFooterInterface headView = (FlipGridViewHeaderOrFooterInterface) v;
        this.mHeaderViewList.add(Integer.valueOf(this.mFlipRunnable.makeHeaderAndFooterViewInfo(headView.getHorCount(), headView.getVerticalCount())));
    }

    public boolean removeHeaderView(View headerView) {
        if (isFlipFinished()) {
            int headerViewIndex = -1;
            int i = 0;
            while (true) {
                if (i < this.mHeaderViewInfos.size()) {
                    AbsBaseListView.FixedViewInfo headerChildView = (AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(i);
                    if (headerChildView != null && headerChildView.view.equals(headerView)) {
                        headerViewIndex = i;
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            if (headerViewIndex >= 0) {
                lockFlipAnimOnceLayout();
                this.mHeaderViewList.remove(headerViewIndex);
                return super.removeHeaderView(headerView);
            }
        } else {
            ZpLogger.e("FlipGridView", "flip running can not remove view");
        }
        return false;
    }

    public void clearHeaderViewInfo() {
        this.mHeaderViewList.clear();
    }

    public void addFooterView(View v) {
        if (v == null || !(v instanceof FlipGridViewHeaderOrFooterInterface)) {
            throw new IllegalStateException("Cannot add FlipGridView footer view to list header view maybe not FlipGridViewHeaderOrFooterInterface");
        }
        FlipGridViewHeaderOrFooterInterface headView = (FlipGridViewHeaderOrFooterInterface) v;
        this.mFooterViewList.add(Integer.valueOf(this.mFlipRunnable.makeHeaderAndFooterViewInfo(headView.getHorCount(), headView.getVerticalCount())));
        super.addFooterView(v);
    }

    public void addFooterViewInfo(View v) {
        if (v == null || !(v instanceof FlipGridViewHeaderOrFooterInterface)) {
            throw new IllegalStateException("Cannot add FlipGridView footer view to list header view maybe not FlipGridViewHeaderOrFooterInterface");
        }
        FlipGridViewHeaderOrFooterInterface footerView = (FlipGridViewHeaderOrFooterInterface) v;
        this.mFooterViewList.add(Integer.valueOf(this.mFlipRunnable.makeHeaderAndFooterViewInfo(footerView.getHorCount(), footerView.getVerticalCount())));
    }

    public boolean removeFooterView(View footerView) {
        if (isFlipFinished()) {
            int footerViewIndex = -1;
            int i = 0;
            while (true) {
                if (i < this.mFooterViewInfos.size()) {
                    AbsBaseListView.FixedViewInfo footerChildView = (AbsBaseListView.FixedViewInfo) this.mFooterViewInfos.get(i);
                    if (footerChildView != null && footerChildView.view.equals(footerView)) {
                        footerViewIndex = i;
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            if (footerViewIndex >= 0) {
                lockFlipAnimOnceLayout();
                this.mFooterViewInfos.remove(footerViewIndex);
                return super.removeFooterView(footerView);
            }
        } else {
            ZpLogger.e("FlipGridView", "flip running can not remove view");
        }
        return false;
    }

    public void clearFooterViewInfo() {
        this.mFooterViewList.clear();
    }

    public int getFlipItemLeftMoveDistance(int index, int secondIndex) {
        if (!this.mFlipRunnable.isFinished()) {
            return this.mFlipRunnable.getFlipItemLeftMoveDistance(index, secondIndex);
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public boolean isFliping() {
        return !this.mFlipRunnable.isFinished();
    }

    private void flipScrollBy(int distance) {
        int firstPos = this.mFirstPosition;
        int childCount = getChildCount();
        int lastPos = firstPos + childCount;
        int topLimit = getPaddingTop();
        int bottomLimit = getHeight() - getPaddingBottom();
        if (distance == 0 || this.mItemCount == 0 || childCount == 0 || ((firstPos == 0 && getChildAt(0).getTop() == topLimit && distance > 0) || (lastPos == this.mItemCount && getChildAt(childCount - 1).getBottom() == bottomLimit && distance < 0))) {
            this.mFlipRunnable.stop();
            return;
        }
        reportScrollStateChange(2);
        this.mFlipRunnable.startScroll(distance);
    }

    /* access modifiers changed from: private */
    public List<Integer> getHeaderViewInfo() {
        return this.mHeaderViewList;
    }

    /* access modifiers changed from: private */
    public List<Integer> getFooterViewInfo() {
        return this.mFooterViewList;
    }

    private void init() {
        this.mFlipRunnable = new FlipRunnable();
        this.mFlipRunnable.setOnFlipRunnableListener(new OnFlipRunnableListener() {
            public void onFlipItemRunnable(float moveRatio, View itemView, int index) {
                FlipGridView.this.onFlipItemRunnableRunning(moveRatio, itemView, index);
            }

            public void onFinished() {
                FlipGridView.this.onFlipItemRunnableFinished();
            }

            public void onStart() {
                FlipGridView.this.onFlipItemRunnableStart();
            }
        });
        this.mHeaderViewList = new ArrayList();
        this.mFooterViewList = new ArrayList();
    }

    /* access modifiers changed from: protected */
    public void detachOffScreenChildren(boolean isDown) {
        int numChildren = getChildCount();
        int firstPosition = this.mFirstPosition;
        int start = 0;
        int count = 0;
        if (!isDown) {
            int bottom = getHeight();
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
            for (int i2 = 0; i2 < numChildren; i2++) {
                int n2 = i2;
                View child2 = getChildAt(n2);
                if (child2.getBottom() >= 0) {
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
    public int getSelectedRowItemOffset(int rowStart, View selectedRowView) {
        int offset = 0;
        int tag = ((Integer) selectedRowView.getTag()).intValue();
        int columnCount = getColumnNum();
        int tagRowStart = getRowStart(tag);
        if (tagRowStart > rowStart + columnCount) {
            int upRowDelta = ((tagRowStart - rowStart) / columnCount) - 1;
            offset = 0 - ((selectedRowView.getHeight() + getVerticalSpacing()) * upRowDelta);
            if (rowStart < this.mHeaderViewInfos.size()) {
                offset -= selectedRowView.getHeight() + getVerticalSpacing();
            }
            int rowStartLeft = getFlipColumnFirstItemLeftMoveDistance(rowStart);
            int tagRowLeft = getFlipColumnFirstItemLeftMoveDistance(tagRowStart);
            int tagLeft = getFlipItemLeftMoveDistance(tag, 0);
            if (this.DEBUG) {
                ZpLogger.i("FlipGridView", "getSelectedRowItemOffset mReferenceViewInSelectedRow=" + selectedRowView.getTag() + " tagRowStart=" + tagRowStart + " rowStart=" + rowStart + " offset=" + offset + " height=" + selectedRowView.getHeight() + " upRowDelta=" + upRowDelta + " rowStartLeft=" + rowStartLeft + " tagLeft=" + tagLeft + " tagRowLeft=" + tagRowLeft);
            }
        }
        return offset;
    }

    private class FlipRunnable implements Runnable {
        /* access modifiers changed from: private */
        public SparseArray<Integer> mFlipItemBottomList = new SparseArray<>();
        /* access modifiers changed from: private */
        public int mHeaderViewDelta = 0;
        private ItemFlipScroller mItemFlipScroller = new ItemFlipScroller();
        /* access modifiers changed from: private */
        public OnFlipRunnableListener mOnFlipRunnableListener;

        FlipRunnable() {
            this.mItemFlipScroller.setItemFlipScrollerListener(new ItemFlipScroller.ItemFlipScrollerListener(FlipGridView.this) {
                public void onOffsetNewChild(int childIndex, int secondIndex, int delta) {
                    View childView = FlipGridView.this.getChildAt(childIndex - FlipGridView.this.getFirstVisiblePosition());
                    if (FlipGridView.this.DEBUG) {
                        ZpLogger.i("FlipGridView", "onOffsetNewChild childIndex=" + childIndex + " secondIndex=" + secondIndex + " delta=" + delta + " before bottom=" + childView.getBottom());
                    }
                    if (childView != null && delta != 0) {
                        if (childIndex >= FlipGridView.this.getHeaderViewsCount()) {
                            childView.offsetTopAndBottom(delta);
                            FlipRunnable.this.mFlipItemBottomList.put(FlipRunnable.this.getFlipItemMapKey(childIndex, 0), Integer.valueOf(childView.getBottom()));
                        } else if (childView == null || !(childView instanceof FlipGridViewHeaderOrFooterInterface)) {
                            childView.offsetTopAndBottom(delta);
                            FlipRunnable.this.mFlipItemBottomList.put(FlipRunnable.this.getFlipItemMapKey(childIndex, 0), Integer.valueOf(childView.getBottom()));
                        } else {
                            FlipGridViewHeaderOrFooterInterface headerView = (FlipGridViewHeaderOrFooterInterface) childView;
                            int headerChildCount = headerView.getHorCount() * headerView.getVerticalCount();
                            if (secondIndex < headerChildCount) {
                                if (secondIndex == headerChildCount - 1) {
                                    int unused = FlipRunnable.this.mHeaderViewDelta = delta;
                                    if (FlipGridView.this.DEBUG) {
                                        ZpLogger.i("FlipGridView", "onOffsetNewChild mHeaderViewDelta=" + FlipRunnable.this.mHeaderViewDelta + " secondIndex=" + secondIndex + " headerChildCount=" + headerChildCount);
                                    }
                                    childView.offsetTopAndBottom(delta);
                                    FlipRunnable.this.mFlipItemBottomList.put(FlipRunnable.this.getFlipItemMapKey(childIndex, -1), Integer.valueOf(childView.getBottom()));
                                }
                                View headerChildView = headerView.getView(secondIndex);
                                if (headerChildView != null) {
                                    headerChildView.offsetTopAndBottom(delta - FlipRunnable.this.mHeaderViewDelta);
                                    FlipRunnable.this.mFlipItemBottomList.put(FlipRunnable.this.getFlipItemMapKey(childIndex, secondIndex), Integer.valueOf(headerChildView.getBottom()));
                                }
                            }
                        }
                    }
                }

                public void onFinished() {
                    if (FlipRunnable.this.mOnFlipRunnableListener != null) {
                        FlipRunnable.this.mOnFlipRunnableListener.onFinished();
                    }
                }
            });
        }

        /* access modifiers changed from: package-private */
        public void setHor_delay_distance(int hor_delay_distance) {
            this.mItemFlipScroller.setHor_delay_distance(hor_delay_distance);
        }

        /* access modifiers changed from: package-private */
        public void setVer_delay_distance(int ver_delay_distance) {
            this.mItemFlipScroller.setVer_delay_distance(ver_delay_distance);
        }

        /* access modifiers changed from: package-private */
        public void setMin_fast_setp_discance(int min_fast_setp_discance) {
            this.mItemFlipScroller.setMin_fast_setp_discance(min_fast_setp_discance);
        }

        /* access modifiers changed from: package-private */
        public void setFlip_scroll_frame_count(int flip_scroll_frame_count) {
            this.mItemFlipScroller.setFlip_scroll_frame_count(flip_scroll_frame_count);
        }

        /* access modifiers changed from: package-private */
        public void setHor_delay_frame_count(int hor_delay_frame_count) {
            this.mItemFlipScroller.setHor_delay_frame_count(hor_delay_frame_count);
        }

        /* access modifiers changed from: package-private */
        public void setVer_delay_frame_count(int ver_delay_frame_count) {
            this.mItemFlipScroller.setVer_delay_frame_count(ver_delay_frame_count);
        }

        public boolean lockKeyEvent(int keyCode) {
            boolean isFinished = this.mItemFlipScroller.isFinished();
            if (FlipGridView.this.DEBUG) {
                ZpLogger.i("FlipGridView", "lockKeyEvent isFinished=" + isFinished + " keyCode=" + keyCode + " isDown=" + this.mItemFlipScroller.isDown());
            }
            if (!isFinished) {
                if (keyCode == 19 && this.mItemFlipScroller.isDown()) {
                    return true;
                }
                if ((keyCode == 20 && !this.mItemFlipScroller.isDown()) || keyCode == 21 || keyCode == 22) {
                    return true;
                }
                return false;
            }
            return false;
        }

        public void setSelectedPosition(int selectedPosition) {
            this.mItemFlipScroller.setSelectedPosition(selectedPosition);
        }

        public int getFastFlipStep() {
            return this.mItemFlipScroller.getFastFlipStep();
        }

        public void startComeDown() {
            this.mItemFlipScroller.startComeDown();
        }

        public void setDelayAnim(boolean delay) {
            this.mItemFlipScroller.setDelayAnim(delay);
        }

        public boolean isFinished() {
            return this.mItemFlipScroller.isFinished();
        }

        public void addView(int start, int end) {
            this.mItemFlipScroller.checkAddView(start, end);
        }

        public void setOnFlipRunnableListener(OnFlipRunnableListener listener) {
            this.mOnFlipRunnableListener = listener;
        }

        public int getFlipItemLeftMoveDistance(int index, int secondIndex) {
            if (FlipGridView.this.DEBUG) {
                ZpLogger.i("FlipGridView", "getFlipItemLeftMoveDistance index=" + index + " secondIndex=" + secondIndex);
            }
            return this.mItemFlipScroller.getFlipItemLeftMoveDistance(index, secondIndex);
        }

        public void setStartingFlipScroll() {
            this.mItemFlipScroller.setStartingFlipScroll();
        }

        public boolean isDown() {
            return this.mItemFlipScroller.isDown();
        }

        public void checkFlipFastScroll() {
            int delta;
            int delta2;
            View headerChildView;
            if (!this.mItemFlipScroller.isFinished()) {
                for (int i = FlipGridView.this.getChildCount() - 1; i >= 0; i--) {
                    int index = i + FlipGridView.this.getFirstVisiblePosition();
                    View childView = FlipGridView.this.getChildAt(i);
                    if (index >= FlipGridView.this.getHeaderViewsCount()) {
                        Integer childBottom = this.mFlipItemBottomList.get(getFlipItemMapKey(index, 0));
                        if (FlipGridView.this.DEBUG) {
                            ZpLogger.i("FlipGridView", "checkFlipFastScroll adapter index=" + index + " childBottom=" + childBottom + " currBottom=" + childView.getBottom());
                        }
                        if (!(childBottom == null || (delta = childBottom.intValue() - childView.getBottom()) == 0)) {
                            this.mItemFlipScroller.setFastScrollOffset(index, 0, delta);
                            childView.offsetTopAndBottom(delta);
                        }
                    } else if (childView == null || !(childView instanceof FlipGridViewHeaderOrFooterInterface)) {
                        Integer childBottom2 = this.mFlipItemBottomList.get(getFlipItemMapKey(index, 0));
                        if (!(childBottom2 == null || (delta2 = childBottom2.intValue() - childView.getBottom()) == 0)) {
                            this.mItemFlipScroller.setFastScrollOffset(index, 0, delta2);
                            childView.offsetTopAndBottom(delta2);
                        }
                    } else {
                        FlipGridViewHeaderOrFooterInterface headerView = (FlipGridViewHeaderOrFooterInterface) childView;
                        int headerChildCount = headerView.getHorCount() * headerView.getVerticalCount();
                        Integer childBottom3 = this.mFlipItemBottomList.get(getFlipItemMapKey(index, -1));
                        if (childBottom3 != null) {
                            int delta3 = childBottom3.intValue() - childView.getBottom();
                            if (FlipGridView.this.DEBUG) {
                                ZpLogger.i("FlipGridView", "checkFlipFastScroll headerView childBottom=" + childBottom3 + " headerView bottom=" + childView.getBottom() + " delta=" + delta3);
                            }
                            if (delta3 != 0) {
                                childView.offsetTopAndBottom(delta3);
                            }
                            for (int headerChildIndex = headerChildCount - 1; headerChildIndex >= 0; headerChildIndex--) {
                                Integer childBottom4 = this.mFlipItemBottomList.get(getFlipItemMapKey(index, headerChildIndex));
                                if (!(childBottom4 == null || (headerChildView = headerView.getView(headerChildIndex)) == null)) {
                                    int delta4 = childBottom4.intValue() - headerChildView.getBottom();
                                    if (FlipGridView.this.DEBUG) {
                                        ZpLogger.i("FlipGridView", "checkFlipFastScroll headerChild headerChildIndex=" + headerChildIndex + " childBottom=" + childBottom4 + " headerChildView bottom=" + headerChildView.getBottom());
                                    }
                                    if (delta4 != 0) {
                                        this.mItemFlipScroller.setFastScrollOffset(index, headerChildIndex, delta4);
                                        headerChildView.offsetTopAndBottom(delta4);
                                    }
                                }
                            }
                        }
                    }
                }
                return;
            }
            this.mFlipItemBottomList.clear();
        }

        public void startScroll(int distance) {
            if (this.mItemFlipScroller.isFinished()) {
                this.mItemFlipScroller.clearChild();
                this.mItemFlipScroller.setColumnCount(FlipGridView.this.getColumnNum());
                this.mItemFlipScroller.setHeaderViewInfo(FlipGridView.this.getHeaderViewInfo());
                this.mItemFlipScroller.setFooterViewInfo(FlipGridView.this.getFooterViewInfo());
                this.mItemFlipScroller.setTotalItemCount(FlipGridView.this.getCount());
                this.mItemFlipScroller.addGridView(FlipGridView.this.getFirstVisiblePosition(), FlipGridView.this.getLastVisiblePosition(), distance < 0);
                this.mItemFlipScroller.startComputDistanceScroll(distance);
                FlipGridView.this.postOnAnimation(this);
                if (this.mOnFlipRunnableListener != null) {
                    this.mOnFlipRunnableListener.onStart();
                    return;
                }
                return;
            }
            this.mItemFlipScroller.startComputDistanceScroll(distance);
            if (this.mOnFlipRunnableListener != null) {
                this.mOnFlipRunnableListener.onStart();
            }
        }

        public void startRealScroll(int distance) {
            if (!this.mItemFlipScroller.isFinished()) {
                this.mItemFlipScroller.startRealScroll(distance);
                if (this.mOnFlipRunnableListener != null) {
                    this.mOnFlipRunnableListener.onStart();
                }
            }
        }

        public int getFlipColumnFirstItemLeftMoveDistance(int index) {
            if (!this.mItemFlipScroller.isFinished()) {
                return this.mItemFlipScroller.getFlipColumnFirstItemLeftMoveDistance(index);
            }
            return 0;
        }

        public void run() {
            setEachFrame(false);
        }

        public void stop() {
            if (!this.mItemFlipScroller.isFinished()) {
                setEachFrame(true);
                this.mItemFlipScroller.finish();
            }
        }

        public int makeHeaderAndFooterViewInfo(int columnCount, int verticalCount) {
            return (columnCount << 16) | verticalCount;
        }

        /* access modifiers changed from: private */
        public int getFlipItemMapKey(int index, int secondIndex) {
            return (index << 8) | secondIndex;
        }

        private void setEachFrame(boolean finishImmediately) {
            boolean more;
            float moveRatio;
            int delta;
            int delta2;
            int delta3;
            int preFirst = FlipGridView.this.getFirstVisiblePosition();
            int preLast = FlipGridView.this.getLastVisiblePosition();
            if (FlipGridView.this.mDataChanged) {
                FlipGridView.this.layoutChildren();
            }
            if (FlipGridView.this.mItemCount != 0 && FlipGridView.this.getChildCount() != 0) {
                if (this.mItemFlipScroller.isFinished() || !finishImmediately) {
                    more = this.mItemFlipScroller.computeScrollOffset();
                } else {
                    more = true;
                }
                if (more) {
                    if (finishImmediately) {
                        moveRatio = 1.0f;
                    } else {
                        moveRatio = this.mItemFlipScroller.getFlipItemMoveRatio(0, 0);
                    }
                    for (int i = FlipGridView.this.getChildCount() - 1; i >= 0; i--) {
                        int index = i + FlipGridView.this.getFirstVisiblePosition();
                        if (index < FlipGridView.this.getHeaderViewsCount()) {
                            View view = FlipGridView.this.getChildAt(i);
                            if (this.mOnFlipRunnableListener != null) {
                                this.mOnFlipRunnableListener.onFlipItemRunnable(moveRatio, view, index);
                            }
                            if (view == null || !(view instanceof FlipGridViewHeaderOrFooterInterface)) {
                                if (finishImmediately) {
                                    delta2 = this.mItemFlipScroller.getFlipItemLeftMoveDistance(index, 0);
                                } else {
                                    delta2 = this.mItemFlipScroller.getCurrDelta(index, 0);
                                }
                                view.offsetTopAndBottom(delta2);
                                this.mFlipItemBottomList.put(getFlipItemMapKey(index, 0), Integer.valueOf(view.getBottom()));
                            } else {
                                FlipGridViewHeaderOrFooterInterface headerView = (FlipGridViewHeaderOrFooterInterface) view;
                                int headerChildCount = headerView.getHorCount() * headerView.getVerticalCount();
                                int headerViewDelta = 0;
                                for (int headerChildIndex = headerChildCount - 1; headerChildIndex >= 0; headerChildIndex--) {
                                    if (finishImmediately) {
                                        delta3 = this.mItemFlipScroller.getFlipItemLeftMoveDistance(index, headerChildIndex);
                                    } else {
                                        delta3 = this.mItemFlipScroller.getCurrDelta(index, headerChildIndex);
                                    }
                                    if (headerChildIndex >= headerChildCount - 1) {
                                        headerViewDelta = delta3;
                                        view.offsetTopAndBottom(headerViewDelta);
                                        this.mFlipItemBottomList.put(getFlipItemMapKey(index, -1), Integer.valueOf(view.getBottom()));
                                    }
                                    View headerChildView = headerView.getView(headerChildIndex);
                                    if (headerChildView != null) {
                                        headerChildView.offsetTopAndBottom(delta3 - headerViewDelta);
                                        this.mFlipItemBottomList.put(getFlipItemMapKey(index, headerChildIndex), Integer.valueOf(headerChildView.getBottom()));
                                    }
                                }
                            }
                        } else {
                            if (finishImmediately) {
                                delta = this.mItemFlipScroller.getFlipItemLeftMoveDistance(index, 0);
                            } else {
                                delta = this.mItemFlipScroller.getCurrDelta(index, 0);
                            }
                            View view2 = FlipGridView.this.getChildAt(i);
                            if (this.mOnFlipRunnableListener != null) {
                                this.mOnFlipRunnableListener.onFlipItemRunnable(moveRatio, view2, index);
                            }
                            if (delta != 0) {
                                if (!FlipGridView.this.DEBUG) {
                                    view2.offsetTopAndBottom(delta);
                                } else if (index == 19 || index == 13 || index == 24) {
                                    int before = view2.getBottom();
                                    view2.offsetTopAndBottom(delta);
                                    ZpLogger.i("FlipGridView", "run index=" + index + " before=" + before + " delta=" + delta + " after=" + view2.getBottom());
                                } else {
                                    view2.offsetTopAndBottom(delta);
                                }
                            }
                            this.mFlipItemBottomList.put(getFlipItemMapKey(index, 0), Integer.valueOf(view2.getBottom()));
                        }
                    }
                    FlipGridView.this.detachOffScreenChildren(this.mItemFlipScroller.isDown());
                    FlipGridView.this.fillGap(this.mItemFlipScroller.isDown());
                    FlipGridView.this.onScrollChanged(0, 0, 0, 0);
                    FlipGridView.this.invalidate();
                    FlipGridView.this.postOnAnimation(this);
                    this.mItemFlipScroller.addGridView(FlipGridView.this.getFirstVisiblePosition(), FlipGridView.this.getLastVisiblePosition(), this.mItemFlipScroller.isDown());
                    int currFirst = FlipGridView.this.getFirstVisiblePosition();
                    int currLast = FlipGridView.this.getLastVisiblePosition();
                    int headerViewCount = FlipGridView.this.getHeaderViewsCount();
                    if (currFirst < preFirst) {
                        if (preFirst <= headerViewCount - 1) {
                            for (int i2 = preFirst; i2 <= headerViewCount - 1; i2++) {
                                int index2 = i2 - currFirst;
                                if (index2 >= 0) {
                                    View childView = FlipGridView.this.getChildAt(index2);
                                    if (childView == null || !(childView instanceof FlipGridViewHeaderOrFooterInterface)) {
                                        this.mFlipItemBottomList.put(getFlipItemMapKey(i2, 0), Integer.valueOf(childView.getBottom()));
                                    } else {
                                        FlipGridViewHeaderOrFooterInterface headerView2 = (FlipGridViewHeaderOrFooterInterface) childView;
                                        this.mFlipItemBottomList.put(getFlipItemMapKey(i2, -1), Integer.valueOf(childView.getBottom()));
                                        for (int headerChildIndex2 = (headerView2.getHorCount() * headerView2.getVerticalCount()) - 1; headerChildIndex2 >= 0; headerChildIndex2--) {
                                            View headerChildView2 = headerView2.getView(headerChildIndex2);
                                            if (headerChildView2 != null) {
                                                this.mFlipItemBottomList.put(getFlipItemMapKey(i2, headerChildIndex2), Integer.valueOf(headerChildView2.getBottom()));
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (preFirst <= headerViewCount - 1 || currFirst > headerViewCount - 1) {
                            for (int i3 = currFirst; i3 < preFirst; i3++) {
                                int index3 = i3 - currFirst;
                                if (index3 >= 0) {
                                    this.mFlipItemBottomList.put(getFlipItemMapKey(i3, 0), Integer.valueOf(FlipGridView.this.getChildAt(index3).getBottom()));
                                }
                            }
                        } else {
                            for (int i4 = currFirst; i4 <= headerViewCount - 1; i4++) {
                                int index4 = i4 - currFirst;
                                if (index4 >= 0) {
                                    View childView2 = FlipGridView.this.getChildAt(index4);
                                    if (childView2 == null || !(childView2 instanceof FlipGridViewHeaderOrFooterInterface)) {
                                        this.mFlipItemBottomList.put(getFlipItemMapKey(i4, 0), Integer.valueOf(childView2.getBottom()));
                                    } else {
                                        FlipGridViewHeaderOrFooterInterface headerView3 = (FlipGridViewHeaderOrFooterInterface) childView2;
                                        this.mFlipItemBottomList.put(getFlipItemMapKey(i4, -1), Integer.valueOf(childView2.getBottom()));
                                        for (int headerChildIndex3 = (headerView3.getHorCount() * headerView3.getVerticalCount()) - 1; headerChildIndex3 >= 0; headerChildIndex3--) {
                                            View headerChildView3 = headerView3.getView(headerChildIndex3);
                                            if (headerChildView3 != null) {
                                                this.mFlipItemBottomList.put(getFlipItemMapKey(i4, headerChildIndex3), Integer.valueOf(headerChildView3.getBottom()));
                                            }
                                        }
                                    }
                                }
                            }
                            for (int i5 = (headerViewCount - 1) + 1; i5 < preFirst; i5++) {
                                int index5 = i5 - currFirst;
                                if (index5 >= 0) {
                                    this.mFlipItemBottomList.put(getFlipItemMapKey(i5, 0), Integer.valueOf(FlipGridView.this.getChildAt(index5).getBottom()));
                                }
                            }
                        }
                    }
                    if (currLast > preLast) {
                        for (int i6 = preLast + 1; i6 <= currLast; i6++) {
                            int index6 = i6 - currFirst;
                            if (index6 >= 0) {
                                this.mFlipItemBottomList.put(getFlipItemMapKey(i6, 0), Integer.valueOf(FlipGridView.this.getChildAt(index6).getBottom()));
                            }
                        }
                    }
                }
            }
        }
    }

    public void setHor_delay_distance(int hor_delay_distance) {
        this.mFlipRunnable.setHor_delay_distance(hor_delay_distance);
    }

    public void setVer_delay_distance(int ver_delay_distance) {
        this.mFlipRunnable.setVer_delay_distance(ver_delay_distance);
    }

    public void setMin_fast_setp_discance(int min_fast_setp_discance) {
        this.mFlipRunnable.setMin_fast_setp_discance(min_fast_setp_discance);
    }

    public void setFlip_scroll_frame_count(int flip_scroll_frame_count) {
        this.mFlipRunnable.setFlip_scroll_frame_count(flip_scroll_frame_count);
    }

    public void setHor_delay_frame_count(int hor_delay_frame_count) {
        this.mFlipRunnable.setHor_delay_frame_count(hor_delay_frame_count);
    }

    public void setVer_delay_frame_count(int ver_delay_frame_count) {
        this.mFlipRunnable.setVer_delay_frame_count(ver_delay_frame_count);
    }
}
