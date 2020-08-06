package com.tvtaobao.voicesdk.request;

import android.text.TextUtils;
import com.tvtaobao.voicesdk.type.PayStatus;
import com.tvtaobao.voicesdk.utils.JSONUtil;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class QueryOrderRequest extends BaseMtopRequest {
    private final String API = "mtop.taobao.tvtao.speech.order.queryByOutOrderId";
    private final String TAG = "QueryOrderRequest";
    private final String VERSION = "1.0";

    public QueryOrderRequest(String outOrderId) {
        addParams("outOrderId", outOrderId);
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        LogPrint.e("TVTao_QueryOrder", "obj : " + obj);
        if (!TextUtils.isEmpty(obj.toString())) {
            if (obj.has("errorMessage")) {
                String errorMessage = obj.getString("errorMessage");
                if (!TextUtils.isEmpty(errorMessage)) {
                    return errorMessage;
                }
            }
            String payStatus = JSONUtil.getString(JSONUtil.getJSON(obj, "bizOrder"), "payStatus");
            LogPrint.e("TVTao_QueryOrder", "payStatus : " + payStatus);
            if (TextUtils.isEmpty(payStatus)) {
                return "query";
            }
            if (payStatus.equals(String.valueOf(PayStatus.STATUS_NOT_PAY.getPayStatus())) || payStatus.equals(String.valueOf(PayStatus.STATUS_PAID.getPayStatus()))) {
                return payStatus;
            }
            if (payStatus.equals(String.valueOf(PayStatus.STATUS_REFUNDED.getPayStatus()))) {
                return PayStatus.STATUS_REFUNDED.getPayInfo();
            }
            if (payStatus.equals(String.valueOf(PayStatus.STATUS_TRANSFERED.getPayStatus()))) {
                return PayStatus.STATUS_TRANSFERED.getPayInfo();
            }
            if (payStatus.equals(String.valueOf(PayStatus.STATUS_NO_OUT_ORDER.getPayStatus()))) {
                return PayStatus.STATUS_NO_OUT_ORDER.getPayInfo();
            }
            if (payStatus.equals(String.valueOf(PayStatus.STATUS_CLOSED_BY_TAOBAO.getPayStatus()))) {
                return PayStatus.STATUS_CLOSED_BY_TAOBAO.getPayInfo();
            }
            if (payStatus.equals(String.valueOf(PayStatus.STATUS_NOT_REDY.getPayStatus()))) {
                return PayStatus.STATUS_NOT_REDY.getPayInfo();
            }
            if (payStatus.equals(String.valueOf(PayStatus.PAY_STATUS_APPEND_PAY.getPayStatus()))) {
                return PayStatus.PAY_STATUS_APPEND_PAY.getPayInfo();
            }
            if (payStatus.equals(String.valueOf(PayStatus.PAY_STATUS_WAIT_ACCOUNT.getPayStatus()))) {
                return PayStatus.PAY_STATUS_WAIT_ACCOUNT.getPayInfo();
            }
        }
        return "query";
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.speech.order.queryByOutOrderId";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
