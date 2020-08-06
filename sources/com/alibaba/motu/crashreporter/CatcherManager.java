package com.alibaba.motu.crashreporter;

import android.content.Context;
import android.os.Build;
import com.alibaba.motu.crashreporter.Utils;
import com.alibaba.motu.crashreporter.ignores.NonSystemThreadIgnore;
import com.alibaba.motu.crashreporter.ignores.UncaughtExceptionIgnore;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import com.alibaba.motu.crashreporter.utrestapi.UTReflect;
import com.uc.crashsdk.JNIBridge;
import com.yunos.tvtaobao.uuid.client.GZipUtil;
import java.io.File;
import java.io.FileFilter;
import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

final class CatcherManager {
    ANRCatcher mANRCatcher;
    Configuration mConfiguration;
    Context mContext;
    String mProcessName;
    ReportBuilder mReportBuilder;
    ReporterContext mReporterContext;
    SendManager mSendManager;
    StorageManager mStorageManager;
    UCNativeExceptionCatcher mUCNativeExceptionCatcher;
    UncaughtExceptionCatcher mUncaughtExceptionCatcher;

    interface UncaughtExceptionLinster {
        Map<String, Object> onUncaughtException(Thread thread, Throwable th);

        boolean originalEquals(Object obj);
    }

    public CatcherManager(Context context, String processName, ReporterContext reporterContext, Configuration configuration, StorageManager storageManager, ReportBuilder reportBuilder, SendManager sendManager) {
        this.mReporterContext = reporterContext;
        this.mContext = context;
        this.mProcessName = processName;
        this.mConfiguration = configuration;
        this.mStorageManager = storageManager;
        this.mReportBuilder = reportBuilder;
        this.mSendManager = sendManager;
        if (configuration.getBoolean(Configuration.enableUncaughtExceptionCatch, true)) {
            long start = System.currentTimeMillis();
            this.mUncaughtExceptionCatcher = new UncaughtExceptionCatcher();
            this.mUncaughtExceptionCatcher.addIgnore(new NonSystemThreadIgnore());
            LogUtil.d("CrashSDK UncaughtExceptionCatcher initialize complete elapsed time:" + (System.currentTimeMillis() - start) + "ms.");
        }
        if (configuration.getBoolean(Configuration.enableNativeExceptionCatch, true)) {
            long start2 = System.currentTimeMillis();
            this.mUCNativeExceptionCatcher = new UCNativeExceptionCatcher();
            LogUtil.d("CrashSDK UCNativeExceptionCatcher initialize complete elapsed time:" + (System.currentTimeMillis() - start2) + "ms.");
        }
        if (configuration.getBoolean(Configuration.enableANRCatch, true)) {
            long start3 = System.currentTimeMillis();
            this.mANRCatcher = new ANRCatcher();
            LogUtil.d("CrashSDK ANRCatcher initialize complete elapsed time:" + (System.currentTimeMillis() - start3) + "ms.");
        }
        if (configuration.getBoolean(Configuration.enableMainLoopBlockCatch, true)) {
            long start4 = System.currentTimeMillis();
            mainLoopCatcher(context, this.mReporterContext.getProperty(Constants.APP_VERSION));
            LogUtil.d("CrashSDK MainLoopCatcher initialize complete elapsed time:" + (System.currentTimeMillis() - start4) + "ms.");
        }
    }

    /* access modifiers changed from: package-private */
    public void enable() {
        if (this.mUncaughtExceptionCatcher != null) {
            this.mUncaughtExceptionCatcher.enable(this.mContext);
        }
        if (this.mUCNativeExceptionCatcher != null) {
            this.mUCNativeExceptionCatcher.enable();
        }
    }

    /* access modifiers changed from: package-private */
    public void disable() {
        if (this.mUncaughtExceptionCatcher != null) {
            this.mUncaughtExceptionCatcher.disable();
        }
        if (this.mUCNativeExceptionCatcher != null) {
            this.mUCNativeExceptionCatcher.disable();
        }
    }

