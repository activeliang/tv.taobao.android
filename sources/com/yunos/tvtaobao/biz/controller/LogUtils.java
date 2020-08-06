package com.yunos.tvtaobao.biz.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.service.UpdateService;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LogUtils {
    /* access modifiers changed from: private */
    public static String LOG_NAME = "atlasUpdate.log";
    /* access modifiers changed from: private */
    public static String TAG = "LogUtils";
    /* access modifiers changed from: private */
    public static File file;
    private static LogUtils instance = null;
    static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private int appid = Process.myPid();
    private String dirPath;
    String logPath = "";
    /* access modifiers changed from: private */
    public Thread logReadThread;
    /* access modifiers changed from: private */
    public LogReadDataRunnable logReadrunnable = null;
    private Thread logThread;
    /* access modifiers changed from: private */
    public UpdateService mContext;
    private Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == 0) {
                LogReadDataRunnable unused = LogUtils.this.logReadrunnable = new LogReadDataRunnable(LogUtils.this.mContext, LogUtils.this.logPath);
                Thread unused2 = LogUtils.this.logReadThread = new Thread(LogUtils.this.logReadrunnable);
                LogUtils.this.logReadThread.start();
            }
        }
    };
    private LogRunnable runnable = null;

    public static LogUtils getInstance(Context mContext2) {
        ZpLogger.v(TAG, "getInstance");
        if (instance == null) {
            instance = new LogUtils(mContext2);
        }
        return instance;
    }

    private LogUtils(Context mContext2) {
        if (Utils.ExistSDCard()) {
            this.dirPath = String.valueOf(mContext2.getExternalCacheDir());
        } else {
            this.dirPath = mContext2.getFilesDir().getPath();
        }
        ZpLogger.v(TAG, "dirPath" + this.dirPath);
    }

    public void start() {
        ZpLogger.v(TAG, "start");
        this.runnable = new LogRunnable(this.appid, this.dirPath);
        this.logThread = new Thread(this.runnable);
        this.logThread.start();
    }

    public void stop() {
        ZpLogger.v(TAG, "stop");
    }

    private static class LogRunnable implements Runnable {
        private String cmds;
        private FileOutputStream fos;
        private String mPid;
        private Process mProcess;
        private BufferedReader mReader;

        public void stopSource() {
            if (this.mProcess != null) {
                this.mProcess.destroy();
                this.mProcess = null;
            }
            try {
                if (this.mReader != null) {
                    this.mReader.close();
                    this.mReader = null;
                }
                if (this.fos != null) {
                    this.fos.close();
                    this.fos = null;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        public LogRunnable(int pid, String dirPath) {
            ZpLogger.v(LogUtils.TAG, "LogRunnable");
            this.mPid = "" + pid;
            try {
                File unused = LogUtils.file = new File(dirPath, LogUtils.LOG_NAME);
                if (LogUtils.file.exists()) {
                    LogUtils.file.delete();
                }
                LogUtils.file.createNewFile();
                this.fos = new FileOutputStream(LogUtils.file, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.cmds = "logcat -v time | grep update";
        }

        public void run() {
            ZpLogger.v(LogUtils.TAG, "LogRunnable-run");
            LogUtils.readWriteLock.writeLock().lock();
            LogUtils.readWriteLock.readLock().lock();
            try {
                this.mProcess = Runtime.getRuntime().exec(new String[]{"sh", "-c", this.cmds});
                this.mReader = new BufferedReader(new InputStreamReader(this.mProcess.getInputStream()), 1024);
                while (true) {
                    String line = this.mReader.readLine();
                    if (line == null) {
                        break;
                    } else if (!(line.length() == 0 || this.fos == null || !line.contains(this.mPid))) {
                        this.fos.write((FormatDate.getFormatTime() + " " + line + "\r\n").getBytes());
                    }
                }
                if (this.mProcess != null) {
                    this.mProcess.destroy();
                    this.mProcess = null;
                }
                try {
                    if (this.mReader != null) {
                        this.mReader.close();
                        this.mReader = null;
                    }
                    if (this.fos != null) {
                        this.fos.close();
                        this.fos = null;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                LogUtils.readWriteLock.writeLock().unlock();
                LogUtils.readWriteLock.readLock().unlock();
            } catch (Exception e) {
                e.printStackTrace();
                if (this.mProcess != null) {
                    this.mProcess.destroy();
                    this.mProcess = null;
                }
                try {
                    if (this.mReader != null) {
                        this.mReader.close();
                        this.mReader = null;
                    }
                    if (this.fos != null) {
                        this.fos.close();
                        this.fos = null;
                    }
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
                LogUtils.readWriteLock.writeLock().unlock();
                LogUtils.readWriteLock.readLock().unlock();
            } catch (Throwable th) {
                if (this.mProcess != null) {
                    this.mProcess.destroy();
                    this.mProcess = null;
                }
                try {
                    if (this.mReader != null) {
                        this.mReader.close();
                        this.mReader = null;
                    }
                    if (this.fos != null) {
                        this.fos.close();
                        this.fos = null;
                    }
                } catch (Exception e23) {
                    e23.printStackTrace();
                }
                LogUtils.readWriteLock.writeLock().unlock();
                LogUtils.readWriteLock.readLock().unlock();
                throw th;
            }
        }
    }

    private static class FormatDate {
        private FormatDate() {
        }

        public static String getFormatDate() {
            return new SimpleDateFormat("yyyyMMddHH").format(Long.valueOf(System.currentTimeMillis()));
        }

        public static String getFormatTime() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(System.currentTimeMillis()));
        }
    }

    public void logReceive(UpdateService mContext2) {
        this.mContext = mContext2;
        ZpLogger.v(TAG, "logReceive");
        if (Utils.ExistSDCard()) {
            this.logPath = mContext2.getExternalCacheDir() + File.separator + LOG_NAME;
        } else {
            this.logPath = mContext2.getFilesDir().getPath() + File.separator + LOG_NAME;
        }
        ZpLogger.v(TAG, "logPath" + this.logPath);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), 10000);
    }

    private static class LogReadDataRunnable implements Runnable {
        private BufferedReader br;
        private FileInputStream fin;
        private String logPath;
        private UpdateService mContext;

        public LogReadDataRunnable(UpdateService context, String mlogPath) {
            ZpLogger.v(LogUtils.TAG, "LogReadDataRunnable");
            this.mContext = context;
            this.logPath = mlogPath;
        }

        public void stopSource() {
            try {
                if (this.br != null) {
                    this.br.close();
                    this.br = null;
                }
                if (this.fin != null) {
                    this.fin.close();
                    this.fin = null;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        public void run() {
            ZpLogger.v(LogUtils.TAG, "LogReadDataRunnable-run");
            String log = "";
            try {
                this.fin = new FileInputStream(LogUtils.file);
                this.br = null;
                StringBuilder sb = new StringBuilder();
                try {
                    this.br = new BufferedReader(new InputStreamReader(this.fin));
                    while (true) {
                        String line = this.br.readLine();
                        if (line != null) {
                            sb.append(line);
                        }
                        break;
                    }
                } catch (IOException e) {
                }
                log = sb.toString();
            } catch (IOException e2) {
                try {
                    e2.printStackTrace();
                } catch (Exception e3) {
                    e3.printStackTrace();
                    try {
                        if (this.br != null) {
                            this.br.close();
                            this.br = null;
                        }
                        if (this.fin != null) {
                            this.fin.close();
                            this.fin = null;
                        }
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                    ZpLogger.v(LogUtils.TAG, "log" + log);
                    Message msg = this.mContext.getmServiceHandler().obtainMessage(106);
                    msg.obj = log;
                    this.mContext.getmServiceHandler().sendMessage(msg);
                } catch (Throwable th) {
                    try {
                        if (this.br != null) {
                            this.br.close();
                            this.br = null;
                        }
                        if (this.fin != null) {
                            this.fin.close();
                            this.fin = null;
                        }
                    } catch (Exception e23) {
                        e23.printStackTrace();
                    }
                    ZpLogger.v(LogUtils.TAG, "log" + log);
                    Message msg2 = this.mContext.getmServiceHandler().obtainMessage(106);
                    msg2.obj = log;
                    this.mContext.getmServiceHandler().sendMessage(msg2);
                    throw th;
                }
            }
            try {
                if (this.br != null) {
                    this.br.close();
                    this.br = null;
                }
                if (this.fin != null) {
                    this.fin.close();
                    this.fin = null;
                }
            } catch (Exception e24) {
                e24.printStackTrace();
            }
            ZpLogger.v(LogUtils.TAG, "log" + log);
            Message msg3 = this.mContext.getmServiceHandler().obtainMessage(106);
            msg3.obj = log;
            this.mContext.getmServiceHandler().sendMessage(msg3);
            ZpLogger.v(LogUtils.TAG, "runfinish");
        }
    }
}
