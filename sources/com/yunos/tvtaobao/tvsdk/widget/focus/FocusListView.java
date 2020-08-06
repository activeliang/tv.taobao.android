package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import com.alibaba.wireless.security.SecExceptionCode;
import com.yunos.tvtaobao.tvsdk.utils.SystemProUtils;
import com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView;
import com.yunos.tvtaobao.tvsdk.widget.ListView;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DeepListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemSelectedListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;

public class FocusListView extends ListView implements DeepListener, ItemListener {
    protected static boolean DEBUG = false;
    protected static String TAG = "FocusListView";
    private boolean forceResetFocusParams = false;
    boolean mAimateWhenGainFocusFromDown = true;
    boolean mAimateWhenGainFocusFromLeft = true;
    boolean mAimateWhenGainFocusFromRight = true;
    boolean mAimateWhenGainFocusFromUp = true;
    boolean mAutoSearch = false;
    protected Rect mClipFocusRect = new Rect();
    protected DeepListener mDeep = null;
    boolean mDeepFocus = false;
    protected boolean mDeepMode = false;
    int mDistance = -1;
    boolean mFocusBackground = false;
    protected FocusRectParams mFocusRectparams = new FocusRectParams();
    protected boolean mIsAnimate = true;
    protected int mItemHeight = 0;
    ItemSelectedListener mItemSelectedListener;
    boolean mLayouted = false;
    protected boolean mNeedReset = false;
    protected Params mParams = new Params(1.1f, 1.1f, 10, (Interpolator) null, true, 20, new AccelerateDecelerateFrameInterpolator());
    protected boolean mReset = false;

    public FocusListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FocusListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusListView(Context context) {
        super(context);
    }

    public void setDeepMode(boolean mode) {
        this.mDeepMode = mode;
    }

