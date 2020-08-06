package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Interpolator;
import anetwork.channel.util.RequestConstant;
import com.alibaba.wireless.security.SecExceptionCode;
import com.yunos.tvtaobao.tvsdk.widget.HListView;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DeepListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemSelectedListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;

public class FocusGroupHorizonalListView extends HListView implements DeepListener, ItemListener {
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

    public FocusGroupHorizonalListView(Context context) {
        super(context);
    }

    public FocusGroupHorizonalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusGroupHorizonalListView(Context context, AttributeSet attrs, int defStyle) {
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
    /* JADX WARNING: Removed duplicated region for block: B:26:0x009f A[Catch:{ all -> 0x0128 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a8 A[Catch:{ all -> 0x0128 }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00cf A[Catch:{ all -> 0x0128 }] */
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
            r28.invalidate()     // Catch:{ all -> 0x0128 }
            android.widget.ListAdapter r2 = r28.getAdapter()     // Catch:{ all -> 0x0128 }
            if (r2 != 0) goto L_0x001f
            r28.resetList()     // Catch:{ all -> 0x0128 }
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r28
            r0.mBlockLayoutRequests = r2
        L_0x001e:
            return
        L_0x001f:
            r0 = r28
            android.graphics.Rect r2 = r0.mListPadding     // Catch:{ all -> 0x0128 }
            int r6 = r2.left     // Catch:{ all -> 0x0128 }
            int r2 = r28.getRight()     // Catch:{ all -> 0x0128 }
            int r26 = r28.getLeft()     // Catch:{ all -> 0x0128 }
            int r2 = r2 - r26
            r0 = r28
            android.graphics.Rect r0 = r0.mListPadding     // Catch:{ all -> 0x0128 }
            r26 = r0
            r0 = r26
            int r0 = r0.right     // Catch:{ all -> 0x0128 }
            r26 = r0
            int r7 = r2 - r26
            int r10 = r28.getChildCount()     // Catch:{ all -> 0x0128 }
            r19 = 0
            r5 = 0
            r24 = 0
            r3 = 0
            r21 = 0
            r4 = 0
            r14 = 0
            int r20 = r28.getLastVisiblePosition()     // Catch:{ all -> 0x0128 }
            r0 = r28
            boolean r11 = r0.mDataChanged     // Catch:{ all -> 0x0128 }
            if (r11 == 0) goto L_0x005b
            r2 = 9
            r0 = r28
            r0.mLayoutMode = r2     // Catch:{ all -> 0x0128 }
        L_0x005b:
            r0 = r28
            int r2 = r0.mLayoutMode     // Catch:{ all -> 0x0128 }
            switch(r2) {
                case 2: goto L_0x00b4;
                case 3: goto L_0x0062;
                case 4: goto L_0x009d;
                case 5: goto L_0x009d;
                case 6: goto L_0x0062;
                case 7: goto L_0x009d;
                case 8: goto L_0x009d;
                default: goto L_0x0062;
            }     // Catch:{ all -> 0x0128 }
        L_0x0062:
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0128 }
            r26 = r0
            int r19 = r2 - r26
            if (r19 < 0) goto L_0x007c
            r0 = r19
            if (r0 >= r10) goto L_0x007c
            r0 = r28
            r1 = r19
            android.view.View r3 = r0.getChildAt(r1)     // Catch:{ all -> 0x0128 }
        L_0x007c:
            r2 = 0
            r0 = r28
            android.view.View r21 = r0.getChildAt(r2)     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0128 }
            if (r2 < 0) goto L_0x0095
            r0 = r28
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0128 }
            r26 = r0
            int r5 = r2 - r26
        L_0x0095:
            int r2 = r19 + r5
            r0 = r28
            android.view.View r4 = r0.getChildAt(r2)     // Catch:{ all -> 0x0128 }
        L_0x009d:
            if (r11 == 0) goto L_0x00a2
            r28.handleDataChanged()     // Catch:{ all -> 0x0128 }
        L_0x00a2:
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0128 }
            if (r2 != 0) goto L_0x00cf
            r28.resetList()     // Catch:{ all -> 0x0128 }
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r28
            r0.mBlockLayoutRequests = r2
            goto L_0x001e
        L_0x00b4:
            r0 = r28
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0128 }
            r26 = r0
            int r19 = r2 - r26
            if (r19 < 0) goto L_0x009d
            r0 = r19
            if (r0 >= r10) goto L_0x009d
            r0 = r28
            r1 = r19
            android.view.View r4 = r0.getChildAt(r1)     // Catch:{ all -> 0x0128 }
            goto L_0x009d
        L_0x00cf:
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0128 }
            android.widget.ListAdapter r26 = r28.getAdapter()     // Catch:{ all -> 0x0128 }
            int r26 = r26.getCount()     // Catch:{ all -> 0x0128 }
            r0 = r26
            if (r2 == r0) goto L_0x0134
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0128 }
            java.lang.StringBuilder r26 = new java.lang.StringBuilder     // Catch:{ all -> 0x0128 }
            r26.<init>()     // Catch:{ all -> 0x0128 }
            java.lang.String r27 = "The content of the adapter has changed but ListView did not receive a notification. Make sure the content of your adapter is not modified from a background thread, but only from the UI thread. [in ListView("
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0128 }
            int r27 = r28.getId()     // Catch:{ all -> 0x0128 }
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0128 }
            java.lang.String r27 = ", "
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0128 }
            java.lang.Class r27 = r28.getClass()     // Catch:{ all -> 0x0128 }
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0128 }
            java.lang.String r27 = ") with Adapter("
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0128 }
            android.widget.ListAdapter r27 = r28.getAdapter()     // Catch:{ all -> 0x0128 }
            java.lang.Class r27 = r27.getClass()     // Catch:{ all -> 0x0128 }
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0128 }
            java.lang.String r27 = ")]"
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ all -> 0x0128 }
            java.lang.String r26 = r26.toString()     // Catch:{ all -> 0x0128 }
            r0 = r26
            r2.<init>(r0)     // Catch:{ all -> 0x0128 }
            throw r2     // Catch:{ all -> 0x0128 }
        L_0x0128:
            r2 = move-exception
            if (r8 != 0) goto L_0x0133
            r26 = 0
            r0 = r26
            r1 = r28
            r1.mBlockLayoutRequests = r0
        L_0x0133:
            throw r2
        L_0x0134:
            r0 = r28
            int r2 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            r0.setSelectedPositionInt(r2)     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r12 = r0.mFirstPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView$RecycleBin r0 = r0.mRecycler     // Catch:{ all -> 0x0128 }
            r23 = r0
            r13 = 0
            if (r11 == 0) goto L_0x0164
            r18 = 0
        L_0x014c:
            r0 = r18
            if (r0 >= r10) goto L_0x0169
            r0 = r28
            r1 = r18
            android.view.View r2 = r0.getChildAt(r1)     // Catch:{ all -> 0x0128 }
            int r26 = r12 + r18
            r0 = r23
            r1 = r26
            r0.addScrapView(r2, r1)     // Catch:{ all -> 0x0128 }
            int r18 = r18 + 1
            goto L_0x014c
        L_0x0164:
            r0 = r23
            r0.fillActiveViews(r10, r12)     // Catch:{ all -> 0x0128 }
        L_0x0169:
            android.view.View r17 = r28.getFocusedChild()     // Catch:{ all -> 0x0128 }
            if (r17 == 0) goto L_0x0189
            if (r11 == 0) goto L_0x017b
            r0 = r28
            r1 = r17
            boolean r2 = r0.isDirectChildHeaderOrFooter(r1)     // Catch:{ all -> 0x0128 }
            if (r2 == 0) goto L_0x0186
        L_0x017b:
            r13 = r17
            android.view.View r14 = r28.findFocus()     // Catch:{ all -> 0x0128 }
            if (r14 == 0) goto L_0x0186
            r14.onStartTemporaryDetach()     // Catch:{ all -> 0x0128 }
        L_0x0186:
            r28.requestFocus()     // Catch:{ all -> 0x0128 }
        L_0x0189:
            r28.detachAllViewsFromParent()     // Catch:{ all -> 0x0128 }
            r23.removeSkippedScrap()     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r2 = r0.mLayoutMode     // Catch:{ all -> 0x0128 }
            switch(r2) {
                case 2: goto L_0x022b;
                case 3: goto L_0x0196;
                case 4: goto L_0x027d;
                case 5: goto L_0x0240;
                case 6: goto L_0x0254;
                case 7: goto L_0x026d;
                case 8: goto L_0x025c;
                case 9: goto L_0x0291;
                default: goto L_0x0196;
            }     // Catch:{ all -> 0x0128 }
        L_0x0196:
            if (r10 != 0) goto L_0x02be
            r0 = r28
            boolean r2 = r0.mStackFromBottom     // Catch:{ all -> 0x0128 }
            if (r2 != 0) goto L_0x0299
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0128 }
            r26 = 1
            r0 = r28
            r1 = r26
            int r22 = r0.lookForSelectablePosition(r2, r1)     // Catch:{ all -> 0x0128 }
            r0 = r28
            r1 = r22
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x0128 }
            r0 = r28
            android.view.View r24 = r0.fillFromLeft(r6)     // Catch:{ all -> 0x0128 }
        L_0x01b9:
            r23.scrapActiveViews()     // Catch:{ all -> 0x0128 }
            if (r24 == 0) goto L_0x0352
            r0 = r28
            boolean r2 = r0.mItemsCanFocus     // Catch:{ all -> 0x0128 }
            if (r2 == 0) goto L_0x0348
            boolean r2 = r28.hasFocus()     // Catch:{ all -> 0x0128 }
            if (r2 == 0) goto L_0x0348
            boolean r2 = r24.hasFocus()     // Catch:{ all -> 0x0128 }
            if (r2 != 0) goto L_0x0348
            r0 = r24
            if (r0 != r13) goto L_0x01dc
            if (r14 == 0) goto L_0x01dc
            boolean r2 = r14.requestFocus()     // Catch:{ all -> 0x0128 }
            if (r2 != 0) goto L_0x01e2
        L_0x01dc:
            boolean r2 = r24.requestFocus()     // Catch:{ all -> 0x0128 }
            if (r2 == 0) goto L_0x0336
        L_0x01e2:
            r15 = 1
        L_0x01e3:
            if (r15 != 0) goto L_0x0339
            android.view.View r16 = r28.getFocusedChild()     // Catch:{ all -> 0x0128 }
            if (r16 == 0) goto L_0x01ee
            r16.clearFocus()     // Catch:{ all -> 0x0128 }
        L_0x01ee:
            r2 = -1
            r0 = r28
            r1 = r24
            r0.positionSelector(r2, r1)     // Catch:{ all -> 0x0128 }
        L_0x01f6:
            if (r14 == 0) goto L_0x0201
            android.os.IBinder r2 = r14.getWindowToken()     // Catch:{ all -> 0x0128 }
            if (r2 == 0) goto L_0x0201
            r14.onFinishTemporaryDetach()     // Catch:{ all -> 0x0128 }
        L_0x0201:
            r2 = 0
            r0 = r28
            r0.mLayoutMode = r2     // Catch:{ all -> 0x0128 }
            r2 = 0
            r0 = r28
            r0.mDataChanged = r2     // Catch:{ all -> 0x0128 }
            r2 = 0
            r0 = r28
            r0.mNeedSync = r2     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            r0.setNextSelectedPositionInt(r2)     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0128 }
            if (r2 <= 0) goto L_0x0222
            r28.checkSelectionChanged()     // Catch:{ all -> 0x0128 }
        L_0x0222:
            if (r8 != 0) goto L_0x001e
            r2 = 0
            r0 = r28
            r0.mBlockLayoutRequests = r2
            goto L_0x001e
        L_0x022b:
            if (r4 == 0) goto L_0x0238
            int r2 = r4.getLeft()     // Catch:{ all -> 0x0128 }
            r0 = r28
            android.view.View r24 = r0.fillFromSelection(r2, r6, r7)     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x0238:
            r0 = r28
            android.view.View r24 = r0.fillFromMiddle(r6, r7)     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x0240:
            r0 = r28
            int r2 = r0.mSyncPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r0 = r0.mSpecificLeft     // Catch:{ all -> 0x0128 }
            r26 = r0
            r0 = r28
            r1 = r26
            android.view.View r24 = r0.fillSpecific(r2, r1)     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x0254:
            r2 = r28
            android.view.View r24 = r2.moveSelection(r3, r4, r5, r6, r7)     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x025c:
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0128 }
            int r2 = r2 + -1
            r0 = r28
            android.view.View r24 = r0.fillLeft(r2, r7)     // Catch:{ all -> 0x0128 }
            r28.adjustViewsLeftOrRight()     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x026d:
            r2 = 0
            r0 = r28
            r0.mFirstPosition = r2     // Catch:{ all -> 0x0128 }
            r0 = r28
            android.view.View r24 = r0.fillFromLeft(r6)     // Catch:{ all -> 0x0128 }
            r28.adjustViewsLeftOrRight()     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x027d:
            int r2 = r28.reconcileSelectedPosition()     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r0 = r0.mSpecificLeft     // Catch:{ all -> 0x0128 }
            r26 = r0
            r0 = r28
            r1 = r26
            android.view.View r24 = r0.fillSpecific(r2, r1)     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x0291:
            r0 = r28
            android.view.View r24 = r0.fillFromMiddle(r6, r7)     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x0299:
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0128 }
            int r2 = r2 + -1
            r26 = 0
            r0 = r28
            r1 = r26
            int r22 = r0.lookForSelectablePosition(r2, r1)     // Catch:{ all -> 0x0128 }
            r0 = r28
            r1 = r22
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r2 = r0.mItemCount     // Catch:{ all -> 0x0128 }
            int r2 = r2 + -1
            r0 = r28
            android.view.View r24 = r0.fillLeft(r2, r7)     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x02be:
            r0 = r28
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0128 }
            r25 = r0
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0128 }
            int r26 = r12 + r5
            r0 = r26
            if (r2 < r0) goto L_0x02fe
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0128 }
            int r26 = r20 + r5
            r0 = r26
            if (r2 > r0) goto L_0x02fe
            r0 = r28
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0128 }
            r25 = r0
        L_0x02de:
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0128 }
            if (r2 < 0) goto L_0x030c
            r0 = r28
            int r2 = r0.mSelectedPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0128 }
            r26 = r0
            r0 = r26
            if (r2 >= r0) goto L_0x030c
            if (r3 != 0) goto L_0x0307
        L_0x02f4:
            r0 = r28
            r1 = r25
            android.view.View r24 = r0.fillSpecific(r1, r6)     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x02fe:
            r3 = r21
            r0 = r28
            int r2 = r0.mFirstPosition     // Catch:{ all -> 0x0128 }
            int r25 = r2 + r5
            goto L_0x02de
        L_0x0307:
            int r6 = r3.getLeft()     // Catch:{ all -> 0x0128 }
            goto L_0x02f4
        L_0x030c:
            r0 = r28
            int r2 = r0.mFirstPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0128 }
            r26 = r0
            r0 = r26
            if (r2 >= r0) goto L_0x032d
            r0 = r28
            int r2 = r0.mFirstPosition     // Catch:{ all -> 0x0128 }
            if (r21 != 0) goto L_0x0328
        L_0x0320:
            r0 = r28
            android.view.View r24 = r0.fillSpecific(r2, r6)     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x0328:
            int r6 = r21.getLeft()     // Catch:{ all -> 0x0128 }
            goto L_0x0320
        L_0x032d:
            r2 = 0
            r0 = r28
            android.view.View r24 = r0.fillSpecific(r2, r6)     // Catch:{ all -> 0x0128 }
            goto L_0x01b9
        L_0x0336:
            r15 = 0
            goto L_0x01e3
        L_0x0339:
            r2 = 0
            r0 = r24
            r0.setSelected(r2)     // Catch:{ all -> 0x0128 }
            r0 = r28
            android.graphics.Rect r2 = r0.mSelectorRect     // Catch:{ all -> 0x0128 }
            r2.setEmpty()     // Catch:{ all -> 0x0128 }
            goto L_0x01f6
        L_0x0348:
            r2 = -1
            r0 = r28
            r1 = r24
            r0.positionSelector(r2, r1)     // Catch:{ all -> 0x0128 }
            goto L_0x01f6
        L_0x0352:
            r0 = r28
            int r2 = r0.mTouchMode     // Catch:{ all -> 0x0128 }
            if (r2 <= 0) goto L_0x038c
            r0 = r28
            int r2 = r0.mTouchMode     // Catch:{ all -> 0x0128 }
            r26 = 3
            r0 = r26
            if (r2 >= r0) goto L_0x038c
            r0 = r28
            int r2 = r0.mMotionPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0128 }
            r26 = r0
            int r2 = r2 - r26
            r0 = r28
            android.view.View r9 = r0.getChildAt(r2)     // Catch:{ all -> 0x0128 }
            if (r9 == 0) goto L_0x037f
            r0 = r28
            int r2 = r0.mMotionPosition     // Catch:{ all -> 0x0128 }
            r0 = r28
            r0.positionSelector(r2, r9)     // Catch:{ all -> 0x0128 }
        L_0x037f:
            boolean r2 = r28.hasFocus()     // Catch:{ all -> 0x0128 }
            if (r2 == 0) goto L_0x01f6
            if (r14 == 0) goto L_0x01f6
            r14.requestFocus()     // Catch:{ all -> 0x0128 }
            goto L_0x01f6
        L_0x038c:
            r0 = r28
            android.graphics.Rect r2 = r0.mSelectorRect     // Catch:{ all -> 0x0128 }
            r2.setEmpty()     // Catch:{ all -> 0x0128 }
            goto L_0x037f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.widget.FocusGroupHorizonalListView.layoutChildren():void");
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
        if (this.mLastScrollState == 2 && (keyCode == 19 || keyCode == 20)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public int amountToCenterScroll(int direction, int nextSelectedPosition) {
        int nextSelectedCenter;
        boolean reset;
        int nextSelectedCenter2;
        boolean reset2;
        int center = (((getWidth() - this.mListPadding.left) - this.mListPadding.right) / 2) + this.mListPadding.left;
        int listRight = getWidth() - this.mListPadding.right;
        int listLeft = this.mListPadding.left;
        int numChildren = getChildCount();
        int amountToScroll = 0;
        int distanceLeft = getLeftScrollDistance();
        if (direction == 130) {
            View nextSelctedView = getChildAt(nextSelectedPosition - this.mFirstPosition);
            if (nextSelctedView == null) {
                nextSelctedView = getChildAt(getChildCount() - 1);
                nextSelectedCenter2 = ((nextSelctedView.getLeft() + nextSelctedView.getRight()) / 2) + (nextSelctedView.getWidth() * (nextSelectedPosition - getLastVisiblePosition()));
                reset2 = false;
            } else {
                nextSelectedCenter2 = (nextSelctedView.getLeft() + nextSelctedView.getRight()) / 2;
                reset2 = true;
            }
            int finalNextSelectedCenter = nextSelectedCenter2 - distanceLeft;
            if (finalNextSelectedCenter > center) {
                amountToScroll = finalNextSelectedCenter - center;
                int maxDiff = getItemDistance(getLastVisiblePosition(), this.mItemCount - 1, 130) - distanceLeft;
                View lastVisibleView = getChildAt(numChildren - 1);
                if (lastVisibleView.getRight() > listRight) {
                    maxDiff += lastVisibleView.getRight() - listRight;
                }
                if (amountToScroll > maxDiff) {
                    amountToScroll = maxDiff;
                }
                if (reset2) {
                    reset();
                    this.mFocusRectparams.focusRect().offset(-distanceLeft, 0);
                }
                if (amountToScroll > 0) {
                    if (reset2) {
                        this.mFocusRectparams.focusRect().offset(-amountToScroll, 0);
                    } else {
                        this.mFocusRectparams.focusRect().offset(nextSelctedView.getWidth() - amountToScroll, 0);
                    }
                    smoothScrollBy(amountToScroll);
                    this.mIsAnimate = true;
                } else {
                    if (!reset2) {
                        this.mFocusRectparams.focusRect().offset(nextSelctedView.getWidth(), 0);
                    }
                    this.mIsAnimate = true;
                }
            } else {
                reset();
                this.mFocusRectparams.focusRect().offset(-distanceLeft, 0);
                this.mIsAnimate = true;
            }
            return amountToScroll;
        } else if (direction != 33) {
            return 0;
        } else {
            View nextSelctedView2 = getChildAt(nextSelectedPosition - this.mFirstPosition);
            ZpLogger.i(RequestConstant.ENV_TEST, "FOCUS_UP nextSelctedView=" + nextSelctedView2 + " distanceLeft=" + distanceLeft);
            if (nextSelctedView2 == null) {
                nextSelctedView2 = getChildAt(0);
                int nextSelectedCenter3 = (nextSelctedView2.getLeft() + nextSelctedView2.getRight()) / 2;
                if (nextSelectedPosition >= getHeaderViewsCount()) {
                    nextSelectedCenter = nextSelectedCenter3 - (nextSelctedView2.getWidth() * (getFirstVisiblePosition() - nextSelectedPosition));
                } else {
                    nextSelectedCenter = nextSelectedCenter3 - (nextSelctedView2.getWidth() * (getFirstVisiblePosition() - getHeaderViewsCount()));
                    for (int i = getHeaderViewsCount() - 1; i >= nextSelectedPosition; i--) {
                        nextSelectedCenter -= getHeaderView(i).getWidth();
                    }
                }
                reset = false;
            } else {
                nextSelectedCenter = (nextSelctedView2.getLeft() + nextSelctedView2.getRight()) / 2;
                ZpLogger.i(RequestConstant.ENV_TEST, "nextSelectedCenter=" + nextSelectedCenter + " left=" + nextSelctedView2.getLeft() + " right=" + nextSelctedView2.getRight());
                reset = true;
            }
            int finalNextSelectedCenter2 = nextSelectedCenter - distanceLeft;
            if (finalNextSelectedCenter2 < center) {
                int amountToScroll2 = center - finalNextSelectedCenter2;
                int maxDiff2 = 0;
                if (getFirstVisiblePosition() >= getHeaderViewsCount()) {
                    maxDiff2 = getItemDistance(getHeaderViewsCount(), getFirstVisiblePosition(), 33);
                }
                int start = getHeaderViewsCount() - 1;
                if (start > getFirstVisiblePosition() - 1) {
                    start = getFirstVisiblePosition() - 1;
                }
                for (int i2 = start; i2 >= 0; i2--) {
                    maxDiff2 += getHeaderView(i2).getWidth();
                }
                if (maxDiff2 < 0) {
                    maxDiff2 = 0;
                }
                int maxDiff3 = maxDiff2 + distanceLeft;
                View firstVisibleView = getChildAt(0);
                if (firstVisibleView.getLeft() < listLeft) {
                    maxDiff3 += listLeft - firstVisibleView.getLeft();
                }
                if (amountToScroll2 > maxDiff3) {
                    amountToScroll2 = maxDiff3;
                }
                if (reset) {
                    reset();
                    this.mFocusRectparams.focusRect().offset(-distanceLeft, 0);
                } else if (nextSelectedPosition < getHeaderViewsCount()) {
                    reset = true;
                    resetHeader(nextSelectedPosition);
                    this.mFocusRectparams.focusRect().offset(-distanceLeft, 0);
                }
                if (amountToScroll > 0) {
                    if (reset) {
                        this.mFocusRectparams.focusRect().offset(amountToScroll, 0);
                    } else {
                        this.mFocusRectparams.focusRect().offset(-(nextSelctedView2.getWidth() - amountToScroll), 0);
                    }
                    smoothScrollBy(-amountToScroll);
                    this.mIsAnimate = true;
                } else {
                    if (!reset) {
                        this.mFocusRectparams.focusRect().offset(-nextSelctedView2.getWidth(), 0);
                    }
                    this.mIsAnimate = true;
                }
            } else {
                reset();
                this.mFocusRectparams.focusRect().offset(-distanceLeft, 0);
                this.mIsAnimate = true;
            }
            return amountToScroll;
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
            case 20:
            case 23:
            case 66:
                return true;
            case 21:
            case SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE:
                if (getSelectedItemPosition() <= 0) {
                    return false;
                }
                return true;
            case 22:
            case 123:
                if (getSelectedItemPosition() >= this.mItemCount - 1) {
                    return false;
                }
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
        if (keyCode == 21) {
            if (getSelectedItemPosition() - 1 >= 0) {
                nextSelectedPosition = getSelectedItemPosition() - 1;
            }
            View nextSelectedView = getChildAt(nextSelectedPosition - getFirstVisiblePosition());
            ZpLogger.i("FocusGroupHorizonalListView", "onKeyDown KEYCODE_DPAD_LEFT nextSelectedPosition=" + nextSelectedPosition + " nextSelectedView=" + nextSelectedView);
            if (nextSelectedView == null) {
                return true;
            }
        } else if (keyCode == 22) {
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
            case 21:
                if (moveLeft()) {
                    playSoundEffect(1);
                    return true;
                }
                break;
            case 22:
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
