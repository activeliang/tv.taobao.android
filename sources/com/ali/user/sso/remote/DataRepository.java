package com.ali.user.sso.remote;

import com.ali.auth.third.core.model.RpcRequestCallbackWithCode;
import org.json.JSONObject;

public class DataRepository {
    public static void invalidSsoToken(String ssoToken, ISsoRemoteRequestParam param, RpcRequestCallbackWithCode callback) {
    }

    private static void buildBaseParam(JSONObject jsonObject, ISsoRemoteRequestParam param) {
        try {
            jsonObject.put("apdid", param.getApdid());
            jsonObject.put("umidToken", param.getUmidToken());
            jsonObject.put("deviceId", "");
            jsonObject.put("ttid", "");
        } catch (Throwable th) {
        }
    }
}
