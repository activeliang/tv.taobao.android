package com.yunos.tvtaobao.biz.request.bo;

import java.util.Map;

public class GetBuyToCashbackResult {
    private Map<String, String> mapIds;

    public Map<String, String> getGoodIds() {
        return this.mapIds;
    }

    public void setGoodIds(Map<String, String> goodIds) {
        this.mapIds = goodIds;
    }
}
