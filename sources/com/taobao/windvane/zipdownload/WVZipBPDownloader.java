package com.taobao.windvane.zipdownload;

import android.os.AsyncTask;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.packageapp.monitor.AppInfoMonitor;
import android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;
import com.taobao.downloader.TbDownloader;
import com.taobao.downloader.request.DownloadListener;
import com.taobao.downloader.request.DownloadRequest;
import com.taobao.downloader.request.Param;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WVZipBPDownloader extends AsyncTask<Void, Integer, Boolean> {
    private static final String TAG = "WVZipBPDownloader";
    /* access modifiers changed from: private */
    public ZipAppInfo appinfo;
    /* access modifiers changed from: private */
    public DownLoadListener listener;
    /* access modifiers changed from: private */
    public Object obj;
    /* access modifiers changed from: private */
    public int token;
    /* access modifiers changed from: private */
    public String zipUrl;

    public WVZipBPDownloader(String url, DownLoadListener listener2, int token2, Object obj2) {
        this.listener = listener2;
        this.token = token2;
        this.zipUrl = url;
        this.obj = obj2;
        if (obj2 instanceof ZipAppInfo) {
            this.appinfo = (ZipAppInfo) obj2;
        }
    }

    /* access modifiers changed from: protected */
    public Boolean doInBackground(Void... params) {
        if (this.appinfo != null) {
            if (this.token == 4) {
                AppInfoMonitor.start(this.appinfo.getNameandVersion(), 1);
            }
            if (this.token == 2) {
                AppInfoMonitor.start(this.appinfo.getNameandVersion(), 2);
            }
        }
        if (!WVCommonConfig.commonConfig.isUseTBDownloader || !ZipAppDownloaderQueue.getInstance().isTBDownloaderEnabled || !doTBDownloadTask()) {
            return Boolean.valueOf(doDefaultTask());
        }
        return true;
    }

    private boolean doTBDownloadTask() {
        try {
            DownloadRequest request = new DownloadRequest(this.zipUrl);
            request.downloadParam.bizId = "windvane";
            request.downloadParam.callbackCondition = 0;
            DownloadListener tbListener = new DownloadListener() {
                public void onDownloadProgress(int i) {
                    if (WVZipBPDownloader.this.appinfo != null && WVZipBPDownloader.this.appinfo.isPreViewApp) {
                        WVEventService.getInstance().onEvent(WVEventId.ZIPAPP_UPLOAD_PERCENT, Integer.valueOf(i));
                    }
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(WVZipBPDownloader.TAG, "onDownloadProgress pro : " + i);
                    }
                }

                public void onDownloadError(String url, int errorCode, String msg) {
                    WVZipBPDownloader.this.listener.callback(WVZipBPDownloader.this.zipUrl, (String) null, (Map<String, String>) null, WVZipBPDownloader.this.token, WVZipBPDownloader.this.obj);
                    AppInfoMonitor.error(WVZipBPDownloader.this.appinfo, ZipAppResultCode.ERR_DOWN_ZIP, "errorCode =" + errorCode + "doTBDownloadTask ErrorMsg=" + msg);
                    if (TaoLog.getLogStatus()) {
                        TaoLog.e(WVZipBPDownloader.TAG, "doTBDownloadTask Exception : " + msg);
                    }
                }

                public void onDownloadFinish(String url, String filePath) {
                    try {
                        WVZipBPDownloader.this.listener.callback(WVZipBPDownloader.this.zipUrl, filePath, new HashMap(), WVZipBPDownloader.this.token, WVZipBPDownloader.this.obj);
                    } catch (Exception e) {
                        WVZipBPDownloader.this.listener.callback(WVZipBPDownloader.this.zipUrl, (String) null, (Map<String, String>) null, WVZipBPDownloader.this.token, WVZipBPDownloader.this.obj);
                        AppInfoMonitor.error(WVZipBPDownloader.this.appinfo, ZipAppResultCode.ERR_DOWN_ZIP, "doTBDownloadTask ErrorMsg=" + e.getMessage());
                        if (TaoLog.getLogStatus()) {
                            TaoLog.e(WVZipBPDownloader.TAG, "doTBDownloadTask Exception : " + e.getMessage());
                        }
                    }
                }

                public void onDownloadStateChange(String s, boolean b) {
                }

                public void onFinish(boolean b) {
                }

                public void onNetworkLimit(int i, Param param, DownloadListener.NetworkLimitCallback networkLimitCallback) {
                }
            };
            File dir = new File(GlobalConfig.context.getCacheDir().getAbsolutePath());
            if (!dir.exists()) {
                dir.mkdir();
                TaoLog.d(TAG, "TMP 目录不存在，新建一个tmp目录");
            }
            request.downloadParam.fileStorePath = dir + File.separator + DigestUtils.md5ToHex(this.zipUrl);
            TbDownloader.getInstance().download(request, tbListener);
            return true;
        } catch (Throwable e) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "doTBDownloadTask Exception : " + e.getMessage());
            }
            if (!(e instanceof ClassNotFoundException)) {
                return false;
            }
            ZipAppDownloaderQueue.getInstance().isTBDownloaderEnabled = false;
            return false;
        }
    }

    /* JADX WARNING: type inference failed for: r4v12, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0242 A[Catch:{ all -> 0x037a }] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0262 A[SYNTHETIC, Splitter:B:51:0x0262] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0267 A[Catch:{ Exception -> 0x036e }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x026e  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x02a5 A[SYNTHETIC, Splitter:B:66:0x02a5] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x02aa A[Catch:{ Exception -> 0x0374 }] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x02b1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean doDefaultTask() {
        /*
            r40 = this;
            long r14 = java.lang.System.currentTimeMillis()
            boolean r4 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r4 == 0) goto L_0x004c
            java.lang.String r4 = "WVZipBPDownloader"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r7 = "appinfoName=【"
            java.lang.StringBuilder r5 = r5.append(r7)
            r0 = r40
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r7 = r0.appinfo
            java.lang.String r7 = r7.name
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r7 = "】 url="
            java.lang.StringBuilder r5 = r5.append(r7)
            r0 = r40
            java.lang.String r7 = r0.zipUrl
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r7 = "线程ID="
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.Thread r7 = java.lang.Thread.currentThread()
            long r8 = r7.getId()
            java.lang.StringBuilder r5 = r5.append(r8)
            java.lang.String r5 = r5.toString()
            android.taobao.windvane.util.TaoLog.d(r4, r5)
        L_0x004c:
            r32 = 1
            r27 = 0
            r19 = 0
            r36 = 0
            r13 = 0
            r22 = 0
            r34 = 0
            r29 = 0
            r24 = 0
            android.app.Application r4 = android.taobao.windvane.config.GlobalConfig.context     // Catch:{ Exception -> 0x0384 }
            java.io.File r4 = r4.getCacheDir()     // Catch:{ Exception -> 0x0384 }
            java.lang.String r18 = r4.getAbsolutePath()     // Catch:{ Exception -> 0x0384 }
            java.io.File r21 = new java.io.File     // Catch:{ Exception -> 0x0384 }
            r0 = r21
            r1 = r18
            r0.<init>(r1)     // Catch:{ Exception -> 0x0384 }
            boolean r4 = r21.exists()     // Catch:{ Exception -> 0x0384 }
            if (r4 != 0) goto L_0x0082
            r21.mkdir()     // Catch:{ Exception -> 0x0384 }
            java.lang.String r4 = "WVZipBPDownloader"
            java.lang.String r5 = "TMP 目录不存在，新建一个tmp目录"
            android.taobao.windvane.util.TaoLog.d(r4, r5)     // Catch:{ Exception -> 0x0384 }
        L_0x0082:
            java.net.URL r39 = new java.net.URL     // Catch:{ Exception -> 0x0384 }
            r0 = r40
            java.lang.String r4 = r0.zipUrl     // Catch:{ Exception -> 0x0384 }
            r0 = r39
            r0.<init>(r4)     // Catch:{ Exception -> 0x0384 }
            java.net.URLConnection r4 = r39.openConnection()     // Catch:{ Exception -> 0x0384 }
            r0 = r4
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x0384 }
            r19 = r0
            r4 = 5000(0x1388, float:7.006E-42)
            r0 = r19
            r0.setConnectTimeout(r4)     // Catch:{ Exception -> 0x0384 }
            java.lang.String r4 = "GET"
            r0 = r19
            r0.setRequestMethod(r4)     // Catch:{ Exception -> 0x0384 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0384 }
            r4.<init>()     // Catch:{ Exception -> 0x0384 }
            r0 = r21
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ Exception -> 0x0384 }
            java.lang.String r5 = java.io.File.separator     // Catch:{ Exception -> 0x0384 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x0384 }
            r0 = r40
            java.lang.String r5 = r0.zipUrl     // Catch:{ Exception -> 0x0384 }
            java.lang.String r5 = android.taobao.windvane.util.DigestUtils.md5ToHex((java.lang.String) r5)     // Catch:{ Exception -> 0x0384 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x0384 }
            java.lang.String r6 = r4.toString()     // Catch:{ Exception -> 0x0384 }
            java.io.File r28 = new java.io.File     // Catch:{ Exception -> 0x0384 }
            r0 = r28
            r0.<init>(r6)     // Catch:{ Exception -> 0x0384 }
            boolean r4 = r28.exists()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            if (r4 != 0) goto L_0x0111
            int r4 = r19.getContentLength()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            long r0 = (long) r4     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r22 = r0
            r4 = 500000(0x7a120, double:2.47033E-318)
            int r4 = (r22 > r4 ? 1 : (r22 == r4 ? 0 : -1))
            if (r4 >= 0) goto L_0x010e
            r32 = 0
            boolean r4 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            if (r4 == 0) goto L_0x010e
            java.lang.String r4 = "WVZipBPDownloader"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r5.<init>()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.String r7 = "isBPDownLoad = false  zipUrl=【"
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r40
            java.lang.String r7 = r0.zipUrl     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.String r7 = "】"
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            android.taobao.windvane.util.TaoLog.d(r4, r5)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
        L_0x010e:
            r28.createNewFile()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
        L_0x0111:
            if (r32 == 0) goto L_0x027a
            java.io.RandomAccessFile r37 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.String r4 = "rwd"
            r0 = r37
            r1 = r28
            r0.<init>(r1, r4)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            long r34 = r37.length()     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            r4 = 0
            int r4 = (r34 > r4 ? 1 : (r34 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x012d
            r4 = 1
            long r34 = r34 - r4
        L_0x012d:
            r0 = r37
            r1 = r34
            r0.seek(r1)     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            r4 = 0
            int r4 = (r34 > r4 ? 1 : (r34 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x016f
            java.lang.String r4 = "Range"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            r5.<init>()     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            java.lang.String r7 = "bytes="
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            r0 = r34
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            java.lang.String r7 = "-"
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            java.lang.String r7 = ""
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            r0 = r19
            r0.setRequestProperty(r4, r5)     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            int r4 = r19.getContentLength()     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            long r0 = (long) r4     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            r22 = r0
            long r22 = r22 + r34
        L_0x016f:
            boolean r4 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            if (r4 == 0) goto L_0x038e
            java.lang.String r4 = "WVZipBPDownloader"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            r5.<init>()     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            java.lang.String r7 = "isBPDownLoad = true  zipUrl=【"
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            r0 = r40
            java.lang.String r7 = r0.zipUrl     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            java.lang.String r7 = "】"
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            android.taobao.windvane.util.TaoLog.d(r4, r5)     // Catch:{ Exception -> 0x0387, all -> 0x037d }
            r36 = r37
        L_0x019c:
            java.io.InputStream r38 = r19.getInputStream()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.String r20 = r19.getContentEncoding()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            int r29 = r19.getResponseCode()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            if (r20 == 0) goto L_0x0287
            java.lang.String r4 = "gzip"
            r0 = r20
            boolean r4 = r4.equals(r0)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            if (r4 == 0) goto L_0x0287
            java.util.zip.GZIPInputStream r31 = new java.util.zip.GZIPInputStream     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r31
            r1 = r38
            r0.<init>(r1)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.io.DataInputStream r25 = new java.io.DataInputStream     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r25
            r1 = r31
            r0.<init>(r1)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r24 = r25
        L_0x01c9:
            android.taobao.windvane.thread.WVFixedThreadPool r4 = android.taobao.windvane.thread.WVFixedThreadPool.getInstance()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            android.taobao.windvane.thread.WVFixedThreadPool$BufferWrapper r17 = r4.getTempBuffer()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r33 = -1
        L_0x01d3:
            if (r24 == 0) goto L_0x02bc
            r0 = r17
            byte[] r4 = r0.tempBuffer     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r5 = 0
            int r7 = android.taobao.windvane.thread.WVFixedThreadPool.bufferSize     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r24
            int r33 = r0.read(r4, r5, r7)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r4 = -1
            r0 = r33
            if (r0 == r4) goto L_0x02bc
            if (r32 == 0) goto L_0x0294
            r0 = r17
            byte[] r4 = r0.tempBuffer     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r5 = 0
            r0 = r36
            r1 = r33
            r0.write(r4, r5, r1)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            goto L_0x01d3
        L_0x01f6:
            r26 = move-exception
            r27 = r28
        L_0x01f9:
            r0 = r40
            com.taobao.windvane.zipdownload.DownLoadListener r7 = r0.listener     // Catch:{ all -> 0x037a }
            r0 = r40
            java.lang.String r8 = r0.zipUrl     // Catch:{ all -> 0x037a }
            r9 = 0
            r10 = 0
            r0 = r40
            int r11 = r0.token     // Catch:{ all -> 0x037a }
            r0 = r40
            java.lang.Object r12 = r0.obj     // Catch:{ all -> 0x037a }
            r7.callback(r8, r9, r10, r11, r12)     // Catch:{ all -> 0x037a }
            r0 = r40
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r4 = r0.appinfo     // Catch:{ all -> 0x037a }
            int r5 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_DOWN_ZIP     // Catch:{ all -> 0x037a }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x037a }
            r7.<init>()     // Catch:{ all -> 0x037a }
            java.lang.String r8 = "httpcode = "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ all -> 0x037a }
            r0 = r29
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x037a }
            java.lang.String r8 = " ErrorMsg = ErrorMsg : "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ all -> 0x037a }
            java.lang.String r8 = r26.getMessage()     // Catch:{ all -> 0x037a }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ all -> 0x037a }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x037a }
            android.taobao.windvane.packageapp.monitor.AppInfoMonitor.error(r4, r5, r7)     // Catch:{ all -> 0x037a }
            boolean r4 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ all -> 0x037a }
            if (r4 == 0) goto L_0x0260
            java.lang.String r4 = "WVZipBPDownloader"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x037a }
            r5.<init>()     // Catch:{ all -> 0x037a }
            java.lang.String r7 = "WVZipBPDownloader  Exception : "
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ all -> 0x037a }
            java.lang.String r7 = r26.getMessage()     // Catch:{ all -> 0x037a }
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ all -> 0x037a }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x037a }
            android.taobao.windvane.util.TaoLog.e(r4, r5)     // Catch:{ all -> 0x037a }
        L_0x0260:
            if (r36 == 0) goto L_0x0265
            r36.close()     // Catch:{ Exception -> 0x036e }
        L_0x0265:
            if (r24 == 0) goto L_0x026a
            r24.close()     // Catch:{ Exception -> 0x036e }
        L_0x026a:
            r36 = 0
            if (r19 == 0) goto L_0x0273
            r19.disconnect()
            r19 = 0
        L_0x0273:
            r4 = 0
            r0 = r40
            r0.listener = r4
        L_0x0278:
            r4 = 1
            return r4
        L_0x027a:
            java.io.FileOutputStream r16 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r16
            r1 = r28
            r0.<init>(r1)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r13 = r16
            goto L_0x019c
        L_0x0287:
            java.io.DataInputStream r25 = new java.io.DataInputStream     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r25
            r1 = r38
            r0.<init>(r1)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r24 = r25
            goto L_0x01c9
        L_0x0294:
            r0 = r17
            byte[] r4 = r0.tempBuffer     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r5 = 0
            r0 = r33
            r13.write(r4, r5, r0)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            goto L_0x01d3
        L_0x02a0:
            r4 = move-exception
            r27 = r28
        L_0x02a3:
            if (r36 == 0) goto L_0x02a8
            r36.close()     // Catch:{ Exception -> 0x0374 }
        L_0x02a8:
            if (r24 == 0) goto L_0x02ad
            r24.close()     // Catch:{ Exception -> 0x0374 }
        L_0x02ad:
            r36 = 0
            if (r19 == 0) goto L_0x02b6
            r19.disconnect()
            r19 = 0
        L_0x02b6:
            r5 = 0
            r0 = r40
            r0.listener = r5
            throw r4
        L_0x02bc:
            r0 = r40
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r4 = r0.appinfo     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            if (r4 == 0) goto L_0x02df
            r0 = r40
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r4 = r0.appinfo     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            boolean r4 = r4.isPreViewApp     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            if (r4 == 0) goto L_0x02df
            android.taobao.windvane.service.WVEventService r4 = android.taobao.windvane.service.WVEventService.getInstance()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r5 = 6004(0x1774, float:8.413E-42)
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r8 = 0
            r9 = 100
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r7[r8] = r9     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r4.onEvent(r5, r7)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
        L_0x02df:
            r4 = 1
            r0 = r17
            r0.setIsFree(r4)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r30 = 0
            r0 = r40
            com.taobao.windvane.zipdownload.DownLoadListener r4 = r0.listener     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            if (r4 == 0) goto L_0x034d
            boolean r4 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            if (r4 == 0) goto L_0x0335
            java.lang.String r4 = "WVZipBPDownloader"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r5.<init>()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.String r7 = "zipUrl =【"
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r40
            java.lang.String r7 = r0.zipUrl     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.String r7 = "】  下载耗时=【"
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            long r8 = r8 - r14
            java.lang.StringBuilder r5 = r5.append(r8)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.String r7 = "】isBPDownLoad  =【"
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r32
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.String r7 = "】"
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            android.taobao.windvane.util.TaoLog.d(r4, r5)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
        L_0x0335:
            r0 = r40
            com.taobao.windvane.zipdownload.DownLoadListener r4 = r0.listener     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r40
            java.lang.String r5 = r0.zipUrl     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            java.util.HashMap r7 = new java.util.HashMap     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r7.<init>()     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r40
            int r8 = r0.token     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r0 = r40
            java.lang.Object r9 = r0.obj     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
            r4.callback(r5, r6, r7, r8, r9)     // Catch:{ Exception -> 0x01f6, all -> 0x02a0 }
        L_0x034d:
            if (r36 == 0) goto L_0x0352
            r36.close()     // Catch:{ Exception -> 0x0369 }
        L_0x0352:
            if (r24 == 0) goto L_0x0357
            r24.close()     // Catch:{ Exception -> 0x0369 }
        L_0x0357:
            r36 = 0
            if (r19 == 0) goto L_0x0360
            r19.disconnect()
            r19 = 0
        L_0x0360:
            r4 = 0
            r0 = r40
            r0.listener = r4
            r27 = r28
            goto L_0x0278
        L_0x0369:
            r26 = move-exception
            r26.printStackTrace()
            goto L_0x0357
        L_0x036e:
            r26 = move-exception
            r26.printStackTrace()
            goto L_0x026a
        L_0x0374:
            r26 = move-exception
            r26.printStackTrace()
            goto L_0x02ad
        L_0x037a:
            r4 = move-exception
            goto L_0x02a3
        L_0x037d:
            r4 = move-exception
            r36 = r37
            r27 = r28
            goto L_0x02a3
        L_0x0384:
            r26 = move-exception
            goto L_0x01f9
        L_0x0387:
            r26 = move-exception
            r36 = r37
            r27 = r28
            goto L_0x01f9
        L_0x038e:
            r36 = r37
            goto L_0x019c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.windvane.zipdownload.WVZipBPDownloader.doDefaultTask():boolean");
    }
}
