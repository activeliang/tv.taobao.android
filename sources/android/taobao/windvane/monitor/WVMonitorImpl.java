package android.taobao.windvane.monitor;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.monitor.WVMonitorConfig;
import android.taobao.windvane.monitor.WVMonitorData;
import android.taobao.windvane.monitor.WVPerformanceMonitorInterface;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class WVMonitorImpl implements WVPerformanceMonitorInterface, WVErrorMonitorInterface, WVConfigMonitorInterface {
    private static final String TAG = "WVMonitor";
    private long appStartTime;
    private String currentUrl;
    private ConcurrentHashMap<String, WVMonitorData> dataMap;
    private boolean enabled;
    private long initTime;
    private boolean isInit;
    private boolean needCommitStartTime;
    private HashSet<String> zipApps;

    public WVMonitorImpl() {
        this.needCommitStartTime = true;
        this.appStartTime = 0;
        this.initTime = 0;
        this.isInit = false;
        this.currentUrl = "";
        this.zipApps = new HashSet<>();
        this.dataMap = new ConcurrentHashMap<>();
        this.enabled = false;
        this.appStartTime = System.currentTimeMillis();
        this.enabled = true;
    }

    private static WVMonitorConfig getConfig() {
        return WVMonitorConfigManager.getInstance().config;
    }

    private boolean isEnabled() {
        if (GlobalConfig.context == null) {
            return false;
        }
        return this.enabled;
    }

    private void upload(String url) {
        WVMonitorData data;
        if (isEnabled() && this.dataMap != null && (data = this.dataMap.get(url)) != null) {
            data.url = formatUrl(data.url);
            UserTrackUtil.commitEvent(UserTrackUtil.EVENTID_MONITOR, formatUrl(url), data.stat.onLoad == 0 ? "" : "" + data.stat.onLoad, "" + ((data.stat.onDomLoad == 0 && data.stat.onLoad == 0) ? "" : Integer.valueOf(data.stat.finish)), data.toJsonStringDict());
            if (this.isInit && data.startTime > this.initTime) {
                this.isInit = false;
                data.isInit = true;
                data.init = data.startTime - this.initTime;
            }
            AppMonitorUtil.commitPerformanceInfo(data);
            String packageAppName = data.stat.packageAppName;
            if (this.zipApps != null && !TextUtils.isEmpty(packageAppName) && !this.zipApps.contains(packageAppName)) {
                AppMonitorUtil.commitPackageVisitStartInfo(packageAppName, System.currentTimeMillis() - this.appStartTime);
                this.zipApps.add(packageAppName);
            }
            if (this.needCommitStartTime && this.appStartTime != 0 && this.appStartTime < data.startTime) {
                AppMonitorUtil.commitStartTimeInfo(data.url, data.startTime - this.appStartTime);
                this.needCommitStartTime = false;
            }
            TaoLog.i(TAG, "upload performance info  URL: " + url + " fromType : " + data.stat.fromType + " packageAppName : " + data.stat.packageAppName);
            this.dataMap.remove(url);
        }
    }

    private static String formatUrl(String url) {
        if (url == null) {
            return null;
        }
        int pos = url.indexOf(63);
        if (pos <= 0) {
            pos = url.length();
        }
        int pos2 = url.indexOf(35);
        if (pos2 <= 0) {
            pos2 = url.length();
        }
        if (pos >= pos2) {
            pos = pos2;
        }
        return url.substring(0, pos);
    }

    private static boolean errorNeedReport(String url, String message, Integer code) {
        boolean isBlackList = getConfig().isErrorBlacklist;
        for (WVMonitorConfig.ErrorRule rule : getConfig().errorRule) {
            if (!(rule.url == null || url == null)) {
                if (rule.urlPattern == null) {
                    rule.urlPattern = Pattern.compile(rule.url);
                }
                if (!rule.urlPattern.matcher(url).matches()) {
                    continue;
                }
            }
            if (!(rule.msg == null || message == null)) {
                if (rule.msgPattern == null) {
                    rule.msgPattern = Pattern.compile(rule.msg);
                }
                if (!rule.msgPattern.matcher(message).matches()) {
                    continue;
                }
            }
            if (TextUtils.isEmpty(rule.code) || code == null || rule.code.equals(code.toString())) {
                return !isBlackList;
            }
        }
        return isBlackList;
    }

    private WVMonitorData initData(String url) {
        WVMonitorData dataM;
        if (this.dataMap == null) {
            return null;
        }
        WVMonitorData dataM2 = this.dataMap.get(url);
        if (dataM2 != null) {
            return dataM2;
        }
        synchronized (WVMonitorImpl.class) {
            if (dataM2 == null) {
                try {
                    TaoLog.i(TAG, "monitor data init");
                    dataM = new WVMonitorData();
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
                try {
                    this.currentUrl = url;
                    this.dataMap.put(url, dataM);
                    dataM2 = dataM;
                } catch (Throwable th2) {
                    th = th2;
                    WVMonitorData wVMonitorData = dataM;
                    throw th;
                }
            }
            return dataM2;
        }
    }

    public void didPageStartLoadAtTime(String url, long time) {
        if (isEnabled() && url != null && Uri.parse(url) != null && Uri.parse(url).isHierarchical()) {
            TaoLog.d(TAG, String.format("pageStart: %s", new Object[]{url}));
            WVMonitorData data = initData(url);
            if (data != null) {
                data.startTime = time;
                data.url = url;
            }
        }
    }

    public void didPageFinishLoadAtTime(String url, long time) {
        if (url != null && Uri.parse(url) != null && Uri.parse(url).isHierarchical()) {
            pageFinish(url, time, true);
        }
    }

    public void didPageDomLoadAtTime(String url, long time) {
        WVMonitorData data;
        if (isEnabled() && url != null && this.dataMap != null && (data = this.dataMap.get(url)) != null) {
            TaoLog.d(TAG, String.format("domLoad: %s", new Object[]{url}));
            if (data.startTime > 0) {
                long onReady = time - data.startTime;
                if (onReady >= getConfig().stat.onDomLoad) {
                    data.stat.onDomLoad = onReady;
                }
            }
        }
    }

    public void didPageReceiveFirstByteAtTime(String url, long time) {
        WVMonitorData data;
        if (isEnabled() && url != null && this.dataMap != null && (data = this.dataMap.get(url)) != null) {
            TaoLog.d(TAG, String.format("domLoad: %s", new Object[]{url}));
            if (data.startTime > 0) {
                data.stat.firstByteTime = time - data.startTime;
            }
        }
    }

    public void didPageOccurSelfDefinedEvent(String url, String eventName, long time) {
        WVMonitorData data;
        if (isEnabled() && url != null && this.dataMap != null && (data = this.dataMap.get(url)) != null) {
            TaoLog.d(TAG, String.format("domLoad: %s", new Object[]{url}));
            if (data.startTime > 0) {
                Map<String, Long> selfDefine = data.args.selfDefine;
                for (Map.Entry<String, Long> entry : selfDefine.entrySet()) {
                    selfDefine.put(entry.getKey(), Long.valueOf(entry.getValue().longValue() - data.startTime));
                }
            }
        }
    }

    public void didGetPageStatusCode(String url, int statusCode, int fromType, String version, String appName, String appSeq, Map<String, String> header, WVPerformanceMonitorInterface.NetStat netStat) {
        WVMonitorData data;
        if (isEnabled() && url != null && (data = initData(url)) != null) {
            data.args.netStat = netStat;
            if (statusCode > 0) {
                data.args.statusCode = statusCode;
            }
            if (fromType > 1 && data.stat.fromType <= 1) {
                data.stat.fromType = fromType;
            }
            if (!TextUtils.isEmpty(version)) {
                data.stat.packageAppVersion = version;
            }
            if (header != null) {
                data.args.via = header.get("via");
            }
            if (!TextUtils.isEmpty(appName)) {
                data.stat.packageAppName = appName;
            }
            if (!TextUtils.isEmpty(appSeq)) {
                data.stat.appSeq = appSeq;
            }
        }
    }

    public void didExitAtTime(String url, long time) {
        pageFinish(url, time, false);
    }

    public void didResourceStartLoadAtTime(String url, long time) {
        WVMonitorData data;
        if (this.dataMap != null && (data = this.dataMap.get(this.currentUrl)) != null && checkNeedCollectResInfo(url)) {
            getResData(url).start = time - data.startTime;
        }
    }

    public void didResourceFinishLoadAtTime(String url, long time) {
        WVMonitorData data;
        if (this.dataMap != null && (data = this.dataMap.get(this.currentUrl)) != null && checkNeedCollectResInfo(url)) {
            getResData(url).end = time - data.startTime;
        }
    }

    public void didGetResourceStatusCode(String url, int statusCode, int fromType, Map<String, String> header, WVPerformanceMonitorInterface.NetStat netStat) {
        WVMonitorData.resStat res;
        if (isPage(url)) {
            didGetPageStatusCode(url, statusCode, fromType, (String) null, (String) null, (String) null, header, netStat);
        } else if (checkNeedCollectResInfo(url) && (res = getResData(url)) != null) {
            res.fromType = fromType;
            res.statusCode = statusCode;
            res.via = header != null ? header.get("Via") : "";
            if (netStat != null && getConfig().stat.netstat) {
                res.netStat = netStat;
            }
        }
    }

    public void didGetResourceVerifyCode(String url, long verifyResTime, long verifyTime, int verifyError, int lruSize) {
        WVMonitorData data;
        if (this.dataMap != null && (data = this.dataMap.get(this.currentUrl)) != null) {
            if (isPage(url)) {
                if (isEnabled() && url != null) {
                    data.stat.verifyResTime = verifyResTime;
                    data.stat.verifyTime = verifyTime;
                    data.stat.verifyError = verifyError;
                } else {
                    return;
                }
            } else if (checkNeedCollectResInfo(url)) {
                WVMonitorData.resStat res = getResData(url);
                res.verifyResTime = verifyResTime;
                res.verifyTime = verifyTime;
                res.verifyError = verifyError;
            }
            data.stat.allVerifyTime += verifyTime;
            data.stat.verifyCacheSize = lruSize;
        }
    }

    public void didPerformanceCheckResult(String url, long score, String version, String bizcode, String result) {
        AppMonitorUtil.commitWebPerfCheckInfo(url, score, version, bizcode, result);
    }

    private boolean isPage(String url) {
        if (this.dataMap == null) {
            return false;
        }
        return formatUrl(this.currentUrl).equals(formatUrl(url));
    }

    private boolean checkNeedCollectResInfo(String url) {
        if (!isEnabled() || url == null || isPage(url)) {
            return false;
        }
        return true;
    }

    private WVMonitorData.resStat getResData(String url) {
        WVMonitorData data;
        if (this.dataMap == null || (data = this.dataMap.get(this.currentUrl)) == null) {
            return null;
        }
        WVMonitorData.resStat res = data.args.resStat.get(url);
        if (res != null) {
            return res;
        }
        WVMonitorData.resStat res2 = WVMonitorData.createNewResStatInstance();
        data.args.resStat.put(url, res2);
        return res2;
    }

    private void pageFinish(String url, long time, boolean finish) {
        WVMonitorData data;
        if (isEnabled() && url != null && this.dataMap != null && (data = this.dataMap.get(url)) != null) {
            TaoLog.d(TAG, String.format("pageFinish: %s", new Object[]{url}));
            if (data.startTime > 0) {
                try {
                    long onLoad = time - data.startTime;
                    TaoLog.d(TAG, String.format("url: %s", new Object[]{url}) + " onLoad time :" + onLoad);
                    WVMonitorConfig config = getConfig();
                    if (config != null && isEnabled() && onLoad >= config.stat.onLoad) {
                        data.stat.onLoad = onLoad;
                        data.stat.finish = finish ? 1 : 0;
                        try {
                            Uri uri = Uri.parse(url);
                            if (uri != null && uri.isHierarchical()) {
                                String monitorType = uri.getQueryParameter("wvAppMonitor");
                                if (!TextUtils.isEmpty(monitorType)) {
                                    try {
                                        data.wvAppMonitor = Integer.getInteger(monitorType).intValue();
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        } catch (Exception e2) {
                        }
                        upload(url);
                    }
                } catch (Exception e3) {
                }
            }
        }
    }

    @SuppressLint({"DefaultLocale"})
    public void didOccurNativeError(String url, int errorCode, String description) {
        if (isEnabled() && url != null && description != null) {
            TaoLog.d(TAG, String.format("reportNativeError: %s ///// %s ///// %d", new Object[]{url, description, Integer.valueOf(errorCode)}));
            if (isEnabled() && errorNeedReport(url, description, Integer.valueOf(errorCode))) {
                AppMonitorUtil.commitFail(AppMonitorUtil.NATIVE_ERROR_POINT, errorCode, String.format("message=%s\ncode=%d", new Object[]{description, Integer.valueOf(errorCode)}), url);
            }
        }
    }

    public void didOccurJSError(String url, String errorMessage, String file, String line) {
        if (isEnabled() && url != null && errorMessage != null && line != null && file != null) {
            TaoLog.d(TAG, String.format("reportJsError: %s ///// %s ///// %s ///// %s", new Object[]{url, file, errorMessage, line}));
            if (errorNeedReport(url, errorMessage, (Integer) null)) {
                AppMonitorUtil.commitFail(AppMonitorUtil.JS_ERROR_POINT, 1, String.format("message=%s\nline=%s\nfile=%s", new Object[]{errorMessage, line, file}), url);
            }
        }
    }

    public void didOccurUpdateConfigError(String configName, int errorCode, String errorMessage) {
        if (isEnabled() && errorMessage != null) {
            AppMonitorUtil.commitConifgUpdateError(configName, errorCode, errorMessage);
        }
    }

    public void didUpdateConfig(String name, int from, long updateTime, int isSuccess, int updateCount) {
        if (isEnabled() && name != null) {
            AppMonitorUtil.commitConifgUpdateInfo(name, from, updateTime, isSuccess, updateCount);
            TaoLog.i(TAG, "updateConfig " + name + " isSuccess : " + isSuccess + " count : " + updateCount);
        }
    }

    public void didOccurUpdateConfigSuccess(String message) {
        if (isEnabled() && message != null) {
            AppMonitorUtil.commitConifgUpdateSuccess(message);
        }
    }

    public void didWebViewInitAtTime(long time) {
        if (isEnabled()) {
            this.isInit = true;
            this.initTime = time;
        }
    }

    public void didPagePerformanceInfo(String url, String info) {
        WVMonitorData data;
        if (this.dataMap != null && (data = this.dataMap.get(url)) != null) {
            data.performanceInfo = info;
        }
    }
}
