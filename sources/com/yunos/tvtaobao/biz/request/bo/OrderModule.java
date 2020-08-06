package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class OrderModule {
    private List<DeliveryListBean> deliveryList;

    public void setDeliveryList(List<DeliveryListBean> deliveryList2) {
        this.deliveryList = deliveryList2;
    }

    public List<DeliveryListBean> getDeliveryList() {
        return this.deliveryList;
    }
}
