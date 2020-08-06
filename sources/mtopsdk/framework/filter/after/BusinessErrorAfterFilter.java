package mtopsdk.framework.filter.after;

import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.ErrorConstant;

public class BusinessErrorAfterFilter implements IAfterFilter {
    private static final String TAG = "mtopsdk.BusinessErrorAfterFilter";

    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        MtopResponse cacheResponse;
        MtopResponse mtopResponse = mtopContext.mtopResponse;
        if (304 == mtopResponse.getResponseCode() && mtopContext.responseSource != null && (cacheResponse = mtopContext.responseSource.cacheResponse) != null) {
            mtopContext.mtopResponse = cacheResponse;
            FilterUtils.handleExceptionCallBack(mtopContext);
            return FilterResult.STOP;
        } else if (mtopResponse.getBytedata() == null) {
            mtopResponse.setRetCode(ErrorConstant.ERRCODE_JSONDATA_BLANK);
            mtopResponse.setRetMsg(ErrorConstant.ERRMSG_JSONDATA_BLANK);
            FilterUtils.handleExceptionCallBack(mtopContext);
            return FilterResult.STOP;
        } else {
            FilterUtils.parseRetCodeFromHeader(mtopResponse);
            return FilterResult.CONTINUE;
        }
    }
}
