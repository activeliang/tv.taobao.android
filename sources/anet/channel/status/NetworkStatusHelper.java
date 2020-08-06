package anet.channel.status;

import android.content.Context;
import android.net.NetworkInfo;
import android.util.Pair;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class NetworkStatusHelper {
    private static final String TAG = "awcn.NetworkStatusHelper";
    /* access modifiers changed from: private */
    public static CopyOnWriteArraySet<INetworkStatusChangeListener> listeners = new CopyOnWriteArraySet<>();

    public interface INetworkStatusChangeListener {
        void onNetworkStatusChanged(NetworkStatus networkStatus);
    }

    public enum NetworkStatus {
        NONE,
        NO,
        G2,
        G3,
        G4,
        WIFI;

        public boolean isMobile() {
            return this == G2 || this == G3 || this == G4;
        }

        public boolean isWifi() {
            return this == WIFI;
        }

        public String getType() {
            if (this == G2) {
                return "2G";
            }
            if (this == G3) {
                return "3G";
            }
            if (this == G4) {
                return "4G";
            }
            return toString();
        }
    }

    public static synchronized void startListener(Context context) {
        synchronized (NetworkStatusHelper.class) {
            NetworkStatusMonitor.context = context;
            NetworkStatusMonitor.registerNetworkReceiver();
        }
    }

    public void stopListener(Context context) {
        NetworkStatusMonitor.unregisterNetworkReceiver();
    }

    public static void addStatusChangeListener(INetworkStatusChangeListener listener) {
        listeners.add(listener);
    }

    public static void removeStatusChangeListener(INetworkStatusChangeListener listener) {
        listeners.remove(listener);
    }

    static void notifyStatusChanged(final NetworkStatus status) {
        ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
            public void run() {
                try {
                    Iterator i$ = NetworkStatusHelper.listeners.iterator();
                    while (i$.hasNext()) {
                        INetworkStatusChangeListener listener = (INetworkStatusChangeListener) i$.next();
                        long start = System.currentTimeMillis();
                        listener.onNetworkStatusChanged(status);
                        if (System.currentTimeMillis() - start > 500) {
                            ALog.e(NetworkStatusHelper.TAG, "call back cost too much time", (String) null, "listener", listener);
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public static NetworkStatus getStatus() {
        return NetworkStatusMonitor.status;
    }

    public static String getNetworkSubType() {
        return NetworkStatusMonitor.subType;
    }

    public static String getApn() {
        return NetworkStatusMonitor.apn;
    }

    public static String getCarrier() {
        return NetworkStatusMonitor.carrier;
    }

    public static String getSimOp() {
        return NetworkStatusMonitor.simOp;
    }

    public static boolean isRoaming() {
        return NetworkStatusMonitor.isRoaming;
    }

    public static String getWifiBSSID() {
        return NetworkStatusMonitor.bssid;
    }

    public static String getWifiSSID() {
        return NetworkStatusMonitor.ssid;
    }

    public static boolean isConnected() {
        if (NetworkStatusMonitor.status != NetworkStatus.NO) {
            return true;
        }
        NetworkInfo info = NetworkStatusMonitor.getNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        return true;
    }

    public static boolean isProxy() {
        NetworkStatus status = NetworkStatusMonitor.status;
        String apn = NetworkStatusMonitor.apn;
        if ((status != NetworkStatus.WIFI || getWifiProxy() == null) && (!status.isMobile() || (!apn.contains("wap") && GlobalAppRuntimeInfo.getProxySetting() == null))) {
            return false;
        }
        return true;
    }

    public static String getProxyType() {
        NetworkStatus status = NetworkStatusMonitor.status;
        if (status == NetworkStatus.WIFI && getWifiProxy() != null) {
            return "proxy";
        }
        if (status.isMobile() && NetworkStatusMonitor.apn.contains("wap")) {
            return "wap";
        }
        if (!status.isMobile() || GlobalAppRuntimeInfo.getProxySetting() == null) {
            return "";
        }
        return "auth";
    }

    public static Pair<String, Integer> getWifiProxy() {
        if (NetworkStatusMonitor.status != NetworkStatus.WIFI) {
            return null;
        }
        return NetworkStatusMonitor.proxy;
    }

    public static void printNetworkDetail() {
        try {
            NetworkStatus networkStatus = getStatus();
            StringBuilder sb = new StringBuilder(128);
            sb.append("\nNetwork detail*******************************\n");
            sb.append("Status: ").append(networkStatus.getType()).append(10);
            sb.append("Subtype: ").append(getNetworkSubType()).append(10);
            if (networkStatus != NetworkStatus.NO) {
                if (networkStatus.isMobile()) {
                    sb.append("Apn: ").append(getApn()).append(10);
                    sb.append("Carrier: ").append(getCarrier()).append(10);
                } else {
                    sb.append("BSSID: ").append(getWifiBSSID()).append(10);
                    sb.append("SSID: ").append(getWifiSSID()).append(10);
                }
            }
            if (isProxy()) {
                sb.append("Proxy: ").append(getProxyType()).append(10);
                Pair<String, Integer> proxyInfo = getWifiProxy();
                if (proxyInfo != null) {
                    sb.append("ProxyHost: ").append((String) proxyInfo.first).append(10);
                    sb.append("ProxyPort: ").append(proxyInfo.second).append(10);
                }
            }
            sb.append("*********************************************");
            ALog.i(TAG, sb.toString(), (String) null, new Object[0]);
        } catch (Exception e) {
        }
    }
}
