package com.yunos.tvtaobao.tvsdk.widget.focus.listener;

import android.graphics.Canvas;

public interface FocusDrawStateListener {
    void drawAfterFocus(Canvas canvas);

    void drawBeforFocus(Canvas canvas);
}
