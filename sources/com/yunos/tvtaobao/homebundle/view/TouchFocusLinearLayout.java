package com.yunos.tvtaobao.homebundle.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusLinearLayout;

public class TouchFocusLinearLayout extends FocusLinearLayout {
    public TouchFocusLinearLayout(Context context) {
        super(context);
    }

    public TouchFocusLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchFocusLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnFocusChanged() {
        onFocusChanged(true, 130, (Rect) null);
    }
}
