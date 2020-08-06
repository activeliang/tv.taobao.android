package com.ali.user.open.ut;

import android.app.Activity;
import android.text.TextUtils;
import com.ali.user.open.core.service.UserTrackerService;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import java.util.Map;

public class UserTrackerImpl implements UserTrackerService {
    public void send(String label, Map<String, String> prop) {
        send("", label, prop);
    }

    public void send(String eventPage, String label, Map<String, String> props) {
        UTHitBuilders.UTCustomHitBuilder lHitBuilder = new UTHitBuilders.UTCustomHitBuilder(label);
        Map<String, String> app = getAppInfo();
        if (props != null) {
            app.putAll(props);
        }
        if (!TextUtils.isEmpty(eventPage)) {
            lHitBuilder.setEventPage(eventPage);
        }
        lHitBuilder.setProperties(app);
        UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder.build());
    }

    public void sendControl(String eventPage, String controlName, String arg2, Map<String, String> props) {
        UTHitBuilders.UTControlHitBuilder lHitBuilder = new UTHitBuilders.UTControlHitBuilder(eventPage, controlName);
        if (!TextUtils.isEmpty(arg2)) {
            lHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG2, arg2);
        }
        lHitBuilder.setProperties(props);
        UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder.build());
    }

    public void updatePageName(Activity activity, String pageName, Map<String, String> props) {
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(activity, pageName);
        if (props != null) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(activity, props);
        }
    }

    public void skipPage(Activity activity) {
        UTAnalytics.getInstance().getDefaultTracker().skipPage(activity);
    }

    public void pageDisAppear(Activity activity) {
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
    }

    private static Map<String, String> getAppInfo() {
        return new HashMap<>();
    }
}
