package com.yunos.tvtaobao.tvsdk.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.GravityCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;
import com.taobao.ju.track.csv.CsvReader;
import com.uc.webview.export.extension.UCCore;
import com.yunos.tvtaobao.tvsdk.utils.ReflectUtils;
import com.yunos.tvtaobao.tvsdk.utils.pool.Pool;
import com.yunos.tvtaobao.tvsdk.utils.pool.Poolable;
import com.yunos.tvtaobao.tvsdk.utils.pool.PoolableManager;
import com.yunos.tvtaobao.tvsdk.utils.pool.Pools;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

@RemoteViews.RemoteView
@TargetApi(17)
public class RelativeLayout extends ViewGroup {
    public static final int ABOVE = 2;
    public static final int ALIGN_BASELINE = 4;
    public static final int ALIGN_BOTTOM = 8;
    public static final int ALIGN_END = 19;
    public static final int ALIGN_LEFT = 5;
    public static final int ALIGN_PARENT_BOTTOM = 12;
    public static final int ALIGN_PARENT_END = 21;
    public static final int ALIGN_PARENT_LEFT = 9;
    public static final int ALIGN_PARENT_RIGHT = 11;
    public static final int ALIGN_PARENT_START = 20;
    public static final int ALIGN_PARENT_TOP = 10;
    public static final int ALIGN_RIGHT = 7;
    public static final int ALIGN_START = 18;
    public static final int ALIGN_TOP = 6;
    public static final int BELOW = 3;
    public static final int CENTER_HORIZONTAL = 14;
    public static final int CENTER_IN_PARENT = 13;
    public static final int CENTER_VERTICAL = 15;
    private static final boolean DEBUG_GRAPH = false;
    public static final int END_OF = 17;
    public static final int LEFT_OF = 0;
    private static final String LOG_TAG = "RelativeLayout";
    public static final int RIGHT_OF = 1;
    private static final int[] RULES_HORIZONTAL = {0, 1, 5, 7, 16, 17, 18, 19};
    private static final int[] RULES_VERTICAL = {2, 3, 4, 6, 8};
    public static final int START_OF = 16;
    public static final int TRUE = -1;
    private static final int VERB_COUNT = 22;
    static boolean isLayoutRtl = false;
    private View mBaselineView = null;
    private final Rect mContentBounds = new Rect();
    private boolean mDirtyHierarchy;
    private final DependencyGraph mGraph = new DependencyGraph();
    private int mGravity = 8388659;
    private boolean mHasBaselineAlignedChild;
    private int mIgnoreGravity;
    private final Rect mSelfBounds = new Rect();
    private View[] mSortedHorizontalChildren = new View[0];
    private View[] mSortedVerticalChildren = new View[0];
    private SortedSet<View> mTopToBottomLeftToRightSet = null;

    public RelativeLayout(Context context) {
        super(context);
        isLayoutRtl = isLayoutRtlByReflect();
    }

