package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class PropValue implements Serializable {
    private static final long serialVersionUID = -8766003133839210515L;
    private String imgUrl;
    private String name;
    private String valueAlias;
    private Long valueId;

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getValueAlias() {
        return this.valueAlias;
    }

    public void setValueAlias(String valueAlias2) {
        this.valueAlias = valueAlias2;
    }

    public String getContent() {
        return getValueAlias() == null ? getName() : getValueAlias();
    }

    public static PropValue resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        PropValue pv = new PropValue();
        if (!obj.isNull("imgUrl")) {
            pv.setImgUrl(obj.getString("imgUrl"));
        }
        if (!obj.isNull("name")) {
            pv.setName(obj.getString("name"));
        }
        if (!obj.isNull("valueAlias")) {
            pv.setValueAlias(obj.getString("valueAlias"));
        }
        if (obj.isNull("valueId")) {
            return pv;
        }
        pv.setValueId(Long.valueOf(obj.getLong("valueId")));
        return pv;
    }

    public Long getValueId() {
        return this.valueId;
    }

    public void setValueId(Long valueId2) {
        this.valueId = valueId2;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl2) {
        this.imgUrl = imgUrl2;
    }
}
