package android.taobao.windvane.packageapp;

import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.monitor.WVPerformanceMonitorInterface;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.webview.WVWrapWebResourceResponse;
import android.webkit.WebResourceResponse;
import java.util.Map;

public class WVPackageAppWebViewClientFilter implements WVEventListener {
    private String TAG = WVPackageAppWebViewClientFilter.class.getSimpleName();

    public WVEventResult onEvent(int id, WVEventContext ctx, Object... obj) {
        if (ctx == null) {
            return new WVEventResult(false);
        }
        if (id == 6002) {
            ZipAppDownloaderQueue.getInstance().startUpdateAppsTask();
        } else if (id == 1004 && WVCommonConfig.commonConfig.packageAppStatus != 0) {
            String monitorUrl = ctx.url;
            if (ctx.url != null && ctx.url.contains("https")) {
                ctx.url = ctx.url.replace("https", "http");
                if (TaoLog.getLogStatus()) {
                    TaoLog.v(this.TAG, "PackageappforDebug repalce https to http , " + ctx.url);
                }
            }
            long currentTimeMillis = System.currentTimeMillis();
            ZipAppInfo appInfo = WVPackageAppRuntime.getAppInfoByUrl(ctx.url);
            WVWrapWebResourceResponse res = null;
            if (appInfo != null) {
                res = WVPackageAppRuntime.getWrapResourceResponse(ctx.url, appInfo);
            }
            long currentTimeMillis2 = System.currentTimeMillis();
            if (res != null) {
                String seq = "0";
                try {
                    seq = String.valueOf(appInfo.s);
                } catch (Exception e) {
                }
                if (WVMonitorService.getPerformanceMonitor() != null) {
                    if (!WVUrlUtil.isHtml(ctx.url) || appInfo == null) {
                        WVMonitorService.getPerformanceMonitor().didGetResourceStatusCode(monitorUrl, 200, 3, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
                    } else {
                        WVMonitorService.getPerformanceMonitor().didGetPageStatusCode(monitorUrl, 200, 3, appInfo.v, appInfo.name, seq, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
                    }
                }
                return new WVEventResult(true, res);
            }
            long start = System.currentTimeMillis();
            ZipGlobalConfig.CacheFileData filedata = ConfigManager.getLocGlobalConfig().isZcacheUrl(ctx.url);
            if (filedata != null) {
                res = WVPackageAppRuntime.getWrapResourceResponse(ctx.url, filedata);
            }
            long end = System.currentTimeMillis();
            if (res != null) {
                String seq2 = "0";
                try {
                    seq2 = String.valueOf(filedata.seq);
                } catch (Exception e2) {
                }
                if (WVMonitorService.getPerformanceMonitor() != null) {
                    if (WVUrlUtil.isHtml(ctx.url)) {
                        WVMonitorService.getPerformanceMonitor().didGetPageStatusCode(monitorUrl, 200, 4, filedata.v, filedata.appName, seq2, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
                    } else {
                        WVMonitorService.getPerformanceMonitor().didGetResourceStatusCode(monitorUrl, 200, 4, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
                    }
                }
                return new WVEventResult(true, res);
            }
            try {
                WebResourceResponse response = WVPackageAppRuntime.makeComboRes(ctx.url);
                if (response != null) {
                    WVWrapWebResourceResponse wVWrapWebResourceResponse = new WVWrapWebResourceResponse(response.getMimeType(), response.getEncoding(), response.getData());
                    try {
                        return new WVEventResult(true, wVWrapWebResourceResponse);
                    } catch (Exception e3) {
                        WVWrapWebResourceResponse wVWrapWebResourceResponse2 = wVWrapWebResourceResponse;
                    }
                }
            } catch (Exception e4) {
            }
        }
        return new WVEventResult(false);
    }
}
