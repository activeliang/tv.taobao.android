package com.ta.audid.filesync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.ta.audid.device.AppUtdid;
import com.ta.audid.store.UtdidContentUtil;
import com.ta.audid.utils.MD5Utils;
import com.ta.audid.utils.TaskExecutor;
import com.ta.audid.utils.UtdidLogger;
import org.json.JSONObject;

public class UtdidBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_UTDID = "com.action.utdid";
    public static final String INTENT_DATA = "data";
    public static final String INTENT_SIGN = "sign";

    public void onReceive(final Context context, final Intent intent) {
        UtdidLogger.d();
        String intentAction = "";
        try {
            intentAction = intent.getAction();
        } catch (Exception e) {
        }
        if (ACTION_UTDID.equalsIgnoreCase(intentAction)) {
            TaskExecutor.getInstance().submit(new Runnable() {
                public void run() {
                    String intentUtdid = "";
                    String intentPackage = "";
                    try {
                        String intentData = intent.getStringExtra("data");
                        String intentSign = intent.getStringExtra("sign");
                        String intentString = UtdidContentUtil.getDecodedContent(intentData);
                        JSONObject json = new JSONObject(intentString);
                        if (json.has("utdid")) {
                            intentUtdid = json.getString("utdid");
                        }
                        if (json.has("appkey")) {
                            String intentAppkey = json.getString("appkey");
                        }
                        if (json.has("appName")) {
                            intentPackage = json.getString("appName");
                        }
                        String currentAppUtdid = AppUtdid.getInstance().getCurrentAppUtdid();
                        UtdidLogger.sd("", "currentAppUtdid:" + currentAppUtdid + ",intentUtdid:" + intentUtdid);
                        if (!TextUtils.isEmpty(currentAppUtdid) && !TextUtils.isEmpty(intentUtdid) && !currentAppUtdid.equals(intentUtdid)) {
                            String currentAppName = context.getPackageName();
                            if (!TextUtils.isEmpty(currentAppName) && currentAppName.equals(intentPackage)) {
                                String appSign = MD5Utils.getHmacMd5Hex(intentString);
                                if (!TextUtils.isEmpty(appSign) && appSign.equalsIgnoreCase(intentSign)) {
                                    AppUtdid.getInstance().setAppUtdid(intentUtdid);
                                }
                            }
                        }
                    } catch (Exception e) {
                        UtdidLogger.d("", e);
                    }
                }
            });
        }
    }
}
