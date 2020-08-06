package mtopsdk.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.TBSdkLog;

public class ConfigStoreManager {
    public static final String APICACHE_STATS_STORE_PREFIX = "APICACHE_STATS_STORE.";
    public static final String API_CONFIG_STORE_PREFIX = "API_CONFIG_STORE.";
    public static final String API_UNIT_ITEM = "API_UNIT_ITEM";
    public static final String CACHE_CONFIG_STORE_PREFIX = "CACHE_CONFIG_STORE.";
    public static final String MTOP_CONFIG_STORE = "MtopConfigStore";
    public static final String PHONE_INFO_STORE_PREFIX = "PHONE_INFO_STORE.";
    private static final String TAG = "mtopsdk.ConfigStoreManager";
    public static final String UNIT_SETTING_STORE_PREFIX = "UNIT_SETTING_STORE.";
    private static volatile ConfigStoreManager instance;

    private ConfigStoreManager() {
    }

    public static ConfigStoreManager getInstance() {
        if (instance == null) {
            synchronized (ConfigStoreManager.class) {
                if (instance == null) {
                    instance = new ConfigStoreManager();
                }
            }
        }
        return instance;
    }

    public boolean saveConfigItem(Context context, String store, String keyPrefix, String key, String value) {
        boolean result = false;
        if (context == null || StringUtils.isBlank(store) || StringUtils.isBlank(key)) {
            return false;
        }
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(store, 0).edit();
            if (StringUtils.isNotBlank(keyPrefix)) {
                editor.putString(keyPrefix + key, value);
            } else {
                editor.putString(key, value);
            }
            if (Build.VERSION.SDK_INT >= 9) {
                editor.apply();
            } else {
                editor.commit();
            }
            result = true;
        } catch (Exception e) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                TBSdkLog.w(TAG, "[saveConfigItem] saveConfigItem error,store=" + store + ",keyprefix=" + keyPrefix + ",key=" + key);
            }
        }
        return result;
    }

    public String getConfigItem(Context context, String store, String keyPrefix, String key) {
        if (context == null || StringUtils.isBlank(store) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            SharedPreferences storage = context.getSharedPreferences(store, 0);
            if (StringUtils.isNotBlank(keyPrefix)) {
                return storage.getString(keyPrefix + key, (String) null);
            }
            return storage.getString(key, (String) null);
        } catch (Exception e) {
            if (!TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                return null;
            }
            TBSdkLog.w(TAG, "[getConfigItem] getConfigItem error,store=" + store + ",keyprefix=" + keyPrefix + ",key=" + key);
            return null;
        }
    }

    public Map<String, String> getAllConfigItems(Context context, String store) {
        if (context == null || StringUtils.isBlank(store)) {
            return null;
        }
        try {
            return context.getSharedPreferences(store, 0).getAll();
        } catch (Exception e) {
            if (!TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                return null;
            }
            TBSdkLog.w(TAG, "[getAllConfigItems] getConfigItem error,store=" + store);
            return null;
        }
    }

    public Map<String, String> getAllConfigItemsByPrefix(Context context, String store, String prefix) {
        if (context == null || StringUtils.isBlank(store)) {
            return null;
        }
        try {
            Map<String, ?> all = context.getSharedPreferences(store, 0).getAll();
            if (StringUtils.isBlank(prefix)) {
                return all;
            }
            if (all != null && !all.isEmpty()) {
                Map<String, String> filteredMap = new HashMap<>();
                for (Map.Entry<String, String> entry : all.entrySet()) {
                    String key = entry.getKey();
                    if (StringUtils.isNotBlank(key) && key.startsWith(prefix)) {
                        filteredMap.put(key, entry.getValue());
                    }
                }
                return filteredMap;
            }
            return null;
        } catch (Exception e) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                TBSdkLog.w(TAG, "[getAllConfigItemsByPrefix] getAllConfigItemsByPrefix error,store=" + store + ",prefix=" + prefix);
            }
        }
    }
}
