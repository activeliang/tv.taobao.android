package com.yunos.tvtaobao.payment.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class TvTaoUtils {
    public static String getIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
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

    public static String getUmtoken(Context context) {
        try {
            return SecurityGuardManager.getInstance(context).getUMIDComp().getSecurityToken(0);
        } catch (SecException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }
}
