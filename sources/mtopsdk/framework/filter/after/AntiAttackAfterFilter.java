package mtopsdk.framework.filter.after;

import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.antiattack.AntiAttackHandler;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.ErrorConstant;

public class AntiAttackAfterFilter implements IAfterFilter {
    private static final String TAG = "mtopsdk.AntiAttackAfterFilter";

    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        MtopResponse mtopResponse = mtopContext.mtopResponse;
        if (419 != mtopResponse.getResponseCode()) {
            return FilterResult.CONTINUE;
        }
        Map<String, List<String>> headers = mtopResponse.getHeaderFields();
        String location = HeaderHandlerUtil.getSingleHeaderFieldByKey(headers, "location");
        String location_ext = HeaderHandlerUtil.getSingleHeaderFieldByKey(headers, HttpHeaderConstant.X_LOCATION_EXT);
        AntiAttackHandler antiAttackHandler = mtopContext.mtopInstance.getMtopConfig().antiAttackHandler;
        if (antiAttackHandler != null) {
            antiAttackHandler.handle(location, location_ext);
        } else {
            TBSdkLog.e(TAG, mtopContext.seqNo, "didn't register AntiAttackHandler.");
        }
        mtopResponse.setRetCode(ErrorConstant.ERRCODE_API_41X_ANTI_ATTACK);
        mtopResponse.setRetMsg(ErrorConstant.ERRMSG_API_41X_ANTI_ATTACK);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
            TBSdkLog.w(TAG, mtopContext.seqNo, "[doAfter] execute AntiAttackAfterFilter apiKey=" + mtopContext.mtopRequest.getKey());
        }
        FilterUtils.handleExceptionCallBack(mtopContext);
        return FilterResult.STOP;
    }
}
