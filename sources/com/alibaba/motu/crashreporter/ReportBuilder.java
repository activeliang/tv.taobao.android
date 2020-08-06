package com.alibaba.motu.crashreporter;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import com.alibaba.motu.crashreporter.CatcherManager;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ReportBuilder {
    Configuration mConfiguration;
    Context mContext;
    ReporterContext mReporterContext;
    StorageManager mStorageManager;

    public ReportBuilder(Context context, ReporterContext repoterContext, Configuration configuration, StorageManager storageManager) {
        this.mContext = context;
        this.mReporterContext = repoterContext;
        this.mConfiguration = configuration;
        this.mStorageManager = storageManager;
    }

    public CrashReport buildUncaughtExceptionReport(Throwable throwable, Thread thread, Map<String, Object> extraInfo) {
        clearCrashRepoterFile();
        long timestamp = System.currentTimeMillis();
        String tag = "catch";
        if ("true".equals(extraInfo.get(Constants.REPORT_IGNORE))) {
            tag = "ignore";
        }
        String reportName = CrashReport.buildReportName(this.mReporterContext.getPropertyAndSet(Constants.UTDID), this.mReporterContext.getProperty(Constants.APP_KEY), this.mReporterContext.getProperty(Constants.APP_VERSION), timestamp, tag, CrashReport.TYPE_JAVA);
        File reportFile = this.mStorageManager.getProcessTombstoneFile(reportName);
        new UncaughtExceptionReportPrintWrite(this.mContext, this.mReporterContext, this.mConfiguration, reportName, timestamp, reportFile, throwable, thread, extraInfo).print();
        return CrashReport.buildCrashReport(this.mContext, reportFile, this.mReporterContext, true);
    }

    public CrashReport buildNativeExceptionReport(File jniLogFile, Map<String, String> map) {
        clearCrashRepoterFile();
        File distFile = this.mStorageManager.getProcessTombstoneFile(CrashReport.buildReportName(this.mReporterContext.getPropertyAndSet(Constants.UTDID), this.mReporterContext.getProperty(Constants.APP_KEY), this.mReporterContext.getProperty(Constants.APP_VERSION), System.currentTimeMillis(), "scan", "native"));
        jniLogFile.renameTo(distFile);
        return CrashReport.buildCrashReport(this.mContext, distFile, this.mReporterContext, false);
    }

    public CrashReport buildANRReport(CatcherManager.ANRCatcher.TracesFinder tracesFinder, Map<String, String> map) {
        clearCrashRepoterFile();
        long timestamp = System.currentTimeMillis();
        String reportName = CrashReport.buildReportName(this.mReporterContext.getPropertyAndSet(Constants.UTDID), this.mReporterContext.getProperty(Constants.APP_KEY), this.mReporterContext.getProperty(Constants.APP_VERSION), timestamp, "scan", CrashReport.TYPE_ANR);
        File reportFile = this.mStorageManager.getProcessTombstoneFile(reportName);
        new ANRReportPrintWrite(this.mContext, this.mReporterContext, this.mConfiguration, reportName, timestamp, reportFile, tracesFinder).print();
        return CrashReport.buildCrashReport(this.mContext, reportFile, this.mReporterContext, false);
    }

    public CrashReport[] listProcessCrashReport() {
        File[] reportFiles = listProcessCrashReportFile();
        if (reportFiles == null || reportFiles.length <= 0) {
            return null;
        }
        List<CrashReport> crashReports = new ArrayList<>();
        for (File reportFile : reportFiles) {
            crashReports.add(CrashReport.buildCrashReport(this.mContext, reportFile, this.mReporterContext, false));
        }
        return (CrashReport[]) crashReports.toArray(new CrashReport[0]);
    }

    private File[] listProcessCrashReportFile() {
        return this.mStorageManager.listProcessTombstoneFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith("java.log") || file.getName().endsWith("native.log") || file.getName().endsWith("anr.log");
            }
        });
    }

    public void clearCrashRepoterFile() {
        try {
            File[] crashReportFiles = listProcessCrashReportFile();
            if (crashReportFiles != null && crashReportFiles.length > 20) {
                List<File> list = Arrays.asList(crashReportFiles);
                Collections.sort(list, new Comparator<File>() {
                    public int compare(File laftFile, File rightFile) {
                        if (laftFile.lastModified() > rightFile.lastModified()) {
                            return -1;
                        }
                        if (laftFile.lastModified() == rightFile.lastModified()) {
                            return 0;
                        }
                        return 1;
                    }
                });
                for (File file : list) {
                    if (0 > 20) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e("clear crashReport file", e);
        }
    }

    public abstract class ReportPrintWrite {
        Configuration mConfiguration;
        Context mContext;
        Map<String, Object> mExtraInfo;
        long mFull;
        long mLimit;
        OutputStream mOutputStream;
        long mReject;
        String mReportName;
        String mReportType;
        ReporterContext mReporterContext;
        long mTimestamp;
        long mWrite;

        /* access modifiers changed from: protected */
        public abstract void printContent();

        public ReportPrintWrite() {
        }

        public void print() {
            printBanner();
            printContent();
            printDone();
        }

        /* access modifiers changed from: protected */
        public void close() {
            Utils.closeQuietly(this.mOutputStream);
        }

        /* access modifiers changed from: protected */
        public void write(String message) {
            byte[] bytes = new byte[0];
            try {
                bytes = message.getBytes("UTF-8");
            } catch (Exception e) {
                LogUtil.e("write.", e);
            }
            this.mFull += (long) bytes.length;
            try {
                LogUtil.i(message);
            } catch (Exception e2) {
            }
            try {
                this.mOutputStream.write(message.getBytes("UTF-8"));
                this.mWrite += (long) bytes.length;
                this.mOutputStream.flush();
            } catch (Exception e3) {
                LogUtil.e("write.", e3);
            }
        }

        /* access modifiers changed from: protected */
        public void printDone() {
            write(String.format("Full: %d bytes, write: %d bytes, limit: %d bytes, reject: %d bytes.\n", new Object[]{Long.valueOf(this.mFull), Long.valueOf(this.mWrite), Long.valueOf(this.mLimit), Long.valueOf(this.mReject)}));
            write(String.format("log end: %s\n", new Object[]{Utils.getGMT8Time(System.currentTimeMillis())}));
        }

        /* access modifiers changed from: protected */
        public void printEnd() {
            write("--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n");
        }

        /* access modifiers changed from: protected */
        public void printBanner() {
            write("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***\n");
            write(String.format("Basic Information: 'pid: %d/tid: %d/logver: 2/time: %s/cpu: %s/cpu hardware: %s'\n", new Object[]{Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()), Long.valueOf(this.mTimestamp), Build.CPU_ABI, Build.HARDWARE}));
            write(String.format("Mobile Information: 'model: %s/version: %s/sdk: %d'\n", new Object[]{Build.MODEL, Build.VERSION.RELEASE, Integer.valueOf(Build.VERSION.SDK_INT)}));
            write(String.format("Build fingerprint: '" + Build.FINGERPRINT + "'\n", new Object[0]));
            write(String.format("Runtime Information: 'start: %s/maxheap: %s'\n", new Object[]{this.mReporterContext.getProperty(Constants.STARTUP_TIME), Long.valueOf(Runtime.getRuntime().maxMemory())}));
            write(String.format("Application Information: 'version: %s/subversion: %s/buildseq: %s'\n", new Object[]{this.mReporterContext.getProperty(Constants.APP_VERSION), this.mReporterContext.getProperty(Constants.APP_SUBVERSION), this.mReporterContext.getProperty(Constants.APP_BUILD)}));
            write(String.format("%s Information: 'version: %s/nativeseq: %s/javaseq: %s/target: %s'\n", new Object[]{CrashReporter._MAGIC, CrashReporter._VERSION, CrashReporter._NATIVE_VERSION, "", "beta"}));
            write("Report Name: " + this.mReportName + "\n");
            write("UUID: " + UUID.randomUUID().toString().toLowerCase() + "\n");
            write("Log Type: " + this.mReportType + "\n");
            printEnd();
        }

        /* access modifiers changed from: protected */
        public void printStatus() {
            try {
                write("meminfo:\n");
                write(StringUtils.defaultString(Utils.getMeminfo(), "") + "\n");
                printEnd();
            } catch (Exception e) {
                LogUtil.e("write meminfo.", e);
            }
            try {
                write("status:\n");
                write(StringUtils.defaultString(Utils.getMyStatus(), "") + "\n");
                printEnd();
            } catch (Exception e2) {
                LogUtil.e("write status.", e2);
            }
            try {
                write("virtual machine:\nMaxMemory: " + Runtime.getRuntime().maxMemory() + " TotalMemory: " + Runtime.getRuntime().totalMemory() + " FreeMemory: " + Runtime.getRuntime().freeMemory() + "\n");
            } catch (Exception e3) {
                LogUtil.e("write virtual machine info.", e3);
            }
            printEnd();
        }

        /* access modifiers changed from: protected */
        public void printStorageinfo() {
            write("storageinfo:\n");
            write(Utils.dumpStorage(this.mContext));
            printEnd();
        }

        /* access modifiers changed from: protected */
        public void printApplictionMeminfo() {
            write("appliction meminfo:\n");
            write(Utils.dumpMeminfo(this.mContext));
            printEnd();
        }

        /* access modifiers changed from: protected */
        public void printFileDescriptor() {
            int writeLimit = this.mConfiguration.getInt(Configuration.fileDescriptorLimit, 900);
            File[] fds = null;
            try {
                fds = new File("/proc/self/fd").listFiles();
                if (fds != null) {
                    write(String.format("opened file count: %d, write limit: %d.\n", new Object[]{Integer.valueOf(fds.length), Integer.valueOf(writeLimit)}));
                } else {
                    write("[DEBUG] listFiles failed!\n");
                }
            } catch (Exception e) {
                LogUtil.e("print file descriptor.", e);
            }
            if (fds != null) {
                try {
                    if (fds.length >= writeLimit) {
                        write("opened files:\n");
                        StringBuilder sb = new StringBuilder();
                        try {
                            for (File fd : fds) {
                                sb.append(fd.getName());
                                sb.append(" -> ");
                                sb.append(fd.getCanonicalPath());
                                sb.append("\n");
                            }
                        } catch (Exception e2) {
                            LogUtil.e("print file descriptor.", e2);
                        }
                        write(sb.toString());
                    }
                } catch (Exception e3) {
                    LogUtil.e("print file descriptor.", e3);
                }
            }
            printEnd();
        }

        /* access modifiers changed from: protected */
        public void printLogcat() {
            int mainLogLineLimit = this.mConfiguration.getInt(Configuration.mainLogLineLimit, 2000);
            int eventsLogLineLimit = this.mConfiguration.getInt(Configuration.eventsLogLineLimit, 200);
            printLogcat((String) null, mainLogLineLimit);
            printLogcat("events", eventsLogLineLimit);
        }

        private void printLogcat(String logname, int limit) {
            List<String> command = new ArrayList<>();
            command.add("logcat");
            command.add("-d");
            if (StringUtils.isBlank(logname)) {
                write("logcat main: \n");
            } else {
                write("logcat " + logname + ": \n");
                command.add("-b");
                command.add(logname);
            }
            command.add("-v");
            command.add("threadtime");
            if (limit < 0) {
                write("[DEBUG] custom java logcat lines count is 0!\n");
            } else {
                command.add("-t");
                command.add(String.valueOf(limit));
                Process process = null;
                try {
                    process = new ProcessBuilder(new String[0]).command(command).redirectErrorStream(true).start();
                } catch (Exception e) {
                    LogUtil.e("exec logcat", e);
                }
                if (process == null) {
                    write("[DEBUG] exec logcat failed!\n");
                } else {
                    int total = 0;
                    int wrote = 0;
                    BufferedReader bufferedReader = null;
                    try {
                        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(process.getInputStream()), 8192);
                        while (true) {
                            try {
                                String line = bufferedReader2.readLine();
                                if (line == null) {
                                    break;
                                }
                                total++;
                                if (wrote < limit) {
                                    write(line + "\n");
                                    wrote++;
                                }
                            } catch (Exception e2) {
                                e = e2;
                                bufferedReader = bufferedReader2;
                                try {
                                    LogUtil.e("print log.", e);
                                    Utils.closeQuietly(bufferedReader);
                                    write(String.format("[DEBUG] Read %d lines, wrote %d lines.\n", new Object[]{Integer.valueOf(total), Integer.valueOf(wrote)}));
                                    printEnd();
                                } catch (Throwable th) {
                                    th = th;
                                    Utils.closeQuietly(bufferedReader);
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                bufferedReader = bufferedReader2;
                                Utils.closeQuietly(bufferedReader);
                                throw th;
                            }
                        }
                        Utils.closeQuietly(bufferedReader2);
                        BufferedReader bufferedReader3 = bufferedReader2;
                    } catch (Exception e3) {
                        e = e3;
                        LogUtil.e("print log.", e);
                        Utils.closeQuietly(bufferedReader);
                        write(String.format("[DEBUG] Read %d lines, wrote %d lines.\n", new Object[]{Integer.valueOf(total), Integer.valueOf(wrote)}));
                        printEnd();
                    }
                    write(String.format("[DEBUG] Read %d lines, wrote %d lines.\n", new Object[]{Integer.valueOf(total), Integer.valueOf(wrote)}));
                }
            }
            printEnd();
        }

        /* access modifiers changed from: protected */
        public void printExtraInfo() {
            if (this.mExtraInfo != null && !this.mExtraInfo.isEmpty()) {
                try {
                    write("extrainfo:\n");
                    for (String key : this.mExtraInfo.keySet()) {
                        write(String.format("%s: %s\n", new Object[]{key, this.mExtraInfo.get(key)}));
                    }
                } catch (Exception e) {
                    LogUtil.e("write extral info", e);
                }
                printEnd();
            }
        }
    }

    public abstract class FileReportPrintWrite extends ReportPrintWrite {
        File mReportFile;

        public FileReportPrintWrite(Context context, ReporterContext reporterContext, Configuration configuration, String reportName, String reportType, long timestamp, File reportFile, Map<String, Object> extraInfo) {
            super();
            this.mContext = context;
            this.mReporterContext = reporterContext;
            this.mConfiguration = configuration;
            this.mReportName = reportName;
            this.mReportType = reportType;
            this.mTimestamp = timestamp;
            this.mReportFile = reportFile;
            this.mExtraInfo = extraInfo;
            if (reportFile.exists()) {
                reportFile.delete();
            }
            try {
                this.mOutputStream = new FileOutputStream(reportFile);
            } catch (FileNotFoundException e) {
                LogUtil.e("create fileOutputStream.", e);
            }
        }
    }

    final class UncaughtExceptionReportPrintWrite extends FileReportPrintWrite {
        Thread mThread;
        Throwable mThrowable;

        UncaughtExceptionReportPrintWrite(Context context, ReporterContext reporterContext, Configuration configuration, String reportName, long timestamp, File reportFile, Throwable throwable, Thread thread, Map<String, Object> extraInfo) {
            super(context, reporterContext, configuration, reportName, CrashReport.TYPE_JAVA, timestamp, reportFile, extraInfo);
            this.mThrowable = throwable;
            this.mThread = thread;
        }

        /* access modifiers changed from: protected */
        public void printContent() {
            printThrowable();
            printExtraInfo();
            printStatus();
            printStorageinfo();
            printFileDescriptor();
            if (this.mThrowable instanceof OutOfMemoryError) {
                printApplictionMeminfo();
            }
            printLogcat();
        }

        private void printThrowable() {
            try {
                write(String.format("Process Name: '%s' \n", new Object[]{this.mReporterContext.getProperty(Constants.PROCESS_NAME)}));
                write(String.format("Thread Name: '%s' \n", new Object[]{this.mThread.getName()}));
                write("Back traces starts.\n");
                ByteArrayOutputStream byteArrayOutputStream = null;
                try {
                    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                    try {
                        this.mThrowable.printStackTrace(new PrintStream(byteArrayOutputStream2));
                        write(byteArrayOutputStream2.toString());
                        Utils.closeQuietly(byteArrayOutputStream2);
                        ByteArrayOutputStream byteArrayOutputStream3 = byteArrayOutputStream2;
                    } catch (Exception e) {
                        e = e;
                        byteArrayOutputStream = byteArrayOutputStream2;
                        try {
                            LogUtil.e("print throwable", e);
                            Utils.closeQuietly(byteArrayOutputStream);
                            write("Back traces end.\n");
                            printEnd();
                            write(Utils.dumpThread(this.mThread));
                            printEnd();
                        } catch (Throwable th) {
                            th = th;
                            Utils.closeQuietly(byteArrayOutputStream);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        byteArrayOutputStream = byteArrayOutputStream2;
                        Utils.closeQuietly(byteArrayOutputStream);
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                    LogUtil.e("print throwable", e);
                    Utils.closeQuietly(byteArrayOutputStream);
                    write("Back traces end.\n");
                    printEnd();
                    write(Utils.dumpThread(this.mThread));
                    printEnd();
                }
                write("Back traces end.\n");
                printEnd();
            } catch (Exception e3) {
                LogUtil.e("write throwable", e3);
            }
            try {
                write(Utils.dumpThread(this.mThread));
            } catch (Exception e4) {
                LogUtil.e("write thread", e4);
            }
            printEnd();
        }
    }

    final class ANRReportPrintWrite extends FileReportPrintWrite {
        CatcherManager.ANRCatcher.TracesFinder mTracesFinder;

        ANRReportPrintWrite(Context context, ReporterContext reporterContext, Configuration configuration, String reportName, long timestamp, File reportFile, CatcherManager.ANRCatcher.TracesFinder tracesFinder) {
            super(context, reporterContext, configuration, reportName, CrashReport.TYPE_ANR, timestamp, reportFile, (Map<String, Object>) null);
            this.mTracesFinder = tracesFinder;
        }

        /* access modifiers changed from: protected */
        public void printContent() {
            printTraces();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0061, code lost:
            if (r2 > 5) goto L_0x0052;
         */
        /* JADX WARNING: Unknown top exception splitter block from list: {B:17:0x0052=Splitter:B:17:0x0052, B:33:0x0078=Splitter:B:33:0x0078} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void printTraces() {
            /*
                r9 = this;
                java.lang.String r6 = "traces starts.\n"
                r9.write(r6)     // Catch:{ Exception -> 0x006f }
                r3 = 0
                r5 = 0
                java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0064 }
                java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0064 }
                java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0064 }
                com.alibaba.motu.crashreporter.CatcherManager$ANRCatcher$TracesFinder r8 = r9.mTracesFinder     // Catch:{ IOException -> 0x0064 }
                java.io.File r8 = r8.mSystemTraceFile     // Catch:{ IOException -> 0x0064 }
                r7.<init>(r8)     // Catch:{ IOException -> 0x0064 }
                r6.<init>(r7)     // Catch:{ IOException -> 0x0064 }
                r4.<init>(r6)     // Catch:{ IOException -> 0x0064 }
                r2 = 0
            L_0x001c:
                java.lang.String r1 = r4.readLine()     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                if (r1 == 0) goto L_0x0052
                int r2 = r2 + 1
                com.alibaba.motu.crashreporter.CatcherManager$ANRCatcher$TracesFinder r6 = r9.mTracesFinder     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                java.lang.String r6 = r6.strStartFlag     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                boolean r6 = r6.equals(r1)     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                if (r6 != 0) goto L_0x002f
                r5 = 1
            L_0x002f:
                if (r5 == 0) goto L_0x0060
                java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                r6.<init>()     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                java.lang.StringBuilder r6 = r6.append(r1)     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                java.lang.String r7 = "\n"
                java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                r9.write(r6)     // Catch:{ IOException -> 0x007f, all -> 0x007c }
            L_0x0048:
                com.alibaba.motu.crashreporter.CatcherManager$ANRCatcher$TracesFinder r6 = r9.mTracesFinder     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                java.lang.String r6 = r6.strEndFlag     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                boolean r6 = r6.equals(r1)     // Catch:{ IOException -> 0x007f, all -> 0x007c }
                if (r6 == 0) goto L_0x001c
            L_0x0052:
                com.alibaba.motu.crashreporter.Utils.closeQuietly(r4)     // Catch:{ Exception -> 0x006f }
                r3 = r4
            L_0x0056:
                java.lang.String r6 = "traces end.\n"
                r9.write(r6)     // Catch:{ Exception -> 0x006f }
            L_0x005c:
                r9.printEnd()
                return
            L_0x0060:
                r6 = 5
                if (r2 <= r6) goto L_0x0048
                goto L_0x0052
            L_0x0064:
                r0 = move-exception
            L_0x0065:
                java.lang.String r6 = "read anr file."
                com.alibaba.motu.crashreporter.LogUtil.e(r6, r0)     // Catch:{ all -> 0x0077 }
                com.alibaba.motu.crashreporter.Utils.closeQuietly(r3)     // Catch:{ Exception -> 0x006f }
                goto L_0x0056
            L_0x006f:
                r0 = move-exception
                java.lang.String r6 = "write traces."
                com.alibaba.motu.crashreporter.LogUtil.e(r6, r0)
                goto L_0x005c
            L_0x0077:
                r6 = move-exception
            L_0x0078:
                com.alibaba.motu.crashreporter.Utils.closeQuietly(r3)     // Catch:{ Exception -> 0x006f }
                throw r6     // Catch:{ Exception -> 0x006f }
            L_0x007c:
                r6 = move-exception
                r3 = r4
                goto L_0x0078
            L_0x007f:
                r0 = move-exception
                r3 = r4
                goto L_0x0065
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.ReportBuilder.ANRReportPrintWrite.printTraces():void");
        }
    }
}
