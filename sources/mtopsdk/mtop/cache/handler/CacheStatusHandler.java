package mtopsdk.mtop.cache.handler;

import android.os.Handler;
import anetwork.network.cache.RpcCache;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ResponseSource;

public class CacheStatusHandler {
    private static final String TAG = "mtopsdk.CacheStatusHandler";

    public static void handleCacheStatus(ResponseSource responseSource, Handler handler) {
        if (responseSource != null) {
            if (responseSource.rpcCache != null) {
                CacheParserFactory.createCacheParser(responseSource.rpcCache.cacheStatus).parse(responseSource, handler);
            } else {
                TBSdkLog.i(TAG, responseSource.seqNo, "[handleCacheStatus]Didn't  hit local cache ");
            }
        }
    }

    protected static MtopResponse initResponseFromCache(RpcCache rpcCache, MtopRequest mtopRequest) {
        MtopResponse mtopResponse = new MtopResponse();
        mtopResponse.setApi(mtopRequest.getApiName());
        mtopResponse.setV(mtopRequest.getVersion());
        mtopResponse.setBytedata(rpcCache.body);
        mtopResponse.setHeaderFields(rpcCache.header);
        mtopResponse.setResponseCode(200);
        FilterUtils.parseRetCodeFromHeader(mtopResponse);
        return mtopResponse;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: mtopsdk.mtop.util.MtopStatistics} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static void finishMtopStatisticsOnExpiredCache(mtopsdk.mtop.util.MtopStatistics r6, mtopsdk.mtop.domain.MtopResponse r7) {
        /*
            if (r6 == 0) goto L_0x0004
            if (r7 != 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            r2 = 0
            java.lang.Object r3 = r6.clone()     // Catch:{ Exception -> 0x0030 }
            r0 = r3
            mtopsdk.mtop.util.MtopStatistics r0 = (mtopsdk.mtop.util.MtopStatistics) r0     // Catch:{ Exception -> 0x0030 }
            r2 = r0
        L_0x000e:
            if (r2 == 0) goto L_0x0004
            r7.setMtopStat(r2)
            java.util.Map r3 = r7.getHeaderFields()
            java.lang.String r4 = "x-s-traceid"
            java.lang.String r3 = mtopsdk.common.util.HeaderHandlerUtil.getSingleHeaderFieldByKey(r3, r4)
            r2.serverTraceId = r3
            int r3 = r7.getResponseCode()
            r2.statusCode = r3
            java.lang.String r3 = r7.getRetCode()
            r2.retCode = r3
            r2.onEndAndCommit()
            goto L_0x0004
        L_0x0030:
            r1 = move-exception
            mtopsdk.common.util.TBSdkLog$LogEnable r3 = mtopsdk.common.util.TBSdkLog.LogEnable.ErrorEnable
            boolean r3 = mtopsdk.common.util.TBSdkLog.isLogEnable(r3)
            if (r3 == 0) goto L_0x000e
            java.lang.String r3 = "mtopsdk.CacheStatusHandler"
            java.lang.String r4 = r6.seqNo
            java.lang.String r5 = "[finishMtopStatisticsOnCache] clone MtopStatistics error."
            mtopsdk.common.util.TBSdkLog.e(r3, r4, r5, r1)
            goto L_0x000e
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.mtop.cache.handler.CacheStatusHandler.finishMtopStatisticsOnExpiredCache(mtopsdk.mtop.util.MtopStatistics, mtopsdk.mtop.domain.MtopResponse):void");
    }
}
