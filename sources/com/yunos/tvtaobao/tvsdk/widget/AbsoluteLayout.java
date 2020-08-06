package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import com.yunos.tvtaobao.tvsdk.utils.ReflectUtils;

@RemoteViews.RemoteView
@Deprecated
public class AbsoluteLayout extends ViewGroup {
    public AbsoluteLayout(Context context) {
        super(context);
    }

    public AbsoluteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsoluteLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth, lp.x + child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, lp.y + child.getMeasuredHeight());
            }
        }
        setMeasuredDimension(resolveSizeAndState(Math.max(maxWidth + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), widthMeasureSpec, 0), resolveSizeAndState(Math.max(maxHeight + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), heightMeasureSpec, 0));
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2, 0, 0);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childLeft = getPaddingLeft() + lp.x;
                int childTop = getPaddingTop() + lp.y;
                child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
            }
        }
        afterLayout(changed, l, count, r, b);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int x;
        public int y;

        public LayoutParams(int width, int height, int x2, int y2) {
            super(width, height);
            this.x = x2;
            this.y = y2;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, ReflectUtils.getInternalIntArray("com.android.internal.R$styleable", "AbsoluteLayout_Layout"));
            this.x = a.getDimensionPixelOffset(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "AbsoluteLayout_Layout_layout_x"), 0);
            this.y = a.getDimensionPixelOffset(ReflectUtils.getInternalInt("com.android.internal.R$styleable", "AbsoluteLayout_Layout_layout_y"), 0);
            a.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public String debug(String output) {
            return output + "Absolute.LayoutParams={width=" + this.width + ", height=" + this.height + " x=" + this.x + " y=" + this.y + "}";
        }
    }

    public Rect getClipFocusRect() {
        return null;
    }
}
