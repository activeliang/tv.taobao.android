package mtopsdk.framework.filter.before;

import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.network.NetworkCallbackAdapter;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.network.Call;

public class ExecuteCallBeforeFilter implements IBeforeFilter {
    private static final String TAG = "mtopsdk.ExecuteCallBeforeFilter";

    public String getName() {
        return TAG;
    }

    public String doBefore(MtopContext mtopContext) {
        try {
            mtopContext.stats.netSendStartTime = mtopContext.stats.currentTimeMillis();
            Call.Factory callFactory = mtopContext.mtopInstance.getMtopConfig().callFactory;
            if (callFactory == null) {
                TBSdkLog.e(TAG, mtopContext.seqNo, "call Factory of mtopInstance is null.instanceId=" + mtopContext.mtopInstance.getInstanceId());
                MtopResponse mtopResponse = new MtopResponse(ErrorConstant.ERRCODE_MTOP_MISS_CALL_FACTORY, ErrorConstant.ERRMSG_MTOP_MISS_CALL_FACTORY);
                mtopResponse.setApi(mtopContext.mtopRequest.getApiName());
                mtopResponse.setV(mtopContext.mtopRequest.getVersion());
                mtopContext.mtopResponse = mtopResponse;
                FilterUtils.handleExceptionCallBack(mtopContext);
                return FilterResult.STOP;
            }
            Call call = callFactory.newCall(mtopContext.networkRequest);
            call.enqueue(new NetworkCallbackAdapter(mtopContext));
            if (mtopContext.apiId != null) {
                mtopContext.apiId.setCall(call);
            }
            return FilterResult.CONTINUE;
        } catch (Exception e) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "invoke call.enqueue of mtopInstance error,apiKey=" + mtopContext.mtopRequest.getKey(), e);
            return FilterResult.STOP;
        }
    }
}
