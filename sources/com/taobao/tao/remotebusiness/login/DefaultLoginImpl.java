package com.taobao.tao.remotebusiness.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.alibaba.fastjson.JSON;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.stat.IUploadStats;
import mtopsdk.xstate.XState;

public final class DefaultLoginImpl implements IRemoteLogin {
    private static final String MTOP_API_REFERENCE = "apiReferer";
    private static final String STATS_MODULE_MTOPRB = "mtoprb";
    private static final String STATS_MONITOR_POINT_SESSION_INVALID = "SessionInvalid";
    private static final String TAG = "mtopsdk.DefaultLoginImpl";
    public static volatile DefaultLoginImpl instance = null;
    private static volatile AtomicBoolean isRegistered = new AtomicBoolean(false);
    static Context mContext;
    private static ThreadLocal<SessionInvalidEvent> threadLocal = new ThreadLocal<>();
    private Method checkSessionValidMethod;
    private Method getNickMethod;
    private Method getSidMethod;
    private Method getUserIdMethod;
    private Method isLoginingMethod;
    private Class<?> loginBroadcastHelperCls = null;
    private Class<?> loginCls = null;
    private LoginContext loginContext = new LoginContext();
    private Method loginMethod;
    private Class<?> loginStatusCls = null;
    protected BroadcastReceiver receiver = null;
    private Method registerReceiverMethod;

    public static DefaultLoginImpl getDefaultLoginImpl(@NonNull Context context) {
        if (instance == null) {
            synchronized (DefaultLoginImpl.class) {
                if (instance == null) {
                    if (context == null) {
                        try {
                            context = MtopUtils.getContext();
                            if (context == null) {
                                TBSdkLog.e(TAG, "context can't be null.reflect context is still null.");
                                Mtop mtopInstance = Mtop.instance(Mtop.Id.INNER, (Context) null);
                                if (mtopInstance.getMtopConfig().context == null) {
                                    TBSdkLog.e(TAG, "context can't be null.wait INNER mtopInstance init.");
                                    mtopInstance.checkMtopSDKInit();
                                }
                                context = mtopInstance.getMtopConfig().context;
                                if (context == null) {
                                    TBSdkLog.e(TAG, "context can't be null.wait INNER mtopInstance init finish,context is still null");
                                    DefaultLoginImpl defaultLoginImpl = instance;
                                    return defaultLoginImpl;
                                }
                                TBSdkLog.e(TAG, "context can't be null.wait INNER mtopInstance init finish.context=" + context);
                            }
                        } catch (Exception e) {
                            TBSdkLog.e(TAG, "get DefaultLoginImpl instance error", (Throwable) e);
                        }
                    }
                    mContext = context;
                    instance = new DefaultLoginImpl();
                }
            }
        }
        return instance;
    }

    private DefaultLoginImpl() throws ClassNotFoundException, NoSuchMethodException {
        try {
            this.loginCls = Class.forName("com.taobao.login4android.Login");
        } catch (ClassNotFoundException e) {
            this.loginCls = Class.forName("com.taobao.login4android.api.Login");
        }
        this.loginMethod = this.loginCls.getDeclaredMethod("login", new Class[]{Boolean.TYPE, Bundle.class});
        this.checkSessionValidMethod = this.loginCls.getDeclaredMethod("checkSessionValid", new Class[0]);
        this.getSidMethod = this.loginCls.getDeclaredMethod("getSid", new Class[0]);
        this.getUserIdMethod = this.loginCls.getDeclaredMethod("getUserId", new Class[0]);
        this.getNickMethod = this.loginCls.getDeclaredMethod("getNick", new Class[0]);
        this.loginStatusCls = Class.forName("com.taobao.login4android.constants.LoginStatus");
        this.isLoginingMethod = this.loginStatusCls.getDeclaredMethod("isLogining", new Class[0]);
        this.loginBroadcastHelperCls = Class.forName("com.taobao.login4android.broadcast.LoginBroadcastHelper");
        this.registerReceiverMethod = this.loginBroadcastHelperCls.getMethod("registerLoginReceiver", new Class[]{Context.class, BroadcastReceiver.class});
        registerReceiver();
        TBSdkLog.i(TAG, "register login event receiver");
    }

