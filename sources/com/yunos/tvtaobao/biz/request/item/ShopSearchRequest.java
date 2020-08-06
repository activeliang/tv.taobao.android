package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.ShopSearchResultBean;
import java.util.Map;
import org.json.JSONObject;

public class ShopSearchRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.wireless.item.search";
    private String version = "1.0";

    public ShopSearchRequest(String shopId, String keyword, String orderType, int pageSize, int pageNo) {
        if (!TextUtils.isEmpty(shopId)) {
            addParams("storeId", shopId);
        }
        if (!TextUtils.isEmpty(keyword)) {
            addParams("keyword", keyword);
        }
        if (!TextUtils.isEmpty(orderType)) {
            addParams("orderType", orderType);
        }
        addParams("pageSize", pageSize + "");
        addParams("pageNo", pageNo + "");
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.version;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public ShopSearchResultBean resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (ShopSearchResultBean) JSON.parseObject(obj.toString(), ShopSearchResultBean.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
