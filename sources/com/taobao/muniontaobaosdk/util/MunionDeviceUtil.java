package com.taobao.muniontaobaosdk.util;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Keep;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.taobao.utils.Global;
import com.ut.device.UTDevice;
import java.util.Locale;

@Keep
public class MunionDeviceUtil {
    private static Context appContext = Global.getApplication();
    private static String userAgent;
    private static String utdid;

    public static String getAccept(Context context, String str) {
        try {
            return new ClientTraceData(context, (Bundle) null).encode(str);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getUserAgent() {
        if (userAgent != null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        String str = Build.VERSION.RELEASE;
        if (str.length() > 0) {
            stringBuffer.append(str);
        } else {
            stringBuffer.append("1.0");
        }
        stringBuffer.append("; ");
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        if (language != null) {
            stringBuffer.append(language.toLowerCase());
            String country = locale.getCountry();
            if (country != null) {
                stringBuffer.append("-");
                stringBuffer.append(country.toLowerCase());
            }
        } else {
            stringBuffer.append("en");
        }
        String str2 = Build.MODEL;
        if (str2.length() > 0) {
            stringBuffer.append("; ");
            stringBuffer.append(str2);
        }
        String str3 = Build.ID;
        if (str3.length() > 0) {
            stringBuffer.append(" Build/");
            stringBuffer.append(str3);
        }
        userAgent = String.format("Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2 (TAOBAO-ANDROID-%s)", new Object[]{stringBuffer, "20130606"});
        return userAgent;
    }

    public static String getUtdid() {
        return getUtdid(appContext);
    }

    public static String getUtdid(Context context) {
        if (utdid != null || context == null) {
            return utdid;
        }
        utdid = UTDevice.getUtdid(context);
        return utdid;
    }

    public static void setAppContext(Context context) {
        appContext = context;
    }
}
