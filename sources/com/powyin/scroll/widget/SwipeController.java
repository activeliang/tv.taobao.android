package com.powyin.scroll.widget;

import android.view.View;

public interface SwipeController {

    public enum SwipeModel {
        SWIPE_BOTH,
        SWIPE_ONLY_REFRESH,
        SWIPE_ONLY_LOADINN,
        SWIPE_NONE
    }

    public enum SwipeStatus {
        SWIPE_HEAD_OVER,
        SWIPE_HEAD_TOAST,
        SWIPE_HEAD_LOADING,
        SWIPE_HEAD_COMPLETE_OK,
        SWIPE_HEAD_COMPLETE_ERROR,
        SWIPE_HEAD_COMPLETE_ERROR_NET,
        SWIPE_LOAD_LOADING,
        SWIPE_LOAD_NO_MORE,
        SWIPE_LOAD_ERROR
    }

    int getOverScrollHei();

    View getSwipeFoot();

    View getSwipeHead();

    void onSwipeStatue(SwipeStatus swipeStatus, int i, int i2);
}
