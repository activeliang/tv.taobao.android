package anet.channel.monitor;

import anet.channel.util.ALog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BandWidthListenerHelper {
    private static final String TAG = "BandWidthListenerHelp";
    private static volatile BandWidthListenerHelper bandWidthListenerHelper;
    private QualityChangeFilter defaultFilter = new QualityChangeFilter();
    private Map<INetworkQualityChangeListener, QualityChangeFilter> qualityListeners = new ConcurrentHashMap();

    private BandWidthListenerHelper() {
    }

    public static BandWidthListenerHelper getInstance() {
        if (bandWidthListenerHelper == null) {
            synchronized (BandWidthListenerHelper.class) {
                if (bandWidthListenerHelper == null) {
                    bandWidthListenerHelper = new BandWidthListenerHelper();
                }
            }
        }
        return bandWidthListenerHelper;
    }

    public void addQualityChangeListener(INetworkQualityChangeListener listener, QualityChangeFilter filter) {
        if (listener == null) {
            ALog.e(TAG, "listener is null", (String) null, new Object[0]);
        } else if (filter == null) {
            this.defaultFilter.filterAddTime = System.currentTimeMillis();
            this.qualityListeners.put(listener, this.defaultFilter);
        } else {
            filter.filterAddTime = System.currentTimeMillis();
            this.qualityListeners.put(listener, filter);
        }
    }

    public void removeQualityChangeListener(INetworkQualityChangeListener listener) {
        this.qualityListeners.remove(listener);
    }

    public void onNetworkSpeedValueNotify(double speed) {
        boolean isNetSlow;
        for (Map.Entry<INetworkQualityChangeListener, QualityChangeFilter> entry : this.qualityListeners.entrySet()) {
            INetworkQualityChangeListener listener = entry.getKey();
            QualityChangeFilter filter = entry.getValue();
            if (!(listener == null || filter == null || filter.checkShouldDelay() || filter.isNetSpeedSlow() == (isNetSlow = filter.detectNetSpeedSlow(speed)))) {
                filter.setNetSpeedSlow(isNetSlow);
                listener.onNetworkQualityChanged(isNetSlow ? NetworkSpeed.Slow : NetworkSpeed.Fast);
            }
        }
    }
}
