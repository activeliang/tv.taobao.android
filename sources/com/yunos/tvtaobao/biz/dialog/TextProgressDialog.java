package com.yunos.tvtaobao.biz.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.yunos.tvtaobao.businessview.R;

public class TextProgressDialog extends Dialog {
    private TextView text = ((TextView) findViewById(R.id.progressText));

    public void setText(CharSequence text2) {
        this.text.setText(text2);
    }

    public CharSequence getText() {
        return this.text.getText();
    }

    public TextProgressDialog(Context context) {
        super(context, R.style.ytbv_QR_Dialog);
        setContentView(R.layout.ytbv_text_progress);
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.gravity = 17;
        window.setAttributes(wl);
    }
}
