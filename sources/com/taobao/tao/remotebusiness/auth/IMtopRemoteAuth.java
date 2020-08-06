package com.taobao.tao.remotebusiness.auth;

public abstract class IMtopRemoteAuth implements IRemoteAuth {
    public abstract void authorize(AuthParam authParam, AuthListener authListener);

    public abstract String getAuthToken(AuthParam authParam);

    public abstract boolean isAuthInfoValid(AuthParam authParam);

    public abstract boolean isAuthorizing(AuthParam authParam);

    @Deprecated
    public void authorize(String bizId, String apiInfo, String failInfo, boolean showUI, AuthListener listener) {
        AuthParam authParam = new AuthParam((String) null, bizId, showUI);
        authParam.apiInfo = apiInfo;
        authParam.failInfo = failInfo;
        authorize(authParam, listener);
    }

    @Deprecated
    public boolean isAuthInfoValid() {
        return false;
    }

    @Deprecated
    public boolean isAuthorizing() {
        return false;
    }

    @Deprecated
    public String getAuthToken() {
        return null;
    }
}
