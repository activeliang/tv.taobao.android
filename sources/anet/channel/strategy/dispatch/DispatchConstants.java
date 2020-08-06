package anet.channel.strategy.dispatch;

import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.util.StringUtils;

public class DispatchConstants {
    public static String[] AMDC_SERVER_DOMAIN = {"amdc.m.taobao.com", "amdc.wapa.taobao.com", "amdc.taobao.net"};
    public static String[][] AMDC_SERVER_FIX_IP = {new String[]{StringUtils.longToIP(140205163087L), StringUtils.longToIP(140205160063L)}, new String[]{StringUtils.longToIP(106011052006L)}, null};
    public static final String ANDROID = "android";
    public static final String APPKEY = "appkey";
    public static final String APP_NAME = "appName";
    public static final String APP_VERSION = "appVersion";
    public static final String BSSID = "bssid";
    public static final String CARRIER = "carrier";
    public static final String CHANNEL = "channel";
    public static final String CONFIG_VERSION = "cv";
    public static final String DEVICEID = "deviceId";
    public static final String DOMAIN = "domain";
    public static final String HOSTS = "hosts";
    public static final String LATITUDE = "lat";
    public static final String LONGTITUDE = "lng";
    public static final String MACHINE = "machine";
    public static final String MNC = "mnc";
    public static final String NET_TYPE = "netType";
    public static final String OTHER = "other";
    public static final String PLATFORM = "platform";
    public static final String PLATFORM_VERSION = "platformVersion";
    public static final String PRE_IP = "preIp";
    public static final String SID = "sid";
    public static final String SIGN = "sign";
    public static final String SIGNTYPE = "signType";
    public static final String SIGN_SPLIT_SYMBOL = "&";
    public static final String TIMESTAMP = "t";
    public static final String VERSION = "v";
    public static final String VER_CODE = "4.0";
    public static String[] initHostArray = new String[0];
    public static final String serverPath = "/amdc/mobileDispatch";

    public static void setAmdcServerDomain(String[] domains) {
        if (domains == null || domains.length < 2) {
            throw new IllegalArgumentException("domains is null or length < 2");
        }
        for (int i = 0; i < domains.length; i++) {
            if (TextUtils.isEmpty(domains[i])) {
                throw new IllegalArgumentException("domains[" + i + "] is null or empty");
            }
        }
        AMDC_SERVER_DOMAIN = domains;
    }

    public static void setAmdcServerFixIp(String[][] ips) {
        if (ips == null || ips.length < 2) {
            throw new IllegalArgumentException("ips is null or length < 2");
        }
        AMDC_SERVER_FIX_IP = ips;
    }

    public static String getAmdcServerDomain() {
        return AMDC_SERVER_DOMAIN[GlobalAppRuntimeInfo.getEnv().getEnvMode()];
    }

    public static boolean isAmdcServerDomain(String host) {
        if (TextUtils.isEmpty(host)) {
            return false;
        }
        return host.equalsIgnoreCase(getAmdcServerDomain());
    }

    public static String[] getAmdcServerFixIp() {
        return AMDC_SERVER_FIX_IP[GlobalAppRuntimeInfo.getEnv().getEnvMode()];
    }
}
