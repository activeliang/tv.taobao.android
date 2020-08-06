package mtopsdk.mtop.cache.handler;

import android.os.Handler;
import anetwork.network.cache.RpcCache;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ResponseSource;
import mtopsdk.mtop.util.MtopStatistics;
import mtopsdk.network.domain.Request;

public class ExpiredCacheParser implements ICacheParser {
    private static final String TAG = "mtopsdk.ExpiredCacheParser";

    public void parse(ResponseSource responseSource, Handler handler) {
        final String seqNo = responseSource.seqNo;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, seqNo, "[parse]ExpiredCacheParser parse called");
        }
        MtopContext mtopContext = responseSource.mtopContext;
        MtopStatistics stats = mtopContext.stats;
        stats.cacheHitType = 2;
        stats.cacheResponseParseStartTime = stats.currentTimeMillis();
        RpcCache rpcCache = responseSource.rpcCache;
        MtopResponse mtopResponse = CacheStatusHandler.initResponseFromCache(rpcCache, mtopContext.mtopRequest);
        mtopResponse.setSource(MtopResponse.ResponseSource.EXPIRED_CACHE);
        stats.cacheResponseParseEndTime = stats.currentTimeMillis();
        mtopResponse.setMtopStat(stats);
        final MtopListener mtopListener = mtopContext.mtopListener;
        final Object reqContext = mtopContext.property.reqContext;
        if (mtopListener instanceof MtopCallback.MtopCacheListener) {
            final MtopCacheEvent cacheEvent = new MtopCacheEvent(mtopResponse);
            cacheEvent.seqNo = seqNo;
            stats.cacheReturnTime = stats.currentTimeMillis();
            CacheStatusHandler.finishMtopStatisticsOnExpiredCache(stats, mtopResponse);
            if (!mtopContext.property.skipCacheCallback) {
                FilterUtils.submitCallbackTask(handler, new Runnable() {
                    public void run() {
                        try {
                            ((MtopCallback.MtopCacheListener) mtopListener).onCached(cacheEvent, reqContext);
                        } catch (Exception e) {
                            TBSdkLog.e(ExpiredCacheParser.TAG, seqNo, "do onCached callback error.", e);
                        }
                    }
                }, mtopContext.seqNo.hashCode());
            }
        }
        stats.cacheHitType = 3;
        Request request = mtopContext.networkRequest;
        if (request != null) {
            if (StringUtils.isNotBlank(rpcCache.lastModified)) {
                request.setHeader(HttpHeaderConstant.IF_MODIFIED_SINCE, rpcCache.lastModified);
            }
            if (StringUtils.isNotBlank(rpcCache.etag)) {
                request.setHeader(HttpHeaderConstant.IF_NONE_MATCH, rpcCache.etag);
            }
        }
        responseSource.cacheResponse = mtopResponse;
    }
}
