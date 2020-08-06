package mtopsdk.framework.filter.after;

import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.MtopStatistics;

public class ExecuteCallbackAfterFilter implements IAfterFilter {
    private static final String TAG = "mtopsdk.ExecuteCallbackAfterFilter";

    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        MtopStatistics stats = mtopContext.stats;
        MtopResponse mtopResponse = mtopContext.mtopResponse;
        String seqNo = mtopContext.seqNo;
        MtopFinishEvent mtopEvent = new MtopFinishEvent(mtopResponse);
        mtopEvent.seqNo = seqNo;
        stats.serverTraceId = HeaderHandlerUtil.getSingleHeaderFieldByKey(mtopResponse.getHeaderFields(), HttpHeaderConstant.SERVER_TRACE_ID);
        stats.retCode = mtopResponse.getRetCode();
        stats.statusCode = mtopResponse.getResponseCode();
        stats.mappingCode = mtopResponse.getMappingCode();
        stats.onEndAndCommit();
        MtopListener mtopListener = mtopContext.mtopListener;
        try {
            if (!(mtopListener instanceof MtopCallback.MtopFinishListener)) {
                return FilterResult.CONTINUE;
            }
            ((MtopCallback.MtopFinishListener) mtopListener).onFinished(mtopEvent, mtopContext.property.reqContext);
            return FilterResult.CONTINUE;
        } catch (Throwable e) {
            TBSdkLog.e(TAG, seqNo, "call MtopFinishListener error,apiKey=" + mtopContext.mtopRequest.getKey(), e);
            return FilterResult.CONTINUE;
        }
    }
}
