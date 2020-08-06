package com.alibaba.analytics.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.core.config.UTBaseConfMgr;
import com.alibaba.analytics.core.db.DBMgr;
import com.alibaba.analytics.core.device.Device;
import com.alibaba.analytics.core.device.DeviceInfo;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.sync.UploadMgr;
import com.alibaba.analytics.core.sync.UploadMode;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Base64;
import com.alibaba.analytics.utils.DeviceUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SpSetting;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.appmonitor.delegate.AppMonitorDelegate;
import com.ut.mini.UTAnalyticsDelegate;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Variables {
    private static final String DEBUG_DATE = "debug_date";
    private static final long DEBUG_TIME = 14400000;
    private static final String TAG_OPENID = "_openid";
    private static final String TAG_TURNOFF_REAL_TIME = "real_time_debug";
    private static final String UTRTD_NAME = "UTRealTimeDebug";
    public static final Variables s_instance = new Variables();
    private volatile boolean bApIsRealTimeDebugging = false;
    private volatile boolean bInit = false;
    private boolean bPackageDebugSwitch = false;
    private boolean hasReadPackageBuildId = false;
    private boolean hasReadPackageDebugSwitch = false;
    private boolean isAllServiceClosed = false;
    private boolean isGzipUpload = false;
    private boolean isHttpService = false;
    private boolean isRealtimeServiceClosed = false;
    private String mAppVersion = null;
    private String mAppkey = null;
    private String mChannel = null;
    private UTBaseConfMgr mConfMgr = null;
    private Context mContext = null;
    private DBMgr mDbMgr = null;
    private boolean mDebugSamplingOption = false;
    private String mDebuggingKey = null;
    private boolean mIsOldDevice = false;
    private boolean mIsRealTimeDebugging = false;
    private boolean mIsSelfMonitorTurnOn = true;
    private volatile boolean mIsTurnOffDebugPlugin = false;
    private String mLUserid = null;
    private String mLUsernick = null;
    private String mOpenid;
    private String mPackageBuildId = null;
    private volatile IUTRequestAuthentication mRequestAuthenticationInstance = null;
    private String mSecret = null;
    private Map<String, String> mSessionProperties = null;
    private volatile String mTPKString = null;
    private String mTransferUrl = null;
    private String mUserid = null;
    private String mUsernick = null;

    public static Variables getInstance() {
        return s_instance;
    }

    public void turnOffDebugPlugin() {
        this.mIsTurnOffDebugPlugin = true;
    }

    public void turnOnSelfMonitor() {
        this.mIsSelfMonitorTurnOn = true;
    }

    public void turnOffSelfMonitor() {
        this.mIsSelfMonitorTurnOn = false;
    }

    public boolean isSelfMonitorTurnOn() {
        return this.mIsSelfMonitorTurnOn;
    }

    public synchronized void setHttpService(boolean httpService) {
        this.isHttpService = httpService;
    }

    public synchronized boolean isHttpService() {
        return this.isHttpService;
    }

    public synchronized void setAllServiceClosed(boolean allServiceClosed) {
        this.isAllServiceClosed = allServiceClosed;
    }

    public synchronized boolean isAllServiceClosed() {
        return this.isAllServiceClosed;
    }

    public synchronized void setRealtimeServiceClosed(boolean realtimeServiceClosed) {
        this.isRealtimeServiceClosed = realtimeServiceClosed;
    }

    public synchronized boolean isRealtimeServiceClosed() {
        return this.isRealtimeServiceClosed;
    }

    public boolean isGzipUpload() {
        return this.isGzipUpload;
    }

    public void setGzipUpload(boolean gzipUpload) {
        this.isGzipUpload = gzipUpload;
    }

    public String getTpkMD5() {
        if (this.mTPKString != null) {
            return "" + this.mTPKString.hashCode();
        }
        return null;
    }

    public String getTPKString() {
        return this.mTPKString;
    }

    public void setTPKString(String aTPKString) {
        this.mTPKString = aTPKString;
    }

    public boolean isTurnOffDebugPlugin() {
        return this.mIsTurnOffDebugPlugin;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e7, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e8, code lost:
        com.alibaba.analytics.utils.Logger.e((java.lang.String) null, r0, new java.lang.Object[0]);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void init(android.app.Application r8) {
        /*
            r7 = this;
            monitor-enter(r7)
            android.content.Context r3 = r8.getApplicationContext()     // Catch:{ all -> 0x00da }
            r7.mContext = r3     // Catch:{ all -> 0x00da }
            android.content.Context r3 = r7.mContext     // Catch:{ all -> 0x00da }
            if (r3 != 0) goto L_0x001c
            java.lang.String r3 = "UTDC init failed"
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x00da }
            r5 = 0
            java.lang.String r6 = "context:null"
            r4[r5] = r6     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r3, (java.lang.Object[]) r4)     // Catch:{ all -> 0x00da }
        L_0x001a:
            monitor-exit(r7)
            return
        L_0x001c:
            r3 = 0
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x00da }
            r5 = 0
            java.lang.String r6 = "init"
            r4[r5] = r6     // Catch:{ all -> 0x00da }
            r5 = 1
            boolean r6 = r7.bInit     // Catch:{ all -> 0x00da }
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r6)     // Catch:{ all -> 0x00da }
            r4[r5] = r6     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r3, (java.lang.Object[]) r4)     // Catch:{ all -> 0x00da }
            boolean r3 = r7.bInit     // Catch:{ all -> 0x00da }
            if (r3 != 0) goto L_0x0103
            com.alibaba.analytics.core.selfmonitor.CrashDispatcher r3 = com.alibaba.analytics.core.selfmonitor.CrashDispatcher.getInstance()     // Catch:{ Throwable -> 0x00dd }
            r3.init()     // Catch:{ Throwable -> 0x00dd }
        L_0x003d:
            com.alibaba.analytics.core.selfmonitor.SelfMonitorHandle r3 = com.alibaba.analytics.core.selfmonitor.SelfMonitorHandle.getInstance()     // Catch:{ Throwable -> 0x00e7 }
            r3.init()     // Catch:{ Throwable -> 0x00e7 }
        L_0x0044:
            r7.getLocalInfo()     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.db.DBMgr r3 = new com.alibaba.analytics.core.db.DBMgr     // Catch:{ all -> 0x00da }
            android.content.Context r4 = r7.mContext     // Catch:{ all -> 0x00da }
            java.lang.String r5 = "ut.db"
            r3.<init>(r4, r5)     // Catch:{ all -> 0x00da }
            r7.mDbMgr = r3     // Catch:{ all -> 0x00da }
            android.content.Context r3 = r7.mContext     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.network.NetworkUtil.register(r3)     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.db.OldDBTransferMgr.checkAndTransfer()     // Catch:{ all -> 0x00da }
            r1 = 0
            java.lang.String r3 = "com.taobao.orange.OrangeConfig"
            java.lang.Class r1 = java.lang.Class.forName(r3)     // Catch:{ Throwable -> 0x0108 }
        L_0x0063:
            if (r1 == 0) goto L_0x00f1
            com.alibaba.analytics.core.config.UTOrangeConfMgr r3 = new com.alibaba.analytics.core.config.UTOrangeConfMgr     // Catch:{ all -> 0x00da }
            r3.<init>()     // Catch:{ all -> 0x00da }
            r7.mConfMgr = r3     // Catch:{ all -> 0x00da }
        L_0x006c:
            com.alibaba.analytics.core.config.UTBaseConfMgr r3 = r7.mConfMgr     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.config.UTSampleConfBiz r4 = com.alibaba.analytics.core.config.UTSampleConfBiz.getInstance()     // Catch:{ all -> 0x00da }
            r3.addConfBiz(r4)     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.config.UTBaseConfMgr r3 = r7.mConfMgr     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.config.UTStreamConfBiz r4 = com.alibaba.analytics.core.config.UTStreamConfBiz.getInstance()     // Catch:{ all -> 0x00da }
            r3.addConfBiz(r4)     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.config.UTBaseConfMgr r3 = r7.mConfMgr     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.config.UTBussinessConfBiz r4 = new com.alibaba.analytics.core.config.UTBussinessConfBiz     // Catch:{ all -> 0x00da }
            r4.<init>()     // Catch:{ all -> 0x00da }
            r3.addConfBiz(r4)     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.config.UTBaseConfMgr r3 = r7.mConfMgr     // Catch:{ all -> 0x00da }
            com.alibaba.appmonitor.sample.AMSamplingMgr r4 = com.alibaba.appmonitor.sample.AMSamplingMgr.getInstance()     // Catch:{ all -> 0x00da }
            r3.addConfBiz(r4)     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.config.UTBaseConfMgr r3 = r7.mConfMgr     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.config.UTRealtimeConfBiz r4 = com.alibaba.analytics.core.config.UTRealtimeConfBiz.getInstance()     // Catch:{ all -> 0x00da }
            r3.addConfBiz(r4)     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.config.UTBaseConfMgr r3 = r7.mConfMgr     // Catch:{ Throwable -> 0x00fa }
            com.alibaba.analytics.core.config.SystemConfigMgr r4 = com.alibaba.analytics.core.config.SystemConfigMgr.getInstance()     // Catch:{ Throwable -> 0x00fa }
            r3.addConfBiz(r4)     // Catch:{ Throwable -> 0x00fa }
            com.alibaba.analytics.core.config.SystemConfigMgr r3 = com.alibaba.analytics.core.config.SystemConfigMgr.getInstance()     // Catch:{ Throwable -> 0x00fa }
            java.lang.String r4 = "sw_plugin"
            com.alibaba.analytics.core.config.DebugPluginSwitch r5 = new com.alibaba.analytics.core.config.DebugPluginSwitch     // Catch:{ Throwable -> 0x00fa }
            r5.<init>()     // Catch:{ Throwable -> 0x00fa }
            r3.register(r4, r5)     // Catch:{ Throwable -> 0x00fa }
        L_0x00b2:
            com.alibaba.analytics.core.config.UTBaseConfMgr r3 = r7.mConfMgr     // Catch:{ all -> 0x00da }
            r3.requestOnlineConfig()     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.logbuilder.TimeStampAdjustMgr r3 = com.alibaba.analytics.core.logbuilder.TimeStampAdjustMgr.getInstance()     // Catch:{ all -> 0x00da }
            r3.startSync()     // Catch:{ all -> 0x00da }
            com.alibaba.appmonitor.delegate.AppMonitorDelegate.init(r8)     // Catch:{ all -> 0x00da }
            com.ut.mini.UTAnalyticsDelegate r3 = com.ut.mini.UTAnalyticsDelegate.getInstance()     // Catch:{ all -> 0x00da }
            r3.initUT(r8)     // Catch:{ all -> 0x00da }
            r7.initRealTimeDebug()     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.core.sync.UploadMgr r3 = com.alibaba.analytics.core.sync.UploadMgr.getInstance()     // Catch:{ all -> 0x00da }
            r3.start()     // Catch:{ all -> 0x00da }
            r7.trackInfoForPreLoad()     // Catch:{ all -> 0x00da }
            r3 = 1
            r7.bInit = r3     // Catch:{ all -> 0x00da }
            goto L_0x001a
        L_0x00da:
            r3 = move-exception
            monitor-exit(r7)
            throw r3
        L_0x00dd:
            r0 = move-exception
            r3 = 0
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.utils.Logger.e(r3, r0, r4)     // Catch:{ all -> 0x00da }
            goto L_0x003d
        L_0x00e7:
            r0 = move-exception
            r3 = 0
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.utils.Logger.e(r3, r0, r4)     // Catch:{ all -> 0x00da }
            goto L_0x0044
        L_0x00f1:
            com.alibaba.analytics.core.config.UTDefaultConfMgr r3 = new com.alibaba.analytics.core.config.UTDefaultConfMgr     // Catch:{ all -> 0x00da }
            r3.<init>()     // Catch:{ all -> 0x00da }
            r7.mConfMgr = r3     // Catch:{ all -> 0x00da }
            goto L_0x006c
        L_0x00fa:
            r2 = move-exception
            r3 = 0
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x00da }
            com.alibaba.analytics.utils.Logger.e(r3, r2, r4)     // Catch:{ all -> 0x00da }
            goto L_0x00b2
        L_0x0103:
            com.alibaba.analytics.core.config.UTConfigMgr.postAllServerConfig()     // Catch:{ all -> 0x00da }
            goto L_0x001a
        L_0x0108:
            r3 = move-exception
            goto L_0x0063
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.Variables.init(android.app.Application):void");
    }

    private void trackInfoForPreLoad() {
        try {
            Map info = AppInfoUtil.getInfoForPreApk(this.mContext);
            if (info != null && info.size() > 0) {
                Map<String, String> logMap = new HashMap<>();
                logMap.put(LogField.EVENTID.toString(), "1021");
                logMap.putAll(info);
                UTAnalyticsDelegate.getInstance().transferLog(logMap);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public UTBaseConfMgr getConfMgr() {
        return this.mConfMgr;
    }

    private void getLocalInfo() {
        SharedPreferences lSP = this.mContext.getSharedPreferences("UTCommon", 0);
        String lUN = lSP.getString("_lun", "");
        if (!StringUtils.isEmpty(lUN)) {
            try {
                this.mLUsernick = new String(Base64.decode(lUN.getBytes(), 2), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String lUid = lSP.getString("_luid", "");
        if (!StringUtils.isEmpty(lUid)) {
            try {
                this.mLUserid = new String(Base64.decode(lUid.getBytes(), 2), "UTF-8");
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
        }
        String openid = lSP.getString(TAG_OPENID, "");
        if (!StringUtils.isEmpty(openid)) {
            try {
                this.mOpenid = new String(Base64.decode(openid.getBytes(), 2), "UTF-8");
            } catch (Throwable e3) {
                e3.printStackTrace();
            }
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public String getAppkey() {
        return this.mAppkey;
    }

    public String getSecret() {
        return this.mSecret;
    }

    public void setSecret(String secret) {
        this.mSecret = secret;
    }

    public void setAppVersion(String aAppVersion) {
        this.mAppVersion = aAppVersion;
    }

    public String getAppVersion() {
        Map<String, String> deviceInfo;
        if (TextUtils.isEmpty(this.mAppVersion) && (deviceInfo = DeviceUtil.getDeviceInfo(getContext())) != null) {
            this.mAppVersion = deviceInfo.get(LogField.APPVERSION);
        }
        return this.mAppVersion;
    }

    public void setRequestAuthenticationInstance(IUTRequestAuthentication aRequestAuthenticationInstance) {
        this.mRequestAuthenticationInstance = aRequestAuthenticationInstance;
        if (aRequestAuthenticationInstance != null) {
            this.mAppkey = aRequestAuthenticationInstance.getAppkey();
        }
    }

    public IUTRequestAuthentication getRequestAuthenticationInstance() {
        return this.mRequestAuthenticationInstance;
    }

    public void turnOnDebug() {
        setDebug(true);
    }

    public void setChannel(String aChannel) {
        Logger.d((String) null, aChannel, aChannel);
        this.mChannel = aChannel;
    }

    public String getLongLoginUsernick() {
        return this.mLUsernick;
    }

    public String getLongLoingUserid() {
        return this.mLUserid;
    }

    public String getChannel() {
        if (TextUtils.isEmpty(this.mChannel)) {
            String channel = SpSetting.get(getContext(), "channel");
            if (!TextUtils.isEmpty(channel)) {
                return channel;
            }
        }
        return this.mChannel;
    }

    public String getUsernick() {
        return this.mUsernick;
    }

    public String getUserid() {
        return this.mUserid;
    }

    public String getOpenid() {
        return this.mOpenid;
    }

    public void updateUserAccount(String aUsernick, String aUserid, String openid) {
        setUsernick(aUsernick);
        setUserid(aUserid);
        setOpenid(openid);
    }

    public void setIsOldDevice(boolean old) {
        if (this.mContext != null) {
            this.mContext.getSharedPreferences("UTCommon", 0).edit().putBoolean("_isolddevice", old).commit();
        }
    }

    public boolean isOldDevice() {
        if (!this.mIsOldDevice && this.mContext != null) {
            this.mIsOldDevice = this.mContext.getSharedPreferences("UTCommon", 0).getBoolean("_isolddevice", false);
        }
        return this.mIsOldDevice;
    }

    public synchronized String getHostPackageImei() {
        String str;
        DeviceInfo deviceInfo = Device.getDevice(this.mContext);
        if (deviceInfo != null) {
            str = deviceInfo.getImei();
        } else {
            str = "";
        }
        return str;
    }

    public synchronized String getHostPackageImsi() {
        String str;
        DeviceInfo deviceInfo = Device.getDevice(this.mContext);
        if (deviceInfo != null) {
            str = deviceInfo.getImsi();
        } else {
            str = "";
        }
        return str;
    }

    public synchronized void setDebugSamplingOption() {
        this.mDebugSamplingOption = true;
        AppMonitorDelegate.IS_DEBUG = true;
    }

    public synchronized boolean getDebugSamplingOption() {
        return this.mDebugSamplingOption;
    }

    public synchronized void setSessionProperties(Map<String, String> aMap) {
        this.mSessionProperties = aMap;
    }

    public synchronized Map<String, String> getSessionProperties() {
        return this.mSessionProperties;
    }

    public synchronized void setDebugKey(String aDebuggingKey) {
        this.mDebuggingKey = aDebuggingKey;
    }

    public synchronized String getDebugKey() {
        return this.mDebuggingKey;
    }

    public synchronized boolean isRealTimeDebug() {
        return this.mIsRealTimeDebugging;
    }

    public synchronized void setRealTimeDebugFlag() {
        this.mIsRealTimeDebugging = true;
    }

    public synchronized void resetRealTimeDebugFlag() {
        this.mIsRealTimeDebugging = false;
    }

    public boolean isApRealTimeDebugging() {
        return this.bApIsRealTimeDebugging;
    }

    public void turnOffRealTimeDebug() {
        resetRealTimeDebugFlag();
        setDebugKey((String) null);
        UploadMgr.getInstance().setMode(UploadMode.INTERVAL);
        storeRealTimeDebugSharePreference((Map<String, String>) null);
        this.bApIsRealTimeDebugging = false;
    }

    public void turnOnRealTimeDebug(Map<String, String> aDebuggingMap) {
        Logger.d();
        if ("0".equalsIgnoreCase(SystemConfigMgr.getInstance().get(TAG_TURNOFF_REAL_TIME))) {
            Logger.w("Variables", "Server Config turn off RealTimeDebug Mode!");
            return;
        }
        if (aDebuggingMap != null && aDebuggingMap.containsKey(Constants.RealTimeDebug.DEBUG_API_URL) && aDebuggingMap.containsKey(Constants.RealTimeDebug.DEBUG_KEY)) {
            String lDebugKey = aDebuggingMap.get(Constants.RealTimeDebug.DEBUG_KEY);
            if (!StringUtils.isEmpty(aDebuggingMap.get(Constants.RealTimeDebug.DEBUG_API_URL)) && !StringUtils.isEmpty(lDebugKey)) {
                setRealTimeDebugFlag();
                setDebugKey(lDebugKey);
            }
            if (aDebuggingMap.containsKey(Constants.RealTimeDebug.DEBUG_SAMPLING_OPTION)) {
                setDebugSamplingOption();
            }
            setDebug(true);
            UploadMgr.getInstance().setMode(UploadMode.REALTIME);
        }
        storeRealTimeDebugSharePreference(aDebuggingMap);
    }

    private void storeRealTimeDebugSharePreference(Map<String, String> aDebuggingMap) {
        if (this.mContext != null) {
            Logger.d("", aDebuggingMap);
            SharedPreferences.Editor editor = this.mContext.getSharedPreferences(UTRTD_NAME, 0).edit();
            if (aDebuggingMap == null || !aDebuggingMap.containsKey(Constants.RealTimeDebug.DEBUG_STORE)) {
                editor.putLong(DEBUG_DATE, 0);
            } else {
                editor.putString(Constants.RealTimeDebug.DEBUG_API_URL, aDebuggingMap.get(Constants.RealTimeDebug.DEBUG_API_URL));
                editor.putString(Constants.RealTimeDebug.DEBUG_KEY, aDebuggingMap.get(Constants.RealTimeDebug.DEBUG_KEY));
                editor.putLong(DEBUG_DATE, System.currentTimeMillis());
            }
            editor.commit();
        }
    }

    private void initRealTimeDebug() {
        if (this.mContext != null) {
            Logger.d();
            SharedPreferences preferences = this.mContext.getSharedPreferences(UTRTD_NAME, 0);
            long debugDate = preferences.getLong(DEBUG_DATE, 0);
            Logger.d("", "debugDate", Long.valueOf(debugDate));
            if (System.currentTimeMillis() - debugDate <= DEBUG_TIME) {
                Map<String, String> debuggingMap = new HashMap<>();
                debuggingMap.put(Constants.RealTimeDebug.DEBUG_API_URL, preferences.getString(Constants.RealTimeDebug.DEBUG_API_URL, ""));
                debuggingMap.put(Constants.RealTimeDebug.DEBUG_KEY, preferences.getString(Constants.RealTimeDebug.DEBUG_KEY, ""));
                turnOnRealTimeDebug(debuggingMap);
            }
        }
    }

    public void setDebug(boolean mIsDebug) {
        Logger.setDebug(mIsDebug);
    }

    public String getTransferUrl() {
        if (TextUtils.isEmpty(this.mTransferUrl)) {
            this.mTransferUrl = Constants.G_TRANSFER_URL;
            String localConfigHost = AppInfoUtil.getString(getContext(), Constants.UT.TAG_SP_HTTP_TRANSFER_HOST);
            if (!TextUtils.isEmpty(localConfigHost)) {
                try {
                    String ret = this.mTransferUrl.replace(Uri.parse(this.mTransferUrl).getHost(), localConfigHost);
                    Logger.e(ret, new Object[0]);
                    return ret;
                } catch (Throwable th) {
                    return this.mTransferUrl;
                }
            } else {
                String host = SpSetting.get(getContext(), Constants.UT.TAG_SP_HTTP_TRANSFER_HOST);
                if (!TextUtils.isEmpty(host)) {
                    try {
                        String ret2 = this.mTransferUrl.replace(Uri.parse(this.mTransferUrl).getHost(), host);
                        Logger.e(ret2, new Object[0]);
                        return ret2;
                    } catch (Throwable th2) {
                        return this.mTransferUrl;
                    }
                }
            }
        }
        return this.mTransferUrl;
    }

    private void setUsernick(String pUsernick) {
        this.mUsernick = pUsernick;
        if (!StringUtils.isEmpty(pUsernick)) {
            this.mLUsernick = pUsernick;
        }
        if (!StringUtils.isEmpty(pUsernick) && this.mContext != null) {
            try {
                SharedPreferences.Editor lE = this.mContext.getSharedPreferences("UTCommon", 0).edit();
                lE.putString("_lun", new String(Base64.encode(pUsernick.getBytes("UTF-8"), 2)));
                lE.commit();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUserid(String pUserid) {
        this.mUserid = pUserid;
        if (!StringUtils.isEmpty(pUserid)) {
            this.mLUserid = pUserid;
        }
        if (!StringUtils.isEmpty(pUserid) && this.mContext != null) {
            try {
                SharedPreferences.Editor lE = this.mContext.getSharedPreferences("UTCommon", 0).edit();
                lE.putString("_luid", new String(Base64.encode(pUserid.getBytes("UTF-8"), 2)));
                lE.commit();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public DBMgr getDbMgr() {
        return this.mDbMgr;
    }

    public static boolean isNotDisAM() {
        return true;
    }

    public boolean isInit() {
        return this.bInit;
    }

    public boolean isDebugPackage() {
        if (this.hasReadPackageDebugSwitch) {
            return this.bPackageDebugSwitch;
        }
        Context context = getContext();
        if (context == null) {
            return false;
        }
        if ("1".equalsIgnoreCase(AppInfoUtil.getString(context, "package_type"))) {
            this.bPackageDebugSwitch = true;
            this.hasReadPackageDebugSwitch = true;
        }
        return this.bPackageDebugSwitch;
    }

    public String getPackageBuildId() {
        if (this.hasReadPackageBuildId) {
            return this.mPackageBuildId;
        }
        Context context = getContext();
        if (context == null) {
            return null;
        }
        this.mPackageBuildId = AppInfoUtil.getString(context, "build_id");
        this.hasReadPackageBuildId = true;
        return this.mPackageBuildId;
    }

    public void setOpenid(String openid) {
        this.mOpenid = openid;
        if (this.mContext != null) {
            try {
                SharedPreferences.Editor lE = this.mContext.getSharedPreferences("UTCommon", 0).edit();
                if (TextUtils.isEmpty(openid)) {
                    lE.putString(TAG_OPENID, (String) null);
                } else {
                    lE.putString(TAG_OPENID, new String(Base64.encode(openid.getBytes("UTF-8"), 2)));
                }
                lE.commit();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
