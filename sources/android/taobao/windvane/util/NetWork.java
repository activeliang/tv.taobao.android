package android.taobao.windvane.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.os.Build;
import android.taobao.windvane.config.GlobalConfig;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.util.HashMap;
import org.apache.http.HttpHost;

public class NetWork {
    public static final int CHINA_MOBILE = 1;
    public static final int CHINA_TELECOM = 3;
    public static final int CHINA_UNICOM = 2;
    public static final String CONN_TYPE_GPRS = "gprs";
    public static final String CONN_TYPE_NONE = "none";
    public static final String CONN_TYPE_WIFI = "wifi";
    public static final int SIM_NO = -1;
    public static final int SIM_OK = 0;
    public static final int SIM_UNKNOW = -2;
    private static BroadcastReceiver connChangerRvr;
    public static boolean proxy = false;

    public static boolean isAvailable(Context context) {
        if (getNetworkType(context) >= 0) {
            return true;
        }
        return false;
    }

    public static int getNetworkType(Context context) {
        NetworkInfo info;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity == null || (info = connectivity.getActiveNetworkInfo()) == null || !info.isConnectedOrConnecting()) {
            return -9;
        }
        return info.getType();
    }

    public static String getNetworkInfo(Context context) {
        NetworkInfo info;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity == null || (info = connectivity.getActiveNetworkInfo()) == null) {
            return null;
        }
        NetworkInfo.State tem = info.getState();
        if (tem == NetworkInfo.State.CONNECTED || tem == NetworkInfo.State.CONNECTING) {
            return info.getTypeName() + " " + info.getSubtypeName() + info.getExtraInfo();
        }
        return null;
    }

    public static int getSimState(Context context) {
        int simState = ((TelephonyManager) context.getSystemService("phone")).getSimState();
        if (simState == 5) {
            return 0;
        }
        if (simState == 1) {
            return -1;
        }
        return -2;
    }

    public static int getNSP(Context context) {
        if (getSimState(context) != 0) {
            return -1;
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        String operator = tm.getNetworkOperatorName().replaceAll(" ", "");
        String numeric = tm.getNetworkOperator();
        TaoLog.d("NSP: ", "operator = " + operator + "  type = " + numeric);
        if ((operator == null || "".equals("")) && numeric != null) {
            operator = numeric;
        }
        if (operator == null || operator.length() == 0) {
            return -2;
        }
        if (operator.compareToIgnoreCase("中国移动") == 0 || operator.compareToIgnoreCase("CMCC") == 0 || operator.compareToIgnoreCase("ChinaMobile") == 0 || operator.compareToIgnoreCase("46000") == 0) {
            return 1;
        }
        if (operator.compareToIgnoreCase("中国电信") == 0 || operator.compareToIgnoreCase("ChinaTelecom") == 0 || operator.compareToIgnoreCase("46003") == 0 || operator.compareToIgnoreCase("ChinaTelcom") == 0 || operator.compareToIgnoreCase("460003") == 0) {
            return 3;
        }
        if (operator.compareToIgnoreCase("中国联通") == 0 || operator.compareToIgnoreCase("ChinaUnicom") == 0 || operator.compareToIgnoreCase("46001") == 0 || operator.compareToIgnoreCase("CU-GSM") == 0 || operator.compareToIgnoreCase("CHN-CUGSM") == 0 || operator.compareToIgnoreCase("CHNUnicom") == 0) {
            return 2;
        }
        String imsi = PhoneInfo.getImsi(context);
        if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
            return 1;
        }
        if (imsi.startsWith("46001")) {
            return 2;
        }
        if (imsi.startsWith("46003")) {
            return 3;
        }
        return -2;
    }

    public static void unRegNetWorkRev(Context context) {
        setProxy((String) null, (String) null);
        try {
            if (connChangerRvr != null) {
                context.unregisterReceiver(connChangerRvr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setProxy(String host, String port) {
        if (host == null || host.length() == 0) {
            System.getProperties().put("proxySet", "false");
            proxy = false;
            return;
        }
        proxy = true;
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", host);
        if (port == null || port.length() <= 0) {
            System.getProperties().put("proxyPort", "80");
        } else {
            System.getProperties().put("proxyPort", port);
        }
    }

    public static HashMap<String, String> getProxyInfo(Context context, Uri uri) {
        String result = getNetworkInfo(context);
        HashMap<String, String> proxy2 = new HashMap<>();
        if (result == null) {
            return null;
        }
        TaoLog.d("getProxyInfo", "current network:" + result);
        if (result.contains("WIFI") || result.compareToIgnoreCase("MOBILE UMTS") == 0) {
            return proxy2;
        }
        Cursor cr = null;
        try {
            cr = context.getContentResolver().query(uri, (String[]) null, "mcc =?", new String[]{"460"}, (String) null);
            if (cr.moveToFirst()) {
                do {
                    if (cr.getCount() > 0) {
                        proxy2.put("host", cr.getString(cr.getColumnIndex("proxy")));
                        proxy2.put("port", cr.getString(cr.getColumnIndex("port")));
                        String apn = cr.getString(cr.getColumnIndex("apn"));
                        TaoLog.d("getProxyInfo", "apn:" + apn);
                        if (result.contains(apn)) {
                            if (cr == null) {
                                return proxy2;
                            }
                            cr.close();
                            return proxy2;
                        }
                    }
                } while (cr.moveToNext());
            }
            if (cr != null) {
                cr.close();
            }
        } catch (Exception e) {
            if (cr != null) {
                cr.close();
            }
        } catch (Throwable th) {
            if (cr != null) {
                cr.close();
            }
            throw th;
        }
        return null;
    }

    public static HttpHost getHttpsProxyInfo(Context context) {
        HttpHost proxy2 = null;
        if (Build.VERSION.SDK_INT < 11) {
            NetworkInfo info = null;
            try {
                info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (info != null && info.isAvailable() && info.getType() == 0) {
                String proxyHost = Proxy.getDefaultHost();
                int port = Proxy.getDefaultPort();
                if (proxyHost != null) {
                    proxy2 = new HttpHost(proxyHost, port);
                }
            }
            return proxy2;
        }
        String httpsproxyhost = System.getProperty("https.proxyHost");
        String proxyport = System.getProperty("https.proxyPort");
        if (!TextUtils.isEmpty(httpsproxyhost)) {
            proxy2 = new HttpHost(httpsproxyhost, Integer.parseInt(proxyport));
        }
        return proxy2;
    }

    public static String getNetConnType(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connManager == null) {
            TaoLog.w("Network", "can not get Context.CONNECTIVITY_SERVICE");
            return "none";
        }
        NetworkInfo info = connManager.getNetworkInfo(1);
        if (info != null) {
            if (NetworkInfo.State.CONNECTED == info.getState()) {
                return "wifi";
            }
        } else {
            TaoLog.w("Network", "can not get ConnectivityManager.TYPE_WIFI");
        }
        NetworkInfo info2 = connManager.getNetworkInfo(0);
        if (info2 != null) {
            if (NetworkInfo.State.CONNECTED == info2.getState()) {
                return CONN_TYPE_GPRS;
            }
        } else {
            TaoLog.w("Network", "can not get ConnectivityManager.TYPE_MOBILE");
        }
        return "none";
    }

    public static boolean isWiFiActive() {
        return isWiFiActive(GlobalConfig.context);
    }

    public static boolean isWiFiActive(Context cxt) {
        boolean z = true;
        if (cxt == null) {
            return false;
        }
        try {
            if (getNetworkType(cxt) != 1) {
                z = false;
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }
}
