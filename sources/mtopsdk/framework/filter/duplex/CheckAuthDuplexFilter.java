package mtopsdk.framework.filter.duplex;

import android.support.annotation.NonNull;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.taobao.tao.remotebusiness.RequestPool;
import com.taobao.tao.remotebusiness.auth.AuthParam;
import com.taobao.tao.remotebusiness.auth.RemoteAuth;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopBuilder;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;

public class CheckAuthDuplexFilter implements IBeforeFilter, IAfterFilter {
    private static final String TAG = "mtopsdk.CheckAuthDuplexFilter";

    @NonNull
    public String getName() {
        return TAG;
    }

    public String doBefore(MtopContext mtopContext) {
        MtopBuilder mtopBuilder = mtopContext.mtopBuilder;
        if (!(mtopBuilder instanceof MtopBusiness)) {
            return FilterResult.CONTINUE;
        }
        MtopBusiness mtopBusiness = (MtopBusiness) mtopBuilder;
        MtopRequest request = mtopContext.mtopRequest;
        Mtop mtopInstance = mtopContext.mtopInstance;
        boolean needLogin = request.isNeedEcode();
        boolean needAuth = mtopBusiness.isNeedAuth();
        if (needLogin && needAuth) {
            try {
                if (mtopBusiness.getRetryTime() < 3) {
                    AuthParam authParam = new AuthParam(mtopBusiness.mtopProp.openAppKey, mtopBusiness.authParam, mtopBusiness.showAuthUI);
                    if (!RemoteAuth.isAuthInfoValid(mtopInstance, authParam)) {
                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                            TBSdkLog.i(TAG, mtopContext.seqNo, " execute CheckAuthBeforeFilter.isAuthInfoValid = false");
                        }
                        RequestPool.addToRequestPool(mtopInstance, authParam.openAppKey, mtopBusiness);
                        RemoteAuth.authorize(mtopInstance, authParam);
                        return FilterResult.STOP;
                    }
                    String key = StringUtils.concatStr(mtopInstance.getInstanceId(), authParam.openAppKey);
                    if (StringUtils.isBlank(XState.getValue(key, XStateConstants.KEY_ACCESS_TOKEN))) {
                        String authToken = RemoteAuth.getAuthToken(mtopInstance, authParam);
                        if (StringUtils.isNotBlank(authToken)) {
                            XState.setValue(key, XStateConstants.KEY_ACCESS_TOKEN, authToken);
                        } else {
                            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                                TBSdkLog.i(TAG, mtopContext.seqNo, " execute CheckAuthBeforeFilter.isAuthInfoValid = true,getAuthToken is null.");
                            }
                            RequestPool.addToRequestPool(mtopInstance, authParam.openAppKey, mtopBusiness);
                            RemoteAuth.authorize(mtopInstance, authParam);
                            return FilterResult.STOP;
                        }
                    }
                }
            } catch (Exception e) {
                TBSdkLog.e(TAG, mtopContext.seqNo, " execute CheckAuthBeforeFilter error.", e);
            }
        }
        return FilterResult.CONTINUE;
    }

    public String doAfter(MtopContext mtopContext) {
        MtopBuilder mtopBuilder = mtopContext.mtopBuilder;
        if (!(mtopBuilder instanceof MtopBusiness)) {
            return FilterResult.CONTINUE;
        }
        MtopBusiness mtopBusiness = (MtopBusiness) mtopBuilder;
        Mtop mtopInstance = mtopContext.mtopInstance;
        MtopResponse response = mtopContext.mtopResponse;
        String retCode = response.getRetCode();
        try {
            if (mtopBusiness.isNeedAuth() && mtopBusiness.getRetryTime() < 3 && SwitchConfig.authErrorCodeSet.contains(retCode)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, mtopContext.seqNo, " execute CheckAuthAfterFilter.");
                }
                AuthParam authParam = new AuthParam(mtopBusiness.mtopProp.openAppKey, mtopBusiness.authParam, mtopBusiness.showAuthUI);
                authParam.apiInfo = mtopBusiness.request.getKey();
                if (mtopBusiness.mtopProp.isInnerOpen) {
                    authParam.failInfo = retCode;
                } else {
                    authParam.failInfo = HeaderHandlerUtil.getSingleHeaderFieldByKey(response.getHeaderFields(), HttpHeaderConstant.X_ACT_HINT);
                }
                RequestPool.addToRequestPool(mtopInstance, authParam.openAppKey, mtopBusiness);
                RemoteAuth.authorize(mtopInstance, authParam);
                return FilterResult.STOP;
            }
        } catch (Exception e) {
            TBSdkLog.e(TAG, mtopContext.seqNo, " execute CheckAuthAfterFilter error.", e);
        }
        return FilterResult.CONTINUE;
    }
}
