package com.yunos.tvtaobao.tvsdk.widget.focus.listener;

import android.view.View;

public interface FocusStateListener {
    void onFocusFinished(View view, View view2);

    void onFocusStart(View view, View view2);
}
