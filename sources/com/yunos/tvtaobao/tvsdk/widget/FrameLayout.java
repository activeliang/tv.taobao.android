package com.yunos.tvtaobao.tvsdk.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
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
import java.util.ArrayList;

@RemoteViews.RemoteView
@TargetApi(17)
public class FrameLayout extends ViewGroup {
    private static final int DEFAULT_CHILD_GRAVITY = 8388659;
    @ViewDebug.ExportedProperty(category = "drawing")
    private Drawable mForeground;
    boolean mForegroundBoundsChanged;
    @ViewDebug.ExportedProperty(category = "drawing")
    private int mForegroundGravity;
    @ViewDebug.ExportedProperty(category = "drawing")
    protected boolean mForegroundInPadding;
    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingBottom;
    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingLeft;
    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingRight;
    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingTop;
    private final ArrayList<View> mMatchParentChildren;
    @ViewDebug.ExportedProperty(category = "measurement")
    boolean mMeasureAllChildren;
    private final Rect mOverlayBounds;
    private final Rect mSelfBounds;

    public FrameLayout(Context context) {
        super(context);
        this.mMeasureAllChildren = false;
        this.mForegroundPaddingLeft = 0;
        this.mForegroundPaddingTop = 0;
        this.mForegroundPaddingRight = 0;
        this.mForegroundPaddingBottom = 0;
        this.mSelfBounds = new Rect();
        this.mOverlayBounds = new Rect();
        this.mForegroundGravity = 119;
        this.mForegroundInPadding = true;
        this.mForegroundBoundsChanged = false;
        this.mMatchParentChildren = new ArrayList<>(1);
    }

