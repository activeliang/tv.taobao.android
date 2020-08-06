package com.yunos.tvtaobao.tvsdk.widget.focus.listener;

import android.graphics.Canvas;
import android.graphics.Rect;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;

public interface ItemListener {
    void drawAfterFocus(Canvas canvas);

    void drawBeforeFocus(Canvas canvas);

    FocusRectParams getFocusParams();

    int getItemHeight();

    int getItemWidth();

    Rect getManualPadding();

    float getScaleX();

    float getScaleY();

    boolean isFinished();

    boolean isScale();

    void setScaleX(float f);

    void setScaleY(float f);
}
