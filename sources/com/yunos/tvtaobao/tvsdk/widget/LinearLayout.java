package com.yunos.tvtaobao.tvsdk.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;
import com.uc.webview.export.extension.UCCore;
import com.yunos.tvtaobao.tvsdk.utils.ReflectUtils;

@RemoteViews.RemoteView
@TargetApi(17)
public class LinearLayout extends ViewGroup {
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    @ViewDebug.ExportedProperty(category = "layout")
    private boolean mBaselineAligned;
    @ViewDebug.ExportedProperty(category = "layout")
    private int mBaselineAlignedChildIndex;
    @ViewDebug.ExportedProperty(category = "measurement")
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    @ViewDebug.ExportedProperty(category = "measurement", flagMapping = {@ViewDebug.FlagToString(equals = -1, mask = -1, name = "NONE"), @ViewDebug.FlagToString(equals = 0, mask = 0, name = "NONE"), @ViewDebug.FlagToString(equals = 48, mask = 48, name = "TOP"), @ViewDebug.FlagToString(equals = 80, mask = 80, name = "BOTTOM"), @ViewDebug.FlagToString(equals = 3, mask = 3, name = "LEFT"), @ViewDebug.FlagToString(equals = 5, mask = 5, name = "RIGHT"), @ViewDebug.FlagToString(equals = 8388611, mask = 8388611, name = "START"), @ViewDebug.FlagToString(equals = 8388613, mask = 8388613, name = "END"), @ViewDebug.FlagToString(equals = 16, mask = 16, name = "CENTER_VERTICAL"), @ViewDebug.FlagToString(equals = 112, mask = 112, name = "FILL_VERTICAL"), @ViewDebug.FlagToString(equals = 1, mask = 1, name = "CENTER_HORIZONTAL"), @ViewDebug.FlagToString(equals = 7, mask = 7, name = "FILL_HORIZONTAL"), @ViewDebug.FlagToString(equals = 17, mask = 17, name = "CENTER"), @ViewDebug.FlagToString(equals = 119, mask = 119, name = "FILL"), @ViewDebug.FlagToString(equals = 8388608, mask = 8388608, name = "RELATIVE")})
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    @ViewDebug.ExportedProperty(category = "measurement")
    private int mOrientation;
    private int mShowDividers;
    @ViewDebug.ExportedProperty(category = "measurement")
    private int mTotalLength;
    @ViewDebug.ExportedProperty(category = "layout")
    private boolean mUseLargestChild;
    @ViewDebug.ExportedProperty(category = "layout")
    private float mWeightSum;

    public LinearLayout(Context context) {
        super(context);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
    }