    public FrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMeasureAllChildren = false;
        this.mForegroundPaddingLeft = 0;
        this.mForegroundPaddingTop = 0;
        this.mForegroundPaddingRight = 0;
        this.mForegroundPaddingBottom = 0;
        this.mSelfBounds = new Rect();
        this.mOverlayBounds = new Rect();
        this.mForegroundGravity = 119;
        this.mForegroundInPadding = true;
        this.mForegroundBoundsChanged = false;
        this.mMatchParentChildren = new ArrayList<>(1);
        TypedArray a = context.obtainStyledAttributes(attrs, ReflectUtils.getInternalIntArray("com.android.internal.R$styleable", "FrameLayout"), defStyle, 0);
        this.mForegroundGravity = a.getInt(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "FrameLayout_foregroundGravity"), this.mForegroundGravity);
        Drawable d = a.getDrawable(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "FrameLayout_foreground"));
        if (d != null) {
            setForeground(d);
        }
        if (a.getBoolean(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "FrameLayout_measureAllChildren"), false)) {
            setMeasureAllChildren(true);
        }
        this.mForegroundInPadding = a.getBoolean(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "FrameLayout_foregroundInsidePadding"), true);
        a.recycle();
    }

    public int getForegroundGravity() {
        return this.mForegroundGravity;
    }

    public void setForegroundGravity(int foregroundGravity) {
        if (this.mForegroundGravity != foregroundGravity) {
            if ((8388615 & foregroundGravity) == 0) {
                foregroundGravity |= GravityCompat.START;
            }
            if ((foregroundGravity & 112) == 0) {
                foregroundGravity |= 48;
            }
            this.mForegroundGravity = foregroundGravity;
            if (this.mForegroundGravity != 119 || this.mForeground == null) {
                this.mForegroundPaddingLeft = 0;
                this.mForegroundPaddingTop = 0;
                this.mForegroundPaddingRight = 0;
                this.mForegroundPaddingBottom = 0;
            } else {
                Rect padding = new Rect();
                if (this.mForeground.getPadding(padding)) {
                    this.mForegroundPaddingLeft = padding.left;
                    this.mForegroundPaddingTop = padding.top;
                    this.mForegroundPaddingRight = padding.right;
                    this.mForegroundPaddingBottom = padding.bottom;
                }
            }
            requestLayout();
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mForeground;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mForeground != null) {
            this.mForeground.jumpToCurrentState();
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mForeground != null && this.mForeground.isStateful()) {
            this.mForeground.setState(getDrawableState());
        }
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public void setForeground(Drawable drawable) {
        if (this.mForeground != drawable) {
            if (this.mForeground != null) {
                this.mForeground.setCallback((Drawable.Callback) null);
                unscheduleDrawable(this.mForeground);
            }
            this.mForeground = drawable;
            this.mForegroundPaddingLeft = 0;
            this.mForegroundPaddingTop = 0;
            this.mForegroundPaddingRight = 0;
            this.mForegroundPaddingBottom = 0;
            if (drawable != null) {
                setWillNotDraw(false);
                drawable.setCallback(this);
                if (drawable.isStateful()) {
                    drawable.setState(getDrawableState());
                }
                if (this.mForegroundGravity == 119) {
                    Rect padding = new Rect();
                    if (drawable.getPadding(padding)) {
                        this.mForegroundPaddingLeft = padding.left;
                        this.mForegroundPaddingTop = padding.top;
                        this.mForegroundPaddingRight = padding.right;
                        this.mForegroundPaddingBottom = padding.bottom;
                    }
                }
            } else {
                setWillNotDraw(true);
            }
            requestLayout();
            invalidate();
        }
    }

    public Drawable getForeground() {
        return this.mForeground;
    }

    private int getPaddingLeftWithForeground() {
        if (this.mForegroundInPadding) {
            return Math.max(getPaddingLeft(), this.mForegroundPaddingLeft);
        }
        return getPaddingLeft() + this.mForegroundPaddingLeft;
    }

    private int getPaddingRightWithForeground() {
        if (this.mForegroundInPadding) {
            return Math.max(getPaddingRight(), this.mForegroundPaddingRight);
        }
        return getPaddingRight() + this.mForegroundPaddingRight;
    }

    private int getPaddingTopWithForeground() {
        if (this.mForegroundInPadding) {
            return Math.max(getPaddingTop(), this.mForegroundPaddingTop);
        }
        return getPaddingTop() + this.mForegroundPaddingTop;
    }

    private int getPaddingBottomWithForeground() {
        if (this.mForegroundInPadding) {
            return Math.max(getPaddingBottom(), this.mForegroundPaddingBottom);
        }
        return getPaddingBottom() + this.mForegroundPaddingBottom;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;
        int count = getChildCount();
        boolean measureMatchParentChildren = (View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && View.MeasureSpec.getMode(heightMeasureSpec) == 1073741824) ? false : true;
        this.mMatchParentChildren.clear();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (this.mMeasureAllChildren || child.getVisibility() != 8) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren && (lp.width == -1 || lp.height == -1)) {
                    this.mMatchParentChildren.add(child);
                }
            }
        }
        int maxWidth2 = maxWidth + getPaddingLeftWithForeground() + getPaddingRightWithForeground();
        int maxHeight2 = Math.max(maxHeight + getPaddingTopWithForeground() + getPaddingBottomWithForeground(), getSuggestedMinimumHeight());
        int maxWidth3 = Math.max(maxWidth2, getSuggestedMinimumWidth());
        Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight2 = Math.max(maxHeight2, drawable.getMinimumHeight());
            maxWidth3 = Math.max(maxWidth3, drawable.getMinimumWidth());
        }
        setMeasuredDimension(resolveSizeAndState(maxWidth3, widthMeasureSpec, childState), resolveSizeAndState(maxHeight2, heightMeasureSpec, childState << 16));
        int count2 = this.mMatchParentChildren.size();
        if (count2 > 1) {
            for (int i2 = 0; i2 < count2; i2++) {
                View child2 = this.mMatchParentChildren.get(i2);
                ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) child2.getLayoutParams();
                if (lp2.width == -1) {
                    childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec((((getMeasuredWidth() - getPaddingLeftWithForeground()) - getPaddingRightWithForeground()) - lp2.leftMargin) - lp2.rightMargin, UCCore.VERIFY_POLICY_QUICK);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeftWithForeground() + getPaddingRightWithForeground() + lp2.leftMargin + lp2.rightMargin, lp2.width);
                }
                if (lp2.height == -1) {
                    childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((((getMeasuredHeight() - getPaddingTopWithForeground()) - getPaddingBottomWithForeground()) - lp2.topMargin) - lp2.bottomMargin, UCCore.VERIFY_POLICY_QUICK);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTopWithForeground() + getPaddingBottomWithForeground() + lp2.topMargin + lp2.bottomMargin, lp2.height);
                }
                child2.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childLeft;
        int childTop;
        int count = getChildCount();
        int parentLeft = getPaddingLeftWithForeground();
        int parentRight = (right - left) - getPaddingRightWithForeground();
        int parentTop = getPaddingTopWithForeground();
        int parentBottom = (bottom - top) - getPaddingBottomWithForeground();
        this.mForegroundBoundsChanged = true;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int gravity = lp.gravity;
                if (gravity == -1) {
                    gravity = DEFAULT_CHILD_GRAVITY;
                }
                int verticalGravity = gravity & 112;
                switch (Gravity.getAbsoluteGravity(gravity, getLayoutDirection()) & 7) {
                    case 1:
                        childLeft = (((((parentRight - parentLeft) - width) / 2) + parentLeft) + lp.leftMargin) - lp.rightMargin;
                        break;
                    case 3:
                        childLeft = parentLeft + lp.leftMargin;
                        break;
                    case 5:
                        childLeft = (parentRight - width) - lp.rightMargin;
                        break;
                    default:
                        childLeft = parentLeft + lp.leftMargin;
                        break;
                }
                switch (verticalGravity) {
                    case 16:
                        childTop = (((((parentBottom - parentTop) - height) / 2) + parentTop) + lp.topMargin) - lp.bottomMargin;
                        break;
                    case 48:
                        childTop = parentTop + lp.topMargin;
                        break;
                    case 80:
                        childTop = (parentBottom - height) - lp.bottomMargin;
                        break;
                    default:
                        childTop = parentTop + lp.topMargin;
                        break;
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
        afterLayout(changed, left, top, right, bottom);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mForegroundBoundsChanged = true;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mForeground != null) {
            Drawable foreground = this.mForeground;
            if (this.mForegroundBoundsChanged) {
                this.mForegroundBoundsChanged = false;
                Rect selfBounds = this.mSelfBounds;
                Rect overlayBounds = this.mOverlayBounds;
                int w = getRight() - getLeft();
                int h = getBottom() - getTop();
                if (this.mForegroundInPadding) {
                    selfBounds.set(0, 0, w, h);
                } else {
                    selfBounds.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
                }
                Gravity.apply(this.mForegroundGravity, foreground.getIntrinsicWidth(), foreground.getIntrinsicHeight(), selfBounds, overlayBounds, getLayoutDirection());
                foreground.setBounds(overlayBounds);
            }
            foreground.draw(canvas);
        }
    }

    public boolean gatherTransparentRegion(Region region) {
        boolean opaque = super.gatherTransparentRegion(region);
        if (!(region == null || this.mForeground == null)) {
            applyDrawableToTransparentRegionByReflect(this.mForeground, region);
        }
        return opaque;
    }

    private void applyDrawableToTransparentRegionByReflect(Drawable foreground, Region region) {
        try {
            ReflectUtils.invokeMethod(this, "applyDrawableToTransparentRegion", new Object[]{foreground, region});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMeasureAllChildren(boolean measureAll) {
        this.mMeasureAllChildren = measureAll;
    }

    @Deprecated
    public boolean getConsiderGoneChildrenWhenMeasuring() {
        return getMeasureAllChildren();
    }

    public boolean getMeasureAllChildren() {
        return this.mMeasureAllChildren;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(FrameLayout.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(FrameLayout.class.getName());
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, ReflectUtils.getInternalIntArray("com.android.internal.R$styleable", "FrameLayout_Layout"));
            this.gravity = a.getInt(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "FrameLayout_Layout_layout_gravity"), -1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity2) {
            super(width, height);
            this.gravity = gravity2;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
    }

    public Rect getClipFocusRect() {
        return null;
    }
}
