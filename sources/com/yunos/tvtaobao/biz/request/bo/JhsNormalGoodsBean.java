package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class JhsNormalGoodsBean {
    private String goodCount;
    private String goodPage;
    private List<JhsGoodsBean> goods;

    public String getGoodCount() {
        return this.goodCount;
    }

    public void setGoodCount(String goodCount2) {
        this.goodCount = goodCount2;
    }

    public String getGoodPage() {
        return this.goodPage;
    }

    public void setGoodPage(String goodPage2) {
        this.goodPage = goodPage2;
    }

    public List<JhsGoodsBean> getGoods() {
        return this.goods;
    }

    public void setGoods(List<JhsGoodsBean> goods2) {
        this.goods = goods2;
    }
}
