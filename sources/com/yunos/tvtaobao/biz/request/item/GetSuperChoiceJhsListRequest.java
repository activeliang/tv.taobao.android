package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.bo.JhsSuperChoiceGoodsBean;
import org.json.JSONObject;

public class GetSuperChoiceJhsListRequest extends GetJhsListRequest {
    public GetSuperChoiceJhsListRequest(int catId, int pageNum, int pageSize, boolean isSuperChoice) {
        super(catId, pageNum, pageSize, isSuperChoice);
    }

    /* access modifiers changed from: protected */
    public JhsSuperChoiceGoodsBean resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (JhsSuperChoiceGoodsBean) JSON.parseObject(obj.toString(), JhsSuperChoiceGoodsBean.class);
    }
}
