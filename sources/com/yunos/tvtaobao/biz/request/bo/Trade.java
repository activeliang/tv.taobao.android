package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class Trade implements Serializable {
    private static final long serialVersionUID = 6696163235927330971L;
    private Boolean buySupport;
    private Boolean cartSupport;
    private Long tag;
    private String url;

    public Long getTag() {
        return this.tag;
    }

    public void setTag(Long tag2) {
        this.tag = tag2;
    }

    public Boolean getCartSupport() {
        return this.cartSupport;
    }

    public void setCartSupport(Boolean cartSupport2) {
        this.cartSupport = cartSupport2;
    }

    public Boolean getBuySupport() {
        return this.buySupport;
    }

    public void setBuySupport(Boolean buySupport2) {
        this.buySupport = buySupport2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public static Trade resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Trade trade = new Trade();
        if (!obj.isNull("buySupport")) {
            trade.setBuySupport(Boolean.valueOf(obj.getBoolean("buySupport")));
        }
        if (!obj.isNull("cartSupport")) {
            trade.setCartSupport(Boolean.valueOf(obj.getBoolean("cartSupport")));
        }
        if (!obj.isNull("tag")) {
            trade.setTag(Long.valueOf(obj.getLong("tag")));
        }
        if (obj.isNull("url")) {
            return trade;
        }
        trade.setUrl(obj.getString("url"));
        return trade;
    }
}
