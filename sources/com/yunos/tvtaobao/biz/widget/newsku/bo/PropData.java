package com.yunos.tvtaobao.biz.widget.newsku.bo;

public class PropData {
    public boolean enable = false;
    public String imageUrl;
    public long propId;
    public String propKey;
    public boolean selected = false;
    public long valueId;

    public String toString() {
        return "{ propKey = " + this.propKey + ", propId = " + this.propId + ", valueId = " + this.valueId + ", selected = " + this.selected + ", enable = " + this.enable + ", imageUrl = " + this.imageUrl + "}";
    }
}
