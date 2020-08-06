package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.List;

public class DoPayOrders implements Serializable {
    private static final long serialVersionUID = -5551651438805741048L;
    private boolean canPay;
    private List<String> orderOutIds;
    private String price;
    private String reason;

    public List<String> getOrderOutIds() {
        return this.orderOutIds;
    }

    public void setOrderOutIds(List<String> orderOutIds2) {
        this.orderOutIds = orderOutIds2;
    }

    public boolean isCanPay() {
        return this.canPay;
    }

    public void setCanPay(boolean canPay2) {
        this.canPay = canPay2;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason2) {
        this.reason = reason2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }
}
