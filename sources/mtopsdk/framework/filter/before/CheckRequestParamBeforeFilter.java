package mtopsdk.framework.filter.before;

import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ProtocolEnum;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.util.ErrorConstant;

public class CheckRequestParamBeforeFilter implements IBeforeFilter {
    private static final String TAG = "mtopsdk.CheckRequestParamBeforeFilter";

    public String getName() {
        return TAG;
    }

    public String doBefore(MtopContext mtopContext) {
        if (checkRequiredParam(mtopContext)) {
            return FilterResult.CONTINUE;
        }
        return FilterResult.STOP;
    }

    private boolean checkRequiredParam(MtopContext mtopContext) {
        MtopRequest mtopRequest = mtopContext.mtopRequest;
        MtopNetworkProp property = mtopContext.property;
        String seqNo = mtopContext.seqNo;
        String errorMsg = null;
        MtopResponse mtopResponse = null;
        if (mtopRequest == null) {
            errorMsg = "mtopRequest is invalid.mtopRequest=null";
            mtopResponse = new MtopResponse(ErrorConstant.ERRCODE_MTOPCONTEXT_INIT_ERROR, errorMsg);
        } else if (!mtopRequest.isLegalRequest()) {
            errorMsg = "mtopRequest is invalid. " + mtopRequest.toString();
            mtopResponse = new MtopResponse(mtopRequest.getApiName(), mtopRequest.getVersion(), ErrorConstant.ERRCODE_MTOPCONTEXT_INIT_ERROR, errorMsg);
        } else if (property == null) {
            errorMsg = "MtopNetworkProp is invalid.property=null";
            mtopResponse = new MtopResponse(mtopRequest.getApiName(), mtopRequest.getVersion(), ErrorConstant.ERRCODE_MTOPCONTEXT_INIT_ERROR, errorMsg);
        }
        mtopContext.mtopResponse = mtopResponse;
        if (StringUtils.isNotBlank(errorMsg) && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
            TBSdkLog.e(TAG, seqNo, "[checkRequiredParam]" + errorMsg);
        }
        if (mtopRequest != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, seqNo, "[checkRequiredParam]" + mtopRequest.toString());
        }
        FilterUtils.handleExceptionCallBack(mtopContext);
        if (!SwitchConfig.getInstance().isGlobalSpdySslSwitchOpen()) {
            TBSdkLog.w(TAG, seqNo, "[checkRequiredParam]MTOP SSL switch is false");
            mtopContext.property.protocol = ProtocolEnum.HTTP;
        }
        if (mtopResponse == null) {
            return true;
        }
        return false;
    }
}
