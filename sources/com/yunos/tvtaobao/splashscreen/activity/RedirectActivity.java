package com.yunos.tvtaobao.splashscreen.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import com.zhiping.dev.android.logger.ZpLogger;

public class RedirectActivity extends Activity {
    private static final String TAG = RedirectActivity.class.getSimpleName();
    private boolean pauseToFinish = false;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentValue = getIntent();
        if (intentValue != null) {
            if (processIntent(intentValue)) {
                this.pauseToFinish = true;
                startActivity(intentValue);
                return;
            }
            finish();
        }
    }

    private boolean processIntent(Intent intent) {
        if (intent == null) {
            return false;
        }
        ZpLogger.d(TAG, "intent:" + intent.getAction() + intent.getComponent() + intent.toString());
        intent.setPackage(getPackageName());
        if (intent.getData() != null) {
            intent.setComponent((ComponentName) null);
            return true;
        } else if (!"com.tvtaobao.action.StartApp".equals(intent.getAction())) {
            return false;
        } else {
            String uri = intent.getStringExtra(HuasuVideo.TAG_URI);
            ZpLogger.d(TAG, "uri is " + uri);
            if (TextUtils.isEmpty(uri)) {
                return false;
            }
            Uri actionUri = Uri.parse(uri);
            intent.setAction("android.intent.action.VIEW");
            intent.removeExtra(HuasuVideo.TAG_URI);
            intent.setComponent((ComponentName) null);
            intent.setData(actionUri);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.pauseToFinish) {
            finish();
        }
    }
}
