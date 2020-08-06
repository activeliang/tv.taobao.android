package com.yunos.alitvcompliance;

import android.app.Application;
import com.yunos.alitvcompliance.types.RetCode;
import com.yunos.alitvcompliance.types.RetData;
import com.yunos.alitvcompliance.utils.UTHelper;

public class TVCompliance {
    private static final String TAG = "TVCompliance";
    private static boolean checkDEBUG = false;

    public static long getSDKVersionCode() {
        return 2016112901;
    }

    public static void init(Application application, boolean debug, UTHelper.IUTInitializer initializer, UTHelper.IUTCustomEventSender sender) {
    }

    public static RetData getComplianceDomain(String domain) {
        return new RetData(RetCode.Default, domain);
    }
}