    /* access modifiers changed from: package-private */
    public void addUncaughtExceptionIgnore(UncaughtExceptionIgnore ignore) {
        if (this.mUncaughtExceptionCatcher != null) {
            this.mUncaughtExceptionCatcher.addIgnore(ignore);
        }
    }

    /* access modifiers changed from: package-private */
    public void addUncaughtExceptionLinster(UncaughtExceptionLinster linster) {
        if (this.mUncaughtExceptionCatcher != null) {
            this.mUncaughtExceptionCatcher.addLinster(linster);
        }
    }

    /* access modifiers changed from: package-private */
    public List<UncaughtExceptionLinster> getAllUncaughtExceptionLinster() {
        if (this.mUncaughtExceptionCatcher != null) {
            return this.mUncaughtExceptionCatcher.getAllLinster();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void doScan() {
        this.mUCNativeExceptionCatcher.doScan();
        this.mANRCatcher.doScan();
    }

    public void refreshNativeInfo() {
        this.mUCNativeExceptionCatcher.refreshNativeInfo();
    }

    class UncaughtExceptionLinsterAdapterCopyOnWriteArrayList extends CopyOnWriteArrayList<UncaughtExceptionLinster> {
        private static final long serialVersionUID = 4393313111950638180L;

        UncaughtExceptionLinsterAdapterCopyOnWriteArrayList() {
        }

        public boolean remove(Object o) {
            Iterator i$ = iterator();
            while (i$.hasNext()) {
                UncaughtExceptionLinster linster = (UncaughtExceptionLinster) i$.next();
                if (linster.originalEquals(o)) {
                    return super.remove(linster);
                }
            }
            return false;
        }
    }

    class UncaughtExceptionCatcher implements Thread.UncaughtExceptionHandler {
        Context context;
        volatile boolean enable;
        Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
        CopyOnWriteArrayList<UncaughtExceptionIgnore> mIgnoreList = new CopyOnWriteArrayList<>();
        UncaughtExceptionLinsterAdapterCopyOnWriteArrayList mLinsterList = new UncaughtExceptionLinsterAdapterCopyOnWriteArrayList();

        UncaughtExceptionCatcher() {
        }

        public void enable(Context context2) {
            if (context2 != null) {
                this.context = context2;
            }
            if (!this.enable) {
                this.mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
                if (!"com.android.internal.osRuntimeInit$UncaughtHandler".equals(this.mDefaultUncaughtExceptionHandler.getClass().getName())) {
                }
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.enable = true;
            }
        }

        public void disable() {
            if (this.enable) {
                if (this.mDefaultUncaughtExceptionHandler != null) {
                    Thread.setDefaultUncaughtExceptionHandler(this.mDefaultUncaughtExceptionHandler);
                }
                this.enable = false;
            }
        }

        public boolean addIgnore(UncaughtExceptionIgnore ignore) {
            if (ignore == null || !StringUtils.isNotBlank(ignore.getName())) {
                return false;
            }
            return this.mIgnoreList.add(ignore);
        }

        public boolean addLinster(UncaughtExceptionLinster linster) {
            if (linster != null) {
                return this.mLinsterList.add(linster);
            }
            return false;
        }

        public List<UncaughtExceptionLinster> getAllLinster() {
            return this.mLinsterList;
        }

        private void onUncaughtException(Thread thread, Throwable throwable, boolean ignore) {
            Map<String, Object> extraInfo = new HashMap<>();
            if (ignore) {
                extraInfo.put(Constants.REPORT_IGNORE, "true");
            }
            try {
                if (throwable instanceof OutOfMemoryError) {
                    if (CatcherManager.this.mConfiguration.getBoolean(Configuration.enableDumpHprof, false)) {
                    }
                } else if (CatcherManager.this.mConfiguration.getBoolean(Configuration.enableExternalLinster, true)) {
                    Iterator i$ = this.mLinsterList.iterator();
                    while (i$.hasNext()) {
                        extraInfo.putAll(((UncaughtExceptionLinster) i$.next()).onUncaughtException(thread, throwable));
                    }
                }
                Long firstInstallTime = Utils.getContextFirstInstallTime(this.context);
                if (firstInstallTime != null) {
                    extraInfo.put(Constants.FIRST_INSTALL_TIME, firstInstallTime);
                }
                Long lastUpdateTime = Utils.getContextLastUpdateTime(this.context);
                if (lastUpdateTime != null) {
                    extraInfo.put(Constants.LAST_UPDATE_TIME, lastUpdateTime);
                }
            } catch (Throwable e) {
                LogUtil.e("externalData", e);
            }
            CatcherManager.this.mSendManager.sendReport(CatcherManager.this.mReportBuilder.buildUncaughtExceptionReport(throwable, thread, extraInfo));
        }

        public void uncaughtException(Thread thread, Throwable throwable) {
            long start = System.currentTimeMillis();
            try {
                LogUtil.d(String.format("catch uncaught exception:%s on thread:%s.", new Object[]{thread.getName(), throwable.toString()}));
                Boolean isMainThread = Utils.isMainThread(thread);
                if (CatcherManager.this.mConfiguration.getBoolean(Configuration.enableUncaughtExceptionIgnore, true) && isMainThread != null && !isMainThread.booleanValue()) {
                    Iterator i$ = this.mIgnoreList.iterator();
                    while (i$.hasNext()) {
                        if (i$.next().uncaughtExceptionIgnore(thread, throwable)) {
                            onUncaughtException(thread, throwable, true);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.e("ignore uncaught exception.", e);
            } catch (Throwable tw) {
                LogUtil.e("uncaught exception.", tw);
            }
            onUncaughtException(thread, throwable, false);
            LogUtil.d("catch uncaught exception complete elapsed time:" + (System.currentTimeMillis() - start) + ".ms");
            if (this.mDefaultUncaughtExceptionHandler != null) {
                this.mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
            }
        }
    }

    class UCNativeExceptionCatcher {
        volatile boolean enable = false;
        volatile boolean initCrashsdkSuccess = false;
        File mSystemTombstonesDir;
        String mSystemTombstonesDirPath;
        File mUCCrashsdkLogsDir;
        String mUCCrashsdkLogsDirPath;
        File mUCCrashsdkTagsDir;
        String mUCCrashsdkTagsDirPath;
        File mUCCrashsdkTombstonesDir;
        String mUCCrashsdkTombstonesDirPath;

        public UCNativeExceptionCatcher() {
            this.mUCCrashsdkTombstonesDirPath = CatcherManager.this.mStorageManager.mProcessTombstoneDirPath + File.separator + "crashsdk";
            this.mUCCrashsdkTagsDirPath = this.mUCCrashsdkTombstonesDirPath + File.separator + "tags";
            this.mUCCrashsdkLogsDirPath = this.mUCCrashsdkTombstonesDirPath + File.separator + "logs";
            this.mUCCrashsdkTombstonesDir = new File(this.mUCCrashsdkTombstonesDirPath);
            this.mUCCrashsdkTagsDir = new File(this.mUCCrashsdkTagsDirPath);
            this.mUCCrashsdkLogsDir = new File(this.mUCCrashsdkLogsDirPath);
            if (!this.mUCCrashsdkTombstonesDir.exists()) {
                this.mUCCrashsdkTombstonesDir.mkdirs();
            }
            if (!this.mUCCrashsdkTagsDir.exists()) {
                this.mUCCrashsdkTagsDir.mkdirs();
            }
            if (!this.mUCCrashsdkLogsDir.exists()) {
                this.mUCCrashsdkLogsDir.mkdirs();
            }
            initCrashsdkSo((String) null);
        }

        /* access modifiers changed from: package-private */
        public void initCrashsdkSo(String path) {
            try {
                File file = new File(path, "libcrashsdk.so");
                if (file.exists()) {
                    System.load(file.getPath());
                } else {
                    System.loadLibrary("crashsdk");
                }
                long start = System.currentTimeMillis();
                JNIBridge.nativeSetFolderNames(this.mUCCrashsdkTombstonesDirPath, "tags", "logs");
                String processName = CatcherManager.this.mReporterContext.getProperty(Constants.PROCESS_NAME);
                JNIBridge.nativeSetProcessNames(processName, Utils.reverse(processName));
                JNIBridge.nativeSetVersionInfo(CatcherManager.this.mReporterContext.getProperty(Constants.APP_VERSION), CatcherManager.this.mReporterContext.getProperty(Constants.APP_SUBVERSION, ""), CatcherManager.this.mReporterContext.getProperty(Constants.APP_BUILD, ""), "");
                JNIBridge.nativeSetMobileInfo(Build.MODEL, Build.VERSION.RELEASE, "");
                JNIBridge.nativeSetLogStrategy(true);
                JNIBridge.nativeSetCrashLogFileNames("", "", CrashReporter._MAGIC);
                LogUtil.d("CrashSDK nativeSetCrashLogFileNames complete elapsed time:" + (System.currentTimeMillis() - start) + "ms.");
                long start2 = System.currentTimeMillis();
                JNIBridge.nativeSetCrashLogFileNames("", "", "native");
                LogUtil.d("CrashSDK nativeSetCrashLogFileNames complete elapsed time:" + (System.currentTimeMillis() - start2) + "ms.");
                long start3 = System.currentTimeMillis();
                JNIBridge.nativeInitNative();
                LogUtil.d("CrashSDK nativeInitNative complete elapsed time:" + (System.currentTimeMillis() - start3) + "ms.");
                JNIBridge.nativeSetZip(false, GZipUtil.EXT, 1048576);
                this.initCrashsdkSuccess = true;
            } catch (Throwable e) {
                LogUtil.e("init uc crashsdk", e);
            }
        }

        public void refreshNativeInfo() {
            if (this.initCrashsdkSuccess) {
                try {
                    JNIBridge.nativeSetVersionInfo(CatcherManager.this.mReporterContext.getProperty(Constants.APP_VERSION), CatcherManager.this.mReporterContext.getProperty(Constants.APP_SUBVERSION, ""), CatcherManager.this.mReporterContext.getProperty(Constants.APP_BUILD, ""), "");
                    JNIBridge.nativeUpdateCrashLogNames();
                } catch (Throwable e) {
                    LogUtil.e("refresh native version info", e);
                }
            }
        }

        public void enable() {
            if (this.initCrashsdkSuccess && !this.enable) {
                try {
                    JNIBridge.nativeInstallBreakpad();
                } catch (Exception e) {
                    LogUtil.e("enable crashsdk", e);
                }
                this.enable = true;
            }
        }

        public void disable() {
            if (this.initCrashsdkSuccess && this.enable) {
                try {
                    JNIBridge.nativeUninstallBreakpad();
                } catch (Exception e) {
                    LogUtil.e("disable crashsdk", e);
                }
                this.enable = false;
            }
        }

        /* access modifiers changed from: private */
        public void doScan() {
            File[] jniLogFiles;
            long start = System.currentTimeMillis();
            try {
                if (this.mUCCrashsdkLogsDir != null && this.mUCCrashsdkLogsDir.exists() && (jniLogFiles = this.mUCCrashsdkLogsDir.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        if (file.getName().endsWith("jni.log") && file.canRead()) {
                            return true;
                        }
                        file.delete();
                        return false;
                    }
                })) != null && jniLogFiles.length > 0) {
                    for (File jniLogFile : jniLogFiles) {
                        CatcherManager.this.mSendManager.sendReport(CatcherManager.this.mReportBuilder.buildNativeExceptionReport(jniLogFile, new HashMap<>()));
                    }
                }
            } catch (Exception e) {
                LogUtil.e("find uc native log.", e);
            }
            LogUtil.d("find uc native log complete elapsed time:" + (System.currentTimeMillis() - start) + ".ms");
        }
    }

    class ANRCatcher {
        volatile boolean canScan = false;
        volatile boolean enable = false;
        File mProcessANRFlagFile;
        File mSystemTraceFile = new File(this.mSystemTraceFilePath);
        String mSystemTraceFilePath = "/data/anr/traces.txt";
        AtomicBoolean scaning = new AtomicBoolean(false);

        public ANRCatcher() {
            if (!this.mSystemTraceFile.exists()) {
                String propSystemTraceFilePath = Utils.SystemPropertiesUtils.get("dalvik.vm.stack-trace-file");
                if (!this.mSystemTraceFile.equals(propSystemTraceFilePath)) {
                    try {
                        this.mSystemTraceFile = new File(propSystemTraceFilePath);
                        this.mSystemTraceFilePath = propSystemTraceFilePath;
                    } catch (Exception e) {
                        LogUtil.e("system traces file error", e);
                    }
                }
            }
            if (this.mSystemTraceFile != null) {
                this.mProcessANRFlagFile = CatcherManager.this.mStorageManager.getProcessTombstoneFile("ANR_MONITOR");
                if (this.mProcessANRFlagFile.exists() || Utils.writeFile(this.mProcessANRFlagFile, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())))) {
                    this.canScan = true;
                }
            }
        }

        public class TracesFinder {
            boolean found = false;
            File mSystemTraceFile;
            String strEndFlag = "";
            String strPid = "";
            String strProcessName = "";
            String strStartFlag = "";
            String strTriggerTime = "";

            public TracesFinder(File systemTraceFile) {
                this.mSystemTraceFile = systemTraceFile;
            }

            /* JADX WARNING: Removed duplicated region for block: B:15:0x0033 A[SYNTHETIC, Splitter:B:15:0x0033] */
            /* JADX WARNING: Removed duplicated region for block: B:62:0x012d A[SYNTHETIC, Splitter:B:62:0x012d] */
            /* JADX WARNING: Removed duplicated region for block: B:9:0x0024  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void find() {
                /*
                    r18 = this;
                    r8 = 0
                    java.io.BufferedReader r9 = new java.io.BufferedReader     // Catch:{ IOException -> 0x013c }
                    java.io.InputStreamReader r14 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x013c }
                    java.io.FileInputStream r15 = new java.io.FileInputStream     // Catch:{ IOException -> 0x013c }
                    r0 = r18
                    java.io.File r0 = r0.mSystemTraceFile     // Catch:{ IOException -> 0x013c }
                    r16 = r0
                    r15.<init>(r16)     // Catch:{ IOException -> 0x013c }
                    r14.<init>(r15)     // Catch:{ IOException -> 0x013c }
                    r9.<init>(r14)     // Catch:{ IOException -> 0x013c }
                L_0x0016:
                    java.lang.String r4 = r9.readLine()     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    if (r4 == 0) goto L_0x0022
                    boolean r14 = com.alibaba.motu.crashreporter.utils.StringUtils.isNotBlank(r4)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    if (r14 == 0) goto L_0x0016
                L_0x0022:
                    if (r4 != 0) goto L_0x0033
                    if (r9 == 0) goto L_0x0029
                    r9.close()     // Catch:{ IOException -> 0x002b }
                L_0x0029:
                    r8 = r9
                L_0x002a:
                    return
                L_0x002b:
                    r2 = move-exception
                    java.lang.String r14 = "close traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r14, r2)
                    goto L_0x0029
                L_0x0033:
                    java.lang.String r5 = r9.readLine()     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    if (r5 != 0) goto L_0x0048
                    if (r9 == 0) goto L_0x003e
                    r9.close()     // Catch:{ IOException -> 0x0040 }
                L_0x003e:
                    r8 = r9
                    goto L_0x002a
                L_0x0040:
                    r2 = move-exception
                    java.lang.String r14 = "close traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r14, r2)
                    goto L_0x003e
                L_0x0048:
                    java.lang.String r14 = "-----\\spid\\s(\\d+?)\\sat\\s(.+?)\\s-----"
                    java.util.regex.Pattern r13 = java.util.regex.Pattern.compile(r14)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    java.util.regex.Matcher r6 = r13.matcher(r4)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    boolean r14 = r6.find()     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    if (r14 == 0) goto L_0x00f8
                    r14 = 1
                    java.lang.String r14 = r6.group(r14)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    r0 = r18
                    r0.strPid = r14     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    r14 = 2
                    java.lang.String r14 = r6.group(r14)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    r0 = r18
                    r0.strTriggerTime = r14     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    java.lang.String r14 = "Cmd\\sline:\\s(.+)"
                    java.util.regex.Pattern r7 = java.util.regex.Pattern.compile(r14)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    java.util.regex.Matcher r6 = r7.matcher(r5)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    boolean r14 = r6.find()     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    if (r14 == 0) goto L_0x00f8
                    r14 = 1
                    java.lang.String r14 = r6.group(r14)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    r0 = r18
                    r0.strProcessName = r14     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    r0 = r18
                    java.lang.String r14 = r0.strProcessName     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    r0 = r18
                    com.alibaba.motu.crashreporter.CatcherManager$ANRCatcher r15 = com.alibaba.motu.crashreporter.CatcherManager.ANRCatcher.this     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    com.alibaba.motu.crashreporter.CatcherManager r15 = com.alibaba.motu.crashreporter.CatcherManager.this     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    java.lang.String r15 = r15.mProcessName     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    boolean r14 = r14.equals(r15)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    if (r14 == 0) goto L_0x00f8
                    r0 = r18
                    com.alibaba.motu.crashreporter.CatcherManager$ANRCatcher r14 = com.alibaba.motu.crashreporter.CatcherManager.ANRCatcher.this     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    java.io.File r14 = r14.mProcessANRFlagFile     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    java.lang.String r11 = com.alibaba.motu.crashreporter.Utils.readLine((java.io.File) r14)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    boolean r14 = com.alibaba.motu.crashreporter.utils.StringUtils.isNotBlank(r11)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    if (r14 == 0) goto L_0x00f8
                    java.text.SimpleDateFormat r10 = new java.text.SimpleDateFormat     // Catch:{ Exception -> 0x0100 }
                    java.lang.String r14 = "yyyy-MM-dd HH:mm:ss"
                    r10.<init>(r14)     // Catch:{ Exception -> 0x0100 }
                    java.util.Date r3 = r10.parse(r11)     // Catch:{ Exception -> 0x0100 }
                    r0 = r18
                    java.lang.String r14 = r0.strTriggerTime     // Catch:{ Exception -> 0x0100 }
                    java.util.Date r12 = r10.parse(r14)     // Catch:{ Exception -> 0x0100 }
                    long r14 = r12.getTime()     // Catch:{ Exception -> 0x0100 }
                    long r16 = r3.getTime()     // Catch:{ Exception -> 0x0100 }
                    int r14 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
                    if (r14 <= 0) goto L_0x00f8
                    r0 = r18
                    com.alibaba.motu.crashreporter.CatcherManager$ANRCatcher r14 = com.alibaba.motu.crashreporter.CatcherManager.ANRCatcher.this     // Catch:{ Exception -> 0x0100 }
                    java.io.File r14 = r14.mProcessANRFlagFile     // Catch:{ Exception -> 0x0100 }
                    r0 = r18
                    java.lang.String r15 = r0.strTriggerTime     // Catch:{ Exception -> 0x0100 }
                    boolean r14 = com.alibaba.motu.crashreporter.Utils.writeFile(r14, r15)     // Catch:{ Exception -> 0x0100 }
                    if (r14 == 0) goto L_0x00f8
                    r0 = r18
                    r0.strStartFlag = r4     // Catch:{ Exception -> 0x0100 }
                    java.lang.String r14 = "----- end %s -----"
                    r15 = 1
                    java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch:{ Exception -> 0x0100 }
                    r16 = 0
                    r0 = r18
                    java.lang.String r0 = r0.strPid     // Catch:{ Exception -> 0x0100 }
                    r17 = r0
                    r15[r16] = r17     // Catch:{ Exception -> 0x0100 }
                    java.lang.String r14 = java.lang.String.format(r14, r15)     // Catch:{ Exception -> 0x0100 }
                    r0 = r18
                    r0.strEndFlag = r14     // Catch:{ Exception -> 0x0100 }
                    r14 = 1
                    r0 = r18
                    r0.found = r14     // Catch:{ Exception -> 0x0100 }
                L_0x00f8:
                    if (r9 == 0) goto L_0x013e
                    r9.close()     // Catch:{ IOException -> 0x0120 }
                    r8 = r9
                    goto L_0x002a
                L_0x0100:
                    r2 = move-exception
                    java.lang.String r14 = "compare triggerTime"
                    com.alibaba.motu.crashreporter.LogUtil.e(r14, r2)     // Catch:{ IOException -> 0x0108, all -> 0x0139 }
                    goto L_0x00f8
                L_0x0108:
                    r2 = move-exception
                    r8 = r9
                L_0x010a:
                    java.lang.String r14 = "do scan traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r14, r2)     // Catch:{ all -> 0x012a }
                    if (r8 == 0) goto L_0x002a
                    r8.close()     // Catch:{ IOException -> 0x0117 }
                    goto L_0x002a
                L_0x0117:
                    r2 = move-exception
                    java.lang.String r14 = "close traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r14, r2)
                    goto L_0x002a
                L_0x0120:
                    r2 = move-exception
                    java.lang.String r14 = "close traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r14, r2)
                    r8 = r9
                    goto L_0x002a
                L_0x012a:
                    r14 = move-exception
                L_0x012b:
                    if (r8 == 0) goto L_0x0130
                    r8.close()     // Catch:{ IOException -> 0x0131 }
                L_0x0130:
                    throw r14
                L_0x0131:
                    r2 = move-exception
                    java.lang.String r15 = "close traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r15, r2)
                    goto L_0x0130
                L_0x0139:
                    r14 = move-exception
                    r8 = r9
                    goto L_0x012b
                L_0x013c:
                    r2 = move-exception
                    goto L_0x010a
                L_0x013e:
                    r8 = r9
                    goto L_0x002a
                */
                throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.CatcherManager.ANRCatcher.TracesFinder.find():void");
            }
        }

