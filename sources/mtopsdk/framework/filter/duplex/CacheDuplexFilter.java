package mtopsdk.framework.filter.duplex;

import android.content.Context;
import anetwork.network.cache.Cache;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.config.AppConfigManager;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.mtop.cache.CacheManager;
import mtopsdk.mtop.cache.domain.ApiCacheDo;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ResponseSource;
import mtopsdk.mtop.global.SwitchConfig;

public class CacheDuplexFilter implements IBeforeFilter, IAfterFilter {
    private static final String TAG = "mtopsdk.CacheDuplexFilter";
    private static final Map<Cache, CacheManager> cacheManagerMap = new ConcurrentHashMap(2);

    public String getName() {
        return TAG;
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:53:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String doBefore(mtopsdk.framework.domain.MtopContext r16) {
        /*
            r15 = this;
            java.lang.String r10 = "CONTINUE"
            mtopsdk.mtop.global.SwitchConfig r11 = mtopsdk.mtop.global.SwitchConfig.getInstance()
            java.util.Set<java.lang.String> r11 = r11.degradeApiCacheSet
            if (r11 == 0) goto L_0x0046
            r0 = r16
            mtopsdk.mtop.domain.MtopRequest r11 = r0.mtopRequest
            java.lang.String r1 = r11.getKey()
            mtopsdk.mtop.global.SwitchConfig r11 = mtopsdk.mtop.global.SwitchConfig.getInstance()
            java.util.Set<java.lang.String> r11 = r11.degradeApiCacheSet
            boolean r11 = r11.contains(r1)
            if (r11 == 0) goto L_0x0046
            mtopsdk.common.util.TBSdkLog$LogEnable r11 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r11 = mtopsdk.common.util.TBSdkLog.isLogEnable(r11)
            if (r11 == 0) goto L_0x0045
            java.lang.String r11 = "mtopsdk.CacheDuplexFilter"
            r0 = r16
            java.lang.String r12 = r0.seqNo
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "apiKey in degradeApiCacheList,apiKey="
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.StringBuilder r13 = r13.append(r1)
            java.lang.String r13 = r13.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r11, (java.lang.String) r12, (java.lang.String) r13)
        L_0x0045:
            return r10
        L_0x0046:
            r0 = r16
            mtopsdk.mtop.util.MtopStatistics r11 = r0.stats
            r12 = 1
            r11.cacheSwitch = r12
            r0 = r16
            mtopsdk.mtop.intf.Mtop r11 = r0.mtopInstance
            mtopsdk.mtop.global.MtopConfig r11 = r11.getMtopConfig()
            anetwork.network.cache.Cache r3 = r11.cacheImpl
            if (r3 != 0) goto L_0x0088
            mtopsdk.common.util.TBSdkLog$LogEnable r11 = mtopsdk.common.util.TBSdkLog.LogEnable.DebugEnable
            boolean r11 = mtopsdk.common.util.TBSdkLog.isLogEnable(r11)
            if (r11 == 0) goto L_0x0045
            java.lang.String r11 = "mtopsdk.CacheDuplexFilter"
            r0 = r16
            java.lang.String r12 = r0.seqNo
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = " CacheImpl is null. instanceId="
            java.lang.StringBuilder r13 = r13.append(r14)
            r0 = r16
            mtopsdk.mtop.intf.Mtop r14 = r0.mtopInstance
            java.lang.String r14 = r14.getInstanceId()
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r13 = r13.toString()
            mtopsdk.common.util.TBSdkLog.d((java.lang.String) r11, (java.lang.String) r12, (java.lang.String) r13)
            goto L_0x0045
        L_0x0088:
            java.util.Map<anetwork.network.cache.Cache, mtopsdk.mtop.cache.CacheManager> r11 = cacheManagerMap
            java.lang.Object r5 = r11.get(r3)
            mtopsdk.mtop.cache.CacheManager r5 = (mtopsdk.mtop.cache.CacheManager) r5
            if (r5 != 0) goto L_0x00ad
            java.util.Map<anetwork.network.cache.Cache, mtopsdk.mtop.cache.CacheManager> r12 = cacheManagerMap
            monitor-enter(r12)
            java.util.Map<anetwork.network.cache.Cache, mtopsdk.mtop.cache.CacheManager> r11 = cacheManagerMap     // Catch:{ all -> 0x00f7 }
            java.lang.Object r11 = r11.get(r3)     // Catch:{ all -> 0x00f7 }
            r0 = r11
            mtopsdk.mtop.cache.CacheManager r0 = (mtopsdk.mtop.cache.CacheManager) r0     // Catch:{ all -> 0x00f7 }
            r5 = r0
            if (r5 != 0) goto L_0x00ac
            mtopsdk.mtop.cache.CacheManagerImpl r6 = new mtopsdk.mtop.cache.CacheManagerImpl     // Catch:{ all -> 0x00f7 }
            r6.<init>(r3)     // Catch:{ all -> 0x00f7 }
            java.util.Map<anetwork.network.cache.Cache, mtopsdk.mtop.cache.CacheManager> r11 = cacheManagerMap     // Catch:{ all -> 0x0125 }
            r11.put(r3, r6)     // Catch:{ all -> 0x0125 }
            r5 = r6
        L_0x00ac:
            monitor-exit(r12)     // Catch:{ all -> 0x00f7 }
        L_0x00ad:
            r8 = 0
            r0 = r16
            mtopsdk.network.domain.Request r11 = r0.networkRequest     // Catch:{ Exception -> 0x00fa }
            r0 = r16
            mtopsdk.mtop.common.MtopListener r12 = r0.mtopListener     // Catch:{ Exception -> 0x00fa }
            boolean r11 = r5.isNeedReadCache(r11, r12)     // Catch:{ Exception -> 0x00fa }
            if (r11 == 0) goto L_0x00e3
            mtopsdk.mtop.domain.ResponseSource r9 = new mtopsdk.mtop.domain.ResponseSource     // Catch:{ Exception -> 0x00fa }
            r0 = r16
            r9.<init>(r0, r5)     // Catch:{ Exception -> 0x00fa }
            r0 = r16
            r0.responseSource = r9     // Catch:{ Exception -> 0x0122 }
            java.lang.String r4 = r9.getCacheKey()     // Catch:{ Exception -> 0x0122 }
            java.lang.String r2 = r9.getCacheBlock()     // Catch:{ Exception -> 0x0122 }
            r0 = r16
            java.lang.String r11 = r0.seqNo     // Catch:{ Exception -> 0x0122 }
            anetwork.network.cache.RpcCache r11 = r5.getCache(r4, r2, r11)     // Catch:{ Exception -> 0x0122 }
            r9.rpcCache = r11     // Catch:{ Exception -> 0x0122 }
            r0 = r16
            mtopsdk.mtop.common.MtopNetworkProp r11 = r0.property     // Catch:{ Exception -> 0x0122 }
            android.os.Handler r11 = r11.handler     // Catch:{ Exception -> 0x0122 }
            mtopsdk.mtop.cache.handler.CacheStatusHandler.handleCacheStatus(r9, r11)     // Catch:{ Exception -> 0x0122 }
            r8 = r9
        L_0x00e3:
            if (r8 == 0) goto L_0x0045
            boolean r11 = r8.requireConnection
            if (r11 != 0) goto L_0x0045
            mtopsdk.mtop.domain.MtopResponse r11 = r8.cacheResponse
            r0 = r16
            r0.mtopResponse = r11
            mtopsdk.framework.util.FilterUtils.handleExceptionCallBack(r16)
            java.lang.String r10 = "STOP"
            goto L_0x0045
        L_0x00f7:
            r11 = move-exception
        L_0x00f8:
            monitor-exit(r12)     // Catch:{ all -> 0x00f7 }
            throw r11
        L_0x00fa:
            r7 = move-exception
        L_0x00fb:
            java.lang.String r11 = "mtopsdk.CacheDuplexFilter"
            r0 = r16
            java.lang.String r12 = r0.seqNo
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "[initResponseSource] initResponseSource error,apiKey="
            java.lang.StringBuilder r13 = r13.append(r14)
            r0 = r16
            mtopsdk.mtop.domain.MtopRequest r14 = r0.mtopRequest
            java.lang.String r14 = r14.getKey()
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r13 = r13.toString()
            mtopsdk.common.util.TBSdkLog.e(r11, r12, r13, r7)
            goto L_0x00e3
        L_0x0122:
            r7 = move-exception
            r8 = r9
            goto L_0x00fb
        L_0x0125:
            r11 = move-exception
            r5 = r6
            goto L_0x00f8
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.framework.filter.duplex.CacheDuplexFilter.doBefore(mtopsdk.framework.domain.MtopContext):java.lang.String");
    }

    public String doAfter(MtopContext mtopContext) {
        if (SwitchConfig.getInstance().degradeApiCacheSet != null) {
            String apiKey = mtopContext.mtopRequest.getKey();
            if (SwitchConfig.getInstance().degradeApiCacheSet.contains(apiKey)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, mtopContext.seqNo, "apiKey in degradeApiCacheList,apiKey=" + apiKey);
                }
                return FilterResult.CONTINUE;
            }
        }
        MtopResponse mtopResponse = mtopContext.mtopResponse;
        ResponseSource responseSource = mtopContext.responseSource;
        if (mtopResponse.isApiSuccess() && responseSource != null) {
            Map<String, List<String>> responseHeaders = mtopResponse.getHeaderFields();
            CacheManager cacheManager = responseSource.cacheManager;
            if (cacheManager.isNeedWriteCache(mtopContext.networkRequest, responseHeaders)) {
                cacheManager.putCache(responseSource.getCacheKey(), responseSource.getCacheBlock(), mtopResponse);
                updateApiCacheConf(mtopContext, mtopResponse, responseSource.getCacheBlock(), responseHeaders);
            }
        }
        return FilterResult.CONTINUE;
    }

    private void updateApiCacheConf(MtopContext mtopContext, MtopResponse mtopResponse, String blockName, Map<String, List<String>> responseHeaders) {
        String cacheControlHeader = HeaderHandlerUtil.getSingleHeaderFieldByKey(responseHeaders, "cache-control");
        if (!StringUtils.isBlank(cacheControlHeader)) {
            AppConfigManager appConfigManager = AppConfigManager.getInstance();
            String apiName = mtopResponse.getApi();
            String apiVersion = mtopResponse.getV();
            String key = StringUtils.concatStr2LowerCase(apiName, apiVersion);
            ApiCacheDo apiCacheDo = appConfigManager.getApiCacheDoByKey(key);
            Context context = mtopContext.mtopInstance.getMtopConfig().context;
            if (apiCacheDo == null) {
                ApiCacheDo apiCacheDo2 = new ApiCacheDo(apiName, apiVersion, blockName);
                appConfigManager.parseCacheControlHeader(cacheControlHeader, apiCacheDo2);
                appConfigManager.addApiCacheDoToGroup(key, apiCacheDo2);
                appConfigManager.storeApiCacheDoMap(context, mtopContext.seqNo);
            } else if (!cacheControlHeader.equals(apiCacheDo.cacheControlHeader)) {
                appConfigManager.parseCacheControlHeader(cacheControlHeader, apiCacheDo);
                appConfigManager.storeApiCacheDoMap(context, mtopContext.seqNo);
            }
        }
    }
}
