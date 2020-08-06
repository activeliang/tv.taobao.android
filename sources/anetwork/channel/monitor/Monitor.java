package anetwork.channel.monitor;

import android.content.Context;
import anet.channel.monitor.BandWidthListenerHelper;
import anet.channel.monitor.BandWidthSampler;
import anet.channel.monitor.INetworkQualityChangeListener;
import anet.channel.monitor.QualityChangeFilter;
import anet.channel.util.ALog;
import anetwork.channel.monitor.speed.NetworkSpeed;
import java.util.concurrent.atomic.AtomicBoolean;

public class Monitor {
    private static final String TAG = "anet.Monitor";
    static AtomicBoolean isInit = new AtomicBoolean(false);

    public static synchronized void init() {
        synchronized (Monitor.class) {
            if (isInit.compareAndSet(false, true)) {
                BandWidthSampler.getInstance().startNetworkMeter();
            }
        }
    }

    @Deprecated
    public static synchronized void init(Context context) {
        synchronized (Monitor.class) {
            init();
        }
    }

    public static void start() {
        try {
            BandWidthSampler.getInstance().startNetworkMeter();
        } catch (Throwable e) {
            ALog.e(TAG, "start failed", (String) null, e, new Object[0]);
        }
    }

    public static void stop() {
        try {
            BandWidthSampler.getInstance().stopNetworkMeter();
        } catch (Throwable e) {
            ALog.e(TAG, "stop failed", (String) null, e, new Object[0]);
        }
    }

    @Deprecated
    public static NetworkSpeed getNetworkSpeed() {
        return NetworkSpeed.valueOfCode(getNetSpeed().getCode());
    }

    public static anet.channel.monitor.NetworkSpeed getNetSpeed() {
        anet.channel.monitor.NetworkSpeed speed = anet.channel.monitor.NetworkSpeed.Fast;
        try {
            return anet.channel.monitor.NetworkSpeed.valueOfCode(BandWidthSampler.getInstance().getNetworkSpeed());
        } catch (Throwable e) {
            ALog.e(TAG, "getNetworkSpeed failed", (String) null, e, new Object[0]);
            return speed;
        }
    }

    public static void addListener(INetworkQualityChangeListener listener) {
        addListener(listener, (QualityChangeFilter) null);
    }

    public static void addListener(INetworkQualityChangeListener listener, QualityChangeFilter filter) {
        BandWidthListenerHelper.getInstance().addQualityChangeListener(listener, filter);
    }

    public static void removeListener(INetworkQualityChangeListener listener) {
        BandWidthListenerHelper.getInstance().removeQualityChangeListener(listener);
    }

    public static double getNetSpeedValue() {
        return BandWidthSampler.getInstance().getNetSpeedValue();
    }
}
