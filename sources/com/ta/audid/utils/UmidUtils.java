package com.ta.audid.utils;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.umid.IUMIDComponent;
import java.util.HashMap;
import java.util.Map;

public class UmidUtils {
    private static String mUmidToken = "";

    public static synchronized String getUmidToken(Context context) {
        String umidtoken;
        synchronized (UmidUtils.class) {
            if (!TextUtils.isEmpty(mUmidToken)) {
                umidtoken = mUmidToken;
            } else {
                try {
                    long time = System.currentTimeMillis();
                    IUMIDComponent umidComponent = SecurityGuardManager.getInstance(context).getUMIDComp();
                    int errorCode = umidComponent.initUMIDSync(0);
                    if (errorCode != 200) {
                        Map<String, String> pro = new HashMap<>();
                        pro.put("errorCode", "" + errorCode);
                        UtUtils.sendUtdidMonitorEvent("umid", pro);
                    }
                    UtdidLogger.d("", Long.valueOf(System.currentTimeMillis() - time));
                    umidtoken = umidComponent.getSecurityToken(0);
                    if (!TextUtils.isEmpty(umidtoken) && umidtoken.length() != 24) {
                        mUmidToken = umidtoken;
                        umidtoken = mUmidToken;
                    }
                } catch (Throwable t) {
                    UtdidLogger.d("", t);
                    umidtoken = "";
                }
            }
        }
        return umidtoken;
    }
}
