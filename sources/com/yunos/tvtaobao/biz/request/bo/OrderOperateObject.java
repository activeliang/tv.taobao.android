package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tv.alitvasrsdk.CommonData;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderOperateObject implements Serializable {
    private static final long serialVersionUID = -4025251979318382922L;
    private String api;
    private String name;
    private ParamObject param;

    public String getApi() {
        return this.api;
    }

    public void setApi(String api2) {
        this.api = api2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public ParamObject getParam() {
        return this.param;
    }

    public void setParam(ParamObject param2) {
        this.param = param2;
    }

    public static OrderOperateObject resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        OrderOperateObject orderOperateObject = new OrderOperateObject();
        orderOperateObject.setApi(obj.getString("api"));
        orderOperateObject.setName(obj.optString("name"));
        orderOperateObject.setParam(ParamObject.resolverFromMtop(obj.getJSONObject(CommonData.PARAM)));
        return orderOperateObject;
    }
}
