package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.zhiping.dev.android.logger.ZpLogger;

public class TabFocusPositionManager extends GridViewFocusPositionManager {
    public static final int MATCH_PARENT = -1;
    private ColorDrawable mBackgroudColor;
    private boolean mCanScroll;
    private BitmapDrawable mLogoDrawable;
    private Rect mLogoDrawableRect;
    private ColorDrawable mTabColor;
    private Rect mTabColorRect;

    public TabFocusPositionManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onInitTabFocusPositionManager(context);
    }

    public TabFocusPositionManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInitTabFocusPositionManager(context);
    }

    public TabFocusPositionManager(Context context) {
        super(context);
        onInitTabFocusPositionManager(context);
    }

    private void onInitTabFocusPositionManager(Context context) {
        this.mBackgroudColor = null;
        this.mTabColor = null;
        this.mLogoDrawable = null;
        this.mCanScroll = true;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.mBackgroudColor != null) {
            this.mBackgroudColor.setBounds(0, 0, getWidth(), getHeight());
            this.mBackgroudColor.draw(canvas);
        }
        if (this.mTabColor != null) {
            if (this.mTabColorRect.bottom == -1) {
                this.mTabColorRect.bottom = getHeight();
            }
            this.mTabColor.setBounds(this.mTabColorRect);
            this.mTabColor.draw(canvas);
        }
        if (this.mLogoDrawable != null) {
            if (this.mLogoDrawableRect.bottom == -1) {
                this.mLogoDrawableRect.bottom = getHeight();
            }
            this.mLogoDrawable.setBounds(this.mLogoDrawableRect);
            this.mLogoDrawable.draw(canvas);
        }
        super.dispatchDraw(canvas);
    }

    public void setBackgroudColor(int color) {
        if (this.mBackgroudColor != null) {
            this.mBackgroudColor.setCallback((Drawable.Callback) null);
            this.mBackgroudColor = null;
        }
        this.mBackgroudColor = new ColorDrawable(color);
        invalidate();
    }

    public void onInitBackgroud() {
        this.mBackgroudColor = null;
        invalidate();
    }

    public void setTabColor(int color, Rect tabRect) {
        if (this.mTabColor != null) {
            this.mTabColor.setCallback((Drawable.Callback) null);
            this.mTabColor = null;
        }
        this.mTabColorRect = new Rect(tabRect);
        this.mTabColor = new ColorDrawable(color);
        invalidate();
    }

    public void setLeftLogo(Bitmap bm, Rect logoRect) {
        this.mLogoDrawableRect = new Rect(logoRect);
        if (bm != null && !bm.isRecycled()) {
            if (this.mLogoDrawable != null) {
                this.mLogoDrawable.setCallback((Drawable.Callback) null);
                this.mLogoDrawable = null;
            }
            this.mLogoDrawable = new BitmapDrawable(bm);
            invalidate();
        }
    }

    public void setCanScroll(boolean canScroll) {
        this.mCanScroll = canScroll;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ZpLogger.v(TAG, TAG + ".onInterceptTouchEvent.ev = " + ev + ".mCanScroll = " + this.mCanScroll);
        if (ev.getAction() != 2 || this.mCanScroll) {
            return super.onInterceptTouchEvent(ev);
        }
        return true;
    }
}
