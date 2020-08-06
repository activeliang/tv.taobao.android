package com.yunos.tvtaobao.splashscreen.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.yunos.ott.sdk.core.Environment;
import com.yunos.tvtaobao.splashscreen.R;

public class YunosOrDmodeActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Environment.getInstance().isYunos()) {
            setContentView(R.layout.yunos_bundle_activity);
        } else {
            setContentView(R.layout.dmode_bundle_activity);
        }
        findViewById(R.id.back_desk).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                YunosOrDmodeActivity.this.finish();
            }
        });
    }
}
