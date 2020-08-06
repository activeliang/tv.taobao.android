package com.taobao.orange;

import android.content.Context;
import com.taobao.orange.OConstant;
import com.taobao.orange.aidl.OrangeConfigListenerStub;
import com.taobao.orange.util.AndroidUtil;
import com.taobao.orange.util.OLog;
import java.util.List;
import java.util.Map;

@Deprecated
public class OrangeConfigLocal {
    private static final String TAG = "OrangeConfigLocal";
    private static OrangeConfigLocal mInstance = new OrangeConfigLocal();
    private volatile boolean isMainProcess = true;

    private OrangeConfigLocal() {
    }

    @Deprecated
    public static OrangeConfigLocal getInstance() {
        return mInstance;
    }

    @Deprecated
    public void init(Context ctx) {
        init(ctx, (String) null, (String) null);
    }

    @Deprecated
    public void init(Context ctx, String appkey, String appversion) {
        init(ctx, appkey, appversion, OConstant.ENV.ONLINE.getEnvMode());
    }

    @Deprecated
    public void init(Context ctx, String appkey, String appversion, int env) {
        OLog.e(TAG, "OrangeConfigLocal.init", "@Deprecated please use OrangeConfig");
        if (ctx != null) {
            this.isMainProcess = AndroidUtil.isMainProcess(ctx);
        }
    }

    @Deprecated
    public String getConfig(String namespace, String key, String defaultVal) {
        return !this.isMainProcess ? defaultVal : ConfigCenter.getInstance().getConfig(namespace, key, defaultVal);
    }

    @Deprecated
    public Map<String, String> getConfigs(String namespace) {
        if (!this.isMainProcess) {
            return null;
        }
        return ConfigCenter.getInstance().getConfigs(namespace);
    }

    @Deprecated
    public void registerListener(String[] namespaces, OrangeConfigListener listenerV0) {
        if (namespaces != null && namespaces.length != 0 && listenerV0 != null) {
            for (String namespace : namespaces) {
                ConfigCenter.getInstance().registerListener(namespace, new OrangeConfigListenerStub(listenerV0), true);
            }
        }
    }

    @Deprecated
    public void registerListener(String[] namespaces, OrangeConfigListenerV1 listenerV1) {
        if (namespaces != null && namespaces.length != 0 && listenerV1 != null) {
            for (String namespace : namespaces) {
                ConfigCenter.getInstance().registerListener(namespace, new OrangeConfigListenerStub(listenerV1), true);
            }
        }
    }

    @Deprecated
    public void unregisterListener(String[] namespaces) {
        if (namespaces != null && namespaces.length != 0) {
            for (String namespace : namespaces) {
                ConfigCenter.getInstance().unregisterListeners(namespace);
            }
        }
    }

    @Deprecated
    public void enterForeground() {
        OLog.e(TAG, "OrangeConfigLocal.forceCheckUpdate", "@Deprecated please use OrangeConfig");
    }

    @Deprecated
    public void enterBackground() {
        OLog.e(TAG, "OrangeConfigLocal.enterBackground", "@Deprecated please use OrangeConfig");
    }

    @Deprecated
    public void setIndexUpdateMode(int indexUpdateMode) {
        OLog.e(TAG, "OrangeConfigLocal.setIndexUpdateMode", "@Deprecated please use OrangeConfig");
    }

    @Deprecated
    public void setUserId(String userId) {
        OLog.e(TAG, "OrangeConfigLocal.setUserId", "@Deprecated please use OrangeConfig");
    }

    @Deprecated
    public void setHosts(List<String> list) {
        OLog.e(TAG, "OrangeConfigLocal.setHosts", "@Deprecated please use OrangeConfig");
    }

    @Deprecated
    public void setAppSecret(String appSecret) {
        OLog.e(TAG, "OrangeConfigLocal.setAppSecret", "@Deprecated please use OrangeConfig");
    }
}
