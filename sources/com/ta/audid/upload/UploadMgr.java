package com.ta.audid.upload;

import android.content.Context;
import com.ta.audid.utils.TaskExecutor;
import java.util.concurrent.ScheduledFuture;

public class UploadMgr {
    private static final int DEFAULT_DELAY = 60000;
    private static final int DEFAULT_INTERVAL = 180000;
    private static UploadMgr mInstance = null;
    private Context mContext = null;
    private ScheduledFuture mUploadFuture;

    private UploadMgr(Context context) {
        this.mContext = context;
    }

    public static synchronized UploadMgr getInstance(Context context) {
        UploadMgr uploadMgr;
        synchronized (UploadMgr.class) {
            if (mInstance == null) {
                mInstance = new UploadMgr(context);
            }
            uploadMgr = mInstance;
        }
        return uploadMgr;
    }

    public synchronized void start() {
        if (this.mUploadFuture != null) {
            this.mUploadFuture.cancel(true);
        }
        startIntervalMode();
    }

    public synchronized void stop() {
        if (this.mUploadFuture != null) {
            this.mUploadFuture.cancel(true);
        }
    }

    private void startIntervalMode() {
        this.mUploadFuture = TaskExecutor.getInstance().scheduleAtFixedRate(this.mUploadFuture, new UtdidUploadTask(this.mContext), 60000, 180000);
    }
}
