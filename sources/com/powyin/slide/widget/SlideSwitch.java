package com.powyin.slide.widget;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListAdapter;
import android.widget.Scroller;
import com.powyin.slide.R;
import com.uc.webview.export.extension.UCCore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlideSwitch extends ViewGroup {
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId;
    private final DataSetObserver mDataSetObserver;
    float mInitialMotionX;
    float mInitialMotionY;
    private boolean mIsBeingDragged;
    private float mLastMotionX;
    private ListAdapter mListAdapter;
    private List<View> mMatchParentChildren;
    private int mMaxWid;
    private int mMaximumVelocity;
    private OnItemClickListener mOnItemClickListener;
    private OnScrollListener mOnScrollListener;
    /* access modifiers changed from: private */
    public Scroller mScroller;
    private Drawable mSelectDrawable;
    private Drawable mSelectDrawableBac;
    private Path mSelectDrawablePath;
    private Rect mSelectDrawableRect;
    private Rect mSelectDrawableRectBac;
    private int mSelectHei;
    /* access modifiers changed from: private */
    public float mSelectIndex;
    private int mSelectMaxItem;
    private boolean mSelectShowOverScroll;
    private int mTouchSlop;
    Map<Integer, TypeViewInfo> mTypeToView;
    private VelocityTracker mVelocityTracker;
    private ViewPager.OnPageChangeListener mViewPageChangeListener;
    int maxScrollX;
    /* access modifiers changed from: private */
    public ValueAnimator valueAnimator;

    private class TypeViewInfo {
        int currentUsedPosition;
        List<View> holdViews = new ArrayList();
        Integer mType;

        TypeViewInfo(Integer type) {
            this.mType = type;
        }
    }

    public SlideSwitch(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public SlideSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mActivePointerId = -1;
        this.mSelectIndex = 0.0f;
        this.mSelectDrawableRect = new Rect();
        this.mSelectDrawableRectBac = new Rect();
        this.mSelectDrawablePath = new Path();
        this.mMatchParentChildren = new ArrayList();
        this.mTypeToView = new HashMap();
        this.mViewPageChangeListener = new ViewPager.OnPageChangeListener() {
            boolean isDragTouch = false;
            int mFixedScrollX = 0;
            float mFixedSelect;
            boolean needGetPosition;

            public void onPageScrolled(int position, float selectIndexOffset, int positionOffsetPixels) {
                View targetView;
                if (this.isDragTouch) {
                    if (this.needGetPosition) {
                        this.mFixedSelect = (float) ((int) Math.rint((double) (((float) position) + selectIndexOffset)));
                        this.needGetPosition = false;
                    }
                    float targetSelectIndex = ((float) position) + selectIndexOffset;
                    if (targetSelectIndex >= ((float) (SlideSwitch.this.getChildCount() - 1))) {
                        targetSelectIndex = (float) (SlideSwitch.this.getChildCount() - 1);
                    }
                    int locLeft = (int) targetSelectIndex;
                    int locRight = locLeft + 1;
                    View originView = SlideSwitch.this.getChildAt(locLeft);
                    if (locRight < SlideSwitch.this.getChildCount()) {
                        targetView = SlideSwitch.this.getChildAt(locRight);
                    } else {
                        targetView = originView;
                    }
                    int scrollLeft = (originView.getLeft() + originView.getRight()) / 2;
                    int targetScrollX = ((int) (((float) scrollLeft) + ((targetSelectIndex - ((float) locLeft)) * ((float) (((targetView.getLeft() + targetView.getRight()) / 2) - scrollLeft))))) - (SlideSwitch.this.getWidth() / 2);
                    if (targetScrollX <= 0) {
                        targetScrollX = 0;
                    }
                    if (targetScrollX >= SlideSwitch.this.maxScrollX) {
                        targetScrollX = SlideSwitch.this.maxScrollX;
                    }
                    float selectIndexOffset2 = targetSelectIndex - this.mFixedSelect;
                    if (selectIndexOffset2 < 0.0f) {
                        selectIndexOffset2 = -selectIndexOffset2;
                    }
                    int targetScrollX2 = (int) (((float) this.mFixedScrollX) + (((float) (targetScrollX - this.mFixedScrollX)) * selectIndexOffset2));
                    if (targetScrollX2 <= 0) {
                        targetScrollX2 = 0;
                    }
                    if (targetScrollX2 >= SlideSwitch.this.maxScrollX) {
                        targetScrollX2 = SlideSwitch.this.maxScrollX;
                    }
                    if (targetScrollX2 != SlideSwitch.this.getScrollX()) {
                        SlideSwitch.this.scrollTo(targetScrollX2, 0);
                    }
                    float unused = SlideSwitch.this.mSelectIndex = this.mFixedSelect + ((targetSelectIndex - this.mFixedSelect) * selectIndexOffset2);
                    SlideSwitch.this.calculationRect(false);
                }
            }

            public void onPageSelected(int position) {
            }

            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 0:
                        this.isDragTouch = false;
                        float unused = SlideSwitch.this.mSelectIndex = (float) ((int) Math.rint((double) SlideSwitch.this.mSelectIndex));
                        SlideSwitch.this.calculationRect(false);
                        return;
                    case 1:
                        if (SlideSwitch.this.mScroller != null) {
                            SlideSwitch.this.mScroller.abortAnimation();
                        }
                        if (SlideSwitch.this.valueAnimator != null) {
                            SlideSwitch.this.valueAnimator.cancel();
                        }
                        this.isDragTouch = true;
                        this.needGetPosition = true;
                        this.mFixedScrollX = SlideSwitch.this.getScrollX();
                        return;
                    default:
                        return;
                }
            }
        };
        this.mDataSetObserver = new DataSetObserver() {
            public void onChanged() {
                SlideSwitch.this.refreshAdapter();
            }
        };
        float density = context.getResources().getDisplayMetrics().density;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideSwitch);
        this.mSelectDrawable = a.getDrawable(R.styleable.SlideSwitch_pow_checked_drawable);
        if (this.mSelectDrawable == null) {
            this.mSelectDrawable = context.getResources().getDrawable(R.drawable.powyin_switch_slide_switch_select);
        }
        this.mSelectDrawableBac = a.getDrawable(R.styleable.SlideSwitch_pow_checked_bac);
        if (this.mSelectDrawableBac == null) {
            this.mSelectDrawableBac = context.getResources().getDrawable(R.drawable.powyin_switch_slide_switch_select_bac);
        }
        this.mSelectHei = (int) a.getDimension(R.styleable.SlideSwitch_pow_checked_hei, (float) ((int) (3.5d * ((double) density))));
        this.mSelectMaxItem = a.getInt(R.styleable.SlideSwitch_pow_fixed_item, -1);
        this.mSelectShowOverScroll = a.getBoolean(R.styleable.SlideSwitch_pow_show_over_scroll, false);
        a.recycle();
        setScrollingCacheEnabled(true);
        this.mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        boolean measureMatchParentChildren = (View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && View.MeasureSpec.getMode(heightMeasureSpec) == 1073741824) ? false : true;
        this.mMatchParentChildren.clear();
        int maxHeight = 0;
        int maxWidth = 0;
        if (this.mSelectMaxItem <= 0) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, this.mSelectHei);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                if (measureMatchParentChildren && (lp.height == -1 || lp.height == -2)) {
                    this.mMatchParentChildren.add(child);
                }
            }
            setMeasuredDimension(resolveSizeAndState(Math.max(maxWidth, getSuggestedMinimumWidth()), widthMeasureSpec, 0), resolveSizeAndState(this.mSelectHei + Math.max(maxHeight, getSuggestedMinimumHeight() - this.mSelectHei), heightMeasureSpec, 0));
            for (int i2 = 0; i2 < this.mMatchParentChildren.size(); i2++) {
                View child2 = this.mMatchParentChildren.get(i2);
                ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) child2.getLayoutParams();
                child2.measure(getChildMeasureSpec(widthMeasureSpec, lp2.leftMargin + lp2.rightMargin, lp2.width), View.MeasureSpec.makeMeasureSpec(Math.max(0, ((getMeasuredHeight() - this.mSelectHei) - lp2.topMargin) - lp2.bottomMargin), UCCore.VERIFY_POLICY_QUICK));
            }
            return;
        }
        int speWidthMeasure = View.MeasureSpec.makeMeasureSpec((int) ((1.0f / ((float) this.mSelectMaxItem)) * ((float) View.MeasureSpec.getSize(widthMeasureSpec))), UCCore.VERIFY_POLICY_QUICK);
        int usedHei = this.mSelectHei;
        for (int i3 = 0; i3 < count; i3++) {
            View child3 = getChildAt(i3);
            LayoutParams lp3 = (LayoutParams) child3.getLayoutParams();
            child3.measure(speWidthMeasure, getChildMeasureSpec(heightMeasureSpec, lp3.topMargin + usedHei + lp3.bottomMargin, lp3.height));
            maxHeight = Math.max(maxHeight, child3.getMeasuredHeight() + lp3.topMargin + lp3.bottomMargin);
            if (lp3.height == -1 || lp3.height == -2) {
                this.mMatchParentChildren.add(child3);
            }
        }
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), resolveSizeAndState(this.mSelectHei + Math.max(maxHeight, getSuggestedMinimumHeight() - this.mSelectHei), heightMeasureSpec, 0));
        for (int i4 = 0; i4 < this.mMatchParentChildren.size(); i4++) {
            View child4 = this.mMatchParentChildren.get(i4);
            ViewGroup.MarginLayoutParams lp4 = (ViewGroup.MarginLayoutParams) child4.getLayoutParams();
            child4.measure(speWidthMeasure, View.MeasureSpec.makeMeasureSpec(Math.max(0, ((getMeasuredHeight() - lp4.topMargin) - lp4.bottomMargin) - this.mSelectHei), UCCore.VERIFY_POLICY_QUICK));
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int count = getChildCount();
        if (this.mSelectMaxItem <= 0) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childLeft2 = childLeft + lp.leftMargin;
                child.layout(childLeft2, 0, childWidth + childLeft2, childHeight + 0);
                childLeft = childLeft2 + lp.rightMargin + childWidth;
            }
            this.mMaxWid = childLeft;
        } else {
            float pace = (1.0f / ((float) this.mSelectMaxItem)) * ((float) (r - l));
            for (int i2 = 0; i2 < count; i2++) {
                View child2 = getChildAt(i2);
                int childHeight2 = child2.getMeasuredHeight();
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                child2.layout(((int) (((float) i2) * pace)) + 0 + lp2.leftMargin, 0, (((int) (((float) (i2 + 1)) * pace)) + 0) - lp2.rightMargin, childHeight2 + 0);
            }
            this.mMaxWid = Math.max(getWidth(), (int) (((float) count) * pace));
        }
        this.maxScrollX = this.mMaxWid - getWidth();
        this.maxScrollX = this.maxScrollX > 0 ? this.maxScrollX : 0;
        calculationRect(true);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.mSelectHei > 0) {
            canvas.save();
            canvas.clipPath(this.mSelectDrawablePath);
            if (this.mSelectDrawableBac != null) {
                this.mSelectDrawableBac.setBounds(this.mSelectDrawableRectBac);
                this.mSelectDrawableBac.draw(canvas);
            }
            if (this.mSelectDrawable != null) {
                this.mSelectDrawable.setBounds(this.mSelectDrawableRect);
                this.mSelectDrawable.draw(canvas);
            }
            canvas.restore();
        }
        super.dispatchDraw(canvas);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            this.mInitialMotionX = ev.getX();
            this.mInitialMotionY = ev.getY();
            this.mIsBeingDragged = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int newPointerIndex = 1;
        switch (ev.getAction() & 255) {
            case 0:
                this.mLastMotionX = ev.getX();
                this.mActivePointerId = ev.getPointerId(0);
                if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
                    this.mScroller.abortAnimation();
                    this.mIsBeingDragged = true;
                    if (this.mVelocityTracker == null) {
                        this.mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        this.mVelocityTracker.clear();
                    }
                    setScrollingCacheEnabled(true);
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                } else {
                    this.mIsBeingDragged = false;
                    break;
                }
                break;
            case 1:
            case 3:
                this.mActivePointerId = -1;
                if (this.mVelocityTracker == null) {
                    return false;
                }
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                return false;
            case 2:
                float x = ev.getX(ev.findPointerIndex(this.mActivePointerId));
                if (Math.abs(x - this.mLastMotionX) > ((float) this.mTouchSlop)) {
                    this.mIsBeingDragged = true;
                    if (this.mVelocityTracker == null) {
                        this.mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        this.mVelocityTracker.clear();
                    }
                    this.mLastMotionX = x;
                    setScrollingCacheEnabled(true);
                    ViewParent parent2 = getParent();
                    if (parent2 != null) {
                        parent2.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                }
                break;
            case 5:
                int index = MotionEventCompat.getActionIndex(ev);
                this.mLastMotionX = ev.getX(index);
                this.mActivePointerId = ev.getPointerId(index);
                break;
            case 6:
                int actionPointerIndex = MotionEventCompat.getActionIndex(ev);
                if (ev.getPointerId(actionPointerIndex) == this.mActivePointerId) {
                    if (actionPointerIndex != 0) {
                        newPointerIndex = 0;
                    }
                    this.mLastMotionX = ev.getX(newPointerIndex);
                    this.mActivePointerId = ev.getPointerId(newPointerIndex);
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.clear();
                        break;
                    }
                }
                break;
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(ev);
        }
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int newPointerIndex = 0;
        super.onTouchEvent(ev);
        switch (ev.getAction() & 255) {
            case 0:
                this.mLastMotionX = ev.getX();
                this.mActivePointerId = ev.getPointerId(0);
                break;
            case 1:
            case 3:
                if (this.mIsBeingDragged) {
                    startInternalFly();
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                this.mActivePointerId = -1;
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    break;
                }
                break;
            case 2:
                if (this.mIsBeingDragged) {
                    int activePointerIndex = ev.findPointerIndex(this.mActivePointerId);
                    offsetScrollX(ev.getX(activePointerIndex));
                    this.mLastMotionX = ev.getX(activePointerIndex);
                    break;
                } else {
                    float x = ev.getX(ev.findPointerIndex(this.mActivePointerId));
                    if (Math.abs(x - this.mLastMotionX) > ((float) this.mTouchSlop)) {
                        this.mIsBeingDragged = true;
                        if (this.mVelocityTracker == null) {
                            this.mVelocityTracker = VelocityTracker.obtain();
                        } else {
                            this.mVelocityTracker.clear();
                        }
                        this.mLastMotionX = x;
                        setScrollingCacheEnabled(true);
                        ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                            break;
                        }
                    }
                }
                break;
            case 5:
                int index = MotionEventCompat.getActionIndex(ev);
                this.mLastMotionX = ev.getX(index);
                this.mActivePointerId = ev.getPointerId(index);
                break;
            case 6:
                int actionPointerIndex = MotionEventCompat.getActionIndex(ev);
                if (ev.getPointerId(actionPointerIndex) == this.mActivePointerId) {
                    if (actionPointerIndex == 0) {
                        newPointerIndex = 1;
                    }
                    this.mLastMotionX = ev.getX(newPointerIndex);
                    this.mActivePointerId = ev.getPointerId(newPointerIndex);
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.clear();
                        break;
                    }
                }
                break;
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(ev);
        }
        return true;
    }

    public boolean performClick() {
        if (!this.mIsBeingDragged) {
            int i = 0;
            while (true) {
                if (i >= getChildCount()) {
                    break;
                }
                Rect globeRect = new Rect();
                View view = getChildAt(i);
                int currentScrollX = getScrollX();
                globeRect.top = view.getTop();
                globeRect.left = view.getLeft() - currentScrollX;
                globeRect.right = view.getRight() - currentScrollX;
                globeRect.bottom = view.getBottom();
                if (!globeRect.contains((int) this.mInitialMotionX, (int) this.mInitialMotionY)) {
                    i++;
                } else if (this.mSelectIndex != ((float) i)) {
                    preformItemSelectAnimationClick(i);
                    if (this.mOnItemClickListener != null) {
                        this.mOnItemClickListener.onItemClicked(i, view);
                    }
                }
            }
        }
        return true;
    }

    public void setOnClickListener(@Nullable View.OnClickListener l) {
        throw new RuntimeException("not support");
    }

    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (!(oldX == x && oldY == y)) {
                scrollTo(x, y);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void offsetScrollX(float x) {
        float deltaX = this.mLastMotionX - x;
        this.mLastMotionX = x;
        float oldScrollX = (float) getScrollX();
        float scrollX = oldScrollX + deltaX;
        int maxScroll = this.mMaxWid - getWidth();
        if (maxScroll <= 0) {
            maxScroll = 0;
        }
        if (!this.mSelectShowOverScroll) {
            if (scrollX <= 0.0f) {
                scrollX = 0.0f;
            }
            if (scrollX >= ((float) maxScroll)) {
                scrollX = 0.0f;
            }
        } else if (scrollX < 0.0f) {
            int maxLen = getWidth() / 3;
            scrollX = oldScrollX + (deltaX * ((float) Math.pow((double) ((Math.max(0.0f, ((float) maxLen) + scrollX) * 1.0f) / ((float) maxLen)), 3.0d)));
        } else if (scrollX > ((float) maxScroll)) {
            int maxLen2 = getWidth() / 3;
            scrollX = oldScrollX + (deltaX * ((float) Math.pow((double) ((Math.max(0.0f, (((float) maxLen2) - scrollX) + ((float) maxScroll)) * 1.0f) / ((float) maxLen2)), 3.0d)));
        }
        scrollTo((int) scrollX, getScrollY());
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                child.setDrawingCacheEnabled(enabled);
            }
        }
    }

    private void startInternalFly() {
        this.mScroller.abortAnimation();
        this.mVelocityTracker.computeCurrentVelocity(500, (float) this.mMaximumVelocity);
        int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId);
        int scrollX = getScrollX();
        int maxScrollX2 = this.mMaxWid - getWidth();
        if (maxScrollX2 <= 0) {
            maxScrollX2 = 0;
        }
        if (scrollX < 0) {
            this.mScroller.startScroll(scrollX, 0, 0 - scrollX, 0, 350);
        } else if (scrollX > maxScrollX2) {
            this.mScroller.startScroll(scrollX, 0, maxScrollX2 - scrollX, 0, 350);
        } else {
            this.mScroller.fling(getScrollX(), 0, -initialVelocity, 0, 0, maxScrollX2, 0, 0);
        }
    }

    private void preformItemSelectAnimationClick(int targetIndex) {
        if (targetIndex >= 0 && targetIndex < getChildCount() && ((float) targetIndex) != this.mSelectIndex) {
            if (this.valueAnimator != null) {
                this.valueAnimator.cancel();
            }
            View targetView = getChildAt(targetIndex);
            int targetScrollX = ((-getWidth()) / 2) + (targetView.getLeft() / 2) + (targetView.getRight() / 2);
            if (targetScrollX <= 0) {
                targetScrollX = 0;
            }
            int maxScroll = this.mMaxWid - getWidth();
            if (maxScroll <= 0) {
                maxScroll = 0;
            }
            if (targetScrollX >= maxScroll) {
                targetScrollX = maxScroll;
            }
            this.valueAnimator = ValueAnimator.ofPropertyValuesHolder(new PropertyValuesHolder[]{PropertyValuesHolder.ofInt("scrollX", new int[]{getScrollX(), targetScrollX}), PropertyValuesHolder.ofFloat("scrollRadio", new float[]{this.mSelectIndex, (float) targetIndex})});
            this.valueAnimator.setDuration((long) (((int) (250.0f * Math.abs(((((float) targetIndex) - this.mSelectIndex) * 1.0f) / ((float) getChildCount())))) + 150));
            this.valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    int mScrollX = ((Integer) animation.getAnimatedValue("scrollX")).intValue();
                    float unused = SlideSwitch.this.mSelectIndex = ((Float) animation.getAnimatedValue("scrollRadio")).floatValue();
                    if (SlideSwitch.this.getScrollX() != mScrollX) {
                        SlideSwitch.this.scrollTo(mScrollX, 0);
                    }
                    SlideSwitch.this.calculationRect(false);
                }
            });
            this.valueAnimator.start();
        }
    }

    /* access modifiers changed from: private */
    public void calculationRect(boolean reSizeBound) {
        if (this.mSelectDrawable != null && this.mSelectIndex >= 0.0f && this.mSelectIndex <= ((float) (getChildCount() - 1))) {
            int locLeft = (int) this.mSelectIndex;
            int locRight = locLeft + 1;
            View originView = getChildAt(locLeft);
            View targetView = locRight < getChildCount() ? getChildAt(locRight) : null;
            int c_left = targetView != null ? targetView.getLeft() - originView.getLeft() : 0;
            int c_right = targetView != null ? targetView.getRight() - originView.getRight() : 0;
            int oldLeft = this.mSelectDrawableRect.left;
            int oldRight = this.mSelectDrawableRect.right;
            this.mSelectDrawableRect.top = getHeight() - this.mSelectHei;
            this.mSelectDrawableRect.bottom = this.mSelectDrawableRect.top + this.mSelectHei;
            this.mSelectDrawableRect.left = (int) (((float) originView.getLeft()) + (((float) c_left) * (this.mSelectIndex - ((float) locLeft))));
            this.mSelectDrawableRect.right = (int) (((float) originView.getRight()) + (((float) c_right) * (this.mSelectIndex - ((float) locLeft))));
            if (!(oldLeft == this.mSelectDrawableRect.left && oldRight == this.mSelectDrawableRect.right)) {
                if (this.mOnScrollListener != null) {
                    int center = (int) Math.rint((double) this.mSelectIndex);
                    this.mOnScrollListener.onPageScrolled(center, this.mSelectIndex - ((float) center));
                }
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
        if (reSizeBound) {
            this.mSelectDrawablePath.reset();
            this.mSelectDrawablePath.addRect(0.0f, 0.0f, (float) this.mMaxWid, (float) getHeight(), Path.Direction.CCW);
            this.mSelectDrawablePath.close();
            this.mSelectDrawableRectBac.top = getHeight() - this.mSelectHei;
            this.mSelectDrawableRectBac.bottom = this.mSelectDrawableRectBac.top + this.mSelectHei;
            this.mSelectDrawableRectBac.left = 0;
            this.mSelectDrawableRectBac.right = this.mMaxWid;
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
        }
    }

    private void computeAdapter() {
        this.mTypeToView.clear();
        removeAllViews();
        if (this.mListAdapter != null && this.mListAdapter.getCount() != 0) {
            int count = this.mListAdapter.getCount();
            for (int i = 0; i < count; i++) {
                Integer type = Integer.valueOf(this.mListAdapter.getItemViewType(i));
                if (!this.mTypeToView.containsKey(type)) {
                    this.mTypeToView.put(type, new TypeViewInfo(type));
                }
                TypeViewInfo info = this.mTypeToView.get(type);
                View current = this.mListAdapter.getView(i, (View) null, this);
                if (current == null) {
                    throw new RuntimeException("Adapter.getView(postion , convasView, viewParent) cannot be null ");
                }
                info.holdViews.add(current);
                info.currentUsedPosition++;
                addView(current);
            }
            this.mSelectIndex = Math.min(this.mSelectIndex, (float) (count - 1));
            this.mSelectIndex = Math.max(0.0f, this.mSelectIndex);
            calculationRect(true);
        }
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mListAdapter != adapter) {
            if (this.mListAdapter != null) {
                this.mListAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            }
            this.mListAdapter = adapter;
            if (adapter != null) {
                adapter.registerDataSetObserver(this.mDataSetObserver);
            }
            computeAdapter();
        }
    }

    /* access modifiers changed from: private */
    public void refreshAdapter() {
        int count = 0;
        removeAllViews();
        for (TypeViewInfo info : this.mTypeToView.values()) {
            info.currentUsedPosition = 0;
        }
        if (this.mListAdapter != null) {
            count = this.mListAdapter.getCount();
        }
        for (int i = 0; i < count; i++) {
            Integer type = Integer.valueOf(this.mListAdapter.getItemViewType(i));
            if (!this.mTypeToView.containsKey(type)) {
                this.mTypeToView.put(type, new TypeViewInfo(type));
            }
            TypeViewInfo info2 = this.mTypeToView.get(type);
            View canvasView = info2.currentUsedPosition < info2.holdViews.size() ? info2.holdViews.get(info2.currentUsedPosition) : null;
            View current = this.mListAdapter.getView(i, canvasView, this);
            if (current == null) {
                throw new RuntimeException("Adapter.getView(postion , convasView, viewParent) cannot be null ");
            }
            if (canvasView != current) {
                if (canvasView == null) {
                    info2.holdViews.add(current);
                } else {
                    info2.holdViews.remove(canvasView);
                    info2.holdViews.add(info2.currentUsedPosition, current);
                }
            }
            info2.currentUsedPosition++;
            addView(current);
        }
        this.mSelectIndex = Math.min(this.mSelectIndex, (float) (count - 1));
        this.mSelectIndex = Math.max(0.0f, this.mSelectIndex);
        calculationRect(true);
    }

    public ViewPager.OnPageChangeListener getSupportOnPageChangeListener() {
        return this.mViewPageChangeListener;
    }

    public void setSlectIndex(int index) {
        if (this.mSelectIndex != ((float) index)) {
            this.mSelectIndex = (float) index;
            this.mSelectIndex = Math.min(this.mSelectIndex, (float) (getChildCount() - 1));
            this.mSelectIndex = Math.max(0.0f, this.mSelectIndex);
            calculationRect(false);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
        setClickable(this.mOnItemClickListener != null);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }
}
