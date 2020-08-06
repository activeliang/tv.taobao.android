package com.alibaba.analytics.core.network;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.analytics.utils.UTMCDevice;
import java.net.NetworkInterface;
import mtopsdk.common.util.SymbolExpUtil;

public class NetworkUtil {
    public static final String NETWORK_CLASS_2_G = "2G";
    public static final String NETWORK_CLASS_3_G = "3G";
    public static final String NETWORK_CLASS_4_G = "4G";
    public static final String NETWORK_CLASS_UNKNOWN = "Unknown";
    public static final String NETWORK_CLASS_WIFI = "Wi-Fi";
    private static final String WIFIADDRESS_UNKNOWN = "00:00:00:00:00:00";
    private static String[] arrayOfString = {"Unknown", "Unknown"};
    private static boolean mHaveNetworkStatus = false;
    /* access modifiers changed from: private */
    public static NetWorkStatusChecker netStatusChecker = new NetWorkStatusChecker();
    private static NetworkStatusReceiver netStatusReceiver = new NetworkStatusReceiver();

    public static String getNetworkType() {
        Context context = Variables.getInstance().getContext();
        if (context == null) {
            return "Unknown";
        }
        try {
            if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
                return "Unknown";
            }
            NetworkInfo nInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (nInfo == null) {
                return "Unknown";
            }
            if (nInfo.isConnected()) {
                if (nInfo.getType() == 1) {
                    return "Wi-Fi";
                }
                if (nInfo.getType() == 0) {
                    return getNetworkClass(nInfo.getSubtype());
                }
            }
            return "Unknown";
        } catch (Throwable th) {
        }
    }

    private static String getNetworkClass(int networkType) {
        switch (networkType) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return "2G";
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return "3G";
            case 13:
                return "4G";
            default:
                return "Unknown";
        }
    }

    public static boolean isConnectInternet(Context pContext) {
        if (pContext != null) {
            try {
                ConnectivityManager conManager = (ConnectivityManager) pContext.getSystemService("connectivity");
                if (conManager != null && pContext.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", pContext.getPackageName()) == 0) {
                    NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
                    if (networkInfo != null) {
                        return networkInfo.isConnected();
                    }
                    return false;
                }
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static String[] getNetworkState(Context context) {
        if (!mHaveNetworkStatus) {
            getNetworkStatus(context);
        }
        return arrayOfString;
    }

    public static String getAccess(Context aContext) {
        try {
            return getNetworkState(aContext)[0];
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static String getAccsssSubType(Context aContext) {
        try {
            String[] networkStatus = getNetworkState(aContext);
            if (networkStatus[0].equals("2G/3G")) {
                return networkStatus[1];
            }
            return "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static String getWifiAddress(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getWifiMacID23();
        }
        return getWifiMacID22(context);
    }

    @TargetApi(23)
    private static String getWifiMacID23() {
        try {
            byte[] mac = NetworkInterface.getByName("wlan0").getHardwareAddress();
            StringBuilder b = new StringBuilder();
            int i = 0;
            while (i < mac.length) {
                Object[] objArr = new Object[2];
                objArr[0] = Byte.valueOf(mac[i]);
                objArr[1] = i < mac.length + -1 ? SymbolExpUtil.SYMBOL_COLON : "";
                b.append(String.format("%02X%s", objArr));
                i++;
            }
            return b.toString();
        } catch (Exception e) {
            return WIFIADDRESS_UNKNOWN;
        }
    }

    private static String getWifiMacID22(Context context) {
        if (context != null) {
            try {
                WifiInfo wifiinfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
                if (wifiinfo == null) {
                    return WIFIADDRESS_UNKNOWN;
                }
                String address = wifiinfo.getMacAddress();
                if (TextUtils.isEmpty(address)) {
                    return WIFIADDRESS_UNKNOWN;
                }
                return address;
            } catch (Throwable th) {
            }
        }
        return WIFIADDRESS_UNKNOWN;
    }

    private static String convertIntToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    @Deprecated
    public static String getWifiIpAddress(Context context) {
        if (context == null) {
            return null;
        }
        try {
            WifiInfo wifiinfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
            if (wifiinfo != null) {
                return convertIntToIp(wifiinfo.getIpAddress());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Deprecated
    public static boolean isWifi(Context context) {
        if (context == null) {
            return false;
        }
        try {
            if (getNetworkState(context)[0].equals("Wi-Fi")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static void register(Context context) {
        if (context != null) {
            context.registerReceiver(netStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            try {
                NetworkOperatorUtil.registerSIMCardChangedInHandler(context);
            } catch (Exception e) {
            }
            TaskExecutor.getInstance().submit(netStatusChecker.setContext(context));
        }
    }

    public static void unRegister(Context context) {
        if (context != null && netStatusReceiver != null) {
            context.unregisterReceiver(netStatusReceiver);
        }
    }

    private static class NetworkStatusReceiver extends BroadcastReceiver {
        private NetworkStatusReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            TaskExecutor.getInstance().submit(NetworkUtil.netStatusChecker.setContext(context));
        }
    }

    private static class NetWorkStatusChecker implements Runnable {
        private Context context;

        private NetWorkStatusChecker() {
        }

        public NetWorkStatusChecker setContext(Context context2) {
            this.context = context2;
            return this;
        }

        public void run() {
            if (this.context != null) {
                NetworkUtil.getNetworkStatus(this.context);
                NetworkOperatorUtil.updateNetworkOperatorName(this.context);
                UTMCDevice.updateUTMCDeviceNetworkStatus(this.context);
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003b A[Catch:{ Exception -> 0x0033 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void getNetworkStatus(android.content.Context r8) {
        /*
            r7 = 1
            java.lang.Class<com.alibaba.analytics.core.network.NetworkUtil> r5 = com.alibaba.analytics.core.network.NetworkUtil.class
            monitor-enter(r5)
            android.content.pm.PackageManager r2 = r8.getPackageManager()     // Catch:{ Exception -> 0x0033 }
            java.lang.String r4 = "android.permission.ACCESS_NETWORK_STATE"
            java.lang.String r6 = r8.getPackageName()     // Catch:{ Exception -> 0x0033 }
            int r4 = r2.checkPermission(r4, r6)     // Catch:{ Exception -> 0x0033 }
            if (r4 == 0) goto L_0x001f
            java.lang.String[] r4 = arrayOfString     // Catch:{ Exception -> 0x0033 }
            r6 = 0
            java.lang.String r7 = "Unknown"
            r4[r6] = r7     // Catch:{ Exception -> 0x0033 }
        L_0x001d:
            monitor-exit(r5)
            return
        L_0x001f:
            java.lang.String r4 = "connectivity"
            java.lang.Object r1 = r8.getSystemService(r4)     // Catch:{ Exception -> 0x0033 }
            android.net.ConnectivityManager r1 = (android.net.ConnectivityManager) r1     // Catch:{ Exception -> 0x0033 }
            if (r1 != 0) goto L_0x0042
            java.lang.String[] r4 = arrayOfString     // Catch:{ Exception -> 0x0033 }
            r6 = 0
            java.lang.String r7 = "Unknown"
            r4[r6] = r7     // Catch:{ Exception -> 0x0033 }
            goto L_0x001d
        L_0x0033:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x003f }
        L_0x0037:
            boolean r4 = mHaveNetworkStatus     // Catch:{ all -> 0x003f }
            if (r4 != 0) goto L_0x001d
            r4 = 1
            mHaveNetworkStatus = r4     // Catch:{ all -> 0x003f }
            goto L_0x001d
        L_0x003f:
            r4 = move-exception
            monitor-exit(r5)
            throw r4
        L_0x0042:
            android.net.NetworkInfo r3 = r1.getActiveNetworkInfo()     // Catch:{ Exception -> 0x0033 }
            if (r3 == 0) goto L_0x0075
            boolean r4 = r3.isConnected()     // Catch:{ Exception -> 0x0033 }
            if (r4 == 0) goto L_0x0075
            int r4 = r3.getType()     // Catch:{ Exception -> 0x0033 }
            if (r7 != r4) goto L_0x005d
            java.lang.String[] r4 = arrayOfString     // Catch:{ Exception -> 0x0033 }
            r6 = 0
            java.lang.String r7 = "Wi-Fi"
            r4[r6] = r7     // Catch:{ Exception -> 0x0033 }
            goto L_0x0037
        L_0x005d:
            int r4 = r3.getType()     // Catch:{ Exception -> 0x0033 }
            if (r4 != 0) goto L_0x0037
            java.lang.String[] r4 = arrayOfString     // Catch:{ Exception -> 0x0033 }
            r6 = 0
            java.lang.String r7 = "2G/3G"
            r4[r6] = r7     // Catch:{ Exception -> 0x0033 }
            java.lang.String[] r4 = arrayOfString     // Catch:{ Exception -> 0x0033 }
            r6 = 1
            java.lang.String r7 = r3.getSubtypeName()     // Catch:{ Exception -> 0x0033 }
            r4[r6] = r7     // Catch:{ Exception -> 0x0033 }
            goto L_0x0037
        L_0x0075:
            java.lang.String[] r4 = arrayOfString     // Catch:{ Exception -> 0x0033 }
            r6 = 0
            java.lang.String r7 = "Unknown"
            r4[r6] = r7     // Catch:{ Exception -> 0x0033 }
            java.lang.String[] r4 = arrayOfString     // Catch:{ Exception -> 0x0033 }
            r6 = 1
            java.lang.String r7 = "Unknown"
            r4[r6] = r7     // Catch:{ Exception -> 0x0033 }
            goto L_0x0037
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.network.NetworkUtil.getNetworkStatus(android.content.Context):void");
    }
}
