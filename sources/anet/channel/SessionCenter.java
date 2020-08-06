package anet.channel;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.LruCache;
import anet.channel.Config;
import anet.channel.entity.ConnType;
import anet.channel.entity.ENV;
import anet.channel.security.ISecurity;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.IStrategyListener;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.dispatch.IAmdcSign;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import anet.channel.util.SessionSeq;
import anet.channel.util.StringUtils;
import anet.channel.util.Utils;
import anetwork.channel.util.RequestConstant;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import com.uc.webview.export.internal.interfaces.IWaStat;
import java.net.ConnectException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;

public class SessionCenter {
    public static final String TAG = "awcn.SessionCenter";
    static Map<Config, SessionCenter> instancesMap = new HashMap();
    /* access modifiers changed from: private */
    public static boolean mInit = false;
    final AccsSessionManager accsSessionManager;
    final SessionAttributeManager attributeManager = new SessionAttributeManager();
    Config config;
    Context context = GlobalAppRuntimeInfo.getContext();
    final InnerListener innerListener = new InnerListener();
    String seqNum;
    final SessionPool sessionPool = new SessionPool();
    final LruCache<String, SessionRequest> srCache = new LruCache<>(32);

    public static synchronized void init(Context context2) {
        synchronized (SessionCenter.class) {
            if (context2 == null) {
                ALog.e(TAG, "paramter context is null!", (String) null, new Object[0]);
                throw new NullPointerException("init failed. paramter context is null");
            }
            GlobalAppRuntimeInfo.setContext(context2.getApplicationContext());
            if (!mInit) {
                instancesMap.put(Config.DEFAULT_CONFIG, new SessionCenter(Config.DEFAULT_CONFIG));
                AppLifecycle.initialize();
                StrategyCenter.getInstance().initialize(GlobalAppRuntimeInfo.getContext());
                mInit = true;
            }
        }
    }

    @Deprecated
    public static synchronized void init(Context context2, String appkey) {
        synchronized (SessionCenter.class) {
            init(context2, appkey, GlobalAppRuntimeInfo.getEnv());
        }
    }

    public static synchronized void init(Context context2, String appkey, ENV env) {
        synchronized (SessionCenter.class) {
            if (context2 == null) {
                ALog.e(TAG, "paramter context is null!", (String) null, new Object[0]);
                throw new NullPointerException("init failed. paramter context is null");
            }
            Config config2 = Config.getConfig(appkey, env);
            if (config2 == null) {
                config2 = new Config.Builder().setAppkey(appkey).setEnv(env).build();
            }
            init(context2, config2);
        }
    }

    public static synchronized void init(Context context2, Config config2) {
        synchronized (SessionCenter.class) {
            if (context2 == null) {
                ALog.e(TAG, "paramter context is null!", (String) null, new Object[0]);
                throw new NullPointerException("init failed. paramter context is null");
            } else if (config2 == null) {
                ALog.e(TAG, "paramter config is null!", (String) null, new Object[0]);
                throw new NullPointerException("init failed. paramter config is null");
            } else {
                init(context2);
                if (!instancesMap.containsKey(config2)) {
                    instancesMap.put(config2, new SessionCenter(config2));
                }
            }
        }
    }

    private SessionCenter(Config config2) {
        this.config = config2;
        this.seqNum = config2.getAppkey();
        this.innerListener.registerAll();
        this.accsSessionManager = new AccsSessionManager(this);
        if (!config2.getAppkey().equals("[default]")) {
            final ISecurity iSecurity = config2.getSecurity();
            final String appkey = config2.getAppkey();
            AmdcRuntimeInfo.setSign(new IAmdcSign() {
                public String getAppkey() {
                    return appkey;
                }

                public String sign(String input) {
                    return iSecurity.sign(SessionCenter.this.context, ISecurity.SIGN_ALGORITHM_HMAC_SHA1, getAppkey(), input);
                }

                public boolean useSecurityGuard() {
                    return !iSecurity.isSecOff();
                }
            });
        }
    }

    private void dispose() {
        ALog.i(TAG, "instance dispose", this.seqNum, new Object[0]);
        this.accsSessionManager.forceCloseSession(false);
        this.innerListener.unRegisterAll();
    }

    @Deprecated
    public synchronized void switchEnv(ENV env) {
        switchEnvironment(env);
    }

