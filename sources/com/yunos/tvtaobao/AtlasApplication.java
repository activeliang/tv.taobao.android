package com.yunos.tvtaobao;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.os.Process;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.runtime.ActivityTaskMgr;
import android.taobao.atlas.runtime.ClassNotFoundInterceptorCallback;
import android.text.TextUtils;
import android.widget.Toast;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.bftv.fui.tell.Tell;
import com.bftv.fui.tell.TellManager;
import com.taobao.atlas.dexmerge.MergeConstants;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.tvtao.user.dclib.ZPDevice;
import com.tvtao.user.dclib.impl.ZPDeviceImpl;
import com.tvtaobao.android.runtime.RtBaseEnv;
import com.tvtaobao.android.ui3.UI3Logger;
import com.tvtaobao.voicesdk.services.BftvASRService;
import com.tvtaobao.voicesdk.utils.DialogManager;
import com.ut.device.UTDevice;
import com.yunos.CloudUUIDWrapper;
import com.yunos.RunMode;
import com.yunos.tv.blitz.account.AccountUtils;
import com.yunos.tv.blitz.account.BzDebugLog;
import com.yunos.tv.blitz.account.BzJsCallImpAccountListener;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.global.BzAppContext;
import com.yunos.tv.blitz.global.BzAppMain;
import com.yunos.tv.blitz.global.BzAppParams;
import com.yunos.tv.blitz.global.BzApplication;
import com.yunos.tv.blitz.global.BzEnvEnum;
import com.yunos.tv.core.AppInitializer;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.aqm.ActivityQueueManager;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.SystemConfig;
import com.yunos.tv.core.disaster_tolerance.DisasterTolerance;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tv.core.util.TvtaoExtParamsImp;
import com.yunos.tvtaobao.biz.broadcast.SsotokenLoginReceiver;
import com.yunos.tvtaobao.biz.net.network.NetworkManager;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.core.ServiceCode;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.biz.util.StringUtil;
import com.yunos.tvtaobao.blitz.BzAccountListener;
import com.yunos.tvtaobao.blitz.TaobaoBaseBzJsCallBaseListener;
import com.yunos.tvtaobao.blitz.TaobaoBzMiscListener;
import com.yunos.tvtaobao.blitz.TaobaoBzPageStatusListener;
import com.yunos.tvtaobao.blitz.TaobaoUIBzJsCallUIListener;
import com.yunos.tvtaobao.payment.BuildConfig;
import com.yunos.tvtaobao.payment.request.ScanBindRequest;
import com.yunos.tvtaobao.payment.utils.ChannelUtils;
import com.yunos.tvtaobao.payment.utils.TvTaoUtils;
import com.yunos.tvtaobao.payment.utils.TvtaoExtParamsUtil;
import com.yunos.tvtaobao.receiver.UpdateTimeReceiver;
import com.yunos.tvtaobao.request.CheckLogStatusRequest;
import com.yunos.tvtaobao.request.DeviceChannelBuilder;
import com.zhiping.dev.android.logcat.ZpLogCat;
import com.zhiping.dev.android.logcat.config.IExtCfg;
import com.zhiping.dev.android.logcat.config.IFileNameCfg;
import com.zhiping.dev.android.logcat.config.IOssClientCfg;
import com.zhiping.dev.android.logcat.config.IOssStorageCfg;
import com.zhiping.dev.android.logcat.config.ISaveCfg;
import com.zhiping.dev.android.logger.ZpLogger;
import com.zhiping.dev.android.logger.ZpLoggerConfig;
import com.zhiping.dev.android.oss.IClient;
import com.zhiping.dev.android.oss.ZpOssClient;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.xstate.util.PhoneInfo;
import org.json.JSONObject;
import org.osgi.framework.BundleException;