    private void registerReceiver() {
        if (this.receiver != null) {
            return;
        }
        if (mContext == null) {
            TBSdkLog.e(TAG, "Context is null, register receiver fail.");
            return;
        }
        synchronized (DefaultLoginImpl.class) {
            if (this.receiver == null) {
                this.receiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        if (intent != null) {
                            String action = intent.getAction();
                            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                                TBSdkLog.i(DefaultLoginImpl.TAG, "Login Broadcast Received. action=" + action);
                            }
                            if ("NOTIFY_LOGIN_SUCCESS".equals(action)) {
                                LoginHandler.instance().onLoginSuccess();
                            } else if ("NOTIFY_LOGIN_FAILED".equals(action)) {
                                LoginHandler.instance().onLoginFail();
                            } else if ("NOTIFY_LOGIN_CANCEL".equals(action)) {
                                LoginHandler.instance().onLoginCancel();
                            }
                        }
                    }
                };
                invokeMethod(this.registerReceiverMethod, mContext, this.receiver);
            }
        }
    }

    public void setSessionInvalid(Object arg) {
        if (arg instanceof MtopResponse) {
            threadLocal.set(new SessionInvalidEvent((MtopResponse) arg, (String) invokeMethod(this.getNickMethod, new Object[0])));
        } else if (arg instanceof MtopRequest) {
            threadLocal.set(new SessionInvalidEvent((MtopRequest) arg));
        }
    }

    private <T> T invokeMethod(Method method, Object... args) {
        if (method != null) {
            try {
                return method.invoke(this.loginCls, args);
            } catch (Exception e) {
                TBSdkLog.e(TAG, "invokeMethod error", (Throwable) e);
            }
        }
        return null;
    }

    public void login(onLoginListener listener, boolean bShowLoginUI) {
        TBSdkLog.i(TAG, "call login");
        Bundle bundle = null;
        SessionInvalidEvent sessionInvalidEvent = threadLocal.get();
        if (sessionInvalidEvent != null) {
            try {
                Bundle bundle2 = new Bundle();
                try {
                    String apiRefer = sessionInvalidEvent.toJSONString();
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, "apiRefer=" + apiRefer);
                    }
                    bundle2.putString(MTOP_API_REFERENCE, apiRefer);
                    commitSessionInvalidEvent(sessionInvalidEvent);
                    threadLocal.remove();
                    bundle = bundle2;
                } catch (Exception e) {
                    bundle = bundle2;
                    threadLocal.remove();
                    registerReceiver();
                    invokeMethod(this.loginMethod, Boolean.valueOf(bShowLoginUI), bundle);
                } catch (Throwable th) {
                    th = th;
                    Bundle bundle3 = bundle2;
                    threadLocal.remove();
                    throw th;
                }
            } catch (Exception e2) {
                threadLocal.remove();
                registerReceiver();
                invokeMethod(this.loginMethod, Boolean.valueOf(bShowLoginUI), bundle);
            } catch (Throwable th2) {
                th = th2;
                threadLocal.remove();
                throw th;
            }
        }
        registerReceiver();
        invokeMethod(this.loginMethod, Boolean.valueOf(bShowLoginUI), bundle);
    }

    public boolean isSessionValid() {
        Boolean ret = (Boolean) invokeMethod(this.checkSessionValidMethod, new Object[0]);
        if (ret != null) {
            return ret.booleanValue();
        }
        return false;
    }

    public boolean isLogining() {
        Boolean ret = (Boolean) invokeMethod(this.isLoginingMethod, new Object[0]);
        if (ret != null) {
            return ret.booleanValue();
        }
        return false;
    }

    public LoginContext getLoginContext() {
        this.loginContext.sid = (String) invokeMethod(this.getSidMethod, new Object[0]);
        this.loginContext.userId = (String) invokeMethod(this.getUserIdMethod, new Object[0]);
        this.loginContext.nickname = (String) invokeMethod(this.getNickMethod, new Object[0]);
        return this.loginContext;
    }

    private void commitSessionInvalidEvent(SessionInvalidEvent event) {
        IUploadStats uploadStats = Mtop.instance(mContext).getMtopConfig().uploadStats;
        if (uploadStats != null) {
            if (isRegistered.compareAndSet(false, true)) {
                Set<String> dimSet = new HashSet<>();
                dimSet.add("long_nick");
                dimSet.add("apiName");
                dimSet.add("apiV");
                dimSet.add("msgCode");
                dimSet.add("S_STATUS");
                dimSet.add("processName");
                dimSet.add("appBackGround");
                if (uploadStats != null) {
                    uploadStats.onRegister(STATS_MODULE_MTOPRB, STATS_MONITOR_POINT_SESSION_INVALID, dimSet, (Set<String>) null, false);
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(TAG, "onRegister called. module=mtoprb,monitorPoint=SessionInvalid");
                }
            }
            Map<String, String> dimMap = new HashMap<>();
            dimMap.put("long_nick", event.long_nick);
            dimMap.put("apiName", event.apiName);
            dimMap.put("apiV", event.v);
            dimMap.put("msgCode", event.msgCode);
            dimMap.put("S_STATUS", event.S_STATUS);
            dimMap.put("processName", event.processName);
            dimMap.put("appBackGround", event.appBackGround ? "1" : "0");
            if (uploadStats != null) {
                uploadStats.onCommit(STATS_MODULE_MTOPRB, STATS_MONITOR_POINT_SESSION_INVALID, dimMap, (Map<String, Double>) null);
            }
        }
    }

    private static class SessionInvalidEvent {
        private static final String HEADER_KEY = "S";
        public String S_STATUS;
        public String apiName;
        public boolean appBackGround;
        public String eventName;
        public String long_nick;
        public String msgCode;
        public String processName;
        public String v;

        public SessionInvalidEvent(MtopResponse mtopResponse, String long_nick2) {
            this.appBackGround = false;
            this.eventName = "SESSION_INVALID";
            this.long_nick = long_nick2;
            this.apiName = mtopResponse.getApi();
            this.v = mtopResponse.getV();
            this.msgCode = mtopResponse.getRetCode();
            this.S_STATUS = HeaderHandlerUtil.getSingleHeaderFieldByKey(mtopResponse.getHeaderFields(), HEADER_KEY);
            this.processName = MtopUtils.getCurrentProcessName(DefaultLoginImpl.mContext);
            this.appBackGround = XState.isAppBackground();
        }

        public SessionInvalidEvent(MtopRequest mtopRequest) {
            this.appBackGround = false;
            this.apiName = mtopRequest.getApiName();
            this.v = mtopRequest.getVersion();
            this.processName = MtopUtils.getCurrentProcessName(DefaultLoginImpl.mContext);
            this.appBackGround = XState.isAppBackground();
        }

        public String toJSONString() {
            return JSON.toJSONString(this);
        }
    }
}
