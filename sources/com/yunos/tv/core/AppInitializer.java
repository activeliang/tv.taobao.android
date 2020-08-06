package com.yunos.tv.core;

import android.accounts.AuthenticatorException;
import android.content.Context;
import android.text.TextUtils;
import anet.channel.util.ALog;
import anetwork.channel.util.RequestConstant;
import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.util.StringUtil;
import com.ali.auth.third.offline.login.LoginConstants;
import com.ali.auth.third.offline.login.LoginService;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.callback.InitResultCallback;
import com.ali.user.open.core.config.Environment;
import com.ali.user.open.jsbridge.UccJsBridge;
import com.ali.user.sso.SsoLogin;
import com.ali.user.sso.SsoManager;
import com.ali.user.sso.SsoResultListener;
import com.ali.user.sso.UserInfo;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import com.alibaba.motu.crashreporter.MotuCrashReporter;
import com.alibaba.motu.crashreporter.ReporterConfigure;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.taobao.alimama.api.AdSDK;
import com.ut.device.UTDevice;
import com.ut.mini.IUTApplication;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTSecuritySDKRequestAuthentication;
import com.ut.mini.crashhandler.IUTCrashCaughtListner;
import com.yunos.CloudUUIDWrapper;
import com.yunos.alitvcompliance.TVCompliance;
import com.yunos.alitvcompliance.utils.UTHelper;
import com.yunos.tv.core.common.DeviceJudge;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.RunMode;
import com.yunos.tv.core.config.SystemConfig;
import com.yunos.tv.core.listener.MotuCrashCaughtListener;
import com.yunos.tv.core.util.MonitorUtil;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.payment.CustomLoginFragment;
import com.yunos.tvtaobao.payment.account.AccountUtil;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.yunos.tvtaobao.payment.utils.TvTaoUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import com.zhiping.tvtao.payment.AlipayManager;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.global.SDKUtils;
import mtopsdk.mtop.intf.Mtop;

public class AppInitializer {
    /* access modifiers changed from: private */
    public static boolean FLAG_MemberSDK_Fully_Done = false;
    /* access modifiers changed from: private */
    public static final String TAG = AppInitializer.class.getSimpleName();
    private static Mtop mtopInstance;

    public static Mtop getMtopInstance() {
        if (mtopInstance == null) {
            synchronized (AppInitializer.class) {
                if (mtopInstance == null) {
                    mtopInstance = initMtopSDK();
                }
            }
        }
        return mtopInstance;
    }

    public static void doAppInit(String channel) {
        initDeviceAndSystemInfo();
        ZpLogger.i(TAG, "doAppInit initDeviceAndSystemInfo");
        initSecurityGuardSDK();
        ZpLogger.i(TAG, "doAppInit initSecurityGuardSDK");
        initComplianceSDK();
        ZpLogger.i(TAG, "doAppInit initComplianceSDK");
        initUTAnalyticsSDK(channel);
        ZpLogger.i(TAG, "doAppInit initUTAnalyticsSDK");
        getMtopInstance();
        ZpLogger.i(TAG, "doAppInit getMtopInstance");
        initUUID();
        ZpLogger.i(TAG, "doAppInit initUUID");
        initMemberSDK();
        ZpLogger.i(TAG, "doAppInit initMemberSDK");
        checkMemberSDKAndMtopState();
        ZpLogger.i(TAG, "doAppInit checkMemberSDKAndMtopState");
        initCrashReporterSDK();
        ZpLogger.i(TAG, "doAppInit initCrashReporterSDK");
        initMonitorPointsSDK();
        ZpLogger.i(TAG, "doAppInit initMonitorPointsSDK");
        AdSDK.initSDK(CoreApplication.getApplication());
        ZpLogger.i(TAG, "doAppInit initAdSDK");
        AlipayManager.init(CoreApplication.getApplication(), new AlipayManager.BizInfoProvider() {
            public String getDeviceId() {
                return CloudUUIDWrapper.getCloudUUID();
            }

            public Map<String, String> extraParamsForMtop(String s, String s1) {
                JSONObject jo = new JSONObject();
                jo.put("uuid", (Object) CloudUUIDWrapper.getCloudUUID());
                jo.put("wua", (Object) Config.getWua(CoreApplication.getApplication()));
                jo.put("utdid", (Object) UTDevice.getUtdid(CoreApplication.getApplication()));
                jo.put("umtoken", (Object) TvTaoUtils.getUmtoken(CoreApplication.getApplication()));
                HashMap<String, String> extparam = new HashMap<>();
                extparam.put("extParams", jo.toString());
                return extparam;
            }
        }, false);
        AliMemberSDK.setEnvironment(Environment.ONLINE);
        ConfigManager.setAppKeyIndex(0, 2);
        AliMemberSDK.init(CoreApplication.getApplication(), Site.TAOBAO, new InitResultCallback() {
            public void onSuccess() {
                AliMemberSDK.setMasterSite(Site.TAOBAO);
                UccJsBridge.getInstance().setUccDataProvider();
            }

            public void onFailure(int i, String s) {
                ZpLogger.e("ljyljy", "AliMemberSDK-onFailure");
            }
        });
    }