        public void doScan() {
            long start = System.currentTimeMillis();
            if (this.canScan && this.scaning.compareAndSet(false, true)) {
                try {
                    final TracesFinder tracesFinder = new TracesFinder(this.mSystemTraceFile);
                    tracesFinder.find();
                    if (tracesFinder.found) {
                        Thread thread = new Thread("CrashReportANRCatch") {
                            public void run() {
                                try {
                                    CatcherManager.this.mSendManager.sendReport(CatcherManager.this.mReportBuilder.buildANRReport(tracesFinder, new HashMap<>()));
                                } catch (Exception e) {
                                    LogUtil.e("send anr report", e);
                                }
                            }
                        };
                        thread.setDaemon(true);
                        thread.start();
                    }
                } catch (Exception e) {
                    LogUtil.e("do scan traces file", e);
                }
            }
            LogUtil.d("scaning anr complete elapsed time:" + (System.currentTimeMillis() - start) + ".ms");
        }
    }

    /* access modifiers changed from: package-private */
    public void mainLoopCatcher(final Context context, final String appVersion) {
        try {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    try {
                        Class<?> lClass = Class.forName("com.alibaba.motu.watch.MotuWatch");
                        if (lClass != null) {
                            try {
                                Object lMainLoopWatch = UTReflect.invokeStaticMethod((Class) lClass, "getInstance");
                                if (lMainLoopWatch != null) {
                                    UTReflect.invokeMethod(lMainLoopWatch, "enableWatch", new Object[]{context, appVersion, false}, Context.class, String.class, Boolean.class);
                                }
                            } catch (Exception e) {
                                LogUtil.e("enable mainLoopCatcher", e);
                            }
                        }
                    } catch (ClassNotFoundException e2) {
                    }
                }
            }, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
