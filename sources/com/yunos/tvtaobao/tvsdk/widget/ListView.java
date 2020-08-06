package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Checkable;
import android.widget.ListAdapter;
import com.alibaba.wireless.security.SecExceptionCode;
import com.uc.webview.export.extension.UCCore;
import com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView;
import com.zhiping.dev.android.logger.ZpLogger;

public class ListView extends AbsListView {
    private static final int MIN_SCROLL_PREVIEW_PIXELS = 2;
    private static final String TAG = "ListView";
    private final ArrowScrollFocusResult mArrowScrollFocusResult = new ArrowScrollFocusResult();
    Drawable mDivider;
    int mDividerHeight;
    private boolean mDividerIsOpaque;

    public ListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListView(Context context) {
        super(context);
    }

    public View getFirstVisibleChild() {
        return getChildAt(getFirsVisibletChildIndex());
    }

    public View getLastVisibleChild() {
        return getChildAt(getLastVisibleChildIndex());
    }

    public int getFirsVisibletChildIndex() {
        return getUpPreLoadedCount();
    }

    public int getLastVisibleChildIndex() {
        return (getChildCount() - 1) - getDownPreLoadedCount();
    }

    public int getVisibleChildCount() {
        return (getChildCount() - getUpPreLoadedCount()) - getDownPreLoadedCount();
    }

    public int getFirstPosition() {
        return getFirstVisiblePosition() - getUpPreLoadedCount();
    }

    public int getLastPosition() {
        return (getFirstPosition() + getChildCount()) - 1;
    }

    public int getLastVisiblePosition() {
        return (this.mFirstPosition + getVisibleChildCount()) - 1;
    }

