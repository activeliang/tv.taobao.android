package com.powyin.slide.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import com.powyin.slide.R;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.uc.webview.export.extension.UCCore;
import java.util.ArrayList;
import java.util.List;

public class BannerSwitch extends ViewGroup {
    private AnimationRun autoProgress;
    private int mActivePointerId;
    private boolean mCanClick;
    private final DataSetObserver mDataSetObserver;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private float mLastMotionX;
    private float mLastMotionY;
    private ListAdapter mListAdapter;
    private OnItemClickListener mOnItemClickListener;
    private OnScrollListener mOnScrollListener;
    /* access modifiers changed from: private */
    public float mSelectIndex;
    /* access modifiers changed from: private */
    public float mSelectIndexMax;
    /* access modifiers changed from: private */
    public float mSelectIndexMin;
    private int mSwitchAnimationPeriod;
    /* access modifiers changed from: private */
    public ValueAnimator mSwitchAnimator;
    private boolean mSwitchEdge;
    private int mSwitchFixedItem;
    private int mSwitchOrientation;
    /* access modifiers changed from: private */
    public int mSwitchPagePeriod;
    private boolean mTouchScrollEnable;
    private int mTouchSlop;
    SparseArray<TypeViewInfo> mTypeToView;
    private float scrollDirection;

    public interface OnItemClickListener {
        void onItemClicked(int i, View view);
    }

    private class TypeViewInfo {
        int currentUsedPosition;
        List<View> holdViews = new ArrayList();
        Integer mType;

        TypeViewInfo(Integer type) {
            this.mType = type;
        }
    }

    private class AnimationRun implements Runnable {
        boolean delay;
        boolean isCancel;

        private AnimationRun() {
            this.delay = false;
        }

        public void run() {
            if (!this.isCancel && BannerSwitch.this.mSelectIndexMin == Float.MIN_VALUE && BannerSwitch.this.mSelectIndexMax == Float.MAX_VALUE) {
                if (BannerSwitch.this.getVisibility() == 0 && !this.delay) {
                    BannerSwitch.this.startInternalPageFly(-1.0f, true);
                }
                if (this.delay) {
                    BannerSwitch.this.postDelayed(this, (long) (BannerSwitch.this.mSwitchPagePeriod / 2));
                } else {
                    BannerSwitch.this.postDelayed(this, (long) BannerSwitch.this.mSwitchPagePeriod);
                }
                this.delay = false;
            }
        }
    }

