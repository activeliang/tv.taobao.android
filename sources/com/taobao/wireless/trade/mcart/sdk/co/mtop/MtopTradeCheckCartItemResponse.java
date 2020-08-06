package com.taobao.wireless.trade.mcart.sdk.co.mtop;

import mtopsdk.mtop.domain.BaseOutDo;

public class MtopTradeCheckCartItemResponse extends BaseOutDo {
    private String data;

    public void setData(String data2) {
        this.data = data2;
    }

    public String getData() {
        return this.data;
    }
}
