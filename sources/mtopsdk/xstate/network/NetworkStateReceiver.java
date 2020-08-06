package mtopsdk.xstate.network;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import java.util.Locale;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.xstate.NetworkClassEnum;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "mtopsdk.NetworkStateReceiver";
    public static volatile String apn = "unknown";
    public static volatile String bssid = "";
    private static ConnectivityManager mConnectivityManager = null;
    private static WifiManager mWifiManager = null;
    public static volatile String ssid = "";

    public void onReceive(final Context context, Intent intent) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[onReceive] networkStateReceiver onReceive");
        }
        MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
            public void run() {
                try {
                    NetworkStateReceiver.this.updateNetworkStatus(context);
                } catch (Throwable e) {
                    TBSdkLog.e(NetworkStateReceiver.TAG, "[onReceive] updateNetworkStatus error", e);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    @TargetApi(3)
    public void updateNetworkStatus(Context context) {
        if (context != null) {
            NetworkInfo info = null;
            try {
                if (mConnectivityManager == null) {
                    mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                }
                info = mConnectivityManager.getActiveNetworkInfo();
            } catch (Throwable t) {
                TBSdkLog.e(TAG, "getNetworkInfo error.", t);
            }
            if (info == null || !info.isConnected()) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[updateNetworkStatus]no network");
                }
                XState.setValue(XStateConstants.KEY_NQ, NetworkClassEnum.NET_NO.getNetClass());
                XState.setValue("netType", NetworkClassEnum.NET_NO.getNetClass());
                return;
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[updateNetworkStatus] NetworkInfo isConnected=" + info.isConnected() + ", isAvailable=" + info.isAvailable());
            }
            int type = info.getType();
            if (type == 0) {
                int subType = info.getSubtype();
                NetworkClassEnum netClassEnum = NetworkStatus.getNetworkClassByType(subType);
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(TAG, "[updateNetworkStatus]Mobile network," + netClassEnum.getNetClass());
                }
                apn = parseExtraInfo(info.getExtraInfo());
                XState.setValue(XStateConstants.KEY_NQ, netClassEnum.getNetClass());
                XState.setValue("netType", NetworkStatus.getNetworkTypeName(subType));
            } else if (type == 1) {
                WifiInfo wifiInfo = null;
                try {
                    if (mWifiManager == null) {
                        mWifiManager = (WifiManager) context.getSystemService("wifi");
                    }
                    wifiInfo = mWifiManager.getConnectionInfo();
                } catch (Throwable t2) {
                    TBSdkLog.e(TAG, "[updateNetworkStatus]getWifiInfo error.", t2);
                }
                if (wifiInfo != null) {
                    bssid = wifiInfo.getBSSID();
                    ssid = wifiInfo.getSSID();
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[updateNetworkStatus]WIFI network.ssid= " + ssid + ", bssid=" + bssid);
                }
                XState.setValue(XStateConstants.KEY_NQ, NetworkClassEnum.NET_WIFI.getNetClass());
                XState.setValue("netType", NetworkClassEnum.NET_WIFI.getNetClass());
            }
        }
    }

    private String parseExtraInfo(String extraInfo) {
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
}
