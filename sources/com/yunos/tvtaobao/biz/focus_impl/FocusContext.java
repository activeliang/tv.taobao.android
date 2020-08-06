package com.yunos.tvtaobao.biz.focus_impl;

import android.content.Context;
import android.view.Window;

public interface FocusContext {
    Context getAttachedContext();

    Window getAttachedWindow();

    FocusManager getFocusManager();
}
