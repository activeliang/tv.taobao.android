package com.iflytek.xiri;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.taobao.detail.domain.tuwen.TuwenConstants;

public class Feedback {
    public static final int DIALOG = 3;
    public static final int ERROR = 4;
    public static final int EXECUTION = 1;
    public static final int SILENCE = 2;
    public static boolean isXiriTool = false;
    private Context mContext;
    private String mPackageName;
    private int mTalkKey;

    public Feedback(Context context) {
        this.mContext = context;
    }

    public void begin(Intent intent) {
        Log.d("APP", "begin ");
        this.mTalkKey = intent.getIntExtra("_token", -1234567);
        this.mPackageName = intent.getStringExtra("pkgname") == null ? "com.iflytek.xiri" : intent.getStringExtra("pkgname");
    }

    public void feedback(String text, int type) {
        if ("com.iflytek.xiri".equals(this.mPackageName)) {
            Intent intent = new Intent("com.iflytek.xiri2.app.CALL");
            intent.putExtra("_token", this.mTalkKey);
            intent.putExtra(TuwenConstants.MODEL_LIST_KEY.TEXT, text);
            intent.putExtra("type", type);
            intent.putExtra("_action", "FEEDBACK");
            intent.setPackage(this.mPackageName);
            this.mContext.startService(intent);
        } else {
            Intent intent2 = new Intent("tv.yuyin.app.CALL");
            intent2.putExtra("_token", this.mTalkKey);
            intent2.putExtra(TuwenConstants.MODEL_LIST_KEY.TEXT, text);
            intent2.putExtra("type", type);
            intent2.putExtra("_action", "FEEDBACK");
            intent2.setPackage(this.mPackageName);
            this.mContext.startService(intent2);
        }
        if (isXiriTool) {
            Intent intent22 = new Intent("com.iflytek.xiri2.xiritool.CALL");
            intent22.putExtra(TuwenConstants.MODEL_LIST_KEY.TEXT, text);
            intent22.putExtra("type", type);
            this.mContext.startService(intent22);
        }
    }
}
