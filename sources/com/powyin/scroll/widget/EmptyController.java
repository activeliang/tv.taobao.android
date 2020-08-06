package com.powyin.scroll.widget;

import android.view.View;
import com.powyin.scroll.widget.ISwipe;

public interface EmptyController {
    int attachToViewIndex();

    View getView();

    void onSwipeStatue(ISwipe.FreshStatus freshStatus);
}
