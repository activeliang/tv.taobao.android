package com.taobao.orange;

import android.content.Context;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.text.TextUtils;
import anet.channel.entity.ConnType;
import anet.channel.util.HttpConstant;
import anetwork.channel.interceptor.Interceptor;
import anetwork.channel.interceptor.InterceptorManager;
import com.ali.auth.third.offline.login.LoginConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ta.utdid2.device.UTDevice;
import com.taobao.orange.OConstant;
import com.taobao.orange.aidl.OrangeConfigListenerStub;
import com.taobao.orange.aidl.ParcelableConfigListener;
import com.taobao.orange.cache.ConfigCache;
import com.taobao.orange.cache.IndexCache;
import com.taobao.orange.candidate.MultiAnalyze;
import com.taobao.orange.model.ConfigAckDO;
import com.taobao.orange.model.ConfigDO;
import com.taobao.orange.model.IndexAckDO;
import com.taobao.orange.model.IndexDO;
import com.taobao.orange.model.NameSpaceDO;
import com.taobao.orange.receiver.OrangeReceiver;
import com.taobao.orange.sync.AuthRequest;
import com.taobao.orange.sync.BaseRequest;
import com.taobao.orange.sync.CdnRequest;
import com.taobao.orange.sync.IndexUpdateHandler;
import com.taobao.orange.sync.NetworkInterceptor;
import com.taobao.orange.util.AndroidUtil;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeMonitor;
import com.taobao.orange.util.OrangeUtils;
import com.taobao.orange.util.ReportAckUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;

public class ConfigCenter {
    static final int BASE_ACKINTERVAL = 10;
    private static final long FAIL_LOAD_INDEX_UPD_INTERVAL = 100000;
    private static final long FAIL_LOAD_INDEX_UPD_NUM = 10;
    static final String SYSKEY_ACKVIPS = "ackVips";
    static final String SYSKEY_DCVIPS = "dcVips";
    static final String SYSKEY_DELAYACK_INTERVAL = "delayAckInterval";
    static final String SYSKEY_INDEXUPD_MODE = "indexUpdateMode";
    static final String SYSKEY_PROBE_HOSTS = "hosts";
    static final String SYSKEY_REPORT_UPDACK = "reportUpdateAck";
    static final String SYSKEY_REQ_RETRY_NUM = "reqRetryNum";
    static final String SYS_NAMESPACE = "orange";
    static final String TAG = "ConfigCenter";
    private static volatile long failLastIndexUpdTime = 0;
    static ConfigCenter mInstance = new ConfigCenter();
    ConfigCache mConfigCache = new ConfigCache();
    final Set<String> mFailRequestsSet = new HashSet();
    volatile ParcelableConfigListener mGlobalListener;
    IndexCache mIndexCache = new IndexCache();
    public AtomicBoolean mIsOrangeInit = new AtomicBoolean(false);
    final Map<String, Set<ParcelableConfigListener>> mListeners = new HashMap();
    final Set<String> mLoadingConfigSet = new HashSet();
    Interceptor mNetworkInterceptor;

    public static ConfigCenter getInstance() {
        return mInstance;
    }

    private ConfigCenter() {
    }

    public void setGlobalListener(OConfigListener listener) {
        this.mGlobalListener = new OrangeConfigListenerStub(listener);
    }

