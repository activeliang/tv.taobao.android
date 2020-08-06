package anet.channel.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Pair;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import java.lang.reflect.Method;
import java.util.Locale;

class NetworkStatusMonitor {
    private static final String TAG = "awcn.NetworkStatusMonitor";
    static volatile String apn = "";
    static volatile String bssid = "";
    static volatile String carrier = "unknown";
    static Context context = null;
    private static Method getSubInfoMethod;
    private static volatile boolean isRegistered = false;
    static volatile boolean isRoaming = false;
    private static ConnectivityManager mConnectivityManager = null;
    private static TelephonyManager mTelephonyManager = null;
    private static WifiManager mWifiManager = null;
    static volatile Pair<String, Integer> proxy = null;
    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(final Context context, Intent intent) {
            if (ALog.isPrintLog(1)) {
                ALog.d(NetworkStatusMonitor.TAG, "receiver:" + intent.getAction(), (String) null, new Object[0]);
            }
            ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
                public void run() {
                    NetworkStatusMonitor.checkNetworkStatus(context);
                }
            });
        }
    };
    static volatile String simOp = "";
    static volatile String ssid = "";
    static volatile NetworkStatusHelper.NetworkStatus status = NetworkStatusHelper.NetworkStatus.NONE;
    static volatile String subType = "unknown";
    private static SubscriptionManager subscriptionManager = null;

    NetworkStatusMonitor() {
    }

    static void registerNetworkReceiver() {
        if (!isRegistered && context != null) {
            synchronized (context) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                try {
                    context.registerReceiver(receiver, filter);
                } catch (Exception e) {
                    ALog.e(TAG, "registerReceiver failed", (String) null, new Object[0]);
                }
            }
            checkNetworkStatus(context);
        }
    }

    static void unregisterNetworkReceiver() {
        if (context != null) {
            context.unregisterReceiver(receiver);
        }
    }

    /* access modifiers changed from: private */
    public static void checkNetworkStatus(Context context2) {
        ALog.d(TAG, "[checkNetworkStatus]", (String) null, new Object[0]);
        NetworkStatusHelper.NetworkStatus lastStatus = status;
        String lastApn = apn;
        String lastSsid = ssid;
        if (context2 != null) {
            try {
                NetworkInfo info = getNetworkInfo();
                if (info == null || !info.isConnected()) {
                    resetStatus(NetworkStatusHelper.NetworkStatus.NO, "no network");
                    ALog.i(TAG, "checkNetworkStatus", (String) null, "NO NETWORK");
                } else {
                    ALog.i(TAG, "checkNetworkStatus", (String) null, "info.isConnected", Boolean.valueOf(info.isConnected()), "info.isAvailable", Boolean.valueOf(info.isAvailable()));
                    if (info.getType() == 0) {
                        String subTypeName = info.getSubtypeName();
                        String subTypeName2 = !TextUtils.isEmpty(subTypeName) ? subTypeName.replace(" ", "") : "";
                        resetStatus(parseNetworkStatus(info.getSubtype(), subTypeName2), subTypeName2);
                        apn = parseExtraInfo(info.getExtraInfo());
                        parseCarrierInfo();
                    } else if (info.getType() == 1) {
                        resetStatus(NetworkStatusHelper.NetworkStatus.WIFI, "wifi");
                        WifiInfo wifiInfo = getWifiInfo();
                        if (wifiInfo != null) {
                            bssid = wifiInfo.getBSSID();
                            ssid = wifiInfo.getSSID();
                        }
                        carrier = "wifi";
                        simOp = "wifi";
                        proxy = parseWifiProxy();
                    } else {
                        resetStatus(NetworkStatusHelper.NetworkStatus.NONE, "unknown");
                    }
                    isRoaming = info.isRoaming();
                }
                if (status != lastStatus || !apn.equalsIgnoreCase(lastApn) || !ssid.equalsIgnoreCase(lastSsid)) {
                    if (ALog.isPrintLog(2)) {
                        NetworkStatusHelper.printNetworkDetail();
                    }
                    NetworkStatusHelper.notifyStatusChanged(status);
                }
            } catch (Exception e) {
                ALog.e(TAG, "checkNetworkStatus", (String) null, e, new Object[0]);
            }
        }
    }

    private static void resetStatus(NetworkStatusHelper.NetworkStatus status2, String subType2) {
        status = status2;
        subType = subType2;
        apn = "";
        ssid = "";
        bssid = "";
        proxy = null;
        carrier = "";
        simOp = "";
    }

    private static NetworkStatusHelper.NetworkStatus parseNetworkStatus(int type, String subType2) {
        switch (type) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return NetworkStatusHelper.NetworkStatus.G2;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
                return NetworkStatusHelper.NetworkStatus.G3;
            case 13:
            case 18:
            case 19:
                return NetworkStatusHelper.NetworkStatus.G4;
            default:
                if (subType2.equalsIgnoreCase("TD-SCDMA") || subType2.equalsIgnoreCase("WCDMA") || subType2.equalsIgnoreCase("CDMA2000")) {
                    return NetworkStatusHelper.NetworkStatus.G3;
                }
                return NetworkStatusHelper.NetworkStatus.NONE;
        }
    }

    private static String parseExtraInfo(String extraInfo) {
        if (TextUtils.isEmpty(extraInfo)) {
            return "unknown";
        }
        String extraInfo2 = extraInfo.toLowerCase(Locale.US);
        if (extraInfo2.contains("cmwap")) {
            return "cmwap";
        }
        if (extraInfo2.contains("uniwap")) {
            return "uniwap";
        }
        if (extraInfo2.contains("3gwap")) {
            return "3gwap";
        }
        if (extraInfo2.contains("ctwap")) {
            return "ctwap";
        }
        if (extraInfo2.contains("cmnet")) {
            return "cmnet";
        }
        if (extraInfo2.contains("uninet")) {
            return "uninet";
        }
        if (extraInfo2.contains("3gnet")) {
            return "3gnet";
        }
        if (extraInfo2.contains("ctnet")) {
            return "ctnet";
        }
        return "unknown";
    }

    private static void parseCarrierInfo() {
        try {
            if (mTelephonyManager == null) {
                mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
            }
            simOp = mTelephonyManager.getSimOperator();
            if (Build.VERSION.SDK_INT >= 22) {
                if (subscriptionManager == null) {
                    subscriptionManager = SubscriptionManager.from(context);
                    getSubInfoMethod = subscriptionManager.getClass().getDeclaredMethod("getDefaultDataSubscriptionInfo", new Class[0]);
                }
                if (getSubInfoMethod != null) {
                    carrier = ((SubscriptionInfo) getSubInfoMethod.invoke(subscriptionManager, new Object[0])).getCarrierName().toString();
                }
            }
        } catch (Exception e) {
        }
    }

    static NetworkInfo getNetworkInfo() {
        try {
            if (mConnectivityManager == null) {
                mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            }
            return mConnectivityManager.getActiveNetworkInfo();
        } catch (Throwable t) {
            ALog.e(TAG, "getNetworkInfo", (String) null, t, new Object[0]);
            return null;
        }
    }

    private static WifiInfo getWifiInfo() {
        try {
            if (mWifiManager == null) {
                mWifiManager = (WifiManager) context.getSystemService("wifi");
            }
            return mWifiManager.getConnectionInfo();
        } catch (Throwable t) {
            ALog.e(TAG, "getWifiInfo", (String) null, t, new Object[0]);
            return null;
        }
    }

    private static Pair<String, Integer> parseWifiProxy() {
        try {
            String host = System.getProperty("http.proxyHost");
            if (!TextUtils.isEmpty(host)) {
                return Pair.create(host, Integer.valueOf(Integer.parseInt(System.getProperty("http.proxyPort"))));
            }
        } catch (NumberFormatException e) {
        }
        return null;
    }
}
