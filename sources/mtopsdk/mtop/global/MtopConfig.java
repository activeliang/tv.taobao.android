package mtopsdk.mtop.global;

import android.content.Context;
import android.support.annotation.NonNull;
import anetwork.network.cache.Cache;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.log.LogAdapter;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.manager.FilterManager;
import mtopsdk.mtop.antiattack.AntiAttackHandler;
import mtopsdk.mtop.domain.EntranceEnum;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.network.NetworkPropertyService;
import mtopsdk.mtop.stat.IUploadStats;
import mtopsdk.network.Call;
import mtopsdk.security.ISign;

public class MtopConfig {
    private static final String TAG = "mtopsdk.MtopConfig";
    public static LogAdapter logAdapterImpl;
    public AntiAttackHandler antiAttackHandler;
    public String appKey;
    public int appKeyIndex;
    public String appVersion;
    public String authCode;
    public Cache cacheImpl;
    public Call.Factory callFactory = null;
    public Context context;
    public int dailyAppkeyIndex = 0;
    public String deviceId;
    public volatile boolean enableHeaderUrlEncode = false;
    public volatile boolean enableNewDeviceId = true;
    public EntranceEnum entrance = EntranceEnum.GW_INNER;
    public EnvModeEnum envMode = EnvModeEnum.ONLINE;
    public FilterManager filterManager = null;
    public final String instanceId;
    public AtomicBoolean isAllowSwitchEnv = new AtomicBoolean(true);
    protected AtomicBoolean loadPropertyFlag = new AtomicBoolean(false);
    public final byte[] lock = new byte[0];
    public final MtopDomain mtopDomain = new MtopDomain();
    public final Set<Integer> mtopFeatures = new CopyOnWriteArraySet();
    public final Map<String, String> mtopGlobalABTestParams = new ConcurrentHashMap();
    public final Map<String, String> mtopGlobalHeaders = new ConcurrentHashMap();
    public final Map<String, String> mtopGlobalQuerys = new ConcurrentHashMap();
    public Mtop mtopInstance;
    protected final Map<String, String> mtopProperties = new ConcurrentHashMap();
    public NetworkPropertyService networkPropertyService;
    public volatile boolean notifySessionResult = false;
    public int onlineAppKeyIndex = 0;
    public int processId;
    public volatile ISign sign;
    public String ttid;
    public IUploadStats uploadStats;
    public String utdid;
    public String wuaAuthCode;
    public volatile long xAppConfigVersion;
    public String xOrangeQ;

    public MtopConfig(String instanceId2) {
        this.instanceId = instanceId2;
    }

    public static class MtopDomain {
        public static final int FOR_DAILY = 2;
        public static final int FOR_DAILY_2ND = 3;
        public static final int FOR_ONLINE = 0;
        public static final int FOR_PREPARED = 1;
        final String[] defaultDomains = new String[4];

        MtopDomain() {
            this.defaultDomains[0] = "acs.m.taobao.com";
            this.defaultDomains[1] = "acs.wapa.taobao.com";
            this.defaultDomains[2] = "acs.waptest.taobao.com";
            this.defaultDomains[3] = "api.waptest2nd.taobao.com";
        }

        public String getDomain(EnvModeEnum envMode) {
            switch (envMode) {
                case ONLINE:
                    return this.defaultDomains[0];
                case PREPARE:
                    return this.defaultDomains[1];
                case TEST:
                    return this.defaultDomains[2];
                case TEST_SANDBOX:
                    return this.defaultDomains[3];
                default:
                    return this.defaultDomains[0];
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:3:0x0011, code lost:
            r2.defaultDomains[1] = r4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:4:0x0016, code lost:
            r2.defaultDomains[2] = r4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:5:0x001b, code lost:
            r2.defaultDomains[3] = r4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void updateDomain(mtopsdk.mtop.domain.EnvModeEnum r3, java.lang.String r4) {
            /*
                r2 = this;
                int[] r0 = mtopsdk.mtop.global.MtopConfig.AnonymousClass1.$SwitchMap$mtopsdk$mtop$domain$EnvModeEnum
                int r1 = r3.ordinal()
                r0 = r0[r1]
                switch(r0) {
                    case 1: goto L_0x000c;
                    case 2: goto L_0x0011;
                    case 3: goto L_0x0016;
                    case 4: goto L_0x001b;
                    default: goto L_0x000b;
                }
            L_0x000b:
                return
            L_0x000c:
                java.lang.String[] r0 = r2.defaultDomains
                r1 = 0
                r0[r1] = r4
            L_0x0011:
                java.lang.String[] r0 = r2.defaultDomains
                r1 = 1
                r0[r1] = r4
            L_0x0016:
                java.lang.String[] r0 = r2.defaultDomains
                r1 = 2
                r0[r1] = r4
            L_0x001b:
                java.lang.String[] r0 = r2.defaultDomains
                r1 = 3
                r0[r1] = r4
                goto L_0x000b
            */
            throw new UnsupportedOperationException("Method not decompiled: mtopsdk.mtop.global.MtopConfig.MtopDomain.updateDomain(mtopsdk.mtop.domain.EnvModeEnum, java.lang.String):void");
        }
    }

    public void registerMtopSdkProperty(@NonNull String key, @NonNull String value) {
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
            getMtopProperties().put(key, value);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(TAG, "[registerMtopSdkProperty]register MtopSdk Property succeed,key=" + key + ",value=" + value);
            }
        }
    }

    public Map<String, String> getMtopProperties() {
        if (this.loadPropertyFlag.compareAndSet(false, true)) {
            try {
                InputStream is = this.context.getAssets().open("mtopsdk.property");
                Properties properties = new Properties();
                properties.load(is);
                if (!properties.isEmpty()) {
                    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                        try {
                            Object key = entry.getKey();
                            Object value = entry.getValue();
                            if (key == null || value == null) {
                                TBSdkLog.e(TAG, "invalid mtopsdk property,key=" + key + ",value=" + value);
                            } else {
                                this.mtopProperties.put(key.toString(), value.toString());
                            }
                        } catch (Exception e) {
                            TBSdkLog.e(TAG, "load mtopsdk.property in android assets directory error.", (Throwable) e);
                        }
                    }
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, " load mtopsdk.property file in android assets directory succeed");
                }
            } catch (Exception e2) {
                TBSdkLog.e(TAG, "load mtopsdk.property in android assets directory failed!");
            }
        }
        return this.mtopProperties;
    }
}
