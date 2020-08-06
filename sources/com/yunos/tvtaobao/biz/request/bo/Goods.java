package com.yunos.tvtaobao.biz.request.bo;

import android.text.Html;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class Goods implements Serializable {
    private static final long serialVersionUID = 6257317536315697347L;
    private String fastPostFee = null;
    private String itemId = null;
    private String location = null;
    private String nick = null;
    private String picUrl = null;
    private String price = null;
    private String priceWithRate = null;
    private String ratesum = null;
    private String sold = null;
    private String title = null;
    private String userId = null;
    private String userType = null;
    private String zkRate;

    public String getZkRate() {
        return this.zkRate;
    }

    public void setZkRate(String zkRate2) {
        this.zkRate = zkRate2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location2) {
        this.location = location2;
    }

    public String getSold() {
        return this.sold;
    }

    public void setSold(String sold2) {
        this.sold = sold2;
    }

    public String getRatesum() {
        return this.ratesum;
    }

    public void setRatesum(String ratesum2) {
        this.ratesum = ratesum2;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType2) {
        this.userType = userType2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick2) {
        this.nick = nick2;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }

    public String getPriceWithRate() {
        return this.priceWithRate;
    }

    public void setPriceWithRate(String priceWithRate2) {
        this.priceWithRate = priceWithRate2;
    }

    public void setFastPostFee(String fastPostFee2) {
        this.fastPostFee = fastPostFee2;
    }

    public String getFastPostFee() {
        return this.fastPostFee;
    }

    public static Goods resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Goods goods = new Goods();
        if (!obj.isNull("title")) {
            goods.setTitle(Html.fromHtml(obj.getString("title").replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("\r", " ")).toString());
        }
        if (!obj.isNull("price")) {
            goods.setPrice(obj.getString("price"));
        }
        if (!obj.isNull("pic_path")) {
            goods.setPicUrl(obj.getString("pic_path").replace("_60x60.jpg", ""));
        }
        if (!obj.isNull("location")) {
            String location2 = obj.getString("location");
            try {
                int len = location2.indexOf(" ");
                if (len != -1) {
                    location2 = new String(location2.substring(len + 1));
                }
            } catch (Exception e) {
            }
            goods.setLocation(location2);
        }
        if (!obj.isNull("sold")) {
            goods.setSold(obj.getString("sold"));
        }
        if (!obj.isNull("priceWap")) {
            goods.setPriceWithRate(obj.getString("priceWap"));
        }
        if (!obj.isNull("userType")) {
            goods.setUserType(obj.getString("userType"));
        }
        if (!obj.isNull("item_id")) {
            goods.setItemId(obj.getString("item_id"));
        }
        if (!obj.isNull("userId")) {
            goods.setUserId(obj.getString("userId"));
        }
        if (!obj.isNull(TvTaoSharedPerference.NICK)) {
            goods.setNick(obj.getString(TvTaoSharedPerference.NICK));
        }
        if (!obj.isNull("ratesum")) {
            goods.setRatesum(obj.getString("ratesum"));
        }
        if (!obj.isNull("fastPostFee")) {
            goods.setFastPostFee(obj.getString("fastPostFee"));
        }
        goods.setZkRate(obj.optString("zkRate", (String) null));
        return goods;
    }
}
