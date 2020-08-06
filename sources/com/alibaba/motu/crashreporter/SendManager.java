package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.alibaba.motu.crashreporter.utils.Base64;
import com.alibaba.motu.crashreporter.utils.GZipUtils;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import com.alibaba.motu.crashreporter.utrestapi.UTRestReqSend;
import com.alibaba.motu.crashreporter.utrestapi.UTRestUrlWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

final class SendManager {
    Configuration mConfiguration;
    Context mContext;
    ReportBuilder mReportBuilder;
    ReportSender mReportSender;
    ReporterContext mReporterContext;
    AtomicBoolean mSending = new AtomicBoolean(false);
    Map<String, CrashReport> mWaitingSend = new ConcurrentHashMap();
    Map<String, ICrashReportSendListener> sendListenerMap = new ConcurrentHashMap();

    interface ReportSender {
        boolean sendReport(CrashReport crashReport);
    }

    public SendManager(Context context, ReporterContext reporterContext, Configuration configuration, ReportBuilder reportBuilder) {
        this.mContext = context;
        this.mReporterContext = reporterContext;
        this.mConfiguration = configuration;
        this.mReportBuilder = reportBuilder;
        this.mReportSender = new DefaultSender(context, reporterContext, configuration);
    }

    public void addListener(ICrashReportSendListener sendListener) {
        if (sendListener != null && StringUtils.isNotBlank(sendListener.getName())) {
            this.sendListenerMap.put(sendListener.getName(), sendListener);
        }
    }

    public void removeListener(ICrashReportSendListener sendListener) {
        if (sendListener != null && StringUtils.isNotBlank(sendListener.getName())) {
            this.sendListenerMap.remove(sendListener.getName());
        }
    }

    public void sendAllReport() {
        sendReports(this.mReportBuilder.listProcessCrashReport());
    }

    public void sendReport(CrashReport crashReport) {
        sendReports(new CrashReport[]{crashReport});
    }

    public void sendReports(CrashReport[] crashReports) {
        if (crashReports != null) {
            for (CrashReport crashReport : crashReports) {
                if (crashReport != null && StringUtils.isNotBlank(crashReport.mReportPath)) {
                    this.mWaitingSend.put(crashReport.mReportPath, crashReport);
                }
            }
            if (!this.mWaitingSend.isEmpty() && this.mSending.compareAndSet(false, true)) {
                new Thread("CrashReportSender") {
                    public void run() {
                        Iterator<Map.Entry<String, CrashReport>> iterator = SendManager.this.mWaitingSend.entrySet().iterator();
                        if (iterator != null) {
                            while (iterator.hasNext()) {
                                Map.Entry<String, CrashReport> entry = iterator.next();
                                if (entry != null) {
                                    try {
                                        CrashReport crashReport = entry.getValue();
                                        if (crashReport == null) {
                                            iterator.remove();
                                        } else if (StringUtils.isBlank(crashReport.mReportPath) || StringUtils.isBlank(crashReport.mReportName) || StringUtils.isBlank(crashReport.mReportType)) {
                                            crashReport.deleteReportFile();
                                            iterator.remove();
                                        } else {
                                            try {
                                                if (crashReport.isComplete()) {
                                                    crashReport.extractPropertys();
                                                    for (ICrashReportSendListener snedListener : SendManager.this.sendListenerMap.values()) {
                                                        try {
                                                            snedListener.beforeSend(crashReport);
                                                        } catch (Exception e) {
                                                            LogUtil.e("beforeSend", e);
                                                        }
                                                    }
                                                    boolean snedSuccess = SendManager.this.mReportSender.sendReport(crashReport);
                                                    for (ICrashReportSendListener snedListener2 : SendManager.this.sendListenerMap.values()) {
                                                        try {
                                                            snedListener2.afterSend(snedSuccess, crashReport);
                                                        } catch (Exception e2) {
                                                            LogUtil.e("beforeSend", e2);
                                                        }
                                                    }
                                                    if (snedSuccess) {
                                                        crashReport.deleteReportFile();
                                                    }
                                                } else if (!crashReport.mCurrentTrigger) {
                                                    crashReport.deleteReportFile();
                                                }
                                            } catch (Exception e3) {
                                                LogUtil.e("send and del crash report.", e3);
                                            }
                                        }
                                    } catch (Exception e4) {
                                        LogUtil.e("remote invalid crash report.", e4);
                                    } catch (Throwable th) {
                                        iterator.remove();
                                        throw th;
                                    }
                                }
                                try {
                                    iterator.remove();
                                } catch (Throwable th2) {
                                    SendManager.this.mSending.set(false);
                                    throw th2;
                                }
                            }
                        }
                        SendManager.this.mSending.set(false);
                    }
                }.start();
            }
        }
    }

    class DefaultSender implements ReportSender {
        Configuration mConfiguration;
        Context mContext;
        ReporterContext mReporterContext;

        public DefaultSender(Context context, ReporterContext reporterContext, Configuration configuration) {
            this.mContext = context;
            this.mReporterContext = reporterContext;
            this.mConfiguration = configuration;
            if (this.mConfiguration.getBoolean(Configuration.enableSecuritySDK, true)) {
                UTRestUrlWrapper.enableSecuritySDK();
                UTRestUrlWrapper.setContext(this.mContext);
            }
        }

        public boolean sendReport(CrashReport crashReport) {
            int eventId;
            if (crashReport == null) {
                return true;
            }
            if (CrashReport.TYPE_JAVA.equals(crashReport.mReportType)) {
                eventId = 1;
            } else if ("native".equals(crashReport.mReportType) || CrashReport.TYPE_ANR.equals(crashReport.mReportType)) {
                eventId = 61006;
            } else {
                LogUtil.i(String.format("unsupport report type:%s path:%s", new Object[]{crashReport.mReportType, crashReport.mReportPath}));
                return true;
            }
            HashMap hashMap = new HashMap();
            crashReport.mPropertys.copyTo(hashMap);
            if (this.mConfiguration.getBoolean(Configuration.enableReportContentCompress, true)) {
                try {
                    return UTRestReqSend.sendLog(this.mContext, hashMap, System.currentTimeMillis(), "-", eventId, "MOTU_REPORTER_SDK_3.0.0_PRIVATE_COMPRESS", Base64.encodeBase64String(GZipUtils.compress(crashReport.getReportContent().getBytes())), "-", (Map<String, String>) null);
                } catch (Exception e) {
                    LogUtil.e("compress crash report content", e);
                }
            }
            return UTRestReqSend.sendLog(this.mContext, hashMap, System.currentTimeMillis(), "-", eventId, "MOTU_REPORTER_SDK_3.0.0_PRIVATE", crashReport.getReportContent(), "-", (Map<String, String>) null);
        }
    }
}
