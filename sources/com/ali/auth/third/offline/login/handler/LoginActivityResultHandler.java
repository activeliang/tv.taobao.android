package com.ali.auth.third.offline.login.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.webkit.WebView;
import com.ali.auth.third.core.broadcast.LoginAction;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.offline.LoginActivity;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.RequestCode;
import com.ali.auth.third.offline.login.UTConstants;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.login.task.LoginAfterOpenTb;
import com.ali.auth.third.offline.login.task.LoginByIVTokenTask;
import com.ali.auth.third.offline.login.task.RefreshPageAfterOpenTb;
import com.ali.auth.third.offline.login.util.LoginStatus;
import com.ali.auth.third.offline.support.BaseActivityResultHandler;
import com.ali.auth.third.offline.webview.BaseWebViewActivity;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class LoginActivityResultHandler extends BaseActivityResultHandler {
    private static final String TAG = "login";

    /* access modifiers changed from: protected */
    public void onCallbackContext(int requestCode, int resultCode, Intent data, Activity activity, Map<Class<?>, Object> map, WebView webview) {
        SDKLogger.d("login", "onCallbackContext requestCode=" + requestCode + " resultCode = " + resultCode + " authCode = " + (data == null ? "" : data.getStringExtra("result")));
        LoginStatus.resetLoginFlag();
        LoginCallback loginCallback = (LoginCallback) CallbackContext.loginCallback;
        if (requestCode != RequestCode.OPEN_H5_LOGIN || loginCallback == null) {
            if (requestCode == RequestCode.OPEN_TAOBAO && loginCallback != null) {
                ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send("AUTH_TAOBAO", (Map<String, String>) null);
                if (resultCode == -1 && data != null) {
                    new LoginAfterOpenTb((Activity) CallbackContext.activity.get(), loginCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{data.getStringExtra("result")});
                } else if (resultCode == 0) {
                    onLoginFailure(activity, "E_TB_LOGIN_CANCEL", loginCallback, 10004);
                } else {
                    SDKLogger.d("login", "result from taobao : " + (data == null ? "" : data.getStringExtra("result")));
                    onLoginFailure(activity, "E_TB_LOGIN_FAILURE", loginCallback, 10005);
                }
            } else if (requestCode != RequestCode.OPEN_QR_LOGIN || loginCallback == null) {
                if (requestCode != RequestCode.OPEN_QR_LOGIN_CONFIRM || loginCallback == null) {
                    if (requestCode == RequestCode.OPEN_DOUBLE_CHECK) {
                        handleCheck(data, loginCallback);
                    } else if (requestCode == RequestCode.OPEN_H5_UNBIND) {
                        if (resultCode == ResultCode.SUCCESS.code) {
                            onLoginSuccess(activity, "E_H5_UNBIND_SUCCESS", loginCallback);
                        } else {
                            onLoginFailure(activity, "E_H5_UNBIND_FAILURE", loginCallback, 10003);
                        }
                        CallbackContext.loginCallback = null;
                    }
                } else if (resultCode == ResultCode.SUCCESS.code) {
                    onLoginSuccess(activity, "E_QR_LOGIN_CONFIRM_SUCCESS", loginCallback);
                } else {
                    onLoginFailure(activity, "E_QR_LOGIN_CONFIRM_CANCEL", loginCallback, 10003);
                }
            } else if (resultCode == ResultCode.SUCCESS.code) {
                onLoginSuccess(activity, "E_QR_LOGIN_SUCCESS", loginCallback);
            } else {
                onLoginFailure(activity, "E_QR_CANCEL_FAILURE", loginCallback, 10003);
            }
        } else if (resultCode == ResultCode.SUCCESS.code) {
            onLoginSuccess(activity, "E_H5_LOGIN_SUCCESS", loginCallback);
        } else if (resultCode != ResultCode.IGNORE.code) {
            if (resultCode == ResultCode.CHECK.code) {
                handleCheck(data, loginCallback);
            } else {
                onLoginFailure(activity, "E_H5_CANCEL_FAILURE", loginCallback, 10003);
            }
        }
    }

    private void handleCheck(Intent data, LoginCallback loginCallback) {
        SDKLogger.d("login", "handleCheck");
        final LoginCallback trustLoginCallback = loginCallback;
        final WeakReference<Activity> savedActivity = CallbackContext.activity;
        if (savedActivity == null || savedActivity.get() == null || data == null) {
            KernelContext.executorService.postUITask(new Runnable() {
                public void run() {
                    CallbackContext.activity = savedActivity;
                    LoginActivityResultHandler.this.onLoginFailure((Activity) savedActivity.get(), UTConstants.E_H5_OPERATION_BIND_FAILURE, trustLoginCallback, 10003);
                    CallbackContext.activity = null;
                }
            });
        } else if (TextUtils.isEmpty(data.getStringExtra("token"))) {
            KernelContext.executorService.postUITask(new Runnable() {
                public void run() {
                    CallbackContext.activity = savedActivity;
                    LoginActivityResultHandler.this.onLoginFailure((Activity) savedActivity.get(), UTConstants.E_IV_LOGIN_FAILURE, trustLoginCallback, 10101);
                    CallbackContext.activity = null;
                }
            });
        } else {
            new LoginByIVTokenTask((Activity) savedActivity.get(), new LoginCallback() {
                public void onFailure(int code, String msg) {
                    KernelContext.executorService.postUITask(new Runnable() {
                        public void run() {
                            CallbackContext.activity = savedActivity;
                            LoginActivityResultHandler.this.onLoginFailure((Activity) savedActivity.get(), UTConstants.E_IV_LOGIN_FAILURE, trustLoginCallback, 10101);
                            CallbackContext.activity = null;
                        }
                    });
                }

                public void onSuccess(Session session) {
                    KernelContext.executorService.postUITask(new Runnable() {
                        public void run() {
                            CallbackContext.activity = savedActivity;
                            LoginActivityResultHandler.this.onLoginSuccess((Activity) savedActivity.get(), UTConstants.E_H5_LOGIN_SUCCESS, trustLoginCallback);
                            CallbackContext.activity = null;
                        }
                    });
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{data.getStringExtra("token"), data.getStringExtra("scene"), data.getStringExtra(Constants.QUERY_STRING)});
        }
    }

    /* access modifiers changed from: private */
    public void onLoginSuccess(Activity activity, String label, LoginCallback loginCallback) {
        SDKLogger.d("login", "onLoginSuccess ");
        if (loginCallback != null) {
            loginCallback.onSuccess(LoginContext.credentialService.getSession());
            ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_H5_LOGIN_SUCCESS, (Map<String, String>) null);
        }
        if (CallbackContext.mGlobalLoginCallback != null) {
            CallbackContext.mGlobalLoginCallback.onSuccess(LoginContext.credentialService.getSession());
        }
        CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGIN_SUCCESS);
        if (activity != null && (activity instanceof LoginActivity)) {
            CallbackContext.activity = null;
            activity.finish();
            CallbackContext.loginCallback = null;
        }
    }

    /* access modifiers changed from: private */
    public void onLoginFailure(Activity activity, String label, LoginCallback loginCallback, int errorCode) {
        SDKLogger.d("login", "onLoginFailure ");
        if (loginCallback != null) {
            Message errorMessage = MessageUtils.createMessage(errorCode, new Object[0]);
            loginCallback.onFailure(errorMessage.code, errorMessage.message);
            if (!UTConstants.E_IV_LOGIN_FAILURE.equals(label)) {
                if (errorCode == 10003) {
                    ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_H5_LOGIN_CANCEL, (Map<String, String>) null);
                } else if (errorCode == 10004) {
                    ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_TB_LOGIN_CANCEL, (Map<String, String>) null);
                } else if (errorCode == 10005) {
                    Map<String, String> json = new HashMap<>();
                    int code = errorMessage.code;
                    String message = errorMessage.message;
                    json.put("code", code + "");
                    if (!TextUtils.isEmpty(message)) {
                        json.put("message", message);
                    }
                    ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_TB_LOGIN_FAILURE, json);
                } else {
                    Map<String, String> json2 = new HashMap<>();
                    int code2 = errorMessage.code;
                    String message2 = errorMessage.message;
                    json2.put("code", code2 + "");
                    if (!TextUtils.isEmpty(message2)) {
                        json2.put("message", message2);
                    }
                    ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_H5_LOGIN_FAILURE, json2);
                }
            }
        }
        if (CallbackContext.mGlobalLoginCallback != null) {
            Message errorMessage2 = MessageUtils.createMessage(errorCode, new Object[0]);
            CallbackContext.mGlobalLoginCallback.onFailure(errorMessage2.code, errorMessage2.message);
        }
        if (errorCode == 10003 || errorCode == 10004) {
            CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGIN_CANCEL);
        } else {
            CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED);
        }
        if (activity != null && (activity instanceof LoginActivity)) {
            CallbackContext.activity = null;
            activity.finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onTaeSDKActivity(int requestCode, int resultCode, Intent data, BaseWebViewActivity activity, Map<Class<?>, Object> map, WebView webview) {
        SDKLogger.d("login", "onTaeSDKActivity requestCode=" + requestCode + " resultCode = " + resultCode + " authCode = " + (data == null ? "" : data.getStringExtra("result")));
        LoginCallback loginCallback = (LoginCallback) CallbackContext.loginCallback;
        LoginStatus.resetLoginFlag();
        if (requestCode == RequestCode.OPEN_H5_LOGIN) {
            if (resultCode == ResultCode.SUCCESS.code) {
                webview.reload();
            } else {
                activity.setResult(ResultCode.create(10003, new Object[0]));
            }
        } else if (requestCode == RequestCode.OPEN_TAOBAO) {
            if (resultCode == -1 && data != null) {
                new RefreshPageAfterOpenTb(activity, webview).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{data.getStringExtra("result")});
            } else if (resultCode == 0) {
                activity.setResult(ResultCode.create(10003, new Object[0]));
            } else {
                SDKLogger.e("login", "taobao return " + resultCode);
                LoginComponent.INSTANCE.showH5Login(activity);
            }
        } else if (requestCode == RequestCode.OPEN_H5_UNBIND) {
            if (resultCode == ResultCode.SUCCESS.code) {
                onLoginSuccess(activity, "E_H5_UNBIND_SUCCESS", loginCallback);
            } else {
                onLoginFailure(activity, "E_H5_UNBIND_FAILURE", loginCallback, 10003);
            }
            CallbackContext.loginCallback = null;
        }
    }
}
