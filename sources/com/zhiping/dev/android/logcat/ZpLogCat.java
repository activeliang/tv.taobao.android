package com.zhiping.dev.android.logcat;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.util.Log;
import android.widget.Toast;
import com.zhiping.dev.android.logcat.ActionMenu;
import com.zhiping.dev.android.logcat.config.IExtCfg;
import com.zhiping.dev.android.logcat.config.IFileNameCfg;
import com.zhiping.dev.android.logcat.config.IOssClientCfg;
import com.zhiping.dev.android.logcat.config.IOssStorageCfg;
import com.zhiping.dev.android.logcat.config.ISaveCfg;
import com.zhiping.dev.android.oss.BaseIClient;
import com.zhiping.dev.android.oss.IClient;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ZpLogCat {
    /* access modifiers changed from: private */
    public static final String TAG = ZpLogCat.class.getSimpleName();
    private static ZpLogCat instance;
    private ALCB alcb = new ALCB();
    private Application application;
    private LogcatConfig logcatConfig;
    private LogcatDumper logcatDumper;
    private LogcatToast logcatToast;

    private ZpLogCat(Application app) {
        this.application = app;
        this.application.registerActivityLifecycleCallbacks(this.alcb);
    }

    public static ZpLogCat getInstance(Application app) {
        if (instance == null) {
            synchronized (ZpLogCat.class) {
                if (instance == null) {
                    if (app != null) {
                        instance = new ZpLogCat(app);
                    } else {
                        throw new RuntimeException("must call getInstance with Application once !");
                    }
                }
            }
        }
        return instance;
    }

    public ZpLogCat doConfig(ISaveCfg saveCfg, IOssClientCfg ossCfg, IOssStorageCfg ossStorageCfg, IFileNameCfg fileNameCfg, IExtCfg extCfg) {
        final Thread.UncaughtExceptionHandler originalHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                ZpLogCat.this.recordException(true, t, e, new String[0]);
                try {
                    if (ZpLogCat.this.getTurnOnState()) {
                        ZpLogCat.this.dump(false);
                        ZpLogCat.this.uploadLogs(false, true, 30000);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (originalHandler != null) {
                    originalHandler.uncaughtException(t, e);
                }
            }
        });
        this.logcatConfig = new LogcatConfig(this.application);
        this.logcatDumper = new LogcatDumper(this.logcatConfig);
        this.logcatToast = new LogcatToast(this.application);
        if (saveCfg != null) {
            this.logcatConfig.setSaveCfg(saveCfg);
        }
        if (ossCfg != null) {
            this.logcatConfig.setOssCfg(ossCfg);
        }
        if (fileNameCfg != null) {
            this.logcatConfig.setFileNameCfg(fileNameCfg);
        }
        if (ossStorageCfg != null) {
            this.logcatConfig.setOssStorageCfg(ossStorageCfg);
        }
        if (extCfg != null) {
            this.logcatConfig.setExtCfg(extCfg);
        }
        return this;
    }

    public boolean getTurnOnState() {
        return this.application.getSharedPreferences("ZpLogCat", 0).getBoolean("turnOnState", false);
    }

    public void setTurnOnState(boolean state) {
        this.application.getSharedPreferences("ZpLogCat", 0).edit().putBoolean("turnOnState", state).commit();
    }

    public Dialog getMenuDlg(final Activity activity) {
        final ActionMenu actionMenu = new ActionMenu(activity);
        actionMenu.cfgMenu(new ActionMenu.IMenuItem() {
            public String getItemTxt() {
                if (ZpLogCat.this.getTurnOnState()) {
                    return "关闭日志记录";
                }
                return "打开日志上报";
            }

            public void onClick() {
                boolean state = ZpLogCat.this.getTurnOnState();
                ZpLogCat.this.setTurnOnState(!state);
                if (state) {
                    Toast.makeText(activity, "日志记录已关闭", 0).show();
                    ZpLogCat.this.dump(false);
                    ZpLogCat.this.tipTurnOnMsg(false, "日志记录中...\n\n");
                } else {
                    Toast.makeText(activity, "日志记录已打开", 0).show();
                    ZpLogCat.this.dump(true);
                    ZpLogCat.this.tipTurnOnMsg(true, "日志记录中...\n\n按下【\"菜单键\"】关闭");
                }
                actionMenu.dismiss();
            }
        }, new ActionMenu.IMenuItem() {
            public String getItemTxt() {
                return "查看记录的日志文件";
            }

            public void onClick() {
                ZpLogCat.this.showCollectLogDlg(activity);
            }
        }).show();
        return actionMenu;
    }

    public void dump(boolean startOrStop) {
        if (this.logcatDumper == null) {
            throw new RuntimeException(" logcatDumper is Null !");
        } else if (startOrStop) {
            this.logcatDumper.start();
        } else {
            this.logcatDumper.stop();
        }
    }

    public void tipTurnOnMsg(boolean showOrDismiss, String msg) {
        if (this.logcatToast == null) {
            throw new RuntimeException(" logcatToast is Null !");
        } else if (showOrDismiss) {
            this.logcatToast.show(msg);
        } else {
            this.logcatToast.dismiss();
        }
    }

    public Dialog showCollectLogDlg(Activity activity) {
        Dialog dialog = new FileListDialog(activity, this.logcatConfig);
        dialog.show();
        return dialog;
    }

    public void uploadLogs(boolean onlyLast, boolean wait4finish, long waitTimeOut) {
        Log.d(TAG, "uploadLogs(" + onlyLast + "," + wait4finish + "," + waitTimeOut + ") start !");
        List<File> logs = this.logcatConfig.listLogs();
        if (logs != null && !logs.isEmpty()) {
            Log.d(TAG, "uploadLogs exist log file count:" + logs.size());
            if (!onlyLast) {
                final List<File> finalNeedUploadFileList = new ArrayList<>();
                final AtomicInteger needUploadFileCount = new AtomicInteger(logs.size());
                for (int i = 0; i < logs.size(); i++) {
                    final File file = logs.get(i);
                    Log.d(TAG, "uploadLogs check exist 4 log file:" + file.getName());
                    this.logcatConfig.getOssClient().isObjExist(this.logcatConfig.getBucketName(), this.logcatConfig.getObjectKey(file), new BaseIClient.BaseIClientCallBack() {
                        public void onSuccess(Object... objects) {
                            needUploadFileCount.decrementAndGet();
                            if (objects == null || objects.length <= 0 || !(objects[0] instanceof Boolean) || !objects[0].booleanValue()) {
                                finalNeedUploadFileList.add(file);
                                Log.d(ZpLogCat.TAG, "uploadLogs check exist 4 log file:" + file.getName() + " not exist " + needUploadFileCount.get());
                                return;
                            }
                            Log.d(ZpLogCat.TAG, "uploadLogs check exist 4 log file:" + file.getName() + " exist " + needUploadFileCount.get());
                        }

                        public void onFailure(Object... objects) {
                            needUploadFileCount.decrementAndGet();
                            finalNeedUploadFileList.add(file);
                            Log.d(ZpLogCat.TAG, "uploadLogs check exist 4 log file:" + file.getName() + " not exist " + needUploadFileCount.get());
                        }

                        public boolean onSuccessInMainThread() {
                            return false;
                        }

                        public boolean onFailureInMainThread() {
                            return false;
                        }
                    });
                }
                if (wait4finish) {
                    Log.d(TAG, "uploadLogs enter loop [check exist] !");
                    long loopEnterTime = System.currentTimeMillis();
                    long lastLogOutTime = loopEnterTime;
                    while (true) {
                        long nowTime = System.currentTimeMillis();
                        if (nowTime - lastLogOutTime > 1000) {
                            lastLogOutTime = nowTime;
                            Log.d(TAG, "uploadLogs enter loop check : " + needUploadFileCount.get());
                        }
                        if (needUploadFileCount.get() == 0 || (waitTimeOut > 0 && nowTime - loopEnterTime > waitTimeOut)) {
                            break;
                        }
                    }
                }
                Log.d(TAG, "uploadLogs need upload log file count:" + finalNeedUploadFileList.size());
                AtomicInteger uploadFileCount = new AtomicInteger(finalNeedUploadFileList.size());
                for (int i2 = 0; i2 < finalNeedUploadFileList.size(); i2++) {
                    final File file2 = finalNeedUploadFileList.get(i2);
                    this.logcatConfig.appendExtInfo(file2, new String[0]);
                    Log.d(TAG, "uploadLogs upload file : " + file2.getName());
                    final AtomicInteger atomicInteger = uploadFileCount;
                    this.logcatConfig.getOssClient().put(file2, this.logcatConfig.getBucketName(), this.logcatConfig.getObjectKey(file2), (IClient.CallBack) new BaseIClient.BaseIClientCallBack() {
                        public void onSuccess(Object... objects) {
                            atomicInteger.decrementAndGet();
                            Log.d(ZpLogCat.TAG, "uploadLogs upload file done : " + file2.getName());
                        }

                        public void onFailure(Object... objects) {
                            atomicInteger.decrementAndGet();
                            Log.d(ZpLogCat.TAG, "uploadLogs upload file error : " + file2.getName());
                        }

                        public boolean onSuccessInMainThread() {
                            return false;
                        }

                        public boolean onFailureInMainThread() {
                            return false;
                        }
                    });
                }
                if (wait4finish) {
                    Log.d(TAG, "uploadLogs enter loop [check upload] !");
                    long loopEnterTime2 = System.currentTimeMillis();
                    long lastLogOutTime2 = loopEnterTime2;
                    while (true) {
                        long nowTime2 = System.currentTimeMillis();
                        if (nowTime2 - lastLogOutTime2 > 1000) {
                            lastLogOutTime2 = nowTime2;
                            Log.d(TAG, "uploadLogs enter loop check : " + uploadFileCount.get());
                        }
                        if (uploadFileCount.get() == 0 || (waitTimeOut > 0 && nowTime2 - loopEnterTime2 > waitTimeOut)) {
                            break;
                        }
                    }
                }
            } else {
                File last = logs.get(0);
                final AtomicBoolean qryDone = new AtomicBoolean(false);
                final AtomicBoolean exits = new AtomicBoolean(false);
                this.logcatConfig.getOssClient().isObjExist(this.logcatConfig.getBucketName(), this.logcatConfig.getObjectKey(last), new BaseIClient.BaseIClientCallBack() {
                    public void onSuccess(Object... objects) {
                        qryDone.set(true);
                        if (objects != null && objects.length > 0 && (objects[0] instanceof Boolean) && objects[0].booleanValue()) {
                            exits.set(true);
                        }
                    }

                    public void onFailure(Object... objects) {
                        qryDone.set(true);
                    }

                    public boolean onSuccessInMainThread() {
                        return false;
                    }

                    public boolean onFailureInMainThread() {
                        return false;
                    }
                });
                if (wait4finish) {
                    Log.d(TAG, "uploadLogs enter loop [check upload] !");
                    long loopEnterTime3 = System.currentTimeMillis();
                    long lastLogOutTime3 = loopEnterTime3;
                    while (true) {
                        long nowTime3 = System.currentTimeMillis();
                        if (nowTime3 - lastLogOutTime3 > 1000) {
                            lastLogOutTime3 = nowTime3;
                            Log.d(TAG, "uploadLogs enter loop check : " + qryDone.get());
                        }
                        if (qryDone.get() || (waitTimeOut > 0 && System.currentTimeMillis() - loopEnterTime3 > waitTimeOut)) {
                            break;
                        }
                    }
                }
                if (exits.get()) {
                    qryDone.set(false);
                    this.logcatConfig.appendExtInfo(last, new String[0]);
                    this.logcatConfig.getOssClient().put(last, this.logcatConfig.getBucketName(), this.logcatConfig.getObjectKey(last), (IClient.CallBack) new BaseIClient.BaseIClientCallBack() {
                        public void onSuccess(Object... objects) {
                            qryDone.set(true);
                        }

                        public void onFailure(Object... objects) {
                            qryDone.set(true);
                        }

                        public boolean onSuccessInMainThread() {
                            return false;
                        }

                        public boolean onFailureInMainThread() {
                            return false;
                        }
                    });
                    if (wait4finish) {
                        Log.d(TAG, "uploadLogs enter loop [check upload] !");
                        long loopEnterTime4 = System.currentTimeMillis();
                        long lastLogOutTime4 = loopEnterTime4;
                        while (true) {
                            long nowTime4 = System.currentTimeMillis();
                            if (nowTime4 - lastLogOutTime4 > 1000) {
                                lastLogOutTime4 = nowTime4;
                                Log.d(TAG, "uploadLogs enter loop check : " + qryDone.get());
                            }
                            if (qryDone.get() || (waitTimeOut > 0 && System.currentTimeMillis() - loopEnterTime4 > waitTimeOut)) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        Log.d(TAG, "uploadLogs(" + onlyLast + "," + wait4finish + ") over !");
    }

    public void recordException(final boolean wait4LogFinish, Thread thread, Throwable throwable, String... extParams) {
        File logFile = new File(this.logcatConfig.getLogDir() + File.separator + this.logcatConfig.genLogFileName());
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(logFile));
            throwable.printStackTrace(printWriter);
            if (thread != null) {
                printWriter.println("");
                printWriter.println("--------------------------------------------");
                printWriter.println("threadName:" + thread.getName());
            }
            printWriter.flush();
            printWriter.close();
            this.logcatConfig.appendExtInfo(logFile, extParams);
            final AtomicBoolean uploadFinishFlag = new AtomicBoolean(false);
            this.logcatConfig.getOssClient().put(logFile, this.logcatConfig.getBucketName(), this.logcatConfig.getObjectKey(logFile), (IClient.CallBack) new BaseIClient.BaseIClientCallBack() {
                public void onSuccess(Object... objects) {
                    super.onSuccess(objects);
                    if (wait4LogFinish) {
                        uploadFinishFlag.set(true);
                    }
                }

                public void onFailure(Object... objects) {
                    super.onFailure(objects);
                    if (wait4LogFinish) {
                        uploadFinishFlag.set(true);
                    }
                }

                public boolean onSuccessInMainThread() {
                    return !wait4LogFinish;
                }

                public boolean onFailureInMainThread() {
                    return !wait4LogFinish;
                }
            });
            if (wait4LogFinish) {
                do {
                } while (!uploadFinishFlag.get());
            }
            Log.d(TAG, "recordException finish !");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void destroy() {
        if (instance != null) {
            synchronized (ZpLogCat.class) {
                if (instance != null) {
                    instance = null;
                }
            }
        }
        this.logcatDumper.stop();
        this.logcatDumper = null;
        this.logcatToast.dismiss();
        this.logcatToast = null;
        this.logcatConfig.clearCfg();
        this.logcatConfig = null;
        this.application.unregisterActivityLifecycleCallbacks(this.alcb);
        this.application = null;
    }

    /* access modifiers changed from: package-private */
    public ALCB getAlcb() {
        return this.alcb;
    }
}