    public static void initUTAnalyticsSDK(final String channel) {
        UTAnalytics.getInstance().setAppApplicationInstance(CoreApplication.getApplication(), new IUTApplication() {
            public String getUTAppVersion() {
                return SystemConfig.APP_VERSION;
            }

            public String getUTChannel() {
                return channel;
            }

            public IUTRequestAuthentication getUTRequestAuthInstance() {
                return new UTSecuritySDKRequestAuthentication(Config.getAppKey());
            }

            public boolean isUTLogEnable() {
                return true;
            }

            public boolean isAliyunOsSystem() {
                return false;
            }

            public IUTCrashCaughtListner getUTCrashCraughtListener() {
                return null;
            }

            public boolean isUTCrashHandlerDisable() {
                return false;
            }
        });
        UTAnalytics.getInstance().turnOffAutoPageTrack();
    }

    private static Mtop initMtopSDK() {
        TBSdkLog.setTLogEnabled(false);
        if (Config.isDebug()) {
            TBSdkLog.setPrintLog(true);
            TBSdkLog.setLogEnable(TBSdkLog.LogEnable.DebugEnable);
            ALog.setUseTlog(false);
        }
        String ttid = Config.getTTid();
        Mtop mtop = Mtop.instance((Context) CoreApplication.getApplication(), ttid);
        ZpLogger.e(TAG, "[initMtopSdk]" + User.getSessionId());
        if (!TextUtils.isEmpty(User.getSessionId())) {
            mtop.registerSessionInfo(User.getSessionId(), User.getUserId());
        }
        if (Config.getRunMode() == RunMode.PRODUCTION) {
            mtop.switchEnvMode(EnvModeEnum.ONLINE);
        } else if (Config.getRunMode() == RunMode.PREDEPLOY) {
            mtop.switchEnvMode(EnvModeEnum.PREPARE);
        } else {
            mtop.switchEnvMode(EnvModeEnum.TEST);
        }
        SDKUtils.registerTtid(ttid);
        return mtop;
    }

