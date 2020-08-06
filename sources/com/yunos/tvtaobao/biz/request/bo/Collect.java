package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import java.io.Serializable;

public class Collect implements Serializable {
    private static final long serialVersionUID = -610484326142980003L;
    private String collectCount;
    private String img;
    private String numId;
    private String ownernick;
    private String price;
    private String promotionPrice;
    private String title;

    public String getNumId() {
        return this.numId;
    }

    public void setNumId(String numId2) {
        this.numId = numId2;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img2) {
        this.img = img2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getPromotionPrice() {
        if (TextUtils.isEmpty(this.promotionPrice)) {
            return this.price;
        }
        return this.promotionPrice;
    }

    public void setPromotionPrice(String promotionPrice2) {
        this.promotionPrice = promotionPrice2;
    }

    public String getOwnernick() {
        return this.ownernick;
    }

    public void setOwnernick(String ownernick2) {
        this.ownernick = ownernick2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getCollectCount() {
        return this.collectCount;
    }

    public void setCollectCount(String collectCount2) {
        this.collectCount = collectCount2;
    }
}
