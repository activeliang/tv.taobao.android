package mtopsdk.framework.filter.duplex;

import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.antiattack.ApiLockHelper;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.global.SDKUtils;
import mtopsdk.mtop.util.ErrorConstant;

public class FlowLimitDuplexFilter implements IBeforeFilter, IAfterFilter {
    private static final String TAG = "mtopsdk.FlowLimitDuplexFilter";

    public String getName() {
        return TAG;
    }

    public String doBefore(MtopContext mtopContext) {
        if (mtopContext.property != null && mtopContext.property.priorityFlag) {
            return FilterResult.CONTINUE;
        }
        MtopRequest mtopRequest = mtopContext.mtopRequest;
        String apiFullName = mtopRequest.getKey();
        if (MtopUtils.apiWhiteList.contains(apiFullName) || !ApiLockHelper.iSApiLocked(apiFullName, SDKUtils.getCorrectionTime())) {
            return FilterResult.CONTINUE;
        }
        mtopContext.mtopResponse = new MtopResponse(mtopRequest.getApiName(), mtopRequest.getVersion(), ErrorConstant.ERRCODE_API_FLOW_LIMIT_LOCKED, ErrorConstant.ERRMSG_API_FLOW_LIMIT_LOCKED);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
            TBSdkLog.w(TAG, mtopContext.seqNo, "[doBefore] execute FlowLimitDuplexFilter apiKey=" + apiFullName);
        }
        FilterUtils.handleExceptionCallBack(mtopContext);
        return FilterResult.STOP;
    }

    public String doAfter(MtopContext mtopContext) {
        MtopResponse mtopResponse = mtopContext.mtopResponse;
        if (420 != mtopResponse.getResponseCode()) {
            return FilterResult.CONTINUE;
        }
        String apiFullName = mtopContext.mtopRequest.getKey();
        ApiLockHelper.lock(apiFullName, SDKUtils.getCorrectionTime());
        FilterUtils.parseRetCodeFromHeader(mtopResponse);
        if (StringUtils.isBlank(mtopResponse.getRetCode())) {
            mtopContext.mtopResponse.setRetCode(ErrorConstant.ERRCODE_API_FLOW_LIMIT_LOCKED);
            mtopContext.mtopResponse.setRetMsg(ErrorConstant.ERRMSG_API_FLOW_LIMIT_LOCKED);
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
            TBSdkLog.w(TAG, mtopContext.seqNo, "[doAfter] execute FlowLimitDuplexFilter apiKey=" + apiFullName + " ,retCode=" + mtopResponse.getRetCode());
        }
        FilterUtils.handleExceptionCallBack(mtopContext);
        return FilterResult.STOP;
    }
}
