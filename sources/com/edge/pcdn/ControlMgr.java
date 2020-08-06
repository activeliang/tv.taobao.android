package com.edge.pcdn;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.edge.pcdnpar.BuildConfig;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import org.json.JSONObject;

public class ControlMgr implements Runnable {
    public static final int DATA_ERROT = -1;
    public static final int DEFAULT = 99;
    public static final int DOWNLOAD_ERROR = -2;
    public static final int MD5_ERROR = -3;
    public static final int START_DISABLE = 0;
    public static final int START_ENABLE = 1;
    public static final int UNZIP_ERROR = -4;
    public static final String pcdnAccFilename = "libpcdn_acc.so";
    public static final String pcdnAccNewFilename = "libpcdn_acc_new.so";
    public static final String pcdnAccTempFilename = "libpcdn_acc.zip";
    public static final String pcdnLiveFilename = "libpcdn_acc_live.so";
    public static final String pcdnLiveNewFilename = "libpcdn_acc_live_new.so";
    public static final String pcdnLiveTempFilename = "libpcdn_acc_live.zip";
    /* access modifiers changed from: private */
    public String appname = null;
    /* access modifiers changed from: private */
    public String appversion = null;
    /* access modifiers changed from: private */
    public String clientId;
    private Context context;
    private Handler downloadUrlHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null) {
                try {
                    PcdnLog.d(msg.obj.toString());
                    JSONObject jSONObject = new JSONObject(msg.obj.toString());
                    if (jSONObject.getInt("code") == 0) {
                        long nextcheck = jSONObject.getLong("nextcheck");
                        if (jSONObject.has(PcdnType.VOD)) {
                            JSONObject jo = jSONObject.getJSONObject(PcdnType.VOD);
                            int logSwitch = jo.getInt("switch_log");
                            int start = jo.getInt("start");
                            ConfigManager.setVodAccStart(start);
                            ConfigManager.setVodNextCheck(nextcheck);
                            ConfigManager.setVodCheckTimestamp(new Date().getTime());
                            ConfigManager.setVodAccLogSwitch(logSwitch);
                            if (jo.getInt("code") != 0) {
                                PcdnLog.d("PcdnAcc need upgrade");
                                String url = jo.getString("file_url");
                                String version = jo.getString("version");
                                String md5 = jo.getString("md5");
                                new Thread(new DownloadSORunable(url, jo.getLong("file_size"), md5, ControlMgr.this.libDir, ControlMgr.pcdnAccTempFilename, ControlMgr.pcdnAccNewFilename, ControlMgr.pcdnAccFilename, version, start)).start();
                                return;
                            }
                            ControlMgr.this.sendCheckResult(start);
                            PcdnLog.i("PcdnAcc is last version");
                        } else if (jSONObject.has(PcdnType.LIVE)) {
                            JSONObject jo2 = jSONObject.getJSONObject(PcdnType.LIVE);
                            int logSwitch2 = jo2.getInt("switch_log");
                            int start2 = jo2.getInt("start");
                            ConfigManager.setLiveAccStart(start2);
                            ConfigManager.setLiveNextCheck(nextcheck);
                            ConfigManager.setLiveCheckTimestamp(new Date().getTime());
                            ConfigManager.setLiveAccLogSwitch(logSwitch2);
                            if (jo2.getInt("code") != 0) {
                                PcdnLog.d("PcdnLive need upgrade");
                                String url2 = jo2.getString("file_url");
                                String version2 = jo2.getString("version");
                                String md52 = jo2.getString("md5");
                                new Thread(new DownloadSORunable(url2, jo2.getLong("file_size"), md52, ControlMgr.this.libDir, ControlMgr.pcdnLiveTempFilename, ControlMgr.pcdnLiveNewFilename, ControlMgr.pcdnLiveFilename, version2, start2)).start();
                                return;
                            }
                            ControlMgr.this.sendCheckResult(start2);
                            PcdnLog.i("PcdnLive is last version");
                        } else {
                            ControlMgr.this.sendCheckResult(-1);
                            PcdnLog.e("Pcdn server response error data");
                        }
                    } else {
                        ControlMgr.this.sendCheckResult(-1);
                        PcdnLog.e("Pcdn errorcdoe：" + jSONObject.getInt("code") + jSONObject.getString(TuwenConstants.MODEL_LIST_KEY.TEXT));
                    }
                } catch (Exception e) {
                    ControlMgr.this.sendCheckResult(-1);
                    PcdnLog.e(PcdnLog.toString(e));
                }
            }
        }
    };
    private Handler handler = null;
    /* access modifiers changed from: private */
    public File libDir = null;
    /* access modifiers changed from: private */
    public String pcdnType = null;
    private String pcdnversion = "0";
    private String pid = null;

    public ControlMgr(Context context2, String clientId2, String pid2, String pcdnType2, String pcdnversion2, String appname2, String appversion2, Handler handler2) {
        this.context = context2;
        this.clientId = clientId2;
        this.pid = pid2;
        this.pcdnType = pcdnType2;
        this.pcdnversion = pcdnversion2;
        this.appname = appname2;
        this.appversion = appversion2;
        this.handler = handler2;
        this.libDir = this.context.getDir("libs", 0);
    }

    public void run() {
        new Thread(new HttpsTask(ConfigManager.UPGRADE_URL, this.downloadUrlHandler, "&type=" + this.pcdnType + "&version=" + this.pcdnversion + "&os_version=" + urlencode(Build.VERSION.RELEASE) + "&client_id=" + this.clientId + "&app_name=" + urlencode(this.appname) + "&app_version=" + urlencode(this.appversion) + "&arch=" + urlencode(Build.CPU_ABI) + "&phy_mem=" + ((ActivityManager) this.context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getMemoryClass() + "&disk=" + Environment.getExternalStorageState().equals("mounted") + "&pid=" + urlencode(this.pid) + "&my_version=" + BuildConfig.VERSION_NAME, "GET")).start();
    }

    private String urlencode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "default";
        }
    }

    /* access modifiers changed from: private */
    public void sendCheckResult(int startcode) {
        Message msg = new Message();
        msg.what = startcode;
        this.handler.sendMessage(msg);
    }

    class DownloadSORunable implements Runnable {
        private File dir = null;
        private String filename = null;
        private long filesize = 0;
        private String md5 = null;
        private String newFileName = null;
        private int startcode = -1;
        private String tempFileName = null;
        private String urlstr = null;
        private String version = null;

        public DownloadSORunable(String url, long filesize2, String md52, File dir2, String tempFileName2, String newFileName2, String fileName, String version2, int startcode2) {
            this.urlstr = url;
            this.filesize = filesize2;
            this.md5 = md52;
            this.dir = dir2;
            this.tempFileName = tempFileName2;
            this.newFileName = newFileName2;
            this.filename = fileName;
            this.version = version2;
            this.startcode = startcode2;
        }

        /* JADX WARNING: type inference failed for: r2v36, types: [java.net.URLConnection] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x0079  */
        /* JADX WARNING: Removed duplicated region for block: B:91:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r23 = this;
                r12 = 0
                r18 = 0
                r16 = 0
                java.io.File r9 = new java.io.File
                r0 = r23
                java.io.File r2 = r0.dir
                r0 = r23
                java.lang.String r3 = r0.tempFileName
                r9.<init>(r2, r3)
                boolean r2 = r9.exists()
                if (r2 == 0) goto L_0x001b
                r9.delete()
            L_0x001b:
                java.net.URL r22 = new java.net.URL     // Catch:{ Exception -> 0x00ef }
                r0 = r23
                java.lang.String r2 = r0.urlstr     // Catch:{ Exception -> 0x00ef }
                r0 = r22
                r0.<init>(r2)     // Catch:{ Exception -> 0x00ef }
                java.net.URLConnection r2 = r22.openConnection()     // Catch:{ Exception -> 0x00ef }
                r0 = r2
                java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x00ef }
                r12 = r0
                r12.connect()     // Catch:{ Exception -> 0x00ef }
                int r19 = r12.getContentLength()     // Catch:{ Exception -> 0x00ef }
                java.io.InputStream r18 = r12.getInputStream()     // Catch:{ Exception -> 0x00ef }
                java.io.FileOutputStream r17 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00ef }
                r0 = r17
                r0.<init>(r9)     // Catch:{ Exception -> 0x00ef }
                r13 = 0
                r2 = 2048(0x800, float:2.87E-42)
                byte[] r11 = new byte[r2]     // Catch:{ Exception -> 0x0243, all -> 0x023e }
            L_0x0045:
                r0 = r18
                int r21 = r0.read(r11)     // Catch:{ Exception -> 0x0243, all -> 0x023e }
                int r13 = r13 + r21
                r2 = 0
                r0 = r17
                r1 = r21
                r0.write(r11, r2, r1)     // Catch:{ Exception -> 0x0243, all -> 0x023e }
                r0 = r19
                if (r13 != r0) goto L_0x0045
                r17.close()     // Catch:{ Exception -> 0x0243, all -> 0x023e }
                r18.close()     // Catch:{ Exception -> 0x0243, all -> 0x023e }
                r12.disconnect()     // Catch:{ Exception -> 0x0243, all -> 0x023e }
                java.lang.String r2 = "PcdnVod download success !"
                com.edge.pcdn.PcdnLog.i(r2)     // Catch:{ Exception -> 0x0243, all -> 0x023e }
                r17.close()     // Catch:{ Exception -> 0x00d2 }
            L_0x006b:
                r18.close()     // Catch:{ Exception -> 0x00db }
            L_0x006e:
                r12.disconnect()     // Catch:{ Exception -> 0x00e4 }
                r16 = r17
            L_0x0073:
                boolean r2 = r9.exists()
                if (r2 == 0) goto L_0x00d1
                long r2 = r9.length()
                r0 = r23
                long r4 = r0.filesize
                int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r2 != 0) goto L_0x0097
                r0 = r23
                java.lang.String r2 = r0.md5
                java.lang.String r3 = r9.getAbsolutePath()
                java.lang.String r3 = com.edge.pcdn.MD5Util.md5sum(r3)
                boolean r2 = r2.equals(r3)
                if (r2 != 0) goto L_0x0177
            L_0x0097:
                r0 = r23
                com.edge.pcdn.ControlMgr r2 = com.edge.pcdn.ControlMgr.this
                r3 = -3
                r2.sendCheckResult(r3)
                java.lang.String r2 = "PcdnVod Illegal file"
                com.edge.pcdn.PcdnLog.e(r2)
                com.edge.pcdn.ReportLogManager r2 = com.edge.pcdn.ReportLogManager.getInstance()
                r0 = r23
                com.edge.pcdn.ControlMgr r3 = com.edge.pcdn.ControlMgr.this
                java.lang.String r3 = r3.pcdnType
                r0 = r23
                com.edge.pcdn.ControlMgr r4 = com.edge.pcdn.ControlMgr.this
                java.lang.String r4 = r4.clientId
                r0 = r23
                java.lang.String r5 = r0.version
                r6 = 2
                r0 = r23
                com.edge.pcdn.ControlMgr r7 = com.edge.pcdn.ControlMgr.this
                java.lang.String r7 = r7.appname
                r0 = r23
                com.edge.pcdn.ControlMgr r8 = com.edge.pcdn.ControlMgr.this
                java.lang.String r8 = r8.appversion
                r2.reportUpgradeLog(r3, r4, r5, r6, r7, r8)
            L_0x00d1:
                return
            L_0x00d2:
                r14 = move-exception
                java.lang.String r2 = com.edge.pcdn.PcdnLog.toString(r14)
                com.edge.pcdn.PcdnLog.d(r2)
                goto L_0x006b
            L_0x00db:
                r14 = move-exception
                java.lang.String r2 = com.edge.pcdn.PcdnLog.toString(r14)
                com.edge.pcdn.PcdnLog.d(r2)
                goto L_0x006e
            L_0x00e4:
                r14 = move-exception
                java.lang.String r2 = com.edge.pcdn.PcdnLog.toString(r14)
                com.edge.pcdn.PcdnLog.d(r2)
                r16 = r17
                goto L_0x0073
            L_0x00ef:
                r14 = move-exception
            L_0x00f0:
                r0 = r23
                com.edge.pcdn.ControlMgr r2 = com.edge.pcdn.ControlMgr.this     // Catch:{ all -> 0x0151 }
                r3 = -2
                r2.sendCheckResult(r3)     // Catch:{ all -> 0x0151 }
                com.edge.pcdn.ReportLogManager r2 = com.edge.pcdn.ReportLogManager.getInstance()     // Catch:{ all -> 0x0151 }
                r0 = r23
                com.edge.pcdn.ControlMgr r3 = com.edge.pcdn.ControlMgr.this     // Catch:{ all -> 0x0151 }
                java.lang.String r3 = r3.pcdnType     // Catch:{ all -> 0x0151 }
                r0 = r23
                com.edge.pcdn.ControlMgr r4 = com.edge.pcdn.ControlMgr.this     // Catch:{ all -> 0x0151 }
                java.lang.String r4 = r4.clientId     // Catch:{ all -> 0x0151 }
                r0 = r23
                java.lang.String r5 = r0.version     // Catch:{ all -> 0x0151 }
                r6 = 7
                r0 = r23
                com.edge.pcdn.ControlMgr r7 = com.edge.pcdn.ControlMgr.this     // Catch:{ all -> 0x0151 }
                java.lang.String r7 = r7.appname     // Catch:{ all -> 0x0151 }
                r0 = r23
                com.edge.pcdn.ControlMgr r8 = com.edge.pcdn.ControlMgr.this     // Catch:{ all -> 0x0151 }
                java.lang.String r8 = r8.appversion     // Catch:{ all -> 0x0151 }
                r2.reportUpgradeLog(r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0151 }
                java.lang.String r2 = "PcdnVod download failed !"
                com.edge.pcdn.PcdnLog.i(r2)     // Catch:{ all -> 0x0151 }
                r16.close()     // Catch:{ Exception -> 0x013f }
            L_0x012d:
                r18.close()     // Catch:{ Exception -> 0x0148 }
            L_0x0130:
                r12.disconnect()     // Catch:{ Exception -> 0x0135 }
                goto L_0x0073
            L_0x0135:
                r14 = move-exception
                java.lang.String r2 = com.edge.pcdn.PcdnLog.toString(r14)
                com.edge.pcdn.PcdnLog.d(r2)
                goto L_0x0073
            L_0x013f:
                r14 = move-exception
                java.lang.String r2 = com.edge.pcdn.PcdnLog.toString(r14)
                com.edge.pcdn.PcdnLog.d(r2)
                goto L_0x012d
            L_0x0148:
                r14 = move-exception
                java.lang.String r2 = com.edge.pcdn.PcdnLog.toString(r14)
                com.edge.pcdn.PcdnLog.d(r2)
                goto L_0x0130
            L_0x0151:
                r2 = move-exception
            L_0x0152:
                r16.close()     // Catch:{ Exception -> 0x015c }
            L_0x0155:
                r18.close()     // Catch:{ Exception -> 0x0165 }
            L_0x0158:
                r12.disconnect()     // Catch:{ Exception -> 0x016e }
            L_0x015b:
                throw r2
            L_0x015c:
                r14 = move-exception
                java.lang.String r3 = com.edge.pcdn.PcdnLog.toString(r14)
                com.edge.pcdn.PcdnLog.d(r3)
                goto L_0x0155
            L_0x0165:
                r14 = move-exception
                java.lang.String r3 = com.edge.pcdn.PcdnLog.toString(r14)
                com.edge.pcdn.PcdnLog.d(r3)
                goto L_0x0158
            L_0x016e:
                r14 = move-exception
                java.lang.String r3 = com.edge.pcdn.PcdnLog.toString(r14)
                com.edge.pcdn.PcdnLog.d(r3)
                goto L_0x015b
            L_0x0177:
                java.lang.String r2 = r9.getAbsolutePath()     // Catch:{ Exception -> 0x01dd }
                r0 = r23
                java.lang.String r3 = r0.newFileName     // Catch:{ Exception -> 0x01dd }
                com.edge.pcdn.ZipUtil.unzipSingleFileHereWithFileName(r2, r3)     // Catch:{ Exception -> 0x01dd }
                java.lang.String r2 = "Pcdn unzip success"
                com.edge.pcdn.PcdnLog.i(r2)     // Catch:{ Exception -> 0x01dd }
                java.io.File r20 = new java.io.File     // Catch:{ Exception -> 0x01dd }
                r0 = r23
                com.edge.pcdn.ControlMgr r2 = com.edge.pcdn.ControlMgr.this     // Catch:{ Exception -> 0x01dd }
                java.io.File r2 = r2.libDir     // Catch:{ Exception -> 0x01dd }
                r0 = r23
                java.lang.String r3 = r0.newFileName     // Catch:{ Exception -> 0x01dd }
                r0 = r20
                r0.<init>(r2, r3)     // Catch:{ Exception -> 0x01dd }
                java.io.File r15 = new java.io.File     // Catch:{ Exception -> 0x01dd }
                r0 = r23
                com.edge.pcdn.ControlMgr r2 = com.edge.pcdn.ControlMgr.this     // Catch:{ Exception -> 0x01dd }
                java.io.File r2 = r2.libDir     // Catch:{ Exception -> 0x01dd }
                r0 = r23
                java.lang.String r3 = r0.filename     // Catch:{ Exception -> 0x01dd }
                r15.<init>(r2, r3)     // Catch:{ Exception -> 0x01dd }
                boolean r2 = r15.exists()     // Catch:{ Exception -> 0x01dd }
                if (r2 == 0) goto L_0x0233
                boolean r2 = r15.delete()     // Catch:{ Exception -> 0x01dd }
                if (r2 == 0) goto L_0x01d3
                java.lang.String r2 = "old file delete success"
                com.edge.pcdn.PcdnLog.d(r2)     // Catch:{ Exception -> 0x01dd }
                r0 = r20
                r0.renameTo(r15)     // Catch:{ Exception -> 0x01dd }
            L_0x01c3:
                r0 = r23
                com.edge.pcdn.ControlMgr r2 = com.edge.pcdn.ControlMgr.this     // Catch:{ Exception -> 0x01dd }
                r0 = r23
                int r3 = r0.startcode     // Catch:{ Exception -> 0x01dd }
                r2.sendCheckResult(r3)     // Catch:{ Exception -> 0x01dd }
                r9.delete()
                goto L_0x00d1
            L_0x01d3:
                r20.delete()     // Catch:{ Exception -> 0x01dd }
                java.lang.String r2 = "Pcdn delete error"
                com.edge.pcdn.PcdnLog.e(r2)     // Catch:{ Exception -> 0x01dd }
                goto L_0x01c3
            L_0x01dd:
                r14 = move-exception
                r0 = r23
                com.edge.pcdn.ControlMgr r2 = com.edge.pcdn.ControlMgr.this     // Catch:{ all -> 0x0239 }
                r3 = -4
                r2.sendCheckResult(r3)     // Catch:{ all -> 0x0239 }
                java.lang.String r2 = "PcdnVod unzip failed"
                com.edge.pcdn.PcdnLog.i(r2)     // Catch:{ all -> 0x0239 }
                java.io.File r10 = new java.io.File     // Catch:{ all -> 0x0239 }
                r0 = r23
                java.io.File r2 = r0.dir     // Catch:{ all -> 0x0239 }
                r0 = r23
                java.lang.String r3 = r0.newFileName     // Catch:{ all -> 0x0239 }
                r10.<init>(r2, r3)     // Catch:{ all -> 0x0239 }
                boolean r2 = r10.exists()     // Catch:{ all -> 0x0239 }
                if (r2 == 0) goto L_0x0202
                r10.delete()     // Catch:{ all -> 0x0239 }
            L_0x0202:
                com.edge.pcdn.ReportLogManager r2 = com.edge.pcdn.ReportLogManager.getInstance()     // Catch:{ all -> 0x0239 }
                r0 = r23
                com.edge.pcdn.ControlMgr r3 = com.edge.pcdn.ControlMgr.this     // Catch:{ all -> 0x0239 }
                java.lang.String r3 = r3.pcdnType     // Catch:{ all -> 0x0239 }
                r0 = r23
                com.edge.pcdn.ControlMgr r4 = com.edge.pcdn.ControlMgr.this     // Catch:{ all -> 0x0239 }
                java.lang.String r4 = r4.clientId     // Catch:{ all -> 0x0239 }
                r0 = r23
                java.lang.String r5 = r0.version     // Catch:{ all -> 0x0239 }
                r6 = 3
                r0 = r23
                com.edge.pcdn.ControlMgr r7 = com.edge.pcdn.ControlMgr.this     // Catch:{ all -> 0x0239 }
                java.lang.String r7 = r7.appname     // Catch:{ all -> 0x0239 }
                r0 = r23
                com.edge.pcdn.ControlMgr r8 = com.edge.pcdn.ControlMgr.this     // Catch:{ all -> 0x0239 }
                java.lang.String r8 = r8.appversion     // Catch:{ all -> 0x0239 }
                r2.reportUpgradeLog(r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0239 }
                r9.delete()
                goto L_0x00d1
            L_0x0233:
                r0 = r20
                r0.renameTo(r15)     // Catch:{ Exception -> 0x01dd }
                goto L_0x01c3
            L_0x0239:
                r2 = move-exception
                r9.delete()
                throw r2
            L_0x023e:
                r2 = move-exception
                r16 = r17
                goto L_0x0152
            L_0x0243:
                r14 = move-exception
                r16 = r17
                goto L_0x00f0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.edge.pcdn.ControlMgr.DownloadSORunable.run():void");
        }
    }
}
