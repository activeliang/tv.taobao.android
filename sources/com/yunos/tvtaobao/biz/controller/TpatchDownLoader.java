package com.yunos.tvtaobao.biz.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.biz.util.MD5Util;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;

public class TpatchDownLoader extends ABDownloader {
    private static final String TAG = "Downloader2";
    private static final int UPDATEINFO_DELETE_ERROR = 1;
    private static final int UPDATEINFO_DOWNLOAD_RESUME = 3;
    private static final int UPDATEINFO_FILE_INVALID = 4;
    private static final int UPDATEINFO_MISMATCH = 0;
    private static final int UPDATEINFO_TPATCH_EXITS = 2;
    private Context mContext;
    private Handler mHandler;
    private String mMd5;
    private long mSize;
    private String mSource;
    private String mTarget;
    private String mVersionName;

    public TpatchDownLoader(String source, String target, String md5, String versionName, long size, long sleepTime, Context context, Handler handler) {
        super(context, handler);
        this.mSource = source;
        this.mTarget = target;
        this.mMd5 = md5;
        this.mVersionName = versionName;
        this.mSize = size;
        this.mSleepTime = sleepTime;
        this.mContext = context;
        this.mHandler = handler;
    }

    public int checkTpatch(Context context, String versionName, String target, String MD5, long size) throws Exception {
        SharedPreferences sp = context.getSharedPreferences(UpdatePreference.SP_TPATCH_FILE_NAME, 0);
        String oldVersionName = sp.getString("versionName", "");
        String oldFilePath = sp.getString(UpdatePreference.SP_KEY_PATH, "");
        String oldMD5 = sp.getString("MD5", "");
        if (!oldVersionName.equalsIgnoreCase(versionName) || !oldFilePath.equalsIgnoreCase(target) || !oldMD5.equalsIgnoreCase(MD5)) {
            ZpLogger.w(TAG, "Downloader2.download.updateInfo mismatch, delete old file, download new version");
            if (!deleteFile(new File(oldFilePath))) {
                return 1;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("versionName", versionName);
            editor.putString(UpdatePreference.SP_KEY_PATH, target);
            editor.putString("MD5", MD5);
            editor.apply();
        }
        File newAPK = new File(target);
        long curPos = newAPK.length();
        ZpLogger.d(TAG, "Downloader2.download.current file size: " + curPos + ", mSize = " + size + ", mMd5 = " + MD5);
        if (curPos == size) {
            if (MD5.equalsIgnoreCase(MD5Util.getMD5(newAPK))) {
                ZpLogger.d(TAG, "Downloader2.download.integrated file, valid apk");
                return 2;
            }
        }
        if (curPos >= size) {
            ZpLogger.w(TAG, "Downloader2.download.invalid old file, download new file");
            if (!deleteFile(newAPK)) {
                return 1;
            }
        } else if (curPos != 0) {
            ZpLogger.d(TAG, "Downloader2.download.resume from break point, from: " + curPos);
            return 3;
        }
        return -1;
    }

    /* JADX WARNING: type inference failed for: r5v11, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void download() throws java.lang.Exception {
        /*
            r36 = this;
            r0 = r36
            android.content.Context r6 = r0.mContext
            r0 = r36
            java.lang.String r7 = r0.mVersionName
            r0 = r36
            java.lang.String r8 = r0.mTarget
            r0 = r36
            java.lang.String r9 = r0.mMd5
            r0 = r36
            long r10 = r0.mSize
            r5 = r36
            int r19 = r5.checkTpatch(r6, r7, r8, r9, r10)
            java.io.File r27 = new java.io.File
            r0 = r36
            java.lang.String r5 = r0.mTarget
            r0 = r27
            r0.<init>(r5)
            long r14 = r27.length()
            r30 = r14
            switch(r19) {
                case 1: goto L_0x0110;
                case 2: goto L_0x0111;
                case 3: goto L_0x002e;
                case 4: goto L_0x002e;
                default: goto L_0x002e;
            }
        L_0x002e:
            java.io.RandomAccessFile r18 = new java.io.RandomAccessFile
            java.lang.String r5 = "rw"
            r0 = r18
            r1 = r27
            r0.<init>(r1, r5)
            r0 = r18
            r0.seek(r14)
            java.net.URL r33 = new java.net.URL
            r0 = r36
            java.lang.String r5 = r0.mSource
            r0 = r33
            r0.<init>(r5)
            java.lang.String r5 = "Downloader2"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Downloader2.download.url: "
            java.lang.StringBuilder r6 = r6.append(r7)
            r0 = r33
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r6 = r6.toString()
            com.zhiping.dev.android.logger.ZpLogger.d(r5, r6)
            r12 = 0
            r21 = 0
            java.net.URLConnection r5 = r33.openConnection()     // Catch:{ Exception -> 0x0125 }
            r0 = r5
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x0125 }
            r12 = r0
            java.lang.String r5 = "GET"
            r12.setRequestMethod(r5)     // Catch:{ Exception -> 0x0125 }
            r5 = 15000(0x3a98, float:2.102E-41)
            r12.setConnectTimeout(r5)     // Catch:{ Exception -> 0x0125 }
            r5 = 15000(0x3a98, float:2.102E-41)
            r12.setReadTimeout(r5)     // Catch:{ Exception -> 0x0125 }
            java.lang.String r5 = "Range"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0125 }
            r6.<init>()     // Catch:{ Exception -> 0x0125 }
            java.lang.String r7 = "bytes="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x0125 }
            java.lang.StringBuilder r6 = r6.append(r14)     // Catch:{ Exception -> 0x0125 }
            java.lang.String r7 = "-"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x0125 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0125 }
            r12.setRequestProperty(r5, r6)     // Catch:{ Exception -> 0x0125 }
            java.lang.String r5 = "Downloader2"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0125 }
            r6.<init>()     // Catch:{ Exception -> 0x0125 }
            java.lang.String r7 = "Downloader2.download.http stream size: "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x0125 }
            int r7 = r12.getContentLength()     // Catch:{ Exception -> 0x0125 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x0125 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0125 }
            com.zhiping.dev.android.logger.ZpLogger.d(r5, r6)     // Catch:{ Exception -> 0x0125 }
            java.io.InputStream r21 = r12.getInputStream()     // Catch:{ Exception -> 0x0125 }
            r5 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r5]
            r20 = 0
            long r34 = java.lang.System.currentTimeMillis()
            r24 = r34
            r22 = r14
            r32 = 0
        L_0x00d4:
            r0 = r21
            int r20 = r0.read(r4)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r5 = -1
            r0 = r20
            if (r0 == r5) goto L_0x0230
            int r32 = r32 + 1
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            boolean r5 = r5.isInterrupted()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            if (r5 == 0) goto L_0x0166
            java.lang.String r5 = "Downloader2"
            java.lang.String r6 = "Downloader2.download.download thread is interrupted, finish download process"
            com.zhiping.dev.android.logger.ZpLogger.d(r5, r6)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r36
            r1 = r21
            r2 = r18
            r0.closeResource(r1, r12, r2)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r36
            android.os.Handler r5 = r0.mHandler     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r6 = 1007(0x3ef, float:1.411E-42)
            android.os.Message r26 = r5.obtainMessage(r6)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r36
            android.os.Handler r5 = r0.mHandler     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r26
            r5.sendMessage(r0)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
        L_0x0110:
            return
        L_0x0111:
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r6 = 2011(0x7db, float:2.818E-42)
            android.os.Message r26 = r5.obtainMessage(r6)
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r0 = r26
            r5.sendMessage(r0)
            goto L_0x0110
        L_0x0125:
            r13 = move-exception
            java.lang.String r5 = "Downloader2"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Downloader2.download.http connection exception: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r13)
            java.lang.String r6 = r6.toString()
            com.zhiping.dev.android.logger.ZpLogger.e(r5, r6)
            r0 = r36
            r1 = r21
            r2 = r18
            r0.closeResource(r1, r12, r2)
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r6 = 1006(0x3ee, float:1.41E-42)
            android.os.Message r26 = r5.obtainMessage(r6)
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r0 = r26
            r5.sendMessage(r0)
            java.lang.String r5 = "Downloader2"
            java.lang.String r6 = "Downloader2.download.close and send message"
            com.zhiping.dev.android.logger.ZpLogger.d(r5, r6)
            goto L_0x0110
        L_0x0166:
            r5 = 0
            r0 = r18
            r1 = r20
            r0.write(r4, r5, r1)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r20
            long r6 = (long) r0     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            long r14 = r14 + r6
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            long r6 = r6 - r24
            r8 = 500(0x1f4, double:2.47E-321)
            int r5 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r5 < 0) goto L_0x01f9
            r6 = 100
            long r6 = r6 * r14
            r0 = r36
            long r8 = r0.mSize     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            long r6 = r6 / r8
            int r0 = (int) r6     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r29 = r0
            r0 = r36
            android.os.Handler r5 = r0.mHandler     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r6 = 1010(0x3f2, float:1.415E-42)
            r7 = 0
            r0 = r29
            android.os.Message r26 = r5.obtainMessage(r6, r0, r7)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r36
            android.os.Handler r5 = r0.mHandler     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r26
            r5.sendMessage(r0)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r5 = "Downloader2"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r6.<init>()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r7 = "Downloader2.download."
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.StringBuilder r6 = r6.append(r14)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r7 = " "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r29
            java.lang.StringBuilder r6 = r6.append(r0)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r7 = "% "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            long r8 = r14 - r22
            r10 = 1000(0x3e8, double:4.94E-321)
            long r8 = r8 * r10
            r10 = 1024(0x400, double:5.06E-321)
            long r8 = r8 / r10
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            long r10 = r10 - r24
            long r8 = r8 / r10
            java.lang.StringBuilder r6 = r6.append(r8)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r7 = "KB/s "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.Thread r7 = java.lang.Thread.currentThread()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r7 = r7.getName()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r6 = r6.toString()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            com.zhiping.dev.android.logger.ZpLogger.d(r5, r6)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            long r24 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r22 = r14
        L_0x01f9:
            int r5 = r32 % 5
            if (r5 != 0) goto L_0x00d4
            r32 = 0
            r0 = r36
            long r6 = r0.mSleepTime     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.Thread.sleep(r6)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            goto L_0x00d4
        L_0x0208:
            r13 = move-exception
            java.lang.String r5 = "Downloader2"
            java.lang.String r6 = "Downloader2.download. thread is interrupted(InterruptedExcetpion), finish download process"
            com.zhiping.dev.android.logger.ZpLogger.w(r5, r6)
            r0 = r36
            r1 = r21
            r2 = r18
            r0.closeResource(r1, r12, r2)
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r6 = 2002(0x7d2, float:2.805E-42)
            android.os.Message r26 = r5.obtainMessage(r6)
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r0 = r26
            r5.sendMessage(r0)
            goto L_0x0110
        L_0x0230:
            r0 = r36
            android.os.Handler r5 = r0.mHandler     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r6 = 1010(0x3f2, float:1.415E-42)
            r7 = 100
            r8 = 0
            android.os.Message r26 = r5.obtainMessage(r6, r7, r8)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r36
            android.os.Handler r5 = r0.mHandler     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r26
            r5.sendMessage(r0)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            long r16 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r5 = "Downloader2"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r6.<init>()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r7 = "Downloader2.download.elapsed time: "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            long r8 = r16 - r34
            java.lang.StringBuilder r6 = r6.append(r8)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r7 = "ms, average speed: "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            long r8 = r14 - r30
            r10 = 1000(0x3e8, double:4.94E-321)
            long r8 = r8 * r10
            r10 = 1024(0x400, double:5.06E-321)
            long r8 = r8 / r10
            long r10 = r16 - r34
            long r8 = r8 / r10
            java.lang.StringBuilder r6 = r6.append(r8)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r7 = "KB/s"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            java.lang.String r6 = r6.toString()     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            com.zhiping.dev.android.logger.ZpLogger.d(r5, r6)     // Catch:{ InterruptedException -> 0x0208, Exception -> 0x0316 }
            r0 = r36
            r1 = r21
            r2 = r18
            r0.closeResource(r1, r12, r2)
            java.io.File r27 = new java.io.File
            r0 = r36
            java.lang.String r5 = r0.mTarget
            r0 = r27
            r0.<init>(r5)
            java.lang.String r28 = com.yunos.tvtaobao.biz.util.MD5Util.getMD5(r27)
            java.lang.String r5 = "Downloader2"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Downloader2.download. finish, curPos = "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r14)
            java.lang.String r7 = ", mSize = "
            java.lang.StringBuilder r6 = r6.append(r7)
            r0 = r36
            long r8 = r0.mSize
            java.lang.StringBuilder r6 = r6.append(r8)
            java.lang.String r7 = ", mMd5 = "
            java.lang.StringBuilder r6 = r6.append(r7)
            r0 = r36
            java.lang.String r7 = r0.mMd5
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = ", newMD5 = "
            java.lang.StringBuilder r6 = r6.append(r7)
            r0 = r28
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r6 = r6.toString()
            com.zhiping.dev.android.logger.ZpLogger.d(r5, r6)
            r0 = r36
            long r6 = r0.mSize
            int r5 = (r14 > r6 ? 1 : (r14 == r6 ? 0 : -1))
            if (r5 != 0) goto L_0x033e
            r0 = r36
            java.lang.String r5 = r0.mMd5
            r0 = r28
            boolean r5 = r5.equalsIgnoreCase(r0)
            if (r5 == 0) goto L_0x033e
            java.lang.String r5 = "Downloader2"
            java.lang.String r6 = "Downloader2.download. finish, valid new apk"
            com.zhiping.dev.android.logger.ZpLogger.d(r5, r6)
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r6 = 2000(0x7d0, float:2.803E-42)
            java.lang.Long r7 = java.lang.Long.valueOf(r14)
            android.os.Message r26 = r5.obtainMessage(r6, r7)
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r0 = r26
            r5.sendMessage(r0)
            goto L_0x0110
        L_0x0316:
            r13 = move-exception
            java.lang.String r5 = "Downloader2"
            java.lang.String r6 = "Downloader2.download. encounter exception, may be caused by network problem, retry"
            com.zhiping.dev.android.logger.ZpLogger.e(r5, r6)
            r0 = r36
            r1 = r21
            r2 = r18
            r0.closeResource(r1, r12, r2)
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r6 = 2003(0x7d3, float:2.807E-42)
            android.os.Message r26 = r5.obtainMessage(r6)
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r0 = r26
            r5.sendMessage(r0)
            goto L_0x0110
        L_0x033e:
            java.lang.String r5 = "Downloader2"
            java.lang.String r6 = "Downloader2.download.invalid new apk, need to redownload"
            com.zhiping.dev.android.logger.ZpLogger.e(r5, r6)
            r0 = r36
            r1 = r27
            boolean r5 = r0.deleteFile(r1)
            if (r5 == 0) goto L_0x0110
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r6 = 2001(0x7d1, float:2.804E-42)
            android.os.Message r26 = r5.obtainMessage(r6)
            r0 = r36
            android.os.Handler r5 = r0.mHandler
            r0 = r26
            r5.sendMessage(r0)
            goto L_0x0110
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.controller.TpatchDownLoader.download():void");
    }
}
