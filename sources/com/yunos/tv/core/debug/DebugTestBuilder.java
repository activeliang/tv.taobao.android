package com.yunos.tv.core.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.debug.DebugTestDialog;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.zhiping.dev.android.logger.ZpLogger;

public class DebugTestBuilder {
    private static final String TAG = "DebugTestBuilder";
    private static final long TIME_SPACE = 5000;
    /* access modifiers changed from: private */
    public String key;
    private Context mContext;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper());
    Runnable runnable = new Runnable() {
        public void run() {
            String unused = DebugTestBuilder.this.key = null;
            DebugTestBuilder.this.mHandler.postDelayed(this, DebugTestBuilder.TIME_SPACE);
        }
    };

    public DebugTestBuilder(Context context) {
        this.mContext = context;
        this.mHandler.postDelayed(this.runnable, TIME_SPACE);
    }

    public void onDestroy() {
        DeviceStateViewer.get(this.mContext).destroy();
        BaseTestDlg.onDestroy();
        this.mHandler.removeCallbacks(this.runnable);
        this.mHandler = null;
        this.mContext = null;
    }

    public void onKeyAction(int keyCode) {
        try {
            if (this.key == null) {
                this.key = "";
            }
            if (keyCode == 21) {
                this.key = "l";
            } else if (keyCode == 19) {
                this.key += "u";
            } else if (keyCode == 20) {
                this.key += "d";
            } else if (keyCode == 22) {
                this.key += UploadQueueMgr.MSGTYPE_REALTIME;
            }
            if (this.key != null && this.key.length() > 20) {
                this.key = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        String copyKey = "" + this.key;
        ZpLogger.d(TAG, copyKey);
        if (Config.isDebug()) {
            if ("lrrr".equals(copyKey)) {
                Intent intent = new Intent();
                intent.setClassName(this.mContext, BaseConfig.SWITCH_TO_TABPBAO_LIVE_ACTIVITY);
                this.mContext.startActivity(intent);
            }
            if ("ludr".equals(copyKey)) {
                new DebugTestDialog(this.mContext, DebugTestDialog.Action.SWITCH_CHANEL).show();
            }
            if ("lurr".equals(copyKey)) {
                new DebugTestDialog(this.mContext, DebugTestDialog.Action.SWITCH_ENV).show();
            }
            if ("luru".equals(copyKey)) {
                new DebugTestDialog(this.mContext, DebugTestDialog.Action.DisasterTolerance).show();
            }
            if ("luur".equals(copyKey)) {
                new DebugTestDialog(this.mContext, DebugTestDialog.Action.utNow).show();
            }
            if ("luuuuu".equals(copyKey)) {
                new DebugConsoleDialog(this.mContext).show();
            }
            if ("lrruudd".equals(copyKey)) {
                new DebugDegradeDialog(this.mContext).show();
            }
            if ("lrrdduu".equals(copyKey)) {
                DeviceStateViewer.get(CoreApplication.getApplication()).show();
            }
        }
    }
}
