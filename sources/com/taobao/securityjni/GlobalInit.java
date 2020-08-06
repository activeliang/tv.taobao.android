package com.taobao.securityjni;

import android.content.Context;
import android.content.ContextWrapper;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent;

@Deprecated
public final class GlobalInit {
    private static Context globalContext = null;
    private static String sAppKey = null;

    public static synchronized void SetGlobalAppKey(String appKey) {
        synchronized (GlobalInit.class) {
            sAppKey = appKey;
        }
    }

    public static synchronized String GetGlobalAppKey() {
        String str;
        synchronized (GlobalInit.class) {
            str = sAppKey;
        }
        return str;
    }

    public static Context getGlobalContext() {
        return globalContext;
    }

    public static void GlobalSecurityInitSync(ContextWrapper context) {
        GlobalSecurityInitSync(context, (String) null);
    }

    public static void GlobalSecurityInitSync(ContextWrapper context, String nativeLibPath) {
        globalContext = context;
        SecurityGuardManager.getInitializer().loadLibrarySync(context, nativeLibPath);
    }

    public static void GlobalSecurityInitSyncSo(ContextWrapper context) {
        GlobalSecurityInitSyncSo(context, (String) null);
    }

    public static void GlobalSecurityInitSyncSo(ContextWrapper context, String nativeLibPath) {
        globalContext = context;
        if (SecurityGuardManager.getInitializer().loadLibrarySync(context, nativeLibPath) == 0 && SecurityGuardManager.getInstance(context) == null) {
        }
    }

    public static void GlobalSecurityInitSyncSDK(ContextWrapper context) {
        globalContext = context;
        if (SecurityGuardManager.getInitializer().loadLibrarySync(context, (String) null) == 0 && SecurityGuardManager.getInstance(context) == null) {
        }
    }

    public static void GlobalSecurityInitAsync(ContextWrapper context) {
        GlobalSecurityInitAsync(context, (String) null);
    }

    public static void GlobalSecurityInitAsync(ContextWrapper context, String nativeLibPath) {
        globalContext = context;
        SecurityGuardManager.getInitializer().loadLibraryAsync(context, nativeLibPath);
    }

    public static void GlobalSecurityInitAsyncSo(ContextWrapper context) {
        globalContext = context;
    }

    public static void GlobalSecurityInitAsyncSo(ContextWrapper context, String nativeLibPath) {
        globalContext = context;
        SecurityGuardManager.getInitializer().loadLibraryAsync(context, nativeLibPath);
        SecurityGuardManager.getInstance(context);
    }

    public static void GlobalSecurityInitAsyncSDK(ContextWrapper context) {
        globalContext = context;
        SecurityGuardManager.getInitializer().loadLibraryAsync(context, (String) null);
        SecurityGuardManager.getInstance(context);
    }

    public static void setEnableOutPutExpInfo(boolean val) {
    }

    private static void initSecBody(ContextWrapper context) {
        ISecurityBodyComponent securityBodyComponent;
        SecurityGuardManager manager = SecurityGuardManager.getInstance(context);
        if (manager != null && (securityBodyComponent = manager.getSecurityBodyComp()) != null) {
            String appKey = GetGlobalAppKey();
            if (appKey == null) {
                appKey = manager.getStaticDataStoreComp().getAppKeyByIndex(0);
            }
            securityBodyComponent.initSecurityBody(appKey);
        }
    }
}
