package com.yunos.tvtaobao.tvsdk.widget.focus.listener;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface DrawListener {
    void draw(Canvas canvas);

    boolean isDynamicFocus();

    void setAlpha(float f);

    void setRadius(int i);

    void setRect(Rect rect);

    void setVisible(boolean z);

    void start();

    void stop();
}
