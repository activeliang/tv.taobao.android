package com.alibaba.motu.crashreporter;

import android.content.Context;
import android.os.Build;
import com.alibaba.motu.crashreporter.Propertys;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import java.io.File;

public final class CrashReport {
    public static final String TYPE_ANR = "anr";
    public static final String TYPE_JAVA = "java";
    public static final String TYPE_NATIVE = "native";
    Context mContext;
    boolean mCurrentTrigger;
    Propertys mPropertys = new Propertys();
    String mReportContent;
    File mReportFile;
    String mReportName;
    String mReportPath;
    String mReportType;
    ReporterContext mReporterContext;

    public static String buildReportName(String utdid, String appKey, String appVersion, long timestamp, String tag, String reportType) {
        return "CrashSDK_1.0.0.0__df_df_df_" + appKey + "_" + replaceUnderscore(appVersion) + "_" + String.valueOf(timestamp) + "_" + Utils.getGMT8Time(timestamp) + "_" + StringUtils.defaultString(replaceUnderscore(tag), "df") + "_" + reportType + ".log";
    }

    public static String replaceUnderscore(String str) {
        return str != null ? str.replace("_", "&#95;") : "";
    }

    public static String revertUnderscore(String str) {
        return str != null ? str.replace("&#95;", "_") : "";
    }

    public static String[] parseReportName(String reportName) {
        if (StringUtils.isNotBlank(reportName) && reportName.endsWith(".log")) {
            String[] parts = reportName.split("_");
            if (parts.length == 12) {
                parts[11] = parts[11].replace(".log", "");
                if (TYPE_JAVA.equals(parts[11]) || "native".equals(parts[11]) || TYPE_ANR.equals(parts[11])) {
                    return parts;
                }
            }
        }
        return null;
    }

    private CrashReport() {
    }

    public static CrashReport buildCrashReport(Context context, File reportFile, ReporterContext reporterContext, boolean currentTrigger) {
        String reportName = reportFile.getName();
        String reportPath = reportFile.getAbsolutePath();
        String[] parts = parseReportName(reportName);
        if (parts == null) {
            return null;
        }
        CrashReport crashReport = new CrashReport();
        crashReport.mContext = context;
        crashReport.mReporterContext = reporterContext;
        crashReport.mReportFile = reportFile;
        crashReport.mReportName = reportName;
        crashReport.mReportPath = reportPath;
        crashReport.mPropertys.add(new Propertys.Property(Constants.CRASH_SDK_NAME, parts[0]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.CRASH_SDK_VERSION, parts[1]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.CRASH_SDK_BUILD, parts[2]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.BRAND, parts[3]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.DEVICE_MODEL, parts[4]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.UTDID, parts[5]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.APP_KEY, parts[6]));
        String appVersion = revertUnderscore(parts[7]);
        try {
            String currentAppVersion = Utils.getContextAppVersion(context);
            if (appVersion != null && currentAppVersion != null && currentAppVersion.length() > 0 && !appVersion.equals(currentAppVersion)) {
                appVersion = currentAppVersion;
                LogUtil.d("crashreporter update appversion:" + appVersion);
            }
        } catch (Exception e) {
        }
        crashReport.mPropertys.add(new Propertys.Property(Constants.APP_VERSION, appVersion));
        crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_CREATE_TIMESTAMP, parts[8]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_CREATE_TIME, parts[9]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_TAG, revertUnderscore(parts[10])));
        crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_TYPE, parts[11]));
        crashReport.mReportType = parts[11];
        crashReport.mCurrentTrigger = currentTrigger;
        return crashReport;
    }

    public void extractPropertys() {
        extractPropertys(this.mReporterContext);
    }

    public void extractPropertys(ReporterContext reporterContext) {
        this.mPropertys.add(new Propertys.Property(Constants.USERNICK, reporterContext.getPropertyAndSet(Constants.USERNICK)));
        this.mPropertys.add(new Propertys.Property(Constants.BRAND, Build.BOARD));
        this.mPropertys.add(new Propertys.Property(Constants.DEVICE_MODEL, Build.MODEL));
        this.mPropertys.add(new Propertys.Property(Constants.UTDID, reporterContext.getPropertyAndSet(Constants.UTDID)));
        this.mPropertys.add(new Propertys.Property("IMEI", reporterContext.getPropertyAndSet("IMEI")));
        this.mPropertys.add(new Propertys.Property("IMSI", reporterContext.getPropertyAndSet("IMSI")));
        this.mPropertys.add(new Propertys.Property(Constants.DEVICE_ID, reporterContext.getPropertyAndSet(Constants.DEVICE_ID)));
        this.mPropertys.add(new Propertys.Property(Constants.CHANNEL, reporterContext.getProperty(Constants.CHANNEL)));
        this.mPropertys.add(new Propertys.Property(Constants.APP_ID, reporterContext.getProperty(Constants.APP_ID)));
        if (this.mCurrentTrigger) {
        }
    }

    public void deleteReportFile() {
        if (this.mReportFile != null) {
            this.mReportFile.delete();
        }
    }

    public String getProperty(String name) {
        return this.mPropertys.getValue(name);
    }

    public String getReportContent() {
        if (!StringUtils.isBlank(this.mReportContent)) {
            return this.mReportContent;
        }
        String readFully = Utils.readFully(this.mReportFile);
        this.mReportContent = readFully;
        return readFully;
    }

    public boolean isComplete() {
        if (StringUtils.isBlank(this.mReportContent)) {
            this.mReportContent = getReportContent();
        }
        if (StringUtils.isNotBlank(this.mReportContent)) {
            return this.mReportContent.trim().contains("log end:");
        }
        return false;
    }
}
