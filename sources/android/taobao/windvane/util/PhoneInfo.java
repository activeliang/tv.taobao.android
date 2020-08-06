package android.taobao.windvane.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import java.util.Random;

public class PhoneInfo {
    public static final String IMEI = "imei";
    public static final String IMSI = "imsi";
    public static final String MACADDRESS = "mac_address";

    private static String generateImei() {
        try {
            StringBuilder imei = new StringBuilder();
            long time = System.currentTimeMillis();
            String currentTime = Long.toString(time);
            imei.append(currentTime.substring(currentTime.length() - 5));
            StringBuilder model = new StringBuilder();
            model.append(Build.MODEL.replaceAll(" ", ""));
            while (model.length() < 6) {
                model.append('0');
            }
            imei.append(model.substring(0, 6));
            Random random = new Random(time);
            long tmp = 0;
            while (tmp < PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
                tmp = random.nextLong();
            }
            imei.append(Long.toHexString(tmp).substring(0, 4));
            return imei.toString();
        } catch (Throwable th) {
            return "";
        }
    }

    public static String getImei(Context context) {
        try {
            SharedPreferences sp = context.getSharedPreferences("imei", 0);
            String imei = sp.getString("imei", (String) null);
            if (imei == null || imei.length() == 0) {
                String imei2 = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
                if (imei2 == null || imei2.length() == 0) {
                    imei2 = generateImei();
                }
                imei = imei2.replaceAll(" ", "").trim();
                while (imei.length() < 15) {
                    imei = "0" + imei;
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("imei", imei);
                editor.commit();
            }
            return imei.trim();
        } catch (Throwable th) {
            return "";
        }
    }

    public static String getImsi(Context context) {
        SharedPreferences sp = context.getSharedPreferences("imei", 0);
        String imsi = sp.getString("imsi", (String) null);
        if (imsi == null || imsi.length() == 0) {
            String imsi2 = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
            if (imsi2 == null || imsi2.length() == 0) {
                imsi2 = generateImei();
            }
            imsi = imsi2.replaceAll(" ", "").trim();
            while (imsi.length() < 15) {
                imsi = "0" + imsi;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("imsi", imsi);
            editor.commit();
        }
        return imsi;
    }

    public static String getLocalMacAddress(Context context) {
        String wifiaddr = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        if (wifiaddr == null || "".equals(wifiaddr)) {
            return context.getSharedPreferences(MACADDRESS, 0).getString(MACADDRESS, "");
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(MACADDRESS, 0).edit();
        editor.putString(MACADDRESS, wifiaddr);
        editor.commit();
        return wifiaddr;
    }

    public static String getOriginalImei(Context context) {
        String imei = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (imei != null) {
            return imei.trim();
        }
        return imei;
    }

    public static String getOriginalImsi(Context context) {
        String imsi = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        if (imsi != null) {
            return imsi.trim();
        }
        return imsi;
    }

    public static String getSerialNum() {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            return (String) c.getMethod("get", new Class[]{String.class, String.class}).invoke(c, new Object[]{"ro.serialno", "unknown"});
        } catch (Exception e) {
            return null;
        }
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }
}
