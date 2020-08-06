package mtopsdk.mtop.cache;

import anetwork.network.cache.Cache;
import anetwork.network.cache.RpcCache;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.config.AppConfigManager;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.mtop.cache.domain.ApiCacheDo;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.global.SDKUtils;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.protocol.converter.util.NetworkConverterUtils;
import mtopsdk.mtop.util.ReflectUtil;
import mtopsdk.network.domain.Request;
import mtopsdk.xstate.XState;

public class CacheManagerImpl implements CacheManager {
    private static final String METHOD_GET = "GET";
    private static final String QUERY_KEY_DATA = "data";
    private static final String QUERY_KEY_WUA = "wua";
    private static final String TAG = "mtopsdk.CacheManagerImpl";
    private Cache cache = null;

    public CacheManagerImpl(Cache cache2) {
        this.cache = cache2;
    }

    public boolean isNeedReadCache(Request request, MtopListener callback) {
        if (!SwitchConfig.getInstance().isGlobalCacheSwitchOpen()) {
            TBSdkLog.i(TAG, request.seqNo, "[isNeedReadCache]GlobalCacheSwitch=false,Don't read local cache.");
            return false;
        } else if (request == null || !"GET".equalsIgnoreCase(request.method) || HttpHeaderConstant.NO_CACHE.equalsIgnoreCase(request.header("cache-control"))) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isNeedWriteCache(Request request, Map<String, List<String>> responseHeader) {
        if (!SwitchConfig.getInstance().isGlobalCacheSwitchOpen()) {
            TBSdkLog.i(TAG, request.seqNo, "[isNeedWriteCache]GlobalCacheSwitch=false,Don't write local cache.");
            return false;
        } else if (!"GET".equalsIgnoreCase(request.method) || responseHeader == null) {
            return false;
        } else {
            String cacheControl = HeaderHandlerUtil.getSingleHeaderFieldByKey(responseHeader, "cache-control");
            if (cacheControl != null && cacheControl.contains(HttpHeaderConstant.NO_CACHE)) {
                return false;
            }
            String lastModified = HeaderHandlerUtil.getSingleHeaderFieldByKey(responseHeader, "last-modified");
            String etag = HeaderHandlerUtil.getSingleHeaderFieldByKey(responseHeader, HttpHeaderConstant.MTOP_X_ETAG);
            if (etag == null) {
                etag = HeaderHandlerUtil.getSingleHeaderFieldByKey(responseHeader, "etag");
            }
            if (cacheControl == null && lastModified == null && etag == null) {
                return false;
            }
            return true;
        }
    }

    public RpcCache getCache(String cacheKey, String blockName, String seqNo) {
        if (this.cache == null) {
            return null;
        }
        RpcCache cacheResponse = this.cache.get(cacheKey, blockName);
        if (cacheResponse != null) {
            return handleCacheValidation(seqNo, cacheResponse);
        }
        return cacheResponse;
    }

    public boolean putCache(String cacheKey, String blockName, MtopResponse response) {
        if (this.cache == null) {
            return false;
        }
        RpcCache cacheRequest = new RpcCache();
        cacheRequest.header = response.getHeaderFields();
        cacheRequest.body = response.getBytedata();
        return this.cache.put(cacheKey, blockName, handleResponseCacheFlag(response.getMtopStat() != null ? response.getMtopStat().seqNo : "", cacheRequest));
    }

    public String getCacheKey(MtopContext mtopContext) {
        URL cacheUrl;
        String userId;
        if (mtopContext == null) {
            return null;
        }
        MtopRequest mtopRequest = mtopContext.mtopRequest;
        MtopNetworkProp mtopProperty = mtopContext.property;
        String baseUrl = mtopContext.baseUrl;
        Map<String, String> queryParams = mtopContext.queryParams;
        if (mtopRequest == null || mtopProperty == null || baseUrl == null || queryParams == null) {
            return null;
        }
        boolean privateScope = true;
        String cacheKeyType = "ALL";
        List<String> cacheKeyItems = null;
        ApiCacheDo apiCacheDo = AppConfigManager.getInstance().getApiCacheDoByKey(mtopRequest.getKey());
        if (apiCacheDo != null) {
            privateScope = apiCacheDo.privateScope;
            cacheKeyType = apiCacheDo.cacheKeyType;
            cacheKeyItems = apiCacheDo.cacheKeyItems;
        }
        char c = 65535;
        switch (cacheKeyType.hashCode()) {
            case 64897:
                if (cacheKeyType.equals("ALL")) {
                    c = 0;
                    break;
                }
                break;
            case 69104:
                if (cacheKeyType.equals(ApiCacheDo.CacheKeyType.EXC)) {
                    c = 2;
                    break;
                }
                break;
            case 72638:
                if (cacheKeyType.equals(ApiCacheDo.CacheKeyType.INC)) {
                    c = 3;
                    break;
                }
                break;
            case 2402104:
                if (cacheKeyType.equals("NONE")) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                cacheUrl = NetworkConverterUtils.initUrl(baseUrl, queryParams);
                break;
            case 1:
                cacheUrl = NetworkConverterUtils.initUrl(baseUrl, (Map<String, String>) null);
                break;
            case 2:
                if (cacheKeyItems == null) {
                    cacheKeyItems = mtopProperty.cacheKeyBlackList;
                }
                if (cacheKeyItems != null) {
                    if (mtopRequest.dataParams != null) {
                        for (String item : cacheKeyItems) {
                            mtopRequest.dataParams.remove(item);
                        }
                    }
                    String dataStrExcludeKey = ReflectUtil.convertMapToDataStr(mtopRequest.dataParams);
                    Map<String, String> clonedParams = new HashMap<>();
                    for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                        if ("data".equals(entry.getKey())) {
                            clonedParams.put(entry.getKey(), dataStrExcludeKey);
                        } else if (!"wua".equalsIgnoreCase(entry.getKey())) {
                            clonedParams.put(entry.getKey(), entry.getValue());
                        }
                    }
                    cacheUrl = NetworkConverterUtils.initUrl(baseUrl, clonedParams);
                    break;
                } else {
                    cacheUrl = NetworkConverterUtils.initUrl(baseUrl, queryParams);
                    break;
                }
            case 3:
                if (cacheKeyItems != null) {
                    Map<String, String> includeMap = null;
                    if (mtopRequest.dataParams != null) {
                        includeMap = new HashMap<>(mtopRequest.dataParams.size());
                        for (String item2 : cacheKeyItems) {
                            includeMap.put(item2, mtopRequest.dataParams.get(item2));
                        }
                    }
                    cacheUrl = NetworkConverterUtils.initUrl(baseUrl, includeMap);
                    break;
                } else {
                    cacheUrl = NetworkConverterUtils.initUrl(baseUrl, (Map<String, String>) null);
                    break;
                }
            default:
                cacheUrl = NetworkConverterUtils.initUrl(baseUrl, queryParams);
                break;
        }
        if (cacheUrl == null) {
            return null;
        }
        try {
            StringBuilder cacheKey = new StringBuilder(cacheUrl.getFile());
            if (privateScope && (userId = mtopContext.mtopInstance.getMultiAccountUserId(mtopProperty.userInfo)) != null) {
                cacheKey.append(userId);
            }
            String ttid = mtopProperty.ttid;
            if (StringUtils.isNotBlank(ttid)) {
                cacheKey.append(ttid);
            }
            return cacheKey.toString();
        } catch (Exception e) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "[getCacheKey] getCacheKey error.", e);
            return null;
        }
    }

    public String getBlockName(String blockKey) {
        ApiCacheDo apiCacheDo;
        if (StringUtils.isBlank(blockKey) || (apiCacheDo = AppConfigManager.getInstance().getApiCacheDoByKey(blockKey)) == null || apiCacheDo.blockName == null) {
            return "";
        }
        return apiCacheDo.blockName;
    }

    @Deprecated
    public String getBlockName(String apiName, String apiVersion) {
        return getBlockName(StringUtils.concatStr2LowerCase(apiName, apiVersion));
    }

    private RpcCache handleResponseCacheFlag(String seqNo, RpcCache rpcCache) {
        if (!(rpcCache == null || rpcCache.header == null)) {
            Map<String, List<String>> header = rpcCache.header;
            String lastModifiedStr = HeaderHandlerUtil.getSingleHeaderFieldByKey(header, "last-modified");
            String cacheControlStr = HeaderHandlerUtil.getSingleHeaderFieldByKey(header, "cache-control");
            String eTagStr = HeaderHandlerUtil.getSingleHeaderFieldByKey(header, HttpHeaderConstant.MTOP_X_ETAG);
            if (eTagStr == null) {
                eTagStr = HeaderHandlerUtil.getSingleHeaderFieldByKey(header, "etag");
            }
            if (!(cacheControlStr == null && lastModifiedStr == null && eTagStr == null)) {
                if (StringUtils.isNotBlank(cacheControlStr) && StringUtils.isNotBlank(lastModifiedStr)) {
                    rpcCache.lastModified = lastModifiedStr;
                    rpcCache.cacheCreateTime = MtopUtils.convertTimeFormatGMT2Long(lastModifiedStr);
                    String[] items = cacheControlStr.split(",");
                    if (items != null) {
                        for (String item : items) {
                            try {
                                if (item.contains("max-age=")) {
                                    rpcCache.maxAge = Long.parseLong(item.substring("max-age=".length()));
                                } else if (HttpHeaderConstant.OFFLINE_FLAG_ON.equalsIgnoreCase(item)) {
                                    rpcCache.offline = true;
                                }
                            } catch (Exception e) {
                                TBSdkLog.w(TAG, seqNo, "[handleResponseCacheFlag] parse cacheControlStr error." + cacheControlStr);
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(eTagStr)) {
                    rpcCache.etag = eTagStr;
                }
            }
        }
        return rpcCache;
    }

    private RpcCache handleCacheValidation(String seqNo, RpcCache rpcCache) {
        if (rpcCache != null) {
            if (rpcCache.body == null) {
                rpcCache.cacheStatus = RpcCache.CacheStatus.TIMEOUT;
            } else if (rpcCache.lastModified == null && rpcCache.etag == null) {
                if (rpcCache.offline) {
                    rpcCache.cacheStatus = RpcCache.CacheStatus.NEED_UPDATE;
                } else {
                    rpcCache.cacheStatus = RpcCache.CacheStatus.TIMEOUT;
                }
            } else if (StringUtils.isNotBlank(rpcCache.lastModified)) {
                long lastmodified = rpcCache.cacheCreateTime;
                long maxAge = rpcCache.maxAge;
                long currentTime = SDKUtils.getCorrectionTime();
                if (currentTime >= lastmodified && currentTime <= lastmodified + maxAge) {
                    rpcCache.cacheStatus = RpcCache.CacheStatus.FRESH;
                } else if (rpcCache.offline) {
                    rpcCache.cacheStatus = RpcCache.CacheStatus.NEED_UPDATE;
                } else {
                    rpcCache.cacheStatus = RpcCache.CacheStatus.TIMEOUT;
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    StringBuilder str = new StringBuilder(128);
                    str.append("[handleCacheValidation]cacheStatus=").append(rpcCache.cacheStatus);
                    str.append(";lastModifiedStr=").append(rpcCache.lastModified);
                    str.append(";lastModified=").append(lastmodified);
                    str.append(";maxAge=").append(maxAge);
                    str.append(";currentTime=").append(currentTime);
                    str.append(";t_offset=").append(XState.getTimeOffset());
                    TBSdkLog.i(TAG, seqNo, str.toString());
                }
            } else if (StringUtils.isNotBlank(rpcCache.etag)) {
                rpcCache.cacheStatus = RpcCache.CacheStatus.NEED_UPDATE;
            }
        }
        return rpcCache;
    }
}
