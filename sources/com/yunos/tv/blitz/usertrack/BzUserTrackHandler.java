package com.yunos.tv.blitz.usertrack;

import android.app.Application;
import android.text.TextUtils;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import com.ut.mini.UTAnalytics;
import com.ut.mini.core.sign.UTSecuritySDKRequestAuthentication;
import com.ut.mini.crashhandler.IUTCrashCaughtListner;
import com.yunos.tv.blitz.account.BzDebugLog;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.global.BzAppConfig;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BzUserTrackHandler {
    private static final String TAG = "BzUserTrackHandler";
    private static IUTCrashCaughtListner defaultUTCrashListner = new IUTCrashCaughtListner() {
        public Map<String, String> onCrashCaught(Thread pThread, Throwable pException) {
            return null;
        }
    };
    private static boolean mInitTBS = false;

    public static void initTBS(String channel, Application context, boolean isDebug) {
        initTBS(context, channel, isDebug, (String) null, (String) null, false, (IUTCrashCaughtListner) null);
    }

    public static void initTBS(Application context, String channel, boolean isDebug, String appKey, String appVersion, boolean autoPageTrack, IUTCrashCaughtListner iutCrashCaughtListner) {
        Application utContext;
        if (!mInitTBS) {
            if (isDebug) {
                UTAnalytics.getInstance().turnOnDebug();
            }
            if (context != null) {
                utContext = context;
            } else {
                utContext = (Application) BzAppConfig.context.getContext();
            }
            if (channel == null || channel.isEmpty()) {
                BzDebugLog.e(TAG, "channel is Empty!");
            }
            if (appKey == null || appKey.isEmpty()) {
                appKey = BzAppConfig.context.getAppkey();
            }
            if (appVersion == null || appVersion.isEmpty()) {
                appVersion = BzAppConfig.getAppVersionName();
            }
            if (iutCrashCaughtListner == null) {
                iutCrashCaughtListner = defaultUTCrashListner;
            }
            BzDebugLog.i(TAG, "initTBS, channel: " + channel + ", isDebug: " + isDebug + ", appKey: " + appKey + ", appVersion: " + appVersion + ", autoTrack: " + autoPageTrack);
            UTAnalytics.getInstance().setContext(utContext);
            UTAnalytics.getInstance().setAppApplicationInstance(utContext);
            UTAnalytics.getInstance().setChannel(channel);
            UTAnalytics.getInstance().setRequestAuthentication(new UTSecuritySDKRequestAuthentication(appKey));
            UTAnalytics.getInstance().setAppVersion(appVersion);
            UTAnalytics.getInstance().setCrashCaughtListener(iutCrashCaughtListner);
            if (autoPageTrack) {
                UTAnalytics.getInstance().turnOffAutoPageTrack();
            }
            mInitTBS = true;
            BzDebugLog.v(TAG, "initUTMini end");
        }
    }

    public static String onUserTrackClick(String param) {
        BzResult result = new BzResult();
        try {
            JSONObject jsObj = new JSONObject(param);
            String type = jsObj.optString("type");
            String pageName = jsObj.optString("pageName");
            String controlName = jsObj.optString("controlName");
            JSONArray args = jsObj.optJSONArray("args");
            JSONObject properties = jsObj.optJSONObject("properties");
            Map<String, String> p = UserTrackHelper.getProperties();
            if (args != null) {
                int length = args.length();
                for (int i = 0; i < length; i++) {
                    String[] kv = args.optString(i, "").split("=");
                    if (kv.length == 2) {
                        p.put(kv[0], kv[1]);
                    }
                }
            }
            if (properties != null) {
                Iterator<String> keys = properties.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = properties.getString(key);
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        p.put(key, value);
                    }
                }
            }
            BzDebugLog.v(TAG, "BzUserTrackHandler.click pageName = " + pageName + ", controlName = " + controlName + ", p = " + p + ", type = " + type);
            if ("Button".equals(type)) {
                TBS.Adv.ctrlClicked(pageName, CT.Button, controlName, UserTrackHelper.getKvs(p));
            } else if ("List".equals(type)) {
                TBS.Adv.ctrlClicked(pageName, CT.ListItem, controlName, UserTrackHelper.getKvs(p));
            } else {
                UserTrackHelper.utControlHit(controlName, p);
            }
            result.setSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            BzDebugLog.e(TAG, e.toString());
        }
        return result.toJsonString();
    }

    public static String onUserTrackUpdatePageProperties(String param) {
        BzDebugLog.i(TAG, "onUserTrackUpdatePageProperties , param  = " + param);
        BzResult result = new BzResult();
        try {
            JSONObject jsObj = new JSONObject(param);
            String pageName = jsObj.optString("pageName");
            JSONObject properties = jsObj.optJSONObject("properties");
            BzDebugLog.i(TAG, "onUserTrackUpdatePageProperties , pageName  = " + pageName);
            if (!TextUtils.isEmpty(pageName)) {
                Map<String, String> p = UserTrackHelper.getProperties();
                if (properties != null) {
                    Iterator<String> keys = properties.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = properties.getString(key);
                        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                            p.put(key, value);
                        }
                    }
                }
                BzDebugLog.v(TAG, "BzUserTrackHandler.updatePageProperties pageName = " + pageName + ", p = " + p);
                UserTrackHelper.utUpdatePageProperties(pageName, p);
                result.setSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            BzDebugLog.e(TAG, e.toString());
        }
        return result.toJsonString();
    }

    public static String onUserTrackCommitEvent(String param) {
        BzDebugLog.i(TAG, "onUserTrackCommitEvent , param  = " + param);
        BzResult result = new BzResult();
        try {
            JSONObject jsObj = new JSONObject(param);
            String pageName = jsObj.optString("pageName");
            String controlName = jsObj.optString("controlName");
            if (TextUtils.isEmpty(controlName)) {
                controlName = jsObj.optString("EventID");
            }
            if (!TextUtils.isEmpty(controlName)) {
                JSONObject properties = jsObj.optJSONObject("properties");
                Map<String, String> p = UserTrackHelper.getProperties();
                if (properties != null) {
                    Iterator<String> keys = properties.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = properties.getString(key);
                        if (!(key == null || value == null)) {
                            p.put(key, value);
                        }
                    }
                }
                if (TextUtils.isEmpty(pageName)) {
                    pageName = UserTrackHelper.utGetCurrentPage();
                }
                BzDebugLog.v(TAG, "BzUserTrackHandler.commitEvent pageName = " + pageName + ", controlName = " + controlName + ", p = " + p);
                UserTrackHelper.utCustomHit(pageName, controlName, p);
                result.setSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            BzDebugLog.e(TAG, e.toString());
        }
        return result.toJsonString();
    }

    public static String onUserTrackPageEnter(String param) {
        BzDebugLog.i(TAG, "onUserTrackPageEnter , param  = " + param);
        JSONObject jsObj_type = new JSONObject();
        try {
            jsObj_type.put("type", "enter");
        } catch (JSONException e) {
        }
        return page(jsObj_type, param);
    }

    public static String onUserTrackPageLeave(String param) {
        BzDebugLog.i(TAG, "onUserTrackPageLeave , param  = " + param);
        JSONObject jsObj_type = new JSONObject();
        try {
            jsObj_type.put("type", "leave");
        } catch (JSONException e) {
        }
        return page(jsObj_type, param);
    }

    private static String page(JSONObject jsobj_type, String param) {
        BzResult result = new BzResult();
        try {
            String type = jsobj_type.optString("type");
            String pageName = new JSONObject(param).optString("pageName");
            if ("enter".equals(type) && !TextUtils.isEmpty(pageName)) {
                UserTrackHelper.utPageAppear(pageName, pageName);
            } else if ("leave".equals(type) && !TextUtils.isEmpty(pageName)) {
                UserTrackHelper.utPageDisAppear(pageName);
            }
            result.setSuccess();
            BzDebugLog.v(TAG, "BzUserTrackHandler.page pageName = " + pageName + ", type = " + type);
        } catch (Exception e) {
            e.printStackTrace();
            BzDebugLog.e(TAG, e.toString());
        }
        return result.toJsonString();
    }
}
