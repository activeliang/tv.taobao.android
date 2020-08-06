package com.yunos.tvtaobao.biz.focus_impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.LinearInterpolator;
import com.alibaba.analytics.core.device.Constants;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class FakeListView extends ViewGroup {
    private static final int FLAG2_need_init = 4;
    private static final int FLAG_notify_layout_routine = 1;
    private static final int FLAG_scroll_doing = 2;
    private static final int FLAG_view_detached = 8;
    private static final int FLAG_view_ready = 4;
    private static final int MSG_NOTIFY_FINISH = 3;
    private static final int MSG_SCROLL_ANIM_END = 1;
    private static final int MSG_SCROLL_TASK_NOT_RUN = 2;
    public static final int NO_POS = -1;
    /* access modifiers changed from: private */
    public static final String TAG = FakeListView.class.getSimpleName();
    private Adapter adapter;
    private List<Animator> animatorsRecord;
    private Pair<Integer, Integer> cachedMS;
    private List<ViewHolder> cachedViewHolders;
    /* access modifiers changed from: private */
    public int flag;
    private int flag2;
    private int gravity;
    /* access modifiers changed from: private */
    public Handler handler;
    /* access modifiers changed from: private */
    public int immediatelyMovePos;
    private int itemMargin;
    private int layoutCount;
    /* access modifiers changed from: private */
    public LayoutDoneListener layoutDoneListener;
    private OnLayoutParams onLayoutParams;
    private boolean reverseLayoutFlag;
    /* access modifiers changed from: private */
    public int stepY;
    /* access modifiers changed from: private */
    public Style style;
    private List<Runnable> taskCache4AfterNotifyAndLayout;
    private List<Runnable> taskCache4BeforeNotifyAndLayout;
    private List<Runnable> taskCache4ScrollDone;
    /* access modifiers changed from: private */
    public List<ViewHolder> usingViewHolders;

    public interface LayoutDoneListener {
        void onLayoutDone(int i, int i2);
    }

    public enum Style {
        noEdgeLimit,
        edgeLimit
    }

    public FakeListView(Context context) {
        this(context, (AttributeSet) null);
    }

    public FakeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FakeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.flag = 0;
        this.flag2 = 0;
        this.handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                    case 2:
                    case 3:
                        boolean unused = FakeListView.this.handleFirstTaskInScrollTaskCache();
                        if (msg.what == 3) {
                            FakeListView.this.handleTaskCache4PstNotifyAndLayout();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.layoutDoneListener = null;
        this.cachedViewHolders = new ArrayList();
        this.usingViewHolders = new ArrayList();
        this.taskCache4ScrollDone = new ArrayList();
        this.taskCache4BeforeNotifyAndLayout = new ArrayList();
        this.taskCache4AfterNotifyAndLayout = new ArrayList();
        this.animatorsRecord = new ArrayList();
        this.onLayoutParams = null;
        this.style = Style.noEdgeLimit;
        this.gravity = 48;
        this.reverseLayoutFlag = false;
        this.stepY = 0;
        this.itemMargin = 5;
        this.layoutCount = 0;
        this.immediatelyMovePos = -1;
        init();
    }

    private void init() {
    }

    /* access modifiers changed from: protected */
    public int tagObjHashCode() {
        return hashCode();
    }

    public boolean isInEditMode() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View tmp = getChildAt(i);
            if (tmp.getMeasuredWidth() > width) {
                width = tmp.getMeasuredWidth();
            }
            height += tmp.getMeasuredHeight();
        }
        int mW = getDefaultSize(width, widthMeasureSpec);
        int mH = getDefaultSize(height, heightMeasureSpec);
        ZpLogger.d(TAG, tagObjHashCode() + " onMeasure (" + getChildCount() + "):" + mW + "," + mH);
        setMeasuredDimension(mW, mH);
        this.cachedMS = new Pair<>(Integer.valueOf(widthMeasureSpec), Integer.valueOf(heightMeasureSpec));
        if (!isViewReady()) {
            this.flag |= 4;
            if (this.adapter != null) {
                this.adapter.handler.sendEmptyMessage(1);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        synchronized (this) {
            doLayout(changed, l, t, r, b, "onLayout");
        }
    }

    /* access modifiers changed from: protected */
    public void doLayout(boolean changed, int l, int t, int r, int b, String from) {
        this.layoutCount++;
        this.onLayoutParams = new OnLayoutParams(changed, l, t, r, b);
        layoutStep1();
        layoutStep2();
        layoutStep3();
        this.stepY = 0;
        ZpLogger.d(TAG, tagObjHashCode() + " doLayout from " + from + " " + this.layoutCount + " done ! " + "(children:" + getChildCount() + ", viewHolderSize:" + this.usingViewHolders.size() + ")");
        if (isNeedInitLayout()) {
            this.flag2 &= -5;
        }
        if (isNotifyDoing()) {
            this.flag &= -2;
            this.handler.sendEmptyMessage(3);
        }
        notifyLayoutDoneListener(changed, l, t, r, b);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        ZpLogger.d(TAG, tagObjHashCode() + " dispatchDraw done");
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.flag |= 8;
        this.handler.removeCallbacks((Runnable) null);
        clearAnimatorsRecord();
    }

    private void notifyLayoutDoneListener(boolean changed, int l, final int t, int r, final int b) {
        post(new Runnable() {
            public void run() {
                try {
                    if (FakeListView.this.getChildCount() > 0) {
                        View top = FakeListView.this.getChildAt(0);
                        View bottom = FakeListView.this.getChildAt(FakeListView.this.getChildCount() - 1);
                        if (FakeListView.this.layoutDoneListener != null) {
                            FakeListView.this.layoutDoneListener.onLayoutDone(bottom.getBottom() - top.getTop(), b - t);
                        }
                    } else if (FakeListView.this.layoutDoneListener != null) {
                        FakeListView.this.layoutDoneListener.onLayoutDone(0, b - t);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean isDetachedFromWindow() {
        return (this.flag & 8) == 8;
    }

    private boolean isNeedInitLayout() {
        return (this.flag2 & 4) == 4;
    }

    private ViewHolder getInCache(int viewType) {
        ViewHolder vh = null;
        int i = 0;
        while (true) {
            if (i >= this.cachedViewHolders.size()) {
                break;
            }
            ViewHolder tmp = this.cachedViewHolders.get(i);
            if (tmp.viewType == viewType) {
                vh = tmp;
                break;
            }
            i++;
        }
        this.cachedViewHolders.remove(vh);
        return vh;
    }

    public int getGravity() {
        return this.gravity;
    }

    public void setGravity(int gravity2) {
        this.gravity = gravity2;
    }

    public void setAdapter(Adapter a) {
        this.adapter = a;
        if (this.adapter != null) {
            WeakReference unused = this.adapter.fakeListViewWeakReference = new WeakReference(this);
            this.adapter.dataSet.addObserver(new Observer() {
                public void update(Observable o, final Object arg) {
                    int unused = FakeListView.this.flag = FakeListView.this.flag | 1;
                    FakeListView.this.post(new Runnable() {
                        public void run() {
                            try {
                                FakeListView.this.handleTaskCache4BeforeNotifyAndLayout();
                                synchronized (FakeListView.this) {
                                    FakeListView.this.onAdapterDataSetChange((Adapter.NotifyParam) arg);
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            FakeListView.this.requestLayout();
                            FakeListView.this.invalidate();
                        }
                    });
                }
            });
            if (isViewReady()) {
                this.adapter.handler.sendEmptyMessage(1);
            }
        }
    }

    public Adapter getAdapter() {
        return this.adapter;
    }

    public LayoutDoneListener getLayoutDoneListener() {
        return this.layoutDoneListener;
    }

    public void setLayoutDoneListener(LayoutDoneListener layoutDoneListener2) {
        this.layoutDoneListener = layoutDoneListener2;
    }

    public int getItemMargin() {
        return this.itemMargin;
    }

    public void setItemMargin(int itemMargin2) {
        this.itemMargin = itemMargin2;
    }

    public boolean isReverseLayoutFlag() {
        return this.reverseLayoutFlag;
    }

    public void setReverseLayoutFlag(boolean reverseLayoutFlag2) {
        this.reverseLayoutFlag = reverseLayoutFlag2;
    }

    public Style getStyle() {
        return this.style;
    }

    public void setStyle(Style style2) {
        this.style = style2;
    }

    public boolean isViewReady() {
        return (this.flag & 4) == 4;
    }

    public boolean isNotifyDoing() {
        return (this.flag & 1) == 1;
    }

    public boolean isScrollDoing() {
        return (this.flag & 2) == 2;
    }

    private static void clearMeasure(View itemView) {
        if (itemView == null) {
            return;
        }
        if (itemView instanceof ViewGroup) {
            itemView.forceLayout();
            for (int i = 0; i < ((ViewGroup) itemView).getChildCount(); i++) {
                clearMeasure(((ViewGroup) itemView).getChildAt(i));
            }
            return;
        }
        itemView.forceLayout();
    }

    /* access modifiers changed from: protected */
    public void onAdapterDataSetChange(Adapter.NotifyParam notifyParam) {
        ViewHolder vh;
        int shouldStartForm;
        ZpLogger.d(TAG, tagObjHashCode() + " onAdapterDataSetChange : " + "reverseLayoutFlag=" + this.reverseLayoutFlag + ", itemCount=" + getAdapter().getItemCount() + " " + notifyParam.toString());
        clearAnimatorsRecord();
        if (notifyParam.notifyType == Adapter.NotifyType.all) {
            if (getAdapter().getItemCount() == 0) {
                this.flag2 |= 4;
                this.cachedViewHolders.addAll(this.usingViewHolders);
                removeAllViews();
                this.usingViewHolders.clear();
                ZpLogger.e(TAG, tagObjHashCode() + " onAdapterDataSetChange : " + Adapter.NotifyType.all + " clear !");
                return;
            }
            int bgn = -1;
            int end = -1;
            for (int i = 0; i < this.usingViewHolders.size(); i++) {
                ViewHolder tmp = this.usingViewHolders.get(i);
                if (i == 0) {
                    bgn = tmp.posNow;
                }
                if (i == this.usingViewHolders.size() - 1) {
                    end = tmp.posNow;
                }
            }
            ZpLogger.e(TAG, tagObjHashCode() + " onAdapterDataSetChange : " + Adapter.NotifyType.all + "(" + bgn + "," + end + ")");
            if (bgn > this.adapter.getItemCount() - 1) {
                ZpLogger.e(TAG, tagObjHashCode() + " onAdapterDataSetChange : bgn > adapter.getItemCount() - 1  | " + (this.adapter.getItemCount() - 1));
                this.flag2 |= 4;
                this.cachedViewHolders.addAll(this.usingViewHolders);
                removeAllViews();
                this.usingViewHolders.clear();
                int height = (getMeasuredHeight() - getPaddingBottom()) - getPaddingTop();
                int loopCount = 100;
                int j = 1;
                for (int i2 = this.adapter.getItemCount() - 1; i2 > 0 && j > 0; i2--) {
                    loopCount--;
                    int viewType = this.adapter.getItemViewType(i2);
                    ViewHolder vh2 = getInCache(viewType);
                    if (vh2 == null) {
                        vh2 = this.adapter.onCreateViewHolder(this, viewType);
                    }
                    int unused = vh2.posNow = i2;
                    int unused2 = vh2.viewType = viewType;
                    addView(vh2.itemView, 0);
                    this.adapter.onBindViewHolder(vh2, vh2.posNow);
                    clearMeasure(vh2.itemView);
                    measureChild(vh2.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
                    height -= vh2.itemView.getMeasuredHeight();
                    int unused3 = vh2.posNow = i2;
                    this.usingViewHolders.add(0, vh2);
                    if (height < 0) {
                        j--;
                    }
                    if (loopCount <= 0) {
                        ZpLogger.e(TAG, tagObjHashCode() + " onAdapterDataSetChange : may be too more item visible .");
                        return;
                    }
                }
            } else if (bgn > this.adapter.getItemCount() - 1 || bgn < 0) {
                this.flag2 |= 4;
                int height2 = (getMeasuredHeight() - getPaddingBottom()) - getPaddingTop();
                for (int i3 = 0; i3 < this.usingViewHolders.size(); i3++) {
                    ViewHolder vh3 = this.usingViewHolders.get(i3);
                    this.adapter.onBindViewHolder(vh3, vh3.posNow);
                    clearMeasure(vh3.itemView);
                    measureChild(vh3.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
                    vh3.itemView.invalidate();
                    height2 -= vh3.itemView.getMeasuredHeight();
                }
                int loopCount2 = 100;
                if (!this.reverseLayoutFlag) {
                    int j2 = 1;
                    for (int i4 = 0; i4 < this.adapter.getItemCount() && j2 > 0; i4++) {
                        loopCount2--;
                        int viewType2 = this.adapter.getItemViewType(i4);
                        ViewHolder vh4 = getInCache(viewType2);
                        if (vh4 == null) {
                            vh4 = this.adapter.onCreateViewHolder(this, viewType2);
                        }
                        int unused4 = vh4.posNow = i4;
                        int unused5 = vh4.viewType = viewType2;
                        addView(vh4.itemView);
                        this.adapter.onBindViewHolder(vh4, vh4.posNow);
                        clearMeasure(vh4.itemView);
                        measureChild(vh4.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
                        vh4.itemView.invalidate();
                        height2 -= vh4.itemView.getMeasuredHeight();
                        this.usingViewHolders.add(vh4);
                        if (height2 < 0) {
                            j2--;
                        }
                        if (loopCount2 <= 0) {
                            ZpLogger.e(TAG, tagObjHashCode() + " onAdapterDataSetChange : may be too more item visible .");
                            return;
                        }
                    }
                    return;
                }
                int j3 = 1;
                for (int i5 = this.adapter.getItemCount() - 1; i5 >= 0 && j3 > 0; i5--) {
                    loopCount2--;
                    int viewType3 = this.adapter.getItemViewType(i5);
                    ViewHolder vh5 = getInCache(viewType3);
                    if (vh5 == null) {
                        vh5 = this.adapter.onCreateViewHolder(this, viewType3);
                    }
                    int unused6 = vh5.posNow = i5;
                    int unused7 = vh5.viewType = viewType3;
                    addView(vh5.itemView, 0);
                    this.adapter.onBindViewHolder(vh5, vh5.posNow);
                    clearMeasure(vh5.itemView);
                    measureChild(vh5.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
                    vh5.itemView.invalidate();
                    height2 -= vh5.itemView.getMeasuredHeight();
                    int unused8 = vh5.posNow = i5;
                    this.usingViewHolders.add(0, vh5);
                    if (height2 < 0) {
                        j3--;
                    }
                    if (loopCount2 <= 0) {
                        ZpLogger.e(TAG, tagObjHashCode() + " onAdapterDataSetChange : may be too more item visible .");
                        return;
                    }
                }
            } else {
                ZpLogger.e(TAG, tagObjHashCode() + " onAdapterDataSetChange : bgn <= adapter.getItemCount() - 1 && bgn >= 0  | " + (this.adapter.getItemCount() - 1));
                for (int i6 = this.usingViewHolders.size() - 1; i6 >= 0; i6--) {
                    ViewHolder tmp2 = this.usingViewHolders.get(i6);
                    if (tmp2.posNow > this.adapter.getItemCount() - 1) {
                        this.usingViewHolders.remove(i6);
                        removeView(tmp2.itemView);
                        this.cachedViewHolders.add(tmp2);
                    } else {
                        if (tmp2.viewType != this.adapter.getItemViewType(tmp2.posNow)) {
                            this.usingViewHolders.remove(i6);
                            removeView(tmp2.itemView);
                            this.cachedViewHolders.add(tmp2);
                        }
                    }
                }
                if (this.usingViewHolders.isEmpty()) {
                    this.flag2 |= 4;
                }
                if (this.usingViewHolders.isEmpty()) {
                    shouldStartForm = bgn;
                } else {
                    shouldStartForm = this.usingViewHolders.get(this.usingViewHolders.size() - 1).posNow + 1;
                }
                ZpLogger.e(TAG, tagObjHashCode() + " onAdapterDataSetChange : shouldStartForm " + shouldStartForm);
                for (int i7 = 0; i7 < this.usingViewHolders.size(); i7++) {
                    ViewHolder vh6 = this.usingViewHolders.get(i7);
                    this.adapter.onBindViewHolder(vh6, vh6.posNow);
                    clearMeasure(vh6.itemView);
                    measureChild(vh6.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
                }
                int nowContentBottom = 0;
                int leftStart = getPaddingLeft();
                int topStart = Integer.MIN_VALUE;
                if (getChildCount() > 0) {
                    nowContentBottom = getChildAt(getChildCount() - 1).getBottom();
                    topStart = nowContentBottom + this.itemMargin;
                }
                int height3 = getMeasuredHeight() - nowContentBottom;
                int loopCount3 = 100;
                int i8 = shouldStartForm;
                int j4 = 1;
                while (i8 < this.adapter.getItemCount() && j4 > 0) {
                    loopCount3--;
                    if (height3 >= 0) {
                        int viewType4 = this.adapter.getItemViewType(i8);
                        ViewHolder vh7 = getInCache(viewType4);
                        if (vh7 == null) {
                            vh7 = this.adapter.onCreateViewHolder(this, viewType4);
                        }
                        int unused9 = vh7.posNow = i8;
                        int unused10 = vh7.viewType = viewType4;
                        addView(vh7.itemView);
                        this.adapter.onBindViewHolder(vh7, vh7.posNow);
                        clearMeasure(vh7.itemView);
                        measureChild(vh7.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
                        height3 -= vh7.itemView.getMeasuredHeight();
                        this.usingViewHolders.add(vh7);
                        if (topStart != Integer.MIN_VALUE) {
                            vh7.itemView.layout(leftStart, topStart, vh7.itemView.getMeasuredWidth() + leftStart, vh7.itemView.getMeasuredHeight() + topStart);
                            topStart = topStart + vh7.itemView.getMeasuredHeight() + this.itemMargin;
                        }
                        if (height3 < 0) {
                            j4--;
                        }
                        if (loopCount3 <= 0) {
                            ZpLogger.e(TAG, tagObjHashCode() + " onAdapterDataSetChange : may be too more item visible .");
                            return;
                        }
                        i8++;
                    } else {
                        return;
                    }
                }
            }
        } else if (notifyParam.notifyType == Adapter.NotifyType.single && (vh = getViewHolder(notifyParam.singlePos)) != null) {
            this.adapter.onBindViewHolder(vh, notifyParam.singlePos);
            clearMeasure(vh.itemView);
            measureChild(vh.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
            vh.itemView.invalidate();
            ZpLogger.e(TAG, tagObjHashCode() + " onAdapterDataSetChange : " + Adapter.NotifyType.single + "(+notifyParam.singlePos+)");
        }
    }

    /* access modifiers changed from: protected */
    public void layoutStep1() {
        int bottomStart;
        int topStart;
        int leftStart = getPaddingLeft();
        int topStart2 = 0;
        int bottomStart2 = 0;
        ZpLogger.d(TAG, tagObjHashCode() + " layoutStep1:" + "usingViewHolders-size=" + this.usingViewHolders.size() + " , paddings=[" + getPaddingLeft() + "," + getPaddingTop() + "," + getPaddingRight() + "," + getPaddingBottom() + "]" + " , reverseLayoutFlag=" + this.reverseLayoutFlag + " , style=" + this.style + " , stepY=" + this.stepY + " , immediatelyMovePos=" + this.immediatelyMovePos);
        if (this.immediatelyMovePos != -1) {
            int initPos = this.immediatelyMovePos;
            this.immediatelyMovePos = -1;
            if (initPos >= 0 && initPos < getAdapter().getItemCount()) {
                ViewHolder vh4InitPos = null;
                for (int i = 0; i < this.usingViewHolders.size(); i++) {
                    ViewHolder vh = this.usingViewHolders.get(i);
                    if (vh.posNow == initPos) {
                        vh4InitPos = vh;
                    }
                }
                Rect myRect = new Rect();
                getDrawingRect(myRect);
                if (vh4InitPos != null) {
                    Rect itemRect = new Rect();
                    vh4InitPos.itemView.getDrawingRect(itemRect);
                    offsetDescendantRectToMyCoords(vh4InitPos.itemView, itemRect);
                    int distance = myRect.centerY() - itemRect.centerY();
                    for (int i2 = 0; i2 < getChildCount(); i2++) {
                        View tmp = getChildAt(i2);
                        if (i2 == 0) {
                            topStart2 = tmp.getTop() + distance;
                            ZpLogger.d(TAG, tagObjHashCode() + " layoutStep1:" + " real topStart=" + topStart2);
                        }
                        tmp.layout(leftStart, topStart2, tmp.getMeasuredWidth() + leftStart, tmp.getMeasuredHeight() + topStart2);
                        topStart2 += tmp.getMeasuredHeight();
                        if (i2 != getChildCount() - 1) {
                            topStart2 += this.itemMargin;
                        }
                    }
                    return;
                }
                this.cachedViewHolders.addAll(this.usingViewHolders);
                removeAllViews();
                this.usingViewHolders.clear();
                int contentTop = myRect.centerY();
                int contentBottom = myRect.centerY();
                int pre = initPos - 1;
                int pst = initPos + 1;
                int loopCount = 20;
                while (loopCount > 0) {
                    if (loopCount == 20) {
                        int viewType = getAdapter().getItemViewType(initPos);
                        ViewHolder vh2 = getInCache(viewType);
                        if (vh2 == null) {
                            vh2 = getAdapter().onCreateViewHolder(this, viewType);
                        }
                        int unused = vh2.posNow = initPos;
                        addViewInLayout(vh2.itemView, 0, vh2.itemView.getLayoutParams());
                        this.usingViewHolders.add(0, vh2);
                        getAdapter().onBindViewHolder(vh2, vh2.posNow);
                        clearMeasure(vh2.itemView);
                        measureChild(vh2.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
                        contentTop -= vh2.itemView.getMeasuredHeight() / 2;
                        contentBottom += vh2.itemView.getMeasuredHeight() / 2;
                        vh2.itemView.layout(leftStart, contentTop, vh2.itemView.getMeasuredWidth() + leftStart, vh2.itemView.getMeasuredHeight() + contentTop);
                    }
                    if (pre >= 0) {
                        int viewType2 = getAdapter().getItemViewType(pre);
                        ViewHolder vh3 = getInCache(viewType2);
                        if (vh3 == null) {
                            vh3 = getAdapter().onCreateViewHolder(this, viewType2);
                        }
                        int unused2 = vh3.posNow = pre;
                        addViewInLayout(vh3.itemView, 0, vh3.itemView.getLayoutParams());
                        this.usingViewHolders.add(0, vh3);
                        getAdapter().onBindViewHolder(vh3, vh3.posNow);
                        clearMeasure(vh3.itemView);
                        measureChild(vh3.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
                        contentTop = (contentTop - vh3.itemView.getMeasuredHeight()) - this.itemMargin;
                        vh3.itemView.layout(leftStart, contentTop, vh3.itemView.getMeasuredWidth() + leftStart, vh3.itemView.getMeasuredHeight() + contentTop);
                    }
                    if (pst < getAdapter().getItemCount()) {
                        int viewType3 = getAdapter().getItemViewType(pst);
                        ViewHolder vh4 = getInCache(viewType3);
                        if (vh4 == null) {
                            vh4 = getAdapter().onCreateViewHolder(this, viewType3);
                        }
                        int unused3 = vh4.posNow = pst;
                        addViewInLayout(vh4.itemView, -1, vh4.itemView.getLayoutParams());
                        this.usingViewHolders.add(vh4);
                        getAdapter().onBindViewHolder(vh4, vh4.posNow);
                        clearMeasure(vh4.itemView);
                        measureChild(vh4.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
                        contentBottom = vh4.itemView.getMeasuredHeight() + contentBottom + this.itemMargin;
                        vh4.itemView.layout(leftStart, contentBottom - vh4.itemView.getMeasuredHeight(), vh4.itemView.getMeasuredWidth() + leftStart, contentBottom);
                    }
                    if (contentBottom - contentTop <= myRect.height()) {
                        pre--;
                        pst++;
                        loopCount--;
                    } else {
                        return;
                    }
                }
                return;
            }
        }
        if (!this.reverseLayoutFlag) {
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View tmp2 = getChildAt(i3);
                if (i3 == 0) {
                    if (isNeedInitLayout()) {
                        topStart = getPaddingTop();
                    } else {
                        topStart = tmp2.getTop();
                    }
                    topStart2 = topStart + this.stepY;
                    ZpLogger.d(TAG, tagObjHashCode() + " layoutStep1:" + " real topStart=" + topStart2);
                }
                ZpLogger.d(TAG, tagObjHashCode() + " layoutStep1:" + " old child(" + i3 + ") [" + tmp2.getLeft() + "," + tmp2.getTop() + "," + tmp2.getRight() + "," + tmp2.getBottom() + "]");
                tmp2.layout(leftStart, topStart2, tmp2.getMeasuredWidth() + leftStart, tmp2.getMeasuredHeight() + topStart2);
                ZpLogger.d(TAG, tagObjHashCode() + " layoutStep1:" + " new child(" + i3 + ") [" + tmp2.getLeft() + "," + tmp2.getTop() + "," + tmp2.getRight() + "," + tmp2.getBottom() + "]");
                int topStart3 = topStart2 + tmp2.getMeasuredHeight();
                if (i3 != getChildCount() - 1) {
                    topStart3 += this.itemMargin;
                }
            }
            return;
        }
        for (int i4 = getChildCount() - 1; i4 >= 0; i4--) {
            View tmp3 = getChildAt(i4);
            if (i4 == getChildCount() - 1) {
                if (isNeedInitLayout()) {
                    bottomStart = getMeasuredHeight() - getPaddingBottom();
                } else {
                    bottomStart = tmp3.getBottom();
                }
                bottomStart2 = bottomStart + this.stepY;
                ZpLogger.d(TAG, tagObjHashCode() + " layoutStep1:" + " real bottomStart=" + bottomStart2);
            }
            ZpLogger.d(TAG, tagObjHashCode() + " layoutStep1:" + " old child(" + i4 + ") [" + tmp3.getLeft() + "," + tmp3.getTop() + "," + tmp3.getRight() + "," + tmp3.getBottom() + "]");
            tmp3.layout(leftStart, bottomStart2 - tmp3.getMeasuredHeight(), tmp3.getMeasuredWidth() + leftStart, bottomStart2);
            ZpLogger.d(TAG, tagObjHashCode() + " layoutStep1:" + " new child(" + i4 + ") [" + tmp3.getLeft() + "," + tmp3.getTop() + "," + tmp3.getRight() + "," + tmp3.getBottom() + "]");
            bottomStart2 -= tmp3.getMeasuredHeight();
            if (i4 != 0) {
                bottomStart2 -= this.itemMargin;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void layoutMoreAtBottomIfNeed() {
        ViewHolder viewHolder = this.usingViewHolders.get(0);
        ViewHolder end = this.usingViewHolders.get(this.usingViewHolders.size() - 1);
        if (end.itemView.getBottom() - (getMeasuredHeight() - getPaddingBottom()) < end.itemView.getHeight() && end.posNow < this.adapter.getItemCount() - 1) {
            int newPos = end.posNow + 1;
            int newPosViewType = this.adapter.getItemViewType(newPos);
            ViewHolder newVH = getInCache(newPosViewType);
            if (newVH == null) {
                newVH = this.adapter.onCreateViewHolder(this, newPosViewType);
            }
            int unused = newVH.posNow = newPos;
            this.usingViewHolders.add(newVH);
            addViewInLayout(newVH.itemView, -1, newVH.itemView.getLayoutParams());
            this.adapter.onBindViewHolder(newVH, newPos);
            clearMeasure(newVH.itemView);
            measureChild(newVH.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
            newVH.itemView.invalidate();
            if (newVH.itemView.getVisibility() != 8) {
                measureChild(newVH.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
            }
            Rect tmp = new Rect(getPaddingLeft(), end.itemView.getBottom() + this.itemMargin, 0, 0);
            tmp.right = tmp.left + newVH.itemView.getMeasuredWidth();
            tmp.bottom = tmp.top + newVH.itemView.getMeasuredHeight();
            ZpLogger.d(TAG, tagObjHashCode() + " layoutMoreAtBottomIfNeed:" + " old child(" + (getChildCount() - 1) + ") [" + newVH.itemView.getLeft() + "," + newVH.itemView.getTop() + "," + newVH.itemView.getRight() + "," + newVH.itemView.getBottom() + "]");
            newVH.itemView.layout(tmp.left, tmp.top, tmp.right, tmp.bottom);
            ZpLogger.d(TAG, tagObjHashCode() + " layoutMoreAtBottomIfNeed:" + " new child(" + (getChildCount() - 1) + ") [" + newVH.itemView.getLeft() + "," + newVH.itemView.getTop() + "," + newVH.itemView.getRight() + "," + newVH.itemView.getBottom() + "]");
            ZpLogger.d(TAG, tagObjHashCode() + " layoutMoreAtBottomIfNeed: layout more at bottom " + newPos);
            layoutStep2();
        }
    }

    /* access modifiers changed from: protected */
    public void layoutMoreAtTopIfNeed() {
        ViewHolder bgn = this.usingViewHolders.get(0);
        ViewHolder viewHolder = this.usingViewHolders.get(this.usingViewHolders.size() - 1);
        if (getPaddingTop() - bgn.itemView.getTop() < bgn.itemView.getHeight() && bgn.posNow > 0) {
            int newPos = bgn.posNow - 1;
            int newPosViewType = this.adapter.getItemViewType(newPos);
            ViewHolder newVH = getInCache(newPosViewType);
            if (newVH == null) {
                newVH = this.adapter.onCreateViewHolder(this, newPosViewType);
            }
            int unused = newVH.posNow = newPos;
            this.usingViewHolders.add(0, newVH);
            addViewInLayout(newVH.itemView, 0, newVH.itemView.getLayoutParams());
            this.adapter.onBindViewHolder(newVH, newPos);
            clearMeasure(newVH.itemView);
            measureChild(newVH.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
            newVH.itemView.invalidate();
            if (newVH.itemView.getVisibility() != 8) {
                measureChild(newVH.itemView, ((Integer) this.cachedMS.first).intValue(), ((Integer) this.cachedMS.second).intValue());
            }
            Rect tmp = new Rect(getPaddingLeft(), 0, 0, bgn.itemView.getTop() - this.itemMargin);
            tmp.right = tmp.left + newVH.itemView.getMeasuredWidth();
            tmp.top = tmp.bottom - newVH.itemView.getMeasuredHeight();
            ZpLogger.d(TAG, tagObjHashCode() + " layoutMoreAtBottomIfNeed:" + " old child(" + 0 + ") [" + newVH.itemView.getLeft() + "," + newVH.itemView.getTop() + "," + newVH.itemView.getRight() + "," + newVH.itemView.getBottom() + "]");
            newVH.itemView.layout(tmp.left, tmp.top, tmp.right, tmp.bottom);
            ZpLogger.d(TAG, tagObjHashCode() + " layoutMoreAtBottomIfNeed:" + " new child(" + 0 + ") [" + newVH.itemView.getLeft() + "," + newVH.itemView.getTop() + "," + newVH.itemView.getRight() + "," + newVH.itemView.getBottom() + "]");
            ZpLogger.d(TAG, tagObjHashCode() + " layoutMoreAtTopIfNeed: layout more at top " + newPos);
            layoutStep2();
        }
    }

    /* access modifiers changed from: protected */
    public void layoutStep2() {
        if (!this.usingViewHolders.isEmpty()) {
            ZpLogger.d(TAG, tagObjHashCode() + " layoutStep2:" + "usingViewHolders-size=" + this.usingViewHolders.size() + " , bgn-top=" + this.usingViewHolders.get(0).itemView.getTop() + " , end-bottom=" + this.usingViewHolders.get(this.usingViewHolders.size() - 1).itemView.getBottom() + " , height=" + getMeasuredHeight());
            layoutMoreAtBottomIfNeed();
            layoutMoreAtTopIfNeed();
        }
    }

    /* access modifiers changed from: protected */
    public void layoutStep3() {
        if (this.gravity == 48) {
            if (getChildCount() > 0) {
                View top = getChildAt(0);
                View bottom = getChildAt(getChildCount() - 1);
                ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: gravity=" + this.gravity + " top [" + top.getLeft() + "," + top.getTop() + "," + top.getRight() + "," + top.getBottom() + "]" + " bottom [" + bottom.getLeft() + "," + bottom.getTop() + "," + bottom.getRight() + "," + bottom.getBottom() + "]");
                if (top.getTop() >= getPaddingTop()) {
                    int stepY2 = getPaddingTop() - top.getTop();
                    int topStart = top.getTop() + stepY2;
                    for (int i = 0; i < getChildCount(); i++) {
                        View tmp = getChildAt(i);
                        tmp.layout(tmp.getLeft(), topStart, tmp.getRight(), tmp.getMeasuredHeight() + topStart);
                        topStart += tmp.getMeasuredHeight();
                        if (i != getChildCount() - 1) {
                            topStart += this.itemMargin;
                        }
                    }
                    ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: align to top " + stepY2);
                } else if (this.style == Style.edgeLimit) {
                    if (((float) (bottom.getBottom() - top.getTop())) >= ((float) getMeasuredHeight())) {
                        if (bottom.getBottom() < getMeasuredHeight() - getPaddingBottom()) {
                            int stepY3 = (getMeasuredHeight() - getPaddingBottom()) - bottom.getBottom();
                            int bottomStart = bottom.getBottom() + stepY3;
                            for (int i2 = getChildCount() - 1; i2 >= 0; i2--) {
                                View tmp2 = getChildAt(i2);
                                tmp2.layout(tmp2.getLeft(), bottomStart - tmp2.getMeasuredHeight(), tmp2.getRight(), bottomStart);
                                bottomStart -= tmp2.getMeasuredHeight();
                                if (i2 != 0) {
                                    bottomStart -= this.itemMargin;
                                }
                            }
                            ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: align to bottom " + stepY3);
                        }
                    } else if (top.getTop() < getPaddingTop()) {
                        int stepY4 = getPaddingTop() - top.getTop();
                        int topStart2 = top.getTop() + stepY4;
                        for (int i3 = 0; i3 < getChildCount(); i3++) {
                            View tmp3 = getChildAt(i3);
                            tmp3.layout(tmp3.getLeft(), topStart2, tmp3.getRight(), tmp3.getMeasuredHeight() + topStart2);
                            topStart2 += tmp3.getMeasuredHeight();
                            if (i3 != getChildCount() - 1) {
                                topStart2 += this.itemMargin;
                            }
                        }
                        ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: align to top " + stepY4);
                    }
                }
            }
        } else if (this.gravity == 80 && getChildCount() > 0) {
            View top2 = getChildAt(0);
            View bottom2 = getChildAt(getChildCount() - 1);
            ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: gravity=" + this.gravity + " top [" + top2.getLeft() + "," + top2.getTop() + "," + top2.getRight() + "," + top2.getBottom() + "]" + " bottom [" + bottom2.getLeft() + "," + bottom2.getTop() + "," + bottom2.getRight() + "," + bottom2.getBottom() + "]");
            if (bottom2.getBottom() <= getMeasuredHeight() - getPaddingBottom()) {
                int stepY5 = (getMeasuredHeight() - getPaddingBottom()) - bottom2.getBottom();
                int bottomStart2 = bottom2.getBottom() + stepY5;
                for (int i4 = getChildCount() - 1; i4 >= 0; i4--) {
                    View tmp4 = getChildAt(i4);
                    tmp4.layout(tmp4.getLeft(), bottomStart2 - tmp4.getMeasuredHeight(), tmp4.getRight(), bottomStart2);
                    bottomStart2 -= tmp4.getMeasuredHeight();
                    if (i4 != 0) {
                        bottomStart2 -= this.itemMargin;
                    }
                }
                ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: align to bottom " + stepY5);
            } else if (this.style == Style.edgeLimit) {
                if (((float) (bottom2.getBottom() - top2.getTop())) >= ((float) getMeasuredHeight())) {
                    if (top2.getTop() > getPaddingTop()) {
                        int stepY6 = getPaddingTop() - top2.getTop();
                        int topStart3 = top2.getTop() + stepY6;
                        for (int i5 = 0; i5 < getChildCount(); i5++) {
                            View tmp5 = getChildAt(i5);
                            tmp5.layout(tmp5.getLeft(), topStart3, tmp5.getRight(), tmp5.getMeasuredHeight() + topStart3);
                            topStart3 += tmp5.getMeasuredHeight();
                            if (i5 != getChildCount() - 1) {
                                topStart3 += this.itemMargin;
                            }
                        }
                        ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: align to top " + stepY6);
                    }
                } else if (bottom2.getBottom() > getMeasuredHeight() - getPaddingBottom()) {
                    int stepY7 = (getMeasuredHeight() - getPaddingBottom()) - bottom2.getBottom();
                    int bottomStart3 = bottom2.getBottom() + stepY7;
                    for (int i6 = getChildCount() - 1; i6 >= 0; i6--) {
                        View tmp6 = getChildAt(i6);
                        tmp6.layout(tmp6.getLeft(), bottomStart3 - tmp6.getMeasuredHeight(), tmp6.getRight(), bottomStart3);
                        bottomStart3 -= tmp6.getMeasuredHeight();
                        if (i6 != 0) {
                            bottomStart3 -= this.itemMargin;
                        }
                    }
                    ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: align to bottom " + stepY7);
                }
            }
        }
        List<ViewHolder> pre = new ArrayList<>();
        List<ViewHolder> pst = new ArrayList<>();
        for (int i7 = 0; i7 < this.usingViewHolders.size(); i7++) {
            ViewHolder vh = this.usingViewHolders.get(i7);
            if (vh.itemView.getBottom() < getPaddingTop()) {
                pre.add(vh);
            }
            if (vh.itemView.getTop() > getMeasuredHeight()) {
                pst.add(vh);
            }
        }
        while (pre.size() > 1) {
            ViewHolder vh2 = pre.remove(0);
            removeViewInLayout(vh2.itemView);
            this.usingViewHolders.remove(vh2);
            this.cachedViewHolders.add(vh2);
            ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: recycle pre " + vh2.posNow);
        }
        while (pst.size() > 1) {
            ViewHolder vh3 = pst.remove(pst.size() - 1);
            removeViewInLayout(vh3.itemView);
            this.usingViewHolders.remove(vh3);
            this.cachedViewHolders.add(vh3);
            ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: recycle pst " + vh3.posNow);
        }
        while (this.cachedViewHolders.size() > 3) {
            this.cachedViewHolders.remove(0);
            ZpLogger.d(TAG, tagObjHashCode() + " layoutStep3: recycle cache");
        }
    }

    public Pair<Integer, Integer> getLayoutRange() {
        int bgn = -1;
        int end = -1;
        for (int i = 0; i < this.usingViewHolders.size(); i++) {
            if (i == 0) {
                bgn = this.usingViewHolders.get(i).posNow;
            }
            if (i == this.usingViewHolders.size() - 1) {
                end = this.usingViewHolders.get(i).posNow;
            }
        }
        return new Pair<>(Integer.valueOf(bgn), Integer.valueOf(end));
    }

    public ViewHolder getViewHolder(int pos) {
        for (int i = 0; i < this.usingViewHolders.size(); i++) {
            ViewHolder tmp = this.usingViewHolders.get(i);
            if (tmp.posNow == pos) {
                return tmp;
            }
        }
        return null;
    }

    public ViewHolder getViewHolder(View view) {
        View itemView = null;
        ViewParent vp = view.getParent();
        if (vp == this) {
            itemView = view;
        } else {
            int loop = 20;
            while (true) {
                if (vp == null) {
                    break;
                }
                loop--;
                if (loop < 0) {
                    break;
                } else if (vp.getParent() != this) {
                    vp = vp.getParent();
                } else if (vp instanceof View) {
                    itemView = (View) vp;
                }
            }
        }
        if (itemView != null) {
            for (int i = 0; i < this.usingViewHolders.size(); i++) {
                if (this.usingViewHolders.get(i).itemView == itemView) {
                    return this.usingViewHolders.get(i);
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void doLayoutMove() {
        ZpLogger.d(TAG, tagObjHashCode() + " doLayoutMove");
        synchronized (this) {
            doLayout(this.onLayoutParams.changed, this.onLayoutParams.l, this.onLayoutParams.t, this.onLayoutParams.r, this.onLayoutParams.b, "doLayoutMove");
        }
        invalidate();
    }

    /* access modifiers changed from: private */
    public void clearAnimatorsRecord() {
        synchronized (this.animatorsRecord) {
            while (!this.animatorsRecord.isEmpty()) {
                try {
                    ZpLogger.d(TAG, tagObjHashCode() + " cancel (clearAnimatorsRecord) ");
                    this.animatorsRecord.remove(0).cancel();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void addToAnimatorsRecord(Animator animator) {
        synchronized (this.animatorsRecord) {
            while (!this.animatorsRecord.isEmpty()) {
                try {
                    ZpLogger.d(TAG, tagObjHashCode() + " cancel (addToAnimatorsRecord) ");
                    this.animatorsRecord.remove(0).cancel();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            this.animatorsRecord.add(animator);
        }
    }

    private void postOSDTask(Runnable task) {
        synchronized (this.taskCache4ScrollDone) {
            this.taskCache4ScrollDone.add(task);
            while (this.taskCache4ScrollDone.size() > 1) {
                this.taskCache4ScrollDone.remove(0);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean handleFirstTaskInScrollTaskCache() {
        boolean z = false;
        synchronized (this.taskCache4ScrollDone) {
            if (!this.taskCache4ScrollDone.isEmpty()) {
                try {
                    this.taskCache4ScrollDone.remove(0).run();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                z = true;
            }
        }
        return z;
    }

    public void postOANALTask(Runnable task) {
        synchronized (this.taskCache4AfterNotifyAndLayout) {
            this.taskCache4AfterNotifyAndLayout.add(task);
            while (this.taskCache4AfterNotifyAndLayout.size() > 3) {
                this.taskCache4AfterNotifyAndLayout.remove(0);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleTaskCache4PstNotifyAndLayout() {
        synchronized (this.taskCache4AfterNotifyAndLayout) {
            while (!this.taskCache4AfterNotifyAndLayout.isEmpty()) {
                try {
                    this.taskCache4AfterNotifyAndLayout.remove(0).run();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void postOBNALTask(Runnable task) {
        synchronized (this.taskCache4BeforeNotifyAndLayout) {
            this.taskCache4BeforeNotifyAndLayout.add(task);
            while (this.taskCache4BeforeNotifyAndLayout.size() > 3) {
                this.taskCache4BeforeNotifyAndLayout.remove(0);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleTaskCache4BeforeNotifyAndLayout() {
        synchronized (this.taskCache4BeforeNotifyAndLayout) {
            while (!this.taskCache4BeforeNotifyAndLayout.isEmpty()) {
                try {
                    this.taskCache4BeforeNotifyAndLayout.remove(0).run();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void layoutMoveBy(final int pos, final boolean immediately, final Runnable r) {
        Runnable scrollTask = new Runnable() {
            /* access modifiers changed from: private */
            public void doImmediatelyMove() {
                int unused = FakeListView.this.immediatelyMovePos = pos;
                FakeListView.this.doLayoutMove();
            }

            public void run() {
                ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " layoutMoveBy : " + pos);
                if (pos < 0 || pos > FakeListView.this.getAdapter().getItemCount() - 1) {
                    ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " scroll task not run cause pos not fit :" + pos + "," + (FakeListView.this.getAdapter().getItemCount() - 1));
                    FakeListView.this.handler.sendEmptyMessage(2);
                    return;
                }
                int unused = FakeListView.this.flag = FakeListView.this.flag | 2;
                final AnimParams animParams = new AnimParams(pos, immediately, r);
                ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration(10000);
                animator.setTarget(this);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    boolean bottomArrived = false;
                    boolean topArrived = false;

                    private void doAnimScroll(ValueAnimator animation) {
                        Pair<Integer, Integer> range = FakeListView.this.getLayoutRange();
                        int unused = FakeListView.this.stepY = 0;
                        if (pos < ((Integer) range.first).intValue()) {
                            ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " doAnimScroll : a");
                            int unused2 = FakeListView.this.stepY = Math.min(FakeListView.this.getMeasuredHeight() / 2, animParams.getStep()) * 1;
                        } else if (pos > ((Integer) range.second).intValue()) {
                            ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " doAnimScroll : b");
                            int unused3 = FakeListView.this.stepY = Math.min(FakeListView.this.getMeasuredHeight() / 2, animParams.getStep()) * -1;
                        } else {
                            ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " doAnimScroll : c");
                            ViewHolder vh = FakeListView.this.getViewHolder(pos);
                            if (vh == null || vh.itemView == null) {
                                ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " doAnimScroll : usingViewHolders broken !");
                                int unused4 = FakeListView.this.stepY = 0;
                            } else {
                                Rect itemArea = new Rect();
                                Rect contentArea = new Rect();
                                vh.itemView.getDrawingRect(itemArea);
                                FakeListView.this.offsetDescendantRectToMyCoords(vh.itemView, itemArea);
                                FakeListView.this.getDrawingRect(contentArea);
                                int distance = itemArea.centerY() - contentArea.centerY();
                                ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " doAnimScroll : " + " pos=" + pos + " distance=" + distance + " itemArea=" + itemArea.toShortString() + " contentArea=" + contentArea.toShortString());
                                if (distance != 0) {
                                    int unused5 = FakeListView.this.stepY = (distance > 0 ? -1 : 1) * Math.min(Math.abs(distance), Math.abs(animParams.getStep()));
                                } else {
                                    int unused6 = FakeListView.this.stepY = 0;
                                }
                            }
                        }
                        if (FakeListView.this.style == Style.edgeLimit) {
                            ViewHolder first = FakeListView.this.getViewHolder(((Integer) range.first).intValue());
                            ViewHolder last = FakeListView.this.getViewHolder(((Integer) range.second).intValue());
                            if (((Integer) range.first).intValue() == 0 && first != null && first.itemView != null && first.itemView.getTop() + FakeListView.this.stepY >= FakeListView.this.getPaddingTop() && FakeListView.this.stepY > 0) {
                                int unused7 = FakeListView.this.stepY = FakeListView.this.getPaddingTop() - first.itemView.getTop();
                                ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " topArrived : " + FakeListView.this.stepY);
                                this.topArrived = true;
                            }
                            if (((Integer) range.second).intValue() == FakeListView.this.getAdapter().getItemCount() - 1 && !this.topArrived && last != null && last.itemView != null && last.itemView.getBottom() + FakeListView.this.stepY <= FakeListView.this.getMeasuredHeight() - FakeListView.this.getPaddingBottom() && FakeListView.this.stepY < 0) {
                                int unused8 = FakeListView.this.stepY = (FakeListView.this.getMeasuredHeight() - FakeListView.this.getPaddingBottom()) - last.itemView.getBottom();
                                ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " bottomArrived : " + FakeListView.this.stepY);
                                this.bottomArrived = true;
                            }
                        }
                        ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " doAnimScroll : " + animParams.frameCount + "," + FakeListView.this.usingViewHolders.size() + "," + FakeListView.this.stepY);
                        if (FakeListView.this.stepY == 0) {
                            ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " cancel (stepY == 0) ");
                            animParams.endByUs = true;
                            animation.cancel();
                            return;
                        }
                        FakeListView.this.doLayoutMove();
                    }

                    public void onAnimationUpdate(ValueAnimator animation) {
                        ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " onAnimationUpdate " + animation.isStarted() + "&&" + animation.isRunning() + " ," + animParams.toString());
                        if (animation.isStarted() && animation.isRunning()) {
                            animParams.count();
                            if (FakeListView.this.isDetachedFromWindow() || FakeListView.this.isNotifyDoing()) {
                                ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " cancel (isDetachedFromWindow() || isNotifyDoing()) " + FakeListView.this.isDetachedFromWindow() + Constants.SEPARATOR + FakeListView.this.isNotifyDoing());
                                animParams.endByUs = true;
                                animation.cancel();
                            } else if (this.topArrived || this.bottomArrived) {
                                ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " cancel (topArrived || bottomArrived) " + this.topArrived + Constants.SEPARATOR + this.bottomArrived);
                                animParams.endByUs = true;
                                animation.cancel();
                            } else if (FakeListView.this.usingViewHolders.size() <= 0) {
                                ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " cancel (usingViewHolders.size() <=0) ");
                                animParams.endByUs = true;
                                animation.cancel();
                            } else if (!animParams.immediately) {
                                doAnimScroll(animation);
                            } else {
                                if (animation != null) {
                                    ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " cancel (doImmediatelyMove) ");
                                    animParams.endByUs = true;
                                    animation.cancel();
                                }
                                AnonymousClass4.this.doImmediatelyMove();
                            }
                        }
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " scroll start ! ");
                    }

                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " scroll end ! ");
                        int unused = FakeListView.this.flag = FakeListView.this.flag & -3;
                        int unused2 = FakeListView.this.stepY = 0;
                        FakeListView.this.clearAnimatorsRecord();
                        if (!animParams.endByUs) {
                            AnonymousClass4.this.doImmediatelyMove();
                        }
                        if (animParams != null) {
                            try {
                                animParams.runTask();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                        FakeListView.this.handler.sendEmptyMessage(1);
                    }

                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animation.hashCode() + " scroll cancel ! ");
                    }
                });
                FakeListView.this.addToAnimatorsRecord(animator);
                animator.start();
                ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + animator.hashCode() + " call start ! ");
            }
        };
        if (isScrollDoing() || isNotifyDoing()) {
            ZpLogger.d(TAG, tagObjHashCode() + " layoutMoveBy run delay cause : " + "isScrollDoing()=" + isScrollDoing() + ",isNotifyDoing()=" + isNotifyDoing());
            postOSDTask(scrollTask);
            return;
        }
        ZpLogger.d(TAG, tagObjHashCode() + " layoutMoveBy run immediately ");
        scrollTask.run();
    }

    public void scrollToPos(final int pos, final boolean immediately, final Runnable runnable) {
        if (this.adapter != null && !this.usingViewHolders.isEmpty() && pos >= 0 && pos < this.adapter.getItemCount()) {
            Runnable scrollTask = new Runnable() {
                public void run() {
                    FakeListView.this.layoutMoveBy(pos, immediately, runnable);
                }
            };
            if (Looper.myLooper() == Looper.getMainLooper()) {
                scrollTask.run();
            } else {
                post(scrollTask);
            }
        }
    }

    private class AnimParams {
        boolean endByUs = false;
        long frameCount = 0;
        boolean immediately;
        long lastStepTime = 0;
        Runnable runOnceWhenEnd = null;
        float speed = 5.0f;
        long startTime = 0;

        public AnimParams(int pos, boolean immediately2, Runnable task) {
            this.runOnceWhenEnd = task;
            this.immediately = immediately2;
            this.startTime = System.currentTimeMillis();
            Pair<Integer, Integer> range = FakeListView.this.getLayoutRange();
            if (((Integer) range.first).intValue() != -1 && ((Integer) range.second).intValue() != -1) {
                if (pos < ((Integer) range.first).intValue() - 10 || pos > ((Integer) range.second).intValue() + 10) {
                    this.speed = 5.0f;
                } else if (pos < ((Integer) range.first).intValue() - 5 || pos > ((Integer) range.second).intValue() + 5) {
                    this.speed = 3.0f;
                } else if (pos < ((Integer) range.first).intValue() || pos > ((Integer) range.second).intValue()) {
                    this.speed = 2.0f;
                } else {
                    this.speed = 1.0f;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public int getStep() {
            long now = System.currentTimeMillis();
            if (this.lastStepTime == 0) {
                this.lastStepTime = this.startTime;
            }
            int time = (int) (now - this.lastStepTime);
            if (time == 0) {
                time = 1;
            }
            this.lastStepTime = now;
            int step = (int) (((float) time) * this.speed);
            if (step == 0) {
                step = 1;
            }
            ZpLogger.d(FakeListView.TAG, FakeListView.this.tagObjHashCode() + " " + hashCode() + " step:" + step);
            return step;
        }

        /* access modifiers changed from: package-private */
        public void runTask() {
            if (this.runOnceWhenEnd != null) {
                this.runOnceWhenEnd.run();
                this.runOnceWhenEnd = null;
            }
        }

        public void count() {
            this.frameCount++;
        }

        public String toString() {
            return "AnimParams{startTime=" + this.startTime + ", lastStepTime=" + this.lastStepTime + ", frameCount=" + this.frameCount + ", speed=" + this.speed + ", runOnceWhenEnd=" + this.runOnceWhenEnd + ", immediately=" + this.immediately + ", endByUs=" + this.endByUs + '}';
        }
    }

    private class OnLayoutParams {
        int b;
        boolean changed;
        int l;
        int r;
        int t;

        public OnLayoutParams(boolean changed2, int l2, int t2, int r2, int b2) {
            this.changed = changed2;
            this.l = l2;
            this.t = t2;
            this.r = r2;
            this.b = b2;
        }
    }

    public static abstract class Adapter<VH extends ViewHolder> {
        private static final int MSG_VIEW_READY = 1;
        /* access modifiers changed from: private */
        public Adapter<VH>.DataSet dataSet = new DataSet();
        /* access modifiers changed from: private */
        public WeakReference<FakeListView> fakeListViewWeakReference = null;
        /* access modifiers changed from: private */
        public Handler handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Adapter.this.clearNotifyTaskCache();
                        return;
                    default:
                        return;
                }
            }
        };
        private ArrayList<Runnable> notifyTasksCache = new ArrayList<>();

        enum NotifyType {
            all,
            range,
            single
        }

        public abstract int getItemCount();

        public abstract void onBindViewHolder(VH vh, int i);

        public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

        private class DataSet extends Observable {
            private DataSet() {
            }

            /* access modifiers changed from: protected */
            public synchronized void setChanged() {
                super.setChanged();
            }

            /* access modifiers changed from: protected */
            public synchronized void clearChanged() {
                super.clearChanged();
            }
        }

        public int getItemViewType(int position) {
            return 0;
        }

        private void addIntoNotifyTaskCache(Runnable task) {
            synchronized (this.notifyTasksCache) {
                this.notifyTasksCache.add(task);
                while (this.notifyTasksCache.size() > 1) {
                    this.notifyTasksCache.remove(0);
                }
            }
        }

        /* access modifiers changed from: private */
        public void clearNotifyTaskCache() {
            synchronized (this.notifyTasksCache) {
                for (int i = 0; i < this.notifyTasksCache.size(); i++) {
                    try {
                        this.notifyTasksCache.get(i).run();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                this.notifyTasksCache.clear();
            }
        }

        public boolean notifyDataSetChanged() {
            if (this.fakeListViewWeakReference.get() == null) {
                return false;
            }
            Runnable safeNotifyTask = new Runnable() {
                public void run() {
                    Runnable notifyTask = new Runnable() {
                        public void run() {
                            Adapter.this.dataSet.setChanged();
                            Adapter.this.dataSet.notifyObservers(new NotifyParam(NotifyType.all));
                        }
                    };
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        notifyTask.run();
                    } else {
                        Adapter.this.handler.post(notifyTask);
                    }
                }
            };
            if (!((FakeListView) this.fakeListViewWeakReference.get()).isViewReady()) {
                addIntoNotifyTaskCache(safeNotifyTask);
            } else {
                safeNotifyTask.run();
            }
            return true;
        }

        public boolean notifyItemChanged(final int pos) {
            if (this.fakeListViewWeakReference.get() == null) {
                return false;
            }
            Runnable safeNotifyTask = new Runnable() {
                public void run() {
                    Runnable notifyTask = new Runnable() {
                        public void run() {
                            Adapter.this.dataSet.setChanged();
                            Adapter.this.dataSet.notifyObservers(new NotifyParam(NotifyType.single, pos));
                        }
                    };
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        notifyTask.run();
                    } else {
                        Adapter.this.handler.post(notifyTask);
                    }
                }
            };
            if (!((FakeListView) this.fakeListViewWeakReference.get()).isViewReady()) {
                addIntoNotifyTaskCache(safeNotifyTask);
            } else {
                safeNotifyTask.run();
            }
            return true;
        }

        class NotifyParam {
            NotifyType notifyType;
            Pair<Integer, Integer> range;
            int singlePos;

            public NotifyParam(NotifyType notifyType2, int singlePos2) {
                this.notifyType = notifyType2;
                this.singlePos = singlePos2;
            }

            public NotifyParam(NotifyType notifyType2, int bgn, int end) {
                this.notifyType = notifyType2;
                this.range = new Pair<>(Integer.valueOf(bgn), Integer.valueOf(end));
            }

            public NotifyParam(NotifyType notifyType2) {
                this.notifyType = notifyType2;
            }

            public String toString() {
                return "NotifyParam{notifyType=" + this.notifyType + ", range=" + this.range + ", singlePos=" + this.singlePos + '}';
            }
        }
    }

    public static abstract class ViewHolder {
        protected View itemView;
        /* access modifiers changed from: private */
        public int posNow = -1;
        private int posOld = -1;
        /* access modifiers changed from: private */
        public int viewType = 0;

        public int getPosNow() {
            return this.posNow;
        }

        private void setPosNow(int posNow2) {
            this.posOld = this.posNow;
            this.posNow = posNow2;
        }

        public int getViewType() {
            return this.viewType;
        }

        private void setViewType(int viewType2) {
            this.viewType = viewType2;
        }

        public ViewHolder(View itemView2) {
            if (itemView2 == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView2;
        }
    }
}
