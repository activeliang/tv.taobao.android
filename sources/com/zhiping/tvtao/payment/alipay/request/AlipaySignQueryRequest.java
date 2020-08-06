package com.zhiping.tvtao.payment.alipay.request;

import android.text.TextUtils;
import com.zhiping.tvtao.payment.AlipayManager;
import com.zhiping.tvtao.payment.alipay.request.base.BaseMtopRequest;
import com.zhiping.tvtao.payment.alipay.task.AlipayTokenResult;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AlipaySignQueryRequest extends BaseMtopRequest {
    private String API_NAME = "mtop.taobao.tvtao.TvTaoAlipayPageSignQuery";
    private boolean NEED_ECODE = false;
    private boolean NEED_SESSION = false;
    private String VERSION = "1.0";
    private String extParams;
    private String uuid;

    public AlipaySignQueryRequest() {
        setNeedUmt(true);
        setTimeOut(5000);
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

    public AlipayTokenResult resolveResponse(JSONObject obj) throws Exception {
        return AlipayTokenResult.resolveFromJson(obj);
    }

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(this.uuid)) {
            params.put("uuid", this.uuid);
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