    public static synchronized void switchEnvironment(ENV env) {
        int i = 0;
        synchronized (SessionCenter.class) {
            try {
                if (GlobalAppRuntimeInfo.getEnv() != env) {
                    ALog.i(TAG, "switch env", (String) null, IWaStat.KEY_OLD, GlobalAppRuntimeInfo.getEnv(), "new", env);
                    GlobalAppRuntimeInfo.setEnv(env);
                    StrategyCenter.getInstance().switchEnv();
                    SpdyAgent spdyAgent = SpdyAgent.getInstance(GlobalAppRuntimeInfo.getContext(), SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
                    if (env != ENV.TEST) {
                        i = 1;
                    }
                    spdyAgent.switchAccsServer(i);
                }
                Iterator<Map.Entry<Config, SessionCenter>> iterator = instancesMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    SessionCenter instance = (SessionCenter) iterator.next().getValue();
                    if (instance.config.getEnv() != env) {
                        ALog.i(TAG, "remove instance", instance.seqNum, RequestConstant.ENVIRONMENT, instance.config.getEnv());
                        instance.dispose();
                        iterator.remove();
                    }
                }
            } catch (Throwable e) {
                ALog.e(TAG, "switch env error.", (String) null, e, new Object[0]);
            }
        }
        return;
    }

    public static synchronized SessionCenter getInstance(String tag) {
        SessionCenter instance;
        synchronized (SessionCenter.class) {
            Config config2 = Config.getConfigByTag(tag);
            if (config2 == null) {
                throw new RuntimeException("tag not exist!");
            }
            instance = getInstance(config2);
        }
        return instance;
    }

    public static synchronized SessionCenter getInstance(Config config2) {
        SessionCenter instance;
        Context context2;
        synchronized (SessionCenter.class) {
            if (config2 == null) {
                throw new NullPointerException("config is null!");
            }
            if (!mInit && (context2 = Utils.getAppContext()) != null) {
                init(context2);
            }
            instance = instancesMap.get(config2);
            if (instance == null) {
                instance = new SessionCenter(config2);
                instancesMap.put(config2, instance);
            }
        }
        return instance;
    }

    @Deprecated
    public static synchronized SessionCenter getInstance() {
        SessionCenter instance;
        Context context2;
        synchronized (SessionCenter.class) {
            if (!mInit && (context2 = Utils.getAppContext()) != null) {
                init(context2);
            }
            instance = null;
            for (Map.Entry<Config, SessionCenter> entry : instancesMap.entrySet()) {
                instance = entry.getValue();
                if (entry.getKey() != Config.DEFAULT_CONFIG) {
                    break;
                }
            }
        }
        return instance;
    }

    public Session getThrowsException(String u, long timeout) throws Exception {
        return getThrowsException(u, (ConnType.TypeLevel) null, timeout);
    }

    public Session getThrowsException(String u, ConnType.TypeLevel typeClass, long timeout) throws Exception {
        return getInternal(HttpUrl.parse(u), typeClass, timeout);
    }

    public Session getThrowsException(HttpUrl httpUrl, ConnType.TypeLevel typeClass, long timeout) throws Exception {
        return getInternal(httpUrl, typeClass, timeout);
    }

    public Session get(String url, long timeout) {
        return get(url, (ConnType.TypeLevel) null, timeout);
    }

    public Session get(String url, ConnType.TypeLevel typeClass, long timeout) {
        return get(HttpUrl.parse(url), typeClass, timeout);
    }

    public Session get(HttpUrl httpUrl, ConnType.TypeLevel typeClass, long timeout) {
        try {
            return getInternal(httpUrl, typeClass, timeout);
        } catch (InvalidParameterException e) {
            ALog.e(TAG, "[Get]param url is invaild", this.seqNum, e, "url", httpUrl.urlString());
            return null;
        } catch (TimeoutException e2) {
            ALog.e(TAG, "[Get]timeout exception", this.seqNum, e2, "url", httpUrl.urlString());
            return null;
        } catch (NoNetworkException e3) {
            ALog.e(TAG, "[Get]no network", this.seqNum, "url", httpUrl.urlString());
            return null;
        } catch (NoAvailStrategyException e4) {
            ALog.w(TAG, "[Get]no strategy", this.seqNum, "url", httpUrl.urlString());
            return null;
        } catch (ConnectException e5) {
            ALog.e(TAG, "[Get]connect exception", this.seqNum, "errMsg", e5.getMessage(), "url", httpUrl.urlString());
            return null;
        } catch (Exception e6) {
            ALog.e(TAG, "[Get]exception", this.seqNum, e6, "url", httpUrl.urlString());
            return null;
        }
    }

    public void registerSessionInfo(SessionInfo info) {
        this.attributeManager.registerSessionInfo(info);
        if (info.isKeepAlive) {
            this.accsSessionManager.checkAndStartSession();
        }
    }

    public void unregisterSessionInfo(String host) {
        SessionInfo info = this.attributeManager.unregisterSessionInfo(host);
        if (info != null && info.isKeepAlive) {
            this.accsSessionManager.checkAndStartSession();
        }
    }

    public void registerPublicKey(String host, int publicKey) {
        this.attributeManager.registerPublicKey(host, publicKey);
    }

