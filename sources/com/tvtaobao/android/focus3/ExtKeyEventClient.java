package com.tvtaobao.android.focus3;

import android.view.View;
import com.tvtaobao.android.focus3.FocusAssist;
import java.lang.ref.WeakReference;

class ExtKeyEventClient {
    FocusAssist.ExtKeyEventListener listener;
    WeakReference<View> v;

    public ExtKeyEventClient(View view, FocusAssist.ExtKeyEventListener preKeyEventDispatch) {
        this.v = new WeakReference<>(view);
        this.listener = preKeyEventDispatch;
    }
}
