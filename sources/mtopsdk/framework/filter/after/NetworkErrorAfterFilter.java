package mtopsdk.framework.filter.after;

import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.ErrorConstant;

public class NetworkErrorAfterFilter implements IAfterFilter {
    private static final String TAG = "mtopsdk.NetworkErrorAfterFilter";

    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        MtopResponse mtopResponse = mtopContext.mtopResponse;
        if (mtopResponse.getResponseCode() >= 0) {
            return FilterResult.CONTINUE;
        }
        mtopResponse.setRetCode(ErrorConstant.ERRCODE_NETWORK_ERROR);
        mtopResponse.setRetMsg(ErrorConstant.ERRMSG_NETWORK_ERROR);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
            StringBuilder builder = new StringBuilder(128);
            builder.append("api=").append(mtopResponse.getApi());
            builder.append(",v=").append(mtopResponse.getV());
            builder.append(",retCode =").append(mtopResponse.getRetCode());
            builder.append(",responseCode =").append(mtopResponse.getResponseCode());
            builder.append(",responseHeader=").append(mtopResponse.getHeaderFields());
            TBSdkLog.e(TAG, mtopContext.seqNo, builder.toString());
        }
        FilterUtils.handleExceptionCallBack(mtopContext);
        return FilterResult.STOP;
    }
}
