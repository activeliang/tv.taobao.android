package com.ali.auth.third.offline.login.task;

import android.text.TextUtils;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ali.auth.third.core.task.AbsAsyncTask;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.core.util.ResourceUtils;
import com.ali.auth.third.offline.LoginWebViewActivity;
import com.ali.auth.third.offline.context.BridgeCallbackContext;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.UTConstants;
import com.ali.auth.third.offline.login.context.LoginContext;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginByUsernameTask extends AbsAsyncTask<String, Void, Void> {
    private static final String TAG = "login";
    private BridgeCallbackContext bridgeCallbackContext;

    public LoginByUsernameTask(BridgeCallbackContext bridgeCallbackContext2) {
        this.bridgeCallbackContext = bridgeCallbackContext2;
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        if (!CommonUtils.isNetworkAvailable()) {
            RpcResponse<String> result = new RpcResponse<>();
            result.code = -1;
            result.message = ResourceUtils.getString("com_taobao_tae_sdk_network_not_available_message");
            JSONObject jsonResult = new JSONObject();
            try {
                jsonResult.put("code", result.code);
                jsonResult.put("message", result.message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.bridgeCallbackContext.success(jsonResult.toString());
        } else {
            RpcResponse<LoginReturnData> resultData = LoginComponent.INSTANCE.loginByUserName(params[0]);
            if (resultData == null) {
                this.bridgeCallbackContext.onFailure("");
            } else {
                try {
                    if (resultData.code == 3000) {
                        LoginContext.credentialService.refreshWhenLogin((LoginReturnData) resultData.returnValue);
                        this.bridgeCallbackContext.getActivity().setResult(ResultCode.SUCCESS.code);
                        this.bridgeCallbackContext.getActivity().finish();
                    } else if (resultData.code == 13010) {
                        JSONObject js = new JSONObject();
                        js.put("code", resultData.code);
                        js.put("message", resultData.message);
                        JSONObject data = new JSONObject();
                        if (resultData.returnValue != null) {
                            data.put("checkCodeId", ((LoginReturnData) resultData.returnValue).checkCodeId);
                            data.put("checkCodeUrl", ((LoginReturnData) resultData.returnValue).checkCodeUrl);
                        }
                        js.put("data", data);
                        this.bridgeCallbackContext.success(js.toString());
                    } else if (resultData.code == 13011) {
                        JSONObject js2 = new JSONObject();
                        js2.put("code", resultData.code);
                        js2.put("message", resultData.message);
                        JSONObject data2 = new JSONObject();
                        if (resultData.returnValue != null) {
                            data2.put("checkCodeId", ((LoginReturnData) resultData.returnValue).checkCodeId);
                            data2.put("checkCodeUrl", ((LoginReturnData) resultData.returnValue).checkCodeUrl);
                        }
                        js2.put("data", data2);
                        this.bridgeCallbackContext.success(js2.toString());
                    } else if (resultData.code == 13060) {
                        JSONObject js3 = new JSONObject();
                        js3.put("code", resultData.code);
                        js3.put("message", resultData.message);
                        JSONObject data3 = new JSONObject();
                        if (resultData.returnValue != null) {
                            data3.put("doubleCheckUrl", ((LoginReturnData) resultData.returnValue).h5Url);
                        }
                        js3.put("data", data3);
                        if (resultData.returnValue != null) {
                            LoginWebViewActivity loginWebViewActivity = (LoginWebViewActivity) this.bridgeCallbackContext.getActivity();
                            LoginWebViewActivity.token = ((LoginReturnData) resultData.returnValue).token;
                            LoginWebViewActivity loginWebViewActivity2 = (LoginWebViewActivity) this.bridgeCallbackContext.getActivity();
                            LoginWebViewActivity.scene = ((LoginReturnData) resultData.returnValue).scene;
                        }
                        this.bridgeCallbackContext.success(js3.toString());
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", resultData.code);
                        jsonObject.put("message", resultData.message);
                        this.bridgeCallbackContext.success(jsonObject.toString());
                        Map<String, String> json = new HashMap<>();
                        json.put("code", resultData.code + "");
                        if (!TextUtils.isEmpty(resultData.message)) {
                            json.put("message", resultData.message);
                        }
                        ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_H5_LOGIN_FAILURE, json);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        Message errorMessage = MessageUtils.createMessage(10010, t.getMessage());
        SDKLogger.d("login", errorMessage.toString());
        this.bridgeCallbackContext.onFailure(errorMessage.code, errorMessage.message);
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
    }
}
