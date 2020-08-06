package com.edge.pcdn;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import com.taobao.ju.track.constants.Constants;
import java.io.File;
import java.util.Date;

public class AccMgr {
    /* access modifiers changed from: private */
    public String accCacheDir;
    /* access modifiers changed from: private */
    public String accClientId;
    private Context accContext;
    /* access modifiers changed from: private */
    public String accExt;
    /* access modifiers changed from: private */
    public String accPid;
    /* access modifiers changed from: private */
    public String appname = null;
    /* access modifiers changed from: private */
    public String appversion = null;
    private Handler checkHandler;
    /* access modifiers changed from: private */
    public String pcdnversion = Constants.PARAM_OUTER_SPM_NONE;
    /* access modifiers changed from: private */
    public String soPath = "";
    /* access modifiers changed from: private */
    public int startresult = 99;

    public int start(Context context, String clientId, String cacheDir, String pid, String ext) {
        try {
            this.soPath = context.getDir("libs", 0).getAbsolutePath() + WVNativeCallbackUtil.SEPERATER + ControlMgr.pcdnAccFilename;
            this.appname = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
            this.appversion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Throwable throwable) {
            PcdnLog.w(PcdnLog.toString(throwable));
        }
        this.accContext = context;
        this.accClientId = clientId;
        this.accCacheDir = cacheDir;
        this.accPid = pid;
        this.accExt = ext;
        if (this.checkHandler == null) {
            this.checkHandler = new CheckHandler(this.accContext.getMainLooper());
        }
        if (ConfigManager.getVodAccStart() == 1) {
            loadAcc();
            return 0;
        }
        PcdnLog.i("Are not allow to start");
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
            String pcdnUrl = PcdnAcc.PCDNAddress(url, "st=" + type + "&rank=" + rank + "&cid=" + this.accClientId + ext2);
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
            return PcdnAcc.getVersion();
        } catch (Throwable th) {
            return Constants.PARAM_OUTER_SPM_NONE;
        }
    }

    public String pcdnGet(String key) {
        try {
            return PcdnAcc.PCDNGet(key);
        } catch (Throwable th) {
            return "";
        }
    }

    public int pcdnSet(String keyvalue) {
        try {
            return PcdnAcc.PCDNSet(keyvalue);
        } catch (Throwable th) {
            return -1;
        }
    }

    public void pcdnStop() {
        try {
            PcdnAcc.stop();
            PcdnLog.i("PcdnVod stopped");
        } catch (Error e) {
            PcdnLog.w("pcdnStop:" + PcdnLog.toString(e));
        } catch (Exception e2) {
            PcdnLog.w("pcdnStop:" + PcdnLog.toString(e2));
        }
        this.startresult = 7;
        ReportLogManager.getInstance().reportStartLog(PcdnType.VOD, this.accClientId, this.pcdnversion, 7, this.appname, this.appversion, this.accCacheDir, this.accPid, this.accExt);
    }

    public void pcdnExit() {
        try {
            PcdnAcc.exit();
            PcdnLog.i("PcdnVod exited");
        } catch (Error e) {
            PcdnLog.w("pcdnExit:" + PcdnLog.toString(e));
        } catch (Exception e2) {
            PcdnLog.w("pcdnExit:" + PcdnLog.toString(e2));
        }
        this.startresult = 13;
        ReportLogManager.getInstance().reportStartLog(PcdnType.VOD, this.accClientId, this.pcdnversion, 13, this.appname, this.appversion, this.accCacheDir, this.accPid, this.accExt);
    }

    /* access modifiers changed from: private */
    public void pcdnStopFromSer() {
        try {
            PcdnAcc.stop();
            PcdnLog.i("PcdnVod stopped from server");
        } catch (Error e) {
            PcdnLog.w("pcdnStop:" + PcdnLog.toString(e));
        } catch (Exception e2) {
            PcdnLog.w("pcdnStop:" + PcdnLog.toString(e2));
        }
        this.startresult = 8;
        ReportLogManager.getInstance().reportStartLog(PcdnType.VOD, this.accClientId, this.pcdnversion, 8, this.appname, this.appversion, this.accCacheDir, this.accPid, this.accExt);
    }

    /* access modifiers changed from: private */
    public void startAcc() {
        int rs;
        try {
            rs = PcdnAcc.start(this.accClientId, this.accCacheDir, this.accPid, this.accExt);
            PcdnLog.i("PcdnAcc start " + rs);
        } catch (Throwable th) {
            rs = 11;
            PcdnLog.e("PcdnAcc start failed Error");
        }
        this.startresult = rs;
        ReportLogManager.getInstance().reportStartLog(PcdnType.VOD, this.accClientId, this.pcdnversion, rs, this.appname, this.appversion, this.accCacheDir, this.accPid, this.accExt);
    }

    private void reportAddressLog(String type, int code, String url) {
        if (ConfigManager.getVodAccLogSwitch() == 1) {
            PcdnLog.d("add pcdnaddress log");
            ReportLogManager.getInstance().reportAddressLog(type, this.accClientId, this.pcdnversion, code, this.startresult, this.appname, this.appversion, url);
        }
    }

    /* access modifiers changed from: private */
    public void loadAcc() {
        new Thread(new loadAccRunable()).start();
    }

    private class loadAccRunable implements Runnable {
        private loadAccRunable() {
        }

        public void run() {
            File accfile = new File(AccMgr.this.soPath);
            if (accfile.exists()) {
                AccMgr.this.loadSoFile(accfile);
            } else {
                try {
                    System.loadLibrary("pcdn_acc");
                    PcdnLog.i("PcdnAcc so load success");
                    AccMgr.this.startAcc();
                } catch (Throwable e) {
                    int unused = AccMgr.this.startresult = 10;
                    ReportLogManager.getInstance().reportStartLog(PcdnType.VOD, AccMgr.this.accClientId, AccMgr.this.pcdnversion, 10, AccMgr.this.appname, AccMgr.this.appversion, AccMgr.this.accCacheDir, AccMgr.this.accPid, AccMgr.this.accExt);
                    PcdnLog.i("PcdnAcc so load error");
                    e.printStackTrace();
                }
            }
            AccMgr.this.check();
        }
    }

    /* access modifiers changed from: private */
    public void loadSoFile(File accfile) {
        try {
            System.load(accfile.getAbsolutePath());
            PcdnLog.i("PcdnAcc so load success");
            startAcc();
        } catch (Throwable e) {
            this.startresult = 6;
            ReportLogManager.getInstance().reportStartLog(PcdnType.VOD, this.accClientId, this.pcdnversion, 6, this.appname, this.appversion, this.accCacheDir, this.accPid, this.accExt);
            PcdnLog.i("PcdnAcc SO load error");
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void check() {
        try {
            this.pcdnversion = PcdnAcc.getVersion();
        } catch (Throwable th) {
        }
        long checkTimestamp = ConfigManager.getVodCheckTimestamp();
        long checkNext = ConfigManager.getVodNextCheck();
        long nowTimestamp = new Date().getTime();
        PcdnLog.d("nowTimestamp - checkTimestamp = " + (nowTimestamp - checkTimestamp));
        long delayMillis = (1000 * checkNext) - (nowTimestamp - checkTimestamp);
        PcdnLog.d("nextcheck:" + checkNext + "delayMillis:" + delayMillis);
        if (delayMillis <= 0) {
            new Thread(new ControlMgr(this.accContext, this.accClientId, this.accPid, PcdnType.VOD, this.pcdnversion, this.appname, this.appversion, this.checkHandler)).start();
        }
    }

    class CheckHandler extends Handler {
        public CheckHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (AccMgr.this.startresult == 10) {
                    try {
                        PcdnLog.d("check完启动内核");
                        AccMgr.this.loadAcc();
                    } catch (Throwable th) {
                    }
                }
            } else if (msg.what == 0) {
                AccMgr.this.pcdnStopFromSer();
            }
        }
    }
}
