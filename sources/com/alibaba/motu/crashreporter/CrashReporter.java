package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.alibaba.motu.crashreporter.CatcherManager;
import com.alibaba.motu.crashreporter.Propertys;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import com.taobao.orange.model.NameSpaceDO;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CrashReporter {
    static final CrashReporter INSTANCE = new CrashReporter();
    public static final String _BUILD = "";
    public static final String _JAVA_VERSION = "";
    public static final String _MAGIC = "CrashSDK";
    public static final String _NATIVE_VERSION = "160509105620";
    public static final String _TARGET = "beta";
    public static final String _VERSION = "1.0.0.0";
    AtomicBoolean changing = new AtomicBoolean(false);
    volatile boolean enabled = false;
    volatile boolean initSuccess = false;
    AtomicBoolean initing = new AtomicBoolean(false);
    CatcherManager mCatcherManager;
    Configuration mConfiguration;
    Context mContext;
    LabFeatureManager mLabFeatureManager;
    String mProcessName;
    ReportBuilder mReportBuilder;
    ReporterContext mReporterContext;
    RunningStateMonitor mRunningStateMonitor;
    SendManager mSendManager;
    StorageManager mStorageManager;
    AtomicBoolean scaning = new AtomicBoolean(false);
    AtomicBoolean sending = new AtomicBoolean(false);

    public static CrashReporter getInstance() {
        return INSTANCE;
    }

    private CrashReporter() {
    }

    public void initialize(Context context, String appId, String appKey, String appVersion, String channel, Configuration configuration) {
        if (this.initing.compareAndSet(false, true)) {
            try {
                long startupTime = System.currentTimeMillis();
                if (context == null) {
                    throw new NullPointerException("context");
                } else if (StringUtils.isBlank(appId)) {
                    throw new IllegalArgumentException("appId can't empty");
                } else if (StringUtils.isBlank(appKey)) {
                    throw new IllegalArgumentException("appKey");
                } else {
                    this.mContext = context.getApplicationContext();
                    if (this.mContext == null) {
                        this.mContext = context;
                    }
                    if (configuration == null) {
                        this.mConfiguration = new Configuration();
                    } else {
                        this.mConfiguration = configuration;
                    }
                    long start = System.currentTimeMillis();
                    this.mReporterContext = new ReporterContext(this.mContext);
                    this.mReporterContext.setProperty(new Propertys.Property(Constants.STARTUP_TIME, String.valueOf(startupTime)));
                    this.mReporterContext.setProperty(new Propertys.Property(Constants.APP_ID, appId, true));
                    this.mReporterContext.setProperty(new Propertys.Property(Constants.APP_KEY, appKey, true));
                    this.mReporterContext.setProperty(new Propertys.Property(Constants.APP_VERSION, StringUtils.defaultString(appVersion, NameSpaceDO.LEVEL_DEFAULT)));
                    this.mReporterContext.setProperty(new Propertys.Property(Constants.CHANNEL, channel, true));
                    this.mProcessName = Utils.getMyProcessNameByCmdline();
                    if (StringUtils.isBlank(this.mProcessName)) {
                        this.mProcessName = Utils.getMyProcessNameByAppProcessInfo(context);
                    }
                    this.mProcessName = StringUtils.defaultString(this.mProcessName, NameSpaceDO.LEVEL_DEFAULT);
                    this.mReporterContext.setProperty(new Propertys.Property(Constants.PROCESS_NAME, this.mProcessName, true));
                    LogUtil.d("CrashSDK ReporterContext initialize complete elapsed time:" + (System.currentTimeMillis() - start) + "ms.");
                    long start2 = System.currentTimeMillis();
                    this.mStorageManager = new StorageManager(context, this.mProcessName);
                    LogUtil.d("CrashSDK StorageManager initialize complete elapsed time:" + (System.currentTimeMillis() - start2) + "ms.");
                    long start3 = System.currentTimeMillis();
                    this.mReportBuilder = new ReportBuilder(this.mContext, this.mReporterContext, this.mConfiguration, this.mStorageManager);
                    LogUtil.d("CrashSDK ReportBuilder initialize complete elapsed time:" + (System.currentTimeMillis() - start3) + "ms.");
                    long start4 = System.currentTimeMillis();
                    this.mSendManager = new SendManager(this.mContext, this.mReporterContext, this.mConfiguration, this.mReportBuilder);
                    LogUtil.d("CrashSDK SendManager initialize complete elapsed time:" + (System.currentTimeMillis() - start4) + "ms.");
                    long start5 = System.currentTimeMillis();
                    this.mRunningStateMonitor = new RunningStateMonitor(context, appId, appKey, appVersion, this.mProcessName, startupTime, this.mStorageManager);
                    this.mRunningStateMonitor.analyzeStartupState(new DefaultStartupStateAnalyzeCallback());
                    LogUtil.d("CrashSDK RunningStateMonitor initialize complete elapsed time:" + (System.currentTimeMillis() - start5) + "ms.");
                    long start6 = System.currentTimeMillis();
                    this.mCatcherManager = new CatcherManager(context, this.mProcessName, this.mReporterContext, this.mConfiguration, this.mStorageManager, this.mReportBuilder, this.mSendManager);
                    LogUtil.d("CrashSDK CatcherManager initialize complete elapsed time:" + (System.currentTimeMillis() - start6) + "ms.");
                    long start7 = System.currentTimeMillis();
                    this.mLabFeatureManager = new LabFeatureManager(this.mContext, this.mConfiguration, this.mCatcherManager);
                    LogUtil.d("CrashSDK LabFeatureManager initialize complete elapsed time:" + (System.currentTimeMillis() - start7) + "ms.");
                    LogUtil.d("CrashSDK initialize complete elapsed time:" + (System.currentTimeMillis() - startupTime) + "ms.");
                    this.initSuccess = true;
                    long start8 = System.currentTimeMillis();
                    scanAll();
                    sendAll();
                    LogUtil.d("CrashSDK doBefore complete elapsed time:" + (System.currentTimeMillis() - start8) + "ms.");
                }
            } catch (Exception e) {
                LogUtil.e("initialize", e);
            }
        }
    }

    class DefaultStartupStateAnalyzeCallback {
        DefaultStartupStateAnalyzeCallback() {
        }

        public void onComplete(int startupState) {
            int flagFast = startupState & 1;
            int flagMany = startupState & 16;
            if (flagFast != 1 || flagMany != 16) {
                if (flagFast == 1 || flagMany != 16) {
                }
            } else if (Utils.isServiceProcess(CrashReporter.this.mProcessName).booleanValue()) {
                throw new RuntimeException("service process name:" + CrashReporter.this.mProcessName + " launching too fast and too many");
            } else if (!Utils.isUIProcess(CrashReporter.this.mContext, CrashReporter.this.mProcessName).booleanValue()) {
            } else {
                if (Utils.isBackgroundRunning(CrashReporter.this.mContext).booleanValue() || CrashReporter.this.mConfiguration.getBoolean(Configuration.enableUIProcessSafeGuard, false)) {
                    throw new RuntimeException("ui process name:" + CrashReporter.this.mProcessName + " launching too fast and too many");
                }
                Utils.stopService(CrashReporter.this.mContext);
            }
        }
    }

    public void enable() {
        if (this.initSuccess && !this.enabled && this.changing.compareAndSet(false, true)) {
            try {
                long start = System.currentTimeMillis();
                this.mCatcherManager.enable();
                this.mLabFeatureManager.enable();
                this.enabled = true;
                LogUtil.d("CrashSDK enable complete elapsed time:" + (System.currentTimeMillis() - start) + "ms.");
            } finally {
                this.changing.set(false);
            }
        }
    }

    public void disable() {
        if (this.initSuccess && this.enabled && this.changing.compareAndSet(false, true)) {
            try {
                long start = System.currentTimeMillis();
                this.mCatcherManager.disable();
                this.mLabFeatureManager.disable();
                this.enabled = false;
                LogUtil.d("CrashSDK disable complete elapsed time:" + (System.currentTimeMillis() - start) + "ms.");
            } finally {
                this.changing.set(false);
            }
        }
    }

    public void scanAll() {
        if (this.initSuccess && this.scaning.compareAndSet(false, true)) {
            try {
                this.mCatcherManager.doScan();
            } catch (Exception e) {
                LogUtil.e("scan all", e);
            } finally {
                this.scaning.set(false);
            }
        }
    }

    public void sendAll() {
        if (this.initSuccess && this.sending.compareAndSet(false, true)) {
            try {
                this.mSendManager.sendAllReport();
            } catch (Exception e) {
                LogUtil.e("send all", e);
            } finally {
                this.sending.set(false);
            }
        }
    }

    public void setProperty(Propertys.Property property) {
        if (this.initSuccess) {
            this.mReporterContext.setProperty(property);
        }
    }

    public String getProperty(String name) {
        if (this.initSuccess) {
            return this.mReporterContext.getProperty(name);
        }
        return null;
    }

    public String getPropertyAndSet(String name) {
        if (this.initSuccess) {
            return this.mReporterContext.getPropertyAndSet(name);
        }
        return null;
    }

    public void refreshAppVersion(String appVersion) {
        if (this.initSuccess && StringUtils.isNotBlank(appVersion)) {
            setProperty(new Propertys.Property(Constants.APP_VERSION, appVersion));
            this.mCatcherManager.refreshNativeInfo();
        }
    }

    public void addUncaughtExceptionLinster(CatcherManager.UncaughtExceptionLinster linster) {
        if (this.initSuccess) {
            this.mCatcherManager.addUncaughtExceptionLinster(linster);
        }
    }

    public List<CatcherManager.UncaughtExceptionLinster> getAllUncaughtExceptionLinster() {
        if (this.initSuccess) {
            return this.mCatcherManager.getAllUncaughtExceptionLinster();
        }
        return null;
    }

    public void addSendLinster(ICrashReportSendListener sendListener) {
        if (this.initSuccess) {
            this.mSendManager.addListener(sendListener);
        }
    }

    public void removeSendLinster(ICrashReportSendListener sendListener) {
        if (this.initSuccess) {
            this.mSendManager.removeListener(sendListener);
        }
    }
}
