package anet.channel.monitor;

import anet.channel.status.NetworkStatusHelper;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;

public class BandWidthSampler {
    static final int FAST = 5;
    static final int SLOW = 1;
    private static final String TAG = "awcn.BandWidthSampler";
    /* access modifiers changed from: private */
    public static volatile boolean isNetworkMeterStarted = false;
    static long mKalmanDataSize = 0;
    static long mKalmanTimeUsed = 0;
    static int mReceivedDataCount = 0;
    static long mReceivedRequestFinishedTimePre = 0;
    static long mReceivedRequestStartTimePre = 0;
    static long mSpeedKalmanCount = 0;
    static double speed = ClientTraceData.b.f47a;
    static double speedPre = ClientTraceData.b.f47a;
    static double speedPrePre = ClientTraceData.b.f47a;
    static double speedThreshold = 40.0d;
    /* access modifiers changed from: private */
    public int currentNetworkSpeed;
    /* access modifiers changed from: private */
    public NetWorkKalmanFilter mNetWorkKalmanFilter;
    /* access modifiers changed from: private */
    public int mSinceLastNotification;

    static /* synthetic */ int access$308(BandWidthSampler x0) {
        int i = x0.mSinceLastNotification;
        x0.mSinceLastNotification = i + 1;
        return i;
    }

    static class StaticHolder {
        static BandWidthSampler instance = new BandWidthSampler();

        StaticHolder() {
        }
    }

    public static BandWidthSampler getInstance() {
        return StaticHolder.instance;
    }

    private BandWidthSampler() {
        this.currentNetworkSpeed = 5;
        this.mSinceLastNotification = 0;
        this.mNetWorkKalmanFilter = new NetWorkKalmanFilter();
        NetworkStatusHelper.addStatusChangeListener(new NetworkStatusHelper.INetworkStatusChangeListener() {
            public void onNetworkStatusChanged(NetworkStatusHelper.NetworkStatus networkStatus) {
                BandWidthSampler.this.mNetWorkKalmanFilter.ResetKalmanParams();
                BandWidthSampler.mSpeedKalmanCount = 0;
                BandWidthSampler.this.startNetworkMeter();
            }
        });
    }

    public int getNetworkSpeed() {
        if (NetworkStatusHelper.getStatus() == NetworkStatusHelper.NetworkStatus.G2) {
            return 1;
        }
        return this.currentNetworkSpeed;
    }

    public double getNetSpeedValue() {
        return speed;
    }

    public synchronized void startNetworkMeter() {
        try {
            ALog.i(TAG, "[startNetworkMeter]", (String) null, "NetworkStatus", NetworkStatusHelper.getStatus());
            if (NetworkStatusHelper.getStatus() == NetworkStatusHelper.NetworkStatus.G2) {
                isNetworkMeterStarted = false;
            } else {
                isNetworkMeterStarted = true;
            }
        } catch (Exception e) {
            ALog.w(TAG, "startNetworkMeter fail.", (String) null, e, new Object[0]);
        }
        return;
    }

    public void stopNetworkMeter() {
        isNetworkMeterStarted = false;
    }

