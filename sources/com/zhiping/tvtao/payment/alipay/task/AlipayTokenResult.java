package com.zhiping.tvtao.payment.alipay.task;

import android.text.TextUtils;
import com.ali.user.open.tbauth.TbAuthConstants;
import org.json.JSONObject;

public class AlipayTokenResult {
    public String agreementNo;
    public String code;
    public String subCode;
    public String subMsg;
    public boolean success;
    public String token;

    public static AlipayTokenResult resolveFromJson(JSONObject jObj) {
        AlipayTokenResult result = new AlipayTokenResult();
        result.token = jObj.optString(TbAuthConstants.LOGIN_TOKEN);
        result.code = jObj.optString("code");
        result.agreementNo = jObj.optString("agreementNo");
        result.subCode = jObj.optString("subCode");
        result.subMsg = jObj.optString("subMsg");
        result.success = !TextUtils.isEmpty(result.token);
        return result;
    }
}
