package mtopsdk.framework.util;

import android.os.Handler;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.after.ErrorCodeMappingAfterFilter;
import mtopsdk.framework.manager.FilterManager;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;

public final class FilterUtils {
    public static final ErrorCodeMappingAfterFilter errorCodeMappingAfterFilter = new ErrorCodeMappingAfterFilter();

    public static void handleExceptionCallBack(final MtopContext mtopContext) {
        final MtopResponse mtopResponse = mtopContext.mtopResponse;
        if (mtopResponse != null && (mtopContext.mtopListener instanceof MtopCallback.MtopFinishListener)) {
            mtopResponse.setMtopStat(mtopContext.stats);
            final MtopFinishEvent mtopEvent = new MtopFinishEvent(mtopResponse);
            mtopEvent.seqNo = mtopContext.seqNo;
            errorCodeMappingAfterFilter.doAfter(mtopContext);
            submitCallbackTask(mtopContext.property.handler, new Runnable() {
                public void run() {
                    try {
                        mtopContext.stats.serverTraceId = HeaderHandlerUtil.getSingleHeaderFieldByKey(mtopResponse.getHeaderFields(), HttpHeaderConstant.SERVER_TRACE_ID);
                        mtopContext.stats.statusCode = mtopResponse.getResponseCode();
                        mtopContext.stats.retCode = mtopResponse.getRetCode();
                        mtopContext.stats.mappingCode = mtopResponse.getMappingCode();
                        if (mtopResponse.isApiSuccess() && 3 == mtopContext.stats.cacheHitType) {
                            mtopContext.stats.statusCode = 304;
                        }
                        mtopContext.stats.onEndAndCommit();
                        ((MtopCallback.MtopFinishListener) mtopContext.mtopListener).onFinished(mtopEvent, mtopContext.property.reqContext);
                    } catch (Exception e) {
                    }
                }
            }, mtopContext.seqNo.hashCode());
        }
    }

    public static void submitCallbackTask(Handler handler, Runnable task, int TaskId) {
        if (handler != null) {
            handler.post(task);
        } else {
            MtopSDKThreadPoolExecutorFactory.submitCallbackTask(TaskId, task);
        }
    }

    public static void parseRetCodeFromHeader(MtopResponse response) {
        if (response != null) {
            String retCode = HeaderHandlerUtil.getSingleHeaderFieldByKey(response.getHeaderFields(), HttpHeaderConstant.X_RETCODE);
            response.mappingCodeSuffix = HeaderHandlerUtil.getSingleHeaderFieldByKey(response.getHeaderFields(), HttpHeaderConstant.X_MAPPING_CODE);
            if (StringUtils.isNotBlank(retCode)) {
                response.setRetCode(retCode);
            } else {
                response.parseJsonByte();
            }
        }
    }

    public static void checkFilterManager(FilterManager filterManager, MtopContext mtopContext) {
        if (filterManager == null) {
            MtopResponse mtopResponse = new MtopResponse(ErrorConstant.ERRCODE_MTOPSDK_INIT_ERROR, ErrorConstant.ERRMSG_MTOPSDK_INIT_ERROR);
            if (mtopContext.mtopRequest != null) {
                mtopResponse.setApi(mtopContext.mtopRequest.getApiName());
                mtopResponse.setV(mtopContext.mtopRequest.getVersion());
            }
            mtopContext.mtopResponse = mtopResponse;
            handleExceptionCallBack(mtopContext);
        }
    }
}
