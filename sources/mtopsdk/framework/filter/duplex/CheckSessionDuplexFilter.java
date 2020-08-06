package mtopsdk.framework.filter.duplex;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.taobao.tao.remotebusiness.RequestPool;
import com.taobao.tao.remotebusiness.login.LoginContext;
import com.taobao.tao.remotebusiness.login.RemoteLogin;
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
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopBuilder;

public class CheckSessionDuplexFilter implements IBeforeFilter, IAfterFilter {
    private static final String TAG = "mtopsdk.CheckSessionDuplexFilter";

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
        try {
            String multiAccountUserInfo = mtopBusiness.mtopProp.userInfo;
            if (!needLogin || RemoteLogin.isSessionValid(mtopInstance, multiAccountUserInfo)) {
                if (needLogin && StringUtils.isBlank(mtopInstance.getMultiAccountSid(multiAccountUserInfo))) {
                    LoginContext loginContext = RemoteLogin.getLoginContext(mtopInstance, multiAccountUserInfo);
                    if (loginContext == null || StringUtils.isBlank(loginContext.sid)) {
                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                            TBSdkLog.i(TAG, mtopContext.seqNo, "execute CheckSessionBeforeFilter.isSessionInvalid = true,getLoginContext = null");
                        }
                        RequestPool.addToRequestPool(mtopInstance, multiAccountUserInfo, mtopBusiness);
                        RemoteLogin.login(mtopInstance, multiAccountUserInfo, mtopBusiness.isShowLoginUI(), request);
                        return FilterResult.STOP;
                    }
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                        TBSdkLog.w(TAG, mtopContext.seqNo, "session in loginContext is valid but mtopInstance's sid is null");
                    }
                    mtopInstance.registerMultiAccountSession(multiAccountUserInfo, loginContext.sid, loginContext.userId);
                }
                return FilterResult.CONTINUE;
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopContext.seqNo, "execute CheckSessionBeforeFilter.isSessionInvalid = false");
            }
            RequestPool.addToRequestPool(mtopInstance, multiAccountUserInfo, mtopBusiness);
            RemoteLogin.login(mtopInstance, multiAccountUserInfo, mtopBusiness.isShowLoginUI(), request);
            return FilterResult.STOP;
        } catch (Exception e) {
            TBSdkLog.e(TAG, mtopContext.seqNo, " execute CheckSessionBeforeFilter error.", e);
        }
    }

    public String doAfter(MtopContext mtopContext) {
        MtopBuilder mtopBuilder = mtopContext.mtopBuilder;
        if (!(mtopBuilder instanceof MtopBusiness)) {
            return FilterResult.CONTINUE;
        }
        MtopBusiness mtopBusiness = (MtopBusiness) mtopBuilder;
        MtopRequest request = mtopContext.mtopRequest;
        Mtop mtopInstance = mtopContext.mtopInstance;
        MtopResponse response = mtopContext.mtopResponse;
        if (mtopInstance.getMtopConfig().notifySessionResult) {
            String session_ret = HeaderHandlerUtil.getSingleHeaderFieldByKey(response.getHeaderFields(), HttpHeaderConstant.X_SESSION_RET);
            if (StringUtils.isNotBlank(session_ret)) {
                Bundle bundle = new Bundle();
                bundle.putString(HttpHeaderConstant.X_SESSION_RET, session_ret);
                bundle.putString("Date", HeaderHandlerUtil.getSingleHeaderFieldByKey(response.getHeaderFields(), "Date"));
                RemoteLogin.setSessionInvalid(mtopInstance, bundle);
            }
        }
        if (!response.isSessionInvalid() || !request.isNeedEcode() || mtopBusiness.getRetryTime() != 0) {
            return FilterResult.CONTINUE;
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopContext.seqNo, "execute CheckSessionAfterFilter.");
        }
        String multiAccountUserInfo = mtopBusiness.mtopProp.userInfo;
        RequestPool.addToRequestPool(mtopInstance, multiAccountUserInfo, mtopBusiness);
        RemoteLogin.login(mtopInstance, multiAccountUserInfo, mtopBusiness.isShowLoginUI(), response);
        return FilterResult.STOP;
    }
}
