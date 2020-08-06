package com.tvtaobao.voicesdk.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.R;

public class PromptDialog extends Activity {
    private String errorDesc;
    private TextView tvPrompt;
    private View view;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_prompt);
        this.tvPrompt = (TextView) findViewById(R.id.tv_prompt);
        this.errorDesc = getIntent().getStringExtra("errorDesc");
        setPrompt(this.errorDesc);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                PromptDialog.this.finish();
            }
        }, 3000);
    }

    public void setPrompt(String text) {
        if (!TextUtils.isEmpty(text) && this.tvPrompt != null) {
            this.tvPrompt.setText(text);
            ASRNotify.getInstance().playTTS(text);
        }
    }

    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