    public void init(final Context context, final OConfig config) {
        if (context == null || TextUtils.isEmpty(config.appKey) || TextUtils.isEmpty(config.appVersion)) {
            OLog.e(TAG, "init start", "input param error");
            return;
        }
        OThreadFactory.execute(new Runnable() {
            public void run() {
                String str;
                String str2;
                synchronized (ConfigCenter.this) {
                    if (!ConfigCenter.this.mIsOrangeInit.get()) {
                        GlobalOrange.context = context.getApplicationContext();
                        GlobalOrange.deviceId = UTDevice.getUtdid(context);
                        GlobalOrange.appKey = config.appKey;
                        GlobalOrange.appVersion = config.appVersion;
                        GlobalOrange.userId = config.userId;
                        GlobalOrange.appSecret = config.appSecret;
                        GlobalOrange.authCode = config.authCode;
                        GlobalOrange.indexUpdMode = OConstant.UPDMODE.valueOf(config.indexUpdateMode);
                        GlobalOrange.env = OConstant.ENV.valueOf(config.env);
                        GlobalOrange.randomDelayAckInterval = ConfigCenter.this.updateRandomDelayAckInterval(ConfigCenter.FAIL_LOAD_INDEX_UPD_NUM);
                        if (config.probeHosts == null || config.probeHosts.length <= 0) {
                            GlobalOrange.probeHosts.addAll(Arrays.asList(OConstant.PROBE_HOSTS[GlobalOrange.env.getEnvMode()]));
                        } else {
                            GlobalOrange.probeHosts.addAll(Arrays.asList(config.probeHosts));
                        }
                        OConstant.SERVER serverType = OConstant.SERVER.valueOf(config.serverType);
                        if (TextUtils.isEmpty(config.dcHost)) {
                            if (serverType == OConstant.SERVER.TAOBAO) {
                                str2 = OConstant.DC_TAOBAO_HOSTS[GlobalOrange.env.getEnvMode()];
                            } else {
                                str2 = OConstant.DC_YOUKU_HOSTS[GlobalOrange.env.getEnvMode()];
                            }
                            GlobalOrange.dcHost = str2;
                        } else {
                            GlobalOrange.dcHost = config.dcHost;
                        }
                        GlobalOrange.dcVips.addAll(OrangeUtils.getArrayListFromArray(config.dcVips));
                        if (TextUtils.isEmpty(config.ackHost)) {
                            if (serverType == OConstant.SERVER.TAOBAO) {
                                str = OConstant.ACK_TAOBAO_HOSTS[GlobalOrange.env.getEnvMode()];
                            } else {
                                str = OConstant.ACK_YOUKU_HOSTS[GlobalOrange.env.getEnvMode()];
                            }
                            GlobalOrange.ackHost = str;
                        } else {
                            GlobalOrange.ackHost = config.ackHost;
                        }
                        GlobalOrange.ackVips.addAll(OrangeUtils.getArrayListFromArray(config.ackVips));
                        if (OLog.isPrintLog(2)) {
                            OLog.i(ConfigCenter.TAG, "init start", "sdkVersion", OConstant.SDK_VERSION, "appKey", config.appKey, "appVersion", config.appVersion, "env", GlobalOrange.env, ConfigCenter.SYSKEY_INDEXUPD_MODE, GlobalOrange.indexUpdMode, "serverType", serverType, "probeHosts", GlobalOrange.probeHosts, "dcHost", GlobalOrange.dcHost, ConfigCenter.SYSKEY_DCVIPS, GlobalOrange.dcVips, "ackHost", GlobalOrange.ackHost, ConfigCenter.SYSKEY_ACKVIPS, GlobalOrange.ackVips);
                        }
                        MultiAnalyze.initBuildInCandidates();
                        ConfigCenter.this.mListeners.put(ConfigCenter.SYS_NAMESPACE, new HashSet<ParcelableConfigListener>() {
                            {
                                add(new ParcelableConfigListener.Stub() {
                                    public void onConfigUpdate(String groupName, Map args) throws RemoteException {
                                        ConfigCenter.this.updateSystemConfig(args);
                                    }
                                });
                            }
                        });
                        ConfigCenter.this.loadCaches();
                        try {
                            Class.forName(OConstant.REFLECT_NETWORK_INTERCEPTOR);
                            Class.forName(OConstant.REFLECT_NETWORK_INTERCEPTORMANAGER);
                            ConfigCenter.this.mNetworkInterceptor = new NetworkInterceptor();
                            InterceptorManager.addInterceptor(ConfigCenter.this.mNetworkInterceptor);
                            OLog.i(ConfigCenter.TAG, "init", "add orange interceptor success to networksdk");
                        } catch (ClassNotFoundException e) {
                            OLog.w(ConfigCenter.TAG, "init", e, "add orange interceptor fail as not found networksdk");
                        }
                        ConfigCenter.this.mIsOrangeInit.set(true);
                        ConfigCenter.this.forceCheckUpdate();
                    } else {
                        OLog.w(ConfigCenter.TAG, "already init", new Object[0]);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void loadCaches() {
        try {
            long restoreStartTime = System.currentTimeMillis();
            OLog.i(TAG, "loadCaches", "start index");
            this.mIndexCache.load();
            Set<NameSpaceDO> allNamespaces = this.mIndexCache.getAllNameSpaces();
            OLog.i(TAG, "loadCaches", "start restore configs", Integer.valueOf(allNamespaces.size()));
            Set<NameSpaceDO> notMatchNamespaces = this.mConfigCache.load(allNamespaces);
            OLog.i(TAG, "loadCaches", "finish restore configs", Integer.valueOf(allNamespaces.size()), "cost(ms)", Long.valueOf(System.currentTimeMillis() - restoreStartTime));
            if (notMatchNamespaces != null && !notMatchNamespaces.isEmpty()) {
                OLog.i(TAG, "loadCaches", "start load notMatchNamespaces", Integer.valueOf(notMatchNamespaces.size()));
                long loadStartTime = System.currentTimeMillis();
                for (NameSpaceDO nameSpaceDO : notMatchNamespaces) {
                    loadConfig(nameSpaceDO);
                }
                OLog.i(TAG, "loadCaches", "finish load notMatchNamespaces", Integer.valueOf(notMatchNamespaces.size()), "cost(ms)", Long.valueOf(System.currentTimeMillis() - loadStartTime));
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            GlobalOrange.context.registerReceiver(new OrangeReceiver(), filter);
        } catch (Throwable t) {
            OLog.e(TAG, "loadCaches", t, new Object[0]);
            OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_EXCEPTION, "loadCaches: " + t.getMessage(), 1.0d);
        }
    }

    public void forceCheckUpdate() {
        if (!this.mIsOrangeInit.get()) {
            OLog.w(TAG, "forceCheckUpdate fail as not finish orange init", new Object[0]);
        } else if (GlobalOrange.indexUpdMode != OConstant.UPDMODE.O_XMD) {
            OLog.i(TAG, "forceCheckUpdate start", new Object[0]);
            IndexUpdateHandler.checkIndexUpdate(this.mIndexCache.getAppIndexVersion(), this.mIndexCache.getVersionIndexVersion());
        } else {
            OLog.w(TAG, "forceCheckUpdate fail as not allow in O_XMD mode", new Object[0]);
        }
    }

    public String getConfig(String namespace, String key, String defaultVal) {
        Map<String, String> configsMap = getConfigs(namespace);
        if (configsMap == null || !configsMap.containsKey(key)) {
            return defaultVal;
        }
        return configsMap.get(key);
    }

    public Map<String, String> getConfigs(String namespace) {
        if (TextUtils.isEmpty(namespace)) {
            OLog.e(TAG, "getConfigs error, namespace is empty", new Object[0]);
            return null;
        } else if (SYS_NAMESPACE.equals(namespace) || IndexCache.INDEX_STORE_NAME.equals(namespace)) {
            OLog.e(TAG, "getConfigs error, namespace is occupied by sdk", new Object[0]);
            return null;
        } else if (this.mConfigCache.getConfigMap().containsKey(namespace)) {
            return this.mConfigCache.getConfigs(namespace);
        } else {
            if (OLog.isPrintLog(0)) {
                OLog.v(TAG, "getConfigs", "namespace", namespace, "...null");
            }
            final NameSpaceDO namespaceDO = this.mIndexCache.getNameSpace(namespace);
            if (namespaceDO == null || !this.mIsOrangeInit.get()) {
                addFail(namespace);
                return null;
            } else if (checkLoading(namespace, false)) {
                return null;
            } else {
                OThreadFactory.execute(new Runnable() {
                    public void run() {
                        if (OLog.isPrintLog(0)) {
                            OLog.d(ConfigCenter.TAG, "getConfigs force to load", "namespace", namespaceDO.name);
                        }
                        ConfigCenter.this.loadConfig(namespaceDO);
                    }
                });
                return null;
            }
        }
    }

    public void loadConfig(NameSpaceDO nameSpaceDO) {
        String configResourceId;
        String configMd5;
        String configVersion;
        if (nameSpaceDO == null) {
            OLog.e(TAG, "loadConfig fail", "nameSpaceDO is null");
        } else if (NameSpaceDO.TYPE_CUSTOM.equals(nameSpaceDO.type)) {
            OLog.e(TAG, "loadConfig fail", "not support custom type");
        } else {
            if (!checkLoading(nameSpaceDO.name, true)) {
                try {
                    String cdnUrl = this.mIndexCache.getCdnUrl();
                    if (TextUtils.isEmpty(cdnUrl)) {
                        OLog.e(TAG, "loadConfig fail", "cdnUrl is null");
                        addFail(nameSpaceDO.name);
                        removeLoading(nameSpaceDO.name);
                        return;
                    }
                    if (OLog.isPrintLog(1)) {
                        OLog.d(TAG, "loadConfig start", nameSpaceDO);
                    }
                    if (!nameSpaceDO.checkValid(this.mConfigCache.getConfigMap().get(nameSpaceDO.name))) {
                        removeFail(nameSpaceDO.name);
                        removeLoading(nameSpaceDO.name);
                        return;
                    }
                    if (nameSpaceDO.curCandidateDO != null) {
                        configResourceId = nameSpaceDO.curCandidateDO.resourceId;
                        configMd5 = nameSpaceDO.curCandidateDO.md5;
                        configVersion = nameSpaceDO.curCandidateDO.version;
                    } else {
                        configResourceId = nameSpaceDO.resourceId;
                        configMd5 = nameSpaceDO.md5;
                        configVersion = nameSpaceDO.version;
                    }
                    if (OLog.isPrintLog(0)) {
                        OLog.v(TAG, "loadConfig check", LoginConstants.CONFIG, nameSpaceDO.name, "version", configVersion);
                    }
                    BaseRequest<ConfigDO> configReq = new CdnRequest<ConfigDO>(cdnUrl + File.separator + configResourceId, configMd5) {
                        /* access modifiers changed from: protected */
                        public ConfigDO parseResContent(String content) {
                            return (ConfigDO) JSON.parseObject(content, ConfigDO.class);
                        }
                    };
                    ConfigDO configDO = configReq.syncRequest();
                    if (configDO == null || !configDO.checkValid()) {
                        if (OLog.isPrintLog(0)) {
                            OLog.v(TAG, "loadConfig cdnReq fail downgrade to authReq", "code", configReq.getCode(), "msg", configReq.getMessage());
                        }
                        final NameSpaceDO nameSpaceDO2 = nameSpaceDO;
                        configReq = new AuthRequest<ConfigDO>(nameSpaceDO.md5, false, OConstant.REQTYPE_DOWNLOAD_RESOURCE) {
                            /* access modifiers changed from: protected */
                            public Map<String, String> getReqParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("resourceId", nameSpaceDO2.resourceId);
                                return params;
                            }

                            /* access modifiers changed from: protected */
                            public String getReqPostBody() {
                                return null;
                            }

                            /* access modifiers changed from: protected */
                            public ConfigDO parseResContent(String content) {
                                return (ConfigDO) JSON.parseObject(content, ConfigDO.class);
                            }
                        };
                        configDO = configReq.syncRequest();
                    }
                    if (configDO == null || !configDO.checkValid() || !configDO.version.equals(configVersion) || !configDO.name.equals(nameSpaceDO.name)) {
                        addFail(nameSpaceDO.name);
                        removeLoading(nameSpaceDO.name);
                        if (!"-200".equals(configReq.getCode())) {
                            if (configDO != null && !configDO.checkValid()) {
                                configReq.setCode(-4);
                                configReq.setMessage("config is invaild");
                            }
                            OrangeMonitor.commitFail(OConstant.MONITOR_MODULE, OConstant.POINT_CFG_RATE, nameSpaceDO.name, configReq.getCode(), configReq.getMessage());
                        }
                        OLog.e(TAG, "loadConfig fail", "namespace", nameSpaceDO.name, "code", configReq.getCode(), "msg", configReq.getMessage());
                        return;
                    }
                    removeFail(nameSpaceDO.name);
                    removeLoading(nameSpaceDO.name);
                    OrangeMonitor.commitSuccess(OConstant.MONITOR_MODULE, OConstant.POINT_CFG_RATE, nameSpaceDO.name);
                    configDO.candidate = nameSpaceDO.curCandidateDO;
                    this.mConfigCache.cache(configDO);
                    if (OLog.isPrintLog(2)) {
                        OLog.i(TAG, "loadConfig success", configDO);
                    }
                    ReportAckUtils.reportConfigAck(new ConfigAckDO(configDO.name, configDO.id, OrangeUtils.getCurFormatTime(), configDO.version));
                } catch (Throwable t) {
                    addFail(nameSpaceDO.name);
                    removeLoading(nameSpaceDO.name);
                    OrangeMonitor.commitFail(OConstant.MONITOR_MODULE, OConstant.POINT_CFG_RATE, nameSpaceDO.name, "0", t.getMessage());
                    OLog.e(TAG, "loadConfig fail", t, "namespace", nameSpaceDO.name);
                }
            } else if (OLog.isPrintLog(3)) {
                OLog.w(TAG, "loadConfig break as is loading", "namespace", nameSpaceDO.name);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005e, code lost:
        r1 = r11.mConfigCache.getConfigMap().get(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006a, code lost:
        if (r1 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006c, code lost:
        r4 = r1.getCurVersion();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0075, code lost:
        if (com.taobao.orange.util.OLog.isPrintLog(0) == false) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0077, code lost:
        com.taobao.orange.util.OLog.v(TAG, "registerListener onConfigUpdate", "namespace", r12, "version", r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        r0 = new java.util.HashMap<>();
        r0.put(com.taobao.orange.OConstant.LISTENERKEY_FROM_CACHE, "true");
        r0.put(com.taobao.orange.OConstant.LISTENERKEY_CONFIG_VERSION, r4);
        r13.onConfigUpdate(r12, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00ae, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00af, code lost:
        com.taobao.orange.util.OLog.w(TAG, "registerListener", r3, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void registerListener(java.lang.String r12, com.taobao.orange.aidl.ParcelableConfigListener r13, boolean r14) {
        /*
            r11 = this;
            boolean r5 = android.text.TextUtils.isEmpty(r12)
            if (r5 != 0) goto L_0x0008
            if (r13 != 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            java.util.Map<java.lang.String, java.util.Set<com.taobao.orange.aidl.ParcelableConfigListener>> r6 = r11.mListeners
            monitor-enter(r6)
            java.util.Map<java.lang.String, java.util.Set<com.taobao.orange.aidl.ParcelableConfigListener>> r5 = r11.mListeners     // Catch:{ all -> 0x0028 }
            java.lang.Object r2 = r5.get(r12)     // Catch:{ all -> 0x0028 }
            java.util.Set r2 = (java.util.Set) r2     // Catch:{ all -> 0x0028 }
            if (r2 != 0) goto L_0x0020
            java.util.HashSet r2 = new java.util.HashSet     // Catch:{ all -> 0x0028 }
            r2.<init>()     // Catch:{ all -> 0x0028 }
            java.util.Map<java.lang.String, java.util.Set<com.taobao.orange.aidl.ParcelableConfigListener>> r5 = r11.mListeners     // Catch:{ all -> 0x0028 }
            r5.put(r12, r2)     // Catch:{ all -> 0x0028 }
        L_0x0020:
            boolean r5 = r2.contains(r13)     // Catch:{ all -> 0x0028 }
            if (r5 == 0) goto L_0x002b
            monitor-exit(r6)     // Catch:{ all -> 0x0028 }
            goto L_0x0008
        L_0x0028:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0028 }
            throw r5
        L_0x002b:
            if (r14 == 0) goto L_0x00bd
            r2.add(r13)     // Catch:{ all -> 0x0028 }
            r5 = 1
            boolean r5 = com.taobao.orange.util.OLog.isPrintLog(r5)     // Catch:{ all -> 0x0028 }
            if (r5 == 0) goto L_0x005d
            java.lang.String r5 = "ConfigCenter"
            java.lang.String r7 = "registerListener append"
            r8 = 4
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ all -> 0x0028 }
            r9 = 0
            java.lang.String r10 = "namespace"
            r8[r9] = r10     // Catch:{ all -> 0x0028 }
            r9 = 1
            r8[r9] = r12     // Catch:{ all -> 0x0028 }
            r9 = 2
            java.lang.String r10 = "size"
            r8[r9] = r10     // Catch:{ all -> 0x0028 }
            r9 = 3
            int r10 = r2.size()     // Catch:{ all -> 0x0028 }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ all -> 0x0028 }
            r8[r9] = r10     // Catch:{ all -> 0x0028 }
            com.taobao.orange.util.OLog.d(r5, r7, r8)     // Catch:{ all -> 0x0028 }
        L_0x005d:
            monitor-exit(r6)     // Catch:{ all -> 0x0028 }
            com.taobao.orange.cache.ConfigCache r5 = r11.mConfigCache
            java.util.Map r5 = r5.getConfigMap()
            java.lang.Object r1 = r5.get(r12)
            com.taobao.orange.model.ConfigDO r1 = (com.taobao.orange.model.ConfigDO) r1
            if (r1 == 0) goto L_0x0008
            java.lang.String r4 = r1.getCurVersion()
            r5 = 0
            boolean r5 = com.taobao.orange.util.OLog.isPrintLog(r5)
            if (r5 == 0) goto L_0x0095
            java.lang.String r5 = "ConfigCenter"
            java.lang.String r6 = "registerListener onConfigUpdate"
            r7 = 4
            java.lang.Object[] r7 = new java.lang.Object[r7]
            r8 = 0
            java.lang.String r9 = "namespace"
            r7[r8] = r9
            r8 = 1
            r7[r8] = r12
            r8 = 2
            java.lang.String r9 = "version"
            r7[r8] = r9
            r8 = 3
            r7[r8] = r4
            com.taobao.orange.util.OLog.v(r5, r6, r7)
        L_0x0095:
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Throwable -> 0x00ae }
            r0.<init>()     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r5 = "fromCache"
            java.lang.String r6 = "true"
            r0.put(r5, r6)     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r5 = "configVersion"
            r0.put(r5, r4)     // Catch:{ Throwable -> 0x00ae }
            r13.onConfigUpdate(r12, r0)     // Catch:{ Throwable -> 0x00ae }
            goto L_0x0008
        L_0x00ae:
            r3 = move-exception
            java.lang.String r5 = "ConfigCenter"
            java.lang.String r6 = "registerListener"
            r7 = 0
            java.lang.Object[] r7 = new java.lang.Object[r7]
            com.taobao.orange.util.OLog.w(r5, r6, r3, r7)
            goto L_0x0008
        L_0x00bd:
            r5 = 1
            boolean r5 = com.taobao.orange.util.OLog.isPrintLog(r5)     // Catch:{ all -> 0x0028 }
            if (r5 == 0) goto L_0x00d9
            java.lang.String r5 = "ConfigCenter"
            java.lang.String r7 = "registerListener cover"
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ all -> 0x0028 }
            r9 = 0
            java.lang.String r10 = "namespace"
            r8[r9] = r10     // Catch:{ all -> 0x0028 }
            r9 = 1
            r8[r9] = r12     // Catch:{ all -> 0x0028 }
            com.taobao.orange.util.OLog.d(r5, r7, r8)     // Catch:{ all -> 0x0028 }
        L_0x00d9:
            r2.clear()     // Catch:{ all -> 0x0028 }
            r2.add(r13)     // Catch:{ all -> 0x0028 }
            goto L_0x005d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.ConfigCenter.registerListener(java.lang.String, com.taobao.orange.aidl.ParcelableConfigListener, boolean):void");
    }

    public void unregisterListener(String namespace, ParcelableConfigListener listener) {
        if (!TextUtils.isEmpty(namespace) && listener != null) {
            synchronized (this.mListeners) {
                Set<ParcelableConfigListener> configListeners = this.mListeners.get(namespace);
                if (configListeners != null && configListeners.size() > 0 && configListeners.remove(listener) && OLog.isPrintLog(1)) {
                    OLog.d(TAG, "unregisterListener", "namespace", namespace, "size", Integer.valueOf(configListeners.size()));
                }
            }
        }
    }

    public void unregisterListeners(String namespace) {
        if (!TextUtils.isEmpty(namespace)) {
            synchronized (this.mListeners) {
                this.mListeners.remove(namespace);
            }
        }
    }

    public void notifyListeners(String namespace, String version, boolean fromCache) {
        if (!TextUtils.isEmpty(namespace)) {
            Map<String, String> args = new HashMap<>();
            args.put(OConstant.LISTENERKEY_FROM_CACHE, String.valueOf(fromCache));
            args.put(OConstant.LISTENERKEY_CONFIG_VERSION, version);
            if (this.mGlobalListener != null) {
                try {
                    this.mGlobalListener.onConfigUpdate(namespace, args);
                } catch (Throwable t) {
                    OLog.w(TAG, "notifyGlobalListeners", t, new Object[0]);
                }
            }
            Set<ParcelableConfigListener> configListeners = new HashSet<>();
            synchronized (this.mListeners) {
                Set<ParcelableConfigListener> tempConfigListeners = this.mListeners.get(namespace);
                if (tempConfigListeners != null && tempConfigListeners.size() > 0) {
                    configListeners.addAll(tempConfigListeners);
                }
            }
            if (configListeners.size() > 0) {
                if (OLog.isPrintLog(1)) {
                    OLog.d(TAG, "notifyListeners ", "namespace", namespace, "args", args, "listenerSet.size", Integer.valueOf(configListeners.size()));
                }
                for (ParcelableConfigListener listener : configListeners) {
                    try {
                        listener.onConfigUpdate(namespace, args);
                    } catch (Throwable t2) {
                        OLog.w(TAG, "notifyListeners", t2, new Object[0]);
                    }
                }
            }
        }
    }

    public synchronized void updateIndex(IndexUpdateHandler.IndexUpdateInfo indexUpdInfo) {
        if (loadIndex(indexUpdInfo)) {
            long loadStartTime = System.currentTimeMillis();
            Set<String> localNamespaces = new HashSet<>();
            localNamespaces.addAll(this.mConfigCache.getConfigMap().keySet());
            synchronized (this.mFailRequestsSet) {
                localNamespaces.addAll(this.mFailRequestsSet);
            }
            Set<NameSpaceDO> updateNameSpaceList = this.mIndexCache.getUpdateNameSpaces(localNamespaces);
            OLog.i(TAG, "updateIndex", "start load updateNameSpaces", Integer.valueOf(updateNameSpaceList.size()));
            for (NameSpaceDO nameSpaceDO : updateNameSpaceList) {
                loadConfig(nameSpaceDO);
            }
            OLog.i(TAG, "updateIndex", "finish load updateNameSpaces", Integer.valueOf(updateNameSpaceList.size()), "cost(ms)", Long.valueOf(System.currentTimeMillis() - loadStartTime));
        } else if (OLog.isPrintLog(0)) {
            OLog.v(TAG, "updateIndex", "no need update or update fail index file");
        }
    }

    private boolean loadIndex(IndexUpdateHandler.IndexUpdateInfo indexUpdInfo) {
        if (indexUpdInfo == null || TextUtils.isEmpty(indexUpdInfo.cdn) || TextUtils.isEmpty(indexUpdInfo.resourceId) || TextUtils.isEmpty(indexUpdInfo.md5)) {
            OLog.e(TAG, "updateIndex param is null", new Object[0]);
            return false;
        } else if (TextUtils.isEmpty(this.mIndexCache.getIndex().md5) || !this.mIndexCache.getIndex().md5.equals(indexUpdInfo.md5)) {
            if (((long) GlobalOrange.indexContinueFailsNum.get()) >= FAIL_LOAD_INDEX_UPD_NUM) {
                long startUpdTime = System.currentTimeMillis();
                if (failLastIndexUpdTime == 0) {
                    failLastIndexUpdTime = startUpdTime;
                    if (OLog.isPrintLog(3)) {
                        OLog.w(TAG, "updateIndex continuous fail numbers exceed 10", new Object[0]);
                    }
                    return false;
                } else if (startUpdTime - failLastIndexUpdTime <= FAIL_LOAD_INDEX_UPD_INTERVAL) {
                    return false;
                } else {
                    GlobalOrange.indexContinueFailsNum.set(0);
                    failLastIndexUpdTime = 0;
                    if (OLog.isPrintLog(3)) {
                        OLog.w(TAG, "updateIndex continuous fail already wait 100s", new Object[0]);
                    }
                }
            }
            GlobalOrange.indexContinueFailsNum.incrementAndGet();
            if (OLog.isPrintLog(2)) {
                OLog.i(TAG, "loadIndex start", ConnType.PK_CDN, indexUpdInfo.cdn, "resource", indexUpdInfo.resourceId, "md5", indexUpdInfo.md5);
            }
            try {
                StringBuilder builder = new StringBuilder();
                builder.append(GlobalOrange.schema).append(HttpConstant.SCHEME_SPLIT).append(indexUpdInfo.cdn).append(File.separator).append(indexUpdInfo.resourceId);
                BaseRequest<IndexDO> indexReq = new CdnRequest<IndexDO>(builder.toString(), indexUpdInfo.md5) {
                    /* access modifiers changed from: protected */
                    public IndexDO parseResContent(String content) {
                        return (IndexDO) JSON.parseObject(content, IndexDO.class);
                    }
                };
                IndexDO indexDO = indexReq.syncRequest();
                if (indexDO == null || !indexDO.checkValid()) {
                    if (OLog.isPrintLog(0)) {
                        OLog.v(TAG, "loadIndex cdnReq fail downgrade to authReq", "code", indexReq.getCode(), "msg", indexReq.getMessage());
                    }
                    final IndexUpdateHandler.IndexUpdateInfo indexUpdateInfo = indexUpdInfo;
                    indexReq = new AuthRequest<IndexDO>(indexUpdInfo.md5, false, OConstant.REQTYPE_DOWNLOAD_RESOURCE) {
                        /* access modifiers changed from: protected */
                        public Map<String, String> getReqParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("resourceId", indexUpdateInfo.resourceId);
                            return params;
                        }

                        /* access modifiers changed from: protected */
                        public String getReqPostBody() {
                            return null;
                        }

                        /* access modifiers changed from: protected */
                        public IndexDO parseResContent(String content) {
                            return (IndexDO) JSON.parseObject(content, IndexDO.class);
                        }
                    };
                    indexDO = indexReq.syncRequest();
                }
                if (indexDO == null || !indexDO.checkValid()) {
                    if (!"-200".equals(indexReq.getCode())) {
                        if (indexDO != null && !indexDO.checkValid()) {
                            indexReq.setCode(-4);
                            indexReq.setMessage("index is invaild");
                        }
                        OrangeMonitor.commitFail(OConstant.MONITOR_MODULE, OConstant.POINT_INDEX_RATE, indexUpdInfo.resourceId, indexReq.getCode(), indexReq.getMessage());
                    }
                    OLog.e(TAG, "loadIndex fail", "code", indexReq.getCode(), "msg", indexReq.getMessage());
                    return false;
                }
                GlobalOrange.indexContinueFailsNum.set(0);
                if (indexDO.id.equals(this.mIndexCache.getIndex().id) || indexDO.version.equals(this.mIndexCache.getIndex().version)) {
                    OLog.w(TAG, "loadIndex fail", "id or version is match");
                    return false;
                }
                indexDO.md5 = indexUpdInfo.md5;
                List<String> removeNamespaces = this.mIndexCache.cache(indexDO);
                OrangeMonitor.commitSuccess(OConstant.MONITOR_MODULE, OConstant.POINT_INDEX_RATE, indexUpdInfo.resourceId);
                if (OLog.isPrintLog(1)) {
                    OLog.d(TAG, "loadIndex success", "indexDO", OrangeUtils.formatIndexDO(indexDO));
                }
                ReportAckUtils.reportIndexAck(new IndexAckDO(indexDO.id, OrangeUtils.getCurFormatTime(), indexUpdInfo.md5));
                if (removeNamespaces.size() > 0) {
                    if (OLog.isPrintLog(2)) {
                        OLog.i(TAG, "loadIndex remove diff namespace", "removeNamespaces", removeNamespaces);
                    }
                    for (String nameSpace : removeNamespaces) {
                        this.mConfigCache.remove(nameSpace);
                    }
                }
                return true;
            } catch (Throwable t) {
                OrangeMonitor.commitFail(OConstant.MONITOR_MODULE, OConstant.POINT_INDEX_RATE, indexUpdInfo.resourceId, "0", t.getMessage());
                OLog.e(TAG, "loadIndex fail", t, new Object[0]);
            }
        } else {
            OLog.w(TAG, "loadIndex fail", "cdnMd5 is match");
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void updateSystemConfig(Map args) {
        List<String> ackVipList;
        List<String> dcVipList;
        JSONArray jsonArray;
        Map<String, String> orangeConfigs = this.mConfigCache.getConfigs(SYS_NAMESPACE);
        if (OLog.isPrintLog(2)) {
            OLog.i(TAG, "updateSystemConfig", "args", args, "orangeConfigs", orangeConfigs);
        }
        if (orangeConfigs != null && !orangeConfigs.isEmpty()) {
            try {
                String reqRetryNumStr = orangeConfigs.get(SYSKEY_REQ_RETRY_NUM);
                if (!TextUtils.isEmpty(reqRetryNumStr)) {
                    int temp = Integer.parseInt(reqRetryNumStr);
                    if (temp > 5) {
                        temp = 5;
                    }
                    GlobalOrange.reqRetryNum = temp;
                    OLog.i(TAG, "updateSystemConfig", SYSKEY_REQ_RETRY_NUM, Integer.valueOf(GlobalOrange.reqRetryNum));
                }
                String reportUpdateAckStr = orangeConfigs.get(SYSKEY_REPORT_UPDACK);
                if (!TextUtils.isEmpty(reportUpdateAckStr)) {
                    GlobalOrange.reportUpdateAck = Integer.parseInt(reportUpdateAckStr) == 1;
                    OLog.i(TAG, "updateSystemConfig", SYSKEY_REPORT_UPDACK, Boolean.valueOf(GlobalOrange.reportUpdateAck));
                }
                String delayAckIntervalStr = orangeConfigs.get(SYSKEY_DELAYACK_INTERVAL);
                if (!TextUtils.isEmpty(delayAckIntervalStr)) {
                    long delayAckInterval = Long.parseLong(delayAckIntervalStr);
                    OLog.i(TAG, "updateSystemConfig", SYSKEY_DELAYACK_INTERVAL, Long.valueOf(delayAckInterval));
                    if (delayAckInterval > 0) {
                        GlobalOrange.randomDelayAckInterval = updateRandomDelayAckInterval(delayAckInterval);
                        OLog.i(TAG, "updateSystemConfig", "randomDelayAckInterval", Long.valueOf(GlobalOrange.randomDelayAckInterval));
                    }
                }
                String indexUpdateModeStr = orangeConfigs.get(SYSKEY_INDEXUPD_MODE);
                if (!TextUtils.isEmpty(indexUpdateModeStr)) {
                    GlobalOrange.indexUpdMode = OConstant.UPDMODE.valueOf(Integer.parseInt(indexUpdateModeStr));
                    OLog.i(TAG, "updateSystemConfig", "indexUpdMode", GlobalOrange.indexUpdMode);
                }
                String supportHosts = orangeConfigs.get("hosts");
                if (!TextUtils.isEmpty(supportHosts) && (jsonArray = JSON.parseArray(supportHosts)) != null && jsonArray.size() >= 0) {
                    List<String> hosts = new ArrayList<>(jsonArray.size());
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String host = jsonArray.getJSONObject(i).getString("host");
                        if (!TextUtils.isEmpty(host)) {
                            hosts.add(host);
                        }
                    }
                    if (hosts.size() > 0) {
                        GlobalOrange.probeHosts.clear();
                        GlobalOrange.probeHosts.addAll(hosts);
                        OLog.i(TAG, "updateSystemConfig", "probeHosts", GlobalOrange.probeHosts);
                    }
                }
                String dcVips = orangeConfigs.get(SYSKEY_DCVIPS);
                if (!TextUtils.isEmpty(dcVips) && (dcVipList = JSON.parseArray(dcVips, String.class)) != null && dcVipList.size() > 0) {
                    GlobalOrange.dcVips.clear();
                    GlobalOrange.dcVips.addAll(dcVipList);
                    OLog.i(TAG, "updateSystemConfig", SYSKEY_DCVIPS, GlobalOrange.dcVips);
                }
                String ackVips = orangeConfigs.get(SYSKEY_ACKVIPS);
                if (!TextUtils.isEmpty(ackVips) && (ackVipList = JSON.parseArray(ackVips, String.class)) != null && ackVipList.size() > 0) {
                    GlobalOrange.ackVips.clear();
                    GlobalOrange.ackVips.addAll(ackVipList);
                    OLog.i(TAG, "updateSystemConfig", SYSKEY_ACKVIPS, GlobalOrange.ackVips);
                }
            } catch (Throwable t) {
                OLog.e(TAG, "updateSystemConfig", t, new Object[0]);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public long updateRandomDelayAckInterval(long delayAckInterval) {
        if (delayAckInterval == 0) {
            return 0;
        }
        return OrangeUtils.hash(GlobalOrange.deviceId) % (1000 * delayAckInterval);
    }

    public void addFails(String[] namespaces) {
        for (String namespace : namespaces) {
            addFail(namespace);
        }
    }

    private void addFail(String namespace) {
        if (!TextUtils.isEmpty(namespace)) {
            synchronized (this.mFailRequestsSet) {
                if (this.mFailRequestsSet.add(namespace) && OLog.isPrintLog(2)) {
                    OLog.i(TAG, "addFail", "namespace", namespace);
                }
            }
        }
    }

    public void removeFail(String namespace) {
        if (!TextUtils.isEmpty(namespace)) {
            synchronized (this.mFailRequestsSet) {
                if (this.mFailRequestsSet.remove(namespace) && OLog.isPrintLog(2)) {
                    OLog.i(TAG, "removeFail", "namespace", namespace);
                }
            }
        }
    }

    public void retryFailRequests() {
        if (AndroidUtil.isNetworkConnected(GlobalOrange.context)) {
            Set<NameSpaceDO> retryNamespaces = new HashSet<>();
            synchronized (this.mFailRequestsSet) {
                for (String namespace : this.mFailRequestsSet) {
                    NameSpaceDO nameSpaceDO = this.mIndexCache.getNameSpace(namespace);
                    if (nameSpaceDO != null) {
                        retryNamespaces.add(nameSpaceDO);
                    }
                }
            }
            if (!retryNamespaces.isEmpty()) {
                OLog.i(TAG, "retryFailRequests", "start load retryNamespaces", Integer.valueOf(retryNamespaces.size()));
                long loadStartTime = System.currentTimeMillis();
                for (NameSpaceDO nameSpaceDO2 : retryNamespaces) {
                    loadConfig(nameSpaceDO2);
                }
                OLog.i(TAG, "retryFailRequests", "finish load retryNamespaces", Integer.valueOf(retryNamespaces.size()), "cost(ms)", Long.valueOf(System.currentTimeMillis() - loadStartTime));
            } else if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "retryFailRequests no any", new Object[0]);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean checkLoading(java.lang.String r3, boolean r4) {
        /*
            r2 = this;
            java.util.Set<java.lang.String> r1 = r2.mLoadingConfigSet
            monitor-enter(r1)
            java.util.Set<java.lang.String> r0 = r2.mLoadingConfigSet     // Catch:{ all -> 0x0018 }
            boolean r0 = r0.contains(r3)     // Catch:{ all -> 0x0018 }
            if (r0 == 0) goto L_0x000e
            r0 = 1
            monitor-exit(r1)     // Catch:{ all -> 0x0018 }
        L_0x000d:
            return r0
        L_0x000e:
            if (r4 == 0) goto L_0x0015
            java.util.Set<java.lang.String> r0 = r2.mLoadingConfigSet     // Catch:{ all -> 0x0018 }
            r0.add(r3)     // Catch:{ all -> 0x0018 }
        L_0x0015:
            monitor-exit(r1)     // Catch:{ all -> 0x0018 }
            r0 = 0
            goto L_0x000d
        L_0x0018:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0018 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.ConfigCenter.checkLoading(java.lang.String, boolean):boolean");
    }

    private void removeLoading(String namespace) {
        if (!TextUtils.isEmpty(namespace)) {
            synchronized (this.mLoadingConfigSet) {
                this.mLoadingConfigSet.remove(namespace);
            }
        }
    }

    public void addCandidate(OCandidate candidate) {
        if (MultiAnalyze.candidateMap.get(candidate.getKey()) != null) {
            OLog.e(TAG, "addCandidate fail as exist old candidate", "candidate", candidate);
            return;
        }
        if (OLog.isPrintLog(1)) {
            OLog.d(TAG, "addCandidate", "candidate", candidate);
        }
        MultiAnalyze.candidateMap.put(candidate.getKey(), candidate);
    }

    public JSONObject getIndexAndConfigs() {
        try {
            Map<String, JSONObject> all = new HashMap<>();
            all.put("index", getIndex());
            all.put(LoginConstants.CONFIG, getAllConfigs());
            return new JSONObject(all);
        } catch (Exception e) {
            OLog.e(TAG, "getIndexAndConfigs", e, new Object[0]);
            return null;
        }
    }

    public JSONObject getAllConfigs() {
        try {
            return new JSONObject(JSON.toJSONString(OrangeUtils.sortMapByKey(this.mConfigCache.getConfigMap(), true)));
        } catch (Exception e) {
            OLog.e(TAG, "getAllConfigs", e, new Object[0]);
            return null;
        }
    }

    public JSONObject getIndex() {
        try {
            IndexDO indexDO = new IndexDO(this.mIndexCache.getIndex());
            Collections.sort(indexDO.mergedNamespaces, new Comparator<NameSpaceDO>() {
                public int compare(NameSpaceDO n1, NameSpaceDO n2) {
                    return n1.name.compareTo(n2.name);
                }
            });
            return new JSONObject(JSON.toJSONString(indexDO));
        } catch (Exception e) {
            OLog.e(TAG, "getIndex", e, new Object[0]);
            return null;
        }
    }
}