    public LinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TypedArray a = context.obtainStyledAttributes(attrs, ReflectUtils.getInternalIntArray("com.android.internal.R$styleable", "LinearLayout"), defStyle, 0);
        int index = a.getInt(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_orientation"), -1);
        if (index >= 0) {
            setOrientation(index);
        }
        int index2 = a.getInt(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_gravity"), -1);
        if (index2 >= 0) {
            setGravity(index2);
        }
        boolean baselineAligned = a.getBoolean(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_baselineAligned"), true);
        if (!baselineAligned) {
            setBaselineAligned(baselineAligned);
        }
        this.mWeightSum = a.getFloat(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_weightSum"), -1.0f);
        this.mBaselineAlignedChildIndex = a.getInt(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_baselineAlignedChildIndex"), -1);
        this.mUseLargestChild = a.getBoolean(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_measureWithLargestChild"), false);
        setDividerDrawable(a.getDrawable(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_divider")));
        this.mShowDividers = a.getInt(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_showDividers"), 0);
        this.mDividerPadding = a.getDimensionPixelSize(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_dividerPadding"), 0);
        a.recycle();
    }

    public void setShowDividers(int showDividers) {
        if (showDividers != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = showDividers;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable divider) {
        boolean z = false;
        if (divider != this.mDivider) {
            this.mDivider = divider;
            if (divider != null) {
                this.mDividerWidth = divider.getIntrinsicWidth();
                this.mDividerHeight = divider.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (divider == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int padding) {
        this.mDividerPadding = padding;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void drawDividersVertical(Canvas canvas) {
        int bottom;
        int count = getVirtualChildCount();
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8 || !hasDividerBeforeChildAt(i))) {
                drawHorizontalDivider(canvas, (child.getTop() - ((LayoutParams) child.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(count)) {
            View child2 = getVirtualChildAt(count - 1);
            if (child2 == null) {
                bottom = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                bottom = child2.getBottom() + ((LayoutParams) child2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, bottom);
        }
    }

    /* access modifiers changed from: package-private */
    public void drawDividersHorizontal(Canvas canvas) {
        int position;
        int position2;
        int count = getVirtualChildCount();
        boolean isLayoutRtl = isLayoutRtlByReflect();
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8 || !hasDividerBeforeChildAt(i))) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (isLayoutRtl) {
                    position2 = child.getRight() + lp.rightMargin;
                } else {
                    position2 = (child.getLeft() - lp.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, position2);
            }
        }
        if (hasDividerBeforeChildAt(count)) {
            View child2 = getVirtualChildAt(count - 1);
            if (child2 != null) {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (isLayoutRtl) {
                    position = (child2.getLeft() - lp2.leftMargin) - this.mDividerWidth;
                } else {
                    position = child2.getRight() + lp2.rightMargin;
                }
            } else if (isLayoutRtl) {
                position = getPaddingLeft();
            } else {
                position = (getWidth() - getPaddingRight()) - this.mDividerWidth;
            }
            drawVerticalDivider(canvas, position);
        }
    }

    /* access modifiers changed from: package-private */
    public void drawHorizontalDivider(Canvas canvas, int top) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, top, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + top);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: package-private */
    public void drawVerticalDivider(Canvas canvas, int left) {
        this.mDivider.setBounds(left, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + left, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public void setBaselineAligned(boolean baselineAligned) {
        this.mBaselineAligned = baselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    public void setMeasureWithLargestChildEnabled(boolean enabled) {
        this.mUseLargestChild = enabled;
    }

    public int getBaseline() {
        int majorGravity;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        if (getChildCount() <= this.mBaselineAlignedChildIndex) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
        View child = getChildAt(this.mBaselineAlignedChildIndex);
        int childBaseline = child.getBaseline();
        if (childBaseline != -1) {
            int childTop = this.mBaselineChildTop;
            if (this.mOrientation == 1 && (majorGravity = this.mGravity & 112) != 48) {
                switch (majorGravity) {
                    case 16:
                        childTop += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                        break;
                    case 80:
                        childTop = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                        break;
                }
            }
            return ((LayoutParams) child.getLayoutParams()).topMargin + childTop + childBaseline;
        } else if (this.mBaselineAlignedChildIndex == 0) {
            return -1;
        } else {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
        }
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
        }
        this.mBaselineAlignedChildIndex = i;
    }

    /* access modifiers changed from: package-private */
    public View getVirtualChildAt(int index) {
        return getChildAt(index);
    }

    /* access modifiers changed from: package-private */
    public int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float weightSum) {
        this.mWeightSum = Math.max(0.0f, weightSum);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mOrientation == 1) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /* access modifiers changed from: protected */
    public boolean hasDividerBeforeChildAt(int childIndex) {
        if (childIndex == 0) {
            if ((this.mShowDividers & 1) != 0) {
                return true;
            }
            return false;
        } else if (childIndex == getChildCount()) {
            if ((this.mShowDividers & 4) == 0) {
                return false;
            }
            return true;
        } else if ((this.mShowDividers & 2) == 0) {
            return false;
        } else {
            boolean hasVisibleViewBefore = false;
            int i = childIndex - 1;
            while (true) {
                if (i < 0) {
                    break;
                } else if (getChildAt(i).getVisibility() != 8) {
                    hasVisibleViewBefore = true;
                    break;
                } else {
                    i--;
                }
            }
            return hasVisibleViewBefore;
        }
    }

    /* access modifiers changed from: package-private */
    public void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        float weightSum;
        this.mTotalLength = 0;
        int maxWidth = 0;
        int childState = 0;
        int alternativeMaxWidth = 0;
        int weightedMaxWidth = 0;
        boolean allFillParent = true;
        float totalWeight = 0.0f;
        int count = getVirtualChildCount();
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        boolean matchWidth = false;
        int baselineChildIndex = this.mBaselineAlignedChildIndex;
        boolean useLargestChild = this.mUseLargestChild;
        int largestChildHeight = Integer.MIN_VALUE;
        int i = 0;
        while (i < count) {
            View child = getVirtualChildAt(i);
            if (child == null) {
                this.mTotalLength += measureNullChild(i);
            } else if (child.getVisibility() == 8) {
                i += getChildrenSkipCount(child, i);
            } else {
                if (hasDividerBeforeChildAt(i)) {
                    this.mTotalLength += this.mDividerHeight;
                }
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                totalWeight += lp.weight;
                if (heightMode == 1073741824 && lp.height == 0 && lp.weight > 0.0f) {
                    int totalLength = this.mTotalLength;
                    this.mTotalLength = Math.max(totalLength, lp.topMargin + totalLength + lp.bottomMargin);
                } else {
                    int oldHeight = Integer.MIN_VALUE;
                    if (lp.height == 0 && lp.weight > 0.0f) {
                        oldHeight = 0;
                        lp.height = -2;
                    }
                    measureChildBeforeLayout(child, i, widthMeasureSpec, 0, heightMeasureSpec, totalWeight == 0.0f ? this.mTotalLength : 0);
                    if (oldHeight != Integer.MIN_VALUE) {
                        lp.height = oldHeight;
                    }
                    int childHeight = child.getMeasuredHeight();
                    int totalLength2 = this.mTotalLength;
                    this.mTotalLength = Math.max(totalLength2, totalLength2 + childHeight + lp.topMargin + lp.bottomMargin + getNextLocationOffset(child));
                    if (useLargestChild) {
                        largestChildHeight = Math.max(childHeight, largestChildHeight);
                    }
                }
                if (baselineChildIndex >= 0 && baselineChildIndex == i + 1) {
                    this.mBaselineChildTop = this.mTotalLength;
                }
                if (i >= baselineChildIndex || lp.weight <= 0.0f) {
                    boolean matchWidthLocally = false;
                    if (widthMode != 1073741824 && lp.width == -1) {
                        matchWidth = true;
                        matchWidthLocally = true;
                    }
                    int margin = lp.leftMargin + lp.rightMargin;
                    int measuredWidth = child.getMeasuredWidth() + margin;
                    maxWidth = Math.max(maxWidth, measuredWidth);
                    childState = combineMeasuredStates(childState, child.getMeasuredState());
                    allFillParent = allFillParent && lp.width == -1;
                    if (lp.weight > 0.0f) {
                        if (!matchWidthLocally) {
                            margin = measuredWidth;
                        }
                        weightedMaxWidth = Math.max(weightedMaxWidth, margin);
                    } else {
                        if (!matchWidthLocally) {
                            margin = measuredWidth;
                        }
                        alternativeMaxWidth = Math.max(alternativeMaxWidth, margin);
                    }
                    i += getChildrenSkipCount(child, i);
                } else {
                    throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                }
            }
            i++;
        }
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(count)) {
            this.mTotalLength += this.mDividerHeight;
        }
        if (useLargestChild && (heightMode == Integer.MIN_VALUE || heightMode == 0)) {
            this.mTotalLength = 0;
            int i2 = 0;
            while (i2 < count) {
                View child2 = getVirtualChildAt(i2);
                if (child2 == null) {
                    this.mTotalLength += measureNullChild(i2);
                } else if (child2.getVisibility() == 8) {
                    i2 += getChildrenSkipCount(child2, i2);
                } else {
                    LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                    int totalLength3 = this.mTotalLength;
                    this.mTotalLength = Math.max(totalLength3, totalLength3 + largestChildHeight + lp2.topMargin + lp2.bottomMargin + getNextLocationOffset(child2));
                }
                i2++;
            }
        }
        this.mTotalLength += getPaddingTop() + getPaddingBottom();
        int heightSizeAndState = resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumHeight()), heightMeasureSpec, 0);
        int delta = (heightSizeAndState & ViewCompat.MEASURED_SIZE_MASK) - this.mTotalLength;
        if (delta == 0 || totalWeight <= 0.0f) {
            alternativeMaxWidth = Math.max(alternativeMaxWidth, weightedMaxWidth);
            if (useLargestChild && heightMode != 1073741824) {
                for (int i3 = 0; i3 < count; i3++) {
                    View child3 = getVirtualChildAt(i3);
                    if (!(child3 == null || child3.getVisibility() == 8 || ((LayoutParams) child3.getLayoutParams()).weight <= 0.0f)) {
                        child3.measure(View.MeasureSpec.makeMeasureSpec(child3.getMeasuredWidth(), UCCore.VERIFY_POLICY_QUICK), View.MeasureSpec.makeMeasureSpec(largestChildHeight, UCCore.VERIFY_POLICY_QUICK));
                    }
                }
            }
        } else {
            if (this.mWeightSum > 0.0f) {
                weightSum = this.mWeightSum;
            } else {
                weightSum = totalWeight;
            }
            this.mTotalLength = 0;
            for (int i4 = 0; i4 < count; i4++) {
                View child4 = getVirtualChildAt(i4);
                if (child4.getVisibility() != 8) {
                    LayoutParams lp3 = (LayoutParams) child4.getLayoutParams();
                    float childExtra = lp3.weight;
                    if (childExtra > 0.0f) {
                        int share = (int) ((((float) delta) * childExtra) / weightSum);
                        weightSum -= childExtra;
                        delta -= share;
                        int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight() + lp3.leftMargin + lp3.rightMargin, lp3.width);
                        if (lp3.height == 0 && heightMode == 1073741824) {
                            if (share <= 0) {
                                share = 0;
                            }
                            child4.measure(childWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(share, UCCore.VERIFY_POLICY_QUICK));
                        } else {
                            int childHeight2 = child4.getMeasuredHeight() + share;
                            if (childHeight2 < 0) {
                                childHeight2 = 0;
                            }
                            child4.measure(childWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(childHeight2, UCCore.VERIFY_POLICY_QUICK));
                        }
                        childState = combineMeasuredStates(childState, child4.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                    }
                    int margin2 = lp3.leftMargin + lp3.rightMargin;
                    int measuredWidth2 = child4.getMeasuredWidth() + margin2;
                    maxWidth = Math.max(maxWidth, measuredWidth2);
                    if (!(widthMode != 1073741824 && lp3.width == -1)) {
                        margin2 = measuredWidth2;
                    }
                    alternativeMaxWidth = Math.max(alternativeMaxWidth, margin2);
                    allFillParent = allFillParent && lp3.width == -1;
                    int totalLength4 = this.mTotalLength;
                    this.mTotalLength = Math.max(totalLength4, child4.getMeasuredHeight() + totalLength4 + lp3.topMargin + lp3.bottomMargin + getNextLocationOffset(child4));
                }
            }
            this.mTotalLength += getPaddingTop() + getPaddingBottom();
        }
        if (!allFillParent && widthMode != 1073741824) {
            maxWidth = alternativeMaxWidth;
        }
        setMeasuredDimension(resolveSizeAndState(Math.max(maxWidth + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), widthMeasureSpec, childState), heightSizeAndState);
        if (matchWidth) {
            forceUniformWidth(count, heightMeasureSpec);
        }
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), UCCore.VERIFY_POLICY_QUICK);
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.width == -1) {
                    int oldHeight = lp.height;
                    lp.height = child.getMeasuredHeight();
                    measureChildWithMargins(child, uniformMeasureSpec, 0, heightMeasureSpec, 0);
                    lp.height = oldHeight;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        float weightSum;
        int childBaseline;
        int i;
        int childBaseline2;
        int i2;
        this.mTotalLength = 0;
        int maxHeight = 0;
        int childState = 0;
        int alternativeMaxHeight = 0;
        int weightedMaxHeight = 0;
        boolean allFillParent = true;
        float totalWeight = 0.0f;
        int count = getVirtualChildCount();
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        boolean matchHeight = false;
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] maxAscent = this.mMaxAscent;
        int[] maxDescent = this.mMaxDescent;
        maxAscent[3] = -1;
        maxAscent[2] = -1;
        maxAscent[1] = -1;
        maxAscent[0] = -1;
        maxDescent[3] = -1;
        maxDescent[2] = -1;
        maxDescent[1] = -1;
        maxDescent[0] = -1;
        boolean baselineAligned = this.mBaselineAligned;
        boolean useLargestChild = this.mUseLargestChild;
        boolean isExactly = widthMode == 1073741824;
        int largestChildWidth = Integer.MIN_VALUE;
        int i3 = 0;
        while (i3 < count) {
            View child = getVirtualChildAt(i3);
            if (child == null) {
                this.mTotalLength += measureNullChild(i3);
            } else if (child.getVisibility() == 8) {
                i3 += getChildrenSkipCount(child, i3);
            } else {
                if (hasDividerBeforeChildAt(i3)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                totalWeight += lp.weight;
                if (widthMode == 1073741824 && lp.width == 0 && lp.weight > 0.0f) {
                    if (isExactly) {
                        this.mTotalLength += lp.leftMargin + lp.rightMargin;
                    } else {
                        int totalLength = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength, lp.leftMargin + totalLength + lp.rightMargin);
                    }
                    if (baselineAligned) {
                        int freeSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                        child.measure(freeSpec, freeSpec);
                    }
                } else {
                    int oldWidth = Integer.MIN_VALUE;
                    if (lp.width == 0 && lp.weight > 0.0f) {
                        oldWidth = 0;
                        lp.width = -2;
                    }
                    measureChildBeforeLayout(child, i3, widthMeasureSpec, totalWeight == 0.0f ? this.mTotalLength : 0, heightMeasureSpec, 0);
                    if (oldWidth != Integer.MIN_VALUE) {
                        lp.width = oldWidth;
                    }
                    int childWidth = child.getMeasuredWidth();
                    if (isExactly) {
                        this.mTotalLength += lp.leftMargin + childWidth + lp.rightMargin + getNextLocationOffset(child);
                    } else {
                        int totalLength2 = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength2, totalLength2 + childWidth + lp.leftMargin + lp.rightMargin + getNextLocationOffset(child));
                    }
                    if (useLargestChild) {
                        largestChildWidth = Math.max(childWidth, largestChildWidth);
                    }
                }
                boolean matchHeightLocally = false;
                if (heightMode != 1073741824 && lp.height == -1) {
                    matchHeight = true;
                    matchHeightLocally = true;
                }
                int margin = lp.topMargin + lp.bottomMargin;
                int childHeight = child.getMeasuredHeight() + margin;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (baselineAligned && (childBaseline2 = child.getBaseline()) != -1) {
                    if (lp.gravity < 0) {
                        i2 = this.mGravity;
                    } else {
                        i2 = lp.gravity;
                    }
                    int index = (((i2 & 112) >> 4) & -2) >> 1;
                    maxAscent[index] = Math.max(maxAscent[index], childBaseline2);
                    maxDescent[index] = Math.max(maxDescent[index], childHeight - childBaseline2);
                }
                maxHeight = Math.max(maxHeight, childHeight);
                allFillParent = allFillParent && lp.height == -1;
                if (lp.weight > 0.0f) {
                    if (!matchHeightLocally) {
                        margin = childHeight;
                    }
                    weightedMaxHeight = Math.max(weightedMaxHeight, margin);
                } else {
                    if (!matchHeightLocally) {
                        margin = childHeight;
                    }
                    alternativeMaxHeight = Math.max(alternativeMaxHeight, margin);
                }
                i3 += getChildrenSkipCount(child, i3);
            }
            i3++;
        }
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(count)) {
            this.mTotalLength += this.mDividerWidth;
        }
        if (!(maxAscent[1] == -1 && maxAscent[0] == -1 && maxAscent[2] == -1 && maxAscent[3] == -1)) {
            maxHeight = Math.max(maxHeight, Math.max(maxAscent[3], Math.max(maxAscent[0], Math.max(maxAscent[1], maxAscent[2]))) + Math.max(maxDescent[3], Math.max(maxDescent[0], Math.max(maxDescent[1], maxDescent[2]))));
        }
        if (useLargestChild && (widthMode == Integer.MIN_VALUE || widthMode == 0)) {
            this.mTotalLength = 0;
            int i4 = 0;
            while (i4 < count) {
                View child2 = getVirtualChildAt(i4);
                if (child2 == null) {
                    this.mTotalLength += measureNullChild(i4);
                } else if (child2.getVisibility() == 8) {
                    i4 += getChildrenSkipCount(child2, i4);
                } else {
                    LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                    if (isExactly) {
                        this.mTotalLength += lp2.leftMargin + largestChildWidth + lp2.rightMargin + getNextLocationOffset(child2);
                    } else {
                        int totalLength3 = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength3, totalLength3 + largestChildWidth + lp2.leftMargin + lp2.rightMargin + getNextLocationOffset(child2));
                    }
                }
                i4++;
            }
        }
        this.mTotalLength += getPaddingLeft() + getPaddingRight();
        int widthSizeAndState = resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumWidth()), widthMeasureSpec, 0);
        int delta = (widthSizeAndState & ViewCompat.MEASURED_SIZE_MASK) - this.mTotalLength;
        if (delta == 0 || totalWeight <= 0.0f) {
            alternativeMaxHeight = Math.max(alternativeMaxHeight, weightedMaxHeight);
            if (useLargestChild && widthMode != 1073741824) {
                for (int i5 = 0; i5 < count; i5++) {
                    View child3 = getVirtualChildAt(i5);
                    if (!(child3 == null || child3.getVisibility() == 8 || ((LayoutParams) child3.getLayoutParams()).weight <= 0.0f)) {
                        child3.measure(View.MeasureSpec.makeMeasureSpec(largestChildWidth, UCCore.VERIFY_POLICY_QUICK), View.MeasureSpec.makeMeasureSpec(child3.getMeasuredHeight(), UCCore.VERIFY_POLICY_QUICK));
                    }
                }
            }
        } else {
            if (this.mWeightSum > 0.0f) {
                weightSum = this.mWeightSum;
            } else {
                weightSum = totalWeight;
            }
            maxAscent[3] = -1;
            maxAscent[2] = -1;
            maxAscent[1] = -1;
            maxAscent[0] = -1;
            maxDescent[3] = -1;
            maxDescent[2] = -1;
            maxDescent[1] = -1;
            maxDescent[0] = -1;
            maxHeight = -1;
            this.mTotalLength = 0;
            for (int i6 = 0; i6 < count; i6++) {
                View child4 = getVirtualChildAt(i6);
                if (!(child4 == null || child4.getVisibility() == 8)) {
                    LayoutParams lp3 = (LayoutParams) child4.getLayoutParams();
                    float childExtra = lp3.weight;
                    if (childExtra > 0.0f) {
                        int share = (int) ((((float) delta) * childExtra) / weightSum);
                        weightSum -= childExtra;
                        delta -= share;
                        int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp3.topMargin + lp3.bottomMargin, lp3.height);
                        if (lp3.width == 0 && widthMode == 1073741824) {
                            if (share <= 0) {
                                share = 0;
                            }
                            child4.measure(View.MeasureSpec.makeMeasureSpec(share, UCCore.VERIFY_POLICY_QUICK), childHeightMeasureSpec);
                        } else {
                            int childWidth2 = child4.getMeasuredWidth() + share;
                            if (childWidth2 < 0) {
                                childWidth2 = 0;
                            }
                            child4.measure(View.MeasureSpec.makeMeasureSpec(childWidth2, UCCore.VERIFY_POLICY_QUICK), childHeightMeasureSpec);
                        }
                        childState = combineMeasuredStates(childState, child4.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                    }
                    if (isExactly) {
                        this.mTotalLength += child4.getMeasuredWidth() + lp3.leftMargin + lp3.rightMargin + getNextLocationOffset(child4);
                    } else {
                        int totalLength4 = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength4, child4.getMeasuredWidth() + totalLength4 + lp3.leftMargin + lp3.rightMargin + getNextLocationOffset(child4));
                    }
                    boolean matchHeightLocally2 = heightMode != 1073741824 && lp3.height == -1;
                    int margin2 = lp3.topMargin + lp3.bottomMargin;
                    int childHeight2 = child4.getMeasuredHeight() + margin2;
                    maxHeight = Math.max(maxHeight, childHeight2);
                    if (!matchHeightLocally2) {
                        margin2 = childHeight2;
                    }
                    alternativeMaxHeight = Math.max(alternativeMaxHeight, margin2);
                    allFillParent = allFillParent && lp3.height == -1;
                    if (baselineAligned && (childBaseline = child4.getBaseline()) != -1) {
                        if (lp3.gravity < 0) {
                            i = this.mGravity;
                        } else {
                            i = lp3.gravity;
                        }
                        int index2 = (((i & 112) >> 4) & -2) >> 1;
                        maxAscent[index2] = Math.max(maxAscent[index2], childBaseline);
                        maxDescent[index2] = Math.max(maxDescent[index2], childHeight2 - childBaseline);
                    }
                }
            }
            this.mTotalLength += getPaddingLeft() + getPaddingRight();
            if (!(maxAscent[1] == -1 && maxAscent[0] == -1 && maxAscent[2] == -1 && maxAscent[3] == -1)) {
                maxHeight = Math.max(maxHeight, Math.max(maxAscent[3], Math.max(maxAscent[0], Math.max(maxAscent[1], maxAscent[2]))) + Math.max(maxDescent[3], Math.max(maxDescent[0], Math.max(maxDescent[1], maxDescent[2]))));
            }
        }
        if (!allFillParent && heightMode != 1073741824) {
            maxHeight = alternativeMaxHeight;
        }
        setMeasuredDimension((-16777216 & childState) | widthSizeAndState, resolveSizeAndState(Math.max(maxHeight + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), heightMeasureSpec, childState << 16));
        if (matchHeight) {
            forceUniformHeight(count, widthMeasureSpec);
        }
    }

