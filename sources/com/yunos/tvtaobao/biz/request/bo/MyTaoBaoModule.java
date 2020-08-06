package com.yunos.tvtaobao.biz.request.bo;

public class MyTaoBaoModule {
    private FavModule favModule;
    private OrderModule orderModule;

    public FavModule getFavModule() {
        return this.favModule;
    }

    public void setFavModule(FavModule favModule2) {
        this.favModule = favModule2;
    }

    public OrderModule getOrderModule() {
        return this.orderModule;
    }

    public void setOrderModule(OrderModule orderModule2) {
        this.orderModule = orderModule2;
    }
}
