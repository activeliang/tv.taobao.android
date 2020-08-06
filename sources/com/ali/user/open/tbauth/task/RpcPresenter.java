package com.ali.user.open.tbauth.task;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.auth.third.offline.model.ResultActionType;
import com.ali.user.open.callback.LoginCallback;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.model.LoginReturnData;
import com.ali.user.open.core.model.ResultCode;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.core.service.MemberExecutorService;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.CommonUtils;
import com.ali.user.open.device.DeviceTokenAccount;
import com.ali.user.open.device.DeviceTokenManager;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.session.Session;
import com.ali.user.open.tbauth.RequestCode;
import com.ali.user.open.tbauth.context.TbAuthContext;
import com.ali.user.open.tbauth.ui.TbAuthActivity;
import com.ali.user.open.tbauth.ui.TbAuthWebViewActivity;
import com.ali.user.open.tbauth.ui.context.CallbackContext;
import com.ali.user.open.tbauth.util.SessionConvert;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import java.util.Map;

public class RpcPresenter {
    public static String TAG = "login.tbRpc";

    public static void refreshPageAfterOpenTb(final Activity activity, String code, final LoginCallback loginCallback) {
        RpcRepository.refreshPageAfterOpenTb(code, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse resultData) {
                RpcPresenter.handleSuccess(resultData, activity, loginCallback);
            }

            public void onSystemError(String code, RpcResponse response) {
                Map<String, String> props = new HashMap<>();
                props.put("code", "10010");
                props.put("is_success", "F");
                props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_Result", props);
                CommonUtils.onFailure(loginCallback, ResultCode.create(10010, response.message));
            }

            public void onError(String code, RpcResponse response) {
                Map<String, String> props = new HashMap<>();
                props.put("code", "10010");
                props.put("is_success", "F");
                props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_Result", props);
                CommonUtils.onFailure(loginCallback, ResultCode.create(10010, response.message));
            }
        });
    }

    /* access modifiers changed from: private */
    public static void handleSuccess(final RpcResponse resultData, final Activity activity, final LoginCallback loginCallback) {
        LoginReturnData returnValue = (LoginReturnData) resultData.returnValue;
        final int code = resultData.code;
        SDKLogger.d(TAG, "asyncExecute code = " + code);
        if (code == 3000) {
            Session session = null;
            try {
                if (resultData.returnValue != null) {
                    SDKLogger.d(TAG, "asyncExecute returnValue not null ");
                    if (!TbAuthContext.needSession || TextUtils.equals(TbAuthContext.sSceneCode, "10010")) {
                        session = SessionConvert.convertLoginDataToSeesion(returnValue);
                    } else {
                        ((SessionService) AliMemberSDK.getService(SessionService.class)).refreshWhenLogin(Site.TAOBAO, returnValue);
                        session = ((SessionService) AliMemberSDK.getService(SessionService.class)).getSession();
                    }
                }
                final Session finalSession = session;
                ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                    public void run() {
                        RpcPresenter.doWhenResultOk(activity, loginCallback, finalSession);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (code == 13060) {
            String doubleCheckUrl = returnValue.h5Url;
            SDKLogger.d(TAG, "asyncExecute doubleCheckUrl = " + doubleCheckUrl);
            if (!TextUtils.isEmpty(doubleCheckUrl) && activity != null) {
                Activity startFrom = activity;
                CallbackContext.setActivity(startFrom);
                Intent intent = new Intent(startFrom, TbAuthWebViewActivity.class);
                intent.putExtra("url", doubleCheckUrl);
                intent.putExtra("token", returnValue.token);
                intent.putExtra("scene", returnValue.scene);
                TbAuthWebViewActivity.token = returnValue.token;
                TbAuthWebViewActivity.scene = returnValue.scene;
                activity.startActivityForResult(intent, RequestCode.OPEN_DOUBLE_CHECK);
            }
        } else {
            ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                public void run() {
                    SDKLogger.d(RpcPresenter.TAG, "15 : " + resultData.message);
                    RpcPresenter.doWhenResultFail(activity, loginCallback, 15, "login:code " + code + " " + resultData.message);
                }
            });
        }
    }

    public static void doWhenResultOk(Activity activity, LoginCallback loginCallback, Session session) {
        if (loginCallback != null) {
            SDKLogger.d(TAG, "asyncExecute returnValue doWhenResultOk loginCallback not null,session = " + session.toString());
            loginCallback.onSuccess(session);
            Map<String, String> props = new HashMap<>();
            props.put("type", "Native");
            props.put("is_success", "T");
            props.put("authcode", session.topAuthCode);
            props.put("openId", session.openId);
            props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
            ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_Result", props);
        }
        if (CallbackContext.mGlobalLoginCallback != null) {
            CallbackContext.mGlobalLoginCallback.onSuccess(session);
        }
        finishTempActivity(activity);
    }

    public static void doWhenResultFail(Activity activity, LoginCallback loginCallback, int code, String message) {
        if (loginCallback != null) {
            loginCallback.onFailure(code, message);
            Map<String, String> props = new HashMap<>();
            props.put("code", code + "");
            props.put("is_success", "F");
            props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
            ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_Result", props);
        }
        if (CallbackContext.mGlobalLoginCallback != null) {
            CallbackContext.mGlobalLoginCallback.onFailure(code, message);
        }
        finishTempActivity(activity);
    }

    public static void loginByIVToken(final Activity activity, int site, String token, String scene, String aliusersdk_string, final LoginCallback callback) {
        RpcRepository.loginByIVToken(site, token, scene, aliusersdk_string, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse loginReturnData) {
                if (loginReturnData == null || loginReturnData.returnValue == null) {
                    RpcPresenter.doWhenResultFail2(callback, ResultCode.SYSTEM_EXCEPTION.code, ResultCode.SYSTEM_EXCEPTION.message);
                }
                if (loginReturnData.code == 3000) {
                    if (activity != null && (activity instanceof TbAuthActivity)) {
                        activity.finish();
                    }
                    if (TbAuthContext.needSession) {
                        ((SessionService) AliMemberSDK.getService(SessionService.class)).refreshWhenLogin(Site.TAOBAO, (LoginReturnData) loginReturnData.returnValue);
                        RpcPresenter.doWhenResultOk2(callback, ((SessionService) AliMemberSDK.getService(SessionService.class)).getSession());
                        return;
                    }
                    RpcPresenter.doWhenResultOk2(callback, SessionConvert.convertLoginDataToSeesion((LoginReturnData) loginReturnData.returnValue));
                    return;
                }
                RpcPresenter.doWhenResultFail2(callback, loginReturnData.code, loginReturnData.message);
            }

            public void onSystemError(String code, RpcResponse loginReturnData) {
                RpcPresenter.doWhenResultFail2(callback, loginReturnData.code, loginReturnData.message);
            }

            public void onError(String code, RpcResponse resultData) {
                if (!TextUtils.equals(ResultActionType.H5, resultData.actionType) || resultData.returnValue == null) {
                    RpcPresenter.doWhenResultFail2(callback, resultData.code, resultData.message);
                    return;
                }
                LoginReturnData returnValue = (LoginReturnData) resultData.returnValue;
                String doubleCheckUrl = returnValue.h5Url;
                SDKLogger.d(RpcPresenter.TAG, "asyncExecute doubleCheckUrl = " + doubleCheckUrl);
                if (TextUtils.isEmpty(doubleCheckUrl) || activity == null) {
                    RpcPresenter.doWhenResultFail2(callback, resultData.code, resultData.message);
                    return;
                }
                Intent intent = new Intent(activity, TbAuthWebViewActivity.class);
                intent.putExtra("url", doubleCheckUrl);
                intent.putExtra("token", returnValue.token);
                intent.putExtra("scene", returnValue.scene);
                TbAuthWebViewActivity.token = returnValue.token;
                TbAuthWebViewActivity.scene = returnValue.scene;
                activity.startActivityForResult(intent, RequestCode.OPEN_DOUBLE_CHECK);
            }
        });
    }

    static void doWhenResultFail2(final LoginCallback mLoginCallback, final int code, final String message) {
        ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
            public void run() {
                Map<String, String> props = new HashMap<>();
                props.put("code", code + "");
                props.put("is_success", "F");
                props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_Result", props);
                if (mLoginCallback != null) {
                    mLoginCallback.onFailure(code, message);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public static void doWhenResultOk2(final LoginCallback mLoginCallback, final Session session) {
        ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
            public void run() {
                if (mLoginCallback != null) {
                    mLoginCallback.onSuccess(session);
                }
            }
        });
    }

    public static void loginByRefreshToken(final LoginCallback callback) {
        RpcRepository.loginByRefreshToken(new RpcRequestCallbackWithCode() {
            public void onSuccess(final RpcResponse resultData) {
                final int code = resultData.code;
                SDKLogger.d(RpcPresenter.TAG, "asyncExecute code = " + code);
                if (code == 3000) {
                    Session session = null;
                    try {
                        if (resultData.returnValue != null) {
                            if (TbAuthContext.needSession) {
                                ((SessionService) AliMemberSDK.getService(SessionService.class)).refreshWhenLogin(Site.TAOBAO, (LoginReturnData) resultData.returnValue);
                                session = ((SessionService) AliMemberSDK.getService(SessionService.class)).getSession();
                            } else {
                                session = SessionConvert.convertLoginDataToSeesion((LoginReturnData) resultData.returnValue);
                            }
                        }
                        final Session finalSession = session;
                        ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                            public void run() {
                                RpcPresenter.doWhenResultOk3(callback, finalSession);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                        public void run() {
                            SDKLogger.d(RpcPresenter.TAG, "15 : " + resultData.message);
                            RpcPresenter.doWhenResultFail3(callback, 15, "login:code " + code + " " + resultData.message);
                        }
                    });
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                RpcPresenter.doWhenResultFail3(callback, response.code, response.message);
            }

            public void onError(String code, RpcResponse response) {
                RpcPresenter.doWhenResultFail3(callback, response.code, response.message);
            }
        });
    }

    static void doWhenResultFail3(LoginCallback loginCallback, int code, String message) {
        if (loginCallback != null) {
            Map<String, String> props = new HashMap<>();
            props.put("code", code + "");
            props.put("is_success", "F");
            props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
            ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_Result", props);
            if (loginCallback != null) {
                loginCallback.onFailure(code, message);
            }
        }
    }

    static void doWhenResultOk3(LoginCallback loginCallback, Session session) {
        if (loginCallback != null) {
            Map<String, String> props = new HashMap<>();
            props.put("type", "Native");
            props.put("is_success", "T");
            props.put("authcode", session.topAuthCode);
            props.put("openId", session.openId);
            props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
            ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_Result", props);
            loginCallback.onSuccess(session);
        }
    }

    public static void getAccessTokenWithAuthCode(final Activity activity, final String authCode, String targetSite, final LoginCallback mLoginCallback) {
        Map<String, String> props = new HashMap<>();
        props.put("authcode", authCode);
        props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
        ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_openId", props);
        RpcRepository.getAccessTokenWithAuthCode(authCode, targetSite, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse rpcResponse) {
                if (ResultCode.SUCCESS.message.equals(rpcResponse.actionType)) {
                    final ConvertAuthCodeToAccessTokenData data = (ConvertAuthCodeToAccessTokenData) rpcResponse.returnValue;
                    if (data != null) {
                        RpcPresenter.saveDeviceToken(data);
                        ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                            public void run() {
                                Map<String, String> props = new HashMap<>();
                                props.put("authcode", authCode);
                                props.put("is_success", "T");
                                props.put("openId", data.openId);
                                props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
                                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_openIdResult", props);
                                RpcPresenter.finishTempActivity(activity);
                                Session session = new Session();
                                session.openId = data.openId;
                                session.bindToken = data.bindToken;
                                session.topAccessToken = data.accessToken;
                                session.topAuthCode = data.authCode;
                                session.openSid = data.openSid;
                                mLoginCallback.onSuccess(session);
                            }
                        });
                        return;
                    }
                    return;
                }
                rpcResultFailHit("10010");
                CommonUtils.onFailure(mLoginCallback, ResultCode.create(10010, ""));
            }

            public void onSystemError(String code, RpcResponse response) {
                rpcResultFailHit(code);
                RpcPresenter.finishTempActivity(activity);
                if (response != null) {
                    CommonUtils.onFailure(mLoginCallback, response.code, response.message);
                    return;
                }
                CommonUtils.onFailure(mLoginCallback, ResultCode.create(10010, ""));
            }

            public void onError(String code, RpcResponse response) {
                rpcResultFailHit(code);
                RpcPresenter.finishTempActivity(activity);
                if (response != null) {
                    CommonUtils.onFailure(mLoginCallback, response.code, response.message);
                    return;
                }
                CommonUtils.onFailure(mLoginCallback, ResultCode.create(10010, ""));
            }

            private void rpcResultFailHit(String code) {
                Map<String, String> props = new HashMap<>();
                props.put("is_success", "F");
                props.put("code", code);
                props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_openIdResult", props);
            }
        });
    }

    /* access modifiers changed from: private */
    public static void finishTempActivity(Activity activity) {
        if (activity != null && (activity instanceof TbAuthActivity)) {
            CallbackContext.activity = null;
            activity.finish();
        }
    }

    static void saveDeviceToken(ConvertAuthCodeToAccessTokenData data) {
        if (data != null && data.deviceToken != null) {
            DeviceTokenAccount deviceTokenAccount = new DeviceTokenAccount();
            deviceTokenAccount.site = Site.TAOBAO;
            deviceTokenAccount.tokenKey = data.deviceToken.key;
            deviceTokenAccount.openId = data.openId;
            deviceTokenAccount.hid = data.hid;
            DeviceTokenManager.getInstance().putDeviceToken(deviceTokenAccount, data.deviceToken.salt);
        }
    }

    public static void validataAuthCode(String authCode, String site, String targetSite, final LoginCallback mLoginCallback) {
        RpcRepository.validateAuthCode(authCode, site, targetSite, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse rpcResponse) {
                if (ResultCode.SUCCESS.message.equals(rpcResponse.actionType)) {
                    final ConvertAuthCodeToAccessTokenData data = (ConvertAuthCodeToAccessTokenData) rpcResponse.returnValue;
                    if (data != null) {
                        RpcPresenter.saveDeviceToken(data);
                        ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                            public void run() {
                                Session session = new Session();
                                session.openId = data.openId;
                                session.bindToken = data.bindToken;
                                session.topAccessToken = data.accessToken;
                                session.topAuthCode = data.authCode;
                                session.hid = data.hid;
                                mLoginCallback.onSuccess(session);
                            }
                        });
                        return;
                    }
                    return;
                }
                CommonUtils.onFailure(mLoginCallback, ResultCode.create(10010, ""));
            }

            public void onSystemError(String code, RpcResponse response) {
                CommonUtils.onFailure(mLoginCallback, response.code, response.message);
            }

            public void onError(String code, RpcResponse response) {
                CommonUtils.onFailure(mLoginCallback, response.code, response.message);
            }
        });
    }
}
