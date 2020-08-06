package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.PaginationItemRates;
import java.util.Map;
import org.json.JSONObject;

public class GetItemRatesRequest extends BaseMtopRequest {
    private static final long serialVersionUID = 7872347602822464697L;

    public GetItemRatesRequest(String itemId, int pageNo, int pageSize, String rateType) {
        addParams("hasRateContent", "1");
        addParams("auctionNumId", itemId);
        addParams("pageSize", String.valueOf(pageSize));
        addParams("pageNo", String.valueOf(pageNo));
        addParams("rateType", rateType);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.wdetail.getItemRates";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "3.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public PaginationItemRates resolveResponse(JSONObject obj) throws Exception {
        return PaginationItemRates.resolveFromMTOP(obj);
    }
}
