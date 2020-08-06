package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class Guarantee implements Serializable {
    private static final long serialVersionUID = -1372428280992334857L;
    private String icon;
    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon2) {
        this.icon = icon2;
    }

    public static Guarantee resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Guarantee g = new Guarantee();
        if (!obj.isNull("icon")) {
            g.setIcon(obj.getString("icon"));
        }
        if (obj.isNull("title")) {
            return g;
        }
        g.setTitle(obj.getString("title"));
        return g;
    }
}
