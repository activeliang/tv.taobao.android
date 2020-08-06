package com.taobao.tao.remotebusiness.auth;

import android.support.annotation.NonNull;
import com.taobao.tao.remotebusiness.RequestPool;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;

public class RemoteAuth {
    private static final String TAG = "mtopsdk.RemoteAuth";
    private static Map<String, IRemoteAuth> mtopAuthMap = new ConcurrentHashMap();

    @Deprecated
    public static void setAuthImpl(IRemoteAuth remoteAuth) {
        setAuthImpl((Mtop) null, remoteAuth);
    }

    public static void setAuthImpl(@NonNull Mtop mtopInstance, @NonNull IRemoteAuth remoteAuth) {
        if (remoteAuth != null) {
            String instanceId = mtopInstance == null ? Mtop.Id.OPEN : mtopInstance.getInstanceId();
            mtopAuthMap.put(instanceId, remoteAuth);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, instanceId + " [setAuthImpl] set remoteAuthImpl=" + remoteAuth);
            }
        }
    }

    private static IRemoteAuth getAuth(@NonNull Mtop mtopInstance) {
        String instanceId = mtopInstance == null ? Mtop.Id.OPEN : mtopInstance.getInstanceId();
        IRemoteAuth remoteAuth = mtopAuthMap.get(instanceId);
        if (remoteAuth == null) {
            TBSdkLog.e(TAG, instanceId + " [getAuth]remoteAuthImpl is null");
        }
        return remoteAuth;
    }

    public static void authorize(@NonNull Mtop mtopInstance, AuthParam authParam) {
        if (authParam == null) {
            TBSdkLog.e(TAG, "[authorize] authParam is null");
            return;
        }
        IRemoteAuth iRemoteAuth = getAuth(mtopInstance);
        if (iRemoteAuth != null) {
            IMtopRemoteAuth mtopRemoteAuth = null;
            if (iRemoteAuth instanceof IMtopRemoteAuth) {
                mtopRemoteAuth = (IMtopRemoteAuth) iRemoteAuth;
            }
            if (!(mtopRemoteAuth != null ? mtopRemoteAuth.isAuthorizing(authParam) : iRemoteAuth.isAuthorizing())) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "call authorize. " + authParam);
                }
                AuthHandler authListener = new AuthHandler(mtopInstance, authParam);
                if (mtopRemoteAuth != null) {
                    mtopRemoteAuth.authorize(authParam, authListener);
                } else {
                    iRemoteAuth.authorize(authParam.bizParam, authParam.apiInfo, authParam.failInfo, authParam.showAuthUI, authListener);
                }
            }
        } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "didn't set IRemoteAuth implement. remoteAuth=null");
        }
    }

    public static boolean isAuthInfoValid(@NonNull Mtop mtopInstance, AuthParam authParam) {
        if (authParam == null) {
            TBSdkLog.e(TAG, "[isAuthInfoValid] authParam is null");
            return true;
        }
        IRemoteAuth iRemoteAuth = getAuth(mtopInstance);
        if (iRemoteAuth != null) {
            IMtopRemoteAuth mtopRemoteAuth = null;
            if (iRemoteAuth instanceof IMtopRemoteAuth) {
                mtopRemoteAuth = (IMtopRemoteAuth) iRemoteAuth;
            }
            if (mtopRemoteAuth != null ? mtopRemoteAuth.isAuthorizing(authParam) : iRemoteAuth.isAuthorizing()) {
                return false;
            }
            return mtopRemoteAuth != null ? mtopRemoteAuth.isAuthInfoValid(authParam) : iRemoteAuth.isAuthInfoValid();
        } else if (!TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            return true;
        } else {
            TBSdkLog.i(TAG, "didn't set IRemoteAuth implement. remoteAuth=null");
            return true;
        }
    }

    public static String getAuthToken(@NonNull Mtop mtopInstance, AuthParam authParam) {
        if (authParam == null) {
            TBSdkLog.e(TAG, "[getAuthToken] authParam is null");
            return null;
        }
        IRemoteAuth iRemoteAuth = getAuth(mtopInstance);
        if (iRemoteAuth != null) {
            IMtopRemoteAuth mtopRemoteAuth = null;
            if (iRemoteAuth instanceof IMtopRemoteAuth) {
                mtopRemoteAuth = (IMtopRemoteAuth) iRemoteAuth;
            }
            return mtopRemoteAuth != null ? mtopRemoteAuth.getAuthToken(authParam) : iRemoteAuth.getAuthToken();
        } else if (!TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            return null;
        } else {
            TBSdkLog.i(TAG, "didn't set IRemoteAuth implement. remoteAuth=null");
            return null;
        }
    }

    private static class AuthHandler implements AuthListener {
        @NonNull
        private AuthParam authParam;
        @NonNull
        private Mtop mtopInstance;

        public AuthHandler(@NonNull Mtop mtopInstance2, @NonNull AuthParam authParam2) {
            this.mtopInstance = mtopInstance2;
            this.authParam = authParam2;
        }

        public void onAuthSuccess() {
            String openAppKey = this.authParam.openAppKey != null ? this.authParam.openAppKey : "DEFAULT_AUTH";
            String key = StringUtils.concatStr(this.mtopInstance.getInstanceId(), openAppKey);
            String authToken = RemoteAuth.getAuthToken(this.mtopInstance, this.authParam);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(RemoteAuth.TAG, "auth success.authToken=" + authToken + ",key=" + key);
            }
            XState.setValue(key, XStateConstants.KEY_ACCESS_TOKEN, authToken);
            RequestPool.retryAllRequest(this.mtopInstance, openAppKey);
        }

        public void onAuthFail(String code, String msg) {
            String openAppKey = this.authParam.openAppKey != null ? this.authParam.openAppKey : "DEFAULT_AUTH";
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                StringBuilder builder = new StringBuilder(64);
                builder.append("[onAuthFail] auth fail,key=").append(StringUtils.concatStr(this.mtopInstance.getInstanceId(), openAppKey));
                builder.append(",code=").append(code).append(",msg=").append(msg);
                TBSdkLog.e(RemoteAuth.TAG, builder.toString());
            }
            RequestPool.failAllRequest(this.mtopInstance, openAppKey, code, msg);
        }

        public void onAuthCancel(String code, String msg) {
            String openAppKey = this.authParam.openAppKey != null ? this.authParam.openAppKey : "DEFAULT_AUTH";
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                StringBuilder builder = new StringBuilder(64);
                builder.append("[onAuthCancel] auth cancel,key=").append(StringUtils.concatStr(this.mtopInstance.getInstanceId(), openAppKey));
                builder.append(",code=").append(code).append(",msg=").append(msg);
                TBSdkLog.e(RemoteAuth.TAG, builder.toString());
            }
            RequestPool.failAllRequest(this.mtopInstance, openAppKey, code, msg);
        }
    }
}
