package com.yunos.tvtaobao.biz.request.bo;

public class PackageItem {
    private String itemId;
    private String itemPic;
    private String itemSku;
    private String price;
    private String quantity;
    private String title;

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemPic(String itemPic2) {
        this.itemPic = itemPic2;
    }

    public String getItemPic() {
        return this.itemPic;
    }

    public void setItemSku(String itemSku2) {
        this.itemSku = itemSku2;
    }

    public String getItemSku() {
        return this.itemSku;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setQuantity(String quantity2) {
        this.quantity = quantity2;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }
}
