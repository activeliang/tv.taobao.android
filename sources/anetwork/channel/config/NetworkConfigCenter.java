package anetwork.channel.config;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import anet.channel.util.ALog;
import anet.channel.util.HttpSslUtil;
import anetwork.channel.cache.CacheManager;
import anetwork.channel.http.NetworkSdkSetting;
import com.uc.webview.export.internal.interfaces.IWaStat;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public class NetworkConfigCenter {
    private static final String CACHE_FLAG = "Cache.Flag";
    private static volatile long cacheFlag = 0;
    private static volatile IRemoteConfig iRemoteConfig;
    private static volatile boolean isAllowHttpIpRetry = false;
    private static volatile boolean isHttpCacheEnable = true;
    private static volatile boolean isHttpSessionEnable = true;
    private static volatile boolean isRemoteNetworkServiceEnable = true;
    private static volatile boolean isSSLEnabled = true;
    private static volatile boolean isSpdyEnabled = true;

    public static void init() {
        cacheFlag = PreferenceManager.getDefaultSharedPreferences(NetworkSdkSetting.getContext()).getLong(CACHE_FLAG, 0);
    }

    public static void setSSLEnabled(boolean sslEnabled) {
        isSSLEnabled = sslEnabled;
    }

    public static boolean isSSLEnabled() {
        return isSSLEnabled;
    }

    public static void setSpdyEnabled(boolean spdyEnabled) {
        isSpdyEnabled = spdyEnabled;
    }

    public static boolean isSpdyEnabled() {
        return isSpdyEnabled;
    }

    public static void setHttpsValidationEnabled(boolean isEnable) {
        if (!isEnable) {
            HttpSslUtil.setHostnameVerifier(HttpSslUtil.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpSslUtil.setSslSocketFactory(HttpSslUtil.TRUST_ALL_SSL_SOCKET_FACTORY);
            return;
        }
        HttpSslUtil.setHostnameVerifier((HostnameVerifier) null);
        HttpSslUtil.setSslSocketFactory((SSLSocketFactory) null);
    }

    public static void setRemoteNetworkServiceEnable(boolean enable) {
        isRemoteNetworkServiceEnable = enable;
    }

    public static boolean isRemoteNetworkServiceEnable() {
        return isRemoteNetworkServiceEnable;
    }

    public static void setRemoteConfig(IRemoteConfig iRemoteConfig2) {
        if (iRemoteConfig != null) {
            iRemoteConfig.unRegister();
        }
        if (iRemoteConfig2 != null) {
            iRemoteConfig2.register();
        }
        iRemoteConfig = iRemoteConfig2;
    }

    public static boolean isHttpSessionEnable() {
        return isHttpSessionEnable;
    }

    public static void setHttpSessionEnable(boolean bEnable) {
        isHttpSessionEnable = bEnable;
    }

    public static boolean isAllowHttpIpRetry() {
        return isHttpSessionEnable && isAllowHttpIpRetry;
    }

    public static void setAllowHttpIpRetry(boolean b) {
        isAllowHttpIpRetry = b;
    }

    public static boolean isHttpCacheEnable() {
        return isHttpCacheEnable;
    }

    public static void setHttpCacheEnable(boolean isEnable) {
        isHttpCacheEnable = isEnable;
    }

    public static void setCacheFlag(long flag) {
        if (flag != cacheFlag) {
            ALog.i("anet.NetworkConfigCenter", "set cache flag", (String) null, IWaStat.KEY_OLD, Long.valueOf(cacheFlag), "new", Long.valueOf(flag));
            cacheFlag = flag;
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(NetworkSdkSetting.getContext()).edit();
            editor.putLong(CACHE_FLAG, cacheFlag);
            editor.apply();
            CacheManager.clearAllCache();
        }
    }
}
