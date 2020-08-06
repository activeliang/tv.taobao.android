package com.yunos.tvtaobao.splashscreen.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.yunos.tv.core.util.ActivityPathRecorder;
import com.yunos.tvtaobao.tvsdk.widget.FrameLayout;
import com.zhiping.dev.android.logger.ZpLogger;

public class LoadingActivity extends Activity {
    private static final String ACTION_KILL_SELF = (LoadingActivity.class.getSimpleName() + "_action_kill_self");
    public static final String KEY_LoadingActivity_TAG = (LoadingActivity.class.getSimpleName() + "_LoadingActivity_TAG");
    private static final String TAG = LoadingActivity.class.getSimpleName();
    public boolean needFinish = false;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ZpLogger.d(TAG, ".onCreate(" + savedInstanceState + ") and getIntent" + getIntent());
        super.onCreate(savedInstanceState);
        setContentView(generateView());
        transmitIntent(getIntent());
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.needFinish) {
            this.needFinish = false;
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public View generateView() {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1, -1);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(lp);
        frameLayout.setBackgroundColor(0);
        return frameLayout;
    }

    /* access modifiers changed from: protected */
    public void transmitIntent(Intent intent) {
        this.needFinish = true;
        Intent newIntent = new Intent();
        if (intent != null) {
            newIntent.setAction(intent.getAction());
            newIntent.setData(intent.getData());
            if (intent.getExtras() != null) {
                newIntent.putExtras(intent.getExtras());
            }
        }
        newIntent.putExtra(KEY_LoadingActivity_TAG, true);
        newIntent.setPackage(getPackageName());
        if (newIntent.getData() != null) {
            newIntent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_URI, newIntent.getData());
        }
        ComponentName startService = startService(newIntent);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
