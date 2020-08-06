package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderInfoDetails4TradeInfo implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private String alipayNo;
    private String autoConfirmTime;
    private String autoConfirmTimestamp;
    private String cancelOrderTime;
    private String createTime;
    private String payTime;
    private String tbOrderId;

    public String getAlipayNo() {
        return this.alipayNo;
    }

    public void setAlipayNo(String alipayNo2) {
        this.alipayNo = alipayNo2;
    }

    public String getAutoConfirmTime() {
        return this.autoConfirmTime;
    }

    public void setAutoConfirmTime(String autoConfirmTime2) {
        this.autoConfirmTime = autoConfirmTime2;
    }

    public String getAutoConfirmTimestamp() {
        return this.autoConfirmTimestamp;
    }

    public void setAutoConfirmTimestamp(String autoConfirmTimestamp2) {
        this.autoConfirmTimestamp = autoConfirmTimestamp2;
    }

    public String getCancelOrderTime() {
        return this.cancelOrderTime;
    }

    public void setCancelOrderTime(String cancelOrderTime2) {
        this.cancelOrderTime = cancelOrderTime2;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime2) {
        this.createTime = createTime2;
    }

    public String getPayTime() {
        return this.payTime;
    }

    public void setPayTime(String payTime2) {
        this.payTime = payTime2;
    }

    public String getTbOrderId() {
        return this.tbOrderId;
    }

    public void setTbOrderId(String tbOrderId2) {
        this.tbOrderId = tbOrderId2;
    }

    public static TakeOutOrderInfoDetails4TradeInfo resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderInfoDetails4TradeInfo details4TradeInfo = new TakeOutOrderInfoDetails4TradeInfo();
        if (obj == null) {
            return details4TradeInfo;
        }
        details4TradeInfo.setAlipayNo(obj.optString("alipayNo"));
        details4TradeInfo.setAutoConfirmTime(obj.optString("autoConfirmTime"));
        details4TradeInfo.setAutoConfirmTimestamp(obj.optString("autoConfirmTimestamp"));
        details4TradeInfo.setCancelOrderTime(obj.optString("cancelOrderTime"));
        details4TradeInfo.setCreateTime(obj.optString("createTime"));
        details4TradeInfo.setPayTime(obj.optString("payTime"));
        details4TradeInfo.setTbOrderId(obj.optString("tbOrderId"));
        return details4TradeInfo;
    }
}
