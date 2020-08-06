package com.taobao.orange;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.taobao.orange.OConfig;
import com.taobao.orange.OConstant;
import java.util.List;
import java.util.Map;

public abstract class OrangeConfig {
    public abstract void addCandidate(@NonNull OCandidate oCandidate);

    @Deprecated
    public abstract void enterBackground();

    @Deprecated
    public abstract void enterForeground();

    public abstract void forceCheckUpdate();

    public abstract String getConfig(@NonNull String str, @NonNull String str2, @Nullable String str3);

    public abstract Map<String, String> getConfigs(@NonNull String str);

    public abstract void init(@NonNull Context context, @NonNull OConfig oConfig);

    public abstract void registerListener(@NonNull String[] strArr, @NonNull OConfigListener oConfigListener, boolean z);

    @Deprecated
    public abstract void registerListener(@NonNull String[] strArr, @NonNull OrangeConfigListener orangeConfigListener);

    @Deprecated
    public abstract void registerListener(@NonNull String[] strArr, @NonNull OrangeConfigListenerV1 orangeConfigListenerV1);

    @Deprecated
    public abstract void setAppSecret(String str);

    @Deprecated
    public abstract void setHosts(List<String> list);

    @Deprecated
    public abstract void setIndexUpdateMode(int i);

    public abstract void setUserId(@Nullable String str);

    public abstract void unregisterListener(@NonNull String[] strArr);

    public abstract void unregisterListener(@NonNull String[] strArr, @NonNull OConfigListener oConfigListener);

    @Deprecated
    public abstract void unregisterListener(@NonNull String[] strArr, @NonNull OrangeConfigListenerV1 orangeConfigListenerV1);

    public static OrangeConfig getInstance() {
        return OrangeConfigImpl.mInstance;
    }

    @Deprecated
    public void init(Context c) {
        init(c, (String) null, (String) null);
    }

    @Deprecated
    public void init(Context c, String appkey, String appversion) {
        init(c, appkey, appversion, OConstant.ENV.ONLINE.getEnvMode());
    }

    @Deprecated
    public void init(Context ctx, String appkey, String appversion, int env) {
        init(ctx, appkey, appversion, env, OConstant.SERVER.TAOBAO.ordinal());
    }

    @Deprecated
    public void init(Context ctx, String appkey, String appversion, int env, int serverType) {
        init(ctx, appkey, appversion, env, serverType, (String) null, (String) null);
    }

    @Deprecated
    public void init(Context ctx, String appkey, String appversion, int env, int serverType, String dcHost, String ackHost) {
        init(ctx, new OConfig.Builder().setAppKey(appkey).setAppVersion(appversion).setEnv(env).setServerType(serverType).setIndexUpdateMode(OConstant.UPDMODE.O_XMD.ordinal()).setDcHost(dcHost).setAckHost(ackHost).build());
    }
}
