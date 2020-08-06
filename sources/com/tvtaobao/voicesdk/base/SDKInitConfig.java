package com.tvtaobao.voicesdk.base;

import android.text.TextUtils;
import com.tvtaobao.voicesdk.utils.ActivityUtil;
import com.tvtaobao.voicesdk.utils.JSONUtil;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.SharePreferences;
import org.json.JSONException;
import org.json.JSONObject;

public class SDKInitConfig {
    private static final String TAG = "SDKInitConfig";
    private static String alrealdyInit;
    private static String appkey = null;
    private static String currentPage;
    private static String locaStr;
    private static boolean needRegister = false;
    private static boolean needShowUI = true;
    private static boolean needTVTaobaoSearch = true;
    private static boolean needTakeOutTips = true;
    private static String sdkPackageName;
    public static int sdkVersion = 0;

    public static void init(JSONObject data) {
        LogPrint.i(TAG, "SDKInitConfig.init " + data.toString());
        alrealdyInit = "true";
        appkey = JSONUtil.getString(data, "appkey");
        sdkVersion = JSONUtil.getInt(data, "sdkVersion");
        locaStr = JSONUtil.getString(data, "location");
        if (data.has("needShowUI")) {
            needShowUI = JSONUtil.getBoolean(data, "needShowUI", true);
        } else {
            needShowUI = JSONUtil.getBoolean(data, "needSearchUI", true);
        }
        needTakeOutTips = SharePreferences.getBoolean("sdkInit_needTakeOutTips", true).booleanValue();
        needTVTaobaoSearch = SharePreferences.getBoolean("sdkInit_needTVTaobaoSearch", true).booleanValue();
        needRegister = JSONUtil.getBoolean(data, "needRegister");
        sdkPackageName = JSONUtil.getString(data, CommonData.KEY_PACKAGE_NAME);
        SharePreferences.put("sdkInit_Str", data.toString());
    }

    public static String getSDKInitInfo() {
        return SharePreferences.getString("sdkInit_Str");
    }

    public static void init() {
        String sdkInit_Str = SharePreferences.getString("sdkInit_Str");
        if (!TextUtils.isEmpty(sdkInit_Str)) {
            init(sdkInit_Str);
        }
    }

    public static void init(String sdkInit_Str) {
        try {
            init(new JSONObject(sdkInit_Str));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getLocation() {
        if (TextUtils.isEmpty(locaStr)) {
            init();
        }
        return locaStr;
    }

    public static String getAppKey() {
        if (TextUtils.isEmpty(appkey)) {
            init();
        }
        return appkey;
    }

    public static void setAppkey(String appkey1) {
        appkey = appkey1;
    }

    public static String getCurrentPage() {
        if (CoreApplication.getApplication().getMyLifecycleHandler().isApplicationInForeground()) {
            return currentPage;
        }
        return null;
    }

    public static String getCurrentClassName() {
        if (!CoreApplication.getApplication().getMyLifecycleHandler().isApplicationInForeground() || ActivityUtil.getTopActivity() == null) {
            return null;
        }
        return ActivityUtil.getTopActivity().getClass().getName();
    }

    public static void setCurrentPage(String currentPage2) {
        currentPage = currentPage2;
    }

    public static boolean needShowUI() {
        if (TextUtils.isEmpty(alrealdyInit)) {
            init();
        }
        return needShowUI;
    }

    public static void setNeedShowUI(boolean needSearchUI) {
        needShowUI = needSearchUI;
    }

    public static void setNeedTakeOutTips(boolean needTakeOutTips2) {
        needTakeOutTips = needTakeOutTips2;
        SharePreferences.put("sdkInit_needTakeOutTips", needTakeOutTips2);
    }

    public static boolean needTakeOutTips() {
        if (TextUtils.isEmpty(alrealdyInit)) {
            init();
        }
        return needTakeOutTips;
    }

    public static void setNeedTVTaobaoSearch(boolean needTVTaobaoSearch2) {
        needTVTaobaoSearch = needTVTaobaoSearch2;
        SharePreferences.put("sdkInit_needTVTaobaoSearch", needTVTaobaoSearch2);
    }

    public static boolean needRegister() {
        if (TextUtils.isEmpty(alrealdyInit)) {
            init();
        }
        return needRegister;
    }

    public static boolean needTVTaobaoSearch() {
        if ("2016032917".equals(getAppKey())) {
            return false;
        }
        if (TextUtils.isEmpty(alrealdyInit)) {
            init();
        }
        return needTVTaobaoSearch;
    }

    public static String getSdkPackageName() {
        return sdkPackageName;
    }

    public static void setSdkPackageName(String sdkPackageName2) {
        sdkPackageName = sdkPackageName2;
    }
}
