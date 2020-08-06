package com.yunos.tvtaobao.biz.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;

public class WaitProgressDialog extends Dialog {
    public WaitProgressDialog(Context context) {
        super(context, R.style.ytbv_DialogNoTitleStyle);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ytbv_dialog_wait_progress);
        ZpLogger.d("WaitProgressDialog", "thread id:" + Thread.currentThread().getId());
    }
}
