package anet.channel;

import android.text.TextUtils;
import anet.channel.entity.ENV;
import anet.channel.security.ISecurity;
import anet.channel.security.SecurityManager;
import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public final class Config {
    public static final Config DEFAULT_CONFIG = new Builder().setTag("[default]").setAppkey("[default]").setEnv(ENV.ONLINE).build();
    private static final String TAG = "awcn.Config";
    /* access modifiers changed from: private */
    public static Map<String, Config> configMap = new HashMap();
    /* access modifiers changed from: private */
    public String appkey;
    /* access modifiers changed from: private */
    public ENV env = ENV.ONLINE;
    /* access modifiers changed from: private */
    public ISecurity iSecurity;
    /* access modifiers changed from: private */
    public String tag;

    protected Config() {
    }

    public static Config getConfigByTag(String tag2) {
        Config config;
        synchronized (configMap) {
            config = configMap.get(tag2);
        }
        return config;
    }

    public static Config getConfig(String appkey2, ENV env2) {
        synchronized (configMap) {
            for (Config config : configMap.values()) {
                if (config.env == env2 && config.appkey.equals(appkey2)) {
                    return config;
                }
            }
            return null;
        }
    }

    public String getTag() {
        return this.tag;
    }

    public String getAppkey() {
        return this.appkey;
    }

    public ENV getEnv() {
        return this.env;
    }

    public ISecurity getSecurity() {
        return this.iSecurity;
    }

    public String toString() {
        return this.tag;
    }

    public static class Builder {
        private String appSecret;
        private String appkey;
        private String authCode;
        private ENV env = ENV.ONLINE;
        private String tag;

        public Builder setTag(String tag2) {
            this.tag = tag2;
            return this;
        }

        public Builder setAppkey(String appkey2) {
            this.appkey = appkey2;
            return this;
        }

        public Builder setEnv(ENV env2) {
            this.env = env2;
            return this;
        }

        public Builder setAuthCode(String authCode2) {
            this.authCode = authCode2;
            return this;
        }

        public Builder setAppSecret(String appSecret2) {
            this.appSecret = appSecret2;
            return this;
        }

        public Config build() {
            Config config;
            if (TextUtils.isEmpty(this.appkey)) {
                throw new RuntimeException("appkey can not be null or empty!");
            }
            Iterator i$ = Config.configMap.values().iterator();
            while (true) {
                if (i$.hasNext()) {
                    config = (Config) i$.next();
                    if (config.env == this.env && config.appkey.equals(this.appkey)) {
                        ALog.w(Config.TAG, "duplicated config exist!", (String) null, "appkey", this.appkey, "env", this.env);
                        if (!TextUtils.isEmpty(this.tag)) {
                            synchronized (Config.configMap) {
                                Config.configMap.put(this.tag, config);
                            }
                        }
                    }
                } else {
                    config = new Config();
                    String unused = config.appkey = this.appkey;
                    ENV unused2 = config.env = this.env;
                    if (TextUtils.isEmpty(this.tag)) {
                        String unused3 = config.tag = StringUtils.concatString(this.appkey, SymbolExpUtil.SYMBOL_DOLLAR, this.env.toString());
                    } else {
                        String unused4 = config.tag = this.tag;
                    }
                    if (!TextUtils.isEmpty(this.appSecret)) {
                        ISecurity unused5 = config.iSecurity = SecurityManager.getSecurityFactory().createNonSecurity(this.appSecret);
                    } else {
                        ISecurity unused6 = config.iSecurity = SecurityManager.getSecurityFactory().createSecurity(this.authCode);
                    }
                    synchronized (Config.configMap) {
                        Config.configMap.put(config.tag, config);
                    }
                }
            }
            return config;
        }
    }
}
