package com.tvtaobao.voicesdk.bean;

import java.io.Serializable;

public class DetailListVO implements Serializable {
    private String id;
    private String name;
    private String pic;
    private String quantity;
    private String skuId;
    private String uri;

    public String toString() {
        return "DetailListVO{id='" + this.id + '\'' + ", name='" + this.name + '\'' + ", pic='" + this.pic + '\'' + ", uri='" + this.uri + '\'' + ", quantity='" + this.quantity + '\'' + ", skuId='" + this.skuId + '\'' + '}';
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic2) {
        this.pic = pic2;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri2) {
        this.uri = uri2;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity2) {
        this.quantity = quantity2;
    }

    public String getSkuId() {
        return this.skuId;
    }

    public void setSkuId(String skuId2) {
        this.skuId = skuId2;
    }
}