    public void onDataReceived(long mRequestStartTime, long mRequestFinishedTime, long mRequestDataSize) {
        if (isNetworkMeterStarted) {
            final long j = mRequestStartTime;
            final long j2 = mRequestFinishedTime;
            final long j3 = mRequestDataSize;
            ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
                public void run() {
                    int i = 5;
                    if (ALog.isPrintLog(1)) {
                        ALog.d(BandWidthSampler.TAG, "onDataReceived", (String) null, "mRequestStartTime", Long.valueOf(j), "mRequestFinishedTime", Long.valueOf(j2), "mRequestDataSize", Long.valueOf(j3));
                    }
                    if (BandWidthSampler.isNetworkMeterStarted && j3 > 3000 && j < j2) {
                        BandWidthSampler.mReceivedDataCount++;
                        BandWidthSampler.mKalmanDataSize += j3;
                        if (BandWidthSampler.mReceivedDataCount == 1) {
                            BandWidthSampler.mKalmanTimeUsed = j2 - j;
                        }
                        if (BandWidthSampler.mReceivedDataCount >= 2 && BandWidthSampler.mReceivedDataCount <= 3) {
                            if (j >= BandWidthSampler.mReceivedRequestFinishedTimePre) {
                                BandWidthSampler.mKalmanTimeUsed += j2 - j;
                            } else if (j < BandWidthSampler.mReceivedRequestFinishedTimePre && j2 >= BandWidthSampler.mReceivedRequestFinishedTimePre) {
                                BandWidthSampler.mKalmanTimeUsed += j2 - j;
                                BandWidthSampler.mKalmanTimeUsed -= BandWidthSampler.mReceivedRequestFinishedTimePre - j;
                            }
                        }
                        BandWidthSampler.mReceivedRequestStartTimePre = j;
                        BandWidthSampler.mReceivedRequestFinishedTimePre = j2;
                        if (BandWidthSampler.mReceivedDataCount == 3) {
                            BandWidthSampler.speed = (double) ((long) BandWidthSampler.this.mNetWorkKalmanFilter.addMeasurement((double) BandWidthSampler.mKalmanDataSize, (double) BandWidthSampler.mKalmanTimeUsed));
                            BandWidthSampler.mSpeedKalmanCount++;
                            BandWidthSampler.access$308(BandWidthSampler.this);
                            if (BandWidthSampler.mSpeedKalmanCount > 30) {
                                BandWidthSampler.this.mNetWorkKalmanFilter.ResetKalmanParams();
                                BandWidthSampler.mSpeedKalmanCount = 3;
                            }
                            double filtered_speed = (BandWidthSampler.speed * 0.68d) + (BandWidthSampler.speedPre * 0.27d) + (BandWidthSampler.speedPrePre * 0.05d);
                            BandWidthSampler.speedPrePre = BandWidthSampler.speedPre;
                            BandWidthSampler.speedPre = BandWidthSampler.speed;
                            if (BandWidthSampler.speed < 0.65d * BandWidthSampler.speedPrePre || BandWidthSampler.speed > 2.0d * BandWidthSampler.speedPrePre) {
                                BandWidthSampler.speed = filtered_speed;
                            }
                            if (ALog.isPrintLog(1)) {
                                ALog.d(BandWidthSampler.TAG, "NetworkSpeed", (String) null, "mKalmanDataSize", Long.valueOf(BandWidthSampler.mKalmanDataSize), "mKalmanTimeUsed", Long.valueOf(BandWidthSampler.mKalmanTimeUsed), "speed", Double.valueOf(BandWidthSampler.speed), "mSpeedKalmanCount", Long.valueOf(BandWidthSampler.mSpeedKalmanCount));
                            }
                            if (BandWidthSampler.this.mSinceLastNotification > 5 || BandWidthSampler.mSpeedKalmanCount == 2) {
                                BandWidthListenerHelper.getInstance().onNetworkSpeedValueNotify(BandWidthSampler.speed);
                                int unused = BandWidthSampler.this.mSinceLastNotification = 0;
                                BandWidthSampler bandWidthSampler = BandWidthSampler.this;
                                if (BandWidthSampler.speed < BandWidthSampler.speedThreshold) {
                                    i = 1;
                                }
                                int unused2 = bandWidthSampler.currentNetworkSpeed = i;
                                ALog.i(BandWidthSampler.TAG, "NetworkSpeed notification!", (String) null, "Send Network quality notification.");
                            }
                            BandWidthSampler.mKalmanTimeUsed = 0;
                            BandWidthSampler.mKalmanDataSize = 0;
                            BandWidthSampler.mReceivedDataCount = 0;
                        }
                    }
                }
            });
        }
    }
}
