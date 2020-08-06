package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.MyAlipayHongbaoList;
import java.util.Map;
import org.json.JSONObject;

public class GetMyAlipayHongbaoList extends BaseMtopRequest {
    private static final long serialVersionUID = 3542412495554240732L;
    private String api = "mtop.wallet.alipay.tradepacket.queryReceiveList";
    private String v = "1.0";

    public GetMyAlipayHongbaoList(String currentPage) {
        addParams("currentPage", currentPage);
    }

    /* access modifiers changed from: protected */
    public MyAlipayHongbaoList resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (MyAlipayHongbaoList) JSON.parseObject(obj.toString(), MyAlipayHongbaoList.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.api;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.v;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
