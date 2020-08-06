package com.yunos.tvtaobao.tvsdk.utils;

import android.text.TextUtils;
import com.zhiping.dev.android.logger.ZpLogger;

public class SystemProUtils {
    static int mFocusMode = 2;

    public static void setGlobalFocusMode(int mode) {
        mFocusMode = mode;
    }

    public static int getGlobalFocusMode() {
        return mFocusMode;
    }

    public static String getLicense() {
        try {
            String result = (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke((Object) null, new Object[]{"ro.yunos.domain.license", "falsenull"});
            if ("falsenull".equals(result)) {
                ZpLogger.w("System", "domain yingshi mtop is unknow!!!");
                return "1";
            } else if (!TextUtils.isEmpty(result)) {
                return result.trim();
            } else {
                return "1";
            }
        } catch (Exception e) {
            ZpLogger.w("System", "getLicense: error");
            return "1";
        }
    }
}
