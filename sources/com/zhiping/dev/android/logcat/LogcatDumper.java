package com.zhiping.dev.android.logcat;

import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

class LogcatDumper {
    /* access modifiers changed from: private */
    public static final String TAG = LogcatDumper.class.getSimpleName();
    /* access modifiers changed from: private */
    public LogcatConfig logcatConfig;
    /* access modifiers changed from: private */
    public AtomicBoolean startDoing = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public AtomicBoolean stopFlag = new AtomicBoolean(false);

    public LogcatDumper(LogcatConfig logcatConfig2) {
        this.logcatConfig = logcatConfig2;
    }

    public synchronized void start() {
        if (!this.startDoing.getAndSet(true)) {
            this.stopFlag.set(false);
            new Thread(new Runnable() {
                public void run() {
                    FileOutputStream fos;
                    Process logcatProcess;
                    try {
                        File logDir = LogcatDumper.this.logcatConfig.getLogDir();
                        if (!logDir.exists()) {
                            logDir.mkdirs();
                        }
                        File logFile = new File(logDir + File.separator + LogcatDumper.this.logcatConfig.genLogFileName());
                        if (!logFile.exists()) {
                            logFile.createNewFile();
                        }
                        Log.i(LogcatDumper.TAG, "dump start > " + logFile.getAbsolutePath());
                        fos = new FileOutputStream(logFile);
                        logcatProcess = null;
                        logcatProcess = Runtime.getRuntime().exec("logcat -v time");
                        InputStream is = logcatProcess.getInputStream();
                        byte[] buffer = new byte[1024];
                        long total = 0;
                        long lastLogTime = System.currentTimeMillis();
                        long j = lastLogTime;
                        boolean breakFlag = false;
                        while (true) {
                            int len = is.read(buffer);
                            if (len <= 0) {
                                break;
                            }
                            total += (long) len;
                            fos.write(buffer, 0, len);
                            fos.flush();
                            long nowLogTime = System.currentTimeMillis();
                            if (nowLogTime - lastLogTime > 1000) {
                                lastLogTime = nowLogTime;
                                Log.i(LogcatDumper.TAG, "dump size : " + LogcatConfig.getFitSize(total));
                            }
                            if (breakFlag) {
                                break;
                            } else if (LogcatDumper.this.stopFlag.get()) {
                                breakFlag = true;
                                Log.i(LogcatDumper.TAG, "dump end > " + logFile.getAbsolutePath());
                            }
                        }
                        fos.flush();
                        fos.close();
                        is.close();
                        logcatProcess.destroy();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        LogcatDumper.this.startDoing.set(false);
                        return;
                    }
                    LogcatDumper.this.startDoing.set(false);
                }
            }).start();
        }
    }

    public synchronized void stop() {
        this.stopFlag.set(true);
    }
}
