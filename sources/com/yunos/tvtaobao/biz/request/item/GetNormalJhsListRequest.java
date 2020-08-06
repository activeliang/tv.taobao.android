package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.bo.JhsNormalGoodsBean;
import org.json.JSONObject;

public class GetNormalJhsListRequest extends GetJhsListRequest {
    public GetNormalJhsListRequest(int catId, int pageNum, int pageSize, boolean isSuperChoice) {
        super(catId, pageNum, pageSize, isSuperChoice);
    }

    /* access modifiers changed from: protected */
    public JhsNormalGoodsBean resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (JhsNormalGoodsBean) JSON.parseObject(obj.toString(), JhsNormalGoodsBean.class);
    }
}
