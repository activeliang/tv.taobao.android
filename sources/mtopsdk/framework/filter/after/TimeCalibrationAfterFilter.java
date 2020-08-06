package mtopsdk.framework.filter.after;

import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.filter.before.ProtocolParamBuilderBeforeFilter;
import mtopsdk.framework.manager.FilterManager;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.protocol.builder.ProtocolParamBuilder;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;

public class TimeCalibrationAfterFilter implements IAfterFilter {
    private static final String TAG = "mtopsdk.TimeCalibrationAfterFilter";

    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        MtopResponse mtopResponse = mtopContext.mtopResponse;
        MtopNetworkProp property = mtopContext.property;
        if (mtopResponse.isExpiredRequest() && !property.timeCalibrated) {
            property.timeCalibrated = true;
            property.skipCacheCallback = true;
            try {
                String time = HeaderHandlerUtil.getSingleHeaderFieldByKey(mtopResponse.getHeaderFields(), HttpHeaderConstant.X_SYSTIME);
                if (StringUtils.isNotBlank(time)) {
                    XState.setValue(XStateConstants.KEY_TIME_OFFSET, String.valueOf(Long.parseLong(time) - (System.currentTimeMillis() / 1000)));
                    FilterManager filterManager = mtopContext.mtopInstance.getMtopConfig().filterManager;
                    if (filterManager != null) {
                        filterManager.start(new ProtocolParamBuilderBeforeFilter((ProtocolParamBuilder) null).getName(), mtopContext);
                        return FilterResult.STOP;
                    }
                }
            } catch (Exception e) {
                TBSdkLog.e(TAG, mtopContext.seqNo, "parse x-systime from mtop response header error", e);
            }
        }
        return FilterResult.CONTINUE;
    }
}
