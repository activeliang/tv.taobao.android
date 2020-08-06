package com.yunos.tvtaobao.biz.controller;

import android.content.Context;
import android.os.Handler;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

public class DownLoader2 {
    private static final String TAG = "Downloader2";
    private Context mContext;
    private Handler mHandler;
    private String mMd5;
    private long mSize;
    private long mSleepTime;
    private String mSource;
    private String mTarget;
    private int mType;
    private String mVersion;

    public DownLoader2(String source, String target, String mD5, String version, long size, long sleepTime, Context context, int type, Handler myHandler) {
        this.mSource = source;
        this.mTarget = target;
        this.mMd5 = mD5;
        this.mVersion = version;
        this.mSize = size;
        this.mSleepTime = sleepTime;
        this.mContext = context;
        this.mType = type;
        this.mHandler = myHandler;
    }

    /* JADX WARNING: type inference failed for: r34v23, types: [java.net.URLConnection] */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        r40.mHandler.sendMessage(r40.mHandler.obtainMessage(1010, 100, 0));
        r10 = java.lang.System.currentTimeMillis();
        com.zhiping.dev.android.logger.ZpLogger.d(TAG, "Downloader2.download.elapsed time: " + (r10 - r30) + "ms, average speed: " + ((((r6 - r26) * 1000) / android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / (r10 - r30)) + "KB/s");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x04fa, code lost:
        closeResource(r14, r5, r12);
        r0 = new java.io.File(r40.mTarget);
        r21 = com.yunos.tvtaobao.biz.util.MD5Util.getMD5(r0);
        com.zhiping.dev.android.logger.ZpLogger.d(TAG, "Downloader2.download. finish, curPos = " + r6 + ", mSize = " + r40.mSize + ", mMd5 = " + r40.mMd5 + ", newMD5 = " + r21);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0567, code lost:
        if (r6 != r40.mSize) goto L_0x059f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0577, code lost:
        if (r40.mMd5.equalsIgnoreCase(r21) == false) goto L_0x059f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0579, code lost:
        com.zhiping.dev.android.logger.ZpLogger.d(TAG, "Downloader2.download. finish, valid new apk");
        r40.mHandler.sendMessage(r40.mHandler.obtainMessage(1004, java.lang.Long.valueOf(r6)));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x059f, code lost:
        com.zhiping.dev.android.logger.ZpLogger.e(TAG, "Downloader2.download.invalid new apk, need to redownload");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x00de, code lost:
        if (r24.equalsIgnoreCase(r40.mMd5) == false) goto L_0x00e0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x05b0, code lost:
        if (deleteFile(r0) == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x05b2, code lost:
        r40.mHandler.sendMessage(r40.mHandler.obtainMessage(1005));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:?, code lost:
        return;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void download() throws java.lang.Exception {
        /*
            r40 = this;
            r0 = r40
            android.content.Context r0 = r0.mContext
            r34 = r0
            java.lang.String r35 = "updateInfo"
            r36 = 0
            android.content.SharedPreferences r32 = r34.getSharedPreferences(r35, r36)
            r25 = 0
            java.lang.String r34 = "version"
            java.lang.String r35 = ""
            r0 = r32
            r1 = r34
            r2 = r35
            java.lang.String r25 = r0.getString(r1, r2)
            java.lang.String r34 = "filepath"
            java.lang.String r35 = ""
            r0 = r32
            r1 = r34
            r2 = r35
            java.lang.String r23 = r0.getString(r1, r2)
            java.lang.String r34 = "MD5"
            java.lang.String r35 = ""
            r0 = r32
            r1 = r34
            r2 = r35
            java.lang.String r24 = r0.getString(r1, r2)
            java.lang.String r34 = "Downloader2"
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            java.lang.String r36 = "Downloader2.download old version: "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = " new version: "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r40
            java.lang.String r0 = r0.mVersion
            r36 = r0
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = " old filepath: "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r35
            r1 = r23
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = " new filename: "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r40
            java.lang.String r0 = r0.mTarget
            r36 = r0
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = " old MD5: "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r35
            r1 = r24
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = " new MD5: "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r40
            java.lang.String r0 = r0.mMd5
            r36 = r0
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r35 = r35.toString()
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)
            r0 = r40
            java.lang.String r0 = r0.mVersion
            r34 = r0
            r0 = r25
            r1 = r34
            boolean r34 = r0.equalsIgnoreCase(r1)
            if (r34 == 0) goto L_0x00e0
            r0 = r40
            java.lang.String r0 = r0.mTarget
            r34 = r0
            r0 = r23
            r1 = r34
            boolean r34 = r0.equalsIgnoreCase(r1)
            if (r34 == 0) goto L_0x00e0
            r0 = r40
            java.lang.String r0 = r0.mMd5
            r34 = r0
            r0 = r24
            r1 = r34
            boolean r34 = r0.equalsIgnoreCase(r1)
            if (r34 != 0) goto L_0x0130
        L_0x00e0:
            java.lang.String r34 = "Downloader2"
            java.lang.String r35 = "Downloader2.download.updateInfo mismatch, delete old file, download new version"
            com.zhiping.dev.android.logger.ZpLogger.w(r34, r35)
            java.io.File r22 = new java.io.File
            r22.<init>(r23)
            r0 = r40
            r1 = r22
            boolean r34 = r0.deleteFile(r1)
            if (r34 != 0) goto L_0x00f9
        L_0x00f8:
            return
        L_0x00f9:
            android.content.SharedPreferences$Editor r9 = r32.edit()
            java.lang.String r34 = "version"
            r0 = r40
            java.lang.String r0 = r0.mVersion
            r35 = r0
            r0 = r34
            r1 = r35
            r9.putString(r0, r1)
            java.lang.String r34 = "filepath"
            r0 = r40
            java.lang.String r0 = r0.mTarget
            r35 = r0
            r0 = r34
            r1 = r35
            r9.putString(r0, r1)
            java.lang.String r34 = "MD5"
            r0 = r40
            java.lang.String r0 = r0.mMd5
            r35 = r0
            r0 = r34
            r1 = r35
            r9.putString(r0, r1)
            r9.apply()
        L_0x0130:
            java.io.File r20 = new java.io.File
            r0 = r40
            java.lang.String r0 = r0.mTarget
            r34 = r0
            r0 = r20
            r1 = r34
            r0.<init>(r1)
            long r6 = r20.length()
            java.lang.String r34 = "Downloader2"
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            java.lang.String r36 = "Downloader2.download.current file size: "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r35
            java.lang.StringBuilder r35 = r0.append(r6)
            java.lang.String r36 = ", mSize = "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r40
            long r0 = r0.mSize
            r36 = r0
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = ", mMd5 = "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r40
            java.lang.String r0 = r0.mMd5
            r36 = r0
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r35 = r35.toString()
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)
            r0 = r40
            long r0 = r0.mSize
            r34 = r0
            int r34 = (r6 > r34 ? 1 : (r6 == r34 ? 0 : -1))
            if (r34 != 0) goto L_0x0218
            r0 = r40
            java.lang.String r0 = r0.mMd5
            r34 = r0
            java.lang.String r35 = com.yunos.tvtaobao.biz.util.MD5Util.getMD5(r20)
            boolean r34 = r34.equalsIgnoreCase(r35)
            if (r34 == 0) goto L_0x0218
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r35 = 1003(0x3eb, float:1.406E-42)
            android.os.Message r15 = r34.obtainMessage(r35)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)
            r0 = r40
            android.content.Context r0 = r0.mContext
            r34 = r0
            r0 = r40
            java.lang.String r0 = r0.mTarget
            r35 = r0
            r0 = r40
            java.lang.String r0 = r0.mVersion
            r36 = r0
            boolean r34 = com.yunos.tvtaobao.biz.util.CheckAPK.checkAPKFile(r34, r35, r36)
            if (r34 != 0) goto L_0x01f6
            java.lang.String r34 = "Downloader2"
            java.lang.String r35 = "Downloader2.download.update apk check failed"
            com.zhiping.dev.android.logger.ZpLogger.e(r34, r35)
            r0 = r40
            r1 = r20
            boolean r34 = r0.deleteFile(r1)
            if (r34 == 0) goto L_0x00f8
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r35 = 1005(0x3ed, float:1.408E-42)
            android.os.Message r15 = r34.obtainMessage(r35)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)
            goto L_0x00f8
        L_0x01f6:
            java.lang.String r34 = "Downloader2"
            java.lang.String r35 = "Downloader2.download.integrated file, valid apk"
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r35 = 1011(0x3f3, float:1.417E-42)
            android.os.Message r15 = r34.obtainMessage(r35)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)
            goto L_0x00f8
        L_0x0218:
            r0 = r40
            long r0 = r0.mSize
            r34 = r0
            int r34 = (r6 > r34 ? 1 : (r6 == r34 ? 0 : -1))
            if (r34 < 0) goto L_0x0353
            java.lang.String r34 = "Downloader2"
            java.lang.String r35 = "Downloader2.download.invalid old file, download new file"
            com.zhiping.dev.android.logger.ZpLogger.w(r34, r35)
            r0 = r40
            r1 = r20
            boolean r34 = r0.deleteFile(r1)
            if (r34 == 0) goto L_0x00f8
            long r6 = r20.length()
        L_0x0239:
            r26 = r6
            java.io.RandomAccessFile r12 = new java.io.RandomAccessFile
            java.lang.String r34 = "rw"
            r0 = r20
            r1 = r34
            r12.<init>(r0, r1)
            r12.seek(r6)
            java.net.URL r33 = new java.net.URL
            r0 = r40
            java.lang.String r0 = r0.mSource
            r34 = r0
            r33.<init>(r34)
            java.lang.String r34 = "Downloader2"
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            java.lang.String r36 = "Downloader2.download.url: "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r35
            r1 = r33
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r35 = r35.toString()
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)
            r5 = 0
            r14 = 0
            java.net.URLConnection r34 = r33.openConnection()     // Catch:{ Exception -> 0x0377 }
            r0 = r34
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x0377 }
            r5 = r0
            java.lang.String r34 = "GET"
            r0 = r34
            r5.setRequestMethod(r0)     // Catch:{ Exception -> 0x0377 }
            r34 = 15000(0x3a98, float:2.102E-41)
            r0 = r34
            r5.setConnectTimeout(r0)     // Catch:{ Exception -> 0x0377 }
            r34 = 15000(0x3a98, float:2.102E-41)
            r0 = r34
            r5.setReadTimeout(r0)     // Catch:{ Exception -> 0x0377 }
            java.lang.String r34 = "Range"
            java.lang.StringBuilder r35 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0377 }
            r35.<init>()     // Catch:{ Exception -> 0x0377 }
            java.lang.String r36 = "bytes="
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ Exception -> 0x0377 }
            r0 = r35
            java.lang.StringBuilder r35 = r0.append(r6)     // Catch:{ Exception -> 0x0377 }
            java.lang.String r36 = "-"
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ Exception -> 0x0377 }
            java.lang.String r35 = r35.toString()     // Catch:{ Exception -> 0x0377 }
            r0 = r34
            r1 = r35
            r5.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x0377 }
            java.lang.String r34 = "Downloader2"
            java.lang.StringBuilder r35 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0377 }
            r35.<init>()     // Catch:{ Exception -> 0x0377 }
            java.lang.String r36 = "Downloader2.download.http stream size: "
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ Exception -> 0x0377 }
            int r36 = r5.getContentLength()     // Catch:{ Exception -> 0x0377 }
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ Exception -> 0x0377 }
            java.lang.String r35 = r35.toString()     // Catch:{ Exception -> 0x0377 }
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)     // Catch:{ Exception -> 0x0377 }
            java.io.InputStream r14 = r5.getInputStream()     // Catch:{ Exception -> 0x0377 }
            r34 = 1024(0x400, float:1.435E-42)
            r0 = r34
            byte[] r4 = new byte[r0]
            r13 = 0
            long r30 = java.lang.System.currentTimeMillis()
            r18 = r30
            r16 = r6
            r29 = 0
        L_0x02ee:
            int r13 = r14.read(r4)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r34 = -1
            r0 = r34
            if (r13 == r0) goto L_0x049f
            int r29 = r29 + 1
            java.lang.Thread r34 = java.lang.Thread.currentThread()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            boolean r34 = r34.isInterrupted()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            if (r34 == 0) goto L_0x03bb
            java.lang.String r34 = "Downloader2"
            java.lang.String r35 = "Downloader2.download.download thread is interrupted, finish download process"
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r0 = r40
            r0.closeResource(r14, r5, r12)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r0 = r40
            android.os.Handler r0 = r0.mHandler     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r34 = r0
            r35 = 1007(0x3ef, float:1.411E-42)
            android.os.Message r15 = r34.obtainMessage(r35)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r0 = r40
            android.os.Handler r0 = r0.mHandler     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            goto L_0x00f8
        L_0x032b:
            r8 = move-exception
            java.lang.String r34 = "Downloader2"
            java.lang.String r35 = "Downloader2.download. thread is interrupted(InterruptedExcetpion), finish download process"
            com.zhiping.dev.android.logger.ZpLogger.w(r34, r35)
            r0 = r40
            r0.closeResource(r14, r5, r12)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r35 = 1007(0x3ef, float:1.411E-42)
            android.os.Message r15 = r34.obtainMessage(r35)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)
            goto L_0x00f8
        L_0x0353:
            r34 = 0
            int r34 = (r6 > r34 ? 1 : (r6 == r34 ? 0 : -1))
            if (r34 == 0) goto L_0x0239
            java.lang.String r34 = "Downloader2"
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            java.lang.String r36 = "Downloader2.download.resume from break point, from: "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r35
            java.lang.StringBuilder r35 = r0.append(r6)
            java.lang.String r35 = r35.toString()
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)
            goto L_0x0239
        L_0x0377:
            r8 = move-exception
            java.lang.String r34 = "Downloader2"
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            java.lang.String r36 = "Downloader2.download.http connection exception: "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r35
            java.lang.StringBuilder r35 = r0.append(r8)
            java.lang.String r35 = r35.toString()
            com.zhiping.dev.android.logger.ZpLogger.e(r34, r35)
            r0 = r40
            r0.closeResource(r14, r5, r12)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r35 = 1006(0x3ee, float:1.41E-42)
            android.os.Message r15 = r34.obtainMessage(r35)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)
            java.lang.String r34 = "Downloader2"
            java.lang.String r35 = "Downloader2.download.close and send message"
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)
            goto L_0x00f8
        L_0x03bb:
            r34 = 0
            r0 = r34
            r12.write(r4, r0, r13)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            long r0 = (long) r13     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r34 = r0
            long r6 = r6 + r34
            long r34 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            long r34 = r34 - r18
            r36 = 500(0x1f4, double:2.47E-321)
            int r34 = (r34 > r36 ? 1 : (r34 == r36 ? 0 : -1))
            if (r34 < 0) goto L_0x0466
            r34 = 100
            long r34 = r34 * r6
            r0 = r40
            long r0 = r0.mSize     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r36 = r0
            long r34 = r34 / r36
            r0 = r34
            int r0 = (int) r0     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r28 = r0
            r0 = r40
            android.os.Handler r0 = r0.mHandler     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r34 = r0
            r35 = 1010(0x3f2, float:1.415E-42)
            r36 = 0
            r0 = r34
            r1 = r35
            r2 = r28
            r3 = r36
            android.os.Message r15 = r0.obtainMessage(r1, r2, r3)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r0 = r40
            android.os.Handler r0 = r0.mHandler     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r34 = "Downloader2"
            java.lang.StringBuilder r35 = new java.lang.StringBuilder     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r35.<init>()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r36 = "Downloader2.download."
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r0 = r35
            java.lang.StringBuilder r35 = r0.append(r6)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r36 = " "
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r0 = r35
            r1 = r28
            java.lang.StringBuilder r35 = r0.append(r1)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r36 = "% "
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            long r36 = r6 - r16
            r38 = 1000(0x3e8, double:4.94E-321)
            long r36 = r36 * r38
            r38 = 1024(0x400, double:5.06E-321)
            long r36 = r36 / r38
            long r38 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            long r38 = r38 - r18
            long r36 = r36 / r38
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r36 = "KB/s "
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.Thread r36 = java.lang.Thread.currentThread()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r36 = r36.getName()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r35 = r35.toString()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            long r18 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r16 = r6
        L_0x0466:
            int r34 = r29 % 5
            if (r34 != 0) goto L_0x02ee
            r29 = 0
            r0 = r40
            long r0 = r0.mSleepTime     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r34 = r0
            java.lang.Thread.sleep(r34)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            goto L_0x02ee
        L_0x0477:
            r8 = move-exception
            java.lang.String r34 = "Downloader2"
            java.lang.String r35 = "Downloader2.download. encounter exception, may be caused by network problem, retry"
            com.zhiping.dev.android.logger.ZpLogger.e(r34, r35)
            r0 = r40
            r0.closeResource(r14, r5, r12)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r35 = 1006(0x3ee, float:1.41E-42)
            android.os.Message r15 = r34.obtainMessage(r35)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)
            goto L_0x00f8
        L_0x049f:
            r0 = r40
            android.os.Handler r0 = r0.mHandler     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r34 = r0
            r35 = 1010(0x3f2, float:1.415E-42)
            r36 = 100
            r37 = 0
            android.os.Message r15 = r34.obtainMessage(r35, r36, r37)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r0 = r40
            android.os.Handler r0 = r0.mHandler     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r34 = "Downloader2"
            java.lang.StringBuilder r35 = new java.lang.StringBuilder     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r35.<init>()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r36 = "Downloader2.download.elapsed time: "
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            long r36 = r10 - r30
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r36 = "ms, average speed: "
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            long r36 = r6 - r26
            r38 = 1000(0x3e8, double:4.94E-321)
            long r36 = r36 * r38
            r38 = 1024(0x400, double:5.06E-321)
            long r36 = r36 / r38
            long r38 = r10 - r30
            long r36 = r36 / r38
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r36 = "KB/s"
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            java.lang.String r35 = r35.toString()     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)     // Catch:{ InterruptedException -> 0x032b, Exception -> 0x0477 }
            r0 = r40
            r0.closeResource(r14, r5, r12)
            java.io.File r20 = new java.io.File
            r0 = r40
            java.lang.String r0 = r0.mTarget
            r34 = r0
            r0 = r20
            r1 = r34
            r0.<init>(r1)
            java.lang.String r21 = com.yunos.tvtaobao.biz.util.MD5Util.getMD5(r20)
            java.lang.String r34 = "Downloader2"
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            java.lang.String r36 = "Downloader2.download. finish, curPos = "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r35
            java.lang.StringBuilder r35 = r0.append(r6)
            java.lang.String r36 = ", mSize = "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r40
            long r0 = r0.mSize
            r36 = r0
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = ", mMd5 = "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r40
            java.lang.String r0 = r0.mMd5
            r36 = r0
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = ", newMD5 = "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r35
            r1 = r21
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r35 = r35.toString()
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)
            r0 = r40
            long r0 = r0.mSize
            r34 = r0
            int r34 = (r6 > r34 ? 1 : (r6 == r34 ? 0 : -1))
            if (r34 != 0) goto L_0x059f
            r0 = r40
            java.lang.String r0 = r0.mMd5
            r34 = r0
            r0 = r34
            r1 = r21
            boolean r34 = r0.equalsIgnoreCase(r1)
            if (r34 == 0) goto L_0x059f
            java.lang.String r34 = "Downloader2"
            java.lang.String r35 = "Downloader2.download. finish, valid new apk"
            com.zhiping.dev.android.logger.ZpLogger.d(r34, r35)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r35 = 1004(0x3ec, float:1.407E-42)
            java.lang.Long r36 = java.lang.Long.valueOf(r6)
            android.os.Message r15 = r34.obtainMessage(r35, r36)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)
            goto L_0x00f8
        L_0x059f:
            java.lang.String r34 = "Downloader2"
            java.lang.String r35 = "Downloader2.download.invalid new apk, need to redownload"
            com.zhiping.dev.android.logger.ZpLogger.e(r34, r35)
            r0 = r40
            r1 = r20
            boolean r34 = r0.deleteFile(r1)
            if (r34 == 0) goto L_0x00f8
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r35 = 1005(0x3ed, float:1.408E-42)
            android.os.Message r15 = r34.obtainMessage(r35)
            r0 = r40
            android.os.Handler r0 = r0.mHandler
            r34 = r0
            r0 = r34
            r0.sendMessage(r15)
            goto L_0x00f8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.controller.DownLoader2.download():void");
    }

    private boolean deleteFile(File file) {
        ZpLogger.v(TAG, "Downloader2.deleteFile.file = " + file);
        if (file == null || !file.exists()) {
            return true;
        }
        ZpLogger.d(TAG, "Downloader2.deleteFile.file.length: " + file.length() + ",filePath = " + file.getPath());
        boolean deleted = file.delete();
        ZpLogger.d(TAG, "Downloader2.deleteFile.deleted = " + deleted);
        if (deleted) {
            return true;
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1002));
        return false;
    }

    private void closeResource(InputStream in, HttpURLConnection conn, RandomAccessFile file) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        if (file != null) {
            try {
                file.close();
            } catch (IOException e2) {
            }
        }
        if (conn != null) {
            conn.disconnect();
        }
    }
}
