package com.taobao.orange;

import anetwork.channel.util.RequestConstant;

public class OConstant {
    public static final String[] ACK_TAOBAO_HOSTS = {"orange-ack.alibaba.com", "orange-ack-pre.alibaba.com", "orange-ack-daily.alibaba.net"};
    public static final String[] ACK_YOUKU_HOSTS = {"orange-ack.youku.com", "orange-ack-pre.youku.com", "orange-ack-daily.heyi.test"};
    public static final String CANDIDATE_APPVER = "app_ver";
    public static final String CANDIDATE_BRAND = "m_brand";
    public static final String CANDIDATE_HASH_DIS = "did_hash";
    public static final String CANDIDATE_MANUFACTURER = "m_fac";
    public static final String CANDIDATE_MODEL = "m_model";
    public static final String CANDIDATE_OSVER = "os_ver";
    public static final String[] DC_TAOBAO_HOSTS = {"orange-dc.alibaba.com", "orange-dc-pre.alibaba.com", "orange-dc-daily.alibaba.net"};
    public static final String[] DC_YOUKU_HOSTS = {"orange-dc.youku.com", "orange-dc-pre.youku.com", "orange-dc-daily.heyi.test"};
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String KEY_APPKEY = "appKey";
    public static final String KEY_APPVERSION = "appVersion";
    public static final String KEY_CLIENTAPPINDEXVERSION = "clientAppIndexVersion";
    public static final String KEY_CLIENTVERSIONINDEXVERSION = "clientVersionIndexVersion";
    public static final String LAUNCH_APPVERSION = "appVersion";
    public static final String LAUNCH_ENVINDEX = "envIndex";
    public static final String LAUNCH_KEY_USERID = "userId";
    public static final String LAUNCH_ONLINEAPPKEY = "onlineAppKey";
    public static final String LAUNCH_PREAPPKEY = "preAppKey";
    public static final String LAUNCH_TESTAPPKEY = "dailyAppkey";
    public static final String LISTENERKEY_CONFIG_VERSION = "configVersion";
    public static final String LISTENERKEY_FROM_CACHE = "fromCache";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String MONITOR_MODULE = "OrangeConfig";
    public static final String MONITOR_PRIVATE_MODULE = "private_orange";
    public static final String POINT_CDNREQ_CFG_RATE = "ORANGE_GROUP_RATE_POINT";
    public static final String POINT_CFG_RATE = "config_rate";
    public static final String POINT_CONFIG_ACK_COUNTS = "config_ack_counts";
    public static final String POINT_EXCEPTION = "ORANGE_EXCEPTION";
    public static final String POINT_INDEX_ACK_COUNTS = "index_ack_counts";
    public static final String POINT_INDEX_RATE = "index_rate";
    public static final String[][] PROBE_HOSTS = {new String[]{"acs.m.taobao.com"}, new String[]{"acs.wapa.taobao.com"}, new String[]{"acs.waptest.taobao.com"}};
    public static final String REFLECT_APPMONITOR = "com.alibaba.mtl.appmonitor.AppMonitor";
    public static final String REFLECT_NETWORKSDK = "anetwork.channel.degrade.DegradableNetwork";
    public static final String REFLECT_NETWORK_INTERCEPTOR = "anetwork.channel.interceptor.Interceptor";
    public static final String REFLECT_NETWORK_INTERCEPTORMANAGER = "anetwork.channel.interceptor.InterceptorManager";
    public static final String REFLECT_SECURITYGUARD = "com.alibaba.wireless.security.open.SecurityGuardManager";
    public static final String REFLECT_TLOG = "com.taobao.tlog.adapter.AdapterForTLog";
    public static final String REQTYPE_ACK_CONFIG_UPDATE = "/batchNamespaceUpdateAck";
    public static final String REQTYPE_ACK_INDEX_UPDATE = "/indexUpdateAck";
    public static final String REQTYPE_DOWNLOAD_RESOURCE = "/downloadResource";
    public static final String REQTYPE_INDEX_UPDATE = "/checkUpdate";
    public static final String SDK_VERSION = "1.5.3.28";
    public static final String UTF_8 = "utf-8";

    public enum ENV {
        ONLINE(0, RequestConstant.ENV_ONLINE),
        PREPARE(1, RequestConstant.ENV_PRE),
        TEST(2, RequestConstant.ENV_TEST);
        
        private String des;
        private int envMode;

        private ENV(int envMode2, String des2) {
            this.envMode = envMode2;
            this.des = des2;
        }

        public int getEnvMode() {
            return this.envMode;
        }

        public String getDes() {
            return this.des;
        }

        public static ENV valueOf(int envMode2) {
            switch (envMode2) {
                case 0:
                    return ONLINE;
                case 1:
                    return PREPARE;
                case 2:
                    return TEST;
                default:
                    return ONLINE;
            }
        }
    }

    public enum SERVER {
        TAOBAO,
        YOUKU;

        public static SERVER valueOf(int serverType) {
            switch (serverType) {
                case 0:
                    return TAOBAO;
                case 1:
                    return YOUKU;
                default:
                    return TAOBAO;
            }
        }
    }

    public enum UPDMODE {
        O_XMD,
        O_EVENT,
        O_ALL;

        public static UPDMODE valueOf(int indexUpdMode) {
            switch (indexUpdMode) {
                case 0:
                    return O_XMD;
                case 1:
                    return O_EVENT;
                case 2:
                    return O_ALL;
                default:
                    return O_XMD;
            }
        }
    }
}
