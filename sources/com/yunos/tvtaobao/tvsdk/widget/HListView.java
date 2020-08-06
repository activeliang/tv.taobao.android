package com.yunos.tvtaobao.tvsdk.widget;

import android.annotation.SuppressLint;
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

public class HListView extends AbsHListView {
    private static final int MIN_SCROLL_PREVIEW_PIXELS = 2;
    private final ArrowScrollFocusResult mArrowScrollFocusResult = new ArrowScrollFocusResult();
    Drawable mDivider;
    private boolean mDividerIsOpaque;
    int mDividerWidth;
    protected int mSpecificLeft;

    public HListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HListView(Context context) {
        super(context);
    }

    /* access modifiers changed from: package-private */
    public void fillGap(boolean isRight) {
        int startOffset;
        int count = getChildCount();
        if (isRight) {
            int paddingLeft = 0;
            if ((getGroupFlags() & 34) == 34) {
                paddingLeft = getListPaddingLeft();
            }
            if (count > 0) {
                startOffset = getChildAt(count - 1).getRight() + this.mDividerWidth + this.mSpacing;
            } else {
                startOffset = paddingLeft;
            }
            fillRight(this.mFirstPosition + count, startOffset);
            correctTooWide(getChildCount());
            return;
        }
        int paddingRight = 0;
        if ((getGroupFlags() & 34) == 34) {
            paddingRight = getListPaddingRight();
        }
        fillLeft(this.mFirstPosition - 1, count > 0 ? (getChildAt(0).getLeft() - this.mDividerWidth) - this.mSpacing : getWidth() - paddingRight);
        correctTooNarrow(getChildCount());
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        ListAdapter adapter = this.mAdapter;
        int closetChildIndex = -1;
        int closestChildLeft = 0;
        if (!(adapter == null || !gainFocus || previouslyFocusedRect == null)) {
            previouslyFocusedRect.offset(getScrollX(), getScrollY());
            if (adapter.getCount() < getChildCount() + this.mFirstPosition) {
                this.mLayoutMode = 0;
                layoutChildren();
            }
            Rect otherRect = this.mTempRect;
            int minDistance = Integer.MAX_VALUE;
            int childCount = getChildCount();
            int firstPosition = this.mFirstPosition;
            for (int i = 0; i < childCount; i++) {
                if (adapter.isEnabled(firstPosition + i)) {
                    View other = getChildAt(i);
                    other.getDrawingRect(otherRect);
                    offsetDescendantRectToMyCoords(other, otherRect);
                    int distance = getDistance(previouslyFocusedRect, otherRect, direction);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closetChildIndex = i;
                        closestChildLeft = other.getLeft();
                    }
                }
            }
        }
        if (closetChildIndex >= 0) {
            setSelectionFromLeft(this.mFirstPosition + closetChildIndex, closestChildLeft - this.mListPadding.left);
        }
    }

    public void setDivider(Drawable divider) {
        boolean z = false;
        if (divider != null) {
            this.mDividerWidth = divider.getIntrinsicWidth();
        } else {
            this.mDividerWidth = 0;
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
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0091 A[Catch:{ all -> 0x011e }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009a A[Catch:{ all -> 0x011e }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00c1 A[Catch:{ all -> 0x011e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutChildren() {
        /*
            r26 = this;
            r0 = r26
            boolean r8 = r0.mBlockLayoutRequests
            if (r8 != 0) goto L_0x001e
            r2 = 1
            r0 = r26
            r0.mBlockLayoutRequests = r2
            r26.invalidate()     // Catch:{ all -> 0x011e }
            r0 = r26
            android.widget.ListAdapter r2 = r0.mAdapter     // Catch:{ all -> 0x011e }
            if (r2 != 0) goto L_0x001f
            r26.resetList()     // Catch:{ all -> 0x011e }
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r26
            r0.mBlockLayoutRequests = r2
        L_0x001e:
            return
        L_0x001f:
            r0 = r26
            android.graphics.Rect r2 = r0.mListPadding     // Catch:{ all -> 0x011e }
            int r6 = r2.left     // Catch:{ all -> 0x011e }
            int r2 = r26.getRight()     // Catch:{ all -> 0x011e }
            int r24 = r26.getLeft()     // Catch:{ all -> 0x011e }
            int r2 = r2 - r24
            r0 = r26
            android.graphics.Rect r0 = r0.mListPadding     // Catch:{ all -> 0x011e }
            r24 = r0
            r0 = r24
            int r0 = r0.right     // Catch:{ all -> 0x011e }
            r24 = r0
            int r7 = r2 - r24
            int r10 = r26.getChildCount()     // Catch:{ all -> 0x011e }
            r19 = 0
            r5 = 0
            r3 = 0
            r20 = 0
            r4 = 0
            r14 = 0
            r0 = r26
            int r2 = r0.mLayoutMode     // Catch:{ all -> 0x011e }
            switch(r2) {
                case 2: goto L_0x00a6;
                case 3: goto L_0x0050;
                case 4: goto L_0x008b;
                case 5: goto L_0x008b;
                case 6: goto L_0x0050;
                case 7: goto L_0x008b;
                case 8: goto L_0x008b;
                default: goto L_0x0050;
            }     // Catch:{ all -> 0x011e }
        L_0x0050:
            r0 = r26
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x011e }
            r24 = r0
            int r19 = r2 - r24
            if (r19 < 0) goto L_0x006a
            r0 = r19
            if (r0 >= r10) goto L_0x006a
            r0 = r26
            r1 = r19
            android.view.View r3 = r0.getChildAt(r1)     // Catch:{ all -> 0x011e }
        L_0x006a:
            r2 = 0
            r0 = r26
            android.view.View r20 = r0.getChildAt(r2)     // Catch:{ all -> 0x011e }
            r0 = r26
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x011e }
            if (r2 < 0) goto L_0x0083
            r0 = r26
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x011e }
            r24 = r0
            int r5 = r2 - r24
        L_0x0083:
            int r2 = r19 + r5
            r0 = r26
            android.view.View r4 = r0.getChildAt(r2)     // Catch:{ all -> 0x011e }
        L_0x008b:
            r0 = r26
            boolean r11 = r0.mDataChanged     // Catch:{ all -> 0x011e }
            if (r11 == 0) goto L_0x0094
            r26.handleDataChanged()     // Catch:{ all -> 0x011e }
        L_0x0094:
            r0 = r26
            int r2 = r0.mItemCount     // Catch:{ all -> 0x011e }
            if (r2 != 0) goto L_0x00c1
            r26.resetList()     // Catch:{ all -> 0x011e }
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r26
            r0.mBlockLayoutRequests = r2
            goto L_0x001e
        L_0x00a6:
            r0 = r26
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x011e }
            r24 = r0
            int r19 = r2 - r24
            if (r19 < 0) goto L_0x008b
            r0 = r19
            if (r0 >= r10) goto L_0x008b
            r0 = r26
            r1 = r19
            android.view.View r4 = r0.getChildAt(r1)     // Catch:{ all -> 0x011e }
            goto L_0x008b
        L_0x00c1:
            r0 = r26
            int r2 = r0.mItemCount     // Catch:{ all -> 0x011e }
            r0 = r26
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x011e }
            r24 = r0
            int r24 = r24.getCount()     // Catch:{ all -> 0x011e }
            r0 = r24
            if (r2 == r0) goto L_0x012a
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch:{ all -> 0x011e }
            java.lang.StringBuilder r24 = new java.lang.StringBuilder     // Catch:{ all -> 0x011e }
            r24.<init>()     // Catch:{ all -> 0x011e }
            java.lang.String r25 = "The content of the adapter has changed but ListView did not receive a notification. Make sure the content of your adapter is not modified from a background thread, but only from the UI thread. [in ListView("
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ all -> 0x011e }
            int r25 = r26.getId()     // Catch:{ all -> 0x011e }
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ all -> 0x011e }
            java.lang.String r25 = ", "
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ all -> 0x011e }
            java.lang.Class r25 = r26.getClass()     // Catch:{ all -> 0x011e }
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ all -> 0x011e }
            java.lang.String r25 = ") with Adapter("
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ all -> 0x011e }
            r0 = r26
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x011e }
            r25 = r0
            java.lang.Class r25 = r25.getClass()     // Catch:{ all -> 0x011e }
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ all -> 0x011e }
            java.lang.String r25 = ")]"
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ all -> 0x011e }
            java.lang.String r24 = r24.toString()     // Catch:{ all -> 0x011e }
            r0 = r24
            r2.<init>(r0)     // Catch:{ all -> 0x011e }
            throw r2     // Catch:{ all -> 0x011e }
        L_0x011e:
            r2 = move-exception
            if (r8 != 0) goto L_0x0129
            r24 = 0
            r0 = r24
            r1 = r26
            r1.mBlockLayoutRequests = r0
        L_0x0129:
            throw r2
        L_0x012a:
            r0 = r26
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            r0.setSelectedPositionInt(r2)     // Catch:{ all -> 0x011e }
            r0 = r26
            int r12 = r0.mFirstPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView$RecycleBin r0 = r0.mRecycler     // Catch:{ all -> 0x011e }
            r22 = r0
            r13 = 0
            if (r11 == 0) goto L_0x015a
            r18 = 0
        L_0x0142:
            r0 = r18
            if (r0 >= r10) goto L_0x015f
            r0 = r26
            r1 = r18
            android.view.View r2 = r0.getChildAt(r1)     // Catch:{ all -> 0x011e }
            int r24 = r12 + r18
            r0 = r22
            r1 = r24
            r0.addScrapView(r2, r1)     // Catch:{ all -> 0x011e }
            int r18 = r18 + 1
            goto L_0x0142
        L_0x015a:
            r0 = r22
            r0.fillActiveViews(r10, r12)     // Catch:{ all -> 0x011e }
        L_0x015f:
            android.view.View r17 = r26.getFocusedChild()     // Catch:{ all -> 0x011e }
            if (r17 == 0) goto L_0x017f
            if (r11 == 0) goto L_0x0171
            r0 = r26
            r1 = r17
            boolean r2 = r0.isDirectChildHeaderOrFooter(r1)     // Catch:{ all -> 0x011e }
            if (r2 == 0) goto L_0x017c
        L_0x0171:
            r13 = r17
            android.view.View r14 = r26.findFocus()     // Catch:{ all -> 0x011e }
            if (r14 == 0) goto L_0x017c
            r14.onStartTemporaryDetach()     // Catch:{ all -> 0x011e }
        L_0x017c:
            r26.requestFocus()     // Catch:{ all -> 0x011e }
        L_0x017f:
            r26.detachAllViewsFromParent()     // Catch:{ all -> 0x011e }
            r22.removeSkippedScrap()     // Catch:{ all -> 0x011e }
            r0 = r26
            int r2 = r0.mLayoutMode     // Catch:{ all -> 0x011e }
            switch(r2) {
                case 2: goto L_0x021e;
                case 3: goto L_0x018c;
                case 4: goto L_0x0268;
                case 5: goto L_0x0233;
                case 6: goto L_0x027c;
                case 7: goto L_0x0258;
                case 8: goto L_0x0247;
                default: goto L_0x018c;
            }     // Catch:{ all -> 0x011e }
        L_0x018c:
            if (r10 != 0) goto L_0x02a9
            r0 = r26
            boolean r2 = r0.mStackFromBottom     // Catch:{ all -> 0x011e }
            if (r2 != 0) goto L_0x0284
            r2 = 0
            r24 = 1
            r0 = r26
            r1 = r24
            int r21 = r0.lookForSelectablePosition(r2, r1)     // Catch:{ all -> 0x011e }
            r0 = r26
            r1 = r21
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x011e }
            r0 = r26
            android.view.View r23 = r0.fillFromLeft(r6)     // Catch:{ all -> 0x011e }
        L_0x01ac:
            r22.scrapActiveViews()     // Catch:{ all -> 0x011e }
            if (r23 == 0) goto L_0x0316
            r0 = r26
            boolean r2 = r0.mItemsCanFocus     // Catch:{ all -> 0x011e }
            if (r2 == 0) goto L_0x030c
            boolean r2 = r26.hasFocus()     // Catch:{ all -> 0x011e }
            if (r2 == 0) goto L_0x030c
            boolean r2 = r23.hasFocus()     // Catch:{ all -> 0x011e }
            if (r2 != 0) goto L_0x030c
            r0 = r23
            if (r0 != r13) goto L_0x01cf
            if (r14 == 0) goto L_0x01cf
            boolean r2 = r14.requestFocus()     // Catch:{ all -> 0x011e }
            if (r2 != 0) goto L_0x01d5
        L_0x01cf:
            boolean r2 = r23.requestFocus()     // Catch:{ all -> 0x011e }
            if (r2 == 0) goto L_0x02fa
        L_0x01d5:
            r15 = 1
        L_0x01d6:
            if (r15 != 0) goto L_0x02fd
            android.view.View r16 = r26.getFocusedChild()     // Catch:{ all -> 0x011e }
            if (r16 == 0) goto L_0x01e1
            r16.clearFocus()     // Catch:{ all -> 0x011e }
        L_0x01e1:
            r2 = -1
            r0 = r26
            r1 = r23
            r0.positionSelector(r2, r1)     // Catch:{ all -> 0x011e }
        L_0x01e9:
            if (r14 == 0) goto L_0x01f4
            android.os.IBinder r2 = r14.getWindowToken()     // Catch:{ all -> 0x011e }
            if (r2 == 0) goto L_0x01f4
            r14.onFinishTemporaryDetach()     // Catch:{ all -> 0x011e }
        L_0x01f4:
            r2 = 0
            r0 = r26
            r0.mLayoutMode = r2     // Catch:{ all -> 0x011e }
            r2 = 0
            r0 = r26
            r0.mDataChanged = r2     // Catch:{ all -> 0x011e }
            r2 = 0
            r0 = r26
            r0.mNeedSync = r2     // Catch:{ all -> 0x011e }
            r0 = r26
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            r0.setNextSelectedPositionInt(r2)     // Catch:{ all -> 0x011e }
            r0 = r26
            int r2 = r0.mItemCount     // Catch:{ all -> 0x011e }
            if (r2 <= 0) goto L_0x0215
            r26.checkSelectionChanged()     // Catch:{ all -> 0x011e }
        L_0x0215:
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r26
            r0.mBlockLayoutRequests = r2
            goto L_0x001e
        L_0x021e:
            if (r4 == 0) goto L_0x022b
            int r2 = r4.getLeft()     // Catch:{ all -> 0x011e }
            r0 = r26
            android.view.View r23 = r0.fillFromSelection(r2, r6, r7)     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x022b:
            r0 = r26
            android.view.View r23 = r0.fillFromMiddle(r6, r7)     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x0233:
            r0 = r26
            int r2 = r0.mSyncPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            int r0 = r0.mSpecificLeft     // Catch:{ all -> 0x011e }
            r24 = r0
            r0 = r26
            r1 = r24
            android.view.View r23 = r0.fillSpecific(r2, r1)     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x0247:
            r0 = r26
            int r2 = r0.mItemCount     // Catch:{ all -> 0x011e }
            int r2 = r2 + -1
            r0 = r26
            android.view.View r23 = r0.fillLeft(r2, r7)     // Catch:{ all -> 0x011e }
            r26.adjustViewsLeftOrRight()     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x0258:
            r2 = 0
            r0 = r26
            r0.mFirstPosition = r2     // Catch:{ all -> 0x011e }
            r0 = r26
            android.view.View r23 = r0.fillFromLeft(r6)     // Catch:{ all -> 0x011e }
            r26.adjustViewsLeftOrRight()     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x0268:
            int r2 = r26.reconcileSelectedPosition()     // Catch:{ all -> 0x011e }
            r0 = r26
            int r0 = r0.mSpecificLeft     // Catch:{ all -> 0x011e }
            r24 = r0
            r0 = r26
            r1 = r24
            android.view.View r23 = r0.fillSpecific(r2, r1)     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x027c:
            r2 = r26
            android.view.View r23 = r2.moveSelection(r3, r4, r5, r6, r7)     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x0284:
            r0 = r26
            int r2 = r0.mItemCount     // Catch:{ all -> 0x011e }
            int r2 = r2 + -1
            r24 = 0
            r0 = r26
            r1 = r24
            int r21 = r0.lookForSelectablePosition(r2, r1)     // Catch:{ all -> 0x011e }
            r0 = r26
            r1 = r21
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x011e }
            r0 = r26
            int r2 = r0.mItemCount     // Catch:{ all -> 0x011e }
            int r2 = r2 + -1
            r0 = r26
            android.view.View r23 = r0.fillLeft(r2, r7)     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x02a9:
            r0 = r26
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x011e }
            if (r2 < 0) goto L_0x02d0
            r0 = r26
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            int r0 = r0.mItemCount     // Catch:{ all -> 0x011e }
            r24 = r0
            r0 = r24
            if (r2 >= r0) goto L_0x02d0
            r0 = r26
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x011e }
            if (r3 != 0) goto L_0x02cb
        L_0x02c3:
            r0 = r26
            android.view.View r23 = r0.fillSpecific(r2, r6)     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x02cb:
            int r6 = r3.getLeft()     // Catch:{ all -> 0x011e }
            goto L_0x02c3
        L_0x02d0:
            r0 = r26
            int r2 = r0.mFirstPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            int r0 = r0.mItemCount     // Catch:{ all -> 0x011e }
            r24 = r0
            r0 = r24
            if (r2 >= r0) goto L_0x02f1
            r0 = r26
            int r2 = r0.mFirstPosition     // Catch:{ all -> 0x011e }
            if (r20 != 0) goto L_0x02ec
        L_0x02e4:
            r0 = r26
            android.view.View r23 = r0.fillSpecific(r2, r6)     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x02ec:
            int r6 = r20.getLeft()     // Catch:{ all -> 0x011e }
            goto L_0x02e4
        L_0x02f1:
            r2 = 0
            r0 = r26
            android.view.View r23 = r0.fillSpecific(r2, r6)     // Catch:{ all -> 0x011e }
            goto L_0x01ac
        L_0x02fa:
            r15 = 0
            goto L_0x01d6
        L_0x02fd:
            r2 = 0
            r0 = r23
            r0.setSelected(r2)     // Catch:{ all -> 0x011e }
            r0 = r26
            android.graphics.Rect r2 = r0.mSelectorRect     // Catch:{ all -> 0x011e }
            r2.setEmpty()     // Catch:{ all -> 0x011e }
            goto L_0x01e9
        L_0x030c:
            r2 = -1
            r0 = r26
            r1 = r23
            r0.positionSelector(r2, r1)     // Catch:{ all -> 0x011e }
            goto L_0x01e9
        L_0x0316:
            r0 = r26
            int r2 = r0.mTouchMode     // Catch:{ all -> 0x011e }
            if (r2 <= 0) goto L_0x0350
            r0 = r26
            int r2 = r0.mTouchMode     // Catch:{ all -> 0x011e }
            r24 = 3
            r0 = r24
            if (r2 >= r0) goto L_0x0350
            r0 = r26
            int r2 = r0.mMotionPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x011e }
            r24 = r0
            int r2 = r2 - r24
            r0 = r26
            android.view.View r9 = r0.getChildAt(r2)     // Catch:{ all -> 0x011e }
            if (r9 == 0) goto L_0x0343
            r0 = r26
            int r2 = r0.mMotionPosition     // Catch:{ all -> 0x011e }
            r0 = r26
            r0.positionSelector(r2, r9)     // Catch:{ all -> 0x011e }
        L_0x0343:
            boolean r2 = r26.hasFocus()     // Catch:{ all -> 0x011e }
            if (r2 == 0) goto L_0x01e9
            if (r14 == 0) goto L_0x01e9
            r14.requestFocus()     // Catch:{ all -> 0x011e }
            goto L_0x01e9
        L_0x0350:
            r0 = r26
            android.graphics.Rect r2 = r0.mSelectorRect     // Catch:{ all -> 0x011e }
            r2.setEmpty()     // Catch:{ all -> 0x011e }
            goto L_0x0343
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.tvsdk.widget.HListView.layoutChildren():void");
    }

    /* access modifiers changed from: protected */
    public View fillFromSelection(int selectedLeft, int childrenLeft, int childrenRight) {
        int fadingEdgeLength = getHorizontalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int leftSelectionPixel = getLeftSelectionPixel(childrenLeft, fadingEdgeLength, selectedPosition);
        int rightSelectionPixel = getRightSelectionPixel(childrenRight, fadingEdgeLength, selectedPosition);
        View sel = makeAndAddView(selectedPosition, selectedLeft, true, this.mListPadding.top, true);
        if (sel.getRight() > rightSelectionPixel) {
            sel.offsetLeftAndRight(-Math.min(sel.getLeft() - leftSelectionPixel, sel.getRight() - rightSelectionPixel));
        } else if (sel.getLeft() < leftSelectionPixel) {
            sel.offsetLeftAndRight(Math.min(leftSelectionPixel - sel.getLeft(), rightSelectionPixel - sel.getRight()));
        }
        fillLeftAndRight(sel, selectedPosition);
        if (!this.mStackFromBottom) {
            correctTooWide(getChildCount());
        } else {
            correctTooNarrow(getChildCount());
        }
        return sel;
    }

    /* access modifiers changed from: protected */
    public View fillFromMiddle(int childrenLeft, int childrenRight) {
        int width = childrenRight - childrenLeft;
        int position = reconcileSelectedPosition();
        View sel = makeAndAddView(position, childrenLeft, true, this.mListPadding.top, true);
        this.mFirstPosition = position;
        int selWidth = sel.getMeasuredWidth();
        if (selWidth <= width) {
            sel.offsetLeftAndRight((width - selWidth) / 2);
        }
        fillLeftAndRight(sel, position);
        if (!this.mStackFromBottom) {
            correctTooWide(getChildCount());
        } else {
            correctTooNarrow(getChildCount());
        }
        return sel;
    }

    /* access modifiers changed from: protected */
    public View fillSpecific(int position, int left) {
        View rightTowards;
        View leftTowards;
        boolean tempIsSelected = position == this.mSelectedPosition;
        View temp = makeAndAddView(position, left, true, this.mListPadding.top, tempIsSelected);
        this.mFirstPosition = position;
        int dividerWidth = this.mDividerWidth;
        if (!this.mStackFromBottom) {
            leftTowards = fillLeft(position - 1, (temp.getLeft() - dividerWidth) - this.mSpacing);
            adjustViewsLeftOrRight();
            rightTowards = fillRight(position + 1, temp.getRight() + dividerWidth + this.mSpacing);
            int childCount = getChildCount();
            if (childCount > 0) {
                correctTooWide(childCount);
            }
        } else {
            rightTowards = fillRight(position + 1, temp.getRight() + dividerWidth + this.mSpacing);
            adjustViewsLeftOrRight();
            leftTowards = fillLeft(position - 1, (temp.getLeft() - dividerWidth) - this.mSpacing);
            int childCount2 = getChildCount();
            if (childCount2 > 0) {
                correctTooNarrow(childCount2);
            }
        }
        if (tempIsSelected) {
            return temp;
        }
        if (leftTowards != null) {
            return leftTowards;
        }
        return rightTowards;
    }

    /* access modifiers changed from: protected */
    public View fillLeft(int pos, int nextRight) {
        boolean selected;
        View selectedView = null;
        int end = 0;
        if ((getGroupFlags() & 34) == 34) {
            end = this.mListPadding.top;
        }
        while (nextRight > end && pos >= 0) {
            if (pos == this.mSelectedPosition) {
                selected = true;
            } else {
                selected = false;
            }
            View child = makeAndAddView(pos, nextRight, false, this.mListPadding.top, selected);
            nextRight = (child.getLeft() - this.mDividerWidth) - this.mSpacing;
            if (selected) {
                selectedView = child;
            }
            pos--;
        }
        this.mFirstPosition = pos + 1;
        return selectedView;
    }

    /* access modifiers changed from: protected */
    public void adjustViewsLeftOrRight() {
        int delta;
        int childCount = getChildCount();
        if (childCount > 0) {
            if (!this.mStackFromBottom) {
                delta = getChildAt(0).getLeft() - this.mListPadding.left;
                if (this.mFirstPosition != 0) {
                    delta -= this.mDividerWidth - this.mSpacing;
                }
                if (delta < 0) {
                    delta = 0;
                }
            } else {
                int delta2 = getChildAt(childCount - 1).getRight() - (getWidth() - this.mListPadding.right);
                if (this.mFirstPosition + childCount < this.mItemCount) {
                    delta2 += this.mDividerWidth + this.mSpacing;
                }
                if (delta > 0) {
                    delta = 0;
                }
            }
            if (delta != 0) {
                offsetChildrenLeftAndRight(-delta);
            }
        }
    }

    /* access modifiers changed from: protected */
    public View fillFromLeft(int nextLeft) {
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mSelectedPosition);
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mItemCount - 1);
        if (this.mFirstPosition < 0) {
            this.mFirstPosition = 0;
        }
        return fillRight(this.mFirstPosition, nextLeft);
    }

    /* access modifiers changed from: protected */
    public View moveSelection(View oldSel, View newSel, int delta, int childrenLeft, int childrenRight) {
        View sel;
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int leftSelectionPixel = getLeftSelectionPixel(childrenLeft, fadingEdgeLength, selectedPosition);
        int rightSelectionPixel = getRightSelectionPixel(childrenLeft, fadingEdgeLength, selectedPosition);
        if (delta > 0) {
            View oldSel2 = makeAndAddView(selectedPosition - 1, oldSel.getLeft(), true, this.mListPadding.top, false);
            int dividerWidth = this.mDividerWidth;
            sel = makeAndAddView(selectedPosition, oldSel2.getRight() + dividerWidth + this.mSpacing, true, this.mListPadding.top, true);
            if (sel.getRight() > rightSelectionPixel) {
                int offset = Math.min(Math.min(sel.getLeft() - leftSelectionPixel, sel.getRight() - rightSelectionPixel), (childrenRight - childrenLeft) / 2);
                oldSel2.offsetLeftAndRight(-offset);
                sel.offsetLeftAndRight(-offset);
            }
            if (!this.mStackFromBottom) {
                fillLeft(this.mSelectedPosition - 2, (sel.getLeft() - dividerWidth) - this.mSpacing);
                adjustViewsLeftOrRight();
                fillRight(this.mSelectedPosition + 1, sel.getRight() + dividerWidth + this.mSpacing);
            } else {
                fillRight(this.mSelectedPosition + 1, sel.getRight() + dividerWidth + this.mSpacing);
                adjustViewsLeftOrRight();
                fillLeft(this.mSelectedPosition - 2, (sel.getLeft() - dividerWidth) - this.mSpacing);
            }
        } else if (delta < 0) {
            if (newSel != null) {
                sel = makeAndAddView(selectedPosition, newSel.getLeft(), true, this.mListPadding.top, true);
            } else {
                sel = makeAndAddView(selectedPosition, oldSel.getLeft(), false, this.mListPadding.top, true);
            }
            if (sel.getLeft() < leftSelectionPixel) {
                View view = sel;
                view.offsetLeftAndRight(Math.min(Math.min(leftSelectionPixel - sel.getLeft(), rightSelectionPixel - sel.getRight()), (childrenRight - childrenLeft) / 2));
            }
            fillLeftAndRight(sel, selectedPosition);
        } else {
            int oldLeft = oldSel.getLeft();
            sel = makeAndAddView(selectedPosition, oldLeft, true, this.mListPadding.top, true);
            if (oldLeft < childrenLeft && sel.getRight() < childrenLeft + 20) {
                sel.offsetLeftAndRight(childrenLeft - sel.getLeft());
            }
            fillLeftAndRight(sel, selectedPosition);
        }
        return sel;
    }

    private int getLeftSelectionPixel(int childrenLeft, int fadingEdgeLength, int selectedPosition) {
        int leftSelectionPixel = childrenLeft;
        if (selectedPosition > 0) {
            return leftSelectionPixel + fadingEdgeLength;
        }
        return leftSelectionPixel;
    }

    private int getRightSelectionPixel(int childrenRight, int fadingEdgeLength, int selectedPosition) {
        int rightSelectionPixel = childrenRight;
        if (selectedPosition != this.mItemCount - 1) {
            return rightSelectionPixel - fadingEdgeLength;
        }
        return rightSelectionPixel;
    }

    private View fillRight(int pos, int nextLeft) {
        View selectedView = null;
        int end = getRight() - getLeft();
        if ((getGroupFlags() & 34) == 34) {
            end -= this.mListPadding.right;
        }
        while (nextLeft < end && pos < this.mItemCount) {
            boolean selected = pos == this.mSelectedPosition;
            View child = makeAndAddView(pos, nextLeft, true, this.mListPadding.top, selected);
            nextLeft = child.getRight() + this.mDividerWidth + this.mSpacing;
            if (selected) {
                selectedView = child;
            }
            pos++;
        }
        return selectedView;
    }

    private void fillLeftAndRight(View sel, int position) {
        int dividerWidth = this.mDividerWidth;
        if (!this.mStackFromBottom) {
            fillLeft(position - 1, (sel.getLeft() - dividerWidth) - this.mSpacing);
            adjustViewsLeftOrRight();
            fillRight(position + 1, sel.getRight() + dividerWidth + this.mSpacing);
            return;
        }
        fillRight(position + 1, sel.getRight() + dividerWidth + this.mSpacing);
        adjustViewsLeftOrRight();
        fillLeft(position - 1, sel.getLeft() - dividerWidth);
    }

    private void correctTooWide(int childCount) {
        if ((this.mFirstPosition + childCount) - 1 == this.mItemCount - 1 && childCount > 0) {
            int rightOffset = ((getRight() - getLeft()) - this.mListPadding.right) - getChildAt(childCount - 1).getRight();
            View firstChild = getChildAt(0);
            int firstLeft = firstChild.getLeft();
            if (rightOffset <= 0) {
                return;
            }
            if (this.mFirstPosition > 0 || firstLeft < this.mListPadding.left) {
                if (this.mFirstPosition == 0) {
                    rightOffset = Math.min(rightOffset, this.mListPadding.left - firstLeft);
                }
                offsetChildrenLeftAndRight(rightOffset);
                if (this.mFirstPosition > 0) {
                    fillLeft(this.mFirstPosition - 1, (firstChild.getLeft() - this.mDividerWidth) - this.mSpacing);
                    adjustViewsLeftOrRight();
                }
            }
        }
    }

    private void correctTooNarrow(int childCount) {
        if (this.mFirstPosition == 0 && childCount > 0) {
            int firstLeft = getChildAt(0).getLeft();
            int start = this.mListPadding.left;
            int end = (getRight() - getLeft()) - this.mListPadding.right;
            int leftOffset = firstLeft - start;
            View lastChild = getChildAt(childCount - 1);
            int lastRight = lastChild.getRight();
            int lastPosition = (this.mFirstPosition + childCount) - 1;
            if (leftOffset <= 0) {
                return;
            }
            if (lastPosition < this.mItemCount - 1 || lastRight > end) {
                if (lastPosition == this.mItemCount - 1) {
                    leftOffset = Math.min(leftOffset, lastRight - end);
                }
                offsetChildrenLeftAndRight(-leftOffset);
                if (lastPosition < this.mItemCount - 1) {
                    fillRight(lastPosition + 1, lastChild.getRight() + this.mDividerWidth + this.mSpacing);
                    adjustViewsLeftOrRight();
                }
            } else if (lastPosition == this.mItemCount - 1) {
                adjustViewsLeftOrRight();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count;
        int heightSize;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize2 = View.MeasureSpec.getSize(heightMeasureSpec);
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
            measureScrapChild(child, 0, heightMeasureSpec);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            childState = combineMeasuredStates(0, child.getMeasuredState());
            if (recycleOnMeasure() && this.mRecycler.shouldRecycleViewType(((AbsBaseListView.LayoutParams) child.getLayoutParams()).viewType)) {
                this.mRecycler.addScrapView(child, -1);
            }
        }
        if (heightMode == 0) {
            heightSize = this.mListPadding.top + this.mListPadding.bottom + childHeight + getHorizontalFadingEdgeLength();
        } else {
            heightSize = heightSize2 | (-16777216 & childState);
        }
        if (widthMode == 0) {
            widthSize = this.mListPadding.left + this.mListPadding.right + childWidth + (getHorizontalFadingEdgeLength() * 2);
        }
        if (heightMode == Integer.MIN_VALUE) {
            heightSize = measureWidthOfChildren(heightMeasureSpec, 0, -1, widthSize, -1);
        }
        setMeasuredDimension(widthSize, heightSize);
        this.mHeightMeasureSpec = heightMeasureSpec;
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"WrongCall"})
    public void superOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count;
        int heightSize;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize2 = View.MeasureSpec.getSize(heightMeasureSpec);
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
            View child = obtainView(this.mSelectedPosition, this.mIsScrap);
            measureScrapChild(child, this.mSelectedPosition, heightMeasureSpec);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            childState = combineMeasuredStates(0, child.getMeasuredState());
            if (recycleOnMeasure() && this.mRecycler.shouldRecycleViewType(((AbsBaseListView.LayoutParams) child.getLayoutParams()).viewType)) {
                this.mRecycler.addScrapView(child, -1);
            }
        }
        if (heightMode == 0) {
            heightSize = this.mListPadding.top + this.mListPadding.bottom + childHeight + getHorizontalFadingEdgeLength();
        } else {
            heightSize = heightSize2 | (-16777216 & childState);
        }
        if (widthMode == 0) {
            widthSize = this.mListPadding.left + this.mListPadding.right + childWidth + (getHorizontalFadingEdgeLength() * 2);
        }
        if (heightMode == Integer.MIN_VALUE) {
            heightSize = measureWidthOfChildren(heightMeasureSpec, 0, -1, widthSize, -1);
        }
        setMeasuredDimension(widthSize, heightSize);
        this.mHeightMeasureSpec = heightMeasureSpec;
    }

    private View makeAndAddView(int position, int x, boolean flow, int childrenLeft, boolean selected) {
        View child;
        if (this.mDataChanged || (child = this.mRecycler.getActiveView(position)) == null) {
            View child2 = obtainView(position, this.mIsScrap);
            setupChild(child2, position, x, flow, childrenLeft, selected, this.mIsScrap[0]);
            return child2;
        }
        setupChild(child, position, x, flow, childrenLeft, selected, true);
        return child;
    }

    private void setupChild(View child, int position, int x, boolean flowLeft, int childrenTop, boolean selected, boolean recycled) {
        int childWidthSpec;
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
            addViewInLayout(child, flowLeft ? -1 : 0, p, true);
        } else {
            attachViewToParent(child, flowLeft ? -1 : 0, p);
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
            int childHeightSpec = getChildMeasureSpec(this.mHeightMeasureSpec, this.mListPadding.top + this.mListPadding.bottom, p.width);
            int lpWidth = p.width;
            if (lpWidth > 0) {
                childWidthSpec = View.MeasureSpec.makeMeasureSpec(lpWidth, UCCore.VERIFY_POLICY_QUICK);
            } else {
                childWidthSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            }
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childLeft = flowLeft ? x : x - w;
        if (needToMeasure) {
            child.layout(childLeft, childrenTop, childLeft + w, childrenTop + h);
        } else {
            child.offsetTopAndBottom(childrenTop - child.getTop());
            child.offsetLeftAndRight(childLeft - child.getLeft());
        }
        if (this.mCachingStarted && !child.isDrawingCacheEnabled()) {
            child.setDrawingCacheEnabled(true);
        }
        if (recycled && ((AbsBaseListView.LayoutParams) child.getLayoutParams()).scrappedFromPosition != position) {
            child.jumpDrawablesToCurrentState();
        }
    }

    private void measureScrapChild(View child, int position, int heightMeasureSpec) {
        int childWidthSpec;
        AbsBaseListView.LayoutParams p = (AbsBaseListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsBaseListView.LayoutParams) generateDefaultLayoutParams();
            child.setLayoutParams(p);
        }
        p.viewType = this.mAdapter.getItemViewType(position);
        p.forceAdd = true;
        int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, this.mListPadding.top + this.mListPadding.bottom, p.height);
        int lpWidth = p.width;
        if (lpWidth > 0) {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(lpWidth, UCCore.VERIFY_POLICY_QUICK);
        } else {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /* access modifiers changed from: protected */
    public boolean recycleOnMeasure() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public final int measureWidthOfChildren(int heightMeasureSpec, int startPosition, int endPosition, int maxWidth, int disallowPartialChildPosition) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null) {
            return this.mListPadding.left + this.mListPadding.right;
        }
        int returnedWidth = this.mListPadding.left + this.mListPadding.right;
        int prevWidthWithoutPartialChild = 0;
        if (endPosition == -1) {
            endPosition = adapter.getCount() - 1;
        }
        AbsBaseListView.RecycleBin recycleBin = this.mRecycler;
        boolean recyle = recycleOnMeasure();
        boolean[] isScrap = this.mIsScrap;
        int i = startPosition;
        while (i <= endPosition) {
            View child = obtainView(i, isScrap);
            measureScrapChild(child, i, heightMeasureSpec);
            if (i > 0) {
                returnedWidth += 0;
            }
            if (recyle && recycleBin.shouldRecycleViewType(((AbsBaseListView.LayoutParams) child.getLayoutParams()).viewType)) {
                recycleBin.addScrapView(child, -1);
            }
            returnedWidth += child.getMeasuredWidth();
            if (returnedWidth < maxWidth) {
                if (disallowPartialChildPosition >= 0 && i >= disallowPartialChildPosition) {
                    prevWidthWithoutPartialChild = returnedWidth;
                }
                i++;
            } else if (disallowPartialChildPosition < 0 || i <= disallowPartialChildPosition || prevWidthWithoutPartialChild <= 0 || returnedWidth == maxWidth) {
                return maxWidth;
            } else {
                return prevWidthWithoutPartialChild;
            }
        }
        return returnedWidth;
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
        if (action != 1) {
            switch (keyCode) {
                case 19:
                    if (event.hasNoModifiers()) {
                        handled = handleVerticalFocusWithinListItem(33);
                        break;
                    }
                    break;
                case 20:
                    if (event.hasNoModifiers()) {
                        handled = handleVerticalFocusWithinListItem(130);
                        break;
                    }
                    break;
                case 21:
                    if (!event.hasNoModifiers()) {
                        if (event.hasModifiers(2)) {
                            if (!resurrectSelectionIfNeeded() && !fullScroll(17)) {
                                handled = false;
                                break;
                            } else {
                                handled = true;
                                break;
                            }
                        }
                    } else {
                        handled = resurrectSelectionIfNeeded();
                        if (!handled) {
                            while (true) {
                                int count2 = count;
                                count = count2 - 1;
                                if (count2 > 0 && arrowScroll(17)) {
                                    handled = true;
                                }
                            }
                        }
                    }
                    break;
                case 22:
                    if (!event.hasNoModifiers()) {
                        if (event.hasModifiers(2)) {
                            if (!resurrectSelectionIfNeeded() && !fullScroll(66)) {
                                handled = false;
                                break;
                            } else {
                                handled = true;
                                break;
                            }
                        }
                    } else {
                        handled = resurrectSelectionIfNeeded();
                        if (!handled) {
                            while (true) {
                                int count3 = count;
                                count = count3 - 1;
                                if (count3 > 0 && arrowScroll(66)) {
                                    handled = true;
                                }
                            }
                        }
                    }
                    break;
                case 23:
                case 66:
                    if (event.hasNoModifiers() && !(handled = resurrectSelectionIfNeeded()) && event.getRepeatCount() == 0 && getChildCount() > 0) {
                        keyPressed();
                        handled = true;
                        break;
                    }
                case 92:
                    if (!event.hasNoModifiers()) {
                        if (event.hasModifiers(2)) {
                            if (!resurrectSelectionIfNeeded() && !fullScroll(17)) {
                                handled = false;
                                break;
                            } else {
                                handled = true;
                                break;
                            }
                        }
                    } else if (!resurrectSelectionIfNeeded() && !pageScroll(17)) {
                        handled = false;
                        break;
                    } else {
                        handled = true;
                        break;
                    }
                    break;
                case 93:
                    if (!event.hasNoModifiers()) {
                        if (event.hasModifiers(2)) {
                            if (!resurrectSelectionIfNeeded() && !fullScroll(66)) {
                                handled = false;
                                break;
                            } else {
                                handled = true;
                                break;
                            }
                        }
                    } else if (!resurrectSelectionIfNeeded() && !pageScroll(66)) {
                        handled = false;
                        break;
                    } else {
                        handled = true;
                        break;
                    }
                    break;
                case SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE:
                    if (event.hasNoModifiers()) {
                        if (!resurrectSelectionIfNeeded() && !fullScroll(17)) {
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
                        if (!resurrectSelectionIfNeeded() && !fullScroll(66)) {
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
        if (getChildCount() <= 0) {
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
            if (direction != 17) {
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
        offsetChildrenLeftAndRight(amount);
        int listRight = getWidth() - this.mListPadding.right;
        int listLeft = this.mListPadding.left;
        AbsBaseListView.RecycleBin recycleBin = this.mRecycler;
        if (amount < 0) {
            int numChildren = getChildCount();
            View last = getChildAt(numChildren - 1);
            while (last.getRight() < listRight && (this.mFirstPosition + numChildren) - 1 < this.mItemCount - 1) {
                last = addViewRight(last, lastVisiblePosition);
                numChildren++;
            }
            if (last.getRight() < listRight) {
                offsetChildrenLeftAndRight(listRight - last.getRight());
            }
            View first = getChildAt(0);
            while (first.getRight() < listLeft) {
                if (recycleBin.shouldRecycleViewType(((AbsBaseListView.LayoutParams) first.getLayoutParams()).viewType)) {
                    detachViewFromParent(first);
                    recycleBin.addScrapView(first, this.mFirstPosition);
                } else {
                    removeViewInLayout(first);
                }
                first = getChildAt(0);
                this.mFirstPosition++;
            }
            return;
        }
        View first2 = getChildAt(0);
        while (first2.getLeft() > listLeft && this.mFirstPosition > 0) {
            first2 = addViewLeft(first2, this.mFirstPosition);
            this.mFirstPosition--;
        }
        if (first2.getLeft() > listLeft) {
            offsetChildrenLeftAndRight(listLeft - first2.getLeft());
        }
        int lastIndex = getChildCount() - 1;
        View last2 = getChildAt(lastIndex);
        while (last2.getLeft() > listRight) {
            if (recycleBin.shouldRecycleViewType(((AbsBaseListView.LayoutParams) last2.getLayoutParams()).viewType)) {
                detachViewFromParent(last2);
                recycleBin.addScrapView(last2, this.mFirstPosition + lastIndex);
            } else {
                removeViewInLayout(last2);
            }
            lastIndex--;
            last2 = getChildAt(lastIndex);
        }
    }

    private View addViewLeft(View theView, int position) {
        int leftPosition = position - 1;
        View view = obtainView(leftPosition, this.mIsScrap);
        setupChild(view, leftPosition, (theView.getLeft() - this.mDividerWidth) - this.mSpacing, false, this.mListPadding.top, false, this.mIsScrap[0]);
        return view;
    }

    private View addViewRight(View theView, int position) {
        int rightPosition = position + 1;
        View view = obtainView(rightPosition, this.mIsScrap);
        setupChild(view, rightPosition, theView.getRight() + this.mDividerWidth + this.mSpacing, true, this.mListPadding.top, false, this.mIsScrap[0]);
        return view;
    }

    private int distanceToView(View descendant) {
        descendant.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        int listRight = (getRight() - getLeft()) - this.mListPadding.right;
        if (this.mTempRect.right < this.mListPadding.left) {
            return this.mListPadding.left - this.mTempRect.right;
        }
        if (this.mTempRect.left > listRight) {
            return this.mTempRect.left - listRight;
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
        int leftViewIndex;
        int rightViewIndex;
        View leftView;
        View rightView;
        if (newSelectedPosition == -1) {
            throw new IllegalArgumentException("newSelectedPosition needs to be valid");
        }
        boolean leftSelected = false;
        int selectedIndex = this.mSelectedPosition - this.mFirstPosition;
        int nextSelectedIndex = newSelectedPosition - this.mFirstPosition;
        if (direction == 17) {
            leftViewIndex = nextSelectedIndex;
            rightViewIndex = selectedIndex;
            leftView = getChildAt(leftViewIndex);
            rightView = selectedView;
            leftSelected = true;
        } else {
            leftViewIndex = selectedIndex;
            rightViewIndex = nextSelectedIndex;
            leftView = selectedView;
            rightView = getChildAt(rightViewIndex);
        }
        int numChildren = getChildCount();
        if (leftView != null) {
            leftView.setSelected(!newFocusAssigned && leftSelected);
            measureAndAdjustRight(leftView, leftViewIndex, numChildren);
        }
        if (rightView != null) {
            rightView.setSelected(!newFocusAssigned && !leftSelected);
            measureAndAdjustRight(rightView, rightViewIndex, numChildren);
        }
    }

    private void measureAndAdjustRight(View child, int childIndex, int numChildren) {
        int oldWidth = child.getWidth();
        measureItem(child);
        if (child.getMeasuredWidth() != oldWidth) {
            relayoutMeasuredItem(child);
            int widthDelta = child.getMeasuredWidth() - oldWidth;
            for (int i = childIndex + 1; i < numChildren; i++) {
                getChildAt(i).offsetLeftAndRight(widthDelta);
            }
        }
    }

    private void measureItem(View child) {
        int childWidthSpec;
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(-1, -2);
        }
        int childHeightSpec = getChildMeasureSpec(this.mHeightMeasureSpec, this.mListPadding.top + this.mListPadding.bottom, p.height);
        int lpWidth = p.width;
        if (lpWidth > 0) {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(lpWidth, UCCore.VERIFY_POLICY_QUICK);
        } else {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private void relayoutMeasuredItem(View child) {
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childLeft = child.getLeft();
        int childTop = this.mListPadding.top;
        child.layout(childLeft, childTop, childLeft + w, childTop + h);
    }

    private ArrowScrollFocusResult arrowScrollFocused(int direction) {
        int xSearchPoint;
        View newFocus;
        int xSearchPoint2;
        int selectablePosition;
        View selectedView = getSelectedView();
        if (selectedView == null || !selectedView.hasFocus()) {
            if (direction == 66) {
                int listLeft = this.mListPadding.left + (this.mFirstPosition > 0 ? getArrowScrollPreviewLength() : 0);
                if (selectedView == null || selectedView.getLeft() <= listLeft) {
                    xSearchPoint2 = listLeft;
                } else {
                    xSearchPoint2 = selectedView.getLeft();
                }
                this.mTempRect.set(xSearchPoint2, 0, xSearchPoint2, 0);
            } else {
                int listRight = (getWidth() - this.mListPadding.right) - ((this.mFirstPosition + getChildCount()) + -1 < this.mItemCount ? getArrowScrollPreviewLength() : 0);
                if (selectedView == null || selectedView.getRight() >= listRight) {
                    xSearchPoint = listRight;
                } else {
                    xSearchPoint = selectedView.getRight();
                }
                this.mTempRect.set(xSearchPoint, 0, xSearchPoint, 0);
            }
            newFocus = FocusFinder.getInstance().findNextFocusFromRect(this, this.mTempRect, direction);
        } else {
            newFocus = FocusFinder.getInstance().findNextFocus(this, selectedView.findFocus(), direction);
        }
        if (newFocus != null) {
            int positionOfNewFocus = positionOfNewFocus(newFocus);
            if (this.mSelectedPosition != -1 && positionOfNewFocus != this.mSelectedPosition && (selectablePosition = lookForSelectablePositionOnScreen(direction)) != -1 && ((direction == 66 && selectablePosition < positionOfNewFocus) || (direction == 17 && selectablePosition > positionOfNewFocus))) {
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
        if (direction != 17) {
            int listRight = getWidth() - this.mListPadding.right;
            if (this.mTempRect.right <= listRight) {
                return 0;
            }
            int amountToScroll = this.mTempRect.right - listRight;
            if (positionOfNewFocus < this.mItemCount - 1) {
                return amountToScroll + getArrowScrollPreviewLength();
            }
            return amountToScroll;
        } else if (this.mTempRect.left >= this.mListPadding.left) {
            return 0;
        } else {
            int amountToScroll2 = this.mListPadding.left - this.mTempRect.left;
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

    private int lookForSelectablePositionOnScreen(int direction) {
        int startPos;
        int firstPosition = this.mFirstPosition;
        if (direction == 66) {
            if (this.mSelectedPosition != -1) {
                startPos = this.mSelectedPosition + 1;
            } else {
                startPos = firstPosition;
            }
            if (startPos >= this.mAdapter.getCount()) {
                return -1;
            }
            if (startPos < firstPosition) {
                startPos = firstPosition;
            }
            int lastVisiblePos = getLastVisiblePosition();
            ListAdapter adapter = getAdapter();
            for (int pos = startPos; pos <= lastVisiblePos; pos++) {
                if (adapter.isEnabled(pos) && getChildAt(pos - firstPosition).getVisibility() == 0) {
                    return pos;
                }
            }
        } else {
            int last = (getChildCount() + firstPosition) - 1;
            int startPos2 = this.mSelectedPosition != -1 ? this.mSelectedPosition - 1 : (getChildCount() + firstPosition) - 1;
            if (startPos2 < 0 || startPos2 >= this.mAdapter.getCount()) {
                return -1;
            }
            if (startPos2 > last) {
                startPos2 = last;
            }
            ListAdapter adapter2 = getAdapter();
            for (int pos2 = startPos2; pos2 >= firstPosition; pos2--) {
                if (adapter2.isEnabled(pos2) && getChildAt(pos2 - firstPosition).getVisibility() == 0) {
                    return pos2;
                }
            }
        }
        return -1;
    }

    private int amountToScroll(int direction, int nextSelectedPosition) {
        int listRight = getWidth() - this.mListPadding.right;
        int listLeft = this.mListPadding.left;
        int numChildren = getChildCount();
        if (direction == 66) {
            int indexToMakeVisible = numChildren - 1;
            if (nextSelectedPosition != -1) {
                indexToMakeVisible = nextSelectedPosition - this.mFirstPosition;
            }
            int positionToMakeVisible = this.mFirstPosition + indexToMakeVisible;
            View viewToMakeVisible = getChildAt(indexToMakeVisible);
            int goalRight = listRight;
            if (positionToMakeVisible < this.mItemCount - 1) {
                goalRight -= getArrowScrollPreviewLength();
            }
            if (viewToMakeVisible.getRight() <= goalRight) {
                return 0;
            }
            if (nextSelectedPosition != -1 && goalRight - viewToMakeVisible.getLeft() >= getMaxScrollAmount()) {
                return 0;
            }
            int amountToScroll = viewToMakeVisible.getRight() - goalRight;
            if (this.mFirstPosition + numChildren == this.mItemCount) {
                amountToScroll = Math.min(amountToScroll, getChildAt(numChildren - 1).getRight() - listRight);
            }
            return Math.min(amountToScroll, getMaxScrollAmount());
        }
        int indexToMakeVisible2 = 0;
        if (nextSelectedPosition != -1) {
            indexToMakeVisible2 = nextSelectedPosition - this.mFirstPosition;
        }
        int positionToMakeVisible2 = this.mFirstPosition + indexToMakeVisible2;
        View viewToMakeVisible2 = getChildAt(indexToMakeVisible2);
        int goalLeft = listLeft;
        if (positionToMakeVisible2 > 0) {
            goalLeft += getArrowScrollPreviewLength();
        }
        if (viewToMakeVisible2.getLeft() >= goalLeft) {
            return 0;
        }
        if (nextSelectedPosition != -1 && viewToMakeVisible2.getRight() - goalLeft >= getMaxScrollAmount()) {
            return 0;
        }
        int amountToScroll2 = goalLeft - viewToMakeVisible2.getLeft();
        if (this.mFirstPosition == 0) {
            amountToScroll2 = Math.min(amountToScroll2, listLeft - getChildAt(0).getLeft());
        }
        return Math.min(amountToScroll2, getMaxScrollAmount());
    }

    private int getArrowScrollPreviewLength() {
        return Math.max(2, getHorizontalFadingEdgeLength());
    }

    public int getMaxScrollAmount() {
        return (int) (0.33f * ((float) (getRight() - getLeft())));
    }

    /* access modifiers changed from: package-private */
    public boolean fullScroll(int direction) {
        boolean moved = false;
        if (direction == 17) {
            if (this.mSelectedPosition != 0) {
                int position = lookForSelectablePosition(0, true);
                if (position >= 0) {
                    this.mLayoutMode = 7;
                    setSelectionInt(position);
                }
                moved = true;
            }
        } else if (direction == 66 && this.mSelectedPosition < this.mItemCount - 1) {
            int position2 = lookForSelectablePosition(this.mItemCount - 1, true);
            if (position2 >= 0) {
                this.mLayoutMode = 8;
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

    private boolean handleVerticalFocusWithinListItem(int direction) {
        View selectedView;
        if (direction == 33 || direction == 130) {
            int numChildren = getChildCount();
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
        throw new IllegalArgumentException("direction must be one of {View.FOCUS_UP, View.FOCUS_DOWN}");
    }

    /* access modifiers changed from: package-private */
    public boolean pageScroll(int direction) {
        int position;
        int nextPage = -1;
        boolean down = false;
        if (direction == 17) {
            nextPage = Math.max(0, (this.mSelectedPosition - getChildCount()) - 1);
        } else if (direction == 66) {
            nextPage = Math.min(this.mItemCount - 1, (this.mSelectedPosition + getChildCount()) - 1);
            down = true;
        }
        if (nextPage < 0 || (position = lookForSelectablePosition(nextPage, down)) < 0) {
            return false;
        }
        this.mLayoutMode = 4;
        this.mSpecificLeft = getPaddingLeft() + getHorizontalFadingEdgeLength();
        if (down && position > this.mItemCount - getChildCount()) {
            this.mLayoutMode = 8;
        }
        if (!down && position < getChildCount()) {
            this.mLayoutMode = 7;
        }
        setSelectionInt(position);
        if (!awakenScrollBars()) {
            invalidate();
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void rememberSyncState() {
        if (getChildCount() > 0) {
            this.mNeedSync = true;
            this.mSyncHeight = (long) this.mLayoutHeight;
            if (this.mSelectedPosition >= 0) {
                View v = getChildAt(this.mSelectedPosition - this.mFirstPosition);
                this.mSyncRowId = this.mNextSelectedRowId;
                this.mSyncPosition = this.mNextSelectedPosition;
                if (v != null) {
                    this.mSpecificLeft = v.getLeft();
                }
                this.mSyncMode = 0;
                return;
            }
            View v2 = getChildAt(0);
            ListAdapter adapter = getAdapter();
            if (this.mFirstPosition < 0 || this.mFirstPosition >= adapter.getCount()) {
                this.mSyncRowId = -1;
            } else {
                this.mSyncRowId = adapter.getItemId(this.mFirstPosition);
            }
            this.mSyncPosition = this.mFirstPosition;
            if (v2 != null) {
                this.mSpecificLeft = v2.getLeft();
            }
            this.mSyncMode = 1;
        }
    }

    public void setSelection(int position) {
        setSelectionFromLeft(position, 0);
    }

    public void setSelectionFromLeft(int position, int x) {
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
                this.mSpecificLeft = this.mListPadding.left + x;
                if (this.mNeedSync) {
                    this.mSyncPosition = position;
                    this.mSyncRowId = this.mAdapter.getItemId(position);
                }
                layoutChildren();
            }
        }
    }

    /* access modifiers changed from: protected */
    public int lookForSelectablePosition(int position, boolean lookRight) {
        int position2;
        ListAdapter adapter = this.mAdapter;
        if (adapter == null || isInTouchMode()) {
            return -1;
        }
        int count = adapter.getCount();
        if (!this.mAreAllItemsSelectable) {
            if (lookRight) {
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
    public int findMotionRow(int x) {
        int childCount = getChildCount();
        if (childCount > 0) {
            if (!this.mStackFromBottom) {
                for (int i = 0; i < childCount; i++) {
                    if (x <= getChildAt(i).getRight()) {
                        return this.mFirstPosition + i;
                    }
                }
            } else {
                for (int i2 = childCount - 1; i2 >= 0; i2--) {
                    if (x >= getChildAt(i2).getLeft()) {
                        return this.mFirstPosition + i2;
                    }
                }
            }
        }
        return -1;
    }
}
