package com.yunos.tv.alitvasrsdk;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tv.alitvasr.IAliTVASRCallback;

public class CommonUtils {
    public static boolean isForeground(IAliTVASRCallback callback) {
        Boolean bool = null;
        if (callback == null) {
            return false;
        }
        try {
            Bundle bundle = callback.asrToClient(10001, (Bundle) null);
            StringBuilder append = new StringBuilder().append("bundle =");
            if (bundle != null) {
                bool = Boolean.valueOf(bundle.getBoolean(CommonData.KEY_IS_FOREGROUND));
            }
            Log.d("CommonUtils", append.append(bool).toString());
            if (bundle != null) {
                return bundle.getBoolean(CommonData.KEY_IS_FOREGROUND);
            }
            return false;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    public static Bundle isForegroundBundle(Context context) {
        if (context == null) {
            return null;
        }
        boolean isForeground = isForeground(context);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CommonData.KEY_IS_FOREGROUND, isForeground);
        return bundle;
    }

    public static boolean isForeground(Context context) {
        for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != 400) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
