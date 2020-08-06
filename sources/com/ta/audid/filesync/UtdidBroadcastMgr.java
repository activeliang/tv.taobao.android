package com.ta.audid.filesync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.ta.audid.Variables;
import com.ta.audid.store.UtdidContentUtil;
import com.ta.audid.utils.MD5Utils;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class UtdidBroadcastMgr {
    private static UtdidBroadcastMgr mInstance = null;
    private static BroadcastReceiver mReceiver;

    private UtdidBroadcastMgr() {
    }

    public static synchronized UtdidBroadcastMgr getInstance() {
        UtdidBroadcastMgr utdidBroadcastMgr;
        synchronized (UtdidBroadcastMgr.class) {
            if (mInstance == null) {
                mInstance = new UtdidBroadcastMgr();
            }
            utdidBroadcastMgr = mInstance;
        }
        return utdidBroadcastMgr;
    }

    public void startBroadCastReceiver(Context context) {
        if (mReceiver == null && context != null) {
            mReceiver = new UtdidBroadcastReceiver();
            context.registerReceiver(mReceiver, new IntentFilter(UtdidBroadcastReceiver.ACTION_UTDID));
        }
    }

    public void stopBroadCastReceiver(Context context) {
        if (mReceiver != null && context != null) {
            context.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    public void sendBroadCast(String utdid) {
        Context context = Variables.getInstance().getContext();
        if (context != null) {
            Intent intent = new Intent(UtdidBroadcastReceiver.ACTION_UTDID);
            Map<String, String> intentMap = new HashMap<>();
            intentMap.put("utdid", utdid);
            intentMap.put("appkey", Variables.getInstance().getAppkey());
            intentMap.put("appName", context.getPackageName());
            String data = new JSONObject(intentMap).toString();
            intent.putExtra("data", UtdidContentUtil.getEncodedContent(data));
            intent.putExtra("sign", MD5Utils.getHmacMd5Hex(data));
            context.sendBroadcast(intent);
        }
    }
}
