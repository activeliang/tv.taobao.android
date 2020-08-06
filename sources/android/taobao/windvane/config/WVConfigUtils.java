package android.taobao.windvane.config;

import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.util.ConfigStorage;
import android.text.TextUtils;
import android.webkit.CookieManager;
import java.net.URLEncoder;

public class WVConfigUtils {
    public static final String SPNAME = TAG;
    protected static final String TAG = WVConfigUtils.class.getSimpleName();
    private static String appVersion = null;
    private static boolean isAppKeyAvailable = false;
    private static boolean isAppKeyChecked = false;

    public static boolean isNeedUpdate(boolean boot, String SPNAME2, String keyPrefix) {
        long interval = System.currentTimeMillis() - ConfigStorage.getLongVal(SPNAME2, keyPrefix + ConfigStorage.KEY_TIME);
        long maxAge = ConfigStorage.DEFAULT_MAX_AGE;
        if (boot) {
            maxAge = ConfigStorage.DEFAULT_SMALL_MAX_AGE;
        }
        return interval > maxAge || interval < 0;
    }

    /* access modifiers changed from: protected */
    public boolean needSaveConfig(String result) {
        if (!TextUtils.isEmpty(result) && new ApiResponse().parseJsonResult(result).success) {
            return true;
        }
        return false;
    }

    public static boolean isNeedUpdate(String newValue, String key) {
        Long newLongValue;
        String newStringValue = null;
        boolean needUpdate = false;
        if (newValue.contains(".")) {
            String[] array = newValue.split("\\.");
            if (array == null || array.length < 2) {
                return false;
            }
            newLongValue = Long.valueOf(Long.parseLong(array[0]));
            newStringValue = array[1];
        } else {
            newLongValue = Long.valueOf(Long.parseLong(newValue));
        }
        String[] array2 = ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, key, "0").split("\\.");
        if (newLongValue.longValue() > Long.parseLong(array2[0])) {
            needUpdate = true;
        } else if (newLongValue.longValue() != Long.parseLong(array2[0])) {
            needUpdate = false;
        } else if (newStringValue != null) {
            if (array2.length > 1 && !newStringValue.equals(array2[1])) {
                needUpdate = true;
            } else if (array2.length < 2) {
                needUpdate = true;
            }
        } else if (array2.length > 1) {
            needUpdate = true;
        }
        return needUpdate;
    }

    public static String getTargetValue() {
        try {
            String cookie = CookieManager.getInstance().getCookie("h5." + GlobalConfig.env.getValue() + ".taobao.com");
            int index = cookie.indexOf("abt=");
            char abt = index == -1 ? 'a' : cookie.charAt(index + 4);
            if ('a' <= abt && abt <= 'z') {
                String abtValue = String.valueOf(abt);
                if (abtValue.equals(ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, "abt", ""))) {
                    return abtValue;
                }
                ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, "abt", abtValue);
                return abtValue;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static synchronized boolean checkAppKeyAvailable() {
        boolean z;
        synchronized (WVConfigUtils.class) {
            if (isAppKeyChecked) {
                z = isAppKeyAvailable;
            } else {
                isAppKeyChecked = true;
                String appKey = GlobalConfig.getInstance().getAppKey();
                if (appKey == null) {
                    isAppKeyAvailable = false;
                    isAppKeyChecked = false;
                    z = isAppKeyAvailable;
                } else {
                    char[] keyArray = appKey.toCharArray();
                    int i = 0;
                    while (true) {
                        if (i >= keyArray.length) {
                            isAppKeyAvailable = true;
                            z = isAppKeyAvailable;
                            break;
                        } else if (keyArray[i] < '0' || keyArray[i] > '9') {
                            isAppKeyAvailable = false;
                            z = isAppKeyAvailable;
                        } else {
                            i++;
                        }
                    }
                    isAppKeyAvailable = false;
                    z = isAppKeyAvailable;
                }
            }
        }
        return z;
    }

    public static synchronized String dealAppVersion() {
        String str;
        synchronized (WVConfigUtils.class) {
            if (appVersion == null) {
                try {
                    appVersion = URLEncoder.encode(GlobalConfig.getInstance().getAppVersion(), "utf-8");
                    appVersion.replace("-", "%2D");
                } catch (Exception e) {
                }
            }
            str = appVersion;
        }
        return str;
    }
}
