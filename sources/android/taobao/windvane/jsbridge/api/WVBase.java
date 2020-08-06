package android.taobao.windvane.jsbridge.api;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.taobao.windvane.config.EnvEnum;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.jsbridge.IJsApiFailedCallBack;
import android.taobao.windvane.jsbridge.IJsApiSucceedCallBack;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVCallMethodContext;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.api.WVAPI;
import android.taobao.windvane.monitor.UserTrackUtil;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.WVWebView;
import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import anetwork.channel.util.RequestConstant;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tv.alitvasrsdk.CommonData;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class WVBase extends WVApiPlugin {
    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("isWindVaneSDK".equals(action)) {
            isWindVaneSDK(callback, params);
        } else if ("plusUT".equals(action)) {
            plusUT(callback, params);
        } else if ("commitUTEvent".equals(action)) {
            commitUTEvent(callback, params);
        } else if ("isInstall".equals(action)) {
            isInstall(callback, params);
        } else if ("isAppsInstalled".equals(action)) {
            isAppsInstalled(callback, params);
        } else if ("copyToClipboard".equals(action)) {
            copyToClipboard(callback, params);
        } else if (!"addTailJSBridge".equals(action)) {
            return false;
        } else {
            addTailJSBridge(callback, params);
        }
        return true;
    }

    public void addTailJSBridge(WVCallBackContext callback, String params) {
        try {
            JSONObject jso = new JSONObject(params);
            String className = jso.getString(CommonData.KEY_CLASS_NAME);
            String handlerName = jso.getString("handlerName");
            String param = jso.getString("params");
            WVCallMethodContext context = new WVCallMethodContext();
            context.objectName = className;
            context.methodName = handlerName;
            context.params = param;
            context.webview = this.mWebView;
            context.succeedCallBack = new IJsApiSucceedCallBack() {
                public void succeed(String result) {
                }
            };
            context.failedCallBack = new IJsApiFailedCallBack() {
                public void fail(String result) {
                }
            };
            if (WVJsBridge.getInstance().mTailBridges == null) {
                WVJsBridge.getInstance().mTailBridges = new ArrayList<>();
            }
            WVJsBridge.getInstance().mTailBridges.add(context);
            TaoLog.i(WVAPI.PluginName.API_BASE, "addTailJSBridge : " + params);
        } catch (Exception e) {
        }
    }

    public void isWindVaneSDK(WVCallBackContext callback, String params) {
        WVResult result = new WVResult();
        result.addData("os", DispatchConstants.ANDROID);
        result.addData("version", GlobalConfig.VERSION);
        if (TaoLog.getLogStatus()) {
            TaoLog.d(WVAPI.PluginName.API_BASE, "isWindVaneSDK: version=8.0.0");
        }
        String env = "release";
        if (EnvEnum.DAILY.equals(GlobalConfig.env)) {
            env = "daily";
        } else if (EnvEnum.PRE.equals(GlobalConfig.env)) {
            env = RequestConstant.ENV_PRE;
        }
        result.addData("env", env);
        String container = "WVUCWebView";
        if (this.mWebView instanceof WVWebView) {
            container = "WVWebView";
        }
        result.addData("container", container);
        callback.success(result);
    }

    public void plusUT(WVCallBackContext callback, String params) {
        JSONObject args;
        boolean isSucc = false;
        try {
            JSONObject jso = new JSONObject(params);
            int eid = jso.getInt("eid");
            String arg1 = jso.getString("a1");
            String arg2 = jso.getString("a2");
            String arg3 = jso.getString("a3");
            String[] arg4 = new String[0];
            if (jso.has("args") && (args = jso.getJSONObject("args")) != null) {
                arg4 = new String[args.length()];
                Iterator it = args.keys();
                int i = 0;
                while (it.hasNext()) {
                    String key = it.next();
                    arg4[i] = String.format("%s=%s", new Object[]{key, args.getString(key)});
                    i++;
                }
            }
            if ((eid >= 9100 && eid < 9200) || eid == 19999) {
                isSucc = true;
                UserTrackUtil.commitEvent(eid, arg1, arg2, arg3, arg4);
            }
        } catch (JSONException e) {
        }
        WVResult result = new WVResult();
        if (isSucc) {
            callback.success(result);
            if (TaoLog.getLogStatus()) {
                TaoLog.d(WVAPI.PluginName.API_BASE, "plusUT: param=" + params);
                return;
            }
            return;
        }
        TaoLog.e(WVAPI.PluginName.API_BASE, "plusUT: parameter error, param=" + params);
        result.setResult("HY_PARAM_ERR");
        callback.error(result);
    }

    public void commitUTEvent(WVCallBackContext callback, String params) {
        boolean isSucc = false;
        try {
            JSONObject jso = new JSONObject(params);
            int eid = jso.getInt("eventId");
            String arg1 = jso.getString("arg1");
            String arg2 = jso.getString("arg2");
            String arg3 = jso.getString("arg3");
            JSONObject args = jso.getJSONObject("args");
            String[] arg4 = null;
            if (args != null) {
                arg4 = new String[args.length()];
                Iterator it = args.keys();
                int i = 0;
                while (it.hasNext()) {
                    String key = it.next();
                    arg4[i] = String.format("%s=%s", new Object[]{key, args.getString(key)});
                    i++;
                }
            }
            if (64403 == eid) {
                isSucc = true;
                UserTrackUtil.commitEvent(eid, arg1, arg2, arg3, arg4);
            }
        } catch (JSONException e) {
        }
        WVResult result = new WVResult();
        if (isSucc) {
            callback.success(result);
            if (TaoLog.getLogStatus()) {
                TaoLog.d(WVAPI.PluginName.API_BASE, "commitUTEvent: param=" + params);
                return;
            }
            return;
        }
        TaoLog.e(WVAPI.PluginName.API_BASE, "commitUTEvent: parameter error, param=" + params);
        result.setResult("HY_PARAM_ERR");
        callback.error(result);
    }

    public void isInstall(WVCallBackContext callback, String params) {
        String pkg = null;
        try {
            pkg = new JSONObject(params).getString(DispatchConstants.ANDROID);
        } catch (JSONException e) {
            TaoLog.e(WVAPI.PluginName.API_BASE, "isInstall parse params error, params: " + params);
        }
        WVResult result = new WVResult();
        boolean _installed = CommonUtils.isAppInstalled(this.mContext, pkg);
        if (TaoLog.getLogStatus()) {
            TaoLog.d(WVAPI.PluginName.API_BASE, "isInstall " + _installed + " for package " + pkg);
        }
        if (_installed) {
            callback.success(result);
        } else {
            callback.error(result);
        }
    }

    public void isAppsInstalled(WVCallBackContext context, String params) {
        String str;
        try {
            JSONObject obj = new JSONObject(params);
            Iterator<String> keys = obj.keys();
            WVResult res = new WVResult();
            PackageManager pm = this.mContext.getPackageManager();
            while (keys.hasNext()) {
                String appname = keys.next();
                try {
                    PackageInfo pInfo = null;
                    try {
                        pInfo = pm.getPackageInfo(obj.getJSONObject(appname).optString(DispatchConstants.ANDROID), 0);
                    } catch (Exception e) {
                    }
                    if (pInfo == null) {
                        str = "0";
                    } else {
                        str = "1";
                    }
                    res.addData(appname, str);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                    res.addData(appname, "0");
                }
            }
            res.setSuccess();
            context.success(res);
            JSONObject jSONObject = obj;
        } catch (JSONException e3) {
            e3.printStackTrace();
            context.error();
        }
    }

    private void copyToClipboard(WVCallBackContext callback, String params) {
        WVResult result = new WVResult("HY_PARAM_ERR");
        String errMsg = "HY_PARAM_ERR";
        if (!TextUtils.isEmpty(params)) {
            try {
                JSONObject jsonObject = new JSONObject(params);
                if (jsonObject.has(TuwenConstants.MODEL_LIST_KEY.TEXT)) {
                    String clipStr = jsonObject.getString(TuwenConstants.MODEL_LIST_KEY.TEXT);
                    if (Build.VERSION.SDK_INT >= 11) {
                        ((ClipboardManager) this.mContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(clipStr, clipStr));
                        WVResult result2 = new WVResult("HY_SUCCESS");
                        try {
                            callback.success(result2);
                            WVResult wVResult = result2;
                            return;
                        } catch (JSONException e) {
                            e = e;
                            result = result2;
                            e.printStackTrace();
                            result.addData("msg", errMsg);
                            callback.error(result);
                        }
                    } else {
                        errMsg = "HY_FAILED";
                        result = new WVResult("HY_FAILED");
                    }
                }
            } catch (JSONException e2) {
                e = e2;
                e.printStackTrace();
                result.addData("msg", errMsg);
                callback.error(result);
            }
        }
        result.addData("msg", errMsg);
        callback.error(result);
    }
}
