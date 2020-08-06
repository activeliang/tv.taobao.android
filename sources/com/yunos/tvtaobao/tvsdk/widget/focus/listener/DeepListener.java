package com.yunos.tvtaobao.tvsdk.widget.focus.listener;

import android.graphics.Rect;

public interface DeepListener extends FocusListener {
    boolean canDeep();

    boolean hasDeepFocus();

    void onFocusDeeped(boolean z, int i, Rect rect);

    void onItemClick();

    void onItemSelected(boolean z);
}
