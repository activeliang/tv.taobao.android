package com.taobao.tao.remotebusiness.login;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.taobao.tao.remotebusiness.RequestPool;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.util.ErrorConstant;

class LoginHandler extends Handler implements onLoginListener {
    private static final String DEFAULT_USERINFO = "DEFAULT";
    public static final int LOGIN_CANCEL = 911103;
    public static final int LOGIN_FAILED = 911102;
    public static final int LOGIN_SUCCESS = 911101;
    public static final int LOGIN_TIMEOUT = 911104;
    private static final String TAG = "mtopsdk.LoginHandler";
    private static Map<String, LoginHandler> mtopLoginHandlerMap = new ConcurrentHashMap();
    @NonNull
    private Mtop mtopInstance;
    @Nullable
    private String userInfo;

    private LoginHandler(@NonNull Mtop mtopInstance2, @Nullable String userInfo2, Looper looper) {
        super(looper);
        this.mtopInstance = mtopInstance2;
        this.userInfo = userInfo2;
    }

    public static LoginHandler instance(@NonNull Mtop mtopInstance2, @Nullable String userInfo2) {
        Mtop mtop;
        String userInfoExt;
        if (mtopInstance2 == null) {
            mtop = Mtop.instance((Context) null);
        } else {
            mtop = mtopInstance2;
        }
        if (StringUtils.isBlank(userInfo2)) {
            userInfoExt = "DEFAULT";
        } else {
            userInfoExt = userInfo2;
        }
        String key = getKey(mtopInstance2, userInfoExt);
        LoginHandler mHandler = mtopLoginHandlerMap.get(key);
        if (mHandler == null) {
            synchronized (LoginHandler.class) {
                try {
                    mHandler = mtopLoginHandlerMap.get(key);
                    if (mHandler == null) {
                        LoginHandler mHandler2 = new LoginHandler(mtop, userInfoExt, Looper.getMainLooper());
                        try {
                            mtopLoginHandlerMap.put(key, mHandler2);
                            mHandler = mHandler2;
                        } catch (Throwable th) {
                            th = th;
                            LoginHandler loginHandler = mHandler2;
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }
        return mHandler;
    }

    @Deprecated
    public static LoginHandler instance() {
        return instance(Mtop.instance((Context) null), (String) null);
    }

    public void handleMessage(Message msg) {
        String key = getKey(this.mtopInstance, this.userInfo);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, key + " [handleMessage]The MtopBusiness LoginHandler receive message .");
        }
        switch (msg.what) {
            case LOGIN_SUCCESS /*911101*/:
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, key + " [handleMessage]onReceive: NOTIFY_LOGIN_SUCCESS.");
                }
                updateXStateSessionInfo(key);
                RequestPool.retryAllRequest(this.mtopInstance, this.userInfo);
                removeMessages(LOGIN_TIMEOUT);
                return;
            case LOGIN_FAILED /*911102*/:
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, key + "[handleMessage]onReceive: NOTIFY_LOGIN_FAILED.");
                }
                RequestPool.failAllRequest(this.mtopInstance, this.userInfo, ErrorConstant.ERRCODE_ANDROID_SYS_LOGIN_FAIL, ErrorConstant.ERRMSG_ANDROID_SYS_LOGIN_FAIL);
                removeMessages(LOGIN_TIMEOUT);
                return;
            case LOGIN_CANCEL /*911103*/:
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, key + "[handleMessage]onReceive: NOTIFY_LOGIN_CANCEL.");
                }
                RequestPool.failAllRequest(this.mtopInstance, this.userInfo, ErrorConstant.ERRCODE_ANDROID_SYS_LOGIN_CANCEL, ErrorConstant.ERRMSG_ANDROID_SYS_LOGIN_CANCEL);
                removeMessages(LOGIN_TIMEOUT);
                return;
            case LOGIN_TIMEOUT /*911104*/:
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, key + "[handleMessage]onReceive: NOTIFY_LOGIN_TIMEOUT.");
                }
                if (RemoteLogin.isSessionValid(this.mtopInstance, this.userInfo)) {
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, "Session valid, Broadcast may missed!");
                    }
                    updateXStateSessionInfo(key);
                    RequestPool.retryAllRequest(this.mtopInstance, this.userInfo);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void updateXStateSessionInfo(String key) {
        LoginContext context = RemoteLogin.getLoginContext(this.mtopInstance, this.userInfo);
        if (context == null) {
            TBSdkLog.e(TAG, key + " [updateXStateSessionInfo] LoginContext is null.");
            return;
        }
        try {
            if (StringUtils.isNotBlank(context.sid) && !context.sid.equals(this.mtopInstance.getMultiAccountSid(this.userInfo))) {
                this.mtopInstance.registerMultiAccountSession(this.userInfo, context.sid, context.userId);
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, key + " [updateXStateSessionInfo] invoked.");
                }
            }
        } catch (Exception e) {
            TBSdkLog.e(TAG, key + " [updateXStateSessionInfo] error.", (Throwable) e);
        }
    }

    private static String getKey(@NonNull Mtop mtopInstance2, @Nullable String userInfo2) {
        String userInfoExt;
        if (StringUtils.isBlank(userInfo2)) {
            userInfoExt = "DEFAULT";
        } else {
            userInfoExt = userInfo2;
        }
        return StringUtils.concatStr(mtopInstance2.getInstanceId(), userInfoExt);
    }

    public void onLoginSuccess() {
        sendEmptyMessage(LOGIN_SUCCESS);
    }

    public void onLoginFail() {
        sendEmptyMessage(LOGIN_FAILED);
    }

    public void onLoginCancel() {
        sendEmptyMessage(LOGIN_CANCEL);
    }
}
