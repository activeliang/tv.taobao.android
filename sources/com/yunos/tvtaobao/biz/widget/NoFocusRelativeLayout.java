package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;

public class NoFocusRelativeLayout extends RelativeLayout implements ItemListener {
    public NoFocusRelativeLayout(Context context) {
        super(context);
    }

    public NoFocusRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoFocusRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isScale() {
        return false;
    }

    public FocusRectParams getFocusParams() {
        return null;
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
        return false;
    }
}
