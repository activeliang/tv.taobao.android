package com.alibaba.analytics.core.sync;

public class UploadTask implements Runnable {
    public void run() {
        UploadQueueMgr.getInstance().add(UploadQueueMgr.MSGTYPE_INTERVAL);
    }
}
