package com.yunos.tvtaobao.biz.request.bo;

public class FindSameBean {
    private String catId;
    private String itemId;
    private String itemName;
    private String monthSellCount;
    private String nid;
    private String pic;
    private String price;
    private RebateBo rebateBo;
    private String sold;

    public RebateBo getRebateBo() {
        return this.rebateBo;
    }

    public void setRebateBo(RebateBo rebateBo2) {
        this.rebateBo = rebateBo2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic2) {
        this.pic = pic2;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName2) {
        this.itemName = itemName2;
    }

    public String getSold() {
        return this.sold;
    }

    public void setSold(String sold2) {
        this.sold = sold2;
    }

    public String getMonthSellCount() {
        return this.monthSellCount;
    }

    public void setMonthSellCount(String monthSellCount2) {
        this.monthSellCount = monthSellCount2;
    }

    public String getCatId() {
        return this.catId;
    }

    public void setCatId(String catId2) {
        this.catId = catId2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getNid() {
        return this.nid;
    }

    public void setNid(String nid2) {
        this.nid = nid2;
    }
}
