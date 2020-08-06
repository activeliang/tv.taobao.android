package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;

public class FocusNoDeepRelativeLayout extends RelativeLayout implements ItemListener, FocusListener {
    private Rect mClipFocusRect = new Rect();
    private Params mParams = new Params(1.05f, 1.05f, 10, (Interpolator) null, true, 20, new AccelerateDecelerateFrameInterpolator());

    public FocusNoDeepRelativeLayout(Context context) {
        super(context);
    }

    public FocusNoDeepRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusNoDeepRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isScale() {
        return true;
    }

    private Rect getFocusedRect() {
        Rect r = new Rect();
        getFocusedRect(r);
        return r;
    }

    public FocusRectParams getFocusParams() {
        return new FocusRectParams(getFocusedRect(), 0.5f, 0.5f);
    }

    public int getItemWidth() {
        return getWidth();
    }

    public int getItemHeight() {
        return getHeight();
    }

    public Rect getManualPadding() {
        return null;
    }

    public void drawBeforeFocus(Canvas canvas) {
    }

    public void drawAfterFocus(Canvas canvas) {
    }

    public boolean isFinished() {
        return true;
    }

    public boolean canDraw() {
        return true;
    }

    public boolean isAnimate() {
        return true;
    }

    public ItemListener getItem() {
        return this;
    }

    public boolean isScrolling() {
        return false;
    }

    public Params getParams() {
        if (this.mParams != null) {
            return this.mParams;
        }
        throw new IllegalArgumentException("The params is null, you must call setScaleParams before it's running");
    }

    public boolean isFocusBackground() {
        return false;
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 23:
            case 66:
                return true;
            default:
                return false;
        }
    }

    public void onFocusStart() {
    }

    public void onFocusFinished() {
    }

    public Rect getClipFocusRect() {
        if (this.mClipFocusRect != null) {
            return this.mClipFocusRect;
        }
        return new Rect();
    }
}
