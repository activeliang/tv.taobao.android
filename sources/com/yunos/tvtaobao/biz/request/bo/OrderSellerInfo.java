package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderSellerInfo implements Serializable {
    private static final long serialVersionUID = -7979043703805714444L;
    private String alipayAccount;
    private String name;
    private String sellerId;
    private String sellerNick;
    private String tel;

    public String getAlipayAccount() {
        return this.alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount2) {
        this.alipayAccount = alipayAccount2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(String sellerId2) {
        this.sellerId = sellerId2;
    }

    public String getSellerNick() {
        return this.sellerNick;
    }

    public void setSellerNick(String sellerNick2) {
        this.sellerNick = sellerNick2;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel2) {
        this.tel = tel2;
    }

    public static OrderSellerInfo resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        OrderSellerInfo orderSellerInfo = new OrderSellerInfo();
        if (!obj.isNull("name")) {
            orderSellerInfo.setName(obj.getString("name"));
        }
        if (!obj.isNull("sellerId")) {
            orderSellerInfo.setSellerId(obj.getString("sellerId"));
        }
        if (!obj.isNull("sellerNick")) {
            orderSellerInfo.setSellerNick(obj.getString("sellerNick"));
        }
        if (!obj.isNull("tel")) {
            orderSellerInfo.setTel(obj.getString("tel"));
        }
        if (obj.isNull("alipayAccount")) {
            return orderSellerInfo;
        }
        orderSellerInfo.setAlipayAccount(obj.getString("alipayAccount"));
        return orderSellerInfo;
    }
}