    private void forceUniformHeight(int count, int widthMeasureSpec) {
        int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), UCCore.VERIFY_POLICY_QUICK);
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.height == -1) {
                    int oldWidth = lp.width;
                    lp.width = child.getMeasuredWidth();
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int getChildrenSkipCount(View child, int index) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int measureNullChild(int childIndex) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        measureChildWithMargins(child, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    /* access modifiers changed from: package-private */
    public int getLocationOffset(View child) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getNextLocationOffset(View child) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.mOrientation == 1) {
            layoutVertical();
        } else {
            layoutHorizontal();
        }
        afterLayout(changed, l, t, r, b);
    }

    /* access modifiers changed from: package-private */
    public void layoutVertical() {
        int childTop;
        int childLeft;
        int paddingLeft = getPaddingLeft();
        int width = getRight() - getLeft();
        int childRight = width - getPaddingRight();
        int childSpace = (width - paddingLeft) - getPaddingRight();
        int count = getVirtualChildCount();
        int majorGravity = this.mGravity & 112;
        int minorGravity = this.mGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        switch (majorGravity) {
            case 16:
                childTop = getPaddingTop() + (((getBottom() - getTop()) - this.mTotalLength) / 2);
                break;
            case 80:
                childTop = ((getPaddingTop() + getBottom()) - getTop()) - this.mTotalLength;
                break;
            default:
                childTop = getPaddingTop();
                break;
        }
        int i = 0;
        while (i < count) {
            View child = getVirtualChildAt(i);
            if (child == null) {
                childTop += measureNullChild(i);
            } else if (child.getVisibility() != 8) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = minorGravity;
                }
                switch (Gravity.getAbsoluteGravity(gravity, getLayoutDirection()) & 7) {
                    case 1:
                        childLeft = ((((childSpace - childWidth) / 2) + paddingLeft) + lp.leftMargin) - lp.rightMargin;
                        break;
                    case 5:
                        childLeft = (childRight - childWidth) - lp.rightMargin;
                        break;
                    default:
                        childLeft = paddingLeft + lp.leftMargin;
                        break;
                }
                if (hasDividerBeforeChildAt(i)) {
                    childTop += this.mDividerHeight;
                }
                int childTop2 = childTop + lp.topMargin;
                setChildFrame(child, childLeft, childTop2 + getLocationOffset(child), childWidth, childHeight);
                childTop = childTop2 + lp.bottomMargin + childHeight + getNextLocationOffset(child);
                i += getChildrenSkipCount(child, i);
            }
            i++;
        }
    }

    /* access modifiers changed from: package-private */
    public void layoutHorizontal() {
        int childLeft;
        int childTop;
        boolean isLayoutRtl = isLayoutRtlByReflect();
        int paddingTop = getPaddingTop();
        int height = getBottom() - getTop();
        int childBottom = height - getPaddingBottom();
        int childSpace = (height - paddingTop) - getPaddingBottom();
        int count = getVirtualChildCount();
        int majorGravity = this.mGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int minorGravity = this.mGravity & 112;
        boolean baselineAligned = this.mBaselineAligned;
        int[] maxAscent = this.mMaxAscent;
        int[] maxDescent = this.mMaxDescent;
        switch (Gravity.getAbsoluteGravity(majorGravity, getLayoutDirection())) {
            case 1:
                childLeft = getPaddingLeft() + (((getRight() - getLeft()) - this.mTotalLength) / 2);
                break;
            case 5:
                childLeft = ((getPaddingLeft() + getRight()) - getLeft()) - this.mTotalLength;
                break;
            default:
                childLeft = getPaddingLeft();
                break;
        }
        int start = 0;
        int dir = 1;
        if (isLayoutRtl) {
            start = count - 1;
            dir = -1;
        }
        int i = 0;
        while (i < count) {
            int childIndex = start + (dir * i);
            View child = getVirtualChildAt(childIndex);
            if (child == null) {
                childLeft += measureNullChild(childIndex);
            } else if (child.getVisibility() != 8) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                int childBaseline = -1;
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (baselineAligned && lp.height != -1) {
                    childBaseline = child.getBaseline();
                }
                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = minorGravity;
                }
                switch (gravity & 112) {
                    case 16:
                        childTop = ((((childSpace - childHeight) / 2) + paddingTop) + lp.topMargin) - lp.bottomMargin;
                        break;
                    case 48:
                        childTop = paddingTop + lp.topMargin;
                        if (childBaseline != -1) {
                            childTop += maxAscent[1] - childBaseline;
                            break;
                        }
                        break;
                    case 80:
                        childTop = (childBottom - childHeight) - lp.bottomMargin;
                        if (childBaseline != -1) {
                            childTop -= maxDescent[2] - (child.getMeasuredHeight() - childBaseline);
                            break;
                        }
                        break;
                    default:
                        childTop = paddingTop;
                        break;
                }
                if (hasDividerBeforeChildAt(childIndex)) {
                    childLeft += this.mDividerWidth;
                }
                int childLeft2 = childLeft + lp.leftMargin;
                setChildFrame(child, childLeft2 + getLocationOffset(child), childTop, childWidth, childHeight);
                childLeft = childLeft2 + lp.rightMargin + childWidth + getNextLocationOffset(child);
                i += getChildrenSkipCount(child, childIndex);
            }
            i++;
        }
    }

    private boolean isLayoutRtlByReflect() {
        try {
            return ((Boolean) ReflectUtils.invokeMethod(this, "isLayoutRtl", new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    public void setOrientation(int orientation) {
        if (this.mOrientation != orientation) {
            this.mOrientation = orientation;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            if ((8388615 & gravity) == 0) {
                gravity |= GravityCompat.START;
            }
            if ((gravity & 112) == 0) {
                gravity |= 48;
            }
            this.mGravity = gravity;
            requestLayout();
        }
    }

    public void setHorizontalGravity(int horizontalGravity) {
        int gravity = horizontalGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if ((this.mGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) != gravity) {
            this.mGravity = (this.mGravity & -8388616) | gravity;
            requestLayout();
        }
    }

    public void setVerticalGravity(int verticalGravity) {
        int gravity = verticalGravity & 112;
        if ((this.mGravity & 112) != gravity) {
            this.mGravity = (this.mGravity & -113) | gravity;
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -2);
        }
        if (this.mOrientation == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(LinearLayout.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(LinearLayout.class.getName());
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        @ViewDebug.ExportedProperty(category = "layout", mapping = {@ViewDebug.IntToString(from = -1, to = "NONE"), @ViewDebug.IntToString(from = 0, to = "NONE"), @ViewDebug.IntToString(from = 48, to = "TOP"), @ViewDebug.IntToString(from = 80, to = "BOTTOM"), @ViewDebug.IntToString(from = 3, to = "LEFT"), @ViewDebug.IntToString(from = 5, to = "RIGHT"), @ViewDebug.IntToString(from = 8388611, to = "START"), @ViewDebug.IntToString(from = 8388613, to = "END"), @ViewDebug.IntToString(from = 16, to = "CENTER_VERTICAL"), @ViewDebug.IntToString(from = 112, to = "FILL_VERTICAL"), @ViewDebug.IntToString(from = 1, to = "CENTER_HORIZONTAL"), @ViewDebug.IntToString(from = 7, to = "FILL_HORIZONTAL"), @ViewDebug.IntToString(from = 17, to = "CENTER"), @ViewDebug.IntToString(from = 119, to = "FILL")})
        public int gravity;
        @ViewDebug.ExportedProperty(category = "layout")
        public float weight;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.gravity = -1;
            TypedArray a = c.obtainStyledAttributes(ReflectUtils.getInternalIntArray("com.android.internal.R$styleable", "LinearLayout_Layout"));
            this.weight = a.getFloat(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_Layout_layout_weight"), 0.0f);
            this.gravity = a.getInt(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "LinearLayout_Layout_layout_gravity"), -1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int width, int height, float weight2) {
            super(width, height);
            this.gravity = -1;
            this.weight = weight2;
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
            this.gravity = -1;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.gravity = -1;
        }

        public String debug(String output) {
            return output + "LinearLayout.LayoutParams={width=" + this.width + ", height=" + this.height + " weight=" + this.weight + "}";
        }
    }

    public Rect getClipFocusRect() {
        return null;
    }
}
