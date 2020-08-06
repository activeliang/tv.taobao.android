package com.ali.user.open.core.context;

import android.content.Context;
import android.content.res.Resources;
import com.ali.user.open.core.WebViewProxy;
import com.ali.user.open.core.config.AuthOption;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.config.Environment;
import com.ali.user.open.core.registry.ServiceRegistration;
import com.ali.user.open.core.registry.ServiceRegistry;
import com.ali.user.open.core.registry.impl.DefaultServiceRegistry;
import com.ali.user.open.core.registry.impl.ProxyEnabledServiceRegistryDelegator;
import com.ali.user.open.core.service.MemberExecutorService;
import com.ali.user.open.core.service.RpcService;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.core.service.impl.ExecutorServiceImpl;
import com.ali.user.open.core.util.SystemUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class KernelContext {
    public static boolean IS_HAVE_WEBVIEW = true;
    public static final String SDK_VERSION_STD = "android_4.7.1";
    public static final String TAG = "kernel";
    public static String UUID;
    public static volatile Context applicationContext;
    public static AuthOption authOption = AuthOption.NORMAL;
    public static MemberExecutorService executorService = new ExecutorServiceImpl();
    public static final ReentrantLock initLock = new ReentrantLock();
    public static boolean isMini = true;
    public static WebViewProxy mWebViewProxy;
    public static String packageName;
    public static Resources resources;
    public static AuthOption sOneTimeAuthOption = null;
    public static volatile Boolean sdkInitialized = Boolean.FALSE;
    public static String sdkVersion = SDK_VERSION_STD;
    public static volatile ServiceRegistry serviceRegistry = new DefaultServiceRegistry();

    public static void wrapServiceRegistry() {
        if (!(serviceRegistry instanceof ProxyEnabledServiceRegistryDelegator)) {
            serviceRegistry = new ProxyEnabledServiceRegistryDelegator(serviceRegistry);
        }
    }

    public static ServiceRegistration registerService(Class<?>[] types, Object instance, Map<String, String> properties) {
        return serviceRegistry.registerService(types, instance, properties == null ? new HashMap<>() : new HashMap<>(properties));
    }

    public static <T> T getService(Class<T> type, Map<String, String> properties) {
        return serviceRegistry.getService(type, properties);
    }

    public static Environment getEnvironment() {
        return ConfigManager.getInstance().getEnvironment();
    }

    public static <T> T getService(Class<T> type) {
        return serviceRegistry.getService(type, (Map<String, String>) null);
    }

    public static <T> T[] getServices(Class<T> type) {
        return serviceRegistry.getServices(type, (Map<String, String>) null);
    }

    public static synchronized Context getApplicationContext() {
        Context systemApp;
        synchronized (KernelContext.class) {
            if (applicationContext != null) {
                systemApp = applicationContext;
            } else {
                systemApp = SystemUtils.getSystemApp();
            }
        }
        return systemApp;
    }

    public static boolean checkServiceValid() {
        if (applicationContext == null || serviceRegistry == null || getServices(RpcService.class) == null || getServices(StorageService.class) == null || getServices(UserTrackerService.class) == null || getService(StorageService.class) == null) {
            return false;
        }
        return true;
    }
}
