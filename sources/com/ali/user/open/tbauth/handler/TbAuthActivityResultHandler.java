package com.ali.user.open.tbauth.handler;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebView;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.offline.model.ResultActionType;
import com.ali.user.open.callback.LoginCallback;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.message.Message;
import com.ali.user.open.core.message.MessageUtils;
import com.ali.user.open.core.model.ResultCode;
import com.ali.user.open.core.service.MemberExecutorService;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.webview.BaseWebViewActivity;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.session.Session;
import com.ali.user.open.tbauth.RequestCode;
import com.ali.user.open.tbauth.UTConstants;
import com.ali.user.open.tbauth.context.TbAuthContext;
import com.ali.user.open.tbauth.task.RpcPresenter;
import com.ali.user.open.tbauth.ui.TbAuthActivity;
import com.ali.user.open.tbauth.ui.context.CallbackContext;
import com.ali.user.open.tbauth.ui.support.BaseActivityResultHandler;
import com.ut.mini.UTHitBuilders;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class TbAuthActivityResultHandler extends BaseActivityResultHandler {
    private static final String TAG = "login";

    /* access modifiers changed from: protected */
    public void onCallbackContext(int requestCode, int resultCode, Intent data, Activity activity, Map<Class<?>, Object> map, WebView webview) {
        String stringExtra;
        StringBuilder append = new StringBuilder().append("onCallbackContext requestCode=").append(requestCode).append(" resultCode = ").append(resultCode).append(" authCode = ");
        if (data == null) {
            stringExtra = "";
        } else {
            stringExtra = data.getStringExtra("result");
        }
        SDKLogger.d("login", append.append(stringExtra).toString());
        LoginCallback loginCallback = (LoginCallback) CallbackContext.loginCallback;
        if (requestCode != RequestCode.OPEN_H5_LOGIN || loginCallback == null) {
            if (requestCode == RequestCode.OPEN_TAOBAO && loginCallback != null) {
                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("AUTH_TAOBAO", (Map<String, String>) null);
                if (resultCode == -1 && data != null) {
                    String authCode = data.getStringExtra("result");
                    if (TextUtils.isEmpty(authCode)) {
                        onLoginFailure(activity, "E_TB_LOGIN_CANCEL", loginCallback, 10004);
                    } else if (TextUtils.equals(authCode, "00000000")) {
                        onLoginFailure(activity, "E_TB_LOGIN_SKIPL", loginCallback, 1011);
                    } else {
                        Map<String, String> props = new HashMap<>();
                        props.put("authcode", authCode);
                        props.put("is_success", "T");
                        props.put("type", "Native");
                        props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
                        ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_authcode", props);
                        if (TbAuthContext.onlyAuthCode) {
                            onLoginSuccess(activity, authCode, loginCallback);
                        } else if (TbAuthContext.needSession) {
                            RpcPresenter.refreshPageAfterOpenTb((Activity) CallbackContext.activity.get(), authCode, loginCallback);
                        } else {
                            RpcPresenter.getAccessTokenWithAuthCode((Activity) CallbackContext.activity.get(), authCode, Site.TAOBAO, loginCallback);
                        }
                    }
                } else if (resultCode == 0) {
                    onLoginFailure(activity, "E_TB_LOGIN_CANCEL", loginCallback, 10004);
                } else {
                    SDKLogger.d("login", "result from taobao : " + (data == null ? "" : data.getStringExtra("result")));
                    onLoginFailure(activity, "E_TB_LOGIN_FAILURE", loginCallback, 10005);
                }
            } else if (requestCode != RequestCode.OPEN_QR_LOGIN || loginCallback == null) {
                if (requestCode != RequestCode.OPEN_QR_LOGIN_CONFIRM || loginCallback == null) {
                    if (requestCode == RequestCode.OPEN_DOUBLE_CHECK) {
                        handleCheck(0, data, loginCallback);
                    } else if (requestCode == RequestCode.OPEN_H5_UNBIND) {
                        if (resultCode == ResultCode.SUCCESS.code) {
                            onLoginSuccess(activity, "E_H5_UNBIND_SUCCESS", loginCallback);
                        } else {
                            onLoginFailure(activity, "E_H5_UNBIND_FAILURE", loginCallback, 10003);
                        }
                        CallbackContext.loginCallback = null;
                    } else if (requestCode == RequestCode.OPEN_ICBU_H5_LOGIN && loginCallback != null) {
                        if (resultCode == ResultCode.SUCCESS.code) {
                            if (TbAuthContext.onlyAuthCode) {
                                onLoginSuccess(activity, data.getStringExtra("result"), loginCallback);
                            } else {
                                RpcPresenter.getAccessTokenWithAuthCode((Activity) CallbackContext.activity.get(), data.getStringExtra("result"), Site.ICBU, loginCallback);
                            }
                        } else if (resultCode == ResultCode.IGNORE.code) {
                        } else {
                            if (resultCode == ResultCode.CHECK.code) {
                                handleCheck(4, data, loginCallback);
                            } else {
                                onLoginFailure(activity, "E_H5_CANCEL_FAILURE", loginCallback, 10003);
                            }
                        }
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
            Map<String, String> props2 = new HashMap<>();
            props2.put("authcode", data.getStringExtra("result"));
            props2.put("is_success", "T");
            props2.put("type", ResultActionType.H5);
            props2.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
            ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_authcode", props2);
            if (TbAuthContext.onlyAuthCode) {
                onLoginSuccess(activity, data.getStringExtra("result"), loginCallback);
            } else if (TbAuthContext.needSession) {
                RpcPresenter.refreshPageAfterOpenTb((Activity) CallbackContext.activity.get(), data.getStringExtra("result"), loginCallback);
            } else {
                RpcPresenter.getAccessTokenWithAuthCode((Activity) CallbackContext.activity.get(), data.getStringExtra("result"), Site.TAOBAO, loginCallback);
            }
        } else if (resultCode == ResultCode.IGNORE.code) {
        } else {
            if (resultCode == ResultCode.CHECK.code) {
                handleCheck(0, data, loginCallback);
            } else {
                onLoginFailure(activity, "E_H5_CANCEL_FAILURE", loginCallback, 10003);
            }
        }
    }

    private void handleCheck(int site, Intent data, LoginCallback loginCallback) {
        SDKLogger.d("login", "handleCheck");
        final LoginCallback trustLoginCallback = loginCallback;
        final WeakReference<Activity> savedActivity = CallbackContext.activity;
        if (savedActivity == null || savedActivity.get() == null || data == null) {
            ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                public void run() {
                    CallbackContext.activity = savedActivity;
                    TbAuthActivityResultHandler.this.onLoginFailure((Activity) savedActivity.get(), UTConstants.E_H5_OPERATION_BIND_FAILURE, trustLoginCallback, 10003);
                    CallbackContext.activity = null;
                }
            });
        } else if (TextUtils.isEmpty(data.getStringExtra("token"))) {
            ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                public void run() {
                    CallbackContext.activity = savedActivity;
                    TbAuthActivityResultHandler.this.onLoginFailure((Activity) savedActivity.get(), UTConstants.E_IV_LOGIN_FAILURE, trustLoginCallback, 10101);
                    CallbackContext.activity = null;
                }
            });
        } else {
            RpcPresenter.loginByIVToken((Activity) savedActivity.get(), site, data.getStringExtra("token"), data.getStringExtra("scene"), data.getStringExtra(Constants.QUERY_STRING), new LoginCallback() {
                public void onFailure(int code, String msg) {
                    ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                        public void run() {
                            CallbackContext.activity = savedActivity;
                            TbAuthActivityResultHandler.this.onLoginFailure((Activity) savedActivity.get(), UTConstants.E_IV_LOGIN_FAILURE, trustLoginCallback, 10101);
                            CallbackContext.activity = null;
                        }
                    });
                }

                public void onSuccess(Session session) {
                    ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                        public void run() {
                            CallbackContext.activity = savedActivity;
                            TbAuthActivityResultHandler.this.onLoginSuccess((Activity) savedActivity.get(), UTConstants.E_H5_LOGIN_SUCCESS, trustLoginCallback);
                            CallbackContext.activity = null;
                        }
                    });
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onLoginSuccess(Activity activity, String label, LoginCallback loginCallback) {
        SDKLogger.d("login", "onLoginSuccess ");
        if (loginCallback != null) {
            Session session = new Session();
            session.topAuthCode = label;
            loginCallback.onSuccess(session);
        }
        if (CallbackContext.mGlobalLoginCallback != null) {
            CallbackContext.mGlobalLoginCallback.onSuccess(((SessionService) AliMemberSDK.getService(SessionService.class)).getSession());
        }
        if (activity != null && (activity instanceof TbAuthActivity)) {
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
            Map<String, String> props = new HashMap<>();
            props.put("code", errorCode + "");
            props.put("is_success", "F");
            if (errorCode == 10005 || errorCode == 10004) {
                props.put("type", "Native");
            } else {
                props.put("type", ResultActionType.H5);
            }
            props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
            ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_authcode", props);
        }
        if (CallbackContext.mGlobalLoginCallback != null) {
            Message errorMessage2 = MessageUtils.createMessage(errorCode, new Object[0]);
            CallbackContext.mGlobalLoginCallback.onFailure(errorMessage2.code, errorMessage2.message);
        }
        if (activity != null && (activity instanceof TbAuthActivity)) {
            CallbackContext.activity = null;
            activity.finish();
            CallbackContext.loginCallback = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onTaeSDKActivity(int requestCode, int resultCode, Intent data, BaseWebViewActivity activity, Map<Class<?>, Object> map, WebView webview) {
    }
}
