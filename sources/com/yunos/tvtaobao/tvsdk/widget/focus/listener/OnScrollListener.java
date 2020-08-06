package com.yunos.tvtaobao.tvsdk.widget.focus.listener;

import android.view.ViewGroup;

public interface OnScrollListener {
    public static final int SCROLL_STATE_FLING = 2;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

    void onScroll(ViewGroup viewGroup, int i, int i2, int i3);

    void onScrollStateChanged(ViewGroup viewGroup, int i);
}
