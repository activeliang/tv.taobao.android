package com.yunos.tvtaobao.biz.request.bo;

import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import org.json.JSONException;
import org.json.JSONObject;

public class GuessLikeGoods {
    private String category;
    private String curPrice;
    private String discount;
    private String fkid;
    private String nick;
    private String picUrl;
    private String price;
    private String quantity;
    private RebateBo rebateBo;
    private String sold;
    private String soldText;
    private String tid;
    private String title;
    private String type;

    public static GuessLikeGoods resolveData(JSONObject object) throws JSONException {
        GuessLikeGoods guessLikeGoods = new GuessLikeGoods();
        guessLikeGoods.setType(object.getString("type"));
        guessLikeGoods.setCategory(object.getString("category"));
        guessLikeGoods.setDiscount(object.getString("discount"));
        guessLikeGoods.setCurPrice(object.getString("curPrice"));
        guessLikeGoods.setFkid(object.getString("fkid"));
        guessLikeGoods.setNick(object.getString(TvTaoSharedPerference.NICK));
        guessLikeGoods.setPicUrl(object.getString(TuwenConstants.PARAMS.PIC_URL));
        guessLikeGoods.setPrice(object.getString("price"));
        guessLikeGoods.setQuantity(object.getString("quantity"));
        guessLikeGoods.setSold(object.getString("sold"));
        guessLikeGoods.setSoldText(object.getString("soldText"));
        guessLikeGoods.setTid(object.getString(BaseConfig.INTENT_KEY_TID));
        guessLikeGoods.setTitle(object.getString("title"));
        return guessLikeGoods;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category2) {
        this.category = category2;
    }

    public String getDiscount() {
        return this.discount;
    }

    public void setDiscount(String discount2) {
        this.discount = discount2;
    }

    public String getFkid() {
        return this.fkid;
    }

    public void setFkid(String fkid2) {
        this.fkid = fkid2;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick2) {
        this.nick = nick2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity2) {
        this.quantity = quantity2;
    }

    public String getSold() {
        return this.sold;
    }

    public void setSold(String sold2) {
        this.sold = sold2;
    }

    public String getTid() {
        return this.tid;
    }

    public void setTid(String tid2) {
        this.tid = tid2;
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
}
