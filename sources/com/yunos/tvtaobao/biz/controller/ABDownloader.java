package com.yunos.tvtaobao.biz.controller;

import android.content.Context;
import android.os.Handler;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

public abstract class ABDownloader {
    private static final String TAG = "ABDownloader";
    public Context mContext;
    public Handler mHandler;
    protected long mSleepTime;

    public abstract void download() throws Exception;

    public ABDownloader(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    public void setDownloadDelayTime(int sleepTime) {
        this.mSleepTime = (long) sleepTime;
    }

    public void closeResource(InputStream in, HttpURLConnection conn, RandomAccessFile file) {
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

    public boolean deleteFile(File file) {
        ZpLogger.v(TAG, "ABDownloader.deleteFile.file = " + file);
        if (file == null || !file.exists()) {
            return true;
        }
        ZpLogger.d(TAG, "ABDownloader.deleteFile.file.length: " + file.length() + ",filePath = " + file.getPath());
        boolean deleted = file.delete();
        ZpLogger.d(TAG, "ABDownloader.deleteFile.deleted = " + deleted);
        if (deleted) {
            return true;
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1002));
        return false;
    }
}
