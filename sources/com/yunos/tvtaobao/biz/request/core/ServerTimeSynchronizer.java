package com.yunos.tvtaobao.biz.request.core;

import android.content.Context;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerTimeSynchronizer {
    private static final String TAG = "ServerTimeSynchronizer";
    public static Long diffTime = 0L;
    public static AtomicBoolean isServerTime = new AtomicBoolean(Boolean.FALSE.booleanValue());
    private static BusinessRequest mBusinessrequest = BusinessRequest.getBusinessRequest();
    /* access modifiers changed from: private */
    public static final Object mLock = new Object();
    private static Timer timer;

    public static long getCurrentTime() {
        long currentTimeMillis;
        ZpLogger.v(TAG, "ServerTimeSynchronizer,getCurrentTime, isServerTime = " + isServerTime());
        if (!isServerTime()) {
            start();
        }
        synchronized (mLock) {
            ZpLogger.v(TAG, "ServerTimeSynchronizer,getCurrentTime,currentTimeMillis = " + System.currentTimeMillis() + ",diffTime:" + diffTime);
            currentTimeMillis = System.currentTimeMillis() + diffTime.longValue();
        }
        return currentTimeMillis;
    }

    public static synchronized void start() {
        synchronized (ServerTimeSynchronizer.class) {
            updateTime();
        }
    }

    public static synchronized void stop() {
        synchronized (ServerTimeSynchronizer.class) {
            isServerTime.set(false);
            try {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            } catch (Exception e) {
            }
        }
    }

    public static boolean isServerTime() {
        return isServerTime.get();
    }

    public static void updateTime() {
        try {
            if (NetWorkUtil.isNetWorkAvailable()) {
                long threadId = Thread.currentThread().getId();
                ZpLogger.i(TAG, "ServerTimeSynchronizer,updateTime threadId=" + threadId);
                if (threadId == 1) {
                    asyncUpServerTime(CoreApplication.getApplication());
                    return;
                }
                final Long startTime = Long.valueOf(System.currentTimeMillis());
                mBusinessrequest.requestSyncUpdatServerTime(new RequestListener<Long>() {
                    public void onRequestDone(Long data, int resultCode, String msg) {
                        ZpLogger.v(ServerTimeSynchronizer.TAG, "ServerTimeSynchronizer.updateTime.onRequestDone.data = " + data + ", resultCode = " + resultCode + ",msg = " + msg);
                        if (resultCode == 200) {
                            Long endTime = Long.valueOf(System.currentTimeMillis());
                            if (data != null) {
                                synchronized (ServerTimeSynchronizer.mLock) {
                                    ServerTimeSynchronizer.diffTime = Long.valueOf(data.longValue() - (startTime.longValue() + ((endTime.longValue() - startTime.longValue()) / 2)));
                                    ZpLogger.v(ServerTimeSynchronizer.TAG, "ServerTimeSynchronizer.updateTime.onRequestDone, get server diff:" + ServerTimeSynchronizer.diffTime);
                                    ServerTimeSynchronizer.isServerTime.set(true);
                                }
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            ZpLogger.e(TAG, "ServerTimeSynchronizer,updateTime e=" + e);
        }
    }

    public static void asyncUpServerTime(Context context) {
        if (NetWorkUtil.isNetWorkAvailable()) {
            final Long startTime = Long.valueOf(System.currentTimeMillis());
            mBusinessrequest.requestUpdatServerTime(new RequestListener<Long>() {
                public void onRequestDone(Long data, int resultCode, String msg) {
                    ZpLogger.v(ServerTimeSynchronizer.TAG, "ServerTimeSynchronizer.asyncUpServerTime.onRequestDone.data = " + data + ", resultCode = " + resultCode + ",msg = " + msg);
                    if (resultCode == 200) {
                        Long endTime = Long.valueOf(System.currentTimeMillis());
                        if (data != null) {
                            synchronized (ServerTimeSynchronizer.mLock) {
                                ServerTimeSynchronizer.diffTime = Long.valueOf(data.longValue() - (startTime.longValue() + ((endTime.longValue() - startTime.longValue()) / 2)));
                                ZpLogger.v(ServerTimeSynchronizer.TAG, "ServerTimeSynchronizer.asyncUpServerTime.onRequestDone, get server diff:" + ServerTimeSynchronizer.diffTime);
                                ServerTimeSynchronizer.isServerTime.set(true);
                            }
                        }
                    }
                }
            });
        }
    }
}
