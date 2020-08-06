package com.yunos.tvtaobao.biz.request.bo;

import android.text.Html;
import android.text.TextUtils;
import anetwork.channel.util.RequestConstant;
import com.alibaba.fastjson.JSON;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.Serializable;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class GoodsWithZtc implements Serializable, SearchedGoods {
    private static final String TAG = GoodsWithZtc.class.getSimpleName();
    private boolean buyCashback;
    private String discount;
    private String eurl;
    private String ismall;
    private String item_id;
    private String location;
    private String nick;
    private String post;
    private boolean pre;
    private String price;
    private RebateBo rebateBo;
    private String s11;
    private String s11_pre;
    private String sdkurl;
    private String sold;
    private String soldText;
    private List<String> tagNames;
    private String tagPicUrl;
    private String tbgoodslink;
    private String title;
    private String type;
    private String uri;

    public void setItemId(String item_id2) {
        this.item_id = item_id2;
    }

    public String getItemId() {
        return this.item_id;
    }

    public String getShopId() {
        return "";
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

    public String getSdkurl() {
        return this.sdkurl;
    }

    public void setSdkurl(String sdkurl2) {
        this.sdkurl = sdkurl2;
    }

    public void setTagNames(List<String> tagNames2) {
        this.tagNames = tagNames2;
    }

    public void setPre(boolean pre2) {
        this.pre = pre2;
    }

    public void setBuyCashback(boolean buyCashback2) {
        this.buyCashback = buyCashback2;
    }

    public String getEurl() {
        return this.eurl;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public void setImgUrl(String img) {
        this.tbgoodslink = img;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public void setSold(String sold2) {
        this.sold = sold2;
    }

    public String getTitle() {
        return this.title;
    }

    public String getWmPrice() {
        return this.discount;
    }

    public String getPostFee() {
        return this.post;
    }

    public String getDetailUrl() {
        return this.uri;
    }

    public String getImgUrl() {
        return this.tbgoodslink;
    }

    public String getPrice() {
        return this.price;
    }

    public String getSold() {
        return this.sold;
    }

    public String toString() {
        return "[  title = " + this.title + ", uri = " + this.uri + ", img = " + this.tbgoodslink + ", price = " + this.price + ", sold = " + this.sold + ", discount = " + this.discount + ", eurl = " + this.eurl + ", ismall = " + this.ismall + ", tbgoodslink = " + this.tbgoodslink + ", sdkurl = " + this.sdkurl + " ]";
    }

    public String getPost() {
        return null;
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

    public void setRebateBo(RebateBo rebateBo2) {
        this.rebateBo = rebateBo2;
    }

    public RebateBo getRebateBo() {
        return this.rebateBo;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick2) {
        this.nick = nick2;
    }

    public String getTagPicUrl() {
        return this.tagPicUrl;
    }

    public void setTagPicUrl(String tagPicUrl2) {
        this.tagPicUrl = tagPicUrl2;
    }

    public static GoodsWithZtc resolveFromJson(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        GoodsWithZtc goods = new GoodsWithZtc();
        if (!obj.isNull("title")) {
            goods.setTitle(Html.fromHtml(obj.getString("title").replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("\r", " ")).toString());
        }
        if (!obj.isNull("type")) {
            goods.setType(obj.getString("type"));
        }
        if (!obj.isNull("discount")) {
            goods.discount = obj.getString("discount");
        }
        if (!obj.isNull("price")) {
            goods.setPrice(obj.getString("price"));
        }
        if (!obj.isNull("tbgoodslink")) {
            goods.setImgUrl(obj.getString("tbgoodslink"));
        }
        if (!obj.isNull("ismall")) {
            goods.ismall = obj.getString("ismall");
        }
        if (!obj.isNull("type")) {
            goods.type = obj.getString("type");
        }
        if (!obj.isNull("s11")) {
            goods.s11 = obj.getString("s11");
        }
        if (!obj.isNull("s11_pre")) {
            goods.s11_pre = obj.getString("s11_pre");
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
        if (!obj.isNull("eurl")) {
            goods.eurl = obj.getString("eurl");
        }
        if (!obj.isNull(HuasuVideo.TAG_URI)) {
            goods.uri = obj.getString(HuasuVideo.TAG_URI);
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
        if (!obj.isNull(BaseConfig.INTENT_KEY_TID)) {
            goods.setItemId(obj.getString(BaseConfig.INTENT_KEY_TID));
            ZpLogger.v(TAG, "GoodsWithZtcTid = " + goods.getItemId());
        }
        ZpLogger.v(TAG, "GoodsWithZtc.resolveFromJson.goods = " + goods);
        return goods;
    }
}
