package android.taobao.windvane.extra.jsbridge;

import android.content.Context;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.packageapp.WVPackageAppPrefixesConfig;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import com.taobao.ju.track.constants.Constants;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

public class WVPackageAppInfo extends WVApiPlugin {
    private static final String TAG = "WVPackageAppInfo";

    public void initialize(Context context, IWVWebView webView) {
        WVEventService.getInstance().addEventListener(new WVPackageEventListener(webView));
        super.initialize(context, webView);
    }

    public static class WVPackageEventListener implements WVEventListener {
        private WeakReference<IWVWebView> webview;

        public WVPackageEventListener(IWVWebView view) {
            this.webview = new WeakReference<>(view);
        }

        public WVEventResult onEvent(int id, WVEventContext ctx, Object... obj) {
            if (this.webview.get() != null) {
                switch (id) {
                    case WVEventId.PACKAGE_UPLOAD_COMPLETE /*6001*/:
                        ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"全部app安装完成\"}");
                        TaoLog.d(WVPackageAppInfo.TAG, "PACKAGE_UPLOAD_COMPLETE");
                        break;
                    case WVEventId.ZIPAPP_UPLOAD_PERCENT /*6004*/:
                        ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"安装进度 : +" + obj[0] + "%\"}");
                        break;
                    case WVEventId.ZIPAPP_UNZIP /*6005*/:
                        ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"解压成功 : " + obj[0] + "\"}");
                        break;
                    case WVEventId.ZIPAPP_VALID /*6006*/:
                        Boolean success = obj[0];
                        ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"校验成功 : " + success + "\"}");
                        if (success.booleanValue()) {
                            ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"安装完成\"}");
                            break;
                        }
                        break;
                }
            }
            return null;
        }
    }

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("localPathForURL".equals(action)) {
            localPathForURL(callback, params);
        }
        if ("registerApp".equals(action)) {
            registerApp(callback, params);
        }
        if (!"previewApp".equals(action)) {
            return false;
        }
        previewApp(callback, params);
        return true;
    }

    private void localPathForURL(WVCallBackContext callback, String params) {
        WVResult result = new WVResult();
        try {
            String localPath = ZipAppUtils.getLocPathByUrl(new JSONObject(params).optString("url", "").replaceAll("^((?i)https:)?//", "http://"));
            if (TextUtils.isEmpty(localPath)) {
                result.setResult("HY_FAILED");
                callback.error(result);
                return;
            }
            result.addData("localPath", localPath);
            callback.success(result);
        } catch (Exception e) {
            TaoLog.e(TAG, "param parse to JSON error, param=" + params);
            result.setResult("HY_PARAM_ERR");
            callback.error(result);
        }
    }

    private void registerApp(WVCallBackContext callback, String params) {
        WVResult result = new WVResult();
        try {
            String appName = new JSONObject(params).optString("appName");
            ZipAppInfo info = new ZipAppInfo();
            info.name = appName;
            info.isOptional = true;
            ConfigManager.updateGlobalConfig(info, (String) null, false);
            callback.success();
        } catch (JSONException e) {
            TaoLog.e(TAG, "param parse to JSON error, param=" + params);
            result.setResult("HY_PARAM_ERR");
            callback.error(result);
        }
    }

    private void previewApp(WVCallBackContext callback, String params) {
        WVResult result = new WVResult();
        try {
            String appName = new JSONObject(params).optString("appName");
            final ZipGlobalConfig config = ConfigManager.getLocGlobalConfig();
            String prefix = "http://wapp." + GlobalConfig.env.getValue() + ".taobao.com/app/";
            ConnectManager.getInstance().connect(prefix + appName + "/app-prefix.wvc", (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
                public void onFinish(HttpResponse data, int token) {
                    byte[] bytes = data.getData();
                    if (data != null && bytes != null) {
                        try {
                            String content = new String(bytes, "utf-8");
                            new JSONObject(content);
                            if (WVPackageAppPrefixesConfig.getInstance().parseConfig(content)) {
                                WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"app-prefix 解析成功\"}");
                                return;
                            }
                        } catch (Exception e) {
                        }
                        WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"app-prefix 解析失败\"}");
                    }
                }
            });
            ConnectManager.getInstance().connect(prefix + appName + "/config/app.json", (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
                public void onFinish(HttpResponse data, int token) {
                    byte[] bytes = data.getData();
                    if (data != null && bytes != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(bytes, "utf-8"));
                            String appName = jsonObject.keys().next();
                            JSONObject appInfoObj = jsonObject.getJSONObject(appName);
                            if (appInfoObj != null) {
                                String v = appInfoObj.optString("v", "");
                                if (!TextUtils.isEmpty(v)) {
                                    ZipAppInfo appInfo = config.getAppInfo(appName);
                                    if (appInfo == null) {
                                        appInfo = new ZipAppInfo();
                                        config.putAppInfo2Table(appName, appInfo);
                                    }
                                    appInfo.isPreViewApp = true;
                                    appInfo.v = v;
                                    appInfo.name = appName;
                                    appInfo.s = appInfoObj.optLong("s", 0);
                                    appInfo.f = appInfoObj.optLong("f", 5);
                                    appInfo.t = appInfoObj.optLong("t", 0);
                                    appInfo.z = appInfoObj.optString("z", "");
                                    appInfo.installedSeq = 0;
                                    appInfo.installedVersion = Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
                                    WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"app.json 解析成功\"}");
                                    WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"准备下载(如长时间未开始下载，请刷新本页面)\"}");
                                    WVConfigManager.getInstance().resetConfig();
                                    WVConfigManager.getInstance().updateConfig(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypeCustom);
                                }
                            }
                        } catch (Exception e) {
                            WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"app.json 解析失败\"}");
                        }
                    }
                }
            });
            callback.success();
        } catch (JSONException e) {
            TaoLog.e(TAG, "param parse to JSON error, param=" + params);
            result.setResult("HY_PARAM_ERR");
            callback.error(result);
        }
    }
}
