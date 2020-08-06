package android.taobao.windvane.monitor;

import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import anetwork.channel.util.RequestConstant;
import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import org.json.JSONObject;

public class AppMonitorUtil {
    public static final String JS_ERROR_POINT = "JavaScriptError";
    private static final String MONITOR_MODULE = "WindVane";
    private static final String MONITOR_POINT_PACKAGEAPP = "PackageApp";
    private static final String MONITOR_POINT_PACKAGEAPP_UPDATE_START = "PackageUpdateStart";
    private static final String MONITOR_POINT_PACKAGEAPP_VISIT = "PackageAppVisit";
    private static final String MONITOR_POINT_PACKAGEAPP_VISIT_START = "PackageVisitStart";
    private static final String MONITOR_POINT_PACKAGEQUEUE = "PackageQueue";
    private static final String MONITOR_POINT_PACKAGE_CLEANUP = "PackageCleanUp";
    private static final String MONITOR_POINT_PAGE_EMPTY = "PageEmpty";
    private static final String MONITOR_POINT_PERFORMANCE = "H5";
    private static final String MONITOR_POINT_PERFORMANCE_2 = "H5_2";
    private static final String MONITOR_POINT_PERFORMANCE_3 = "H5_3";
    private static final String MONITOR_POINT_PERFORMANCE_4 = "H5_4";
    private static final String MONITOR_POINT_PERFORMANCE_5 = "H5_5";
    private static final String MONITOR_POINT_SECURITY_WARNING = "SecurityWarning";
    private static final String MONITOR_POINT_STATUS_CODE = "StatusCode";
    private static final String MONITOR_POINT_UPDATE_CONFIG_INFO = "Config";
    private static final String MONITOR_POINT_WEBVIEW_START = "WebViewStart";
    private static final String MONITOR_POINT_WEB_PERFORMANCE_CHECK = "WebPerformanceCheck";
    private static final String MONITOR_POINT_WVUCWEBVIEW = "WVUcwebview";
    public static final String NATIVE_ERROR_POINT = "NativeError";
    public static boolean OFF = false;
    private static final String TAG = "AppMonitorUtil";
    private static boolean isAppMonitorEnabled = false;

    private static Measure createMeasuerWithRange(String name, double min, double max) {
        Measure measure = new Measure(name);
        measure.setRange(Double.valueOf(min), Double.valueOf(max));
        return measure;
    }

