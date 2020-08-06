package com.ali.auth.third.mtop.rpc;

import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.util.ReflectionUtils;
import com.taobao.tao.remotebusiness.login.IRemoteLogin;
import com.taobao.tao.remotebusiness.login.LoginContext;
import com.taobao.tao.remotebusiness.login.onLoginListener;

public class MtopRemoteLoginImpl implements IRemoteLogin {
    public void login(final onLoginListener listener, boolean bShowLoginUI) {
        Class<?> loginServiceImplClass;
        LoginCallback loginCallback = new LoginCallback() {
            public void onSuccess(Session session) {
                if (listener != null) {
                    listener.onLoginSuccess();
                }
            }

            public void onFailure(int code, String msg) {
                if (listener == null) {
                    return;
                }
                if (code == 10003) {
                    listener.onLoginCancel();
                } else {
                    listener.onLoginFail();
                }
            }
        };
        String className = ReflectionConstant.LoginClass;
        try {
            loginServiceImplClass = Class.forName(className);
        } catch (Throwable th) {
            className = ReflectionConstant.OfflineLoginClass;
            loginServiceImplClass = Class.forName(ReflectionConstant.OfflineLoginClass);
        }
        try {
            ReflectionUtils.invoke(className, "auth", new String[]{"com.ali.auth.third.core.callback.LoginCallback"}, loginServiceImplClass.newInstance(), new Object[]{loginCallback});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InstantiationException e3) {
            e3.printStackTrace();
        }
    }

    public boolean isSessionValid() {
        return KernelContext.credentialService.isSessionValid();
    }

    public boolean isLogining() {
        Class<?> loginServiceImplClass;
        String className = ReflectionConstant.StatusClass;
        try {
            loginServiceImplClass = Class.forName(className);
        } catch (Throwable th) {
            className = ReflectionConstant.OfflineStatusClass;
            loginServiceImplClass = Class.forName(ReflectionConstant.OfflineStatusClass);
        }
        try {
            Object result = ReflectionUtils.invoke(className, "isLogining", (String[]) null, loginServiceImplClass.newInstance(), (Object[]) null);
            if (result != null && (result instanceof Boolean)) {
                return ((Boolean) result).booleanValue();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return false;
    }

    public LoginContext getLoginContext() {
        LoginContext loginContext = new LoginContext();
        try {
            loginContext.nickname = KernelContext.credentialService.getSession().nick;
        } catch (Exception e) {
        }
        return loginContext;
    }
}
