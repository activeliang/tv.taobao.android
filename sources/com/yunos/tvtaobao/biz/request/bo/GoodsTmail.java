package com.yunos.tvtaobao.biz.request.bo;

import android.text.Html;
import android.text.TextUtils;
import anetwork.channel.util.RequestConstant;
import com.alibaba.fastjson.JSON;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.Serializable;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class GoodsTmail implements Serializable, SearchedGoods {
    private static final long serialVersionUID = 8908397788800721428L;
    private String auction_tag;
    private boolean buyCashback;
    private String comment_num;
    private String country_code;
    private String img;
    private String item_id;
    private String nick;
    private String post_fee;
    private boolean pre;
    private String price;
    private RebateBo rebateBo;
    private String s11;
    private String s11_pre;
    private String sdkurl;
    private String shop_id;
    private String shop_name;
    private String sold;
    private String soldText;
    private List<String> tagNames;
    private String tagPicUrl;
    private String title;
    private String type;
    private String uri;
    private String url;
    private String wm_price;

    public String getSdkurl() {
        return this.sdkurl;
    }

    public void setSdkurl(String sdkurl2) {
        this.sdkurl = sdkurl2;
    }

    public void setPre(boolean pre2) {
        this.pre = pre2;
    }

    public void setBuyCashback(boolean buyCashback2) {
        this.buyCashback = buyCashback2;
    }

    public String getSoldText() {
        return this.soldText;
    }

    public void setSoldText(String soldText2) {
        this.soldText = soldText2;
    }

    public List<String> getTagNames() {
        return this.tagNames;
    }

    public void setTagNames(List<String> tagNames2) {
        this.tagNames = tagNames2;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getEurl() {
        return null;
    }

    public String getType() {
        return this.type;
    }

    public void setItemId(String item_id2) {
        this.item_id = item_id2;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public void setDetailUrl(String url2) {
        this.url = url2;
    }

    public void setImgUrl(String img2) {
        this.img = img2;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public void setWmPrice(String wm_price2) {
        this.wm_price = wm_price2;
    }

    public void setSold(String sold2) {
        this.sold = sold2;
    }

    public void setPostFee(String post_fee2) {
        this.post_fee = post_fee2;
    }

    public void setShopId(String shop_id2) {
        this.shop_id = shop_id2;
    }

    public void setCommentNum(String comment_num2) {
        this.comment_num = comment_num2;
    }

    public void setAuctionTag(String auction_tag2) {
        this.auction_tag = auction_tag2;
    }

    public void setShopName(String shop_name2) {
        this.shop_name = shop_name2;
    }

    public void setCountryCode(String country_code2) {
        this.country_code = country_code2;
    }

    public String getItemId() {
        return this.item_id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDetailUrl() {
        return this.url;
    }

    public String getImgUrl() {
        return this.img;
    }

    public String getPrice() {
        return this.price;
    }

    public String getWmPrice() {
        return this.wm_price;
    }

    public String getSold() {
        return this.sold;
    }

    public String getPostFee() {
        return this.post_fee;
    }

    public String getShopId() {
        return this.shop_id;
    }

    public String getCommentNum() {
        return this.comment_num;
    }

    public String getAuctionTag() {
        return this.auction_tag;
    }

    public String getShopName() {
        return this.shop_name;
    }

    public String getCountryCode() {
        return this.country_code;
    }

    public String toString() {
        return "[ item_id = " + this.item_id + ", title = " + this.title + ", url = " + this.url + ", img = " + this.img + ", price = " + this.price + ", wm_price = " + this.wm_price + ", sold = " + this.sold + ", post_fee = " + this.post_fee + ", shop_id = " + this.shop_id + ", shop_name = " + this.shop_name + ", comment_num = " + this.comment_num + ", auction_tag = " + this.auction_tag + ", country_code = " + this.country_code + ", sdkurl = " + this.sdkurl + " ]";
    }

    public String getUri() {
        return this.uri;
    }

    public String getS11() {
        return this.s11;
    }

    public String getS11Pre() {
        return this.s11_pre;
    }

    public Boolean isPre() {
        return Boolean.valueOf(this.pre);
    }

    public Boolean isBuyCashback() {
        return Boolean.valueOf(this.buyCashback);
    }

    public void setNick(String nick2) {
        this.nick = nick2;
    }

    public String getNick() {
        return this.nick;
    }

    public void setRebateBo(RebateBo rebateBo2) {
        this.rebateBo = rebateBo2;
    }

    public RebateBo getRebateBo() {
        return this.rebateBo;
    }

    public String getTagPicUrl() {
        return this.tagPicUrl;
    }

    public void setTagPicUrl(String tagPicUrl2) {
        this.tagPicUrl = tagPicUrl2;
    }

    public static GoodsTmail resolveFromJson(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        GoodsTmail goods = new GoodsTmail();
        if (!obj.isNull("title")) {
            goods.setTitle(Html.fromHtml(obj.getString("title").replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("\r", " ")).toString());
        }
        if (!obj.isNull(HuasuVideo.TAG_URI)) {
            goods.uri = obj.getString(HuasuVideo.TAG_URI);
        }
        if (!obj.isNull("type")) {
            goods.setType(obj.getString("type"));
        }
        if (!obj.isNull(BaseConfig.INTENT_KEY_TID)) {
            goods.setItemId(obj.getString(BaseConfig.INTENT_KEY_TID));
        }
        if (!obj.isNull("price")) {
            goods.setPrice(obj.getString("price"));
        }
        if (!obj.isNull("discount")) {
            goods.setWmPrice(obj.getString("discount"));
        }
        if (!obj.isNull("sold")) {
            String sold2 = obj.getString("sold").trim();
            if (TextUtils.isEmpty(sold2)) {
                sold2 = "0";
            } else if (!TextUtils.isDigitsOnly(sold2.substring(0, 1))) {
                sold2 = "0";
            }
            goods.setSold(sold2);
        }
        if (!obj.isNull("url")) {
            goods.setDetailUrl(obj.getString("url"));
        }
        if (!obj.isNull("shopId")) {
            goods.setShopId(obj.getString("shopId"));
        }
        if (!obj.isNull("shopTitle")) {
            goods.setShopName(obj.getString("shopTitle"));
        }
        if (!obj.isNull("post")) {
            goods.setPostFee(obj.getString("post"));
        }
        if (!obj.isNull("picPath")) {
            goods.setImgUrl(obj.getString("picPath"));
        } else if (!obj.isNull(TuwenConstants.PARAMS.PIC_URL)) {
            goods.setImgUrl(obj.getString(TuwenConstants.PARAMS.PIC_URL));
        }
        if (!obj.isNull("commentCount")) {
            goods.setCommentNum(obj.getString("commentCount"));
        }
        if (!obj.isNull("auctionTags")) {
            goods.setAuctionTag(obj.getString("auctionTags"));
        }
        if (!obj.isNull("countryCode")) {
            goods.setCountryCode(obj.getString("countryCode"));
        }
        if (!obj.isNull("s11")) {
            goods.s11 = obj.getString("s11");
        }
        if (!obj.isNull("s11_pre")) {
            goods.s11_pre = obj.getString("s11_pre");
        }
        if (!obj.isNull(RequestConstant.ENV_PRE)) {
            goods.pre = obj.getBoolean(RequestConstant.ENV_PRE);
        }
        if (!obj.isNull("buyCashback")) {
            goods.buyCashback = obj.getBoolean("buyCashback");
        }
        if (!obj.isNull(TvTaoSharedPerference.NICK)) {
            goods.setNick(obj.getString(TvTaoSharedPerference.NICK));
        }
        if (!obj.isNull("tagPicUrl")) {
            goods.setTagPicUrl(obj.getString("tagPicUrl"));
        }
        if (!obj.isNull("tagNames")) {
            goods.setTagNames(JSON.parseArray(obj.optString("tagNames"), String.class));
        }
        if (!obj.isNull("soldText")) {
            goods.setSoldText(obj.getString("soldText"));
        }
        if (!obj.isNull(BaseConfig.INTENT_KEY_SDKURL)) {
            goods.setSdkurl(obj.getString(BaseConfig.INTENT_KEY_SDKURL));
        }
        ZpLogger.v("GoodsTmail", "GoodsTmail.resolveFromJson.goods = " + goods);
        return goods;
    }
}
