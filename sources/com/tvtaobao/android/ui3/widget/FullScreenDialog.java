package com.tvtaobao.android.ui3.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import com.tvtaobao.android.ui3.R;

public abstract class FullScreenDialog extends Dialog {
    public abstract View onCreateView();

    public FullScreenDialog(Context context) {
        super(context, R.style.ui3wares_dialogD);
        setContentView(onCreateView());
        getWindow().setLayout(-1, -1);
    }

    public void show() {
        show(false);
    }

    public void show(boolean focusable) {
        if (!focusable && getWindow() != null) {
            getWindow().addFlags(8);
        }
        super.show();
    }
}
