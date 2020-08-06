package android.taobao.windvane.packageapp;

import android.annotation.SuppressLint;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.monitor.WVPackageMonitorInterface;
import android.taobao.windvane.monitor.WVPerformanceMonitorInterface;
import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateTypeEnum;
import android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.webview.WVWrapWebResourceResponse;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class WVPackageAppRuntime {
    public static final String TAG = "PackageApp-Runtime";

    public static WVWrapWebResourceResponse getWrapResourceResponse(String url, ZipGlobalConfig.CacheFileData filedata) {
        WebResourceResponse res = getZcacheResourceResponse(url, filedata);
        if (res != null) {
            return new WVWrapWebResourceResponse(res.getMimeType(), res.getEncoding(), res.getData());
        }
        return null;
    }

    @SuppressLint({"NewApi"})
    public static WebResourceResponse getZcacheResourceResponse(String url, ZipGlobalConfig.CacheFileData filedata) {
        if (filedata != null) {
            try {
                long startTime = System.currentTimeMillis();
                ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(filedata.appName);
                if (appInfo == null || !isAvailable(url, appInfo)) {
                    return null;
                }
                byte[] data = FileAccesser.read(filedata.path);
                String mimeType = WVUrlUtil.getMimeTypeExtra(url);
                long readTime = System.currentTimeMillis();
                if (data == null || data.length <= 0) {
                    if (WVMonitorService.getPackageMonitorInterface() != null) {
                        WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo == null ? "unknown-0" : appInfo.getNameAndSeq(), url, "12");
                    }
                    if (TaoLog.getLogStatus()) {
                        TaoLog.e(TAG, "ZcacheforDebug :命中url 但本地文件读取失败：文件流为空[" + url + "]");
                    }
                } else {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                    if (byteArrayInputStream == null) {
                        if (WVMonitorService.getPackageMonitorInterface() != null) {
                            WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo == null ? "unknown-0" : appInfo.getNameAndSeq(), "create ByteArrayInputStream failed : " + url, "1");
                        }
                        return null;
                    }
                    long verifyTime = 0;
                    if (needCheckSecurity(appInfo.name)) {
                        if (!WVZipSecurityManager.getInstance().isFileSecrity(url, data, filedata.path, appInfo.name)) {
                            if (WVMonitorService.getPackageMonitorInterface() != null) {
                                WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo == null ? "unknown-0" : appInfo.getNameAndSeq(), url, "10");
                            }
                            return null;
                        }
                        verifyTime = System.currentTimeMillis();
                    }
                    WVPackageAppCleanup.getInstance().updateAccessTimes(appInfo);
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(TAG, "ZcacheforDebug :命中[" + url + "]");
                    }
                    long readUseTime = readTime - startTime;
                    long verifyUseTime = verifyTime == 0 ? 0 : verifyTime - readTime;
                    if (WVMonitorService.getPackageMonitorInterface() != null) {
                        WVMonitorService.getPackageMonitorInterface().commitPackageVisitInfo(appInfo.name, verifyTime == 0 ? "false" : "true", readUseTime + verifyUseTime, 0, readUseTime, verifyUseTime, appInfo.installedSeq);
                        WVMonitorService.getPackageMonitorInterface().commitPackageVisitSuccess(appInfo.name, appInfo.installedSeq);
                    }
                    return new WebResourceResponse(mimeType, ZipAppConstants.DEFAULT_ENCODING, byteArrayInputStream);
                }
            } catch (Exception e) {
                if (WVMonitorService.getPackageMonitorInterface() != null) {
                    WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(filedata == null ? "unknown-0" : filedata.appName + "-" + filedata.seq, url + " : " + e.getMessage(), "9");
                }
                TaoLog.e(TAG, "ZcacheforDebug 入口:访问本地zip资源失败 [" + url + "]" + e.getMessage());
            }
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "ZcacheforDebug 入口:未命中[" + url + "]");
        }
        return null;
    }

    private static String getComboUrl(String prefix, String path) {
        int dataIndex = 0;
        while ('/' == path.charAt(dataIndex)) {
            dataIndex++;
        }
        String comboUrlData = path;
        if (dataIndex != 0) {
            comboUrlData = path.substring(dataIndex);
        }
        return prefix + WVNativeCallbackUtil.SEPERATER + comboUrlData;
    }

    @SuppressLint({"NewApi"})
    public static WebResourceResponse makeComboRes(String url) {
        String[] comboUrls;
        long startTime = System.currentTimeMillis();
        if (!WVCommonConfig.commonConfig.isOpenCombo || url == null || url.indexOf("??") == -1 || (comboUrls = WVUrlUtil.parseCombo(url)) == null) {
            return null;
        }
        int comboIndex = url.indexOf("??");
        while (true) {
            if ('/' != url.charAt(comboIndex - 1)) {
                break;
            }
            comboIndex--;
        }
        String urlPrefix = url.substring(0, comboIndex);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String[] filePathDatas = new String[comboUrls.length];
        HashSet<ZipAppInfo> appList = new HashSet<>();
        for (int i = 0; i < comboUrls.length; i++) {
            if (!TextUtils.isEmpty(comboUrls[i])) {
                String path = getComboUrl(urlPrefix, comboUrls[i]);
                ZipAppInfo appInfo = getAppInfoByUrl(path);
                if (appInfo == null || !isAvailable(path, appInfo)) {
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(TAG, "ZcacheforDebug 入口:combo未命中[" + url + "] 含非zcache 资源:[" + path + "]");
                    }
                    return null;
                }
                String localPath = ZipAppUtils.getLocPathByUrl(path);
                if (localPath == null) {
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(TAG, "ZcacheforDebug 入口:combo未命中[" + url + "] 含非zcache 资源:[" + path + "]");
                    }
                    return null;
                }
                appList.add(appInfo);
                filePathDatas[i] = localPath;
            }
        }
        long matchTime = System.currentTimeMillis();
        for (int i2 = 0; i2 < filePathDatas.length; i2++) {
            if (!TextUtils.isEmpty(filePathDatas[i2])) {
                byte[] data = FileAccesser.read(filePathDatas[i2]);
                if (data == null || data.length <= 0) {
                    if (WVMonitorService.getPackageMonitorInterface() != null) {
                        ZipAppInfo appInfo2 = getAppInfoByUrl(getComboUrl(urlPrefix, comboUrls[i2]));
                        WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo2 == null ? "unknown-0" : appInfo2.getNameAndSeq(), url, "12");
                    }
                    return null;
                }
                try {
                    baos.write(data);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(baos.toByteArray());
            String mimeType = WVUrlUtil.getMimeTypeExtra(url);
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "ZcacheforDebug :命中combo[" + url + "]");
            }
            long readUseTime = System.currentTimeMillis() - matchTime;
            long matchUseTime = matchTime - startTime;
            if (WVMonitorService.getPackageMonitorInterface() != null) {
                Iterator<ZipAppInfo> apps = appList.iterator();
                while (apps.hasNext()) {
                    ZipAppInfo app = apps.next();
                    if (app != null) {
                        WVMonitorService.getPackageMonitorInterface().commitPackageVisitSuccess(app.name, app.installedSeq);
                    }
                }
                WVMonitorService.getPackageMonitorInterface().commitPackageVisitInfo("COMBO", "false", readUseTime + matchUseTime, matchUseTime, readUseTime, 0, 1);
            }
            WebResourceResponse webResourceResponse = new WebResourceResponse(mimeType, ZipAppConstants.DEFAULT_ENCODING, byteArrayInputStream);
            if (webResourceResponse != null) {
                if (WVMonitorService.getPerformanceMonitor() == null) {
                    return webResourceResponse;
                }
                WVMonitorService.getPerformanceMonitor().didGetResourceStatusCode(url, 200, 8, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
                return webResourceResponse;
            }
        } catch (Exception e2) {
            TaoLog.e(TAG, "ZcacheforDebug 入口:访问本地combo zip资源失败 [" + url + "]" + e2.getMessage());
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "ZcacheforDebug 入口:combo未命中[" + url + "]");
        }
        return null;
    }

    public static ZipAppInfo getAppInfoByUrl(String url) {
        String appName = ZipPrefixesManager.getInstance().getZipAppName(url);
        if (appName == null) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "PackageappforDebug :appName==null[" + url + "]");
            }
            return null;
        }
        try {
            ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(appName);
            if (appInfo != null) {
                return appInfo;
            }
            if (WVCommonConfig.commonConfig.isAutoRegisterApp) {
                ZipAppInfo info = new ZipAppInfo();
                info.name = appName;
                info.isOptional = true;
                ConfigManager.updateGlobalConfig(info, (String) null, false);
                WVCustomPackageAppConfig.getInstance().resetConfig();
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "PackageappforDebug :autoRegist [" + appName + "]");
                }
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "PackageappforDebug :appInfo==null[" + url + "]");
            }
            return null;
        } catch (Exception e) {
            TaoLog.e(TAG, "PackageappforDebug 通过url获取APPinfo异常ul: [" + url + "  appName:" + appName + "],errorMag:" + e.getMessage());
            return null;
        }
    }

    public static boolean isAvailable(String url, ZipAppInfo appInfo) {
        String nameAndSeq;
        if (appInfo == null || appInfo.installedSeq == 0) {
            if (WVMonitorService.getPackageMonitorInterface() != null) {
                WVPackageMonitorInterface packageMonitorInterface = WVMonitorService.getPackageMonitorInterface();
                if (appInfo == null) {
                    nameAndSeq = "unknown-0";
                } else {
                    nameAndSeq = appInfo.getNameAndSeq();
                }
                packageMonitorInterface.commitPackageVisitError(nameAndSeq, url, WVPackageMonitorInterface.NOT_INSTALL_FAILED);
            }
            return false;
        } else if (WVCommonConfig.commonConfig.packageAppStatus == 0) {
            if (WVMonitorService.getPackageMonitorInterface() != null) {
                WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo == null ? "unknown-0" : appInfo.getNameAndSeq(), url, WVPackageMonitorInterface.CONFIG_CLOSED_FAILED);
            }
            return false;
        } else if (appInfo.getUpdateType() == ZipUpdateTypeEnum.ZIP_APP_TYPE_ONLINE) {
            if (WVMonitorService.getPackageMonitorInterface() != null) {
                WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo == null ? "unknown-0" : appInfo.getNameAndSeq(), url, WVPackageMonitorInterface.FORCE_ONLINE_FAILED);
            }
            return false;
        } else if (appInfo.getUpdateType() != ZipUpdateTypeEnum.ZIP_APP_TYPE_FORCE || appInfo.installedSeq == appInfo.s) {
            return true;
        } else {
            if (WVMonitorService.getPackageMonitorInterface() != null) {
                WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo == null ? "unknown-0" : appInfo.getNameAndSeq(), url, WVPackageMonitorInterface.FORCE_UPDATE_FAILED);
            }
            return false;
        }
    }

    public static boolean canSupportPackageApp(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("http")) {
                url = url.replace("https", "http");
            } else {
                url = "http:" + url;
            }
        }
        if (getResourceResponse(url, getAppInfoByUrl(url)) != null) {
            return true;
        }
        return false;
    }

    public static WVWrapWebResourceResponse getWrapResourceResponse(String url, ZipAppInfo appInfo) {
        WebResourceResponse res = getResourceResponse(url, appInfo);
        if (res != null) {
            return new WVWrapWebResourceResponse(res.getMimeType(), res.getEncoding(), res.getData());
        }
        return null;
    }

    @SuppressLint({"NewApi"})
    public static WebResourceResponse getResourceResponse(String url, ZipAppInfo appInfo) {
        String resPath;
        try {
            long startTime = System.currentTimeMillis();
            url = WVUrlUtil.removeQueryParam(url);
            if (appInfo == null || !isAvailable(url, appInfo)) {
                return null;
            }
            if (!(appInfo.status == ZipAppConstants.ZIP_REMOVED || (resPath = ZipAppUtils.parseUrlSuffix(appInfo, url)) == null)) {
                byte[] data = ZipAppFileManager.getInstance().readZipAppResByte(appInfo, resPath, false);
                long readTime = System.currentTimeMillis();
                String mimeType = WVUrlUtil.getMimeType(url);
                if (data != null && data.length > 0) {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                    if (byteArrayInputStream == null) {
                        if (WVMonitorService.getPackageMonitorInterface() != null) {
                            WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo == null ? "unknown-0" : appInfo.getNameAndSeq(), "create ByteArrayInputStream failed : " + url, "1");
                        }
                        return null;
                    }
                    long verifyTime = 0;
                    if (needCheckSecurity(appInfo.name)) {
                        if (!WVZipSecurityManager.getInstance().isFileSecrity(url, data, ZipAppFileManager.getInstance().getZipResAbsolutePath(appInfo, ZipAppConstants.APP_RES_NAME, false), appInfo.name)) {
                            if (WVMonitorService.getPackageMonitorInterface() != null) {
                                WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo == null ? "unknown-0" : appInfo.getNameAndSeq(), url, "10");
                            }
                            return null;
                        }
                        verifyTime = System.currentTimeMillis();
                    }
                    WVPackageAppCleanup.getInstance().updateAccessTimes(appInfo);
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(TAG, "PackageappforDebug  入口:命中[" + url + "]");
                    }
                    long readUseTime = readTime - startTime;
                    long verifyUseTime = verifyTime == 0 ? 0 : verifyTime - readTime;
                    if (WVMonitorService.getPackageMonitorInterface() != null) {
                        WVMonitorService.getPackageMonitorInterface().commitPackageVisitInfo(appInfo.name, verifyTime == 0 ? "false" : "true", readUseTime + verifyUseTime, 0, readUseTime, verifyUseTime, appInfo.installedSeq);
                        WVMonitorService.getPackageMonitorInterface().commitPackageVisitSuccess(appInfo.name, appInfo.installedSeq);
                    }
                    return new WebResourceResponse(mimeType, ZipAppConstants.DEFAULT_ENCODING, byteArrayInputStream);
                } else if (-1 == url.indexOf("??") && WVMonitorService.getPackageMonitorInterface() != null) {
                    WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo == null ? "unknown-0" : appInfo.getNameAndSeq(), url, "12");
                }
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "PackageappforDebug 入口:未命中[" + url + "]");
            }
            return null;
        } catch (Exception e) {
            if (WVMonitorService.getPackageMonitorInterface() != null) {
                WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo == null ? "unknown-0" : appInfo.getNameAndSeq(), url + " : " + e.getMessage(), "9");
            }
            TaoLog.w(TAG, "PackageappforDebug 入口:访问本地zip资源失败 [" + url + "]" + e.getMessage());
        }
    }

    private static boolean needCheckSecurity(String appName) {
        double sampleRate = WVZipSecurityManager.getInstance().getAppSample(appName);
        double random = Math.random();
        if (random >= sampleRate) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "采样率不满足要求，不对【" + appName + "】进行校验 当前配置采样率为: " + sampleRate + "  获取的随机值为:" + random);
            }
            return false;
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "采样率满足要求，对【" + appName + "】进行校验 当前配置采样率为: " + sampleRate + "  获取的随机值为:" + random);
        }
        return true;
    }
}
