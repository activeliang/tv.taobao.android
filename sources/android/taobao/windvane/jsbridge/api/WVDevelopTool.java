package android.taobao.windvane.jsbridge.api;

import android.os.Build;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.debug.WVPageFinishJSRender;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.monitor.WVLocPerformanceMonitor;
import android.taobao.windvane.packageapp.WVPackageAppService;
import android.taobao.windvane.packageapp.WVPackageAppTool;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.log.AndroidLog;
import android.taobao.windvane.webview.WVWebView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WVDevelopTool extends WVApiPlugin {
    private static final String TAG = "WVDevelopTool";
    private static int mLastMode = 0;
    private boolean mIsDebugOpen = false;

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("isDebugEnabled".equals(action)) {
            WVResult result = new WVResult();
            result.addData("global", String.valueOf(EnvUtil.isDebug()));
            callback.success(result);
        } else if ("clearWindVaneCache".equals(action)) {
            clearWindVaneCache(params, callback);
        } else if ("setWebViewDebugEnabled".equals(action)) {
            setWebViewDebugEnabled(params, callback);
        } else if ("setWebViewFinishJs".equals(action)) {
            setWebViewFinishJs(params, callback);
        } else if ("clearWebViewFinishJs".equals(action)) {
            clearWebViewFinishJs(params, callback);
        } else if ("setPackageAppEnabled".equals(action)) {
            setPackageAppEnabled(params, callback);
        } else if ("isPackageAppEnabled".equals(action)) {
            isPackageAppEnabled(params, callback);
        } else if ("setUCEnabled".equals(action)) {
            setUCEnabled(params, callback);
        } else if ("isUCEnabled".equals(action)) {
            isUCEnabled(params, callback);
        } else if ("readPackageAppMemoryInfo".equals(action)) {
            readPackageAppMemoryInfo(params, callback);
        } else if ("readMemoryZCacheMap".equals(action)) {
            readMemoryZCacheMap(params, callback);
        } else if ("readMemoryPrefixes".equals(action)) {
            readMemoryPrefixes(params, callback);
        } else if ("readPackageAppDiskConfig".equals(action)) {
            readPackageAppDiskConfig(params, callback);
        } else if ("readPackageAppDiskFileList".equals(action)) {
            readPackageAppDiskFileList(params, callback);
        } else if ("clearPackageApp".equals(action)) {
            clearPackageApp(params, callback);
        } else if ("updatePackageApp".equals(action)) {
            updatePackageApp(params, callback);
        } else if ("getLocPerformanceData".equals(action)) {
            getLocPerformanceData(params, callback);
        } else if ("openSpdyforDebug".equals(action)) {
            openSpdyforDebug(params, callback);
        } else if ("closeSpdyforDebug".equals(action)) {
            closeSpdyforDebug(params, callback);
        } else if ("openLocPerformanceMonitor".equals(action)) {
            openLocPerformanceMonitor(params, callback);
        } else if ("closeLocPerformanceMonitor".equals(action)) {
            closeLocPerformanceMonitor(params, callback);
        } else if ("resetConfig".equals(action)) {
            resetConfig(callback, params);
        } else if ("updateConfig".equals(action)) {
            updateConfig(callback, params);
        } else if ("getConfigVersions".equals(action)) {
            getConfigVersions(callback, params);
        } else if ("setDebugEnabled".equals(action)) {
            setDebugEnabled(callback, params);
        } else if ("cleanUp".equals(action)) {
            cleanUp(callback, params);
        } else if (!"readMemoryStatisitcs".equals(action)) {
            return false;
        } else {
            readMemoryStatisitcs(callback, params);
        }
        return true;
    }

    public void openLocPerformanceMonitor(String params, WVCallBackContext callback) {
        WVLocPerformanceMonitor.setOpenLocPerformanceMonitor(true);
    }

    public void closeLocPerformanceMonitor(String params, WVCallBackContext callback) {
        WVLocPerformanceMonitor.setOpenLocPerformanceMonitor(false);
    }

    public void openSpdyforDebug(String params, WVCallBackContext callback) {
        EnvUtil.setOpenSpdyforDebug(true);
    }

    public void closeSpdyforDebug(String params, WVCallBackContext callback) {
        EnvUtil.setOpenSpdyforDebug(false);
    }

    public void getLocPerformanceData(String params, WVCallBackContext callback) {
        WVResult result = new WVResult();
        try {
            result.setData(new JSONObject(WVLocPerformanceMonitor.getInstance().getMonitorData().toString()));
            callback.success(result);
        } catch (Exception e) {
            callback.error(e.getMessage());
        }
    }

    public final void clearWindVaneCache(String params, WVCallBackContext callback) {
        this.mWebView.clearCache();
        callback.success();
    }

    public final void setPackageAppEnabled(String params, WVCallBackContext callback) {
        try {
            if (!new JSONObject(params).optBoolean(IWaStat.KEY_ENABLE, false)) {
                WVCommonConfig.getInstance();
                WVCommonConfig.commonConfig.packageAppStatus = 0;
            } else {
                WVCommonConfig.getInstance();
                WVCommonConfig.commonConfig.packageAppStatus = 2;
            }
            callback.success();
        } catch (Exception e) {
            callback.error();
        }
    }

    public final void isPackageAppEnabled(String params, WVCallBackContext callback) {
        WVResult result = new WVResult();
        WVCommonConfig.getInstance();
        if (WVCommonConfig.commonConfig.packageAppStatus == 0) {
            result.addData("enabled", "false");
        } else {
            result.addData("enabled", "true");
        }
        callback.success(result);
    }

    public final void setUCEnabled(String params, WVCallBackContext callback) {
        try {
            if (!new JSONObject(params).optBoolean(IWaStat.KEY_ENABLE, false)) {
                WVCommonConfig.getInstance();
                WVCommonConfig.commonConfig.useSystemWebView = true;
                Toast.makeText(this.mContext, "关闭UC, 重启后生效", 1).show();
            } else {
                WVCommonConfig.getInstance();
                WVCommonConfig.commonConfig.useSystemWebView = false;
                Toast.makeText(this.mContext, "启用UC, 重启后生效", 1).show();
            }
            callback.success();
        } catch (Exception e) {
            callback.error();
        }
    }

    public final void isUCEnabled(String params, WVCallBackContext callback) {
        WVResult result = new WVResult();
        WVCommonConfig.getInstance();
        if (WVCommonConfig.commonConfig.useSystemWebView) {
            result.addData("enabled", "false");
        } else {
            result.addData("enabled", "true");
        }
        callback.success(result);
    }

    public final void setWebViewDebugEnabled(String params, WVCallBackContext callback) {
        WVResult result = new WVResult();
        try {
            boolean on = new JSONObject(params).optBoolean("enabled", false);
            if (Build.VERSION.SDK_INT < 19) {
                result.addData("error", "api level < 19");
                callback.error(result);
                return;
            }
            if (this.mWebView instanceof WVWebView) {
                WVWebView wVWebView = (WVWebView) this.mWebView;
                WVWebView.setWebContentsDebuggingEnabled(on);
            }
            this.mIsDebugOpen = on;
            callback.success();
        } catch (Throwable th) {
            result.addData("error", "failed to enable debugging");
            callback.error(result);
        }
    }

    public final void setWebViewFinishJs(String params, WVCallBackContext callback) {
        WVResult result = new WVResult();
        try {
            WVPageFinishJSRender.setJsContent(new JSONObject(params).optString("js"));
            callback.success();
        } catch (JSONException e) {
            callback.error(WVResult.RET_PARAM_ERR);
        } catch (Throwable th) {
            result.addData("error", "failed to enable setWebViewFinishJs");
            callback.error(result);
        }
    }

    public final void clearWebViewFinishJs(String params, WVCallBackContext callback) {
        WVResult result = new WVResult();
        try {
            WVPageFinishJSRender.clearJsRender();
            callback.success();
        } catch (Throwable th) {
            result.addData("error", "failed to enable clearWebViewFinishJs");
            callback.error(result);
        }
    }

    public final void readMemoryZCacheMap(String params, WVCallBackContext callback) {
        ZipGlobalConfig config = null;
        if (WVPackageAppService.getWvPackageAppConfig() != null) {
            config = WVPackageAppService.getWvPackageAppConfig().getGlobalConfig();
        }
        if (config == null) {
            callback.error();
        } else {
            callback.success(JSON.toJSONString(config.getZcacheResConfig()));
        }
    }

    public final void readMemoryPrefixes(String params, WVCallBackContext callback) {
        String prefixes = ConfigStorage.getStringVal(ZipPrefixesManager.SPNAME, ZipPrefixesManager.DATA_KEY, "");
        if (prefixes == null) {
            callback.error();
        } else {
            callback.success(prefixes);
        }
    }

    public final void readPackageAppMemoryInfo(String params, WVCallBackContext callback) {
        ZipGlobalConfig config = null;
        if (WVPackageAppService.getWvPackageAppConfig() != null) {
            config = WVPackageAppService.getWvPackageAppConfig().getGlobalConfig();
        }
        if (config == null) {
            callback.error();
        } else {
            callback.success(JSON.toJSONString(config));
        }
    }

    public final void readPackageAppDiskConfig(String params, WVCallBackContext callback) {
        String config = ZipAppFileManager.getInstance().readGlobalConfig(false);
        WVResult result = new WVResult();
        result.addData(TuwenConstants.MODEL_LIST_KEY.TEXT, config);
        callback.success(result);
    }

    public final void readPackageAppDiskFileList(String params, WVCallBackContext callback) {
        List<String> fileList = WVPackageAppTool.getAppsFileList();
        WVResult result = new WVResult();
        result.addData("list", new JSONArray(fileList));
        callback.success(result);
    }

    public final void clearPackageApp(String params, WVCallBackContext callback) {
        WVPackageAppTool.uninstallAll();
        callback.success();
    }

    public final void updatePackageApp(String params, WVCallBackContext callback) {
        WVConfigManager.getInstance().resetConfig();
        WVConfigManager.getInstance().updateConfig(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypeCustom);
        callback.success();
    }

    private void resetConfig(WVCallBackContext callback, String param) {
        WVConfigManager.getInstance().resetConfig();
        WVConfigManager.getInstance().updateConfig(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypeCustom);
        callback.success();
    }

    private void updateConfig(WVCallBackContext callback, String param) {
        try {
            JSONObject jso = new JSONObject(param);
            WVConfigManager.getInstance().updateConfig(jso.optString("configName", ""), String.valueOf(Long.MAX_VALUE), jso.optString("configUrl", ""), WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypeCustom);
            callback.success();
        } catch (JSONException e) {
            callback.error(WVResult.RET_PARAM_ERR);
        }
    }

    private void getConfigVersions(WVCallBackContext callback, String param) {
        HashMap versionMap = WVConfigManager.getInstance().getConfigVersions();
        WVResult result = new WVResult();
        result.setSuccess();
        if (versionMap != null) {
            for (String key : versionMap.keySet()) {
                result.addData(key, (String) versionMap.get(key));
            }
        }
        callback.success(result);
    }

    private void cleanUp(WVCallBackContext callback, String param) {
        List<String> retList = WVPackageAppCleanup.getInstance().cleanUp(1);
        WVResult result = new WVResult();
        if (retList != null) {
            result.addData("validApps", new JSONArray(retList));
        }
        callback.success(result);
    }

    private void readMemoryStatisitcs(WVCallBackContext callback, String param) {
        callback.success();
    }

    private void setDebugEnabled(WVCallBackContext callback, String param) {
        WVResult result = new WVResult();
        try {
            if (new JSONObject(param).optBoolean("logLevel", true)) {
                TaoLog.setImpl(new AndroidLog());
                TaoLog.setLogSwitcher(true);
            } else {
                TaoLog.setLogSwitcher(false);
            }
            UCCore.setPrintLog(true);
            callback.success();
        } catch (JSONException e) {
            callback.error(WVResult.RET_PARAM_ERR);
        } catch (Throwable th) {
            result.addData("error", "failed to setDebugEnabled");
            callback.error(result);
        }
    }
}
