package com.ali.user.open.oauth.alipay3;

import android.os.Build;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.device.DeviceInfo;
import com.ali.user.open.core.model.RpcRequest;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.core.service.MemberExecutorService;
import com.ali.user.open.core.service.RpcService;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.util.CommonUtils;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.ali.user.open.ucc.data.ApiConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class AlipayRpcPresenter {
    public static void getAlipaySign(final GetSignCallback mGetSignCallback, SignRequest mSignRequest) {
        getAlipaySign(mSignRequest, (RpcRequestCallbackWithCode) new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse rpcResponse) {
                if (rpcResponse == null || rpcResponse.returnValue == null) {
                    CommonUtils.onFailure(mGetSignCallback, 203, "");
                    return;
                }
                final String requestStr = ((SignResult) rpcResponse.returnValue).queryUrlArgs;
                if (mGetSignCallback != null) {
                    ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                        public void run() {
                            mGetSignCallback.onGetSignSuccessed(requestStr);
                        }
                    });
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                CommonUtils.onFailure(mGetSignCallback, response.code, response.message);
            }

            public void onError(String code, RpcResponse response) {
                CommonUtils.onFailure(mGetSignCallback, response.code, response.message);
            }
        });
    }

    public static void getAlipaySign(SignRequest mSignRequest, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "mtop.taobao.login.signForAlipaySNSLogin";
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appName", ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
            jsonObject.put("appVersion", "android_1.0.2");
            jsonObject.put("ttid", "android_1.0.2");
            jsonObject.put("utdid", DeviceInfo.deviceId);
            jsonObject.put("sdkVersion", KernelContext.SDK_VERSION_STD);
            jsonObject.put("deviceId", DeviceInfo.deviceId);
            jsonObject.put("site", "0");
            JSONObject extJsonObject = new JSONObject();
            extJsonObject.put("target_id", mSignRequest.target_id);
            extJsonObject.put("pid", mSignRequest.pid);
            extJsonObject.put("app_id", mSignRequest.app_id);
            extJsonObject.put("sign_type", mSignRequest.sign_type);
            extJsonObject.put("apiVersion", "2.0");
            try {
                extJsonObject.put(ApiConstants.ApiField.DEVICE_NAME, Build.MODEL);
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonObject.put("ext", extJsonObject);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        rpcRequest.addParam(TbAuthConstants.PARAN_LOGIN_INFO, jsonObject.toString());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, SignResult.class, callback);
    }
}
