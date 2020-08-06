package com.yunos.tv.core.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.Toast;
import anetwork.channel.util.RequestConstant;
import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.ali.auth.third.offline.login.LoginService;
import com.ali.auth.third.offline.login.callback.LogoutCallback;
import com.ali.auth.third.offline.webview.AuthWebView;
import com.tvtaobao.android.runtime.RtBaseEnv;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.global.BzAppMain;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.payment.MemberSDKLoginHelper;
import com.yunos.tvtaobao.payment.MemberSDKLoginStatus;
import com.yunos.tvtaobao.payment.account.AccountUtil;
import com.yunos.tvtaobao.payment.request.RequestLoginCallBack;
import com.zhiping.dev.android.logger.ZpLogger;
import com.zhiping.tvtao.payment.alipay.request.ReleaseContractRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import mtopsdk.mtop.common.ApiID;
import mtopsdk.mtop.common.DefaultMtopCallback;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.global.SDKUtils;
import mtopsdk.mtop.intf.Mtop;

public class LoginHelperImpl implements LoginHelper, MemberSDKLoginHelper {
    public static final String MEMBER_LOGIN_BROADCAST_ACTION = "com.membersSDK.login";
    public static final String MEMBER_LOGOUT_BROADCAST_ACTION = "com.membersSDK.loginOut";
    private static final String TAG = "LoginHelper";
    private static LoginHelperImpl mJuLoginHelper;
    public static Context mRegistedContext;
    private LoginCallback autoLoginCallback;
    private boolean forceLogout;
    private RtBaseEnv.MsgEar loginEventEar;
    /* access modifiers changed from: private */
    public FrequentLock mFrequentLock;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private BroadcastReceiver mLoginListenerReceiver;
    /* access modifiers changed from: private */
    public SparseArray<MemberSDKLoginHelper.SyncLoginListener> mMemberReceiveLoginListenerMap;
    private String mNewestToken;
    /* access modifiers changed from: private */
    public SparseArray<LoginHelper.SyncLoginListener> mReceiveLoginListenerMap;
    /* access modifiers changed from: private */
    public String nick;