    /* access modifiers changed from: package-private */
    public void fillGap(boolean isDown) {
        int startOffset;
        int visibleChildCount = getVisibleChildCount();
        if (isDown) {
            int paddingTop = 0;
            if ((getGroupFlags() & 34) == 34) {
                paddingTop = getListPaddingTop();
            }
            if (visibleChildCount > 0) {
                startOffset = getChildAt(getLastVisibleChildIndex()).getBottom() + this.mDividerHeight + this.mSpacing;
            } else {
                startOffset = paddingTop;
            }
            fillDown(getFirstVisiblePosition() + visibleChildCount, startOffset);
            correctTooHigh(visibleChildCount);
            return;
        }
        int paddingBottom = 0;
        if ((getGroupFlags() & 34) == 34) {
            paddingBottom = getListPaddingBottom();
        }
        fillUp(getFirstVisiblePosition() - 1, visibleChildCount > 0 ? (getChildAt(getFirsVisibletChildIndex()).getTop() - this.mDividerHeight) - this.mSpacing : getHeight() - paddingBottom);
        correctTooLow(visibleChildCount);
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        ListAdapter adapter = this.mAdapter;
        int closetChildIndex = -1;
        int closestChildTop = 0;
        if (!(adapter == null || !gainFocus || previouslyFocusedRect == null)) {
            previouslyFocusedRect.offset(getScrollX(), getScrollY());
            if (adapter.getCount() < getVisibleChildCount() + this.mFirstPosition) {
                this.mLayoutMode = 0;
                layoutChildren();
            }
            Rect otherRect = this.mTempRect;
            int minDistance = Integer.MAX_VALUE;
            int visibleChildCount = getVisibleChildCount();
            int firstPosition = getFirstVisiblePosition();
            for (int i = 0; i < visibleChildCount; i++) {
                if (adapter.isEnabled(firstPosition + i)) {
                    View other = getChildAt(i);
                    other.getDrawingRect(otherRect);
                    offsetDescendantRectToMyCoords(other, otherRect);
                    int distance = getDistance(previouslyFocusedRect, otherRect, direction);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closetChildIndex = i;
                        closestChildTop = other.getTop();
                    }
                }
            }
        }
        if (closetChildIndex >= 0) {
            if (closestChildTop <= this.mListPadding.top) {
                closestChildTop = this.mListPadding.top;
            }
            setSelectionFromTop(getFirstVisiblePosition() + closetChildIndex, closestChildTop);
        }
    }

    public void setDivider(Drawable divider) {
        boolean z = false;
        if (divider != null) {
            this.mDividerHeight = divider.getIntrinsicHeight();
        } else {
            this.mDividerHeight = 0;
        }
        this.mDivider = divider;
        if (divider == null || divider.getOpacity() == -1) {
            z = true;
        }
        this.mDividerIsOpaque = z;
        requestLayout();
        invalidate();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x009e A[Catch:{ all -> 0x0131 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a7 A[Catch:{ all -> 0x0131 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00d4 A[Catch:{ all -> 0x0131 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutChildren() {
        /*
            r28 = this;
            r0 = r28
            boolean r8 = r0.mBlockLayoutRequests
            if (r8 != 0) goto L_0x001e
            r2 = 1
            r0 = r28
            r0.mBlockLayoutRequests = r2
            r28.invalidate()     // Catch:{ all -> 0x0131 }
            r0 = r28
            android.widget.ListAdapter r2 = r0.mAdapter     // Catch:{ all -> 0x0131 }
            if (r2 != 0) goto L_0x001f
            r28.resetList()     // Catch:{ all -> 0x0131 }
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r28
            r0.mBlockLayoutRequests = r2
        L_0x001e:
            return
        L_0x001f:
            r0 = r28
            android.graphics.Rect r2 = r0.mListPadding     // Catch:{ all -> 0x0131 }
            int r6 = r2.top     // Catch:{ all -> 0x0131 }
            int r2 = r28.getBottom()     // Catch:{ all -> 0x0131 }
            int r26 = r28.getTop()     // Catch:{ all -> 0x0131 }
            int r2 = r2 - r26
            r0 = r28
            android.graphics.Rect r0 = r0.mListPadding     // Catch:{ all -> 0x0131 }
            r26 = r0
            r0 = r26
            int r0 = r0.bottom     // Catch:{ all -> 0x0131 }
            r26 = r0
            int r7 = r2 - r26
            int r25 = r28.getVisibleChildCount()     // Catch:{ all -> 0x0131 }
            int r10 = r28.getChildCount()     // Catch:{ all -> 0x0131 }
            r20 = 0
            r5 = 0
            r3 = 0
            r21 = 0
            r4 = 0
            r15 = 0
            r0 = r28
            int r2 = r0.mLayoutMode     // Catch:{ all -> 0x0131 }
            switch(r2) {
                case 1: goto L_0x0098;
                case 2: goto L_0x00b3;
                case 3: goto L_0x0098;
                case 4: goto L_0x0098;
                case 5: goto L_0x0098;
                default: goto L_0x0054;
            }     // Catch:{ all -> 0x0131 }
        L_0x0054:
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0131 }
            r26 = r0
            int r20 = r2 - r26
            if (r20 < 0) goto L_0x0074
            r0 = r20
            r1 = r25
            if (r0 >= r1) goto L_0x0074
            int r2 = r28.getUpPreLoadedCount()     // Catch:{ all -> 0x0131 }
            int r2 = r2 + r20
            r0 = r28
            android.view.View r3 = r0.getChildAt(r2)     // Catch:{ all -> 0x0131 }
        L_0x0074:
            android.view.View r21 = r28.getFirstVisibleChild()     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0131 }
            if (r2 < 0) goto L_0x008a
            r0 = r28
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0131 }
            r26 = r0
            int r5 = r2 - r26
        L_0x008a:
            int r2 = r20 + r5
            int r26 = r28.getUpPreLoadedCount()     // Catch:{ all -> 0x0131 }
            int r2 = r2 + r26
            r0 = r28
            android.view.View r4 = r0.getChildAt(r2)     // Catch:{ all -> 0x0131 }
        L_0x0098:
            r0 = r28
            boolean r11 = r0.mDataChanged     // Catch:{ all -> 0x0131 }
            if (r11 == 0) goto L_0x00a1
            r28.handleDataChanged()     // Catch:{ all -> 0x0131 }
        L_0x00a1:
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0131 }
            if (r2 != 0) goto L_0x00d4
            r28.resetList()     // Catch:{ all -> 0x0131 }
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r28
            r0.mBlockLayoutRequests = r2
            goto L_0x001e
        L_0x00b3:
            r0 = r28
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0131 }
            r26 = r0
            int r20 = r2 - r26
            if (r20 < 0) goto L_0x0098
            r0 = r20
            r1 = r25
            if (r0 >= r1) goto L_0x0098
            int r2 = r28.getUpPreLoadedCount()     // Catch:{ all -> 0x0131 }
            int r2 = r2 + r20
            r0 = r28
            android.view.View r4 = r0.getChildAt(r2)     // Catch:{ all -> 0x0131 }
            goto L_0x0098
        L_0x00d4:
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0131 }
            r0 = r28
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x0131 }
            r26 = r0
            int r26 = r26.getCount()     // Catch:{ all -> 0x0131 }
            r0 = r26
            if (r2 == r0) goto L_0x013d
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0131 }
            java.lang.StringBuilder r26 = new java.lang.StringBuilder     // Catch:{ all -> 0x0131 }
            r26.<init>()     // Catch:{ all -> 0x0131 }
            java.lang.String r27 = "The content of the adapter has changed but ListView did not receive a notification. Make sure the content of your adapter is not modified from a background thread, but only from the UI thread. [in ListView("
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0131 }
            int r27 = r28.getId()     // Catch:{ all -> 0x0131 }
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0131 }
            java.lang.String r27 = ", "
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0131 }
            java.lang.Class r27 = r28.getClass()     // Catch:{ all -> 0x0131 }
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0131 }
            java.lang.String r27 = ") with Adapter("
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0131 }
            r0 = r28
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x0131 }
            r27 = r0
            java.lang.Class r27 = r27.getClass()     // Catch:{ all -> 0x0131 }
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0131 }
            java.lang.String r27 = ")]"
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0131 }
            java.lang.String r26 = r26.toString()     // Catch:{ all -> 0x0131 }
            r0 = r26
            r2.<init>(r0)     // Catch:{ all -> 0x0131 }
            throw r2     // Catch:{ all -> 0x0131 }
        L_0x0131:
            r2 = move-exception
            if (r8 != 0) goto L_0x013c
            r26 = 0
            r0 = r26
            r1 = r28
            r1.mBlockLayoutRequests = r0
        L_0x013c:
            throw r2
        L_0x013d:
            r0 = r28
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0131 }
            r0 = r28
            r0.setSelectedPositionInt(r2)     // Catch:{ all -> 0x0131 }
            int r12 = r28.getFirstVisiblePosition()     // Catch:{ all -> 0x0131 }
            r0 = r28
            com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView$RecycleBin r0 = r0.mRecycler     // Catch:{ all -> 0x0131 }
            r23 = r0
            r14 = 0
            if (r11 == 0) goto L_0x0195
            int r13 = r28.getFirsVisibletChildIndex()     // Catch:{ all -> 0x0131 }
            int r19 = r13 + -1
        L_0x0159:
            if (r19 < 0) goto L_0x0175
            r0 = r28
            r1 = r19
            android.view.View r2 = r0.getChildAt(r1)     // Catch:{ all -> 0x0131 }
            int r26 = r28.getUpPreLoadedCount()     // Catch:{ all -> 0x0131 }
            int r26 = r19 - r26
            int r26 = r26 + r12
            r0 = r23
            r1 = r26
            r0.addScrapView(r2, r1)     // Catch:{ all -> 0x0131 }
            int r19 = r19 + -1
            goto L_0x0159
        L_0x0175:
            r19 = r13
        L_0x0177:
            r0 = r19
            if (r0 >= r10) goto L_0x019a
            r0 = r28
            r1 = r19
            android.view.View r2 = r0.getChildAt(r1)     // Catch:{ all -> 0x0131 }
            int r26 = r28.getUpPreLoadedCount()     // Catch:{ all -> 0x0131 }
            int r26 = r19 - r26
            int r26 = r26 + r12
            r0 = r23
            r1 = r26
            r0.addScrapView(r2, r1)     // Catch:{ all -> 0x0131 }
            int r19 = r19 + 1
            goto L_0x0177
        L_0x0195:
            r0 = r23
            r0.fillActiveViews(r10, r12)     // Catch:{ all -> 0x0131 }
        L_0x019a:
            android.view.View r18 = r28.getFocusedChild()     // Catch:{ all -> 0x0131 }
            if (r18 == 0) goto L_0x01ba
            if (r11 == 0) goto L_0x01ac
            r0 = r28
            r1 = r18
            boolean r2 = r0.isDirectChildHeaderOrFooter(r1)     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x01b7
        L_0x01ac:
            r14 = r18
            android.view.View r15 = r28.findFocus()     // Catch:{ all -> 0x0131 }
            if (r15 == 0) goto L_0x01b7
            r15.onStartTemporaryDetach()     // Catch:{ all -> 0x0131 }
        L_0x01b7:
            r28.requestFocus()     // Catch:{ all -> 0x0131 }
        L_0x01ba:
            r28.detachAllViewsFromParent()     // Catch:{ all -> 0x0131 }
            r23.removeSkippedScrap()     // Catch:{ all -> 0x0131 }
            r2 = 0
            r0 = r28
            r0.mUpPreLoadedCount = r2     // Catch:{ all -> 0x0131 }
            r2 = 0
            r0 = r28
            r0.mDownPreLoadedCount = r2     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r2 = r0.mLayoutMode     // Catch:{ all -> 0x0131 }
            switch(r2) {
                case 1: goto L_0x029e;
                case 2: goto L_0x0264;
                case 3: goto L_0x028d;
                case 4: goto L_0x02ae;
                case 5: goto L_0x0279;
                case 6: goto L_0x02c2;
                default: goto L_0x01d1;
            }     // Catch:{ all -> 0x0131 }
        L_0x01d1:
            if (r10 != 0) goto L_0x02ef
            r0 = r28
            boolean r2 = r0.mStackFromBottom     // Catch:{ all -> 0x0131 }
            if (r2 != 0) goto L_0x02ca
            r2 = 0
            r26 = 1
            r0 = r28
            r1 = r26
            int r22 = r0.lookForSelectablePosition(r2, r1)     // Catch:{ all -> 0x0131 }
            r0 = r28
            r1 = r22
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x0131 }
            r0 = r28
            android.view.View r24 = r0.fillFromTop(r6)     // Catch:{ all -> 0x0131 }
        L_0x01f1:
            r23.scrapActiveViews()     // Catch:{ all -> 0x0131 }
            if (r24 == 0) goto L_0x035d
            r0 = r28
            boolean r2 = r0.mItemsCanFocus     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x0353
            boolean r2 = r28.hasFocus()     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x0353
            boolean r2 = r24.hasFocus()     // Catch:{ all -> 0x0131 }
            if (r2 != 0) goto L_0x0353
            r0 = r24
            if (r0 != r14) goto L_0x0214
            if (r15 == 0) goto L_0x0214
            boolean r2 = r15.requestFocus()     // Catch:{ all -> 0x0131 }
            if (r2 != 0) goto L_0x021a
        L_0x0214:
            boolean r2 = r24.requestFocus()     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x0340
        L_0x021a:
            r16 = 1
        L_0x021c:
            if (r16 != 0) goto L_0x0344
            android.view.View r17 = r28.getFocusedChild()     // Catch:{ all -> 0x0131 }
            if (r17 == 0) goto L_0x0227
            r17.clearFocus()     // Catch:{ all -> 0x0131 }
        L_0x0227:
            r2 = -1
            r0 = r28
            r1 = r24
            r0.positionSelector(r2, r1)     // Catch:{ all -> 0x0131 }
        L_0x022f:
            if (r15 == 0) goto L_0x023a
            android.os.IBinder r2 = r15.getWindowToken()     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x023a
            r15.onFinishTemporaryDetach()     // Catch:{ all -> 0x0131 }
        L_0x023a:
            r2 = 0
            r0 = r28
            r0.mLayoutMode = r2     // Catch:{ all -> 0x0131 }
            r2 = 0
            r0 = r28
            r0.mDataChanged = r2     // Catch:{ all -> 0x0131 }
            r2 = 0
            r0 = r28
            r0.mNeedSync = r2     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0131 }
            r0 = r28
            r0.setNextSelectedPositionInt(r2)     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0131 }
            if (r2 <= 0) goto L_0x025b
            r28.checkSelectionChanged()     // Catch:{ all -> 0x0131 }
        L_0x025b:
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r28
            r0.mBlockLayoutRequests = r2
            goto L_0x001e
        L_0x0264:
            if (r4 == 0) goto L_0x0271
            int r2 = r4.getTop()     // Catch:{ all -> 0x0131 }
            r0 = r28
            android.view.View r24 = r0.fillFromSelection(r2, r6, r7)     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x0271:
            r0 = r28
            android.view.View r24 = r0.fillFromMiddle(r6, r7)     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x0279:
            r0 = r28
            int r2 = r0.mSyncPosition     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r0 = r0.mSpecificTop     // Catch:{ all -> 0x0131 }
            r26 = r0
            r0 = r28
            r1 = r26
            android.view.View r24 = r0.fillSpecific(r2, r1)     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x028d:
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0131 }
            int r2 = r2 + -1
            r0 = r28
            android.view.View r24 = r0.fillUp(r2, r7)     // Catch:{ all -> 0x0131 }
            r28.adjustViewsUpOrDown()     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x029e:
            r2 = 0
            r0 = r28
            r0.mFirstPosition = r2     // Catch:{ all -> 0x0131 }
            r0 = r28
            android.view.View r24 = r0.fillFromTop(r6)     // Catch:{ all -> 0x0131 }
            r28.adjustViewsUpOrDown()     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x02ae:
            int r2 = r28.reconcileSelectedPosition()     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r0 = r0.mSpecificTop     // Catch:{ all -> 0x0131 }
            r26 = r0
            r0 = r28
            r1 = r26
            android.view.View r24 = r0.fillSpecific(r2, r1)     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x02c2:
            r2 = r28
            android.view.View r24 = r2.moveSelection(r3, r4, r5, r6, r7)     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x02ca:
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0131 }
            int r2 = r2 + -1
            r26 = 0
            r0 = r28
            r1 = r26
            int r22 = r0.lookForSelectablePosition(r2, r1)     // Catch:{ all -> 0x0131 }
            r0 = r28
            r1 = r22
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0131 }
            int r2 = r2 + -1
            r0 = r28
            android.view.View r24 = r0.fillUp(r2, r7)     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x02ef:
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0131 }
            if (r2 < 0) goto L_0x0316
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0131 }
            r26 = r0
            r0 = r26
            if (r2 >= r0) goto L_0x0316
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0131 }
            if (r3 != 0) goto L_0x0311
        L_0x0309:
            r0 = r28
            android.view.View r24 = r0.fillSpecific(r2, r6)     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x0311:
            int r6 = r3.getTop()     // Catch:{ all -> 0x0131 }
            goto L_0x0309
        L_0x0316:
            r0 = r28
            int r2 = r0.mFirstPosition     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0131 }
            r26 = r0
            r0 = r26
            if (r2 >= r0) goto L_0x0337
            r0 = r28
            int r2 = r0.mFirstPosition     // Catch:{ all -> 0x0131 }
            if (r21 != 0) goto L_0x0332
        L_0x032a:
            r0 = r28
            android.view.View r24 = r0.fillSpecific(r2, r6)     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x0332:
            int r6 = r21.getTop()     // Catch:{ all -> 0x0131 }
            goto L_0x032a
        L_0x0337:
            r2 = 0
            r0 = r28
            android.view.View r24 = r0.fillSpecific(r2, r6)     // Catch:{ all -> 0x0131 }
            goto L_0x01f1
        L_0x0340:
            r16 = 0
            goto L_0x021c
        L_0x0344:
            r2 = 0
            r0 = r24
            r0.setSelected(r2)     // Catch:{ all -> 0x0131 }
            r0 = r28
            android.graphics.Rect r2 = r0.mSelectorRect     // Catch:{ all -> 0x0131 }
            r2.setEmpty()     // Catch:{ all -> 0x0131 }
            goto L_0x022f
        L_0x0353:
            r2 = -1
            r0 = r28
            r1 = r24
            r0.positionSelector(r2, r1)     // Catch:{ all -> 0x0131 }
            goto L_0x022f
        L_0x035d:
            r0 = r28
            int r2 = r0.mTouchMode     // Catch:{ all -> 0x0131 }
            if (r2 <= 0) goto L_0x0397
            r0 = r28
            int r2 = r0.mTouchMode     // Catch:{ all -> 0x0131 }
            r26 = 3
            r0 = r26
            if (r2 >= r0) goto L_0x0397
            r0 = r28
            int r2 = r0.mMotionPosition     // Catch:{ all -> 0x0131 }
            r0 = r28
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0131 }
            r26 = r0
            int r2 = r2 - r26
            r0 = r28
            android.view.View r9 = r0.getChildAt(r2)     // Catch:{ all -> 0x0131 }
            if (r9 == 0) goto L_0x038a
            r0 = r28
            int r2 = r0.mMotionPosition     // Catch:{ all -> 0x0131 }
            r0 = r28
            r0.positionSelector(r2, r9)     // Catch:{ all -> 0x0131 }
        L_0x038a:
            boolean r2 = r28.hasFocus()     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x022f
            if (r15 == 0) goto L_0x022f
            r15.requestFocus()     // Catch:{ all -> 0x0131 }
            goto L_0x022f
        L_0x0397:
            r0 = r28
            android.graphics.Rect r2 = r0.mSelectorRect     // Catch:{ all -> 0x0131 }
            r2.setEmpty()     // Catch:{ all -> 0x0131 }
            goto L_0x038a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.tvsdk.widget.ListView.layoutChildren():void");
    }

    public View fillFromSelection(int selectedTop, int childrenTop, int childrenBottom) {
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, selectedPosition);
        int bottomSelectionPixel = getBottomSelectionPixel(childrenBottom, fadingEdgeLength, selectedPosition);
        View sel = makeAndAddView(selectedPosition, selectedTop, true, this.mListPadding.left, true);
        if (sel.getBottom() > bottomSelectionPixel) {
            sel.offsetTopAndBottom(-Math.min(sel.getTop() - topSelectionPixel, sel.getBottom() - bottomSelectionPixel));
        } else if (sel.getTop() < topSelectionPixel) {
            sel.offsetTopAndBottom(Math.min(topSelectionPixel - sel.getTop(), bottomSelectionPixel - sel.getBottom()));
        }
        fillAboveAndBelow(sel, selectedPosition);
        if (!this.mStackFromBottom) {
            correctTooHigh(getVisibleChildCount());
        } else {
            correctTooLow(getVisibleChildCount());
        }
        return sel;
    }

    /* access modifiers changed from: protected */
    public View fillFromMiddle(int childrenTop, int childrenBottom) {
        int height = childrenBottom - childrenTop;
        int position = reconcileSelectedPosition();
        View sel = makeAndAddView(position, childrenTop, true, this.mListPadding.left, true);
        this.mFirstPosition = position;
        int selHeight = sel.getMeasuredHeight();
        if (selHeight <= height) {
            sel.offsetTopAndBottom((height - selHeight) / 2);
        }
        fillAboveAndBelow(sel, position);
        if (!this.mStackFromBottom) {
            correctTooHigh(getVisibleChildCount());
        } else {
            correctTooLow(getVisibleChildCount());
        }
        return sel;
    }

    /* access modifiers changed from: protected */
    public View fillSpecific(int position, int top) {
        View below;
        View above;
        boolean tempIsSelected = position == this.mSelectedPosition;
        View temp = makeAndAddView(position, top, true, this.mListPadding.left, tempIsSelected);
        this.mFirstPosition = position;
        int dividerHeight = this.mDividerHeight;
        if (!this.mStackFromBottom) {
            above = fillUp(position - 1, (temp.getTop() - dividerHeight) - this.mSpacing);
            adjustViewsUpOrDown();
            below = fillDown(position + 1, temp.getBottom() + dividerHeight + this.mSpacing);
            int visibleChildCount = getVisibleChildCount();
            if (visibleChildCount > 0) {
                correctTooHigh(visibleChildCount);
            }
        } else {
            below = fillDown(position + 1, temp.getBottom() + dividerHeight + this.mSpacing);
            adjustViewsUpOrDown();
            above = fillUp(position - 1, (temp.getTop() - dividerHeight) - this.mSpacing);
            int visibleChildCount2 = getVisibleChildCount();
            if (visibleChildCount2 > 0) {
                correctTooLow(visibleChildCount2);
            }
        }
        if (tempIsSelected) {
            return temp;
        }
        if (above != null) {
            return above;
        }
        return below;
    }

    /* access modifiers changed from: protected */
    public View fillUp(int pos, int nextBottom) {
        boolean selected;
        View selectedView = null;
        int end = 0;
        if ((getGroupFlags() & 34) == 34) {
            end = this.mListPadding.top;
        }
        while (nextBottom > end && pos >= 0) {
            if (this.mUpPreLoadedCount > 0) {
                nextBottom = (getChildAt(pos - getFirstPosition()).getTop() - this.mDividerHeight) - this.mSpacing;
                this.mUpPreLoadedCount--;
                pos--;
            } else {
                if (pos == this.mSelectedPosition) {
                    selected = true;
                } else {
                    selected = false;
                }
                View child = makeAndAddView(pos, nextBottom, false, this.mListPadding.left, selected);
                nextBottom = (child.getTop() - this.mDividerHeight) - this.mSpacing;
                if (selected) {
                    selectedView = child;
                }
                pos--;
            }
        }
        this.mFirstPosition = pos + 1;
        fillUpPreLoad();
        return selectedView;
    }

    /* access modifiers changed from: protected */
    public void fillUpPreLoad() {
        if (this.mPreLoadCount > 0 && this.mUpPreLoadedCount < this.mPreLoadCount) {
            int pos = getFirstPosition() - 1;
            int nextBottom = (getFirstChild().getTop() - this.mDividerHeight) - this.mSpacing;
            int preLoadPos = pos - (this.mPreLoadCount - this.mUpPreLoadedCount);
            while (pos > preLoadPos && pos >= 0) {
                nextBottom = (makeAndAddView(pos, nextBottom, false, this.mListPadding.left, false).getTop() - this.mDividerHeight) - this.mSpacing;
                pos--;
                this.mUpPreLoadedCount++;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void adjustViewsUpOrDown() {
        int delta;
        int visibleChildCount = getVisibleChildCount();
        if (visibleChildCount > 0) {
            if (!this.mStackFromBottom) {
                delta = getFirstVisibleChild().getTop() - this.mListPadding.top;
                if (this.mFirstPosition != 0) {
                    delta -= this.mDividerHeight - this.mSpacing;
                }
                if (delta < 0) {
                    delta = 0;
                }
            } else {
                int delta2 = getLastVisibleChild().getBottom() - (getHeight() - this.mListPadding.bottom);
                if (this.mFirstPosition + visibleChildCount < this.mItemCount) {
                    delta2 += this.mDividerHeight + this.mSpacing;
                }
                if (delta > 0) {
                    delta = 0;
                }
            }
            if (delta != 0) {
                offsetChildrenTopAndBottom(-delta);
            }
        }
    }

    /* access modifiers changed from: protected */
    public View fillFromTop(int nextTop) {
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mSelectedPosition);
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mItemCount - 1);
        if (this.mFirstPosition < 0) {
            this.mFirstPosition = 0;
        }
        return fillDown(this.mFirstPosition, nextTop);
    }

    public View moveSelection(View oldSel, View newSel, int delta, int childrenTop, int childrenBottom) {
        View sel;
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, selectedPosition);
        int bottomSelectionPixel = getBottomSelectionPixel(childrenTop, fadingEdgeLength, selectedPosition);
        if (delta > 0) {
            View oldSel2 = makeAndAddView(selectedPosition - 1, oldSel.getTop(), true, this.mListPadding.left, false);
            int dividerHeight = this.mDividerHeight;
            sel = makeAndAddView(selectedPosition, oldSel2.getBottom() + dividerHeight + this.mSpacing, true, this.mListPadding.left, true);
            if (sel.getBottom() > bottomSelectionPixel) {
                int offset = Math.min(Math.min(sel.getTop() - topSelectionPixel, sel.getBottom() - bottomSelectionPixel), (childrenBottom - childrenTop) / 2);
                oldSel2.offsetTopAndBottom(-offset);
                sel.offsetTopAndBottom(-offset);
            }
            if (!this.mStackFromBottom) {
                fillUp(this.mSelectedPosition - 2, (sel.getTop() - dividerHeight) - this.mSpacing);
                adjustViewsUpOrDown();
                fillDown(this.mSelectedPosition + 1, sel.getBottom() + dividerHeight + this.mSpacing);
            } else {
                fillDown(this.mSelectedPosition + 1, sel.getBottom() + dividerHeight + this.mSpacing);
                adjustViewsUpOrDown();
                fillUp(this.mSelectedPosition - 2, (sel.getTop() - dividerHeight) - this.mSpacing);
            }
        } else if (delta < 0) {
            if (newSel != null) {
                sel = makeAndAddView(selectedPosition, newSel.getTop(), true, this.mListPadding.left, true);
            } else {
                sel = makeAndAddView(selectedPosition, oldSel.getTop(), false, this.mListPadding.left, true);
            }
            if (sel.getTop() < topSelectionPixel) {
                sel.offsetTopAndBottom(Math.min(Math.min(topSelectionPixel - sel.getTop(), bottomSelectionPixel - sel.getBottom()), (childrenBottom - childrenTop) / 2));
            }
            fillAboveAndBelow(sel, selectedPosition);
        } else {
            int oldTop = oldSel.getTop();
            sel = makeAndAddView(selectedPosition, oldTop, true, this.mListPadding.left, true);
            if (oldTop < childrenTop && sel.getBottom() < childrenTop + 20) {
                sel.offsetTopAndBottom(childrenTop - sel.getTop());
            }
            fillAboveAndBelow(sel, selectedPosition);
        }
        return sel;
    }

    private int getTopSelectionPixel(int childrenTop, int fadingEdgeLength, int selectedPosition) {
        int topSelectionPixel = childrenTop;
        if (selectedPosition > 0) {
            return topSelectionPixel + fadingEdgeLength;
        }
        return topSelectionPixel;
    }

    private int getBottomSelectionPixel(int childrenBottom, int fadingEdgeLength, int selectedPosition) {
        int bottomSelectionPixel = childrenBottom;
        if (selectedPosition != this.mItemCount - 1) {
            return bottomSelectionPixel - fadingEdgeLength;
        }
        return bottomSelectionPixel;
    }

    private View fillDown(int pos, int nextTop) {
        View selectedView = null;
        int end = getBottom() - getTop();
        if ((getGroupFlags() & 34) == 34) {
            end -= this.mListPadding.bottom;
        }
        while (nextTop < end && pos < this.mItemCount) {
            if (this.mDownPreLoadedCount > 0) {
                nextTop = getChildAt(pos - getFirstPosition()).getBottom() + this.mDividerHeight + this.mSpacing;
                pos++;
                this.mDownPreLoadedCount--;
            } else {
                boolean selected = pos == this.mSelectedPosition;
                View child = makeAndAddView(pos, nextTop, true, this.mListPadding.left, selected);
                nextTop = child.getBottom() + this.mDividerHeight + this.mSpacing;
                if (selected) {
                    selectedView = child;
                }
                pos++;
            }
        }
        fillDownPreLoad();
        return selectedView;
    }

    private void fillDownPreLoad() {
        if (this.mPreLoadCount > 0 && this.mDownPreLoadedCount < this.mPreLoadCount) {
            int nextTop = getLastChild().getBottom() + this.mDividerHeight + this.mSpacing;
            int pos = getLastPosition() + 1;
            int preLoadPos = (this.mPreLoadCount + pos) - this.mDownPreLoadedCount;
            while (pos < preLoadPos && pos < this.mItemCount) {
                nextTop = makeAndAddView(pos, nextTop, true, this.mListPadding.left, pos == this.mSelectedPosition).getBottom() + this.mDividerHeight + this.mSpacing;
                pos++;
                this.mDownPreLoadedCount++;
            }
        }
    }

    private void fillAboveAndBelow(View sel, int position) {
        int dividerHeight = this.mDividerHeight;
        if (!this.mStackFromBottom) {
            fillUp(position - 1, (sel.getTop() - dividerHeight) - this.mSpacing);
            adjustViewsUpOrDown();
            fillDown(position + 1, sel.getBottom() + dividerHeight + this.mSpacing);
            return;
        }
        fillDown(position + 1, sel.getBottom() + dividerHeight + this.mSpacing);
        adjustViewsUpOrDown();
        fillUp(position - 1, sel.getTop() - dividerHeight);
    }

    private void correctTooHigh(int bisibleChildCount) {
        if (getLastVisiblePosition() == this.mItemCount - 1 && bisibleChildCount > 0) {
            int bottomOffset = ((getBottom() - getTop()) - this.mListPadding.bottom) - getLastVisibleChild().getBottom();
            View firstChild = getFirstVisibleChild();
            int firstTop = firstChild.getTop();
            if (bottomOffset <= 0) {
                return;
            }
            if (this.mFirstPosition > 0 || firstTop < this.mListPadding.top) {
                if (this.mFirstPosition == 0) {
                    bottomOffset = Math.min(bottomOffset, this.mListPadding.top - firstTop);
                }
                offsetChildrenTopAndBottom(bottomOffset);
                if (this.mFirstPosition > 0) {
                    fillUp(this.mFirstPosition - 1, (firstChild.getTop() - this.mDividerHeight) - this.mSpacing);
                    adjustViewsUpOrDown();
                }
            }
        }
    }

    private void correctTooLow(int bisibleChildCount) {
        if (this.mFirstPosition == 0 && bisibleChildCount > 0) {
            int firstTop = getFirstVisibleChild().getTop();
            int start = this.mListPadding.top;
            int end = (getBottom() - getTop()) - this.mListPadding.bottom;
            int topOffset = firstTop - start;
            View lastChild = getChildAt(getLastVisibleChildIndex());
            int lastBottom = lastChild.getBottom();
            int lastPosition = getLastVisiblePosition();
            if (topOffset <= 0) {
                return;
            }
            if (lastPosition < this.mItemCount - 1 || lastBottom > end) {
                if (lastPosition == this.mItemCount - 1) {
                    topOffset = Math.min(topOffset, lastBottom - end);
                }
                offsetChildrenTopAndBottom(-topOffset);
                if (lastPosition < this.mItemCount - 1) {
                    fillDown(lastPosition + 1, lastChild.getBottom() + this.mDividerHeight + this.mSpacing);
                    adjustViewsUpOrDown();
                }
            } else if (lastPosition == this.mItemCount - 1) {
                adjustViewsUpOrDown();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count;
        int widthSize;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize2 = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int childWidth = 0;
        int childHeight = 0;
        int childState = 0;
        if (this.mAdapter == null) {
            count = 0;
        } else {
            count = this.mAdapter.getCount();
        }
        this.mItemCount = count;
        if (this.mItemCount > 0 && (widthMode == 0 || heightMode == 0)) {
            View child = obtainView(0, this.mIsScrap);
            measureScrapChild(child, 0, widthMeasureSpec);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            childState = combineMeasuredStates(0, child.getMeasuredState());
            if (recycleOnMeasure() && this.mRecycler.shouldRecycleViewType(((AbsBaseListView.LayoutParams) child.getLayoutParams()).viewType)) {
                this.mRecycler.addScrapView(child, -1);
            }
        }
        if (widthMode == 0) {
            widthSize = this.mListPadding.left + this.mListPadding.right + childWidth + getVerticalScrollbarWidth();
        } else {
            widthSize = widthSize2 | (-16777216 & childState);
        }
        if (heightMode == 0) {
            heightSize = this.mListPadding.top + this.mListPadding.bottom + childHeight + (getVerticalFadingEdgeLength() * 2);
        }
        if (heightMode == Integer.MIN_VALUE && isMeasueHeightOfChildren()) {
            heightSize = measureHeightOfChildren(widthMeasureSpec, 0, -1, heightSize, -1);
        }
        setMeasuredDimension(widthSize, heightSize);
        this.mWidthMeasureSpec = widthMeasureSpec;
    }

    /* access modifiers changed from: protected */
    public boolean isMeasueHeightOfChildren() {
        return true;
    }

    private View makeAndAddView(int position, int y, boolean flow, int childrenLeft, boolean selected) {
        View child;
        if (this.mDataChanged || (child = this.mRecycler.getActiveView(position)) == null) {
            View child2 = obtainView(position, this.mIsScrap);
            setupChild(child2, position, y, flow, childrenLeft, selected, this.mIsScrap[0]);
            return child2;
        }
        setupChild(child, position, y, flow, childrenLeft, selected, true);
        return child;
    }

    private void setupChild(View child, int position, int y, boolean flowDown, int childrenLeft, boolean selected, boolean recycled) {
        int childHeightSpec;
        boolean isSelected = selected && shouldShowSelector();
        boolean updateChildSelected = isSelected != child.isSelected();
        int mode = this.mTouchMode;
        boolean isPressed = mode > 0 && mode < 3 && this.mMotionPosition == position;
        boolean updateChildPressed = isPressed != child.isPressed();
        boolean needToMeasure = !recycled || updateChildSelected || child.isLayoutRequested();
        AbsBaseListView.LayoutParams p = (AbsBaseListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsBaseListView.LayoutParams) generateDefaultLayoutParams();
        }
        p.viewType = this.mAdapter.getItemViewType(position);
        if ((!recycled || p.forceAdd) && (!p.recycledHeaderFooter || p.viewType != -2)) {
            p.forceAdd = false;
            if (p.viewType == -2) {
                p.recycledHeaderFooter = true;
            }
            addViewInLayout(child, flowDown ? -1 : 0, p, true);
        } else {
            attachViewToParent(child, flowDown ? -1 : 0, p);
        }
        if (updateChildSelected) {
            child.setSelected(isSelected);
        }
        if (updateChildPressed) {
            child.setPressed(isPressed);
        }
        if (!(this.mChoiceMode == 0 || this.mCheckStates == null)) {
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(this.mCheckStates.get(position));
            } else if (getContext().getApplicationInfo().targetSdkVersion >= 11) {
                child.setActivated(this.mCheckStates.get(position));
            }
        }
        if (needToMeasure) {
            int childWidthSpec = getChildMeasureSpec(this.mWidthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
            int lpHeight = p.height;
            if (lpHeight > 0) {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, UCCore.VERIFY_POLICY_QUICK);
            } else {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            }
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childTop = flowDown ? y : y - h;
        if (needToMeasure) {
            child.layout(childrenLeft, childTop, childrenLeft + w, childTop + h);
        } else {
            child.offsetLeftAndRight(childrenLeft - child.getLeft());
            child.offsetTopAndBottom(childTop - child.getTop());
        }
        if (this.mCachingStarted && !child.isDrawingCacheEnabled()) {
            child.setDrawingCacheEnabled(true);
        }
        if (recycled && ((AbsBaseListView.LayoutParams) child.getLayoutParams()).scrappedFromPosition != position) {
            child.jumpDrawablesToCurrentState();
        }
    }

    private void measureScrapChild(View child, int position, int widthMeasureSpec) {
        int childHeightSpec;
        AbsBaseListView.LayoutParams p = (AbsBaseListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsBaseListView.LayoutParams) generateDefaultLayoutParams();
            child.setLayoutParams(p);
        }
        p.viewType = this.mAdapter.getItemViewType(position);
        p.forceAdd = true;
        int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, UCCore.VERIFY_POLICY_QUICK);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /* access modifiers changed from: protected */
    public boolean recycleOnMeasure() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public final int measureHeightOfChildren(int widthMeasureSpec, int startPosition, int endPosition, int maxHeight, int disallowPartialChildPosition) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null) {
            return this.mListPadding.top + this.mListPadding.bottom;
        }
        int returnedHeight = this.mListPadding.top + this.mListPadding.bottom;
        int prevHeightWithoutPartialChild = 0;
        if (endPosition == -1) {
            endPosition = adapter.getCount() - 1;
        }
        AbsBaseListView.RecycleBin recycleBin = this.mRecycler;
        boolean recyle = recycleOnMeasure();
        boolean[] isScrap = this.mIsScrap;
        int i = startPosition;
        while (i <= endPosition) {
            View child = obtainView(i, isScrap);
            measureScrapChild(child, i, widthMeasureSpec);
            if (i > 0) {
                returnedHeight += 0;
            }
            if (recyle && recycleBin.shouldRecycleViewType(((AbsBaseListView.LayoutParams) child.getLayoutParams()).viewType)) {
                recycleBin.addScrapView(child, -1);
            }
            returnedHeight += child.getMeasuredHeight();
            if (returnedHeight < maxHeight) {
                if (disallowPartialChildPosition >= 0 && i >= disallowPartialChildPosition) {
                    prevHeightWithoutPartialChild = returnedHeight;
                }
                i++;
            } else if (disallowPartialChildPosition < 0 || i <= disallowPartialChildPosition || prevHeightWithoutPartialChild <= 0 || returnedHeight == maxHeight) {
                return maxHeight;
            } else {
                return prevHeightWithoutPartialChild;
            }
        }
        return returnedHeight;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handled = super.dispatchKeyEvent(event);
        if (handled || getFocusedChild() == null || event.getAction() != 0) {
            return handled;
        }
        return onKeyDown(event.getKeyCode(), event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return commonKey(keyCode, 1, event);
    }

    private boolean commonKey(int keyCode, int count, KeyEvent event) {
        if (this.mAdapter == null || !this.mIsAttached) {
            return false;
        }
        if (this.mDataChanged) {
            layoutChildren();
        }
        boolean handled = false;
        int action = event.getAction();
        int navigation = 1;
        if (action != 1) {
            switch (keyCode) {
                case 19:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded();
                        if (!handled) {
                            while (true) {
                                int count2 = count;
                                count = count2 - 1;
                                if (count2 > 0 && arrowScroll(33)) {
                                    handled = true;
                                }
                            }
                        }
                    } else if (event.hasModifiers(2)) {
                        if (resurrectSelectionIfNeeded() || fullScroll(33)) {
                            handled = true;
                        } else {
                            handled = false;
                        }
                    }
                    navigation = 2;
                    break;
                case 20:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded();
                        if (!handled) {
                            while (true) {
                                int count3 = count;
                                count = count3 - 1;
                                if (count3 > 0 && arrowScroll(130)) {
                                    handled = true;
                                }
                            }
                        }
                    } else if (event.hasModifiers(2)) {
                        if (resurrectSelectionIfNeeded() || fullScroll(130)) {
                            handled = true;
                        } else {
                            handled = false;
                        }
                    }
                    navigation = 4;
                    break;
                case 21:
                    if (event.hasNoModifiers()) {
                        handled = handleHorizontalFocusWithinListItem(17);
                        break;
                    }
                    break;
                case 22:
                    if (event.hasNoModifiers()) {
                        handled = handleHorizontalFocusWithinListItem(66);
                    }
                    navigation = 3;
                    break;
                case 23:
                case 66:
                    if (event.hasNoModifiers() && !(handled = resurrectSelectionIfNeeded()) && event.getRepeatCount() == 0 && getVisibleChildCount() > 0) {
                        keyPressed();
                        handled = true;
                        break;
                    }
                case 92:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || pageScroll(33);
                    } else if (event.hasModifiers(2)) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(33);
                    }
                    navigation = 2;
                    break;
                case 93:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || pageScroll(130);
                    } else if (event.hasModifiers(2)) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(130);
                    }
                    navigation = 4;
                    break;
                case SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE:
                    if (event.hasNoModifiers()) {
                        if (!resurrectSelectionIfNeeded() && !fullScroll(33)) {
                            handled = false;
                            break;
                        } else {
                            handled = true;
                            break;
                        }
                    }
                    break;
                case 123:
                    if (event.hasNoModifiers()) {
                        if (!resurrectSelectionIfNeeded() && !fullScroll(130)) {
                            handled = false;
                            break;
                        } else {
                            handled = true;
                            break;
                        }
                    }
                    break;
            }
        }
        if (handled) {
            playSoundEffect(navigation);
            return true;
        }
        switch (action) {
            case 0:
                return super.onKeyDown(keyCode, event);
            case 1:
                return super.onKeyUp(keyCode, event);
            case 2:
                return super.onKeyMultiple(keyCode, count, event);
            default:
                return false;
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public boolean arrowScroll(int direction) {
        try {
            this.mInLayout = true;
            boolean handled = arrowScrollImpl(direction);
            if (handled) {
                playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            }
            this.mInLayout = false;
            return handled;
        } catch (Throwable th) {
            this.mInLayout = false;
            throw th;
        }
    }

    private static class ArrowScrollFocusResult {
        private int mAmountToScroll;
        private int mSelectedPosition;

        private ArrowScrollFocusResult() {
        }

        /* access modifiers changed from: package-private */
        public void populate(int selectedPosition, int amountToScroll) {
            this.mSelectedPosition = selectedPosition;
            this.mAmountToScroll = amountToScroll;
        }

        public int getSelectedPosition() {
            return this.mSelectedPosition;
        }

        public int getAmountToScroll() {
            return this.mAmountToScroll;
        }
    }

    private boolean arrowScrollImpl(int direction) {
        boolean needToRedraw;
        boolean z;
        View focused;
        if (getVisibleChildCount() <= 0) {
            return false;
        }
        View selectedView = getSelectedView();
        int selectedPos = this.mSelectedPosition;
        int nextSelectedPosition = lookForSelectablePositionOnScreen(direction);
        int amountToScroll = amountToScroll(direction, nextSelectedPosition);
        ArrowScrollFocusResult focusResult = this.mItemsCanFocus ? arrowScrollFocused(direction) : null;
        if (focusResult != null) {
            nextSelectedPosition = focusResult.getSelectedPosition();
            amountToScroll = focusResult.getAmountToScroll();
        }
        if (focusResult != null) {
            needToRedraw = true;
        } else {
            needToRedraw = false;
        }
        if (nextSelectedPosition != -1) {
            if (focusResult != null) {
                z = true;
            } else {
                z = false;
            }
            handleNewSelectionChange(selectedView, direction, nextSelectedPosition, z);
            setSelectedPositionInt(nextSelectedPosition);
            setNextSelectedPositionInt(nextSelectedPosition);
            selectedView = getSelectedView();
            selectedPos = nextSelectedPosition;
            if (this.mItemsCanFocus && focusResult == null && (focused = getFocusedChild()) != null) {
                focused.clearFocus();
            }
            needToRedraw = true;
            checkSelectionChanged();
        }
        if (amountToScroll > 0) {
            if (direction != 33) {
                amountToScroll = -amountToScroll;
            }
            scrollListItemsBy(amountToScroll);
            needToRedraw = true;
        }
        if (this.mItemsCanFocus && focusResult == null && selectedView != null && selectedView.hasFocus()) {
            View focused2 = selectedView.findFocus();
            if (!isViewAncestorOf(focused2, this) || distanceToView(focused2) > 0) {
                focused2.clearFocus();
            }
        }
        if (nextSelectedPosition == -1 && selectedView != null && !isViewAncestorOf(selectedView, this)) {
            selectedView = null;
            hideSelector();
            this.mResurrectToPosition = -1;
        }
        if (!needToRedraw) {
            return false;
        }
        if (selectedView != null) {
            positionSelector(selectedPos, selectedView);
        }
        if (!awakenScrollBars()) {
            invalidate();
        }
        return true;
    }

    private void scrollListItemsBy(int amount) {
        int lastVisiblePosition;
        offsetChildrenTopAndBottom(amount);
        int listBottom = getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        AbsBaseListView.RecycleBin recycleBin = this.mRecycler;
        if (amount < 0) {
            int visibleChildCount = getVisibleChildCount();
            View last = getLastVisibleChild();
            while (last.getBottom() < listBottom && (lastVisiblePosition = getLastVisiblePosition()) < this.mItemCount - 1) {
                last = addViewBelow(last, lastVisiblePosition);
                visibleChildCount++;
            }
            if (last.getBottom() < listBottom) {
                offsetChildrenTopAndBottom(listBottom - last.getBottom());
            }
            View firstVisibleChild = getFirstVisibleChild();
            while (firstVisibleChild.getBottom() < listTop) {
                View firstChild = getFirstChild();
                if (recycleBin.shouldRecycleViewType(((AbsBaseListView.LayoutParams) firstVisibleChild.getLayoutParams()).viewType)) {
                    detachViewFromParent(firstChild);
                    recycleBin.addScrapView(firstChild, getFirstPosition());
                } else {
                    removeViewInLayout(firstChild);
                }
                firstVisibleChild = getFirstVisibleChild();
                this.mFirstPosition++;
            }
            return;
        }
        View first = getFirstVisibleChild();
        while (first.getTop() > listTop && this.mFirstPosition > 0) {
            first = addViewAbove(first, this.mFirstPosition);
            this.mFirstPosition--;
        }
        if (first.getTop() > listTop) {
            offsetChildrenTopAndBottom(listTop - first.getTop());
        }
        View lastVisibleChild = getLastVisibleChild();
        while (lastVisibleChild.getTop() > listBottom) {
            View lastChild = getLastChild();
            if (recycleBin.shouldRecycleViewType(((AbsBaseListView.LayoutParams) lastVisibleChild.getLayoutParams()).viewType)) {
                detachViewFromParent(lastChild);
                recycleBin.addScrapView(lastChild, getLastPosition());
            } else {
                removeViewInLayout(lastChild);
            }
            lastVisibleChild = getLastVisibleChild();
        }
    }

    private View addViewAbove(View theView, int position) {
        int abovePosition = (position - 1) - this.mUpPreLoadedCount;
        View view = obtainView(abovePosition, this.mIsScrap);
        if (view != null) {
            setupChild(view, abovePosition, (theView.getTop() - this.mDividerHeight) - this.mSpacing, false, this.mListPadding.left, false, this.mIsScrap[0]);
            return view;
        }
        this.mUpPreLoadedCount--;
        return getFirstChild();
    }

    private View addViewBelow(View theView, int position) {
        int belowPosition = position + 1 + this.mDownPreLoadedCount;
        View view = obtainView(belowPosition, this.mIsScrap);
        if (view != null) {
            setupChild(view, belowPosition, getLastChild().getBottom() + this.mDividerHeight + this.mSpacing, true, this.mListPadding.left, false, this.mIsScrap[0]);
            return view;
        }
        this.mDownPreLoadedCount--;
        return getLastChild();
    }

    private int distanceToView(View descendant) {
        descendant.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        int listBottom = (getBottom() - getTop()) - this.mListPadding.bottom;
        if (this.mTempRect.bottom < this.mListPadding.top) {
            return this.mListPadding.top - this.mTempRect.bottom;
        }
        if (this.mTempRect.top > listBottom) {
            return this.mTempRect.top - listBottom;
        }
        return 0;
    }

    private boolean isViewAncestorOf(View child, View parent) {
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        if (!(theParent instanceof ViewGroup) || !isViewAncestorOf((View) theParent, parent)) {
            return false;
        }
        return true;
    }

    private void handleNewSelectionChange(View selectedView, int direction, int newSelectedPosition, boolean newFocusAssigned) {
        int topViewIndex;
        int bottomViewIndex;
        View topView;
        View bottomView;
        if (newSelectedPosition == -1) {
            throw new IllegalArgumentException("newSelectedPosition needs to be valid");
        }
        boolean topSelected = false;
        int selectedIndex = this.mSelectedPosition - this.mFirstPosition;
        int nextSelectedIndex = newSelectedPosition - this.mFirstPosition;
        if (direction == 33) {
            topViewIndex = nextSelectedIndex;
            bottomViewIndex = selectedIndex;
            topView = getChildAt(topViewIndex);
            bottomView = selectedView;
            topSelected = true;
        } else {
            topViewIndex = selectedIndex;
            bottomViewIndex = nextSelectedIndex;
            topView = selectedView;
            bottomView = getChildAt(bottomViewIndex);
        }
        int numChildren = getChildCount();
        if (topView != null) {
            topView.setSelected(!newFocusAssigned && topSelected);
            measureAndAdjustDown(topView, topViewIndex, numChildren);
        }
        if (bottomView != null) {
            bottomView.setSelected(!newFocusAssigned && !topSelected);
            measureAndAdjustDown(bottomView, bottomViewIndex, numChildren);
        }
    }

    private void measureAndAdjustDown(View child, int childIndex, int numChildren) {
        int oldHeight = child.getHeight();
        measureItem(child);
        if (child.getMeasuredHeight() != oldHeight) {
            relayoutMeasuredItem(child);
            int heightDelta = child.getMeasuredHeight() - oldHeight;
            for (int i = childIndex + 1; i < numChildren; i++) {
                getChildAt(i).offsetTopAndBottom(heightDelta);
            }
        }
    }

    private void measureItem(View child) {
        int childHeightSpec;
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(-1, -2);
        }
        int childWidthSpec = getChildMeasureSpec(this.mWidthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, UCCore.VERIFY_POLICY_QUICK);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private void relayoutMeasuredItem(View child) {
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childLeft = this.mListPadding.left;
        int childTop = child.getTop();
        child.layout(childLeft, childTop, childLeft + w, childTop + h);
    }

    private ArrowScrollFocusResult arrowScrollFocused(int direction) {
        int ySearchPoint;
        View newFocus;
        int ySearchPoint2;
        int selectablePosition;
        View selectedView = getSelectedView();
        if (selectedView == null || !selectedView.hasFocus()) {
            if (direction == 130) {
                int listTop = this.mListPadding.top + (this.mFirstPosition > 0 ? getArrowScrollPreviewLength() : 0);
                if (selectedView == null || selectedView.getTop() <= listTop) {
                    ySearchPoint2 = listTop;
                } else {
                    ySearchPoint2 = selectedView.getTop();
                }
                this.mTempRect.set(0, ySearchPoint2, 0, ySearchPoint2);
            } else {
                int listBottom = (getHeight() - this.mListPadding.bottom) - ((this.mFirstPosition + getChildCount()) + -1 < this.mItemCount ? getArrowScrollPreviewLength() : 0);
                if (selectedView == null || selectedView.getBottom() >= listBottom) {
                    ySearchPoint = listBottom;
                } else {
                    ySearchPoint = selectedView.getBottom();
                }
                this.mTempRect.set(0, ySearchPoint, 0, ySearchPoint);
            }
            newFocus = FocusFinder.getInstance().findNextFocusFromRect(this, this.mTempRect, direction);
        } else {
            newFocus = FocusFinder.getInstance().findNextFocus(this, selectedView.findFocus(), direction);
        }
        if (newFocus != null) {
            int positionOfNewFocus = positionOfNewFocus(newFocus);
            if (this.mSelectedPosition != -1 && positionOfNewFocus != this.mSelectedPosition && (selectablePosition = lookForSelectablePositionOnScreen(direction)) != -1 && ((direction == 130 && selectablePosition < positionOfNewFocus) || (direction == 33 && selectablePosition > positionOfNewFocus))) {
                return null;
            }
            int focusScroll = amountToScrollToNewFocus(direction, newFocus, positionOfNewFocus);
            int maxScrollAmount = getMaxScrollAmount();
            if (focusScroll < maxScrollAmount) {
                newFocus.requestFocus(direction);
                this.mArrowScrollFocusResult.populate(positionOfNewFocus, focusScroll);
                return this.mArrowScrollFocusResult;
            } else if (distanceToView(newFocus) < maxScrollAmount) {
                newFocus.requestFocus(direction);
                this.mArrowScrollFocusResult.populate(positionOfNewFocus, maxScrollAmount);
                return this.mArrowScrollFocusResult;
            }
        }
        return null;
    }

    private int amountToScrollToNewFocus(int direction, View newFocus, int positionOfNewFocus) {
        newFocus.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(newFocus, this.mTempRect);
        if (direction != 33) {
            int listBottom = getHeight() - this.mListPadding.bottom;
            if (this.mTempRect.bottom <= listBottom) {
                return 0;
            }
            int amountToScroll = this.mTempRect.bottom - listBottom;
            if (positionOfNewFocus < this.mItemCount - 1) {
                return amountToScroll + getArrowScrollPreviewLength();
            }
            return amountToScroll;
        } else if (this.mTempRect.top >= this.mListPadding.top) {
            return 0;
        } else {
            int amountToScroll2 = this.mListPadding.top - this.mTempRect.top;
            return positionOfNewFocus > 0 ? amountToScroll2 + getArrowScrollPreviewLength() : amountToScroll2;
        }
    }

    private int positionOfNewFocus(View newFocus) {
        int numChildren = getChildCount();
        for (int i = 0; i < numChildren; i++) {
            if (isViewAncestorOf(newFocus, getChildAt(i))) {
                return this.mFirstPosition + i;
            }
        }
        throw new IllegalArgumentException("newFocus is not a child of any of the children of the list!");
    }

    /* access modifiers changed from: protected */
    public int lookForSelectablePositionOnScreen(int direction) {
        int startPos;
        int startPos2;
        int firstPosition = getFirstVisiblePosition();
        if (direction == 130) {
            if (this.mSelectedPosition != -1) {
                startPos2 = this.mSelectedPosition + 1;
            } else {
                startPos2 = firstPosition;
            }
            if (startPos2 >= this.mAdapter.getCount()) {
                return -1;
            }
            if (startPos2 < firstPosition) {
                startPos2 = firstPosition;
            }
            int lastVisiblePos = getLastVisiblePosition();
            ListAdapter adapter = getAdapter();
            for (int pos = startPos2; pos <= lastVisiblePos; pos++) {
                if (adapter.isEnabled(pos) && getChildAt(pos - firstPosition).getVisibility() == 0) {
                    return pos;
                }
            }
        } else {
            int last = getLastVisiblePosition();
            if (this.mSelectedPosition != -1) {
                startPos = this.mSelectedPosition - 1;
            } else {
                startPos = last;
            }
            if (startPos < 0 || startPos >= this.mAdapter.getCount()) {
                return -1;
            }
            if (startPos > last) {
                startPos = last;
            }
            ListAdapter adapter2 = getAdapter();
            for (int pos2 = startPos; pos2 >= firstPosition; pos2--) {
                if (adapter2.isEnabled(pos2) && getChildAt(pos2 - firstPosition).getVisibility() == 0) {
                    return pos2;
                }
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public int amountToScroll(int direction, int nextSelectedPosition) {
        int listBottom = getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        int numChildren = getVisibleChildCount();
        if (direction == 130) {
            int indexToMakeVisible = numChildren - 1;
            if (nextSelectedPosition != -1) {
                indexToMakeVisible = nextSelectedPosition - getFirstVisiblePosition();
            }
            int positionToMakeVisible = getFirstVisiblePosition() + indexToMakeVisible;
            View viewToMakeVisible = getChildAt(indexToMakeVisible);
            int goalBottom = listBottom;
            if (positionToMakeVisible < this.mItemCount - 1) {
                goalBottom -= getArrowScrollPreviewLength();
            }
            if (viewToMakeVisible.getBottom() <= goalBottom) {
                return 0;
            }
            if (nextSelectedPosition != -1 && goalBottom - viewToMakeVisible.getTop() >= getMaxScrollAmount()) {
                return 0;
            }
            int amountToScroll = viewToMakeVisible.getBottom() - goalBottom;
            if (this.mFirstPosition + numChildren == this.mItemCount) {
                amountToScroll = Math.min(amountToScroll, getChildAt(numChildren - 1).getBottom() - listBottom);
            }
            return Math.min(amountToScroll, getMaxScrollAmount());
        }
        int indexToMakeVisible2 = 0;
        if (nextSelectedPosition != -1) {
            indexToMakeVisible2 = nextSelectedPosition - getFirstVisiblePosition();
        }
        int positionToMakeVisible2 = getFirstVisiblePosition() + indexToMakeVisible2;
        View viewToMakeVisible2 = getChildAt(indexToMakeVisible2);
        int goalTop = listTop;
        if (positionToMakeVisible2 > 0) {
            goalTop += getArrowScrollPreviewLength();
        }
        if (viewToMakeVisible2.getTop() >= goalTop) {
            return 0;
        }
        if (nextSelectedPosition != -1 && viewToMakeVisible2.getBottom() - goalTop >= getMaxScrollAmount()) {
            return 0;
        }
        int amountToScroll2 = goalTop - viewToMakeVisible2.getTop();
        if (getFirstVisiblePosition() == 0) {
            amountToScroll2 = Math.min(amountToScroll2, listTop - getChildAt(0).getTop());
        }
        return Math.min(amountToScroll2, getMaxScrollAmount());
    }

    /* access modifiers changed from: protected */
    public int getArrowScrollPreviewLength() {
        return Math.max(2, getVerticalFadingEdgeLength());
    }

    public int getMaxScrollAmount() {
        return (int) (0.33f * ((float) (getBottom() - getTop())));
    }

    /* access modifiers changed from: package-private */
    public boolean fullScroll(int direction) {
        boolean moved = false;
        if (direction == 33) {
            if (this.mSelectedPosition != 0) {
                int position = lookForSelectablePosition(0, true);
                if (position >= 0) {
                    this.mLayoutMode = 1;
                    setSelectionInt(position);
                }
                moved = true;
            }
        } else if (direction == 130 && this.mSelectedPosition < this.mItemCount - 1) {
            int position2 = lookForSelectablePosition(this.mItemCount - 1, true);
            if (position2 >= 0) {
                this.mLayoutMode = 3;
                setSelectionInt(position2);
            }
            moved = true;
        }
        if (moved && !awakenScrollBars()) {
            awakenScrollBars();
            invalidate();
        }
        return moved;
    }

    private boolean handleHorizontalFocusWithinListItem(int direction) {
        View selectedView;
        if (direction == 17 || direction == 66) {
            int numChildren = getVisibleChildCount();
            if (this.mItemsCanFocus && numChildren > 0 && this.mSelectedPosition != -1 && (selectedView = getSelectedView()) != null && selectedView.hasFocus() && (selectedView instanceof ViewGroup)) {
                View currentFocus = selectedView.findFocus();
                View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup) selectedView, currentFocus, direction);
                if (nextFocus != null) {
                    currentFocus.getFocusedRect(this.mTempRect);
                    offsetDescendantRectToMyCoords(currentFocus, this.mTempRect);
                    offsetRectIntoDescendantCoords(nextFocus, this.mTempRect);
                    if (nextFocus.requestFocus(direction, this.mTempRect)) {
                        return true;
                    }
                }
                View globalNextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup) getRootView(), currentFocus, direction);
                if (globalNextFocus != null) {
                    return isViewAncestorOf(globalNextFocus, this);
                }
            }
            return false;
        }
        throw new IllegalArgumentException("direction must be one of {View.FOCUS_LEFT, View.FOCUS_RIGHT}");
    }

    /* access modifiers changed from: package-private */
    public boolean pageScroll(int direction) {
        int position;
        int nextPage = -1;
        boolean down = false;
        if (direction == 33) {
            nextPage = Math.max(0, (this.mSelectedPosition - getVisibleChildCount()) - 1);
        } else if (direction == 130) {
            nextPage = Math.min(this.mItemCount - 1, (this.mSelectedPosition + getVisibleChildCount()) - 1);
            down = true;
        }
        if (nextPage < 0 || (position = lookForSelectablePosition(nextPage, down)) < 0) {
            return false;
        }
        this.mLayoutMode = 4;
        this.mSpecificTop = getPaddingTop() + getVerticalFadingEdgeLength();
        if (down && position > this.mItemCount - getVisibleChildCount()) {
            this.mLayoutMode = 3;
        }
        if (!down && position < getVisibleChildCount()) {
            this.mLayoutMode = 1;
        }
        setSelectionInt(position);
        if (awakenScrollBars()) {
            return true;
        }
        invalidate();
        return true;
    }

    public void setSelection(int position) {
        setSelectionFromTop(position, this.mListPadding.top);
    }

    public void setSelectionFromTop(int position, int y) {
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
                this.mSpecificTop = y;
                if (this.mNeedSync) {
                    this.mSyncPosition = position;
                    this.mSyncRowId = this.mAdapter.getItemId(position);
                }
                layoutChildren();
            }
        }
    }

    /* access modifiers changed from: protected */
    public int lookForSelectablePosition(int position, boolean lookDown) {
        int position2;
        ListAdapter adapter = this.mAdapter;
        if (adapter == null || isInTouchMode()) {
            return -1;
        }
        int count = adapter.getCount();
        if (!this.mAreAllItemsSelectable) {
            if (lookDown) {
                position2 = Math.max(0, position);
                while (position2 < count && !adapter.isEnabled(position2)) {
                    position2++;
                }
            } else {
                int position3 = Math.min(position, count - 1);
                while (position2 >= 0 && !adapter.isEnabled(position2)) {
                    position3 = position2 - 1;
                }
            }
            if (position2 < 0 || position2 >= count) {
                return -1;
            }
            return position2;
        } else if (position < 0 || position >= count) {
            return -1;
        } else {
            return position;
        }
    }

    /* access modifiers changed from: package-private */
    public int findMotionRow(int y) {
        int childCount = getVisibleChildCount();
        if (childCount > 0) {
            if (!this.mStackFromBottom) {
                for (int i = this.mUpPreLoadedCount; i < this.mUpPreLoadedCount + childCount; i++) {
                    if (y <= getChildAt(i).getBottom()) {
                        return this.mFirstPosition + i;
                    }
                }
            } else {
                for (int i2 = (this.mUpPreLoadedCount + childCount) - 1; i2 >= this.mUpPreLoadedCount; i2--) {
                    if (y >= getChildAt(i2).getTop()) {
                        return this.mFirstPosition + i2;
                    }
                }
            }
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public void detachOffScreenChildren(boolean isDown) {
        int childCount = getChildCount();
        int firstPosition = this.mFirstPosition;
        int count = 0;
        if (!isDown) {
            int bottom = getHeight() - getPaddingBottom();
            int firstVisibleIndex = getFirsVisibletChildIndex();
            int i = getLastVisibleChildIndex();
            while (i >= firstVisibleIndex) {
                int n = i;
                int i2 = i;
                View child = getChildAt(n);
                if (child != null) {
                    if (child.getTop() <= bottom) {
                        break;
                    }
                    int start = n;
                    count++;
                    int i3 = firstPosition + n;
                    if (this.mDownPreLoadedCount >= this.mPreLoadCount) {
                        View child2 = getLastChild();
                        int pos = getLastPosition();
                        detachViewFromParent(getChildCount() - 1);
                        this.mRecycler.addScrapView(child2, pos);
                    } else {
                        this.mDownPreLoadedCount++;
                    }
                    i--;
                } else {
                    ZpLogger.e(TAG, "ListView.detachOffScreenChildren.up.child == null");
                    return;
                }
            }
        } else {
            int top = getPaddingTop();
            int lastVisibleIndex = getLastVisibleChildIndex();
            int i4 = getFirsVisibletChildIndex();
            while (i4 <= lastVisibleIndex) {
                int n2 = i4;
                int i5 = i4;
                View child3 = getChildAt(n2);
                if (child3 == null) {
                    ZpLogger.e(TAG, "ListView.detachOffScreenChildren.down.child == null");
                    return;
                } else if (child3.getBottom() >= top) {
                    break;
                } else {
                    int start2 = n2;
                    count++;
                    int i6 = firstPosition + n2;
                    if (this.mUpPreLoadedCount >= this.mPreLoadCount) {
                        View child4 = getFirstChild();
                        int pos2 = getFirstPosition();
                        detachViewFromParent(0);
                        this.mRecycler.addScrapView(child4, pos2);
                    } else {
                        this.mUpPreLoadedCount++;
                    }
                    i4++;
                }
            }
        }
        if (isDown) {
            this.mFirstPosition += count;
        }
    }

    public View getSelectedView() {
        if (this.mItemCount > 0 && this.mSelectedPosition >= 0) {
            return getChildAt(this.mSelectedPosition - getFirstPosition());
        }
        ZpLogger.e(TAG, "getSelectedView: return null! this:" + toString() + ", mItemCount:" + this.mItemCount + ", mSelectedPosition:" + this.mSelectedPosition);
        return null;
    }
}
