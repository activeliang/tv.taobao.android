package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Interpolator;
import com.alibaba.wireless.security.SecExceptionCode;
import com.yunos.tvtaobao.tvsdk.utils.SystemProUtils;
import com.yunos.tvtaobao.tvsdk.widget.ListView;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DeepListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemSelectedListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;

public class FocusGroupListView extends ListView implements DeepListener, ItemListener {
    protected static final boolean DEBUG = false;
    private final String TAG = "FocusGroupHorizonalListView";
    boolean mAimateWhenGainFocusFromDown = true;
    boolean mAimateWhenGainFocusFromLeft = true;
    boolean mAimateWhenGainFocusFromRight = true;
    boolean mAimateWhenGainFocusFromUp = true;
    boolean mAutoSearch = false;
    private Rect mClipFocusRect = new Rect();
    boolean mDeepFocus = false;
    int mDistance = -1;
    boolean mFocusBackground = false;
    FocusRectParams mFocusRectparams = new FocusRectParams();
    boolean mIsAnimate = true;
    ItemSelectedListener mItemSelectedListener;
    int mItemWidth;
    boolean mLayouted = false;
    protected Params mParams = new Params(1.05f, 1.05f, 10, (Interpolator) null, true, 20, new AccelerateDecelerateFrameInterpolator());
    boolean mReset = false;

    public FocusGroupListView(Context context) {
        super(context);
    }

    public FocusGroupListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusGroupListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAnimateWhenGainFocus(boolean fromleft, boolean fromUp, boolean fromRight, boolean fromDown) {
        this.mAimateWhenGainFocusFromLeft = fromleft;
        this.mAimateWhenGainFocusFromUp = fromUp;
        this.mAimateWhenGainFocusFromRight = fromRight;
        this.mAimateWhenGainFocusFromDown = fromDown;
    }

    public void setFocusBackground(boolean back) {
        this.mFocusBackground = back;
    }

    public void setOnItemSelectedListener(ItemSelectedListener listener) {
        this.mItemSelectedListener = listener;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        ZpLogger.d("FocusGroupHorizonalListView", "onFocusChanged");
        if (!this.mAutoSearch && getOnFocusChangeListener() != null) {
            getOnFocusChangeListener().onFocusChange(this, gainFocus);
        }
        if (this.mAutoSearch) {
            super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        }
        if (gainFocus && getChildCount() > 0 && this.mLayouted && getLeftScrollDistance() == 0) {
            reset();
        }
        if (getSelectedView() == null || !this.mLayouted) {
            this.mReset = true;
        } else {
            onItemSelected(gainFocus);
        }
        this.mIsAnimate = checkAnimate(direction);
    }

    private boolean checkAnimate(int direction) {
        switch (direction) {
            case 17:
                if (!this.mAimateWhenGainFocusFromRight) {
                    return false;
                }
                return true;
            case 33:
                if (!this.mAimateWhenGainFocusFromDown) {
                    return false;
                }
                return true;
            case 66:
                if (!this.mAimateWhenGainFocusFromLeft) {
                    return false;
                }
                return true;
            case 130:
                return this.mAimateWhenGainFocusFromUp;
            default:
                return true;
        }
    }