    /* access modifiers changed from: private */
    public void userLoginout() {
        try {
            BzAppMain.mMtopInstance.logout();
            SDKUtils.logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LoginHelperImpl(Context context) {
        this.forceLogout = false;
        this.mNewestToken = "";
        this.mLoginListenerReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(LoginHelperImpl.MEMBER_LOGIN_BROADCAST_ACTION)) {
                    int listenerSize = LoginHelperImpl.this.mReceiveLoginListenerMap.size();
                    for (int i = 0; i < listenerSize; i++) {
                        LoginHelper.SyncLoginListener listener = (LoginHelper.SyncLoginListener) LoginHelperImpl.this.mReceiveLoginListenerMap.get(LoginHelperImpl.this.mReceiveLoginListenerMap.keyAt(i));
                        if (listener != null) {
                            listener.onLogin(true);
                        }
                    }
                    int memberListenerSize = LoginHelperImpl.this.mMemberReceiveLoginListenerMap.size();
                    for (int i2 = 0; i2 < memberListenerSize; i2++) {
                        MemberSDKLoginHelper.SyncLoginListener listener2 = (MemberSDKLoginHelper.SyncLoginListener) LoginHelperImpl.this.mMemberReceiveLoginListenerMap.get(LoginHelperImpl.this.mMemberReceiveLoginListenerMap.keyAt(i2));
                        if (listener2 != null) {
                            listener2.onLogin(true);
                        }
                    }
                } else if (intent.getAction().equals(LoginHelperImpl.MEMBER_LOGOUT_BROADCAST_ACTION)) {
                    LoginHelperImpl.this.userLoginout();
                    User.clearUser();
                    int listenerSize2 = LoginHelperImpl.this.mReceiveLoginListenerMap.size();
                    for (int i3 = 0; i3 < listenerSize2; i3++) {
                        LoginHelper.SyncLoginListener listener3 = (LoginHelper.SyncLoginListener) LoginHelperImpl.this.mReceiveLoginListenerMap.get(LoginHelperImpl.this.mReceiveLoginListenerMap.keyAt(i3));
                        if (listener3 != null) {
                            listener3.onLogin(false);
                        }
                    }
                    int memberListenerSize2 = LoginHelperImpl.this.mMemberReceiveLoginListenerMap.size();
                    for (int i4 = 0; i4 < memberListenerSize2; i4++) {
                        MemberSDKLoginHelper.SyncLoginListener listener4 = (MemberSDKLoginHelper.SyncLoginListener) LoginHelperImpl.this.mMemberReceiveLoginListenerMap.get(LoginHelperImpl.this.mMemberReceiveLoginListenerMap.keyAt(i4));
                        if (listener4 != null) {
                            listener4.onLogin(false);
                        }
                    }
                }
            }
        };
        this.loginEventEar = new RtBaseEnv.MsgEar() {
            public void onMsg(RtBaseEnv.Msg msg) {
                if (msg != null && AccountUtil.EVENT_LOGIN.equals(msg.name) && (msg.data instanceof AccountUtil.LoginResult)) {
                    AccountUtil.LoginResult rlt = (AccountUtil.LoginResult) msg.data;
                    if (rlt.successResult) {
                        LoginHelperImpl.this.onLoginSuccess(rlt.session);
                    } else {
                        LoginHelperImpl.this.onLoginFailure(rlt.i, rlt.s);
                    }
                }
            }
        };
        this.autoLoginCallback = new LoginCallback() {
            public void onSuccess(Session session) {
                LoginHelperImpl.this.mFrequentLock.clearLoack();
                SharePreferences.put("account_tr", true);
                ConfigManager.getInstance().setSsoToken((String) null);
                User.updateUser(session);
                String unused = LoginHelperImpl.this.nick = session.nick;
                if (LoginHelperImpl.this.mReceiveLoginListenerMap != null) {
                    int listenerSize = LoginHelperImpl.this.mReceiveLoginListenerMap.size();
                    for (int i = 0; i < listenerSize; i++) {
                        LoginHelper.SyncLoginListener listener = (LoginHelper.SyncLoginListener) LoginHelperImpl.this.mReceiveLoginListenerMap.get(LoginHelperImpl.this.mReceiveLoginListenerMap.keyAt(i));
                        if (listener != null) {
                            listener.onLogin(true);
                        }
                    }
                }
                if (LoginHelperImpl.this.mMemberReceiveLoginListenerMap != null) {
                    int listenerSize2 = LoginHelperImpl.this.mMemberReceiveLoginListenerMap.size();
                    for (int i2 = 0; i2 < listenerSize2; i2++) {
                        MemberSDKLoginHelper.SyncLoginListener listener2 = (MemberSDKLoginHelper.SyncLoginListener) LoginHelperImpl.this.mMemberReceiveLoginListenerMap.get(LoginHelperImpl.this.mMemberReceiveLoginListenerMap.keyAt(i2));
                        if (listener2 != null) {
                            listener2.onLogin(true);
                        }
                    }
                }
            }

            public void onFailure(int code, String msg) {
                LoginHelperImpl.this.mFrequentLock.clearLoack();
                if (LoginHelperImpl.this.mReceiveLoginListenerMap != null) {
                    int listenerSize = LoginHelperImpl.this.mReceiveLoginListenerMap.size();
                    for (int i = 0; i < listenerSize; i++) {
                        LoginHelper.SyncLoginListener listener = (LoginHelper.SyncLoginListener) LoginHelperImpl.this.mReceiveLoginListenerMap.get(LoginHelperImpl.this.mReceiveLoginListenerMap.keyAt(i));
                        if (listener != null) {
                            listener.onLogin(false);
                        }
                    }
                }
                if (LoginHelperImpl.this.mMemberReceiveLoginListenerMap != null) {
                    int listenerSize2 = LoginHelperImpl.this.mMemberReceiveLoginListenerMap.size();
                    for (int i2 = 0; i2 < listenerSize2; i2++) {
                        MemberSDKLoginHelper.SyncLoginListener listener2 = (MemberSDKLoginHelper.SyncLoginListener) LoginHelperImpl.this.mMemberReceiveLoginListenerMap.get(LoginHelperImpl.this.mMemberReceiveLoginListenerMap.keyAt(i2));
                        if (listener2 != null) {
                            listener2.onLogin(false);
                        }
                    }
                }
            }
        };
        this.mReceiveLoginListenerMap = new SparseArray<>();
        this.mReceiveLoginListenerMap = new SparseArray<>();
        this.mMemberReceiveLoginListenerMap = new SparseArray<>();
        this.mMemberReceiveLoginListenerMap = new SparseArray<>();
        this.mFrequentLock = new FrequentLock();
        this.mHandler = new Handler(Looper.getMainLooper());
        ZpLogger.d(TAG, "wvsupport: " + SharePreferences.getInt("wv_support", 0).intValue());
        try {
            Class.forName(AuthWebView.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!SharePreferences.getBoolean("account_tr", false).booleanValue()) {
            ZpLogger.d(TAG, "Loginhelperimpl trytotransfer");
            tryToTransferAccount();
        }
        registerLoginListener(context);
        RtEnv.listen(this.loginEventEar);
    }

