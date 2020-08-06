package com.taobao.media.connectionclass;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import java.util.concurrent.atomic.AtomicInteger;

public class DeviceBandwidthSampler {
    static final long SAMPLE_TIME = 1000;
    /* access modifiers changed from: private */
    public static long sPreviousBytes = -1;
    /* access modifiers changed from: private */
    public final ConnectionClassManager mConnectionClassManager;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public long mLastTimeReading;
    private AtomicInteger mSamplingCounter;
    private HandlerThread mThread;

    private static class DeviceBandwidthSamplerHolder {
        public static final DeviceBandwidthSampler instance = new DeviceBandwidthSampler(ConnectionClassManager.getInstance());

        private DeviceBandwidthSamplerHolder() {
        }
    }

    public static DeviceBandwidthSampler getInstance() {
        return DeviceBandwidthSamplerHolder.instance;
    }

    private DeviceBandwidthSampler(ConnectionClassManager connectionClassManager) {
        this.mConnectionClassManager = connectionClassManager;
        this.mSamplingCounter = new AtomicInteger();
        this.mThread = new HandlerThread("ParseThread");
        this.mThread.start();
        this.mHandler = new SamplingHandler(this.mThread.getLooper());
    }

    public void startSampling() {
        if (this.mSamplingCounter.getAndIncrement() == 0) {
            this.mHandler.sendEmptyMessage(2);
            this.mLastTimeReading = SystemClock.elapsedRealtime();
        }
    }

    public void stopSampling() {
        if (this.mSamplingCounter.decrementAndGet() == 0) {
            this.mHandler.sendMessageAtFrontOfQueue(this.mHandler.obtainMessage(3));
            sPreviousBytes = -1;
        }
    }

    private class SamplingHandler extends Handler {
        static final int MSG_START = 2;
        static final int MSG_STOP = 3;

        public SamplingHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    addSample();
                    sendEmptyMessageDelayed(2, 1000);
                    return;
                case 3:
                    addSample();
                    removeMessages(2);
                    return;
                default:
                    Log.d("AVSDK", "Unknown what=" + msg.what);
                    return;
            }
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void addSample() {
            /*
                r10 = this;
                r8 = -1
                int r6 = android.os.Process.myUid()     // Catch:{ Throwable -> 0x0040 }
                long r4 = android.net.TrafficStats.getUidRxBytes(r6)     // Catch:{ Throwable -> 0x0040 }
                long r6 = com.taobao.media.connectionclass.DeviceBandwidthSampler.sPreviousBytes     // Catch:{ Throwable -> 0x0040 }
                int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r6 != 0) goto L_0x0016
                long unused = com.taobao.media.connectionclass.DeviceBandwidthSampler.sPreviousBytes = r4     // Catch:{ Throwable -> 0x0040 }
            L_0x0015:
                return
            L_0x0016:
                long r6 = com.taobao.media.connectionclass.DeviceBandwidthSampler.sPreviousBytes     // Catch:{ Throwable -> 0x0040 }
                long r0 = r4 - r6
                monitor-enter(r10)     // Catch:{ Throwable -> 0x0040 }
                long r2 = android.os.SystemClock.elapsedRealtime()     // Catch:{ all -> 0x0042 }
                int r6 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1))
                if (r6 == 0) goto L_0x0036
                com.taobao.media.connectionclass.DeviceBandwidthSampler r6 = com.taobao.media.connectionclass.DeviceBandwidthSampler.this     // Catch:{ all -> 0x0042 }
                com.taobao.media.connectionclass.ConnectionClassManager r6 = r6.mConnectionClassManager     // Catch:{ all -> 0x0042 }
                com.taobao.media.connectionclass.DeviceBandwidthSampler r7 = com.taobao.media.connectionclass.DeviceBandwidthSampler.this     // Catch:{ all -> 0x0042 }
                long r8 = r7.mLastTimeReading     // Catch:{ all -> 0x0042 }
                long r8 = r2 - r8
                r6.addBandwidth(r0, r8)     // Catch:{ all -> 0x0042 }
            L_0x0036:
                com.taobao.media.connectionclass.DeviceBandwidthSampler r6 = com.taobao.media.connectionclass.DeviceBandwidthSampler.this     // Catch:{ all -> 0x0042 }
                long unused = r6.mLastTimeReading = r2     // Catch:{ all -> 0x0042 }
                monitor-exit(r10)     // Catch:{ all -> 0x0042 }
                long unused = com.taobao.media.connectionclass.DeviceBandwidthSampler.sPreviousBytes = r4     // Catch:{ Throwable -> 0x0040 }
                goto L_0x0015
            L_0x0040:
                r6 = move-exception
                goto L_0x0015
            L_0x0042:
                r6 = move-exception
                monitor-exit(r10)     // Catch:{ all -> 0x0042 }
                throw r6     // Catch:{ Throwable -> 0x0040 }
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.media.connectionclass.DeviceBandwidthSampler.SamplingHandler.addSample():void");
        }
    }

    public boolean isSampling() {
        return this.mSamplingCounter.get() != 0;
    }
}