    public void setSelection(int position) {
        setSelectedPositionInt(position);
        setNextSelectedPositionInt(position);
        if (getChildCount() > 0 && this.mLayouted) {
            this.mLayoutMode = 9;
            if (!isLayoutRequested()) {
                layoutChildren();
            } else {
                return;
            }
        }
        reset();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00ac A[Catch:{ all -> 0x013b }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b5 A[Catch:{ all -> 0x013b }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00e2 A[Catch:{ all -> 0x013b }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutChildren() {
        /*
            r29 = this;
            r0 = r29
            boolean r8 = r0.mBlockLayoutRequests
            if (r8 != 0) goto L_0x001e
            r2 = 1
            r0 = r29
            r0.mBlockLayoutRequests = r2
            r29.invalidate()     // Catch:{ all -> 0x013b }
            android.widget.ListAdapter r2 = r29.getAdapter()     // Catch:{ all -> 0x013b }
            if (r2 != 0) goto L_0x001f
            r29.resetList()     // Catch:{ all -> 0x013b }
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r29
            r0.mBlockLayoutRequests = r2
        L_0x001e:
            return
        L_0x001f:
            r0 = r29
            android.graphics.Rect r2 = r0.mListPadding     // Catch:{ all -> 0x013b }
            int r6 = r2.top     // Catch:{ all -> 0x013b }
            int r2 = r29.getBottom()     // Catch:{ all -> 0x013b }
            int r27 = r29.getTop()     // Catch:{ all -> 0x013b }
            int r2 = r2 - r27
            r0 = r29
            android.graphics.Rect r0 = r0.mListPadding     // Catch:{ all -> 0x013b }
            r27 = r0
            r0 = r27
            int r0 = r0.bottom     // Catch:{ all -> 0x013b }
            r27 = r0
            int r7 = r2 - r27
            int r26 = r29.getVisibleChildCount()     // Catch:{ all -> 0x013b }
            int r10 = r29.getChildCount()     // Catch:{ all -> 0x013b }
            r19 = 0
            r5 = 0
            r24 = 0
            r3 = 0
            r21 = 0
            r4 = 0
            r14 = 0
            int r20 = r29.getLastVisiblePosition()     // Catch:{ all -> 0x013b }
            r0 = r29
            boolean r11 = r0.mDataChanged     // Catch:{ all -> 0x013b }
            if (r11 == 0) goto L_0x005f
            r2 = 9
            r0 = r29
            r0.mLayoutMode = r2     // Catch:{ all -> 0x013b }
        L_0x005f:
            r0 = r29
            int r2 = r0.mLayoutMode     // Catch:{ all -> 0x013b }
            switch(r2) {
                case 1: goto L_0x00aa;
                case 2: goto L_0x00c1;
                case 3: goto L_0x00aa;
                case 4: goto L_0x00aa;
                case 5: goto L_0x00aa;
                default: goto L_0x0066;
            }     // Catch:{ all -> 0x013b }
        L_0x0066:
            r0 = r29
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x013b }
            r27 = r0
            int r19 = r2 - r27
            if (r19 < 0) goto L_0x0086
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0086
            int r2 = r29.getUpPreLoadedCount()     // Catch:{ all -> 0x013b }
            int r2 = r2 + r19
            r0 = r29
            android.view.View r3 = r0.getChildAt(r2)     // Catch:{ all -> 0x013b }
        L_0x0086:
            android.view.View r21 = r29.getFirstVisibleChild()     // Catch:{ all -> 0x013b }
            r0 = r29
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x013b }
            if (r2 < 0) goto L_0x009c
            r0 = r29
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x013b }
            r27 = r0
            int r5 = r2 - r27
        L_0x009c:
            int r2 = r19 + r5
            int r27 = r29.getUpPreLoadedCount()     // Catch:{ all -> 0x013b }
            int r2 = r2 + r27
            r0 = r29
            android.view.View r4 = r0.getChildAt(r2)     // Catch:{ all -> 0x013b }
        L_0x00aa:
            if (r11 == 0) goto L_0x00af
            r29.handleDataChanged()     // Catch:{ all -> 0x013b }
        L_0x00af:
            r0 = r29
            int r2 = r0.mItemCount     // Catch:{ all -> 0x013b }
            if (r2 != 0) goto L_0x00e2
            r29.resetList()     // Catch:{ all -> 0x013b }
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r29
            r0.mBlockLayoutRequests = r2
            goto L_0x001e
        L_0x00c1:
            r0 = r29
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x013b }
            r27 = r0
            int r19 = r2 - r27
            if (r19 < 0) goto L_0x00aa
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x00aa
            int r2 = r29.getUpPreLoadedCount()     // Catch:{ all -> 0x013b }
            int r2 = r2 + r19
            r0 = r29
            android.view.View r4 = r0.getChildAt(r2)     // Catch:{ all -> 0x013b }
            goto L_0x00aa
        L_0x00e2:
            r0 = r29
            int r2 = r0.mItemCount     // Catch:{ all -> 0x013b }
            android.widget.ListAdapter r27 = r29.getAdapter()     // Catch:{ all -> 0x013b }
            int r27 = r27.getCount()     // Catch:{ all -> 0x013b }
            r0 = r27
            if (r2 == r0) goto L_0x0147
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch:{ all -> 0x013b }
            java.lang.StringBuilder r27 = new java.lang.StringBuilder     // Catch:{ all -> 0x013b }
            r27.<init>()     // Catch:{ all -> 0x013b }
            java.lang.String r28 = "The content of the adapter has changed but ListView did not receive a notification. Make sure the content of your adapter is not modified from a background thread, but only from the UI thread. [in ListView("
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ all -> 0x013b }
            int r28 = r29.getId()     // Catch:{ all -> 0x013b }
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ all -> 0x013b }
            java.lang.String r28 = ", "
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ all -> 0x013b }
            java.lang.Class r28 = r29.getClass()     // Catch:{ all -> 0x013b }
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ all -> 0x013b }
            java.lang.String r28 = ") with Adapter("
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ all -> 0x013b }
            android.widget.ListAdapter r28 = r29.getAdapter()     // Catch:{ all -> 0x013b }
            java.lang.Class r28 = r28.getClass()     // Catch:{ all -> 0x013b }
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ all -> 0x013b }
            java.lang.String r28 = ")]"
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ all -> 0x013b }
            java.lang.String r27 = r27.toString()     // Catch:{ all -> 0x013b }
            r0 = r27
            r2.<init>(r0)     // Catch:{ all -> 0x013b }
            throw r2     // Catch:{ all -> 0x013b }
        L_0x013b:
            r2 = move-exception
            if (r8 != 0) goto L_0x0146
            r27 = 0
            r0 = r27
            r1 = r29
            r1.mBlockLayoutRequests = r0
        L_0x0146:
            throw r2
        L_0x0147:
            r0 = r29
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            r0.setSelectedPositionInt(r2)     // Catch:{ all -> 0x013b }
            r0 = r29
            int r12 = r0.mFirstPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView$RecycleBin r0 = r0.mRecycler     // Catch:{ all -> 0x013b }
            r23 = r0
            r13 = 0
            if (r11 == 0) goto L_0x0177
            r18 = 0
        L_0x015f:
            r0 = r18
            if (r0 >= r10) goto L_0x017c
            r0 = r29
            r1 = r18
            android.view.View r2 = r0.getChildAt(r1)     // Catch:{ all -> 0x013b }
            int r27 = r12 + r18
            r0 = r23
            r1 = r27
            r0.addScrapView(r2, r1)     // Catch:{ all -> 0x013b }
            int r18 = r18 + 1
            goto L_0x015f
        L_0x0177:
            r0 = r23
            r0.fillActiveViews(r10, r12)     // Catch:{ all -> 0x013b }
        L_0x017c:
            android.view.View r17 = r29.getFocusedChild()     // Catch:{ all -> 0x013b }
            if (r17 == 0) goto L_0x019c
            if (r11 == 0) goto L_0x018e
            r0 = r29
            r1 = r17
            boolean r2 = r0.isDirectChildHeaderOrFooter(r1)     // Catch:{ all -> 0x013b }
            if (r2 == 0) goto L_0x0199
        L_0x018e:
            r13 = r17
            android.view.View r14 = r29.findFocus()     // Catch:{ all -> 0x013b }
            if (r14 == 0) goto L_0x0199
            r14.onStartTemporaryDetach()     // Catch:{ all -> 0x013b }
        L_0x0199:
            r29.requestFocus()     // Catch:{ all -> 0x013b }
        L_0x019c:
            r29.detachAllViewsFromParent()     // Catch:{ all -> 0x013b }
            r23.removeSkippedScrap()     // Catch:{ all -> 0x013b }
            r0 = r29
            int r2 = r0.mLayoutMode     // Catch:{ all -> 0x013b }
            switch(r2) {
                case 1: goto L_0x0280;
                case 2: goto L_0x023e;
                case 3: goto L_0x026f;
                case 4: goto L_0x0290;
                case 5: goto L_0x0253;
                case 6: goto L_0x0267;
                case 7: goto L_0x01a9;
                case 8: goto L_0x01a9;
                case 9: goto L_0x02a4;
                default: goto L_0x01a9;
            }     // Catch:{ all -> 0x013b }
        L_0x01a9:
            if (r10 != 0) goto L_0x02d1
            r0 = r29
            boolean r2 = r0.mStackFromBottom     // Catch:{ all -> 0x013b }
            if (r2 != 0) goto L_0x02ac
            r0 = r29
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x013b }
            r27 = 1
            r0 = r29
            r1 = r27
            int r22 = r0.lookForSelectablePosition(r2, r1)     // Catch:{ all -> 0x013b }
            r0 = r29
            r1 = r22
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x013b }
            r0 = r29
            android.view.View r24 = r0.fillFromTop(r6)     // Catch:{ all -> 0x013b }
        L_0x01cc:
            r23.scrapActiveViews()     // Catch:{ all -> 0x013b }
            if (r24 == 0) goto L_0x0365
            r0 = r29
            boolean r2 = r0.mItemsCanFocus     // Catch:{ all -> 0x013b }
            if (r2 == 0) goto L_0x035b
            boolean r2 = r29.hasFocus()     // Catch:{ all -> 0x013b }
            if (r2 == 0) goto L_0x035b
            boolean r2 = r24.hasFocus()     // Catch:{ all -> 0x013b }
            if (r2 != 0) goto L_0x035b
            r0 = r24
            if (r0 != r13) goto L_0x01ef
            if (r14 == 0) goto L_0x01ef
            boolean r2 = r14.requestFocus()     // Catch:{ all -> 0x013b }
            if (r2 != 0) goto L_0x01f5
        L_0x01ef:
            boolean r2 = r24.requestFocus()     // Catch:{ all -> 0x013b }
            if (r2 == 0) goto L_0x0349
        L_0x01f5:
            r15 = 1
        L_0x01f6:
            if (r15 != 0) goto L_0x034c
            android.view.View r16 = r29.getFocusedChild()     // Catch:{ all -> 0x013b }
            if (r16 == 0) goto L_0x0201
            r16.clearFocus()     // Catch:{ all -> 0x013b }
        L_0x0201:
            r2 = -1
            r0 = r29
            r1 = r24
            r0.positionSelector(r2, r1)     // Catch:{ all -> 0x013b }
        L_0x0209:
            if (r14 == 0) goto L_0x0214
            android.os.IBinder r2 = r14.getWindowToken()     // Catch:{ all -> 0x013b }
            if (r2 == 0) goto L_0x0214
            r14.onFinishTemporaryDetach()     // Catch:{ all -> 0x013b }
        L_0x0214:
            r2 = 0
            r0 = r29
            r0.mLayoutMode = r2     // Catch:{ all -> 0x013b }
            r2 = 0
            r0 = r29
            r0.mDataChanged = r2     // Catch:{ all -> 0x013b }
            r2 = 0
            r0 = r29
            r0.mNeedSync = r2     // Catch:{ all -> 0x013b }
            r0 = r29
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            r0.setNextSelectedPositionInt(r2)     // Catch:{ all -> 0x013b }
            r0 = r29
            int r2 = r0.mItemCount     // Catch:{ all -> 0x013b }
            if (r2 <= 0) goto L_0x0235
            r29.checkSelectionChanged()     // Catch:{ all -> 0x013b }
        L_0x0235:
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r29
            r0.mBlockLayoutRequests = r2
            goto L_0x001e
        L_0x023e:
            if (r4 == 0) goto L_0x024b
            int r2 = r4.getLeft()     // Catch:{ all -> 0x013b }
            r0 = r29
            android.view.View r24 = r0.fillFromSelection(r2, r6, r7)     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x024b:
            r0 = r29
            android.view.View r24 = r0.fillFromMiddle(r6, r7)     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x0253:
            r0 = r29
            int r2 = r0.mSyncPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            int r0 = r0.mSpecificTop     // Catch:{ all -> 0x013b }
            r27 = r0
            r0 = r29
            r1 = r27
            android.view.View r24 = r0.fillSpecific(r2, r1)     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x0267:
            r2 = r29
            android.view.View r24 = r2.moveSelection(r3, r4, r5, r6, r7)     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x026f:
            r0 = r29
            int r2 = r0.mItemCount     // Catch:{ all -> 0x013b }
            int r2 = r2 + -1
            r0 = r29
            android.view.View r24 = r0.fillUp(r2, r7)     // Catch:{ all -> 0x013b }
            r29.adjustViewsUpOrDown()     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x0280:
            r2 = 0
            r0 = r29
            r0.mFirstPosition = r2     // Catch:{ all -> 0x013b }
            r0 = r29
            android.view.View r24 = r0.fillFromTop(r6)     // Catch:{ all -> 0x013b }
            r29.adjustViewsUpOrDown()     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x0290:
            int r2 = r29.reconcileSelectedPosition()     // Catch:{ all -> 0x013b }
            r0 = r29
            int r0 = r0.mSpecificTop     // Catch:{ all -> 0x013b }
            r27 = r0
            r0 = r29
            r1 = r27
            android.view.View r24 = r0.fillSpecific(r2, r1)     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x02a4:
            r0 = r29
            android.view.View r24 = r0.fillFromMiddle(r6, r7)     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x02ac:
            r0 = r29
            int r2 = r0.mItemCount     // Catch:{ all -> 0x013b }
            int r2 = r2 + -1
            r27 = 0
            r0 = r29
            r1 = r27
            int r22 = r0.lookForSelectablePosition(r2, r1)     // Catch:{ all -> 0x013b }
            r0 = r29
            r1 = r22
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x013b }
            r0 = r29
            int r2 = r0.mItemCount     // Catch:{ all -> 0x013b }
            int r2 = r2 + -1
            r0 = r29
            android.view.View r24 = r0.fillUp(r2, r7)     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x02d1:
            r0 = r29
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x013b }
            r25 = r0
            r0 = r29
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x013b }
            int r27 = r12 + r5
            r0 = r27
            if (r2 < r0) goto L_0x0311
            r0 = r29
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x013b }
            int r27 = r20 + r5
            r0 = r27
            if (r2 > r0) goto L_0x0311
            r0 = r29
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x013b }
            r25 = r0
        L_0x02f1:
            r0 = r29
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x013b }
            if (r2 < 0) goto L_0x031f
            r0 = r29
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            int r0 = r0.mItemCount     // Catch:{ all -> 0x013b }
            r27 = r0
            r0 = r27
            if (r2 >= r0) goto L_0x031f
            if (r3 != 0) goto L_0x031a
        L_0x0307:
            r0 = r29
            r1 = r25
            android.view.View r24 = r0.fillSpecific(r1, r6)     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x0311:
            r3 = r21
            r0 = r29
            int r2 = r0.mFirstPosition     // Catch:{ all -> 0x013b }
            int r25 = r2 + r5
            goto L_0x02f1
        L_0x031a:
            int r6 = r3.getTop()     // Catch:{ all -> 0x013b }
            goto L_0x0307
        L_0x031f:
            r0 = r29
            int r2 = r0.mFirstPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            int r0 = r0.mItemCount     // Catch:{ all -> 0x013b }
            r27 = r0
            r0 = r27
            if (r2 >= r0) goto L_0x0340
            r0 = r29
            int r2 = r0.mFirstPosition     // Catch:{ all -> 0x013b }
            if (r21 != 0) goto L_0x033b
        L_0x0333:
            r0 = r29
            android.view.View r24 = r0.fillSpecific(r2, r6)     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x033b:
            int r6 = r21.getTop()     // Catch:{ all -> 0x013b }
            goto L_0x0333
        L_0x0340:
            r2 = 0
            r0 = r29
            android.view.View r24 = r0.fillSpecific(r2, r6)     // Catch:{ all -> 0x013b }
            goto L_0x01cc
        L_0x0349:
            r15 = 0
            goto L_0x01f6
        L_0x034c:
            r2 = 0
            r0 = r24
            r0.setSelected(r2)     // Catch:{ all -> 0x013b }
            r0 = r29
            android.graphics.Rect r2 = r0.mSelectorRect     // Catch:{ all -> 0x013b }
            r2.setEmpty()     // Catch:{ all -> 0x013b }
            goto L_0x0209
        L_0x035b:
            r2 = -1
            r0 = r29
            r1 = r24
            r0.positionSelector(r2, r1)     // Catch:{ all -> 0x013b }
            goto L_0x0209
        L_0x0365:
            r0 = r29
            int r2 = r0.mTouchMode     // Catch:{ all -> 0x013b }
            if (r2 <= 0) goto L_0x039f
            r0 = r29
            int r2 = r0.mTouchMode     // Catch:{ all -> 0x013b }
            r27 = 3
            r0 = r27
            if (r2 >= r0) goto L_0x039f
            r0 = r29
            int r2 = r0.mMotionPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x013b }
            r27 = r0
            int r2 = r2 - r27
            r0 = r29
            android.view.View r9 = r0.getChildAt(r2)     // Catch:{ all -> 0x013b }
            if (r9 == 0) goto L_0x0392
            r0 = r29
            int r2 = r0.mMotionPosition     // Catch:{ all -> 0x013b }
            r0 = r29
            r0.positionSelector(r2, r9)     // Catch:{ all -> 0x013b }
        L_0x0392:
            boolean r2 = r29.hasFocus()     // Catch:{ all -> 0x013b }
            if (r2 == 0) goto L_0x0209
            if (r14 == 0) goto L_0x0209
            r14.requestFocus()     // Catch:{ all -> 0x013b }
            goto L_0x0209
        L_0x039f:
            r0 = r29
            android.graphics.Rect r2 = r0.mSelectorRect     // Catch:{ all -> 0x013b }
            r2.setEmpty()     // Catch:{ all -> 0x013b }
            goto L_0x0392
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.widget.FocusGroupListView.layoutChildren():void");
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if ((hasFocus() || hasDeepFocus()) && getLeftScrollDistance() == 0) {
            reset();
        }
        this.mLayouted = true;
    }

    public void requestLayout() {
        super.requestLayout();
        this.mLayouted = false;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int index = -1;
        if (getAdapter() != null) {
            if (getHeaderViewsCount() > 0 && getAdapter().getCount() > getHeaderViewsCount()) {
                index = getHeaderViewsCount();
            }
            if (getAdapter().getCount() > 0) {
                index = 0;
            }
            if (index >= 0) {
                this.mItemWidth = obtainView(index, this.mIsScrap).getMeasuredWidth();
            }
        }
    }

    public Params getParams() {
        if (this.mParams != null) {
            return this.mParams;
        }
        throw new IllegalArgumentException("The params is null, you must call setScaleParams before it's running");
    }

    public void getFocusedRect(Rect r) {
        if (hasFocus() || hasDeepFocus()) {
            super.getFocusedRect(r);
        } else {
            getDrawingRect(r);
        }
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (hasFocus()) {
            super.addFocusables(views, direction, focusableMode);
        } else if (views != null && isFocusable()) {
            if ((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) {
                views.add(this);
            }
        }
    }

    private void reset() {
        ItemListener item = (ItemListener) getSelectedView();
        if (item != null) {
            this.mFocusRectparams.set(item.getFocusParams());
            offsetDescendantRectToMyCoords(getSelectedView(), this.mFocusRectparams.focusRect());
        }
    }

    private void resetHeader(int nextSelectedPosition) {
        int width;
        View header = getHeaderView(nextSelectedPosition);
        ItemListener item = (ItemListener) header;
        if (item != null) {
            this.mFocusRectparams.set(item.getFocusParams());
        }
        int left = getChildAt(0).getLeft();
        for (int index = getFirstVisiblePosition() - 1; index >= 0; index--) {
            if (index >= getHeaderViewsCount()) {
                if (this.mItemWidth <= 0) {
                    ZpLogger.e("FocusGroupHorizonalListView", "FocusHList mItemWidth <= 0");
                }
                width = this.mItemWidth;
            } else {
                width = getHeaderView(index).getWidth();
            }
            left -= width;
        }
        this.mFocusRectparams.focusRect().left = left;
        this.mFocusRectparams.focusRect().right = header.getWidth() + left;
    }

    public FocusRectParams getFocusParams() {
        return this.mFocusRectparams;
    }

    public boolean canDraw() {
        if (getSelectedView() != null && this.mReset) {
            performSelect(true);
            this.mReset = false;
        }
        if (getSelectedView() == null || !this.mLayouted) {
            return false;
        }
        return true;
    }

    public boolean isAnimate() {
        return this.mIsAnimate;
    }

    private boolean moveLeft() {
        int nextSelectedPosition;
        if (Math.abs(getLeftScrollDistance()) > getChildAt(0).getWidth() * 3) {
            return true;
        }
        performSelect(false);
        this.mReset = false;
        if (getSelectedItemPosition() - 1 >= 0) {
            nextSelectedPosition = getSelectedItemPosition() - 1;
        } else {
            nextSelectedPosition = -1;
        }
        if (nextSelectedPosition == -1) {
            return false;
        }
        setSelectedPositionInt(nextSelectedPosition);
        setNextSelectedPositionInt(nextSelectedPosition);
        if (canDraw()) {
            this.mReset = false;
            performSelect(true);
        } else {
            this.mReset = true;
        }
        amountToCenterScroll(33, nextSelectedPosition);
        return true;
    }

    /* access modifiers changed from: protected */
    public void performSelect(boolean select) {
        if (this.mItemSelectedListener != null) {
            this.mItemSelectedListener.onItemSelected(getSelectedView(), getSelectedItemPosition(), select, this);
        }
    }

    private boolean moveRight() {
        int nextSelectedPosition;
        if (getLeftScrollDistance() > getChildAt(0).getWidth() * 3) {
            return true;
        }
        performSelect(false);
        this.mReset = false;
        if (getSelectedItemPosition() + 1 < this.mItemCount) {
            nextSelectedPosition = getSelectedItemPosition() + 1;
        } else {
            nextSelectedPosition = -1;
        }
        if (nextSelectedPosition == -1) {
            return false;
        }
        setSelectedPositionInt(nextSelectedPosition);
        setNextSelectedPositionInt(nextSelectedPosition);
        if (canDraw()) {
            this.mReset = false;
            performSelect(true);
        } else {
            this.mReset = true;
        }
        amountToCenterScroll(130, nextSelectedPosition);
        return true;
    }

    public boolean checkState(int keyCode) {
        if (this.mLastScrollState == 2 && (keyCode == 21 || keyCode == 22)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public int amountToCenterScroll(int direction, int nextSelectedPosition) {
        int nextSelectedCenter;
        boolean reset;
        int nextSelectedCenter2;
        boolean reset2;
        int center = (((getHeight() - this.mListPadding.top) - this.mListPadding.bottom) / 2) + this.mListPadding.top;
        int height = getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        int visibleChildCount = getVisibleChildCount();
        int amountToScroll = 0;
        int distanceLeft = getLeftScrollDistance();
        if (direction == 130) {
            View nextSelctedView = getChildAt(nextSelectedPosition - getFirstPosition());
            if (nextSelctedView == null) {
                nextSelctedView = getLastChild();
                nextSelectedCenter2 = ((nextSelctedView.getTop() + nextSelctedView.getBottom()) / 2) + ((nextSelctedView.getHeight() + this.mSpacing) * (nextSelectedPosition - getLastPosition()));
                reset2 = false;
            } else {
                nextSelectedCenter2 = (nextSelctedView.getTop() + nextSelctedView.getBottom()) / 2;
                reset2 = true;
            }
            int finalNextSelectedCenter = nextSelectedCenter2 - distanceLeft;
            if (finalNextSelectedCenter > center) {
                amountToScroll = finalNextSelectedCenter - center;
                int maxDiff = ((nextSelctedView.getHeight() + this.mSpacing) * ((this.mItemCount - getLastVisiblePosition()) - 1)) - distanceLeft;
                View lastVisibleView = getLastVisibleChild();
                if (lastVisibleView.getBottom() > getHeight() - this.mListPadding.bottom) {
                    maxDiff += lastVisibleView.getBottom() - (getHeight() - this.mListPadding.bottom);
                }
                if (amountToScroll > maxDiff) {
                    amountToScroll = maxDiff;
                }
                if (reset2) {
                    reset();
                    offsetFocusRect(0, -distanceLeft);
                }
                if (amountToScroll > 0) {
                    if (reset2) {
                        offsetFocusRect(0, -amountToScroll);
                    } else {
                        offsetFocusRect(0, (nextSelctedView.getHeight() + this.mSpacing) - amountToScroll);
                    }
                    smoothScrollBy(amountToScroll);
                    this.mIsAnimate = true;
                } else {
                    if (!reset2) {
                        offsetFocusRect(0, nextSelctedView.getHeight() + this.mSpacing);
                    }
                    this.mIsAnimate = true;
                }
            } else {
                reset();
                offsetFocusRect(0, -distanceLeft);
                this.mIsAnimate = true;
            }
            return amountToScroll;
        } else if (direction != 33) {
            return 0;
        } else {
            View nextSelctedView2 = getChildAt(nextSelectedPosition - getFirstPosition());
            if (nextSelctedView2 == null) {
                nextSelctedView2 = getFirstVisibleChild();
                int nextSelectedCenter3 = (nextSelctedView2.getTop() + nextSelctedView2.getBottom()) / 2;
                if (nextSelectedPosition >= getHeaderViewsCount()) {
                    nextSelectedCenter = nextSelectedCenter3 - ((nextSelctedView2.getHeight() + this.mSpacing) * (getFirstVisiblePosition() - nextSelectedPosition));
                } else {
                    nextSelectedCenter = nextSelectedCenter3 - ((nextSelctedView2.getHeight() + this.mSpacing) * (getFirstVisiblePosition() - getHeaderViewsCount()));
                    for (int i = getHeaderViewsCount() - 1; i >= nextSelectedPosition; i--) {
                        nextSelectedCenter -= getHeaderView(i).getHeight();
                    }
                }
                reset = false;
            } else {
                nextSelectedCenter = (nextSelctedView2.getTop() + nextSelctedView2.getBottom()) / 2;
                reset = true;
            }
            int finalNextSelectedCenter2 = nextSelectedCenter - distanceLeft;
            if (finalNextSelectedCenter2 < center) {
                int amountToScroll2 = center - finalNextSelectedCenter2;
                int maxDiff2 = 0;
                int start = getHeaderViewsCount() - 1;
                if (getFirstVisiblePosition() >= getHeaderViewsCount()) {
                    maxDiff2 = (nextSelctedView2.getHeight() + this.mSpacing) * (getFirstVisiblePosition() - getHeaderViewsCount());
                } else {
                    start = getFirstVisiblePosition() - 1;
                }
                for (int i2 = start; i2 >= 0; i2--) {
                    maxDiff2 += getHeaderView(i2).getHeight();
                }
                if (maxDiff2 < 0) {
                    maxDiff2 = 0;
                }
                int maxDiff3 = maxDiff2 + distanceLeft;
                View firstVisibleView = getFirstVisibleChild();
                if (firstVisibleView.getTop() < listTop) {
                    int firstOffset = getFirsVisibletChildIndex() - nextSelectedPosition;
                    if (firstOffset > 0) {
                        maxDiff3 += (listTop - firstVisibleView.getTop()) - (firstOffset * listTop);
                    } else {
                        maxDiff3 += listTop - firstVisibleView.getTop();
                    }
                }
                if (amountToScroll2 > maxDiff3) {
                    amountToScroll2 = maxDiff3;
                }
                if (reset) {
                    reset();
                    offsetFocusRect(0, -distanceLeft);
                } else if (nextSelectedPosition < getHeaderViewsCount()) {
                    reset = true;
                    resetHeader(nextSelectedPosition);
                    offsetFocusRect(0, -distanceLeft);
                }
                if (amountToScroll > 0) {
                    if (reset) {
                        offsetFocusRect(0, amountToScroll);
                    } else {
                        offsetFocusRect(0, -((nextSelctedView2.getHeight() + this.mSpacing) - amountToScroll));
                    }
                    smoothScrollBy(-amountToScroll);
                    this.mIsAnimate = true;
                } else {
                    if (!reset) {
                        offsetFocusRect(0, -(nextSelctedView2.getHeight() + this.mSpacing));
                    }
                    this.mIsAnimate = true;
                }
            } else {
                reset();
                offsetFocusRect(0, -distanceLeft);
                this.mIsAnimate = true;
            }
            return amountToScroll;
        }
    }

    public void offsetFocusRect(int offsetX, int offsetY) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            this.mFocusRectparams.focusRect().offset(offsetX, offsetY);
        }
    }

    public ItemListener getItem() {
        return (ItemListener) getSelectedView();
    }

    public boolean isScrolling() {
        return false;
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        if (checkState(keyCode)) {
            return true;
        }
        switch (keyCode) {
            case 19:
            case SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE:
                if (getSelectedItemPosition() <= 0) {
                    return false;
                }
                return true;
            case 20:
            case 123:
                if (getSelectedItemPosition() >= this.mItemCount - 1) {
                    return false;
                }
                return true;
            case 21:
            case 22:
            case 23:
            case 66:
                return true;
            default:
                return false;
        }
    }

    public boolean hasDeepFocus() {
        return this.mDeepFocus;
    }

    public boolean canDeep() {
        return true;
    }

    public void onFocusDeeped(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        this.mDeepFocus = gainFocus;
        onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public boolean isScale() {
        return true;
    }

    public int getItemWidth() {
        return getWidth();
    }

    public int getItemHeight() {
        return getHeight();
    }

    public Rect getManualPadding() {
        return null;
    }

    public void onItemSelected(boolean selected) {
        performSelect(selected);
    }

    public void onItemClick() {
        if (getSelectedView() != null) {
            performItemClick(getSelectedView(), getSelectedItemPosition(), 0);
        }
    }

    public boolean isFocusBackground() {
        return this.mFocusBackground;
    }

    public void drawBeforeFocus(Canvas canvas) {
    }

    public void drawAfterFocus(Canvas canvas) {
    }

    public boolean isFinished() {
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int nextSelectedPosition = -1;
        if (keyCode == 19) {
            if (getSelectedItemPosition() - 1 >= 0) {
                nextSelectedPosition = getSelectedItemPosition() - 1;
            }
            View nextSelectedView = getChildAt(nextSelectedPosition - getFirstVisiblePosition());
            ZpLogger.i("FocusGroupHorizonalListView", "onKeyDown KEYCODE_DPAD_LEFT nextSelectedPosition=" + nextSelectedPosition + " nextSelectedView=" + nextSelectedView);
            if (nextSelectedView == null) {
                return true;
            }
        } else if (keyCode == 20) {
            if (getSelectedItemPosition() + 1 < this.mItemCount) {
                nextSelectedPosition = getSelectedItemPosition() + 1;
            }
            View nextSelectedView2 = getChildAt(nextSelectedPosition - getFirstVisiblePosition());
            ZpLogger.i("FocusGroupHorizonalListView", "onKeyDown KEYCODE_DPAD_RIGHT nextSelectedPosition=" + nextSelectedPosition + " nextSelectedView=" + nextSelectedView2);
            if (nextSelectedView2 == null) {
                return true;
            }
        }
        if (getChildCount() <= 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (checkState(keyCode)) {
            return true;
        }
        if (this.mDistance < 0) {
            this.mDistance = getChildAt(0).getWidth();
        }
        switch (keyCode) {
            case 19:
                if (moveLeft()) {
                    playSoundEffect(1);
                    return true;
                }
                break;
            case 20:
                if (moveRight()) {
                    playSoundEffect(3);
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private int getItemDistance(int startPos, int endPos, int direction) {
        int totalWidth;
        int totalWidth2;
        GroupBaseAdapter groupAdapter = (GroupBaseAdapter) getAdapter();
        if (startPos < 0 || endPos < 0 || endPos < startPos) {
            throw new IllegalArgumentException();
        } else if (startPos == endPos) {
            return 0;
        } else {
            int startGroupPos = groupAdapter.getGroupPos(startPos);
            int startItemPos = groupAdapter.getGroupItemPos(startPos);
            int endGroupPos = groupAdapter.getGroupPos(endPos);
            int endItemPos = groupAdapter.getGroupItemPos(endPos);
            int hintWidth = getGroupHintWidth(groupAdapter);
            int itemWidth = getGroupItemWidth(groupAdapter);
            int totalWidth3 = 0;
            if (direction == 130) {
                if (startGroupPos != endGroupPos) {
                    if (startItemPos == Integer.MAX_VALUE) {
                        totalWidth2 = 0 + (groupAdapter.getItemCount(startGroupPos) * itemWidth);
                    } else {
                        totalWidth2 = 0 + (((groupAdapter.getItemCount(startGroupPos) - 1) - startItemPos) * itemWidth);
                    }
                    if (endItemPos == Integer.MAX_VALUE) {
                        totalWidth3 = totalWidth2 + hintWidth;
                    } else {
                        totalWidth3 = totalWidth2 + ((endItemPos + 1) * itemWidth) + hintWidth;
                    }
                    for (int i = startGroupPos + 1; i < endGroupPos; i++) {
                        totalWidth3 = totalWidth3 + (groupAdapter.getItemCount(i) * itemWidth) + hintWidth;
                    }
                } else if (startItemPos == Integer.MAX_VALUE) {
                    totalWidth3 = 0 + ((endItemPos + 1) * itemWidth);
                } else {
                    totalWidth3 = 0 + ((endItemPos - startItemPos) * itemWidth);
                }
            } else if (direction == 33) {
                if (startGroupPos != endGroupPos) {
                    if (startItemPos == Integer.MAX_VALUE) {
                        totalWidth = 0 + (groupAdapter.getItemCount(startGroupPos) * itemWidth) + hintWidth;
                    } else {
                        totalWidth = 0 + ((groupAdapter.getItemCount(startGroupPos) - startItemPos) * itemWidth);
                    }
                    if (endItemPos != Integer.MAX_VALUE) {
                        totalWidth += (endItemPos * itemWidth) + hintWidth;
                    }
                    for (int i2 = startGroupPos + 1; i2 < endGroupPos; i2++) {
                        totalWidth = totalWidth3 + (groupAdapter.getItemCount(i2) * itemWidth) + hintWidth;
                    }
                } else if (startItemPos == Integer.MAX_VALUE) {
                    totalWidth3 = 0 + hintWidth + (endItemPos * itemWidth);
                } else {
                    totalWidth3 = 0 + ((endItemPos - startItemPos) * itemWidth);
                }
            }
            ZpLogger.i("FocusGroupHorizonalListView", "getItemDistance startPos=" + startPos + " endPos=" + endPos + " startGroupPos=" + startGroupPos + " startItemPos=" + startItemPos + " endGroupPos=" + endGroupPos + " endItemPos=" + endItemPos + " hintWidth=" + hintWidth + " itemWidth=" + itemWidth + " totalWidth=" + totalWidth3);
            return totalWidth3;
        }
    }

    private int getGroupHintWidth(GroupBaseAdapter groupAdapter) {
        Rect hintRect = groupAdapter.getGroupHintRect();
        if (hintRect != null) {
            return hintRect.width();
        }
        return 0;
    }

    private int getGroupItemWidth(GroupBaseAdapter groupAdapter) {
        Rect itemRect = groupAdapter.getGroupItemRect();
        if (itemRect != null) {
            return itemRect.width();
        }
        return 0;
    }

    public Rect getClipFocusRect() {
        if (this.mClipFocusRect != null) {
            return this.mClipFocusRect;
        }
        return new Rect();
    }
}
