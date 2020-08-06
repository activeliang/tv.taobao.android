package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tvtaobao.biz.common.BaseConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class KMGoods {
    private String curPrice;
    private String eurl;
    private String goodsprice;
    private String ismall;
    private String location;
    private String postfee;
    private RebateBo rebateBo;
    private String resourceid;
    private String saleprice;
    private String sdkurl;
    private String sell;
    private String showAd;
    private String soldText;
    private String tbgoodslink;
    private String title;
    private String type;

    public static KMGoods resolveData(JSONObject object) throws JSONException {
        KMGoods kmGoods = new KMGoods();
        kmGoods.setType(object.optString("type"));
        kmGoods.setEurl(object.optString("eurl"));
        kmGoods.setGoodsprice(object.optString("goodsprice"));
        kmGoods.setCurPrice(object.optString("curPrice"));
        kmGoods.setIsmall(object.optString("ismall"));
        kmGoods.setLocation(object.optString("location"));
        kmGoods.setPostfee(object.optString("postfee"));
        kmGoods.setSaleprice(object.optString("saleprice"));
        kmGoods.setSell(object.optString("sell"));
        kmGoods.setShowAd(object.optString("showAd"));
        kmGoods.setSoldText(object.optString("soldText"));
        kmGoods.setTbgoodslink(object.optString("tbgoodslink"));
        kmGoods.setTitle(object.optString("title"));
        kmGoods.setResourceid(object.optString("resourceid"));
        kmGoods.setSdkurl(object.optString(BaseConfig.INTENT_KEY_SDKURL));
        return kmGoods;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getEurl() {
        return this.eurl;
    }

    public void setEurl(String eurl2) {
        this.eurl = eurl2;
    }

    public String getGoodsprice() {
        return this.goodsprice;
    }

    public void setGoodsprice(String goodsprice2) {
        this.goodsprice = goodsprice2;
    }

    public String getIsmall() {
        return this.ismall;
    }

    public void setIsmall(String ismall2) {
        this.ismall = ismall2;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location2) {
        this.location = location2;
    }

    public String getPostfee() {
        return this.postfee;
    }

    public void setPostfee(String postfee2) {
        this.postfee = postfee2;
    }

    public String getSaleprice() {
        return this.saleprice;
    }

    public void setSaleprice(String saleprice2) {
        this.saleprice = saleprice2;
    }

    public String getSell() {
        return this.sell;
    }

    public void setSell(String sell2) {
        this.sell = sell2;
    }

    public String getShowAd() {
        return this.showAd;
    }

    public void setShowAd(String showAd2) {
        this.showAd = showAd2;
    }

    public String getTbgoodslink() {
        return this.tbgoodslink;
    }

    public void setTbgoodslink(String tbgoodslink2) {
        this.tbgoodslink = tbgoodslink2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public RebateBo getRebateBo() {
        return this.rebateBo;
    }

    public void setRebateBo(RebateBo rebateBo2) {
        this.rebateBo = rebateBo2;
    }

    public String getSoldText() {
        return this.soldText;
    }

    public void setSoldText(String soldText2) {
        this.soldText = soldText2;
    }

    public String getCurPrice() {
        return this.curPrice;
    }

    public void setCurPrice(String curPrice2) {
        this.curPrice = curPrice2;
    }

    public String getResourceid() {
        return this.resourceid;
    }

    public void setResourceid(String resourceid2) {
        this.resourceid = resourceid2;
    }

    public String getSdkurl() {
        return this.sdkurl;
    }

    public void setSdkurl(String sdkurl2) {
        this.sdkurl = sdkurl2;
    }
}
