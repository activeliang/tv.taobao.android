package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderInfoDetails4ContactInfo implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private String sellerName;
    private String sellerPhone;
    private String serverName;
    private String serverPhone;
    private String serviceProvider;
    private String storeName;
    private String storePhone;

    public String getSellerName() {
        return this.sellerName;
    }

    public void setSellerName(String sellerName2) {
        this.sellerName = sellerName2;
    }

    public String getSellerPhone() {
        return this.sellerPhone;
    }

    public void setSellerPhone(String sellerPhone2) {
        this.sellerPhone = sellerPhone2;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName2) {
        this.serverName = serverName2;
    }

    public String getServerPhone() {
        return this.serverPhone;
    }

    public void setServerPhone(String serverPhone2) {
        this.serverPhone = serverPhone2;
    }

    public String getServiceProvider() {
        return this.serviceProvider;
    }

    public void setServiceProvider(String serviceProvider2) {
        this.serviceProvider = serviceProvider2;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName2) {
        this.storeName = storeName2;
    }

    public String getStorePhone() {
        return this.storePhone;
    }

    public void setStorePhone(String storePhone2) {
        this.storePhone = storePhone2;
    }

    public static TakeOutOrderInfoDetails4ContactInfo resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderInfoDetails4ContactInfo contactInfo = new TakeOutOrderInfoDetails4ContactInfo();
        if (obj == null) {
            return contactInfo;
        }
        contactInfo.setSellerName(obj.optString("sellerName"));
        contactInfo.setSellerPhone(obj.optString("sellerPhone"));
        contactInfo.setServerName(obj.optString("serverName"));
        contactInfo.setServerPhone(obj.optString("serverPhone"));
        contactInfo.setServiceProvider(obj.optString("serviceProvider"));
        contactInfo.setStoreName(obj.optString("storeName"));
        contactInfo.setStorePhone(obj.optString("storePhone"));
        return contactInfo;
    }
}
