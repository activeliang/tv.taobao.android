package com.yunos.tv.blitz.usertrack;

import android.text.TextUtils;
import android.util.Log;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTPageHitHelper;
import com.yunos.tv.blitz.global.BzAppConfig;
import java.util.HashMap;
import java.util.Map;

public class UserTrackHelper {
    public static boolean AUTO_ADD_PAGE_ = true;

    public static Map<String, String> getProperties() {
        Map<String, String> p = new HashMap<>();
        String uuid = BzAppConfig.getInstance().getUuid();
        if (!TextUtils.isEmpty(uuid)) {
            p.put("uuid", uuid);
        }
        return p;
    }

    public static String[] getKvs(Map<String, String> p) {
        String[] kvs = new String[p.size()];
        int i = 0;
        for (Object key : p.keySet()) {
            kvs[i] = key.toString() + "=" + p.get(key).toString();
            i++;
        }
        return kvs;
    }

    public static void utControlHit(String controlName, Map<String, String> p) {
        utControlHit((String) null, controlName, p);
    }

    public static void utControlHit(String pageName, String controlName, Map<String, String> p) {
        UTHitBuilders.UTControlHitBuilder lHitBuilder;
        if (TextUtils.isEmpty(pageName)) {
            lHitBuilder = new UTHitBuilders.UTControlHitBuilder(controlName);
        } else {
            if (AUTO_ADD_PAGE_ && !pageName.startsWith("Page_")) {
                pageName = "Page_" + pageName;
            }
            lHitBuilder = new UTHitBuilders.UTControlHitBuilder(pageName, controlName);
        }
        lHitBuilder.setProperties(p);
        UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder.build());
    }

    public static void utUpdatePageProperties(String pageName, Map<String, String> p) {
        if (!TextUtils.isEmpty(pageName)) {
            if (AUTO_ADD_PAGE_ && !pageName.startsWith("Page_")) {
                pageName = "Page_" + pageName;
            }
            Log.i("UT", UTAnalytics.getInstance().toString());
            Log.i("UT", String.valueOf(UTAnalytics.getInstance().getDefaultTracker()));
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(pageName, p);
        }
    }

    public static String utGetCurrentPage() {
        return UTPageHitHelper.getInstance().getCurrentPageName();
    }

    public static void utCustomHit(String eventName, Map<String, String> p) {
        utCustomHit((String) null, eventName, p);
    }

    public static void utCustomHit(String pageName, String eventName, Map<String, String> p) {
        UTHitBuilders.UTCustomHitBuilder lHitBuilder = new UTHitBuilders.UTCustomHitBuilder(eventName);
        if (!TextUtils.isEmpty(pageName)) {
            if (AUTO_ADD_PAGE_ && !pageName.startsWith("Page_")) {
                pageName = "Page_" + pageName;
            }
            Log.i("UT", lHitBuilder.toString());
            lHitBuilder.setEventPage(pageName);
        }
        lHitBuilder.setProperties(p);
        UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder.build());
    }

    public static void utPageAppear(String aPageObject, String aCustomPageName) {
        if (!TextUtils.isEmpty(aPageObject) && !TextUtils.isEmpty(aCustomPageName)) {
            if (AUTO_ADD_PAGE_ && !aPageObject.startsWith("Page_")) {
                aPageObject = "Page_" + aPageObject;
            }
            if (AUTO_ADD_PAGE_ && !aCustomPageName.startsWith("Page_")) {
                aCustomPageName = "Page_" + aCustomPageName;
            }
            UTAnalytics.getInstance().getDefaultTracker().pageAppear(aPageObject, aCustomPageName);
        }
    }

    public static void utPageDisAppear(String aPageObject) {
        if (!TextUtils.isEmpty(aPageObject)) {
            if (AUTO_ADD_PAGE_ && !aPageObject.startsWith("Page_")) {
                aPageObject = "Page_" + aPageObject;
            }
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(aPageObject);
        }
    }
}
