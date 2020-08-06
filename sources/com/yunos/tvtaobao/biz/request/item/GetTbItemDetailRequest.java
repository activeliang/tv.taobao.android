package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TbItemDetail;
import java.util.Map;
import org.json.JSONObject;

public class GetTbItemDetailRequest extends BaseMtopRequest {
    private static final String API = "mtop.wdetail.getItemDetail";
    private static final String KEY_ITEM_NUM_ID = "itemNumId";
    private static final long serialVersionUID = 73580007519257859L;
    private Long itemNumId;

    public GetTbItemDetailRequest(Long itemNumId2) {
        this.itemNumId = itemNumId2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams(KEY_ITEM_NUM_ID, String.valueOf(this.itemNumId));
        return null;
    }

    public String getApi() {
        return API;
    }

    public String getApiVersion() {
        return "3.2";
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public TbItemDetail resolveResponse(JSONObject obj) throws Exception {
        return TbItemDetail.resolveFromMTOP(obj);
    }
}
