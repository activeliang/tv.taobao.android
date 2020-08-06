package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class Pay {
    private String currencySymbol;
    private int currencyUnitFactor;
    private JSONObject data;
    private boolean multipleCurrencySymbol = false;
    private boolean showInstruction = true;

    public Pay(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public boolean isShowInstruction() {
        return this.showInstruction;
    }

    public void setShowInstruction(boolean showInstruction2) {
        this.showInstruction = showInstruction2;
    }

    public boolean isFromServer() {
        return this.data.getBooleanValue("fromServer");
    }

    public void setFromLocal() {
        this.data.put("fromServer", (Object) false);
    }

    public String getTotalDiscount() {
        return this.data.getString("totalDiscount");
    }

    public void setTotalDiscount(String totalDiscount) {
        this.data.put("totalDiscount", (Object) totalDiscount);
    }

    public String getTotalDiscountTitle() {
        return this.data.getString("totalDiscountTitle");
    }

    public void setTotalDiscountTitle(String title) {
        this.data.put("totalDiscountTitle", (Object) title);
        this.data.remove("totalDiscount");
    }

    public long getPrice() {
        return this.data.getLongValue("price");
    }

    public void setPrice(long price) {
        this.data.put("price", (Object) Long.valueOf(price));
    }

    public String getCurrencySymbol() {
        if (this.multipleCurrencySymbol || this.currencySymbol != null) {
            return this.currencySymbol;
        }
        return "￥";
    }

    public void setCurrencySymbol(String currencySymbol2) {
        this.currencySymbol = currencySymbol2;
    }

    public int getCurrencyUnitFactor() {
        if (this.currencyUnitFactor == 0) {
            this.currencyUnitFactor = 100;
        }
        return this.currencyUnitFactor;
    }

    public boolean isMultipleCurrencySymbol() {
        return this.multipleCurrencySymbol;
    }

    public void setMultipleCurrencySymbol(boolean multipleCurrencySymbol2) {
        this.multipleCurrencySymbol = multipleCurrencySymbol2;
    }

    public void setCurrencyUnitFactor(int currencyUnitFactor2) {
        this.currencyUnitFactor = currencyUnitFactor2;
    }

    public String getPriceTitle() {
        return this.data.getString("priceTitle");
    }

    public void setPriceTitle(String priceTitle) {
        this.data.put("priceTitle", (Object) priceTitle);
    }

    public String getPostTitle() {
        return this.data.getString("postTitle");
    }

    public void setPostTitle(String title) {
        this.data.put("postTitle", (Object) title);
    }

    public long getTsmTotalDiscountPrice() {
        return this.data.getLongValue("tsmTotalDiscount");
    }

    public void setTsmTotalDiscount(String title) {
        this.data.put("tsmTotalDiscountTitle", (Object) title);
    }

    public String getTsmTotalDiscount() {
        return this.data.getString("tsmTotalDiscountTitle");
    }

    public String getDiscountTips() {
        return this.data.getString("discountTips");
    }

    public String toString() {
        return "Pay [price=" + getPrice() + ",priceTitle=" + getPriceTitle() + ",postTitle=" + getPostTitle() + "]";
    }
}
