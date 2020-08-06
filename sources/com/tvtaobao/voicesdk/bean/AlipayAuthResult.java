package com.tvtaobao.voicesdk.bean;

import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;

public class AlipayAuthResult {
    @SerializedName("alipayAccountForUser")
    public String alipayAccount;
    @SerializedName("authResultForUser")
    public boolean authResult;
    @SerializedName("authStateForDevice")
    public int authState;
    @SerializedName("applyQrCodeUrl")
    public String qrCodeUrl;

    public static AlipayAuthResult resolveFromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        AlipayAuthResult result = new AlipayAuthResult();
        result.alipayAccount = jsonObject.optString("alipayAccountForUser");
        result.qrCodeUrl = jsonObject.optString("applyQrCodeUrl");
        result.authResult = jsonObject.optBoolean("authResultForUser");
        result.authState = jsonObject.optInt("authStateForDevice");
        return result;
    }
}