    public void setAutoSearch(boolean autoSearch) {
        this.mAutoSearch = autoSearch;
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
        ZpLogger.d(TAG, "onFocusChanged");
        if (!this.mAutoSearch && getOnFocusChangeListener() != null) {
            getOnFocusChangeListener().onFocusChange(this, gainFocus);
        }
        if (this.mAutoSearch) {
            super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        }
        if (getChildCount() > 0) {
            if (gainFocus) {
                if (this.mLayouted && getLeftScrollDistance() == 0) {
                    reset();
                }
                if (this.mDeepMode) {
                    boolean isDeep = false;
                    if (getSelectedView() instanceof DeepListener) {
                        this.mDeep = (DeepListener) getSelectedView();
                        if (this.mDeep != null && this.mDeep.canDeep()) {
                            isDeep = true;
                            Rect focusRect = null;
                            if (previouslyFocusedRect != null) {
                                focusRect = new Rect(previouslyFocusedRect);
                                offsetRectIntoDescendantCoords((View) this.mDeep, focusRect);
                            }
                            this.mDeep.onFocusDeeped(gainFocus, direction, focusRect);
                            reset();
                        }
                    }
                    if (!isDeep) {
                        if (!this.mLayouted) {
                            this.mNeedReset = true;
                        } else {
                            reset();
                            performSelect(gainFocus);
                        }
                    }
                } else {
                    performSelect(gainFocus);
                }
            } else if (!this.mDeepMode) {
                performSelect(gainFocus);
            } else if (this.mDeep != null && this.mDeep.canDeep()) {
                this.mDeep.onFocusDeeped(gainFocus, direction, (Rect) null);
            } else if (this.mLayouted) {
                performSelect(gainFocus);
            } else {
                this.mNeedReset = true;
            }
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
        if (getVisibleChildCount() > 0 && this.mLayouted) {
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
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00c6 A[Catch:{ ClassCastException -> 0x02c2, all -> 0x0160 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00d1 A[Catch:{ ClassCastException -> 0x02c2, all -> 0x0160 }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0105 A[Catch:{ ClassCastException -> 0x02c2, all -> 0x0160 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutChildren() {
        /*
            r32 = this;
            r0 = r32
            boolean r3 = r0.mBlockLayoutRequests
            if (r3 != 0) goto L_0x0024
            r29 = 1
            r0 = r29
            r1 = r32
            r1.mBlockLayoutRequests = r0
            r32.invalidate()     // Catch:{ all -> 0x0160 }
            android.widget.ListAdapter r29 = r32.getAdapter()     // Catch:{ all -> 0x0160 }
            if (r29 != 0) goto L_0x0025
            r32.resetList()     // Catch:{ all -> 0x0160 }
            if (r3 != 0) goto L_0x0024
            r29 = 0
            r0 = r29
            r1 = r32
            r1.mBlockLayoutRequests = r0
        L_0x0024:
            return
        L_0x0025:
            r0 = r32
            android.graphics.Rect r0 = r0.mListPadding     // Catch:{ all -> 0x0160 }
            r29 = r0
            r0 = r29
            int r7 = r0.top     // Catch:{ all -> 0x0160 }
            int r29 = r32.getBottom()     // Catch:{ all -> 0x0160 }
            int r30 = r32.getTop()     // Catch:{ all -> 0x0160 }
            int r29 = r29 - r30
            r0 = r32
            android.graphics.Rect r0 = r0.mListPadding     // Catch:{ all -> 0x0160 }
            r30 = r0
            r0 = r30
            int r0 = r0.bottom     // Catch:{ all -> 0x0160 }
            r30 = r0
            int r6 = r29 - r30
            int r28 = r32.getVisibleChildCount()     // Catch:{ all -> 0x0160 }
            int r5 = r32.getChildCount()     // Catch:{ all -> 0x0160 }
            r19 = 0
            r9 = 0
            r26 = 0
            r23 = 0
            r22 = 0
            r21 = 0
            r14 = 0
            int r20 = r32.getLastPosition()     // Catch:{ all -> 0x0160 }
            r0 = r32
            boolean r8 = r0.mDataChanged     // Catch:{ all -> 0x0160 }
            if (r8 == 0) goto L_0x006d
            r29 = 9
            r0 = r29
            r1 = r32
            r1.mLayoutMode = r0     // Catch:{ all -> 0x0160 }
        L_0x006d:
            r0 = r32
            int r0 = r0.mLayoutMode     // Catch:{ all -> 0x0160 }
            r29 = r0
            switch(r29) {
                case 1: goto L_0x00c4;
                case 2: goto L_0x00e0;
                case 3: goto L_0x00c4;
                case 4: goto L_0x00c4;
                case 5: goto L_0x00c4;
                default: goto L_0x0076;
            }     // Catch:{ all -> 0x0160 }
        L_0x0076:
            r0 = r32
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            r0 = r32
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0160 }
            r30 = r0
            int r19 = r29 - r30
            if (r19 < 0) goto L_0x009a
            r0 = r19
            r1 = r28
            if (r0 >= r1) goto L_0x009a
            int r29 = r32.getUpPreLoadedCount()     // Catch:{ all -> 0x0160 }
            int r29 = r29 + r19
            r0 = r32
            r1 = r29
            android.view.View r23 = r0.getChildAt(r1)     // Catch:{ all -> 0x0160 }
        L_0x009a:
            android.view.View r22 = r32.getFirstVisibleChild()     // Catch:{ all -> 0x0160 }
            r0 = r32
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            if (r29 < 0) goto L_0x00b4
            r0 = r32
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            r0 = r32
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0160 }
            r30 = r0
            int r9 = r29 - r30
        L_0x00b4:
            int r29 = r19 + r9
            int r30 = r32.getUpPreLoadedCount()     // Catch:{ all -> 0x0160 }
            int r29 = r29 + r30
            r0 = r32
            r1 = r29
            android.view.View r21 = r0.getChildAt(r1)     // Catch:{ all -> 0x0160 }
        L_0x00c4:
            if (r8 == 0) goto L_0x00c9
            r32.handleDataChanged()     // Catch:{ all -> 0x0160 }
        L_0x00c9:
            r0 = r32
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0160 }
            r29 = r0
            if (r29 != 0) goto L_0x0105
            r32.resetList()     // Catch:{ all -> 0x0160 }
            if (r3 != 0) goto L_0x0024
            r29 = 0
            r0 = r29
            r1 = r32
            r1.mBlockLayoutRequests = r0
            goto L_0x0024
        L_0x00e0:
            r0 = r32
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            r0 = r32
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0160 }
            r30 = r0
            int r19 = r29 - r30
            if (r19 < 0) goto L_0x00c4
            r0 = r19
            r1 = r28
            if (r0 >= r1) goto L_0x00c4
            int r29 = r32.getUpPreLoadedCount()     // Catch:{ all -> 0x0160 }
            int r29 = r29 + r19
            r0 = r32
            r1 = r29
            android.view.View r21 = r0.getChildAt(r1)     // Catch:{ all -> 0x0160 }
            goto L_0x00c4
        L_0x0105:
            r0 = r32
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0160 }
            r29 = r0
            android.widget.ListAdapter r30 = r32.getAdapter()     // Catch:{ all -> 0x0160 }
            int r30 = r30.getCount()     // Catch:{ all -> 0x0160 }
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x016c
            java.lang.IllegalStateException r29 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0160 }
            java.lang.StringBuilder r30 = new java.lang.StringBuilder     // Catch:{ all -> 0x0160 }
            r30.<init>()     // Catch:{ all -> 0x0160 }
            java.lang.String r31 = "The content of the adapter has changed but ListView did not receive a notification. Make sure the content of your adapter is not modified from a background thread, but only from the UI thread. [in ListView("
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ all -> 0x0160 }
            int r31 = r32.getId()     // Catch:{ all -> 0x0160 }
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ all -> 0x0160 }
            java.lang.String r31 = ", "
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ all -> 0x0160 }
            java.lang.Class r31 = r32.getClass()     // Catch:{ all -> 0x0160 }
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ all -> 0x0160 }
            java.lang.String r31 = ") with Adapter("
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ all -> 0x0160 }
            android.widget.ListAdapter r31 = r32.getAdapter()     // Catch:{ all -> 0x0160 }
            java.lang.Class r31 = r31.getClass()     // Catch:{ all -> 0x0160 }
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ all -> 0x0160 }
            java.lang.String r31 = ")]"
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ all -> 0x0160 }
            java.lang.String r30 = r30.toString()     // Catch:{ all -> 0x0160 }
            r29.<init>(r30)     // Catch:{ all -> 0x0160 }
            throw r29     // Catch:{ all -> 0x0160 }
        L_0x0160:
            r29 = move-exception
            if (r3 != 0) goto L_0x016b
            r30 = 0
            r0 = r30
            r1 = r32
            r1.mBlockLayoutRequests = r0
        L_0x016b:
            throw r29
        L_0x016c:
            r0 = r32
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            r0 = r32
            r1 = r29
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x0160 }
            r0 = r32
            int r11 = r0.mFirstPosition     // Catch:{ all -> 0x0160 }
            r0 = r32
            com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView$RecycleBin r0 = r0.mRecycler     // Catch:{ all -> 0x0160 }
            r25 = r0
            r13 = 0
            if (r8 == 0) goto L_0x01cc
            int r12 = r32.getFirsVisibletChildIndex()     // Catch:{ ClassCastException -> 0x02c2 }
            int r18 = r12 + -1
        L_0x018c:
            if (r18 < 0) goto L_0x01aa
            r0 = r32
            r1 = r18
            android.view.View r29 = r0.getChildAt(r1)     // Catch:{ ClassCastException -> 0x02c2 }
            int r30 = r32.getUpPreLoadedCount()     // Catch:{ ClassCastException -> 0x02c2 }
            int r30 = r18 - r30
            int r30 = r30 + r11
            r0 = r25
            r1 = r29
            r2 = r30
            r0.addScrapView(r1, r2)     // Catch:{ ClassCastException -> 0x02c2 }
            int r18 = r18 + -1
            goto L_0x018c
        L_0x01aa:
            r18 = r12
        L_0x01ac:
            r0 = r18
            if (r0 >= r5) goto L_0x01d1
            r0 = r32
            r1 = r18
            android.view.View r29 = r0.getChildAt(r1)     // Catch:{ ClassCastException -> 0x02c2 }
            int r30 = r32.getUpPreLoadedCount()     // Catch:{ ClassCastException -> 0x02c2 }
            int r30 = r18 - r30
            int r30 = r30 + r11
            r0 = r25
            r1 = r29
            r2 = r30
            r0.addScrapView(r1, r2)     // Catch:{ ClassCastException -> 0x02c2 }
            int r18 = r18 + 1
            goto L_0x01ac
        L_0x01cc:
            r0 = r25
            r0.fillActiveViews(r5, r11)     // Catch:{ ClassCastException -> 0x02c2 }
        L_0x01d1:
            android.view.View r17 = r32.getFocusedChild()     // Catch:{ all -> 0x0160 }
            if (r17 == 0) goto L_0x01f1
            if (r8 == 0) goto L_0x01e3
            r0 = r32
            r1 = r17
            boolean r29 = r0.isDirectChildHeaderOrFooter(r1)     // Catch:{ all -> 0x0160 }
            if (r29 == 0) goto L_0x01ee
        L_0x01e3:
            r13 = r17
            android.view.View r14 = r32.findFocus()     // Catch:{ all -> 0x0160 }
            if (r14 == 0) goto L_0x01ee
            r14.onStartTemporaryDetach()     // Catch:{ all -> 0x0160 }
        L_0x01ee:
            r32.requestFocus()     // Catch:{ all -> 0x0160 }
        L_0x01f1:
            r32.detachAllViewsFromParent()     // Catch:{ all -> 0x0160 }
            r25.removeSkippedScrap()     // Catch:{ all -> 0x0160 }
            r29 = 0
            r0 = r29
            r1 = r32
            r1.mUpPreLoadedCount = r0     // Catch:{ all -> 0x0160 }
            r29 = 0
            r0 = r29
            r1 = r32
            r1.mDownPreLoadedCount = r0     // Catch:{ all -> 0x0160 }
            r0 = r32
            int r0 = r0.mLayoutMode     // Catch:{ all -> 0x0160 }
            r29 = r0
            switch(r29) {
                case 1: goto L_0x02dd;
                case 2: goto L_0x0210;
                case 3: goto L_0x02c8;
                case 4: goto L_0x02f0;
                case 5: goto L_0x0210;
                case 6: goto L_0x0210;
                case 7: goto L_0x0210;
                case 8: goto L_0x0210;
                case 9: goto L_0x0306;
                default: goto L_0x0210;
            }     // Catch:{ all -> 0x0160 }
        L_0x0210:
            if (r5 != 0) goto L_0x033b
            r0 = r32
            boolean r0 = r0.mStackFromBottom     // Catch:{ all -> 0x0160 }
            r29 = r0
            if (r29 != 0) goto L_0x030e
            r0 = r32
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            r30 = 1
            r0 = r32
            r1 = r29
            r2 = r30
            int r24 = r0.lookForSelectablePosition(r1, r2)     // Catch:{ all -> 0x0160 }
            r0 = r32
            r1 = r24
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x0160 }
            r0 = r32
            android.view.View r26 = r0.fillFromTop(r7)     // Catch:{ all -> 0x0160 }
        L_0x0239:
            r25.scrapActiveViews()     // Catch:{ all -> 0x0160 }
            if (r26 == 0) goto L_0x03f2
            r0 = r32
            boolean r0 = r0.mItemsCanFocus     // Catch:{ all -> 0x0160 }
            r29 = r0
            if (r29 == 0) goto L_0x03e5
            boolean r29 = r32.hasFocus()     // Catch:{ all -> 0x0160 }
            if (r29 == 0) goto L_0x03e5
            boolean r29 = r26.hasFocus()     // Catch:{ all -> 0x0160 }
            if (r29 != 0) goto L_0x03e5
            r0 = r26
            if (r0 != r13) goto L_0x025e
            if (r14 == 0) goto L_0x025e
            boolean r29 = r14.requestFocus()     // Catch:{ all -> 0x0160 }
            if (r29 != 0) goto L_0x0264
        L_0x025e:
            boolean r29 = r26.requestFocus()     // Catch:{ all -> 0x0160 }
            if (r29 == 0) goto L_0x03ce
        L_0x0264:
            r15 = 1
        L_0x0265:
            if (r15 != 0) goto L_0x03d1
            android.view.View r16 = r32.getFocusedChild()     // Catch:{ all -> 0x0160 }
            if (r16 == 0) goto L_0x0270
            r16.clearFocus()     // Catch:{ all -> 0x0160 }
        L_0x0270:
            r29 = -1
            r0 = r32
            r1 = r29
            r2 = r26
            r0.positionSelector(r1, r2)     // Catch:{ all -> 0x0160 }
        L_0x027b:
            if (r14 == 0) goto L_0x0286
            android.os.IBinder r29 = r14.getWindowToken()     // Catch:{ all -> 0x0160 }
            if (r29 == 0) goto L_0x0286
            r14.onFinishTemporaryDetach()     // Catch:{ all -> 0x0160 }
        L_0x0286:
            r29 = 0
            r0 = r29
            r1 = r32
            r1.mLayoutMode = r0     // Catch:{ all -> 0x0160 }
            r29 = 0
            r0 = r29
            r1 = r32
            r1.mDataChanged = r0     // Catch:{ all -> 0x0160 }
            r29 = 0
            r0 = r29
            r1 = r32
            r1.mNeedSync = r0     // Catch:{ all -> 0x0160 }
            r0 = r32
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            r0 = r32
            r1 = r29
            r0.setNextSelectedPositionInt(r1)     // Catch:{ all -> 0x0160 }
            r0 = r32
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0160 }
            r29 = r0
            if (r29 <= 0) goto L_0x02b6
            r32.checkSelectionChanged()     // Catch:{ all -> 0x0160 }
        L_0x02b6:
            if (r3 != 0) goto L_0x0024
            r29 = 0
            r0 = r29
            r1 = r32
            r1.mBlockLayoutRequests = r0
            goto L_0x0024
        L_0x02c2:
            r10 = move-exception
            r10.printStackTrace()     // Catch:{ all -> 0x0160 }
            goto L_0x01d1
        L_0x02c8:
            r0 = r32
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0160 }
            r29 = r0
            int r29 = r29 + -1
            r0 = r32
            r1 = r29
            android.view.View r26 = r0.fillUp(r1, r6)     // Catch:{ all -> 0x0160 }
            r32.adjustViewsUpOrDown()     // Catch:{ all -> 0x0160 }
            goto L_0x0239
        L_0x02dd:
            r29 = 0
            r0 = r29
            r1 = r32
            r1.mFirstPosition = r0     // Catch:{ all -> 0x0160 }
            r0 = r32
            android.view.View r26 = r0.fillFromTop(r7)     // Catch:{ all -> 0x0160 }
            r32.adjustViewsUpOrDown()     // Catch:{ all -> 0x0160 }
            goto L_0x0239
        L_0x02f0:
            int r29 = r32.reconcileSelectedPosition()     // Catch:{ all -> 0x0160 }
            r0 = r32
            int r0 = r0.mSpecificTop     // Catch:{ all -> 0x0160 }
            r30 = r0
            r0 = r32
            r1 = r29
            r2 = r30
            android.view.View r26 = r0.fillSpecific(r1, r2)     // Catch:{ all -> 0x0160 }
            goto L_0x0239
        L_0x0306:
            r0 = r32
            android.view.View r26 = r0.fillFromMiddle(r7, r6)     // Catch:{ all -> 0x0160 }
            goto L_0x0239
        L_0x030e:
            r0 = r32
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0160 }
            r29 = r0
            int r29 = r29 + -1
            r30 = 0
            r0 = r32
            r1 = r29
            r2 = r30
            int r24 = r0.lookForSelectablePosition(r1, r2)     // Catch:{ all -> 0x0160 }
            r0 = r32
            r1 = r24
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x0160 }
            r0 = r32
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0160 }
            r29 = r0
            int r29 = r29 + -1
            r0 = r32
            r1 = r29
            android.view.View r26 = r0.fillUp(r1, r6)     // Catch:{ all -> 0x0160 }
            goto L_0x0239
        L_0x033b:
            r0 = r32
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0160 }
            r27 = r0
            r0 = r32
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            int r30 = r11 + r9
            r0 = r29
            r1 = r30
            if (r0 < r1) goto L_0x0389
            r0 = r32
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            int r30 = r20 + r9
            r0 = r29
            r1 = r30
            if (r0 > r1) goto L_0x0389
            r0 = r32
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0160 }
            r27 = r0
        L_0x0363:
            r0 = r32
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            if (r29 < 0) goto L_0x0399
            r0 = r32
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            r0 = r32
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0160 }
            r30 = r0
            r0 = r29
            r1 = r30
            if (r0 >= r1) goto L_0x0399
            if (r23 != 0) goto L_0x0394
        L_0x037f:
            r0 = r32
            r1 = r27
            android.view.View r26 = r0.fillSpecific(r1, r7)     // Catch:{ all -> 0x0160 }
            goto L_0x0239
        L_0x0389:
            r23 = r22
            r0 = r32
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            int r27 = r29 + r9
            goto L_0x0363
        L_0x0394:
            int r7 = r23.getTop()     // Catch:{ all -> 0x0160 }
            goto L_0x037f
        L_0x0399:
            r0 = r32
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            r0 = r32
            int r0 = r0.mItemCount     // Catch:{ all -> 0x0160 }
            r30 = r0
            r0 = r29
            r1 = r30
            if (r0 >= r1) goto L_0x03c2
            r0 = r32
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            if (r22 != 0) goto L_0x03bd
        L_0x03b3:
            r0 = r32
            r1 = r29
            android.view.View r26 = r0.fillSpecific(r1, r7)     // Catch:{ all -> 0x0160 }
            goto L_0x0239
        L_0x03bd:
            int r7 = r22.getTop()     // Catch:{ all -> 0x0160 }
            goto L_0x03b3
        L_0x03c2:
            r29 = 0
            r0 = r32
            r1 = r29
            android.view.View r26 = r0.fillSpecific(r1, r7)     // Catch:{ all -> 0x0160 }
            goto L_0x0239
        L_0x03ce:
            r15 = 0
            goto L_0x0265
        L_0x03d1:
            r29 = 0
            r0 = r26
            r1 = r29
            r0.setSelected(r1)     // Catch:{ all -> 0x0160 }
            r0 = r32
            android.graphics.Rect r0 = r0.mSelectorRect     // Catch:{ all -> 0x0160 }
            r29 = r0
            r29.setEmpty()     // Catch:{ all -> 0x0160 }
            goto L_0x027b
        L_0x03e5:
            r29 = -1
            r0 = r32
            r1 = r29
            r2 = r26
            r0.positionSelector(r1, r2)     // Catch:{ all -> 0x0160 }
            goto L_0x027b
        L_0x03f2:
            r0 = r32
            int r0 = r0.mTouchMode     // Catch:{ all -> 0x0160 }
            r29 = r0
            if (r29 <= 0) goto L_0x043a
            r0 = r32
            int r0 = r0.mTouchMode     // Catch:{ all -> 0x0160 }
            r29 = r0
            r30 = 3
            r0 = r29
            r1 = r30
            if (r0 >= r1) goto L_0x043a
            r0 = r32
            int r0 = r0.mMotionPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            r0 = r32
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x0160 }
            r30 = r0
            int r29 = r29 - r30
            r0 = r32
            r1 = r29
            android.view.View r4 = r0.getChildAt(r1)     // Catch:{ all -> 0x0160 }
            if (r4 == 0) goto L_0x042d
            r0 = r32
            int r0 = r0.mMotionPosition     // Catch:{ all -> 0x0160 }
            r29 = r0
            r0 = r32
            r1 = r29
            r0.positionSelector(r1, r4)     // Catch:{ all -> 0x0160 }
        L_0x042d:
            boolean r29 = r32.hasFocus()     // Catch:{ all -> 0x0160 }
            if (r29 == 0) goto L_0x027b
            if (r14 == 0) goto L_0x027b
            r14.requestFocus()     // Catch:{ all -> 0x0160 }
            goto L_0x027b
        L_0x043a:
            r0 = r32
            android.graphics.Rect r0 = r0.mSelectorRect     // Catch:{ all -> 0x0160 }
            r29 = r0
            r29.setEmpty()     // Catch:{ all -> 0x0160 }
            goto L_0x042d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.tvsdk.widget.focus.FocusListView.layoutChildren():void");
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if ((hasFocus() || hasDeepFocus()) && getLeftScrollDistance() == 0) {
            reset();
        }
        if (getChildCount() > 0) {
            if (this.mNeedReset) {
                performSelect(hasFocus() || hasDeepFocus());
                this.mNeedReset = false;
            }
            if (hasFocus() && this.mDeepMode) {
                boolean isDeep = false;
                if (getSelectedView() instanceof DeepListener) {
                    this.mDeep = (DeepListener) getSelectedView();
                    if (this.mDeep != null && this.mDeep.canDeep() && !this.mDeep.hasDeepFocus()) {
                        isDeep = true;
                        this.mDeep.onFocusDeeped(true, 17, (Rect) null);
                        reset();
                    }
                }
                if (!isDeep) {
                    reset();
                }
            }
        }
        this.mLayouted = true;
        this.mClipFocusRect.set(0, 0, getWidth(), getHeight());
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
            if (getAdapter().getCount() > 0) {
                index = 0;
            }
            if (getHeaderViewsCount() > 0 && getAdapter().getCount() > getHeaderViewsCount()) {
                index = getHeaderViewsCount();
            }
            measureItemHeight(index);
        }
    }

    /* access modifiers changed from: protected */
    public void measureItemHeight(int index) {
        View child = obtainView(index, this.mIsScrap);
        int measureHeight = 0;
        if (child != null) {
            measureHeight = child.getMeasuredHeight();
            if (((AbsBaseListView.LayoutParams) child.getLayoutParams()) == null) {
                child.setLayoutParams((AbsBaseListView.LayoutParams) generateDefaultLayoutParams());
            }
            if (measureHeight == 0 && child.getLayoutParams() != null) {
                child.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                measureHeight = child.getMeasuredHeight();
            }
        }
        this.mItemHeight = measureHeight;
    }

    public Params getParams() {
        if (this.mParams != null) {
            return this.mParams;
        }
        throw new IllegalArgumentException("The params is null, you must call setScaleParams before it's running");
    }

    public void setParams(Params params) {
        this.mParams = params;
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

    /* access modifiers changed from: protected */
    public void reset() {
        if (getSelectedView() != null) {
            if (this.mDeep != null) {
                this.mFocusRectparams.set(this.mDeep.getFocusParams());
            } else {
                ItemListener item = (ItemListener) getSelectedView();
                if (item != null) {
                    this.mFocusRectparams.set(item.getFocusParams());
                }
            }
            offsetDescendantRectToMyCoords(getSelectedView(), this.mFocusRectparams.focusRect());
        }
    }

    /* access modifiers changed from: protected */
    public void resetHeader(int nextSelectedPosition) {
        int height;
        View header = getHeaderView(nextSelectedPosition);
        ItemListener item = (ItemListener) header;
        if (item != null) {
            this.mFocusRectparams.set(item.getFocusParams());
        }
        int top = getChildAt(0).getTop();
        for (int index = getFirstPosition() - 1; index >= 0; index--) {
            if (index >= getHeaderViewsCount()) {
                height = this.mItemHeight;
            } else {
                height = getHeaderView(index).getHeight();
            }
            top -= height;
        }
        this.mFocusRectparams.focusRect().top = top;
        this.mFocusRectparams.focusRect().bottom = header.getHeight() + top;
    }

    public void forceResetFocusParams(FocusPositionManager positionManager) {
        if (positionManager != null) {
            this.forceResetFocusParams = true;
            positionManager.forceDrawFocus();
        }
    }

    public FocusRectParams getFocusParams() {
        View v = getSelectedView();
        if (!isFocusedOrSelected() || v == null) {
            Rect r = new Rect();
            getFocusedRect(r);
            this.mFocusRectparams.set(r, 0.5f, 0.5f);
            return this.mFocusRectparams;
        }
        if (this.mFocusRectparams == null || isScrolling()) {
            reset();
        } else if (this.forceResetFocusParams) {
            this.forceResetFocusParams = false;
            reset();
        }
        return this.mFocusRectparams;
    }

    private boolean isFocusedOrSelected() {
        return isFocused() || isSelected();
    }

    public boolean isFocused() {
        return super.isFocused() || hasFocus() || hasDeepFocus();
    }

    public boolean canDraw() {
        if (this.mDeep != null) {
            return this.mDeep.canDraw();
        }
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
        if (this.mDeep != null) {
            return this.mDeep.isAnimate();
        }
        return this.mIsAnimate;
    }

    public boolean onKeyDownDeep(int keyCode, KeyEvent event) {
        if (!this.mDeepMode || this.mDeep == null || !this.mDeep.canDeep() || !this.mDeep.hasDeepFocus() || !this.mDeep.onKeyDown(keyCode, event)) {
            return false;
        }
        reset();
        offsetFocusRect(0, -getLeftScrollDistance());
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ZpLogger.d(TAG, "onKeyDown keyCode = " + keyCode);
        if (onKeyDownDeep(keyCode, event)) {
            return true;
        }
        if (getChildCount() <= 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (checkState(keyCode)) {
            return true;
        }
        if (this.mDistance < 0) {
            this.mDistance = getChildAt(0).getHeight();
        }
        switch (keyCode) {
            case 19:
                if (moveUp()) {
                    playSoundEffect(1);
                    return true;
                }
                break;
            case 20:
                if (moveDown()) {
                    playSoundEffect(1);
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mDeep == null || !this.mDeep.canDeep() || !this.mDeep.hasDeepFocus()) {
            return super.onKeyUp(keyCode, event);
        }
        return this.mDeep.onKeyUp(keyCode, event);
    }

    /* access modifiers changed from: protected */
    public boolean moveUp() {
        int nextSelectedPosition = getNextSelectedPosition(33);
        if (this.mDeepMode && getChildAt(nextSelectedPosition - getFirstPosition()) == null) {
            return true;
        }
        performSelect(false);
        this.mReset = false;
        if (nextSelectedPosition == -1) {
            return false;
        }
        setSelectedPositionInt(nextSelectedPosition);
        setNextSelectedPositionInt(nextSelectedPosition);
        if (this.mDeepMode) {
            View lastSelectView = (View) this.mDeep;
            Rect focusRect = null;
            if (this.mDeep != null) {
                focusRect = new Rect(this.mDeep.getFocusParams().focusRect());
                this.mDeep.onFocusDeeped(false, 33, (Rect) null);
            }
            this.mDeep = null;
            DeepListener deep = (DeepListener) getSelectedView();
            if (deep != null && deep.canDeep()) {
                this.mDeep = deep;
                if (!(focusRect == null || lastSelectView == null)) {
                    offsetDescendantRectToMyCoords(lastSelectView, focusRect);
                    offsetRectIntoDescendantCoords(getSelectedView(), focusRect);
                }
                this.mDeep.onFocusDeeped(true, 33, focusRect);
            }
        }
        if (canDraw()) {
            this.mReset = false;
            performSelect(true);
        } else {
            this.mReset = true;
        }
        int amountToCenterScroll = amountToCenterScroll(33, nextSelectedPosition);
        return true;
    }

    /* access modifiers changed from: protected */
    public void performSelect(boolean select) {
        if (this.mItemSelectedListener != null) {
            this.mItemSelectedListener.onItemSelected(getSelectedView(), getSelectedItemPosition(), select, this);
        }
    }

    public int getNextSelectedPosition(int direction) {
        int i = 1;
        boolean isDown = direction == 130;
        int selectedItemPosition = getSelectedItemPosition();
        if (!isDown) {
            i = -1;
        }
        int nextSelectedPosition = lookForSelectablePosition(i + selectedItemPosition, isDown);
        if (nextSelectedPosition >= 0) {
            return nextSelectedPosition;
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public boolean moveDown() {
        int nextSelectedPosition = getNextSelectedPosition(130);
        if (this.mDeepMode && getChildAt(nextSelectedPosition - getFirstPosition()) == null) {
            return true;
        }
        performSelect(false);
        this.mReset = false;
        if (nextSelectedPosition == -1) {
            return false;
        }
        setSelectedPositionInt(nextSelectedPosition);
        setNextSelectedPositionInt(nextSelectedPosition);
        if (this.mDeepMode) {
            View lastSelectView = (View) this.mDeep;
            Rect focusRect = null;
            if (this.mDeep != null) {
                focusRect = new Rect(this.mDeep.getFocusParams().focusRect());
                this.mDeep.onFocusDeeped(false, 130, (Rect) null);
            }
            this.mDeep = null;
            DeepListener deep = (DeepListener) getSelectedView();
            if (deep != null && deep.canDeep()) {
                this.mDeep = deep;
                if (!(focusRect == null || lastSelectView == null)) {
                    ViewParent theParent = lastSelectView.getParent();
                    while (theParent != null && (theParent instanceof View) && theParent != this) {
                        theParent = ((View) theParent).getParent();
                    }
                    if (theParent == this) {
                        offsetDescendantRectToMyCoords(lastSelectView, focusRect);
                        offsetRectIntoDescendantCoords(getSelectedView(), focusRect);
                    }
                }
                this.mDeep.onFocusDeeped(true, 130, focusRect);
            }
        }
        if (canDraw()) {
            this.mReset = false;
            performSelect(true);
        } else {
            this.mReset = true;
        }
        int amountToCenterScroll = amountToCenterScroll(130, nextSelectedPosition);
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
                    if (DEBUG) {
                        ZpLogger.i(TAG, "amountToCenterScroll: focus rect = " + this.mFocusRectparams.focusRect() + ", distanceLeft = " + distanceLeft + ", nextSelectedPosition = " + nextSelectedPosition);
                    }
                }
                if (amountToScroll > 0) {
                    if (reset2) {
                        offsetFocusRect(0, -amountToScroll);
                    } else {
                        offsetFocusRect(0, (nextSelctedView.getHeight() + this.mSpacing) - amountToScroll);
                    }
                    if (DEBUG) {
                        ZpLogger.d(TAG, "amountToCenterScroll: focus down amountToScroll = " + amountToScroll + ", focus rect = " + this.mFocusRectparams.focusRect());
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
                    if (DEBUG) {
                        ZpLogger.i(TAG, "amountToCenterScroll: focus rect = " + this.mFocusRectparams.focusRect() + ", distanceLeft = " + distanceLeft + ", nextSelectedPosition = " + nextSelectedPosition);
                    }
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
                    if (DEBUG) {
                        ZpLogger.d(TAG, "amountToCenterScroll: focus down amountToScroll = " + amountToScroll + ", focus rect = " + this.mFocusRectparams.focusRect());
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

    public ItemListener getItem() {
        if (this.mDeep == null || !this.mDeep.hasDeepFocus()) {
            return (ItemListener) getSelectedView();
        }
        return this.mDeep.getItem();
    }

    public boolean isScrolling() {
        boolean isScrolling;
        if (this.mLastScrollState != 0) {
            isScrolling = true;
        } else {
            isScrolling = false;
        }
        if (this.mDeep == null) {
            return isScrolling;
        }
        if (this.mDeep.isScrolling() || isScrolling) {
            return true;
        }
        return false;
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        if (this.mDeep != null && this.mDeep.preOnKeyDown(keyCode, event)) {
            return true;
        }
        switch (keyCode) {
            case 19:
            case 92:
            case SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE:
                if (getNextSelectedPosition(33) == -1) {
                    return false;
                }
                return true;
            case 20:
            case 93:
            case 123:
                if (getNextSelectedPosition(130) == -1) {
                    return false;
                }
                return true;
            case 21:
            case 22:
                return false;
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
        if (this.mDeep != null) {
            return this.mDeep.isFocusBackground();
        }
        return this.mFocusBackground;
    }

    public void drawBeforeFocus(Canvas canvas) {
    }

    public void drawAfterFocus(Canvas canvas) {
    }

    public boolean isFinished() {
        return true;
    }

    public void onFocusStart() {
        if (this.mDeep != null) {
            this.mDeep.onFocusStart();
        } else {
            super.onFocusStart();
        }
    }

    public void onFocusFinished() {
        if (this.mDeep != null) {
            this.mDeep.onFocusFinished();
        } else {
            super.onFocusFinished();
        }
    }

    public boolean performItemClick(View view, int position, long id) {
        if (this.mDeep == null) {
            return super.performItemClick(view, position, id);
        }
        this.mDeep.onItemClick();
        return true;
    }

    public void offsetFocusRect(int offsetX, int offsetY) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            this.mFocusRectparams.focusRect().offset(offsetX, offsetY);
        }
    }

    public Rect getClipFocusRect() {
        return this.mClipFocusRect;
    }
}
