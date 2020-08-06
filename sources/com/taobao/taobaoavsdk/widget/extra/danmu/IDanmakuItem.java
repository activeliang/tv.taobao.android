package com.taobao.taobaoavsdk.widget.extra.danmu;

import android.graphics.Canvas;

public interface IDanmakuItem {
    void addDrawingList(boolean z);

    void doDraw(Canvas canvas, boolean z);

    boolean drawing();

    int getCurrX();

    int getCurrY();

    int getHeight();

    float getSpeedFactor();

    int getWidth();

    boolean isOut();

    void release();

    void setSpeedFactor(float f);

    void setStartPosition(int i, int i2);

    void setTextColor(int i);

    void setTextSize(int i);

    long showTime();

    boolean willHit(IDanmakuItem iDanmakuItem);
}
