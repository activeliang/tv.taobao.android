package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.uc.webview.export.internal.interfaces.IWaStat;
import java.util.HashMap;
import java.util.Map;

public class YouKuCrashReporter {
    public static void initYouKuCrashReporter(final Context context) {
        try {
            ReporterConfigure reporterConfigure = new ReporterConfigure();
            reporterConfigure.setEnableDumpSysLog(true);
            reporterConfigure.setEnableDumpRadioLog(true);
            reporterConfigure.setEnableDumpEventsLog(true);
            reporterConfigure.setEnableCatchANRException(true);
            reporterConfigure.setEnableANRMainThreadOnly(true);
            reporterConfigure.setEnableDumpAllThread(true);
            String baseAppVersion = Utils.getContextAppVersion(context);
            if (baseAppVersion == null) {
                baseAppVersion = "defaultVersion";
            }
            if (MotuCrashReporter.getInstance().enable(context, "23570660@android", "23570660", baseAppVersion, "channel", (String) null, reporterConfigure)) {
                LogUtil.d("crashreporter enable success");
            } else {
                LogUtil.d("crashreporter enable failure");
            }
            MotuCrashReporter.getInstance().setCrashCaughtListener((IUTCrashCaughtListener) new IUTCrashCaughtListener() {
                public Map<String, Object> onCrashCaught(Thread thread, Throwable throwable) {
                    Map<String, Object> metaData = new HashMap<>();
                    try {
                        Utils.getMTLMetaData(metaData, context);
                    } catch (Exception e) {
                    }
                    return metaData;
                }
            });
        } catch (Exception e) {
            LogUtil.e(IWaStat.KEY_ENABLE, e);
        }
    }
}
