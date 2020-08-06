package com.ali.auth.third.core;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.ali.auth.third.core.callback.InitResultCallback;
import com.ali.auth.third.core.config.AuthOption;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.config.Environment;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.task.InitTask;
import java.util.Collections;
import java.util.Map;

public class MemberSDK {
    private static final Map<String, String> USER_SERVICE_FILTER = Collections.singletonMap(Constants.ISV_SCOPE_FLAG, "true");
    private static Environment env;
    public static String ttid;

    public static void setTtid(String ttid2) {
        ttid = ttid2;
    }

    public static void setEnvironment(Environment env2) {
        env = env2;
    }

    public static void init(Context context, InitResultCallback initResultCallback) {
        internalAsyncInit(context, initResultCallback);
    }

    private static InitTask internalAsyncInit(Context context, InitResultCallback initResultCallback) {
        KernelContext.context = context.getApplicationContext();
        if (env == null) {
            env = Environment.ONLINE;
        }
        InitTask initTask = new InitTask(initResultCallback, Integer.valueOf(env.ordinal()));
        KernelContext.executorService.postHandlerTask(initTask);
        return initTask;
    }

    public static void turnOnDebug() {
        Log.w("AuthSDK", "************************************\nDebug is enabled, make sure to turn it off in the production environment\n************************************");
        ConfigManager.DEBUG = true;
    }

    @Deprecated
    public static void turnOffDebug() {
    }

    public static <T> T getService(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        return KernelContext.serviceRegistry.getService(clazz, USER_SERVICE_FILTER);
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
