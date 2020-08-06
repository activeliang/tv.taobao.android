package android.taobao.windvane.packageapp.zipapp;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.WVPackageAppManager;
import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateInfoEnum;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.util.NetWork;
import android.taobao.windvane.util.TaoLog;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.windvane.zipdownload.WVZipBPDownloader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

public class ZipAppDownloaderQueue extends PriorityBlockingQueue {
    private static String TAG = "PackageApp-ZipAppDownloaderQueue";
    private static volatile ZipAppDownloaderQueue instance = null;
    private WVZipBPDownloader currentDownloader = null;
    public int finishedCount = 0;
    private boolean isResetState = false;
    public boolean isTBDownloaderEnabled = true;
    public int needDownloadCount = WVCommonConfig.commonConfig.packageDownloadLimit;
    public int successCount = 0;
    private long taskStartTime = 0;
    private long updateInterval = 600000;

    public static ZipAppDownloaderQueue getInstance() {
        if (instance == null) {
            synchronized (ZipAppDownloaderQueue.class) {
                if (instance == null) {
                    instance = new ZipAppDownloaderQueue();
                }
            }
        }
        return instance;
    }

    public void startPriorityDownLoader() {
        if (getInstance().size() <= this.needDownloadCount) {
            this.needDownloadCount = getInstance().size();
        } else {
            this.needDownloadCount = WVCommonConfig.commonConfig.packageDownloadLimit;
        }
        this.finishedCount = 0;
        this.successCount = 0;
        this.isResetState = false;
        this.currentDownloader = null;
        this.updateInterval = WVCommonConfig.commonConfig.updateInterval * 2;
        doTask();
    }

