package com.ali.auth.third.offline.login.task;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.model.AccountContract;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.rpc.protocol.RpcException;
import com.ali.auth.third.core.storage.aes.MD5;
import com.ali.auth.third.core.task.AbsAsyncTask;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.core.util.ResourceUtils;
import com.ali.auth.third.core.util.StringUtil;
import com.ali.auth.third.offline.LoginWebViewActivity;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.RequestCode;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.login.util.ActivityUIHelper;
import com.ali.auth.third.offline.login.util.LoginStatus;
import com.ali.auth.third.offline.login.util.SqliteHelper;
import com.ali.auth.third.offline.model.LoginParam;
import com.ali.auth.third.offline.model.ResultActionType;

public class LoginByPwdTask extends AbsAsyncTask<String, Void, Void> {
    private static final String TAG = "login";
    /* access modifiers changed from: private */
    public Activity mActivity;
    private ActivityUIHelper mActivityHelper;

    public LoginByPwdTask(Activity activity) {
        this.mActivity = activity;
        this.mActivityHelper = new ActivityUIHelper(activity);
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        this.mActivityHelper.showProgress("");
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        String username = params[0];
        String password = params[1];
        String querystring = params[2];
        if (CommonUtils.isNetworkAvailable()) {
            RpcResponse<LoginReturnData> resultData = null;
            try {
                LoginParam param = new LoginParam();
                param.username = username;
                param.password = password;
                if (!TextUtils.isEmpty(querystring)) {
                    param.ext.put("aliusersdk_querystring", querystring);
                }
                if (!TextUtils.isEmpty(ConfigManager.getInstance().getOfflineDeviceID())) {
                    param.ext.put("posSN", ConfigManager.getInstance().getOfflineDeviceID());
                }
                resultData = LoginComponent.INSTANCE.pwdLogin(param);
            } catch (RpcException e) {
                if (RpcException.isNetworkError(e.getCode())) {
                    tryOfflineLogin(username, password);
                }
            }
            if (resultData == null) {
                onFailure(-1, ResourceUtils.getString("aliusersdk_network_error"));
            } else {
                try {
                    if (resultData.code == 3000) {
                        LoginContext.credentialService.refreshWhenLogin((LoginReturnData) resultData.returnValue);
                        SqliteHelper.getInstance().saveToSqlite(username, password);
                        onSuccess();
                    } else if (ResultActionType.H5.equals(resultData.actionType)) {
                        String url = ((LoginReturnData) resultData.returnValue).h5Url;
                        if (!TextUtils.isEmpty(url)) {
                            Intent intent = new Intent(this.mActivity, LoginWebViewActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("token", ((LoginReturnData) resultData.returnValue).token);
                            intent.putExtra("scene", ((LoginReturnData) resultData.returnValue).scene);
                            this.mActivity.startActivityForResult(intent, RequestCode.OPEN_DOUBLE_CHECK);
                        } else {
                            onFailure(resultData.code, resultData.message);
                        }
                    } else if (ResultActionType.ALERT.equals(resultData.actionType)) {
                        onFailure(resultData.code, resultData.message);
                    } else if (ResultActionType.TOAST.equals(resultData.actionType)) {
                        onFailure(resultData.code, resultData.message);
                    } else {
                        onFailure(resultData.code, resultData.message);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } else if (KernelContext.supportOfflineLogin) {
            tryOfflineLogin(username, password);
        } else {
            RpcResponse<String> result = new RpcResponse<>();
            result.code = -1;
            result.message = ResourceUtils.getString("com_taobao_tae_sdk_network_not_available_message");
            onFailure(result.code, result.message);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        Message errorMessage = MessageUtils.createMessage(10010, t.getMessage());
        SDKLogger.d("login", errorMessage.toString());
        onFailure(errorMessage.code, errorMessage.message);
    }

    private void onFailure(final int code, final String message) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (!TextUtils.isEmpty(message)) {
                    Toast.makeText(LoginByPwdTask.this.mActivity, message, 0).show();
                }
                if (CallbackContext.loginCallback != null) {
                    ((LoginCallback) CallbackContext.loginCallback).onFailure(code, message);
                }
            }
        });
    }

    private void onSuccess() {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (CallbackContext.loginCallback != null) {
                    ((LoginCallback) CallbackContext.loginCallback).onSuccess(LoginContext.credentialService.getSession());
                }
                if (LoginByPwdTask.this.mActivity != null) {
                    LoginByPwdTask.this.mActivity.finish();
                }
                LoginStatus.resetLoginFlag();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
        this.mActivityHelper.dismissProgressDialog();
    }

    private void tryOfflineLogin(String username, String password) {
        String newHash = MD5.getSHA256(username + "&" + password);
        AccountContract historyAccount = LoginContext.databaseHandler.getAccount(newHash);
        if (historyAccount == null || !StringUtil.equals(newHash, historyAccount.getHash())) {
            onFailure(-1, ResourceUtils.getString("aliuser_offline_login_fail"));
            return;
        }
        LoginContext.credentialService.refreshWhenOfflineLogin(historyAccount);
        onSuccess();
    }
}
