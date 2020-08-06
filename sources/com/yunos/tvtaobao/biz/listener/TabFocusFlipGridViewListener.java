package com.yunos.tvtaobao.biz.listener;

import android.view.View;
import com.yunos.tvtaobao.tvsdk.widget.AdapterView;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusFlipGridView;

public interface TabFocusFlipGridViewListener {
    void onFinished(FocusFlipGridView focusFlipGridView, String str);

    void onFocusChange(FocusFlipGridView focusFlipGridView, String str, View view, boolean z);

    boolean onGetview(FocusFlipGridView focusFlipGridView, String str, int i, int i2);

    boolean onItemClick(FocusFlipGridView focusFlipGridView, String str, AdapterView<?> adapterView, View view, int i, long j);

    void onItemSelected(FocusFlipGridView focusFlipGridView, String str, View view, int i, boolean z, View view2);

    void onLayoutDone(FocusFlipGridView focusFlipGridView, String str, boolean z);

    void onStart(FocusFlipGridView focusFlipGridView, String str);
}
