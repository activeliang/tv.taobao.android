package com.yunos.tv.core.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Pair;
import com.alibaba.motu.crashreporter.utrestapi.UTRestUrlWrapper;
import com.taobao.ju.track.constants.Constants;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTPageHitHelper;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.account.LoginHelperImpl;
import com.yunos.tv.core.common.CoreIntentKey;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.payment.utils.ChannelUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    private static String TAG = "Utils";
    private static String appkey = "appkey";
    static long lastClickTime = 0;
    static int spaceTime = 1000;
    private static String subkey = "subkey";

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

    public static String[] getKvs(Map<String, String> p) {
        String[] kvs = new String[p.size()];
        int i = 0;
        for (Object key : p.keySet()) {
            kvs[i] = key + "=" + p.get(key);
            i++;
        }
        return kvs;
    }

    public static Map<String, String> getProperties(String from, String act, String appName) {
        Map<String, String> p = getProperties();
        if (!TextUtils.isEmpty(from)) {
            p.put("from_channel", from);
        }
        if (!TextUtils.isEmpty(act)) {
            p.put("from_act", act);
        }
        if (!TextUtils.isEmpty(appName)) {
            p.put(CoreIntentKey.URI_FROM_APP, appName);
        }
        return p;
    }

    public static Map<String, String> getProperties(String... keyValPairs) {
        Map<String, String> p = getProperties();
        if (keyValPairs != null) {
            for (int i = 0; i < keyValPairs.length; i += 2) {
                String key = keyValPairs[i];
                String val = null;
                if (i + 1 < keyValPairs.length) {
                    val = keyValPairs[i + 1];
                }
                p.put(key, val);
            }
        }
        return p;
    }

    public static Map<String, String> getProperties() {
        Map<String, String> p = new HashMap<>();
        String uuid = CloudUUIDWrapper.getCloudUUID();
        if (!TextUtils.isEmpty(uuid)) {
            p.put("uuid", uuid);
        }
        p.put(UTRestUrlWrapper.FIELD_APPVERSION, getVersionNameAndCode(CoreApplication.getApplication()).first);
        p.put("model", getDeviceName());
        if (LoginHelperImpl.getJuLoginHelper().isLogin()) {
            p.put("user_id", LoginHelperImpl.getJuLoginHelper().getUserId());
            p.put("user_nick", LoginHelperImpl.getJuLoginHelper().getNick());
            p.put("is_sign", "1");
        } else {
            p.put("is_sign", "0");
        }
        p.put("appkey", Config.getChannel());
        if (p != null && ChannelUtils.isThisTag(ChannelUtils.HY)) {
            p.put(subkey, getSubkey());
            p.put(appkey, getAppkey());
        }
        return p;
    }

    public static void utControlSkip(Context context) {
        UTAnalytics.getInstance().getDefaultTracker().skipPage(context);
    }

    public static void utControlHit(String controlName, Map<String, String> p) {
        if (p != null && ChannelUtils.isThisTag(ChannelUtils.HY)) {
            p.put(subkey, getSubkey());
            p.put(appkey, getAppkey());
        }
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
            if (p != null && ChannelUtils.isThisTag(ChannelUtils.HY)) {
                p.put(subkey, getSubkey());
                p.put(appkey, getAppkey());
            }
            lHitBuilder.setProperties(com.yunos.tvtaobao.payment.analytics.Utils.addCommonParams(p));
            UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder.build());
        }
    }

    public static void utCustomHit(String eventName, Map<String, String> p) {
        if (p != null) {
            try {
                if (ChannelUtils.isThisTag(ChannelUtils.HY)) {
                    p.put(subkey, getSubkey());
                    p.put(appkey, getAppkey());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
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
        if (p != null && ChannelUtils.isThisTag(ChannelUtils.HY)) {
            p.put(subkey, getSubkey());
            p.put(appkey, getAppkey());
        }
        lHitBuilder.setProperties(com.yunos.tvtaobao.payment.analytics.Utils.addCommonParams(p));
        UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder.build());
    }

    public static void utExposeHit(String pageName, String eventName, Map<String, String> p) {
        UTHitBuilders.UTCustomHitBuilder lHitBuilder = new UTHitBuilders.UTCustomHitBuilder(eventName);
        lHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_EVENT_ID, "2201");
        if (!TextUtils.isEmpty(pageName)) {
            if (!pageName.startsWith("Page_")) {
                pageName = "Page_" + pageName;
            }
            lHitBuilder.setEventPage(pageName);
        }
        if (p != null && ChannelUtils.isThisTag(ChannelUtils.HY)) {
            p.put(subkey, getSubkey());
            p.put(appkey, getAppkey());
        }
        lHitBuilder.setProperties(com.yunos.tvtaobao.payment.analytics.Utils.addCommonParams(p));
        UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder.build());
    }

    public static void utUpdatePageProperties(String pageName, Map<String, String> p) {
        if (!TextUtils.isEmpty(pageName)) {
            if (!pageName.startsWith("Page_")) {
                pageName = "Page_" + pageName;
            }
            if (p != null && ChannelUtils.isThisTag(ChannelUtils.HY)) {
                p.put(subkey, getSubkey());
                p.put(appkey, getAppkey());
            }
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(pageName, com.yunos.tvtaobao.payment.analytics.Utils.addCommonParams(p));
        }
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
            try {
                UTAnalytics.getInstance().getDefaultTracker().pageAppear(aPageObject, aCustomPageName);
            } catch (Throwable e) {
                e.printStackTrace();
            }
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

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0042  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void startNetWorkSettingActivity(android.content.Context r7, java.lang.String r8) {
        /*
            r3 = 0
            int r5 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x002f }
            r6 = 10
            if (r5 <= r6) goto L_0x0014
            android.content.Intent r4 = new android.content.Intent     // Catch:{ Exception -> 0x002f }
            java.lang.String r5 = "android.settings.WIFI_SETTINGS"
            r4.<init>(r5)     // Catch:{ Exception -> 0x002f }
            r3 = r4
        L_0x0010:
            r7.startActivity(r3)     // Catch:{ Exception -> 0x002f }
        L_0x0013:
            return
        L_0x0014:
            android.content.Intent r4 = new android.content.Intent     // Catch:{ Exception -> 0x002f }
            r4.<init>()     // Catch:{ Exception -> 0x002f }
            android.content.ComponentName r0 = new android.content.ComponentName     // Catch:{ Exception -> 0x0050 }
            java.lang.String r5 = "com.android.settings"
            java.lang.String r6 = "com.android.settings.network"
            r0.<init>(r5, r6)     // Catch:{ Exception -> 0x0050 }
            r4.setComponent(r0)     // Catch:{ Exception -> 0x0050 }
            java.lang.String r5 = "android.intent.action.VIEW"
            r4.setAction(r5)     // Catch:{ Exception -> 0x0050 }
            r3 = r4
            goto L_0x0010
        L_0x002f:
            r1 = move-exception
        L_0x0030:
            java.lang.String r5 = "NetWork"
            java.lang.String r6 = "openg network setting activity error"
            com.zhiping.dev.android.logger.ZpLogger.e(r5, r6)
            java.lang.String r2 = "open setting error"
            boolean r5 = android.text.TextUtils.isEmpty(r8)
            if (r5 != 0) goto L_0x0043
            r2 = r8
        L_0x0043:
            com.yunos.tv.core.CoreApplication r5 = com.yunos.tv.core.CoreApplication.getApplication()
            r6 = 0
            android.widget.Toast r5 = android.widget.Toast.makeText(r5, r2, r6)
            r5.show()
            goto L_0x0013
        L_0x0050:
            r1 = move-exception
            r3 = r4
            goto L_0x0030
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.core.util.Utils.startNetWorkSettingActivity(android.content.Context, java.lang.String):void");
    }

    public static String jsonString2HttpParam(String json) {
        if (TextUtils.isEmpty(json)) {
            return "";
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
        }
        if (obj == null) {
            return "";
        }
        String param = "";
        Iterator it = obj.keys();
        while (it.hasNext()) {
            String key = it.next();
            String value = obj.optString(key);
            if (TextUtils.isEmpty(param)) {
                param = key + "=" + value;
            } else {
                param = param + "&" + key + "=" + value;
            }
        }
        return param;
    }

    public static String parseHost(String httpUrl) {
        try {
            return new URL(httpUrl).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int dp2px(Context context, int dpVal) {
        return (int) ((((float) dpVal) * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public static boolean isFastClick() {
        boolean isAllowClick;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > ((long) spaceTime)) {
            isAllowClick = false;
        } else {
            isAllowClick = true;
        }
        lastClickTime = currentTime;
        return isAllowClick;
    }

    public static void updateNextPageProperties(String spm_url) {
        if (!TextUtils.isEmpty(spm_url)) {
            Map<String, String> nextparam = new HashMap<>();
            nextparam.put(Constants.PARAM_OUTER_SPM_URL, spm_url);
            UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(nextparam);
        }
    }

    public static String getPriceString(String symbol, double price) {
        return getPriceString(symbol, price, true);
    }

    public static String getPriceString(String symbol, String price) {
        try {
            return getPriceString(symbol, Double.parseDouble(price));
        } catch (Exception e) {
            return price;
        }
    }

    public static String getPriceString(String symbol, double price, boolean space) {
        String format;
        int digits = 0;
        int intPrice = (int) Math.abs(100.0d * price);
        if (intPrice % 10 > 0) {
            digits = 2;
        } else if (intPrice % 100 > 0) {
            digits = 1;
        }
        if (space) {
            format = String.format("%%s %%.%df", new Object[]{Integer.valueOf(digits)});
        } else {
            format = String.format("%%s%%.%df", new Object[]{Integer.valueOf(digits)});
        }
        return String.format(format, new Object[]{symbol, Double.valueOf(price)});
    }

    public static String getRebateCoupon(String rebateBoCoupon) {
        if (TextUtils.isEmpty(rebateBoCoupon)) {
            return null;
        }
        try {
            Double coupon = Double.valueOf(Double.parseDouble(rebateBoCoupon));
            if (coupon.doubleValue() > ClientTraceData.b.f47a) {
                String numString = (coupon.doubleValue() / 100.0d) + "";
                if (numString.indexOf(".") > 0) {
                    numString = numString.replaceAll("0+?$", "").replaceAll("[.]$", "");
                }
                ZpLogger.e("Rebate", "numString = " + numString);
                return numString;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSYCMItemIdListString(List<String> itemIdList) {
        ZpLogger.i("getSYCMItemIdListString", ".getListAddItemId,itemIdList = " + itemIdList.toString());
        if (itemIdList == null || itemIdList.size() <= 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < itemIdList.size(); i++) {
            if (i == itemIdList.size() - 1) {
                stringBuffer.append(itemIdList.get(i));
            } else {
                stringBuffer.append(itemIdList.get(i) + ",");
            }
        }
        return stringBuffer.toString();
    }

    private static String getAppkey() {
        ZpLogger.e(TAG, "Utils==get==appkey=====" + ((String) RtEnv.get("APPKEY")));
        return (String) RtEnv.get("APPKEY");
    }

    private static String getSubkey() {
        ZpLogger.e(TAG, "Utils==get==subkey=====" + ((String) RtEnv.get(RtEnv.SUBKEY)));
        return (String) RtEnv.get(RtEnv.SUBKEY);
    }

    public static Pair<String, String> getVersionNameAndCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return Pair.create(pInfo.versionName, "" + pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Pair.create("", "");
        }
    }

    public static String getDeviceName() {
        final String[] rlt = {""};
        new Runnable() {
            private String capitalize(String s) {
                if (s == null || s.length() == 0) {
                    return "";
                }
                char first = s.charAt(0);
                return !Character.isUpperCase(first) ? Character.toUpperCase(first) + s.substring(1) : s;
            }

            public void run() {
                String manufacturer = Build.MANUFACTURER;
                String model = Build.MODEL;
                if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
                    rlt[0] = capitalize(model);
                } else {
                    rlt[0] = capitalize(manufacturer) + " " + model;
                }
            }
        }.run();
        return rlt[0];
    }
}
