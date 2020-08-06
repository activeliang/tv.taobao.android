package com.yunos.tvtaobao.splashscreen.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import com.yunos.tv.core.aqm.ActivityQueueManager;
import com.yunos.tvtaobao.biz.dialog.util.DialogUtil;
import com.yunos.tvtaobao.tvsdk.widget.FrameLayout;

public class Page404Activity extends Activity {
    private static final String KEY_MSG = (Page404Activity.class.getSimpleName() + "_key_msg");
    private static final String KEY_TYPE = (Page404Activity.class.getSimpleName() + "_key_type");
    DialogUtil dialogUtil = new DialogUtil(this);

    public enum Type {
        unknown,
        error,
        networkError;

        public static Type from(int val) {
            if (error.ordinal() == val) {
                return error;
            }
            if (networkError.ordinal() == val) {
                return networkError;
            }
            return unknown;
        }
    }

    public static void start(Context con, Type type) {
        Context context = ActivityQueueManager.getTop();
        if (context == null) {
            context = con;
        }
        if (context == null) {
            throw new RuntimeException("context can not be null !!!");
        }
        Intent intent1 = new Intent(context, Page404Activity.class);
        if (!(context instanceof Activity)) {
            intent1.addFlags(268435456);
        }
        intent1.putExtra(KEY_TYPE, type.ordinal());
        context.startActivity(intent1);
    }

    public static void start(Context con, Type type, String msg) {
        Context context = ActivityQueueManager.getTop();
        if (context == null) {
            context = con;
        }
        if (context == null) {
            throw new RuntimeException("context can not be null !!!");
        }
        Intent intent1 = new Intent(context, Page404Activity.class);
        if (!(context instanceof Activity)) {
            intent1.addFlags(268435456);
        }
        intent1.putExtra(KEY_TYPE, type.ordinal());
        intent1.putExtra(KEY_MSG, msg);
        context.startActivity(intent1);
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1, -1);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(lp);
        frameLayout.setBackgroundColor(0);
        setContentView(frameLayout);
        processIntent(getIntent());
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.dialogUtil.onDestroy();
    }

    private void processIntent(Intent intent) {
        if (intent != null) {
            Type type = Type.from(intent.getIntExtra(KEY_TYPE, -1));
            if (type == Type.error) {
                showErrorDialog(intent.getStringExtra(KEY_MSG));
                return;
            } else if (type == Type.networkError) {
                showNetworkErrorDialog(true);
                return;
            }
        }
        finish();
    }

    public void showErrorDialog(String msg) {
        this.dialogUtil.showErrorDialog(msg);
    }

    public void showNetworkErrorDialog(boolean isFinishActivity) {
        this.dialogUtil.showNetworkErrorDialog(isFinishActivity);
    }
}
