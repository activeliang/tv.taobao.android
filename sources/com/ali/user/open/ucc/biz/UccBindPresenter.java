package com.ali.user.open.ucc.biz;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.ali.auth.third.offline.model.ResultActionType;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.core.service.MemberExecutorService;
import com.ali.user.open.core.util.DialogHelper;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.ucc.DefaultUccServiceProviderImpl;
import com.ali.user.open.ucc.R;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccServiceProvider;
import com.ali.user.open.ucc.alipay3.AlipayUccServiceProviderImpl;
import com.ali.user.open.ucc.context.UccContext;
import com.ali.user.open.ucc.data.DataRepository;
import com.ali.user.open.ucc.eleme.ElemeUccServiceProviderImpl;
import com.ali.user.open.ucc.model.BindResult;
import com.ali.user.open.ucc.model.UccParams;
import com.ali.user.open.ucc.taobao.TaobaoUccServiceProviderImpl;
import com.ali.user.open.ucc.ui.UccActivity;
import com.ali.user.open.ucc.util.UTHitConstants;
import com.ali.user.open.ucc.util.UTHitUtils;
import com.ali.user.open.ucc.util.UccConstants;
import com.ali.user.open.ucc.util.Utils;
import com.ali.user.open.ucc.webview.UccWebViewActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import java.util.HashMap;
import java.util.Map;

public class UccBindPresenter {
    public static final String TAG = "TaobaoUccServiceProviderImpl";
    private static volatile UccBindPresenter instance;

    public static UccBindPresenter getInstance() {
        if (instance == null) {
            synchronized (UccBindPresenter.class) {
                if (instance == null) {
                    instance = new UccBindPresenter();
                }
            }
        }
        return instance;
    }

    public void bindByNativeAuth(Activity context, UccParams uccParams, String bindUserToken, String bindUserTokenType, Map<String, String> params, UccCallback uccCallback) {
        Map<String, String> props = new HashMap<>();
        String sourceType = "native";
        if (context != null && (context instanceof UccWebViewActivity)) {
            sourceType = ResultActionType.H5;
        }
        final String sourceTypeFinal = sourceType;
        props.put("type", sourceType);
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_NativeAuthBind", uccParams, props);
        if (uccParams == null) {
            uccParams = new UccParams();
        }
        final UccParams newUccParams = uccParams;
        if (params == null || TextUtils.isEmpty(params.get("site"))) {
            newUccParams.site = AliMemberSDK.getMasterSite();
        } else {
            newUccParams.site = params.get("site");
        }
        newUccParams.bindSite = uccParams.bindSite;
        newUccParams.userToken = uccParams.userToken;
        newUccParams.bindUserToken = bindUserToken;
        newUccParams.bindUserTokenType = bindUserTokenType;
        newUccParams.requestToken = params.get("requestToken");
        newUccParams.scene = params.get("scene");
        if (params == null || !TextUtils.equals("1", params.get("needSession"))) {
            newUccParams.createBindSiteSession = false;
        } else {
            newUccParams.createBindSiteSession = true;
        }
        final Activity activity = context;
        final Map<String, String> map = params;
        final UccCallback uccCallback2 = uccCallback;
        RpcRequestCallbackWithCode callback = new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                rpcResultlHit(response.code + "", response.actionType);
                String loginReturnData = (String) response.returnValue;
                if (TextUtils.equals("CHANGEBIND", response.actionType)) {
                    UccBindPresenter.this.changeBind(activity, newUccParams, 0, response.message, ((BindResult) JSON.parseObject(loginReturnData, BindResult.class)).changeBindToken, "0", map, uccCallback2);
                } else if (TextUtils.equals("CONFLICTUPGRADE", response.actionType)) {
                    UccBindPresenter.this.conflictupgrade(activity, newUccParams, 0, response.message, ((BindResult) JSON.parseObject(loginReturnData, BindResult.class)).requestToken, "0", map, uccCallback2);
                } else {
                    HashMap hashMap = new HashMap();
                    hashMap.put(MtopJSBridge.MtopJSParam.NEED_LOGIN, newUccParams.createBindSiteSession ? "T" : "F");
                    UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Success", newUccParams, hashMap);
                    boolean isNeedLocalSession = true;
                    if (map != null && TextUtils.equals((CharSequence) map.get(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION), "0")) {
                        isNeedLocalSession = false;
                    }
                    boolean cookieOnly = UccOauthLoginPresenter.isCookieOnly(map);
                    if (!TextUtils.isEmpty(loginReturnData) && isNeedLocalSession) {
                        UccBindPresenter.this.refreshWhenLogin(newUccParams.bindSite, loginReturnData, cookieOnly);
                    }
                    UccBindPresenter.this.finishActivity(activity);
                    if (uccCallback2 != null) {
                        Map<String, String> resultparams = UccBindPresenter.buildSessionInfo(newUccParams.bindSite, loginReturnData);
                        String loginData = JSON.parseObject(loginReturnData).getString("authorizationResponse");
                        if (TextUtils.isEmpty(loginData)) {
                            resultparams.put(UccConstants.PARAM_LOGIN_DATA, loginReturnData);
                        } else {
                            resultparams.put(UccConstants.PARAM_LOGIN_DATA, loginData);
                        }
                        uccCallback2.onSuccess(newUccParams.bindSite, resultparams);
                    }
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1007);
                rpcResultlHit(errorCode + "", "");
                UccBindPresenter.this.finishActivity(activity);
                if (uccCallback2 != null) {
                    uccCallback2.onFail(newUccParams.bindSite, errorCode, Utils.buidErrorMessage(response, "bindByNativeAuth接口报错"));
                }
            }

