package com.ut.mini.core;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.util.Log;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHybridHelper;
import com.ut.mini.internal.UTTeamWork;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class WVUserTrack extends WVApiPlugin {
    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("toUT".equals(action)) {
            toUT(params, callback);
            return true;
        } else if ("toUT2".equalsIgnoreCase(action)) {
            toUT2(params, callback);
            return true;
        } else if ("turnOnUTRealtimeDebug".equals(action)) {
            turnOnUTRealtimeDebug(params, callback);
            return true;
        } else if ("turnOffUTRealtimeDebug".equals(action)) {
            turnOffUTRealtimeDebug(params, callback);
            return true;
        } else if ("turnOnRealtimeDebug".equals(action)) {
            turnOnAppMonitorRealtimeDebug(params, callback);
            return true;
        } else if ("turnOffRealtimeDebug".equals(action)) {
            turnOffAppMonitorRealtimeDebug(params, callback);
            return true;
        } else if ("selfCheck".equals(action)) {
            selfCheck(params, callback);
            Log.i("selfCheck", "params" + params);
            return true;
        } else if (!"skipPage".equals(action)) {
            return false;
        } else {
            Log.i("skipPage", "params" + params);
            try {
                UTAnalytics.getInstance().getDefaultTracker().skipPage(this.mContext);
                return true;
            } catch (Throwable th) {
                return true;
            }
        }
    }

    public final void toUT(String params, WVCallBackContext callback) {
        Map<String, String> lDataMap;
        if (!(this.mContext == null || (lDataMap = transStringToMap(params)) == null)) {
            UTHybridHelper.getInstance().h5UT(lDataMap, this.mContext);
        }
        callback.success();
    }

    public void toUT2(String params, WVCallBackContext callback) {
        Map<String, String> lDataMap;
        if (!(this.mContext == null || (lDataMap = transStringToMap(params)) == null)) {
            UTHybridHelper.getInstance().h5UT2(lDataMap, this.mContext);
        }
        callback.success();
    }

    private Map<String, String> transStringToMap(String params) {
        Map<String, String> lDataMap = new HashMap<>();
        try {
            JSONObject lJsonObj = new JSONObject(params);
            Iterator<String> lKeys = lJsonObj.keys();
            while (lKeys.hasNext()) {
                String lKey = lKeys.next();
                if (!StringUtils.isEmpty(lKey)) {
                    String lValue = lJsonObj.getString(lKey);
                    if (!StringUtils.isEmpty(lValue)) {
                        lDataMap.put(lKey, lValue);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lDataMap;
    }

    public final void selfCheck(String params, WVCallBackContext callback) {
        if (!isEmpty(params)) {
            try {
                String utapSample = com.alibaba.fastjson.JSONObject.parseObject(params).get("utap_sample").toString();
                Log.i("selfCheck", "utap_sample:" + utapSample);
                String result = UTAnalytics.getInstance().selfCheck(utapSample);
                Log.i("selfCheck", "result:" + result);
                WVResult wvResult = new WVResult();
                wvResult.addData("result", result);
                callback.success(wvResult);
            } catch (com.alibaba.fastjson.JSONException e) {
                callback.error();
            }
        }
    }

    public final void turnOnUTRealtimeDebug(String params, WVCallBackContext callback) {
        if (!isEmpty(params)) {
            try {
                com.alibaba.fastjson.JSONObject jsObject = com.alibaba.fastjson.JSONObject.parseObject(params);
                Set<String> keySet = jsObject.keySet();
                Map<String, String> map = new HashMap<>();
                if (keySet != null && keySet.size() > 0) {
                    for (String key : keySet) {
                        map.put(key, jsObject.get(key).toString());
                    }
                    UTTeamWork.getInstance().turnOnRealTimeDebug(map);
                }
            } catch (com.alibaba.fastjson.JSONException e) {
                callback.error();
            }
        }
        callback.success();
    }

    public final void turnOffUTRealtimeDebug(String params, WVCallBackContext callback) {
        try {
            UTTeamWork.getInstance().turnOffRealTimeDebug();
        } catch (com.alibaba.fastjson.JSONException e) {
            callback.error();
        }
        callback.success();
    }

    public final void turnOnAppMonitorRealtimeDebug(String params, WVCallBackContext callback) {
        if (!isEmpty(params)) {
            try {
                com.alibaba.fastjson.JSONObject jsObject = com.alibaba.fastjson.JSONObject.parseObject(params);
                Set<String> keySet = jsObject.keySet();
                Map<String, String> map = new HashMap<>();
                if (keySet != null && keySet.size() > 0) {
                    for (String key : keySet) {
                        map.put(key, jsObject.get(key).toString());
                    }
                    AppMonitor.turnOnRealTimeDebug(map);
                }
            } catch (com.alibaba.fastjson.JSONException e) {
                callback.error();
            }
        }
        callback.success();
    }

    public final void turnOffAppMonitorRealtimeDebug(String params, WVCallBackContext callback) {
        try {
            AppMonitor.turnOffRealTimeDebug();
        } catch (com.alibaba.fastjson.JSONException e) {
            callback.error();
        }
        callback.success();
    }

    private boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }
}
