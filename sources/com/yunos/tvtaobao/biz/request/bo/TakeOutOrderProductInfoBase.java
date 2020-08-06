package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderProductInfoBase implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private int discountPrice;
    private int originalPrice;
    private String productId;
    private String productLogo;
    private String productTitle;
    private int quantity;
    private String skuId;
    private String skuName;
    private String skuTitle;

    public int getDiscountPrice() {
        return this.discountPrice;
    }

    public void setDiscountPrice(int discountPrice2) {
        this.discountPrice = discountPrice2;
    }

    public int getOriginalPrice() {
        return this.originalPrice;
    }

    public void setOriginalPrice(int originalPrice2) {
        this.originalPrice = originalPrice2;
    }

    public String getSkuId() {
        return this.skuId;
    }

    public void setSkuId(String skuId2) {
        this.skuId = skuId2;
    }

    public String getSkuTitle() {
        return this.skuTitle;
    }

    public void setSkuTitle(String skuTitle2) {
        this.skuTitle = skuTitle2;
    }

    public String getSkuName() {
        return this.skuName;
    }

    public void setSkuName(String skuName2) {
        this.skuName = skuName2;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity2) {
        this.quantity = quantity2;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId2) {
        this.productId = productId2;
    }

    public String getProductLogo() {
        return this.productLogo;
    }

    public void setProductLogo(String productLogo2) {
        this.productLogo = productLogo2;
    }

    public String getProductTitle() {
        return this.productTitle;
    }

    public void setProductTitle(String productTitle2) {
        this.productTitle = productTitle2;
    }

    public static TakeOutOrderProductInfoBase resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderProductInfoBase productInfoBase = new TakeOutOrderProductInfoBase();
        productInfoBase.setProductLogo("");
        if (obj == null) {
            return productInfoBase;
        }
        productInfoBase.setSkuName(obj.optString("sku"));
        productInfoBase.setSkuId(obj.optString("skuId"));
        productInfoBase.setSkuTitle(obj.optString("skuTitle"));
        productInfoBase.setDiscountPrice(obj.optInt("discountPrice", 0));
        productInfoBase.setOriginalPrice(obj.optInt("originalPrice", 0));
        productInfoBase.setQuantity(obj.optInt("quantity", 0));
        productInfoBase.setProductLogo(obj.optString("itemLogo"));
        productInfoBase.setProductTitle(obj.optString("itemTitle"));
        productInfoBase.setProductId(obj.optString("itemId"));
        return productInfoBase;
    }
}
