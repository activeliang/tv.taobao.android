package com.yunos.tvtaobao.tvsdk.widget.focus.listener;

import android.graphics.Rect;
import android.view.KeyEvent;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;

public interface FocusListener {
    boolean canDraw();

    Rect getClipFocusRect();

    FocusRectParams getFocusParams();

    ItemListener getItem();

    Params getParams();

    boolean isAnimate();

    boolean isFocusBackground();

    boolean isScrolling();

    void onFocusFinished();

    void onFocusStart();

    boolean onKeyDown(int i, KeyEvent keyEvent);

    boolean onKeyUp(int i, KeyEvent keyEvent);

    boolean preOnKeyDown(int i, KeyEvent keyEvent);
}
