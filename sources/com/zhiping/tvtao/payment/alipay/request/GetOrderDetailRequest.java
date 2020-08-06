package com.zhiping.tvtao.payment.alipay.request;

import android.text.TextUtils;
import com.zhiping.tvtao.payment.AlipayManager;
import com.zhiping.tvtao.payment.alipay.request.base.BaseMtopRequest;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class GetOrderDetailRequest extends BaseMtopRequest {
    private String API_NAME = "mtop.order.queryOrderDetail";
    private boolean NEED_ECODE = false;
    private boolean NEED_SESSION = true;
    private String VERSION = "1.0.alipay";
    private String extParams;
    private String orderId;

    public GetOrderDetailRequest(String orderId2) {
        setNeedEcode(this.NEED_ECODE);
        setNeedLogin(this.NEED_SESSION);
        this.orderId = orderId2;
        if (AlipayManager.getBizInfoProvider() != null) {
            String uuid = AlipayManager.getBizInfoProvider().getDeviceId();
            try {
                JSONObject jo = new JSONObject();
                jo.put("uuid", uuid);
                this.extParams = jo.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getResponseStatus(JSONObject dataObject) {
        JSONObject orderInfo;
        if (dataObject == null || (orderInfo = dataObject.optJSONObject("orderInfo")) == null) {
            return null;
        }
        return orderInfo.optString("orderStatusCode");
    }

    public Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(this.orderId)) {
            params.put("orderId", this.orderId);
        }
        if (!TextUtils.isEmpty(this.extParams)) {
            params.put("extParams", this.extParams);
        }
        return params;
    }

    public String getApi() {
        return this.API_NAME;
    }

    public String getApiVersion() {
        return this.VERSION;
    }
}
