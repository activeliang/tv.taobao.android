package com.yunos.tvtaobao.tvsdk.widget.focus.listener;

import android.graphics.Rect;
import android.view.View;

public interface PositionListener {
    void invalidate();

    void offsetDescendantRectToItsCoords(View view, Rect rect);

    void postInvalidate();

    void postInvalidateDelayed(long j);

    void reset();
}
