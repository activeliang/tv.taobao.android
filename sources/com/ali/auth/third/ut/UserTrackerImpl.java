package com.ali.auth.third.ut;

import android.app.Application;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.core.sign.UTSecuritySDKRequestAuthentication;
import java.util.HashMap;
import java.util.Map;

public class UserTrackerImpl implements UserTrackerService {
    public UserTrackerImpl() {
        try {
            UTAnalytics.getInstance().setContext(KernelContext.context);
            UTAnalytics.getInstance().setAppApplicationInstance((Application) KernelContext.context.getApplicationContext());
            UTAnalytics.getInstance().setRequestAuthentication(new UTSecuritySDKRequestAuthentication(KernelContext.getAppKey(), ""));
            baichuanReport();
        } catch (Throwable th) {
        }
    }

    private void baichuanReport() {
        UTHitBuilders.UTCustomHitBuilder lHitBuilder = new UTHitBuilders.UTCustomHitBuilder("80001");
        Map<String, String> info = new HashMap<>();
        info.put("model", "auth");
        info.put("version", ConfigManager.SDK_VERSION.toString());
        lHitBuilder.setProperties(info);
        UTAnalytics.getInstance().getTracker("25").send(lHitBuilder.build());
    }

    public void send(String label, Map<String, String> prop) {
        UTHitBuilders.UTCustomHitBuilder lHitBuilder = new UTHitBuilders.UTCustomHitBuilder(label);
        Map<String, String> app = getAppInfo();
        if (prop != null) {
            app.putAll(prop);
        }
        lHitBuilder.setProperties(app);
        UTAnalytics.getInstance().getTracker("onesdk").send(lHitBuilder.build());
    }

    private static Map<String, String> getAppInfo() {
        return new HashMap<>();
    }
}
