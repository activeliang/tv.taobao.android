package anetwork.channel.http;

import android.app.Application;
import android.content.Context;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.SessionCenter;
import anet.channel.entity.ENV;
import anet.channel.util.ALog;
import anet.channel.util.Utils;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.cookie.CookieManager;
import anetwork.channel.monitor.Monitor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkSdkSetting implements Serializable {
    public static ENV CURRENT_ENV = ENV.ONLINE;
    private static final String TAG = "anet.NetworkSdkSetting";
    private static Context context;
    private static AtomicBoolean isInit = new AtomicBoolean(false);

    public static void init(Context context2) {
        if (context2 != null) {
            try {
                if (isInit.compareAndSet(false, true)) {
                    context = context2;
                    initTaobaoAdapter();
                    Monitor.init();
                    NetworkConfigCenter.init();
                    CookieManager.setup(context2);
                    SessionCenter.init(context2);
                }
            } catch (Throwable e) {
                ALog.e(TAG, "Network SDK initial failed!", (String) null, e, new Object[0]);
            }
        }
    }

    public static void init(Application application, HashMap<String, Object> params) {
        try {
            GlobalAppRuntimeInfo.setTtid((String) params.get("ttid"));
            init(application.getApplicationContext());
        } catch (Exception e) {
            ALog.e(TAG, "Network SDK initial failed!", (String) null, e, new Object[0]);
        }
    }

    public static void setTtid(String ttid) {
        GlobalAppRuntimeInfo.setTtid(ttid);
    }

    public static Context getContext() {
        return context;
    }

    private static void initTaobaoAdapter() {
        try {
            Utils.invokeStaticMethodThrowException("anet.channel.TaobaoNetworkAdapter", "init", (Class<?>[]) null, (Object[]) null);
            ALog.i(TAG, "init taobao adapter success", (String) null, new Object[0]);
        } catch (Exception e) {
            ALog.i(TAG, "initTaobaoAdapter failed. maybe not taobao app", (String) null, e);
        }
    }
}
