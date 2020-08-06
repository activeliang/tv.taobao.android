package com.ali.user.open.core.task;

import android.webkit.CookieSyncManager;
import com.ali.user.open.core.callback.InitResultCallback;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.device.DeviceInfo;
import com.ali.user.open.core.exception.MemberSDKException;
import com.ali.user.open.core.service.MemberExecutorService;
import com.ali.user.open.core.service.RpcService;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.core.service.impl.ExecutorServiceImpl;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.CommonUtils;
import com.ali.user.open.core.util.ReflectionUtils;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class InitTask implements Callable<Void> {
    private static final String TAG = "initTask";
    /* access modifiers changed from: private */
    public InitResultCallback initResultCallback;

    public InitTask(InitResultCallback initResultCallback2) {
        this.initResultCallback = initResultCallback2;
    }

    private boolean initialize() {
        SDKLogger.d(TAG, "sdk version = 4.7.1");
        initUtdid();
        try {
            CookieSyncManager.createInstance(KernelContext.getApplicationContext());
            if (initializeCoreComponents()) {
                KernelContext.sdkInitialized = Boolean.TRUE;
                return true;
            }
        } catch (Throwable t) {
            SDKLogger.e(TAG, "fail to sync start", t);
            doWhenException(t);
        }
        return false;
    }

    private void initUtdid() {
        DeviceInfo.init(KernelContext.getApplicationContext());
    }

    private boolean initializeCoreComponents() {
        KernelContext.wrapServiceRegistry();
        ConfigManager.getInstance().init();
        if (!registerRpc() || !registerStorage() || !registerUserTrack()) {
            return false;
        }
        ReflectionUtils.invoke("com.ali.user.open.module.SessionModule", "init", (String[]) null, (Object) null, (Object[]) null);
        ReflectionUtils.invoke("com.ali.user.open.oauth.module.OauthModule", "init", (String[]) null, (Object) null, (Object[]) null);
        ReflectionUtils.invoke("com.ali.user.open.ucc.module.UccModule", "init", (String[]) null, (Object) null, (Object[]) null);
        KernelContext.registerService(new Class[]{MemberExecutorService.class, ExecutorService.class}, new ExecutorServiceImpl(), (Map<String, String>) null);
        boolean loadLogin = loadLogin();
        SDKLogger.d(TAG, "INIT SUCCESS");
        return loadLogin;
    }

    public Void call() {
        try {
            KernelContext.initLock.lock();
            asyncRun();
            return null;
        } catch (Throwable t) {
            SDKLogger.e(TAG, t.getMessage(), t);
            doWhenException(t);
            return null;
        } finally {
            doFinally();
        }
    }

    private void asyncRun() {
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
        SDKLogger.d(TAG, "INIT SUCCESS");
    }

    private boolean registerStorage() {
        SDKLogger.d(TAG, "registerStorage");
        boolean isSecurityGuardDetected = false;
        try {
            Class.forName("com.ali.user.open.securityguard.SecurityGuardWrapper");
            isSecurityGuardDetected = true;
            KernelContext.isMini = false;
        } catch (Throwable th) {
        }
        if (!isSecurityGuardDetected) {
            return false;
        }
        try {
            Class[] clsArr = {StorageService.class};
            KernelContext.registerService(clsArr, getServiceInstance("com.ali.user.open.securityguard.SecurityGuardWrapper", (String[]) null, (Object[]) null), (Map<String, String>) null);
            return true;
        } catch (NoSuchMethodError e2) {
            e2.printStackTrace();
            return false;
        }
    }

    private boolean registerRpc() {
        SDKLogger.d(TAG, "registerRpc");
        boolean isMtopRpc = false;
        try {
            Class.forName("com.ali.user.open.mtop.rpc.impl.MtopRpcServiceImpl");
            isMtopRpc = true;
        } catch (Throwable th) {
        }
        if (!isMtopRpc) {
            return false;
        }
        try {
            Class[] clsArr = {RpcService.class};
            KernelContext.registerService(clsArr, getServiceInstance("com.ali.user.open.mtop.rpc.impl.MtopRpcServiceImpl", (String[]) null, (Object[]) null), (Map<String, String>) null);
            return true;
        } catch (NoSuchMethodError e2) {
            e2.printStackTrace();
            return false;
        }
    }

    private boolean registerUserTrack() {
        SDKLogger.d(TAG, "registerUserTrack");
        boolean hasUT = false;
        try {
            Class.forName("com.ali.user.open.ut.UserTrackerImpl");
            hasUT = true;
        } catch (Throwable th) {
        }
        if (hasUT) {
            try {
                Class[] clsArr = {UserTrackerService.class};
                KernelContext.registerService(clsArr, getServiceInstance("com.ali.user.open.ut.UserTrackerImpl", (String[]) null, (Object[]) null), (Map<String, String>) null);
                return true;
            } catch (NoSuchMethodError e) {
                e.printStackTrace();
            }
        }
        return false;
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
            ReflectionUtils.invoke("com.ali.user.open.tbauth.TbAuthLifecycleAdapter", "init", (String[]) null, Class.forName("com.ali.user.open.tbauth.TbAuthLifecycleAdapter"), (Object[]) null);
        } catch (ClassNotFoundException e) {
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        int errorCode;
        String errorMessage;
        if (t instanceof MemberSDKException) {
            MemberSDKException error = (MemberSDKException) t;
            errorCode = error.code;
            errorMessage = error.message;
        } else {
            errorCode = 10010;
            errorMessage = CommonUtils.toString(t);
        }
        CommonUtils.onFailure(this.initResultCallback, errorCode, errorMessage);
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
        KernelContext.initLock.unlock();
    }
}
