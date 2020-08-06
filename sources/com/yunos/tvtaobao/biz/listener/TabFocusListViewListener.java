package com.yunos.tvtaobao.biz.listener;

import android.view.View;
import com.yunos.tvtaobao.tvsdk.widget.AdapterView;

public interface TabFocusListViewListener {
    void onFocusChange(View view, boolean z);

    void onItemClick(AdapterView<?> adapterView, View view, int i, long j);

    void onItemSelected(View view, int i, boolean z, View view2);
}
