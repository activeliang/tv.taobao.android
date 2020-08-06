package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeCatesBo extends BaseMO {
    private static final long serialVersionUID = 5000582262085167573L;
    private String bgcolor;
    private String cid;
    private String e_name;
    private String icon;
    private String iconHl;
    private String name;
    private String type;
    private Boolean visible;

    public static HomeCatesBo resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        HomeCatesBo item = new HomeCatesBo();
        item.setName(obj.optString("name"));
        item.setE_name(obj.optString("e_name"));
        item.setCid(obj.optString("cid"));
        item.setType(obj.optString("type"));
        item.setBgcolor(obj.optString("bgcolor"));
        item.setIcon(obj.optString("icon"));
        item.setIconHl(obj.optString("iconHl"));
        item.setVisible(Boolean.valueOf(obj.optBoolean("visible")));
        return item;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getE_name() {
        return this.e_name;
    }

    public void setE_name(String e_name2) {
        this.e_name = e_name2;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid2) {
        this.cid = cid2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getBgcolor() {
        return this.bgcolor;
    }

    public void setBgcolor(String bgcolor2) {
        this.bgcolor = bgcolor2;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon2) {
        this.icon = icon2;
    }

    public String getIconHl() {
        return this.iconHl;
    }

    public void setIconHl(String iconHl2) {
        this.iconHl = iconHl2;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public void setVisible(Boolean visible2) {
        this.visible = visible2;
    }

    public String toString() {
        return "HomeCatesBo [name=" + this.name + ", e_name=" + this.e_name + ", cid=" + this.cid + ", type=" + this.type + ", bgcolor=" + this.bgcolor + ", icon=" + this.icon + ", iconHl=" + this.iconHl + ", visible=" + this.visible + "]";
    }
}
