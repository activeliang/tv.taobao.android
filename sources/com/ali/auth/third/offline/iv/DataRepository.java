package com.ali.auth.third.offline.iv;

import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.RpcRequest;
import com.ali.auth.third.core.model.RpcRequestCallbackWithCode;
import com.ali.auth.third.core.service.RpcService;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.user.open.ucc.data.ApiConstants;
import org.json.JSONObject;

public class DataRepository {
    public static void fetchIVStrategys(String trustToken, String validatorTags, RpcRequestCallbackWithCode callback) {
        RpcRequest request = new RpcRequest();
        request.target = "mtop.alibaba.havanaappiv.getStrategyForNative";
        request.version = "1.0";
        request.addParam("trustToken", trustToken);
        request.addParam("validatorTags", validatorTags);
        try {
            JSONObject ext = new JSONObject();
            ext.put("appKey", KernelContext.getAppKey());
            ext.put("sdkVersion", KernelContext.sdkVersion);
            ext.put("appVersion", CommonUtils.getAppVersion());
            ext.put("site", "0");
            request.addParam("ext", ext);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        ((RpcService) KernelContext.getService(RpcService.class)).remoteBusiness(request, String.class, callback);
    }

    public static void sendSMSCode(String trustToken, String validatorTags, String mobile, RpcRequestCallbackWithCode callback) {
        RpcRequest request = new RpcRequest();
        request.target = "mtop.alibaba.havanaappiv.sendCommonCode";
        request.version = "1.0";
        request.addParam(ApiConstants.ApiField.BIND_IV_TOKEN, trustToken);
        request.addParam("tag", validatorTags);
        request.addParam("mobile", mobile);
        ((RpcService) KernelContext.getService(RpcService.class)).remoteBusiness(request, String.class, callback);
    }

    public static void checkCommonCode(String trustToken, String codes, String mobile, RpcRequestCallbackWithCode callback) {
        RpcRequest request = new RpcRequest();
        request.target = "mtop.alibaba.havanaappiv.checkCommonCode";
        request.version = "1.0";
        request.addParam(ApiConstants.ApiField.BIND_IV_TOKEN, trustToken);
        request.addParam("codes", codes);
        request.addParam("mobile", mobile);
        ((RpcService) KernelContext.getService(RpcService.class)).remoteBusiness(request, String.class, callback);
    }
}
