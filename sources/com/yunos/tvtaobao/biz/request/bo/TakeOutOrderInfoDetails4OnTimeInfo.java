package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderInfoDetails4OnTimeInfo implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private int code;
    private String desc;
    private String icon;

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon2) {
        this.icon = icon2;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public static TakeOutOrderInfoDetails4OnTimeInfo resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderInfoDetails4OnTimeInfo onTimeInfo = new TakeOutOrderInfoDetails4OnTimeInfo();
        if (obj == null) {
            return onTimeInfo;
        }
        onTimeInfo.setCode(obj.optInt("code", 0));
        onTimeInfo.setDesc(obj.optString("desc"));
        onTimeInfo.setIcon(obj.optString("icon"));
        return onTimeInfo;
    }
}
