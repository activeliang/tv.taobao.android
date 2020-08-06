package com.ali.user.open.core;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.ali.user.open.core.callback.InitResultCallback;
import com.ali.user.open.core.config.AuthOption;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.config.Environment;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.task.InitTask;
import com.ali.user.open.core.util.Validate;
import java.util.Map;
import java.util.concurrent.FutureTask;

public class AliMemberSDK {
    private static String sMasterSite;
    public static String ttid;

    public static synchronized void init(Context context, InitResultCallback initResultCallback) {
        synchronized (AliMemberSDK.class) {
            if (!KernelContext.sdkInitialized.booleanValue()) {
                Validate.notNull(context, "context");
                KernelContext.applicationContext = context.getApplicationContext();
                KernelContext.executorService.postHandlerTask(new FutureTask(new InitTask(initResultCallback)));
            } else if (initResultCallback != null) {
                initResultCallback.onSuccess();
            }
        }
    }

    public static synchronized void init(Context context, String site, InitResultCallback initResultCallback) {
        synchronized (AliMemberSDK.class) {
            sMasterSite = site;
            init(context, initResultCallback);
        }
    }

    public static void turnOnDebug() {
        Log.w("AliMemberSDK", "************************************\nDebug is enabled, make sure to turn it off in the production environment\n************************************");
        ConfigManager.DEBUG = true;
    }

    public static <T> T getService(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        return KernelContext.serviceRegistry.getService(clazz, (Map<String, String>) null);
    }

    public static void setMasterSite(String masterSite) {
        sMasterSite = masterSite;
    }

    public static String getMasterSite() {
        return sMasterSite;
    }

    public static void setTtid(String ttid2) {
        ttid = ttid2;
    }

    public static void setEnvironment(Environment env) {
        ConfigManager.getInstance().setEnvironment(env);
    }

    public static void setUUID(String uuid) {
        KernelContext.UUID = uuid;
    }

    public static void setAuthOption(AuthOption authOption) {
        KernelContext.authOption = authOption;
    }

    public static void setPackageName(String packageName) {
        KernelContext.packageName = packageName;
    }

    public static void setResources(Resources resources) {
        KernelContext.resources = resources;
    }
}
