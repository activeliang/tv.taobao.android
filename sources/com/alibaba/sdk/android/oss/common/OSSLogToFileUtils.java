package com.alibaba.sdk.android.oss.common;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;
import android.taobao.windvane.jsbridge.api.WVFile;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OSSLogToFileUtils {
    private static final String LOG_DIR_NAME = "OSSLog";
    /* access modifiers changed from: private */
    public static long LOG_MAX_SIZE = WVFile.FILE_MAX_SIZE;
    /* access modifiers changed from: private */
    public static OSSLogToFileUtils instance;
    private static LogThreadPoolManager logService = LogThreadPoolManager.newInstance();
    private static Context sContext;
    /* access modifiers changed from: private */
    public static File sLogFile;
    /* access modifiers changed from: private */
    public static SimpleDateFormat sLogSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private boolean useSdCard = true;

    private OSSLogToFileUtils() {
    }

    public static void init(Context context, ClientConfiguration cfg) {
        OSSLog.logDebug("init ...", false);
        if (cfg != null) {
            LOG_MAX_SIZE = cfg.getMaxLogSize();
        }
        if (sContext == null || instance == null || sLogFile == null || !sLogFile.exists()) {
            sContext = context.getApplicationContext();
            instance = getInstance();
            logService.addExecuteTask(new Runnable() {
                public void run() {
                    File unused = OSSLogToFileUtils.sLogFile = OSSLogToFileUtils.instance.getLogFile();
                    if (OSSLogToFileUtils.sLogFile != null) {
                        OSSLog.logInfo("LogFilePath is: " + OSSLogToFileUtils.sLogFile.getPath(), false);
                        if (OSSLogToFileUtils.LOG_MAX_SIZE < OSSLogToFileUtils.getLogFileSize(OSSLogToFileUtils.sLogFile)) {
                            OSSLog.logInfo("init reset log file", false);
                            OSSLogToFileUtils.instance.resetLogFile();
                        }
                    }
                }
            });
            return;
        }
        OSSLog.logDebug("LogToFileUtils has been init ...", false);
    }

    public static OSSLogToFileUtils getInstance() {
        if (instance == null) {
            synchronized (OSSLogToFileUtils.class) {
                if (instance == null) {
                    instance = new OSSLogToFileUtils();
                }
            }
        }
        return instance;
    }

    public static void reset() {
        sContext = null;
        instance = null;
        sLogFile = null;
    }

    public static long getLogFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        return file.length();
    }

    public static long getLocalLogFileSize() {
        return getLogFileSize(sLogFile);
    }

    private long readSDCardSpace() {
        long sdCardSize = 0;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
            sdCardSize = ((long) sf.getAvailableBlocks()) * ((long) sf.getBlockSize());
        }
        OSSLog.logDebug("sd卡存储空间:" + String.valueOf(sdCardSize) + "kb", false);
        return sdCardSize;
    }

    private long readSystemSpace() {
        StatFs sf = new StatFs(Environment.getRootDirectory().getPath());
        long systemSpaceSize = (((long) sf.getAvailableBlocks()) * ((long) sf.getBlockSize())) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        OSSLog.logDebug("内部存储空间:" + String.valueOf(systemSpaceSize) + "kb", false);
        return systemSpaceSize;
    }

    public void setUseSdCard(boolean useSdCard2) {
        this.useSdCard = useSdCard2;
    }

    public void resetLogFile() {
        OSSLog.logDebug("Reset Log File ... ", false);
        if (!sLogFile.getParentFile().exists()) {
            OSSLog.logDebug("Reset Log make File dir ... ", false);
            sLogFile.getParentFile().mkdir();
        }
        File logFile = new File(sLogFile.getParent() + "/logs.csv");
        if (logFile.exists()) {
            logFile.delete();
        }
        createNewFile(logFile);
    }

    public void deleteLogFile() {
        File logFile = new File(sLogFile.getParent() + "/logs.csv");
        if (logFile.exists()) {
            OSSLog.logDebug("delete Log File ... ", false);
            logFile.delete();
        }
    }

    public void deleteLogFileDir() {
        deleteLogFile();
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + LOG_DIR_NAME);
        if (dir.exists()) {
            OSSLog.logDebug("delete Log FileDir ... ", false);
            dir.delete();
        }
    }

    /* access modifiers changed from: private */
    public File getLogFile() {
        File file;
        boolean canStorage = true;
        if (!this.useSdCard || !Environment.getExternalStorageState().equals("mounted")) {
            if (readSystemSpace() <= LOG_MAX_SIZE / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                canStorage = false;
            }
            file = new File(sContext.getFilesDir().getPath() + File.separator + LOG_DIR_NAME);
        } else {
            if (readSDCardSpace() <= LOG_MAX_SIZE / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                canStorage = false;
            }
            file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + LOG_DIR_NAME);
        }
        File logFile = null;
        if (canStorage) {
            if (!file.exists()) {
                file.mkdirs();
            }
            logFile = new File(file.getPath() + "/logs.csv");
            if (!logFile.exists()) {
                createNewFile(logFile);
            }
        }
        return logFile;
    }

    public void createNewFile(File logFile) {
        try {
            logFile.createNewFile();
        } catch (Exception e) {
            OSSLog.logError("Create log file failure !!! " + e.toString(), false);
        }
    }

    /* access modifiers changed from: private */
    public String getFunctionInfo(StackTraceElement[] ste) {
        if (ste == null) {
            return "[" + sLogSDF.format(new Date()) + "]";
        }
        return null;
    }

    public synchronized void write(Object str) {
        if (!(!OSSLog.isEnableLog() || sContext == null || instance == null || sLogFile == null)) {
            if (!sLogFile.exists()) {
                resetLogFile();
            }
            logService.addExecuteTask(new WriteCall(str));
        }
    }

    private static class WriteCall implements Runnable {
        private Object mStr;

        public WriteCall(Object mStr2) {
            this.mStr = mStr2;
        }

        public void run() {
            if (OSSLogToFileUtils.sLogFile != null) {
                OSSLogToFileUtils.getInstance();
                if (OSSLogToFileUtils.getLogFileSize(OSSLogToFileUtils.sLogFile) > OSSLogToFileUtils.LOG_MAX_SIZE) {
                    OSSLogToFileUtils.getInstance().resetLogFile();
                }
                try {
                    PrintWriter pw = new PrintWriter(new FileWriter(OSSLogToFileUtils.sLogFile, true), true);
                    if (pw != null) {
                        if (this.mStr instanceof Throwable) {
                            printEx(pw);
                        } else {
                            pw.println(OSSLogToFileUtils.getInstance().getFunctionInfo((StackTraceElement[]) null) + " - " + this.mStr.toString());
                        }
                        pw.println("------>end of log");
                        pw.println();
                        pw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private PrintWriter printEx(PrintWriter pw) {
            pw.println("crash_time：" + OSSLogToFileUtils.sLogSDF.format(new Date()));
            ((Throwable) this.mStr).printStackTrace(pw);
            return pw;
        }
    }
}