    public static void init() {
        try {
            isAppMonitorEnabled = false;
            DimensionSet packageQueueDimensionSet = DimensionSet.create();
            packageQueueDimensionSet.addDimension("isInitialUpdate");
            MeasureSet packageQueueMeasureSet = MeasureSet.create();
            packageQueueMeasureSet.addMeasure(createMeasuerWithRange("updateCount", 0.1d, 900.0d));
            packageQueueMeasureSet.addMeasure(createMeasuerWithRange("successCount", 0.1d, 900.0d));
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_PACKAGEQUEUE, packageQueueMeasureSet, packageQueueDimensionSet);
            DimensionSet packageVisitDimensionSet = DimensionSet.create();
            packageVisitDimensionSet.addDimension("pkgName");
            packageVisitDimensionSet.addDimension("hasVerifyTime");
            MeasureSet packageVisitMeasureSet = MeasureSet.create();
            packageVisitMeasureSet.addMeasure(createMeasuerWithRange("time", ClientTraceData.b.f47a, 60000.0d));
            packageVisitMeasureSet.addMeasure(createMeasuerWithRange("matchTime", ClientTraceData.b.f47a, 60000.0d));
            packageVisitMeasureSet.addMeasure(createMeasuerWithRange("readTime", ClientTraceData.b.f47a, 60000.0d));
            packageVisitMeasureSet.addMeasure(createMeasuerWithRange("verifyTime", ClientTraceData.b.f47a, 60000.0d));
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP_VISIT, packageVisitMeasureSet, packageVisitDimensionSet);
            DimensionSet webPerCheckDimensionSet = DimensionSet.create();
            webPerCheckDimensionSet.addDimension("url");
            webPerCheckDimensionSet.addDimension("version");
            webPerCheckDimensionSet.addDimension("bizcode");
            webPerCheckDimensionSet.addDimension("result");
            MeasureSet webPerCheckMeasureSet = MeasureSet.create();
            webPerCheckMeasureSet.addMeasure(createMeasuerWithRange("score", ClientTraceData.b.f47a, 100.0d));
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_WEB_PERFORMANCE_CHECK, webPerCheckMeasureSet, webPerCheckDimensionSet);
            DimensionSet packageVisitStartDimensionSet = DimensionSet.create();
            packageVisitStartDimensionSet.addDimension("pkgName");
            MeasureSet packageVisitStartMeasureSet = MeasureSet.create();
            packageVisitStartMeasureSet.addMeasure(createMeasuerWithRange("time", ClientTraceData.b.f47a, 4.32E7d));
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP_VISIT_START, packageVisitStartMeasureSet, packageVisitStartDimensionSet);
            DimensionSet packageUpdateStartDimensionSet = DimensionSet.create();
            MeasureSet packageUpdateStartMeasureSet = MeasureSet.create();
            packageUpdateStartMeasureSet.addMeasure(createMeasuerWithRange("startTime", ClientTraceData.b.f47a, 4.32E7d));
            packageUpdateStartMeasureSet.addMeasure(createMeasuerWithRange("endTime", ClientTraceData.b.f47a, 4.32E7d));
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP_UPDATE_START, packageUpdateStartMeasureSet, packageUpdateStartDimensionSet);
            DimensionSet packageClearUpDimensionSet = DimensionSet.create();
            packageClearUpDimensionSet.addDimension("cleanCause");
            MeasureSet packageClearUpMeasureSet = MeasureSet.create();
            packageClearUpMeasureSet.addMeasure("beforeDelSpace");
            packageClearUpMeasureSet.addMeasure("expectedNum");
            packageClearUpMeasureSet.addMeasure("installedNum");
            packageClearUpMeasureSet.addMeasure("willDeleteCount");
            packageClearUpMeasureSet.addMeasure("customRadio");
            packageClearUpMeasureSet.addMeasure("noCacheCount");
            packageClearUpMeasureSet.addMeasure("normalCount");
            packageClearUpMeasureSet.addMeasure("noCacheRatio");
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_PACKAGE_CLEANUP, packageClearUpMeasureSet, packageClearUpDimensionSet);
            DimensionSet webviewSatrtDimensionSet = DimensionSet.create();
            webviewSatrtDimensionSet.addDimension("url");
            MeasureSet webviewSatrtMeasureSet = MeasureSet.create();
            webviewSatrtMeasureSet.addMeasure(createMeasuerWithRange("time", ClientTraceData.b.f47a, 1800000.0d));
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_WEBVIEW_START, webviewSatrtMeasureSet, webviewSatrtDimensionSet);
            DimensionSet dimensionSetConfig = DimensionSet.create();
            dimensionSetConfig.addDimension("name");
            dimensionSetConfig.addDimension("from");
            dimensionSetConfig.addDimension(VPMConstants.DIMENSION_ISSUCCESS);
            MeasureSet measureSetConfig = MeasureSet.create();
            measureSetConfig.addMeasure(createMeasuerWithRange("updateTime", ClientTraceData.b.f47a, 600000.0d));
            measureSetConfig.addMeasure(createMeasuerWithRange("updateCount", ClientTraceData.b.f47a, 1000.0d));
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_UPDATE_CONFIG_INFO, measureSetConfig, dimensionSetConfig);
            DimensionSet dimensionSetStatus = DimensionSet.create();
            dimensionSetStatus.addDimension("url");
            dimensionSetStatus.addDimension("isHTML");
            dimensionSetStatus.addDimension("statusCode");
            dimensionSetStatus.addDimension("referrer");
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_STATUS_CODE, MeasureSet.create(), dimensionSetStatus);
            DimensionSet dimensionSetPackageApp = DimensionSet.create();
            dimensionSetPackageApp.addDimension("appName");
            dimensionSetPackageApp.addDimension("version");
            dimensionSetPackageApp.addDimension("seq");
            dimensionSetPackageApp.addDimension(RequestConstant.ENV_ONLINE);
            dimensionSetPackageApp.addDimension("networkSupport");
            MeasureSet measureSetPackageApp = MeasureSet.create();
            measureSetPackageApp.addMeasure(createMeasuerWithRange("updateTime", ClientTraceData.b.f47a, 600000.0d));
            measureSetPackageApp.addMeasure(createMeasuerWithRange("downloadTime", ClientTraceData.b.f47a, 600000.0d));
            measureSetPackageApp.addMeasure(createMeasuerWithRange("publishTime", ClientTraceData.b.f47a, 6.048E8d));
            measureSetPackageApp.addMeasure(createMeasuerWithRange("notificationTime", ClientTraceData.b.f47a, 6.048E8d));
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP, measureSetPackageApp, dimensionSetPackageApp);
            DimensionSet dimensionSetPerformance = DimensionSet.create();
            dimensionSetPerformance.addDimension("url");
            dimensionSetPerformance.addDimension("via");
            dimensionSetPerformance.addDimension("pkgName");
            dimensionSetPerformance.addDimension("pkgVersion");
            dimensionSetPerformance.addDimension("pkgSeq");
            dimensionSetPerformance.addDimension("fromType");
            dimensionSetPerformance.addDimension("protocolType");
            dimensionSetPerformance.addDimension("hasInit");
            dimensionSetPerformance.addDimension("isFinished");
            dimensionSetPerformance.addDimension("statusCode");
            dimensionSetPerformance.addDimension("verifyError");
            MeasureSet measureSetPerformance = MeasureSet.create();
            measureSetPerformance.addMeasure(createMeasuerWithRange("verifyResTime", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("verifyTime", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("verifyCacheSize", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("allVerifyTime", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("initTime", ClientTraceData.b.f47a, 60000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("tcp", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("ssl", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("firstByte", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("domLoad", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("pageLoad", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("c", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("dc", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("dcl", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("dl", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("dns", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("lee", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("req", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("rpe", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("rps", ClientTraceData.b.f47a, 600000.0d));
            measureSetPerformance.addMeasure(createMeasuerWithRange("scs", ClientTraceData.b.f47a, 600000.0d));
            AppMonitor.register(MONITOR_MODULE, "H5", measureSetPerformance, dimensionSetPerformance);
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_PERFORMANCE_2, measureSetPerformance, dimensionSetPerformance);
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_PERFORMANCE_3, measureSetPerformance, dimensionSetPerformance);
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_PERFORMANCE_4, measureSetPerformance, dimensionSetPerformance);
            AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_PERFORMANCE_5, measureSetPerformance, dimensionSetPerformance);
            isAppMonitorEnabled = true;
        } catch (Throwable th) {
            TaoLog.i(TAG, "AppMonitor not found");
        }
    }

    public static void commitFail(String monitorPoint, int errorCode, String errorMsg, String url) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            AppMonitor.Alarm.commitFail(MONITOR_MODULE, monitorPoint, url, Integer.toString(errorCode), errorMsg);
        }
    }

    public static void commitConifgUpdateInfo(String name, int from, long updateTime, int isSuccess, int updateCount) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet demensionSet = DimensionValueSet.create();
                demensionSet.setValue("name", name);
                demensionSet.setValue("from", Integer.toString(from));
                demensionSet.setValue(VPMConstants.DIMENSION_ISSUCCESS, Integer.toString(isSuccess));
                MeasureValueSet measureValueSet = MeasureValueSet.create();
                measureValueSet.setValue("updateTime", (double) updateTime);
                measureValueSet.setValue("updateCount", (double) updateCount);
                AppMonitor.Stat.commit(MONITOR_MODULE, MONITOR_POINT_UPDATE_CONFIG_INFO, demensionSet, measureValueSet);
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor exception");
            }
        }
    }

    public static void commitConifgUpdateError(String configName, int errorCode, String errorMsg) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            AppMonitor.Alarm.commitFail(MONITOR_MODULE, MONITOR_POINT_UPDATE_CONFIG_INFO, configName, Integer.toString(errorCode), errorMsg);
        }
    }

    public static void commitConifgUpdateSuccess(String message) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            AppMonitor.Alarm.commitSuccess(MONITOR_MODULE, MONITOR_POINT_UPDATE_CONFIG_INFO, message);
        }
    }

    public static void commitPackageAppUpdateError(String errorCode, String errorMsg, String name) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            AppMonitor.Alarm.commitFail(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP, name, errorCode, errorMsg);
        }
    }

    public static void commitUCWebviewError(String errorCode, String errorMsg, String name) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            AppMonitor.Alarm.commitFail(MONITOR_MODULE, MONITOR_POINT_WVUCWEBVIEW, name, errorCode, errorMsg);
        }
    }

    public static void commitPackageAppUpdateSuccess(String name) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            AppMonitor.Alarm.commitSuccess(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP, name);
        }
    }

    public static void commitPackageAppVisitError(String appName, String errorInfo, String errorCode) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            AppMonitor.Alarm.commitFail(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP_VISIT, appName, errorCode, errorInfo);
        }
    }

    public static void commitSecurityWarning(String url, String from) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            AppMonitor.Alarm.commitFail(MONITOR_MODULE, MONITOR_POINT_SECURITY_WARNING, from, "101", url);
        }
    }

    public static void commitEmptyPage(String url, String type) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            AppMonitor.Alarm.commitFail(MONITOR_MODULE, MONITOR_POINT_PAGE_EMPTY, type, "101", url);
        }
    }

    public static void commitPackageAppUpdateInfo(ZipAppInfo info, String online, String isWifi, long updateTime, long downloadTime, long publishTime, long updateUseTime) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet demensionSet = DimensionValueSet.create();
                demensionSet.setValue("appName", info.name);
                demensionSet.setValue("version", info.v);
                demensionSet.setValue("seq", String.valueOf(info.s));
                demensionSet.setValue(RequestConstant.ENV_ONLINE, online);
                demensionSet.setValue("networkSupport", isWifi);
                MeasureValueSet measureValueSet = MeasureValueSet.create();
                measureValueSet.setValue("updateTime", (double) updateTime);
                measureValueSet.setValue("downloadTime", (double) downloadTime);
                measureValueSet.setValue("publishTime", (double) publishTime);
                measureValueSet.setValue("notificationTime", (double) updateUseTime);
                AppMonitor.Stat.commit(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP, demensionSet, measureValueSet);
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor exception");
            }
        }
    }

    public static void commitStartTimeInfo(String url, long useTime) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet demensionSet = DimensionValueSet.create();
                if (!TextUtils.isEmpty(url)) {
                    demensionSet.setValue("url", WVUrlUtil.removeQueryParam(url));
                }
                MeasureValueSet measureValueSet = MeasureValueSet.create();
                measureValueSet.setValue("time", (double) useTime);
                TaoLog.i(TAG, "Webview start after : " + useTime + "ms, url : " + WVUrlUtil.removeQueryParam(url));
                AppMonitor.Stat.commit(MONITOR_MODULE, MONITOR_POINT_WEBVIEW_START, demensionSet, measureValueSet);
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor commitStartTimeInfo exception");
            }
        }
    }

    public static void commitPackageQueueInfo(String isInitialUpdate, long updateCount, long succesCount) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet demensionSet = DimensionValueSet.create();
                if (!TextUtils.isEmpty(isInitialUpdate)) {
                    demensionSet.setValue("isInitialUpdate", isInitialUpdate);
                }
                MeasureValueSet measureValueSet = MeasureValueSet.create();
                measureValueSet.setValue("updateCount", (double) updateCount);
                measureValueSet.setValue("successCount", (double) succesCount);
                AppMonitor.Stat.commit(MONITOR_MODULE, MONITOR_POINT_PACKAGEQUEUE, demensionSet, measureValueSet);
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor commitPackageQueueInfo exception");
            }
        }
    }

    public static void commitPackageVisitInfo(String pkgName, String hasVerifyTime, long time, long matchTime, long readTime, long verifyTime, long seq) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet demensionSet = DimensionValueSet.create();
                demensionSet.setValue("pkgName", pkgName);
                demensionSet.setValue("hasVerifyTime", hasVerifyTime);
                MeasureValueSet measureValueSet = MeasureValueSet.create();
                measureValueSet.setValue("time", (double) time);
                measureValueSet.setValue("matchTime", (double) matchTime);
                measureValueSet.setValue("readTime", (double) readTime);
                measureValueSet.setValue("verifyTime", (double) verifyTime);
                AppMonitor.Stat.commit(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP_VISIT, demensionSet, measureValueSet);
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor commitPackageVisitInfo exception");
            }
        }
    }

    public static void commitPackageVisitSuccess(String pkgName, long seq) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                AppMonitor.Alarm.commitSuccess(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP_VISIT, pkgName + "-" + seq);
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor commitPackageVisitSuccess exception");
            }
        }
    }

    public static void commitPackageVisitStartInfo(String pkgName, long time) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet demensionSet = DimensionValueSet.create();
                demensionSet.setValue("pkgName", pkgName);
                MeasureValueSet measureValueSet = MeasureValueSet.create();
                measureValueSet.setValue("time", (double) time);
                AppMonitor.Stat.commit(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP_VISIT_START, demensionSet, measureValueSet);
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor commitPackageVisitStartInfo exception");
            }
        }
    }

    public static void commitPackageUpdateStartInfo(long startTime, long endTime) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet demensionSet = DimensionValueSet.create();
                MeasureValueSet measureValueSet = MeasureValueSet.create();
                measureValueSet.setValue("startTime", (double) startTime);
                measureValueSet.setValue("endTime", (double) endTime);
                AppMonitor.Stat.commit(MONITOR_MODULE, MONITOR_POINT_PACKAGEAPP_UPDATE_START, demensionSet, measureValueSet);
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor commitPackageUpdateStartInfo exception");
            }
        }
    }

    public static void commitStatusCode(String url, String referrer, String statusCode, String isHTML) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet demensionSet = DimensionValueSet.create();
                demensionSet.setValue("url", url);
                demensionSet.setValue("isHTML", isHTML);
                demensionSet.setValue("statusCode", statusCode);
                demensionSet.setValue("referrer", referrer);
                AppMonitor.Stat.commit(MONITOR_MODULE, MONITOR_POINT_STATUS_CODE, demensionSet, MeasureValueSet.create());
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor commitStatusCode exception");
            }
        }
    }

    public static void commitWebPerfCheckInfo(String url, long score, String version, String bizcode, String result) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet demensionSet = DimensionValueSet.create();
                demensionSet.setValue("url", url);
                demensionSet.setValue("version", version);
                demensionSet.setValue("bizcode", bizcode);
                demensionSet.setValue("result", result);
                MeasureValueSet measureValueSet = MeasureValueSet.create();
                measureValueSet.setValue("score", (double) score);
                AppMonitor.Stat.commit(MONITOR_MODULE, MONITOR_POINT_WEB_PERFORMANCE_CHECK, demensionSet, measureValueSet);
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor commitPackageUpdateStartInfo exception");
            }
        }
    }

    public static void commitPackageClearUpInfo(long beforeDelSpace, int expectedNum, int installedNum, int willDeleteCount, float customRadio, int noCacheCount, int normalCount, float noCacheRatio, int cleanCause) {
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet demensionSet = DimensionValueSet.create();
                demensionSet.setValue("cleanCause", String.valueOf(cleanCause));
                MeasureValueSet measureValueSet = MeasureValueSet.create();
                measureValueSet.setValue("beforeDelSpace", (double) beforeDelSpace);
                measureValueSet.setValue("expectedNum", (double) expectedNum);
                measureValueSet.setValue("installedNum", (double) installedNum);
                measureValueSet.setValue("willDeleteCount", (double) willDeleteCount);
                measureValueSet.setValue("customRadio", (double) customRadio);
                measureValueSet.setValue("noCacheCount", (double) noCacheCount);
                measureValueSet.setValue("normalCount", (double) normalCount);
                measureValueSet.setValue("noCacheRatio", (double) noCacheRatio);
                AppMonitor.Stat.commit(MONITOR_MODULE, MONITOR_POINT_PACKAGE_CLEANUP, demensionSet, measureValueSet);
            } catch (Exception e) {
                TaoLog.i(TAG, "AppMonitor commitPackageClearUpInfo exception");
            }
        }
    }

    public static void commitPerformanceInfo(WVMonitorData data) {
        String monitorPointH5;
        if (WVCommonConfig.commonConfig.monitorStatus != 0 && isAppMonitorEnabled) {
            try {
                DimensionValueSet dimensionSet = DimensionValueSet.create();
                dimensionSet.setValue("url", data.url);
                dimensionSet.setValue("via", data.args.via);
                dimensionSet.setValue("pkgName", data.stat.packageAppName);
                dimensionSet.setValue("pkgVersion", data.stat.packageAppVersion);
                dimensionSet.setValue("pkgSeq", data.stat.appSeq);
                dimensionSet.setValue("fromType", String.valueOf(data.stat.fromType));
                dimensionSet.setValue("protocolType", "");
                dimensionSet.setValue("hasInit", data.isInit ? "1" : "0");
                dimensionSet.setValue("isFinished", String.valueOf(data.stat.finish));
                dimensionSet.setValue("statusCode", String.valueOf(data.args.statusCode));
                dimensionSet.setValue("verifyError", String.valueOf(data.stat.verifyError));
                MeasureValueSet measureValueSet = MeasureValueSet.create();
                measureValueSet.setValue("verifyResTime", (double) data.stat.verifyResTime);
                measureValueSet.setValue("verifyTime", (double) data.stat.verifyTime);
                measureValueSet.setValue("verifyCacheSize", (double) data.stat.verifyCacheSize);
                measureValueSet.setValue("allVerifyTime", (double) data.stat.allVerifyTime);
                measureValueSet.setValue("initTime", (double) data.init);
                measureValueSet.setValue("tcp", (double) ClientTraceData.b.f47a);
                measureValueSet.setValue("ssl", (double) ClientTraceData.b.f47a);
                measureValueSet.setValue("firstByte", (double) data.stat.firstByteTime);
                measureValueSet.setValue("domLoad", (double) data.stat.onDomLoad);
                measureValueSet.setValue("pageLoad", (double) data.stat.onLoad);
                JSONObject performanceData = null;
                try {
                    if (!TextUtils.isEmpty(data.performanceInfo)) {
                        String performanceString = data.performanceInfo;
                        if (performanceString.startsWith("\"") && performanceString.endsWith("\"")) {
                            performanceString = data.performanceInfo.substring(1, performanceString.length() - 1);
                        }
                        performanceData = new JSONObject(performanceString);
                    }
                } catch (Exception e) {
                }
                if (performanceData != null) {
                    measureValueSet.setValue("c", (double) performanceData.optInt("c", 0));
                    measureValueSet.setValue("dc", (double) performanceData.optInt("dc", 0));
                    measureValueSet.setValue("dcl", (double) performanceData.optInt("dcl", 0));
                    measureValueSet.setValue("dl", (double) performanceData.optInt("dl", 0));
                    measureValueSet.setValue("dns", (double) performanceData.optInt("dns", 0));
                    measureValueSet.setValue("lee", (double) performanceData.optInt("lee", 0));
                    measureValueSet.setValue("req", (double) performanceData.optInt("req", 0));
                    measureValueSet.setValue("rpe", (double) performanceData.optInt("rpe", 0));
                    measureValueSet.setValue("rps", (double) performanceData.optInt("rps", 0));
                    measureValueSet.setValue("scs", (double) performanceData.optInt("scs", 0));
                }
                switch (data.wvAppMonitor) {
                    case 1:
                        monitorPointH5 = "H5";
                        break;
                    case 2:
                        monitorPointH5 = MONITOR_POINT_PERFORMANCE_2;
                        break;
                    case 3:
                        monitorPointH5 = MONITOR_POINT_PERFORMANCE_3;
                        break;
                    case 4:
                        monitorPointH5 = MONITOR_POINT_PERFORMANCE_4;
                        break;
                    case 5:
                        monitorPointH5 = MONITOR_POINT_PERFORMANCE_5;
                        break;
                    default:
                        monitorPointH5 = "H5";
                        break;
                }
                AppMonitor.Stat.commit(MONITOR_MODULE, monitorPointH5, dimensionSet, measureValueSet);
                if (TaoLog.getLogStatus() && data != null && data.stat != null) {
                    TaoLog.i(TAG, "PERFORMANCE : " + data.url + ": pageLoad : " + data.stat.onLoad + " fromType : " + data.stat.fromType);
                }
            } catch (Exception e2) {
                TaoLog.i(TAG, "AppMonitor exception");
            }
        }
    }
}