    public RelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs);
        isLayoutRtl = isLayoutRtlByReflect();
    }

    public RelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFromAttributes(context, attrs);
        isLayoutRtl = isLayoutRtlByReflect();
    }

    private void initFromAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, ReflectUtils.getInternalIntArray("com.android.internal.R$styleable", LOG_TAG));
        this.mIgnoreGravity = a.getResourceId(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_ignoreGravity"), -1);
        this.mGravity = a.getInt(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_gravity"), this.mGravity);
        a.recycle();
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public void setIgnoreGravity(int viewId) {
        this.mIgnoreGravity = viewId;
    }

    public int getGravity() {
        return this.mGravity;
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

    public int getBaseline() {
        return this.mBaselineView != null ? this.mBaselineView.getBaseline() : super.getBaseline();
    }

    public void requestLayout() {
        super.requestLayout();
        this.mDirtyHierarchy = true;
    }

    private void sortChildren() {
        int count = getChildCount();
        if (this.mSortedVerticalChildren.length != count) {
            this.mSortedVerticalChildren = new View[count];
        }
        if (this.mSortedHorizontalChildren.length != count) {
            this.mSortedHorizontalChildren = new View[count];
        }
        DependencyGraph graph = this.mGraph;
        graph.clear();
        for (int i = 0; i < count; i++) {
            graph.add(getChildAt(i));
        }
        graph.getSortedViews(this.mSortedVerticalChildren, RULES_VERTICAL);
        graph.getSortedViews(this.mSortedHorizontalChildren, RULES_HORIZONTAL);
    }

    /* access modifiers changed from: protected */
    @TargetApi(17)
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            sortChildren();
        }
        int myWidth = -1;
        int myHeight = -1;
        int width = 0;
        int height = 0;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != 0) {
            myWidth = widthSize;
        }
        if (heightMode != 0) {
            myHeight = heightSize;
        }
        if (widthMode == 1073741824) {
            width = myWidth;
        }
        if (heightMode == 1073741824) {
            height = myHeight;
        }
        this.mHasBaselineAlignedChild = false;
        View ignore = null;
        int gravity = this.mGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        boolean horizontalGravity = (gravity == 8388611 || gravity == 0) ? false : true;
        int gravity2 = this.mGravity & 112;
        boolean verticalGravity = (gravity2 == 48 || gravity2 == 0) ? false : true;
        int left = Integer.MAX_VALUE;
        int top = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int bottom = Integer.MIN_VALUE;
        boolean offsetHorizontalAxis = false;
        boolean offsetVerticalAxis = false;
        if ((horizontalGravity || verticalGravity) && this.mIgnoreGravity != -1) {
            ignore = findViewById(this.mIgnoreGravity);
        }
        boolean isWrapContentWidth = widthMode != 1073741824;
        boolean isWrapContentHeight = heightMode != 1073741824;
        View[] views = this.mSortedHorizontalChildren;
        int count = views.length;
        for (int i = 0; i < count; i++) {
            View child = views[i];
            if (child.getVisibility() != 8) {
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                applyHorizontalSizeRules(params, myWidth);
                measureChildHorizontal(child, params, myWidth, myHeight);
                if (positionChildHorizontal(child, params, myWidth, isWrapContentWidth)) {
                    offsetHorizontalAxis = true;
                }
            }
        }
        View[] views2 = this.mSortedVerticalChildren;
        int count2 = views2.length;
        for (int i2 = 0; i2 < count2; i2++) {
            View child2 = views2[i2];
            if (child2.getVisibility() != 8) {
                LayoutParams params2 = (LayoutParams) child2.getLayoutParams();
                applyVerticalSizeRules(params2, myHeight);
                measureChild(child2, params2, myWidth, myHeight);
                if (positionChildVertical(child2, params2, myHeight, isWrapContentHeight)) {
                    offsetVerticalAxis = true;
                }
                if (isWrapContentWidth) {
                    width = Math.max(width, params2.mRight);
                }
                if (isWrapContentHeight) {
                    height = Math.max(height, params2.mBottom);
                }
                if (child2 != ignore || verticalGravity) {
                    left = Math.min(left, params2.mLeft - params2.leftMargin);
                    top = Math.min(top, params2.mTop - params2.topMargin);
                }
                if (child2 != ignore || horizontalGravity) {
                    right = Math.max(right, params2.mRight + params2.rightMargin);
                    bottom = Math.max(bottom, params2.mBottom + params2.bottomMargin);
                }
            }
        }
        if (this.mHasBaselineAlignedChild) {
            for (int i3 = 0; i3 < count2; i3++) {
                View child3 = getChildAt(i3);
                if (child3.getVisibility() != 8) {
                    LayoutParams params3 = (LayoutParams) child3.getLayoutParams();
                    alignBaseline(child3, params3);
                    if (child3 != ignore || verticalGravity) {
                        left = Math.min(left, params3.mLeft - params3.leftMargin);
                        top = Math.min(top, params3.mTop - params3.topMargin);
                    }
                    if (child3 != ignore || horizontalGravity) {
                        right = Math.max(right, params3.mRight + params3.rightMargin);
                        bottom = Math.max(bottom, params3.mBottom + params3.bottomMargin);
                    }
                }
            }
        }
        int layoutDirection = getLayoutDirection();
        if (isWrapContentWidth) {
            int width2 = width + getPaddingRight();
            if (getLayoutParams().width >= 0) {
                width2 = Math.max(width2, getLayoutParams().width);
            }
            width = resolveSize(Math.max(width2, getSuggestedMinimumWidth()), widthMeasureSpec);
            if (offsetHorizontalAxis) {
                for (int i4 = 0; i4 < count2; i4++) {
                    View child4 = getChildAt(i4);
                    if (child4.getVisibility() != 8) {
                        LayoutParams params4 = (LayoutParams) child4.getLayoutParams();
                        int[] rules = params4.getRules(layoutDirection);
                        if (rules[13] != 0 || rules[14] != 0) {
                            centerHorizontal(child4, params4, width);
                        } else if (rules[11] != 0) {
                            int childWidth = child4.getMeasuredWidth();
                            int unused = params4.mLeft = (width - getPaddingRight()) - childWidth;
                            int unused2 = params4.mRight = params4.mLeft + childWidth;
                        }
                    }
                }
            }
        }
        if (isWrapContentHeight) {
            int height2 = height + getPaddingBottom();
            if (getLayoutParams().height >= 0) {
                height2 = Math.max(height2, getLayoutParams().height);
            }
            height = resolveSize(Math.max(height2, getSuggestedMinimumHeight()), heightMeasureSpec);
            if (offsetVerticalAxis) {
                for (int i5 = 0; i5 < count2; i5++) {
                    View child5 = getChildAt(i5);
                    if (child5.getVisibility() != 8) {
                        LayoutParams params5 = (LayoutParams) child5.getLayoutParams();
                        int[] rules2 = params5.getRules(layoutDirection);
                        if (rules2[13] != 0 || rules2[15] != 0) {
                            centerVertical(child5, params5, height);
                        } else if (rules2[12] != 0) {
                            int childHeight = child5.getMeasuredHeight();
                            int unused3 = params5.mTop = (height - getPaddingBottom()) - childHeight;
                            int unused4 = params5.mBottom = params5.mTop + childHeight;
                        }
                    }
                }
            }
        }
        if (horizontalGravity || verticalGravity) {
            Rect selfBounds = this.mSelfBounds;
            selfBounds.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());
            Rect contentBounds = this.mContentBounds;
            Gravity.apply(this.mGravity, right - left, bottom - top, selfBounds, contentBounds, layoutDirection);
            int horizontalOffset = contentBounds.left - left;
            int verticalOffset = contentBounds.top - top;
            if (!(horizontalOffset == 0 && verticalOffset == 0)) {
                for (int i6 = 0; i6 < count2; i6++) {
                    View child6 = getChildAt(i6);
                    if (!(child6.getVisibility() == 8 || child6 == ignore)) {
                        LayoutParams params6 = (LayoutParams) child6.getLayoutParams();
                        if (horizontalGravity) {
                            int unused5 = params6.mLeft = params6.mLeft + horizontalOffset;
                            int unused6 = params6.mRight = params6.mRight + horizontalOffset;
                        }
                        if (verticalGravity) {
                            int unused7 = params6.mTop = params6.mTop + verticalOffset;
                            int unused8 = params6.mBottom = params6.mBottom + verticalOffset;
                        }
                    }
                }
            }
        }
        setMeasuredDimension(width, height);
    }

    private void alignBaseline(View child, LayoutParams params) {
        LayoutParams anchorParams;
        int[] rules = params.getRules(getLayoutDirection());
        int anchorBaseline = getRelatedViewBaseline(rules, 4);
        if (!(anchorBaseline == -1 || (anchorParams = getRelatedViewParams(rules, 4)) == null)) {
            int offset = anchorParams.mTop + anchorBaseline;
            int baseline = child.getBaseline();
            if (baseline != -1) {
                offset -= baseline;
            }
            int height = params.mBottom - params.mTop;
            int unused = params.mTop = offset;
            int unused2 = params.mBottom = params.mTop + height;
        }
        if (this.mBaselineView == null) {
            this.mBaselineView = child;
            return;
        }
        LayoutParams lp = (LayoutParams) this.mBaselineView.getLayoutParams();
        if (params.mTop < lp.mTop || (params.mTop == lp.mTop && params.mLeft < lp.mLeft)) {
            this.mBaselineView = child;
        }
    }

    private void measureChild(View child, LayoutParams params, int myWidth, int myHeight) {
        child.measure(getChildMeasureSpec(params.mLeft, params.mRight, params.width, params.leftMargin, params.rightMargin, getPaddingLeft(), getPaddingRight(), myWidth), getChildMeasureSpec(params.mTop, params.mBottom, params.height, params.topMargin, params.bottomMargin, getPaddingTop(), getPaddingBottom(), myHeight));
    }

    private void measureChildHorizontal(View child, LayoutParams params, int myWidth, int myHeight) {
        int childHeightMeasureSpec;
        int childWidthMeasureSpec = getChildMeasureSpec(params.mLeft, params.mRight, params.width, params.leftMargin, params.rightMargin, getPaddingLeft(), getPaddingRight(), myWidth);
        if (params.width == -1) {
            childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(myHeight, UCCore.VERIFY_POLICY_QUICK);
        } else {
            childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(myHeight, Integer.MIN_VALUE);
        }
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private int getChildMeasureSpec(int childStart, int childEnd, int childSize, int startMargin, int endMargin, int startPadding, int endPadding, int mySize) {
        int childSpecMode = 0;
        int childSpecSize = 0;
        int tempStart = childStart;
        int tempEnd = childEnd;
        if (tempStart < 0) {
            tempStart = startPadding + startMargin;
        }
        if (tempEnd < 0) {
            tempEnd = (mySize - endPadding) - endMargin;
        }
        int maxAvailable = tempEnd - tempStart;
        if (childStart >= 0 && childEnd >= 0) {
            childSpecMode = UCCore.VERIFY_POLICY_QUICK;
            childSpecSize = maxAvailable;
        } else if (childSize >= 0) {
            childSpecMode = UCCore.VERIFY_POLICY_QUICK;
            if (maxAvailable >= 0) {
                childSpecSize = Math.min(maxAvailable, childSize);
            } else {
                childSpecSize = childSize;
            }
        } else if (childSize == -1) {
            childSpecMode = UCCore.VERIFY_POLICY_QUICK;
            childSpecSize = maxAvailable;
        } else if (childSize == -2) {
            if (maxAvailable >= 0) {
                childSpecMode = Integer.MIN_VALUE;
                childSpecSize = maxAvailable;
            } else {
                childSpecMode = 0;
                childSpecSize = 0;
            }
        }
        return View.MeasureSpec.makeMeasureSpec(childSpecSize, childSpecMode);
    }

    private boolean positionChildHorizontal(View child, LayoutParams params, int myWidth, boolean wrapContent) {
        int[] rules = params.getRules(getLayoutDirection());
        if (params.mLeft < 0 && params.mRight >= 0) {
            int unused = params.mLeft = params.mRight - child.getMeasuredWidth();
        } else if (params.mLeft >= 0 && params.mRight < 0) {
            int unused2 = params.mRight = params.mLeft + child.getMeasuredWidth();
        } else if (params.mLeft < 0 && params.mRight < 0) {
            if (rules[13] == 0 && rules[14] == 0) {
                if (isLayoutRtlByReflect()) {
                    int unused3 = params.mRight = (myWidth - getPaddingRight()) - params.rightMargin;
                    int unused4 = params.mLeft = params.mRight - child.getMeasuredWidth();
                } else {
                    int unused5 = params.mLeft = getPaddingLeft() + params.leftMargin;
                    int unused6 = params.mRight = params.mLeft + child.getMeasuredWidth();
                }
            } else if (!wrapContent) {
                centerHorizontal(child, params, myWidth);
                return true;
            } else {
                int unused7 = params.mLeft = getPaddingLeft() + params.leftMargin;
                int unused8 = params.mRight = params.mLeft + child.getMeasuredWidth();
                return true;
            }
        }
        if (rules[21] != 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isLayoutRtlByReflect() {
        try {
            return ((Boolean) ReflectUtils.invokeMethod(this, "isLayoutRtl", new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean positionChildVertical(View child, LayoutParams params, int myHeight, boolean wrapContent) {
        int[] rules = params.getRules();
        if (params.mTop < 0 && params.mBottom >= 0) {
            int unused = params.mTop = params.mBottom - child.getMeasuredHeight();
        } else if (params.mTop >= 0 && params.mBottom < 0) {
            int unused2 = params.mBottom = params.mTop + child.getMeasuredHeight();
        } else if (params.mTop < 0 && params.mBottom < 0) {
            if (rules[13] == 0 && rules[15] == 0) {
                int unused3 = params.mTop = getPaddingTop() + params.topMargin;
                int unused4 = params.mBottom = params.mTop + child.getMeasuredHeight();
            } else if (!wrapContent) {
                centerVertical(child, params, myHeight);
                return true;
            } else {
                int unused5 = params.mTop = getPaddingTop() + params.topMargin;
                int unused6 = params.mBottom = params.mTop + child.getMeasuredHeight();
                return true;
            }
        }
        if (rules[12] != 0) {
            return true;
        }
        return false;
    }

    private void applyHorizontalSizeRules(LayoutParams childParams, int myWidth) {
        int[] rules = childParams.getRules(getLayoutDirection());
        int unused = childParams.mLeft = -1;
        int unused2 = childParams.mRight = -1;
        LayoutParams anchorParams = getRelatedViewParams(rules, 0);
        if (anchorParams != null) {
            int unused3 = childParams.mRight = anchorParams.mLeft - (anchorParams.leftMargin + childParams.rightMargin);
        } else if (childParams.alignWithParent && rules[0] != 0 && myWidth >= 0) {
            int unused4 = childParams.mRight = (myWidth - getPaddingRight()) - childParams.rightMargin;
        }
        LayoutParams anchorParams2 = getRelatedViewParams(rules, 1);
        if (anchorParams2 != null) {
            int unused5 = childParams.mLeft = anchorParams2.mRight + anchorParams2.rightMargin + childParams.leftMargin;
        } else if (childParams.alignWithParent && rules[1] != 0) {
            int unused6 = childParams.mLeft = getPaddingLeft() + childParams.leftMargin;
        }
        LayoutParams anchorParams3 = getRelatedViewParams(rules, 5);
        if (anchorParams3 != null) {
            int unused7 = childParams.mLeft = anchorParams3.mLeft + childParams.leftMargin;
        } else if (childParams.alignWithParent && rules[5] != 0) {
            int unused8 = childParams.mLeft = getPaddingLeft() + childParams.leftMargin;
        }
        LayoutParams anchorParams4 = getRelatedViewParams(rules, 7);
        if (anchorParams4 != null) {
            int unused9 = childParams.mRight = anchorParams4.mRight - childParams.rightMargin;
        } else if (childParams.alignWithParent && rules[7] != 0 && myWidth >= 0) {
            int unused10 = childParams.mRight = (myWidth - getPaddingRight()) - childParams.rightMargin;
        }
        if (rules[9] != 0) {
            int unused11 = childParams.mLeft = getPaddingLeft() + childParams.leftMargin;
        }
        if (rules[11] != 0 && myWidth >= 0) {
            int unused12 = childParams.mRight = (myWidth - getPaddingRight()) - childParams.rightMargin;
        }
    }

    private void applyVerticalSizeRules(LayoutParams childParams, int myHeight) {
        int[] rules = childParams.getRules();
        int unused = childParams.mTop = -1;
        int unused2 = childParams.mBottom = -1;
        LayoutParams anchorParams = getRelatedViewParams(rules, 2);
        if (anchorParams != null) {
            int unused3 = childParams.mBottom = anchorParams.mTop - (anchorParams.topMargin + childParams.bottomMargin);
        } else if (childParams.alignWithParent && rules[2] != 0 && myHeight >= 0) {
            int unused4 = childParams.mBottom = (myHeight - getPaddingBottom()) - childParams.bottomMargin;
        }
        LayoutParams anchorParams2 = getRelatedViewParams(rules, 3);
        if (anchorParams2 != null) {
            int unused5 = childParams.mTop = anchorParams2.mBottom + anchorParams2.bottomMargin + childParams.topMargin;
        } else if (childParams.alignWithParent && rules[3] != 0) {
            int unused6 = childParams.mTop = getPaddingTop() + childParams.topMargin;
        }
        LayoutParams anchorParams3 = getRelatedViewParams(rules, 6);
        if (anchorParams3 != null) {
            int unused7 = childParams.mTop = anchorParams3.mTop + childParams.topMargin;
        } else if (childParams.alignWithParent && rules[6] != 0) {
            int unused8 = childParams.mTop = getPaddingTop() + childParams.topMargin;
        }
        LayoutParams anchorParams4 = getRelatedViewParams(rules, 8);
        if (anchorParams4 != null) {
            int unused9 = childParams.mBottom = anchorParams4.mBottom - childParams.bottomMargin;
        } else if (childParams.alignWithParent && rules[8] != 0 && myHeight >= 0) {
            int unused10 = childParams.mBottom = (myHeight - getPaddingBottom()) - childParams.bottomMargin;
        }
        if (rules[10] != 0) {
            int unused11 = childParams.mTop = getPaddingTop() + childParams.topMargin;
        }
        if (rules[12] != 0 && myHeight >= 0) {
            int unused12 = childParams.mBottom = (myHeight - getPaddingBottom()) - childParams.bottomMargin;
        }
        if (rules[4] != 0) {
            this.mHasBaselineAlignedChild = true;
        }
    }

    private View getRelatedView(int[] rules, int relation) {
        int id = rules[relation];
        if (id == 0) {
            return null;
        }
        DependencyGraph.Node node = (DependencyGraph.Node) this.mGraph.mKeyNodes.get(id);
        if (node == null) {
            return null;
        }
        View v = node.view;
        while (v.getVisibility() == 8) {
            DependencyGraph.Node node2 = (DependencyGraph.Node) this.mGraph.mKeyNodes.get(((LayoutParams) v.getLayoutParams()).getRules()[relation]);
            if (node2 == null) {
                return null;
            }
            v = node2.view;
        }
        return v;
    }

    private LayoutParams getRelatedViewParams(int[] rules, int relation) {
        View v = getRelatedView(rules, relation);
        if (v == null || !(v.getLayoutParams() instanceof LayoutParams)) {
            return null;
        }
        return (LayoutParams) v.getLayoutParams();
    }

    private int getRelatedViewBaseline(int[] rules, int relation) {
        View v = getRelatedView(rules, relation);
        if (v != null) {
            return v.getBaseline();
        }
        return -1;
    }

    private void centerHorizontal(View child, LayoutParams params, int myWidth) {
        int childWidth = child.getMeasuredWidth();
        int left = (myWidth - childWidth) / 2;
        int unused = params.mLeft = left;
        int unused2 = params.mRight = left + childWidth;
    }

    private void centerVertical(View child, LayoutParams params, int myHeight) {
        int childHeight = child.getMeasuredHeight();
        int top = (myHeight - childHeight) / 2;
        int unused = params.mTop = top;
        int unused2 = params.mBottom = top + childHeight;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams st = (LayoutParams) child.getLayoutParams();
                child.layout(st.mLeft, st.mTop, st.mRight, st.mBottom);
            }
        }
        afterLayout(changed, l, count, r, b);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (this.mTopToBottomLeftToRightSet == null) {
            this.mTopToBottomLeftToRightSet = new TreeSet(new TopToBottomLeftToRightComparator());
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            this.mTopToBottomLeftToRightSet.add(getChildAt(i));
        }
        for (View view : this.mTopToBottomLeftToRightSet) {
            if (view.getVisibility() == 0 && view.dispatchPopulateAccessibilityEvent(event)) {
                this.mTopToBottomLeftToRightSet.clear();
                return true;
            }
        }
        this.mTopToBottomLeftToRightSet.clear();
        return false;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(RelativeLayout.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(RelativeLayout.class.getName());
    }

    private class TopToBottomLeftToRightComparator implements Comparator<View> {
        private TopToBottomLeftToRightComparator() {
        }

        public int compare(View first, View second) {
            int topDifference = first.getTop() - second.getTop();
            if (topDifference != 0) {
                return topDifference;
            }
            int leftDifference = first.getLeft() - second.getLeft();
            if (leftDifference != 0) {
                return leftDifference;
            }
            int heightDiference = first.getHeight() - second.getHeight();
            if (heightDiference != 0) {
                return heightDiference;
            }
            int widthDiference = first.getWidth() - second.getWidth();
            if (widthDiference != 0) {
                return widthDiference;
            }
            return 0;
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        @ViewDebug.ExportedProperty(category = "layout")
        public boolean alignWithParent;
        /* access modifiers changed from: private */
        public int mBottom;
        private int mEnd = Integer.MIN_VALUE;
        private int[] mInitialRules = new int[22];
        /* access modifiers changed from: private */
        public int mLeft;
        /* access modifiers changed from: private */
        public int mRight;
        /* access modifiers changed from: private */
        @ViewDebug.ExportedProperty(category = "layout", indexMapping = {@ViewDebug.IntToString(from = 2, to = "above"), @ViewDebug.IntToString(from = 4, to = "alignBaseline"), @ViewDebug.IntToString(from = 8, to = "alignBottom"), @ViewDebug.IntToString(from = 5, to = "alignLeft"), @ViewDebug.IntToString(from = 12, to = "alignParentBottom"), @ViewDebug.IntToString(from = 9, to = "alignParentLeft"), @ViewDebug.IntToString(from = 11, to = "alignParentRight"), @ViewDebug.IntToString(from = 10, to = "alignParentTop"), @ViewDebug.IntToString(from = 7, to = "alignRight"), @ViewDebug.IntToString(from = 6, to = "alignTop"), @ViewDebug.IntToString(from = 3, to = "below"), @ViewDebug.IntToString(from = 14, to = "centerHorizontal"), @ViewDebug.IntToString(from = 13, to = "center"), @ViewDebug.IntToString(from = 15, to = "centerVertical"), @ViewDebug.IntToString(from = 0, to = "leftOf"), @ViewDebug.IntToString(from = 1, to = "rightOf"), @ViewDebug.IntToString(from = 18, to = "alignStart"), @ViewDebug.IntToString(from = 19, to = "alignEnd"), @ViewDebug.IntToString(from = 20, to = "alignParentStart"), @ViewDebug.IntToString(from = 21, to = "alignParentEnd"), @ViewDebug.IntToString(from = 16, to = "startOf"), @ViewDebug.IntToString(from = 17, to = "endOf")}, mapping = {@ViewDebug.IntToString(from = -1, to = "true"), @ViewDebug.IntToString(from = 0, to = "false/NO_ID")}, resolveId = true)
        public int[] mRules = new int[22];
        private boolean mRulesChanged = false;
        private int mStart = Integer.MIN_VALUE;
        /* access modifiers changed from: private */
        public int mTop;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, ReflectUtils.getInternalIntArray("com.android.internal.R$styleable", "RelativeLayout_Layout"));
            int[] rules = this.mRules;
            int[] initialRules = this.mInitialRules;
            int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignWithParentIfMissing")) {
                    this.alignWithParent = a.getBoolean(attr, false);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_toLeftOf")) {
                    rules[0] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_toRightOf")) {
                    rules[1] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_above")) {
                    rules[2] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_below")) {
                    rules[3] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignBaseline")) {
                    rules[4] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignLeft")) {
                    rules[5] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignTop")) {
                    rules[6] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignRight")) {
                    rules[7] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignBottom")) {
                    rules[8] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignParentLeft")) {
                    rules[9] = a.getBoolean(attr, false) ? -1 : 0;
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignParentTop")) {
                    rules[10] = a.getBoolean(attr, false) ? -1 : 0;
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignParentRight")) {
                    rules[11] = a.getBoolean(attr, false) ? -1 : 0;
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignParentBottom")) {
                    rules[12] = a.getBoolean(attr, false) ? -1 : 0;
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_centerInParent")) {
                    rules[13] = a.getBoolean(attr, false) ? -1 : 0;
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_centerHorizontal")) {
                    rules[14] = a.getBoolean(attr, false) ? -1 : 0;
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_centerVertical")) {
                    rules[15] = a.getBoolean(attr, false) ? -1 : 0;
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_toStartOf")) {
                    rules[16] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_toEndOf")) {
                    rules[17] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignStart")) {
                    rules[18] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignEnd")) {
                    rules[19] = a.getResourceId(attr, 0);
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignParentStart")) {
                    rules[20] = a.getBoolean(attr, false) ? -1 : 0;
                } else if (attr == ReflectUtils.getInternalInt("com.android.internal.R$styleable", "RelativeLayout_Layout_layout_alignParentEnd")) {
                    rules[21] = a.getBoolean(attr, false) ? -1 : 0;
                }
            }
            for (int n = 0; n < 22; n++) {
                initialRules[n] = rules[n];
            }
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public String debug(String output) {
            return output + "ViewGroup.LayoutParams={ width=" + this.width + ", height=" + this.height + " }";
        }

        public void addRule(int verb) {
            this.mRules[verb] = -1;
            this.mInitialRules[verb] = -1;
            this.mRulesChanged = true;
        }

        public void addRule(int verb, int anchor) {
            this.mRules[verb] = anchor;
            this.mInitialRules[verb] = anchor;
            this.mRulesChanged = true;
        }

        public void removeRule(int verb) {
            this.mRules[verb] = 0;
            this.mInitialRules[verb] = 0;
            this.mRulesChanged = true;
        }

        private boolean hasRelativeRules() {
            return (this.mInitialRules[16] == 0 && this.mInitialRules[17] == 0 && this.mInitialRules[18] == 0 && this.mInitialRules[19] == 0 && this.mInitialRules[20] == 0 && this.mInitialRules[21] == 0) ? false : true;
        }

        private void resolveRules(int layoutDirection) {
            boolean isLayoutRtl;
            char c;
            char c2;
            char c3 = 9;
            char c4 = 5;
            char c5 = 1;
            if (layoutDirection == 1) {
                isLayoutRtl = true;
            } else {
                isLayoutRtl = false;
            }
            for (int n = 0; n < 22; n++) {
                this.mRules[n] = this.mInitialRules[n];
            }
            if (this.mRules[18] != 0) {
                this.mRules[isLayoutRtl ? (char) 7 : 5] = this.mRules[18];
            }
            if (this.mRules[19] != 0) {
                int[] iArr = this.mRules;
                if (!isLayoutRtl) {
                    c4 = 7;
                }
                iArr[c4] = this.mRules[19];
            }
            if (this.mRules[16] != 0) {
                int[] iArr2 = this.mRules;
                if (isLayoutRtl) {
                    c2 = 1;
                } else {
                    c2 = 0;
                }
                iArr2[c2] = this.mRules[16];
            }
            if (this.mRules[17] != 0) {
                int[] iArr3 = this.mRules;
                if (isLayoutRtl) {
                    c5 = 0;
                }
                iArr3[c5] = this.mRules[17];
            }
            if (this.mRules[20] != 0) {
                int[] iArr4 = this.mRules;
                if (isLayoutRtl) {
                    c = CsvReader.Letters.VERTICAL_TAB;
                } else {
                    c = 9;
                }
                iArr4[c] = this.mRules[20];
            }
            if (this.mRules[21] != 0) {
                int[] iArr5 = this.mRules;
                if (!isLayoutRtl) {
                    c3 = CsvReader.Letters.VERTICAL_TAB;
                }
                iArr5[c3] = this.mRules[21];
            }
            this.mRulesChanged = false;
        }

        public int[] getRules(int layoutDirection) {
            if (hasRelativeRules() && (this.mRulesChanged || layoutDirection != getLayoutDirection())) {
                resolveRules(layoutDirection);
                if (layoutDirection != getLayoutDirection()) {
                    setLayoutDirection(layoutDirection);
                }
            }
            return this.mRules;
        }

        public int[] getRules() {
            return this.mRules;
        }

        public void resolveLayoutDirection(int layoutDirection) {
            if (RelativeLayout.isLayoutRtl) {
                if (this.mStart != Integer.MIN_VALUE) {
                    this.mRight = this.mStart;
                }
                if (this.mEnd != Integer.MIN_VALUE) {
                    this.mLeft = this.mEnd;
                }
            } else {
                if (this.mStart != Integer.MIN_VALUE) {
                    this.mLeft = this.mStart;
                }
                if (this.mEnd != Integer.MIN_VALUE) {
                    this.mRight = this.mEnd;
                }
            }
            if (hasRelativeRules() && layoutDirection != getLayoutDirection()) {
                resolveRules(layoutDirection);
            }
            super.resolveLayoutDirection(layoutDirection);
        }
    }

    private static class DependencyGraph {
        /* access modifiers changed from: private */
        public SparseArray<Node> mKeyNodes;
        private ArrayList<Node> mNodes;
        private ArrayDeque<Node> mRoots;

        private DependencyGraph() {
            this.mNodes = new ArrayList<>();
            this.mKeyNodes = new SparseArray<>();
            this.mRoots = new ArrayDeque<>();
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            ArrayList<Node> nodes = this.mNodes;
            int count = nodes.size();
            for (int i = 0; i < count; i++) {
                nodes.get(i).release();
            }
            nodes.clear();
            this.mKeyNodes.clear();
            this.mRoots.clear();
        }

        /* access modifiers changed from: package-private */
        public void add(View view) {
            int id = view.getId();
            Node node = Node.acquire(view);
            if (id != -1) {
                this.mKeyNodes.put(id, node);
            }
            this.mNodes.add(node);
        }

        /* access modifiers changed from: package-private */
        public void getSortedViews(View[] sorted, int... rules) {
            ArrayDeque<Node> roots = findRoots(rules);
            int index = 0;
            while (true) {
                Node node = roots.pollLast();
                if (node == null) {
                    break;
                }
                View view = node.view;
                int key = view.getId();
                int index2 = index + 1;
                sorted[index] = view;
                for (Node dependent : node.dependents.keySet()) {
                    SparseArray<Node> dependencies = dependent.dependencies;
                    dependencies.remove(key);
                    if (dependencies.size() == 0) {
                        roots.add(dependent);
                    }
                }
                index = index2;
            }
            if (index < sorted.length) {
                throw new IllegalStateException("Circular dependencies cannot exist in RelativeLayout");
            }
        }

        private ArrayDeque<Node> findRoots(int[] rulesFilter) {
            Node dependency;
            SparseArray<Node> keyNodes = this.mKeyNodes;
            ArrayList<Node> nodes = this.mNodes;
            int count = nodes.size();
            for (int i = 0; i < count; i++) {
                Node node = nodes.get(i);
                node.dependents.clear();
                node.dependencies.clear();
            }
            for (int i2 = 0; i2 < count; i2++) {
                Node node2 = nodes.get(i2);
                int[] rules = ((LayoutParams) node2.view.getLayoutParams()).mRules;
                for (int i3 : rulesFilter) {
                    int rule = rules[i3];
                    if (!(rule <= 0 || (dependency = keyNodes.get(rule)) == null || dependency == node2)) {
                        dependency.dependents.put(node2, this);
                        node2.dependencies.put(rule, dependency);
                    }
                }
            }
            ArrayDeque<Node> roots = this.mRoots;
            roots.clear();
            for (int i4 = 0; i4 < count; i4++) {
                Node node3 = nodes.get(i4);
                if (node3.dependencies.size() == 0) {
                    roots.addLast(node3);
                }
            }
            return roots;
        }

        /* access modifiers changed from: package-private */
        public void log(Resources resources, int... rules) {
            Iterator<Node> it = findRoots(rules).iterator();
            while (it.hasNext()) {
                printNode(resources, it.next());
            }
        }

        static void printViewId(Resources resources, View view) {
            if (view.getId() != -1) {
                ZpLogger.d(RelativeLayout.LOG_TAG, resources.getResourceEntryName(view.getId()));
            } else {
                ZpLogger.d(RelativeLayout.LOG_TAG, "NO_ID");
            }
        }

        private static void appendViewId(Resources resources, Node node, StringBuilder buffer) {
            if (node.view.getId() != -1) {
                buffer.append(resources.getResourceEntryName(node.view.getId()));
            } else {
                buffer.append("NO_ID");
            }
        }

        private static void printNode(Resources resources, Node node) {
            if (node.dependents.size() == 0) {
                printViewId(resources, node.view);
                return;
            }
            for (Node dependent : node.dependents.keySet()) {
                StringBuilder buffer = new StringBuilder();
                appendViewId(resources, node, buffer);
                printdependents(resources, dependent, buffer);
            }
        }

        private static void printdependents(Resources resources, Node node, StringBuilder buffer) {
            buffer.append(" -> ");
            appendViewId(resources, node, buffer);
            if (node.dependents.size() == 0) {
                ZpLogger.d(RelativeLayout.LOG_TAG, buffer.toString());
                return;
            }
            for (Node dependent : node.dependents.keySet()) {
                printdependents(resources, dependent, new StringBuilder(buffer));
            }
        }

        static class Node implements Poolable<Node> {
            private static final int POOL_LIMIT = 100;
            private static final Pool<Node> sPool = Pools.synchronizedPool(Pools.finitePool(new PoolableManager<Node>() {
                public Node newInstance() {
                    return new Node();
                }

                public void onAcquired(Node element) {
                }

                public void onReleased(Node element) {
                }
            }, 100));
            final SparseArray<Node> dependencies = new SparseArray<>();
            final HashMap<Node, DependencyGraph> dependents = new HashMap<>();
            private boolean mIsPooled;
            private Node mNext;
            View view;

            Node() {
            }

            public void setNextPoolable(Node element) {
                this.mNext = element;
            }

            public Node getNextPoolable() {
                return this.mNext;
            }

            public boolean isPooled() {
                return this.mIsPooled;
            }

            public void setPooled(boolean isPooled) {
                this.mIsPooled = isPooled;
            }

            static Node acquire(View view2) {
                Node node = sPool.acquire();
                node.view = view2;
                return node;
            }

            /* access modifiers changed from: package-private */
            public void release() {
                this.view = null;
                this.dependents.clear();
                this.dependencies.clear();
                sPool.release(this);
            }
        }
    }

    public Rect getClipFocusRect() {
        return null;
    }
}