    private boolean doTask() {
        if (GlobalConfig.context != null) {
            if (isAppBackground(GlobalConfig.context)) {
                TaoLog.i(TAG, "doTask app is background");
            } else {
                TaoLog.i(TAG, "doTask app is forground");
            }
        }
        if (getInstance().size() == 0 || this.finishedCount >= this.needDownloadCount) {
            this.finishedCount = 0;
            this.isResetState = false;
            return false;
        }
        ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(((ZipDownloaderComparable) getInstance().poll()).getAppName());
        if (appInfo == null) {
            updateState();
            return false;
        }
        if (!(this.taskStartTime == 0 || this.finishedCount == 0 || appInfo == null || !TaoLog.getLogStatus())) {
            TaoLog.d(TAG, appInfo.name + " doTask use time(ms) : " + (System.currentTimeMillis() - this.taskStartTime));
        }
        this.taskStartTime = System.currentTimeMillis();
        if (appInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE || appInfo.status == ZipAppConstants.ZIP_REMOVED) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "startUpdateApps:[updateApps] 卸载[" + appInfo + "]");
            }
            try {
                int resultcode = ZipAppManager.getInstance().unInstall(appInfo);
                if (resultcode != ZipAppResultCode.SECCUSS && TaoLog.getLogStatus()) {
                    TaoLog.w(TAG, "resultcode:" + resultcode + "[updateApps] [" + appInfo + "]" + " unInstall fail ");
                }
                updateState();
                return true;
            } catch (Exception e) {
            }
        }
        if (!isContinueUpdate(appInfo)) {
            updateState();
            return false;
        } else if (appInfo.s == appInfo.installedSeq && appInfo.status == ZipAppConstants.ZIP_NEWEST) {
            updateState();
            return false;
        } else if (appInfo.installedSeq != 0 || WVPackageAppCleanup.getInstance().needInstall(appInfo) || !WVCommonConfig.commonConfig.isCheckCleanup) {
            this.currentDownloader = new WVZipBPDownloader(appInfo.getZipUrl(), WVPackageAppManager.getInstance(), appInfo.v.equals(appInfo.installedVersion) ? 2 : 4, appInfo);
            try {
                this.currentDownloader.execute(new Void[0]);
                return true;
            } catch (Exception e2) {
                TaoLog.w(TAG, "update app error : " + appInfo.name);
                updateState();
                return false;
            }
        } else {
            appInfo.status = ZipAppConstants.ZIP_REMOVED;
            if (appInfo.isOptional) {
                appInfo.s = 0;
                appInfo.v = "0";
            }
            updateState();
            return false;
        }
    }

    public void startUpdateAppsTask() {
        if (WVCommonConfig.commonConfig.packageAppStatus != 2) {
            TaoLog.i(TAG, "not update zip, packageAppStatus is : " + WVCommonConfig.commonConfig.packageAppStatus);
        } else if (GlobalConfig.context != null && isAppBackground(GlobalConfig.context)) {
            TaoLog.i(TAG, "not update zip, app is background");
        } else if (isUpdateFinish()) {
            for (Map.Entry<String, ZipAppInfo> entry : ConfigManager.getLocGlobalConfig().getAppsTable().entrySet()) {
                ZipAppInfo appInfo = entry.getValue();
                if ((appInfo.installedSeq != 0 && (appInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE || appInfo.status == ZipAppConstants.ZIP_REMOVED)) || appInfo.installedSeq < appInfo.s) {
                    int p = appInfo.getPriority();
                    if (appInfo.isPreViewApp) {
                        p = 10;
                    }
                    getInstance().offer(new ZipDownloaderComparable(appInfo.name, p));
                }
            }
            getInstance().removeDuplicate();
            startPriorityDownLoader();
        } else if (this.currentDownloader == null || this.currentDownloader.getStatus() == AsyncTask.Status.FINISHED) {
            doTask();
        } else if (this.updateInterval < System.currentTimeMillis() - this.taskStartTime) {
            this.currentDownloader.cancel(true);
            this.currentDownloader = null;
            doTask();
        }
    }

    public void resetState() {
        if (!(WVMonitorService.getPackageMonitorInterface() == null || this.finishedCount == 0)) {
            WVMonitorService.getPackageMonitorInterface().commitPackageQueueInfo("1", (long) this.finishedCount, (long) this.successCount);
            TaoLog.i(TAG, "packageAppQueue s : " + this.successCount + "f : " + this.finishedCount);
        }
        this.finishedCount = 0;
        this.successCount = 0;
        this.isResetState = true;
        this.currentDownloader = null;
        this.needDownloadCount = WVCommonConfig.commonConfig.packageDownloadLimit;
    }

    public boolean isUpdateFinish() {
        return this.isResetState || getInstance().size() == 0 || this.needDownloadCount == 0 || this.finishedCount >= this.needDownloadCount;
    }

    public synchronized void updateState() {
        if (isUpdateFinish()) {
            resetState();
        } else if (!this.isResetState) {
            this.currentDownloader = null;
            doTask();
        }
    }

    public synchronized void updateFinshCount(boolean isSuccess) {
        if (!this.isResetState) {
            if (isSuccess) {
                this.successCount++;
            }
            this.finishedCount++;
        }
    }

    @SuppressLint({"NewApi"})
    public static boolean isContinueUpdate(ZipAppInfo appInfo) {
        if (appInfo == null) {
            return false;
        }
        if (appInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_REACT || appInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_UNKNOWN) {
            appInfo.status = ZipAppConstants.ZIP_REMOVED;
            ConfigManager.updateGlobalConfig(appInfo, (String) null, false);
            return true;
        } else if (NetWork.isWiFiActive()) {
            return true;
        } else {
            if (appInfo.getIs2GUpdate() || appInfo.getIs3GUpdate()) {
                return true;
            }
            if (!TaoLog.getLogStatus()) {
                return false;
            }
            TaoLog.i(TAG, "updateAllApps: can not install app [" + appInfo.name + "] network is not wifi");
            return false;
        }
    }

    public void removeDuplicate() {
        try {
            Iterator it = getInstance().iterator();
            HashSet set = new HashSet();
            List newList = new ArrayList();
            while (it.hasNext()) {
                ZipDownloaderComparable oldObj = (ZipDownloaderComparable) it.next();
                if (set.add(oldObj.getAppName())) {
                    newList.add(oldObj);
                }
            }
            getInstance().clear();
            getInstance().addAll(newList);
        } catch (Exception e) {
        }
    }

    public static boolean isAppBackground(Context context) {
        for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == 400) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
