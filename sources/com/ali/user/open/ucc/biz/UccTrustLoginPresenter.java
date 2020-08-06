package com.ali.user.open.ucc.biz;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import com.ali.auth.third.offline.model.ResultActionType;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.model.LoginReturnData;
import com.ali.user.open.core.model.ResultCode;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.core.util.DialogHelper;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.ucc.R;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccService;
import com.ali.user.open.ucc.UccServiceProviderFactory;
import com.ali.user.open.ucc.context.UccContext;
import com.ali.user.open.ucc.data.DataRepository;
import com.ali.user.open.ucc.model.UccParams;
import com.ali.user.open.ucc.util.UTHitConstants;
import com.ali.user.open.ucc.util.UTHitUtils;
import com.ali.user.open.ucc.util.UccConstants;
import com.ali.user.open.ucc.util.Utils;
import com.ali.user.open.ucc.webview.UccWebViewActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class UccTrustLoginPresenter {
    private static volatile UccTrustLoginPresenter instance;

    public static UccTrustLoginPresenter getInstance() {
        if (instance == null) {
            synchronized (UccTrustLoginPresenter.class) {
                if (instance == null) {
                    instance = new UccTrustLoginPresenter();
                }
            }
        }
        return instance;
    }

    public void doTrustLogin(Activity activity, UccParams uccParams, String targetSite, Map<String, String> params, UccCallback uccCallback) {
        final String str = targetSite;
        final Map<String, String> map = params;
        final UccCallback uccCallback2 = uccCallback;
        final Activity activity2 = activity;
        final UccParams uccParams2 = uccParams;
        DataRepository.trustLogin(uccParams, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                if (response != null) {
                    rpcResultHit(response.code + "", response.actionType, "");
                    if (response.code == 3000 || TextUtils.equals("SUCCESS", response.actionType)) {
                        UccBizContants.mBusyControlMap.put(str, Long.valueOf(System.currentTimeMillis()));
                        boolean cookieOnly = UccOauthLoginPresenter.isCookieOnly(map);
                        String loginReturnData = (String) response.returnValue;
                        UccServiceProviderFactory.getInstance().getUccServiceProvider(str).refreshWhenLogin(str, loginReturnData, cookieOnly);
                        if (uccCallback2 != null) {
                            Map<String, String> map = new HashMap<>();
                            map.put(UccConstants.PARAM_LOGIN_DATA, loginReturnData);
                            uccCallback2.onSuccess(str, map);
                            return;
                        }
                        return;
                    }
                    rpcResultHit(response.code + "", "", "");
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(str, response.code, Utils.buidErrorMessage(response, "免登response为空"));
                    }
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1005);
                rpcResultHit(errorCode + "", response == null ? "" : response.actionType, "");
                Integer time = UccBizContants.mTrustLoginErrorTime.get(str);
                if (time == null) {
                    time = 0;
                }
                UccBizContants.mTrustLoginErrorTime.put(str, Integer.valueOf(time.intValue() + 1));
                if (uccCallback2 != null) {
                    uccCallback2.onFail(str, errorCode, Utils.buidErrorMessage(response, "免登失败"));
                }
            }

            public void onError(String code, RpcResponse response) {
                boolean needUI = true;
                if (map != null && TextUtils.equals((CharSequence) map.get(ParamsConstants.Key.PARAM_NEED_UI), "0")) {
                    needUI = false;
                }
                int errorCode = Utils.buidErrorCode(response, 1005);
                if (needUI && TextUtils.equals("NEED_BIND", response.actionType)) {
                    rpcResultHit(errorCode + "", response.actionType, "");
                    String returnValue = (String) response.returnValue;
                    boolean noNeedBindFromParam = map != null && "0".equals(map.get(ParamsConstants.Key.PARAM_NEED_BIND));
                    if ((!Site.ICBU.equals(str) || !Site.DING.equals(AliMemberSDK.getMasterSite())) && !noNeedBindFromParam) {
                        JSONObject json = JSON.parseObject(returnValue);
                        String h5Url = "";
                        String h5Type = "";
                        if (json != null) {
                            h5Url = json.getString(ParamsConstants.Key.PARAM_H5URL);
                            h5Type = json.getString("h5Type");
                        }
                        Map<String, String> mapParams = map;
                        if (mapParams == null) {
                            mapParams = new HashMap<>();
                        }
                        if (!TextUtils.isEmpty(h5Url)) {
                            mapParams.put(UccConstants.PARAM_BIND_URL, h5Url);
                        }
                        mapParams.put("needSession", "1");
                        mapParams.put(ParamsConstants.Key.PARAM_NEED_LOCAL_COOKIE_ONLY, UccOauthLoginPresenter.isCookieOnly(map) ? "1" : "0");
                        mapParams.put(ParamsConstants.Key.PARAM_NEED_TOAST, "0");
                        mapParams.put("h5Type", h5Type);
                        ((UccService) AliMemberSDK.getService(UccService.class)).bind(activity2, uccParams2.userToken, str, mapParams, uccCallback2);
                    } else if (uccCallback2 != null) {
                        uccCallback2.onFail(str, response.code, response.message);
                    }
                } else if (!needUI || !TextUtils.equals(ResultActionType.H5, response.actionType) || response.returnValue == null) {
                    rpcResultHit(errorCode + "", response == null ? "" : response.actionType, "");
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(str, errorCode, Utils.buidErrorMessage(response, "免登失败"));
                    }
                } else {
                    JSONObject json2 = JSON.parseObject((String) response.returnValue);
                    if (json2 != null) {
                        String h5Url2 = json2.getString(ParamsConstants.Key.PARAM_H5URL);
                        String token = json2.getString("token");
                        String scene = json2.getString("scene");
                        String h5Type2 = json2.getString("h5Type");
                        Bundle bundle = new Bundle();
                        bundle.putString("url", h5Url2);
                        bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(uccParams2));
                        bundle.putString("needSession", "1");
                        bundle.putString("token", token);
                        bundle.putString("scene", scene);
                        bundle.putString("params", Utils.convertMapToJsonStr(map));
                        if (UccContext.getBindComponentProxy() == null) {
                            UccH5Presenter.openUrl(activity2, bundle, uccCallback2);
                        } else if (uccCallback2 != null) {
                            uccCallback2.onFail(str, 1114, "免登失败");
                        }
                        rpcResultHit(errorCode + "", response.actionType, h5Type2);
                        return;
                    }
                    rpcResultHit(errorCode + "", response.actionType, "");
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(str, errorCode, Utils.buidErrorMessage(response, "免登失败"));
                    }
                }
            }

            private void rpcResultHit(String code, String actionType, String h5Type) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                props.put("actionType", actionType);
                props.put("h5Type", h5Type);
                UTHitUtils.send(UTHitConstants.PageUccLogin, "UccLogin_Result", uccParams2, props);
            }
        });
    }

    public void tokenLogin(Activity activity, UccParams uccParams, String scene, String loginToken, String h5QueryString, Map<String, String> params, UccCallback uccCallback) {
        Map<String, String> props = new HashMap<>();
        props.put("token", loginToken);
        UTHitUtils.send(UTHitConstants.PageUccLogin, "UccLogin_TokenLogin", uccParams, props);
        loginByIVToken(activity, uccParams, params, loginToken, scene, h5QueryString, uccCallback);
    }

    public void upgradeLogin(Activity activity, UccParams uccParams, String targetSite, String scene, String requestToken, String callFrom, Map<String, String> params, UccCallback uccCallback) {
        Map<String, String> props = new HashMap<>();
        props.put("type", callFrom);
        props.put("scene", scene);
        props.put("requestToken", requestToken);
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccLogin_UpgradeAccount", uccParams, props);
        final String str = targetSite;
        final Map<String, String> map = params;
        final Activity activity2 = activity;
        final UccCallback uccCallback2 = uccCallback;
        final String str2 = callFrom;
        DataRepository.upgrade(uccParams, scene, requestToken, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                if (response != null) {
                    rpcResultHit(response.code + "", response.actionType, "");
                    if (response.code == 3000 || TextUtils.equals("SUCCESS", response.actionType)) {
                        UccBizContants.mBusyControlMap.put(str, Long.valueOf(System.currentTimeMillis()));
                        String loginReturnData = (String) response.returnValue;
                        boolean isNeedLocalSession = true;
                        if (map != null && TextUtils.equals((CharSequence) map.get(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION), "0")) {
                            isNeedLocalSession = false;
                        }
                        if (!TextUtils.isEmpty(loginReturnData) && isNeedLocalSession) {
                            UccServiceProviderFactory.getInstance().getUccServiceProvider(str).refreshWhenLogin(str, loginReturnData, UccOauthLoginPresenter.isCookieOnly(map));
                        }
                        UccTrustLoginPresenter.this.finishActivity(activity2);
                        if (uccCallback2 != null) {
                            Map<String, String> map = UccBindPresenter.buildSessionInfo(str, loginReturnData);
                            String loginData = JSON.parseObject(loginReturnData).getString("authorizationResponse");
                            if (TextUtils.isEmpty(loginData)) {
                                map.put(UccConstants.PARAM_LOGIN_DATA, loginReturnData);
                            } else {
                                map.put(UccConstants.PARAM_LOGIN_DATA, loginData);
                            }
                            uccCallback2.onSuccess(str, map);
                        }
                    } else if (!TextUtils.equals(ResultActionType.H5, response.actionType) || response.returnValue == null) {
                        rpcResultHit(response.code + "", "", "");
                        if (uccCallback2 != null) {
                            uccCallback2.onFail(str, response.code, Utils.buidErrorMessage(response, "免登response为空"));
                        }
                    } else {
                        JSONObject json = JSON.parseObject((String) response.returnValue);
                        if (json != null) {
                            String h5Url = json.getString(ParamsConstants.Key.PARAM_H5URL);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", h5Url);
                            bundle.putString("token", json.getString("token"));
                            bundle.putString("scene", json.getString("scene"));
                            bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(new UccParams()));
                            bundle.putString("needSession", "1");
                            UccH5Presenter.openUrl(activity2, bundle, uccCallback2);
                            if (activity2 != null && !(activity2 instanceof UccWebViewActivity)) {
                                activity2.finish();
                                return;
                            }
                            return;
                        }
                        UccTrustLoginPresenter.this.finishActivity(activity2);
                        if (uccCallback2 != null) {
                            uccCallback2.onFail(str, 1500, Utils.buidErrorMessage(response, "免登失败"));
                        }
                    }
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                rpcResultHit(Utils.buidErrorCode(response, 1500) + "", "", "");
            }

            public void onError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1500);
                rpcResultHit(errorCode + "", "", "");
                if (TextUtils.equals(str2, "h5")) {
                    DialogHelper.getInstance().alert(activity2, "", response == null ? "" : response.message, activity2.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, "", (DialogInterface.OnClickListener) null);
                    return;
                }
                UccTrustLoginPresenter.this.finishActivity(activity2);
                if (uccCallback2 != null) {
                    uccCallback2.onFail(str, errorCode, Utils.buidErrorMessage(response, "OauthLogin接口错误"));
                }
            }

            private void rpcResultHit(String code, String actionType, String h5Type) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                props.put("actionType", actionType);
                props.put("h5Type", h5Type);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccLogin_UpgradeAccountResult", (UccParams) null, props);
            }
        });
    }

    /* access modifiers changed from: private */
    public void finishActivity(Context context) {
        if (context != null && (context instanceof UccWebViewActivity)) {
            ((Activity) context).finish();
        }
    }

    public static void loginByIVToken(Activity activity, UccParams uccParams, Map<String, String> params, String token, String scene, String aliusersdk_string, UccCallback uccCallback) {
        final int site = Site.getHavanaSite(uccParams.bindSite);
        final boolean cookieOnly = UccOauthLoginPresenter.isCookieOnly(params);
        final UccCallback uccCallback2 = uccCallback;
        final Activity activity2 = activity;
        final UccParams uccParams2 = uccParams;
        final String str = token;
        DataRepository.loginByIVToken(site, token, scene, aliusersdk_string, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse loginReturnData) {
                if (loginReturnData == null || loginReturnData.returnValue == null) {
                    uccCallback2.onFail(site + "", ResultCode.SYSTEM_EXCEPTION.code, ResultCode.SYSTEM_EXCEPTION.message);
                }
                if (loginReturnData.code == 3000) {
                    rpcResultHit("3000");
                    if (activity2 != null && (activity2 instanceof UccWebViewActivity)) {
                        activity2.finish();
                    }
                    String returnData = (String) loginReturnData.returnValue;
                    UccServiceProviderFactory.getInstance().getUccServiceProvider(uccParams2.bindSite).refreshWhenLogin(uccParams2.bindSite, returnData, cookieOnly);
                    Map<String, String> resultparams = new HashMap<>();
                    resultparams.put(UccConstants.PARAM_LOGIN_DATA, returnData);
                    uccCallback2.onSuccess(site + "", resultparams);
                    return;
                }
                uccCallback2.onFail(site + "", loginReturnData.code, loginReturnData.message);
            }

            public void onSystemError(String code, RpcResponse loginReturnData) {
                rpcResultHit(code + "");
                uccCallback2.onFail(site + "", loginReturnData.code, loginReturnData.message);
                if (activity2 != null && (activity2 instanceof UccWebViewActivity)) {
                    activity2.finish();
                }
            }

            public void onError(String code, RpcResponse resultData) {
                rpcResultHit(code + "");
                if (!TextUtils.equals(ResultActionType.H5, resultData.actionType) || resultData.returnValue == null) {
                    if (activity2 != null && (activity2 instanceof UccWebViewActivity)) {
                        activity2.finish();
                    }
                    uccCallback2.onFail(site + "", resultData.code, resultData.message);
                    return;
                }
                LoginReturnData returnValue = (LoginReturnData) resultData.returnValue;
                String doubleCheckUrl = returnValue.h5Url;
                if (TextUtils.isEmpty(doubleCheckUrl) || activity2 == null) {
                    if (activity2 != null && (activity2 instanceof UccWebViewActivity)) {
                        activity2.finish();
                    }
                    uccCallback2.onFail(site + "", resultData.code, resultData.message);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("url", doubleCheckUrl);
                bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(uccParams2));
                bundle.putString("needSession", "1");
                bundle.putString("token", returnValue.token);
                bundle.putString("scene", returnValue.scene);
                if (UccContext.getBindComponentProxy() == null) {
                    UccH5Presenter.openUrl(activity2, bundle, uccCallback2);
                } else if (uccCallback2 != null) {
                    uccCallback2.onFail(site + "", 1114, "免登失败");
                }
            }

            private void rpcResultHit(String code) {
                Map<String, String> props = new HashMap<>();
                props.put("code", "" + code);
                props.put("token", str);
                UTHitUtils.send(UTHitConstants.PageUccLogin, "UccLogin_TokenLoginResult", uccParams2, props);
            }
        });
    }
}
