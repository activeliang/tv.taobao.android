package com.yunos.tvtaobao.biz.focus_impl;

import android.graphics.Canvas;

public abstract class ConsumerB implements FocusConsumer {
    public boolean onFocusEnter() {
        return false;
    }

    public boolean onFocusLeave() {
        return false;
    }

    public boolean onFocusClick() {
        return false;
    }

    public boolean onFocusDraw(Canvas canvas) {
        return false;
    }
}
