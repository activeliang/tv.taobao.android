package com.yunos.tv.core.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.yunos.tv.core.CoreApplication;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetWorkUtil {
    public static int getNetWorkType() {
        if (!isNetWorkAvailable()) {
            return -1;
        }
        if (((ConnectivityManager) CoreApplication.getApplication().getSystemService("connectivity")).getNetworkInfo(9).isConnected()) {
            return 9;
        }
        if (isWifi()) {
            return 1;
        }
        return -1;
    }

    public static String getIpAddress() {
        try {
            WifiManager wifiManager = (WifiManager) CoreApplication.getApplication().getSystemService("wifi");
            wifiManager.getConnectionInfo().getIpAddress();
            if (wifiManager == null) {
                return getEthIpAddress();
            }
            WifiInfo info = wifiManager.getConnectionInfo();
            if (info != null) {
                return intIP2StringIP(info.getIpAddress());
            }
            return null;
        } catch (Exception e) {
        }
    }

    private static String getEthIpAddress() throws SocketException {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface intf = netInterfaces.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {
                    Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                    while (enumIpAddr.hasMoreElements()) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::")) {
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipaddress;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 255) + "." + ((ip >> 8) & 255) + "." + ((ip >> 16) & 255) + "." + ((ip >> 24) & 255);
    }

    public static boolean isNetWorkAvailable() {
        NetworkInfo info;
        try {
            ConnectivityManager cm = getConnectivityManager();
            if (cm == null || (info = cm.getActiveNetworkInfo()) == null) {
                return false;
            }
            return info.isAvailable();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isWifi() {
        NetworkInfo ni;
        ConnectivityManager cm = getConnectivityManager();
        if (cm == null || (ni = cm.getActiveNetworkInfo()) == null || !ni.getTypeName().equals("WIFI")) {
            return false;
        }
        return true;
    }

    private static ConnectivityManager getConnectivityManager() {
        try {
            return (ConnectivityManager) CoreApplication.getApplication().getSystemService("connectivity");
        } catch (Exception e) {
            return null;
        }
    }
}