    private static void initMemberSDK() {
        ConfigManager.getInstance();
        if (Config.isDebug()) {
            MemberSDK.turnOnDebug();
        }
        ZpLogger.d(RequestConstant.ENV_TEST, "memberSDK init begin");
        MemberSDK.init(CoreApplication.getApplication(), new com.ali.auth.third.core.callback.InitResultCallback() {
            public void onSuccess() {
                ZpLogger.d(RequestConstant.ENV_TEST, "memberSDK init success");
                if (Config.getRunMode() == RunMode.PRODUCTION) {
                    MemberSDK.setEnvironment(com.ali.auth.third.core.config.Environment.ONLINE);
                } else if (Config.getRunMode() == RunMode.PREDEPLOY) {
                    MemberSDK.setEnvironment(com.ali.auth.third.core.config.Environment.PRE);
                } else {
                    MemberSDK.setEnvironment(com.ali.auth.third.core.config.Environment.TEST);
                }
                if (((Boolean) TvTaoSharedPerference.getSp(CoreApplication.getApplication(), TvTaoSharedPerference.LOGIN23, false)).booleanValue()) {
                    ZpLogger.v(AppInitializer.TAG, AppInitializer.TAG + "==二三方初始化成功");
                    AppInitializer.initSSotoken();
                } else {
                    ZpLogger.v(AppInitializer.TAG, AppInitializer.TAG + "==二三方没有初始化");
                }
                done();
            }

            public void onFailure(int i, String s) {
                ZpLogger.d(RequestConstant.ENV_TEST, "memberSDK init fail " + s);
                try {
                    if (Config.getRunMode() == RunMode.PRODUCTION) {
                        MemberSDK.setEnvironment(com.ali.auth.third.core.config.Environment.ONLINE);
                    } else if (Config.getRunMode() == RunMode.PREDEPLOY) {
                        MemberSDK.setEnvironment(com.ali.auth.third.core.config.Environment.PRE);
                    } else {
                        MemberSDK.setEnvironment(com.ali.auth.third.core.config.Environment.TEST);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                done();
            }

            private void done() {
                boolean unused = AppInitializer.FLAG_MemberSDK_Fully_Done = true;
            }
        });
        ConfigManager.getInstance().setFullyCustomizedLoginFragment(CustomLoginFragment.class);
        Map<String, Object> params = new HashMap<>();
        params.put(LoginConstants.CONFIG, "{\"qrwidth\": 80}");
        ConfigManager.getInstance().setScanParams(params);
    }

    /* access modifiers changed from: private */
    public static void initSSotoken() {
        final SsoLogin ssoLogin = new SsoLogin(CoreApplication.getApplication());
        ZpLogger.e(TAG, "new SsoLogin");
        new Thread(new Runnable() {
            public void run() {
                try {
                    Map<String, String> p = new HashMap<>();
                    p.put("appkey", "23039499");
                    Utils.utCustomHit("", "Request_GetSsoProperties", p);
                    ssoLogin.getSsoTokenWithType(ssoLogin.taobaoAccountType(), new SsoResultListener() {
                        public void onFailed(String s) {
                            ZpLogger.e(AppInitializer.TAG, s == null ? "" : s.toString());
                        }

                        public void onSuccess(UserInfo userInfo) {
                            if (StringUtil.isEmpty((String) TvTaoSharedPerference.getSp(CoreApplication.getApplication(), TvTaoSharedPerference.NICK, "")) || (!StringUtil.isEmpty(userInfo.mNick) && !StringUtil.isEmpty((String) TvTaoSharedPerference.getSp(CoreApplication.getApplication(), TvTaoSharedPerference.NICK, "")) && !TvTaoSharedPerference.getSp(CoreApplication.getApplication(), TvTaoSharedPerference.NICK, "").equals(userInfo.mNick))) {
                                LoginService loginService = (LoginService) MemberSDK.getService(LoginService.class);
                                if (loginService != null) {
                                    loginService.loginBySsoToken(userInfo.mSsoToken, new LoginCallback() {
                                        public void onFailure(int i, String s) {
                                            ZpLogger.e(AppInitializer.TAG, s.toString());
                                        }

                                        public void onSuccess(Session session) {
                                            if (session != null && !TextUtils.isEmpty(session.nick)) {
                                                UTAnalytics.getInstance().updateUserAccount(session.nick, session.userid, (String) null);
                                                ZpLogger.e(AppInitializer.TAG, "UT用户信息传入：session.nick:" + session.nick + "++session.userid:" + session.userid);
                                            }
                                            ZpLogger.e(AppInitializer.TAG, session.toString());
                                            AccountUtil.notifyListener(CoreApplication.getApplication(), AccountUtil.ACTION.LOGIN_ACTION);
                                        }
                                    });
                                }
                                TvTaoSharedPerference.saveSp(CoreApplication.getApplication(), TvTaoSharedPerference.NICK, userInfo.mNick);
                            }
                        }
                    });
                } catch (SsoManager.UnauthorizedAccessException e) {
                    e.printStackTrace();
                } catch (AuthenticatorException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    private static void checkMemberSDKAndMtopState() {
        String sid;
        String uid;
        try {
            long checkTime = System.currentTimeMillis();
            ZpLogger.i(TAG, "[checkMemberSDKAndMtopState]" + null + "," + null);
            while (true) {
                sid = getMtopInstance().getMultiAccountSid((String) null);
                uid = getMtopInstance().getMultiAccountUserId((String) null);
                if (!TextUtils.isEmpty(sid) || !TextUtils.isEmpty(uid) || System.currentTimeMillis() - checkTime > 300 || FLAG_MemberSDK_Fully_Done) {
                    ZpLogger.i(TAG, "[checkMemberSDKAndMtopState]" + sid + "," + uid);
                } else {
                    Thread.yield();
                }
            }
            ZpLogger.i(TAG, "[checkMemberSDKAndMtopState]" + sid + "," + uid);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void initSecurityGuardSDK() {
        ZpLogger.i(TAG, TAG + ".initSecurity() is running!");
        try {
            SecurityGuardManager.getInstance(CoreApplication.getApplication().getBaseContext());
            SecurityGuardManager.getInitializer().initialize(CoreApplication.getApplication());
        } catch (Exception e) {
            e.printStackTrace();
            ZpLogger.i(TAG, TAG + ".initSecurity() is SecException!");
        }
    }

    private static void initCrashReporterSDK() {
        ReporterConfigure reporterConfigure = new ReporterConfigure();
        reporterConfigure.setEnableDebug(Config.isDebug());
        reporterConfigure.setEnableDumpSysLog(true);
        reporterConfigure.setEnableDumpRadioLog(true);
        reporterConfigure.setEnableDumpEventsLog(true);
        reporterConfigure.setEnableCatchANRException(true);
        reporterConfigure.setEnableANRMainThreadOnly(false);
        reporterConfigure.setEnableDumpAllThread(true);
        reporterConfigure.enableDeduplication = false;
        String version = SystemConfig.APP_VERSION;
        ZpLogger.i(TAG, TAG + ".initMotuCrashSDK version = " + version);
        MotuCrashReporter.getInstance().enable(CoreApplication.getApplication(), Config.getAppId(), Config.getAppKey(), version, Config.getChannel(), (String) null, reporterConfigure);
        MotuCrashReporter.getInstance().setUserNick(User.getNick());
        MotuCrashReporter.getInstance().setCrashCaughtListener((IUTCrashCaughtListener) new MotuCrashCaughtListener());
    }

    private static void initComplianceSDK() {
        if (com.yunos.RunMode.needDomainCompliance()) {
            try {
                TVCompliance.init(CoreApplication.getApplication(), Config.isDebug(), (UTHelper.IUTInitializer) null, new UTHelper.IUTCustomEventSender() {
                    public void sendCustomEvent(String s, Map<String, String> map) {
                        UTHitBuilders.UTCustomHitBuilder builder = new UTHitBuilders.UTCustomHitBuilder(s);
                        builder.setProperties(map);
                        UTAnalytics.getInstance().getDefaultTracker().send(builder.build());
                    }
                });
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private static void initMonitorPointsSDK() {
        MonitorUtil.init();
    }

    private static void initUUID() {
        if (!com.yunos.RunMode.isYunos()) {
            CloudUUIDWrapper.init(CoreApplication.getApplication(), true);
            ZpLogger.d(TAG, TAG + ".initUUID uuid = " + CloudUUIDWrapper.getCloudUUID());
            if (TextUtils.isEmpty(CloudUUIDWrapper.getCloudUUID())) {
                new Thread(new Runnable() {
                    public void run() {
                        ZpLogger.d(AppInitializer.TAG, AppInitializer.TAG + ".GenerateUUIDRunnable, genereate uuid");
                        CloudUUIDWrapper.setAndroidOnly(true);
                        CloudUUIDWrapper.generateUUIDAsyn(new CloudUUIDWrapper.IUUIDListener() {
                            public void onCompleted(int error, float time) {
                                ZpLogger.d(AppInitializer.TAG, AppInitializer.TAG + ".GenerateUUIDRunnable onCompleted: error=" + error + " time:" + time + ", uuid = " + CloudUUIDWrapper.getCloudUUID());
                                if (error == 0) {
                                }
                            }
                        }, "TVAppStore", (String) null);
                    }
                }).start();
            }
        }
    }

    private static void initDeviceAndSystemInfo() {
        try {
            DeviceJudge.getInstance(CoreApplication.getApplication());
            ZpLogger.i(TAG, DeviceJudge.getDevicePerformanceString());
            SystemConfig.init(CoreApplication.getApplication());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
