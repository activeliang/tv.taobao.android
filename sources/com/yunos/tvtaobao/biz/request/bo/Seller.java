package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Seller extends BaseMO implements Serializable {
    private static final long serialVersionUID = -6704952130389496030L;
    private String certify;
    private Integer creditLevel;
    private EvaluateInfo[] evaluateInfos;
    private String goodRatePercent;
    private String location;
    private String mobile;
    private String nick;
    private String phone;
    private Long shopId;
    private String type;
    private Long userNumId;
    private String userRegDate;

    public Long getUserNumId() {
        return this.userNumId;
    }

    public void setUserNumId(Long userNumId2) {
        this.userNumId = userNumId2;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location2) {
        this.location = location2;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick2) {
        this.nick = nick2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile2) {
        this.mobile = mobile2;
    }

    public String getCertify() {
        return this.certify;
    }

    public void setCertify(String certify2) {
        this.certify = certify2;
    }

    public Integer getCreditLevel() {
        return this.creditLevel;
    }

    public void setCreditLevel(Integer creditLevel2) {
        this.creditLevel = creditLevel2;
    }

    public String getGoodRatePercent() {
        return this.goodRatePercent;
    }

    public void setGoodRatePercent(String goodRatePercent2) {
        this.goodRatePercent = goodRatePercent2;
    }

    public String getUserRegDate() {
        return this.userRegDate;
    }

    public void setUserRegDate(String userRegDate2) {
        this.userRegDate = userRegDate2;
    }

    public static Seller resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Seller s = new Seller();
        if (!obj.isNull("certify")) {
            s.setCertify(obj.getString("certify"));
        }
        if (!obj.isNull("creditLevel")) {
            s.setCreditLevel(Integer.valueOf(obj.getInt("creditLevel")));
        }
        if (!obj.isNull("goodRatePercentage")) {
            s.setGoodRatePercent(obj.getString("goodRatePercentage"));
        }
        if (!obj.isNull("location")) {
            s.setLocation(obj.getString("location"));
        }
        if (!obj.isNull("mobile")) {
            s.setMobile(obj.getString("mobile"));
        }
        if (!obj.isNull(TvTaoSharedPerference.NICK)) {
            s.setNick(obj.getString(TvTaoSharedPerference.NICK));
        }
        if (!obj.isNull("phone")) {
            s.setPhone(obj.getString("phone"));
        }
        if (!obj.isNull("type")) {
            s.setType(obj.getString("type"));
        }
        if (!obj.isNull("userNumId")) {
            s.setUserNumId(Long.valueOf(obj.getLong("userNumId")));
        }
        if (!obj.isNull("userRegDate")) {
            s.setUserRegDate(obj.getString("userRegDate"));
        }
        if (!obj.isNull("shopId")) {
            s.setShopId(Long.valueOf(obj.getLong("shopId")));
        }
        if (obj.isNull("evaluateInfo")) {
            return s;
        }
        JSONArray array = obj.getJSONArray("evaluateInfo");
        EvaluateInfo[] temp = new EvaluateInfo[array.length()];
        for (int i = 0; i < array.length(); i++) {
            temp[i] = EvaluateInfo.resolveFromMTOP(array.getJSONObject(i));
        }
        s.setEvaluateInfos(temp);
        return s;
    }

    public EvaluateInfo[] getEvaluateInfos() {
        return this.evaluateInfos;
    }

    public void setEvaluateInfos(EvaluateInfo[] evaluateInfos2) {
        this.evaluateInfos = evaluateInfos2;
    }

    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long shopId2) {
        this.shopId = shopId2;
    }
}
