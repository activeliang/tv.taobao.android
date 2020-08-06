package com.ali.user.open.ucc.biz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.callback.CallbackManager;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.context.UccContext;
import com.ali.user.open.ucc.data.DataRepository;
import com.ali.user.open.ucc.model.FetchBindPageUrlResult;
import com.ali.user.open.ucc.model.UccParams;
import com.ali.user.open.ucc.util.UTHitConstants;
import com.ali.user.open.ucc.util.UTHitUtils;
import com.ali.user.open.ucc.util.UccConstants;
import com.ali.user.open.ucc.util.Utils;
import com.ali.user.open.ucc.webview.UccWebViewActivity;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;

public class UccH5Presenter {
    public static void showH5BindPage(Context context, UccParams uccParams, Map<String, String> params, UccCallback uccCallback) {
        String needSessionTemp = "0";
        String needToastTemp = "0";
        if (params != null) {
            needSessionTemp = TextUtils.isEmpty(params.get("needSession")) ? "0" : params.get("needSession");
            if (TextUtils.isEmpty(params.get(ParamsConstants.Key.PARAM_NEED_TOAST))) {
                needToastTemp = "0";
            } else {
                needToastTemp = params.get(ParamsConstants.Key.PARAM_NEED_TOAST);
            }
        }
        final String needSession = needSessionTemp;
        final String needToast = needToastTemp;
        if (params == null || TextUtils.isEmpty(params.get(UccConstants.PARAM_BIND_URL))) {
            if (params == null || TextUtils.isEmpty(params.get("site"))) {
                uccParams.site = AliMemberSDK.getMasterSite();
            } else {
                uccParams.site = params.get("site");
            }
            if (params != null && !TextUtils.isEmpty(params.get("scene"))) {
                uccParams.scene = params.get("scene");
            }
            uccParams.createBindSiteSession = TextUtils.equals("1", needSession);
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_GetLocalSiteUrl", uccParams, (Map<String, String>) null);
            final UccParams uccParams2 = uccParams;
            final Map<String, String> map = params;
            final Context context2 = context;
            final UccCallback uccCallback2 = uccCallback;
            DataRepository.fetchBindPageUrl(uccParams, new RpcRequestCallbackWithCode() {
                public void onSuccess(RpcResponse response) {
                    FetchBindPageUrlResult fetchBindPageUrlResult = (FetchBindPageUrlResult) response.returnValue;
                    if (fetchBindPageUrlResult != null && !TextUtils.isEmpty(fetchBindPageUrlResult.returnUrl)) {
                        rpcResultHit(response.code + "", fetchBindPageUrlResult.h5Type);
                        Map<String, String> props = new HashMap<>();
                        props.put("h5Type", fetchBindPageUrlResult.h5Type);
                        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_GoH5BindAction", uccParams2, props);
                        Bundle bundle = new Bundle();
                        bundle.putString("url", fetchBindPageUrlResult.returnUrl);
                        bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(uccParams2));
                        bundle.putString("needSession", needSession);
                        bundle.putString(ParamsConstants.Key.PARAM_NEED_TOAST, needToast);
                        bundle.putString("params", Utils.convertMapToJsonStr(map));
                        if (UccContext.getBindComponentProxy() == null) {
                            UccH5Presenter.openUrl(context2, bundle, uccCallback2);
                        } else {
                            UccContext.getBindComponentProxy().openPage(context2, bundle, uccCallback2);
                        }
                    } else if (uccCallback2 != null) {
                        uccCallback2.onFail(uccParams2.bindSite, 1401, Utils.buidErrorMessage(response, "url 为空"));
                    }
                }

                public void onSystemError(String code, RpcResponse response) {
                    int errorCode = Utils.buidErrorCode(response, 1402);
                    rpcResultHit(errorCode + "", "");
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "获取页面失败"));
                    }
                }

