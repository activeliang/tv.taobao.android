package com.yunos.tvtaobao.biz.widget.newsku.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.yunos.tvtaobao.businessview.R;

public class WaitProgressDialog extends Dialog {
    public WaitProgressDialog(@NonNull Context context) {
        super(context, R.style.DialogNoTitleStyle);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
    }
}
