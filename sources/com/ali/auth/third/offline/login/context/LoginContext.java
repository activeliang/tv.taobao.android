package com.ali.auth.third.offline.login.context;

import com.ali.auth.third.core.service.CredentialService;
import com.ali.auth.third.core.service.RpcService;
import com.ali.auth.third.core.service.StorageService;
import com.ali.auth.third.offline.sqlite.DatabaseHandler;
import java.util.concurrent.ExecutorService;

public class LoginContext {
    public static final String TAG = LoginContext.class.getSimpleName();
    public static final String bindBridgeName = "loginBridge";
    public static CredentialService credentialService = null;
    public static DatabaseHandler databaseHandler = null;
    public static ExecutorService executorService = null;
    public static final String loginBridgeName = "accountBridge";
    public static RpcService rpcService;
    public static StorageService storageService;
}
