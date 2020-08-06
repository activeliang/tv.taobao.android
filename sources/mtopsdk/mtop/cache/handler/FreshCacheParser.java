package mtopsdk.mtop.cache.handler;

import android.os.Handler;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ResponseSource;
import mtopsdk.mtop.util.MtopStatistics;

public class FreshCacheParser implements ICacheParser {
    private static final String TAG = "mtopsdk.FreshCacheParser";

    public void parse(ResponseSource responseSource, Handler handler) {
        final String seqNo = responseSource.seqNo;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, seqNo, "[parse]FreshCacheParser parse called");
        }
        responseSource.requireConnection = false;
        MtopContext mtopContext = responseSource.mtopContext;
        MtopRequest mtopRequest = mtopContext.mtopRequest;
        MtopStatistics stats = mtopContext.stats;
        stats.cacheHitType = 1;
        stats.cacheResponseParseStartTime = stats.currentTimeMillis();
        MtopResponse mtopResponse = CacheStatusHandler.initResponseFromCache(responseSource.rpcCache, mtopRequest);
        mtopResponse.setSource(MtopResponse.ResponseSource.FRESH_CACHE);
        stats.cacheResponseParseEndTime = stats.currentTimeMillis();
        mtopResponse.setMtopStat(stats);
        responseSource.cacheResponse = mtopResponse;
        stats.cacheReturnTime = stats.currentTimeMillis();
        if (mtopContext.property.forceRefreshCache) {
            responseSource.requireConnection = true;
            final MtopListener mtopListener = mtopContext.mtopListener;
            if (mtopListener instanceof MtopCallback.MtopCacheListener) {
                final Object reqContext = mtopContext.property.reqContext;
                final MtopCacheEvent cacheEvent = new MtopCacheEvent(mtopResponse);
                cacheEvent.seqNo = seqNo;
                CacheStatusHandler.finishMtopStatisticsOnExpiredCache(stats, mtopResponse);
                if (!mtopContext.property.skipCacheCallback) {
                    FilterUtils.submitCallbackTask(handler, new Runnable() {
                        public void run() {
                            try {
                                ((MtopCallback.MtopCacheListener) mtopListener).onCached(cacheEvent, reqContext);
                            } catch (Exception e) {
                                TBSdkLog.e(FreshCacheParser.TAG, seqNo, "do onCached callback error.", e);
                            }
                        }
                    }, mtopContext.seqNo.hashCode());
                }
                stats.cacheHitType = 3;
            }
        }
    }
}