            public void onError(String code, final RpcResponse response) {
                final int errorCode = Utils.buidErrorCode(response, 1007);
                rpcResultlHit(errorCode + "", "");
                if (TextUtils.equals(ResultActionType.H5, response.actionType) && response.returnValue != null) {
                    JSONObject json = JSON.parseObject((String) response.returnValue);
                    if (json != null) {
                        String h5Url = json.getString("returnUrl");
                        Bundle bundle = new Bundle();
                        bundle.putString("url", h5Url);
                        bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(newUccParams));
                        bundle.putString("needSession", "1");
                        bundle.putString("params", Utils.convertMapToJsonStr(map));
                        if (UccContext.getBindComponentProxy() == null) {
                            UccH5Presenter.openUrl(activity, bundle, uccCallback2);
                        } else if (uccCallback2 != null) {
                            uccCallback2.onFail(newUccParams.bindSite, 1114, "免登失败");
                        }
                        if (activity != null && !(activity instanceof UccWebViewActivity)) {
                            activity.finish();
                            return;
                        }
                        return;
                    }
                    UccBindPresenter.this.finishActivity(activity);
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(newUccParams.bindSite, errorCode, Utils.buidErrorMessage(response, "bindByNativeAuth接口报错"));
                    }
                } else if (TextUtils.equals(ResultActionType.TOAST, response.actionType) && !TextUtils.isEmpty(response.message)) {
                    ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), response.message, 0).show();
                        }
                    });
                    if (!(activity instanceof UccWebViewActivity) && uccCallback2 != null) {
                        uccCallback2.onFail(newUccParams.bindSite, errorCode, Utils.buidErrorMessage(response, "bindByNativeAuth接口报错"));
                    }
                } else if (!TextUtils.equals(ResultActionType.ALERT, response.actionType) || TextUtils.isEmpty(response.message)) {
                    UccBindPresenter.this.finishActivity(activity);
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(newUccParams.bindSite, errorCode, Utils.buidErrorMessage(response, "bindByNativeAuth接口报错"));
                    }
                } else {
                    DialogHelper.getInstance().alert(activity, "", response.message, activity.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (activity != null && !(activity instanceof UccWebViewActivity) && uccCallback2 != null) {
                                uccCallback2.onFail(newUccParams.bindSite, errorCode, Utils.buidErrorMessage(response, "bindByNativeAuth接口报错"));
                            }
                        }
                    }, "", (DialogInterface.OnClickListener) null);
                }
            }

            private void rpcResultlHit(String code, String actionType) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                props.put("type", sourceTypeFinal);
                props.put("actionType", actionType);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_NativeAuthBindResult", newUccParams, props);
            }
        };
        if (params == null || TextUtils.isEmpty(newUccParams.requestToken) || !TextUtils.equals("UCC_Upgrade", params.get("from"))) {
            DataRepository.bindByNativeAuth(newUccParams, callback);
        } else {
            DataRepository.bindByRequestToken(newUccParams, callback);
        }
    }

    public void bindByRequestToken(Activity context, UccParams uccParams, String requestToken, String bindUserToken, String bindUserTokenType, Map<String, String> params, UccCallback uccCallback) {
        String sourceType = "native";
        if (context != null && (context instanceof UccWebViewActivity)) {
            sourceType = ResultActionType.H5;
        }
        final String sourceTypeFinal = sourceType;
        Map<String, String> props = new HashMap<>();
        props.put("type", sourceType);
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_BindByRequestToken", uccParams, props);
        if (uccParams == null) {
            uccParams = new UccParams();
        }
        final UccParams newUccParams = uccParams;
        if (params == null || TextUtils.isEmpty(params.get("site"))) {
            newUccParams.site = AliMemberSDK.getMasterSite();
        } else {
            newUccParams.site = params.get("site");
        }
        newUccParams.bindSite = uccParams.bindSite;
        newUccParams.userToken = uccParams.userToken;
        if (TextUtils.isEmpty(bindUserToken)) {
            bindUserToken = "";
        }
        newUccParams.bindUserToken = bindUserToken;
        if (TextUtils.isEmpty(bindUserTokenType)) {
            bindUserTokenType = "";
        }
        newUccParams.bindUserTokenType = bindUserTokenType;
        if (TextUtils.isEmpty(requestToken)) {
            requestToken = "";
        }
        newUccParams.requestToken = requestToken;
        newUccParams.scene = params.get("scene");
        if (params == null || !TextUtils.equals("1", params.get("needSession"))) {
            newUccParams.createBindSiteSession = false;
        } else {
            newUccParams.createBindSiteSession = true;
        }
        final Activity activity = context;
        final Map<String, String> map = params;
        final UccCallback uccCallback2 = uccCallback;
        RpcRequestCallbackWithCode callback = new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                rpcResultlHit(response.code + "", response.actionType);
                String loginReturnData = (String) response.returnValue;
                if (TextUtils.equals("CHANGEBIND", response.actionType)) {
                    UccBindPresenter.this.changeBind(activity, newUccParams, 0, response.message, ((BindResult) JSON.parseObject(loginReturnData, BindResult.class)).changeBindToken, "0", map, uccCallback2);
                } else if (TextUtils.equals("CONFLICTUPGRADE", response.actionType)) {
                    UccBindPresenter.this.conflictupgrade(activity, newUccParams, 0, response.message, ((BindResult) JSON.parseObject(loginReturnData, BindResult.class)).requestToken, "0", map, uccCallback2);
                } else {
                    HashMap hashMap = new HashMap();
                    hashMap.put(MtopJSBridge.MtopJSParam.NEED_LOGIN, newUccParams.createBindSiteSession ? "T" : "F");
                    UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Success", newUccParams, hashMap);
                    boolean isNeedLocalSession = true;
                    if (map != null && TextUtils.equals((CharSequence) map.get(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION), "0")) {
                        isNeedLocalSession = false;
                    }
                    boolean cookieOnly = UccOauthLoginPresenter.isCookieOnly(map);
                    if (!TextUtils.isEmpty(loginReturnData) && isNeedLocalSession) {
                        UccBindPresenter.this.refreshWhenLogin(newUccParams.bindSite, loginReturnData, cookieOnly);
                    }
                    UccBindPresenter.this.finishActivity(activity);
                    if (uccCallback2 != null) {
                        Map<String, String> resultparams = UccBindPresenter.buildSessionInfo(newUccParams.bindSite, loginReturnData);
                        String loginData = JSON.parseObject(loginReturnData).getString("authorizationResponse");
                        if (TextUtils.isEmpty(loginData)) {
                            resultparams.put(UccConstants.PARAM_LOGIN_DATA, loginReturnData);
                        } else {
                            resultparams.put(UccConstants.PARAM_LOGIN_DATA, loginData);
                        }
                        uccCallback2.onSuccess(newUccParams.bindSite, resultparams);
                    }
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1007);
                rpcResultlHit(errorCode + "", "");
                UccBindPresenter.this.finishActivity(activity);
                if (uccCallback2 != null) {
                    uccCallback2.onFail(newUccParams.bindSite, errorCode, Utils.buidErrorMessage(response, "bindByNativeAuth接口报错"));
                }
            }

            public void onError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1007);
                rpcResultlHit(errorCode + "", "");
                if (!TextUtils.equals(ResultActionType.H5, response.actionType) || response.returnValue == null) {
                    UccBindPresenter.this.finishActivity(activity);
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(newUccParams.bindSite, errorCode, Utils.buidErrorMessage(response, "bindByNativeAuth接口报错"));
                        return;
                    }
                    return;
                }
                JSONObject json = JSON.parseObject((String) response.returnValue);
                if (json != null) {
                    String h5Url = json.getString("returnUrl");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", h5Url);
                    bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(newUccParams));
                    bundle.putString("needSession", "1");
                    bundle.putString("params", Utils.convertMapToJsonStr(map));
                    if (UccContext.getBindComponentProxy() == null) {
                        UccH5Presenter.openUrl(activity, bundle, uccCallback2);
                    } else if (uccCallback2 != null) {
                        uccCallback2.onFail(newUccParams.bindSite, 1114, "免登失败");
                    }
                    if (activity != null && !(activity instanceof UccWebViewActivity)) {
                        activity.finish();
                        return;
                    }
                    return;
                }
                UccBindPresenter.this.finishActivity(activity);
                if (uccCallback2 != null) {
                    uccCallback2.onFail(newUccParams.bindSite, errorCode, Utils.buidErrorMessage(response, "bindByNativeAuth接口报错"));
                }
            }

            private void rpcResultlHit(String code, String actionType) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                props.put("type", sourceTypeFinal);
                props.put("actionType", actionType);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_BindByRequestTokenResult", newUccParams, props);
            }
        };
        if (newUccParams == null || TextUtils.isEmpty(newUccParams.requestToken)) {
            uccCallback.onFail(newUccParams.bindSite, -1, "token.authcode入参报错");
            return;
        }
        DataRepository.bindByRequestToken(newUccParams, callback);
    }

    /* access modifiers changed from: private */
    public void changeBind(Activity context, UccParams uccParams, int bizType, String bindMessage, String changeBindToken, String needToast, Map<String, String> params, UccCallback uccCallback) {
        if (context == null || !(context instanceof Activity)) {
            finishActivity(context);
            if (uccCallback != null) {
                uccCallback.onFail(uccParams.bindSite, 1008, "换绑失败");
                return;
            }
            return;
        }
        final Map<String, String> utProps = new HashMap<>();
        utProps.put("changeBindToken", changeBindToken);
        if (context == null || !(context instanceof UccWebViewActivity)) {
            utProps.put("type", "native");
        } else {
            utProps.put("type", ResultActionType.H5);
        }
        DialogHelper instance2 = DialogHelper.getInstance();
        String string = context.getString(R.string.member_sdk_continue_bind);
        final UccParams uccParams2 = uccParams;
        final Activity activity = context;
        final int i = bizType;
        final String str = changeBindToken;
        final String str2 = needToast;
        final Map<String, String> map = params;
        final UccCallback uccCallback2 = uccCallback;
        AnonymousClass3 r3 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_ChangeBind", uccParams2, utProps);
                UccBindPresenter.this.doChangeBind(activity, uccParams2, i, str, str2, map, uccCallback2);
            }
        };
        final UccParams uccParams3 = uccParams;
        final Activity activity2 = context;
        DialogHelper dialogHelper = instance2;
        Activity activity3 = context;
        String str3 = "";
        String str4 = bindMessage;
        String str5 = string;
        AnonymousClass3 r12 = r3;
        dialogHelper.alert(activity3, str3, str4, str5, r12, context.getString(R.string.member_sdk_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_ChangeBindCancel", uccParams3, utProps);
                Log.e("ljyljy", "close关闭");
                activity2.finish();
            }
        });
    }

    /* access modifiers changed from: private */
    public void conflictupgrade(Context context, UccParams uccParams, int bizType, String bindMessage, String requestToken, String needToast, Map<String, String> params, UccCallback uccCallback) {
        if (context == null || !(context instanceof Activity)) {
            finishActivity(context);
            if (uccCallback != null) {
                uccCallback.onFail(uccParams.bindSite, 1008, "换绑失败");
                return;
            }
            return;
        }
        final Map<String, String> utProps = new HashMap<>();
        utProps.put("requestToken", requestToken);
        String callFromTemp = "native";
        if (context == null || !(context instanceof UccWebViewActivity)) {
            utProps.put("type", "native");
        } else {
            utProps.put("type", ResultActionType.H5);
            callFromTemp = ResultActionType.H5;
        }
        final String callFrom = callFromTemp;
        final UccParams uccParams2 = uccParams;
        final Context context2 = context;
        final String str = requestToken;
        final Map<String, String> map = params;
        final UccCallback uccCallback2 = uccCallback;
        final UccParams uccParams3 = uccParams;
        final Map<String, String> map2 = utProps;
        final Context context3 = context;
        final String str2 = callFrom;
        final Map<String, String> map3 = params;
        final UccCallback uccCallback3 = uccCallback;
        DialogHelper.getInstance().alert((Activity) context, "", bindMessage, context.getString(R.string.member_sdk_continue_upgrade), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_ConflictUpgradePositive", uccParams2, utProps);
                UccTrustLoginPresenter.getInstance().upgradeLogin((Activity) context2, uccParams2, uccParams2.bindSite, uccParams2.scene, str, callFrom, map, uccCallback2);
            }
        }, context.getString(R.string.member_sdk_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_ConflictUpgradeNagetive", uccParams3, map2);
                UccBindPresenter.this.skipUpgrade(context3, uccParams3, str2, map3, uccCallback3);
            }
        });
    }

    /* access modifiers changed from: private */
    public void doChangeBind(Activity context, UccParams uccParams, int bizType, String changeBindToken, String needToast, Map<String, String> params, UccCallback uccCallback) {
        final Activity activity = context;
        final UccParams uccParams2 = uccParams;
        final Map<String, String> map = params;
        final UccCallback uccCallback2 = uccCallback;
        final String str = needToast;
        final int i = bizType;
        DataRepository.changeBind(uccParams, changeBindToken, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                rpcResultlHit(response.code + "");
                String loginReturnData = (String) response.returnValue;
                if (TextUtils.equals("CONFLICTUPGRADE", response.actionType)) {
                    UccBindPresenter.this.conflictupgrade(activity, uccParams2, 0, response.message, ((BindResult) JSON.parseObject(loginReturnData, BindResult.class)).requestToken, "0", map, uccCallback2);
                } else if (!TextUtils.equals(ResultActionType.H5, response.actionType) || response.returnValue == null) {
                    HashMap hashMap = new HashMap();
                    String needLogin = "T";
                    if (TextUtils.isEmpty(loginReturnData) || loginReturnData.length() < 10) {
                        needLogin = "F";
                    }
                    hashMap.put(MtopJSBridge.MtopJSParam.NEED_LOGIN, needLogin);
                    UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Success", uccParams2, hashMap);
                    boolean isNeedLocalSession = true;
                    if (map != null && TextUtils.equals((CharSequence) map.get(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION), "0")) {
                        isNeedLocalSession = false;
                    }
                    boolean cookieOnly = UccOauthLoginPresenter.isCookieOnly(map);
                    if (!TextUtils.isEmpty(loginReturnData) && isNeedLocalSession) {
                        UccBindPresenter.this.refreshWhenLogin(uccParams2.bindSite, loginReturnData, cookieOnly);
                    }
                    if (activity != null && !TextUtils.isEmpty(response.message) && TextUtils.equals(str, "1")) {
                        final RpcResponse rpcResponse = response;
                        ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), rpcResponse.message, 0).show();
                            }
                        });
                    }
                    UccBindPresenter.this.finishActivity(activity);
                    if (uccCallback2 != null) {
                        Map<String, String> resultparams = UccBindPresenter.buildSessionInfo(uccParams2.bindSite, loginReturnData);
                        String loginData = JSON.parseObject(loginReturnData).getString("authorizationResponse");
                        if (TextUtils.isEmpty(loginData)) {
                            resultparams.put(UccConstants.PARAM_LOGIN_DATA, loginReturnData);
                        } else {
                            resultparams.put(UccConstants.PARAM_LOGIN_DATA, loginData);
                        }
                        uccCallback2.onSuccess(uccParams2.bindSite, resultparams);
                    }
                } else {
                    JSONObject json = JSON.parseObject(loginReturnData);
                    if (json != null) {
                        String h5Url = json.getString("returnUrl");
                        Bundle bundle = new Bundle();
                        bundle.putString("url", h5Url);
                        bundle.putString("token", json.getString("trustLoginToken"));
                        bundle.putString("scene", json.getString("scene"));
                        bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(uccParams2));
                        bundle.putString("needSession", "1");
                        bundle.putString("params", Utils.convertMapToJsonStr(map));
                        if (UccContext.getBindComponentProxy() == null) {
                            UccH5Presenter.openUrl(activity, bundle, uccCallback2);
                        } else if (uccCallback2 != null) {
                            uccCallback2.onFail(uccParams2.bindSite, 1114, "免登失败");
                        }
                        if (activity != null && !(activity instanceof UccWebViewActivity)) {
                            activity.finish();
                            return;
                        }
                        return;
                    }
                    UccBindPresenter.this.finishActivity(activity);
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(uccParams2.bindSite, 1008, Utils.buidErrorMessage(response, "换绑失败"));
                    }
                }
            }

            public void onSystemError(String code, final RpcResponse response) {
                final int errorCode = Utils.buidErrorCode(response, 1008);
                rpcResultlHit(errorCode + "");
                final String message = response == null ? "" : response.message;
                if (i == 1) {
                    DialogHelper.getInstance().alert(activity, "", message, activity.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            UccBindPresenter.this.finishActivity(activity);
                            if (uccCallback2 != null) {
                                uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "换绑失败"));
                            }
                        }
                    }, "", (DialogInterface.OnClickListener) null);
                } else {
                    ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), message, 0).show();
                            UccBindPresenter.this.finishActivity(activity);
                        }
                    });
                }
            }

            public void onError(String code, final RpcResponse response) {
                final int errorCode = Utils.buidErrorCode(response, 1008);
                rpcResultlHit(errorCode + "");
                final String message = response == null ? "" : response.message;
                if (i == 1) {
                    DialogHelper.getInstance().alert(activity, "", message, activity.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            UccBindPresenter.this.finishActivity(activity);
                            if (uccCallback2 != null) {
                                uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "换绑失败"));
                            }
                        }
                    }, "", (DialogInterface.OnClickListener) null);
                } else {
                    ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), message, 0).show();
                            UccBindPresenter.this.finishActivity(activity);
                        }
                    });
                }
            }

            private void rpcResultlHit(String code) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                if (activity == null || !(activity instanceof UccWebViewActivity)) {
                    props.put("type", "native");
                } else {
                    props.put("type", ResultActionType.H5);
                }
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_ChangeBindResult", uccParams2, props);
            }
        });
    }

    public void bindAfterRecommend(Context context, String requestToken, UccParams uccParams, String bindUserToken, String needToast, Map<String, String> params, UccCallback uccCallback) {
        Map<String, String> props = new HashMap<>();
        props.put("requestToken", requestToken);
        props.put("bindUserToken", bindUserToken);
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_RecommendBind", uccParams, props);
        uccParams.requestToken = requestToken;
        uccParams.bindUserToken = bindUserToken;
        DataRepository.bindAfterRecommend(uccParams, new BindRpcRequestCallback(context, 1, 0, uccParams, needToast, params, uccCallback));
    }

    public void bindIdentify(Context context, String requestToken, UccParams uccParams, String ivToken, String bindUserToken, String needToast, Map<String, String> params, UccCallback uccCallback) {
        int bizType = 0;
        Map<String, String> props = new HashMap<>();
        props.put("bindUserToken", bindUserToken);
        if (!TextUtils.isEmpty(ivToken)) {
            props.put("bizToken", ivToken);
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_BindIdentify_IV", uccParams, props);
            bizType = 1;
        } else if (!TextUtils.isEmpty(requestToken)) {
            props.put("bizToken", requestToken);
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_BindIdentify_oauthLogin", uccParams, props);
            bizType = 2;
        }
        uccParams.requestToken = requestToken;
        uccParams.bindUserToken = bindUserToken;
        uccParams.ivToken = ivToken;
        DataRepository.bindIdentify(uccParams, new BindRpcRequestCallback(context, 2, bizType, uccParams, needToast, params, uccCallback));
    }

    private class BindRpcRequestCallback implements RpcRequestCallbackWithCode {
        private Map<String, String> bizParams;
        private int bizType;
        /* access modifiers changed from: private */
        public Context context;
        private String needToast;
        /* access modifiers changed from: private */
        public int type;
        /* access modifiers changed from: private */
        public UccCallback uccCallback;
        /* access modifiers changed from: private */
        public UccParams uccParams;

        public BindRpcRequestCallback(Context context2, int type2, int bizType2, UccParams uccParams2, String needToast2, Map<String, String> params, UccCallback uccCallback2) {
            this.context = context2;
            this.uccParams = uccParams2;
            this.uccCallback = uccCallback2;
            this.needToast = needToast2;
            this.type = type2;
            this.bizType = bizType2;
            this.bizParams = params;
        }

        public void onSuccess(RpcResponse response) {
            rpcResultlHit(response.code + "", response.actionType);
            String loginReturnData = (String) response.returnValue;
            if (TextUtils.equals("CHANGEBIND", response.actionType)) {
                UccBindPresenter.this.changeBind((Activity) this.context, this.uccParams, this.bizType, response.message, ((BindResult) JSON.parseObject(loginReturnData, BindResult.class)).changeBindToken, this.needToast, this.bizParams, this.uccCallback);
            } else if (TextUtils.equals("CONFLICTUPGRADE", response.actionType)) {
                UccBindPresenter.this.conflictupgrade(this.context, this.uccParams, 0, response.message, ((BindResult) JSON.parseObject(loginReturnData, BindResult.class)).requestToken, "0", this.bizParams, this.uccCallback);
            } else if (!TextUtils.equals(ResultActionType.H5, response.actionType) || response.returnValue == null) {
                HashMap hashMap = new HashMap();
                String needLogin = "T";
                if (TextUtils.isEmpty(loginReturnData) || loginReturnData.length() < 10) {
                    needLogin = "F";
                }
                hashMap.put(MtopJSBridge.MtopJSParam.NEED_LOGIN, needLogin);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Success", this.uccParams, hashMap);
                boolean isNeedLocalSession = true;
                if (this.bizParams != null && TextUtils.equals(this.bizParams.get(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION), "0")) {
                    isNeedLocalSession = false;
                }
                boolean cookieOnly = UccOauthLoginPresenter.isCookieOnly(this.bizParams);
                if (!TextUtils.isEmpty(loginReturnData) && isNeedLocalSession) {
                    UccBindPresenter.this.refreshWhenLogin(this.uccParams.bindSite, loginReturnData, cookieOnly);
                }
                if (this.context != null && !TextUtils.isEmpty(response.message) && TextUtils.equals(this.needToast, "1")) {
                    final RpcResponse rpcResponse = response;
                    ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                        public void run() {
                            Toast.makeText(BindRpcRequestCallback.this.context.getApplicationContext(), rpcResponse.message, 0).show();
                        }
                    });
                }
                UccBindPresenter.this.finishActivity(this.context);
                if (this.uccCallback != null) {
                    Map<String, String> map = UccBindPresenter.buildSessionInfo(this.uccParams.bindSite, loginReturnData);
                    String loginData = JSON.parseObject(loginReturnData).getString("authorizationResponse");
                    if (TextUtils.isEmpty(loginData)) {
                        map.put(UccConstants.PARAM_LOGIN_DATA, loginReturnData);
                    } else {
                        map.put(UccConstants.PARAM_LOGIN_DATA, loginData);
                    }
                    this.uccCallback.onSuccess(this.uccParams.bindSite, map);
                }
            } else {
                JSONObject json = JSON.parseObject(loginReturnData);
                if (json != null) {
                    String h5Url = json.getString("returnUrl");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", h5Url);
                    bundle.putString("token", json.getString("trustLoginToken"));
                    bundle.putString("scene", json.getString("scene"));
                    bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(this.uccParams));
                    bundle.putString("needSession", "1");
                    bundle.putString("params", Utils.convertMapToJsonStr(this.bizParams));
                    if (UccContext.getBindComponentProxy() == null) {
                        UccH5Presenter.openUrl(this.context, bundle, this.uccCallback);
                    } else if (this.uccCallback != null) {
                        this.uccCallback.onFail(this.uccParams.bindSite, 1114, "免登失败");
                    }
                    if (this.context != null && !(this.context instanceof UccWebViewActivity)) {
                        ((Activity) this.context).finish();
                        return;
                    }
                    return;
                }
                UccBindPresenter.this.finishActivity(this.context);
                if (this.uccCallback != null) {
                    this.uccCallback.onFail(this.uccParams.bindSite, 1005, Utils.buidErrorMessage(response, "免登失败"));
                }
            }
        }

        public void onSystemError(String code, final RpcResponse response) {
            final int errorCode = Utils.buidErrorCode(response, 1007);
            rpcResultlHit(errorCode + "", "");
            DialogHelper.getInstance().alert((Activity) this.context, "", response == null ? "" : response.message, this.context.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (BindRpcRequestCallback.this.type == 2) {
                        UccBindPresenter.this.finishActivity(BindRpcRequestCallback.this.context);
                        if (BindRpcRequestCallback.this.uccCallback != null) {
                            BindRpcRequestCallback.this.uccCallback.onFail(BindRpcRequestCallback.this.uccParams.bindSite, errorCode, Utils.buidErrorMessage(response, "绑定失败"));
                        }
                    }
                }
            }, "", (DialogInterface.OnClickListener) null);
        }

        public void onError(String code, final RpcResponse response) {
            final int errorCode = Utils.buidErrorCode(response, 1007);
            rpcResultlHit(errorCode + "", "");
            DialogHelper.getInstance().alert((Activity) this.context, "", response == null ? "" : response.message, this.context.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (BindRpcRequestCallback.this.type == 2) {
                        UccBindPresenter.this.finishActivity(BindRpcRequestCallback.this.context);
                        if (BindRpcRequestCallback.this.uccCallback != null) {
                            BindRpcRequestCallback.this.uccCallback.onFail(BindRpcRequestCallback.this.uccParams.bindSite, errorCode, Utils.buidErrorMessage(response, "绑定失败"));
                        }
                    }
                }
            }, "", (DialogInterface.OnClickListener) null);
        }

        private void rpcResultlHit(String code, String actionType) {
            Map<String, String> props = new HashMap<>();
            props.put("code", code);
            props.put("bindUserToken", this.uccParams.bindUserToken);
            props.put("actionType", actionType);
            if (this.type == 1) {
                props.put("bizToken", this.uccParams.requestToken);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_RecommendBindResult", this.uccParams, props);
            } else if (!TextUtils.isEmpty(this.uccParams.ivToken)) {
                props.put("bizToken", this.uccParams.ivToken);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_BindIdentifyResult_IV", this.uccParams, props);
            } else if (!TextUtils.isEmpty(this.uccParams.requestToken)) {
                props.put("bizToken", this.uccParams.requestToken);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_BindIdentifyResult_oauthLogin", this.uccParams, props);
            }
        }
    }

    public void tokenLoginAfterBind(Context context, UccParams uccParams, String trustToken, String action, String needToast, String message, Map<String, String> params, UccCallback uccCallback) {
        Map<String, String> props = new HashMap<>();
        props.put("trustToken", trustToken);
        props.put("action", action);
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_OauthLogin", uccParams, props);
        final Map<String, String> map = params;
        final UccParams uccParams2 = uccParams;
        final Context context2 = context;
        final String str = message;
        final String str2 = needToast;
        final UccCallback uccCallback2 = uccCallback;
        final String str3 = action;
        final String str4 = trustToken;
        DataRepository.tokenLoginAfterBind(trustToken, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                rpcResultlHit(response.code + "");
                String loginReturnData = (String) response.returnValue;
                boolean isNeedLocalSession = true;
                if (map != null && TextUtils.equals((CharSequence) map.get(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION), "0")) {
                    isNeedLocalSession = false;
                }
                boolean cookieOnly = UccOauthLoginPresenter.isCookieOnly(map);
                if (!TextUtils.isEmpty(loginReturnData) && isNeedLocalSession) {
                    UccBindPresenter.this.refreshWhenLogin(uccParams2.bindSite, loginReturnData, cookieOnly);
                }
                if (context2 != null && !TextUtils.isEmpty(str) && TextUtils.equals(str2, "1")) {
                    ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                        public void run() {
                            Toast.makeText(context2.getApplicationContext(), str, 0).show();
                        }
                    });
                }
                UccBindPresenter.this.finishActivity(context2);
                if (uccCallback2 != null) {
                    Map<String, String> map = UccBindPresenter.buildSessionInfo(uccParams2.bindSite, loginReturnData);
                    String loginData = JSON.parseObject(loginReturnData).getString("authorizationResponse");
                    if (TextUtils.isEmpty(loginData)) {
                        map.put(UccConstants.PARAM_LOGIN_DATA, loginReturnData);
                    } else {
                        map.put(UccConstants.PARAM_LOGIN_DATA, loginData);
                    }
                    uccCallback2.onSuccess(uccParams2.bindSite, map);
                }
            }

            public void onSystemError(String code, final RpcResponse response) {
                final int errorCode = Utils.buidErrorCode(response, 1007);
                rpcResultlHit(errorCode + "");
                DialogHelper.getInstance().alert((Activity) context2, "", response == null ? "" : response.message, context2.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UccBindPresenter.this.finishActivity(context2);
                        if (uccCallback2 != null) {
                            uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "OauthLogin接口错误"));
                        }
                    }
                }, "", (DialogInterface.OnClickListener) null);
            }

            public void onError(String code, final RpcResponse response) {
                final int errorCode = Utils.buidErrorCode(response, 1007);
                rpcResultlHit(errorCode + "");
                DialogHelper.getInstance().alert((Activity) context2, "", response == null ? "" : response.message, context2.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UccBindPresenter.this.finishActivity(context2);
                        if (uccCallback2 != null) {
                            uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "OauthLogin接口错误"));
                        }
                    }
                }, "", (DialogInterface.OnClickListener) null);
            }

            private void rpcResultlHit(String code) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                props.put("action", str3);
                props.put("trustToken", str4);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_OauthLoginResult", uccParams2, props);
            }
        });
    }

    public void getUserInfo(Context context, UccParams uccParams, String bindUserToken, String bindUserTokenType, String callFrom, Map<String, String> params, UccCallback uccCallback) {
        Map<String, String> props = new HashMap<>();
        props.put("type", callFrom);
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_GetAuthInfo", uccParams, props);
        final UccParams newUccParams = new UccParams();
        if (params == null || TextUtils.isEmpty(params.get("site"))) {
            newUccParams.site = AliMemberSDK.getMasterSite();
        } else {
            newUccParams.site = params.get("site");
        }
        if (params == null || TextUtils.isEmpty(params.get("bindSite"))) {
            newUccParams.bindSite = uccParams.bindSite;
        } else {
            newUccParams.bindSite = params.get("bindSite");
        }
        newUccParams.userToken = uccParams.userToken;
        newUccParams.bindUserToken = bindUserToken;
        newUccParams.bindUserTokenType = bindUserTokenType;
        newUccParams.createBindSiteSession = true;
        if (params != null && !TextUtils.isEmpty(params.get("scene"))) {
            newUccParams.scene = params.get("scene");
        }
        final UccCallback uccCallback2 = uccCallback;
        final UccParams uccParams2 = uccParams;
        final Map<String, String> map = params;
        final Context context2 = context;
        final String str = callFrom;
        DataRepository.getUserInfo(newUccParams, callFrom, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                rpcResultlHit(response.code + "", response.actionType);
                if (response.returnValue != null) {
                    String loginReturnData = (String) response.returnValue;
                    if (!TextUtils.equals(ResultActionType.H5, response.actionType) || response.returnValue == null) {
                        Map<String, String> props = new HashMap<>();
                        props.put(MtopJSBridge.MtopJSParam.NEED_LOGIN, newUccParams.createBindSiteSession ? "T" : "F");
                        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Success", uccParams2, props);
                        boolean isNeedLocalSession = true;
                        if (map != null && TextUtils.equals((CharSequence) map.get(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION), "0")) {
                            isNeedLocalSession = false;
                        }
                        boolean cookieOnly = UccOauthLoginPresenter.isCookieOnly(map);
                        if (!TextUtils.isEmpty(loginReturnData) && isNeedLocalSession) {
                            UccBindPresenter.this.refreshWhenLogin(uccParams2.bindSite, loginReturnData, cookieOnly);
                        }
                        UccBindPresenter.this.finishActivity(context2);
                        if (uccCallback2 != null) {
                            Map<String, String> map = UccBindPresenter.buildSessionInfo(uccParams2.bindSite, loginReturnData);
                            String loginData = JSON.parseObject(loginReturnData).getString("authorizationResponse");
                            if (TextUtils.isEmpty(loginData)) {
                                map.put(UccConstants.PARAM_LOGIN_DATA, loginReturnData);
                            } else {
                                map.put(UccConstants.PARAM_LOGIN_DATA, loginData);
                            }
                            uccCallback2.onSuccess(uccParams2.bindSite, map);
                            return;
                        }
                        return;
                    }
                    JSONObject json = JSON.parseObject(loginReturnData);
                    if (json != null) {
                        String h5Url = json.getString(ParamsConstants.Key.PARAM_H5URL);
                        Bundle bundle = new Bundle();
                        bundle.putString("url", h5Url);
                        bundle.putString("token", json.getString("token"));
                        bundle.putString("scene", json.getString("scene"));
                        bundle.putString(UccConstants.PARAM_UCC_PARAMS, JSON.toJSONString(uccParams2));
                        bundle.putString("needSession", "1");
                        bundle.putString("params", Utils.convertMapToJsonStr(map));
                        UccH5Presenter.openUrl(context2, bundle, uccCallback2);
                        if (context2 != null && !(context2 instanceof UccWebViewActivity)) {
                            ((Activity) context2).finish();
                            return;
                        }
                        return;
                    }
                    UccBindPresenter.this.finishActivity(context2);
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(uccParams2.bindSite, 1005, Utils.buidErrorMessage(response, "免登失败"));
                    }
                } else if (uccCallback2 != null) {
                    uccCallback2.onFail(uccParams2.bindSite, 1013, Utils.buidErrorMessage(response, "GetUserInfo接口错误"));
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1007);
                rpcResultlHit(errorCode + "", "");
                if (TextUtils.equals(str, "h5")) {
                    DialogHelper.getInstance().alert((Activity) context2, "", response == null ? "" : response.message, context2.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, "", (DialogInterface.OnClickListener) null);
                    return;
                }
                UccBindPresenter.this.finishActivity(context2);
                if (uccCallback2 != null) {
                    uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "OauthLogin接口错误"));
                }
            }

            public void onError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1007);
                rpcResultlHit(errorCode + "", "");
                if (TextUtils.equals(str, "h5")) {
                    DialogHelper.getInstance().alert((Activity) context2, "", response == null ? "" : response.message, context2.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, "", (DialogInterface.OnClickListener) null);
                    return;
                }
                UccBindPresenter.this.finishActivity(context2);
                if (uccCallback2 != null) {
                    uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "OauthLogin接口错误"));
                }
            }

            private void rpcResultlHit(String code, String actionType) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                props.put("type", TextUtils.isEmpty(str) ? "" : str);
                props.put("actionType", actionType);
                if (!TextUtils.isEmpty(newUccParams.bindUserToken)) {
                    props.put("bindUserToken", newUccParams.bindUserToken);
                }
                if (!TextUtils.isEmpty(newUccParams.scene)) {
                    props.put("scene", newUccParams.scene);
                }
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_GetAuthInfoResult", uccParams2, props);
            }
        });
    }

    public void skipUpgrade(Context context, UccParams uccParams, String callFrom, Map<String, String> params, UccCallback uccCallback) {
        Map<String, String> props = new HashMap<>();
        props.put("type", callFrom);
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_LoginContinue", uccParams, props);
        final UccParams newUccParams = new UccParams();
        if (params == null || TextUtils.isEmpty(params.get("site"))) {
            newUccParams.site = AliMemberSDK.getMasterSite();
        } else {
            newUccParams.site = params.get("site");
        }
        if (params == null || TextUtils.isEmpty(params.get("bindSite"))) {
            newUccParams.bindSite = uccParams.bindSite;
        } else {
            newUccParams.bindSite = params.get("bindSite");
        }
        newUccParams.userToken = uccParams.userToken;
        newUccParams.requestToken = uccParams.requestToken;
        newUccParams.createBindSiteSession = true;
        if (params != null && !TextUtils.isEmpty(params.get("scene"))) {
            newUccParams.scene = params.get("scene");
        }
        final UccCallback uccCallback2 = uccCallback;
        final UccParams uccParams2 = uccParams;
        final Map<String, String> map = params;
        final Context context2 = context;
        final String str = callFrom;
        DataRepository.skipUpgrade(uccParams, callFrom, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                rpcResultlHit(response.code + "", response.actionType);
                if (response.returnValue != null) {
                    String loginReturnData = (String) response.returnValue;
                    boolean isNeedLocalSession = true;
                    if (map != null && TextUtils.equals((CharSequence) map.get(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION), "0")) {
                        isNeedLocalSession = false;
                    }
                    boolean cookieOnly = UccOauthLoginPresenter.isCookieOnly(map);
                    if (!TextUtils.isEmpty(loginReturnData) && isNeedLocalSession) {
                        UccBindPresenter.this.refreshWhenLogin(uccParams2.bindSite, loginReturnData, cookieOnly);
                    }
                    UccBindPresenter.this.finishActivity(context2);
                    if (uccCallback2 != null) {
                        Map<String, String> map = UccBindPresenter.buildSessionInfo(uccParams2.bindSite, loginReturnData);
                        String loginData = JSON.parseObject(loginReturnData).getString("authorizationResponse");
                        if (TextUtils.isEmpty(loginData)) {
                            map.put(UccConstants.PARAM_LOGIN_DATA, loginReturnData);
                        } else {
                            map.put(UccConstants.PARAM_LOGIN_DATA, loginData);
                        }
                        uccCallback2.onSuccess(uccParams2.bindSite, map);
                    }
                } else if (uccCallback2 != null) {
                    uccCallback2.onFail(uccParams2.bindSite, 1012, Utils.buidErrorMessage(response, "skipUpgrade接口错误"));
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                onFail(code, response);
            }

            public void onError(String code, RpcResponse response) {
                onFail(code, response);
            }

            /* access modifiers changed from: package-private */
            public void onFail(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1012);
                rpcResultlHit(errorCode + "", "");
                if (TextUtils.equals(str, "h5")) {
                    DialogHelper.getInstance().alert((Activity) context2, "", response == null ? "" : response.message, context2.getString(R.string.member_sdk_iknow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, "", (DialogInterface.OnClickListener) null);
                    return;
                }
                UccBindPresenter.this.finishActivity(context2);
                if (uccCallback2 != null) {
                    uccCallback2.onFail(uccParams2.bindSite, errorCode, Utils.buidErrorMessage(response, "skipUpgrade接口错误"));
                }
            }

            private void rpcResultlHit(String code, String actionType) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                props.put("type", TextUtils.isEmpty(str) ? "" : str);
                props.put("actionType", actionType);
                props.put("requestToken", uccParams2.requestToken);
                if (!TextUtils.isEmpty(newUccParams.scene)) {
                    props.put("scene", newUccParams.scene);
                }
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_LoginContinueResult", uccParams2, props);
            }
        });
    }

    /* access modifiers changed from: private */
    public void refreshWhenLogin(String targetSite, String loginReturnData, boolean cookieOnly) {
        UccServiceProvider uccServiceProvider;
        String loginData = JSON.parseObject(loginReturnData).getString("authorizationResponse");
        if (TextUtils.equals(targetSite, "alipay")) {
            uccServiceProvider = new AlipayUccServiceProviderImpl();
        } else if (TextUtils.equals(targetSite, Site.TAOBAO)) {
            uccServiceProvider = new TaobaoUccServiceProviderImpl();
        } else if (TextUtils.equals(targetSite, Site.ELEME)) {
            uccServiceProvider = new ElemeUccServiceProviderImpl();
        } else {
            uccServiceProvider = new DefaultUccServiceProviderImpl();
        }
        if (!TextUtils.isEmpty(loginData)) {
            uccServiceProvider.refreshWhenLogin(targetSite, loginData, cookieOnly);
        } else {
            uccServiceProvider.refreshWhenLogin(targetSite, loginReturnData, cookieOnly);
        }
    }

    public static Map buildSessionInfo(String targetSite, String loginReturnData) {
        UccServiceProvider uccServiceProvider;
        String loginData = JSON.parseObject(loginReturnData).getString("authorizationResponse");
        if (TextUtils.equals(targetSite, "alipay")) {
            uccServiceProvider = new AlipayUccServiceProviderImpl();
        } else if (TextUtils.equals(targetSite, Site.TAOBAO)) {
            uccServiceProvider = new TaobaoUccServiceProviderImpl();
        } else if (TextUtils.equals(targetSite, Site.ELEME)) {
            uccServiceProvider = new ElemeUccServiceProviderImpl();
        } else {
            uccServiceProvider = new DefaultUccServiceProviderImpl();
        }
        if (!TextUtils.isEmpty(loginData)) {
            return uccServiceProvider.buildSessionInfo(targetSite, loginData);
        }
        return uccServiceProvider.buildSessionInfo(targetSite, loginReturnData);
    }

    /* access modifiers changed from: private */
    public void finishActivity(Context context) {
        if (context == null) {
            return;
        }
        if ((context instanceof UccWebViewActivity) || (context instanceof UccActivity)) {
            ((Activity) context).finish();
        }
    }
}
