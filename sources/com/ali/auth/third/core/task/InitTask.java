package com.ali.auth.third.core.task;

import android.os.AsyncTask;
import android.webkit.CookieSyncManager;
import com.ali.auth.third.core.callback.InitResultCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.device.DeviceInfo;
import com.ali.auth.third.core.exception.AlibabaSDKException;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.registry.ServiceRegistry;
import com.ali.auth.third.core.service.CredentialService;
import com.ali.auth.third.core.service.MemberExecutorService;
import com.ali.auth.third.core.service.RpcService;
import com.ali.auth.third.core.service.StorageService;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.core.util.ReflectionUtils;
import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class InitTask implements Runnable {
    private static final String TAG = "init";
    private Integer envIndex;
    /* access modifiers changed from: private */
    public InitResultCallback initResultCallback;

    public InitTask(InitResultCallback initResultCallback2, Integer envIndex2) {
        this.initResultCallback = initResultCallback2;
        this.envIndex = envIndex2;
    }

    public boolean initialize() {
        SDKLogger.d(TAG, "sdk version = " + ConfigManager.SDK_VERSION.toString());
        initializeUTDId();
        if (KernelContext.syncInitialized) {
            return true;
        }
        try {
            CookieSyncManager.createInstance(KernelContext.getApplicationContext());
            if (initializeCoreComponents()) {
                KernelContext.syncInitialized = true;
                return true;
            }
        } catch (Throwable t) {
            SDKLogger.e(TAG, "fail to sync start", t);
            doWhenException(t);
        }
        return false;
    }

    private void initializeUTDId() {
        DeviceInfo.init(KernelContext.context);
    }

    private boolean initializeCoreComponents() {
        boolean loginPluginsStarted = false;
        try {
            Class.forName(AsyncTask.class.getName());
        } catch (Exception e) {
        }
        KernelContext.wrapServiceRegistry();
        ConfigManager.getInstance().init(this.envIndex.intValue());
        ServiceRegistry serviceRegistry = KernelContext.serviceRegistry;
        if (registerRpc(serviceRegistry) && registerStorage(serviceRegistry) && registerUserTrack(serviceRegistry)) {
            serviceRegistry.registerService(new Class[]{MemberExecutorService.class, ExecutorService.class}, KernelContext.executorService, Collections.singletonMap(Constants.PLUGIN_VENDOR_KEY, "kernel"));
            serviceRegistry.registerService(new Class[]{CredentialService.class}, CredentialManager.INSTANCE, Collections.singletonMap("scop", "system"));
            KernelContext.credentialService = (CredentialService) serviceRegistry.getService(CredentialService.class, (Map<String, String>) null);
            if (loadLogin() || loadOfflineLogin()) {
                loginPluginsStarted = true;
            }
            if (!KernelContext.isMini) {
                loadAccountLink();
            }
            SDKLogger.d(TAG, "INIT SUCCESS");
        }
        return loginPluginsStarted;
    }

    private boolean registerStorage(ServiceRegistry serviceRegistry) {
        Object instance;
        SDKLogger.d(TAG, "registerStorage");
        boolean isSecurityGuardDetected = false;
        try {
            Class.forName("com.ali.auth.third.securityguard.SecurityGuardWrapper");
            isSecurityGuardDetected = true;
            KernelContext.isMini = false;
            KernelContext.sdkVersion = KernelContext.SDK_VERSION_STD;
        } catch (Throwable th) {
        }
        if (isSecurityGuardDetected) {
            try {
                instance = getServiceInstance("com.ali.auth.third.securityguard.SecurityGuardWrapper", (String[]) null, (Object[]) null);
            } catch (NoSuchMethodError e2) {
                e2.printStackTrace();
                return false;
            }
        } else {
            instance = getServiceInstance("com.ali.auth.third.core.storage.CommonStorageServiceImpl", (String[]) null, (Object[]) null);
        }
        serviceRegistry.registerService(new Class[]{StorageService.class}, instance, (Map<String, String>) null);
        KernelContext.storageService = (StorageService) serviceRegistry.getService(StorageService.class, (Map<String, String>) null);
        return true;
    }

    private boolean registerRpc(ServiceRegistry serviceRegistry) {
        Object instance;
        SDKLogger.d(TAG, "registerRpc");
        boolean isMtopRpc = false;
        try {
            Class.forName("com.ali.auth.third.mtop.rpc.impl.MtopRpcServiceImpl");
            isMtopRpc = true;
        } catch (Throwable th) {
        }
        if (isMtopRpc) {
            try {
                instance = getServiceInstance("com.ali.auth.third.mtop.rpc.impl.MtopRpcServiceImpl", (String[]) null, (Object[]) null);
            } catch (NoSuchMethodError e2) {
                e2.printStackTrace();
                return false;
            }
        } else {
            instance = getServiceInstance("com.ali.auth.third.core.rpc.CommRpcServiceImpl", (String[]) null, (Object[]) null);
        }
        serviceRegistry.registerService(new Class[]{RpcService.class}, instance, (Map<String, String>) null);
        return true;
    }

    private boolean registerUserTrack(ServiceRegistry serviceRegistry) {
        Object instance;
        SDKLogger.d(TAG, "registerUserTrack");
        boolean hasUT = false;
        try {
            Class.forName("com.ali.auth.third.ut.UserTrackerImpl");
            hasUT = true;
        } catch (Throwable th) {
        }
        if (hasUT) {
            try {
                instance = getServiceInstance("com.ali.auth.third.ut.UserTrackerImpl", (String[]) null, (Object[]) null);
            } catch (NoSuchMethodError e) {
                e.printStackTrace();
                return false;
            }
        } else {
            instance = getServiceInstance("com.ali.auth.third.core.ut.UserTracer", (String[]) null, (Object[]) null);
        }
        serviceRegistry.registerService(new Class[]{UserTrackerService.class}, instance, (Map<String, String>) null);
        return true;
    }

    private Object getServiceInstance(String clzName, String[] paramTypeNames, Object[] paramValues) {
        try {
            return ReflectionUtils.newInstance(clzName, paramTypeNames, paramValues);
        } catch (NoSuchMethodError e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private boolean loadLogin() {
        SDKLogger.d(TAG, "register login service");
        try {
            ReflectionUtils.invoke("com.ali.auth.third.login.LoginLifecycleAdapter", TAG, (String[]) null, Class.forName("com.ali.auth.third.login.LoginLifecycleAdapter"), (Object[]) null);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean loadOfflineLogin() {
        SDKLogger.d(TAG, "register offline login service");
        try {
            ReflectionUtils.invoke("com.ali.auth.third.offline.login.LoginLifecycleAdapter", TAG, (String[]) null, Class.forName("com.ali.auth.third.offline.login.LoginLifecycleAdapter"), (Object[]) null);
            KernelContext.supportOfflineLogin = true;
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean loadAccountLink() {
        SDKLogger.d(TAG, "register account link service");
        try {
            ReflectionUtils.invoke("com.ali.auth.third.accountlink.AccountLinkLifecycleAdapter", TAG, (String[]) null, Class.forName("com.ali.auth.third.accountlink.AccountLinkLifecycleAdapter"), (Object[]) null);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public void run() {
        try {
            KernelContext.initLock.lock();
            asyncRun();
        } catch (Throwable t) {
            SDKLogger.e(TAG, t.getMessage(), t);
            doWhenException(t);
        } finally {
            doFinally();
        }
    }

    private void asyncRun() {
        try {
            long timeStamp = getTimeStamp();
            SDKLogger.e(TAG, "timeStamp=" + timeStamp);
            KernelContext.timestamp = timeStamp;
        } catch (Exception e) {
            SDKLogger.e(TAG, e.getMessage(), e);
        }
        if (!initialize()) {
            SDKLogger.d(TAG, " INIT FAILURE");
            KernelContext.executorService.postUITask(new Runnable() {
                public void run() {
                    if (InitTask.this.initResultCallback != null) {
                        InitTask.this.initResultCallback.onFailure(-1, "service register fail");
                    }
                }
            });
            return;
        }
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (InitTask.this.initResultCallback != null) {
                    InitTask.this.initResultCallback.onSuccess();
                }
            }
        });
        KernelContext.isInitOk = true;
        SDKLogger.d(TAG, "INIT SUCCESS");
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        int errorCode;
        String errorMessage;
        KernelContext.isInitOk = false;
        if (!(t instanceof AlibabaSDKException) || ((AlibabaSDKException) t).getSDKMessage() == null) {
            errorCode = 10010;
            errorMessage = CommonUtils.toString(t);
        } else {
            Message error = ((AlibabaSDKException) t).getSDKMessage();
            errorCode = error.code;
            errorMessage = error.message;
        }
        CommonUtils.onFailure(this.initResultCallback, errorCode, errorMessage);
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
        KernelContext.initLock.unlock();
    }

    private long getTimeStamp() {
        File tempFile = new File(KernelContext.context.getFilesDir().getAbsolutePath() + File.separator + "timestamp");
        if (tempFile.exists()) {
            return tempFile.lastModified();
        }
        try {
            tempFile.createNewFile();
            return tempFile.lastModified();
        } catch (Exception e) {
            SDKLogger.e(TAG, e.getMessage(), e);
            return 0;
        }
    }
}
