package mtopsdk.framework.filter.before;

import android.support.annotation.NonNull;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.protocol.converter.INetworkConverter;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.network.domain.Request;

public class NetworkConvertBeforeFilter implements IBeforeFilter {
    private static final String TAG = "mtopsdk.NetworkConvertBeforeFilter";
    private INetworkConverter networkConverter;

    public NetworkConvertBeforeFilter(@NonNull INetworkConverter networkConverter2) {
        this.networkConverter = networkConverter2;
    }

    public String getName() {
        return TAG;
    }

    public String doBefore(MtopContext mtopContext) {
        Request request = this.networkConverter.convert(mtopContext);
        mtopContext.networkRequest = request;
        if (request != null) {
            return FilterResult.CONTINUE;
        }
        mtopContext.mtopResponse = new MtopResponse(mtopContext.mtopRequest.getApiName(), mtopContext.mtopRequest.getVersion(), ErrorConstant.ERRCODE_NETWORK_REQUEST_CONVERT_ERROR, ErrorConstant.ERRMSG_NETWORK_REQUEST_CONVERT_ERROR);
        FilterUtils.handleExceptionCallBack(mtopContext);
        return FilterResult.STOP;
    }
}
