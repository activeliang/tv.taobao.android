package com.zhiping.tvtao.payment.alipay.task;

import org.json.JSONObject;

public class AlipayQRResult {
    public String qrCode;
    public String qrToken;

    public static AlipayQRResult resolveFromJson(JSONObject jsonObject) {
        AlipayQRResult result = new AlipayQRResult();
        result.qrCode = jsonObject.optString("qrCode");
        result.qrToken = jsonObject.optString("qrToken");
        return result;
    }
}
