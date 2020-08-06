package com.zhiping.tvtao.payment.alipay.request;

import android.text.TextUtils;
import com.zhiping.tvtao.payment.AlipayManager;
import com.zhiping.tvtao.payment.alipay.request.base.BaseMtopRequest;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AgreementPayRequest extends BaseMtopRequest {
    private String API_NAME = "mtop.taobao.tvtao.TvTaoAlipayTpAgreementPay";
    private boolean NEED_ECODE = false;
    private boolean NEED_SESSION = true;
    private String VERSION = "1.0";
    private String bizOrderId;
    private String extParams;
    private String uuid;

    public AgreementPayRequest(String bizOrderId2) {
        this.bizOrderId = bizOrderId2;
        setNeedUmt(true);
        setNeedEcode(this.NEED_ECODE);
        setNeedLogin(this.NEED_SESSION);
        if (AlipayManager.getBizInfoProvider() != null) {
            this.uuid = AlipayManager.getBizInfoProvider().getDeviceId();
            try {
                JSONObject jo = new JSONObject();
                jo.put("uuid", this.uuid);
                this.extParams = jo.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getApi() {
        return this.API_NAME;
    }

    public String getApiVersion() {
        return this.VERSION;
    }

    public Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("bizOrderId", this.bizOrderId);
        if (!TextUtils.isEmpty(this.uuid)) {
            params.put("uuid", this.uuid);
        }
        if (!TextUtils.isEmpty(this.extParams)) {
            params.put("extParams", this.extParams);
        }
        return params;
    }
}
