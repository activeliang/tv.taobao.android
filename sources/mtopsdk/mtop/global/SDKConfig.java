package mtopsdk.mtop.global;

import android.content.Context;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.intf.Mtop;

@Deprecated
public class SDKConfig {
    private static final String TAG = "mtopsdk.SDKConfig";
    private static final SDKConfig config = new SDKConfig();

    private SDKConfig() {
    }

    public static SDKConfig getInstance() {
        return config;
    }

    @Deprecated
    public Context getGlobalContext() {
        return Mtop.instance((Context) null).getMtopConfig().context;
    }

    @Deprecated
    public String getGlobalAppKey() {
        return Mtop.instance((Context) null).getMtopConfig().appKey;
    }

    @Deprecated
    public String getGlobalAuthCode() {
        return Mtop.instance((Context) null).getMtopConfig().authCode;
    }

    @Deprecated
    public String getGlobalDeviceId() {
        return Mtop.instance((Context) null).getMtopConfig().deviceId;
    }

    @Deprecated
    public String getGlobalUtdid() {
        return Mtop.instance((Context) null).getMtopConfig().utdid;
    }

    @Deprecated
    public String getGlobalTtid() {
        return Mtop.instance((Context) null).getMtopConfig().ttid;
    }

    @Deprecated
    public EnvModeEnum getGlobalEnvMode() {
        return Mtop.instance((Context) null).getMtopConfig().envMode;
    }

    @Deprecated
    public String getGlobalAppVersion() {
        return Mtop.instance((Context) null).getMtopConfig().appVersion;
    }

    @Deprecated
    public int getGlobalDailyAppKeyIndex() {
        return Mtop.instance((Context) null).getMtopConfig().dailyAppkeyIndex;
    }

    @Deprecated
    public int getGlobalOnlineAppKeyIndex() {
        return Mtop.instance((Context) null).getMtopConfig().onlineAppKeyIndex;
    }
}