public class AtlasApplication extends CoreApplication {
    public static final String POWER_SERVER_ID = "powermsg";
    /* access modifiers changed from: private */
    public static final String TAG = AtlasApplication.class.getSimpleName();
    public static final String TVTAOBAO_SERVER_ID = "tvtaobao";
    private static final Map<String, String> accsServices = new HashMap();
    public static volatile boolean mForceBindUser = false;
    RtBaseEnv.MsgEar msgEar = new RtBaseEnv.MsgEar() {
        public void onMsg(RtBaseEnv.Msg msg) {
            if ("makeCrash".equals(msg.name)) {
                throw new RuntimeException("" + msg.data);
            } else if ("test_ZpLoggerConfig_level".equals(msg.name)) {
                ZpLoggerConfig.Level l = null;
                try {
                    l = ZpLoggerConfig.Level.valueOf("" + msg.data);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                if (l != null) {
                    SharePreferences.put("ZpLoggerConfig_level", l.getVal());
                    ZpLoggerConfig.setLevelCfg(l);
                    return;
                }
                SharePreferences.rmv("ZpLoggerConfig_level");
                ZpLoggerConfig.setLevelCfg(ZpLoggerConfig.Level.v);
            }
        }
    };

    public AtlasApplication() {
        RtEnv.listen(this.msgEar);
    }

    static {
        accsServices.put(POWER_SERVER_ID, "com.taobao.tao.messagekit.base.AccsReceiverService");
        accsServices.put("tvtaobao", "com.yunos.tvtaobao.tvtaomsg.TvTaobaoReceviceService");
        accsServices.put("orange", "com.taobao.orange.accssupport.OrangeAccsService");
        accsServices.put("agooSend", "org.android.agoo.accs.AgooService");
        accsServices.put("agooAck", "org.android.agoo.accs.AgooService");
        accsServices.put("agooTokenReport", "org.android.agoo.accs.AgooService");
    }

    public static String getProcessName(Context context) {
        int pid = Process.myPid();
        List<ActivityManager.RunningAppProcessInfo> runningApps = ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public void onCreate() {
        onCreateTime = System.currentTimeMillis();
        mApplication = this;
        Config.getInstance();
        if (!Config.isDebug()) {
            ZpLoggerConfig.setLevelCfg(ZpLoggerConfig.Level.shutdown);
        }
        int zpLoggerCfgVal = SharePreferences.getInt("ZpLoggerConfig_level", -1).intValue();
        if (zpLoggerCfgVal != -1) {
            ZpLoggerConfig.setLevelCfg(ZpLoggerConfig.Level.from(zpLoggerCfgVal));
        }
        ZpLogger.d(TAG, ".onCreate() zpLoggerCfgVal:" + zpLoggerCfgVal);
        if (!isACCSChannel()) {
            super.onCreate();
            atlasInstance();
            AppInitializer.doAppInit(Config.getTTid());
            try {
                ZpOssClient.getInstance().getAliyunOssClient(new IClient.IConfig() {
                    public Application getApplication() {
                        return CoreApplication.getApplication();
                    }

                    public boolean getEnableOssLog() {
                        return false;
                    }

                    public boolean isNeedListBucket() {
                        return true;
                    }

                    public String getAccessKey() {
                        return "LTAIPLE4RUuJg1vz";
                    }

                    public String getAccessSecret() {
                        return "SoPm81mxnKocGFmhXW6kAzCBrAqzui";
                    }

                    public String getEndPoint() {
                        return OSSConstants.DEFAULT_OSS_ENDPOINT;
                    }

                    public Map getExtCfg() {
                        return null;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            ZpLogCat.getInstance(getApplication()).doConfig((ISaveCfg) null, new IOssClientCfg() {
                public IClient getOssClient() {
                    return ZpOssClient.getInstance().getAliyunOssClient((IClient.IConfig) null);
                }
            }, (IOssStorageCfg) null, (IFileNameCfg) null, new IExtCfg() {
                String appVersionCode;
                String appVersionName;

                {
                    PackageManager pm = CoreApplication.getApplication().getPackageManager();
                    if (pm != null) {
                        try {
                            PackageInfo info = pm.getPackageInfo(CoreApplication.getApplication().getPackageName(), 16384);
                            this.appVersionCode = "" + info.versionCode;
                            this.appVersionName = "" + info.versionName;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                public String getDeviceId() {
                    return CloudUUIDWrapper.getCloudUUID();
                }

                public String getUserId() {
                    return User.getUserId();
                }

                public String getUserName() {
                    return User.getNick();
                }

                public String getAppVersionName() {
                    return this.appVersionName;
                }

                public String getAppVersionCode() {
                    return this.appVersionCode;
                }

                public String getAppChannel() {
                    return Config.getChannel();
                }

                public String getAppBuildSeq() {
                    return null;
                }
            });
            checkLogSettings();
            initASR();
            initBlitzSdk();
            initReceiver();
            initExitBroadcast();
            NetworkManager.instance().init(this);
            getLoginHelper(getApplication()).registerLoginListener(getApplication());
            initDevice();
            GlobalConfigInfo.getInstance().getTvtaobaoSwitch(this);
            initZPDevice(this);
            TvtaoExtParamsUtil.setExtParamsInterface(new TvtaoExtParamsImp());
            DisasterTolerance.getInstance().catchLooperException(Looper.getMainLooper(), new DisasterTolerance.ExceptionFilter() {
                public boolean filterException(Throwable throwable) {
                    if (throwable != null) {
                        if (throwable instanceof NullPointerException) {
                            DisasterTolerance.getInstance().catchNullPointerException(throwable, new String[0]);
                            return true;
                        } else if (throwable instanceof ClassCastException) {
                            DisasterTolerance.getInstance().catchClassCastException(throwable, new String[0]);
                            return true;
                        }
                    }
                    return false;
                }
            });
            UI3Logger.setLogger(new UI3Logger.LoggerProxy() {
                public void i(String s, String s1) {
                    ZpLogger.i(s, s1);
                }

                public void e(String s, String s1) {
                    ZpLogger.e(s, s1);
                }

                public void d(String s, String s1) {
                    ZpLogger.d(s, s1);
                }
            });
            ZpLogger.d(TAG, ".onCreate() done!");
        }
    }

    private void initZPDevice(Context context) {
        DeviceUtil.initMacAddress(this);
        Map<String, String> extParams = new HashMap<>();
        try {
            extParams.put("umToken", TvTaoUtils.getUmtoken(context));
            extParams.put("appkey", Config.getChannel());
            extParams.put("versionName", AppInfo.getAppVersionName());
            extParams.put("utdid", UTDevice.getUtdid(context));
            extParams.put("wua", Config.getWua(context));
            extParams.put("isSimulator", String.valueOf(Config.isSimulator(context)));
            extParams.put("userAgent", Config.getAndroidSystem(context));
            extParams.put("uuid", CloudUUIDWrapper.getCloudUUID());
            extParams.put("mac", DeviceUtil.getStbID());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("params", extParams.toString());
            jsonObject.put("extParams", extParams.toString());
            ZPDevice.init(context, extParams, ZPDeviceImpl.SDKScene.INNER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initASR() {
        if (RunMode.isYunos() || Config.getChannel().equals("2016102417")) {
            Intent intent = new Intent();
            intent.setPackage(getApplication().getPackageName());
            intent.setAction("com.yunos.tvtaobao.asr.startASRService");
            startService(intent);
        } else if (Config.getChannel().equals("2016010811")) {
            ZpLogger.e(TAG, TAG + ".initBaofengASR registed all asr");
            Tell tell = new Tell();
            tell.pck = AppInfo.getPackageName();
            tell.tellType = 64;
            tell.key = "MFwwDQYJKoZIhvcNAQE";
            TellManager.getInstance().tell(CoreApplication.getApplication(), tell);
        }
    }

    private void checkLogSettings() {
        BusinessRequest.getBusinessRequest().baseRequest((BaseMtopRequest) new CheckLogStatusRequest(), new RequestListener<Boolean>() {
            public void onRequestDone(Boolean data, int resultCode, String msg) {
                if (resultCode == ServiceCode.SERVICE_OK.getCode()) {
                    if (data == null || !data.booleanValue()) {
                        ZpLogCat.getInstance((Application) null).setTurnOnState(false);
                        if (!Config.isDebug()) {
                            SharePreferences.rmv("ZpLoggerConfig_level");
                            ZpLoggerConfig.setLevelCfg(ZpLoggerConfig.Level.shutdown);
                        }
                    } else {
                        ZpLogCat.getInstance((Application) null).setTurnOnState(true);
                        if (!Config.isDebug()) {
                            SharePreferences.put("ZpLoggerConfig_level", ZpLoggerConfig.Level.v.getVal());
                            ZpLoggerConfig.setLevelCfg(ZpLoggerConfig.Level.v);
                        }
                    }
                    if (ZpLogCat.getInstance((Application) null).getTurnOnState()) {
                        ZpLogCat.getInstance((Application) null).tipTurnOnMsg(true, "\n\n按下【\"菜单键\"】开始记录");
                        return;
                    }
                    return;
                }
                ZpLogCat.getInstance((Application) null).setTurnOnState(false);
            }
        }, false);
    }

    private void initDevice() {
        if (RunMode.needInitDevice()) {
            String appkey = SharePreferences.getString("device_appkey", "");
            String brandName = SharePreferences.getString("device_brandname", "");
            ScanBindRequest.setAppKey(appkey);
            if (TextUtils.isEmpty(appkey) && TextUtils.isEmpty(brandName)) {
                new DeviceChannelBuilder(getApplication()).onRequestData();
            }
        } else if (ChannelUtils.isThisTag(ChannelUtils.HY)) {
            String subkey = SharePreferences.getString("hy_device_subkey", "");
            ZpLogger.e(TAG, "subkey=====" + subkey + "");
            if (StringUtil.isEmpty(subkey) || BuildConfig.CHANNELID.equals(subkey)) {
                try {
                    Cursor cursorAppkey = getContentResolver().query(Uri.parse("content://" + "com.cmri.hjmall" + ".tvtaobao.channel.provider/tvtaochannel/25954021"), (String[]) null, (String) null, (String[]) null, (String) null);
                    if (cursorAppkey == null || cursorAppkey.getCount() <= 0) {
                        subkey = BuildConfig.CHANNELID;
                        SharePreferences.put("hy_device_appkey", BuildConfig.CHANNELID);
                        SharePreferences.put("hy_device_subkey", subkey);
                        ZpLogger.e(TAG, "appkey:" + BuildConfig.CHANNELID + ",subkey = " + subkey);
                        ZpLogger.e(TAG, "appkeySP=====" + BuildConfig.CHANNELID + "");
                        ZpLogger.e(TAG, "subkeySP=====" + subkey + "");
                    } else {
                        while (cursorAppkey.moveToNext()) {
                            subkey = cursorAppkey.getString(cursorAppkey.getColumnIndex("subkey"));
                            SharePreferences.put("hy_device_appkey", BuildConfig.CHANNELID);
                            SharePreferences.put("hy_device_subkey", subkey);
                            ZpLogger.e(TAG, "appkey:" + BuildConfig.CHANNELID + ",subkey = " + subkey);
                        }
                        cursorAppkey.close();
                        ZpLogger.e(TAG, "appkeySP=====" + BuildConfig.CHANNELID + "");
                        ZpLogger.e(TAG, "subkeySP=====" + subkey + "");
                    }
                } catch (Exception e) {
                    subkey = BuildConfig.CHANNELID;
                    SharePreferences.put("hy_device_appkey", BuildConfig.CHANNELID);
                    SharePreferences.put("hy_device_subkey", subkey);
                    ZpLogger.e(TAG, "appkey:" + BuildConfig.CHANNELID + ",subkey = " + subkey);
                    e.printStackTrace();
                }
            }
            RtEnv.set("APPKEY", BuildConfig.CHANNELID);
            if (StringUtil.isEmpty(subkey)) {
                subkey = BuildConfig.CHANNELID;
            }
            RtEnv.set(RtEnv.SUBKEY, subkey);
        }
    }

    private void initBlitzSdk() {
        BzAppMain.mMtopInstance = AppInitializer.getMtopInstance();
        setJsCallBaseListener(new TaobaoBaseBzJsCallBaseListener());
        setJsCallUIListener(new TaobaoUIBzJsCallUIListener());
        setMiscListener(new TaobaoBzMiscListener());
        setPageStatusListener(new TaobaoBzPageStatusListener());
        setAppGlobalListener(new BzAccountListener());
        setJsCallAccountListener(new BzJsCallImpAccountListener() {
            public String onAccountGetUserInfo(Context context, String params, int callback) {
                final BzResult result = new BzResult();
                final BzApplication app = (BzApplication) BzAppConfig.context.getContext();
                int tokenType = getTokeType(params);
                ZpLogger.d(AtlasApplication.TAG, "tyidVersion=" + AccountUtils.getVersioncode(BzAppConfig.context.getContext(), "com.aliyun.ams.tyid") + " ,type = " + tokenType);
                if (tokenType == 1) {
                    if (BzApplication.getLoginHelper(BzAppConfig.context.getContext()).isLogin()) {
                        LoginHelper loginHelper = BzApplication.getLoginHelper(BzAppConfig.context.getContext());
                        result.addData("userNick", loginHelper.getNick());
                        result.addData("sid", loginHelper.getSessionId());
                        BzDebugLog.d(AtlasApplication.TAG, "usernick:" + loginHelper.getNick() + "sid:" + loginHelper.getSessionId());
                        result.setSuccess();
                        app.replyCallBack(callback, true, result.toJsonString());
                        result.setSuccess();
                        return result.toJsonString();
                    }
                    if (BzApplication.getLoginHelper(BzAppConfig.context.getContext()).getRegistedContext() == null) {
                        BzApplication.getLoginHelper(BzAppConfig.context.getContext()).registerLoginListener(BzAppConfig.context.getContext());
                    }
                    final LoginHelper helper = BzApplication.getLoginHelper(BzAppConfig.context.getContext());
                    final int i = callback;
                    helper.addSyncLoginListener(new LoginHelper.SyncLoginListener() {
                        public void onLogin(boolean isSuccess) {
                            BzApplication.getLoginHelper(BzAppConfig.context.getContext()).removeSyncLoginListener(this);
                            if (isSuccess) {
                                result.addData("userNick", helper.getNick());
                                result.addData("sid", helper.getSessionId());
                                BzDebugLog.d(AtlasApplication.TAG, "usernick:" + helper.getNick() + "sid:" + helper.getSessionId());
                                result.setSuccess();
                                app.replyCallBack(i, true, result.toJsonString());
                                return;
                            }
                            app.replyCallBack(i, false, BzResult.RET_NOT_LOGIN.toJsonString());
                        }
                    });
                    BzApplication.getLoginHelper(BzAppConfig.context.getContext()).login(BzAppConfig.context.getContext());
                    result.setSuccess();
                    return result.toJsonString();
                } else if (tokenType != 0) {
                    app.replyCallBack(callback, false, BzResult.RET_NOT_LOGIN.toJsonString());
                    return "";
                } else if (BzApplication.getLoginHelper(BzAppConfig.context.getContext()).isLogin()) {
                    LoginHelper loginHelper2 = BzApplication.getLoginHelper(BzAppConfig.context.getContext());
                    result.addData("userNick", loginHelper2.getNick());
                    result.addData("sid", loginHelper2.getSessionId());
                    BzDebugLog.d(AtlasApplication.TAG, "usernick:" + loginHelper2.getNick() + "sid:" + loginHelper2.getSessionId());
                    result.setSuccess();
                    app.replyCallBack(callback, true, result.toJsonString());
                    result.setSuccess();
                    return result.toJsonString();
                } else {
                    if (BzApplication.getLoginHelper(BzAppConfig.context.getContext()).getRegistedContext() == null) {
                        BzApplication.getLoginHelper(BzAppConfig.context.getContext()).registerLoginListener(BzAppConfig.context.getContext());
                    }
                    final LoginHelper helper2 = BzApplication.getLoginHelper(BzAppConfig.context.getContext());
                    final int i2 = callback;
                    helper2.addSyncLoginListener(new LoginHelper.SyncLoginListener() {
                        public void onLogin(boolean isSuccess) {
                            BzApplication.getLoginHelper(BzAppConfig.context.getContext()).removeSyncLoginListener(this);
                            if (isSuccess) {
                                result.addData("userNick", helper2.getNick());
                                result.addData("sid", helper2.getSessionId());
                                BzDebugLog.d(AtlasApplication.TAG, "usernick:" + helper2.getNick() + "sid:" + helper2.getSessionId());
                                result.setSuccess();
                                app.replyCallBack(i2, true, result.toJsonString());
                                return;
                            }
                            app.replyCallBack(i2, false, BzResult.RET_NOT_LOGIN.toJsonString());
                        }
                    });
                    BzApplication.getLoginHelper(BzAppConfig.context.getContext()).login(BzAppConfig.context.getContext());
                    result.setSuccess();
                    return result.toJsonString();
                }
            }
        });
        BzDebugLog.setLogSwitcher(Config.isDebug());
        BzAppContext.setEnvMode(com.yunos.tv.core.config.RunMode.DAILY.equals(Config.getRunMode()) ? BzEnvEnum.DAILY : BzEnvEnum.ONLINE);
        BzAppParams params = new BzAppParams();
        params.imei = PhoneInfo.getImei(this);
        params.imsi = PhoneInfo.getImsi(this);
        params.appKey = Config.getAppKey();
        params.ttid = Config.getTTid();
        params.appTag = "TVTB";
        params.appVersion = SystemConfig.APP_VERSION;
        params.uuid = CloudUUIDWrapper.getCloudUUID();
        BzAppContext.setBzAppParams(params);
    }

    private void atlasInstance() {
        Atlas.getInstance().setClassNotFoundInterceptorCallback(new ClassNotFoundInterceptorCallback() {
            public Intent returnIntent(Intent intent) {
                String bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(intent.getComponent().getClassName());
                if (!TextUtils.isEmpty(bundleName) && !AtlasBundleInfoManager.instance().isInternalBundle(bundleName)) {
                    Activity activity = ActivityTaskMgr.getInstance().peekTopActivity();
                    File remoteBundleFile = new File(activity.getExternalCacheDir(), "lib" + bundleName.replace(".", "_") + MergeConstants.SO_SUFFIX);
                    if (remoteBundleFile.exists()) {
                        String path = remoteBundleFile.getAbsolutePath();
                        try {
                            Atlas.getInstance().installBundle(activity.getPackageManager().getPackageArchiveInfo(path, 0).packageName, new File(path));
                        } catch (BundleException e) {
                            Toast.makeText(activity, " 远程bundle 安装失败，" + e.getMessage(), 1).show();
                            e.printStackTrace();
                        }
                        activity.startActivities(new Intent[]{intent});
                    } else {
                        Toast.makeText(activity, " 远程bundle不存在，请确定 : " + remoteBundleFile.getAbsolutePath(), 1).show();
                    }
                }
                return intent;
            }
        });
    }

    private void initReceiver() {
        UpdateTimeReceiver receiver = new UpdateTimeReceiver();
        IntentFilter dateChange = new IntentFilter("android.intent.action.DATE_CHANGED");
        IntentFilter timeSet = new IntentFilter("android.intent.action.TIME_SET");
        IntentFilter timezoneChange = new IntentFilter("android.intent.action.TIMEZONE_CHANGED");
        registerReceiver(receiver, dateChange);
        registerReceiver(receiver, timeSet);
        registerReceiver(receiver, timezoneChange);
        registerReceiver(new SsotokenLoginReceiver(), new IntentFilter("com.tvtaobao.common.login"));
    }

    /* access modifiers changed from: protected */
    public boolean isACCSChannel() {
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
            if (("com.yunos.tvtaobao:channel".equals(appProcess.processName) || "rca.rc.tvtaobao:channel".equals(appProcess.processName)) && appProcess.pid == myPid) {
                ZpLogger.i(TAG, "processName=" + appProcess.processName);
                return true;
            }
        }
        return false;
    }

    public void clear() {
        ZpLogger.d(TAG, TAG + "------clear");
        if (RunMode.isYunos()) {
            Intent intent = new Intent();
            intent.setPackage(getApplication().getPackageName());
            intent.setAction("com.yunos.tvtaobao.asr.startASRService");
            stopService(intent);
        } else if (Config.getChannel().equals("2016010811")) {
            Intent intent2 = new Intent();
            intent2.setPackage(getApplication().getPackageName());
            intent2.setAction("intent.action.user.rca.rc.tvtaobao");
            stopService(intent2);
            Intent intent1 = new Intent();
            intent1.setClass(this, BftvASRService.class);
            stopService(intent1);
            ZpLogger.i(TAG, "关闭BftvASRService");
        }
        try {
            DialogManager.getManager().dismissAllDialog();
            ActivityQueueManager.getInstance().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.clear();
        DisasterTolerance.destroy();
    }

    private void initExitBroadcast() {
        ExitReceiver exitReceiver = new ExitReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.yunos.tvtaobao.exit.application");
        registerReceiver(exitReceiver, intentFilter);
    }

    private class ExitReceiver extends BroadcastReceiver {
        private ExitReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ZpLogger.d(AtlasApplication.TAG, "ExitReceiver.onReceive(" + intent + ")");
            if ("com.yunos.tvtaobao.exit.application".equals(intent.getAction())) {
                AtlasApplication.this.clear();
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        }
    }
}
