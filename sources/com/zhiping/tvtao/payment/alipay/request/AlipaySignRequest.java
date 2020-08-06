package com.zhiping.tvtao.payment.alipay.request;

import android.text.TextUtils;
import com.zhiping.tvtao.payment.AlipayManager;
import com.zhiping.tvtao.payment.alipay.request.base.BaseMtopRequest;
import com.zhiping.tvtao.payment.alipay.task.AlipayQRResult;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AlipaySignRequest extends BaseMtopRequest {
    private String API_NAME = "mtop.taobao.tvtao.TvTaoAlipayPageSign";
    private boolean NEED_ECODE = true;
    private boolean NEED_SESSION = false;
    private String VERSION = "1.0";
    private String alipayUserId;
    private String extParams;
    private String signvalidityPeriod = "6m";
    private String uuid;

    public AlipaySignRequest(String alipayUserId2) {
        this.alipayUserId = alipayUserId2;
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

    public AlipayQRResult resolveResponse(JSONObject obj) throws Exception {
        return AlipayQRResult.resolveFromJson(obj);
    }

    public String getApi() {
        return this.API_NAME;
    }

    public String getApiVersion() {
        return this.VERSION;
    }

    public Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(this.alipayUserId)) {
            params.put("signUserId", this.alipayUserId);
        }
        if (!TextUtils.isEmpty(this.uuid)) {
            params.put("uuid", this.uuid);
        }
        if (!TextUtils.isEmpty(this.extParams)) {
            params.put("extParams", this.extParams);
        }
        params.put("signvalidityPeriod", this.signvalidityPeriod);
        return params;
    }
}
