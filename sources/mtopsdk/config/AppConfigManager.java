package mtopsdk.config;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.cache.domain.ApiCacheDo;
import mtopsdk.mtop.cache.domain.AppConfigDo;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class AppConfigManager {
    private static final String CACEH_KEY_TYPE = "kt=";
    private static final String CACHE_KEY_LIST = "ks=";
    private static final String FILE_DIR_MTOP = "/mtop";
    private static final String FILE_NAME_API_CACHE_CONFIG = "apiCacheConf";
    private static final String FILE_NAME_APP_CONFIG = "appConf";
    private static final String TAG = "mtopsdk.AppConfigManager";
    ConcurrentHashMap<String, ApiCacheDo> apiCacheGroup;
    private Set<String> tradeUnitApiSet;

    private AppConfigManager() {
        this.apiCacheGroup = new ConcurrentHashMap<>();
        this.tradeUnitApiSet = new HashSet();
    }

    private static class AppConfigManagerInstanceHolder {
        /* access modifiers changed from: private */
        public static AppConfigManager instance = new AppConfigManager();

        private AppConfigManagerInstanceHolder() {
        }
    }

    public static AppConfigManager getInstance() {
        return AppConfigManagerInstanceHolder.instance;
    }

    public ApiCacheDo getApiCacheDoByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return this.apiCacheGroup.get(key);
    }

    public void addApiCacheDoToGroup(String key, ApiCacheDo apiCacheDo) {
        if (!StringUtils.isBlank(key) && apiCacheDo != null) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(TAG, "[addApiCacheDoToGroup] apiCacheDo:" + apiCacheDo);
            }
            this.apiCacheGroup.put(key, apiCacheDo);
        }
    }

    public boolean isTradeUnitApi(String apiKey) {
        return this.tradeUnitApiSet.contains(apiKey);
    }

    public void parseCacheControlHeader(@NonNull String cacheControlHeaderStr, @NonNull ApiCacheDo apiCacheDo) {
        if (cacheControlHeaderStr != null && apiCacheDo != null) {
            for (String item : cacheControlHeaderStr.split(",")) {
                try {
                    if (HttpHeaderConstant.OFFLINE_FLAG_ON.equalsIgnoreCase(item)) {
                        apiCacheDo.offline = true;
                    } else if (HttpHeaderConstant.PRIVATE_FLAG_FALSE.equalsIgnoreCase(item)) {
                        apiCacheDo.privateScope = false;
                    } else if (item.contains(CACEH_KEY_TYPE)) {
                        String kt = item.substring(CACEH_KEY_TYPE.length());
                        char c = 65535;
                        switch (kt.hashCode()) {
                            case 64897:
                                if (kt.equals("ALL")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 69104:
                                if (kt.equals(ApiCacheDo.CacheKeyType.EXC)) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case 72638:
                                if (kt.equals(ApiCacheDo.CacheKeyType.INC)) {
                                    c = 2;
                                    break;
                                }
                                break;
                            case 2402104:
                                if (kt.equals("NONE")) {
                                    c = 1;
                                    break;
                                }
                                break;
                        }
                        switch (c) {
                            case 0:
                                apiCacheDo.cacheKeyType = "ALL";
                                break;
                            case 1:
                                apiCacheDo.cacheKeyType = "NONE";
                                break;
                            case 2:
                                apiCacheDo.cacheKeyType = ApiCacheDo.CacheKeyType.INC;
                                break;
                            case 3:
                                apiCacheDo.cacheKeyType = ApiCacheDo.CacheKeyType.EXC;
                                break;
                        }
                    } else {
                        if (item.contains(CACHE_KEY_LIST)) {
                            apiCacheDo.cacheKeyItems = Arrays.asList(item.substring(CACHE_KEY_LIST.length()).split(SymbolExpUtil.SYMBOL_VERTICALBAR));
                        }
                        apiCacheDo.cacheControlHeader = cacheControlHeaderStr;
                    }
                } catch (Exception e) {
                    TBSdkLog.w(TAG, "[parseCacheControlHeader] parse item in CacheControlHeader error.item =" + item + ",CacheControlHeader=" + cacheControlHeaderStr);
                }
            }
        }
    }

    public boolean parseAppConfig(@NonNull String appConfig, String seqNo) {
        JSONArray tradeUnitApiList;
        try {
            JSONObject appConfigJson = new JSONObject(appConfig);
            JSONObject clientCacheJson = appConfigJson.optJSONObject("clientCache");
            if (clientCacheJson == null) {
                return false;
            }
            JSONArray appConfigList = clientCacheJson.optJSONArray("clientCacheAppConfList");
            if (appConfigList == null) {
                return false;
            }
            for (int index = appConfigList.length() - 1; index >= 0; index--) {
                JSONObject object = appConfigList.optJSONObject(index);
                if (object != null) {
                    String api = object.optString("api");
                    String v = object.optString("v");
                    String block = object.optString("block");
                    String key = StringUtils.concatStr2LowerCase(api, v);
                    ApiCacheDo apiCache = getInstance().getApiCacheDoByKey(key);
                    if (apiCache != null) {
                        apiCache.blockName = block;
                    } else {
                        getInstance().addApiCacheDoToGroup(key, new ApiCacheDo(api, v, block));
                    }
                }
            }
            JSONObject unitJson = appConfigJson.optJSONObject("unit");
            if (!(unitJson == null || (tradeUnitApiList = unitJson.optJSONArray("tradeUnitApiList")) == null)) {
                HashSet<String> temp = new HashSet<>();
                for (int index2 = tradeUnitApiList.length() - 1; index2 >= 0; index2--) {
                    JSONObject object2 = tradeUnitApiList.optJSONObject(index2);
                    if (object2 != null) {
                        temp.add(StringUtils.concatStr2LowerCase(object2.optString("api"), object2.optString("v")));
                    }
                }
                this.tradeUnitApiSet = temp;
            }
            return true;
        } catch (Exception e) {
            TBSdkLog.e(TAG, seqNo, "[parseAppConfig]parse appConf node error.", e);
            return false;
        }
    }

    public void reloadAppConfig(MtopConfig mtopConfig) {
        if (mtopConfig != null) {
            try {
                File fileDir = new File(mtopConfig.context.getExternalFilesDir((String) null).getAbsoluteFile() + FILE_DIR_MTOP);
                AppConfigDo appConfigDo = (AppConfigDo) MtopUtils.readObject(fileDir, FILE_NAME_APP_CONFIG);
                if (appConfigDo != null && StringUtils.isNotBlank(appConfigDo.appConf) && appConfigDo.appConfigVersion > mtopConfig.xAppConfigVersion) {
                    synchronized (mtopConfig.lock) {
                        if (appConfigDo.appConfigVersion > mtopConfig.xAppConfigVersion && getInstance().parseAppConfig(appConfigDo.appConf, "")) {
                            mtopConfig.xAppConfigVersion = appConfigDo.appConfigVersion;
                            TBSdkLog.i(TAG, "[reloadAppConfig] reload appConf succeed. appConfVersion=" + mtopConfig.xAppConfigVersion);
                        }
                    }
                }
                Map<String, ApiCacheDo> apiCacheDoMap = (Map) MtopUtils.readObject(fileDir, FILE_NAME_API_CACHE_CONFIG);
                if (apiCacheDoMap != null) {
                    for (Map.Entry<String, ApiCacheDo> entry : apiCacheDoMap.entrySet()) {
                        String apiKey = entry.getKey();
                        ApiCacheDo apiCacheDoWithConfig = entry.getValue();
                        ApiCacheDo apiCacheDo = this.apiCacheGroup.get(apiKey);
                        if (apiCacheDo == null) {
                            this.apiCacheGroup.put(apiKey, apiCacheDoWithConfig);
                            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                                TBSdkLog.i(TAG, "[reloadAppConfig] add apiCacheDo config,apiKey=" + apiKey);
                            }
                        } else if (!apiCacheDo.equals(apiCacheDoWithConfig)) {
                            apiCacheDo.cacheControlHeader = apiCacheDoWithConfig.cacheControlHeader;
                            apiCacheDo.privateScope = apiCacheDoWithConfig.privateScope;
                            apiCacheDo.offline = apiCacheDoWithConfig.offline;
                            apiCacheDo.cacheKeyType = apiCacheDoWithConfig.cacheKeyType;
                            apiCacheDo.cacheKeyItems = apiCacheDoWithConfig.cacheKeyItems;
                            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                                TBSdkLog.i(TAG, "[reloadAppConfig] update apiCacheDo config,apiKey=" + apiKey);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                TBSdkLog.e(TAG, "[reloadAppConfig] reload appConf file error.");
            }
        }
    }

    public void storeApiCacheDoMap(final Context context, final String seqNo) {
        MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
            public void run() {
                try {
                    MtopUtils.writeObject(AppConfigManager.this.apiCacheGroup, new File(context.getExternalFilesDir((String) null).getAbsoluteFile() + AppConfigManager.FILE_DIR_MTOP), AppConfigManager.FILE_NAME_API_CACHE_CONFIG);
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(AppConfigManager.TAG, seqNo, "[storeApiCacheDoMap] save apiCacheConf succeed.");
                    }
                } catch (Exception e) {
                    TBSdkLog.e(AppConfigManager.TAG, seqNo, "[storeApiCacheDoMap] save apiCacheConf error.", e);
                }
            }
        });
    }
}
