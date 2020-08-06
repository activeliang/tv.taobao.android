package com.yunos.tvtaobao.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.Window;
import com.yunos.tvtaobao.payment.R;
import com.yunos.tvtaobao.payment.analytics.Utils;

public class AlphaDialog extends Dialog {
    private Window window = null;

    public AlphaDialog(Context context, View view) {
        super(context);
        setContentView(view);
        windowDeploy();
    }

    public AlphaDialog(Context context, @LayoutRes int resId) {
        super(context);
        setContentView(resId);
        windowDeploy();
    }

    public void show() {
        super.show();
        Utils.utCustomHit("Expore_login_Disuse", Utils.getProperties());
    }

    public void windowDeploy() {
        this.window = getWindow();
        this.window.setWindowAnimations(R.style.AlphaAnimDialog);
        this.window.setBackgroundDrawableResource(R.color.transparent);
        this.window.setAttributes(this.window.getAttributes());
    }
}
