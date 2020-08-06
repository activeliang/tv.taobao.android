package com.ali.user.open.jsbridge;

import android.app.Activity;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jsbridge.WVResult;
import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.callback.InitResultCallback;
import com.ali.user.open.core.callback.MemberCallback;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.model.RpcRequest;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.core.service.RpcService;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccDataProvider;
import com.ali.user.open.ucc.UccService;
import com.ali.user.open.ucc.model.UccParams;
import com.ali.user.open.ucc.util.UTHitConstants;
import com.ali.user.open.ucc.util.UTHitUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class UccJsBridge extends WVApiPlugin {
    private static volatile UccJsBridge instance;
    private final int TYPE_UCC_BIND = 1;
    private final int TYPE_UCC_TRUSTLOGIN = 2;
    private final int TYPE_UCC_UNBIND = 3;

    public static UccJsBridge getInstance() {
        if (instance == null) {
            synchronized (UccJsBridge.class) {
                if (instance == null) {
                    instance = new UccJsBridge();
                }
            }
        }
        return instance;
    }

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("uccBind".equals(action)) {
            uccAction(1, params, callback);
            return true;
        } else if ("uccTrustLogin".equals(action)) {
            uccAction(2, params, callback);
            return true;
        } else if ("uccUnbind".equals(action)) {
            uccAction(3, params, callback);
            return true;
        } else {
            callback.error();
            return false;
        }
    }

    private void uccAction(final int type, final String params, final WVCallBackContext callback) {
        if (!KernelContext.sdkInitialized.booleanValue()) {
            try {
                WVPluginManager.registerPlugin("aluUccJSBridge", (Class<? extends WVApiPlugin>) UccJsBridge.class);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            if (TextUtils.isEmpty(AliMemberSDK.getMasterSite())) {
                AliMemberSDK.setMasterSite(Site.TAOBAO);
            }
            AliMemberSDK.init(this.mContext.getApplicationContext(), new InitResultCallback() {
                public void onSuccess() {
                    UccJsBridge.this.setUccDataProvider();
                    if (type == 1) {
                        UccJsBridge.this.uccBind(params, callback);
                    } else if (type == 2) {
                        UccJsBridge.this.uccTrustLogin(params, callback);
                    } else if (type == 3) {
                        UccJsBridge.this.uccUnbind(params, callback);
                    }
                }

                public void onFailure(int code, String msg) {
                }
            });
        } else if (type == 1) {
            uccBind(params, callback);
        } else if (type == 2) {
            uccTrustLogin(params, callback);
        } else if (type == 3) {
            uccUnbind(params, callback);
        }
    }

    /* access modifiers changed from: private */
    public void uccTrustLogin(String paramString, final WVCallBackContext callbackContext) {
        try {
            JSONObject params = new JSONObject(paramString);
            String site = params.optString("site");
            if (TextUtils.isEmpty(site)) {
                onFailCallback(callbackContext, 1108, "site不能为空");
                return;
            }
            if (((UccService) AliMemberSDK.getService(UccService.class)).getUccDataProvider() == null && Site.isHavanaSite(AliMemberSDK.getMasterSite())) {
                setUccDataProvider();
            }
            Map<String, String> param = new HashMap<>();
            Iterator iterator = params.keys();
            while (iterator.hasNext()) {
                String key = "" + iterator.next();
                if (!TextUtils.equals("site", key)) {
                    param.put(key, params.optString(key));
                }
            }
            ((UccService) AliMemberSDK.getService(UccService.class)).trustLogin((Activity) this.mContext, site, param, new UccCallback() {
                public void onSuccess(String s, Map map) {
                    if (callbackContext != null) {
                        callbackContext.success();
                    }
                }

                public void onFail(String site, int code, String message) {
                    UccJsBridge uccJsBridge = UccJsBridge.this;
                    WVCallBackContext wVCallBackContext = callbackContext;
                    if (TextUtils.isEmpty(message)) {
                        message = "免登失败";
                    }
                    uccJsBridge.onFailCallback(wVCallBackContext, code, message);
                }
            });
        } catch (Exception e) {
            Map<String, String> props = new HashMap<>();
            props.put("message", e.getMessage());
            UTHitUtils.send(UTHitConstants.PageUccLogin, "UccLogin_EXCEPTION", (UccParams) null, props);
            onFailCallback(callbackContext, 1199, "系统异常:" + e.getMessage());
        }
    }

    public void uccBind(String paramString, WVCallBackContext callbackContext) {
        uccBind((Activity) this.mContext, paramString, callbackContext);
    }

    public void uccBind(Activity activity, String paramString, final WVCallBackContext callbackContext) {
        try {
            JSONObject params = new JSONObject(paramString);
            String site = params.optString("site");
            if (TextUtils.isEmpty(site)) {
                onFailCallback(callbackContext, 1108, "site不能为空");
                return;
            }
            if (((UccService) AliMemberSDK.getService(UccService.class)).getUccDataProvider() == null && Site.isHavanaSite(AliMemberSDK.getMasterSite())) {
                setUccDataProvider();
            }
            Map<String, String> param = new HashMap<>();
            Iterator iterator = params.keys();
            while (iterator.hasNext()) {
                String key = "" + iterator.next();
                param.put(key, params.optString(key));
            }
            ((UccService) AliMemberSDK.getService(UccService.class)).bind(activity, site, param, (UccCallback) new UccCallback() {
                public void onSuccess(String s, Map map) {
                    if (callbackContext != null) {
                        callbackContext.success();
                    }
                }

                public void onFail(String site, int code, String message) {
                    UccJsBridge uccJsBridge = UccJsBridge.this;
                    WVCallBackContext wVCallBackContext = callbackContext;
                    if (TextUtils.isEmpty(message)) {
                        message = "绑定失败";
                    }
                    uccJsBridge.onFailCallback(wVCallBackContext, code, message);
                }
            });
        } catch (Exception e) {
            Map<String, String> props = new HashMap<>();
            props.put("message", e.getMessage());
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccLogin_EXCEPTION", (UccParams) null, props);
            onFailCallback(callbackContext, 1199, "系统异常:" + e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void uccUnbind(String paramString, final WVCallBackContext callbackContext) {
        try {
            String site = new JSONObject(paramString).optString("site");
            if (TextUtils.isEmpty(site)) {
                onFailCallback(callbackContext, 1108, "site不能为空");
                return;
            }
            if (((UccService) AliMemberSDK.getService(UccService.class)).getUccDataProvider() == null && Site.isHavanaSite(AliMemberSDK.getMasterSite())) {
                setUccDataProvider();
            }
            ((UccService) AliMemberSDK.getService(UccService.class)).unbind(site, new UccCallback() {
                public void onSuccess(String s, Map map) {
                    if (callbackContext != null) {
                        callbackContext.success();
                    }
                }

                public void onFail(String site, int code, String message) {
                    UccJsBridge uccJsBridge = UccJsBridge.this;
                    WVCallBackContext wVCallBackContext = callbackContext;
                    if (TextUtils.isEmpty(message)) {
                        message = "解绑失败";
                    }
                    uccJsBridge.onFailCallback(wVCallBackContext, code, message);
                }
            });
        } catch (Exception e) {
            Map<String, String> props = new HashMap<>();
            props.put("message", e.getMessage());
            UTHitUtils.send(UTHitConstants.PageUccUnBind, "UccLogin_EXCEPTION", (UccParams) null, props);
            onFailCallback(callbackContext, 1199, "系统异常:" + e.getMessage());
        }
    }

    public void setUccDataProvider() {
        final RpcRequest request = new RpcRequest();
        request.target = "mtop.alibaba.ucc.taobao.apply.usertoken";
        request.version = "1.0";
        request.NEED_ECODE = true;
        request.NEED_SESSION = true;
        ((UccService) AliMemberSDK.getService(UccService.class)).setUccDataProvider(new UccDataProvider() {
            public void getUserToken(String s, final MemberCallback<String> memberCallback) {
                ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(request, UserTokenModel.class, new RpcRequestCallbackWithCode() {
                    public void onSuccess(RpcResponse rpcResponse) {
                        UserTokenModel userTokenModel = (UserTokenModel) rpcResponse.returnValue;
                        if (rpcResponse != null && rpcResponse.returnValue != null) {
                            String userToken = userTokenModel.userToken;
                            if (memberCallback != null) {
                                memberCallback.onSuccess(userToken);
                            }
                        } else if (memberCallback != null) {
                            memberCallback.onFailure(ResultCode.UCC_ERROR_USER_TOKEN_IS_NULL, rpcResponse == null ? "" : rpcResponse.message);
                        }
                    }

                    public void onSystemError(String s, RpcResponse rpcResponse) {
                        if (memberCallback != null) {
                            memberCallback.onFailure(ResultCode.UCC_ERROR_USER_TOKEN_IS_NULL, rpcResponse == null ? "" : rpcResponse.message);
                        }
                    }

                    public void onError(String s, RpcResponse rpcResponse) {
                        if (memberCallback != null) {
                            memberCallback.onFailure(ResultCode.UCC_ERROR_USER_TOKEN_IS_NULL, rpcResponse == null ? "" : rpcResponse.message);
                        }
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void onFailCallback(WVCallBackContext callbackContext, int code, String message) {
        if (callbackContext != null) {
            WVResult result = new WVResult();
            result.setResult("HY_FAILED");
            result.addData("code", String.valueOf(code));
            result.addData("message", message);
            callbackContext.error(result);
        }
    }
}
