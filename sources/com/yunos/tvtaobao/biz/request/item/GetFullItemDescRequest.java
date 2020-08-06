package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import java.util.Map;
import org.json.JSONObject;

public class GetFullItemDescRequest extends BaseHttpRequest {
    private static final long serialVersionUID = 9159601022400516693L;
    private String REQ_DATA = "%7B%22item_num_id%22%3A%22${itemId}%22%7D";
    private String itemId;

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public GetFullItemDescRequest(String itemId2) {
        this.itemId = itemId2;
    }

    public String getDataInfo() {
        return this.REQ_DATA.replace("${itemId}", this.itemId);
    }

    public String getUrl() {
        return getHttpDomain() + "?data=" + getDataInfo();
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return "https://hws.alicdn.com/cache/mtop.wdetail.getItemFullDesc/4.1/";
    }

    public String resolveResult(String response) throws Exception {
        JSONObject obj = new JSONObject(response.replace("\n", ""));
        if (obj != null && obj.has("data")) {
            obj = obj.getJSONObject("data");
        }
        if (obj == null || !obj.has("desc")) {
            return null;
        }
        return obj.getString("desc");
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
