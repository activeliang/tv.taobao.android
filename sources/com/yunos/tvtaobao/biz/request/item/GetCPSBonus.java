package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.BonusBean;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetCPSBonus extends BaseMtopRequest {
    private final String API = "mtop.aladdin.vegas.lottery.draw";
    private final String version = "1.0";

    public GetCPSBonus(String refpid, String e, String wua, String asac) {
        addParams("refpid", refpid);
        addParams("e", e);
        addParams("wua", wua);
        addParams("asac", asac);
    }

    /* access modifiers changed from: protected */
    public BonusBean resolveResponse(JSONObject obj) throws Exception {
        if (TextUtils.isEmpty(obj.toString())) {
            return null;
        }
        ZpLogger.e("GetCPSBonus", "obj : " + obj);
        return (BonusBean) JSON.parseObject(obj.toString(), BonusBean.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.aladdin.vegas.lottery.draw";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
