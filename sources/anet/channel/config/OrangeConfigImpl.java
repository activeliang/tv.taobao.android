package anet.channel.config;

import anet.channel.AwcnConfig;
import anet.channel.strategy.SchemeGuesser;
import anet.channel.util.ALog;
import anetwork.channel.config.IRemoteConfig;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.statist.StatisticReqTimes;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;

public class OrangeConfigImpl implements IRemoteConfig {
    private static final String NETWORK_ACCS_SESSION_BG_SWITCH = "network_accs_session_bg_switch";
    private static final String NETWORK_EMPTY_SCHEME_HTTPS_SWITCH = "network_empty_scheme_https_switch";
    private static final String NETWORK_HTTPS_SNI_ENABLE_SWITCH = "network_https_sni_enable_switch";
    private static final String NETWORK_HTTP_CACHE_FLAG = "network_http_cache_flag";
    private static final String NETWORK_HTTP_CACHE_SWITCH = "network_http_cache_switch";
    private static final String NETWORK_MONITOR_WHITELIST_URL = "network_monitor_whitelist_url";
    private static final String NETWORK_SDK_GROUP = "networkSdk";
    private static final String NETWORK_SPDY_ENABLE_SWITCH = "network_spdy_enable_switch";
    private static final String TAG = "awcn.OrangeConfigImpl";
    private static boolean mOrangeValid;

    static {
        mOrangeValid = false;
        try {
            Class.forName("com.taobao.orange.OrangeConfig");
            mOrangeValid = true;
        } catch (Exception e) {
            mOrangeValid = false;
        }
    }

    public void register() {
        if (!mOrangeValid) {
            ALog.w(TAG, "no orange sdk", (String) null, new Object[0]);
            return;
        }
        try {
            OrangeConfig.getInstance().registerListener(new String[]{NETWORK_SDK_GROUP}, (OrangeConfigListenerV1) new OrangeConfigListenerV1() {
                public void onConfigUpdate(String s, boolean b) {
                    OrangeConfigImpl.this.onConfigUpdate(s);
                }
            });
            getConfig(NETWORK_SDK_GROUP, NETWORK_EMPTY_SCHEME_HTTPS_SWITCH, "true");
            StatisticReqTimes.getIntance().updateWhiteReqUrls(getConfig(NETWORK_SDK_GROUP, NETWORK_MONITOR_WHITELIST_URL, null));
        } catch (Exception e) {
            ALog.e(TAG, "register fail", (String) null, e, new Object[0]);
        }
    }

    public void unRegister() {
        if (!mOrangeValid) {
            ALog.w(TAG, "no orange sdk", (String) null, new Object[0]);
            return;
        }
        OrangeConfig.getInstance().unregisterListener(new String[]{NETWORK_SDK_GROUP});
    }

    public String getConfig(String... args) {
        if (!mOrangeValid) {
            ALog.w(TAG, "no orange sdk", (String) null, new Object[0]);
            return null;
        }
        try {
            return OrangeConfig.getInstance().getConfig(args[0], args[1], args[2]);
        } catch (Exception e) {
            ALog.e(TAG, "get config failed!", (String) null, e, new Object[0]);
            return null;
        }
    }

    public void onConfigUpdate(String namespace) {
        if (NETWORK_SDK_GROUP.equals(namespace)) {
            ALog.i(TAG, "onConfigUpdate", (String) null, "namespace", namespace);
            try {
                SchemeGuesser.getInstance().setEnabled(Boolean.valueOf(getConfig(namespace, NETWORK_EMPTY_SCHEME_HTTPS_SWITCH, "true")).booleanValue());
            } catch (Exception e) {
            }
            try {
                NetworkConfigCenter.setSpdyEnabled(Boolean.valueOf(getConfig(namespace, NETWORK_SPDY_ENABLE_SWITCH, "true")).booleanValue());
            } catch (Exception e2) {
            }
            try {
                NetworkConfigCenter.setHttpCacheEnable(Boolean.valueOf(getConfig(namespace, NETWORK_HTTP_CACHE_SWITCH, "true")).booleanValue());
            } catch (Exception e3) {
            }
            try {
                String v = getConfig(namespace, NETWORK_HTTP_CACHE_FLAG, null);
                if (v != null) {
                    NetworkConfigCenter.setCacheFlag(Long.valueOf(v).longValue());
                }
            } catch (Exception e4) {
            }
            try {
                AwcnConfig.setHttpsSniEnable(Boolean.valueOf(Boolean.valueOf(getConfig(namespace, NETWORK_HTTPS_SNI_ENABLE_SWITCH, "true")).booleanValue()).booleanValue());
            } catch (Exception e5) {
            }
            try {
                AwcnConfig.setAccsSessionCreateForbiddenInBg(Boolean.valueOf(getConfig(namespace, NETWORK_ACCS_SESSION_BG_SWITCH, "true")).booleanValue());
            } catch (Exception e6) {
            }
            StatisticReqTimes.getIntance().updateWhiteReqUrls(getConfig(NETWORK_SDK_GROUP, NETWORK_MONITOR_WHITELIST_URL, null));
        }
    }
}
