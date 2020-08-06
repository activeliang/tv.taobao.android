package com.ali.user.open.ucc.biz;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import com.ali.auth.third.offline.model.ResultActionType;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccServiceProviderFactory;
import com.ali.user.open.ucc.data.DataRepository;
import com.ali.user.open.ucc.model.UccParams;
import com.ali.user.open.ucc.util.UTHitConstants;
import com.ali.user.open.ucc.util.UTHitUtils;
import com.ali.user.open.ucc.util.UccConstants;
import com.ali.user.open.ucc.util.Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class UccOauthLoginPresenter {
    private static volatile UccOauthLoginPresenter instance;

    public static UccOauthLoginPresenter getInstance() {
        if (instance == null) {
            synchronized (UccOauthLoginPresenter.class) {
                if (instance == null) {
                    instance = new UccOauthLoginPresenter();
                }
            }
        }
        return instance;
    }

    public void doUccOAuthLogin(Activity activity, UccParams uccParams, Map<String, String> params, UccCallback uccCallback) {
        final UccParams uccParams2 = uccParams;
        final Map<String, String> map = params;
        final UccCallback uccCallback2 = uccCallback;
        final Activity activity2 = activity;
        DataRepository.uccOAuthLogin(uccParams, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                if (response != null) {
                    rpcResultHit(response.code + "", response.actionType, "");
                    if (response.code == 3000 || TextUtils.equals("SUCCESS", response.actionType)) {
                        UccBizContants.mBusyControlMap.put(uccParams2.bindSite, Long.valueOf(System.currentTimeMillis()));
                        String loginReturnData = (String) response.returnValue;
                        UccServiceProviderFactory.getInstance().getUccServiceProvider(uccParams2.bindSite).refreshWhenLogin(uccParams2.bindSite, loginReturnData, UccOauthLoginPresenter.isCookieOnly(map));
                        if (uccCallback2 != null) {
                            Map<String, String> map = new HashMap<>();
                            map.put(UccConstants.PARAM_LOGIN_DATA, JSON.parseObject(loginReturnData).getString("authorizationResponse"));
                            uccCallback2.onSuccess(uccParams2.bindSite, map);
                            return;
                        }
                        return;
                    }
                    rpcResultHit(response.code + "", "", "");
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(uccParams2.bindSite, response.code, Utils.buidErrorMessage(response, "免登response为空"));
                    }
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1005);
                rpcResultHit(errorCode + "", response == null ? "" : response.actionType, "");
                Integer time = UccBizContants.mTrustLoginErrorTime.get(uccParams2.bindSite);
                if (time == null) {
                    time = 0;
                }
                UccBizContants.mTrustLoginErrorTime.put(uccParams2.bindSite, Integer.valueOf(time.intValue() + 1));
                if (uccCallback2 != null) {
                    uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "免登失败"));
                }
            }

            public void onError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1005);
                if (TextUtils.equals("NEED_BIND", response.actionType)) {
                    rpcResultHit(errorCode + "", response.actionType, "");
                    JSONObject json = JSON.parseObject((String) response.returnValue);
                    String h5Url = "";
                    String h5Type = "";
                    if (json != null) {
                        h5Url = json.getString("returnUrl");
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
                    mapParams.put(ParamsConstants.Key.PARAM_NEED_TOAST, "0");
                    mapParams.put("h5Type", h5Type);
                    UccH5Presenter.showH5BindPage(activity2, uccParams2, mapParams, uccCallback2);
                } else if (!TextUtils.equals(ResultActionType.H5, response.actionType) || response.returnValue == null) {
                    rpcResultHit(errorCode + "", response == null ? "" : response.actionType, "");
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "免登失败"));
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
                        UccH5Presenter.openUrl(activity2, bundle, uccCallback2);
                        rpcResultHit(errorCode + "", response.actionType, h5Type2);
                        return;
                    }
                    rpcResultHit(errorCode + "", response.actionType, "");
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "免登失败"));
                    }
                }
            }

            private void rpcResultHit(String code, String actionType, String h5Type) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                props.put("actionType", actionType);
                props.put("h5Type", h5Type);
                UTHitUtils.send(UTHitConstants.PageUccOAuthLogin, "UccOAuthLogin_Result", uccParams2, props);
            }
        });
    }

    public static boolean isCookieOnly(Map<String, String> params) {
        if (params == null || !"1".equals(params.get(ParamsConstants.Key.PARAM_NEED_LOCAL_COOKIE_ONLY))) {
            return false;
        }
        return true;
    }
}
