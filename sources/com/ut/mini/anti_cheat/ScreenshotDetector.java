package com.ut.mini.anti_cheat;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.alibaba.analytics.utils.Logger;

class ScreenshotDetector {
    private static final long TIME_MAX = 30;
    private ContentObserver contentObserver;
    private ScreenshotListener listener;
    /* access modifiers changed from: private */
    public Context mContext;

    public interface ScreenshotListener {
        void onScreenCaptured(String str);

        void onScreenCapturedWithDeniedPermission();
    }

    ScreenshotDetector(Context context) {
        this.mContext = context;
        try {
            this.contentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
                public boolean deliverSelfNotifications() {
                    return super.deliverSelfNotifications();
                }

                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                }

                public void onChange(boolean selfChange, Uri uri) {
                    Logger.d();
                    super.onChange(selfChange, uri);
                    FileData data = ScreenshotDetector.this.getFilePathFromContentResolver(ScreenshotDetector.this.mContext, uri);
                    if (ScreenshotDetector.this.isValidScreenshot(data)) {
                        ScreenshotDetector.this.onScreenCaptured(data.path);
                    }
                }
            };
        } catch (Throwable th) {
        }
    }

    public void start(ScreenshotListener listener2) {
        this.listener = listener2;
        try {
            this.mContext.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, this.contentObserver);
        } catch (Throwable th) {
        }
    }

    public void stop() {
        try {
            this.mContext.getContentResolver().unregisterContentObserver(this.contentObserver);
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: private */
    public void onScreenCaptured(String path) {
        Logger.d();
        if (this.listener != null) {
            this.listener.onScreenCaptured(path);
        }
    }

    /* access modifiers changed from: private */
    public boolean isValidScreenshot(FileData data) {
        if (data == null || TextUtils.isEmpty(data.path)) {
            return false;
        }
        Logger.d("", "data.path", data.path);
        if (data.path.toLowerCase().contains("screenshots")) {
            return false;
        }
        long localtime = System.currentTimeMillis() / 1000;
        Logger.d("", "localtime", Long.valueOf(localtime), "data.date", Long.valueOf(data.date));
        if (Math.abs(localtime - data.date) < TIME_MAX) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public FileData getFilePathFromContentResolver(Context context, Uri uri) {
        try {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{"_display_name", "_data", "date_added"}, (String) null, (String[]) null, "date_added DESC");
            if (cursor != null && cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndex("_data"));
                long date = cursor.getLong(cursor.getColumnIndex("date_added"));
                cursor.close();
                return new FileData(path, date);
            }
        } catch (Throwable throwable) {
            Logger.e("", throwable, new Object[0]);
        }
        return null;
    }

    class FileData {
        /* access modifiers changed from: private */
        public final long date;
        /* access modifiers changed from: private */
        public final String path;

        public FileData(String path2, long date2) {
            this.path = path2;
            this.date = date2;
        }
    }
}