    public static void checkAndStartAccsSession() {
        for (SessionCenter instance : instancesMap.values()) {
            instance.accsSessionManager.checkAndStartSession();
        }
    }

    public void forceRecreateAccsSession() {
        this.accsSessionManager.forceReCreateSession();
    }

    /* access modifiers changed from: protected */
    public Session getInternal(HttpUrl httpUrl, ConnType.TypeLevel typeClass, long timeout) throws Exception {
        String host;
        SessionInfo sessionInfo;
        if (!mInit) {
            ALog.e(TAG, "getInternal not inited!", this.seqNum, new Object[0]);
            return null;
        } else if (httpUrl == null) {
            return null;
        } else {
            ALog.d(TAG, "getInternal", this.seqNum, "u", httpUrl.urlString(), "TypeClass", typeClass, MtopJSBridge.MtopJSParam.TIMEOUT, Long.valueOf(timeout));
            String cname = StrategyCenter.getInstance().getCNameByHost(httpUrl.host());
            if (cname == null) {
                host = httpUrl.host();
            } else {
                host = cname;
            }
            String scheme = httpUrl.scheme();
            if (!httpUrl.isSchemeLocked()) {
                scheme = StrategyCenter.getInstance().getSchemeByHost(host, scheme);
            }
            SessionRequest request = getSessionRequest(StringUtils.concatString(scheme, HttpConstant.SCHEME_SPLIT, host));
            Session session = this.sessionPool.getSession(request, typeClass);
            if (session != null) {
                ALog.d(TAG, "get internal hit cache session", this.seqNum, "session", session);
                return session;
            } else if (this.config == Config.DEFAULT_CONFIG && typeClass == ConnType.TypeLevel.SPDY) {
                return null;
            } else {
                if (!GlobalAppRuntimeInfo.isAppBackground() || typeClass != ConnType.TypeLevel.SPDY || !AwcnConfig.isAccsSessionCreateForbiddenInBg() || (sessionInfo = this.attributeManager.getSessionInfo(httpUrl.host())) == null || !sessionInfo.isAccs) {
                    request.start(this.context, typeClass, SessionSeq.createSequenceNo(this.seqNum));
                    if (timeout <= 0 || request.getConnectingType() != typeClass) {
                        return session;
                    }
                    request.await(timeout);
                    Session session2 = this.sessionPool.getSession(request, typeClass);
                    if (session2 != null) {
                        return session2;
                    }
                    throw new ConnectException("session connecting failed or timeout");
                }
                ALog.w(TAG, "app background, forbid to create accs session", this.seqNum, new Object[0]);
                throw new ConnectException("accs session connecting forbidden in background");
            }
        }
    }

    @Deprecated
    public void enterBackground() {
        AppLifecycle.onBackground();
    }

    @Deprecated
    public void enterForeground() {
        AppLifecycle.onForeground();
    }

    /* access modifiers changed from: private */
    public void checkStrategy(StrategyResultParser.HttpDnsResponse response) {
        StrategyResultParser.DnsInfo[] dnsInfos = response.dnsInfo;
        for (StrategyResultParser.DnsInfo dnsInfo : dnsInfos) {
            if (dnsInfo.effectNow) {
                handleEffectNow(dnsInfo);
            }
            if (dnsInfo.unit != null) {
                handleUnitChange(dnsInfo);
            }
        }
    }

    private void handleUnitChange(StrategyResultParser.DnsInfo dnsInfo) {
        for (Session session : this.sessionPool.getSessions(getSessionRequest(StringUtils.buildKey(dnsInfo.safeAisles, dnsInfo.host)))) {
            if (!StringUtils.isStringEqual(session.unit, dnsInfo.unit)) {
                ALog.i(TAG, "unit change", session.mSeq, "session unit", session.unit, "unit", dnsInfo.unit);
                session.close(true);
            }
        }
    }

