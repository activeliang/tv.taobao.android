package com.yunos.tvtaobao.biz.request.bo;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderDeliveryData implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private String deliveryMobile;
    private String deliveryName;
    private String latitude;
    private String longitude;

    public String getDeliveryName() {
        return this.deliveryName;
    }

    public void setDeliveryName(String deliveryName2) {
        this.deliveryName = deliveryName2;
    }

    public String getDeliveryMobile() {
        return this.deliveryMobile;
    }

    public void setDeliveryMobile(String deliveryMobile2) {
        this.deliveryMobile = deliveryMobile2;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude2) {
        this.latitude = latitude2;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude2) {
        this.longitude = longitude2;
    }

    public static TakeOutOrderDeliveryData resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderDeliveryData deliveryData = new TakeOutOrderDeliveryData();
        if (obj == null) {
            return deliveryData;
        }
        if (!obj.isNull("deliveryman_info")) {
            JSONObject manInfo = obj.getJSONObject("deliveryman_info");
            deliveryData.setDeliveryName(manInfo.optString("name"));
            deliveryData.setDeliveryMobile(manInfo.optString("phone"));
        }
        if (obj.isNull("tracking_info")) {
            return deliveryData;
        }
        JSONObject address = obj.getJSONObject("tracking_info");
        deliveryData.setLatitude(address.optString(ClientTraceData.b.d));
        deliveryData.setLongitude(address.optString(ClientTraceData.b.f54c));
        return deliveryData;
    }
}
