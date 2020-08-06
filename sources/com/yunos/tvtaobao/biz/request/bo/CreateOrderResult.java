package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONObject;

public class CreateOrderResult implements Serializable {
    private static final long serialVersionUID = 4393098295706332235L;
    private String alipayOrderId;
    private String bizOrderId;
    private String buyerNumId;
    private String nextUrl;
    private String orderKey;
    private String secrityPay;
    private boolean simplePay;
    private long time;

    public static CreateOrderResult fromMTOP(JSONObject json) throws Exception {
        CreateOrderResult result = new CreateOrderResult();
        result.setBizOrderId(json.optString("bizOrderId"));
        result.setAlipayOrderId(json.optString("alipayOrderId"));
        result.setBuyerNumId(json.optString("buyerNumId"));
        result.setNextUrl(json.optString("nextUrl"));
        result.setSecrityPay(json.optString("secrityPay"));
        result.setTime(json.optLong("time"));
        result.setOrderKey(json.optString("orderKey"));
        result.setSimplePay(json.optBoolean("simplePay"));
        return result;
    }

    public boolean isSimplePay() {
        return this.simplePay;
    }

    public void setSimplePay(boolean simplePay2) {
        this.simplePay = simplePay2;
    }

    public String getBizOrderId() {
        return this.bizOrderId;
    }

    public void setBizOrderId(String bizOrderId2) {
        this.bizOrderId = bizOrderId2;
    }

    public String getAlipayOrderId() {
        return this.alipayOrderId;
    }

    public void setAlipayOrderId(String alipayOrderId2) {
        this.alipayOrderId = alipayOrderId2;
    }

    public String getBuyerNumId() {
        return this.buyerNumId;
    }

    public void setBuyerNumId(String buyerNumId2) {
        this.buyerNumId = buyerNumId2;
    }

    public String getNextUrl() {
        return this.nextUrl;
    }

    public void setNextUrl(String nextUrl2) {
        this.nextUrl = nextUrl2;
    }

    public String getSecrityPay() {
        return this.secrityPay;
    }

    public void setSecrityPay(String secrityPay2) {
        this.secrityPay = secrityPay2;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time2) {
        this.time = time2;
    }

    public String getOrderKey() {
        return this.orderKey;
    }

    public void setOrderKey(String orderKey2) {
        this.orderKey = orderKey2;
    }

    public String toString() {
        return "CreateOrderResult [bizOrderId=" + this.bizOrderId + ", alipayOrderId=" + this.alipayOrderId + ", buyerNumId=" + this.buyerNumId + ", nextUrl=" + this.nextUrl + ", secrityPay=" + this.secrityPay + ", time=" + this.time + ", orderKey=" + this.orderKey + ", simplePay=" + this.simplePay + "]";
    }
}
