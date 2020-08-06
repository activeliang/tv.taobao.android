package com.taobao.tao.remotebusiness.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.intf.Mtop;

public class RemoteLogin {
    private static final String DEFAULT_USERINFO = "DEFAULT";
    public static final String TAG = "mtopsdk.RemoteLogin";
    private static Map<String, IRemoteLogin> mtopLoginMap = new ConcurrentHashMap();

    public static IRemoteLogin getLogin(Mtop mtopInstance) {
        String instanceId = mtopInstance == null ? Mtop.Id.INNER : mtopInstance.getInstanceId();
        IRemoteLogin login = mtopLoginMap.get(instanceId);
        if (login == null) {
            synchronized (RemoteLogin.class) {
                login = mtopLoginMap.get(instanceId);
                if (login == null) {
                    login = DefaultLoginImpl.getDefaultLoginImpl(mtopInstance == null ? null : mtopInstance.getMtopConfig().context);
                    if (login == null) {
                        TBSdkLog.e(TAG, instanceId + " [getLogin]loginImpl is null");
                        throw new LoginNotImplementException(instanceId + " [getLogin] Login Not Implement!");
                    }
                    mtopLoginMap.put(instanceId, login);
                }
            }
        }
        return login;
    }

    public static void setLoginImpl(@NonNull Mtop mtopInstance, @NonNull IRemoteLogin login) {
        if (login != null) {
            String instanceId = mtopInstance == null ? Mtop.Id.INNER : mtopInstance.getInstanceId();
            mtopLoginMap.put(instanceId, login);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, instanceId + " [setLoginImpl] set loginImpl=" + login);
            }
        }
    }

    public static void login(@NonNull Mtop mtopInstance, @Nullable String userInfo, boolean showLoginUI, Object extra) {
        String bizInfoExt;
        String acturalUserInfo;
        IRemoteLogin iRemoteLogin = getLogin(mtopInstance);
        String instanceId = mtopInstance == null ? Mtop.Id.INNER : mtopInstance.getInstanceId();
        if (StringUtils.isBlank(userInfo)) {
            bizInfoExt = "DEFAULT";
        } else {
            bizInfoExt = userInfo;
        }
        String key = StringUtils.concatStr(instanceId, bizInfoExt);
        MultiAccountRemoteLogin multiAccountRemoteLogin = null;
        if (iRemoteLogin instanceof MultiAccountRemoteLogin) {
            multiAccountRemoteLogin = (MultiAccountRemoteLogin) iRemoteLogin;
        }
        if ("DEFAULT".equals(userInfo)) {
            acturalUserInfo = null;
        } else {
            acturalUserInfo = userInfo;
        }
        if (!(multiAccountRemoteLogin != null ? multiAccountRemoteLogin.isLogining(acturalUserInfo) : iRemoteLogin.isLogining())) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, key + " [login]call login");
            }
            if (extra != null && (iRemoteLogin instanceof DefaultLoginImpl)) {
                ((DefaultLoginImpl) iRemoteLogin).setSessionInvalid(extra);
            }
            LoginHandler loginHandler = LoginHandler.instance(mtopInstance, userInfo);
            if (multiAccountRemoteLogin != null) {
                multiAccountRemoteLogin.login(acturalUserInfo, loginHandler, showLoginUI);
            } else {
                iRemoteLogin.login(loginHandler, showLoginUI);
            }
            loginHandler.sendEmptyMessageDelayed(LoginHandler.LOGIN_TIMEOUT, 20000);
        } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
            TBSdkLog.w(TAG, key + " [login] loginsdk is logining");
        }
    }

    public static void setSessionInvalid(@NonNull Mtop mtopInstance, Bundle bundle) {
        IRemoteLogin iRemoteLogin = getLogin(mtopInstance);
        if (iRemoteLogin instanceof IRemoteLoginAdapter) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, (mtopInstance == null ? Mtop.Id.INNER : mtopInstance.getInstanceId()) + " [setSessionInvalid] bundle=" + bundle);
            }
            ((IRemoteLoginAdapter) iRemoteLogin).setSessionInvalid(bundle);
        }
    }

    public static boolean isSessionValid(@NonNull Mtop mtopInstance, @Nullable String userInfo) {
        String acturalUserInfo;
        IRemoteLogin iRemoteLogin = getLogin(mtopInstance);
        MultiAccountRemoteLogin multiAccountRemoteLogin = null;
        if (iRemoteLogin instanceof MultiAccountRemoteLogin) {
            multiAccountRemoteLogin = (MultiAccountRemoteLogin) iRemoteLogin;
        }
        if ("DEFAULT".equals(userInfo)) {
            acturalUserInfo = null;
        } else {
            acturalUserInfo = userInfo;
        }
        if (multiAccountRemoteLogin != null ? multiAccountRemoteLogin.isLogining(acturalUserInfo) : iRemoteLogin.isLogining()) {
            return false;
        }
        return multiAccountRemoteLogin != null ? multiAccountRemoteLogin.isSessionValid(acturalUserInfo) : iRemoteLogin.isSessionValid();
    }

    public static LoginContext getLoginContext(@NonNull Mtop mtopInstance, @Nullable String userInfo) {
        String acturalUserInfo;
        IRemoteLogin iRemoteLogin = getLogin(mtopInstance);
        if (!(iRemoteLogin instanceof MultiAccountRemoteLogin)) {
            return iRemoteLogin.getLoginContext();
        }
        if ("DEFAULT".equals(userInfo)) {
            acturalUserInfo = null;
        } else {
            acturalUserInfo = userInfo;
        }
        return ((MultiAccountRemoteLogin) iRemoteLogin).getLoginContext(acturalUserInfo);
    }

    @Deprecated
    public static IRemoteLogin getLogin() {
        return getLogin((Mtop) null);
    }

    @Deprecated
    public static void setLoginImpl(IRemoteLogin login) {
        setLoginImpl((Mtop) null, login);
    }

    @Deprecated
    public static void login(boolean showLoginUI) {
        login((Mtop) null, (String) null, showLoginUI, (Object) null);
    }

    @Deprecated
    public static void login(boolean showLoginUI, Object extra) {
        login((Mtop) null, (String) null, showLoginUI, extra);
    }

    @Deprecated
    public static boolean isSessionValid() {
        return isSessionValid((Mtop) null, (String) null);
    }

    @Deprecated
    public static LoginContext getLoginContext() {
        return getLoginContext((Mtop) null, (String) null);
    }
}
