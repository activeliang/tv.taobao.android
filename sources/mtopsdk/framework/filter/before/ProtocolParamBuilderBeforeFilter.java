package mtopsdk.framework.filter.before;

import java.util.Map;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.protocol.builder.ProtocolParamBuilder;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.xstate.util.XStateConstants;

public class ProtocolParamBuilderBeforeFilter implements IBeforeFilter {
    private static final String TAG = "mtopsdk.ProtocolParamBuilderBeforeFilter";
    private ProtocolParamBuilder paramBuilder;

    public ProtocolParamBuilderBeforeFilter(ProtocolParamBuilder paramBuilder2) {
        this.paramBuilder = paramBuilder2;
    }

    public String getName() {
        return TAG;
    }

    public String doBefore(MtopContext mtopContext) {
        MtopRequest mtopRequest = mtopContext.mtopRequest;
        MtopResponse mtopResponse = null;
        Map<String, String> protocolParams = null;
        try {
            protocolParams = this.paramBuilder.buildParams(mtopContext);
            if (protocolParams == null) {
                mtopResponse = new MtopResponse(mtopRequest.getApiName(), mtopRequest.getVersion(), ErrorConstant.ERRCODE_INIT_MTOP_ISIGN_ERROR, ErrorConstant.ERRMSG_INIT_MTOP_ISIGN_ERROR);
            } else if (protocolParams.get("sign") == null) {
                String errorCode = protocolParams.get(XStateConstants.KEY_SG_ERROR_CODE);
                StringBuilder retCode = new StringBuilder(48);
                retCode.append(ErrorConstant.ERRCODE_GENERATE_MTOP_SIGN_ERROR);
                if (errorCode != null) {
                    retCode.append("(").append(errorCode).append(")");
                }
                mtopResponse = new MtopResponse(mtopRequest.getApiName(), mtopRequest.getVersion(), retCode.toString(), ErrorConstant.ERRMSG_GENERATE_MTOP_SIGN_ERROR);
            }
        } catch (Throwable e) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "[deBefore]execute ProtocolParamBuilder buildParams error.", e);
            mtopResponse = new MtopResponse(mtopRequest.getApiName(), mtopRequest.getVersion(), ErrorConstant.ERRCODE_BUILD_PROTOCOL_PARAMS_ERROR, ErrorConstant.ERRMSG_BUILD_PROTOCOL_PARAMS_ERROR);
        }
        if (mtopResponse != null) {
            mtopContext.mtopResponse = mtopResponse;
            FilterUtils.handleExceptionCallBack(mtopContext);
            return FilterResult.STOP;
        }
        mtopContext.protocolParams = protocolParams;
        return FilterResult.CONTINUE;
    }
}
