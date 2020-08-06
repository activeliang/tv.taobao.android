package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.bftv.fui.constantplugin.Constant;
import com.uc.webview.export.extension.UCCore;
import com.yunos.tvtaobao.biz.util.VisualMarkConfig;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import mtopsdk.common.util.SymbolExpUtil;

public class SdkViewPager extends ViewGroup {
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "SdkViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            float t2 = t - 1.0f;
            return (t2 * t2 * t2) + 1.0f;
        }
    };
    private PagerAdapter mAdapter;
    private float mBaseLineFlingVelocity;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCurItem;
    private boolean mFirstLayout = true;
    private float mFlingVelocityInfluence;
    private boolean mInLayout;
    private final ArrayList<ItemInfo> mItems = new ArrayList<>();
    private Drawable mMarginDrawable;
    private int mOffscreenPageLimit = 1;
    private OnPageChangeListener mOnPageChangeListener = null;
    private int mPageGap = 0;
    private int mPageMargin;
    private int mPageWidth = 0;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private int mScrollState = 0;
    private Scroller mScroller;
    private boolean mScrolling;
    private boolean mScrollingCacheEnabled;

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    static class ItemInfo {
        Object object;
        int position;
        boolean scrolling;

        ItemInfo() {
        }
    }

    public SdkViewPager(Context context) {
        super(context);
        initViewPager();
    }

    public SdkViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager();
    }

    /* access modifiers changed from: package-private */
    public void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        this.mBaseLineFlingVelocity = 2500.0f * context.getResources().getDisplayMetrics().density;
        this.mFlingVelocityInfluence = 0.4f;
        this.mPageWidth = VisualMarkConfig.PAGE_WIDTH + VisualMarkConfig.PAGEVIEW_GAP;
        this.mPageGap = 14;
    }

    private void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageScrollStateChanged(newState);
            }
        }
    }

    public void setAdapter(PagerAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.startUpdate((ViewGroup) this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo ii = this.mItems.get(i);
                this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeAllViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            this.mPopulatePending = false;
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
                return;
            }
            populate();
        }
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setCurrentItem(int item) {
        boolean z;
        this.mPopulatePending = false;
        if (!this.mFirstLayout) {
            z = true;
        } else {
            z = false;
        }
        setCurrentItemInternal(item, z, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    /* access modifiers changed from: package-private */
    public void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    /* access modifiers changed from: package-private */
    public void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        boolean dispatchSelected = true;
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(false);
        } else if (always || this.mCurItem != item || this.mItems.size() == 0) {
            if (item < 0) {
                item = 0;
            } else if (item >= this.mAdapter.getCount()) {
                item = this.mAdapter.getCount() - 1;
            }
            int pageLimit = this.mOffscreenPageLimit;
            if (item > this.mCurItem + pageLimit || item < this.mCurItem - pageLimit) {
                for (int i = 0; i < this.mItems.size(); i++) {
                    this.mItems.get(i).scrolling = true;
                }
            }
            if (this.mCurItem == item) {
                dispatchSelected = false;
            }
            this.mCurItem = item;
            populate();
            int destX = (this.mPageMargin + (this.mPageWidth == 0 ? getWidth() : this.mPageWidth)) * item;
            if (smoothScroll) {
                smoothScrollTo(destX, 0, velocity);
                if (dispatchSelected && this.mOnPageChangeListener != null) {
                    this.mOnPageChangeListener.onPageSelected(item);
                    return;
                }
                return;
            }
            if (dispatchSelected && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(item);
            }
            completeScroll();
            scrollTo(destX, 0);
        } else {
            setScrollingCacheEnabled(false);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            ZpLogger.i(TAG, "Requested offscreen page limit " + limit + " too small; defaulting to " + 1);
            limit = 1;
        }
        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            populate();
        }
    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = getWidth();
        recomputeScrollPosition(width, width, marginPixels, oldMargin);
        requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            refreshDrawableState();
        }
        setWillNotDraw(d == null);
        invalidate();
    }

    public void setPageMarginDrawable(int resId) {
        setPageMarginDrawable(getContext().getResources().getDrawable(resId));
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mMarginDrawable;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

    /* access modifiers changed from: package-private */
    public float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    /* access modifiers changed from: package-private */
    public void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, 0);
    }

    /* access modifiers changed from: package-private */
    public void smoothScrollTo(int x, int y, int velocity) {
        int duration;
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int sx = getScrollX();
        int sy = getScrollY();
        int dx = x - sx;
        int dy = y - sy;
        if (dx == 0 && dy == 0) {
            completeScroll();
            setScrollState(0);
            return;
        }
        setScrollingCacheEnabled(true);
        this.mScrolling = true;
        setScrollState(2);
        int duration2 = (int) (100.0f * (((float) Math.abs(dx)) / ((float) (getWidth() + this.mPageMargin))));
        int velocity2 = Math.abs(velocity);
        if (velocity2 > 0) {
            duration = (int) (((float) duration2) + ((((float) duration2) / (((float) velocity2) / this.mBaseLineFlingVelocity)) * this.mFlingVelocityInfluence));
        } else {
            duration = duration2 + 100;
        }
        this.mScroller.startScroll(sx, sy, dx, dy, Math.min(duration, 600));
        invalidate();
    }

    /* access modifiers changed from: package-private */
    public void addNewItem(int position, int index) {
        Object object = this.mAdapter.instantiateItem((ViewGroup) this, position);
        if (object != null) {
            ItemInfo ii = new ItemInfo();
            ii.position = position;
            ii.object = object;
            if (index < 0) {
                this.mItems.add(ii);
            } else {
                this.mItems.add(index, ii);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void populate() {
        Object obj;
        ItemInfo ii;
        if (this.mAdapter != null) {
            if (this.mPopulatePending) {
                ZpLogger.i(TAG, "populate is pending, skipping for now...");
            } else if (getWindowToken() != null) {
                this.mAdapter.startUpdate((ViewGroup) this);
                int pageLimit = this.mOffscreenPageLimit;
                int startPos = Math.max(0, this.mCurItem - pageLimit);
                int endPos = Math.min(this.mAdapter.getCount() - 1, this.mCurItem + pageLimit);
                ZpLogger.i(TAG, "populating: startPos=" + startPos + " endPos=" + endPos);
                int lastPos = -1;
                int i = 0;
                while (i < this.mItems.size()) {
                    ItemInfo ii2 = this.mItems.get(i);
                    if ((ii2.position < startPos || ii2.position > endPos) && !ii2.scrolling) {
                        ZpLogger.i(TAG, "removing: " + ii2.position + " @ " + i);
                        this.mItems.remove(i);
                        i--;
                        this.mAdapter.destroyItem((ViewGroup) this, ii2.position, ii2.object);
                    } else if (lastPos < endPos && ii2.position > startPos) {
                        int lastPos2 = lastPos + 1;
                        if (lastPos2 < startPos) {
                            lastPos2 = startPos;
                        }
                        while (lastPos2 <= endPos && lastPos2 < ii2.position) {
                            ZpLogger.i(TAG, "inserting: " + lastPos2 + " @ " + i);
                            addNewItem(lastPos2, i);
                            lastPos2++;
                            i++;
                        }
                    }
                    lastPos = ii2.position;
                    i++;
                }
                int lastPos3 = this.mItems.size() > 0 ? this.mItems.get(this.mItems.size() - 1).position : -1;
                if (lastPos3 < endPos) {
                    int lastPos4 = lastPos3 + 1;
                    if (lastPos4 <= startPos) {
                        lastPos4 = startPos;
                    }
                    while (lastPos4 <= endPos) {
                        ZpLogger.i(TAG, "appending: " + lastPos4);
                        addNewItem(lastPos4, -1);
                        lastPos4++;
                    }
                }
                ZpLogger.i(TAG, "Current page list:");
                for (int i2 = 0; i2 < this.mItems.size(); i2++) {
                    ZpLogger.i(TAG, Constant.INTENT_JSON_MARK + i2 + ": page " + this.mItems.get(i2).position);
                }
                ItemInfo curItem = null;
                int i3 = 0;
                while (true) {
                    if (i3 >= this.mItems.size()) {
                        break;
                    } else if (this.mItems.get(i3).position == this.mCurItem) {
                        curItem = this.mItems.get(i3);
                        break;
                    } else {
                        i3++;
                    }
                }
                PagerAdapter pagerAdapter = this.mAdapter;
                int i4 = this.mCurItem;
                if (curItem != null) {
                    obj = curItem.object;
                } else {
                    obj = null;
                }
                pagerAdapter.setPrimaryItem((ViewGroup) this, i4, obj);
                this.mAdapter.finishUpdate((ViewGroup) this);
                if (hasFocus()) {
                    View currentFocused = findFocus();
                    if (currentFocused != null) {
                        ii = infoForAnyChild(currentFocused);
                    } else {
                        ii = null;
                    }
                    if (ii == null || ii.position != this.mCurItem) {
                        int i5 = 0;
                        while (i5 < getChildCount()) {
                            View child = getChildAt(i5);
                            ItemInfo ii3 = infoForChild(child);
                            if (ii3 == null || ii3.position != this.mCurItem || !child.requestFocus(2)) {
                                i5++;
                            } else {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        });
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        SavedState(Parcel in, ClassLoader loader2) {
            super(in);
            loader2 = loader2 == null ? getClass().getClassLoader() : loader2;
            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader2);
            this.loader = loader2;
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.position = this.mCurItem;
        if (this.mAdapter != null) {
            ss.adapterState = this.mAdapter.saveState();
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (this.mAdapter != null) {
            this.mAdapter.restoreState(ss.adapterState, ss.loader);
            setCurrentItemInternal(ss.position, false, true);
            return;
        }
        this.mRestoredCurItem = ss.position;
        this.mRestoredAdapterState = ss.adapterState;
        this.mRestoredClassLoader = ss.loader;
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (this.mInLayout) {
            addViewInLayout(child, index, params);
            child.measure(this.mChildWidthMeasureSpec, this.mChildHeightMeasureSpec);
            return;
        }
        super.addView(child, index, params);
    }

    /* access modifiers changed from: package-private */
    public ItemInfo infoForChild(View child) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public ItemInfo infoForAnyChild(View child) {
        while (true) {
            ViewParent parent = child.getParent();
            if (parent == this) {
                return infoForChild(child);
            }
            if (parent == null || !(parent instanceof View)) {
                return null;
            }
            child = (View) parent;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), UCCore.VERIFY_POLICY_QUICK);
        this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), UCCore.VERIFY_POLICY_QUICK);
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                ZpLogger.i(TAG, "Measuring #" + i + " " + child + ": " + this.mChildWidthMeasureSpec);
                child.measure(this.mChildWidthMeasureSpec, this.mChildHeightMeasureSpec);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
        }
    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        int widthWithMargin = width + margin;
        if (oldWidth > 0) {
            int oldScrollPos = getScrollX();
            int oldwwm = oldWidth + oldMargin;
            int scrollPos = (int) ((((float) (oldScrollPos / oldwwm)) + (((float) (oldScrollPos % oldwwm)) / ((float) oldwwm))) * ((float) widthWithMargin));
            scrollTo(scrollPos, getScrollY());
            if (!this.mScroller.isFinished()) {
                this.mScroller.startScroll(scrollPos, 0, this.mCurItem * widthWithMargin, 0, this.mScroller.getDuration() - this.mScroller.timePassed());
                return;
            }
            return;
        }
        int scrollPos2 = this.mCurItem * widthWithMargin;
        if (scrollPos2 != getScrollX()) {
            completeScroll();
            scrollTo(scrollPos2, getScrollY());
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        ItemInfo ii;
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        int count = getChildCount();
        int width = VisualMarkConfig.PAGE_WIDTH + VisualMarkConfig.PAGEVIEW_GAP;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (!(child.getVisibility() == 8 || (ii = infoForChild(child)) == null)) {
                int childLeft = getPaddingLeft() + ((this.mPageMargin + width) * ii.position);
                int childTop = getPaddingTop();
                ZpLogger.i(TAG, "Positioning #" + i + " " + child + " f=" + ii.object + SymbolExpUtil.SYMBOL_COLON + childLeft + "," + childTop + " " + child.getMeasuredWidth() + "x" + child.getMeasuredHeight());
                child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
            }
        }
        this.mFirstLayout = false;
    }

    public void computeScroll() {
        ZpLogger.i(TAG, "computeScroll: finished=" + this.mScroller.isFinished());
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll();
            return;
        }
        ZpLogger.i(TAG, "computeScroll: still scrolling");
        int oldX = getScrollX();
        int oldY = getScrollY();
        int x = this.mScroller.getCurrX();
        int y = this.mScroller.getCurrY();
        if (!(oldX == x && oldY == y)) {
            scrollTo(x, y);
        }
        if (this.mOnPageChangeListener != null) {
            int widthWithMargin = (this.mPageWidth == 0 ? getWidth() : this.mPageWidth) + this.mPageMargin;
            int offsetPixels = x % widthWithMargin;
            this.mOnPageChangeListener.onPageScrolled(x / widthWithMargin, ((float) offsetPixels) / ((float) widthWithMargin), offsetPixels);
        }
        invalidate();
    }

    private void completeScroll() {
        boolean needPopulate = this.mScrolling;
        if (needPopulate) {
            setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (!(oldX == x && oldY == y)) {
                scrollTo(x, y);
            }
            setScrollState(0);
        }
        this.mPopulatePending = false;
        this.mScrolling = false;
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = this.mItems.get(i);
            if (ii.scrolling) {
                needPopulate = true;
                ii.scrolling = false;
            }
        }
        if (needPopulate) {
            populate();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null) {
            int scrollX = getScrollX();
            int width = getWidth();
            int offset = scrollX % (this.mPageMargin + width);
            if (offset != 0) {
                int left = (scrollX - offset) + width;
                this.mMarginDrawable.setBounds(left, 0, this.mPageMargin + left, getHeight());
                this.mMarginDrawable.draw(canvas);
            }
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }
    }

    /* access modifiers changed from: protected */
    public boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
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

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 21:
                return arrowScroll(17);
            case 22:
                return arrowScroll(66);
            case 61:
                if (KeyEventCompat.hasNoModifiers(event)) {
                    return arrowScroll(2);
                }
                if (KeyEventCompat.hasModifiers(event, 1)) {
                    return arrowScroll(1);
                }
                return false;
            default:
                return false;
        }
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        boolean handled = false;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused == null || nextFocused == currentFocused) {
            if (direction == 17 || direction == 1) {
                handled = pageLeft();
            } else if (direction == 66 || direction == 2) {
                handled = pageRight();
            }
        } else if (direction == 17) {
            handled = (currentFocused == null || nextFocused.getLeft() < currentFocused.getLeft()) ? nextFocused.requestFocus() : pageLeft();
        } else if (direction == 66) {
            handled = (currentFocused == null || nextFocused.getLeft() > currentFocused.getLeft()) ? nextFocused.requestFocus() : pageRight();
        }
        if (handled) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return handled;
    }

    /* access modifiers changed from: package-private */
    public boolean pageLeft() {
        if (this.mCurItem <= 0) {
            return false;
        }
        setCurrentItem(this.mCurItem - 1, true);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean pageRight() {
        if (this.mAdapter == null || this.mCurItem >= this.mAdapter.getCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        ItemInfo ii;
        if (views != null) {
            int focusableCount = views.size();
            int descendantFocusability = getDescendantFocusability();
            if (descendantFocusability != 393216) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() == 0 && (ii = infoForChild(child)) != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
            if ((descendantFocusability == 262144 && focusableCount != views.size()) || !isFocusable()) {
                return;
            }
            if (((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && views != null) {
                views.add(this);
            }
        }
    }

    public void addTouchables(ArrayList<View> views) {
        ItemInfo ii;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0 && (ii = infoForChild(child)) != null && ii.position == this.mCurItem) {
                child.addTouchables(views);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        ItemInfo ii;
        int count = getChildCount();
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        for (int i = index; i != end; i += increment) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0 && (ii = infoForChild(child)) != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        ItemInfo ii;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0 && (ii = infoForChild(child)) != null && ii.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(event)) {
                return true;
            }
        }
        return false;
    }
}