    private void tryToTransferAccount() {
    }

    public static LoginHelperImpl getJuLoginHelper() {
        if (mJuLoginHelper == null) {
            synchronized (LoginHelperImpl.class) {
                if (mJuLoginHelper == null) {
                    mJuLoginHelper = new LoginHelperImpl(CoreApplication.getApplication());
                }
            }
        }
        return mJuLoginHelper;
    }

    public void destroy() {
        this.mReceiveLoginListenerMap.clear();
        this.mMemberReceiveLoginListenerMap.clear();
        try {
            CredentialManager.INSTANCE.logout();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        unregisterLoginListener();
        RtEnv.unlisten(this.loginEventEar);
    }

    public void addSyncLoginListener(LoginHelper.SyncLoginListener listener) {
        if (listener != null) {
            this.mReceiveLoginListenerMap.put(listener.hashCode(), listener);
        }
    }

    public void addSyncLoginListener(MemberSDKLoginHelper.SyncLoginListener listener) {
        if (listener != null) {
            this.mMemberReceiveLoginListenerMap.put(listener.hashCode(), listener);
        }
    }

    public void removeSyncLoginListener(LoginHelper.SyncLoginListener listener) {
        if (listener != null) {
            this.mReceiveLoginListenerMap.remove(listener.hashCode());
        }
    }

    public void removeSyncLoginListener(MemberSDKLoginHelper.SyncLoginListener listener) {
        if (listener != null) {
            this.mMemberReceiveLoginListenerMap.remove(listener.hashCode());
        }
    }

    public void registerLoginListener(Context context) {
        unregisterLoginListener();
        mRegistedContext = context;
        IntentFilter intentFilter = new IntentFilter(MEMBER_LOGIN_BROADCAST_ACTION);
        intentFilter.addAction(MEMBER_LOGOUT_BROADCAST_ACTION);
        mRegistedContext.registerReceiver(this.mLoginListenerReceiver, intentFilter);
    }

    public Context getRegistedContext() {
        return mRegistedContext;
    }

    public void unregisterLoginListener() {
        if (mRegistedContext != null) {
            mRegistedContext.unregisterReceiver(this.mLoginListenerReceiver);
            mRegistedContext = null;
        }
    }

    public void addReceiveLoginListener(LoginHelper.SyncLoginListener listener) {
        if (listener != null) {
            this.mReceiveLoginListenerMap.put(listener.hashCode(), listener);
        }
    }

    public void addReceiveLoginListener(MemberSDKLoginHelper.SyncLoginListener listener) {
        if (listener != null) {
            this.mMemberReceiveLoginListenerMap.put(listener.hashCode(), listener);
        }
    }

    public void removeReceiveLoginListener(LoginHelper.SyncLoginListener listener) {
        if (listener != null) {
            this.mReceiveLoginListenerMap.remove(listener.hashCode());
        }
    }

    public void removeReceiveLoginListener(MemberSDKLoginHelper.SyncLoginListener listener) {
        if (listener != null) {
            this.mMemberReceiveLoginListenerMap.remove(listener.hashCode());
        }
    }

    public boolean isLogin() {
        try {
            LoginService service = (LoginService) MemberSDK.getService(LoginService.class);
            if (service == null) {
                return false;
            }
            return service.checkSessionValid();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void login(Context context) {
        LoginService service;
        if (this.mFrequentLock == null || !this.mFrequentLock.isLock()) {
            ZpLogger.i(TAG, "login islogginout " + MemberSDKLoginStatus.isLoggingOut());
            if (!MemberSDKLoginStatus.isLoggingOut() && (service = (LoginService) MemberSDK.getService(LoginService.class)) != null) {
                service.autoLogin(this.autoLoginCallback);
                return;
            }
            return;
        }
        ZpLogger.i(TAG, "login isLock = " + this.mFrequentLock.isLock());
    }

    public void ssoLogin() {
        if (this.mFrequentLock == null || this.mFrequentLock.isLock()) {
        }
        LoginService service = (LoginService) MemberSDK.getService(LoginService.class);
        if (service != null) {
            try {
                Method ssoTokenLogin = service.getClass().getDeclaredMethod("ssoTokenLogin", new Class[]{LoginCallback.class});
                ssoTokenLogin.setAccessible(true);
                ssoTokenLogin.invoke(service, new Object[]{this.autoLoginCallback});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
    }

    public void startYunosAccountActivity(final Context context, boolean ifChangeAccount) {
        LoginService service;
        if (MemberSDKLoginStatus.compareAndSetLogin(false, true) && (service = (LoginService) MemberSDK.getService(LoginService.class)) != null) {
            service.auth(new LoginCallback() {
                public void onSuccess(Session session) {
                    LoginHelperImpl.this.onLoginSuccess(session);
                    LoginHelperImpl.this.sendBroadcastLogin(context, true);
                    MemberSDKLoginStatus.compareAndSetLogin(true, false);
                }

                public void onFailure(int i, String s) {
                    LoginHelperImpl.this.onLoginFailure(i, s);
                    MemberSDKLoginStatus.compareAndSetLogin(true, false);
                }
            });
        }
    }

    public void logout(final Context context) {
        try {
            Mtop.instance(context).build((MtopRequest) new RequestLoginCallBack(User.getUserId(), Config.getExtParams(), false), (String) null).useWua().reqMethod(MethodEnum.POST).addListener(new MtopCallback.MtopFinishListener() {
                public void onFinished(MtopFinishEvent mtopFinishEvent, Object o) {
                    ZpLogger.d(LoginHelperImpl.TAG, "logout callback: " + mtopFinishEvent.getMtopResponse().getDataJsonObject());
                }
            }).asyncRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((LoginService) MemberSDK.getService(LoginService.class)).logout(new LogoutCallback() {
            public void onSuccess() {
                ZpLogger.d(LoginHelperImpl.TAG, " memberSDK loginOut succsss");
                Toast.makeText(context, "成功登出", 0).show();
                LoginHelperImpl.this.sendBroadcastLogin(context, false);
            }

            public void onFailure(int i, String s) {
                ZpLogger.d(LoginHelperImpl.TAG, " memberSDK loginOut failure");
            }
        });
        ApiID asyncRequest = Mtop.instance(context).build((Object) new ReleaseContractRequest(), (String) null).useWua().addListener(new DefaultMtopCallback() {
            public void onFinished(MtopFinishEvent event, Object context) {
                ZpLogger.d(RequestConstant.ENV_TEST, "releaseContract response: " + event.getMtopResponse().getDataJsonObject());
                super.onFinished(event, context);
            }
        }).asyncRequest();
    }

    public String getNick() {
        return User.getNick();
    }

    public void loginRequest(Context context) {
        login(context);
    }

    public void sendBroadcastLogin(Context context, boolean islogin) {
        if (islogin) {
            context.sendBroadcast(new Intent(MEMBER_LOGIN_BROADCAST_ACTION));
        } else {
            context.sendBroadcast(new Intent(MEMBER_LOGOUT_BROADCAST_ACTION));
        }
    }

    /* access modifiers changed from: private */
    public void onLoginSuccess(final Session session) {
        this.mHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(BzAppConfig.context.getContext(), "欢迎回来，" + session.nick, 1).show();
            }
        });
        User.updateUser(session);
        this.nick = session.nick;
        try {
            Mtop.instance(BzAppConfig.context.getContext()).build((MtopRequest) new RequestLoginCallBack(User.getUserId(), Config.getExtParams(), true), (String) null).reqMethod(MethodEnum.POST).useWua().addListener(new MtopCallback.MtopFinishListener() {
                public void onFinished(MtopFinishEvent mtopFinishEvent, Object o) {
                    ZpLogger.d(LoginHelperImpl.TAG, "loginSuccess callback: " + mtopFinishEvent.getMtopResponse().getDataJsonObject());
                }
            }).asyncRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int listenerSize = this.mReceiveLoginListenerMap.size();
        for (int i = 0; i < listenerSize; i++) {
            LoginHelper.SyncLoginListener listener = this.mReceiveLoginListenerMap.get(this.mReceiveLoginListenerMap.keyAt(i));
            if (listener != null) {
                listener.onLogin(true);
            }
        }
        int memberListenerSize = this.mMemberReceiveLoginListenerMap.size();
        for (int i2 = 0; i2 < memberListenerSize; i2++) {
            MemberSDKLoginHelper.SyncLoginListener listener2 = this.mMemberReceiveLoginListenerMap.get(this.mMemberReceiveLoginListenerMap.keyAt(i2));
            if (listener2 != null) {
                listener2.onLogin(!TextUtils.isEmpty(session.openSid));
            }
        }
        sendBroadcastLogin(BzAppConfig.context.getContext(), true);
    }

    /* access modifiers changed from: private */
    public void onLoginFailure(int code, String message) {
        int listenerSize = this.mReceiveLoginListenerMap.size();
        for (int j = 0; j < listenerSize; j++) {
            LoginHelper.SyncLoginListener listener = this.mReceiveLoginListenerMap.get(this.mReceiveLoginListenerMap.keyAt(j));
            if (listener != null) {
                listener.onLogin(false);
            }
        }
        int memberListenerSize = this.mMemberReceiveLoginListenerMap.size();
        for (int i = 0; i < memberListenerSize; i++) {
            MemberSDKLoginHelper.SyncLoginListener listener2 = this.mMemberReceiveLoginListenerMap.get(this.mMemberReceiveLoginListenerMap.keyAt(i));
            if (listener2 != null) {
                listener2.onLogin(false);
            }
        }
    }

    class LoginBroadcastReceiver extends BroadcastReceiver {
        LoginBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (LoginHelperImpl.MEMBER_LOGIN_BROADCAST_ACTION.equals(intent.getAction())) {
                LoginHelperImpl.this.mHandler.sendEmptyMessage(10001);
            } else if (LoginHelperImpl.MEMBER_LOGOUT_BROADCAST_ACTION.equals(intent.getAction())) {
                LoginHelperImpl.this.mHandler.sendEmptyMessage(10002);
            }
        }
    }

    class FrequentLock {
        private final int DELAY_TIME = 1000;
        private boolean haveLock = false;
        private long lastTimeLock = 0;

        FrequentLock() {
        }

        public boolean isLock() {
            long current = System.currentTimeMillis();
            if (this.haveLock && this.lastTimeLock > 0 && current - this.lastTimeLock > 1000) {
                clearLoack();
            }
            if (this.haveLock) {
                this.lastTimeLock = System.currentTimeMillis();
                return true;
            }
            this.haveLock = true;
            this.lastTimeLock = System.currentTimeMillis();
            return false;
        }

        public void clearLoack() {
            this.haveLock = false;
            this.lastTimeLock = 0;
        }
    }

    public String getSessionId() {
        return User.getSessionId();
    }

    public String getUserId() {
        return User.getUserId();
    }
}
