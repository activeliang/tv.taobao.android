package com.alibaba.analytics.core.config;

import android.content.Context;
import android.content.Intent;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.Logger;
import java.util.HashMap;
import java.util.Map;

public class UTConfigMgr {
    static final String INTENT_CONFIG_CHANGE = "com.alibaba.analytics.config.change";
    static final String INTENT_EXTRA_KEY = "key";
    static final String INTENT_EXTRA_VALUE = "value";
    private static final String TAG = "UTConfigMgr";
    private static Map<String, String> configMap = new HashMap();

    public static synchronized void postServerConfig(String key, String value) {
        synchronized (UTConfigMgr.class) {
            try {
                Context context = Variables.getInstance().getContext();
                if (context == null) {
                    context = ClientVariables.getInstance().getContext();
                }
                if (context != null) {
                    configMap.put(key, value);
                    String packageName = context.getPackageName();
                    Logger.d(TAG, "postServerConfig packageName", packageName, "key", key, INTENT_EXTRA_VALUE, value);
                    Intent intent = new Intent(INTENT_CONFIG_CHANGE);
                    intent.setPackage(packageName);
                    intent.putExtra("key", key);
                    intent.putExtra(INTENT_EXTRA_VALUE, value);
                    context.sendBroadcast(intent);
                }
            } catch (Throwable throwable) {
                Logger.e(TAG, throwable, new Object[0]);
            }
        }
        return;
    }

    public static synchronized void postAllServerConfig() {
        synchronized (UTConfigMgr.class) {
            for (Map.Entry<String, String> entry : configMap.entrySet()) {
                postServerConfig(entry.getKey(), entry.getValue());
            }
        }
    }
}
