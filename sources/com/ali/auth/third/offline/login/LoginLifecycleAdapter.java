package com.ali.auth.third.offline.login;

import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.service.CredentialService;
import com.ali.auth.third.core.service.RpcService;
import com.ali.auth.third.core.service.StorageService;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.login.handler.LoginActivityResultHandler;
import com.ali.auth.third.offline.login.util.LoginStatus;
import com.ali.auth.third.offline.sqlite.DatabaseHandler;
import com.ali.auth.third.offline.support.ActivityResultHandler;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class LoginLifecycleAdapter {
    public static final String TAG = "Member.LoginLifecycleAdapter";

    public static void init() {
        SDKLogger.d(TAG, "LoginLifecycle init ");
        LoginContext.rpcService = (RpcService) KernelContext.getService(RpcService.class, (Map<String, String>) null);
        LoginContext.credentialService = (CredentialService) KernelContext.getService(CredentialService.class, (Map<String, String>) null);
        LoginContext.executorService = (ExecutorService) KernelContext.getService(ExecutorService.class, (Map<String, String>) null);
        LoginContext.storageService = (StorageService) KernelContext.getService(StorageService.class, (Map<String, String>) null);
        LoginContext.databaseHandler = new DatabaseHandler(KernelContext.getApplicationContext());
        LoginActivityResultHandler resultHandler = new LoginActivityResultHandler();
        KernelContext.registerService(new Class[]{ActivityResultHandler.class}, resultHandler, Collections.singletonMap(ActivityResultHandler.REQUEST_CODE_KEY, String.valueOf(RequestCode.OPEN_H5_LOGIN)));
        KernelContext.registerService(new Class[]{ActivityResultHandler.class}, resultHandler, Collections.singletonMap(ActivityResultHandler.REQUEST_CODE_KEY, String.valueOf(RequestCode.OPEN_TAOBAO)));
        KernelContext.registerService(new Class[]{ActivityResultHandler.class}, resultHandler, Collections.singletonMap(ActivityResultHandler.REQUEST_CODE_KEY, String.valueOf(RequestCode.OPEN_DOUBLE_CHECK)));
        KernelContext.registerService(new Class[]{ActivityResultHandler.class}, resultHandler, Collections.singletonMap(ActivityResultHandler.REQUEST_CODE_KEY, String.valueOf(RequestCode.OPEN_QR_LOGIN)));
        KernelContext.registerService(new Class[]{ActivityResultHandler.class}, resultHandler, Collections.singletonMap(ActivityResultHandler.REQUEST_CODE_KEY, String.valueOf(RequestCode.OPEN_QR_LOGIN_CONFIRM)));
        Map<String, String> loginServicePropertiess = Collections.singletonMap(Constants.ISV_SCOPE_FLAG, "true");
        Class[] clsArr = {LoginService.class};
        KernelContext.registerService(clsArr, new LoginServiceImpl(), loginServicePropertiess);
        LoginStatus.init(KernelContext.getApplicationContext());
    }
}