    public BannerSwitch(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public BannerSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mActivePointerId = -1;
        this.mSelectIndexMin = Float.MIN_VALUE;
        this.mSelectIndexMax = Float.MAX_VALUE;
        this.mTypeToView = new SparseArray<>();
        this.mDataSetObserver = new DataSetObserver() {
            public void onChanged() {
                BannerSwitch.this.refreshAdapter();
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerSwitch);
        this.mSwitchFixedItem = a.getInt(R.styleable.BannerSwitch_pow_switch_fixed_item, 1);
        this.mTouchScrollEnable = a.getBoolean(R.styleable.BannerSwitch_pow_switch_touch_scroll, true);
        this.mSwitchEdge = a.getBoolean(R.styleable.BannerSwitch_pow_switch_fixed_edge, false);
        this.mSwitchPagePeriod = a.getInt(R.styleable.BannerSwitch_pow_switch_period, 2550);
        this.mSwitchPagePeriod = Math.max(1500, this.mSwitchPagePeriod);
        this.mSwitchPagePeriod = Math.min(10000, this.mSwitchPagePeriod);
        this.mSwitchAnimationPeriod = a.getInt(R.styleable.BannerSwitch_pow_switch_animation_period, 550);
        this.mSwitchAnimationPeriod = Math.max(100, this.mSwitchAnimationPeriod);
        this.mSwitchAnimationPeriod = Math.min(1500, this.mSwitchAnimationPeriod);
        this.mSwitchOrientation = a.getInt(R.styleable.BannerSwitch_pow_switch_switch_orientation, 0);
        a.recycle();
        setScrollingCacheEnabled(true);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (this.mSwitchFixedItem + 1 > getChildCount() || this.mSwitchEdge) {
            this.mSelectIndexMax = (float) (this.mSwitchFixedItem / 2);
            this.mSelectIndexMin = (this.mSelectIndexMax - ((float) getChildCount())) + 1.0f;
            return;
        }
        this.mSelectIndexMin = Float.MIN_VALUE;
        this.mSelectIndexMax = Float.MAX_VALUE;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int speWidthMeasure;
        int count = getChildCount();
        int maxHeight = 0;
        float pace = (float) (Math.max((View.MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()) - getPaddingRight(), 0) / this.mSwitchFixedItem);
        if (count > 0) {
            speWidthMeasure = View.MeasureSpec.makeMeasureSpec((int) pace, UCCore.VERIFY_POLICY_QUICK);
        } else {
            speWidthMeasure = 0;
        }
        int usedHei = getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.measure(speWidthMeasure, getChildMeasureSpec(heightMeasureSpec, usedHei, -2));
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
        }
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), resolveSizeAndState(Math.max(maxHeight + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), heightMeasureSpec, 0));
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            getChildAt(i2).measure(speWidthMeasure, View.MeasureSpec.makeMeasureSpec(Math.max(0, (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()), UCCore.VERIFY_POLICY_QUICK));
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();
        int count = getChildCount();
        int pace = Math.max(((r - l) - getPaddingLeft()) - getPaddingRight(), 0) / this.mSwitchFixedItem;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.layout(childLeft, childTop, childLeft + pace, child.getMeasuredHeight() + childTop);
        }
        ensureTranslationOrder();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & 255;
        if (this.autoProgress != null) {
            this.autoProgress.delay = true;
        }
        switch (action) {
            case 0:
                this.mInitialMotionX = ev.getX();
                this.mInitialMotionY = ev.getY();
                this.mIsUnableToDrag = false;
                this.mIsBeingDragged = false;
                this.mCanClick = true;
                this.mActivePointerId = -1;
                break;
            case 1:
            case 3:
                setScrollingCacheEnabled(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!this.mTouchScrollEnable || this.mIsUnableToDrag) {
            return false;
        }
        switch (ev.getAction() & 255) {
            case 0:
                this.mActivePointerId = ev.getPointerId(0);
                this.mIsUnableToDrag = false;
                this.mLastMotionX = ev.getX();
                this.mLastMotionY = ev.getY();
                if (this.mSwitchAnimator != null && this.mSwitchAnimator.isStarted() && this.mSwitchAnimator.isRunning()) {
                    this.mSwitchAnimator.cancel();
                    this.mSwitchAnimator = null;
                    this.mIsBeingDragged = true;
                    this.mCanClick = false;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    setScrollingCacheEnabled(false);
                    break;
                }
            case 2:
                int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                float x = ev.getX(pointerIndex);
                float dx = x - this.mLastMotionX;
                float xDiff = Math.abs(dx);
                float y = ev.getY(pointerIndex);
                float yDiff = Math.abs(y - this.mLastMotionY);
                if (dx != 0.0f) {
                    if (canScroll(this, false, (int) dx, (int) x, (int) y)) {
                        this.mIsUnableToDrag = true;
                        return false;
                    }
                }
                if (xDiff > ((float) this.mTouchSlop) && 0.5f * xDiff > yDiff && !this.mIsUnableToDrag) {
                    this.mIsBeingDragged = true;
                    this.mCanClick = false;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    setScrollingCacheEnabled(false);
                } else if (yDiff > ((float) this.mTouchSlop)) {
                    this.mIsUnableToDrag = true;
                    this.mCanClick = false;
                }
                if (this.mIsBeingDragged) {
                    offsetScrollX(x);
                    break;
                }
                break;
            case 5:
                int index = MotionEventCompat.getActionIndex(ev);
                this.mLastMotionX = ev.getX(index);
                this.mLastMotionY = ev.getY(index);
                this.mActivePointerId = ev.getPointerId(index);
                break;
            case 6:
                int pointerIndex2 = MotionEventCompat.getActionIndex(ev);
                if (ev.getPointerId(pointerIndex2) == this.mActivePointerId) {
                    int newPointerIndex = pointerIndex2 == 0 ? 1 : 0;
                    this.mLastMotionX = ev.getX(newPointerIndex);
                    this.mLastMotionY = ev.getY(newPointerIndex);
                    this.mActivePointerId = ev.getPointerId(newPointerIndex);
                    break;
                }
                break;
        }
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean ret = super.onTouchEvent(ev);
        if (!this.mTouchScrollEnable) {
            return ret;
        }
        switch (ev.getAction() & 255) {
            case 0:
                this.mLastMotionX = ev.getX();
                this.mLastMotionY = ev.getY();
                this.mActivePointerId = ev.getPointerId(0);
                break;
            case 1:
            case 3:
                if (this.mIsBeingDragged) {
                    startInternalTouchFly();
                    ViewCompat.postInvalidateOnAnimation(this);
                    break;
                }
                break;
            case 2:
                if (this.mIsBeingDragged) {
                    offsetScrollX(ev.getX(ev.findPointerIndex(this.mActivePointerId)));
                    break;
                } else {
                    int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                    float x = ev.getX(pointerIndex);
                    float y = ev.getY(pointerIndex);
                    float xDiff = Math.abs(x - this.mLastMotionX);
                    float yDiff = Math.abs(y - this.mLastMotionY);
                    if (xDiff != 0.0f) {
                        if (canScroll(this, false, (int) xDiff, (int) x, (int) y)) {
                            this.mIsUnableToDrag = true;
                            return false;
                        }
                    }
                    if (xDiff <= ((float) this.mTouchSlop) || 0.5f * xDiff <= yDiff || this.mIsUnableToDrag) {
                        if (yDiff > ((float) this.mTouchSlop)) {
                            this.mIsUnableToDrag = true;
                            this.mCanClick = false;
                            break;
                        }
                    } else {
                        this.mIsBeingDragged = true;
                        this.mCanClick = false;
                        getParent().requestDisallowInterceptTouchEvent(true);
                        setScrollingCacheEnabled(false);
                        break;
                    }
                }
                break;
            case 5:
                int index = MotionEventCompat.getActionIndex(ev);
                this.mLastMotionX = ev.getX(index);
                this.mLastMotionY = ev.getY(index);
                this.mActivePointerId = ev.getPointerId(index);
                break;
            case 6:
                int pointerIndex2 = MotionEventCompat.getActionIndex(ev);
                if (ev.getPointerId(pointerIndex2) == this.mActivePointerId) {
                    int newPointerIndex = pointerIndex2 == 0 ? 1 : 0;
                    this.mLastMotionX = ev.getX(newPointerIndex);
                    this.mLastMotionY = ev.getY(newPointerIndex);
                    this.mActivePointerId = ev.getPointerId(newPointerIndex);
                    break;
                }
                break;
        }
        return true;
    }

    private void tryAddAutoBanner() {
        if (this.autoProgress != null) {
            this.autoProgress.isCancel = true;
        }
        if (this.mSelectIndexMin == Float.MIN_VALUE && this.mSelectIndexMax == Float.MAX_VALUE) {
            this.autoProgress = new AnimationRun();
            postDelayed(this.autoProgress, (long) ((int) (((float) this.mSwitchPagePeriod) / 1.5f)));
        }
    }

    private void cancelBanner() {
        if (this.autoProgress != null) {
            this.autoProgress.isCancel = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        tryAddAutoBanner();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelBanner();
    }

    public void setOnClickListener(View.OnClickListener l) {
        throw new RuntimeException("not support onClickListener");
    }

    private boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            for (int i = group.getChildCount() - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()) {
                    if (canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
                        return true;
                    }
                }
            }
        }
        return checkV && ViewCompat.canScrollHorizontally(v, -dx);
    }

    /* access modifiers changed from: protected */
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    public boolean canScrollHorizontally(int direction) {
        if (this.mSwitchOrientation != 0) {
            return super.canScrollHorizontally(direction);
        }
        if (!this.mSwitchEdge) {
            return true;
        }
        if (getChildCount() != 0) {
            if (direction < 0 && this.mSelectIndex <= -0.05f) {
                return true;
            }
            if (direction <= 0 || this.mSelectIndex < ((float) ((-getChildCount()) + 1)) + 0.05f) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean canScrollVertically(int direction) {
        if (this.mSwitchOrientation == 0) {
            return super.canScrollVertically(direction);
        }
        if (!this.mSwitchEdge) {
            return true;
        }
        if (getChildCount() != 0) {
            if (direction < 0 && this.mSelectIndex <= -0.05f) {
                return true;
            }
            if (direction <= 0 || this.mSelectIndex < ((float) ((-getChildCount()) + 1)) + 0.05f) {
                return false;
            }
            return true;
        }
        return false;
    }

    private void offsetScrollX(float x) {
        float deltaX = this.mLastMotionX - x;
        this.scrollDirection = deltaX != 0.0f ? deltaX : this.scrollDirection;
        this.mLastMotionX = x;
        float scrollX = ((float) getScrollX()) + deltaX;
        this.mLastMotionX += scrollX - ((float) ((int) scrollX));
        float pace = (float) (Math.max((getWidth() - getPaddingLeft()) - getPaddingRight(), 0) / this.mSwitchFixedItem);
        if (pace > 0.0f) {
            this.mSelectIndex -= deltaX / pace;
            this.mSelectIndex = this.mSelectIndex > this.mSelectIndexMin ? this.mSelectIndex : this.mSelectIndexMin;
            this.mSelectIndex = this.mSelectIndex < this.mSelectIndexMax ? this.mSelectIndex : this.mSelectIndexMax;
            ensureTranslationOrder();
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            getChildAt(i).setDrawingCacheEnabled(enabled);
        }
    }

    private void startInternalTouchFly() {
        float mTarget;
        double diff = ((double) this.mSelectIndex) - Math.rint((double) this.mSelectIndex);
        if (this.mSwitchAnimator != null) {
            this.mSwitchAnimator.cancel();
            this.mSwitchAnimator = null;
        }
        int left = ((int) this.mSelectIndex) + (this.mSelectIndex % 1.0f > 0.0f ? 1 : 0);
        int righ = ((int) this.mSelectIndex) + (this.mSelectIndex % 1.0f > 0.0f ? 0 : -1);
        if (Math.abs(diff) < 0.08d) {
            mTarget = (float) ((int) Math.rint((double) this.mSelectIndex));
        } else if (this.scrollDirection < 0.0f) {
            mTarget = (float) left;
        } else {
            mTarget = (float) righ;
        }
        if (mTarget <= this.mSelectIndexMin) {
            mTarget = this.mSelectIndexMin;
        }
        if (mTarget >= this.mSelectIndexMax) {
            mTarget = this.mSelectIndexMax;
        }
        if (((double) this.mSelectIndex) - Math.rint((double) mTarget) != ClientTraceData.b.f47a) {
            final ValueAnimator current = ValueAnimator.ofFloat(new float[]{this.mSelectIndex, mTarget});
            this.mSwitchAnimator = current;
            this.mSwitchAnimator.setDuration((long) Math.max(50, ((int) Math.abs((this.mSelectIndex - mTarget) * ((float) this.mSwitchAnimationPeriod))) / this.mSwitchFixedItem));
            this.mSwitchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (BannerSwitch.this.mSwitchAnimator == current) {
                        float unused = BannerSwitch.this.mSelectIndex = ((Float) animation.getAnimatedValue()).floatValue();
                        BannerSwitch.this.ensureTranslationOrder();
                    }
                }
            });
            this.mSwitchAnimator.start();
            if (this.autoProgress != null) {
                this.autoProgress.delay = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public void startInternalPageFly(float step, boolean animation) {
        if (this.mSwitchAnimator != null) {
            this.mSwitchAnimator.cancel();
            this.mSwitchAnimator = null;
        }
        float mTarget = (float) ((int) Math.rint((double) (this.mSelectIndex + step)));
        if (animation) {
            final ValueAnimator current = ValueAnimator.ofFloat(new float[]{this.mSelectIndex, mTarget});
            this.mSwitchAnimator = current;
            this.mSwitchAnimator.setDuration((long) Math.max(150, ((int) Math.abs((this.mSelectIndex - mTarget) * ((float) this.mSwitchAnimationPeriod))) / this.mSwitchFixedItem));
            this.mSwitchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (BannerSwitch.this.mSwitchAnimator == current) {
                        float unused = BannerSwitch.this.mSelectIndex = ((Float) animation.getAnimatedValue()).floatValue();
                        BannerSwitch.this.ensureTranslationOrder();
                    }
                }
            });
            this.mSwitchAnimator.start();
            return;
        }
        this.mSelectIndex = mTarget;
        ensureTranslationOrder();
    }

    /* access modifiers changed from: private */
    public void ensureTranslationOrder() {
        float pace;
        int count = getChildCount();
        if (this.mSwitchOrientation == 0) {
            pace = (float) (Math.max((getWidth() - getPaddingLeft()) - getPaddingRight(), 0) / this.mSwitchFixedItem);
        } else {
            pace = (float) (Math.max((getHeight() - getPaddingTop()) - getPaddingBottom(), 0) / this.mSwitchFixedItem);
        }
        if (count >= 2 && pace > 0.0f) {
            if (this.mOnScrollListener != null) {
                float[] target = getSelectCenter();
                this.mOnScrollListener.onPageScrolled((int) target[0], target[1]);
            }
            int scrollLen = (int) (((float) Math.max(count, this.mSwitchFixedItem)) * pace);
            if (this.mSelectIndexMin == Float.MIN_VALUE && this.mSelectIndexMax == Float.MAX_VALUE) {
                for (int i = 0; i < count; i++) {
                    if (this.mSwitchOrientation == 0) {
                        int transX = ((int) ((((float) i) + this.mSelectIndex) * pace)) % scrollLen;
                        if (transX >= ((int) (((float) this.mSwitchFixedItem) * pace))) {
                            getChildAt(i).setTranslationX((float) (transX - scrollLen));
                        } else if (transX <= ((int) (-pace))) {
                            getChildAt(i).setTranslationX((float) (transX + scrollLen));
                        } else {
                            getChildAt(i).setTranslationX((float) transX);
                        }
                    } else {
                        int transY = ((int) ((((float) i) + this.mSelectIndex) * pace)) % scrollLen;
                        if (transY >= ((int) (((float) this.mSwitchFixedItem) * pace))) {
                            getChildAt(i).setTranslationY((float) (transY - scrollLen));
                        } else if (transY <= ((int) (-pace))) {
                            getChildAt(i).setTranslationY((float) (transY + scrollLen));
                        } else {
                            getChildAt(i).setTranslationY((float) transY);
                        }
                    }
                }
                return;
            }
            for (int i2 = 0; i2 < count; i2++) {
                if (this.mSwitchOrientation == 0) {
                    getChildAt(i2).setTranslationX((float) ((int) ((((float) i2) + this.mSelectIndex) * pace)));
                } else {
                    getChildAt(i2).setTranslationY((float) ((int) ((((float) i2) + this.mSelectIndex) * pace)));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void refreshAdapter() {
        int count = 0;
        removeAllViews();
        for (int i = 0; i < this.mTypeToView.size(); i++) {
            this.mTypeToView.valueAt(i).currentUsedPosition = 0;
        }
        if (this.mListAdapter != null) {
            count = this.mListAdapter.getCount();
        }
        for (int i2 = 0; i2 < count; i2++) {
            Integer type = Integer.valueOf(this.mListAdapter.getItemViewType(i2));
            if (this.mTypeToView.indexOfKey(type.intValue()) < 0) {
                this.mTypeToView.put(type.intValue(), new TypeViewInfo(type));
            }
            TypeViewInfo info = this.mTypeToView.get(type.intValue());
            View canvasView = info.currentUsedPosition < info.holdViews.size() ? info.holdViews.get(info.currentUsedPosition) : null;
            View current = this.mListAdapter.getView(i2, canvasView, this);
            if (current == null) {
                throw new RuntimeException("Adapter.getView(postion , convasView, viewParent) cannot be null ");
            }
            if (canvasView != current) {
                if (canvasView == null) {
                    info.holdViews.add(current);
                } else {
                    info.holdViews.remove(canvasView);
                    info.holdViews.add(info.currentUsedPosition, current);
                }
            }
            info.currentUsedPosition++;
            addView(current);
        }
    }

    private void computeAdapter() {
        this.mTypeToView.clear();
        removeAllViews();
        if (this.mListAdapter != null && this.mListAdapter.getCount() != 0) {
            int count = this.mListAdapter.getCount();
            for (int i = 0; i < count; i++) {
                Integer type = Integer.valueOf(this.mListAdapter.getItemViewType(i));
                if (this.mTypeToView.indexOfKey(type.intValue()) < 0) {
                    this.mTypeToView.put(type.intValue(), new TypeViewInfo(type));
                }
                TypeViewInfo info = this.mTypeToView.get(type.intValue());
                View current = this.mListAdapter.getView(i, (View) null, this);
                if (current == null) {
                    throw new RuntimeException("Adapter.getView(postion , convasView, viewParent) cannot be null ");
                }
                info.holdViews.add(current);
                info.currentUsedPosition++;
                addView(current);
            }
        }
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mListAdapter != adapter) {
            cancelBanner();
            if (this.mListAdapter != null) {
                this.mListAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            }
            this.mListAdapter = adapter;
            if (adapter != null) {
                adapter.registerDataSetObserver(this.mDataSetObserver);
            }
            computeAdapter();
            tryAddAutoBanner();
        }
    }

    private float[] getSelectCenter() {
        float[] ret = new float[3];
        int count = getChildCount();
        float mCurrentIndex = (((float) count) + ((((float) (this.mSwitchFixedItem / 2)) - this.mSelectIndex) % ((float) count))) % ((float) count);
        int mCurrentCenter = (int) Math.rint((double) mCurrentIndex);
        float radio = mCurrentIndex - ((float) mCurrentCenter);
        if (mCurrentCenter == count) {
            mCurrentCenter = 0;
        }
        ret[0] = (float) mCurrentCenter;
        ret[1] = radio;
        ret[2] = mCurrentIndex;
        return ret;
    }

    public int getSelectPage() {
        return (int) getSelectCenter()[0];
    }

    public void setSelectPage(int index, boolean animation) {
        if (this.mSwitchAnimator != null) {
            this.mSwitchAnimator.cancel();
            this.mSwitchAnimator = null;
        }
        int index2 = (getChildCount() + (index % getChildCount())) % getChildCount();
        int center = (int) getSelectCenter()[0];
        if (index2 != center) {
            float step1 = (float) (center - index2);
            float step2 = ((float) Math.max(this.mSwitchFixedItem, getChildCount())) + step1;
            float step3 = step1 - ((float) Math.max(this.mSwitchFixedItem, getChildCount()));
            float step = step1;
            if (Math.abs(step) >= Math.abs(step2)) {
                step = step2;
            }
            if (Math.abs(step) >= Math.abs(step3)) {
                step = step3;
            }
            float target = (float) ((int) Math.rint((double) (this.mSelectIndex + step)));
            if (target <= this.mSelectIndexMin) {
                target = this.mSelectIndexMin;
            }
            if (target >= this.mSelectIndexMax) {
                target = this.mSelectIndexMax;
            }
            startInternalPageFly(target - this.mSelectIndex, animation);
            if (this.autoProgress != null) {
                this.autoProgress.delay = true;
            }
        }
    }

    public boolean performClick() {
        if (this.mOnItemClickListener != null && this.mCanClick) {
            int i = 0;
            while (true) {
                if (i >= getChildCount()) {
                    break;
                }
                Rect globeRect = new Rect();
                View view = getChildAt(i);
                globeRect.top = view.getTop();
                globeRect.left = view.getLeft() + ((int) view.getTranslationX());
                globeRect.right = view.getRight() + ((int) view.getTranslationX());
                globeRect.bottom = view.getBottom();
                if (globeRect.contains((int) this.mInitialMotionX, (int) this.mInitialMotionY)) {
                    this.mOnItemClickListener.onItemClicked(i, view);
                    break;
                }
                i++;
            }
        }
        return true;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
        setClickable(this.mOnItemClickListener != null);
    }

    public void setEnableTouchScroll(boolean isEnable) {
        this.mTouchScrollEnable = isEnable;
        setSelectPage(getSelectPage(), false);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }
}
