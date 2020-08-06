package com.yunos.tvtaobao.biz.request.bo;

public class AddBagBo {
    private String addedTotalItemPrice;
    private long cartId;
    private int cartQuantity;
    private long itemId;
    private long skuId;

    public long getItemId() {
        return this.itemId;
    }

    public void setItemId(long itemId2) {
        this.itemId = itemId2;
    }

    public long getCartId() {
        return this.cartId;
    }

    public void setCartId(long cartId2) {
        this.cartId = cartId2;
    }

    public String getAddedTotalItemPrice() {
        return this.addedTotalItemPrice;
    }

    public void setAddedTotalItemPrice(String addedTotalItemPrice2) {
        this.addedTotalItemPrice = addedTotalItemPrice2;
    }

    public long getSkuId() {
        return this.skuId;
    }

    public void setSkuId(long skuId2) {
        this.skuId = skuId2;
    }

    public int getCartQuantity() {
        return this.cartQuantity;
    }

    public void setCartQuantity(int cartQuantity2) {
        this.cartQuantity = cartQuantity2;
    }
}
