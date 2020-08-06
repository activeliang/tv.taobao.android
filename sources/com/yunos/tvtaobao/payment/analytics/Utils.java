package com.yunos.tvtaobao.payment.analytics;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.ju.track.constants.Constants;
import com.tvtao.user.dclib.ZPDevice;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTPageHitHelper;
import com.yunos.tv.core.common.CoreIntentKey;
import com.yunos.tvtaobao.payment.utils.CloudUUIDWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static String getControlName(String pageName, String controlName, Integer position, String... args) {
        String name = "";
        if (!TextUtils.isEmpty(pageName)) {
            name = pageName + "_";
        }
        if (!TextUtils.isEmpty(controlName)) {
            name = name + controlName;
        }
        if (position != null) {
            name = name + "_" + position;
        }
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                name = name + "_" + args[i];
            }
        }
        return name;
    }

    public static String getControlName(String controlName, Integer position, String... args) {
        return getControlName((String) null, controlName, position, args);
    }

    public static Map<String, String> getProperties(String from, String act, String appName) {
        Map<String, String> p = new HashMap<>();
        if (!TextUtils.isEmpty(from)) {
            p.put("from_channel", from);
        }
        String uuid = CloudUUIDWrapper.getCloudUUID();
        if (!TextUtils.isEmpty(uuid)) {
            p.put("uuid", uuid);
        }
        if (!TextUtils.isEmpty(act)) {
            p.put("from_act", act);
        }
        if (!TextUtils.isEmpty(appName)) {
            p.put(CoreIntentKey.URI_FROM_APP, appName);
        }
        return p;
    }

    public static Map<String, String> getProperties() {
        Map<String, String> p = new HashMap<>();
        String uuid = CloudUUIDWrapper.getCloudUUID();
        if (!TextUtils.isEmpty(uuid)) {
            p.put("uuid", uuid);
        }
        return p;
    }

    public static void utControlHit(String controlName, Map<String, String> p) {
        utControlHit((String) null, controlName, p);
    }

    public static void utControlHit(String pageName, String controlName, Map<String, String> p) {
        UTHitBuilders.UTControlHitBuilder lHitBuilder = null;
        if (TextUtils.isEmpty(pageName) && !TextUtils.isEmpty(utGetCurrentPage())) {
            lHitBuilder = new UTHitBuilders.UTControlHitBuilder(controlName);
        } else if (pageName != null) {
            if (!pageName.startsWith("Page_")) {
                pageName = "Page_" + pageName;
            }
            lHitBuilder = new UTHitBuilders.UTControlHitBuilder(pageName, controlName);
        }
        if (lHitBuilder != null) {
            lHitBuilder.setProperties(addCommonParams(p));
            UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder.build());
        }
    }

    public static void utCustomHit(String eventName, Map<String, String> p) {
        utCustomHit((String) null, eventName, p);
    }

    public static void utCustomHit(String pageName, String eventName, Map<String, String> p) {
        UTHitBuilders.UTCustomHitBuilder lHitBuilder = new UTHitBuilders.UTCustomHitBuilder(eventName);
        if (!TextUtils.isEmpty(pageName)) {
            if (!pageName.startsWith("Page_")) {
                pageName = "Page_" + pageName;
            }
            lHitBuilder.setEventPage(pageName);
        }
        lHitBuilder.setProperties(addCommonParams(p));
        UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder.build());
    }

    public static void utUpdatePageProperties(String pageName, Map<String, String> p) {
        if (!TextUtils.isEmpty(pageName)) {
            if (!pageName.startsWith("Page_")) {
                pageName = "Page_" + pageName;
            }
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(pageName, addCommonParams(p));
        }
    }

    public static Map<String, String> addCommonParams(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        if (!TextUtils.isEmpty(ZPDevice.getZpDid((Context) null))) {
            params.put("zpDid", ZPDevice.getZpDid((Context) null));
        }
        if (!TextUtils.isEmpty(ZPDevice.getAugurZpId((Context) null))) {
            params.put("augurZpUid", ZPDevice.getAugurZpId((Context) null));
        }
        return params;
    }

    public static String utGetCurrentPage() {
        return UTPageHitHelper.getInstance().getCurrentPageName();
    }

    public static void utPageAppear(String aPageObject, String aCustomPageName) {
        if (!TextUtils.isEmpty(aPageObject) && !TextUtils.isEmpty(aCustomPageName)) {
            if (!aPageObject.startsWith("Page_")) {
                aPageObject = "Page_" + aPageObject;
            }
            if (!aCustomPageName.startsWith("Page_")) {
                aCustomPageName = "Page_" + aCustomPageName;
            }
            UTAnalytics.getInstance().getDefaultTracker().pageAppear(aPageObject, aCustomPageName);
        }
    }

    public static void utPageDisAppear(String aPageObject) {
        if (!TextUtils.isEmpty(aPageObject)) {
            if (!aPageObject.startsWith("Page_")) {
                aPageObject = "Page_" + aPageObject;
            }
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(aPageObject);
        }
    }

    public static boolean isAppOnForeground(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = ((ActivityManager) context.getApplicationContext().getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == 100) {
                return true;
            }
        }
        return false;
    }

    public static void updateNextPageProperties(String spm_url) {
        if (!TextUtils.isEmpty(spm_url)) {
            Map<String, String> nextparam = new HashMap<>();
            nextparam.put(Constants.PARAM_OUTER_SPM_URL, spm_url);
            UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(nextparam);
        }
    }
}
