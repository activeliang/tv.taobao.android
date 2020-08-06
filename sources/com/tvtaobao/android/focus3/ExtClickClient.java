package com.tvtaobao.android.focus3;

import android.view.View;
import com.tvtaobao.android.focus3.FocusAssist;
import java.lang.ref.WeakReference;

class ExtClickClient {
    FocusAssist.ExtClickListener listener;
    WeakReference<View> v;

    public ExtClickClient(View view, FocusAssist.ExtClickListener listener2) {
        this.v = new WeakReference<>(view);
        this.listener = listener2;
    }
}