                public void onError(String code, RpcResponse response) {
                    int errorCode = Utils.buidErrorCode(response, 1402);
                    rpcResultHit(errorCode + "", "");
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "fetchBindPageUrl fail"));
                    }
                }

                private void rpcResultHit(String code, String h5Type) {
                    Map<String, String> props = new HashMap<>();
                    props.put("code", code);
                    props.put("h5Type", h5Type);
                    UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_GetLocalSiteUrlResult", uccParams2, props);
                }
            });
            return;
        }
        Map<String, String> props = new HashMap<>();
        props.put("h5Type", params.get("h5Type"));
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_GoH5BindAction", uccParams, props);
        Bundle bundle = new Bundle();
        bundle.putString("url", params.get(UccConstants.PARAM_BIND_URL));
        bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(uccParams));
        bundle.putString("needSession", needSession);
        bundle.putString(ParamsConstants.Key.PARAM_NEED_TOAST, needToast);
        bundle.putString("params", Utils.convertMapToJsonStr(params));
        if (UccContext.getBindComponentProxy() == null) {
            openUrl(context, bundle, uccCallback);
        } else {
            UccContext.getBindComponentProxy().openPage(context, bundle, uccCallback);
        }
    }

    public static void openUrl(Context context, Bundle bundle, UccCallback uccCallback) {
        if (context == null) {
            context = KernelContext.getApplicationContext();
        }
        CallbackManager.registerCallback(UccConstants.UCC_H5_CALLBACK_TYPE, uccCallback);
        Intent intent = new Intent(context, UccWebViewActivity.class);
        intent.putExtras(bundle);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static void showH5BindPageFoeNewBind(Context context, UccParams uccParams, String ibb, Map<String, String> params, UccCallback uccCallback) {
        String needSessionTemp = "0";
        String needToastTemp = "0";
        if (params != null) {
            needSessionTemp = TextUtils.isEmpty(params.get("needSession")) ? "0" : params.get("needSession");
            needToastTemp = TextUtils.isEmpty(params.get(ParamsConstants.Key.PARAM_NEED_TOAST)) ? "0" : params.get(ParamsConstants.Key.PARAM_NEED_TOAST);
        }
        final String needSession = needSessionTemp;
        final String needToast = needToastTemp;
        UccParams newUccParams = new UccParams();
        if (params == null || TextUtils.isEmpty(params.get("site"))) {
            newUccParams.site = AliMemberSDK.getMasterSite();
        } else {
            newUccParams.site = params.get("site");
        }
        newUccParams.bindSite = uccParams.bindSite;
        newUccParams.userToken = uccParams.userToken;
        if (params != null && !TextUtils.isEmpty(params.get("scene"))) {
            newUccParams.scene = params.get("scene");
        }
        newUccParams.createBindSiteSession = TextUtils.equals("1", needSession);
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_GetLocalSiteUrl", uccParams, (Map<String, String>) null);
        final UccParams uccParams2 = uccParams;
        final Map<String, String> map = params;
        final Context context2 = context;
        final UccCallback uccCallback2 = uccCallback;
        DataRepository.fetchNewBindPageUrl(newUccParams, ibb, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                FetchBindPageUrlResult fetchBindPageUrlResult = (FetchBindPageUrlResult) response.returnValue;
                if (fetchBindPageUrlResult != null && !TextUtils.isEmpty(fetchBindPageUrlResult.returnUrl)) {
                    rpcResultHit(response.code + "", fetchBindPageUrlResult.h5Type);
                    Map<String, String> props = new HashMap<>();
                    props.put("h5Type", fetchBindPageUrlResult.h5Type);
                    UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_GoH5BindAction", uccParams2, props);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", fetchBindPageUrlResult.returnUrl);
                    bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(uccParams2));
                    bundle.putString("needSession", needSession);
                    bundle.putString(ParamsConstants.Key.PARAM_NEED_TOAST, needToast);
                    bundle.putString("params", Utils.convertMapToJsonStr(map));
                    UccH5Presenter.openUrl(context2, bundle, uccCallback2);
                } else if (uccCallback2 != null) {
                    uccCallback2.onFail(uccParams2.bindSite, 1401, Utils.buidErrorMessage(response, "url 为空"));
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1402);
                rpcResultHit(errorCode + "", "");
                if (uccCallback2 != null) {
                    uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "获取页面失败"));
                }
            }

            public void onError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1402);
                rpcResultHit(errorCode + "", "");
                if (uccCallback2 != null) {
                    uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "fetchBindPageUrl fail"));
                }
            }

            private void rpcResultHit(String code, String h5Type) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                props.put("h5Type", h5Type);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_GetLocalSiteUrlResult", uccParams2, props);
            }
        });
    }
}
