package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import com.yunos.tvtaobao.biz.request.core.JsonResolver;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeItemsBo extends BaseMO {
    private static final long serialVersionUID = -1738409524853905138L;
    private String bg_img;
    private Map<String, String> content;
    private String desc;
    private String e_name;
    private String front_img;
    private String title;
    private String type;

    public static HomeItemsBo resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        HomeItemsBo item = new HomeItemsBo();
        item.setTitle(obj.optString("title"));
        item.setE_name(obj.optString("e_name"));
        item.setDesc(obj.optString("desc"));
        item.setType(obj.optString("type"));
        item.setBg_img(obj.optString("bg_img"));
        item.setFront_img(obj.optString("front_img"));
        if (!obj.has("content")) {
            return item;
        }
        item.setContent(JsonResolver.jsonobjToMap(obj.getJSONObject("content")));
        return item;
    }

    public String toString() {
        return "HomeItemsBo [title=" + this.title + ", e_name=" + this.e_name + ", desc=" + this.desc + ", type=" + this.type + ", bg_img=" + this.bg_img + ", front_img=" + this.front_img + ", content=" + this.content + "]";
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getE_name() {
        return this.e_name;
    }

    public void setE_name(String e_name2) {
        this.e_name = e_name2;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getBg_img() {
        return this.bg_img;
    }

    public void setBg_img(String bg_img2) {
        this.bg_img = bg_img2;
    }

    public String getFront_img() {
        return this.front_img;
    }

    public void setFront_img(String front_img2) {
        this.front_img = front_img2;
    }

    public Map<String, String> getContent() {
        return this.content;
    }

    public void setContent(Map<String, String> content2) {
        this.content = content2;
    }
}
