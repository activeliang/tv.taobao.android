package com.ali.auth.third.offline.login.task;

import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.task.AbsAsyncTask;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.offline.QRView;
import com.ali.auth.third.offline.context.BridgeCallbackContext;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.login.util.LoginStatus;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginByQrCodeTask extends AbsAsyncTask<String, Void, Void> {
    private static final String TAG = "login";
    /* access modifiers changed from: private */
    public BridgeCallbackContext bridgeCallbackContext;
    private boolean supportNativeIvOnly;

    public LoginByQrCodeTask(BridgeCallbackContext bridgeCallbackContext2, boolean supportNativeIvOnly2) {
        this.bridgeCallbackContext = bridgeCallbackContext2;
        this.supportNativeIvOnly = supportNativeIvOnly2;
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        JSONObject qrCodeInfoJson;
        try {
            JSONObject paramsJson = new JSONObject(params[0]).optJSONObject("params");
            if (!(paramsJson == null || (qrCodeInfoJson = paramsJson.optJSONObject("qrCodeInfo")) == null)) {
                RpcResponse<LoginReturnData> resultData = LoginComponent.INSTANCE.loginByQRCode(qrCodeInfoJson.optString("at"), qrCodeInfoJson.optLong("t"), this.supportNativeIvOnly);
                if (resultData == null) {
                    JSONObject js = new JSONObject();
                    js.put("code", 10010);
                    js.put("message", "");
                    this.bridgeCallbackContext.success(js.toString());
                    LoginStatus.resetLoginFlag();
                    return null;
                }
                try {
                    int code = resultData.code;
                    SDKLogger.d("login", "get qrcode of " + qrCodeInfoJson + " status " + code);
                    if (code == 3000) {
                        LoginContext.credentialService.refreshWhenLogin((LoginReturnData) resultData.returnValue);
                        KernelContext.executorService.postUITask(new Runnable() {
                            public void run() {
                                if (CallbackContext.loginCallback != null) {
                                    ((LoginCallback) CallbackContext.loginCallback).onSuccess(LoginContext.credentialService.getSession());
                                }
                                if (LoginByQrCodeTask.this.bridgeCallbackContext.getActivity() != null) {
                                    LoginByQrCodeTask.this.bridgeCallbackContext.getActivity().setResult(ResultCode.SUCCESS.code);
                                    LoginByQrCodeTask.this.bridgeCallbackContext.getActivity().finish();
                                    LoginStatus.resetLoginFlag();
                                } else if (QRView.mLoginCallback != null) {
                                    QRView.mLoginCallback.onSuccess(LoginContext.credentialService.getSession());
                                    QRView.mLoginCallback = null;
                                }
                            }
                        });
                    } else if (code == 14030) {
                        JSONObject js2 = new JSONObject();
                        js2.put("code", 4);
                        js2.put("message", resultData.message);
                        this.bridgeCallbackContext.success(js2.toString());
                    } else if (code == 14031) {
                        JSONObject js3 = new JSONObject();
                        js3.put("code", 5);
                        js3.put("message", resultData.message);
                        this.bridgeCallbackContext.success(js3.toString());
                    } else if (code == 14042) {
                        JSONObject js4 = new JSONObject();
                        js4.put("code", 6);
                        js4.put("message", resultData.message);
                        this.bridgeCallbackContext.success(js4.toString());
                    } else if (code == 13060) {
                        JSONObject js5 = new JSONObject();
                        if (((LoginReturnData) resultData.returnValue).extMap != null) {
                            js5.put("nativeIv", ((LoginReturnData) resultData.returnValue).extMap.get("nativeIv"));
                            js5.put(Constants.PARAM_IV_TOKEN, ((LoginReturnData) resultData.returnValue).extMap.get("nativeIvToken"));
                            js5.put("scene", ((LoginReturnData) resultData.returnValue).scene);
                            js5.put("login_token", ((LoginReturnData) resultData.returnValue).token);
                        }
                        this.bridgeCallbackContext.onFailure(13060, js5.toString());
                    } else if (code == 1) {
                        JSONObject js6 = new JSONObject();
                        js6.put("code", 10010);
                        js6.put("message", resultData.message);
                        this.bridgeCallbackContext.success(js6.toString());
                    } else if (code == 13082) {
                        JSONObject js7 = new JSONObject();
                        js7.put("code", 6);
                        js7.put("message", resultData.message);
                        this.bridgeCallbackContext.success(js7.toString());
                    } else {
                        JSONObject js8 = new JSONObject();
                        js8.put("code", code);
                        js8.put("message", resultData.message);
                        this.bridgeCallbackContext.success(js8.toString());
                    }
                } catch (Throwable th) {
                }
            }
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        Message errorMessage = MessageUtils.createMessage(10010, t.getMessage());
        SDKLogger.log("login", errorMessage, t);
        this.bridgeCallbackContext.onFailure(errorMessage.code, errorMessage.message);
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
    }
}
