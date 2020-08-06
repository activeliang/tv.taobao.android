package com.edge.pcdn;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import com.taobao.ju.track.constants.Constants;
import java.io.File;
import java.util.Date;

public class LiveMgr {
    /* access modifiers changed from: private */
    public String appname = null;
    /* access modifiers changed from: private */
    public String appversion = null;
    private Handler checkHandler;
    /* access modifiers changed from: private */
    public String liveCacheDir;
    /* access modifiers changed from: private */
    public String liveClientId;
    private Context liveContext;
    /* access modifiers changed from: private */
    public String liveExt;
    /* access modifiers changed from: private */
    public String livePid;
    /* access modifiers changed from: private */
    public String pcdnversion = Constants.PARAM_OUTER_SPM_NONE;
    /* access modifiers changed from: private */
    public String soPath = "";
    /* access modifiers changed from: private */
    public int startresult = 99;

    public int start(Context context, String clientId, String cacheDir, String pid, String ext) {
        try {
            this.soPath = context.getDir("libs", 0).getAbsolutePath() + WVNativeCallbackUtil.SEPERATER + ControlMgr.pcdnLiveFilename;
            this.appname = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
            this.appversion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            this.liveContext = context;
            this.liveClientId = clientId;
            this.liveCacheDir = cacheDir;
            this.livePid = pid;
            this.liveExt = ext;
        } catch (Throwable throwable) {
            PcdnLog.w(PcdnLog.toString(throwable));
        }
        if (this.checkHandler == null) {
            this.checkHandler = new CheckHandler(this.liveContext.getMainLooper());
        }
        if (ConfigManager.getLiveAccStart() == 1) {
            loadAcc();
            return 0;
        }
        check();
        return -1;
    }

    public String pcdnAddress(String url, String type, int rank, String ext) {
        PcdnLog.i("PcdnAddress in:" + url);
        if (ext == null) {
            ext = "";
        }
        try {
            String ext2 = ext.trim();
            if (!"".equals(ext2)) {
                ext2 = "&" + ext2;
            }
            String pcdnUrl = PcdnLive.PCDNAddress(url, "st=" + type + "&rank=" + rank + "&cid=" + this.liveClientId + ext2);
            if (pcdnUrl == null || "".equals(pcdnUrl)) {
                reportAddressLog(type, -5, url);
            } else {
                int rs = Integer.parseInt(pcdnUrl.substring(0, 3));
                pcdnUrl = pcdnUrl.substring(3);
                reportAddressLog(type, rs, pcdnUrl);
            }
            PcdnLog.i("PcdnAddress out:" + pcdnUrl);
            return pcdnUrl;
        } catch (NumberFormatException e) {
            reportAddressLog(type, -4, url);
            PcdnLog.i("PCDNAddress out:" + url);
            return url;
        } catch (Throwable th) {
            reportAddressLog(type, -3, url);
            PcdnLog.i("PCDNAddress out:" + url);
            return url;
        }
    }

    public String getVersion() {
        try {
            return PcdnLive.getVersion();
        } catch (Throwable th) {
            return Constants.PARAM_OUTER_SPM_NONE;
        }
    }

    public String pcdnGet(String key) {
        try {
            return PcdnLive.PCDNGet(key);
        } catch (Throwable th) {
            return "";
        }
    }

    public int pcdnSet(String keyvalue) {
        try {
            return PcdnLive.PCDNSet(keyvalue);
        } catch (Throwable th) {
            return -1;
        }
    }

    public void pcdnStop() {
        try {
            PcdnLive.stop();
            PcdnLog.i("PcdnLive stopped");
        } catch (Error e) {
            PcdnLog.w("pcdnStop:" + PcdnLog.toString(e));
        } catch (Exception e2) {
            PcdnLog.w("pcdnStop:" + PcdnLog.toString(e2));
        }
        this.startresult = 7;
        ReportLogManager.getInstance().reportStartLog(PcdnType.LIVE, this.liveClientId, this.pcdnversion, 7, this.appname, this.appversion, this.liveCacheDir, this.livePid, this.liveExt);
    }

    public void pcdnExit() {
        try {
            PcdnLive.exit();
            PcdnLog.i("PcdnVod exited");
        } catch (Error e) {
            PcdnLog.w("pcdnExit:" + PcdnLog.toString(e));
        } catch (Exception e2) {
            PcdnLog.w("pcdnExit:" + PcdnLog.toString(e2));
        }
        this.startresult = 13;
        ReportLogManager.getInstance().reportStartLog(PcdnType.VOD, this.liveClientId, this.pcdnversion, 13, this.appname, this.appversion, this.liveCacheDir, this.livePid, this.liveExt);
    }

