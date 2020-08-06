package com.yunos.tvtaobao.biz.focus_impl;

import android.graphics.Canvas;

public interface FocusConsumer {
    boolean onFocusClick();

    boolean onFocusDraw(Canvas canvas);

    boolean onFocusEnter();

    boolean onFocusLeave();
}
