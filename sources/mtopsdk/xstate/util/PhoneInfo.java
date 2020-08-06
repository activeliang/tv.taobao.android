package mtopsdk.xstate.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import java.util.Random;
import mtopsdk.common.util.ConfigStoreManager;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.common.util.TBSdkLog;

public class PhoneInfo {
    private static final String IMEI = "mtopsdk_imei";
    private static final String IMSI = "mtopsdk_imsi";
    private static final String MACADDRESS = "mtopsdk_mac_address";
    private static final String TAG = "mtopsdk.PhoneInfo";
    private static ConfigStoreManager storeManager = ConfigStoreManager.getInstance();

    private static String generateImei() {
        StringBuffer imei = new StringBuffer();
        try {
            long time = System.currentTimeMillis();
            String currentTime = String.valueOf(time);
            imei.append(currentTime.substring(currentTime.length() - 5));
            StringBuffer model = new StringBuffer();
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
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[generateImei] error --->" + e.toString());
        }
        return imei.toString();
    }

    @TargetApi(8)
    public static String getImei(Context context) {
        try {
            String imei = storeManager.getConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, ConfigStoreManager.PHONE_INFO_STORE_PREFIX, IMEI);
            if (StringUtils.isNotBlank(imei)) {
                return new String(Base64.decode(imei, 0));
            }
            String imei2 = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            if (StringUtils.isBlank(imei2)) {
                imei2 = generateImei();
            }
            String imei3 = imei2.replaceAll(" ", "").trim();
            while (imei3.length() < 15) {
                imei3 = "0" + imei3;
            }
            storeManager.saveConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, ConfigStoreManager.PHONE_INFO_STORE_PREFIX, IMEI, Base64.encodeToString(imei3.getBytes(), 0));
            return imei3 == null ? "" : imei3.trim();
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[getImei] error ---" + e.toString());
            return "";
        }
    }

    @TargetApi(8)
    public static String getImsi(Context context) {
        String imsi = "";
        try {
            String imsi2 = storeManager.getConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, ConfigStoreManager.PHONE_INFO_STORE_PREFIX, IMSI);
            if (StringUtils.isNotBlank(imsi2)) {
                return new String(Base64.decode(imsi2, 0));
            }
            String imsi3 = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
            if (StringUtils.isBlank(imsi3)) {
                imsi3 = generateImei();
            }
            imsi = imsi3.replaceAll(" ", "").trim();
            while (imsi.length() < 15) {
                imsi = "0" + imsi;
            }
            storeManager.saveConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, ConfigStoreManager.PHONE_INFO_STORE_PREFIX, IMSI, Base64.encodeToString(imsi.getBytes(), 0));
            return imsi;
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[getImsi]error ---" + e.toString());
        }
    }

    public static String getPhoneBaseInfo(Context context) {
        try {
            String osrelease = Build.VERSION.RELEASE;
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            StringBuilder phoneBaseInfo = new StringBuilder(32);
            phoneBaseInfo.append("MTOPSDK/").append(HttpHeaderConstant.M_SDKVER_VALUE);
            phoneBaseInfo.append(" (").append("Android").append(SymbolExpUtil.SYMBOL_SEMICOLON);
            phoneBaseInfo.append(osrelease).append(SymbolExpUtil.SYMBOL_SEMICOLON);
            phoneBaseInfo.append(manufacturer).append(SymbolExpUtil.SYMBOL_SEMICOLON);
            phoneBaseInfo.append(model).append(")");
            return phoneBaseInfo.toString();
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[getPhoneBaseInfo] error ---" + e.toString());
            return "";
        }
    }

    public static String getOriginalImei(Context context) {
        String imei = null;
        if (context == null) {
            return null;
        }
        try {
            imei = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            if (imei != null) {
                imei = imei.trim();
            }
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[getOriginalImei]error ---" + e.toString());
        }
        return imei;
    }

    public static String getOriginalImsi(Context context) {
        String imsi = null;
        if (context == null) {
            return null;
        }
        try {
            imsi = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
            if (imsi != null) {
                imsi = imsi.trim();
            }
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[getOriginalImsi]error ---" + e.toString());
        }
        return imsi;
    }

    public static String getSerialNum() {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            return (String) c.getMethod("get", new Class[]{String.class, String.class}).invoke(c, new Object[]{"ro.serialno", "unknown"});
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[getSerialNum]error ---" + e.toString());
            return null;
        }
    }

    @TargetApi(3)
    public static String getAndroidId(Context context) {
        String androidId = null;
        if (context == null) {
            return null;
        }
        try {
            androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[getAndroidId]error ---" + e.toString());
        }
        return androidId;
    }

    @TargetApi(8)
    public static String getLocalMacAddress(Context context) {
        WifiInfo info;
        if (context == null) {
            return "";
        }
        try {
            String wifiaddr = storeManager.getConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, ConfigStoreManager.PHONE_INFO_STORE_PREFIX, MACADDRESS);
            if (StringUtils.isNotBlank(wifiaddr)) {
                return new String(Base64.decode(wifiaddr, 0));
            }
            WifiManager wifi = (WifiManager) context.getSystemService("wifi");
            if (!(wifi == null || (info = wifi.getConnectionInfo()) == null)) {
                wifiaddr = info.getMacAddress();
            }
            if (!StringUtils.isNotBlank(wifiaddr)) {
                return wifiaddr;
            }
            storeManager.saveConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, ConfigStoreManager.PHONE_INFO_STORE_PREFIX, MACADDRESS, Base64.encodeToString(wifiaddr.getBytes(), 0));
            return wifiaddr;
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[getLocalMacAddress]error ---" + e.toString());
            return "";
        }
    }
}