    public void pcdnStopFromSer() {
        try {
            PcdnLive.stop();
            PcdnLog.i("PcdnVod stopped from server");
        } catch (Error e) {
            PcdnLog.w("pcdnStop:" + PcdnLog.toString(e));
        } catch (Exception e2) {
            PcdnLog.w("pcdnStop:" + PcdnLog.toString(e2));
        }
        ReportLogManager.getInstance().reportStartLog(PcdnType.LIVE, this.liveClientId, this.pcdnversion, 8, this.appname, this.appversion, this.liveCacheDir, this.livePid, this.liveExt);
    }

    /* access modifiers changed from: private */
    public void startAcc() {
        int rs;
        try {
            rs = PcdnLive.start(this.liveClientId, this.liveCacheDir, this.livePid, this.liveExt);
            PcdnLog.i("PcndLive start " + rs);
        } catch (Throwable th) {
            rs = 11;
            PcdnLog.e("PcndLive start failed Error");
        }
        this.startresult = rs;
        ReportLogManager.getInstance().reportStartLog(PcdnType.LIVE, this.liveClientId, this.pcdnversion, rs, this.appname, this.appversion, this.liveCacheDir, this.livePid, this.liveExt);
    }

    private void reportAddressLog(String type, int code, String url) {
        if (ConfigManager.getLiveAccLogSwitch() == 1) {
            PcdnLog.d("add pcdnaddress log");
            ReportLogManager.getInstance().reportAddressLog(type, this.liveClientId, this.pcdnversion, code, this.startresult, this.appname, this.appversion, url);
        }
    }

    /* access modifiers changed from: private */
    public void loadAcc() {
        new Thread(new loadLiveRunable()).start();
    }

    private class loadLiveRunable implements Runnable {
        private loadLiveRunable() {
        }

        public void run() {
            File accfile = new File(LiveMgr.this.soPath);
            if (accfile.exists()) {
                LiveMgr.this.loadSoFile(accfile);
            } else {
                try {
                    System.loadLibrary("pcdn_acc_live");
                    PcdnLog.i("PcndLive load success");
                    LiveMgr.this.startAcc();
                } catch (Throwable e) {
                    PcdnLog.i("PcndLive load failed");
                    int unused = LiveMgr.this.startresult = 10;
                    ReportLogManager.getInstance().reportStartLog(PcdnType.LIVE, LiveMgr.this.liveClientId, LiveMgr.this.pcdnversion, 10, LiveMgr.this.appname, LiveMgr.this.appversion, LiveMgr.this.liveCacheDir, LiveMgr.this.livePid, LiveMgr.this.liveExt);
                    e.printStackTrace();
                }
            }
            LiveMgr.this.check();
        }
    }

    /* access modifiers changed from: private */
    public void loadSoFile(File accfile) {
        try {
            System.load(accfile.getAbsolutePath());
            PcdnLog.i("PcndLive so load success");
            startAcc();
        } catch (Throwable e) {
            this.startresult = 6;
            ReportLogManager.getInstance().reportStartLog(PcdnType.LIVE, this.liveClientId, this.pcdnversion, 6, this.appname, this.appversion, this.liveCacheDir, this.livePid, this.liveExt);
            PcdnLog.i("PcndLive SO load error");
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void check() {
        try {
            this.pcdnversion = PcdnLive.getVersion();
        } catch (Throwable th) {
        }
        long checkTimestamp = ConfigManager.getLiveCheckTimestamp();
        long checkNext = ConfigManager.getLiveNextCheck();
        long nowTimestamp = new Date().getTime();
        PcdnLog.d("nowTimestamp - checkTimestamp = " + (nowTimestamp - checkTimestamp));
        long delayMillis = (1000 * checkNext) - (nowTimestamp - checkTimestamp);
        PcdnLog.d("nextcheck:" + checkNext + "delayMillis:" + delayMillis);
        if (delayMillis <= 0) {
            new Thread(new ControlMgr(this.liveContext, this.liveClientId, this.livePid, PcdnType.LIVE, this.pcdnversion, this.appname, this.appversion, this.checkHandler)).start();
        }
    }

    private class CheckHandler extends Handler {
        public CheckHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (LiveMgr.this.startresult == 10) {
                    try {
                        PcdnLog.d("check 完启动内核");
                        LiveMgr.this.loadAcc();
                    } catch (Throwable th) {
                    }
                }
            } else if (msg.what == 0) {
                LiveMgr.this.pcdnStopFromSer();
            }
        }
    }
}
