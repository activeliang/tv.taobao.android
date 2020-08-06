package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DrawListener;

public class StaticFocusDrawable implements DrawListener {
    public static final boolean DEBUG = false;
    float mAlpha = 1.0f;
    Drawable mDrawable;
    Rect mPadding = new Rect();
    Rect mRect = new Rect();
    boolean mVisible = true;

    public StaticFocusDrawable(Drawable d) {
        this.mDrawable = d;
        d.getPadding(this.mPadding);
    }

    public boolean isDynamicFocus() {
        return false;
    }

    public void setRect(Rect r) {
        this.mRect.set(r);
        this.mRect.top -= this.mPadding.top;
        this.mRect.left -= this.mPadding.left;
        this.mRect.right += this.mPadding.right;
        this.mRect.bottom += this.mPadding.bottom;
        this.mDrawable.setBounds(this.mRect);
    }

    public void setRadius(int r) {
    }

    public void start() {
    }

    public void stop() {
    }

    public void setVisible(boolean visible) {
        this.mVisible = visible;
    }

    public void draw(Canvas canvas) {
        if (this.mDrawable == null) {
            throw new IllegalArgumentException("StaticFocusDrawable: drawable is null");
        } else if (this.mVisible) {
            if (this.mDrawable != null) {
                this.mDrawable.setAlpha((int) (this.mAlpha * 255.0f));
            }
            this.mDrawable.draw(canvas);
        }
    }

    public void setAlpha(float alpha) {
        this.mAlpha = alpha;
    }
}