    private void handleEffectNow(StrategyResultParser.DnsInfo dnsInfo) {
        ALog.i(TAG, "find effectNow", this.seqNum, "host", dnsInfo.host);
        StrategyResultParser.Aisles[] aisles = dnsInfo.aisleses;
        String[] ips = dnsInfo.ips;
        for (Session session : this.sessionPool.getSessions(getSessionRequest(StringUtils.buildKey(dnsInfo.safeAisles, dnsInfo.host)))) {
            if (!session.getConnType().isHttpType()) {
                boolean isIpMatch = false;
                int j = 0;
                while (true) {
                    if (j >= ips.length) {
                        break;
                    } else if (session.getIp().equals(ips[j])) {
                        isIpMatch = true;
                        break;
                    } else {
                        j++;
                    }
                }
                if (!isIpMatch) {
                    if (ALog.isPrintLog(2)) {
                        ALog.i(TAG, "ip not match", session.mSeq, "session ip", session.getIp(), "ips", Arrays.toString(ips));
                    }
                    session.close(true);
                } else {
                    boolean isAisleMatch = false;
                    int k = 0;
                    while (true) {
                        if (k < aisles.length) {
                            if (session.getPort() == aisles[k].port && session.getConnType().equals(ConnType.valueOf(ConnProtocol.valueOf(aisles[k])))) {
                                isAisleMatch = true;
                                break;
                            }
                            k++;
                        } else {
                            break;
                        }
                    }
                    if (!isAisleMatch) {
                        if (ALog.isPrintLog(2)) {
                            ALog.i(TAG, "aisle not match", session.mSeq, "port", Integer.valueOf(session.getPort()), "connType", session.getConnType(), "aisle", Arrays.toString(aisles));
                        }
                        session.close(true);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public SessionRequest getSessionRequest(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        synchronized (this.srCache) {
            try {
                SessionRequest ret = this.srCache.get(key);
                if (ret == null) {
                    SessionRequest ret2 = new SessionRequest(key, this);
                    try {
                        this.srCache.put(key, ret2);
                        ret = ret2;
                    } catch (Throwable th) {
                        th = th;
                        SessionRequest sessionRequest = ret2;
                        throw th;
                    }
                }
                return ret;
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    private class InnerListener implements NetworkStatusHelper.INetworkStatusChangeListener, AppLifecycle.AppLifecycleListener, IStrategyListener {
        boolean foreGroundCheckRunning;

        private InnerListener() {
            this.foreGroundCheckRunning = false;
        }

        /* access modifiers changed from: package-private */
        public void registerAll() {
            AppLifecycle.registerLifecycleListener(this);
            NetworkStatusHelper.addStatusChangeListener(this);
            StrategyCenter.getInstance().registerListener(this);
        }

        /* access modifiers changed from: package-private */
        public void unRegisterAll() {
            StrategyCenter.getInstance().unregisterListener(this);
            AppLifecycle.unregisterLifecycleListener(this);
            NetworkStatusHelper.removeStatusChangeListener(this);
        }

        public void onNetworkStatusChanged(NetworkStatusHelper.NetworkStatus networkStatus) {
            ALog.e(SessionCenter.TAG, "onNetworkStatusChanged. reCreateSession", SessionCenter.this.seqNum, "networkStatus", networkStatus);
            List<SessionRequest> infos = SessionCenter.this.sessionPool.getInfos();
            if (!infos.isEmpty()) {
                for (SessionRequest request : infos) {
                    ALog.d(SessionCenter.TAG, "network change, try recreate session", SessionCenter.this.seqNum, new Object[0]);
                    request.reCreateSession((String) null);
                }
            } else {
                ALog.i(SessionCenter.TAG, "recreate session failed: infos is empty", SessionCenter.this.seqNum, new Object[0]);
            }
            SessionCenter.this.accsSessionManager.checkAndStartSession();
        }

        public void onStrategyUpdated(StrategyResultParser.HttpDnsResponse response) {
            SessionCenter.this.checkStrategy(response);
            SessionCenter.this.accsSessionManager.checkAndStartSession();
        }

        public void forground() {
            ALog.i(SessionCenter.TAG, "[forground]", SessionCenter.this.seqNum, new Object[0]);
            if (SessionCenter.this.context != null && !this.foreGroundCheckRunning) {
                this.foreGroundCheckRunning = true;
                if (!SessionCenter.mInit) {
                    ALog.e(SessionCenter.TAG, "forground not inited!", SessionCenter.this.seqNum, new Object[0]);
                    return;
                }
                try {
                    ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
                        public void run() {
                            try {
                                if (AppLifecycle.lastEnterBackgroundTime == 0 || System.currentTimeMillis() - AppLifecycle.lastEnterBackgroundTime <= 300000) {
                                    SessionCenter.this.accsSessionManager.checkAndStartSession();
                                } else {
                                    SessionCenter.this.accsSessionManager.forceCloseSession(true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                InnerListener.this.foreGroundCheckRunning = false;
                            }
                        }
                    });
                } catch (Exception e) {
                }
            }
        }

        public void background() {
            ALog.i(SessionCenter.TAG, "[background]", SessionCenter.this.seqNum, new Object[0]);
            if (!SessionCenter.mInit) {
                ALog.e(SessionCenter.TAG, "background not inited!", SessionCenter.this.seqNum, new Object[0]);
                return;
            }
            try {
                StrategyCenter.getInstance().saveData();
                if ("OPPO".equalsIgnoreCase(Build.BRAND)) {
                    ALog.i(SessionCenter.TAG, "close session for OPPO", SessionCenter.this.seqNum, new Object[0]);
                    SessionCenter.this.accsSessionManager.forceCloseSession(false);
                }
            } catch (Exception e) {
            }
        }
    }
}
