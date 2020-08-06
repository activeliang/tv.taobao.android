package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class DeviceBo implements Serializable {
    private String appKey;
    private String brandName;
    private String model;
    private String productModel;

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey2) {
        this.appKey = appKey2;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public void setBrandName(String brandName2) {
        this.brandName = brandName2;
    }

    public String getProductModel() {
        return this.productModel;
    }

    public void setProductModel(String productModel2) {
        this.productModel = productModel2;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model2) {
        this.model = model2;
    }

    public String toString() {
        return "DeviceBo{appkey='" + this.appKey + '\'' + ", brandName='" + this.brandName + '\'' + ", productModel='" + this.productModel + '\'' + ", model='" + this.model + '\'' + '}';
    }
}
