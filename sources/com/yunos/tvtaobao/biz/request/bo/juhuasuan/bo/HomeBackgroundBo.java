package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeBackgroundBo extends BaseMO {
    private static final long serialVersionUID = -8346084825413748032L;
    private String bg_img;
    private String logo;

    public static HomeBackgroundBo resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        HomeBackgroundBo item = new HomeBackgroundBo();
        item.setBg_img(obj.optString("bg_img"));
        item.setLogo(obj.optString("logo"));
        return item;
    }

    public String getBg_img() {
        return this.bg_img;
    }

    public void setBg_img(String bg_img2) {
        this.bg_img = bg_img2;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo2) {
        this.logo = logo2;
    }
}
